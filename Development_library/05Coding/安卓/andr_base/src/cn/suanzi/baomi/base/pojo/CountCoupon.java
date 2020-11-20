// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.base.pojo;

/**
 * 优惠券每批次统计信息
 * @author yanfang.li
 */
public class CountCoupon implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 发放优惠券的总批数
	 */
	private String nbrOfBatch;
	
	/**
	 * 共抵扣的金额
	 */
	private String nbrOfDeductionPrice;
	
	/**
	 * 带来的消费金额
	 */
	private String totalPrice;
	
	/**
	 * 带来的消费人次
	 */
	private String totalPersonAmount;
	
	/**
	 * 当前未使用的优惠券
	 */
	private String restOfCoupon;

	public String getNbrOfBatch() {
		return nbrOfBatch;
	}

	public void setNbrOfBatch(String nbrOfBatch) {
		this.nbrOfBatch = nbrOfBatch;
	}

	public String getNbrOfDeductionPrice() {
		return nbrOfDeductionPrice;
	}

	public void setNbrOfDeductionPrice(String nbrOfDeductionPrice) {
		this.nbrOfDeductionPrice = nbrOfDeductionPrice;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getTotalPersonAmount() {
		return totalPersonAmount;
	}

	public void setTotalPersonAmount(String totalPersonAmount) {
		this.totalPersonAmount = totalPersonAmount;
	}

	public String getRestOfCoupon() {
		return restOfCoupon;
	}

	public void setRestOfCoupon(String restOfCoupon) {
		this.restOfCoupon = restOfCoupon;
	}
	
	
	
}
