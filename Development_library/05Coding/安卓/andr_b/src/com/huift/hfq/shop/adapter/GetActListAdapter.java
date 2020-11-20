package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Campaign;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * @author wensi.yu
 * 活动列表
 */
public class GetActListAdapter extends CommonListViewAdapter<Campaign> {
	
	private final static String TAG = "ActListContentAdapter";
	private final static double NUM_ZERO = 0.00;
	
	private Activity activity;
	
	
	public GetActListAdapter(Activity activity, List<Campaign> datas) {
		super(activity, datas);
		this.activity = activity;
	}
	
	@Override
	public void setItems(List<Campaign> datas) {
		
		super.setItems(datas);
	}
	@Override
	public void addItems(List<Campaign> datas) {
		
		super.addItems(datas);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_campaign_list, position);
		final Campaign item = (Campaign) getItem(position);//在数据源中获取实体类对象
		
		ImageView actlistCountImage = ((ImageView) holder.getView(R.id.tv_campaign_image));
		Util.showImage(mActivity, item.getActivityImg(), actlistCountImage);//显示活动图片
		RelativeLayout.LayoutParams image = (LayoutParams) actlistCountImage.getLayoutParams();
		image.width = Util.getWindowWidthAndHeight(mActivity)[0];
		image.height = Util.getWindowWidthAndHeight(mActivity)[0]*32/75;
		actlistCountImage.setLayoutParams(image);
		
		((TextView) holder.getView(R.id.tv_campaign_name)).setText(item.getActivityName());//活动名称
		((TextView) holder.getView(R.id.tv_campaign_time)).setText(item.getStartTime());//活动时间
		TextView tvCampaignStatus = holder.getView(R.id.tv_campaign_status);//活动状态
		TextView tvCampaignMoney = holder.getView(R.id.tv_campaign_money);//活动费用
		
		//活动状态
		if (!Util.isEmpty(item.getStatus())) {
			if (Integer.parseInt(item.getStatus()) == Util.NUM_ZERO) { //0 未发布
				tvCampaignStatus.setText(R.string.campaign_list_status_zero);
			} else if (Integer.parseInt(item.getStatus()) == Util.NUM_ONE) { //1已报名
				if (Integer.parseInt(item.getLimitedParticipators()) == 0) { // 活动人数不限制
					tvCampaignStatus.setText("已报名" + item.getParticipators());
				} else {
					tvCampaignStatus.setText("已报名" + item.getParticipators() + "/" + item.getLimitedParticipators());
				}
				/*if (!Util.isEmpty(item.getParticipators())) {
					tvCampaignStatus.setText("已报名" + item.getParticipators() + "/" + item.getLimitedParticipators());
				} else {
					tvCampaignStatus.setText("已报名" );
				}*/
			} else if (Integer.parseInt(item.getStatus()) == Util.NUM_TWO) {//2停止报名
				tvCampaignStatus.setText(R.string.campaign_list_status_two);
			} else if (Integer.parseInt(item.getStatus()) == Util.NUM_THIRD) {//3活动已取消
				tvCampaignStatus.setText(R.string.campaign_list_status_three);
			} else if (Integer.parseInt(item.getStatus()) == Util.NUM_FOUR) {//4活动已结束
				tvCampaignStatus.setText(R.string.campaign_list_status_four);
			} else if (Integer.parseInt(item.getStatus()) == Util.NUM_FIVE) {//5活动已满员
				tvCampaignStatus.setText(R.string.campaign_list_status_five);
			}
		}
		
		//活动费用
		if (!Util.isEmpty(item.getTotalPayment())) {
			if (Double.parseDouble(item.getTotalPayment()) > NUM_ZERO) {//收费
				if (!Util.isEmpty(item.getMinPrice())) {
					if (item.getTotalPayment().equals(item.getMinPrice())) {
						tvCampaignMoney.setText("¥" + item.getTotalPayment());
					} else {
						tvCampaignMoney.setText("¥" + item.getMinPrice() + " - " + "¥" + item.getTotalPayment());
					}
				} else {
					tvCampaignMoney.setText("¥" + item.getMinPrice());
				}
			} else if (Double.parseDouble(item.getTotalPayment()) == Util.NUM_ZERO) {//免费
				tvCampaignMoney.setText("免费");
			}
		}
		
		return holder.getConvertView();
	}
}
