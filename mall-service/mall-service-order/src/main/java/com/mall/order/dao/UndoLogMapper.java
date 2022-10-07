package com.mall.order.dao;
import com.mall.order.pojo.UndoLog;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository(value = "undoLogMapper")
public interface UndoLogMapper extends Mapper<UndoLog> {
}
