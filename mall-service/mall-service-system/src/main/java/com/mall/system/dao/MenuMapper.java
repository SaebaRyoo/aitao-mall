package com.mall.system.dao;

import com.mall.system.pojo.Menu;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository("menuMapper")
public interface MenuMapper extends Mapper<Menu> {
}
