/**
 *
 * @Author: Jianping Chen
 * @Date: 2015.5.8
 * @Version: 1.0.0
 * @Copyright Suanzi Co.,Ltd. @ 2015
 * 
 */

package com.huift.hfq.base.pojo;

public class Role implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/** 
	 * 主键.
	 */
	private int roleId;

	/** 
	 * 角色名称.
	 */
	private String roleName;

	/** 
	 * 角色值.
	 */
	private Integer roleValue;

	/** 
	 * 角色类型.
	 */
	private Integer roleType;

	/** 
	 * 角色描述.
	 */
	private String roleDes;

	/** 
	 * 父角色.
	 */
	private Integer parentRole;

	/** 
	 * 备注.
	 */
	private String remark;

	public Role() {
	}

	public Role(int roleId) {
		this.roleId = roleId;
	}

	public Role(int roleId, String roleName, Integer roleValue,
			Integer roleType, String roleDes, Integer parentRole, String remark) {
		this.roleId = roleId;
		this.roleName = roleName;
		this.roleValue = roleValue;
		this.roleType = roleType;
		this.roleDes = roleDes;
		this.parentRole = parentRole;
		this.remark = remark;
	}

	/**
	 * 获取 主键. 
	 */
	public int getRoleId() {
		return this.roleId;
	}

	/**
	 * 设置 主键. 
	 */
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	/**
	 * 获取 角色名称. 
	 */
	public String getRoleName() {
		return this.roleName;
	}

	/**
	 * 设置 角色名称. 
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * 获取 角色值. 
	 */
	public Integer getRoleValue() {
		return this.roleValue;
	}

	/**
	 * 设置 角色值. 
	 */
	public void setRoleValue(Integer roleValue) {
		this.roleValue = roleValue;
	}

	/**
	 * 获取 角色类型. 
	 */
	public Integer getRoleType() {
		return this.roleType;
	}

	/**
	 * 设置 角色类型. 
	 */
	public void setRoleType(Integer roleType) {
		this.roleType = roleType;
	}

	/**
	 * 获取 角色描述. 
	 */
	public String getRoleDes() {
		return this.roleDes;
	}

	/**
	 * 设置 角色描述. 
	 */
	public void setRoleDes(String roleDes) {
		this.roleDes = roleDes;
	}

	/**
	 * 获取 父角色. 
	 */
	public Integer getParentRole() {
		return this.parentRole;
	}

	/**
	 * 设置 父角色. 
	 */
	public void setParentRole(Integer parentRole) {
		this.parentRole = parentRole;
	}

	/**
	 * 获取 备注. 
	 */
	public String getRemark() {
		return this.remark;
	}

	/**
	 * 设置 备注. 
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
