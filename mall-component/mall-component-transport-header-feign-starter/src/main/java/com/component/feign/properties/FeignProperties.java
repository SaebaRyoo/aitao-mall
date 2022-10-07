package com.component.feign.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 配置解析类
 *
 */
@Data
@ConfigurationProperties(prefix = "server-protect")
public class FeignProperties {

    private Boolean transportHeaderByFeign = Boolean.TRUE;
}
