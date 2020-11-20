package com.huift.hfq.shop.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huift.hfq.base.pojo.Campaign;
import com.huift.hfq.shop.R;

import java.util.List;

/**
 * 活动类型
 * @author wensi.yu
 *
 */
public class CampaignTypeAdapter extends CommonListViewAdapter<Campaign>{

	public CampaignTypeAdapter(Activity activity, List<Campaign> datas) {
		super(activity, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_campaign_type, position);
		final Campaign item = (Campaign) getItem(position);//在数据源中获取实体类对象
		((TextView) holder.getView(R.id.tv_campaigntype_name)).setText(item.getName());//活动类型
		RelativeLayout rlCampaignIcon = holder.getView(R.id.rl_camapigntype_line);
		CheckBox ckCampaign = holder.getView(R.id.ck_campaigntype_item);//活动图标
		if (item.getChecked()) {
			ckCampaign.setButtonDrawable(R.drawable.radio_yes);
			ckCampaign.setChecked(true);
		} else {
			ckCampaign.setButtonDrawable(R.drawable.radio_no);
			ckCampaign.setChecked(false);
		}
		return  holder.getConvertView();
	}

}
