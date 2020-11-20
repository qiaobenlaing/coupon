package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * couponlist 对象 
 * @author yanfang.li
 *
 */
public class Coupons implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 商店发布的优惠券有效 且可领取的
	 */
	private List<BatchCoupon> shopCoupon;
	
	/**
	 * 用户已经领取的优惠券 且可使用的
	 */
	private List<BatchCoupon> userCoupon;

	public List<BatchCoupon> getShopCoupon() {
		return shopCoupon;
	}

	public void setShopCoupon(List<BatchCoupon> shopCoupon) {
		this.shopCoupon = shopCoupon;
	}

	public List<BatchCoupon> getUserCoupon() {
		return userCoupon;
	}

	public void setUserCoupon(List<BatchCoupon> userCoupon) {
		this.userCoupon = userCoupon;
	}
	
	
}
