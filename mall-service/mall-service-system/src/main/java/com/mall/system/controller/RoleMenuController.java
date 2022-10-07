package com.mall.system.controller;

import com.github.pagehelper.PageInfo;
import com.mall.system.service.RoleMenuService;
import com.mall.system.pojo.RoleMenu;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/roleMenu")
public class RoleMenuController {
    private RoleMenuService roleMenuService;

    @Autowired
    public void setAdminRoleService(RoleMenuService roleMenuService) {
        this.roleMenuService = roleMenuService;
    }


    /***
     * 分页条件搜索实现
     * @param roleMenu
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}")
    public Result<PageInfo> findPage(@RequestBody(required = false) RoleMenu roleMenu, @PathVariable int page, @PathVariable int size) {
        PageInfo<RoleMenu> pageInfo = roleMenuService.findPage(roleMenu, page, size);
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
        PageInfo<RoleMenu> pageInfo = roleMenuService.findPage(page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    /***
     * 多条件搜索数据
     * @param roleMenu
     * @return
     */
    @PostMapping(value = "/search")
    public Result<List<RoleMenu>> findList(@RequestBody(required = false) RoleMenu roleMenu) {
        List<RoleMenu> list = roleMenuService.findList(roleMenu);
        return new Result<List<RoleMenu>>(true, StatusCode.OK, "查询成功", list);
    }

    /***
     * 根据ID删除用户
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable String id) {
        roleMenuService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /***
     * 修改数据
     * @param roleMenu
     * @param id
     * @return
     */
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody RoleMenu roleMenu, @PathVariable String id) {
        //设置主键值
        roleMenu.setMenuId(id);
        roleMenuService.update(roleMenu);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /***
     * 新增数据
     * @param roleMenu
     * @return
     */
    @PostMapping
    public Result add(@RequestBody RoleMenu roleMenu) {
        roleMenuService.add(roleMenu);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @GetMapping({"/{id}"})
    public Result<RoleMenu> findById(@PathVariable String id) {
        RoleMenu roleMenu = roleMenuService.findById(id);
        return new Result<RoleMenu>(true, StatusCode.OK, "查询成功", roleMenu);
    }

    /***
     * 查询全部数据
     * @return
     */
    @GetMapping
    public Result<List<RoleMenu>> findAll() {
        List<RoleMenu> list = roleMenuService.findAll();
        return new Result<List<RoleMenu>>(true, StatusCode.OK, "查询成功", list);
    }
}
