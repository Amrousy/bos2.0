package com.baidu.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.baidu.bos.domain.base.Standard;

public interface StandardService {
	public void save(Standard standard);

	public Page<Standard> findPageData(Pageable pageable);

	public List<Standard> finAll();
}
