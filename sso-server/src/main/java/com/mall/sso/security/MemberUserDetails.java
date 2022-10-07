package com.mall.sso.security;


import cn.dev33.satoken.stp.StpUtil;
import com.mall.user.feign.UserFeign;
import com.mall.user.pojo.User;
import entity.BCrypt;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("memberUserDetails")
public class MemberUserDetails {
    UserFeign userFeign;

    @Autowired
    public void setUserFeign(UserFeign userFeign) {
        this.userFeign = userFeign;
    }


    public Map<String, String> loadUserByUsername(String username, String password) {
        Map<String, String> forwardUser = new HashMap<>();
        //查询用户信息
        Result<User> result = userFeign.findById(username);
        User user = result.getData();
        if (user == null) {
            return forwardUser;
        }
        if (username.equals(user.getUsername()) && BCrypt.checkpw(password, user.getPassword())) {
            //在查询数据库的时候，设置一个用户唯一的id到login中，在网关鉴权中需要使用该id查询数据库并找到对应的用户角色
            StpUtil.login(user.getUsername());
            //将当前登录用户信息保存到session中
            StpUtil.getSession().set("username", user.getUsername());
            forwardUser.put("username", user.getUsername());
            forwardUser.put("email", user.getEmail());
            forwardUser.put("saToken", StpUtil.getTokenValue());
        }
        return forwardUser;
    }
}
