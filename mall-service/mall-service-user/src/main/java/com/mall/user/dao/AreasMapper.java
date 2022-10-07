package com.mall.user.dao;
import com.mall.user.pojo.Areas;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository("areasMapper")
public interface AreasMapper extends Mapper<Areas> {
}
