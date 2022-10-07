package com.component.security.configure;

import com.component.security.properties.CloudSecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 自动配置
 * 2. 在spring.factories中配置了该类路径时，此包会被spring-boot自动扫到，然后加载配置解析类
 */
@EnableConfigurationProperties(CloudSecurityProperties.class)
public class CloudSecurityAutoConfigure {

    /**
     * 3. 添加拦截器配置
     * @return
     */
    @Bean
    public CloudSecurityInterceptorConfigure cloudSecurityInterceptorConfigure() {
        return new CloudSecurityInterceptorConfigure();
    }
}
