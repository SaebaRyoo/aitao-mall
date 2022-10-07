package com.mall.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.system.dao.AdminRoleMapper;
import com.mall.system.service.AdminRoleService;
import com.mall.system.pojo.AdminRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class AdminRoleServiceImpl implements AdminRoleService {

    private AdminRoleMapper adminRoleMapper;

    @Autowired
    public void setAdminRoleMapper(AdminRoleMapper adminRoleMapper) {
        this.adminRoleMapper = adminRoleMapper;
    }



    /**
     * 条件+分页查询
     * @param adminRole 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<AdminRole> findPage(AdminRole adminRole, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(adminRole);
        //执行搜索
        return new PageInfo<AdminRole>(adminRoleMapper.selectByExample(example));
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<AdminRole> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<AdminRole>(adminRoleMapper.selectAll());
    }

    /**
     * 条件查询
     * @param adminRole
     * @return
     */
    @Override
    public List<AdminRole> findList(AdminRole adminRole){
        //构建查询条件
        Example example = createExample(adminRole);
        //根据构建的条件查询数据
        return adminRoleMapper.selectByExample(example);
    }


    /**
     * 构建查询对象
     * @param adminRole
     * @return
     */
    public Example createExample(AdminRole adminRole){
        Example example=new Example(AdminRole.class);
        Example.Criteria criteria = example.createCriteria();
        if(adminRole!=null){
            // 用户名
            if(!StringUtils.isEmpty(adminRole.getAdminId())){
                criteria.andLike("adminId","%"+adminRole.getAdminId()+"%");
            }
            // 密码，加密存储
            if(!StringUtils.isEmpty(adminRole.getRoleId())){
                criteria.andEqualTo("roleId",adminRole.getRoleId());
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
        adminRoleMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改
     * @param adminRole
     */
    @Override
    public void update(AdminRole adminRole){
        adminRoleMapper.updateByPrimaryKeySelective(adminRole);
    }

    /**
     * 增加AdminRole
     * @param adminRole
     */
    @Override
    public void add(AdminRole adminRole){
        adminRoleMapper.insert(adminRole);
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public AdminRole findById(Integer id){
        return  adminRoleMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询全部数据
     * @return
     */
    @Override
    public List<AdminRole> findAll() {
        return adminRoleMapper.selectAll();
    }
}
