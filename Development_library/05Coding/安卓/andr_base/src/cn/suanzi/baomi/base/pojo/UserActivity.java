/**
 *
 * @Author: Jianping Chen
 * @Date: 2015.5.8
 * @Version: 1.0.0
 * @Copyright Suanzi Co.,Ltd. @ 2015
 * 
 */

package cn.suanzi.baomi.base.pojo;

/**
 * 用户拥有的活动
 * @author liyanfang
 *
 */
public class UserActivity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/** 
	 * 主键.
	 */
	private String userActivityCode;

	/** 
	 * 活动编码.
	 */
	private String activityCode;

	/** 
	 * 参与用户编码.
	 */
	private String userCode;

	/** 
	 * 预付款金额，单位分.
	 */
	private double prePayment;

	/** 
	 * 总付款.
	 */
	private double totalPayment;

	/** 
	 * 预付款时间.
	 */
	private String prepayTime;

	/** 
	 * 最后付款时间.
	 */
	private String finalPayTime;
	
	/**
	 * 订单编码
	 */
	private String orderCode;
	
	/**
	 * 申请退款时间
	 */
	private String applyRefundTime;
	
	/**
	 * 退款状态
	 */
	private int status;
	
	/**
	 * 退款原因
	 */
	private String refundReason;
	
	/**
	 * 退款备注
	 */
	private String refundRemark;
	
	/**
	 * 退款时间
	 */
	private String refundTime;
	
	/**
	 * 购买活动的编码
	 */
	private String actCode;
	
	/**
	 * 条形码是否显示
	 */
	private int showBarCode;
	
	public UserActivity() {
	}

	public UserActivity(String userActivityCode) {
		this.userActivityCode = userActivityCode;
	}

	public UserActivity(String userActivityCode, String activityCode,
			String userCode, double prePayment, double totalPayment,
			String prepayTime, String finalPayTime) {
		this.userActivityCode = userActivityCode;
		this.activityCode = activityCode;
		this.userCode = userCode;
		this.prePayment = prePayment;
		this.totalPayment = totalPayment;
		this.prepayTime = prepayTime;
		this.finalPayTime = finalPayTime;
	}

	public int getShowBarCode() {
		return showBarCode;
	}

	public void setShowBarCode(int showBarCode) {
		this.showBarCode = showBarCode;
	}

	/**
	 * 获取 主键. 
	 */
	public String getUserActivityCode() {
		return this.userActivityCode;
	}

	/**
	 * 设置 主键. 
	 */
	public void setUserActivityCode(String userActivityCode) {
		this.userActivityCode = userActivityCode;
	}

	/**
	 * 获取 活动编码. 
	 */
	public String getActivityCode() {
		return this.activityCode;
	}

	/**
	 * 设置 活动编码. 
	 */
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	/**
	 * 获取 参与用户编码. 
	 */
	public String getUserCode() {
		return this.userCode;
	}

	/**
	 * 设置 参与用户编码. 
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * 获取 预付款金额，单位分. 
	 */
	public double getPrePayment() {
		return this.prePayment;
	}

	/**
	 * 设置 预付款金额，单位分. 
	 */
	public void setPrePayment(double prePayment) {
		this.prePayment = prePayment;
	}

	/**
	 * 获取 总付款. 
	 */
	public double getTotalPayment() {
		return this.totalPayment;
	}

	/**
	 * 设置 总付款. 
	 */
	public void setTotalPayment(double totalPayment) {
		this.totalPayment = totalPayment;
	}

	/**
	 * 获取 预付款时间. 
	 */
	public String getPrepayTime() {
		return this.prepayTime;
	}

	/**
	 * 设置 预付款时间. 
	 */
	public void setPrepayTime(String prepayTime) {
		this.prepayTime = prepayTime;
	}

	/**
	 * 获取 最后付款时间. 
	 */
	public String getFinalPayTime() {
		return this.finalPayTime;
	}

	/**
	 * 设置 最后付款时间. 
	 */
	public void setFinalPayTime(String finalPayTime) {
		this.finalPayTime = finalPayTime;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getApplyRefundTime() {
		return applyRefundTime;
	}

	public void setApplyRefundTime(String applyRefundTime) {
		this.applyRefundTime = applyRefundTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRefundReason() {
		return refundReason;
	}

	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}

	public String getRefundRemark() {
		return refundRemark;
	}

	public void setRefundRemark(String refundRemark) {
		this.refundRemark = refundRemark;
	}

	public String getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(String refundTime) {
		this.refundTime = refundTime;
	}

	public String getActCode() {
		return actCode;
	}

	public void setActCode(String actCode) {
		this.actCode = actCode;
	}
	
	

}
