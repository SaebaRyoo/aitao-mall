package com.mall.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.system.dao.RoleMenuMapper;
import com.mall.system.service.RoleMenuService;
import com.mall.system.pojo.RoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class RoleMenuServiceImpl implements RoleMenuService {

    private RoleMenuMapper roleMenuMapper;

    @Autowired
    public void setRoleMenuMapper(RoleMenuMapper roleMenuMapper) {
        this.roleMenuMapper = roleMenuMapper;
    }



    /**
     * 条件+分页查询
     * @param roleMenu 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<RoleMenu> findPage(RoleMenu roleMenu, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(roleMenu);
        //执行搜索
        return new PageInfo<RoleMenu>(roleMenuMapper.selectByExample(example));
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<RoleMenu> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<RoleMenu>(roleMenuMapper.selectAll());
    }

    /**
     * 条件查询
     * @param roleMenu
     * @return
     */
    @Override
    public List<RoleMenu> findList(RoleMenu roleMenu){
        //构建查询条件
        Example example = createExample(roleMenu);
        //根据构建的条件查询数据
        return roleMenuMapper.selectByExample(example);
    }


    /**
     * 构建查询对象
     * @param roleMenu
     * @return
     */
    public Example createExample(RoleMenu roleMenu){
        Example example=new Example(RoleMenu.class);
        Example.Criteria criteria = example.createCriteria();
        if(roleMenu!=null){
            // 角色id
            if(!StringUtils.isEmpty(roleMenu.getRoleId())){
                criteria.andLike("roleId","%"+roleMenu.getRoleId()+"%");
            }
            // 菜单id
            if(!StringUtils.isEmpty(roleMenu.getMenuId())){
                criteria.andEqualTo("menuId",roleMenu.getMenuId());
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
        roleMenuMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改
     * @param roleMenu
     */
    @Override
    public void update(RoleMenu roleMenu){
        roleMenuMapper.updateByPrimaryKeySelective(roleMenu);
    }

    /**
     * 增加RoleMenu
     * @param roleMenu
     */
    @Override
    public void add(RoleMenu roleMenu){
        roleMenuMapper.insert(roleMenu);
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public RoleMenu findById(String id){
        return  roleMenuMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询全部数据
     * @return
     */
    @Override
    public List<RoleMenu> findAll() {
        return roleMenuMapper.selectAll();
    }
}
