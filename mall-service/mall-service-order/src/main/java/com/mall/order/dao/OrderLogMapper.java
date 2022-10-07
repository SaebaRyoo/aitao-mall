package com.mall.order.dao;
import com.mall.order.pojo.OrderLog;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository(value = "orderLogMapper")
public interface OrderLogMapper extends Mapper<OrderLog> {
}
