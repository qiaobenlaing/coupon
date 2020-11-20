package com.huift.hfq.cust.adapter;

import java.io.Serializable;
import java.util.List;

import com.huift.hfq.cust.activity.BatchCouponDetailActivity;
import com.huift.hfq.cust.activity.CouponBuyActivity;
import com.huift.hfq.cust.activity.H5ShopDetailActivity;
import com.huift.hfq.cust.activity.LoginActivity;
import com.huift.hfq.cust.activity.PayMoneyActivity;
import com.huift.hfq.cust.activity.ShopPayBillActivity;
import com.huift.hfq.cust.activity.UseCouponActivity;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.fragment.BatchCouponDetailFragment;
import com.huift.hfq.cust.fragment.CouponBuyFragment;
import com.huift.hfq.cust.fragment.ShopDetailFragment;
import com.huift.hfq.cust.fragment.ShopPayBillFragment;
import com.huift.hfq.cust.model.GrabCouponTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.BatchCoupon;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.base.utils.ShareCouponUtil;
import com.huift.hfq.cust.R;

public class NewCouponAdapter extends BaseAdapter implements View.OnClickListener{
	

	private List<BatchCoupon> datas;
	private Context context;
	private int readyGet; //1---代表已经领取的   2---未领取
	
	
	private OnGrabCoupon onGrabCoupon;
	
	private Shop shop;
	public NewCouponAdapter(List<BatchCoupon> datas, Context context,int readyGet,Shop shop,OnGrabCoupon onGrabCoupon) {
		super();
		this.datas = datas;
		this.context = context;
		this.readyGet = readyGet;
		this.shop = shop;
		this.onGrabCoupon = onGrabCoupon;
	}
	
	/**抢券操作的接口(领取) 为领取成功后回掉接口*/
	public interface OnGrabCoupon{
		void grabCoupon(boolean success);
	}
	
	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int arg0) {
		return datas;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.new_coupon_item, null);
			
			holder.couponType = (TextView) convertView.findViewById(R.id.tv_coupon_type);
			holder.unit = (TextView) convertView.findViewById(R.id.tv_coupon_unit);
			holder.info = (TextView) convertView.findViewById(R.id.tv_coupon_info);
			holder.describle = (TextView) convertView.findViewById(R.id.tv_coupon_des);
			holder.have = (TextView) convertView.findViewById(R.id.tv_coupon_have);
			holder.share = (ImageView) convertView.findViewById(R.id.iv_share);
			holder.share.setTag(position);
			holder.share.setOnClickListener(this);
			
			holder.useAround = (TextView) convertView.findViewById(R.id.tv_coupon_fanwei);
			
			holder.outTime = (TextView) convertView.findViewById(R.id.tv_out_time);
			holder.useOrGet = (TextView) convertView.findViewById(R.id.tv_useOrget);
			holder.useOrGet.setTag(position);
			holder.useOrGet.setOnClickListener(this);
			
			holder.refundAnyTime = (TextView) convertView.findViewById(R.id.tv_refund_anytime);
			holder.refundOutTime = (TextView) convertView.findViewById(R.id.tv_refund_outtime);
			
			holder.couponLeftProgressBar = (ProgressBar) convertView.findViewById(R.id.pb_coupon_left);
			holder.couponLeftTextView = (TextView) convertView.findViewById(R.id.tv_coupon_left);
			
			//holder.useMustKonw = (TextView) convertView.findViewById(R.id.tv_coupon_mustkonw);
			holder.useTime = (TextView) convertView.findViewById(R.id.tv_coupon_usetime);
			holder.useRule = (TextView) convertView.findViewById(R.id.tv_coupon_useRule);
			holder.alreadyGet = (ImageView) convertView.findViewById(R.id.iv_already_get);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		//判断优惠券类型 
		BatchCoupon batchCoupon = datas.get(position);
		String couponType = batchCoupon.getCouponType();
		if(couponType.startsWith(String.valueOf(3))){  //已3开头为抵扣券   3---领取的抵扣券   32--送新用户的抵扣券  33--送邀请人的优惠券 
			//优惠券类型
			holder.couponType.setText("抵扣券");
			//优惠券单位
			holder.unit.setText("￥");
			//优惠优惠的钱
			holder.info.setText(batchCoupon.getInsteadPrice());
			//优惠券描述
			holder.describle.setText("订单满"+batchCoupon.getAvailablePrice()+"减"+batchCoupon.getInsteadPrice()+"元");
			
			setRefund(holder, false);
			
			setUseAround(holder, false);
			
		}else if(couponType.equals(CustConst.Coupon.DISCOUNT)){ //折扣券
			holder.couponType.setText("折扣券");
			holder.unit.setText("折");
			//优惠券折扣
			holder.info.setText(batchCoupon.getDiscountPercent());
			holder.describle.setText("订单满"+batchCoupon.getAvailablePrice()+"打"+batchCoupon.getDiscountPercent()+"折");
			
			
			holder.refundAnyTime.setVisibility(View.VISIBLE);
			holder.refundOutTime.setVisibility(View.VISIBLE);
			
			setRefund(holder, false);
			setUseAround(holder, false);
			
		}else if(couponType.equals(CustConst.Coupon.EXCHANGE_VOUCHER)){ //兑换券
			holder.couponType.setText("兑换券");
			holder.unit.setText("￥");
			//优惠券价格
			holder.info.setText(batchCoupon.getPayPrice());
			holder.describle.setText("兑换:"+batchCoupon.getFunction());
			
			setRefund(holder, true);
			setUseAround(holder, true);
			
		}else if(couponType.equals(CustConst.Coupon.VOUCHER)){ //代金券
			holder.couponType.setText("代金券");
			holder.unit.setText("￥");
			//优惠券价格
			holder.info.setText(batchCoupon.getPayPrice());
			holder.describle.setText("价值"+batchCoupon.getInsteadPrice()+"元");
			
			setRefund(holder, true);
			setUseAround(holder, true);
			
		}else if(couponType.equals(CustConst.Coupon.N_BUY)){ //N元购   
			holder.couponType.setText("N元购");
			holder.unit.setText("￥");
			holder.info.setText(batchCoupon.getInsteadPrice());
			holder.describle.setText("此券可换"+batchCoupon.getInsteadPrice()+"元商品一份");
			
			setRefund(holder, false);
			setUseAround(holder, true);
			
		}else if(couponType.equals(CustConst.Coupon.REAL_COUPON)){ //实物券
			holder.couponType.setText("实物券");
			holder.unit.setVisibility(View.INVISIBLE);
			holder.info.setVisibility(View.INVISIBLE);
			holder.describle.setText(batchCoupon.getFunction());
			
			setRefund(holder, false);
			setUseAround(holder, true);
		}else if(couponType.equals(CustConst.Coupon.EXPERIENCE)){ //体验券
			holder.couponType.setText("体验券");
			holder.unit.setVisibility(View.INVISIBLE);
			holder.info.setVisibility(View.INVISIBLE);
			holder.describle.setText(batchCoupon.getFunction());
			
			setRefund(holder, false);
			setUseAround(holder, true);
		}
		
		//过期时间
		holder.outTime.setText("过期时间:"+batchCoupon.getExpireTime());
		
		//操作按钮
		if(readyGet == 1){  //已获取
 			holder.useOrGet.setText("立即使用");
 			holder.have.setVisibility(View.VISIBLE);
 			holder.have.setText("X"+batchCoupon.getUserCount());
 			
 			holder.couponLeftProgressBar.setVisibility(View.GONE);
			holder.couponLeftTextView.setVisibility(View.GONE);
			
			holder.alreadyGet.setVisibility(View.VISIBLE);
		}else if(readyGet == 2){ //未领取
			holder.couponLeftProgressBar.setVisibility(View.VISIBLE);
			holder.couponLeftTextView.setVisibility(View.VISIBLE);
			//进度条
			holder.couponLeftProgressBar.setProgress(batchCoupon.getRemaining()*100/batchCoupon.getTotalVolume());
			//剩余提示
			holder.couponLeftTextView.setText("剩余"+batchCoupon.getRemaining()+"张");
			
			holder.have.setVisibility(View.INVISIBLE);
			
			holder.alreadyGet.setVisibility(View.GONE);
			if(couponType.startsWith(String.valueOf(3)) || couponType.equals(String.valueOf(CustConst.Coupon.DISCOUNT))){ //显示领取
				holder.useOrGet.setText("立即领取");
			}else if(couponType.equals(String.valueOf(CustConst.Coupon.EXCHANGE_VOUCHER)) || couponType.equals(String.valueOf(CustConst.Coupon.VOUCHER))){ //购买
				holder.useOrGet.setText("立即购买");
			}
		}
			
		//使用时间
		holder.useTime.setText("使用时间："+batchCoupon.getDayStartUsingTime()+"-"+batchCoupon.getDayEndUsingTime());
		
		//使用规则
		if(!Util.isEmpty(batchCoupon.getRemark())){
			holder.useRule.setText("使用规则:\n"+batchCoupon.getRemark());
		}
		return convertView;
	}
	class ViewHolder{
		TextView couponType;
		TextView unit;
		TextView info;
		TextView describle;
		TextView have;
		ImageView share;
		
		TextView useAround;
		TextView outTime;
		TextView useOrGet;
		
		TextView refundAnyTime;
		TextView refundOutTime;
		
		ProgressBar couponLeftProgressBar;
		TextView  couponLeftTextView;
		
		TextView useMustKonw;
		TextView useTime;
		TextView useRule;
		ImageView alreadyGet;
	}
	
	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.tv_useOrget){ //按钮
			//判断是否登录
			if (!DB.getBoolean(DB.Key.CUST_LOGIN)) {
				Intent intentPay = new Intent(context, LoginActivity.class);
				context.startActivity(intentPay);
			}else{
				int position = (Integer) v.getTag();
				BatchCoupon batchCoupon = datas.get(position);
				String couponType = batchCoupon.getCouponType();
				if(readyGet == 1){ //已经领取  的使用
					if(couponType.startsWith(String.valueOf(3)) || couponType.equals(String.valueOf(CustConst.Coupon.DISCOUNT))){ //折扣券和抵扣券    进入支付流程
						goToPayProcess(batchCoupon);
					}else if(couponType.equals(String.valueOf(CustConst.Coupon.EXCHANGE_VOUCHER)) || couponType.equals(String.valueOf(CustConst.Coupon.VOUCHER))){ //代金券和兑换券   到优惠券详情
						goToCouponDetail(batchCoupon);
					}else if (couponType.equals(CustConst.Coupon.N_BUY)){ //N元购
						if(Util.isEmpty(batchCoupon.getUserCouponCode())){
							Util.showToastZH("尚未拥有该优惠券");
							return;
						}
						Intent intent = new Intent(context, PayMoneyActivity.class);
						intent.putExtra(PayMoneyActivity.USE_CPOUPON_CODE, batchCoupon.getUserCouponCode());
						context.startActivity(intent);
					}else if(couponType.equals(CustConst.Coupon.REAL_COUPON)|| couponType.equals(CustConst.Coupon.EXPERIENCE)) //实物券和体验券
					{
						if(Util.isEmpty(batchCoupon.getUserCouponCode())){
							Util.showToastZH("尚未拥有该优惠券");
							return;
						}
						Intent intent = new Intent(context, UseCouponActivity.class);
						intent.putExtra(UseCouponActivity.USE_CPOUPON_CODE, batchCoupon.getUserCouponCode()); 
						intent.putExtra(UseCouponActivity.TYPE, UseCouponActivity.SHOP_DETAIL);
						context.startActivity(intent);;
					}
				}else if(readyGet == 2){  //未领取
						/*if(couponType.startsWith(String.valueOf(3)) || 
								couponType.equals(String.valueOf(CustConst.Coupon.DISCOUNT))) //折扣券和抵扣券    直接调用API领取  
						{ 
							getCoupon(batchCoupon);
						}*/
						 if(couponType.equals(String.valueOf(CustConst.Coupon.EXCHANGE_VOUCHER)) || 
								couponType.equals(String.valueOf(CustConst.Coupon.VOUCHER)))//代金券和兑换券    跳到购买界面
						{ 
							buyCoupon(batchCoupon);
						}else{  //其他券  直接调用API领取  
							getCoupon(batchCoupon);
						}
				}
			}
		}else if(v.getId() == R.id.iv_share){ //分享
			if (!DB.getBoolean(DB.Key.CUST_LOGIN)) {
				Intent intentPay = new Intent(context, LoginActivity.class);
				context.startActivity(intentPay);
			}else{
				int position  = (Integer) v.getTag();
				BatchCoupon batchCoupon = datas.get(position);
				ShareCouponUtil.shareCoupon((Activity)context, batchCoupon); 
			}
		}
		
	}
	
	/**
	 * 进入支付流程
	 * @param batchCoupon
	 */
	private void goToPayProcess(BatchCoupon batchCoupon) {
		//判断优惠券是否可用
		if(shop.getShowPayBtn()==1){ 
			int ifCanPay = shop.getIfCanPay();
			if(ifCanPay==0){ //不可以支付
				Util.showToastZH("商家还未营业");
				return;
			}
		}else{ //不可支付
			Util.showToastZH("商家还未营业");
			return;
		}
		
		Intent intent = new Intent(context, ShopPayBillActivity.class);
		Shop shop = new Shop();
		shop.setShopName(batchCoupon.getShopName());
		shop.setShopCode(batchCoupon.getShopCode());
		shop.setBatchCouponCode(batchCoupon.getBatchCouponCode());
		
		intent.putExtra(ShopPayBillFragment.PAY_OBJ, (Serializable) shop);
		intent.putExtra(ShopPayBillFragment.NO_DISCOUNT, "1");
		intent.putExtra(ShopPayBillFragment.HAVE_MEAL, "3");
		context.startActivity(intent);
	}

	/**
	 * 购买优惠券
	 * @param batchCoupon
	 */ 
	private void buyCoupon(BatchCoupon batchCoupon) {
		Intent intent = new Intent(context, CouponBuyActivity.class);
		intent.putExtra(CouponBuyFragment.COUPON, batchCoupon.getBatchCouponCode());
		context.startActivity(intent);
	}

	/**
	 * 跳到优惠券详情
	 * @param batchCoupon
	 */
	private void goToCouponDetail(BatchCoupon batchCoupon) {
		Intent intent = new Intent(context, BatchCouponDetailActivity.class);
		intent.putExtra(BatchCouponDetailFragment.BATCH_COUPON_CODE, batchCoupon.getBatchCouponCode());
		intent.putExtra(BatchCouponDetailFragment.BATCH_COUPON_TYPE, batchCoupon.getCouponType());
		context.startActivity(intent);
	}

	/**
	 * 领取优惠券
	 */
	private void getCoupon(BatchCoupon batchCoupon){
		new GrabCouponTask((Activity)context, new GrabCouponTask.Callback() {
			@Override
			public void getResult(int resultCode) {
				if(resultCode==ErrorCode.SUCC){
					if(null != onGrabCoupon){
						onGrabCoupon.grabCoupon(true);
					}
				}else{
					if(null != onGrabCoupon){
						//提示失败
						Util.showToastZH("领取失败");
						onGrabCoupon.grabCoupon(false);
					}
				}
			}
		}).execute(batchCoupon.getBatchCouponCode(),String.valueOf(Util.NUM_TWO));
	}

	
	private void  setRefund(ViewHolder holder,boolean show){
		if(show){
			holder.refundAnyTime.setVisibility(View.VISIBLE);
			holder.refundOutTime.setVisibility(View.VISIBLE);
		}else{
			holder.refundAnyTime.setVisibility(View.GONE);
			holder.refundOutTime.setVisibility(View.GONE);
		}
		
	}
	
	private void setUseAround(ViewHolder holder,boolean noLimit){
		if(noLimit){
			holder.useAround.setText("使用范围:全场通用   单笔金额不限制");
		}else{
			holder.useAround.setText("使用范围:全场通用 ");
		}
	}
}
