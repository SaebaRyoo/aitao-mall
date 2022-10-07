package com.mall.goods.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.goods.dao.CategoryMapper;
import com.mall.goods.dao.ParaMapper;
import com.mall.goods.dao.TemplateMapper;
import com.mall.goods.pojo.Category;
import com.mall.goods.pojo.Para;
import com.mall.goods.pojo.Template;
import com.mall.goods.service.ParaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service("paraService")
public class ParaServiceImpl implements ParaService {

    ParaMapper paraMapper;
    TemplateMapper templateMapper;
    CategoryMapper categoryMapper;

    @Autowired
    public void setParaMapper(ParaMapper paraMapper) {
        this.paraMapper = paraMapper;
    }

    @Autowired
    public void setTemplateMapper(TemplateMapper templateMapper) {
        this.templateMapper = templateMapper;
    }

    @Autowired
    public void setCategoryMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    /**
     * 更新template中paraNum的统计数据
     *
     * @param para
     * @param count
     */
    public void updateTemplateParaNum(Para para, Integer count) {
        Template template = templateMapper.selectByPrimaryKey(para.getTemplateId());
        template.setParaNum(template.getParaNum() + count);
        templateMapper.updateByPrimaryKeySelective(template);
    }

    /**
     * 根据分类id查询参数列表
     * @param categoryId
     * @return
     */
    @Override
    public List<Para> findByCategoryId(Integer categoryId) {
        //查找分类
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        //通过查找到的分类中的template_id作为条件查询对应的参数列表
        Para para = new Para();
        para.setTemplateId(category.getTemplateId());
        return paraMapper.select(para);
    }

    /**
     * 添加参数
     *
     * @param para
     */
    @Override
    @Transactional // 增加事务管理
    public void add(Para para) {
        paraMapper.insert(para);
        updateTemplateParaNum(para, 1);
    }

    /**
     * 根据id删除参数
     *
     * @param id
     */
    @Override
    @Transactional // 增加事务管理
    public void delete(Integer id) {
        //查询指定模板
        Para para = paraMapper.selectByPrimaryKey(id);

        //删除模板
        paraMapper.deleteByPrimaryKey(id);

        // 更新统计数据
        updateTemplateParaNum(para, -1);
    }

    /**
     * 更新参数
     *
     * @param para
     */
    @Override
    public void update(Para para) {
        paraMapper.updateByPrimaryKeySelective(para);
    }

    public Example createExample(Para para) {
        Example example = new Example(Para.class);
        //创建条件
        Example.Criteria criteria = example.createCriteria();

        if (para != null) {
            //模糊查询 where name like %name%
            if (!StringUtils.isEmpty(para.getName())) {
                criteria.andLike("name", "%" + para.getName());
            }

            //and options=...
            if (!StringUtils.isEmpty(para.getOptions())) {
                criteria.andEqualTo("options", para.getOptions());
            }

            //and seq=...
            if (!StringUtils.isEmpty(para.getSeq())) {
                criteria.andEqualTo("seq", para.getSeq());
            }

            //and seq=...
            if (!StringUtils.isEmpty(para.getTemplateId())) {
                criteria.andEqualTo("template_id", para.getTemplateId());
            }
        }

        return example;
    }

    /**
     * 分页 + 条件搜索
     *
     * @param para
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Para> findPage(Para para, Integer page, Integer size) {
        //分页
        PageHelper.startPage(page, size);
        //构建搜索条件
        Example example = createExample(para);
        //执行搜索
        return new PageInfo<Para>(paraMapper.selectByExample(example));
    }

    /**
     * 分页搜索
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Para> findPage(Integer page, Integer size) {
        PageHelper.startPage(page,size);
        return new PageInfo<Para>(paraMapper.selectAll());
    }

    /**
     * 条件搜索
     * @param para
     * @return
     */
    @Override
    public List<Para> findList(Para para) {
        Example example = createExample(para);
        return paraMapper.selectByExample(example);
    }

    /**
     * 通过id查找
     * @param id
     * @return
     */
    @Override
    public Para findById(Integer id) {
        return paraMapper.selectByPrimaryKey(id);
    }

    /**
     * 查找全部
     * @return
     */
    @Override
    public List<Para> findAll() {
        return paraMapper.selectAll();
    }
}
