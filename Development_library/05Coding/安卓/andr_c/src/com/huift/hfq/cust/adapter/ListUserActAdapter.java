package com.huift.hfq.cust.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.adapter.CommenViewHolder;
import com.huift.hfq.base.adapter.CommonListViewAdapter;
import com.huift.hfq.base.pojo.ListUserAct;
import com.huift.hfq.cust.R;

/**
 * 顾客报名的活动列表
 * @author wensi.yu
 *
 */
public class ListUserActAdapter extends CommonListViewAdapter<ListUserAct>{

	private final static String TAG = "ActMyContentAdapter";
	
	private Activity activity;
	
	public ListUserActAdapter(Activity activity, List<ListUserAct> datas) {
		super(activity, datas);
		this.activity = activity;
		
	}
	
	@Override
	public void setItems(List<ListUserAct> datas) {
		
		super.setItems(datas);
	}
	@Override
	public void addItems(List<ListUserAct> datas) {
		
		super.addItems(datas);
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.fragment_actmycontent_item, position);
		final ListUserAct item = (ListUserAct) getItem(position);
		
		ImageView listUserImage = ((ImageView) holder.getView(R.id.iv_listuser_image));
		Util.showImage(mActivity, item.getActivityLogo(), listUserImage);//活动图片
		
		((TextView) holder.getView(R.id.tv_activityCode)).setText(item.getActivityCode());//活动编码
		((TextView) holder.getView(R.id.tv_listuser_activityCode)).setText(item.getUserActivityCode());//用户活动编码
		((TextView) holder.getView(R.id.tv_listuser_theam)).setText(item.getActivityName());//活动标题
		((TextView) holder.getView(R.id.tv_listuser_shopname)).setText(item.getShopName());//店铺名
		//判断距离
		if (Util.isEmpty(item.getDistance())) {
			return null;
		} else {
			String distanceSrc = item.getDistance().replace(",", "").replace(".", "");
			String distatnceSimple = "";
			try {
				int distance = Integer.parseInt(distanceSrc);
				if (distance > 1000) {
					int dist = distance / 1000;
					if (dist > 100) {
						distatnceSimple = ">100 Km" ;
					} else {
						distatnceSimple = String.valueOf(dist) +" Km";
					}
				} else {
					distatnceSimple = String.valueOf(distance) +" M";
				}
			} catch (Exception e) {
				return null;
			}
			((TextView) holder.getView(R.id.tv_listuser_distance)).setText(distatnceSimple);
		}
		
		((TextView) holder.getView(R.id.tv_listuser_enroll)).setText(item.getPersonCount() + "报名");//人数
		
		//时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		Date startDate = null;
		if (Util.isEmpty(item.getSignUpTime())) {
			((TextView) holder.getView(R.id.tv_listuser_time)).setText(0);
		} else {
			String startDatestr = item.getSignUpTime().replaceAll("-",".");
			Log.i(TAG, "startDatestr========"+startDatestr);
			try {
				startDate = sdf.parse(startDatestr);
			    Log.i(TAG, "startDate======="+startDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		((TextView) holder.getView(R.id.tv_listuser_time)).setText(sdf.format(startDate));//报名时间
		
		return holder.getConvertView();
	}

}
