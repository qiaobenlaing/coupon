package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.pojo.Item;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * 店员管理适配器
 * @author qian.zhou
 */
public class StaffManagerShopAdapter extends CommonListViewAdapter<Item>{
	
	public StaffManagerShopAdapter(Activity activity, List<Item> list) {
		super(activity, list);
	}
	@Override
	public void setItems(List<Item> items) {
		super.setItems(items);
	}
	@Override
	public void addItems(List<Item> items) {
		super.addItems(items);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_staff_manager_shop, position);
		final Item item = (Item) getItem(position);
		
		//初始化数据
		TextView tvManagerShop = holder.getView(R.id.tv_manager_shop);//店铺
		tvManagerShop.setText(item.getShopName());
		return holder.getConvertView();
	}
}
