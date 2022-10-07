package com.mall.order.dao;
import com.mall.order.pojo.ReturnOrderItem;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository(value = "returnOrderItemMapper")
public interface ReturnOrderItemMapper extends Mapper<ReturnOrderItem> {
}
