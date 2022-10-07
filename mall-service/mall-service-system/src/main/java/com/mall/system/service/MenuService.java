package com.mall.system.service;

import com.github.pagehelper.PageInfo;
import com.mall.system.pojo.Menu;

import java.util.List;

public interface MenuService {

    /***
     * 多条件分页查询
     * @param menu
     * @param page
     * @param size
     * @return
     */
    PageInfo<Menu> findPage(Menu menu, int page, int size);

    /***
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Menu> findPage(int page, int size);

    /***
     * 多条件搜索方法
     * @param menu
     * @return
     */
    List<Menu> findList(Menu menu);

    /***
     * 删除
     * @param id
     */
    void delete(String id);

    /***
     * 修改数据
     * @param menu
     */
    void update(Menu menu);

    /***
     * 新增
     * @param menu
     */
    void add(Menu menu);

    /**
     * 通过id查找
     * @param id
     * @return
     */
    Menu findById(String id);

    /***
     * 查询所有
     * @return
     */
    List<Menu> findAll();

}
