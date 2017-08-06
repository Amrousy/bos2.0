package com.baidu.bos.web.action.base;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.baidu.bos.domain.base.TakeTime;
import com.baidu.bos.service.base.TakeTimeService;
import com.baidu.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class TakeTimeAction extends BaseAction<TakeTime> {
	@Autowired
	private TakeTimeService takeTimeService;

	// 查询所有收派事件
	@Action(value = "taketime_findAll", results = { @Result(name = "success", type = "json") })
	public String findAll() {
		List<TakeTime> takeTimes = takeTimeService.findAll();
		ActionContext.getContext().getValueStack().push(takeTimes);
		return SUCCESS;
	}

}
