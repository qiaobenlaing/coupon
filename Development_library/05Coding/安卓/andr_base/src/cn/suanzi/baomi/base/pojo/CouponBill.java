package cn.suanzi.baomi.base.pojo;


public class CouponBill implements java.io.Serializable {

	/**
	 * 优惠券类型
	 */
	private String couponType;
	
	/**
	 * 消费张数
	 */
	private String usedCount;
	
	/**
	 * 共计多少元
	 */
	private String usedAmount;
	
	/**
	 * 惠圈出多少钱
	 */
	private String hqAmount;

	public CouponBill() {
		super();
	}

	public CouponBill(String couponType, String usedCount, String usedAmount, String hqAmount) {
		super();
		this.couponType = couponType;
		this.usedCount = usedCount;
		this.usedAmount = usedAmount;
		this.hqAmount = hqAmount;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public String getUsedCount() {
		return usedCount;
	}

	public void setUsedCount(String usedCount) {
		this.usedCount = usedCount;
	}

	public String getUsedAmount() {
		return usedAmount;
	}

	public void setUsedAmount(String usedAmount) {
		this.usedAmount = usedAmount;
	}

	public String getHqAmount() {
		return hqAmount;
	}

	public void setHqAmount(String hqAmount) {
		this.hqAmount = hqAmount;
	}
	
	
}
