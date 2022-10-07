package com.mall.order.dao;
import com.mall.order.pojo.ReturnOrder;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository(value = "returnOrderMapper")
public interface ReturnOrderMapper extends Mapper<ReturnOrder> {
}
