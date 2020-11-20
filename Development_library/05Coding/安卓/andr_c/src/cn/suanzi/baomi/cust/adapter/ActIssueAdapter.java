package cn.suanzi.baomi.cust.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.adapter.CommenViewHolder;
import cn.suanzi.baomi.base.adapter.CommonListViewAdapter;
import cn.suanzi.baomi.base.pojo.Activitys;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.activity.ActThemeDetailActivity;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.util.ActUtils;

/**
 * 发行的活动
 * @author liyanfang
 */
public class ActIssueAdapter extends CommonListViewAdapter<Activitys> {
	
	private static final String TAG = ActIssueAdapter.class.getSimpleName() ;    
	
	public ActIssueAdapter(Activity activity, List<Activitys> datas) {
		super(activity, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView, parent,R.layout.item_promotion, position);
		final Activitys activitys = mDatas.get(position);
		ImageView ivPromotionIcon = holder.getView(R.id.iv_promotion_icon); // 背景图片
		
		RelativeLayout.LayoutParams params = (LayoutParams) ivPromotionIcon.getLayoutParams();
		params.width = Util.getWindowWidthAndHeight(mActivity)[0];
		params.height = Util.getWindowWidthAndHeight(mActivity)[0]*32/75;
		ivPromotionIcon.setLayoutParams(params);
		
		TextView tvPromotionDescrible = holder.getView(R.id.tv_promotion_describle); // 描述文字
		TextView tvPromotionDate = holder.getView(R.id.tv_promotion_date); // 截取时间
		TextView tvPromotionStatus = holder.getView(R.id.tv_promotion_status); // 报名人数
		TextView tvPromotionPrice = holder.getView(R.id.tv_promotion_price); // 价格
		Util.showBannnerImage(mActivity, activitys.getActivityImg(), ivPromotionIcon);
		// 描述文字
		tvPromotionDescrible.setText(activitys.getActivityName());
		// 活动时间
		tvPromotionDate.setText(ActUtils.formatTime(activitys.getStartTime()) + " - " + ActUtils.formatTime(activitys.getEndTime()));
		// 价格
		if (activitys.getTotalPayment() == 0) {
			tvPromotionPrice.setText("免费");
			tvPromotionStatus.setVisibility(View.GONE);
		} else {
			// 活动人数限制
			tvPromotionStatus.setVisibility(View.VISIBLE);
			if (activitys.getLimitedParticipators() == 0) { // 活动人数不限制
				tvPromotionStatus.setText("已报名：" + activitys.getParticipators());
			} else {
				tvPromotionStatus.setText("已报名：" + activitys.getParticipators() + "/" + activitys.getLimitedParticipators());
			}
			String price = "";
			if (activitys.getMinPrice() == activitys.getTotalPayment()) {
				price = ActUtils.formatPrice(activitys.getTotalPayment()+"");
			} else {
				price = ActUtils.formatPrice(activitys.getMinPrice()+"") + " - " + ActUtils.formatPrice(activitys.getTotalPayment()+"");
			}
			tvPromotionPrice.setText("￥" + price);
		}
		View view = holder.getConvertView();
		// 点击事件
		view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mActivity, ActThemeDetailActivity.class);
				intent.putExtra(ActThemeDetailActivity.TYPE, CustConst.HactTheme.HOME_ACTIVITY);
				intent.putExtra(ActThemeDetailActivity.THEME_URL, "Browser/getActInfo?activityCode=" + activitys.getActivityCode() + "&appType=1");
				mActivity.startActivity(intent);
				
			}
		});
		return holder.getConvertView();
	}
	
}
