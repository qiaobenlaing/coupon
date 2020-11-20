package com.huift.hfq.shop.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.OrderInfo;
import com.huift.hfq.base.utils.Calculate;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.activity.BillDetailActivity;
import com.huift.hfq.shop.fragment.BillDetailFragment;

import java.util.List;

/**
 * 我的订单
 * @author yanfang.li
 */
public class MyOrderAdapter extends CommonListViewAdapter<OrderInfo> {
	
	private final static String TAG = MyOrderAdapter.class.getSimpleName();

	public MyOrderAdapter(Activity activity, List<OrderInfo> datas) {
		super(activity, datas);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView, parent, R.layout.item_myorder,position);
		final OrderInfo orderInfo = mDatas.get(position);
		// 店铺详情
		ImageView ivAvatarUrl = holder.getView(R.id.iv_avatarUrl);
		TextView tvNickName = holder.getView(R.id.tv_nickName);
		TextView tvMobileNbr = holder.getView(R.id.tv_mobileNbr);
		TextView tvOrderAmount = holder.getView(R.id.tv_orderAmount); // 消费金额
		TextView tvRealPay = holder.getView(R.id.tv_realPay); // 支付金额
		TextView tvDeduction = holder.getView(R.id.tv_deduction); // 优惠金额
		TextView tvPayedTime = holder.getView(R.id.tv_payedTime); // 支付时间
		TextView tvOrderNbr = holder.getView(R.id.tv_orderNbr); // 订单编码
		Util.showImage(mActivity, orderInfo.getAvatarUrl(), ivAvatarUrl);
		tvNickName.setText((Util.isEmpty(orderInfo.getNickName()) ? "XX" : orderInfo.getNickName()));
		tvMobileNbr.setText(Util.getString(R.string.mobileNbr) + (Util.isEmpty(orderInfo.getMobileNbr()) ? "XX" : orderInfo.getMobileNbr()));
		tvOrderAmount.setText(Util.getString(R.string.orderAmount) + (Util.isEmpty(orderInfo.getOrderAmount()) ? "0" : Calculate.subZeroAndDot(orderInfo.getOrderAmount())) + "元");
		tvRealPay.setText(Util.getString(R.string.realPay) + (Util.isEmpty(orderInfo.getRealPay()) ? "0" : Calculate.subZeroAndDot(orderInfo.getRealPay())) + "元");
		tvDeduction.setText(Util.getString(R.string.deduction) + (Util.isEmpty(orderInfo.getDeduction()) ? "0" : Calculate.subZeroAndDot(orderInfo.getDeduction())) + "元");
		tvPayedTime.setText(Util.getString(R.string.payedTime) + (Util.isEmpty(orderInfo.getPayedTime()) ? "0" : orderInfo.getPayedTime()));
		tvOrderNbr.setText(Util.getString(R.string.amout_orderNbr) + (Util.isEmpty(orderInfo.getOrderNbr()) ? "0" : orderInfo.getOrderNbr()));
		View view = holder.getConvertView();
		view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mActivity, BillDetailActivity.class);
				intent.putExtra(BillDetailFragment.ORDER_NBR, orderInfo.getConsumeCode());
				mActivity.startActivity(intent);
			}
		});
		return view;
	}
}
