package com.baidu.bos.web.action.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.baidu.bos.domain.base.FixedArea;
import com.baidu.bos.domain.constant.Constants;
import com.baidu.bos.service.base.FixedAreaService;
import com.baidu.bos.web.action.common.BaseAction;
import com.baidu.crm.domain.Customer;
import com.opensymphony.xwork2.ActionContext;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class FixedAreaAction extends BaseAction<FixedArea> {

	@Autowired
	private FixedAreaService fixedAreaService;

	// 添加定区
	@Action(value = "fixedArea_save", results = {
			@Result(name = "success", location = "./pages/base/fixed_area.html", type = "redirect") })
	public String fixedAreaSave() {
		fixedAreaService.save(model);
		return SUCCESS;
	}

	// 定区列表分页条件查询
	@Action(value = "fixedArea_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		// 构造分页查询对象
		PageRequest pageable = new PageRequest(page - 1, rows);
		// 构造条件查询对象
		Specification<FixedArea> specification = new Specification<FixedArea>() {
			@Override
			public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				// 构造查询条件 等值
				if (StringUtils.isNotBlank(model.getId())) {
					// 根据定区编号查询
					Predicate p1 = cb.equal(root.get("id").as(String.class), model.getId());
					list.add(p1);
				}
				// 根据公司查询 模糊
				if (StringUtils.isNoneBlank(model.getCompany())) {
					Predicate p2 = cb.like(root.get("company").as(String.class), "%" + model.getCompany() + "%");
					list.add(p2);
				}
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		// 调用业务层
		Page<FixedArea> pageData = fixedAreaService.findPageData(specification, pageable);
		// 压入值栈
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}

	// 查询未关联定区客户列表
	@Action(value = "fixedArea_findNoAssociationCustomers", results = { @Result(name = "success", type = "json") })
	public String findNoAssociationCustomers() {
		// 使用WebClient调用WebService接口
		Collection<? extends Customer> collection = WebClient
				.create(Constants.CRM_MANAGEMENT_URL + "/services/customerService/noassociationcustomers")
				.accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		// 压入值栈
		ActionContext.getContext().getValueStack().push(collection);
		return SUCCESS;
	}

	// 查询已关联定区客户列表
	@Action(value = "fixedArea_findHasAssociationFixedAreaCustomers", results = {
			@Result(name = "success", type = "json") })
	public String findHasAssociationFixedAreaCustomers() {
		Collection<? extends Customer> collection = WebClient
				.create(Constants.CRM_MANAGEMENT_URL + "/services/customerService/associationfixedareacustomers/"
						+ model.getId())
				.accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		// 压入值栈
		ActionContext.getContext().getValueStack().push(collection);
		return SUCCESS;
	}

	// 属性驱动
	private String[] customerIds;

	public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}

	// 关联客户到定区
	@Action(value = "fixedArea_associationCustomersToFixedArea", results = {
			@Result(name = "success", location = "./pages/base/fixed_area.html", type = "redirect") })
	public String associationCustomersToFixedArea() {
		String customerIdStr = StringUtils.join(customerIds, ",");

		WebClient.create(Constants.CRM_MANAGEMENT_URL + "/services/customerService/associationcustomerstofixedarea"
				+ "?customerIdStr=" + customerIdStr + "&fixedAreaId=" + model.getId()).put(null);
		return SUCCESS;
	}

	// 属性驱动
	private Integer courierId;
	private Integer takeTimeId;

	public void setCourierId(Integer courierId) {
		this.courierId = courierId;
	}

	public void setTakeTimeId(Integer takeTimeId) {
		this.takeTimeId = takeTimeId;
	}

	// 定区关联快递员
	@Action(value = "fixedArea_associationCourierToFixedArea", results = {
			@Result(name = "success", location = "./pages/base/fixed_area.html", type = "redirect") })
	public String associationCourierToFixedArea() {
		fixedAreaService.associationCourierToFixedArea(model, courierId, takeTimeId);
		return SUCCESS;
	}
}
