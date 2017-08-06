package com.baidu.crm.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baidu.crm.dao.CustomerRepository;
import com.baidu.crm.domain.Customer;
import com.baidu.crm.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	// 查询所有未关联客户列表
	@Override
	public List<Customer> findNoAssociationCustomers() {
		// FixedAreaId is null
		return customerRepository.findByFixedAreaIdIsNull();
	}

	// 查询已经关联到指定定区的客户列表
	@Override
	public List<Customer> findHasAssociationFixedAreaCustomers(String fixedAreaId) {
		// FixedAreaId is ?
		return customerRepository.findByFixedAreaId(fixedAreaId);
	}

	// 将客户关联到定区上 将所有客户id 拼成字符串1，2，3
	@Override
	public void associationCustomersToFixedArea(String customerIdStr, String fixedAreaId) {
		// 解除关联动作
		customerRepository.clearFixedAreaId(fixedAreaId);
		// cut string 1,2,3
		if (StringUtils.isBlank(customerIdStr) || customerIdStr.equals("null")) {
			return;
		}
		String[] customerIdArray = customerIdStr.split(",");
		for (String idStr : customerIdArray) {
			Integer id = Integer.parseInt(idStr);
			customerRepository.updateFixedAreaId(fixedAreaId, id);
		}
	}

	// 用户注册
	@Override
	public void regist(Customer customer) {
		customerRepository.save(customer);
	}

	// 根据电话查询客户
	@Override
	public Customer findCustomerByTelephone(String telephone) {
		return customerRepository.findCustomerByTelephone(telephone);
	}

	// 修改用户电话
	@Override
	public void updateType(String telephone) {
		customerRepository.updateType(telephone);
	}

	// 用户登录
	@Override
	public Customer login(String telephone, String password) {
		return customerRepository.findCustomerByTelephoneAndPassword(telephone, password);
	}

	// 根据地址 获取定区编码
	@Override
	public String findFixedAreaIdByAddress(String address) {
		return customerRepository.findFixedAreaIdByAddress(address);
	}
	
}
