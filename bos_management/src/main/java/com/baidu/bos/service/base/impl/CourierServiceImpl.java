package com.baidu.bos.service.base.impl;

import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baidu.bos.dao.base.CourierRepository;
import com.baidu.bos.domain.base.Courier;
import com.baidu.bos.service.base.CourierService;

@Service
@Transactional
public class CourierServiceImpl implements CourierService {

	@Autowired
	private CourierRepository courierRepository;

	@Override
	@RequiresPermissions("courier_add")
	public void save(Courier courier) {
		courierRepository.save(courier);
	}

	@Override
	public Page<Courier> findPageData(Specification<Courier> specification, Pageable pageable) {
		return courierRepository.findAll(specification, pageable);
	}

	// 取派员作废
	@Override
	public void delBatch(String[] idArray) {
		for (String idStr : idArray) {
			Integer id = Integer.parseInt(idStr);
			courierRepository.updateDelTag(1, id);
		}
	}

	// 取派员还原
	@Override
	public void recBatch(String[] idArray) {
		for (String idStr : idArray) {
			Integer id = Integer.parseInt(idStr);
			courierRepository.updateDelTag(null, id);
		}
	}

	// 查询所有未关联定区的快递员
	@Override
	public List<Courier> findNoAssociation() {
		// 封装specification
		Specification<Courier> specification = new Specification<Courier>() {
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 查询条件fixedAreas为空
				Predicate p = cb.isEmpty(root.get("fixedAreas").as(Set.class));
				return p;
			}
		};
		return courierRepository.findAll(specification);
	}
}