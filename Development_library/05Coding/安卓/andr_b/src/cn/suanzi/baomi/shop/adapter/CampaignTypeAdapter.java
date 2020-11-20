package cn.suanzi.baomi.shop.adapter;

import java.util.List;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.adapter.CommenViewHolder;
import cn.suanzi.baomi.base.adapter.CommonListViewAdapter;
import cn.suanzi.baomi.base.pojo.Campaign;
import cn.suanzi.baomi.shop.R;

/**
 * 活动类型
 * @author wensi.yu
 *
 */
public class CampaignTypeAdapter extends CommonListViewAdapter<Campaign>{
	
	private final static String TAG = "CampaignTypeAdapter";

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
			Log.d(TAG, "checked ----11");
			ckCampaign.setButtonDrawable(R.drawable.radio_yes);
			ckCampaign.setChecked(true);
		} else {
			Log.d(TAG, "checked ----22");
			ckCampaign.setButtonDrawable(R.drawable.radio_no);
			ckCampaign.setChecked(false);
		}
		return  holder.getConvertView();
	}

}
