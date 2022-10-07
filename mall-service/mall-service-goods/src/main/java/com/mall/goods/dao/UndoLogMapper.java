package com.mall.goods.dao;
import com.mall.goods.pojo.UndoLog;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository("undoLogMapper")
public interface UndoLogMapper extends Mapper<UndoLog> {
}
