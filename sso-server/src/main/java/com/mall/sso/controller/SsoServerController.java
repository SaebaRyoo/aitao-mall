package com.mall.sso.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mall.sso.security.MemberUserDetails;
import com.mall.sso.security.SysUserDetails;
import com.mall.user.pojo.User;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/sso")
public class SsoServerController {

    MemberUserDetails memberUserDetails;

    SysUserDetails sysUserDetails;

    @Autowired
    public void setMemberUserDetails(MemberUserDetails memberUserDetails) {
        this.memberUserDetails = memberUserDetails;
    }

    @Autowired
    public void setSysUserDetails(SysUserDetails sysUserDetails) {
        this.sysUserDetails = sysUserDetails;
    }

    /**
     * 单点登录接口
     * @param usr
     * usr.username 用户登录账号
     * usr.password 用户密码
     * usr.scope    来源: web: 网站; sys: 后台管理人员
     */
    @GetMapping("/login")
    public Result login(@RequestParam(required = false) Map<String, String> usr) throws JsonProcessingException {

        String username = usr.get("username");
        String password = usr.get("password");
        String scope = usr.get("scope");

        if (!StringUtils.hasLength(username) || !StringUtils.hasLength(password)) {
            return new Result<User>(false, StatusCode.LOGINERROR, "登录失败，用户名和密码不能为空");
        }

        if (StringUtils.hasLength(scope)) {
            //系统用户登录
            if (scope.equals("sys")) {
                Map<String, String> user = sysUserDetails.loadAdminUserByUsername(username, password);
                return new Result(true, StatusCode.OK, "登录成功", user);
            }
        } else {
            // 没有scope就是会员用户登录,查会员的表
            Map<String, String> user = memberUserDetails.loadUserByUsername(username, password);
            return new Result(true, StatusCode.OK, "登录成功", user);
        }
        return new Result(false, StatusCode.LOGINERROR, "登录失败，用户名或者密码错误");
    }

    /**
     * 查看登录状态
     * @return
     */
    @GetMapping("/isLogin")
    public Result isLogin() {
        return new Result(true, StatusCode.OK, "", StpUtil.isLogin());
    }

    /**
     * 用户注销
     * @return
     */
    @GetMapping("/logout")
    public Result logout() {
        StpUtil.logout();
        return new Result(true, StatusCode.OK, "注销成功");
    }
}
