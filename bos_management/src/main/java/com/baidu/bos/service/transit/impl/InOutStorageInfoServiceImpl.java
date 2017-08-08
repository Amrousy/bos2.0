package com.baidu.bos.service.transit.impl;

import com.baidu.bos.dao.transit.InOutStorageInfoRepository;
import com.baidu.bos.dao.transit.TransitInfoRepository;
import com.baidu.bos.domain.transit.InOutStorageInfo;
import com.baidu.bos.domain.transit.TransitInfo;
import com.baidu.bos.service.transit.InOutStorageInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Amrous on 2017/08/08.
 */
@Service
@Transactional
public class InOutStorageInfoServiceImpl implements InOutStorageInfoService {

    @Autowired
    private InOutStorageInfoRepository inOutStorageInfoRepository;

    @Autowired
    private TransitInfoRepository transitInfoRepository;

    @Override
    public void save(String transitInfoId, InOutStorageInfo inOutStorageInfo) {
        // 保存出入库信息
        inOutStorageInfoRepository.save(inOutStorageInfo);

        // 查询transitInfoId
        TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));

        // 关联出入库信息到运输配送对象
        transitInfo.getInOutStorageInfos().add(inOutStorageInfo);

        // 修改状态
        if (inOutStorageInfo.getOperation().equals("到达网点")) {
            transitInfo.setStatus("到达网点");
            // 更新网点地址，显示配送路径
            transitInfo.setOutletAddress(inOutStorageInfo.getAddress());
        }
    }
}
