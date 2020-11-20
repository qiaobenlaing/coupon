package cn.suanzi.baomi.shop.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.adapter.CommenViewHolder;
import cn.suanzi.baomi.base.pojo.MyOrderItem;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.activity.MyBalanceDetailActivity;

/**
 * 订单管理(未完成)
 * @author qian.zhou
 */
public class MyOrderManagerFailAdapter extends CommonListViewAdapter<MyOrderItem>{
	
	public MyOrderManagerFailAdapter(Activity activity, List<MyOrderItem> list) {
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
		final CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_order_fail, position);
		final MyOrderItem myOrderItem = (MyOrderItem) getItem(position);
		ImageView ivShopLogo = holder.getView(R.id.iv_fail_shoplogo);//头像
		TextView tvShopName = holder.getView(R.id.tv_fail_shopname);//店铺名称
		TextView tvDate = holder.getView(R.id.tv_fail_date);//时间
		TextView tvStatus = holder.getView(R.id.tv_fail_status);//状态
		TextView tvPayMoney = holder.getView(R.id.tv_fail_paymeoney);//支付金额
		TextView tvOrderNbr = holder.getView(R.id.tv_ordernbr);//订单号
		RelativeLayout ryOrderDetail = holder.getView(R.id.ry_order_detail);//订单详情
		
		//赋值
		Util.showImage(mActivity, myOrderItem.getAvatarUrl() , ivShopLogo);
		tvShopName.setText(Util.isEmpty(myOrderItem.getNickName()) ? myOrderItem.getRealName() : myOrderItem.getNickName());
		tvOrderNbr.setText("订单 " + myOrderItem.getOrderNbr());
		tvDate.setText("下单时间 ：" + myOrderItem.getOrderTime());
		tvStatus.setText("未完成");
		tvPayMoney.setText(myOrderItem.getRealPay() + "元");
		
		//点击头像进去订单详情
		ryOrderDetail.setTag(position);
		ryOrderDetail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final int index = (Integer) v.getTag(); 
				String consumeCode = mDatas.get(index).getConsumeCode();
				Intent intent = new Intent(mActivity, MyBalanceDetailActivity.class);
				intent.putExtra(MyBalanceDetailActivity.CONSUME_CODE, consumeCode);
				intent.putExtra(MyBalanceDetailActivity.ORDER_STATUS, false);
				mActivity.startActivity(intent);
			}
		});
		return holder.getConvertView();
	}
}
