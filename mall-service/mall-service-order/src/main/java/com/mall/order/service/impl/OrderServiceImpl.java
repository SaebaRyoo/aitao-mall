package com.mall.order.service.impl;

import com.mall.goods.feign.SkuFeign;
import com.mall.goods.pojo.Sku;
import com.mall.order.dao.OrderItemMapper;
import com.mall.order.dao.OrderMapper;
import com.mall.order.pojo.Order;
import com.mall.order.pojo.OrderItem;
import com.mall.order.service.CartService;
import com.mall.order.service.OrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.pay.feign.AlipayFeign;
import com.mall.user.feign.UserFeign;
import entity.IdWorker;
import entity.RabbitMQConstants;
import entity.Result;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private OrderMapper orderMapper;

    private OrderItemMapper orderItemMapper;

    private CartService cartService;

    private IdWorker idWorker;

    private RedisTemplate redisTemplate;

    private SkuFeign skuFeign;

    private UserFeign userFeign;

    private AlipayFeign alipayFeign;

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public void setUserFeign(UserFeign userFeign) {
        this.userFeign = userFeign;
    }

    @Autowired
    public void setSkuFeign(SkuFeign skuFeign) {
        this.skuFeign = skuFeign;
    }

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Autowired
    public void setOrderItemMapper(OrderItemMapper orderItemMapper) {
        this.orderItemMapper = orderItemMapper;
    }

    @Autowired
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @Autowired
    public void setIdWorker(IdWorker idWorker) {
        this.idWorker = idWorker;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setAlipayFeign(AlipayFeign alipayFeign) {
        this.alipayFeign = alipayFeign;
    }

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Order??????+????????????
     *
     * @param order ????????????
     * @param page  ??????
     * @param size  ?????????
     * @return ????????????
     */
    @Override
    public PageInfo<Order> findPage(Order order, int page, int size) {
        //??????
        PageHelper.startPage(page, size);
        //??????????????????
        Example example = createExample(order);
        //????????????
        return new PageInfo<Order>(orderMapper.selectByExample(example));
    }

    /**
     * Order????????????
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Order> findPage(int page, int size) {
        //????????????
        PageHelper.startPage(page, size);
        //????????????
        return new PageInfo<Order>(orderMapper.selectAll());
    }

    /**
     * Order????????????
     *
     * @param order
     * @return
     */
    @Override
    public List<Order> findList(Order order) {
        //??????????????????
        Example example = createExample(order);
        //?????????????????????????????????
        return orderMapper.selectByExample(example);
    }


    /**
     * Order??????????????????
     *
     * @param order
     * @return
     */
    public Example createExample(Order order) {
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        if (order != null) {
            // ??????id
            if (!StringUtils.isEmpty(order.getId())) {
                criteria.andEqualTo("id", order.getId());
            }
            // ????????????
            if (!StringUtils.isEmpty(order.getTotalNum())) {
                criteria.andEqualTo("totalNum", order.getTotalNum());
            }
            // ????????????
            if (!StringUtils.isEmpty(order.getTotalMoney())) {
                criteria.andEqualTo("totalMoney", order.getTotalMoney());
            }
            // ????????????
            if (!StringUtils.isEmpty(order.getPreMoney())) {
                criteria.andEqualTo("preMoney", order.getPreMoney());
            }
            // ??????
            if (!StringUtils.isEmpty(order.getPostFee())) {
                criteria.andEqualTo("postFee", order.getPostFee());
            }
            // ????????????
            if (!StringUtils.isEmpty(order.getPayMoney())) {
                criteria.andEqualTo("payMoney", order.getPayMoney());
            }
            // ???????????????1??????????????????0 ????????????
            if (!StringUtils.isEmpty(order.getPayType())) {
                criteria.andEqualTo("payType", order.getPayType());
            }
            // ??????????????????
            if (!StringUtils.isEmpty(order.getCreateTime())) {
                criteria.andEqualTo("createTime", order.getCreateTime());
            }
            // ??????????????????
            if (!StringUtils.isEmpty(order.getUpdateTime())) {
                criteria.andEqualTo("updateTime", order.getUpdateTime());
            }
            // ????????????
            if (!StringUtils.isEmpty(order.getPayTime())) {
                criteria.andEqualTo("payTime", order.getPayTime());
            }
            // ????????????
            if (!StringUtils.isEmpty(order.getConsignTime())) {
                criteria.andEqualTo("consignTime", order.getConsignTime());
            }
            // ??????????????????
            if (!StringUtils.isEmpty(order.getEndTime())) {
                criteria.andEqualTo("endTime", order.getEndTime());
            }
            // ??????????????????
            if (!StringUtils.isEmpty(order.getCloseTime())) {
                criteria.andEqualTo("closeTime", order.getCloseTime());
            }
            // ????????????
            if (!StringUtils.isEmpty(order.getShippingName())) {
                criteria.andEqualTo("shippingName", order.getShippingName());
            }
            // ????????????
            if (!StringUtils.isEmpty(order.getShippingCode())) {
                criteria.andEqualTo("shippingCode", order.getShippingCode());
            }
            // ????????????
            if (!StringUtils.isEmpty(order.getUsername())) {
                criteria.andLike("username", "%" + order.getUsername() + "%");
            }
            // ????????????
            if (!StringUtils.isEmpty(order.getBuyerMessage())) {
                criteria.andEqualTo("buyerMessage", order.getBuyerMessage());
            }
            // ????????????
            if (!StringUtils.isEmpty(order.getBuyerRate())) {
                criteria.andEqualTo("buyerRate", order.getBuyerRate());
            }
            // ?????????
            if (!StringUtils.isEmpty(order.getReceiverContact())) {
                criteria.andEqualTo("receiverContact", order.getReceiverContact());
            }
            // ???????????????
            if (!StringUtils.isEmpty(order.getReceiverMobile())) {
                criteria.andEqualTo("receiverMobile", order.getReceiverMobile());
            }
            // ???????????????
            if (!StringUtils.isEmpty(order.getReceiverAddress())) {
                criteria.andEqualTo("receiverAddress", order.getReceiverAddress());
            }
            // ???????????????1:web???2???app???3?????????????????????4??????????????????  5 H5????????????
            if (!StringUtils.isEmpty(order.getSourceType())) {
                criteria.andEqualTo("sourceType", order.getSourceType());
            }
            // ???????????????
            if (!StringUtils.isEmpty(order.getTransactionId())) {
                criteria.andEqualTo("transactionId", order.getTransactionId());
            }
            // ????????????,0:?????????,1:????????????2????????????
            if (!StringUtils.isEmpty(order.getOrderStatus())) {
                criteria.andEqualTo("orderStatus", order.getOrderStatus());
            }
            // ????????????,0:????????????1???????????????2???????????????
            if (!StringUtils.isEmpty(order.getPayStatus())) {
                criteria.andEqualTo("payStatus", order.getPayStatus());
            }
            // ????????????,0:????????????1???????????????2????????????
            if (!StringUtils.isEmpty(order.getShippingStatus())) {
                criteria.andEqualTo("shippingStatus", order.getShippingStatus());
            }
            // ????????????
            if (!StringUtils.isEmpty(order.getIsDelete())) {
                criteria.andEqualTo("isDelete", order.getIsDelete());
            }
        }
        return example;
    }

    /**
     * ??????
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        orderMapper.deleteByPrimaryKey(id);
    }

    /**
     * ??????Order
     *
     * @param order
     */
    @Override
    public void update(Order order) {
        orderMapper.updateByPrimaryKeySelective(order);
    }

    /**
     * ???????????????
     *
     * @param order
     */
    @Override
    public int add(Order order) {
        String username = order.getUsername();
        //?????????????????????????????????
        List<OrderItem> orderItems = cartService.list(username);

        //????????????
        int totalMoney = 0;
        int totalPayMoney = 0;
        int num = 0;
        for (OrderItem orderItem : orderItems) {
            //?????????
            totalMoney += orderItem.getMoney();

            //??????????????????
            totalPayMoney += orderItem.getPayMoney();
            //?????????
            num += orderItem.getNum();
        }
        order.setTotalNum(num);
        order.setTotalMoney(totalMoney);
        order.setPayMoney(totalPayMoney);
        order.setPreMoney(totalMoney - totalPayMoney);

        //??????????????????
        order.setCreateTime(new Date());
        order.setUpdateTime(order.getCreateTime());
        order.setBuyerRate("0");        //0:????????????1????????????
        order.setSourceType("1");       //?????????1???WEB
        order.setOrderStatus("0");      //0:?????????,1:????????????2????????????
        order.setPayStatus("0");        //0:????????????1???????????????2???????????????
        order.setShippingStatus("0");    //0:????????????1???????????????2????????????
        order.setId("NO." + idWorker.nextId());
        int count = orderMapper.insertSelective(order);

        //??????????????????
        for (OrderItem orderItem : orderItems) {
            orderItem.setId("NO." + idWorker.nextId());
            orderItem.setIsReturn("0");
            orderItem.setOrderId(order.getId());
            orderItemMapper.insertSelective(orderItem);
        }

        //????????????
        skuFeign.decrCount(username);
        //??????????????????
        //????????????????????????10??????
        userFeign.addPoints(username, 10);

        //???????????????????????????
        //if(order.getPayType().equalsIgnoreCase("1")){
        //    //????????????????????????Redis namespace  key  value
        //    redisTemplate.boundHashOps("Order").put(order.getId(),order);
        //}
        //??????Redis?????????????????????
        redisTemplate.delete("Cart_" + username);
        //??????????????????
        sendDelayMessage(order.getId());
        return count;
    }

    /**
     * ?????????????????????RabbitMQ???
     *
     * @param orderId
     */
    public void sendDelayMessage(String orderId) {

        Map<String, String> data = new HashMap<>();
        data.put("out_trade_no", orderId);

        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setRoutingKey(RabbitMQConstants.QUEUE_ORDER_DELAY);
        //?????????????????????????????????????????????
        rabbitTemplate.convertAndSend(data, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //??????????????????
                message.getMessageProperties().setExpiration(RabbitMQConstants.DELAY_TIME); //35min
                return message;
            }
        });
    }

    /**
     * ????????????
     *
     * @param orderId
     * @return
     */
    @Override
    public boolean closeOrder(String orderId) {

        //1.??????????????????
        List<OrderItem> orderItems = orderItemMapper.findByOrderId(orderId);
        for (OrderItem orderItem : orderItems) {
            // 2.????????????id ??? ??????????????????
            Long skuId = orderItem.getSkuId();
            Integer num = orderItem.getNum();

            //3. ??????????????????
            Result<Sku> result = skuFeign.findById(skuId);
            Sku goods = result.getData();
            goods.setNum(goods.getNum() + num);

            //????????????
            skuFeign.update(goods, orderId);
        }

        //4.??????????????????
        alipayFeign.tradeClose(orderId);

        return true;
    }

    /**
     * ??????ID??????Order
     *
     * @param id
     * @return
     */
    @Override
    public Order findById(String id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    /**
     * ??????Order????????????
     *
     * @return
     */
    @Override
    public List<Order> findAll() {
        return orderMapper.selectAll();
    }
}
