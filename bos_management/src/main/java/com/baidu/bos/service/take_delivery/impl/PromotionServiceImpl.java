package com.baidu.bos.service.take_delivery.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baidu.bos.dao.take_delivery.PromotionRepository;
import com.baidu.bos.domain.take_delivery.PageBean;
import com.baidu.bos.domain.take_delivery.Promotion;
import com.baidu.bos.service.take_delivery.PromotionService;

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {

	@Autowired
	private PromotionRepository promotionRepository;

	@Override
	public void save(Promotion model) {
		promotionRepository.save(model);
	}

	@Override
	public Page<Promotion> findPageData(PageRequest pageable) {
		return promotionRepository.findAll(pageable);
	}

	@Override
	public PageBean<Promotion> findPageData(int page, int rows) {
		PageRequest pageable = new PageRequest(page - 1, rows);
		Page<Promotion> pageData = promotionRepository.findAll(pageable);

		// 封装到Page对象
		PageBean<Promotion> pageBean = new PageBean<>();
		pageBean.setTotalCount(pageData.getTotalElements());
		pageBean.setPageData(pageData.getContent());
		return pageBean;
	}

	@Override
	public Promotion findById(Integer id) {
		return promotionRepository.findOne(id);
	}

	@Override
	public void updateStatus(Date date) {
		promotionRepository.updataStatus(date);
	}

}
