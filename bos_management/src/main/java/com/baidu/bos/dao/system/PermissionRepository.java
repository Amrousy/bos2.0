package com.baidu.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.baidu.bos.domain.system.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
	
	@Query(value = "from Permission p inner join fetch p.roles r inner join fetch r.users u where u.id = ?")
	@Modifying
	List<Permission> findByUser(Integer id);

}
