package com.mall.user.dao;
import com.mall.user.pojo.UndoLog;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;


@Repository("undoLogMapper")
public interface UndoLogMapper extends Mapper<UndoLog> {
}
