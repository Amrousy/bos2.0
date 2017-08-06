package com.baidu.bos.base.test;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.bos.domain.base.Standard;
import com.baidu.bos.service.base.StandardService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class StandardSaveActionTest extends ActionSupport implements ModelDriven<Standard> {

	@Autowired
	private StandardService standardService;

	private Standard standard = new Standard();

	@Override
	public Standard getModel() {
		return standard;
	}

	@Action(value = "standard_save", results = {
			@Result(name = "success", type = "redirect", location = "./pages/base/standard.html") })
	public String save() {
		HttpServletRequest request = ServletActionContext.getRequest();
		standard.setName(request.getParameter("name"));
		standard.setMinWeight(Integer.parseInt(request.getParameter("minWeight")));
		standard.setMaxWeight(Integer.parseInt(request.getParameter("maxWeight")));
		standard.setMinLength(Integer.parseInt(request.getParameter("minLength")));
		standard.setMaxLength(Integer.parseInt(request.getParameter("maxLength")));
		standardService.save(standard);
		return SUCCESS;
	}
}
