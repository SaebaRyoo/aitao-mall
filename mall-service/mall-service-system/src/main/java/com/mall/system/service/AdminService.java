package com.mall.system.service;

import com.github.pagehelper.PageInfo;
import com.mall.system.pojo.Admin;

import java.util.List;

public interface AdminService {

    /***
     * Admin多条件分页查询
     * @param admin
     * @param page
     * @param size
     * @return
     */
    PageInfo<Admin> findPage(Admin admin, int page, int size);

    /***
     * Admin分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Admin> findPage(int page, int size);

    /***
     * Admin多条件搜索方法
     * @param admin
     * @return
     */
    List<Admin> findList(Admin admin);

    /***
     * 删除Admin
     * @param id
     */
    void delete(String id);

    /***
     * 修改Admin数据
     * @param admin
     */
    void update(Admin admin);

    /***
     * 新增Admin
     * @param admin
     */
    void add(Admin admin);

    /**
     * 根据ID查询Admin
     * @param id
     * @return
     */
     Admin findById(String id);

    /***
     * 查询所有Admin
     * @return
     */
    List<Admin> findAll();


    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    Admin findByAdminName(String username);

}
