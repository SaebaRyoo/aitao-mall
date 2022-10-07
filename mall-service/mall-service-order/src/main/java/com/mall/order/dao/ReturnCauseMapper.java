package com.mall.order.dao;
import com.mall.order.pojo.ReturnCause;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository(value = "returnCauseMapper")
public interface ReturnCauseMapper extends Mapper<ReturnCause> {
}
