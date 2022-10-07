package com.mall.pay.feign;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "pay", path = "/alipay", contextId = "0")
public interface AlipayFeign {

    /**
     * 根据支付宝流水号关闭订单
     * @param trade_no
     * @return
     */
    @RequestMapping(value = "/tradeClose")
    Result tradeClose(String trade_no);
}
