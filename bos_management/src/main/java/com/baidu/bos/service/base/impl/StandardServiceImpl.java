package com.baidu.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baidu.bos.dao.base.StandardRepository;
import com.baidu.bos.domain.base.Standard;
import com.baidu.bos.service.base.StandardService;

@Service
@Transactional
public class StandardServiceImpl implements StandardService {

	@Autowired
	private StandardRepository standardRepository;
	
	@Override
	public void save(Standard standard) {
		standardRepository.save(standard);
	}

	@Override
	public Page<Standard> findPageData(Pageable pageable) {
		return standardRepository.findAll(pageable);
	}

	@Override
	public List<Standard> finAll() {
		return standardRepository.findAll();
	}

}
