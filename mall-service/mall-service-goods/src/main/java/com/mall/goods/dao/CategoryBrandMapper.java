package com.mall.goods.dao;
import com.mall.goods.pojo.CategoryBrand;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;


@Repository("categoryBrandMapper")
public interface CategoryBrandMapper extends Mapper<CategoryBrand> {
}
