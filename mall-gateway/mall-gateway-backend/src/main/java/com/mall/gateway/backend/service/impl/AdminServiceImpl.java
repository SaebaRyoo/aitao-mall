package com.mall.gateway.backend.service.impl;

import com.mall.gateway.backend.dao.AdminMapper;
import com.mall.gateway.backend.service.AdminService;
import com.mall.system.pojo.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    AdminMapper adminMapper;

    @Autowired
    public void setAdminMapper(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @Override
    public Admin findByAdminName(String username) {
        return adminMapper.findByAdminName(username);
    }
}
