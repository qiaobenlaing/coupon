/**
 *
 * @Author: Jianping Chen
 * @Date: 2015.5.8
 * @Version: 1.0.0
 * @Copyright Suanzi Co.,Ltd. @ 2015
 * 
 */

package cn.suanzi.baomi.base.pojo;

public class RoleModule implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/** 
	 * 主键.
	 */
	private String roleModuleCode;

	/** 
	 * 角色Id.
	 */
	private Integer roleId;

	/** 
	 * 对应模块的主键，可能是商店的主键，可能是某个功能的主键.
	 */
	private String moduleCode;

	/** 
	 * 权限值.
	 */
	private Integer authValue;

	/** 
	 * 备注.
	 */
	private String remark;

	public RoleModule() {
	}

	public RoleModule(String roleModuleCode) {
		this.roleModuleCode = roleModuleCode;
	}

	public RoleModule(String roleModuleCode, Integer roleId, String moduleCode,
			Integer authValue, String remark) {
		this.roleModuleCode = roleModuleCode;
		this.roleId = roleId;
		this.moduleCode = moduleCode;
		this.authValue = authValue;
		this.remark = remark;
	}

	/**
	 * 获取 主键. 
	 */
	public String getRoleModuleCode() {
		return this.roleModuleCode;
	}

	/**
	 * 设置 主键. 
	 */
	public void setRoleModuleCode(String roleModuleCode) {
		this.roleModuleCode = roleModuleCode;
	}

	/**
	 * 获取 角色Id. 
	 */
	public Integer getRoleId() {
		return this.roleId;
	}

	/**
	 * 设置 角色Id. 
	 */
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	/**
	 * 获取 对应模块的主键，可能是商店的主键，可能是某个功能的主键. 
	 */
	public String getModuleCode() {
		return this.moduleCode;
	}

	/**
	 * 设置 对应模块的主键，可能是商店的主键，可能是某个功能的主键. 
	 */
	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	/**
	 * 获取 权限值. 
	 */
	public Integer getAuthValue() {
		return this.authValue;
	}

	/**
	 * 设置 权限值. 
	 */
	public void setAuthValue(Integer authValue) {
		this.authValue = authValue;
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
