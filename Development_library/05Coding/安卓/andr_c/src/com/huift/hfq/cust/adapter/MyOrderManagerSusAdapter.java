package com.huift.hfq.cust.adapter;

import java.util.List;

import com.huift.hfq.cust.activity.MyOrderDetailActivity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.adapter.CommenViewHolder;
import com.huift.hfq.base.adapter.CommonListViewAdapter;
import com.huift.hfq.base.pojo.MyOrderItem;
import com.huift.hfq.cust.R;

/**
 * 订单管理(已完成)
 * @author qian.zhou
 */
public class MyOrderManagerSusAdapter extends CommonListViewAdapter<MyOrderItem>{
	
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
		TextView tvPayMoney = holder.getView(R.id.tv_order_paymeoney);//订单总金额
		RelativeLayout ryOrderDetail = holder.getView(R.id.ry_order_detail);//订单详情
		TextView tvOrderNbr = holder.getView(R.id.tv_ordernbr);//订单号
		
		
		//赋值
		Util.showImage(mActivity, myOrderItem.getLogoUrl() , ivShopLogo);
		if(Util.isEmpty(myOrderItem.getShopName())){
			tvShopName.setText(myOrderItem.getNickName());
		} else{
			tvShopName.setText(myOrderItem.getShopName());
		}
		tvDate.setText("下单时间 ：" + myOrderItem.getOrderTime());
		tvStatus.setText("已完成");
		tvPayMoney.setText(myOrderItem.getOrderAmount() + "元");
		tvOrderNbr.setText("订单 " + myOrderItem.getOrderNbr());
		
		//点击头像进去订单详情
		ryOrderDetail.setTag(position);
		ryOrderDetail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final int index = (Integer) v.getTag(); 
				String consumeCode = mDatas.get(index).getConsumeCode();
				Intent intent = new Intent(mActivity, MyOrderDetailActivity.class);
				intent.putExtra(MyOrderDetailActivity.CONSUME_CODE, consumeCode);
				intent.putExtra(MyOrderDetailActivity.ORDER_STATUS, true);
				mActivity.startActivity(intent);
			}
		});
		return holder.getConvertView();
	}
}
