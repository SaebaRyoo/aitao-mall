package com.mall.system.service;

import com.github.pagehelper.PageInfo;
import com.mall.system.pojo.AdminRole;

import java.util.List;

public interface AdminRoleService {

    /***
     * 多条件分页查询
     * @param adminRole
     * @param page
     * @param size
     * @return
     */
    PageInfo<AdminRole> findPage(AdminRole adminRole, int page, int size);

    /***
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<AdminRole> findPage(int page, int size);

    /***
     * 多条件搜索方法
     * @param adminRole
     * @return
     */
    List<AdminRole> findList(AdminRole adminRole);

    /***
     * 删除
     * @param id
     */
    void delete(String id);

    /***
     * 修改数据
     * @param adminRole
     */
    void update(AdminRole adminRole);

    /***
     * 新增
     * @param adminRole
     */
    void add(AdminRole adminRole);

    /**
     * 通过id查询数据
     * @param id
     * @return
     */
    AdminRole findById(Integer id);

    /***
     * 查询所有
     * @return
     */
    List<AdminRole> findAll();

}
