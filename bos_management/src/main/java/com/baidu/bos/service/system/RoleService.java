package com.baidu.bos.service.system;

import java.util.List;

import com.baidu.bos.domain.system.Role;
import com.baidu.bos.domain.system.User;

public interface RoleService {

	List<Role> findByUser(User user);

    List<Role> findAll();

    void saveRole(Role model, String[] permissionIds, String menuIds);
}
