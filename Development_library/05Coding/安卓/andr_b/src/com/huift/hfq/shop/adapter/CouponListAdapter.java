// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.BatchCoupon;
import com.huift.hfq.base.utils.MyProgress;
import com.huift.hfq.base.utils.ShareCouponUtil;
import com.huift.hfq.shop.ShopApplication;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.CouponCsmDetailActivity;
import com.huift.hfq.shop.fragment.CouponCsmDetailFragment;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * 优惠券列表信息显示的适配器
 * @author yanfang.li
 *
 */
public class CouponListAdapter extends CommonListViewAdapter<BatchCoupon>{
	
	private final static String TAG = CouponListAdapter.class.getSimpleName();
	private final static int IS_EXPIRE = 0; // 过期
	private OnItemClickListener mOnItemClickLitener;
	public final static int ENABLE = 1;
	public final static int STOP = 0;
	public final static String STOP_STR = "停发";
	/** 定义全局变量*/
	private ShopApplication mShopApplication;
	/** 得到是否入驻的标示*/
	private boolean mSettledflag;
	
	public CouponListAdapter(Activity activity, List<BatchCoupon> datas) {
		super(activity, datas);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_allcoupon, position);
		final BatchCoupon batch = mDatas.get(position);//在数据源中获取实体类对象
		TextView tvMoney = holder.getView(R.id.tv_money); // 钱
		ImageView ivShare = holder.getView(R.id.iv_share); // 分享
		TextView tvInsteadPrice = holder.getView(R.id.tv_insteadPrice); // 面值
		TextView tvSymbol = holder.getView(R.id.tv_symbol); // 折扣单位
		TextView tvCouponType = holder.getView(R.id.tv_coupontype); // 实物券的类型
		TextView tvCouponBatch = holder.getView(R.id.coupon_batch); // 批次
		TextView tvCouponAvailable = holder.getView(R.id.coupon_available); // 满多少可减
		Button btnDraw = (Button) holder.getView(R.id.btn_draw); // 领取人员按钮
		MyProgress pgDrawCoupon = (MyProgress) holder.getView(R.id.progress_drawcoupon); // 领取的进度条
		TextView tvCouponPercentage = holder.getView(R.id.coupon_percentage); // 领取的进度条的数额
		LinearLayout lyCouponBgroud = (LinearLayout) holder.getView(R.id.ly_couponpic); // 优惠券背景
		LinearLayout lyUnavailable = (LinearLayout) holder.getView(R.id.ly_unavailable); // 优惠券失效
		RelativeLayout rlCoupon = (RelativeLayout) holder.getView(R.id.rl_coupon); // 整个列表
		int isExpire = batch.getIsExpire(); // 是否过期
		mShopApplication =  (ShopApplication) mActivity.getApplication();
		mSettledflag = mShopApplication.getSettledflag();

		// 可以干嘛
		String canMoney = "";
		// 面值
		String insteadPrice = "";
		// 优惠券类型名称
		String couponTypeName = "";
		if (ShopConst.Coupon.DEDUCT.equals(batch.getCouponType())) { // 抵扣券
			getCouponBg (isExpire,R.drawable.left_dedct,lyCouponBgroud,lyUnavailable);
			canMoney = "满" + batch.getAvailablePrice()+"元立减" + batch.getInsteadPrice() +"元";
			couponTypeName = getString(R.string.coupon_deduct);
			tvSymbol.setVisibility(View.GONE);
			tvInsteadPrice.setTextSize(22);
			tvCouponType.setVisibility(View.VISIBLE);
			tvMoney.setVisibility(View.VISIBLE);
			insteadPrice = batch.getInsteadPrice();
		} else if (ShopConst.Coupon.DISCOUNT.equals(batch.getCouponType())) { //折扣券
			getCouponBg (isExpire,R.drawable.left_discount,lyCouponBgroud,lyUnavailable);
			canMoney = "满" + batch.getAvailablePrice()+"元打" + batch.getDiscountPercent() + "折";
			couponTypeName = getString(R.string.coupon_discount);
			insteadPrice = batch.getDiscountPercent();
			tvSymbol.setVisibility(View.VISIBLE);
			tvCouponType.setVisibility(View.VISIBLE);
			tvInsteadPrice.setTextSize(22);
			tvMoney.setVisibility(View.GONE);
		} else if (ShopConst.Coupon.N_BUY.equals(batch.getCouponType())) { // N元购
			getCouponBg(isExpire, R.drawable.left_nbuy, lyCouponBgroud, lyUnavailable);
			canMoney = batch.getFunction();
			tvSymbol.setVisibility(View.GONE);
			tvMoney.setVisibility(View.VISIBLE);
			insteadPrice = batch.getInsteadPrice();
			couponTypeName = getString(R.string.coupon_nbuy);
			tvInsteadPrice.setTextSize(22);
			tvCouponType.setVisibility(View.VISIBLE);
			// 实物券和体验券
		} else if (ShopConst.Coupon.REAL_COUPON.equals(batch.getCouponType())
				|| ShopConst.Coupon.EXPERIENCE.equals(batch.getCouponType())) {
			tvMoney.setVisibility(View.GONE);
			tvSymbol.setVisibility(View.GONE);
			canMoney = batch.getFunction();
			tvInsteadPrice.setTextSize(16);
			tvCouponType.setVisibility(View.GONE);
			if (ShopConst.Coupon.REAL_COUPON.equals(batch.getCouponType())) {
				getCouponBg(isExpire, R.drawable.left_real, lyCouponBgroud, lyUnavailable);
				insteadPrice = getString(R.string.coupon_real);
			} else {
				getCouponBg(isExpire, R.drawable.left_experience, lyCouponBgroud, lyUnavailable);
				insteadPrice = getString(R.string.coupon_exp);
			}
		} 
		else if (ShopConst.Coupon.EXCHANGE_VOUCHER.equals(batch.getCouponType())) { // 兑换券
			getCouponBg(isExpire, R.drawable.left_ex_voucher, lyCouponBgroud, lyUnavailable);
			canMoney = batch.getFunction();
			tvSymbol.setVisibility(View.GONE);
			tvMoney.setVisibility(View.VISIBLE);
			insteadPrice = batch.getInsteadPrice();
			couponTypeName = getString(R.string.exchange_voucher);
			tvInsteadPrice.setTextSize(22);
			tvCouponType.setVisibility(View.VISIBLE);
			// 实物券和体验券
		} else if (ShopConst.Coupon.VOUCHER.equals(batch.getCouponType())) { // 代金券 s
			getCouponBg(isExpire, R.drawable.left_voucher, lyCouponBgroud, lyUnavailable);
			canMoney = batch.getFunction();
			tvSymbol.setVisibility(View.GONE);
			tvMoney.setVisibility(View.VISIBLE);
			insteadPrice = batch.getInsteadPrice();
			couponTypeName = getString(R.string.voucher);
			tvInsteadPrice.setTextSize(22);
			tvCouponType.setVisibility(View.VISIBLE);
		}
		if (Util.isEmpty(batch.getIsSend())) {
			ivShare.setVisibility(View.INVISIBLE);
		} else {
			int isSend = Integer.parseInt(batch.getIsSend());
			if (isSend == Util.NUM_ZERO) { // 如果等于0的话就是不是满就送
				ivShare.setVisibility(View.VISIBLE);
			} else {
				ivShare.setVisibility(View.INVISIBLE);
			}
		}
		tvCouponAvailable.setText(canMoney); // 满多少可干嘛
		tvInsteadPrice.setText(insteadPrice); // 面值多少
		tvCouponBatch.setText("批次: "+batch.getBatchNbr()); //批次
		tvCouponType.setText(couponTypeName);
		if (batch.getTotalVolume() == -1) { // 发行张数无限张
			pgDrawCoupon.setVisibility(View.GONE);
			tvCouponPercentage.setText("领取张数：" + batch.getTakenCount() );
		} else { // 发行张数大于0
			pgDrawCoupon.setVisibility(View.VISIBLE);
			try{
				int takenCount = Integer.parseInt( batch.getTakenCount());
				if (Util.isEmpty(batch.getTakenCount()) || takenCount <= 0) {
					pgDrawCoupon.setMax(batch.getTotalVolume());
					pgDrawCoupon.setProgress(0);
					if (batch.getIsAvailable() == STOP) { // 停发
						tvCouponPercentage.setText(STOP_STR + " (0/"+ batch.getTotalVolume() +")"); //批次
					} else {
						tvCouponPercentage.setText("未领取 (0/"+ batch.getTotalVolume() +")"); //批次
					}
				} else {
					pgDrawCoupon.setMax(batch.getTotalVolume());
					pgDrawCoupon.setProgress(takenCount);
					if (batch.getIsAvailable() == STOP) { // 停发
						tvCouponPercentage.setText(STOP_STR + "("+ batch.getTakenCount() + "/"+ batch.getTotalVolume() +")"); //批次
					} else {
						tvCouponPercentage.setText("已领取"+ batch.getTakenPercent() + "% ("+ batch.getTakenCount() + "/"+ batch.getTotalVolume() +")"); //批次
					}
				}
				
			} catch (Exception e) {
				Log.e(TAG, "优惠券批次转换出错="+e.getMessage()); //TODO
			}
		}
		
		OnClickListener clickSkip = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = null;
				switch (v.getId()) {
				case R.id.iv_share: // 分享
					if (mSettledflag) {
						ShareCouponUtil.shareCoupon(mActivity, batch); // 分享
					} else {
						mShopApplication.getDateInfo(mActivity);
					}
					break;
				case R.id.rl_coupon: // 列表跳转
					mOnItemClickLitener.onItemClick(null, holder.getConvertView(), position, position);
					break;
				case R.id.btn_draw: // 领取人员界面的跳转
					intent = new Intent(mActivity, CouponCsmDetailActivity.class);
					intent.putExtra(CouponCsmDetailFragment.BATCH_COUPON_CODE, batch.getBatchCouponCode());
					mActivity.startActivity(intent);   //TODO
					break;
				}
				
			}
		};
		rlCoupon.setOnClickListener(clickSkip);
		btnDraw.setOnClickListener(clickSkip);
		ivShare.setOnClickListener(clickSkip);
		return holder.getConvertView();
	}
	
	/**
	 * 提示信息
	 * @param strId 获得的Id
	 * @return 
	 */
	private String getString(int strId) {
		
		return mActivity.getResources().getString(strId);
	}
	
	/**
	 * 设置图片背景
	 * @param isExpire 是否过期
	 * @param imageId 图片的Id
	 * @param linearLayout 图片控件
	 */
	private void getCouponBg (int isExpire,int imageId,LinearLayout linearLayout,LinearLayout lyUnavailable) {
		if (isExpire == IS_EXPIRE) { // 过期
			linearLayout.setBackgroundResource(R.drawable.available_n);
			lyUnavailable.setVisibility(View.VISIBLE);
		} else {
			lyUnavailable.setVisibility(View.GONE);
			linearLayout.setBackgroundResource(imageId);
		}
	}
	
	public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
		this.mOnItemClickLitener = mOnItemClickLitener;
	}
}
