package com.baidu.bos.service.system;

import com.baidu.bos.domain.system.Menu;
import com.baidu.bos.domain.system.User;

import java.util.List;

/**
 * Created by Amrous on 2017/08/04.
 */
public interface MenuService {
    public List<Menu> findAll();

    public void save(Menu model);

    List<Menu> findByUser(User user);
}
