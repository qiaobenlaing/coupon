//---------------------------------------------------------
//@author    yanfang.li
//@version   1.0.0
//@createTime 2015.6.2
//@copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
//---------------------------------------------------------
package cn.suanzi.baomi.cust.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.adapter.CommenViewHolder;
import cn.suanzi.baomi.base.adapter.CommonListViewAdapter;
import cn.suanzi.baomi.base.pojo.BatchCoupon;
import cn.suanzi.baomi.base.utils.TimeUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.util.SkipActivityUtil;

/**
 * 优惠券失效
 * 
 * @author yanfang.li
 */
public class CouponUsedCardAdapter extends CommonListViewAdapter<BatchCoupon> {

	private final static String TAG = CouponEffectCardAdapter.class.getSimpleName();

	public CouponUsedCardAdapter(Activity activity, List<BatchCoupon> datas) {
		super(activity, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView, parent, R.layout.item_couponhistory_use,
				position);
		final BatchCoupon coupon = mDatas.get(position);
		// 商家Logo
		String logoUrl = coupon.getLogoUrl();
		ImageView ivCardImage = ((ImageView) holder.getView(R.id.iv_couponpic));
		// 商家图片显示图片
		Util.showImage(mActivity, logoUrl, ivCardImage);
		// 店铺名称
		((TextView) holder.getView(R.id.tv_shopname)).setText(coupon.getShopName());
		((TextView) holder.getView(R.id.tv_couponcount)).setText("X"+coupon.getUserCount());
		// 折
		TextView tvSymbol = holder.getView(R.id.tv_symbol);
		TextView tvMoney = holder.getView(R.id.tv_money);
		// 抵用金额
		TextView tvCouponprice = holder.getView(R.id.tv_coupon_price);
		// 优惠券类型
		TextView tvCouponDraw = holder.getView(R.id.tv_couponedraw);
		// 说明优惠券
		String canMoney = "";
		String insteadPrice = "";
		String couponType = "";
		if (CustConst.Coupon.DEDUCT.equals(coupon.getCouponType())
				|| CustConst.Coupon.REG_DEDUCT.equals(coupon.getCouponType())
				|| CustConst.Coupon.INVITE_DEDUCT.equals(coupon.getCouponType())) { // 抵扣券
			couponType = Util.getString(R.string.coupon_deduct);
			canMoney = "满" + coupon.getAvailablePrice() + "减" + coupon.getInsteadPrice() + "元";
			tvSymbol.setVisibility(View.GONE);
			tvMoney.setVisibility(View.VISIBLE);
			insteadPrice = coupon.getInsteadPrice();
			tvCouponprice.setVisibility(View.VISIBLE);
		} else if (CustConst.Coupon.DISCOUNT.equals(coupon.getCouponType())) { // 折扣券
			couponType = Util.getString(R.string.coupon_discount);
			canMoney = "满" + coupon.getAvailablePrice() + "打" + coupon.getDiscountPercent() + "折";
			insteadPrice = coupon.getDiscountPercent();
			tvSymbol.setVisibility(View.VISIBLE);
			tvMoney.setVisibility(View.GONE);
			tvCouponprice.setVisibility(View.VISIBLE);
		} else if (CustConst.Coupon.N_BUY.equals(coupon.getCouponType())) { // N元购
			couponType = Util.getString(R.string.n_buy);
			tvSymbol.setVisibility(View.GONE);
			tvMoney.setVisibility(View.VISIBLE);
			double money = Util.isEmpty(coupon.getInsteadPrice()) ? 0 : Double.parseDouble(coupon.getInsteadPrice());
			canMoney = coupon.getFunction();
			if (money > 0) {
				tvCouponprice.setVisibility(View.VISIBLE);
				tvMoney.setVisibility(View.VISIBLE);
				insteadPrice = coupon.getInsteadPrice();
			} else {
				tvCouponprice.setVisibility(View.GONE);
				tvMoney.setVisibility(View.GONE);
			}
		} else if (CustConst.Coupon.REAL_COUPON.equals(coupon.getCouponType())) {// 实物券
			canMoney = coupon.getFunction();
			tvSymbol.setVisibility(View.GONE);
			tvMoney.setVisibility(View.GONE);
			couponType = Util.getString(R.string.real_coupon);
			tvCouponprice.setVisibility(View.GONE);
		} else if (CustConst.Coupon.EXPERIENCE.equals(coupon.getCouponType())) { // 体验券
			canMoney = coupon.getFunction();
			tvSymbol.setVisibility(View.GONE);
			tvMoney.setVisibility(View.GONE);
			tvCouponprice.setVisibility(View.GONE);
			couponType = Util.getString(R.string.experience);
		} else if (CustConst.Coupon.EXCHANGE_VOUCHER.equals(coupon.getCouponType())) { // 兑换券
			couponType = Util.getString(R.string.exchange_voucher);
			tvSymbol.setVisibility(View.GONE);
			double money = Util.isEmpty(coupon.getInsteadPrice()) ? 0 : Double.parseDouble(coupon.getInsteadPrice());
			canMoney = coupon.getFunction();
			if (money > 0) {
				tvCouponprice.setVisibility(View.VISIBLE);
				tvMoney.setVisibility(View.VISIBLE);
				insteadPrice = coupon.getInsteadPrice();
			} else {
				tvCouponprice.setVisibility(View.GONE);
				tvMoney.setVisibility(View.GONE);
			}
		} else if (CustConst.Coupon.VOUCHER.equals(coupon.getCouponType())) { // 代金券
			couponType = Util.getString(R.string.voucher);
			tvSymbol.setVisibility(View.GONE);
			double payPrice = Util.isEmpty(coupon.getPayPrice()) ? 0 : Double.parseDouble(coupon.getPayPrice());
			if (payPrice > 0) {
				canMoney = "代" + coupon.getInsteadPrice();
				tvCouponprice.setVisibility(View.VISIBLE);
				tvMoney.setVisibility(View.VISIBLE);
				insteadPrice = coupon.getPayPrice();
			} else {
				canMoney = coupon.getFunction();
				tvCouponprice.setVisibility(View.GONE);
				tvMoney.setVisibility(View.GONE);
			}
		}

		// 抵用金额
		tvCouponprice.setText(insteadPrice);
		// 优惠券类型
		tvCouponDraw.setText(couponType);
		// 达到多少金额可用
		((TextView) holder.getView(R.id.tv_coupon_gd)).setText(canMoney);
		// 使用时间
		String useTime = TimeUtils.getTime(coupon);
		useTime = Util.isEmpty(useTime) ? Util.getString(R.string.day_use) : useTime;
		((TextView) holder.getView(R.id.tv_coupon_effet)).setText(useTime);
		// 距离
		((TextView) holder.getView(R.id.tv_distance)).setText(TimeUtils.getDistance(coupon.getDistance()));
		ImageView couponStatus = holder.getView(R.id.ckb_coupon_up);
		if (coupon.getStatus() == 30) { // 已使用
			couponStatus.setBackgroundResource(0);
			couponStatus.setBackgroundResource(R.drawable.available);
		} else { // 退款
			couponStatus.setBackgroundResource(0);
			couponStatus.setBackgroundResource(R.drawable.refunded);
		}
		// 进入店铺
		View view = holder.getConvertView();
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String shopName = getString(R.string.huiquan_plat);
				if (coupon.getShopName().equals(shopName)) {
					return;
				} else {
					SkipActivityUtil.skipNewShopDetailActivity(mActivity, coupon.getShopCode());
				}
			}
		});

		return view;
	}

	private String getString(int stringid) {
		return mActivity.getResources().getString(stringid);
	}
}
