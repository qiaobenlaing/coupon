package cn.suanzi.baomi.cust.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.adapter.CommenViewHolder;
import cn.suanzi.baomi.base.adapter.CommonListViewAdapter;
import cn.suanzi.baomi.base.pojo.BatchCoupon;
import cn.suanzi.baomi.base.utils.QrCodeUtils;
import cn.suanzi.baomi.base.utils.ViewSolveUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.activity.ActThemeDetailActivity;
import cn.suanzi.baomi.cust.application.CustConst;

/**
 * 已领取得优惠券列表
 * @author yanfang.li
 */
public class UserBatchCouponAdapter extends CommonListViewAdapter<BatchCoupon> {
	
	private static final String TAG = UserBatchCouponAdapter.class.getSimpleName();
	/** 显示优惠券*/
	private static final int SHOW_BAR_CODE = 1;
	/** 11-已退款*/
	private static final int REFUNDED = 11;
	/** 12-申请退款*/
	private static final int REQUEST_REFUND = 12;
	/** 20-退款*/
	private static final int REFUND = 20;
	/** 30-不可退款*/
	private static final int NON_REFUNDABLE = 30;
	
	public UserBatchCouponAdapter(Activity activity, List<BatchCoupon> datas) {
		super(activity, datas);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView, parent,R.layout.item_batchcoupon, position);
		final BatchCoupon batchCoupon = mDatas.get(position);
		TextView tvUserCouponNbr = holder.getView(R.id.tv_user_couponnbr); // 拥有领用的券号
		TextView tvRefund = holder.getView(R.id.tv_refund); // 退款
		ImageView ivBarCode = holder.getView(R.id.iv_bar_code); // 条形码
		ImageView ivDisplay = holder.getView(R.id.iv_display); // 显示条形码
		ImageView ivHideCoupon = holder.getView(R.id.iv_hide_coupon); // 隐藏条形码
		RelativeLayout rlCouponBg = holder.getView(R.id.rl_coupon_bg); // 隐藏条形码
		ImageView ivCouponBg = holder.getView(R.id.iv_coupon_bg); // 优惠券背景
		tvUserCouponNbr.setText(batchCoupon.getUserCouponNbr());
		setCouponName(batchCoupon, ivCouponBg,tvRefund);
		// 生成条形码
		try {
			if (!Util.isEmpty(batchCoupon.getUserCouponNbr())) {
				ivBarCode.setImageBitmap(QrCodeUtils.CreateOneDCode(batchCoupon.getUserCouponNbr()));
			} else {
				Log.d(TAG, "userCouponNbr  is null" );
			}
		} catch (Exception e) {
			Log.e(TAG, "生成条形码 >>> " + e.getMessage());
		}
		// 查看条形码
		if (batchCoupon.getShowBarCode() == SHOW_BAR_CODE) {
			ivHideCoupon.setVisibility(View.GONE);
			ivBarCode.setVisibility(View.VISIBLE);
			rlCouponBg.setBackgroundResource(R.drawable.batch_bg);
			ivDisplay.setImageResource(R.drawable.batch_down);
		} else {
			ivHideCoupon.setVisibility(View.VISIBLE);
			ivBarCode.setVisibility(View.GONE);
			rlCouponBg.setBackgroundResource(0); // 不用默认图片
			ivDisplay.setImageResource(R.drawable.batch_up);
		}
		// 隐藏显示条形码
		ivDisplay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (batchCoupon.getShowBarCode() == SHOW_BAR_CODE) {
					batchCoupon.setShowBarCode(0);
				} else {
					batchCoupon.setShowBarCode(1);
				}
				notifyDataSetChanged();
			}
		});
		
		return holder.getConvertView();
	}
	
	/**
	 * 设置优惠券的名字
	 * @param tvCouponName 优惠券名字的空间
	 */
	private void setCouponName (BatchCoupon coupon ,ImageView couponBg,TextView tvRefund) {
		if (!Util.isEmpty(coupon.getCouponType())) {
			int type = ViewSolveUtils.getInputNum(coupon.getCouponType());
			switch (type) {
			case CustConst.Coupon.INT_DEDUCT: // 抵扣券
				couponBg.setImageResource(R.drawable.detail_deduct);
				tvRefund.setVisibility(View.GONE);
				break;
			case CustConst.Coupon.INT_DISCOUNT: // 折扣券
				couponBg.setImageResource(R.drawable.detail_discount);
				tvRefund.setVisibility(View.GONE);
				break;
			case CustConst.Coupon.INT_REAL_COUPON: // 实物券
				couponBg.setImageResource(R.drawable.detail_real);
				tvRefund.setVisibility(View.GONE);
				break;
			case CustConst.Coupon.INT_EXPERIENCE: // 体验券
				couponBg.setImageResource(R.drawable.detail_experience);
				tvRefund.setVisibility(View.GONE);
				break;
			case CustConst.Coupon.INT_N_BUY: // N元购
				couponBg.setImageResource(R.drawable.detail_nbuy);
				tvRefund.setVisibility(View.GONE);
				break;
			case CustConst.Coupon.INT_EXCHANGE_VOUCHER: // 兑换券
				couponBg.setImageResource(R.drawable.detail_ex_voucher);
				double money = Util.isEmpty(coupon.getInsteadPrice()) ? 0 : Double.parseDouble(coupon.getInsteadPrice());
				if (money > 0) {
					tvRefund.setVisibility(View.VISIBLE);
					setRefund(coupon, tvRefund);
				} else {
					tvRefund.setVisibility(View.GONE);
				}
				break;
			case CustConst.Coupon.INT_VOUCHER: // 代金券
				couponBg.setImageResource(R.drawable.detail_voucher);
				double payMoeny = Util.isEmpty(coupon.getPayPrice()) ? 0 : Double.parseDouble(coupon.getPayPrice());
				if (payMoeny > 0) {
					tvRefund.setVisibility(View.VISIBLE);
					setRefund(coupon, tvRefund);
				} else {
					tvRefund.setVisibility(View.GONE);
				}
				break;

			default:
				break;
			}
		} else {
			// TODO
		}
	}
	
	/**
	 * 退款
	 * @param coupon 优惠券对象
	 * @param tvRefund 退款按钮
	 */
	private void setRefund(BatchCoupon coupon ,TextView tvRefund) {
		switch (coupon.getOrderCouponStatus()) {
		case REFUNDED: // 已退款
			tvRefund.setText(Util.getString(R.string.refunded));
			tvRefund.setEnabled(true);
			coupon.setCouponRefundUrl("Browser/orderCouponRefund?orderCode=" + coupon.getOrderCode());
			refundListener(tvRefund,coupon.getCouponRefundUrl());
			break;
		case REQUEST_REFUND: // 申请退款
			tvRefund.setEnabled(true);
			tvRefund.setText(Util.getString(R.string.request_refund));
			coupon.setCouponRefundUrl("Browser/orderCouponRefunding?orderCode=" + coupon.getOrderCode());
			refundListener(tvRefund,coupon.getCouponRefundUrl());
			break;
		case REFUND: // 退款
			tvRefund.setEnabled(true);
			tvRefund.setText(Util.getString(R.string.refund));
			coupon.setCouponRefundUrl("Browser/cApplyRefund?orderCode=" + coupon.getOrderCode());
			refundListener(tvRefund,coupon.getCouponRefundUrl());
			break;
		case NON_REFUNDABLE: // 不可退款
			tvRefund.setEnabled(false);
			tvRefund.setText(Util.getString(R.string.non_refundable));
			break;
			
		default:
			tvRefund.setVisibility(View.GONE);
			break;
		}
	}
	
	private void refundListener (TextView tvRefund,final String refundUrl) {
		// 退款
		tvRefund.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mActivity, ActThemeDetailActivity.class);
				intent.putExtra(ActThemeDetailActivity.TYPE, CustConst.HactTheme.HOME_ACTIVITY);
				intent.putExtra(ActThemeDetailActivity.THEME_URL, refundUrl);
				mActivity.startActivity(intent);
			}
		});
	}
}
