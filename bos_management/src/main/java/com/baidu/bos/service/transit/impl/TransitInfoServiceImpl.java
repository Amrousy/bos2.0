package com.baidu.bos.service.transit.impl;

import com.baidu.bos.dao.take_delivery.WayBillRepository;
import com.baidu.bos.dao.transit.TransitInfoRepository;
import com.baidu.bos.domain.take_delivery.WayBill;
import com.baidu.bos.domain.transit.TransitInfo;
import com.baidu.bos.index.WayBillIndexRepository;
import com.baidu.bos.service.transit.TransitInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Amrous on 2017/08/07.
 */
@Service
@Transactional
public class TransitInfoServiceImpl implements TransitInfoService {

    @Autowired
    private TransitInfoRepository transitInfoRepository;

    @Autowired
    private WayBillRepository wayBillRepository;

    @Autowired
    private WayBillIndexRepository wayBillIndexRepository;

    @Override
    public void creatTransits(String wayBillIds) {
        if (StringUtils.isNotBlank(wayBillIds)) {
            for (String wayBillId : wayBillIds.split(",")) {
                WayBill wayBill = wayBillRepository.findOne(Integer.parseInt(wayBillId));
                // 判断运单是否为发货状态
                if (wayBill.getSignStatus() == 1) {
                    // 生成TransitInfo信息
                    TransitInfo transitInfo = new TransitInfo();
                    transitInfo.setWayBill(wayBill);
                    transitInfo.setStatus("出入库中转");
                    transitInfoRepository.save(transitInfo);

                    // 更改运单状态
                    wayBill.setSignStatus(2);// 派送中

                    // 同步索引库
                    wayBillIndexRepository.save(wayBill);
                }
            }
        }
    }

    @Override
    public Page<TransitInfo> findPageQuery(Pageable pageable) {
        return transitInfoRepository.findAll(pageable);
    }
}
