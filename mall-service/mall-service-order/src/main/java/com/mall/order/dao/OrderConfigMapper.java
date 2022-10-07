package com.mall.order.dao;
import com.mall.order.pojo.OrderConfig;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository(value = "orderConfigMapper")
public interface OrderConfigMapper extends Mapper<OrderConfig> {
}
