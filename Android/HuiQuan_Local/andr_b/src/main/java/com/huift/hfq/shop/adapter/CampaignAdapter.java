package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.pojo.ActListContentItem;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import com.huift.hfq.shop.R;

/**
 * 活动列表
 * @author wenis.yu
 *
 */
public class CampaignAdapter extends CommonListViewAdapter<ActListContentItem>{
	
	private final static String TAG = "CampaignAdapter";
	
	public CampaignAdapter(Activity activity, List<ActListContentItem> datas) {
		super(activity, datas);
	}

	@Override
	public void setItems(List<ActListContentItem> datas) {
		
		super.setItems(datas);
	}
	@Override
	public void addItems(List<ActListContentItem> datas) {
		
		super.addItems(datas);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_campaign_list, position);
		return holder.getConvertView();
	}

}
