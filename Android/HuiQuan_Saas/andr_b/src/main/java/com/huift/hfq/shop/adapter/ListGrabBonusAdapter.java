package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.ListGrabBonus;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * 领取红包的人员
 * @author 
 *
 */
public class ListGrabBonusAdapter extends CommonListViewAdapter<ListGrabBonus>{

	private final static String TAG = "ListGrabBonusAdapter";
	
	private Activity activity;
	
	public ListGrabBonusAdapter(Activity activity, List<ListGrabBonus> datas) {
		super(activity, datas);
		this.activity = activity;
	}
	
	@Override
	public void setItems(List<ListGrabBonus> datas) {
		
		super.setItems(datas);
	}
	@Override
	public void addItems(List<ListGrabBonus> datas) {
		
		super.addItems(datas);
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.fragment_money_receiver_item, position);
		final ListGrabBonus listGrabBonus  = (ListGrabBonus) getItem(position);
		
		//商家log
		ImageView moneyImage = ((ImageView) holder.getView(R.id.iv_receiverhead));
		Util.showImage(mActivity, listGrabBonus.getAvatarUrl(), moneyImage);
		
		((TextView) holder.getView(R.id.tv_receivername)).setText(listGrabBonus.getNickName());//领取红包的用户名
		((TextView) holder.getView(R.id.tv_receiverdate)).setText(listGrabBonus.getGetDate());//领取时间
		((TextView) holder.getView(R.id.tv_receivercontent)).setText(listGrabBonus.getValue() + "元");//领取的金额
		
		return holder.getConvertView();
	}

}
