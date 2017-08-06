package com.baidu.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baidu.bos.domain.base.TakeTime;

public interface TakeTimeRepository extends JpaRepository<TakeTime, Integer> {

}
