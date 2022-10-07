package com.mall.gateway.backend.service;

import com.mall.system.pojo.Admin;
import org.springframework.stereotype.Service;


public interface AdminService {
    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    Admin findByAdminName(String username);
}
