package com.mall.system.service;

import com.github.pagehelper.PageInfo;
import com.mall.system.pojo.RoleMenu;

import java.util.List;

public interface RoleMenuService {


    /***
     * 多条件分页查询
     * @param menuRole
     * @param page
     * @param size
     * @return
     */
    PageInfo<RoleMenu> findPage(RoleMenu menuRole, int page, int size);

    /***
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<RoleMenu> findPage(int page, int size);

    /***
     * 多条件搜索方法
     * @param menuRole
     * @return
     */
    List<RoleMenu> findList(RoleMenu menuRole);

    /***
     * 删除
     * @param id
     */
    void delete(String id);

    /***
     * 修改数据
     * @param menuRole
     */
    void update(RoleMenu menuRole);

    /***
     * 新增
     * @param menuRole
     */
    void add(RoleMenu menuRole);

    /**
     * 通过id查找
     * @param id
     * @return
     */
    RoleMenu findById(String id);

    /***
     * 查询所有
     * @return
     */
    List<RoleMenu> findAll();
}
