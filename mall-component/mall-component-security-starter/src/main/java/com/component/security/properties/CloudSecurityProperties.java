package com.component.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 配置解析类
 *
 */
@Data
@ConfigurationProperties(prefix = "server-protect")
public class CloudSecurityProperties {

    private Boolean onlyFetchByGateway = Boolean.TRUE;
}
