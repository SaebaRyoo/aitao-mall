package com.mall.order.dao;
import com.mall.order.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository(value = "orderItemMapper")
public interface OrderItemMapper extends Mapper<OrderItem> {

    @Select("select * from tb_order_item where order_id=#{orderId}")
    List<OrderItem> findByOrderId(@Param("orderId")String orderId);
}
