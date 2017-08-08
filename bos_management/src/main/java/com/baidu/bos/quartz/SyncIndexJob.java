package com.baidu.bos.quartz;


import com.baidu.bos.service.take_delivery.WayBillService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Amrous on 2017/08/08.
 */
public class SyncIndexJob implements Job {

    @Autowired
    private WayBillService wayBillService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("定时更新索引库执行....");
        // 定时更新索引库
        wayBillService.syncIndex();
    }
}
