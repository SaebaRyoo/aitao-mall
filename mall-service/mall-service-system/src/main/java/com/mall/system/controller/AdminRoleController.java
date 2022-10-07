package com.mall.system.controller;

import com.github.pagehelper.PageInfo;
import com.mall.system.service.AdminRoleService;
import com.mall.system.pojo.AdminRole;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adminRole")
public class AdminRoleController {

    private AdminRoleService adminRoleService;

    @Autowired
    public void setAdminRoleService(AdminRoleService adminRoleService) {
        this.adminRoleService = adminRoleService;
    }


    /***
     * 分页条件搜索实现
     * @param adminRole
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}")
    public Result<PageInfo> findPage(@RequestBody(required = false) AdminRole adminRole, @PathVariable int page, @PathVariable int size) {
        PageInfo<AdminRole> pageInfo = adminRoleService.findPage(adminRole, page, size);
        return new Result(true, StatusCode.OK, "查询成功", pageInfo);
    }

    /***
     * 分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}")
    public Result<PageInfo> findPage(@PathVariable int page, @PathVariable int size) {
        PageInfo<AdminRole> pageInfo = adminRoleService.findPage(page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    /***
     * 多条件搜索数据
     * @param adminRole
     * @return
     */
    @PostMapping(value = "/search")
    public Result<List<AdminRole>> findList(@RequestBody(required = false) AdminRole adminRole) {
        List<AdminRole> list = adminRoleService.findList(adminRole);
        return new Result<List<AdminRole>>(true, StatusCode.OK, "查询成功", list);
    }

    /***
     * 根据ID删除用户
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable String id) {
        adminRoleService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /***
     * 修改数据
     * @param adminRole
     * @param id
     * @return
     */
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody AdminRole adminRole, @PathVariable Integer id) {
        //设置主键值
        adminRole.setAdminId(id);
        adminRoleService.update(adminRole);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /***
     * 新增数据
     * @param adminRole
     * @return
     */
    @PostMapping
    public Result add(@RequestBody AdminRole adminRole) {
        adminRoleService.add(adminRole);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @GetMapping({"/{id}"})
    public Result<AdminRole> findById(@PathVariable Integer id) {
        //调用AdminRoleService实现根据主键查询Admin
        AdminRole adminRole = adminRoleService.findById(id);
        return new Result<AdminRole>(true, StatusCode.OK, "查询成功", adminRole);
    }

    /***
     * 查询全部数据
     * @return
     */
    @GetMapping
    public Result<List<AdminRole>> findAll() {
        //调用AdminRoleService实现查询所有Admin
        List<AdminRole> list = adminRoleService.findAll();
        return new Result<List<AdminRole>>(true, StatusCode.OK, "查询成功", list);
    }
}
