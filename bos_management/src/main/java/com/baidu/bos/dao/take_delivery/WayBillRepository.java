package com.baidu.bos.dao.take_delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.baidu.bos.domain.take_delivery.Order;
import com.baidu.bos.domain.take_delivery.WayBill;

public interface WayBillRepository extends JpaRepository<WayBill, Integer> {

	WayBill findByWayBillNum(String wayBillNum);

}
