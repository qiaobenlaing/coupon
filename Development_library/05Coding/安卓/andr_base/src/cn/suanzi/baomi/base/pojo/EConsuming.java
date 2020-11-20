package cn.suanzi.baomi.base.pojo;
/**
 * E支付买单消息返回字段对应的属性
 * @author yanfang.li
 *
 */
public class EConsuming implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 消费金额
	 */
	private double totalAmount;
	
	/**
	 * 标志码
	 */
	private String identityCode;
	
	/**
	 * 优惠券使用张数
	 */
	private String couponUsed;
	
	/**
	 * 优惠券抵扣金额
	 */
	private String couponPay;
	
	/**
	 * 红包抵扣金额
	 */
	private String bonusPay;
	
	/**
	 * 消费的用户头像
	 */
	private String avatarUrl;
	
	/**
	 * 抵扣金额
	 */
	private double deduction; 
	
	/**
	 * 积分
	 */
	private int point;
	
	/**
	 * 支付金额
	 */
	private double consumeAmount;
	
	/**
	 * 时间
	 */
	private String consumeTime;
	
	/**
	 * 消费金额
	 */
	private String totalPay;
	
	/**
	 * 实际支付金额
	 */
	private String realPay;
	
	private int msgType;
	
	
	public String getCouponPay() {
		return couponPay;
	}

	public void setCouponPay(String couponPay) {
		this.couponPay = couponPay;
	}

	public String getBonusPay() {
		return bonusPay;
	}

	public void setBonusPay(String bonusPay) {
		this.bonusPay = bonusPay;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getConsumeTime() {
		return consumeTime;
	}

	public void setConsumeTime(String consumeTime) {
		this.consumeTime = consumeTime;
	}

	public String getTotalPay() {
		return totalPay;
	}

	public void setTotalPay(String totalPay) {
		this.totalPay = totalPay;
	}

	public String getRealPay() {
		return realPay;
	}

	public void setRealPay(String realPay) {
		this.realPay = realPay;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public String getCouponUsed() {
		return couponUsed;
	}

	public void setCouponUsed(String couponUsed) {
		this.couponUsed = couponUsed;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getIdentityCode() {
		return identityCode;
	}

	public void setIdentityCode(String identityCode) {
		this.identityCode = identityCode;
	}

	public double getDeduction() {
		return deduction;
	}

	public void setDeduction(double deduction) {
		this.deduction = deduction;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public double getConsumeAmount() {
		return consumeAmount;
	}

	public void setConsumeAmount(double consumeAmount) {
		this.consumeAmount = consumeAmount;
	}

}
