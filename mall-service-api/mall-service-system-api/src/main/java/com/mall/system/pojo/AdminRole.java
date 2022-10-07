package com.mall.system.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 管理员和角色的关联表
 */
@Table(name = "tb_admin_role")
public class AdminRole {


    // 管理员id
    @Id
    @Column(name = "admin_id")
    private Integer adminId;

    //角色id
    @Id
    @Column(name = "role_id")
    private Integer roleId;

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
