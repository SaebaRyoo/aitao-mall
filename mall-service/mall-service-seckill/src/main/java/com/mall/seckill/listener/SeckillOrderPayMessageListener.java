package com.mall.seckill.listener;

import com.alibaba.fastjson.JSON;
import com.mall.seckill.service.SeckillOrderService;
import entity.RabbitMQConstants;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 秒杀支付消息队列
 */
@Component
@RabbitListener(queues = RabbitMQConstants.QUEUE_SEC_KILL_ORDER_PAY)
public class SeckillOrderPayMessageListener {

    SeckillOrderService seckillOrderService;

    @Autowired
    public void setSeckillOrderService(SeckillOrderService seckillOrderService) {
        this.seckillOrderService = seckillOrderService;
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
        //当前用户
        String username = result.get("username");
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

        if(trade_status.equals("TRADE_SUCCESS")){
            System.out.println("out_trade_no--------"+out_trade_no);
            System.out.println("验证成功");
            seckillOrderService.updatePayStatus(out_trade_no, trade_no, username);
        }else if (trade_status.equals("TRADE_FINISHED")){
            System.out.println("支付失败");
        }
    }
}
