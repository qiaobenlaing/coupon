// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.pojo.Activitys;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.ActivityManagerActivity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * 活动信息显示的适配器
 * @author qian.zhou
 */
public class ActivityListAdapter extends CommonListViewAdapter<Activitys>{
	private final static String TAG = ActivityListAdapter.class.getSimpleName();
	
	public ActivityListAdapter(Activity activity, List<Activitys> datas) {
		super(activity, datas);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_myactivity, position);
		final Activitys activitys = mDatas.get(position);//在数据源中获取实体类对象
		TextView tvActivityName =  holder.getView(R.id.tv_activity_name);//活动名称
		RelativeLayout actList = holder.getView(R.id.rl_actlist);
		//赋值
		tvActivityName.setText(activitys.getActivityName() + activitys.getActNumber());
		
		actList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mActivity, ActivityManagerActivity.class);
				intent.putExtra(ShopConst.UppActStatus.ACT_CODE, activitys.getActivityCode());
				mActivity.startActivity(intent);
			}
		});
		return holder.getConvertView();
	}
}
