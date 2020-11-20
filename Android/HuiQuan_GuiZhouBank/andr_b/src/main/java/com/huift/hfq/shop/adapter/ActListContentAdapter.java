package com.huift.hfq.shop.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.ActListContentItem;
import com.huift.hfq.shop.activity.ContentEnrollActivity;
import com.huift.hfq.shop.fragment.ContentEnrollFragment;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * @author wensi.yu
 * 
 * 营销活动中的活动列表
 */
public class ActListContentAdapter extends CommonListViewAdapter<ActListContentItem> {
	
	private Activity activity;
	
	
	public ActListContentAdapter(Activity activity, List<ActListContentItem> datas) {
		super(activity, datas);
		this.activity = activity;
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
		CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.activity_actlist_content_item, position);
		final ActListContentItem item = (ActListContentItem) getItem(position);//在数据源中获取实体类对象
		
		ImageView actlistCountImage = ((ImageView) holder.getView(R.id.iv_aclistcontent_image));
		Util.showImage(mActivity, item.getActivityLogo(), actlistCountImage);//显示图片
		
		((TextView) holder.getView(R.id.tv_activityCode)).setText(item.getActivityCode());//活动编码
		((TextView) holder.getView(R.id.tv_aclist_theam)).setText(item.getActivityName());//主题
		
		if(Util.isEmpty(item.getParticipators())){
			((TextView) holder.getView(R.id.tv_aclist_activitycount)).setText("0");//活动人数
		}else{
			((TextView) holder.getView(R.id.tv_aclist_activitycount)).setText(item.getParticipators());//活动人数
		}
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		Date startDate = null;
		Date endDate = null;
		if (Util.isEmpty(item.getStartTime()) || Util.isEmpty(item.getEndTime())) {
			((TextView) holder.getView(R.id.tv_aclist_to)).setText(0);
			((TextView) holder.getView(R.id.tv_aclist_back)).setText(0);
		} else {
			String startDatestr = item.getStartTime().replaceAll("-",".");
			String endDatestr = item.getEndTime().replaceAll("-",".");
			try {
				startDate = sdf.parse(startDatestr);
			    endDate = sdf.parse(endDatestr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		((TextView) holder.getView(R.id.tv_aclist_to)).setText(sdf.format(startDate));//开始活动时间
		((TextView) holder.getView(R.id.tv_aclist_back)).setText(sdf.format(endDate));//结束活动时间
		final TextView contentEnroll = holder.getView(R.id.tv_aclist_person);//活动报名人数
		
		contentEnroll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mActivity, ContentEnrollActivity.class);
				intent.putExtra(ContentEnrollFragment.newInstance().CONTENTCODE, item.getActivityCode());
				mActivity.startActivity(intent);
			}
		});
		return holder.getConvertView();
	}
}
