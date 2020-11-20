package com.huift.hfq.cust.adapter;

import java.util.List;

import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.util.SkipActivityUtil;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.adapter.CommenViewHolder;
import com.huift.hfq.base.adapter.CommonListViewAdapter;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.cust.R;

public class LikeShopAdapter extends CommonListViewAdapter<Shop> {
	
	private final static String TAG = "LikeShopAdapter";
	
	private List<Shop> mDatas;
	public LikeShopAdapter(Activity activity, List<Shop> datas) {
		super(activity, datas);
		mDatas = datas;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView, parent,R.layout.item_grab, position);
		final Shop shop = mDatas.get(position);
		ImageView shopHead = holder.getView(R.id.iv_shophead);
		Util.showImage(mActivity, shop.getLogoUrl() , shopHead);
		((TextView) holder.getView(R.id.tv_shopname)).setText(shop.getShopName());
		TextView tvGoTo = holder.getView(R.id.tv_gotolike);
		tvGoTo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SkipActivityUtil.skipNewShopDetailActivity(mActivity, shop.getShopCode());
			}
		});
		return holder.getConvertView();
	}

}
