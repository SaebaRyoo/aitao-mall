package com.mall.goods.dao;

import com.mall.goods.pojo.Brand;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * DAO需要继承通用Mapper来获取操作方法
 * 增加数据 调用Mapper.insert()
 * 修改数据 调用Mapper.update(T)
 * 查询数据 根据id查询 Mapper.selectByPrimaryKey(ID)
 */
@Repository("brandMapper")
public interface BrandMapper extends Mapper<Brand> {

    /**
     * 查询分类对应的品牌集合
     * @param categoryId
     * @return
     */
    @Select("SELECT tb.* FROM tb_category_brand tcb, tb_brand tb WHERE tcb.category_id=#{categoryId} AND tb.id=tcb.brand_id")
    List<Brand> findByCategoryId(Integer categoryId);
}
