package com.mall.system.feign;

import com.mall.system.pojo.Role;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "system", path = "/role", contextId = "2")
public interface RoleFeign {


    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @GetMapping({"/{id}"})
    Result<Role> findById(@PathVariable Integer id);
}
