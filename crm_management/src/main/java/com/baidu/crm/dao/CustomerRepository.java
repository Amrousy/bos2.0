package com.baidu.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.baidu.crm.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>, JpaSpecificationExecutor<Customer> {

	// 查询所有未关联客户列表
	public List<Customer> findByFixedAreaIdIsNull();

	// 已经关联到指定定区的客户列表
	public List<Customer> findByFixedAreaId(String fixedAreaId);

	// 将客户关联到定区上 将所有客户id 拼成字符串1，2，3
	@Query("update Customer set fixedAreaId = ? where id = ?")
	@Modifying
	public void updateFixedAreaId(String fixedAreaId, Integer id);

	// 解除关联动作
	@Query("update Customer set fixedAreaId = null where fixedAreaId = ?")
	@Modifying
	public void clearFixedAreaId(String fixedAreaId);

	public Customer findCustomerByTelephone(String telephone);

	@Query("update Customer set type = 1 where telephone = ?")
	@Modifying
	public void updateType(String telephone);

	public Customer findCustomerByTelephoneAndPassword(String telephone, String password);

	@Query("select fixedAreaId from Customer where address = ?")
	public String findFixedAreaIdByAddress(String address);

}
