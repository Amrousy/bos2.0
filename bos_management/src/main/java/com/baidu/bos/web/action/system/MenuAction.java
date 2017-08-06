package com.baidu.bos.web.action.system;

import com.baidu.bos.domain.system.Menu;
import com.baidu.bos.service.system.MenuService;
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

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class MenuAction extends BaseAction<Menu> {

    @Autowired
    private MenuService menuService;

    @Action(value = "menu_list", results = {@Result(name = "success", type = "json")})
    public String list() {
        // 调用业务层，查询所有菜单数据
        List<Menu> menus = menuService.findAll();
        // 存入值栈
        ActionContext.getContext().getValueStack().push(menus);
        return SUCCESS;
    }

    @Action(value = "menu_save", results = {@Result(name = "success", type = "redirect", location = "pages/system/menu.html")})
    public String save() {
        // 调用业务层保存菜单数据
        menuService.save(model);
        return SUCCESS;
    }

}