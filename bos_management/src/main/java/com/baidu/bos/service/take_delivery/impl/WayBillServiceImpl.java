package com.baidu.bos.service.take_delivery.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.net.SyslogAppender;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baidu.bos.dao.take_delivery.WayBillRepository;
import com.baidu.bos.domain.take_delivery.WayBill;
import com.baidu.bos.index.WayBillIndexRepository;
import com.baidu.bos.service.take_delivery.WayBillService;

@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {

	@Autowired
	private WayBillRepository wayBillRepository;

	@Autowired
	private WayBillIndexRepository wayBillIndexRepository;

	@Override
	public void save(WayBill wayBill) {
		// 判断运单号是否存在
		WayBill persistWayBill = wayBillRepository.findByWayBillNum(wayBill.getWayBillNum());
		if (persistWayBill != null) {
			try {
				// 运单存在
				Integer id = persistWayBill.getId();
				BeanUtils.copyProperties(persistWayBill, wayBill);
				persistWayBill.setId(id);
				
				// 保存索引
				wayBillIndexRepository.save(persistWayBill);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
		} else if (persistWayBill == null || persistWayBill.getId() == null) {
			// 运单不存在
			wayBillRepository.save(wayBill);
			// 保存索引
			wayBillIndexRepository.save(wayBill);
		}
	}

	// 快速录入无条件分页查询
	@Override
	public Page<WayBill> pageQuery(Pageable pageable) {
		return wayBillRepository.findAll(pageable);
	}

	@Override
	public WayBill findByWayBillNum(String wayBillNum) {
		return wayBillRepository.findByWayBillNum(wayBillNum);
	}

	@Override
	public Page<WayBill> findPageData(WayBill wayBill, Pageable pageable) {
		// 判断wayBill中条件是否存在
		if (StringUtils.isBlank(wayBill.getWayBillNum()) && StringUtils.isBlank(wayBill.getSendAddress())
				&& StringUtils.isBlank(wayBill.getRecAddress()) && StringUtils.isBlank(wayBill.getSendProNum())
				&& (wayBill.getSignStatus() == null || wayBill.getSignStatus() == 0)) {
			// 无条件分页查询
			return wayBillRepository.findAll(pageable);
		} else {
			// 查询条件
			// must 条件必须成立,must not 条件必须不成立,should 条件可以成立or
			BoolQueryBuilder query = new BoolQueryBuilder(); // 布尔查询，多条件组合查询
			// 向组合查询对象添加条件
			if (StringUtils.isNoneBlank(wayBill.getWayBillNum())) {
				// 运单号查询
				TermQueryBuilder termQuery = new TermQueryBuilder("wayBillNum", wayBill.getWayBillNum());
				query.must(termQuery);
			}
			if (StringUtils.isNoneBlank(wayBill.getSendAddress())) {
				// 发货地址 模糊查询
				WildcardQueryBuilder wildcardQuery = new WildcardQueryBuilder("sendAddress",
						"*" + wayBill.getSendAddress() + "*");
				query.must(wildcardQuery);
			}
			if (StringUtils.isNoneBlank(wayBill.getRecAddress())) {
				// 收获地址 模糊查询
				WildcardQueryBuilder wildcardQuery = new WildcardQueryBuilder("recAddress", "*" + wayBill.getRecAddress() + "*");
				query.must(wildcardQuery);
			}
			if (StringUtils.isNoneBlank(wayBill.getSendProNum())) {
				// 速运类型  等值查询
				TermQueryBuilder termQuery = new TermQueryBuilder("sendProNum", wayBill.getSendProNum());
				query.must(termQuery);
			}
			if (wayBill.getSignStatus() != 0 && wayBill.getSignStatus() != null) {
				// 签收状态查询
				TermQueryBuilder termQuery = new TermQueryBuilder("signStatus", wayBill.getSignStatus());
				query.must(termQuery);
			}
			
			NativeSearchQuery searchQuery = new NativeSearchQuery(query);
			searchQuery.setPageable(pageable); // 分页效果
			// 有条件查询、查询索引库
			return wayBillIndexRepository.search(searchQuery);
		}
	}

	@Override
	// 定时任务更新索引库
	public void syncIndex() {
		// 查询数据库
		List<WayBill> wayBills = wayBillRepository.findAll();
		// 同步索引库
		wayBillIndexRepository.save(wayBills);
	}
}
