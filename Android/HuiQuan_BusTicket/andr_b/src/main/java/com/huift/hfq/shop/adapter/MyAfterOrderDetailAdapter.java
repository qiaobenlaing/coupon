package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.pojo.ActListContentItem;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * 订单详情--餐后支付
 * @author ad
 *
 */
public class MyAfterOrderDetailAdapter extends CommonListViewAdapter<ActListContentItem> {

	public MyAfterOrderDetailAdapter(Activity activity, List<ActListContentItem> datas) {
		super(activity, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.fragment_myafterorderdetail_item, position);
		final ActListContentItem item = (ActListContentItem) getItem(position);//在数据源中获取实体类对象
		((TextView) holder.getView(R.id.tv_activityCode)).setText(item.getActivityCode());//活动编码
		((TextView) holder.getView(R.id.tv_aclist_theam)).setText(item.getActivityName());//主题
		
		return holder.getConvertView();
	}

}
