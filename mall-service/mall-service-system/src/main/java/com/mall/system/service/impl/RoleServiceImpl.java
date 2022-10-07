package com.mall.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.system.dao.RoleMapper;
import com.mall.system.service.RoleService;
import com.mall.system.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleMapper roleMapper;

    @Autowired
    public void setRoleMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }



    /**
     * 条件+分页查询
     * @param role 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Role> findPage(Role role, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(role);
        //执行搜索
        return new PageInfo<Role>(roleMapper.selectByExample(example));
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Role> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<Role>(roleMapper.selectAll());
    }

    /**
     * 条件查询
     * @param role
     * @return
     */
    @Override
    public List<Role> findList(Role role){
        //构建查询条件
        Example example = createExample(role);
        //根据构建的条件查询数据
        return roleMapper.selectByExample(example);
    }


    /**
     * 构建查询对象
     * @param role
     * @return
     */
    public Example createExample(Role role){
        Example example=new Example(Role.class);
        Example.Criteria criteria = example.createCriteria();
        if(role!=null){
            // id
            if(!StringUtils.isEmpty(role.getId())){
                criteria.andLike("id","%"+role.getId()+"%");
            }
            // 角色名称
            if(!StringUtils.isEmpty(role.getName())){
                criteria.andEqualTo("name",role.getName());
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
        roleMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改
     * @param role
     */
    @Override
    public void update(Role role){
        roleMapper.updateByPrimaryKeySelective(role);
    }

    /**
     * 增加Role
     * @param role
     */
    @Override
    public void add(Role role){
        roleMapper.insert(role);
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public Role findById(Integer id){
        return  roleMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询全部数据
     * @return
     */
    @Override
    public List<Role> findAll() {
        return roleMapper.selectAll();
    }
}
