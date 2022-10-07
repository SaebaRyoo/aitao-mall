package com.mall.seckill.listener;

import com.alibaba.fastjson.JSON;
import com.mall.pay.feign.AlipayFeign;
import com.mall.seckill.service.SeckillOrderService;
import entity.RabbitMQConstants;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 订单支付支付超时延时队列
 */
@Component
@RabbitListener(queues = RabbitMQConstants.QUEUE_SEC_KILL_ORDER_CHECK)
public class SeckillOrderCheckMessageListener {

    SeckillOrderService seckillOrderService;

    AlipayFeign alipayFeign;

    @Autowired
    public void setSeckillOrderService(SeckillOrderService seckillOrderService) {
        this.seckillOrderService = seckillOrderService;
    }

    @Autowired
    public void setAlipayFeign(AlipayFeign alipayFeign) {
        this.alipayFeign = alipayFeign;
    }



    /***
     * 监听消息 延时队列测试
     * @param data
     */
    //@RabbitHandler
    //public void msg(Object data) throws UnsupportedEncodingException {
    //    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //    System.out.println("当前时间:"+dateFormat.format(new Date()));
    //    System.out.println("收到信息:"+data);
    //
    //    //发送端发送的是Object数据，在这里转一下
    //    Message data1 = (Message) data;
    //    String str = new String(data1.getBody(), "UTF-8");
    //    Map result = JSON.parseObject(str, Map.class);
    //    String name = (String) result.get("name");
    //    System.out.println(name);
    //
    //}

    /**
     * 监听消费消息
     * @param data
     */
    @RabbitHandler
    public void consumeMessage(Object data) throws UnsupportedEncodingException {
        //发送端发送的是Object数据，在这里转一下
        Message data1 = (Message) data;
        String str = new String(data1.getBody(), "UTF-8");
        Map<String, String> result = JSON.parseObject(str, Map.class);
        //支付宝交易号
        String out_trade_no = result.get("out_trade_no");
        String username = result.get("username");


        //用户在下单后的30分后会进入该任务，判断用户是否付款
        //支付失败,
        //1.取消支付宝的支付订单
        alipayFeign.tradeClose(out_trade_no);
        //2.关闭订单，库存回滚
        seckillOrderService.closeOrder(username);
    }
}
