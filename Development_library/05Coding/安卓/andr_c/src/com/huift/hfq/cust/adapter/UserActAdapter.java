package com.huift.hfq.cust.adapter;

import java.util.List;

import com.huift.hfq.cust.activity.ActRefundActivity;
import com.huift.hfq.cust.activity.ActThemeDetailActivity;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.fragment.ActRefundFragment;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.adapter.CommenViewHolder;
import com.huift.hfq.base.adapter.CommonListViewAdapter;
import com.huift.hfq.base.pojo.UserActivity;
import com.huift.hfq.base.utils.QrCodeUtils;
import com.huift.hfq.cust.R;

/**
 * 我报名活动的列表
 * @author yanfang.li
 */
public class UserActAdapter extends CommonListViewAdapter<UserActivity> {
	
	private static final String TAG = UserActAdapter.class.getSimpleName();
	/** 显示优惠券*/
	private static final int SHOW_BAR_CODE = 1;
	/** 1-申请退款中*/
	private static final int REQUEST_REFUND = 1;
	/** 2-已退款*/
	private static final int REFUNDED = 2;
	/** 3-不可退款*/
	private static final int NON_REFUNDABLE = 3;
	/** 4-退款*/
	private static final int REFUND = 4;
	
	public UserActAdapter(Activity activity, List<UserActivity> datas) {
		super(activity, datas);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView, parent,R.layout.item_batchcoupon, position);
		final UserActivity userActivity = mDatas.get(position);
		TextView tvActNbr = holder.getView(R.id.tv_user_couponnbr); // 拥有领用的券号
		TextView tvRefund = holder.getView(R.id.tv_refund); // 退款
		ImageView ivBarCode = holder.getView(R.id.iv_bar_code); // 条形码
		ImageView ivDisplay = holder.getView(R.id.iv_display); // 显示条形码
		ImageView ivHideAct = holder.getView(R.id.iv_hide_coupon); // 隐藏条形码
		RelativeLayout rlActBg = holder.getView(R.id.rl_coupon_bg); // 隐藏条形码
		tvActNbr.setText(userActivity.getActCode());
		// 生成条形码
		try {
			if (!Util.isEmpty(userActivity.getActCode())) {
				ivBarCode.setImageBitmap(QrCodeUtils.CreateOneDCode(userActivity.getActCode()));
			} else {
				Log.d(TAG, "userCouponNbr  is null" );
			}
		} catch (Exception e) {
			Log.e(TAG, "生成条形码 >>> " + e.getMessage());
		}
		// 查看条形码
		if (userActivity.getShowBarCode() == SHOW_BAR_CODE) {
			ivHideAct.setVisibility(View.GONE);
			ivBarCode.setVisibility(View.VISIBLE);
			rlActBg.setBackgroundResource(R.drawable.batch_bg);
			ivDisplay.setImageResource(R.drawable.batch_down);
		} else {
			ivHideAct.setVisibility(View.VISIBLE);
			ivBarCode.setVisibility(View.GONE);
			rlActBg.setBackgroundResource(0); // 不用默认图片
			ivDisplay.setImageResource(R.drawable.batch_up);
		}
		// 隐藏显示条形码
		ivDisplay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (userActivity.getShowBarCode() == SHOW_BAR_CODE) {
					userActivity.setShowBarCode(0);
				} else {
					userActivity.setShowBarCode(1);
				}
				notifyDataSetChanged();
			}
		});
		setRefund(userActivity, tvRefund);
		return holder.getConvertView();
	}
	
	/**
	 * 退款
	 * @param coupon 优惠券对象
	 * @param tvRefund 退款按钮
	 */
	private void setRefund(UserActivity userActivity ,TextView tvRefund) {
		tvRefund.setEnabled(true);
		tvRefund.setVisibility(userActivity.getTotalPayment() == 0 ? View.GONE : View.VISIBLE);
		switch (userActivity.getStatus()) {
		case REFUNDED: // 已退款
			tvRefund.setText(Util.getString(R.string.refunded));
			refundListener(tvRefund, userActivity.getActCode());
			break;
		case REQUEST_REFUND: // 申请退款
			tvRefund.setText(Util.getString(R.string.request_refund));
			refundListener(tvRefund, userActivity.getActCode());
			break;
		case REFUND: // 退款
			tvRefund.setText(Util.getString(R.string.refund));
			refundListener(tvRefund, userActivity.getActCode());
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
	
	private void refundListener (TextView tvRefund,final String actCode) {
		// 退款
		tvRefund.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mActivity, ActRefundActivity.class);
				intent.putExtra(ActRefundFragment.ACT_CODE, actCode);
				mActivity.startActivity(intent);
			}
		});
	}
}
