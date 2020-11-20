package cn.suanzi.baomi.base.pojo;
/**
 * 优惠券使用消息返回的对象
 * @author yanfang.li
 *
 */
public class CouponUse implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 使用张数
	 */
	private int couponUsed;
	
	/**
	 * 标志码
	 */
	private int identityCode;
	
	/**
	 * 抵扣金额
	 */
	private double deduction;
	
	/**
	 * 积分
	 */
	private double point;
	
	/**
	 * 消费金额
	 */
	private double totalAmount;
	
	/**
	 * 时间
	 */
	private String actionDate;

	/**
	 * 无参构造方法
	 */
	public CouponUse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 带参构造方法
	 * @param couponUsed
	 * @param identityCode
	 * @param deduction
	 * @param point
	 * @param totalAmount
	 * @param actionDate
	 */
	public CouponUse(int couponUsed, int identityCode, double deduction,
			double point, double totalAmount, String actionDate) {
		super();
		this.couponUsed = couponUsed;
		this.identityCode = identityCode;
		this.deduction = deduction;
		this.point = point;
		this.totalAmount = totalAmount;
		this.actionDate = actionDate;
	}

	public int getCouponUsed() {
		return couponUsed;
	}

	public void setCouponUsed(int couponUsed) {
		this.couponUsed = couponUsed;
	}

	public int getIdentityCode() {
		return identityCode;
	}

	public void setIdentityCode(int identityCode) {
		this.identityCode = identityCode;
	}

	public double getDeduction() {
		return deduction;
	}

	public void setDeduction(double deduction) {
		this.deduction = deduction;
	}

	public double getPoint() {
		return point;
	}

	public void setPoint(double point) {
		this.point = point;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getActionDate() {
		return actionDate;
	}

	public void setActionDate(String actionDate) {
		this.actionDate = actionDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	

}
