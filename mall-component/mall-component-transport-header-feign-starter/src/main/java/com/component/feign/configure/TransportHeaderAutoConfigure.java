package com.component.feign.configure;

import com.component.feign.intercptor.TransportGatewayHeaderByFeignInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import com.component.feign.properties.FeignProperties;

@EnableConfigurationProperties(FeignProperties.class)
public class TransportHeaderAutoConfigure {
    /**
     * 3. 添加拦截器配置
     * @return
     */
    @Bean
    public TransportGatewayHeaderByFeignInterceptor transportHeaderInterceptorConfigure() {
        TransportHeaderInterceptorConfigure configure = new TransportHeaderInterceptorConfigure();
        return configure.transportGatewayHeaderByFeignInterceptor();
    }
}
