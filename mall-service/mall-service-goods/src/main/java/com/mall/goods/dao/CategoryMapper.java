package com.mall.goods.dao;
import com.mall.goods.pojo.Category;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository("categoryMapper")
public interface CategoryMapper extends Mapper<Category> {
}
