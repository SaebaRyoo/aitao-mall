package com.component.security.interceptor;

import com.component.security.properties.CloudSecurityProperties;
import com.component.security.utils.WebUtils;
import org.json.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器逻辑实现
 */
public class ServerProtectInterceptor implements HandlerInterceptor {

    private CloudSecurityProperties properties;

    public void setProperties(CloudSecurityProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!properties.getOnlyFetchByGateway()) {
            //不需要经过网关访问,直接放行
            return true;
        }
        //需要经过网关访问，查看
        String entry = request.getHeader("entry-filter");
        if (entry == null || !entry.equals("custom-security")) {
            //System.out.println("-------请通过网关访问资源------");
            JSONObject res = new JSONObject();
            res.put("success", false);
            res.put("code", 20003);
            res.put("message", "请从网关访问资源");
            WebUtils.sendJson(response, res);
            return false;
        }
        return true;
    }


}
