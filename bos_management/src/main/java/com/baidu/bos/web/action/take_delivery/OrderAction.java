package com.baidu.bos.web.action.take_delivery;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.baidu.bos.domain.take_delivery.Order;
import com.baidu.bos.service.take_delivery.OrderService;
import com.baidu.bos.service.take_delivery.WayBillService;
import com.baidu.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;

// 前端系统订单数据处理
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class OrderAction extends BaseAction<Order> {

	@Autowired
	private OrderService orderService;

	// 根据订单号查询订单信息
	@Action(value = "order_findByOrderNum", results = { @Result(name = "success", type = "json") })
	public String findByOrderNum() {
		// 调用业务层查询订单信息
		Order order = orderService.findByOrderNum(model.getOrderNum());
		Map<String, Object> result = new HashMap<>();
		if (order == null) {
			// 订单不存在
			result.put("success", false);
		} else if (order != null) {
			result.put("success", true);
			result.put("orderData", order);
		}
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}

}
