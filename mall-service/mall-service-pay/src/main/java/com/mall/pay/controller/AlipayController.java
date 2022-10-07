package com.mall.pay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.mall.order.feign.OrderFeign;
import com.mall.order.feign.OrderItemFeign;
import com.mall.order.pojo.Order;
import com.mall.order.pojo.OrderItem;
import com.mall.pay.config.AlipayConfig;
import entity.RabbitMQConstants;
import entity.Result;
import entity.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

@RestController
@RequestMapping("/alipay")
public class AlipayController {
    final static Logger log = LoggerFactory.getLogger(AlipayController.class);

    OrderFeign orderFeign;

    OrderItemFeign orderItemFeign;

    RabbitTemplate rabbitTemplate;

    @Autowired
    public void setOrderFeign(OrderFeign orderFeign) {
        this.orderFeign = orderFeign;
    }

    @Autowired
    public void setOrderItemFeign(OrderItemFeign orderItemFeign) {
        this.orderItemFeign = orderItemFeign;
    }

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     *
     * 前往支付宝第三方网关进行支付
     * 支付宝支付接入文档: https://opendocs.alipay.com/open/028r8t?scene=22
     * @param orderId
     * @param queueName  ORDER_PAY: 正常订单支付  SEC_KILL_ORDER_PAY: 秒杀支付
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/goAlipay", produces = "text/html; charset=UTF-8")
    @ResponseBody
    public String goAlipay(String orderId, String queueName) throws Exception {
        Result<Order> orderResult = orderFeign.findById(orderId);
        Order order = orderResult.getData();
        Result<List<OrderItem>> orderItemsResult = orderItemFeign.findItemsByOrderId(orderId);
        List<OrderItem> orderItems = orderItemsResult.getData();
        OrderItem orderItem = orderItems.get(0);

        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, AlipayConfig.format, AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        String out_trade_no = orderId;
        Integer total_amount = order.getTotalMoney();
        String subject = orderItems.size() > 1 ? orderItem.getName() + "等商品" : orderItem.getName();
        String body = "用户订购商品个数：" + order.getTotalNum();

        //过期时间
        String timeout_express = "30m";
        JSONObject bizContent = new JSONObject();
        //商户订单号，商户网站订单系统中唯一订单号，必填
        bizContent.put("out_trade_no", out_trade_no);
        //付款金额，必填
        bizContent.put("total_amount", total_amount);
        //订单名称，必填.当有多个商品时，总订单只显示第一个商品名称
        bizContent.put("subject", subject);
        // 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
        bizContent.put("timeout_express", timeout_express);
        //销售产品码 必选，商家和支付宝签约的产品码。电脑场景支付为: FAST_INSTANT_TRADE_PAY, 手机网站支付: QUICK_WAP_WAY
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        //商品描述，可空
        bizContent.put("body", body);

        JSONObject passBackParams = new JSONObject();
        passBackParams.put("queueName", queueName);//队列名称
        passBackParams.put("username", order.getUsername());//用户名称
        bizContent.put("passback_params", URLEncoder.encode(passBackParams.toString(), "UTF-8"));//公共回传参数

        // 商品明细信息
        JSONArray goodsDetail = new JSONArray();
        for (OrderItem orderItem1 : orderItems) {
            JSONObject goods = new JSONObject();

            goods.put("goods_id", orderItem1.getId());//商品的编号
            goods.put("goods_name", orderItem1.getName());//商品名称
            goods.put("quantity", orderItem1.getNum());//商品数量
            goods.put("price", orderItem1.getPrice());//商品价格
            //商品类目数，从商品类目根节点到叶子节点的类目id组成，类目id值使用|分割 11|22|33
            goods.put("categories_tree", orderItem1.getCategoryId1() + "|" + orderItem1.getCategoryId2() + "|" + orderItem1.getCategoryId3());
            goodsDetail.add(goods);
        }
        bizContent.put("goods_detail", goodsDetail);
        alipayRequest.setBizContent(bizContent.toString());

        //请求alipay，生成支付页面
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        return result;
    }

    /**
     * @Title: AlipayController.java
     * @Package com.sihai.controller
     * @Description: 支付宝同步通知页面  用户确认支付后，支付宝get请求该接口，返回同步返回参数
     * 用途，支付订单后同步跳转到前端页面
     */
    @RequestMapping(value = "/alipayReturnNotice")
    public void alipayReturnNotice(HttpServletRequest request, HttpServletRequest response) throws Exception {

        log.info("支付成功, 进入同步通知接口...");
    }

    /**
     * 支付宝异步 支付是否成功以异步通知为准，
     * 用户支付后，支付宝post请求该接口，携带支付状态信息
     * 将支付状态信息发送给RabbitMQ
     * 订单系统监听到RabbitMQ中的消息获取支付状态，并根据支付状态修改订单状态
     */
    @RequestMapping(value = "/alipayNotifyNotice")
    public void alipayNotifyNotice(HttpServletRequest request) throws Exception {

        log.info("支付成功, 进入异步通知接口...");

        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "UTF-8");
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

        /*
         * passbackParams    queue       ORDER_PAY(正常订单支付)  SEC_KILL_ORDER_PAY(秒杀订单支付)
         *                   username    guest1
         * */
        String passbackParams = new String(request.getParameter("passback_params").getBytes("ISO-8859-1"), "UTF-8");
        String decode = URLDecoder.decode(passbackParams, "UTF-8");
        JSONObject extraParam = JSONObject.parseObject(decode);
        String targetPipe = (String) extraParam.get("queueName"); // 选择队列
        String username = (String)  extraParam.get("username"); // 当前用户

        JSONObject data = new JSONObject();
        if (signVerified) {
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
            //交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
            //付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");
            data.put("signVerified", "1");
            data.put("out_trade_no", out_trade_no);
            data.put("trade_no", trade_no);
            data.put("trade_status", trade_status);
            data.put("total_amount", total_amount);
            data.put("username", username);

        } else {
            data.put("signVerified", "0");
            data.put("out_trade_no", "");
            data.put("trade_no", "");
            data.put("trade_status", "");
            data.put("total_amount", "");
            data.put("username", "");
        }
        //队列名称需要改成动态的，秒杀服务和订单服务采用不同的队列
        //将消息发送到mq队列，其他服务需要就自己取

        /****正常订单支付队列****/
        if (targetPipe.equals("ORDER_PAY")) {
            rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_ORDER_PAY, RabbitMQConstants.QUEUE_ORDER_PAY, JSON.toJSONString(data));
        }

        /****秒杀订单支付队列****/
        if (targetPipe.equals("SEC_KILL_ORDER_PAY")) {
            rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_SEC_KILL_ORDER_PAY, RabbitMQConstants.QUEUE_SEC_KILL_ORDER_PAY, JSON.toJSONString(data));
        }
    }


    /**
     * 支付状态查询
     *
     * @param orderId
     * @return
     * @throws AlipayApiException
     */
    @RequestMapping(value = "/tradeQuery")
    public Result tradeQuery(String orderId) throws AlipayApiException {

        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, AlipayConfig.format, AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderId);
        request.setBizContent(bizContent.toString());
        AlipayTradeQueryResponse response = alipayClient.execute(request);

        Map<String, String> data = new HashMap<>();

        if (response.isSuccess()) {
            //支付宝交易号
            String tradeNo = response.getTradeNo();
            //支付状态
            //WAIT_BUYER_PAY    交易创建，等待买家付款
            //TRADE_CLOSED      未付款交易超时关闭，或支付完成后全额退款
            //TRADE_SUCCESS     交易支付成功
            //TRADE_FINISHED    交易结束，不可退款
            String tradeStatus = response.getTradeStatus();
            //支付金额
            String totalAmount = response.getTotalAmount();
            //支付宝账号
            String buyerLogonId = response.getBuyerLogonId();
            data.put("tradeNo", tradeNo);
            data.put("tradeStatus", tradeStatus);
            data.put("totalAmount", totalAmount);
            data.put("buyerLogonId", buyerLogonId);
        } else {
            System.out.println("调用失败");
        }

        return new Result(true, StatusCode.OK, "支付成功", data);
    }


    /**
     * 关闭支付订单
     *
     * @param trade_no 支付宝交易流水号
     * @return
     */
    @RequestMapping(value = "/tradeClose")
    public Result tradeClose(String trade_no) throws AlipayApiException {

        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, AlipayConfig.format, AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("trade_no", trade_no);
        request.setBizContent(bizContent.toString());
        AlipayTradeCloseResponse response = alipayClient.execute(request);

        String msg = "";
        if (response.isSuccess()) {
            msg = "订单关闭成功";
        } else {
            msg = "订单关闭失败";
        }
        return new Result(true, StatusCode.OK, msg);
    }
}
