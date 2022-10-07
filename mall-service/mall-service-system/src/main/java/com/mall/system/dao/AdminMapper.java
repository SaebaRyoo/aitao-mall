package com.mall.system.dao;
import com.mall.system.pojo.Admin;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;


@Repository("adminMapper")
public interface AdminMapper extends Mapper<Admin> {


    @Select("select * from tb_admin where username=#{username}")
    public Admin findByAdminName(String username);
}
