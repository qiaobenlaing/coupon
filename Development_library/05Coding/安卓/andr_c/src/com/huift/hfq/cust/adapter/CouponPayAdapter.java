package com.huift.hfq.cust.adapter;

import java.util.List;

import com.huift.hfq.cust.application.CustConst;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.adapter.CommenViewHolder;
import com.huift.hfq.base.adapter.CommonListViewAdapter;
import com.huift.hfq.base.pojo.Coupon;
import com.huift.hfq.base.utils.Calculate;
import com.huift.hfq.cust.R;

/**
 * 支付的优惠券
 * @author liyanfang
 *
 */
public class CouponPayAdapter extends CommonListViewAdapter<Coupon> {
	
	private static final String TAG = CouponPayAdapter.class.getSimpleName();
	private static final String DISCOUNT = "0";
	private CallBackData mBackData;
	private double money = 0;
	public CouponPayAdapter(Activity activity, List<Coupon> datas,double money) {
		super(activity, datas);
		this.money = money;
	}
	
	public void setItems(List<Coupon> datas,double money,CallBackData mBackData) {
		super.setItems(datas);
		this.money = money;
		this.mBackData = mBackData;
	}
	
	public CouponPayAdapter(Activity activity, List<Coupon> datas,double money,CallBackData mBackData) {
		super(activity, datas);
		this.money = money;
		this.mBackData = mBackData;
	}
	

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView, parent,R.layout.item_drawcoupon, position);
		final Coupon coupon = mDatas.get(position);
		final ImageView ckbCanCoupon = holder.getView(R.id.ckb_canusecoupon);
		TextView tvCanCoupon = holder.getView(R.id.tv_cancoupon);
		final TextView tvCouponMoney = holder.getView(R.id.tv_couponmoney);
		final TextView tvMoneyUnit = holder.getView(R.id.tv_moneyunit);
		double availablePrice = 0;
		double couponPrice = 0;
		try {
			if (!Util.isEmpty(coupon.getAvailablePrice())) {
				availablePrice = Double.parseDouble(coupon.getAvailablePrice());
			} else {
				availablePrice = 0;
			}
		} catch (Exception e) {
			Log.e(TAG, "优惠券满多少转换成double类型的时候出错="+e.getMessage()); // TODO
		}
		
		if (CustConst.Coupon.DEDUCT.equals(coupon.getCouponType()) 
				|| CustConst.Coupon.REG_DEDUCT.equals(coupon.getCouponType())  
				|| CustConst.Coupon.INVITE_DEDUCT.equals(coupon.getCouponType()) ) { //抵扣券
			tvCanCoupon.setText("满" + coupon.getAvailablePrice() + "元立减" + coupon.getInsteadPrice() + "元");
			try {
				if (!Util.isEmpty(coupon.getInsteadPrice())) {
					couponPrice = Double.parseDouble(coupon.getInsteadPrice());
				} else {
					couponPrice = 0;
				}
			} catch (Exception e) {
				Log.e(TAG, "优惠券面值转换出错="+e.getMessage());//TODO
			}
			if (couponPrice > 0) {
				tvCouponMoney.setText("- " + coupon.getInsteadPrice());
			} else {
				tvCouponMoney.setText("0");
			}
		} else if (CustConst.Coupon.DISCOUNT.equals(coupon.getCouponType())){ // 折扣券
			double available = 0; 
			double discount = 0 ;
			try {
				if (!Util.isEmpty(coupon.getDiscountPercent())) {
					discount = Double.parseDouble(coupon.getDiscountPercent());
				} else {
					discount = 0;
				}
			} catch (Exception e) {
				Log.e(TAG, "优惠券满多少转换成double类型的时候出错="+e.getMessage()); // TODO
			}
			// 计算折扣
			if (money > 0 && money >= availablePrice) {
				double mulMoney = Calculate.mul(money, discount);
				double divMoney = Calculate.div(mulMoney, 10); 
				available =Calculate.suBtraction(money, divMoney);
			} else {
				/*double mulMoney = Calculate.mul(availablePrice, discount);
				double divMoney = Calculate.div(mulMoney, 10); 
				available = Calculate.suBtraction(availablePrice,divMoney);*/
				available = 0;
			}
			tvCanCoupon.setText("满" + coupon.getAvailablePrice() + "元打" + coupon.getDiscountPercent() + "折");
			couponPrice = available;
			if (available > 0) {
				tvCouponMoney.setText("- " + Calculate.ceilBigDecimal(available));
			} else {
				tvCouponMoney.setText("0");
			}
		}
		if (couponPrice >= 0) {
			coupon.setCoupouPrice(couponPrice);
		} else {
			coupon.setCoupouPrice(0);
		}
		
		
		Integer selectFlag=coupon.getSelectFlag();
		if (selectFlag==null || selectFlag==0) {
			ckbCanCoupon.setImageResource(R.drawable.radio_no);
		} else {
			ckbCanCoupon.setImageResource(R.drawable.radio_yes);
		}
		View v=holder.getConvertView();
		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Integer selectFlag=coupon.getSelectFlag();
				if (selectFlag==null || selectFlag==0) {
					coupon.setSelectFlag(1);
					cleanOtherSelectItems(coupon);
					ckbCanCoupon.setImageResource(R.drawable.radio_yes);
				} else {
					coupon.setSelectFlag(0);
					ckbCanCoupon.setImageResource(R.drawable.radio_no);
				}
				Log.d(TAG, "mActivity="+mActivity);
				if (selectFlag == null) {
					selectFlag = 0;
				}
				
				double discount = 0;
				// 没有折扣
				if (DISCOUNT.equals(coupon.getDiscountPercent()) || Util.isEmpty(coupon.getDiscountPercent())) {
					discount = 0;
				} else {
					discount = Double.parseDouble(coupon.getDiscountPercent());
				}
				Log.d(TAG, "coupon.getCoupouPrice()="+coupon.getCoupouPrice() + "discount="+discount);
				mBackData.getItemData(coupon.getUserCouponCode(), coupon.getCoupouPrice(),discount,selectFlag,coupon.getAvailablePrice());
				notifyDataSetChanged();
			}
		});
		
		if (money >= availablePrice) {
			v.setEnabled(true);
			tvMoneyUnit.setTextColor(mActivity.getResources().getColor(R.color.deep_textcolor));
			tvCanCoupon.setTextColor(mActivity.getResources().getColor(R.color.deep_textcolor));
			tvCouponMoney.setTextColor(mActivity.getResources().getColor(R.color.red));
		} else {
			v.setEnabled(false);
			tvCanCoupon.setTextColor(mActivity.getResources().getColor(R.color.tv_content_color));
			tvCouponMoney.setTextColor(mActivity.getResources().getColor(R.color.tv_content_color));
			tvMoneyUnit.setTextColor(mActivity.getResources().getColor(R.color.tv_content_color));
			ckbCanCoupon.setImageResource(R.drawable.radio_ban);
		}
		
		return v;
	}
	
	private void cleanOtherSelectItems(Coupon coupon){
		for(Coupon c:mDatas){
			if(c==coupon){
				continue;
			}
			c.setSelectFlag(0);
		}
	}
	
	public interface CallBackData {
		public void getItemData (String couponCode,double couponPrice,double discount,int selectFlag,String availablePrice);
	}
	
}
