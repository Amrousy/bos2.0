package com.baidu.bos.service.system.impl;

import com.baidu.bos.dao.system.MenuRespository;
import com.baidu.bos.dao.system.PermissionRepository;
import com.baidu.bos.dao.system.RoleRepository;
import com.baidu.bos.domain.system.Menu;
import com.baidu.bos.domain.system.Permission;
import com.baidu.bos.domain.system.Role;
import com.baidu.bos.domain.system.User;
import com.baidu.bos.service.system.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private MenuRespository menuRespository;

    @Override
    public List<Role> findByUser(User user) {
        // 基于用户查询角色
        // admin具有所有角色
        if (user.getUsername().equals("admin")) {
            return roleRepository.findAll();
        } else {
            return roleRepository.findByUser(user.getId());
        }
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public void saveRole(Role role, String[] permissionIds, String menuIds) {
        // 保存角色信息
        roleRepository.save(role);

        // 关联权限
        if (permissionIds != null) {
            for (String permissionId : permissionIds) {
                Permission permission = permissionRepository.findOne(Integer.parseInt(permissionId));
                role.getPermissions().add(permission);
            }
        }

        // 关联菜单
        if (StringUtils.isNoneBlank(menuIds)) {
            String[] menuIdArray = menuIds.split(",");
            for (String menuId : menuIdArray) {
                Menu menu = menuRespository.findOne(Integer.parseInt(menuId));
                role.getMenus().add(menu);
            }
        }
    }

}
