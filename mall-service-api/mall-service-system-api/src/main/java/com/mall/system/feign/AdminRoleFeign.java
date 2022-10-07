package com.mall.system.feign;

import com.mall.system.pojo.AdminRole;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "system", path = "/adminRole", contextId = "1")
public interface AdminRoleFeign {


    @PostMapping(value = "/search")
    Result<List<AdminRole>> findList(@RequestBody(required = false) AdminRole adminRole);
}
