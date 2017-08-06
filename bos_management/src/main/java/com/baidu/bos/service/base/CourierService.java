package com.baidu.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.baidu.bos.domain.base.Courier;

public interface CourierService {

	public void save(Courier courier);

	public Page<Courier> findPageData(Specification<Courier> specification, Pageable pageable);

	public void delBatch(String[] idArray);

	public void recBatch(String[] idArray);

	public List<Courier> findNoAssociation();

}
