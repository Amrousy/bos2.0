package com.baidu.bos.service.transit;

import com.baidu.bos.domain.transit.TransitInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Amrous on 2017/08/07.
 */
public interface TransitInfoService {
    void creatTransits(String wayBillIds);

    Page<TransitInfo> findPageQuery(Pageable pageable);
}
