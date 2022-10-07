package com.mall.order.dao;
import com.mall.order.pojo.Preferential;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository(value = "preferentialMapper")
public interface PreferentialMapper extends Mapper<Preferential> {
}
