package com.baidu.bos.service.transit.impl;

import com.baidu.bos.dao.transit.DeliveryInfoRepository;
import com.baidu.bos.dao.transit.TransitInfoRepository;
import com.baidu.bos.domain.transit.DeliveryInfo;
import com.baidu.bos.domain.transit.TransitInfo;
import com.baidu.bos.service.transit.DeliveryInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Amrous on 2017/08/08.
 */
@Service
@Transactional
public class DeliveryInfoServiceImpl implements DeliveryInfoService {

    @Autowired
    private DeliveryInfoRepository deliveryInfoRepository;

    @Autowired
    private TransitInfoRepository transitInfoRepository;

    @Override
    public void save(String transitInfoId, DeliveryInfo deliveryInfo) {
        // 保存开始配送信息
        deliveryInfoRepository.save(deliveryInfo);

        // 查询运输配送对象
        TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));
        transitInfo.setDeliveryInfo(deliveryInfo);

        // 更改状态
        transitInfo.setStatus("开始配送");
    }
}
