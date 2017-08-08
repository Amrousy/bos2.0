package com.baidu.bos.service.transit;

import com.baidu.bos.domain.transit.DeliveryInfo;

/**
 * Created by Amrous on 2017/08/08.
 */
public interface DeliveryInfoService {
    void save(String transitinfoId, DeliveryInfo model);
}
