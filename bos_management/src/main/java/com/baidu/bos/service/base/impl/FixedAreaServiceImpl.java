package com.baidu.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baidu.bos.dao.base.CourierRepository;
import com.baidu.bos.dao.base.FixedAreaRepository;
import com.baidu.bos.dao.base.TakeTimeRepository;
import com.baidu.bos.domain.base.Courier;
import com.baidu.bos.domain.base.FixedArea;
import com.baidu.bos.domain.base.TakeTime;
import com.baidu.bos.service.base.FixedAreaService;

@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {

	@Autowired
	private FixedAreaRepository fixedAreaRepository;
	@Autowired
	private CourierRepository courierRepository;
	@Autowired
	private TakeTimeRepository takeTimeRepository;

	@Override
	public void save(FixedArea model) {
		fixedAreaRepository.save(model);
	}

	@Override
	public Page<FixedArea> findPageData(Specification<FixedArea> specification, PageRequest pageable) {
		return fixedAreaRepository.findAll(specification, pageable);
	}

	@Override
	public void associationCourierToFixedArea(FixedArea model, Integer courierId, Integer takeTimeId) {
		FixedArea fixedArea = fixedAreaRepository.findOne(model.getId());
		Courier courier = courierRepository.findOne(courierId);
		TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);
		// 快递员关联到定区上
		fixedArea.getCouriers().add(courier);
		// 将收派标准关联到定区上
		courier.setTakeTime(takeTime);
	}

}
