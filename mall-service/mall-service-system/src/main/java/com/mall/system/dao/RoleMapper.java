package com.mall.system.dao;

import com.mall.system.pojo.Role;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository("roleMapper")
public interface RoleMapper extends Mapper<Role> {
}
