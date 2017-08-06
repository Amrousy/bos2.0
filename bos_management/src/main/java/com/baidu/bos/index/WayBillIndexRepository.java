package com.baidu.bos.index;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.baidu.bos.domain.take_delivery.WayBill;

public interface WayBillIndexRepository extends ElasticsearchRepository<WayBill, Integer>{

}
