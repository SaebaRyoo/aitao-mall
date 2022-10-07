package com.mall.goods.dao;
import com.mall.goods.pojo.Spu;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository("spuMapper")
public interface SpuMapper extends Mapper<Spu> {
}
