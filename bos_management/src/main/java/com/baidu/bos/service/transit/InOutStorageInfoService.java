package com.baidu.bos.service.transit;

import com.baidu.bos.domain.transit.InOutStorageInfo;

/**
 * Created by Amrous on 2017/08/08.
 */
public interface InOutStorageInfoService {
    void save(String transitInfoId, InOutStorageInfo model);
}
