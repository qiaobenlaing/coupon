package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.MyOrderItem;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * 订单管理(未完成)
 * @author qian.zhou
 */
public class MyStoreOrderTitleAdapter extends CommonListViewAdapter<MyOrderItem>{
	
	public MyStoreOrderTitleAdapter(Activity activity, List<MyOrderItem> list) {
		super(activity, list);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_storeorder_title, position);
		final MyOrderItem myOrderItem = (MyOrderItem) getItem(position);
		LinearLayout lyOrderContent = holder.getView(R.id.ly_order_content);
		if(Integer.parseInt(myOrderItem.getCount()) == 0){
			lyOrderContent.setVisibility(View.GONE);
		} else{
			lyOrderContent.setVisibility(View.VISIBLE);
			TextView tvTitle = holder.getView(R.id.tv_storeorder_title);//订单类别
			tvTitle.setText(myOrderItem.getTitle() + "( " + myOrderItem.getCount() + " )");
		}
		ListView lvOrderContent = holder.getView(R.id.lv_store_order_content);//订单的具体内容
		MyStoreOrderContentAdapter contentAdapter = new MyStoreOrderContentAdapter(mActivity, myOrderItem.getOrderList());
		lvOrderContent.setAdapter(contentAdapter);
		Util.setListViewHeight(lvOrderContent);
		return holder.getConvertView();
	}
}
