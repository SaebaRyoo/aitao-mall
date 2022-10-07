package com.mall.user.dao;
import com.mall.user.pojo.Provinces;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;


@Repository("provincesMapper")
public interface ProvincesMapper extends Mapper<Provinces> {
}
