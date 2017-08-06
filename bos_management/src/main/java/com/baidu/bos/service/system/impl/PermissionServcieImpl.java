package com.baidu.bos.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baidu.bos.dao.system.PermissionRepository;
import com.baidu.bos.domain.system.Permission;
import com.baidu.bos.domain.system.User;
import com.baidu.bos.service.system.PermissionService;

@Service
@Transactional
public class PermissionServcieImpl implements PermissionService {

	@Autowired
	private PermissionRepository permissionRepository;
	
	@Override
	public List<Permission> findByUser(User user) {
		if (user.getUsername().equals("admin")) {
			return permissionRepository.findAll();
		} else {
			return permissionRepository.findByUser(user.getId());
		}
	}

	@Override
	public List<Permission> findAll() {
		return permissionRepository.findAll();
	}

	@Override
	public void save(Permission permission) {
		permissionRepository.save(permission);
	}

}
