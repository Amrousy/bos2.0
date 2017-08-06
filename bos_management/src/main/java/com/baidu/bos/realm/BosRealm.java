package com.baidu.bos.realm;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.bos.domain.system.Permission;
import com.baidu.bos.domain.system.Role;
import com.baidu.bos.domain.system.User;
import com.baidu.bos.service.system.PermissionService;
import com.baidu.bos.service.system.RoleService;
import com.baidu.bos.service.system.UserService;

@Service("bosRealm")
public class BosRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private PermissionService permissionService;

	@Override
	// 授权
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		System.out.println("shiro 授权管理....");
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		// 根据当前登录用户查询对应角色和权限
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		// 调用业务层，查询角色
		List<Role> roles = roleService.findByUser(user);
		for (Role role : roles) {
			authorizationInfo.addRole(role.getKeyword());
		}
		// 调用业务层查询权限
		List<Permission> permissions = permissionService.findByUser(user);
		for (Permission permission : permissions) {
			authorizationInfo.addStringPermission(permission.getKeyword());
		}
		
		return authorizationInfo;
	}

	@Override
	// 认证
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("shiro 认证管理....");

		// 转化token
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;

		// 根据用户名 查询用户信息
		User user = userService.findByUsername(usernamePasswordToken.getUsername());
		if (user == null) {
			// 用户不存在
			// 参数一: 期望登录成功后保存在Subject中的信息
			// 参数二: 如果返回Null 说明用户不存在，报用户名
			// 参数三： realm名
			return null;
		} else {
			// 用户存在
			// 当返回用户密码时，securityManager安全管理器，自动比较返回密码和用户输入密码比较是否一致
			// 如果密码一直，登陆成功，如果不一致，报密码错误异常
			return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
		}
	}

}
