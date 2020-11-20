package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.OrderListItem;
import com.huift.hfq.shop.activity.MyAfterOrderDetailDistributionActivity;
import com.huift.hfq.shop.activity.MyFastingOrderDetailActivity;
import com.huift.hfq.shop.activity.MyRefundOrderDetailActivity;
import com.huift.hfq.shop.fragment.MyAfterOrderDetailDistributionFragment;
import com.huift.hfq.shop.fragment.MyFlatingOrderDetailFragment;
import com.huift.hfq.shop.fragment.MyRefundOrderDetailFragment;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * 订单管理(未完成)
 * @author qian.zhou
 */
public class MyStoreOrderContentAdapter extends CommonListViewAdapter<OrderListItem>{
	
	private final static String TAG = MyStoreOrderContentAdapter.class.getSimpleName();
	/** 订单状态*/
	private String mStatus;
	/** 进入店铺详情*/
	private RelativeLayout mRyIntoDetail;
	
	public MyStoreOrderContentAdapter(Activity activity, List<OrderListItem> list) {
		super(activity, list);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_store_order_content, position);
		final OrderListItem listItem = (OrderListItem) getItem(position);
		//0- 待商家结算 ，1-未支付，2-支付中，3-已支付，4-已取消订单，5-支付失败,6-退款申请中，7-已退款
		TextView tvOrderNumber = holder.getView(R.id.tv_order_number);//餐号
		TextView tvOrderDate = holder.getView(R.id.tv_order_date);//时间
		TextView tvOrderStatus = holder.getView(R.id.tv_order_status);//状态
		mRyIntoDetail = holder.getView(R.id.ry_into_detail);
		
		tvOrderNumber.setText("餐号：" + listItem.getMealNbr());
		tvOrderDate.setText(listItem.getOrderTime());
		if(listItem.getStatus().equals(String.valueOf(Util.NUM_ZERO))){
			mStatus = mActivity.getString(R.string.pend_store);
		} else if(listItem.getStatus().equals(String.valueOf(Util.NUM_ONE))){
			mStatus = mActivity.getString(R.string.unpaid);
		} else if(listItem.getStatus().equals(String.valueOf(Util.NUM_TWO))){
			mStatus = mActivity.getString(R.string.payment);
		} else if(listItem.getStatus().equals(String.valueOf(Util.NUM_THIRD))){
			mStatus = mActivity.getString(R.string.paid);
		} else if(listItem.getStatus().equals(String.valueOf(Util.NUM_FOUR))){
			mStatus = mActivity.getString(R.string.order_cancel);
		} else if(listItem.getStatus().equals(String.valueOf(Util.NUM_FIVE))){
			mStatus = mActivity.getString(R.string.fail_pay);
		} else if(listItem.getStatus().equals(String.valueOf(Util.NUM_SIX))){
			mStatus = mActivity.getString(R.string.refund_application);
		} else if(listItem.getStatus().equals(String.valueOf(Util.NUM_SEVEN))){
			mStatus = mActivity.getString(R.string.refunded);
		}
		tvOrderStatus.setText(mStatus);
		
		//点击进去订单详情
		mRyIntoDetail.setTag(position);
		mRyIntoDetail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final int index = (Integer) v.getTag(); 
				String orderCode = mDatas.get(index).getOrderCode();
				String status = mDatas.get(index).getStatus();
				if(status.equals(String.valueOf(Util.NUM_SIX))){//退款申请详情
					Intent intent = new Intent(mActivity, MyRefundOrderDetailActivity.class);
					intent.putExtra(MyRefundOrderDetailFragment.ORDER_CODE, orderCode);
					mActivity.startActivity(intent);
				} else{
					//判断是餐前还是餐后付款
					if(listItem.getEatPayType().equals(String.valueOf(Util.NUM_ONE))){//表示餐前
						Intent intent = new Intent(mActivity, MyFastingOrderDetailActivity.class);
						intent.putExtra(MyFlatingOrderDetailFragment.ORDER_CODE, orderCode);
						mActivity.startActivity(intent);
					} else{//表示餐后
						Intent intent = new Intent(mActivity, MyAfterOrderDetailDistributionActivity.class);
						intent.putExtra(MyAfterOrderDetailDistributionFragment.ORDER_CODE, orderCode);
						mActivity.startActivity(intent);
					}
				}
			}
		});
		return holder.getConvertView();
	}
}
