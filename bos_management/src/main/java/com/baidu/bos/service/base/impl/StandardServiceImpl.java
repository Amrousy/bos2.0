package com.baidu.bos.service.base.impl;

import com.baidu.bos.dao.base.StandardRepository;
import com.baidu.bos.domain.base.Standard;
import com.baidu.bos.service.base.StandardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StandardServiceImpl implements StandardService {

    @Autowired
    private StandardRepository standardRepository;

    @Override
    @CacheEvict(value = "standard", allEntries = true)
    public void save(Standard standard) {
        standardRepository.save(standard);
    }

    @Override
    @Cacheable(value = "standard", key = "#pageable.pageNumber+'_'+#pageable.pageSize")
    public Page<Standard> findPageData(Pageable pageable) {
        return standardRepository.findAll(pageable);
    }

    @Override
    @Cacheable("standard")
    public List<Standard> finAll() {
        return standardRepository.findAll();
    }

}
