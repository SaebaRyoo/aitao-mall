package com.component.feign.intercptor;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.component.feign.properties.FeignProperties;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
//
//@Component
public class TransportGatewayHeaderByFeignInterceptor implements RequestInterceptor {
    private FeignProperties properties;

    public void setProperties(FeignProperties properties) {
        this.properties = properties;
    }
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //if (properties.getTransportHeaderByFeign()) {
            //不需要经过网关访问,直接放行
            if (requestAttributes != null) {
                //1.获取请求对象
                HttpServletRequest request = requestAttributes.getRequest();
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    //2.获取请求对象中的所有的头信息(网关传递过来的)
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();//头的名称
                        String value = request.getHeader(name);//头名称对应的值
                        //System.out.println("name:" + name + "::::::::value:" + value);

                        //解决调用Feign时出现IO Exception too many bytes written 的错误
                        //if (name.equalsIgnoreCase("content-length")) {
                        //    continue;
                        //}
                        //3.将头信息传递给fegin (restTemplate)
                        requestTemplate.header(name,value);
                    }
                }
            }
        //}
    }
}
