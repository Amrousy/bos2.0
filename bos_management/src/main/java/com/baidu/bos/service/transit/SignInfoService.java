package com.baidu.bos.service.transit;

import com.baidu.bos.domain.transit.SignInfo;

/**
 * Created by Amrous on 2017/08/08.
 */
public interface SignInfoService {
    void save(String transitInfoId, SignInfo model);
}
