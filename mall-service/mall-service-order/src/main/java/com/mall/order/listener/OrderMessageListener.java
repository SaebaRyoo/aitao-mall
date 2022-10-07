package com.mall.order.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mall.order.feign.OrderFeign;
import com.mall.order.feign.OrderItemFeign;
import com.mall.order.pojo.Order;
import com.mall.order.pojo.OrderItem;
import com.mall.order.service.OrderItemService;
import com.mall.order.service.OrderService;
import entity.RabbitMQConstants;
import entity.Result;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@RabbitListener(queues = RabbitMQConstants.QUEUE_ORDER_PAY)
public class OrderMessageListener {
    private OrderService orderService;

    @Autowired
    public void setOrderFeign(OrderService orderService) {
        this.orderService = orderService;
    }

    /***
     * 接收消息
     */
    @RabbitHandler
    public void consumeMessage(String msg){
        //将数据转成Map
        Map<String,String> result = JSON.parseObject(msg, Map.class);
        String signVerified = result.get("signVerified");
        //商户订单号
        String out_trade_no = result.get("out_trade_no");
        //支付宝交易号
        String trade_no = result.get("trade_no");
        //交易状态
        String trade_status = result.get("trade_status");
        //付款金额
        String total_amount = result.get("total_amount");

		/* 实际验证过程建议商户务必添加以下校验：
		1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
		2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
		3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
		4、验证app_id是否为该商户本身。
		*/
        if(!signVerified.equals("1")) {
            System.out.println("验证失败");
            return;
        }

        if (trade_status.equals("TRADE_SUCCESS")){
            //判断该笔订单是否在商户网站中已经做过处理
            //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
            //如果有做过处理，不执行商户的业务程序

            //注意：
            //付款完成后，支付宝系统发送该交易状态通知

            // 修改订单状态，改为 支付成功，已付款; 同时新增支付流水
            Order order1 = new Order();
            //设置id
            order1.setId(out_trade_no);
            //支付状态
            order1.setPayStatus("1");
            //支付时间
            order1.setPayTime(new Date());
            //支付宝交易流水号
            order1.setTransactionId(trade_no);
            //修改支付状态
            orderService.update(order1);

            System.out.println("out_trade_no--------"+out_trade_no);
            System.out.println("验证成功");
        } else if(trade_status.equals("TRADE_FINISHED")){
            //判断该笔订单是否在商户网站中已经做过处理
            //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
            //如果有做过处理，不执行商户的业务程序

            //注意： 尚自习的订单没有退款功能, 这个条件判断是进不来的, 所以此处不必写代码
            //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
        }
    }
}
