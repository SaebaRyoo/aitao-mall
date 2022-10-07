package com.mall.goods.service;

import com.github.pagehelper.PageInfo;
import com.mall.goods.pojo.Para;

import java.util.List;

public interface ParaService {

    /**
     * 根据分类id查询参数列表
     * @param categoryId
     * @return
     */
    List<Para> findByCategoryId(Integer categoryId);

    /**
     * 增加参数
     * @param para
     */
    void add(Para para);

    /**
     * 根据id删除参数
     * @param id
     */
    void delete(Integer id);

    /**
     * 修改参数
     * @param para
     */
    void update(Para para);

    /**
     * 分页 + 条件查询
     * @param para
     * @param page
     * @param size
     * @return
     */
    PageInfo<Para> findPage(Para para, Integer page, Integer size);

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Para> findPage(Integer page, Integer size);

    /**
     * 条件查询
     * @param para
     * @return
     */
    List<Para> findList(Para para);

    /**
     * 根据id查询参数
     * @param id
     * @return
     */
    Para findById(Integer id);

    /**
     * 查询所有参数
     * @return
     */
    List<Para> findAll();
}
