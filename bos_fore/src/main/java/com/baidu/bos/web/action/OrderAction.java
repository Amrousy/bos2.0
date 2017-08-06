package com.baidu.bos.web.action;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.baidu.bos.domain.base.Area;
import com.baidu.bos.domain.constant.Constants;
import com.baidu.bos.domain.take_delivery.Order;
import com.baidu.crm.domain.Customer;

// 前端系统订单数据处理
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class OrderAction extends BaseAction<Order> {

	private String sendAreaInfo; // 客户省市区信息
	private String recAreaInfo; // 收件人省市区信息

	public void setSendAreaInfo(String sendAreaInfo) {
		this.sendAreaInfo = sendAreaInfo;
	}

	public void setRecAreaInfo(String recAreaInfo) {
		this.recAreaInfo = recAreaInfo;
	}

	@Action(value = "order_add", results = { @Result(name = "success", type = "redirect", location = "index.html"),
			@Result(name = "none", type = "redirect", location = "login.html") })

	public String add() {
		Customer customer = (Customer) ServletActionContext.getRequest().getSession().getAttribute("customer");
		if (customer == null) {
			return NONE;
		}
		// 手动封装Area关联
		Area sendArea = new Area();
		String[] sendAreaData = sendAreaInfo.split("/");
		sendArea.setProvince(sendAreaData[0]);
		sendArea.setCity(sendAreaData[1]);
		sendArea.setDistrict(sendAreaData[2]);

		Area recArea = new Area();
		String[] recAreaData = recAreaInfo.split("/");
		recArea.setProvince(recAreaData[0]);
		recArea.setCity(recAreaData[1]);
		recArea.setDistrict(recAreaData[2]);

		model.setSendArea(sendArea);
		model.setRecArea(recArea);

		// 关联当前客户
		model.setCustomer_id(customer.getId());

		// 使用webservice 将数据传递到bos_management
		WebClient.create(Constants.BOS_MANAGEMENT_URL + "/services/orderService/order").type(MediaType.APPLICATION_JSON)
				.post(model);
		return SUCCESS;
	}

}
