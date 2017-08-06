package com.baidu.bos.web.action.system;

import com.baidu.bos.domain.system.Role;
import com.baidu.bos.service.system.RoleService;
import com.baidu.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Created by Amrous on 2017/08/05.
 */
@Controller
@Namespace("/")
@ParentPackage("json-default")
@Scope("prototype")
public class RoleAction extends BaseAction<Role> {

    @Autowired
    private RoleService roleService;

    @Action(value = "role_list", results = {@Result(name = "success", type = "json")})
    public String findAll() {
        List<Role> roles = roleService.findAll();
        ActionContext.getContext().getValueStack().push(roles);
        return SUCCESS;
    }

    // 属性驱动
    private String[] permissionIds;
    private String menuIds;

    public void setPermissionIds(String[] permissionIds) {
        this.permissionIds = permissionIds;
    }

    public void setMenuIds(String menuIds) {
        this.menuIds = menuIds;
    }

    @Action(value = "role_save", results = {@Result(name = "success", type = "redirect", location = "pages/system/role.html")})
    public String save() {
        roleService.saveRole(model, permissionIds, menuIds);
        return SUCCESS;
    }
}
