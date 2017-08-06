package com.baidu.bos.quartz;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.bos.service.take_delivery.PromotionService;

public class PromotionJob implements Job{

	@Autowired
	private PromotionService promotionService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("活动过期处理程序执行....");
		// 每分钟执行一次，当前时间大于Promotion数据表endDate,活动过期，设置status为2
		promotionService.updateStatus(new Date());
	}
	
}
