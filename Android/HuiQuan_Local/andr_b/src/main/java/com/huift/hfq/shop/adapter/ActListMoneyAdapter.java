package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.ActListMoneyItem;
import com.huift.hfq.base.utils.MyProgress;
import com.huift.hfq.shop.activity.MoneyReceiverActivity;
import com.huift.hfq.shop.fragment.MoneyReceiverFragment;

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
 * 营销活动中的红包列表
 */
public class ActListMoneyAdapter extends CommonListViewAdapter<ActListMoneyItem> {
	
	private final static String TAG = ActListMoneyAdapter.class.getSimpleName();
	
	public final static int STOP = 0;
	public final static String STOP_STR = "停发";
	private Activity activity;
	
	public ActListMoneyAdapter(Activity activity, List<ActListMoneyItem> datas) {
		super(activity, datas);
		this.activity = activity;
	}
	
	/*@Override
	public void setItems(List<ActListMoneyItem> datas) {
		
		super.setItems(datas);
	}
	@Override
	public void addItems(List<ActListMoneyItem> datas) {
		
		super.addItems(datas);
	}*/

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.activity_actlist_money_item, position);
		final ActListMoneyItem listMoneyItem = mDatas.get(position);//在数据源中获取实体类对象
		
		TextView tvMoneyPercentage = holder.getView(R.id.tv_moneyreceiver_receiver); // 领取的进度条的数额
		MyProgress progressBar = (MyProgress) holder.getView(R.id.progress_horizontal); // 领取的进度条
		
		//商家log
		ImageView actListMoneyImage = ((ImageView) holder.getView(R.id.iv_actmoney_image));
		Util.showImage(mActivity, listMoneyItem.getLogoUrl(), actListMoneyImage);
		
		((TextView) holder.getView(R.id.tv_money_activityCode)).setText(listMoneyItem.getBonusCode());//红包编码
		((TextView) holder.getView(R.id.tv_actlist_money_Several)).setText(listMoneyItem.getBatchNbr());//红包期数
		
		int getNum = Integer.parseInt(listMoneyItem.getGetNbr());
		if(Util.isEmpty(listMoneyItem.getGetNbr()) || getNum<=0){
			 progressBar.setMax(Integer.parseInt(listMoneyItem.getTotalVolume()));
			 progressBar.setProgress(0);
			 Log.d(TAG, "statusaa==="+listMoneyItem.getStatus());
			 if(listMoneyItem.getStatus() == STOP){
				 tvMoneyPercentage.setText("未领取 " + STOP_STR + " (0/"+ listMoneyItem.getTotalVolume() +")");
			 }else{
				 tvMoneyPercentage.setText("未领取 (0/"+ listMoneyItem.getTotalVolume() +")");
			 }
		} else {
			progressBar.setMax(Integer.parseInt(listMoneyItem.getTotalVolume()));
			progressBar.setProgress(getNum);
			Log.d(TAG, "statusaa==="+listMoneyItem.getStatus());
			if (listMoneyItem.getStatus() == STOP) { // 停发
				tvMoneyPercentage.setText("已领取"+ STOP_STR + "("+ listMoneyItem.getGetNbr() + "/"+ listMoneyItem.getTotalVolume() +")");
			} else {
				tvMoneyPercentage.setText("已领取"+ listMoneyItem.getGetPercent() + "% ("+ listMoneyItem.getGetNbr() + "/"+ listMoneyItem.getTotalVolume() +")");
			}
		}
		
	    //点击领取人数
	    final TextView moneyReceiver = holder.getView(R.id.tv_receiver_person);
	    moneyReceiver.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mActivity, MoneyReceiverActivity.class);
				intent.putExtra(MoneyReceiverFragment.newInstance().BONUS_CODE, listMoneyItem.getBonusCode());
				mActivity.startActivity(intent);
			}
		});
		
		return holder.getConvertView();
	}
}
