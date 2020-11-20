/**
 *
 * @Author: Jianping Chen
 * @Date: 2015.5.8
 * @Version: 1.0.0
 * @Copyright Suanzi Co.,Ltd. @ 2015
 * 
 */

package cn.suanzi.baomi.base.pojo;

import java.util.Date;

public class CouponRule implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/** 
	 * 主键.
	 */
	private int ruleId;

	/** 
	 * 规则名称.
	 */
	private String ruleName;

	/** 
	 * 规则描述.
	 */
	private String ruleDes;

	/** 
	 * 创建者.
	 */
	private String creatorCode;

	/** 
	 * 创建时间.
	 */
	private Date createTime;

	public CouponRule() {
	}

	public CouponRule(int ruleId) {
		this.ruleId = ruleId;
	}

	public CouponRule(int ruleId, String ruleName, String ruleDes,
			String creatorCode, Date createTime) {
		this.ruleId = ruleId;
		this.ruleName = ruleName;
		this.ruleDes = ruleDes;
		this.creatorCode = creatorCode;
		this.createTime = createTime;
	}

	/**
	 * 获取 主键. 
	 */
	public int getRuleId() {
		return this.ruleId;
	}

	/**
	 * 设置 主键. 
	 */
	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}

	/**
	 * 获取 规则名称. 
	 */
	public String getRuleName() {
		return this.ruleName;
	}

	/**
	 * 设置 规则名称. 
	 */
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	/**
	 * 获取 规则描述. 
	 */
	public String getRuleDes() {
		return this.ruleDes;
	}

	/**
	 * 设置 规则描述. 
	 */
	public void setRuleDes(String ruleDes) {
		this.ruleDes = ruleDes;
	}

	/**
	 * 获取 创建者. 
	 */
	public String getCreatorCode() {
		return this.creatorCode;
	}

	/**
	 * 设置 创建者. 
	 */
	public void setCreatorCode(String creatorCode) {
		this.creatorCode = creatorCode;
	}

	/**
	 * 获取 创建时间. 
	 */
	public Date getCreateTime() {
		return this.createTime;
	}

	/**
	 * 设置 创建时间. 
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
