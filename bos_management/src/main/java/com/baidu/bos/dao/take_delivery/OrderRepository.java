package com.baidu.bos.dao.take_delivery;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baidu.bos.domain.take_delivery.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{

	public Order findByOrderNum(String orderNum);

}
