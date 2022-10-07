package com.mall.order.listener;

import com.alibaba.fastjson.JSON;
import com.mall.order.service.OrderService;
import com.mall.pay.feign.AlipayFeign;
import entity.RabbitMQConstants;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 订单支付支付超时延时队列
 */
@Component
@RabbitListener(queues = RabbitMQConstants.QUEUE_ORDER_CHECK)
public class OrderTimerMessageListener {
    AlipayFeign alipayFeign;

    OrderService orderService;

    @Autowired
    public void setAlipayFeign(AlipayFeign alipayFeign) {
        this.alipayFeign = alipayFeign;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

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

        //用户在下单后的30分后会进入该任务，判断用户是否付款
        //支付失败,
        //1.取消支付宝的支付订单
        alipayFeign.tradeClose(out_trade_no);
        //2.关闭订单，
        orderService.closeOrder(out_trade_no);
    }
}
