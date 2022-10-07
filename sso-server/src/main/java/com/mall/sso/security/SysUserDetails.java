package com.mall.sso.security;


import cn.dev33.satoken.stp.StpUtil;
import com.mall.system.feign.AdminFeign;
import com.mall.system.pojo.Admin;
import entity.BCrypt;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("sysUserDetails")
public class SysUserDetails {

    AdminFeign adminFeign;

    @Autowired
    public void setAdminFeign(AdminFeign adminFeign) {
        this.adminFeign = adminFeign;
    }


    public Map<String, String> loadAdminUserByUsername(String username, String password) {
        Map<String, String> backEndUser = new HashMap<>();
        //查询用户信息
        Result<Admin> result = adminFeign.findByAdminName(username);
        Admin admin = result.getData();
        if (admin == null) {
            return backEndUser;
        }
        if (username.equals(admin.getUsername()) && BCrypt.checkpw(password, admin.getPassword())) {
            //在查询数据库的时候，设置一个用户唯一的id到login中，在网关鉴权中需要使用该id查询数据库并找到对应的用户角色
            StpUtil.login(admin.getUsername());
            //将当前登录用户信息保存到session中
            StpUtil.getSession().set("username", admin.getUsername());
            backEndUser.put("username", admin.getUsername());
            backEndUser.put("saToken", StpUtil.getTokenValue());
        }
        return backEndUser;
    }
}
