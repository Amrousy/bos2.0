package com.baidu.bos.oracle.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.crm.domain.Customer;
import com.baidu.crm.service.CustomerService;

public class SelectTypeByTelephone {
	
	@Autowired
	private CustomerService customerService;
	
	@Test
	public void selectType(){
		Customer customer = customerService.findCustomerByTelephone("17607116570");
		Integer type = customer.getType();
		System.out.println(type);
	}
}
