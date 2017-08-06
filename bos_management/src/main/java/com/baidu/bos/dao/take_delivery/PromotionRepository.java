package com.baidu.bos.dao.take_delivery;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.baidu.bos.domain.take_delivery.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

	// 修改活动status
	@Query("update Promotion set status = '2' where endDate < ? and status = '1'")
	@Modifying
	public void updataStatus(Date date);

}
