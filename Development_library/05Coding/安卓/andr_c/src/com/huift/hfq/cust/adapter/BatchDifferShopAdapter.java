package com.huift.hfq.cust.adapter;

import java.util.List;

import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.util.SkipActivityUtil;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.adapter.CommenViewHolder;
import com.huift.hfq.base.adapter.CommonListViewAdapter;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.cust.R;

/**
 * 批次优惠券里面的异业商家
 * @author yanfang.li
 */
public class BatchDifferShopAdapter extends CommonListViewAdapter<Shop> {
	
	public BatchDifferShopAdapter(Activity activity, List<Shop> datas) {
		super(activity, datas);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView, parent,R.layout.item_differ_shop, position);
		final Shop shop = mDatas.get(position);
		ImageView ivShopHead = holder.getView(R.id.tv_shophead); // 商家头像
		TextView tvShopName = holder.getView(R.id.tv_shopname); // 商家名称
		TextView tvPopularity = holder.getView(R.id.tv_popularity); // 人气
		TextView tvRepeat = holder.getView(R.id.tv_repeat); // 回头客
		Util.showFirstImages(mActivity, shop.getLogoUrl(), ivShopHead);
		tvShopName.setText(Util.isEmpty(shop.getShopName()) ? Util.getString(R.string.shop_name) : shop.getShopName());
		// 商家人气和回头客
		tvPopularity.setText(Util.getString(R.string.popularity) + shop.getPopularity());
		tvRepeat.setText(Util.getString(R.string.repeatcustomer) + shop.getRepeatCustomers());
		View view = holder.getConvertView();
		// 进入店铺详情
		view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SkipActivityUtil.skipNewShopDetailActivity(mActivity, shop.getShopCode());
			}
		});
		return holder.getConvertView();
	}
}
