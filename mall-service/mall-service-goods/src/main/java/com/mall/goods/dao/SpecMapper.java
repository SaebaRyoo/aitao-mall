package com.mall.goods.dao;

import com.mall.goods.pojo.Spec;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository("specMapper")
public interface SpecMapper extends Mapper<Spec> {
}
