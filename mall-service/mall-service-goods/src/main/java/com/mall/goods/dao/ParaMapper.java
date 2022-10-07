package com.mall.goods.dao;

import com.mall.goods.pojo.Para;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository("paraMapper")
public interface ParaMapper extends Mapper<Para> {
}
