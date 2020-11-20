package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Enroll;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * 报名册人数
 * @author wensi.yu
 *
 */
public class ListActParticipantAdapter extends CommonListViewAdapter<Enroll>{

	private final static String TAG = "ListActParticipantAdapter";
	
	
	public ListActParticipantAdapter(Activity activity, List<Enroll> datas) {
		super(activity, datas);
	}
	
	@Override
	public void setItems(List<Enroll> datas) {
		
		super.setItems(datas);
	}
	@Override
	public void addItems(List<Enroll> datas) {
		
		super.addItems(datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.fragment_actlist_enroll_item, position);
		Enroll enrollItem = (Enroll) getItem(position);
		
		ImageView actEnrollImage = ((ImageView) holder.getView(R.id.iv_enroll_image));
		Util.showImage(mActivity, enrollItem.getAvatarUrl(), actEnrollImage);// 商家图片显示图片
		
		((TextView) holder.getView(R.id.tv_userCode)).setText(enrollItem.getUserCode());//顾客编码
		((TextView) holder.getView(R.id.tv_enroll_ninkname)).setText(enrollItem.getNinkName());//顾客昵称
		((TextView) holder.getView(R.id.tv_enroll_time)).setText(enrollItem.getSignUpTime());//报名时间
		((TextView) holder.getView(R.id.tv_enroll_man)).setText(enrollItem.getAdultM());//男性大人人数
		((TextView) holder.getView(R.id.tv_enroll_woman)).setText(enrollItem.getAdultF());//女性大人人数
		((TextView) holder.getView(R.id.tv_enroll_childman)).setText(enrollItem.getKidM());//男性小孩人数
		((TextView) holder.getView(R.id.tv_enroll_childwoman)).setText(enrollItem.getKidF());//女性小孩人数
		((TextView) holder.getView(R.id.tv_enroll_allperson)).setText(enrollItem.getParticipantNbr());//总人数
		
		return holder.getConvertView();
	}
}
