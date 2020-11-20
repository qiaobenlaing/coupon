package cn.suanzi.baomi.base.utils;

import android.app.Activity;
import cn.suanzi.baomi.base.Const;
import cn.suanzi.baomi.base.api.Tools;
import cn.suanzi.baomi.base.pojo.BatchCoupon;

/**
 * 分享优惠券
 * @author ad
 *
 */
public class ShareCouponUtil {
	private static final String TAG = ShareCouponUtil.class.getSimpleName();
	/**
	 * 分享优惠券
	 * @param batchCoupon
	 */
	public static void shareCoupon (Activity activity, BatchCoupon coupon) {
		String describe = "";
		String title = "【" + coupon.getCity() + "】";
		if (Const.Coupon.DEDUCT.equals(coupon.getCouponType())
				|| Const.Coupon.REG_DEDUCT.equals(coupon.getCouponType())
				|| Const.Coupon.INVITE_DEDUCT.equals(coupon.getCouponType())) { // 抵扣券
			
			describe = "满" + coupon.getAvailablePrice() + "元立减" + coupon.getInsteadPrice() + "元";
			
		} else if (Const.Coupon.DISCOUNT.equals(coupon.getCouponType())) { // 折扣券
			
			describe = "满" + coupon.getAvailablePrice() + "元打" + coupon.getDiscountPercent() + "折";
			
		} else if (Const.Coupon.N_BUY.equals(coupon.getCouponType())
				|| Const.Coupon.EXPERIENCE.equals(coupon.getCouponType())
				|| Const.Coupon.REAL_COUPON.equals(coupon.getCouponType())) { // N元购  实物券  体验券
																					
			describe = coupon.getFunction();
		}
		describe = describe + ", 我分享你一张" + coupon.getShopName() + "的优惠券,到惠圈，惠生活";
		
		String filePath = Tools.getFilePath(activity) + Tools.APP_ICON;
//		filePath = "";
		String logoUrl = Const.IMG_URL + coupon.getLogoUrl();
		logoUrl = "";
		Tools.showCouponShare(activity, "BatchCoupon/share?batchCouponCode=", describe, title, coupon.getBatchCouponCode(),filePath,logoUrl);
	}
}
