//---------------------------------------------------------
//@version   1.0.0
//@createTime 2015.6.2
//@copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
//---------------------------------------------------------
package com.huift.hfq.cust.adapter;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.adapter.CommenViewHolder;
import com.huift.hfq.base.adapter.CommonListViewAdapter;
import com.huift.hfq.base.pojo.BatchCoupon;
import com.huift.hfq.base.utils.ShareCouponUtil;
import com.huift.hfq.base.utils.TimeUtils;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.activity.BatchCouponDetailActivity;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.fragment.BatchCouponDetailFragment;
import com.umeng.analytics.MobclickAgent;

/**
 * 优惠券有效
 * @author yanfang.li
 */
public class CouponEffectCardAdapter extends CommonListViewAdapter<BatchCoupon> {

	private final static String TAG = CouponEffectCardAdapter.class.getSimpleName();

	public CouponEffectCardAdapter(Activity activity, List<BatchCoupon> datas) {
		super(activity, datas);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView, parent, R.layout.item_couponeffect,position);
		final BatchCoupon coupon = mDatas.get(position);
		// 商家Logo
		ImageView ivCardImage = ((ImageView) holder.getView(R.id.iv_couponpic));
		Util.showImage(mActivity, coupon.getLogoUrl(), ivCardImage);// 商家图片显示图片
		// 店铺名称
		((TextView) holder.getView(R.id.tv_shopname)).setText(coupon.getShopName());
		((TextView) holder.getView(R.id.tv_couponcount)).setText("X"+coupon.getUserCount());

		// 折
		TextView tvSymbol = holder.getView(R.id.tv_symbol);
		TextView tvMoney = holder.getView(R.id.tv_money);
		TextView tvCouponNbr = holder.getView(R.id.tv_coupon_nbr); // 券号
		TextView tvCouponEffet = holder.getView(R.id.tv_coupon_effet); // 有效期
		TextView tvCouponDate = holder.getView(R.id.tv_coupon_effet2);
		TextView tvInsteadPrice = holder.getView(R.id.tv_coupon_price);
		LinearLayout tvRight = holder.getView(R.id.right_content);
		ImageView ivShare = holder.getView(R.id.iv_share); // 分享
		tvCouponNbr.setText(Util.getString(R.string.tv_batch_nbr) + coupon.getBatchNbr());
		// 说明优惠券
		String canMoney = "";
		String insteadPrice = "";
		String couponType = "";
		if (CustConst.Coupon.DEDUCT.equals(coupon.getCouponType())
				|| CustConst.Coupon.REG_DEDUCT.equals(coupon.getCouponType())
				|| CustConst.Coupon.INVITE_DEDUCT.equals(coupon.getCouponType())) { // 抵扣券
			if (CustConst.Coupon.REG_DEDUCT.equals(coupon.getCouponType())
					|| CustConst.Coupon.INVITE_DEDUCT.equals(coupon.getCouponType())) {
				ivShare.setVisibility(View.GONE);
			} else {
				ivShare.setVisibility(View.VISIBLE);
			}
			couponType = Util.getString(R.string.coupon_deduct);
			canMoney = "满" + coupon.getAvailablePrice() + "减" + coupon.getInsteadPrice() + "元";
			tvSymbol.setVisibility(View.GONE);
			tvMoney.setVisibility(View.VISIBLE);
			tvRight.setBackgroundResource(R.drawable.full_send);
			insteadPrice = coupon.getInsteadPrice();
			tvInsteadPrice.setVisibility(View.VISIBLE);
		} else if (CustConst.Coupon.DISCOUNT.equals(coupon.getCouponType())) { // 折扣券
			couponType = Util.getString(R.string.coupon_discount);
			canMoney = "满" + coupon.getAvailablePrice() + "打" + coupon.getDiscountPercent() + "折";
			tvRight.setBackgroundResource(R.drawable.full_cut);
			insteadPrice = coupon.getDiscountPercent();
			tvSymbol.setVisibility(View.VISIBLE);
			tvMoney.setVisibility(View.GONE);
			ivShare.setVisibility(View.VISIBLE);
			tvInsteadPrice.setVisibility(View.VISIBLE);
		} else if (CustConst.Coupon.N_BUY.equals(coupon.getCouponType())) { // N元购
			couponType = Util.getString(R.string.n_buy);
			tvRight.setBackgroundResource(R.drawable.n_buy);
			tvSymbol.setVisibility(View.GONE);
			tvMoney.setVisibility(View.VISIBLE);
			ivShare.setVisibility(View.VISIBLE);
			double money = Util.isEmpty(coupon.getInsteadPrice()) ? 0 : Double.parseDouble(coupon.getInsteadPrice());
			canMoney = coupon.getFunction();
			if (money > 0) {
				tvInsteadPrice.setVisibility(View.VISIBLE);
				tvMoney.setVisibility(View.VISIBLE);
				insteadPrice = coupon.getInsteadPrice();
			} else {
				tvInsteadPrice.setVisibility(View.GONE);
				tvMoney.setVisibility(View.GONE);
			}
		} else if (CustConst.Coupon.REAL_COUPON.equals(coupon.getCouponType())) {// 实物券
			canMoney = coupon.getFunction();
			tvSymbol.setVisibility(View.GONE);
			tvMoney.setVisibility(View.GONE);
			ivShare.setVisibility(View.VISIBLE);
			tvRight.setBackgroundResource(R.drawable.real_coupon);
			couponType = Util.getString(R.string.real_coupon);
			tvInsteadPrice.setVisibility(View.GONE);
		} else if (CustConst.Coupon.EXPERIENCE.equals(coupon.getCouponType())) { // 体验券
			canMoney = coupon.getFunction();
			tvSymbol.setVisibility(View.GONE);
			tvMoney.setVisibility(View.GONE);
			ivShare.setVisibility(View.VISIBLE);
			tvRight.setBackgroundResource(R.drawable.experience_coupon);
			tvInsteadPrice.setVisibility(View.GONE);
			couponType = Util.getString(R.string.experience);
		} else if (CustConst.Coupon.EXCHANGE_VOUCHER.equals(coupon.getCouponType())) { // 兑换券
			couponType = Util.getString(R.string.exchange_voucher);
			tvSymbol.setVisibility(View.GONE);
			tvRight.setBackgroundResource(R.drawable.limmit_buy);
			double money = Util.isEmpty(coupon.getInsteadPrice()) ? 0 : Double.parseDouble(coupon.getInsteadPrice());
			canMoney = coupon.getFunction();
			if (money > 0) {
				tvInsteadPrice.setVisibility(View.VISIBLE);
				tvMoney.setVisibility(View.VISIBLE);
				ivShare.setVisibility(View.GONE);
				insteadPrice = coupon.getInsteadPrice();
			} else {
				tvInsteadPrice.setVisibility(View.GONE);
				tvMoney.setVisibility(View.GONE);
				ivShare.setVisibility(View.VISIBLE);
			}
		} else if (CustConst.Coupon.VOUCHER.equals(coupon.getCouponType())) { // 代金券
			couponType = Util.getString(R.string.voucher);
			tvSymbol.setVisibility(View.GONE);
			tvRight.setBackgroundResource(R.drawable.voucher);
			double payPrice = Util.isEmpty(coupon.getPayPrice()) ? 0 : Double.parseDouble(coupon.getPayPrice());
			if (payPrice > 0) {
				canMoney = "代" + coupon.getInsteadPrice();
				tvInsteadPrice.setVisibility(View.VISIBLE);
				tvMoney.setVisibility(View.VISIBLE);
				ivShare.setVisibility(View.GONE);
				insteadPrice = coupon.getPayPrice();
			} else {
				canMoney = coupon.getFunction();
				tvInsteadPrice.setVisibility(View.GONE);
				tvMoney.setVisibility(View.GONE);
				ivShare.setVisibility(View.VISIBLE);
			}
		} 
		// 抵用金额
		tvInsteadPrice.setText(insteadPrice);
		// 达到多少金额可用
		((TextView) holder.getView(R.id.tv_coupon_gd)).setText(canMoney);
		// 优惠券类型
		((TextView) holder.getView(R.id.tv_couponedraw)).setText(couponType);
		// 有效时间
		String useTime = TimeUtils.getTime(coupon);
		useTime = Util.isEmpty(useTime) ? Util.getString(R.string.no_limit_time) : useTime;
		tvCouponDate.setText(useTime);
		// 使用时间
		String useDate = TimeUtils.getDate(coupon);
		useDate = Util.isEmpty(useDate) ? Util.getString(R.string.day_use) : useDate;
		tvCouponEffet.setText(Util.getString(R.string.use_coupon_time) + useDate);
		// 使用说明
		((TextView) holder.getView(R.id.tv_coupon_use1)).setText(Util.isEmpty(coupon.getRemark()) ? Util.getString(R.string.no_remark) :coupon.getRemark());
		// 距离
		((TextView)holder.getView(R.id.tv_distance)).setText(TimeUtils.getDistance(coupon.getDistance()));
		// 查看批次优惠券
		View view = holder.getConvertView();
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (CustConst.Coupon.REG_DEDUCT.equals(coupon.getCouponType())
						|| CustConst.Coupon.INVITE_DEDUCT.equals(coupon.getCouponType())) {
					return;
				} else {
					Intent intent = new Intent(mActivity, BatchCouponDetailActivity.class);
					intent.putExtra(BatchCouponDetailFragment.BATCH_COUPON_CODE,coupon.getBatchCouponCode());
					intent.putExtra(BatchCouponDetailFragment.BATCH_COUPON_TYPE,coupon.getCouponType());
					mActivity.startActivity(intent);
				     // 进入店铺
//					SkipActivityUtil.skipH5ShopDetailActivity(mActivity, coupon.getShopCode(),CustConst.HactTheme.H5SHOP_DETAIL);
				}
			}
		});
		
		// 分享
		ivShare.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "getBatchCouponCode=" + coupon.getBatchCouponCode());
				// 友盟统计
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("shopcode", coupon.getShopCode());
				map.put("shopName", coupon.getShopName());
				MobclickAgent.onEvent(mActivity, "coupon_share", map);
				ShareCouponUtil.shareCoupon(mActivity, coupon); // 分享
			}
		});

		final ImageView ivMoreShow = holder.getView(R.id.iv_coupon_up);
		final RelativeLayout rlMoreShow = holder.getView(R.id.hideOrShow);
		final Integer selectFlag = coupon.getShowMore();
		LinearLayout lyArrow = holder.getView(R.id.ly_arrow);
		// 显示更多
		if (coupon.getShowMore() == 0) {
			rlMoreShow.setVisibility(View.GONE);
			ivMoreShow.setImageResource(R.drawable.arrow_down);
		} else {
			rlMoreShow.setVisibility(View.VISIBLE);
			ivMoreShow.setImageResource(R.drawable.arrow_up);
		}

		lyArrow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (selectFlag == null || selectFlag == 0) {
					coupon.setShowMore(1);
					ivMoreShow.setImageResource(R.drawable.arrow_up);
				} else {
					coupon.setShowMore(0);
					ivMoreShow.setImageResource(R.drawable.arrow_down);
				}
				notifyDataSetChanged();
			}
		});
		return view;
	}

}
