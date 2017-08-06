package com.baidu.bos.service.system;

import java.util.List;

import com.baidu.bos.domain.system.Permission;
import com.baidu.bos.domain.system.User;

public interface PermissionService {

	List<Permission> findByUser(User user);

    List<Permission> findAll();

    void save(Permission permission);
}
