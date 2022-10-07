package com.mall.goods.dao;

import com.mall.goods.pojo.Template;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository("templateMapper")
public interface TemplateMapper extends Mapper<Template> {
}
