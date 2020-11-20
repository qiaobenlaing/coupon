// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.Card;
import com.huift.hfq.base.pojo.Coupon;
import com.huift.hfq.base.pojo.OrderInfo;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.PaymentCompletionActivity;
import com.huift.hfq.shop.model.BankcardPayConfirmTask;
import com.huift.hfq.shop.model.CancelBankcardPayTask;
import com.huift.hfq.shop.model.GetOptimalPayTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.minidev.json.JSONObject;

import java.lang.reflect.Type;

/**
 * 扫码支付的确认订单界面
 * @author qian.zhou
 */
public class ConfirmOrderFragment extends Fragment {
	
	/** 传入的bankAccountCode*/
	public static final String BANK_ACCOUNTCODE = "bankAccountCode";
	/** 传入的userCode*/
	public static final String USER_CODE = "userCode";
	/** 传入的消费金额*/
	public static final String USER_AMOUNT = "amount";
	/** 传入的不打折优惠*/
	public static final String USER_NODISCOUNT = "nodiscount";
	/** 设备型号*/
	public static final String DEVICEMODEL = "SQ";
	/** 消费金额*/
	private String mOrderAmount;
	/** 不参与优惠的金额*/
	private String mNoDiscountPrice;
	/** 优惠券优惠*/
	private RelativeLayout mRyCouponDeduction;
	/** 会员卡优惠*/
	private RelativeLayout mRyCardDeduction;
	/** 商家红包优惠*/
	private RelativeLayout mRyShopBouns;
	/** 平台红包*/
	private RelativeLayout mRyPlatBouns;
	/** 工行优惠*/
	private RelativeLayout mRyicbcCardDeduction;
	/** 首单立减*/
	private RelativeLayout mRyFirstDeduction;
	/** 订单号*/
	private String mConsumeCode;
	/** 首单使用情况参数*/
	private String isUseFirstDeduction;
	/** 用户信息*/
	private OrderInfo mOrderInfo;
	/** 用户名称*/
	private TextView mShopName;
	/** 银行卡编码*/
	private String mBankAccountCode;
	/** 用户编码*/
	private String mUserCode;
	/** 设备型号*/
	private String mDeviceModel;
	
	/**
	 * 需要传递参数时有利于解耦 
	 * @return PosPayFragment
	 */
	public static ConfirmOrderFragment newInstance() {
		Bundle args = new Bundle();
		ConfirmOrderFragment fragment = new ConfirmOrderFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_confirm_order, container, false);
		ViewUtils.inject(this, v);
		ActivityUtils.add(getMyActivity());//订单完成
		Util.addActivity(getMyActivity());//手机取消
		init(v);
		return v;
	}
	
	private Activity getMyActivity(){
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;       
	}
	
	/**
	 * 初始化数据
	 */
	private void init(View v) {
		mDeviceModel = Build.MODEL; // 设备型号  
		Intent intent = getMyActivity().getIntent();
		mBankAccountCode = intent.getStringExtra(BANK_ACCOUNTCODE);
		mUserCode = intent.getStringExtra(USER_CODE);
		//标题
		TextView tvContent = (TextView) v.findViewById(R.id.tv_mid_content);
		tvContent.setText(R.string.confirm_order);
		if (mDeviceModel.startsWith(DEVICEMODEL)) {
			//pos获取传递过来的消费金额和不参与优惠的金额
			mOrderAmount = getMyActivity().getIntent().getStringExtra(USER_AMOUNT);
			mNoDiscountPrice = getMyActivity().getIntent().getStringExtra(USER_NODISCOUNT);
		} else {
			//手机获取传递过来的消费金额和不参与优惠的金额
			mOrderAmount = DB.getStr(ShopConst.InputAmout.INPUT_CONSUMPTION);
			mNoDiscountPrice = DB.getStr(ShopConst.InputAmout.INPUT_NODISCOUNT);
		}
		//初始化数据
		TextView tvOrderAmount = (TextView) v.findViewById(R.id.tv_order_amount);//消费金额
		TextView tvNotPrice = (TextView) v.findViewById(R.id.tv_order_notprice);//不参与优惠的金额
		if (!Util.isEmpty(mOrderAmount)) {
			tvOrderAmount.setText(mOrderAmount);
		} else {
			tvOrderAmount.setText("0.00");
		}
		if (!Util.isEmpty(mNoDiscountPrice)) {
			tvNotPrice.setText(mNoDiscountPrice);
		} else {
			tvNotPrice.setText("0.00");
		}
		mRyCouponDeduction = (RelativeLayout) v.findViewById(R.id.ry_coupon_deduction);
		mRyCardDeduction = (RelativeLayout) v.findViewById(R.id.ry_card_deduction);
		mRyShopBouns = (RelativeLayout) v.findViewById(R.id.ry_shop_bouns);
		mRyPlatBouns = (RelativeLayout) v.findViewById(R.id.ry_plat_bouns);
		mRyicbcCardDeduction = (RelativeLayout) v.findViewById(R.id.ry_icbccard_deduction);
		mRyFirstDeduction = (RelativeLayout) v.findViewById(R.id.ry_first_deduction);
		
		//获得最优的支付结果
		getOptimalPay(v);
	}
	
	/**
	 * 确定要结算吗
	 */
	OnClickListener btnListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			DialogUtils.showDialog(getActivity(), getString(R.string.cue),getString(R.string.payment_isclearing), getString(R.string.ok), getString(R.string.no), new DialogUtils().new OnResultListener() {
				@Override
				public void onOK() {
					bankCardPayConfirm();
				} 
				@Override
				public void onCancel(){
					
				}
			});
		}
	};
	
	
	/**
	 * 确定要取消吗
	 */
	public void cancelOrder () {
		
		DialogUtils.showDialog(getActivity(), getString(R.string.cue),getString(R.string.payment_iscancel), getString(R.string.ok), getString(R.string.no), new DialogUtils().new OnResultListener() {
			@Override
			public void onOK() {
				cancelBankcardPay();
			} 
			@Override
			public void onCancel(){
				
			}
		});
	}
	
	/**
	 * 点击结算的确定按钮
	 */
	public void bankCardPayConfirm(){
		
		new BankcardPayConfirmTask(getActivity(), new BankcardPayConfirmTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if(null != result){
					JSONObject object = (JSONObject) result.get("consumeInfo");
					Type jsonType = new TypeToken<OrderInfo>() {}.getType();
					OrderInfo orderInfo = new Gson().fromJson(object.toJSONString(), jsonType);
					Intent intent = new Intent(getActivity(), PaymentCompletionActivity.class);
					intent.putExtra(PaymentCompletionFragment.ORDERINFO_OBJ, orderInfo);
					startActivity(intent);
				}
			}
		}).execute(mConsumeCode, mBankAccountCode, isUseFirstDeduction);
	}
	
	/**
	 * 点击结算的取消按钮
	 */
	public void cancelBankcardPay(){
		
		new CancelBankcardPayTask(getActivity(), new CancelBankcardPayTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (result == null) {
					return;
				} else {
					if (String.valueOf(ErrorCode.SUCC).equals(result.get("code").toString())) {
						if (mDeviceModel.startsWith(DEVICEMODEL)) {
							getMyActivity().finish();
						} else {
							Util.exit();
						}
					}
				}
			}
		}).execute(mConsumeCode);
	}
	
	/**
	 * 获得最优的支付结果
	 */
	public void getOptimalPay(final View v) {
		
		new GetOptimalPayTask(getActivity(), new GetOptimalPayTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (result == null) {
					return;
				} else {
					Button btnRelease = (Button) v.findViewById(R.id.btn_release_activity);//结算
					btnRelease.setOnClickListener(btnListener);
					mConsumeCode = result.get("consumeCode").toString();
					JSONObject jsonObject = null;
					//优惠券
					jsonObject = (JSONObject) result.get("coupon");
					if (jsonObject != null) {
						
						setCoupon(v, jsonObject);
					} 
					//会员卡
					jsonObject = (JSONObject) result.get("card");
					if (jsonObject != null) {
						
						setCard(v, jsonObject);
					} 
					//商家红包、平台红包
					jsonObject = (JSONObject) result.get("bonus");
					if (jsonObject != null) {
						
						setShopPlatBouns(v, jsonObject);
					} 
					//工行卡折扣
					jsonObject = (JSONObject) result.get("bankCard");
					if (jsonObject != null) {
						
						setIcBcCard(v, jsonObject);
					} 
					jsonObject = (JSONObject) result.get("userInfo");
					mOrderInfo = Util.json2Obj(jsonObject.toString(), OrderInfo.class);
					if (null != jsonObject) {
						ImageView shopImage = (ImageView) v.findViewById(R.id.iv_shoplogo);//图像
						TextView shopName = (TextView) v.findViewById(R.id.tv_shopname);//用户昵称
						Util.showImage(getMyActivity(), mOrderInfo.getAvatarUrl() , shopImage);
						shopName.setText(!Util.isEmpty(mOrderInfo.getNickName()) ? mOrderInfo.getNickName() : "");
					}
					//首单立减
					String firstDeduction = result.get("firstDeduction").toString();
					TextView tvFirstDeduction  = (TextView) v.findViewById(R.id.tv_first_deduction);//首单优惠金额
					if (Util.isEmpty(firstDeduction) || "0".equals(firstDeduction) || "0.00".equals(firstDeduction)) {
						isUseFirstDeduction = "0";
						setVisibility(mRyFirstDeduction,false);
					} else {
						isUseFirstDeduction = "1";
						setVisibility(mRyFirstDeduction,true);
						tvFirstDeduction.setText("首单优惠" + firstDeduction);
					}
					//实际支付金额
					String realPay = result.get("realPay").toString();
					TextView TvRealPay = (TextView) v.findViewById(R.id.tv_actual_payment);
					TvRealPay.setText(!Util.isEmpty(realPay) ? realPay : "");
				}
			}
		}).execute(mUserCode,mOrderAmount, mNoDiscountPrice);
	}
	
	/**
	 * 优惠券
	 */
	public void setCoupon(View v, JSONObject result){
		//优惠券
		TextView tvCouponType = (TextView) v.findViewById(R.id.tv_coupon_type);//优惠券类型
		TextView tvCouponDescribe = (TextView) v.findViewById(R.id.tv_coupon_discount);//优惠券的抵扣具体描述
		TextView tvCouponCount = (TextView) v.findViewById(R.id.tv_coupon_count);//使用的优惠券数量
		TextView tvCouponInsteadPrice= (TextView) v.findViewById(R.id.tv_order_reduce_price);//优惠券抵扣的金额
		if (result != null) {
			/** 将JSONobject转换成对象 */
			Type jsonType = new TypeToken<Coupon>() {}.getType();
			Coupon coupon = new Gson().fromJson(result.toJSONString(), jsonType);
			setCouponName(tvCouponType, coupon.getCouponType());
			tvCouponDescribe.setText(!Util.isEmpty(coupon.getCouponString()) ? coupon.getCouponString() : "");
			tvCouponCount.setText(!Util.isEmpty(coupon.getUseNbr()) ? "x" + coupon.getUseNbr() : "");
			if (Util.isEmpty(coupon.getCouponInsteadPrice()) || "0".equals(coupon.getCouponInsteadPrice()) || "0.00".equals(coupon.getCouponInsteadPrice())) {
				setVisibility(mRyCouponDeduction,false);
			} else {
				setVisibility(mRyCouponDeduction,true);
				tvCouponInsteadPrice.setText("减免" + coupon.getCouponInsteadPrice());
			}
			
		}
	}
	
	/**
	 * 会员卡
	 */
	public void setCard (View v, JSONObject result) {
		//会员卡
		TextView tvCardDeduction = (TextView) v.findViewById(R.id.tv_card_deduction);//会员卡抵扣金额
		TextView tvCardDiscount = (TextView) v.findViewById(R.id.tv_card_discount);//会员卡折扣
		if  (result != null) {
			Type jsonType = new TypeToken<Card>() {}.getType();
			Card card  = new Gson().fromJson(result.toJSONString(), jsonType);
			tvCardDiscount.setText(!Util.isEmpty(card.getCardDiscount()) ? card.getCardDiscount() : "");
			if (Util.isEmpty(card.getCardInsteadPrice()) || "0".equals(card.getCardInsteadPrice()) || "0.00".equals(card.getCardInsteadPrice())) {
				setVisibility(mRyCardDeduction, false);
			} else {
				setVisibility(mRyCardDeduction, true);
				tvCardDeduction.setText("会员卡可优惠" + card.getCardInsteadPrice());
			}
			
		}
	}
	
	/**
	 * 商家红包、平台红包
	 */
	public void setShopPlatBouns (View v, JSONObject result) {
		TextView tvUserShopBouns  = (TextView) v.findViewById(R.id.tv_shop_platbouns_price);//用户使用商家红包 
		TextView tvShopBouns  = (TextView) v.findViewById(R.id.tv_shopplat_price);//商家红包抵扣金额 
		TextView tvUserPlatBouns  = (TextView) v.findViewById(R.id.tv_order_huiquanred_price);//用户使用平台红包
		TextView tvPlatBouns  = (TextView) v.findViewById(R.id.tv_huiquanred_price);//平台红包抵扣金额 
		if  (result != null) {
			Type jsonType = new TypeToken<OrderInfo>() {}.getType();
			OrderInfo orderInfo  = new Gson().fromJson(result.toJSONString(), jsonType);
			tvUserShopBouns.setText(!Util.isEmpty(orderInfo.getShopBonus()) ? orderInfo.getShopBonus() : "");
			if (Util.isEmpty(orderInfo.getUserShopBonus()) || "0".equals(orderInfo.getShopBonus()) || "0.00".equals(orderInfo.getShopBonus())) {
				setVisibility(mRyShopBouns, false);
			} else {
				setVisibility(mRyShopBouns, true);
				tvShopBouns.setText("商家红包可用" + orderInfo.getUserShopBonus());
			}
			tvUserPlatBouns.setText(!Util.isEmpty(orderInfo.getPlatBonus()) ? orderInfo.getPlatBonus() : "");
			if (Util.isEmpty(orderInfo.getUserPlatBonus()) || "0".equals(orderInfo.getPlatBonus()) || "0.00".equals(orderInfo.getPlatBonus())) {
				setVisibility(mRyPlatBouns, false);
			} else {
				setVisibility(mRyPlatBouns, true);
				tvPlatBouns.setText("惠圈红包可用" + orderInfo.getUserPlatBonus());
			}
		}
	}
	
	/**
	 * 工行折扣
	 */
	public void setIcBcCard (View v, JSONObject result) {
		TextView tvIcBcCardDiscount  = (TextView) v.findViewById(R.id.tv_icbccard_discount);//使用工行卡的折扣
		TextView tvIcBcCardDeduction  = (TextView) v.findViewById(R.id.tv_icbccard_discount_price);//使用工行卡的抵扣金额
		if  (result != null) {
			Type jsonType = new TypeToken<OrderInfo>() {}.getType();
			OrderInfo orderInfo = new Gson().fromJson(result.toJSONString(), jsonType);
			tvIcBcCardDiscount.setText(!Util.isEmpty(orderInfo.getBankCardDiscount()) ? orderInfo.getBankCardDiscount() : "");
			if (Util.isEmpty(orderInfo.getBankCardDeduction()) || "0".equals(orderInfo.getBankCardDeduction()) || "0.00".equals(orderInfo.getBankCardDeduction())) {
				setVisibility(mRyicbcCardDeduction, false);
			} else {
				setVisibility(mRyicbcCardDeduction, true);
				tvIcBcCardDeduction.setText("工行卡可优惠" + orderInfo.getBankCardDeduction());
			}
			
		}
	}
	
	/**
	 * 判断是否显示
	 * @param rl
	 * @param isShow
	 */
	public void setVisibility(RelativeLayout rl, boolean isShow){
		if(isShow){
			rl.setVisibility(View.VISIBLE);
		}else{
			rl.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 设置优惠券的名称
	 */
	public void setCouponName(TextView tvCouponType, String couponType) {
		if (couponType.equals(ShopConst.Coupon.N_BUY)) {//N元购
			
			tvCouponType.setText(R.string.coupon_nbuy);
			
		} else if (couponType.equals(ShopConst.Coupon.DEDUCT)) {//抵扣券
			
			tvCouponType.setText(R.string.coupon_deduct);
			
		} else if (couponType.equals(ShopConst.Coupon.DISCOUNT)) {//折扣券
			
			tvCouponType.setText(R.string.coupon_discount);
			
		} else if (couponType.equals(ShopConst.Coupon.REAL_COUPON)) {//实物券
			
			tvCouponType.setText(R.string.coupon_real);
			
		} else if (couponType.equals(ShopConst.Coupon.EXPERIENCE)) {//体验券
			
			tvCouponType.setText(R.string.coupon_exp);
			
		} else if (couponType.equals(ShopConst.Coupon.REG_DEDUCT)) {//抵扣券
			
			tvCouponType.setText(R.string.coupon_deduct);
			
		} else if (couponType.equals(ShopConst.Coupon.REG_DEDUCT)) {//抵扣券
			
			tvCouponType.setText(R.string.coupon_deduct);
		}
	}
	
	/** 点击返回 **/
	@OnClick(R.id.layout_turn_in)
	private void ivuppBackClick(View v) {
		cancelBankcardPay();
	}
}
