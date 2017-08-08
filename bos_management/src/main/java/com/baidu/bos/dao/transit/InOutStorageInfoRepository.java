package com.baidu.bos.dao.transit;

import com.baidu.bos.domain.transit.InOutStorageInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Amrous on 2017/08/08.
 */
public interface InOutStorageInfoRepository extends JpaRepository<InOutStorageInfo, Integer> {
}
