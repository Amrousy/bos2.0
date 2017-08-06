package com.baidu.bos.dao.system;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baidu.bos.domain.system.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findByUsername(String username);

}
