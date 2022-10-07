package com.mall.seckill.task;

import com.mall.seckill.dao.SeckillGoodsMapper;
import com.mall.seckill.pojo.SeckillGoods;
import com.mall.seckill.pojo.SeckillOrder;
import com.mall.seckill.pojo.SeckillStatus;
import entity.IdWorker;
import entity.RabbitMQConstants;
import entity.SystemConstants;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class MultiThreadingCreateOrder {

    private RedisTemplate redisTemplate;

    private IdWorker idWorker;

    private SeckillGoodsMapper seckillGoodsMapper;

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setIdWorker(IdWorker idWorker) {
        this.idWorker = idWorker;
    }

    @Autowired
    public void setSeckillGoodsMapper(SeckillGoodsMapper seckillGoodsMapper) {
        this.seckillGoodsMapper = seckillGoodsMapper;
    }

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    //创建订单 异步处理 加入一个注解
    @Async
    public void createOrder(){


        //从队列中获取抢单信息
        SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundListOps(SystemConstants.SEC_KILL_USER_QUEUE_KEY).rightPop();

        if(seckillStatus!=null) {
            String time = seckillStatus.getTime();
            Long id = seckillStatus.getGoodsId();//秒杀商品的ID
            String username = seckillStatus.getUsername();

            //判断 从hashmap中取出商品库存 ,如果能获取到,说明该商品有库存,如果获取不到,说明 没库存 卖完了 return.
            Integer stockCount = (Integer) redisTemplate.boundHashOps(SystemConstants.SECK_KILL_GOODS_COUNT_KEY).get(id);
            if(stockCount.intValue() <= 0){
                //卖完了
                clearQueue(username, id);
                return;
            }
            //1.根据商品的ID 获取秒杀商品的数据
            SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).get(id);

            //3.如果有库存
            //4.创建一个预订单
            SeckillOrder seckillOrder = new SeckillOrder();
            seckillOrder.setId(idWorker.nextId());//订单的ID
            seckillOrder.setSeckillId(id);//秒杀商品ID
            seckillOrder.setMoney(seckillGoods.getCostPrice());//金额
            seckillOrder.setUserId(username);//登录的用户名
            seckillOrder.setCreateTime(new Date());//创建时间
            seckillOrder.setStatus("0");//未支付
            redisTemplate.boundHashOps(SystemConstants.SEC_KILL_ORDER_KEY).put(username, seckillOrder);

            //5.减库存
            //这种方法在多线程同时下单时，会出现多人下单，但是执行到这时，使用的还是历史库存，会出现库存展示错误
            //seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
            //使用redis自增(单线程不会出现以上问题)
            Long increment = redisTemplate.boundHashOps(SystemConstants.SECK_KILL_GOODS_COUNT_KEY).increment(id, -1);
            seckillGoods.setStockCount(increment.intValue());


            //6.判断库存是是否为0  如果 是,更新到数据库中,删除掉redis中的秒杀商品
            if (seckillGoods.getStockCount() <= 0) {
                seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);//数据库的库存更新为0
                redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).delete(id);
            } else {
                //设置回redis中
                redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).put(id, seckillGoods);

            }
            //模拟并发
            try {
                System.out.println("开始模拟下单操作=====start====" + new Date() + Thread.currentThread().getName());
                Thread.sleep(10000);
                System.out.println("开始模拟下单操作=====end====" + new Date() + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //7.创建订单成功了 修改用户的抢单的信息
            seckillStatus.setOrderId(seckillOrder.getId());
            seckillStatus.setStatus(2);//待付款
            seckillStatus.setMoney(Float.valueOf(seckillOrder.getMoney()));
            //重新设置回redis中
            redisTemplate.boundHashOps(SystemConstants.SEC_KILL_USER_STATUS_KEY).put(username,seckillStatus);
            //发送延时消息
            sendDelayMessage(username, seckillOrder.getId());
        }

    }

    /**
     *
     * 发送延时消息到RabbitMQ中
     * @param username
     * @param orderId
     */
    public void sendDelayMessage(String username, Long orderId ){

        Map<String,String> data = new HashMap<>();
        data.put("username", username);
        data.put("out_trade_no", orderId.toString());

        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setRoutingKey(RabbitMQConstants.QUEUE_SEC_KILL_ORDER_DELAY);
        //将需要消费的信息发送到死信队列
        rabbitTemplate.convertAndSend(data, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //设置过期时间
                message.getMessageProperties().setExpiration("5000");//5 S
                return message;
            }
        });
    }
    /***
     * 清理用户排队信息
     * @param username
     */
    public void clearQueue(String username, Long id) {
        //清除掉  库存的key(超卖控制)
        redisTemplate.boundHashOps(SystemConstants.SECK_KILL_GOODS_COUNT_KEY).delete(id);
        //清除掉  排队标识(存储用户的抢单信息)
        redisTemplate.boundHashOps(SystemConstants.SEC_KILL_QUEUE_REPEAT_KEY).delete(username);
        redisTemplate.boundHashOps(SystemConstants.SEC_KILL_USER_STATUS_KEY).delete(username);
    }

}
