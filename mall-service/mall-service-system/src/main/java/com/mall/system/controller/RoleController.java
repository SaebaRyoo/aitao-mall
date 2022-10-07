package com.mall.system.controller;

import com.github.pagehelper.PageInfo;
import com.mall.system.service.RoleService;
import com.mall.system.pojo.Role;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    private RoleService roleService;

    @Autowired
    public void setAdminRoleService(RoleService roleService) {
        this.roleService = roleService;
    }


    /***
     * 分页条件搜索实现
     * @param role
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}")
    public Result<PageInfo> findPage(@RequestBody(required = false) Role role, @PathVariable int page, @PathVariable int size) {
        PageInfo<Role> pageInfo = roleService.findPage(role, page, size);
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
        PageInfo<Role> pageInfo = roleService.findPage(page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    /***
     * 多条件搜索数据
     * @param role
     * @return
     */
    @PostMapping(value = "/search")
    public Result<List<Role>> findList(@RequestBody(required = false) Role role) {
        List<Role> list = roleService.findList(role);
        return new Result<List<Role>>(true, StatusCode.OK, "查询成功", list);
    }

    /***
     * 根据ID删除用户
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable String id) {
        roleService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /***
     * 修改数据
     * @param role
     * @param id
     * @return
     */
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody Role role, @PathVariable Integer id) {
        //设置主键值
        role.setId(id);
        roleService.update(role);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /***
     * 新增数据
     * @param role
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Role role) {
        roleService.add(role);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @GetMapping({"/{id}"})
    public Result<Role> findById(@PathVariable Integer id) {
        Role role = roleService.findById(id);
        return new Result<Role>(true, StatusCode.OK, "查询成功", role);
    }

    /***
     * 查询全部数据
     * @return
     */
    @GetMapping
    public Result<List<Role>> findAll() {
        List<Role> list = roleService.findAll();
        return new Result<List<Role>>(true, StatusCode.OK, "查询成功", list);
    }
}
