package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.pojo.Campaign;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * 活动退款
 * @author wensi.yu
 *
 */
public class GetActRefundChoiseAdapter extends CommonListViewAdapter<Campaign>{
	
	private final static String TAG = "GetActRefundChoiseAdapter";

	public GetActRefundChoiseAdapter(Activity activity, List<Campaign> datas) {
		super(activity, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_campaign_refund, position);
		final Campaign item = (Campaign) getItem(position);//在数据源中获取实体类对象
		((TextView) holder.getView(R.id.tv_campaign_refund_item)).setText(item.getRefundName());//活动退款
		return  holder.getConvertView();
	}

}
