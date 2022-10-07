package com.mall.gateway.backend.config;

import cn.dev33.satoken.stp.StpInterface;
import com.mall.gateway.backend.service.AdminRoleService;
import com.mall.gateway.backend.service.AdminService;
import com.mall.gateway.backend.service.RoleService;
import com.mall.system.pojo.Admin;
import com.mall.system.pojo.AdminRole;
import com.mall.system.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限验证接口扩展 
 */
@Component	// 打开此注解，保证此类被springboot扫描，即可完成sa-token的自定义权限验证扩展
@RefreshScope //TODO: 服务无法启动，添加了这个动态刷新就可以了，为啥？
public class StpInterfaceImpl implements StpInterface {

	AdminService adminService;

	AdminRoleService adminRoleService;

	RoleService roleService;

	@Autowired
	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	@Autowired
	public void setAdminRoleService(AdminRoleService adminRoleService) {
		this.adminRoleService = adminRoleService;
	}

	@Autowired
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	/**
	 * 返回一个账号所拥有的权限码集合 
	 */
	@Override
	public List<String> getPermissionList(Object loginId, String loginType) {
		// 本list仅做模拟，实际项目中要根据具体业务逻辑来查询权限

		//在数据库中查询该用户对应的角色
		List<String> list = new ArrayList<String>();
		//list.add("user-add");
		//list.add("user-delete");
		//list.add("user-update");
		//list.add("user-get");
		return list;
	}

	/**
	 * 返回一个账号所拥有的角色标识集合 
	 */
	@Override
	public List<String> getRoleList(Object loginId, String loginType) {
		// 本list仅做模拟，实际项目中要根据具体业务逻辑来查询角色
		List<String> list = new ArrayList<String>();

		Admin admin = adminService.findByAdminName((String) loginId);
		Integer adminId = admin.getId();
		AdminRole adminRole1 = new AdminRole();
		adminRole1.setAdminId(adminId);

		//查询中间表
		List<AdminRole> adminRoleList = adminRoleService.findList(adminRole1);

		//依次添加该用户角色
		for (AdminRole adminRole: adminRoleList) {
			Integer roleId = adminRole.getRoleId();
			Role role = roleService.findById(roleId);
			String name = role.getName();
			list.add(name);
		}

		return list;
	}

}
