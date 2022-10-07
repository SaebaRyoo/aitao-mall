package com.mall.order.dao;
import com.mall.order.pojo.Order;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository(value = "orderMapper")
public interface OrderMapper extends Mapper<Order> {
}
