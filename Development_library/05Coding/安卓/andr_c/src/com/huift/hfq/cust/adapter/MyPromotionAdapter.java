package com.huift.hfq.cust.adapter;

import java.util.List;

import com.huift.hfq.cust.util.ActUtils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Activitys;
import com.huift.hfq.cust.R;

public class MyPromotionAdapter extends BaseAdapter {
	private Context mContext;
	private List<Activitys> mData;
	private int mType;
	
	
	public MyPromotionAdapter(Context mContext, List<Activitys> mData, int mType) {
		super();
		this.mContext = mContext;
		this.mData = mData;
		this.mType = mType;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_promotion, null);
			
			holder.icon = (ImageView) convertView.findViewById(R.id.iv_promotion_icon);
			RelativeLayout.LayoutParams params = (LayoutParams) holder.icon.getLayoutParams();
			params.width = Util.getWindowWidthAndHeight(mContext)[0];
			params.height = Util.getWindowWidthAndHeight(mContext)[0]*32/75;
			holder.icon.setLayoutParams(params);
			
			holder.content = (TextView) convertView.findViewById(R.id.tv_promotion_describle);
			holder.date = (TextView) convertView.findViewById(R.id.tv_promotion_date);
			holder.status = (TextView) convertView.findViewById(R.id.tv_promotion_status);
			holder.price = (TextView) convertView.findViewById(R.id.tv_promotion_price);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		Activitys activity = mData.get(position);
		
		//活动图片
		Util.showBannnerImage((Activity)mContext,activity.getActivityImg(),holder.icon);
		
		//活动描述
		holder.content.setText(activity.getActivityName());
		
		//活动时间
		holder.date.setText(ActUtils.formatTime(activity.getStartTime()) + " - " + ActUtils.formatTime(activity.getEndTime()));
		
		//活动状态
		if(mType==1){ //进行中的活动
			holder.status.setText("已报名");
		}else { //已完成的活动 和 收藏的活动
			StringBuffer buffer = new StringBuffer();
			buffer.append("已报名");
			if(activity.getParticipators()>0){
				buffer.append(activity.getParticipators());
			}
			if(activity.getLimitedParticipators()>0){
				buffer.append("/");
				buffer.append(activity.getLimitedParticipators());
			}
			holder.status.setText(buffer.toString());
		}/*else if(mType == 3){ //收藏的活动
			holder.status.setText("");
		}*/
		
		holder.status.setVisibility(View.VISIBLE);
		//活动价格
		if(activity.getTotalPayment()==0){
			holder.price.setText("免费");
			holder.status.setVisibility(View.GONE);
		}else if(activity.getMinPrice()==activity.getTotalPayment()){
			holder.price.setText("￥"+activity.getMinPrice());
		}else{
			holder.price.setText("￥"+activity.getMinPrice()+"-"+activity.getTotalPayment());
		}
		return convertView;
	}
	
	public void setDatas(List<Activitys> list){
		mData = list;
		this.notifyDataSetChanged();
	}
	
	class ViewHolder{
		public ImageView icon;
		public TextView content;
		public TextView date;
		public TextView status;
		public TextView price;
	}

}
