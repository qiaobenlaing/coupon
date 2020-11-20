/**
 *
 * @Author: Jianping Chen
 * @Date: 2015.5.8
 * @Version: 1.0.0
 * @Copyright Suanzi Co.,Ltd. @ 2015
 * 
 */

package com.huift.hfq.base.pojo;

public class UserRole implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/** 
	 * 主键.
	 */
	private String userRoleCode;

	/** 
	 * 用户编码.
	 */
	private String userCode;

	/** 
	 * 角色ID.
	 */
	private Integer roleId;

	/** 
	 * 备注.
	 */
	private String remark;

	public UserRole() {
	}

	public UserRole(String userRoleCode) {
		this.userRoleCode = userRoleCode;
	}

	public UserRole(String userRoleCode, String userCode, Integer roleId,
			String remark) {
		this.userRoleCode = userRoleCode;
		this.userCode = userCode;
		this.roleId = roleId;
		this.remark = remark;
	}

	/**
	 * 获取 主键. 
	 */
	public String getUserRoleCode() {
		return this.userRoleCode;
	}

	/**
	 * 设置 主键. 
	 */
	public void setUserRoleCode(String userRoleCode) {
		this.userRoleCode = userRoleCode;
	}

	/**
	 * 获取 用户编码. 
	 */
	public String getUserCode() {
		return this.userCode;
	}

	/**
	 * 设置 用户编码. 
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * 获取 角色ID. 
	 */
	public Integer getRoleId() {
		return this.roleId;
	}

	/**
	 * 设置 角色ID. 
	 */
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
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
