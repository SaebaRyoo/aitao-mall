package com.mall.system.dao;

import com.mall.system.pojo.AdminRole;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository("adminRoleMapper")
public interface AdminRoleMapper extends Mapper<AdminRole> {
}
