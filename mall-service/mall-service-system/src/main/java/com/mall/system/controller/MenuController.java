package com.mall.system.controller;

import com.github.pagehelper.PageInfo;
import com.mall.system.service.MenuService;
import com.mall.system.pojo.Menu;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    private MenuService menuService;

    @Autowired
    public void setAdminRoleService(MenuService menuService) {
        this.menuService = menuService;
    }


    /***
     * 分页条件搜索实现
     * @param menu
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}")
    public Result<PageInfo> findPage(@RequestBody(required = false) Menu menu, @PathVariable int page, @PathVariable int size) {
        PageInfo<Menu> pageInfo = menuService.findPage(menu, page, size);
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
        PageInfo<Menu> pageInfo = menuService.findPage(page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    /***
     * 多条件搜索数据
     * @param menu
     * @return
     */
    @PostMapping(value = "/search")
    public Result<List<Menu>> findList(@RequestBody(required = false) Menu menu) {
        List<Menu> list = menuService.findList(menu);
        return new Result<List<Menu>>(true, StatusCode.OK, "查询成功", list);
    }

    /***
     * 根据ID删除用户
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable String id) {
        menuService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /***
     * 修改数据
     * @param menu
     * @param id
     * @return
     */
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody Menu menu, @PathVariable String id) {
        //设置主键值
        menu.setId(id);
        menuService.update(menu);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /***
     * 新增数据
     * @param menu
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Menu menu) {
        menuService.add(menu);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @GetMapping({"/{id}"})
    public Result<Menu> findById(@PathVariable String id) {
        //调用AdminRoleService实现根据主键查询Admin
        Menu menu = menuService.findById(id);
        return new Result<Menu>(true, StatusCode.OK, "查询成功", menu);
    }

    /***
     * 查询全部数据
     * @return
     */
    @GetMapping
    public Result<List<Menu>> findAll() {
        //调用AdminRoleService实现查询所有Admin
        List<Menu> list = menuService.findAll();
        return new Result<List<Menu>>(true, StatusCode.OK, "查询成功", list);
    }
}
