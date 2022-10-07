package com.mall.gateway.web.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(0)
public class AddTagFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //System.out.println("---------全局filter---------");
        ServerHttpRequest request = exchange.getRequest();
        // 在网关中添加一个自定义头，防止用户绕过网关请求微服务
        request = exchange.getRequest().mutate().header("entry-filter", "custom-security").build();

        return chain.filter(exchange.mutate().request(request).build());
    }

}
