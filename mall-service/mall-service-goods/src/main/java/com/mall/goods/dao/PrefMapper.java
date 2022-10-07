package com.mall.goods.dao;
import com.mall.goods.pojo.Pref;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository("prefMapper")
public interface PrefMapper extends Mapper<Pref> {
}
