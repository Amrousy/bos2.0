package com.baidu.bos.base.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.baidu.bos.dao.base.StandardRepository;
import com.baidu.bos.domain.base.Standard;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class StandardRepositoryTest {

	private Standard standard = new Standard();

	@Autowired
	private StandardRepository standardRepository;

	@Test
	public void save() {
		standard.setMaxLength(11);
		standardRepository.save(standard);
	}

	@Test
	public void delete() {
		standardRepository.delete(3);
	}

	@Test
	public void update() {
		standard.setId(5);
		standard.setMaxLength(55);
		standardRepository.save(standard);
	}

	@Test
	public void findAll() {
		System.out.println(standardRepository.findAll());
	}

	@Test
	@Transactional
	@Rollback(false)
	public void insertData() {
		for (int i = 0; i < 100; i++) {
			Standard standard = new Standard();
			standard.setMaxLength(i+20);
			standard.setMaxWeight(i+1);
			standard.setMinLength(i+10);
			standard.setMinWeight(i+5);
			standard.setName("fox" + i);
			standard.setOperatingCompany("武汉"+ i);
			standard.setOperator("操作员" + i);
			standardRepository.save(standard);
		}
	}
}
