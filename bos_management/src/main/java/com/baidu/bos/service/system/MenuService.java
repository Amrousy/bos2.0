package com.baidu.bos.service.system;

import com.baidu.bos.domain.system.Menu;

import java.util.List;

/**
 * Created by Amrous on 2017/08/04.
 */
public interface MenuService {
    public List<Menu> findAll();

    public void save(Menu model);
}
