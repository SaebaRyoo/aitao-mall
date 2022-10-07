package com.mall.shipping.service.impl;

import com.kuaidi100.sdk.cloud.CloudBase;
import com.kuaidi100.sdk.contant.CloudApiCodeConstant;
import com.kuaidi100.sdk.core.IBaseClient;
import com.kuaidi100.sdk.pojo.HttpResult;
import com.kuaidi100.sdk.request.cloud.COrderCancelReq;
import com.kuaidi100.sdk.request.cloud.COrderQueryReq;
import com.kuaidi100.sdk.request.cloud.COrderReq;
import com.kuaidi100.sdk.utils.SignUtils;
import com.mall.order.feign.OrderFeign;
import com.mall.order.pojo.Order;
import com.mall.shipping.pojo.KData;
import com.mall.shipping.pojo.Param;
import com.mall.shipping.service.ShippingService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ShippingServiceImpl implements ShippingService {
    @Value("${shipping.secret_key}")
    private String secret_key;

    @Value("${shipping.secret_secret}")
    private String secret_secret;

    @Value("${shipping.callback}")
    private String callback;

    OrderFeign orderFeign;

    @Autowired
    public void setOrderFeign(OrderFeign orderFeign) {
        this.orderFeign = orderFeign;
    }

    /**
     * 运力查询
     *
     */
    @Override
    public HttpResult cOrderQuery(String sendManPrintAddr, String recManPrintAddr) throws Exception {
        COrderQueryReq cOrderQueryReq = new COrderQueryReq();
        //寄件人地址
        cOrderQueryReq.setSendManPrintAddr("浙江省杭州市西湖区中大银座");
        //收件人地址
        cOrderQueryReq.setRecManPrintAddr("广东省深圳市深圳市南山区科技南十二路2号金蝶软件园");
        cOrderQueryReq.setSecret_key(secret_key);
        cOrderQueryReq.setSecret_code(CloudApiCodeConstant.ORDER_QUERY_EXPRESS);
        cOrderQueryReq.setSecret_sign(SignUtils.cloudSign(secret_key, secret_secret));

        IBaseClient cloudBase = new CloudBase();
        return cloudBase.execute(cOrderQueryReq);
    }

    /**
     * 寄件
     *
     */
    @Override
    public HttpResult cOrder(Order order, String company) throws Exception {
        COrderReq cOrderReq = new COrderReq();
        // 快递公司的编码，一律用小写字母，见《快递公司编码》
        cOrderReq.setCom(company);
        //寄件人姓名
        cOrderReq.setSendManName("爱桃");
        //寄件人的手机号，手机号和电话号二者其一必填
        cOrderReq.setSendManMobile("15966666666");
        //寄件人所在完整地址，如广东深圳市深圳市南山区科技南十二路2号金蝶软件园
        cOrderReq.setSendManPrintAddr("浙江杭州西湖区中大银座");
        //收件人姓名
        cOrderReq.setRecManName(order.getReceiverContact());
        //收件人的手机号，手机号和电话号二者其一必填
        cOrderReq.setRecManMobile(order.getReceiverMobile());
        //收件人所在的完整地址
        cOrderReq.setRecManPrintAddr(order.getReceiverAddress());
        //callBackUrl订单信息回调 orderId是当前订单的id，不是快递平台id
        cOrderReq.setCallBackUrl(callback+"?orderId="+order.getId());
        //物品名称,例：文件
        cOrderReq.setCargo("生活用品");
        //支付方式，SHIPPER: 寄方付（默认），CONSIGNEE: 到付
        cOrderReq.setPayment("SHIPPER");
        //备注
        cOrderReq.setRemark("贵重物品，请轻拿轻放");
        //物品总重量KG，不需带单位，例：1.5
        cOrderReq.setWeight("1");
        //签名用随机字符串
        cOrderReq.setSalt("123456");
        cOrderReq.setSecret_key(secret_key);
        cOrderReq.setSecret_code(CloudApiCodeConstant.ORDER);
        cOrderReq.setSecret_sign(SignUtils.cloudSign(secret_key, secret_secret));

        IBaseClient cloudBase = new CloudBase();
        return cloudBase.execute(cOrderReq);
    }

    /**
     * 取消寄件
     *
     * @param taskId
     * @return
     * @throws Exception
     */
    @Override
    public HttpResult cancelOrder(String taskId) throws Exception {
        COrderCancelReq cOrderCancelReq = new COrderCancelReq();
        cOrderCancelReq.setTaskId("4ACF6D2B08A38E8AA01E1B9272E62455");
        cOrderCancelReq.setOrderId("25483336");
        cOrderCancelReq.setCancelMsg("测试单");
        cOrderCancelReq.setSecret_key(secret_key);
        cOrderCancelReq.setSecret_code(CloudApiCodeConstant.ORDER_CANCEL);
        cOrderCancelReq.setSecret_sign(SignUtils.cloudSign(secret_key, secret_secret));

        IBaseClient cloudBase = new CloudBase();
        return cloudBase.execute(cOrderCancelReq);
    }

    /**
     * 发货后的回调处理
     */
    @Override
    public boolean shippingCallback(String taskId, Param param, String sign, String orderId) {
        KData data = param.getData();
        //System.out.println("taskId----------->" + taskId);
        //System.out.println("param----------->" + param.getKuaidicom() + param.getKuaidinum() + param.getMessage() + param.getStatus());
        //System.out.println("data----------->" + data.getOrderId() + data.getStatus() + data.getCourierName() + data.getCourierMobile() + data.getWeight() + data.getFreight());
        //System.out.println("sign----------->" + sign);
        //在这里处理订单中物流相关信息
        Order order = new Order();
        //order.setShippingTaskId(taskId);
        order.setShippingName(param.getKuaidicom());
        order.setShippingCode(param.getKuaidinum());
        order.setShippingStatus(data.getStatus());
        Result result = orderFeign.update(order, orderId);
        return result.getCode().equals(StatusCode.OK);
    }
}
