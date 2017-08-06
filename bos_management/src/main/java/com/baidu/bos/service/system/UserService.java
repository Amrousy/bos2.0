package com.baidu.bos.service.system;

import com.baidu.bos.domain.system.User;

import java.util.List;

public interface UserService {

	User findByUsername(String username);

    List<User> findAll();

    void saveUser(User model, String[] roleIds);
}
