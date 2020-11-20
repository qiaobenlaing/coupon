package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.pojo.Shop;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * 配送方案
 * @author qian.zhou
 */
public class DeliveryProgramAdapter extends CommonListViewAdapter<Shop>{
	
	public DeliveryProgramAdapter(Activity activity, List<Shop> list) {
		super(activity, list);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_delively, position);
		final Shop shop = mDatas.get(position);
		TextView tvDeliveLyArea = holder.getView(R.id.tv_delivery_area);//配送范围
		TextView tvRequireMoney = holder.getView(R.id.tv_require_money);//起送价
		TextView tvDeliveLyFee = holder.getView(R.id.tv_delivery_fee);//配送费
		//赋值
		tvDeliveLyArea.setText(shop.getDeliveryDistance() + "km");
		tvRequireMoney.setText(shop.getRequireMoney() + "元");
		tvDeliveLyFee.setText(shop.getDeliveryFee() + "元");
		return holder.getConvertView();
	}
}
