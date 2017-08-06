package com.baidu.bos.service.system.impl;

import com.baidu.bos.dao.system.MenuRespository;
import com.baidu.bos.domain.system.Menu;
import com.baidu.bos.domain.system.User;
import com.baidu.bos.service.system.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Amrous on 2017/08/04.
 */
@Service
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRespository menuRespository;

    @Override
    public List<Menu> findAll() {
        return menuRespository.findAll();
    }

    @Override
    public void save(Menu menu) {
        // 防止用户没有选择父菜单
        if (menu.getParentMenu() != null && menu.getParentMenu().getId() == 0) {
            menu.setParentMenu(null);
        }
        menuRespository.save(menu);
    }

    @Override
    public List<Menu> findByUser(User user) {
        if (user.getUsername().equals("admin")) {
            return menuRespository.findAll();
        } else {
        return menuRespository.findByUser(user.getId());
        }
    }
}
