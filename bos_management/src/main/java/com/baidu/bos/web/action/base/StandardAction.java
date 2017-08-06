package com.baidu.bos.web.action.base;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.baidu.bos.domain.base.Standard;
import com.baidu.bos.service.base.StandardService;
import com.baidu.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class StandardAction extends BaseAction<Standard> {

	// 属性注册驱动
	private int page;

	private int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	@Autowired
	private StandardService standardService;

	// 分页查询
	@Action(value = "standard_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<Standard> pageData = standardService.findPageData(pageable);
		// 压入值栈
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}

	// 查询所有的取派标准
	@Action(value = "standard_findAll", results = { @Result(name = "success", type = "json") })
	public String findAll() {
		List<Standard> standards = standardService.finAll();
		ActionContext.getContext().getValueStack().push(standards);
		return SUCCESS;
	}
}
