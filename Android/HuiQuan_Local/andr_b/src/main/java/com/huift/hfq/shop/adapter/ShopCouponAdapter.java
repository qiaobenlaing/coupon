package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.pojo.Coupon;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import com.huift.hfq.shop.R;

public class ShopCouponAdapter extends Adapter<ShopCouponAdapter.ViewHolder> {
	private final static String TAG = ShopCouponAdapter.class.getSimpleName();
	private List<Coupon> coupons;
	private OnItemClickListener mOnItemClickLitener;

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private TextView insteadPriceTextView, timeAvailableTextView, avaiLablePriceTextView;

		public ViewHolder(View view) {
			super(view);
			insteadPriceTextView = (TextView) view.findViewById(R.id.tv_image_insteadPrice);// 抵扣金额
			timeAvailableTextView = (TextView) view.findViewById(R.id.time_available);// 有效期
			avaiLablePriceTextView = (TextView) view.findViewById(R.id.tv_availablePrice);// 满多少可以
		}
	}

	public ShopCouponAdapter(List<Coupon> coupons) {
		this.coupons = coupons;
		Log.d(TAG, "coupons1=" + coupons.size());
		for (int i = 0; i < coupons.size(); i++) {
			Coupon coupon = coupons.get(i);
			Log.d(TAG, "coupon=" + coupon.getCouponName());
			Log.d(TAG, "coupon=" + coupon.getAvailablePrice());
		}
	}

	@Override
	public int getItemCount() {
		return coupons.size();
	}

	@Override
	public void onBindViewHolder(final ViewHolder viewholder, final int arg1) {
		Coupon coupon = coupons.get(arg1);
		viewholder.insteadPriceTextView.setText(coupon.getInsteadPrice());
		viewholder.timeAvailableTextView.setText("最后领取时间" + coupon.getEndTakingTime());
		viewholder.avaiLablePriceTextView.setText("满" + coupon.getAvailablePrice() + "元可使用");
		// 如果设置了回调，则设置点击事件
		if (mOnItemClickLitener != null) {
			viewholder.itemView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mOnItemClickLitener.onItemClick(null, viewholder.itemView, arg1, viewholder.getItemId());
				}
			});

		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View v = LayoutInflater.from(arg0.getContext()).inflate(R.layout.item_myimageshop, arg0, false);
		ViewHolder viewHolder = new ViewHolder(v);
		return viewHolder;
	}

	public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
		this.mOnItemClickLitener = mOnItemClickLitener;
	}
}
