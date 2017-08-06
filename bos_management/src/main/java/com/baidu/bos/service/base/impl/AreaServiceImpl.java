package com.baidu.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baidu.bos.dao.base.AreaRepository;
import com.baidu.bos.domain.base.Area;
import com.baidu.bos.service.base.AreaService;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {

	@Autowired
	private AreaRepository areaRepository;
	
	@Override
	public void saveBatch(List<Area> areas) {
		areaRepository.save(areas);
	}

	@Override
	public Page<Area> findPagePageData(Specification<Area> specification, PageRequest pageable) {
		return areaRepository.findAll(specification, pageable);
	}

}
