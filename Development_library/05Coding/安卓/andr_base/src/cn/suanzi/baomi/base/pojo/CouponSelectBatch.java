package cn.suanzi.baomi.base.pojo;

/**
 * 优惠券选择批次
 * @author yingchen
 *	
 */
public class CouponSelectBatch {
	private int insteadPrice; //减
	
	private double discountPercent; //折扣券的折扣
	
	private int availablePrice; //满
	
	private int limitedNbr; //抵扣券使用张数的限制  0--->无限制
	
	private String couponType; //优惠券的类型  3--抵扣券  4---折扣券
	
	private String batchCouponCode; //优惠券编码
	
	private String batchNbr;//优惠券批次号
	
	private String startUsingTime; //优惠券使用开始日期
	
	private String expireTime; //优惠券使用的结束日期
	
	private String dayStartUsingTime;//优惠券每天使用的开始时间
	
	private String dayEndUsingTime; //优惠券每天使用的结束时间
	
	private String function; //优惠券的描述 用途
	
	private int userCount; //拥有的优惠券的数量
	
	private String isSend; //是否 送的优惠券  1---->    0------>
	
	private int nbrPerPerson;
	
	private boolean isShowCouponCount = true;  //显示抵扣券的数量 还是 操作(增删优惠券数量)  true--->显示优惠券数量   false--->显示优惠券的操作
	
	private boolean isCheck = false;  //显示折扣券的勾选情况      false--->不勾选    true--->勾选

	public CouponSelectBatch() {
		super();
	}

	public CouponSelectBatch(int insteadPrice, double discountPercent, int availablePrice, int limitedNbr, String couponType, String batchCouponCode, String batchNbr, String startUsingTime, String expireTime, String dayStartUsingTime, String dayEndUsingTime, String function, int userCount, String isSend, int nbrPerPerson, boolean isShowCouponCount, boolean isCheck) {
		super();
		this.insteadPrice = insteadPrice;
		this.discountPercent = discountPercent;
		this.availablePrice = availablePrice;
		this.limitedNbr = limitedNbr;
		this.couponType = couponType;
		this.batchCouponCode = batchCouponCode;
		this.batchNbr = batchNbr;
		this.startUsingTime = startUsingTime;
		this.expireTime = expireTime;
		this.dayStartUsingTime = dayStartUsingTime;
		this.dayEndUsingTime = dayEndUsingTime;
		this.function = function;
		this.userCount = userCount;
		this.isSend = isSend;
		this.nbrPerPerson = nbrPerPerson;
		this.isShowCouponCount = isShowCouponCount;
		this.isCheck = isCheck;
	}

	public int getInsteadPrice() {
		return insteadPrice;
	}

	public void setInsteadPrice(int insteadPrice) {
		this.insteadPrice = insteadPrice;
	}

	public double getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(double discountPercent) {
		this.discountPercent = discountPercent;
	}

	public int getAvailablePrice() {
		return availablePrice;
	}

	public void setAvailablePrice(int availablePrice) {
		this.availablePrice = availablePrice;
	}

	public int getLimitedNbr() {
		return limitedNbr;
	}

	public void setLimitedNbr(int limitedNbr) {
		this.limitedNbr = limitedNbr;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public String getBatchCouponCode() {
		return batchCouponCode;
	}

	public void setBatchCouponCode(String batchCouponCode) {
		this.batchCouponCode = batchCouponCode;
	}

	public String getBatchNbr() {
		return batchNbr;
	}

	public void setBatchNbr(String batchNbr) {
		this.batchNbr = batchNbr;
	}

	public String getStartUsingTime() {
		return startUsingTime;
	}

	public void setStartUsingTime(String startUsingTime) {
		this.startUsingTime = startUsingTime;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	public String getDayStartUsingTime() {
		return dayStartUsingTime;
	}

	public void setDayStartUsingTime(String dayStartUsingTime) {
		this.dayStartUsingTime = dayStartUsingTime;
	}

	public String getDayEndUsingTime() {
		return dayEndUsingTime;
	}

	public void setDayEndUsingTime(String dayEndUsingTime) {
		this.dayEndUsingTime = dayEndUsingTime;
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	public boolean isShowCouponCount() {
		return isShowCouponCount;
	}

	public void setShowCouponCount(boolean isShowCouponCount) {
		this.isShowCouponCount = isShowCouponCount;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	
	public String getIsSend() {
		return isSend;
	}


	public void setIsSend(String isSend) {
		this.isSend = isSend;
	}


	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	
	public int getNbrPerPerson() {
		return nbrPerPerson;
	}

	public void setNbrPerPerson(int nbrPerPerson) {
		this.nbrPerPerson = nbrPerPerson;
	}

	@Override
	public String toString() {
		return "CouponSelectBatch [insteadPrice=" + insteadPrice + ", discountPercent=" + discountPercent + ", availablePrice=" + availablePrice + ", limitedNbr=" + limitedNbr + ", couponType=" + couponType + ", batchCouponCode=" + batchCouponCode + ", batchNbr=" + batchNbr + ", startUsingTime=" + startUsingTime + ", expireTime=" + expireTime + ", dayStartUsingTime=" + dayStartUsingTime + ", dayEndUsingTime=" + dayEndUsingTime + ", function=" + function + ", userCount=" + userCount + ", isSend=" + isSend + ", isShowCouponCount=" + isShowCouponCount + ", isCheck=" + isCheck + "]";
	}

	
}
