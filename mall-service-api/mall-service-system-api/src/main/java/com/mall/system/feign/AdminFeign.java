package com.mall.system.feign;


import com.mall.system.pojo.Admin;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "system", path = "/admin", contextId = "0")
public interface AdminFeign {

    @GetMapping({"/find/{username}"})
    Result<Admin> findByAdminName(@PathVariable String username);
}
