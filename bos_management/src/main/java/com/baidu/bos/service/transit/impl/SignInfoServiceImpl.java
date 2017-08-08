package com.baidu.bos.service.transit.impl;

import com.baidu.bos.dao.transit.SignInfoRepository;
import com.baidu.bos.dao.transit.TransitInfoRepository;
import com.baidu.bos.domain.transit.SignInfo;
import com.baidu.bos.domain.transit.TransitInfo;
import com.baidu.bos.index.WayBillIndexRepository;
import com.baidu.bos.service.transit.SignInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Amrous on 2017/08/08.
 */
@Service
@Transactional
public class SignInfoServiceImpl implements SignInfoService {

    @Autowired
    private SignInfoRepository signInfoRepository;

    @Autowired
    private TransitInfoRepository transitInfoRepository;

    @Autowired
    private WayBillIndexRepository wayBillIndexRepository;

    @Override
    public void save(String transitInfoId, SignInfo signInfo) {
        // 保存签收录入信息
        signInfoRepository.save(signInfo);

        // 关联运输流程
        TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));
        transitInfo.setSignInfo(signInfo);

        // 更改状态
        if (signInfo.getSignType().equals("正常")) {
            // 正常签收
            transitInfo.setStatus("正常签收");
            // 更改运单状态
            transitInfo.getWayBill().setSignStatus(3);
            // 更改索引库
            wayBillIndexRepository.save(transitInfo.getWayBill());
        } else {
            // 异常运单
            transitInfo.setStatus("异常");
            // 更改运单状态
            transitInfo.getWayBill().setSignStatus(4);
            // 更改索引库
            wayBillIndexRepository.save(transitInfo.getWayBill());
        }
    }
}
