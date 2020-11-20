package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.pojo.BatchCoupon;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * 店铺形象
 * @author qian.zhou
 */
public class MyImageShopAdapter extends CommonListViewAdapter<BatchCoupon>{

	public MyImageShopAdapter(Activity activity, List<BatchCoupon> datas) {
		super(activity, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mContext , convertView , parent, 
				R.layout.item_myimageshop, position);
		BatchCoupon batchCoupon  = (BatchCoupon)getItem(position);//在数据源中获取实体类对象
		((TextView) holder.getView(R.id.tv_money)).setText(batchCoupon.getInsteadPrice());
		return holder.getConvertView();
	}
}
