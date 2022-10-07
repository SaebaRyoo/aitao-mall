package com.mall.gateway.backend.service;

import com.mall.system.pojo.Role;

public interface RoleService {

    /**
     * 通过id查找
     * @param id
     * @return
     */
    Role findById(Integer id);
}
