package com.baidu.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.baidu.bos.domain.base.Area;

public interface AreaService {

	public void saveBatch(List<Area> areas);

	public Page<Area> findPagePageData(Specification<Area> specification, PageRequest pageable);
}
