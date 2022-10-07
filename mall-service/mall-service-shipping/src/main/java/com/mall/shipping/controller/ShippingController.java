package com.mall.shipping.controller;

import com.alibaba.fastjson2.JSON;
import com.kuaidi100.sdk.pojo.HttpResult;
import com.mall.order.pojo.Order;
import com.mall.shipping.pojo.Param;
import com.mall.shipping.service.ShippingService;
import com.mall.shipping.service.impl.ShippingServiceImpl;
import com.mall.shipping.pojo.KResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/shipping")
public class ShippingController {
    ShippingService logisticsService;

    @Autowired
    public void setLogisticsService(ShippingServiceImpl logisticsService) {
        this.logisticsService = logisticsService;
    }

    /**
     * 运力查询
     * @return
     */
    @PostMapping(value = "/cOrderQuery")
    public Result cOrderQuery(@RequestParam String sendManPrintAddr, @RequestParam String recManPrintAddr) throws Exception {
        HttpResult httpResult = logisticsService.cOrderQuery(sendManPrintAddr, recManPrintAddr);

        return new Result(true, StatusCode.OK, "查询成功", JSON.parseObject(httpResult.getBody()));
    }

    /**
     * 寄件
     * @return
     */
    @PostMapping(value = "/cOrder")
    public Result cOrder(@RequestBody Order order, @RequestParam String company) throws Exception {
        HttpResult httpResult = logisticsService.cOrder(order, company);
        return new Result(true, StatusCode.OK, "请求成功", JSON.parseObject(httpResult.getBody()));
    }


    /**
     * 取消寄件
     * @param taskId
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/cancel")
    public Result cancelOrder(@RequestParam String taskId) throws Exception {
        HttpResult httpResult = logisticsService.cancelOrder(taskId);
        return new Result(true, StatusCode.OK, "请求成功", JSON.parseObject(httpResult.getBody()));
    }



    /**
     * 下单后的回调
     */
    @PostMapping("/callback")
    public KResult shippingCallback(String taskId,String param,String sign, String orderId) {
        Param param1 = JSON.parseObject(param, Param.class);
        boolean flag = logisticsService.shippingCallback(taskId, param1, sign, orderId);
        if (flag) {
            return new KResult(true, "200", "提交成功");
        }
        return new KResult(false, "500", "服务器内部错误");
    }
}
