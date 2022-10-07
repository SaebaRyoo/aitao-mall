package com.component.feign.configure;


import com.component.feign.intercptor.TransportGatewayHeaderByFeignInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import com.component.feign.properties.FeignProperties;

public class TransportHeaderInterceptorConfigure {
    private FeignProperties properties;

    @Autowired
    public void setProperties(FeignProperties properties) {
        this.properties = properties;
    }

    public TransportGatewayHeaderByFeignInterceptor transportGatewayHeaderByFeignInterceptor() {
        TransportGatewayHeaderByFeignInterceptor interceptor = new TransportGatewayHeaderByFeignInterceptor();
        interceptor.setProperties(properties);
        return interceptor;
    }
}
