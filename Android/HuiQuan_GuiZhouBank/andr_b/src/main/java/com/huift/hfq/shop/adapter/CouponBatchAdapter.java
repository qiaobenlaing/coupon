// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.pojo.BatchCoupon;
import com.huift.hfq.shop.ShopConst;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * 优惠券设置界面适配器
 * @author yanfang.li
 */
public class CouponBatchAdapter extends CommonListViewAdapter<BatchCoupon> {
	

	public CouponBatchAdapter(Context context, List<BatchCoupon> mdata) {
		super(context, mdata);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mContext , convertView , parent, 
				R.layout.item_couponstyle, position);
		BatchCoupon batch = (BatchCoupon)getItem(position);//在数据源中获取实体类对象
		Log.d("msg", "batch.getCouponType()="+batch.getCouponType());
		LinearLayout rlCoupon = holder.getView(R.id.rl_couponstyle);
		if (ShopConst.Coupon.N_BUY.equals(batch.getCouponType())) { // N元购
			rlCoupon.setBackgroundResource(R.drawable.cardh_n);
		}
		if (ShopConst.Coupon.DEDUCT.equals(batch.getCouponType())) {
			rlCoupon.setBackgroundResource(R.drawable.cardh_reward);
		}
		if (ShopConst.Coupon.DISCOUNT.equals(batch.getCouponType())) {
			rlCoupon.setBackgroundResource(R.drawable.cardh_fullcut);
		}
		if (ShopConst.Coupon.REAL_COUPON.equals(batch.getCouponType())) {
			rlCoupon.setBackgroundResource(R.drawable.cardh_real);
		}
		if (ShopConst.Coupon.EXPERIENCE.equals(batch.getCouponType())) {
			rlCoupon.setBackgroundResource(R.drawable.cardh_experience);
		}
//		sTvCouponSetDtl.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
		
		((TextView)holder.getView(R.id.tv_coupon_batch)).setText("共"+batch.getBatchNbrAmt()+"批");
		((TextView)holder.getView(R.id.tv_coupon_num)).setText("剩余"+batch.getRestNbr()+"张");
		return holder.getConvertView();
	}



}
