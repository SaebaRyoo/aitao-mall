package com.mall.user.dao;
import com.mall.user.pojo.Cities;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository("citiesMapper")
public interface CitiesMapper extends Mapper<Cities> {
}
