package cn.suanzi.baomi.shop.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import cn.suanzi.baomi.base.pojo.ActListContentItem;
import cn.suanzi.baomi.shop.R;

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
