package com.mall.goods.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.goods.dao.BrandMapper;
import com.mall.goods.pojo.Brand;
import com.mall.goods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    private BrandMapper brandMapper;

    @Autowired
    public void setBrandMapper(BrandMapper brandMapper) {
        this.brandMapper = brandMapper;
    }

    /**
     * 查询对应分类下的品牌集合
     * @param categoryId 分类id
     * @return
     */
    @Override
    public List<Brand> findByCategoryId(Integer categoryId) {
        //自己在dao中实现连表查询
        return brandMapper.findByCategoryId(categoryId);
    }

    @Override
    public List<Brand> findAll() {
        return brandMapper.selectAll();
    }

    @Override
    public Brand findById(int id) {
        // 根据id查询->通用Mapper
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(Brand brand) {
        //但凡包含selective，会忽略brand对象中的空值
        brandMapper.insertSelective(brand);
    }

    @Override
    public void update(Brand brand) {
        // 忽略空值，有啥改啥，空值就不更改
        brandMapper.updateByPrimaryKeySelective(brand);
    }

    @Override
    public void delete(int id) {
        brandMapper.deleteByPrimaryKey(id);
    }

    public Example createExample(Brand brand) {

        // 自定义条件搜索对象 Example
        Example example = new Example(Brand.class);
        // 条件构造器
        Example.Criteria criteria = example.createCriteria();

        if (brand != null) {
            // brand.name != null 根据名字模糊搜索 根据名字模搜索where name like '%华%'
            if (!StringUtils.isEmpty(brand.getName())) {
                /**
                 * 1： Brand(JavaBean)的属性名
                 * 2:  占位符参数，搜索的条件
                 */
                criteria.andLike("name", "%"+brand.getName()+"%");
            }

            // brand.letter != null 根据首字母模糊搜索 and letter='H'
            if (!StringUtils.isEmpty(brand.getLetter())) {
                //添加一个 and letter='H' 语句
                //如果是criteria.orEqualTo 就是 or letter='H'
                criteria.andEqualTo("letter", brand.getLetter());
            }
        }

        return example;
    }

    @Override
    public List<Brand> findList(Brand brand) {
        Example example = createExample(brand);

        return brandMapper.selectByExample(example);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Brand> findPage(int page, int size) {
        /**
         * 分页实现 PageHelper.startPage(page, size)， 后面需的查询要紧跟集合查询
         * 1. 当前页
         * 2. 每页显示多少题
         */
        PageHelper.startPage(page, size);
        //查询集合
        List<Brand> brands = brandMapper.selectAll();
        //封装PageInfo
        return new PageInfo<Brand>(brands);
    }

    /**
     * 分页+条件搜索
     * @param brand
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Brand> findPage(Brand brand, int page, int size) {
        //分页
        PageHelper.startPage(page, size);
        //搜索数据
        Example example = createExample(brand);
        List<Brand> brands = brandMapper.selectByExample(example);
        //封装PageInfo<Brand>
        return new PageInfo<Brand>(brands);
    }
}
