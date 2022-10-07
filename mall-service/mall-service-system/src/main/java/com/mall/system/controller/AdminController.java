package com.mall.system.controller;

import com.github.pagehelper.PageInfo;
import com.mall.system.service.AdminService;
import com.mall.system.pojo.Admin;
import entity.BCrypt;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private AdminService adminService;

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }


    /***
     * 分页条件搜索实现
     * @param admin
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}")
    public Result<PageInfo> findPage(@RequestBody(required = false) Admin admin, @PathVariable int page, @PathVariable int size) {
        //调用AdminService实现分页条件查询Admin
        PageInfo<Admin> pageInfo = adminService.findPage(admin, page, size);
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
        //调用AdminService实现分页查询Admin
        PageInfo<Admin> pageInfo = adminService.findPage(page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    /***
     * 多条件搜索数据
     * @param admin
     * @return
     */
    @PostMapping(value = "/search")
    public Result<List<Admin>> findList(@RequestBody(required = false) Admin admin) {
        //调用AdminService实现条件查询Admin
        List<Admin> list = adminService.findList(admin);
        return new Result<List<Admin>>(true, StatusCode.OK, "查询成功", list);
    }

    /***
     * 根据ID删除用户
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable String id) {
        //调用AdminService实现根据主键删除
        adminService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /***
     * 修改数据
     * @param admin
     * @param id
     * @return
     */
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody Admin admin, @PathVariable Integer id) {
        //设置主键值
        admin.setId(id);
        //调用AdminService实现修改Admin
        adminService.update(admin);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /***
     * 新增数据
     * @param admin
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Admin admin) {
        //调用AdminService实现添加Admin
        //对密码进行加密
        String hashpw = BCrypt.hashpw(admin.getPassword(), BCrypt.gensalt());
        admin.setPassword(hashpw);
        adminService.add(admin);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @GetMapping({"/{id}"})
    public Result<Admin> findById(@PathVariable String id) {
        //调用AdminService实现根据主键查询Admin
        Admin admin = adminService.findById(id);
        return new Result<Admin>(true, StatusCode.OK, "查询成功", admin);
    }

    /**
     * 根据用户名查找
     * @param username
     * @return
     */
    @GetMapping({"/find/{username}"})
    public Result<Admin> findByAdminName(@PathVariable String username) {
        //调用AdminService实现根据主键查询Admin
        Admin admin = adminService.findByAdminName(username);
        return new Result<Admin>(true, StatusCode.OK, "查询成功", admin);
    }

    /***
     * 查询全部数据
     * @return
     */
    @GetMapping
    public Result<List<Admin>> findAll() {
        //调用AdminService实现查询所有Admin
        List<Admin> list = adminService.findAll();
        return new Result<List<Admin>>(true, StatusCode.OK, "查询成功", list);
    }


}
