package com.mall.system.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_menu")
public class Menu {

    //菜单id
    @Id
    @Column(name = "id")
    private String id;

    //菜单名称
    @Column(name = "name")
    private String name;

    //图标
    @Column(name = "icon")
    private String icon;

    //资源路径
    @Column(name = "url")
    private String url;

    //上级菜单id
    @Column(name = "parent_id")
    private String parentId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
