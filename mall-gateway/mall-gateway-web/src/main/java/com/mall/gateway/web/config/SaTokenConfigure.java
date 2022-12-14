package com.mall.gateway.web.config;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * [Sa-Token 权限认证] 全局配置类
 */
@Configuration
public class SaTokenConfigure {
    /**
     * 注册 [Sa-Token全局过滤器]
     */
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 指定 [拦截路由]
                .addInclude("/**")
                // 指定 [放行路由]
                .addExclude("/favicon.ico")
                // 指定[认证函数]: 每次请求执行
                .setAuth(obj -> {
                    //System.out.println("---------- sa全局认证");
                    // 登录校验 -- 拦截所有路由，并排除认证服务下的接口 用于开放登录，查询登录状态，注销
                    SaRouter.match("/**", "/api/sso/**", r -> StpUtil.checkLogin());
                    // 权限认证 -- 不同模块, 校验不同权限
                    //SaRouter.match("/admin/**", r -> StpUtil.checkPermission("user-delete"));
                    //SaRouter.match("/orders/**", r -> StpUtil.checkPermission("user-update"));

                    //商城用户目前只通过角色来分配访问权限
                    SaRouter.match("/api/spu/**", r -> StpUtil.checkRole("member"));
                    SaRouter.match("/api/user/**", r -> StpUtil.checkRole("member"));
                })
                // 指定[异常处理函数]：每次[认证函数]发生异常时执行此函数
                .setError(e -> {
                    System.out.println("---------- sa全局异常 "+ e.getMessage());
                    return SaResult.error(e.getMessage());
                });
    }
}
