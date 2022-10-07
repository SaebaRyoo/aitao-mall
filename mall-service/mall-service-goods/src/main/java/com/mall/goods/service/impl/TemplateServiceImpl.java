package com.mall.goods.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.goods.dao.TemplateMapper;
import com.mall.goods.pojo.Template;
import com.mall.goods.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Repository("templateService")
public class TemplateServiceImpl implements TemplateService {

    TemplateMapper templateMapper;

    @Autowired
    public void setTemplateMapper(TemplateMapper templateMapper) {
        this.templateMapper = templateMapper;
    }

    /**
     * 构建Template查询对象
     * @param template
     * @return
     */
    public Example createExample(Template template) {
        Example example = new Example(Template.class);
        //条件对象
        Example.Criteria criteria = example.createCriteria();

        //添加查询条件(and, or, like等语句)
        if (template != null) {
            // id
            if (!StringUtils.isEmpty(template.getId())) {
                criteria.andEqualTo("id", template.getId());
            }

            //模板名称
            if (!StringUtils.isEmpty(template.getName())) {
                criteria.andLike("name", "%"+template.getName());
            }

            //规格数量
            if (!StringUtils.isEmpty(template.getSpecNum())) {
                criteria.andEqualTo("specNum", template.getSpecNum());
            }

            // 参数数量
            if (!StringUtils.isEmpty(template.getParaNum())) {
                criteria.andEqualTo("paraNum", template.getParaNum());
            }

        }

        return example;
    }

    /**
     * 条件+分页搜索
     * @param template
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Template> findPage(Template template, int page, int size) {
        //分页
        PageHelper.startPage(page, size);
        //搜索条件构建
        Example example = createExample(template);
        //执行搜索
        return new PageInfo<Template>(templateMapper.selectByExample(example));
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Template> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        return new PageInfo<Template>(templateMapper.selectAll());
    }

    /**
     * 条件查询
     * @param template
     * @return
     */
    @Override
    public List<Template> findList(Template template) {
        Example example = createExample(template);
        return templateMapper.selectByExample(example);
    }

    /**
     * 根据id删除模板
     * @param id
     */
    @Override
    public void delete(Integer id) {
        templateMapper.deleteByPrimaryKey(id);
    }

    /**
     * 更新template
     * @param template
     */
    @Override
    public void update(Template template) {
        templateMapper.updateByPrimaryKeySelective(template);
    }

    /**
     * 添加template
     * @param template
     */
    @Override
    public void add(Template template) {
        templateMapper.insert(template);
    }

    /**
     * 根据Id查询Template数据
     * @param id
     * @return
     */
    @Override
    public Template findById(Integer id) {
        return templateMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有Template数据
     * @return
     */
    @Override
    public List<Template> findAll() {
        return templateMapper.selectAll();
    }
}
