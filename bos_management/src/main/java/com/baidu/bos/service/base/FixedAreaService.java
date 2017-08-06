package com.baidu.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.baidu.bos.domain.base.FixedArea;

public interface FixedAreaService {

	public void save(FixedArea model);

	public Page<FixedArea> findPageData(Specification<FixedArea> specification, PageRequest pageable);

	public void associationCourierToFixedArea(FixedArea model, Integer courierId, Integer takeTimeId);

}
