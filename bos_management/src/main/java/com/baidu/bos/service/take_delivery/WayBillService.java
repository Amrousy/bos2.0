package com.baidu.bos.service.take_delivery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.baidu.bos.domain.take_delivery.WayBill;

import java.util.List;

public interface WayBillService {

	// 保存运单
	public void save(WayBill model);

	// 无条件分页查询
	public Page<WayBill> pageQuery(Pageable pageable);

	// 根据运单号查询
	public WayBill findByWayBillNum(String wayBillNum);

	public Page<WayBill> findPageData(WayBill model, Pageable pageable);

	// 定时任务更新索引库
	public void syncIndex();

	// 查询所有的wayBill
    List<WayBill> findWayBills(WayBill wayBill);
}
