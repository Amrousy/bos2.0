package com.baidu.bos.web.action.take_delivery;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import com.baidu.bos.domain.take_delivery.WayBill;
import com.baidu.bos.service.take_delivery.WayBillService;
import com.baidu.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;

@Controller
@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
public class WayBillAction extends BaseAction<WayBill> {

	private static final Logger LOGGER = Logger.getLogger(WayBillAction.class);

	@Autowired
	private WayBillService wayBillService;

	// 保存快速录入运单
	@Action(value = "wayBill_save", results = { @Result(name = "success", type = "json") })
	public String save() {
		// 去除没有Id的 order对象
		if (model.getOrder() != null && (model.getOrder().getId() == null || model.getOrder().getId() == 0)) {
			model.setOrder(null);
		}
		Map<String, Object> result = new HashMap<>();
		try {
			wayBillService.save(model);
			// 保存成功
			result.put("success", true);
			result.put("msg", "运单保存成功!!!");
			LOGGER.info("保存运单成功,运单号：" + model.getWayBillNum());
		} catch (Exception e) {
			e.printStackTrace();
			// 保存失败
			result.put("success", false);
			result.put("msg", "运单保存失败!!!");
			LOGGER.info("保存运单失败,运单号:" + model.getWayBillNum(), e);
		}
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}

	@Action(value = "waybill_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		// 无条件查询
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Sort.Order(Sort.Direction.DESC, "id")));
		// 调用业务层进行查询
		Page<WayBill> pageData = wayBillService.findPageData(model, pageable);
		// 压入值栈
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}

	@Action(value = "order_findByWayBillNum", results = { @Result(name = "success", type = "json") })
	public String findByWayBillNum() {
		// 调用业务层查询
		WayBill wayBill = wayBillService.findByWayBillNum(model.getWayBillNum());
		Map<String, Object> result = new HashMap<>();
		if (wayBill != null) {
			result.put("success", true);
			result.put("wayBillData", wayBill);
		} else if (wayBill == null) {
			result.put("success", false);
		}
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
}
