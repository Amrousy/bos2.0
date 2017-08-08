package com.baidu.bos.dao.transit;

import com.baidu.bos.domain.transit.DeliveryInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Amrous on 2017/08/08.
 */
public interface DeliveryInfoRepository extends JpaRepository<DeliveryInfo, Integer> {
}
