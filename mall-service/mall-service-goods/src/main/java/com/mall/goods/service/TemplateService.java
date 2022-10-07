package com.mall.goods.service;

import com.github.pagehelper.PageInfo;
import com.mall.goods.pojo.Template;

import java.util.List;

public interface TemplateService {

    /**
     * 分页条件搜索
     *
     * @param template
     * @param page
     * @param size
     * @return
     */
    PageInfo<Template> findPage(Template template, int page, int size);

    /**
     * 分页查找
     *
     * @param page
     * @param size
     * @return
     */
    PageInfo<Template> findPage(int page, int size);

    /**
     * 多条件搜索方法
     *
     * @param template
     * @return
     */

    List<Template> findList(Template template);

    /**
     * 根据模板id删除模板
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 更新模板
     *
     * @param template
     */
    void update(Template template);

    /**
     * 创建模板
     *
     * @param template
     */
    void add(Template template);

    /**
     * 根据模板id查询
     *
     * @param id
     * @return
     */
    Template findById(Integer id);

    /**
     * 查询所有Template
     *
     * @return
     */
    List<Template> findAll();
}
