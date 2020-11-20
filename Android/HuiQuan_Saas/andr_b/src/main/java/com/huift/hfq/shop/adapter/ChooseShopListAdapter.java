package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.pojo.StaffShop;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * 门店列表
 * @author qian.zhou
 *
 */
public class ChooseShopListAdapter extends CommonListViewAdapter<StaffShop>{

	private final static String TAG = "ChooseShopListAdapter";
	
	public ChooseShopListAdapter(Activity activity, List<StaffShop> datas) {
		super(activity, datas);
	}
	

	@Override
	public void setItems(List<StaffShop> datas) {
		super.setItems(datas);
	}
	@Override
	public void addItems(List<StaffShop> datas) {
		super.addItems(datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_choose_store_list, position);
		final StaffShop item = (StaffShop) getItem(position);
		
		((TextView) holder.getView(R.id.tv_storelist_branch)).setText(item.getShopName());//商家名字
		RelativeLayout ryChooseStore = holder.getView(R.id.ry_choose_store);
		
		/*ryChooseStore.setTag(position);
		ryChooseStore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});*/
		return holder.getConvertView();
	}
}
