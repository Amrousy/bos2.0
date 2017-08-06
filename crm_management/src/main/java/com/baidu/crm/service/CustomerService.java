package com.baidu.crm.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.baidu.crm.domain.Customer;

public interface CustomerService {

	// 查询所有未关联客户列表
	@Path("/noassociationcustomers")
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<Customer> findNoAssociationCustomers();

	// 已经关联到指定定区的客户列表
	@Path("/associationfixedareacustomers/{fixedareaid}")
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<Customer> findHasAssociationFixedAreaCustomers(@PathParam("fixedareaid") String fixedAreaId);

	// 将客户关联到定区上 将所有客户id 拼成字符串1，2，3
	@Path("/associationcustomerstofixedarea")
	@PUT
	public void associationCustomersToFixedArea(@QueryParam("customerIdStr") String customerIdStr,
			@QueryParam("fixedAreaId") String fixedAreaId);

	// 用户注册
	@Path("/customer")
	@POST
	@Consumes({ "application/xml", "application/json" })
	public void regist(Customer customer);

	// 根据电话查询客户
	@Path("/customer/telephone/{telephone}")
	@GET
	@Consumes({ "application/xml", "application/json" })
	public Customer findCustomerByTelephone(@PathParam("telephone") String telephone);

	// 修改用户电话
	@Path("/customer/updatetype/{telephone}")
	@GET
	public void updateType(@PathParam("telephone") String telephone);

	// 用户登录
	@Path("customer/login")
	@GET
	@Consumes({ "application/xml", "application/json" })
	public Customer login(@QueryParam("telephone") String telephone, @QueryParam("password") String password);

	// 根据地址 获取定区编码
	@Path("/customer/findFixedAreaIdByAddress")
	@GET
	@Consumes({ "application/xml", "application/json" })
	public String findFixedAreaIdByAddress(@QueryParam("address") String address);
}
