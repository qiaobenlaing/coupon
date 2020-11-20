package com.huift.hfq.cust.adapter;

import java.util.List;

import com.huift.hfq.cust.activity.ActIcBcDetailActivity;
import com.huift.hfq.cust.fragment.ActIcBcDetailFragment;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.huift.hfq.base.adapter.CommenViewHolder;
import com.huift.hfq.base.adapter.CommonListViewAdapter;
import com.huift.hfq.base.pojo.Activitys;
import com.huift.hfq.cust.R;

/**
 * 商店详情
 * @author ad
 *
 */
public class ShopActAdapter extends CommonListViewAdapter<Activitys> {
	private final static String TAG = ShopActAdapter.class.getSimpleName();
	
	public ShopActAdapter(Activity activity, List<Activitys> datas) {
		super(activity, datas);
	}

	@Override
	public void setItems(List<Activitys> shops) {
		super.setItems(shops);
	}
	@Override
	public void addItems(List<Activitys> shops) {
		super.addItems(shops);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView, parent,R.layout.item_shop_act, position);
		final Activitys act = mDatas.get(position);
		final TextView tvShopName = holder.getView(R.id.tv_shopact);
		tvShopName.setText(act.getActivityName() + act.getActNumber());
		View view = holder.getConvertView();
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String typeflag = "0"; 
				Intent intent = new Intent(mActivity, ActIcBcDetailActivity.class);
				intent.putExtra(ActIcBcDetailFragment.ACTIVITY_CODE, act.getActivityCode());
				intent.putExtra(ActIcBcDetailFragment.TYPE, typeflag);
				mActivity.startActivity(intent);
			}
		});
		return view;
	}
}
