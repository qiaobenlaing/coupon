package cn.suanzi.baomi.base.pojo;

import java.util.List;

/**
 * 退款
 * @author liyanfang
 */
public class Refund implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6671334571670716409L;

	/**
	 * 退款的原因对于的Id
	 */
	private int id;
	
	/**
	 * 退款原因
	 */
	private String  text;
	
	/**
	 * 是否显示
	 */
	private int selected;
	
	/**
	 * 工行卡
	 */
	private double bankcardRefund;

	/**
	 * 订单编号
	 */
	private String orderNbr;

	/**
	 * 惠圈红包
	 */
	private double platBonusRefund;

	/**
	 * 商家红包
	 */
	private double shopBonusRefund;

	/**
	 * 状态 0-未付款，不可用； 1-已申请退款； 2-已退款； 3-已验证； 4-未验证，可用；
	 */
	private int status;
	
	/**
	 * 退款金额
	 */
	private double toRefundAmount;
	
	/**
	 * 热线
	 */
	private String hotLine;
	
	/**
	 * 退款原因
	 */
	private String refundExplain;
	
	/**
	 * 退款原因
	 */
	private List<Refund> sltReason;
	
	/**
	 * 备注
	 */
	private String refundRemark;
	
	/**
	 * 订单编码
	 */
	private String orderCode;
	
	/**
	 * 退款人的头像
	 */
	private String avatarUrl;
	
	/**
	 * 优惠金额
	 */
	private double deduction;
	
	/**
	 * 用户手机号
	 */
	private String mobileNbr;
	
	/**
	 * 昵称
	 */
	private String nickName;
	
	/**
	 * 消费金额
	 */
	private double orderAmount;
	
	/**
	 * 消费时间
	 */
	private String payedTime;
	
	/**
	 * 实付金额
	 */
	private double realPay;
	
	/**
	 * 退款编码
	 */
	private String refundCode;
	
	/**
	 * 退款理由
	 */
	private String refundReason;
	
	/**
	 * 退款金额
	 */
	private double refundAmount;
	
	/**
	 * 申请退款时间
	 */
	private String refundApplyTime;
	
	/**
	 * 不同意退款的理由
	 */
	private String rejectReason;
	
	/**
	 * 商家处理退款的结果
	 */
	private int handleFlag;
	
	/**
	 * 是不是当天的交易
	 */
	private int isToday;
	
	public String getRejectReason() {
		return rejectReason;
	}
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
	public int getHandleFlag() {
		return handleFlag;
	}
	public void setHandleFlag(int handleFlag) {
		this.handleFlag = handleFlag;
	}
	public int getIsToday() {
		return isToday;
	}
	public void setIsToday(int isToday) {
		this.isToday = isToday;
	}
	public double getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
	}
	public String getRefundApplyTime() {
		return refundApplyTime;
	}
	public void setRefundApplyTime(String refundApplyTime) {
		this.refundApplyTime = refundApplyTime;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public double getDeduction() {
		return deduction;
	}
	public void setDeduction(double deduction) {
		this.deduction = deduction;
	}
	public String getMobileNbr() {
		return mobileNbr;
	}
	public void setMobileNbr(String mobileNbr) {
		this.mobileNbr = mobileNbr;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public double getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getPayedTime() {
		return payedTime;
	}
	public void setPayedTime(String payedTime) {
		this.payedTime = payedTime;
	}
	public double getRealPay() {
		return realPay;
	}
	public void setRealPay(double realPay) {
		this.realPay = realPay;
	}
	public String getRefundCode() {
		return refundCode;
	}
	public void setRefundCode(String refundCode) {
		this.refundCode = refundCode;
	}
	public String getRefundReason() {
		return refundReason;
	}
	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getRefundRemark() {
		return refundRemark;
	}
	public void setRefundRemark(String refundRemark) {
		this.refundRemark = refundRemark;
	}
	public int getSelected() {
		return selected;
	}
	public void setSelected(int selected) {
		this.selected = selected;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getHotLine() {
		return hotLine;
	}
	public void setHotLine(String hotLine) {
		this.hotLine = hotLine;
	}
	public String getRefundExplain() {
		return refundExplain;
	}
	public void setRefundExplain(String refundExplain) {
		this.refundExplain = refundExplain;
	}
	public List<Refund> getSltReason() {
		return sltReason;
	}
	public void setSltReason(List<Refund> sltReason) {
		this.sltReason = sltReason;
	}
	public double getBankcardRefund() {
		return bankcardRefund;
	}
	public void setBankcardRefund(double bankcardRefund) {
		this.bankcardRefund = bankcardRefund;
	}
	public String getOrderNbr() {
		return orderNbr;
	}
	public void setOrderNbr(String orderNbr) {
		this.orderNbr = orderNbr;
	}
	public double getPlatBonusRefund() {
		return platBonusRefund;
	}
	public void setPlatBonusRefund(double platBonusRefund) {
		this.platBonusRefund = platBonusRefund;
	}
	public double getShopBonusRefund() {
		return shopBonusRefund;
	}
	public void setShopBonusRefund(double shopBonusRefund) {
		this.shopBonusRefund = shopBonusRefund;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public double getToRefundAmount() {
		return toRefundAmount;
	}
	public void setToRefundAmount(double toRefundAmount) {
		this.toRefundAmount = toRefundAmount;
	}
	
	
}
