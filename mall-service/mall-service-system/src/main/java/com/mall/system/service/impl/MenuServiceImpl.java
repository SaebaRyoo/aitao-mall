package com.mall.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.system.dao.MenuMapper;
import com.mall.system.service.MenuService;
import com.mall.system.pojo.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    private MenuMapper menuMapper;

    @Autowired
    public void setMenuMapper(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }



    /**
     * 条件+分页查询
     * @param admin 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Menu> findPage(Menu admin, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(admin);
        //执行搜索
        return new PageInfo<Menu>(menuMapper.selectByExample(example));
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Menu> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<Menu>(menuMapper.selectAll());
    }

    /**
     * 条件查询
     * @param admin
     * @return
     */
    @Override
    public List<Menu> findList(Menu admin){
        //构建查询条件
        Example example = createExample(admin);
        //根据构建的条件查询数据
        return menuMapper.selectByExample(example);
    }


    /**
     * 构建查询对象
     * @param admin
     * @return
     */
    public Example createExample(Menu admin){
        Example example=new Example(Menu.class);
        Example.Criteria criteria = example.createCriteria();
        if(admin!=null){
            //菜单id
            if(!StringUtils.isEmpty(admin.getId())){
                criteria.andLike("id","%"+admin.getId()+"%");
            }
            // 菜单名
            if(!StringUtils.isEmpty(admin.getName())){
                criteria.andLike("name","%"+admin.getName()+"%");
            }
            // 图标
            if(!StringUtils.isEmpty(admin.getIcon())){
                criteria.andEqualTo("icon",admin.getIcon());
            }
            // url
            if(!StringUtils.isEmpty(admin.getUrl())){
                criteria.andEqualTo("url",admin.getUrl());
            }
            //父级id
            if(!StringUtils.isEmpty(admin.getParentId())){
                criteria.andEqualTo("parentId",admin.getParentId());
            }
        }
        return example;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(String id){
        menuMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改
     * @param admin
     */
    @Override
    public void update(Menu admin){
        menuMapper.updateByPrimaryKeySelective(admin);
    }

    /**
     * 增加Menu
     * @param admin
     */
    @Override
    public void add(Menu admin){
        menuMapper.insert(admin);
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public Menu findById(String id){
        return  menuMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询全部数据
     * @return
     */
    @Override
    public List<Menu> findAll() {
        return menuMapper.selectAll();
    }
}
