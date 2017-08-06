package com.baidu.bos.web.action.system;

import com.baidu.bos.domain.system.Permission;
import com.baidu.bos.service.system.PermissionService;
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
public class PermissionAction extends BaseAction<Permission> {

    @Autowired
    private PermissionService permissionService;

    @Action(value = "permission_list", results = {@Result(name = "success", type = "json")})
    public String findAll() {
        List<Permission> permissions = permissionService.findAll();
        ActionContext.getContext().getValueStack().push(permissions);
        return SUCCESS;
    }

    @Action(value = "permission_save", results = {@Result(name = "success", type = "redirect", location = "pages/system/permission.html")})
    public String save() {
        permissionService.save(model);
        return SUCCESS;
    }
}