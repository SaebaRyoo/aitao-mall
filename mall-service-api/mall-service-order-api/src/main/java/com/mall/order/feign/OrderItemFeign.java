package com.mall.order.feign;

import com.mall.order.pojo.OrderItem;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "order", path = "/orderItem", contextId = "1")
public interface OrderItemFeign {


    /***
     * 根据ID查询OrderItem数据
     * @param orderId
     * @return
     */
    @GetMapping("/orderDetails/{orderId}")
    Result<List<OrderItem>> findItemsByOrderId(@PathVariable String orderId);
}
