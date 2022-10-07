package com.mall.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.system.dao.AdminMapper;
import com.mall.system.service.AdminService;
import com.mall.system.pojo.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private AdminMapper adminMapper;

    @Autowired
    public void setAdminMapper(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }



    /**
     * 条件+分页查询
     * @param admin 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Admin> findPage(Admin admin, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(admin);
        //执行搜索
        return new PageInfo<Admin>(adminMapper.selectByExample(example));
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Admin> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<Admin>(adminMapper.selectAll());
    }

    /**
     * 条件查询
     * @param admin
     * @return
     */
    @Override
    public List<Admin> findList(Admin admin){
        //构建查询条件
        Example example = createExample(admin);
        //根据构建的条件查询数据
        return adminMapper.selectByExample(example);
    }


    /**
     * 构建查询对象
     * @param admin
     * @return
     */
    public Example createExample(Admin admin){
        Example example=new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        if(admin!=null){
            // 用户名
            if(!StringUtils.isEmpty(admin.getUsername())){
                criteria.andLike("username","%"+admin.getUsername()+"%");
            }
            // 密码，加密存储
            if(!StringUtils.isEmpty(admin.getPassword())){
                criteria.andEqualTo("password",admin.getPassword());
            }
            // 用户状态
            if(!StringUtils.isEmpty(admin.getStatus())){
                criteria.andEqualTo("status",admin.getStatus());
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
        adminMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改
     * @param admin
     */
    @Override
    public void update(Admin admin){
        adminMapper.updateByPrimaryKeySelective(admin);
    }

    /**
     * 增加Admin
     * @param admin
     */
    @Override
    public void add(Admin admin){
        adminMapper.insert(admin);
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public Admin findById(String id){
        return  adminMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询全部数据
     * @return
     */
    @Override
    public List<Admin> findAll() {
        return adminMapper.selectAll();
    }

    /**
     * 通过用户名查找
     * @param username
     * @return
     */
    @Override
    public Admin findByAdminName(String username) {
        return adminMapper.findByAdminName(username);
    }
}
