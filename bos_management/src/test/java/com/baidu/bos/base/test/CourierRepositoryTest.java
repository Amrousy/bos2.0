package com.baidu.bos.base.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.baidu.bos.dao.base.CourierRepository;
import com.baidu.bos.dao.base.StandardRepository;
import com.baidu.bos.domain.base.Courier;
import com.baidu.bos.domain.base.Standard;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CourierRepositoryTest {

	@Autowired
	private StandardRepository standardRepository;
	
	@Autowired
	private CourierRepository courierRepository;

	@Test
	@Transactional
	@Rollback(false)
	public void insertData() {
		Standard standard = new Standard();
		standard.setMaxLength(20);
		standard.setMaxWeight(1);
		standard.setMinLength(10);
		standard.setMinWeight(5);
		standard.setName("fox0");
		standard.setOperatingCompany("武汉0");
		standard.setOperator("操作员0");
		standardRepository.save(standard);
		for (int i = 0; i < 100; i++) {
			Courier courier = new Courier();
			courier.setCourierNum("1000" + i);
			courier.setName("zhangsan" + i);
			courier.setTelephone("123456789" + i);
			courier.setPda("2000" + i);
			courier.setCheckPwd("5555" + i);
			courier.setType("小件" + i);
			courier.setCompany("武汉" + i);
			courier.setVehicleType("板车" + i);
			courier.setVehicleNum("鄂A " + "2927" + i);
			courier.setStandard(standard);
			courierRepository.save(courier);
		}
	}
}
