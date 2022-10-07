package com.component.security.configure;

import com.component.security.interceptor.ServerProtectInterceptor;
import com.component.security.properties.CloudSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



public class CloudSecurityInterceptorConfigure implements WebMvcConfigurer {
    //
    //private final String[] WHITE_LIST = {
    //        "/sku",
    //        "/sku/status/**",
    //        "/user/load/**"
    //};
    private CloudSecurityProperties properties;

    @Autowired
    public void setProperties(CloudSecurityProperties properties) {
        this.properties = properties;
    }

    public HandlerInterceptor serverProtectInterceptor() {
        ServerProtectInterceptor interceptor = new ServerProtectInterceptor();
        interceptor.setProperties(properties);
        return interceptor;
    }

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(serverProtectInterceptor())
                .addPathPatterns("/**");
    }
}
