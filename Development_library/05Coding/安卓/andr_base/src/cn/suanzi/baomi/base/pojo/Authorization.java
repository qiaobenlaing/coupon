/**
 *
 * @Author: Jianping Chen
 * @Date: 2015.5.8
 * @Version: 1.0.0
 * @Copyright Suanzi Co.,Ltd. @ 2015
 * 
 */

package cn.suanzi.baomi.base.pojo;

public class Authorization implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/** 
	 * 主键.
	 */
	private int authorizationId;

	/** 
	 * 权限名称.
	 */
	private String authName;

	/** 
	 * 权限值.
	 */
	private Integer authValue;

	/** 
	 * 权限描述.
	 */
	private String authDes;

	/** 
	 * 备注，权限为二进制值，如1=0001表示删除.
	 */
	private String remark;

	public Authorization() {
	}

	public Authorization(int authorizationId) {
		this.authorizationId = authorizationId;
	}

	public Authorization(int authorizationId, String authName,
			Integer authValue, String authDes, String remark) {
		this.authorizationId = authorizationId;
		this.authName = authName;
		this.authValue = authValue;
		this.authDes = authDes;
		this.remark = remark;
	}

	/**
	 * 获取 主键. 
	 */
	public int getAuthorizationId() {
		return this.authorizationId;
	}

	/**
	 * 设置 主键. 
	 */
	public void setAuthorizationId(int authorizationId) {
		this.authorizationId = authorizationId;
	}

	/**
	 * 获取 权限名称. 
	 */
	public String getAuthName() {
		return this.authName;
	}

	/**
	 * 设置 权限名称. 
	 */
	public void setAuthName(String authName) {
		this.authName = authName;
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
	 * 获取 权限描述. 
	 */
	public String getAuthDes() {
		return this.authDes;
	}

	/**
	 * 设置 权限描述. 
	 */
	public void setAuthDes(String authDes) {
		this.authDes = authDes;
	}

	/**
	 * 获取 备注，权限为二进制值，如1=0001表示删除. 
	 */
	public String getRemark() {
		return this.remark;
	}

	/**
	 * 设置 备注，权限为二进制值，如1=0001表示删除. 
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
