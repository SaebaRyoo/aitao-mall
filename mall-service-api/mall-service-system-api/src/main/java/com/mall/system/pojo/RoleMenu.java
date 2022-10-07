package com.mall.system.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 菜单和权限的关联表
 */
@Table(name = "tb_role_menu")
public class RoleMenu {

    //角色id
    @Id
    @Column(name = "role_id")
    private String roleId;


    //菜单id
    @Id
    @Column(name = "menu_id")
    private String menuId;


    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
