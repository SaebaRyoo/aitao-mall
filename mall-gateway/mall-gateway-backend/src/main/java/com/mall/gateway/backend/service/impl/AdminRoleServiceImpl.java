package com.mall.gateway.backend.service.impl;

import com.mall.gateway.backend.dao.AdminRoleMapper;
import com.mall.gateway.backend.service.AdminRoleService;
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
    @Override
    public List<AdminRole> findList(AdminRole adminRole) {
        //构建查询条件
        Example example = createExample(adminRole);
        //根据构建的条件查询数据
        return adminRoleMapper.selectByExample(example);
    }
}
