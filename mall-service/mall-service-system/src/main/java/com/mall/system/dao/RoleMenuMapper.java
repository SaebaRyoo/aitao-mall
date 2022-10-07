package com.mall.system.dao;

import com.mall.system.pojo.RoleMenu;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository("roleMenuMapper")
public interface RoleMenuMapper extends Mapper<RoleMenu> {
}
