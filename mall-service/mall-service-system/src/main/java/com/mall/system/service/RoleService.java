package com.mall.system.service;

import com.github.pagehelper.PageInfo;
import com.mall.system.pojo.Role;

import java.util.List;

public interface RoleService {

    /***
     * 多条件分页查询
     * @param role
     * @param page
     * @param size
     * @return
     */
    PageInfo<Role> findPage(Role role, int page, int size);

    /***
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Role> findPage(int page, int size);

    /***
     * 多条件搜索方法
     * @param role
     * @return
     */
    List<Role> findList(Role role);

    /***
     * 删除
     * @param id
     */
    void delete(String id);

    /***
     * 修改数据
     * @param role
     */
    void update(Role role);

    /***
     * 新增
     * @param role
     */
    void add(Role role);

    /**
     * 通过id查找
     * @param id
     * @return
     */
    Role findById(Integer id);

    /***
     * 查询所有
     * @return
     */
    List<Role> findAll();
}
