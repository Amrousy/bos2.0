package com.baidu.bos.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baidu.bos.domain.take_delivery.WayBill;
import com.baidu.bos.index.WayBillIndexRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class ElasticsearchTest {

	@Autowired
	private WayBillIndexRepository wayBillIndexRepository;
	
	@Test
	public void deleteData() {
		WayBill wayBill = new WayBill();
		wayBill.setId(2064);
		wayBillIndexRepository.delete(wayBill);
	}
	
	@Test
	public void queryTest() {
		Iterable<WayBill> wayBills = wayBillIndexRepository.findAll();
		for (WayBill wayBill : wayBills) {
			System.out.println(wayBill);
		}
	}
	
	@Test
	public void deleteAll() {
		wayBillIndexRepository.deleteAll();
	}
}
