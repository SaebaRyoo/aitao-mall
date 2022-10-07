package com.mall.goods.service;

import com.github.pagehelper.PageInfo;
import com.mall.goods.pojo.Brand;

import java.util.List;

public interface BrandService {

    /**
     * 查询对应分类下的品牌集合
     * @param categoryId
     * @return
     */
    List<Brand> findByCategoryId(Integer categoryId);

    /**
     * 查询所有品牌
     */
    List<Brand> findAll();

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Brand findById(int id);


    /**
     * 新建品牌
     * @param brand
     */
    void add(Brand brand);


    /**
     * 修改品牌
     * @param brand
     */
    void update(Brand brand);

    /**
     * 删除品牌
     * @param id
     */
    void delete(int id);

    /**
     * 多条件搜索
     * @param brand
     * @return
     */
    List<Brand> findList(Brand brand);

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Brand> findPage(int page, int size);



    /**
     * 分页+条件搜索
     * @param brand
     * @param page
     * @param size
     * @return
     */
    PageInfo<Brand> findPage(Brand brand, int page, int size);
}
