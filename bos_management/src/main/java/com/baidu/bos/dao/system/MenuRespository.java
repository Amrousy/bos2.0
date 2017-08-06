package com.baidu.bos.dao.system;

import com.baidu.bos.domain.system.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Amrous on 2017/08/04.
 */
public interface MenuRespository extends JpaRepository<Menu, Integer> {

    @Query("from Menu m inner join fetch m.roles r inner join fetch r.users u where u.id = ? order by m.priority")
    List<Menu> findByUser(Integer id);
}
