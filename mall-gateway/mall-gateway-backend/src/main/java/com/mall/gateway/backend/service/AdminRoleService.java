package com.mall.gateway.backend.service;

import com.mall.system.pojo.AdminRole;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AdminRoleService {

    List<AdminRole> findList(AdminRole adminRole);
}
