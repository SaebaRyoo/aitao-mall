package com.mall.shipping.service;

import com.kuaidi100.sdk.pojo.HttpResult;
import com.mall.order.pojo.Order;
import com.mall.shipping.pojo.Param;

public interface ShippingService {


    /**
     * 运力查询
     * @param sendManPrintAddr
     * @param recManPrintAddr
     * @return
     */
    HttpResult cOrderQuery(String sendManPrintAddr, String recManPrintAddr) throws Exception;


    /**
     * 寄件
     * @param order
     * @return
     * @throws Exception
     */
    HttpResult cOrder(Order order, String company) throws Exception;

    /**
     * 取消寄件
     * @param taskId
     * @return
     */
    HttpResult cancelOrder(String taskId) throws Exception;

    /**
     * 发货后的回调
     */
    boolean shippingCallback(String taskId, Param param, String sign, String orderId);
}
