package com.baidu.bos.domain.system;

import org.apache.struts2.json.annotations.JSON;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @description:菜单
 */
@Entity
@Table(name = "T_MENU")
public class Menu {
    @Id
    @GeneratedValue
    @Column(name = "C_ID")
    private int id;
    @Column(name = "C_NAME")
    private String name; // 菜单名称
    @Column(name = "C_PAGE")
    private String page; // 访问路径
    @Column(name = "C_PRIORITY")
    private Integer priority; // 优先级
    @Column(name = "C_DESCRIPTION")
    private String description; // 描述

    @ManyToMany(mappedBy = "menus")
    private Set<Role> roles = new HashSet<Role>(0);

    @OneToMany(mappedBy = "parentMenu")
    private Set<Menu> childrenMenus = new HashSet<Menu>();

    @ManyToOne
    @JoinColumn(name = "C_PID")
    private Menu parentMenu;

    @Transient
    public Integer getpId() {
        if (parentMenu == null) {
            return 0;
        } else {
            return parentMenu.getId();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JSON(serialize = false)
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @JSON(serialize = false)
    public Set<Menu> getChildrenMenus() {
        return childrenMenus;
    }

    public void setChildrenMenus(Set<Menu> childrenMenus) {
        this.childrenMenus = childrenMenus;
    }

    @JSON(serialize = false)
    public Menu getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(Menu parentMenu) {
        this.parentMenu = parentMenu;
    }

}
