package com.huift.hfq.cust.fragment;


import com.huift.hfq.cust.activity.ICBCOnlinePayActivity;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.model.AddCouponOrderTask;
import com.huift.hfq.cust.model.GetInfoWhenCouponPay;
import com.umeng.analytics.MobclickAgent;

import net.minidev.json.JSONObject;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.BatchCoupon;
import com.huift.hfq.base.pojo.Bonus;
import com.huift.hfq.base.pojo.CouponBuy;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.Calculate;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.cust.R;

/**
 * 购买优惠券
 * @author yingchen
 */
public class CouponBuyFragment extends Fragment implements View.OnClickListener{
	private static final String TAG = CouponBuyFragment.class.getSimpleName();
	public  static final String COUPON = "coupon";
	public  static final String COUPON_CODE = "BatchcouponCode";
	
	/**加载的视图*/
	private View view;
	/**多张优惠券+的按钮*/
	private TextView mPlusTextView;
	/**多张优惠券-的按钮*/
	private TextView mMinusTextView;
	/**多张优惠券的数量*/
	private TextView mCountTextView;
	/**支付金额*/
	private TextView mPay;
	/**优惠券对象*/
	private BatchCoupon mCoupon;
	/**红包对象*/
	private Bonus mBonus;
	/**支付金额*/
	private double mTotalPrice;
	/**支付按钮*/
	private Button pay;
	/**限购数量*/
	private int mLimitedBuy; //如果为0的话 则没有限制
	/**最小支付金额*/
	private double miniPay;
	/**使用商家红包数量*/
	private double mShopBonus;;
	/**使用平台红包数量*/
	private double mPlatBonus;
	/**商户是否受理工行卡*/
	private boolean mAcceptICBC;
	/**使用的商家红包*/
	private TextView mUseShopBonus;
	/**使用的平台红包*/
	private TextView mUsePlatBonus;
	/**
	 * 需要传递参数时有利于解耦
	 */
	public static CouponBuyFragment newInstance() {
		Bundle args = new Bundle();
		CouponBuyFragment fragment = new CouponBuyFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//****记录买单流程*********
		ActivityUtils.add(getActivity());
		
		view = inflater.inflate(R.layout.fragment_coupon_buy, container, false);
		initView();
		//获取优惠券对象
		String batchCouponCode = getActivity().getIntent().getStringExtra(COUPON);
		Log.d(TAG, "batchCouponCode==="+batchCouponCode);
		
		initData(batchCouponCode);
		return view;
	}
	
	/**					
	 * 初始化数据
	 * @param batchCouponCode  优惠券编码
	 */
	private void initData(String batchCouponCode){
		new GetInfoWhenCouponPay(getActivity(), new GetInfoWhenCouponPay.CallBack() {
			
			@Override
			public void getResult(JSONObject result) {
				if(null != result){
					 CouponBuy couponBuy = Util.json2Obj(result.toString(), CouponBuy.class);
					 mCoupon = couponBuy.getBatchCouponInfo();
					 mBonus = couponBuy.getBonus();
					 miniPay = couponBuy.getMinRealPay();
					 Log.d(TAG, "IsAcceptBankCard()==="+couponBuy.getIsAcceptBankCard());
					 if(String.valueOf(Util.NUM_ONE).equals(couponBuy.getIsAcceptBankCard())){
						 mAcceptICBC = true;
					 }else if(String.valueOf(Util.NUM_ZERO).equals(couponBuy.getIsAcceptBankCard())){
						 mAcceptICBC = false;
					 }
					 showView();
				}
			}
		}).execute(batchCouponCode);
	}
	
	
	/**
	 * 根据返回的对象显示视图
	 * @param view
	 */
	private void showView() {
		//商家名称
		TextView shopName = (TextView) view.findViewById(R.id.tv_coupon_buy_shopname);
		shopName.setText(mCoupon.getShopName());
		
		//优惠券类型
		TextView couponType = (TextView) view.findViewById(R.id.tv_coupon_buy_type);
		
		//描述
		TextView desrible = (TextView) view.findViewById(R.id.tv_coupon_buy_describle);
		
		//单价
		TextView univalent = (TextView) view.findViewById(R.id.tv_coupon_buy_univalent);
		univalent.setText(mCoupon.getPayPrice()+"元");
		
		if(CustConst.Coupon.VOUCHER.equals(mCoupon.getCouponType())){ //8 代金券
			couponType.setText(Util.getString(R.string.cashcoupon));
			desrible.setText("抵"+mCoupon.getInsteadPrice()+"元");
			
		}else if(CustConst.Coupon.EXCHANGE_VOUCHER.equals(mCoupon.getCouponType())){ //7 兑换券
			couponType.setText(Util.getString(R.string.changecoupon));
			String function = mCoupon.getFunction();
			if(null!= function && function.length()>6){
				function = function.substring(0, 6)+"...";
			}
			desrible.setText(function);
		}
			
		//优惠券多张操作      显示+ - 
		LinearLayout many = (LinearLayout) view.findViewById(R.id.lv_coupon_buy_many);
		mPlusTextView = (TextView) view.findViewById(R.id.tv_coupon_buy_plus);
		mMinusTextView = (TextView) view.findViewById(R.id.tv_coupon_buy_minus); 
		mCountTextView = (TextView) view.findViewById(R.id.tv_coupon_buy_count);
		
		//+ -的监听
		mPlusTextView.setOnClickListener(this);
		mMinusTextView.setOnClickListener(this);
		
		//优惠券限购单张 
		TextView single = (TextView) view.findViewById(R.id.tv_coupon_buy_single);	
		
		//优惠券限购多张
		RelativeLayout mentioned = (RelativeLayout) view.findViewById(R.id.rl_coupon_buy_mention);
		TextView  limit = (TextView) view.findViewById(R.id.tv_coupon_buy_limited);
		
		//限购数量
		String nbrPerPerson = mCoupon.getNbrPerPerson();
	
		mLimitedBuy = Integer.parseInt(nbrPerPerson);
		
		if(mLimitedBuy == 1){ //限购一张
			many.setVisibility(View.GONE);
			single.setVisibility(View.VISIBLE);
			mentioned.setVisibility(View.GONE);
		}else{ //多张
			many.setVisibility(View.VISIBLE);
			single.setVisibility(View.GONE);
			mentioned.setVisibility(View.VISIBLE);
			
			if(mLimitedBuy==0){ //购买数量无限制
				limit.setText("您可以尽情购买优惠券");
				mCountTextView.setText(String.valueOf(Util.NUM_ONE)); //至少购买一张
			}else { //购买数量有限制
				limit.setText("您最多可以购买"+mLimitedBuy+"张");
				mCountTextView.setText(String.valueOf(mLimitedBuy));
			}
		}
		
		//提示remark
		TextView remark = (TextView) view.findViewById(R.id.tv_coupon_buy_remark);
		if(!Util.isEmpty(mCoupon.getRemark())){
			remark.setText(mCoupon.getRemark());
		}
		
		//支付金额
		mPay = (TextView) view.findViewById(R.id.tv_coupon_buy_pay);
	
		showTotalPriceByCount(mLimitedBuy);
		
		//显示红包
		showBounds();
	}

	/**
	 * 根据返回的情况显示平台红包和商家红包
	 */
	private void showBounds() {
		//平台红包
		LinearLayout platBonus = (LinearLayout) view.findViewById(R.id.ly_plat_bouns);
		
		//商家红包
		LinearLayout shopBonus = (LinearLayout) view.findViewById(R.id.ly_shop_bouns);
		
		
		//是否可以使用平台红包  0--不可用    1--可以用
		int canUsePlatBonus = mBonus.getCanUsePlatBonus();
		if(canUsePlatBonus == 1){
			platBonus.setVisibility(View.VISIBLE);
			//拥有的平台红包
			TextView totalPlatBonus = (TextView) view.findViewById(R.id.plat_canbouns);
			totalPlatBonus.setText(mBonus.getPlatBonus());
			
			//使用的平台红包
			mUsePlatBonus = (TextView) view.findViewById(R.id.platform_bounsprice);
			
			//拥有的平台红包的总金额
			final double ownPlatBons = Double.parseDouble(mBonus.getPlatBonus());
			//使用平台红包的监听
			mUsePlatBonus.addTextChangedListener(new TextWatcher() {
				@Override
				public void afterTextChanged(Editable s) {
					String inputPlatBonus = s.toString();
					if("".equals(inputPlatBonus)){
						inputPlatBonus = "0";
					}
					//获取输入的金额
					mPlatBonus= Double.parseDouble(inputPlatBonus);
					if(mPlatBonus>ownPlatBons){
						Util.getContentValidate(R.string.huiquan_insufficient_amount);
						setPayButton(false);
					}else{
						setPayButton(true);
						int efficitiveCount = getEfficitiveCount();
						double totalPrice = showTotalPriceByCount(efficitiveCount);
						showTotalPriceByBonus(totalPrice, mPlatBonus, 0);
					}
				}
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					
				}
				
			});
		}else{
			platBonus.setVisibility(View.GONE);
		}
		
		
		
		//是否可以使用商家红包  0--不可用   1--可以用
		int canUseShopBonus = mBonus.getCanUseShopBonus();
		if(canUseShopBonus == 1){
			shopBonus.setVisibility(View.VISIBLE);
			//拥有的商家红包
			TextView totalShopBonus = (TextView) view.findViewById(R.id.shop_canbouns);
			totalShopBonus.setText(mBonus.getShopBonus());
			
			//使用的商家红包
			mUseShopBonus = (TextView) view.findViewById(R.id.shop_bounsprice);
			//拥有的商家红包的总金额
			final double ownShopBons = Double.parseDouble(mBonus.getShopBonus());
			//使用商家红包的监听
			mUseShopBonus.addTextChangedListener(new TextWatcher() {
				@Override
				public void afterTextChanged(Editable s) {
					String inputShopBonus = s.toString();
					if("".equals(inputShopBonus)){
						inputShopBonus = "0";
					}
					mShopBonus = Double.parseDouble(inputShopBonus);
					//获取输入的金额
					if(mShopBonus>ownShopBons){
						Util.getContentValidate(R.string.insufficient_amount);
						setPayButton(false);
					}else{
						setPayButton(true);
						int efficitiveCount = getEfficitiveCount();
						double totalPrice = showTotalPriceByCount(efficitiveCount);
						showTotalPriceByBonus(totalPrice, 0, mShopBonus);
					}
				}
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					
				}
				
			});
		}else{
			shopBonus.setVisibility(View.GONE);
		}
		
	}

	/**
	 * 基本视图初始化  
	 */
	private void initView(){
		//标题
		TextView title = (TextView) view.findViewById(R.id.tv_mid_content);
		title.setText(Util.getString(R.string.icbc_pay));
		
		//回退
		ImageView back = (ImageView) view.findViewById(R.id.iv_turn_in);
		back.setOnClickListener(this);
		
		//支付按钮
		pay = (Button) view.findViewById(R.id.btn_coupon_buy);
		pay.setOnClickListener(this);
	}
	
	/**
	 * 点击事件的回掉
	 */
	@Override
	public void onClick(View v) {
		int buyCount = 0;
		
		//获取当前选择的数量
		switch (v.getId()) {
		case R.id.tv_coupon_buy_plus: //+
			buyCount = Integer.parseInt(mCountTextView.getText().toString().trim());
			buyCount++;
			int afterPlusCheckCount = checkCount(buyCount);
			mCountTextView.setText(String.valueOf(afterPlusCheckCount));
			showTotalPriceByCount(afterPlusCheckCount);
			
			//置空红包
			restBonus();
			break;
			
		case R.id.tv_coupon_buy_minus: //-
			buyCount = Integer.parseInt(mCountTextView.getText().toString().trim());
			buyCount--;
			int afterMinusCheckCount = checkCount(buyCount);
			mCountTextView.setText(String.valueOf(afterMinusCheckCount));
			showTotalPriceByCount(afterMinusCheckCount);
			
			//置空红包
			restBonus();
			break;

		case R.id.iv_turn_in://回退
			getActivity().finish();
			break;
			
		case R.id.btn_coupon_buy:
			//判断该店铺是否受理工行卡
			Log.d(TAG, "---"+mAcceptICBC);
			if(!mAcceptICBC){
				Util.getContentValidate(R.string.no_accept_icbc);
				return;
			}
			
			int efficitiveCount = getEfficitiveCount();
			Log.d(TAG, "shopCode==="+mCoupon.getShopCode());
			Log.d(TAG, "batchCoupeCode==="+mCoupon.getBatchCouponCode());
			Log.d(TAG, "couponNbr==="+efficitiveCount);
			Log.d(TAG, "shopBonus==="+mShopBonus);
			Log.d(TAG, "platBonus==="+mPlatBonus);
			goToPay(efficitiveCount);
			break;
			
		default:
			break;
		}
		
	}
	
	/**
	 * 置空红包
	 */
	private void restBonus(){
		if(null != mUseShopBonus){
			mUseShopBonus.setText("");
		}
		
		if(null != mUsePlatBonus){
			mUsePlatBonus.setText("");
		}
	}
	
	/**
	 * 
	 * @param count购买优惠券的数量
	 */
	public void goToPay(int count){
		new AddCouponOrderTask(getActivity(), new AddCouponOrderTask.CallBack() {
			
			@Override
			public void getResult(String result) { 
				if(null != result){  //此时  result为orderNbr||consumeCode||realPay拼接而成
					Log.d(TAG, "result==="+result);
					String[] split = result.split("\\|\\|");
					String orderNbr = split[0];
					String consumeCode = split[1];
					String realPay = split[2];
					Intent intent = new Intent(getActivity(), ICBCOnlinePayActivity.class);
					intent.putExtra(ICBCOnlinePayFragment.IS_BANK_PAY, true);
					intent.putExtra(ICBCOnlinePayFragment.SHOP_CODE, mCoupon.getShopCode());
					intent.putExtra(ICBCOnlinePayFragment.SHOP_NAME, mCoupon.getShopName());
					intent.putExtra(ICBCOnlinePayFragment.REAL_PAY, String.valueOf(realPay));
					intent.putExtra(ICBCOnlinePayFragment.REAL_CONSUMECODE, consumeCode);
					intent.putExtra(ICBCOnlinePayFragment.ORDERNBR, orderNbr);
					getActivity().startActivity(intent);
				}else{
					
				}
			}
		}).execute(mCoupon.getShopCode(),mCoupon.getBatchCouponCode(),String.valueOf(count),
				String.valueOf(mPlatBonus),String.valueOf(mShopBonus));
	}
	
	
	/**
	 * 检查购买优惠券的数量
	 * @param buyCount -- 购买数量
	 * @return -- 合理的购买数量
	 */
	private int checkCount(int buyCount){
		//最小数量为1
		if(buyCount<1){
			DialogUtils.showDialogSingle(getActivity(), "最低必须购买1张优惠券", R.string.cue,  R.string.ok, null);
			buyCount = Util.NUM_ONE;
		}//判断mLimitedBuy是否为0 如果为0则代表购买数量最大无限制
		else if(mLimitedBuy!=0 && buyCount>mLimitedBuy){
			DialogUtils.showDialogSingle(getActivity(), "购买优惠券数量最多为"+mLimitedBuy+"张", R.string.cue,  R.string.ok, null);
			buyCount =  mLimitedBuy;
		}
		return buyCount;
	}
	
	/**
	 * 根据购买优惠券的数量  显示购买的总金额
	 * @param effectiveCount---可以购买的优惠券数量
	 */
	private double showTotalPriceByCount(int effectiveCount){
		
		if(effectiveCount == 0){  //购买数量无限制
			mTotalPrice = Double.parseDouble(mCoupon.getPayPrice())*1;
		}else{   //购买数量有限制
			mTotalPrice = Double.parseDouble(mCoupon.getPayPrice())*effectiveCount;
		}
		mPay.setText(Calculate.ceilBigDecimal(mTotalPrice)+"元");
		return mTotalPrice;
	};
	
	/**
	 * 减去平台红包和商家红包的金额
	 * @param platBonus  平台红包（惠圈红包）
	 * @param shopBonus  商家红包
	 */
	private void showTotalPriceByBonus(double totalPrice,double platBonus,double shopBonus){
		mTotalPrice = totalPrice - platBonus - shopBonus;
		//支付金额 不能小于最小限额  且不能等于0
		if(mTotalPrice<miniPay || mTotalPrice == 0){
			Util.getContentValidate(R.string.pay_money_limit);
			mPay.setText(miniPay+"元");
			setPayButton(false);
		}else{
			setPayButton(true);
			mPay.setText(Calculate.ceilBigDecimal(mTotalPrice)+"元");
		}
	}
	
	/**
	 * 获取使用优惠券的数量
	 */
	private int getEfficitiveCount(){
		int effectiveCount = 0;
		if(mLimitedBuy == 1){
			effectiveCount = 1;
		}else{
			effectiveCount = Integer.parseInt(mCountTextView.getText().toString());
		} 
		return effectiveCount;
	}
	
	/**
	 * 设置支付按钮
	 */
	public void setPayButton(boolean clickStatus){
		if(clickStatus){
			pay.setEnabled(true);
			pay.setBackgroundResource(R.drawable.login_btn);
		}else{
			pay.setEnabled(false);
			pay.setBackgroundColor(Color.GRAY);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(CouponBuyFragment.class.getSimpleName()); //统计页面
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd(CouponBuyFragment.class.getSimpleName()); 
	}
}
