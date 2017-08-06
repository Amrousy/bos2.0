package com.baidu.bos.service.take_delivery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.baidu.bos.domain.take_delivery.WayBill;

public interface WayBillService {

	// 保存运单
	public void save(WayBill model);

	// 无条件分页查询
	public Page<WayBill> pageQuery(Pageable pageable);

	// 根据运单号查询
	public WayBill findByWayBillNum(String wayBillNum);

	public Page<WayBill> findPageData(WayBill model, Pageable pageable);

}
