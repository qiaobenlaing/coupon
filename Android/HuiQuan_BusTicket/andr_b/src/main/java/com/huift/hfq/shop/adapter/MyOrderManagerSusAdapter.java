package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.MyOrderItem;
import com.huift.hfq.shop.activity.MyBalanceDetailActivity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * 订单管理(已完成)
 * @author qian.zhou
 */
public class MyOrderManagerSusAdapter extends CommonListViewAdapter<MyOrderItem>{
	
	private static final String TAG = "MyOrderManagerSusAdapter";
	
	public MyOrderManagerSusAdapter(Activity activity, List<MyOrderItem> list) {
		super(activity, list);
	}
	@Override      
	public void setItems(List<MyOrderItem> myorderItem) {
		super.setItems(myorderItem);
	}
	@Override
	public void addItems(List<MyOrderItem> myorderItem) {
		super.addItems(myorderItem);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_order_suggest, position);
		final MyOrderItem myOrderItem = (MyOrderItem) getItem(position);
		ImageView ivShopLogo = holder.getView(R.id.shop_logo);//头像
		TextView tvShopName = holder.getView(R.id.tv_shop_name);//店铺名称
		TextView tvDate = holder.getView(R.id.tv_shop_date);//时间
		TextView tvStatus = holder.getView(R.id.tv_order_status);//状态
		TextView tvPayMoney = holder.getView(R.id.tv_order_paymeoney);//支付金额
		TextView tvPayPhone = holder.getView(R.id.tv_shop_phone);//电话
		RelativeLayout ryOrderDetail = holder.getView(R.id.ry_order_detail);//订单详情
		TextView tvOrderCoupon = holder.getView(R.id.tv_order_coupon);//兑换券或者代金券
		
		//赋值
		Util.showImage(mActivity, myOrderItem.getAvatarUrl(), ivShopLogo);
		tvShopName.setText(Util.isEmpty(myOrderItem.getNickName()) ? myOrderItem.getRealName() : myOrderItem.getNickName());
		tvPayPhone.setText(Util.isEmpty(myOrderItem.getUserMobileNbr()) ? "" : "("+myOrderItem.getUserMobileNbr()+")");
		tvDate.setText(myOrderItem.getConsumeTime());   
		
		if (Integer.parseInt(myOrderItem.getUserConsumeStatus()) == 3){
			tvStatus.setText("");
			tvPayMoney.setText(Util.isEmpty(myOrderItem.getRealPay()) ? "0" + "元" : "+" + myOrderItem.getRealPay() + "元");
		}
		else if (Integer.parseInt(myOrderItem.getUserConsumeStatus()) == 6){
			tvStatus.setText("退款申请中");
			tvPayMoney.setText(Util.isEmpty(myOrderItem.getRealPay()) ? "0" + "元" : "-" + myOrderItem.getRealPay() + "元");
		}   
		else if (Integer.parseInt(myOrderItem.getUserConsumeStatus()) == 7){
			tvStatus.setText("已退款");
			tvPayMoney.setText(Util.isEmpty(myOrderItem.getRealPay()) ? "0"+ "元" : "-" + myOrderItem.getRealPay() + "元");
		}
		
		if (!Util.isEmpty(myOrderItem.getUsedCouponName())) {
			tvOrderCoupon.setText(myOrderItem.getUsedCouponName());
		} else {
			tvOrderCoupon.setText("");
		}
		
		//点击头像进去订单详情
		ryOrderDetail.setTag(position);
		ryOrderDetail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final int index = (Integer) v.getTag();  
				String consumeCode = mDatas.get(index).getConsumeCode();
				Intent intent = new Intent(mActivity, MyBalanceDetailActivity.class);
				intent.putExtra(MyBalanceDetailActivity.CONSUME_CODE, consumeCode);
				intent.putExtra(MyBalanceDetailActivity.ORDER_STATUS, true);
				mActivity.startActivity(intent);
			}
		});
		return holder.getConvertView();
	}
}
