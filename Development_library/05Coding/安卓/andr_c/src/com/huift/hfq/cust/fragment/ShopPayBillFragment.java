package com.huift.hfq.cust.fragment;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.BatchCoupon;
import com.huift.hfq.base.pojo.Bonus;
import com.huift.hfq.base.pojo.Card;
import com.huift.hfq.base.pojo.Icbc;
import com.huift.hfq.base.pojo.Pay;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.Calculate;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.cust.R;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huift.hfq.cust.activity.CouponSelectActivity;
import com.huift.hfq.cust.activity.ICBCOnlinePayActivity;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.model.BankCardPayTask;
import com.huift.hfq.cust.model.GetNewPriceTask;
import com.huift.hfq.cust.model.ListUserPayInfoTask;
import com.huift.hfq.cust.model.POBankcardPayTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 获取付款是的消费金额
 * @author ZhongHui.dong , yanfang.li
 */
public class ShopPayBillFragment extends Fragment implements TextWatcher {

	private static final String TAG = ShopPayBillFragment.class.getSimpleName();
	/** 可以干嘛 */
	public static final int IS_CAN = 1;
	/** 不可以干嘛 */
	public static final int NO_CAN = 0;
	/** 可以进行下一步 */ 
	public static final int CAN_CLICK = 1;
	/** 不可以进行下一步 */
	public static final int NO_CLICK = 0;
	public static final String PAY_OBJ = "payobj";
	public static final String ORDER_CODE = "orderCode";
	public static final String COUPONS_CODE = "coupons_code";
	public static final String COUPONS_PRICE = "coupons_price";
	public static final String BONUS_CODE = "bonus_code";
	public static final String BONUS_PRICE = "bonus_price";
	public static final String COUPONS_DDT = "coupon_ddt";
	/** 判断此次买单是否有不参与折扣的金额   优惠券点击买单   工行卡折扣按钮进来买单  堂食  都是参与折扣的*/
	public static final String NO_DISCOUNT = "noDiscount";
	/** 参与折扣*/
	public static final String JOIN_DISCOUNT = "1";
	/** 不参与折扣*/
	public static final String NOT_JOIN_DISCOUNT = "0";
	/** 堂食和外面的key*/
	public static final String HAVE_MEAL = "haveMeal";
	/** 既不是堂食也不是外卖*/
	public static final String NOT_MEAL = "3";
	/** 堂食*/
	public static final String EAT = "20";
	/** 外卖*/
	public static final String TAKE_OUT = "21";
	/** 请求的标识码*/
	public static final int COUPONS_FLAG = 100;
	/** 银行卡支付*/
	private static final int BANK_PAY = 200;
	/** 外卖等其他产品支付*/
	private static final int PRODUCT_PAY = 201;
	/** 线上银行卡支付编码*/
	public static final int PAY_ONLINE_CODE = 1;

	/** 商家红包 */
	private EditText mEdtShopBouns;
	/** 商家可用红包 */
	private TextView mTvShopCanBouns;
	/** 平台红包 */
	private EditText mEdtPlatBouns;
	/** 平台可用红包 */
	private TextView mTvPlatCanBouns;
	/** 工行卡打折 */
	private TextView mTvBankCardCanDiscount;
	/** 优惠券可以干嘛 */
	private TextView mTvCouponDeductCan;
	/** 折扣券可以干嘛 */
	private TextView mTvCouponDiscountCan;
	/** 抵扣多少钱 */
	private TextView mTvDeductMoney;
	/** 折扣多少钱 */
	private TextView mTvDiscountMoney;
	/** 抵扣券的数量 */
	private TextView mTvDeductNum;
	/** 输入金额 */
	private EditText mEdtInputPrice;
	/** 最后支付金额*/
	private EditText mEdtPayPrice;
	/** 下一步按钮 */
	private Button mBtnNext;
	/** 优惠券数量减减 */
	private ImageView mIvDeductSub;
	/** 优惠券数量加加 */
	private ImageView mIvDeductAdd;
	/** 折扣券跳转的箭头*/
	private ImageView mIvSkipDiscount;
	/** 抵扣券的箭头*/
	private ImageView mIvSkipDeduct;
	/** 显示抵扣金额和数量的布局*/
	private LinearLayout mLyDeductNum;
	/** 显示折扣金额的布局*/
	private LinearLayout mLyDiscountNum;
	/** 会员卡打折*/
	private TextView mTvCardDiscount;
	/** 选中不参与折扣的金额的图标（如酒水，海鲜）*/
	private CheckBox mCkbNoDiscount;
	/** 输入不参与折扣金额的列*/
	private RelativeLayout mRyNoDicount;
	/** 不参与折扣的金额*/
	private EditText mNoDiscountPrice;
	/** 支付的对象 */
	private Pay mPay;
	/** 商店实体 */
	private Shop mShop;
	/** 判断*/
	private boolean flag = false;
	/** 判断是否是工行卡支付 还是外卖支付(或其他) */
	private boolean mIsCardPay = true;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case BANK_PAY:
				payOnlineTask();
				break;
			case PRODUCT_PAY:
				payOnlienProduct();
				break;
			default:
				break;
			}
		};
	};
	
	/**
	 * 外卖等其他产品的在线支付的异步
	 * 
	 * @param pay
	 */
	public void payOnlienProduct() {
		Log.d(TAG, "payOnlienProduct()");
		String drinkPrice = mCkbNoDiscount.isChecked() ? getInputMoney(mNoDiscountPrice)+"" : "0";
		new POBankcardPayTask(getMyActivity(), new POBankcardPayTask.Callback() {
			@Override
			public void getResult(String result) {
				if (result != null) {
					String[] split = result.split("##"); 
					String consumeCode = split[0];
					String orderNbr = split[1];
					Intent intent = new Intent(getMyActivity().getApplicationContext(), ICBCOnlinePayActivity.class);
					intent.putExtra(ICBCOnlinePayFragment.IS_BANK_PAY, false);
					intent.putExtra(ICBCOnlinePayFragment.SHOP_CODE, mShop.getShopCode());
					intent.putExtra(ICBCOnlinePayFragment.SHOP_NAME, mShop.getShopName());
					intent.putExtra(ICBCOnlinePayFragment.REAL_PAY, String.valueOf(mPay.getNewPrice()));
					intent.putExtra(ICBCOnlinePayFragment.REAL_CONSUMECODE, consumeCode);
					intent.putExtra(ICBCOnlinePayFragment.ORDERNBR, orderNbr);
					startActivity(intent);
				}
			}
		}).execute(mShop.getOrderCode(), mPay.getBatchCouponCode(),mPay.getCouponCount()+"", String.valueOf(mPay.getPlatBonus()),
				String.valueOf(mPay.getShopBonus()),drinkPrice);
	}

	/**
	 * 工行卡在线支付的异步
	 */
	public void payOnlineTask() {
		Log.d(TAG, "payOnlineTask()");
		String drinkPrice = mCkbNoDiscount.isChecked() ? getInputMoney(mNoDiscountPrice)+"" : "0";
		new BankCardPayTask(getMyActivity(), new BankCardPayTask.Callback() {

			@Override
			public void getResult(String result) {
				if (result != null) { // 在线支付成功 result----返回的订单编码##订单编号
					String[] split = result.split("##");
					String consumeCode = split[0];
					String orderNbr = split[1];
					Intent intent = new Intent(getMyActivity().getApplicationContext(), ICBCOnlinePayActivity.class);
					intent.putExtra(ICBCOnlinePayFragment.IS_BANK_PAY, true);
					intent.putExtra(ICBCOnlinePayFragment.SHOP_CODE, mShop.getShopCode());
					intent.putExtra(ICBCOnlinePayFragment.SHOP_NAME, mShop.getShopName());
					intent.putExtra(ICBCOnlinePayFragment.REAL_PAY, String.valueOf(mPay.getNewPrice()));
					intent.putExtra(ICBCOnlinePayFragment.REAL_CONSUMECODE, consumeCode);
					intent.putExtra(ICBCOnlinePayFragment.ORDERNBR, orderNbr);
					startActivity(intent);
				} else {

				}

			}
		}).execute(mShop.getShopCode(),mEdtInputPrice.getText().toString(), mPay.getBatchCouponCode(),
				String.valueOf(mPay.getCouponCount()),String.valueOf(mPay.getPlatBonus()),String.valueOf(mPay.getShopBonus()),drinkPrice);
	}

	/**
	 * 需要传递参数时有利于解耦
	 */
	public static ShopPayBillFragment newInstance() {
		Bundle args = new Bundle();
		ShopPayBillFragment fragment = new ShopPayBillFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_shop_paybill, container, false);
		ViewUtils.inject(this, view);
		// ****记录买单流程*********
		ActivityUtils.add(getMyActivity());
		Util.addActivity(getMyActivity());
		Util.addLoginActivity(getMyActivity());
		initView(view);
		initData(view);
		return view;
	}

	/**
	 * 保证activity不为空
	 * 
	 * @return activity
	 */
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	/**
	 * 初始化数据
	 */
	private void initData(View view) {
		mShop = (Shop) getMyActivity().getIntent().getSerializableExtra(PAY_OBJ);
		mPay = new Pay();
		mPay.setCouponType("0"); // 默认优惠券的类型
		mPay.setPlatBonusPrice(0);// 初始化红包的金额
		mPay.setShopBonusPrice(0); // 初始化红包的金额
		if (null == mShop) {
			Log.e(TAG, "商家类为空");
			return;
		}
		// 给商家姓名赋值
		TextView shopName = (TextView) view.findViewById(R.id.shop_name);
		shopName.setText(mShop.getShopName());
		// 订单编码
		TextView tvOrderCode = (TextView) view.findViewById(R.id.tv_order_code);
		RelativeLayout rlOrderCode = (RelativeLayout) view.findViewById(R.id.rl_order_code);
		if (Util.isEmpty(mShop.getOrderNbr())) {
			rlOrderCode.setVisibility(View.GONE);
			mPay.setBatchCouponCode(""); // 优惠券编码
			mPay.setCouponCount(0);
			mEdtInputPrice.setEnabled(true);
			mIsCardPay = true;
		} else {
			tvOrderCode.setText(mShop.getOrderNbr());
			rlOrderCode.setVisibility(View.VISIBLE);
			mEdtInputPrice.setEnabled(false);
			mEdtInputPrice.setText(mShop.getOrderAmount());
			mPay.setCouponCount(1);
			mPay.setBatchCouponCode(mShop.getBatchCouponCode()); // 优惠券编码
			mIsCardPay = false;
		}
		listUserPayInfo(view);
		getBtnStatus(NO_CAN);
		mEdtInputPrice.addTextChangedListener(this);
		mNoDiscountPrice.addTextChangedListener(this);
		mEdtShopBouns.addTextChangedListener(bounsShopTextChange);
		mEdtPlatBouns.addTextChangedListener(bounsPlatTextChange);
		// 是否是堂食和外面
		String haveMeal = getMyActivity().getIntent().getStringExtra(HAVE_MEAL);
		// 是否参与折扣  
		String notDisCount = getMyActivity().getIntent().getStringExtra(NO_DISCOUNT);
		// 是否参与折扣的布局
		RelativeLayout lyNoDiscount = (RelativeLayout)view.findViewById(R.id.no_discount);
		// 判断 优惠券点击买单   工行卡折扣按钮进来买单  堂食  都是参与折扣的
		if (haveMeal.equals(EAT) || haveMeal.equals(NOT_MEAL) || notDisCount.equals(JOIN_DISCOUNT)) {
			lyNoDiscount.setVisibility(View.VISIBLE);
		} else {
			lyNoDiscount.setVisibility(View.GONE );
		}
	}
	
	/**
	 * 加减的点击事件
	 * @param limitedNbr 限用的优惠券
	 * @param userCount 我拥有的优惠券
	 * @param currentCount 当前优惠券
	 */
	private void ivDeductNumClick (final int limitedNbr,final double insteadPrice,double availablePrice) {
		OnClickListener ivDeductNumListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				int currentCount = getCurrentCouponNum();
				switch (v.getId()) {
				case R.id.iv_deduct_sub: // 减
					if ( currentCount <= 1) {
						Util.getContentValidate(R.string.use_couponnum_small);
					} else {
						-- currentCount;
					}
					mTvDeductNum.setText(currentCount + "");
					break;
				case R.id.iv_deduct_add: // 加
					if (currentCount >= limitedNbr ) {
						// 不提示
					} else {
						currentCount ++;
					}
					mTvDeductNum.setText(currentCount + "");
					break;

				default:
					break;
				}
				mPay.setCouponPayMoney(insteadPrice*currentCount); // 折扣券最后支付的金额
				mTvDeductMoney.setText("减免" + (int)insteadPrice*currentCount + "元");
				getPayNewMoney(); // 重新计算金额
				mPay.setCouponCount(currentCount);
			}
		};
		mIvDeductSub.setOnClickListener(ivDeductNumListener);
		mIvDeductAdd.setOnClickListener(ivDeductNumListener);
	}
	
	/**
	 * 检查使用抵扣券的张数
	 * 大于0
	 * 小于领取的数量  userCount
	 * 小于限制的数量 limitCount
	 * 小于 输入金额/满xx 的数量    cusumeAmount/availablePrice
	 */
	public int checkUseCount(int count,int userCount,int limitCount,double cusumeAmount,double availablePrice){
		
		//不能大于 cusumeAmount/availablePrice数量
		count =(int) (cusumeAmount/availablePrice);
		if (count >= userCount || count >= limitCount) {
			if (userCount >= limitCount) {
				count = limitCount;
			} else {
				count = userCount;
			}
		} 
		//不能大于限制的数量  limitCount=0 没有限制
		return count;
	}

	/**
	 * 初始化视图
	 */
	private void initView(View view) {
		mEdtShopBouns = (EditText) view.findViewById(R.id.shop_bounsprice);
		mTvShopCanBouns = (TextView) view.findViewById(R.id.shop_canbouns);
		mEdtPlatBouns = (EditText) view.findViewById(R.id.platform_bounsprice);
		mTvPlatCanBouns = (TextView) view.findViewById(R.id.plat_canbouns);
		mTvBankCardCanDiscount = (TextView) view.findViewById(R.id.tv_bankcard_can_discount);
		mBtnNext = (Button) view.findViewById(R.id.next);
		mEdtInputPrice = (EditText) view.findViewById(R.id.price);
		mEdtPayPrice = (EditText) view.findViewById(R.id.pay_price);
		mIvDeductSub = (ImageView) view.findViewById(R.id.iv_deduct_sub);
		mIvDeductAdd = (ImageView) view.findViewById(R.id.iv_deduct_add);
		mIvSkipDeduct = (ImageView) view.findViewById(R.id.iv_skip_deduct);
		mIvSkipDiscount = (ImageView) view.findViewById(R.id.iv_skip_discount);
		mTvCouponDeductCan = (TextView) view.findViewById(R.id.tv_coupon_deduct_can);
		mTvCouponDiscountCan = (TextView) view.findViewById(R.id.tv_coupon_discount_can);
		mTvDeductMoney = (TextView) view.findViewById(R.id.tv_deduct_money);
		mTvDiscountMoney = (TextView) view.findViewById(R.id.tv_discount_money);
		mTvDeductNum = (TextView) view.findViewById(R.id.tv_deduct_num);
		mLyDeductNum = (LinearLayout) view.findViewById(R.id.ly_deduct_num);
		mLyDiscountNum = (LinearLayout) view.findViewById(R.id.ly_discount_num);
		mTvCardDiscount = (TextView) view.findViewById(R.id.tv_card_discount);
		mCkbNoDiscount = (CheckBox) view.findViewById(R.id.ckb_no_discount);
		mRyNoDicount = (RelativeLayout) view.findViewById(R.id.rl_no_dicount);
		mNoDiscountPrice = (EditText) view.findViewById(R.id.no_discount_price);
		// 不享受优惠的折扣
		mCkbNoDiscount.setChecked(false);
		mCkbNoDiscount.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					mCkbNoDiscount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.radio_yes, 0, 0, 0);
					mNoDiscountPrice.setFocusable(true);   
					mNoDiscountPrice.setFocusableInTouchMode(true);   
					mNoDiscountPrice.requestFocus();  
					mRyNoDicount.setVisibility(View.VISIBLE);
					mCkbNoDiscount.setChecked(true);
				} else {
					mNoDiscountPrice.setFocusable(false);   
					mNoDiscountPrice.setFocusableInTouchMode(false); 
					mCkbNoDiscount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.radio_no, 0, 0, 0);
					mRyNoDicount.setVisibility(View.GONE);
				}
				// 计算最后金额
				getPayNewMoney();
			}
		});
		
	}

	/**
	 * 获取优惠券
	 */
	private void listUserPayInfo(final View view) {
		// $shopCode, $consumeAmount, $batchCouponCode
		String batchCouponCode = "";
		if (!Util.isEmpty(mShop.getBatchCouponCode())) {
			batchCouponCode = mShop.getBatchCouponCode();
		}
		
		Log.d(TAG, "batchCouponCode==="+batchCouponCode);
		Log.d(TAG, "shopCode==="+mShop.getShopCode());
		
		double consumeAmount = 0;
		if (!Util.isEmpty(mShop.getOrderAmount())) {
			consumeAmount = Double.parseDouble(mShop.getOrderAmount());
		}
		String params[] = { mShop.getShopCode(), consumeAmount+"s", batchCouponCode };
		new ListUserPayInfoTask(getActivity(), new ListUserPayInfoTask.Callback() {

			@Override
			public void getResult(JSONObject result) {
				if (result == null) {
					Log.d(TAG, "返回类型为空");
					if (!TextUtils.isEmpty(mEdtInputPrice.getText())) {
						getPayNewMoney();
					}
				} else {
					Log.d(TAG, "shopPayBill result >>>  " + result.toString());
					mPay = new Gson().fromJson(result.toString(), new TypeToken<Pay>() {}.getType());
					Log.d(TAG, "mPay >>> " + mPay.getIsFirst() + ">>>  消费金额 >>> " + mPay.getMealFirstDec());
					setControls(mPay, view);
				}

			}
		}).execute(params);
	}
	
	/**
	 * 输入金额不为空判断 然后转换为double类型的数字
	 * @param editText  输入的金额
	 * @return 返回double的数据
	 */
	public double getInputMoney(EditText editText) { // TODO
		double inputprice = 0;
		String edtPrice = TextUtils.isEmpty(editText.getText()) ? "0" : editText.getText().toString();
		try {
			inputprice = edtPrice.lastIndexOf(".") == 0 ? 0 : Double.parseDouble(edtPrice);
		} catch (Exception e) {
			Log.e(TAG, "输入金额不为空判断  error >> " + e.getMessage());
		}
		return inputprice;
	}
	
	/**
	 * 去掉不参与折扣的金额
	 * @param editText  输入的金额
	 * @return 返回double的数据
	 */
	public double getInputMoney() { // TODO
		double inputprice = 0;
		String edtPrice = TextUtils.isEmpty(mEdtInputPrice.getText()) ? "0" : mEdtInputPrice.getText().toString();
		try {
			inputprice = edtPrice.lastIndexOf(".") == 0 ? 0 : Double.parseDouble(edtPrice);
		} catch (Exception e) {
			Log.e(TAG, "输入金额不为空判断  error >> " + e.getMessage());
		}
		// 有不参与折扣的金额
		if (mCkbNoDiscount.isChecked()) { 
			String nodistCount = TextUtils.isEmpty(mNoDiscountPrice.getText()) ? "0" : mNoDiscountPrice.getText().toString();
			double noDiscountPrice = 0;
			try {
				noDiscountPrice = nodistCount.lastIndexOf(".") == 0 ? 0 : Double.parseDouble(nodistCount);
			} catch (Exception e) {
				Log.e(TAG, "输入金额不为空判断  error >> " + e.getMessage());
			}
			inputprice = Calculate.suBtraction(inputprice, noDiscountPrice);
		} 
		return inputprice;
	}

	/**
	 * 计算会员卡打折
	 */
	private void setCardDiscount ( double discount , TextView textView) {
		// 需要参与优惠的金额
		double couponAmount = getInputMoney() < 0 ? 0 : getInputMoney();
		// 会员卡折扣的金额
		double cardPrice = Calculate.suBtraction(couponAmount, mPay.getCouponPayMoney());
		textView.setText(Calculate.ceilBigDecimal(getDisCountMoney(cardPrice < 0 ? 0 : cardPrice, discount)) + "");
	}
	
	/**
	 * 计算工行卡的折扣
	 */
	private void setICBCDiscount (double discount , TextView textView) {
		double totalMoney = Calculate.add(mPay.getCouponPayMoney(),
							Calculate.add(ViewSolveUtils.getInputMoney(mTvCardDiscount),
							Calculate.add(mPay.getPlatBonus(),mPay.getShopBonus())));
		// 优惠金额
		double couponAmount = getInputMoney() < 0 ? 0 : getInputMoney();
		// 工行折折扣的金额
		double icbcPrice = Calculate.suBtraction(couponAmount, totalMoney);
		textView.setText(Calculate.ceilBigDecimal(getDisCountMoney(icbcPrice < 0 ? 0 : icbcPrice, discount)) + "");
	}
	
	/**
	 * 设置控件
	 * @param pay
	 * @param view
	 */
	private void setControls(Pay pay, View view) {
		if (null != pay && null != view) {
			// 满就送
			setActInfo(view, R.id.rl_full_send_act, R.id.tv_full_send_act, pay.getHasSendCoupon(), pay.getSendCoupon());
			// 会员卡打折
			RelativeLayout rlCardDiscount = (RelativeLayout) view.findViewById(R.id.rl_card_discount);
			Card card = pay.getCard();
			// 设置是否可以使用会员卡
			mPay.setCanUseCard(card.getCanUseCard());
			if (card.getCanUseCard() == IS_CAN) {
				rlCardDiscount.setVisibility(View.VISIBLE);
				mPay.setCardDisCount(getDouleData(card.getDiscount())); // 保存会员卡折扣
				Log.d(TAG, "mTvCardDiscount  >>> " +  mPay.getCardDisCount());
				setCardDiscount(mPay.getCardDisCount(), mTvCardDiscount);
			} else {
				mPay.setCardDisCount(0);
				rlCardDiscount.setVisibility(View.GONE);
			}
			// 红包
			LinearLayout lyShopBouns = (LinearLayout) view.findViewById(R.id.ly_shop_bouns);
			LinearLayout lyPlatBouns = (LinearLayout) view.findViewById(R.id.ly_plat_bouns);
			Bonus bouns = pay.getBonus();
			
			// 平台红包
			if (bouns.getCanUsePlatBonus() == IS_CAN) {
				lyPlatBouns.setVisibility(View.VISIBLE);
				mTvPlatCanBouns.setText(bouns.getPlatBonus());
				mPay.setPlatBonusPrice(getDouleData(bouns.getPlatBonus()));
			} else {
				lyPlatBouns.setVisibility(View.GONE);
			}
			// 商家红包
			if (bouns.getCanUseShopBonus() == IS_CAN) {
				lyShopBouns.setVisibility(View.VISIBLE);
				mTvShopCanBouns.setText(bouns.getShopBonus());
				mPay.setShopBonusPrice(getDouleData(bouns.getShopBonus()));
			} else {
				lyShopBouns.setVisibility(View.GONE);
			}
			// 工行卡折扣
			LinearLayout lyBankCardDiscount = (LinearLayout) view.findViewById(R.id.ly_bankcard_discount);
			TextView tvBankCardDiscount = (TextView) view.findViewById(R.id.tv_bankcard_discount);
			Icbc icbc = pay.getIcbc();
			mPay.setCanDiscount(icbc.getCanDiscount());
			mPay.setCanIcbcDiscount(icbc.getCanDiscount());
			if (icbc.getCanDiscount() == IS_CAN) {
				lyBankCardDiscount.setVisibility(View.VISIBLE);
				tvBankCardDiscount.setText(icbc.getOnlinePaymentDiscount()+""); // TODO
				mPay.setIcbcDisCount(icbc.getOnlinePaymentDiscount());
				setICBCDiscount(mPay.getIcbcDisCount(), mTvBankCardCanDiscount);
			} else {
				lyBankCardDiscount.setVisibility(View.GONE);
			}
			// 首单立减
			LinearLayout lyIsFirst = (LinearLayout) view.findViewById(R.id.ly_isfirst);
			TextView tvIsFirst = (TextView) view.findViewById(R.id.tv_isfirst);
			if (pay.getIsFirst() == IS_CAN) {
				lyIsFirst.setVisibility(View.VISIBLE);
				tvIsFirst.setText(pay.getMealFirstDec());
			} else {
				lyIsFirst.setVisibility(View.GONE);
			}
			// 优惠券
			BatchCoupon coupon = pay.getCoupon();
			mPay.setMinDiscount(coupon.getMinDiscount());
			mPay.setMinReduction(coupon.getMinReduction()); // TODO
			RelativeLayout rlCouponDeduct = (RelativeLayout) view.findViewById(R.id.rl_coupon_deduct);
			RelativeLayout rlCouponDisCount = (RelativeLayout) view.findViewById(R.id.rl_coupon_disCount);
			// 还差多少钱可享受优惠
			LinearLayout lyCoupon = (LinearLayout) view.findViewById(R.id.ly_coupon);
			// 抵扣券
			if (Util.isEmpty(mShop.getBatchCouponCode())) {
				if (coupon.getReduction() == IS_CAN) {
					rlCouponDeduct.setVisibility(View.VISIBLE);
				} else {
					rlCouponDeduct.setVisibility(View.GONE);
				}
				// 折扣券
				if (coupon.getDiscount() == IS_CAN) {
					rlCouponDisCount.setVisibility(View.VISIBLE);
				} else {
					rlCouponDisCount.setVisibility(View.GONE);
				}
				if (coupon.getReduction() == IS_CAN || coupon.getDiscount() == IS_CAN) {
					lyCoupon.setVisibility(View.VISIBLE);
				} else {
					lyCoupon.setVisibility(View.GONE);
				}
				Log.d(TAG, "test pay 0");	
			} else {
				Log.d(TAG, "test pay 1 : " + mShop.getBatchCouponCode() );
				BatchCoupon couponInfo = coupon.getCouponInfo();
				if (null == couponInfo) {
					return;
				}
				flag = true;
				mPay.setAvailablePrice(getDouleData(couponInfo.getAvailablePrice())); // 达到多少可用
				mPay.setCouponType(couponInfo.getCouponType()); // 设置优惠券类型
				mPay.setBatchCouponCode(couponInfo.getBatchCouponCode()); // 优惠券编码
				mPay.setCouponCount(1);
				if (null != couponInfo) {
					if (couponInfo.getCouponType().equals(CustConst.Coupon.DEDUCT)) { // 抵扣券
						setShowCoupon(rlCouponDeduct, rlCouponDisCount,lyCoupon, CustConst.Coupon.DEDUCT);
						String couponDeductCan = "满" + couponInfo.getAvailablePrice() + "减" + couponInfo.getInsteadPrice();
						mTvCouponDeductCan.setText(couponDeductCan);
						mTvDeductNum.setText("1"); // 默认为一张
						int limitedNbr = 0;
						if (!Util.isEmpty(couponInfo.getLimitedNbr())) {
							limitedNbr = Integer.parseInt(couponInfo.getLimitedNbr());
						}
						mTvDeductMoney.setText("减免" + couponInfo.getInsteadPrice() + "元");
						mPay.setCouponPayMoney(getDouleData(couponInfo.getInsteadPrice())); // 保存折扣券最后支付的金额
						mPay.setInsteadPrice(getDouleData(couponInfo.getInsteadPrice())); // 可用金额
						mPay.setLimitedNbr(limitedNbr); // 限用多少张优惠券
						mPay.setUserCount(couponInfo.getUserCount());
						mPay.setInsteadPrice(getDouleData(couponInfo.getInsteadPrice()));
						double couponAmount = getInputMoney() < 0 ? 0 : getInputMoney();
						int limitedCount = checkUseCount(getCurrentCouponNum(), couponInfo.getUserCount(), limitedNbr, couponAmount, mPay.getAvailablePrice());
						ivDeductNumClick(limitedCount,mPay.getInsteadPrice(),mPay.getAvailablePrice());
						
					} else { // 折扣券
						setShowCoupon(rlCouponDeduct, rlCouponDisCount, lyCoupon, CustConst.Coupon.DISCOUNT);
						String couponDeductCan = "满" + couponInfo.getAvailablePrice() + "折" + couponInfo.getDiscountPercent();
						mTvCouponDiscountCan.setText(couponDeductCan);
						mPay.setCouponDisCount(getDouleData(couponInfo.getDiscountPercent())); // 设置优惠券的折扣
						double inputMoney = getInputMoney() < 0 ? 0 : getInputMoney(); // 输入金额
						getTvDisCountMoney(inputMoney, mPay.getCouponDisCount());
						
					}
				}
			}
			if (!TextUtils.isEmpty(mEdtInputPrice.getText())) {
				getPayNewMoney();
			}
		}
	}
	
	/**
	 * 折扣输入框
	 * @param inputMoney 输入金额 
	 * @param discount 折扣
	 */
	private void getTvDisCountMoney (double inputMoney ,double discount) {
		double discountMoney =  getDisCountMoney(inputMoney,discount);
		if (discountMoney == 0) {
			mTvDiscountMoney.setText("减免" + 0 + "元");
		} else {
			mTvDiscountMoney.setText("减免" + Calculate.ceilBigDecimal(discountMoney < 0 ? 0 : discountMoney) + "元");
		}
		mPay.setCouponPayMoney(discountMoney < 0 ? 0 : discountMoney); // 折扣券最后支付的金额
	}
	
	/**
	 * 计算折扣金额
	 * @param discount 折扣
	 */
	private double getDisCountMoney (double inputMoney ,double discount) {
		double discountMoney = 0;
		if (discount == 0) {
			discountMoney = 0;
		} else if (discount == 10) {
			discountMoney = 0;
		} else {
			discountMoney = Calculate.suBtraction(inputMoney, Calculate.mul(inputMoney, (Calculate.div(discount, 10))));
			Log.d(TAG, "discountMoney   >>> " +discountMoney);
		}
		return discountMoney;
	}
	
	/**
	 * 得到当前优惠券的数量
	 * @return 返回优惠券的数量
	 */
	private int getCurrentCouponNum () {
		int currentCouponNum = 0;
		try {
			if (!TextUtils.isEmpty(mTvDeductNum.getText())) {
				currentCouponNum = Integer.parseInt(mTvDeductNum.getText().toString());
			}
		} catch (Exception e) {
			Log.e(TAG, "获取优惠数量出错 error >>> " +e.getMessage());
		}
		
		return currentCouponNum;
	}
	
	/**
	 * 显示优惠券
	 * @param rlCouponDeduct 抵扣券的布局
	 * @param tvVlineDedcut 抵扣券的线
	 * @param rlCouponDisCount 折扣券
	 * @param tvVlineDiscount 折扣券的线
	 * @param couponType 优惠券类型
	 */
	private void setShowCoupon(View rlCouponDeduct, View rlCouponDisCount,View lyCoupon, String couponType) {
		lyCoupon.setVisibility(View.VISIBLE);
		if (couponType.equals(CustConst.Coupon.DEDUCT)) { //抵扣券
			rlCouponDeduct.setVisibility(View.VISIBLE);
			rlCouponDisCount.setVisibility(View.GONE);
			mLyDeductNum.setVisibility(View.VISIBLE);
			mIvSkipDeduct.setVisibility(View.GONE);
		} else if (couponType.equals(CustConst.Coupon.DISCOUNT)) { // 折扣券
			rlCouponDeduct.setVisibility(View.GONE);
			rlCouponDisCount.setVisibility(View.VISIBLE);
			mLyDiscountNum.setVisibility(View.VISIBLE);
			mIvSkipDiscount.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置活动
	 * @param view 视图
	 * @param rlId RelativeLayout的Id
	 * @param tvIdTextView 的Id
	 * @param isAct 是否有活动的判断
	 * @param actInfo 活动信息
	 */
	private void setActInfo(View view, int rlId, int tvId, int isAct, String actInfo) {
		RelativeLayout rlShopAct = (RelativeLayout) view.findViewById(rlId);
		TextView tvShopAct = (TextView) view.findViewById(tvId);
		if (isAct == IS_CAN) { // 1就显示
			rlShopAct.setVisibility(View.VISIBLE);
			tvShopAct.setText(actInfo);
		} else {
			rlShopAct.setVisibility(View.GONE);
		}
	}

	/**
	 * String类型的数字转换成Double类型的数据
	 * @param StrData 字符串类型的数字
	 * @return double类型的数据
	 */
	private double getDouleData(String StrData) {
		double dataNum = 0;
		try {
			if (!Util.isEmpty(StrData)) {
				dataNum = Double.parseDouble(StrData);
			}
		} catch (Exception e) {
			Log.e(TAG, "String类型的数字转换成Double类型的数据  error >> " + e.getMessage());
		}
		return dataNum;
	}

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (COUPONS_FLAG == requestCode && resultCode == getMyActivity().RESULT_OK) {
			flag = true;
			String couponType = data.getStringExtra("COUPON_TYPE");
			String batchCouponCode = data.getStringExtra("BATCHCOUPONCODE"); //string 优惠券批次编码
			String deduct = data.getStringExtra("FAVOURABLE_DUCTION"); //int  抵扣券优惠的金额  = 数量*减
			String description = data.getStringExtra("DISCRIBLE_DUCTION");//string 抵扣券的描述
			Log.d(TAG, "description >>> " + description);
			mPay.setBatchCouponCode(batchCouponCode); // 优惠券编码
			mPay.setCouponPayMoney(getDouleData(deduct)); // 优惠券最后支付的金额
			mPay.setCouponType(couponType); // 设置优惠券类型
			getPayNewMoney(); // 计算金额
			if (Util.isEmpty(batchCouponCode)) {
				mPay.setBatchCouponCode(""); // 优惠券编码
				clearCouponInfo(mLyDeductNum, mIvSkipDeduct, mTvCouponDeductCan); // 清除抵扣券的数据
			} else {
				if (!Util.isEmpty(couponType) && couponType.equals(CustConst.Coupon.DEDUCT)) { // 抵扣券
					clearCouponInfo(mLyDiscountNum, mIvSkipDiscount, mTvCouponDiscountCan); // 清除折扣券的数据
					String deductCount = data.getStringExtra("COUNT"); //int 抵扣券数量
					mPay.setCouponCount(Integer.parseInt(deductCount));
					mTvDeductNum.setText(deductCount+"张");
					mTvCouponDeductCan.setText(description);
					mTvDeductMoney.setText("减免" + deduct + "元");
					mIvDeductAdd.setVisibility(View.GONE);
					mIvDeductSub.setVisibility(View.GONE);
					mIvSkipDeduct.setVisibility(View.GONE);
					mLyDeductNum.setVisibility(View.VISIBLE);
				} else { // 折扣券
					mPay.setCouponCount(1);
					clearCouponInfo(mLyDeductNum, mIvSkipDeduct, mTvCouponDeductCan); // 清除抵扣券的数据
					mIvSkipDiscount.setVisibility(View.GONE);
					mLyDiscountNum.setVisibility(View.VISIBLE);
					mTvCouponDiscountCan.setText(description);
					mTvDiscountMoney.setText("减免" + deduct + "元");
				}
			}
		}
	}
	
	/**
	 * 隐藏数据
	 * @param lyCouponNum 显示数据的布局
	 * @param ivSkipCoupon 跳转的箭头
	 * @param tvCouponCouponCan 次优惠券可以干嘛
	 */
	private void clearCouponInfo (View lyCouponNum,View ivSkipCoupon,TextView tvCouponCouponCan) {
		lyCouponNum.setVisibility(View.GONE);
		ivSkipCoupon.setVisibility(View.VISIBLE);
		tvCouponCouponCan.setText("");
	}
	
	/**
	 * 控件的点击事件
	 * @param view
	 */
	@OnClick({R.id.backup,R.id.rl_coupon_deduct,R.id.rl_coupon_disCount,R.id.next})
	private void ivControlsClick (View view) {
		switch (view.getId()) {
		case R.id.backup: // 回退时间
			getMyActivity().finish();
			break;
		case R.id.rl_coupon_deduct: // 抵扣券 3
			Log.d(TAG, "getMinReduction >>> " + mPay.getMinReduction() + " 元" + ",, 折扣券 >> " +mPay.getMinDiscount());
			if (mPay.getMinReduction() <= getInputMoney()) {
				skipCouponSelectActivity(CustConst.Coupon.DEDUCT);
			} else {
				Util.showToastZH(Util.getString(R.string.not_enough_money) + Calculate.floorBigDecimal(Calculate.suBtraction(mPay.getMinReduction(), getInputMoney())) + Util.getString(R.string.can_use_coupon));
			}
			break;
		case R.id.rl_coupon_disCount: // 折扣券
			if (mPay.getMinDiscount() <= getInputMoney()) {
				skipCouponSelectActivity(CustConst.Coupon.DISCOUNT);
			} else {
				Util.showToastZH(Util.getString(R.string.not_enough_money) +Calculate.floorBigDecimal(Calculate.suBtraction(mPay.getMinDiscount(), getInputMoney())) + Util.getString(R.string.can_use_coupon));
			}
			break;
		case R.id.next: // 下一步 
			if (mPay.getIsFirst() == IS_CAN && getInputMoney(mEdtPayPrice) < 1) { // 首单立减 支付金额小于1
				
				DialogUtils.showDialog(getMyActivity(), Util.getString(R.string.cue), Util.getString(R.string.is_fisrt_sub), Util.getString(R.string.ok), Util.getString(R.string.no), new DialogUtils().new OnResultListener() {
					@Override
					public void onOK() {
						super.onOK();
						getNewPrice(PAY_ONLINE_CODE); // 工行卡支付 
						mEdtPayPrice.setText("1");
					}
				});
			} else {
				getNewPrice(PAY_ONLINE_CODE); // 工行卡支付 
			}
			break;

		default:
			break;
		}
	}
	
	/**
	 * 支付
	 */
	private void getNewPrice (final int payType) {
		
		String drinkPrice = mCkbNoDiscount.isChecked() ? getInputMoney(mNoDiscountPrice)+"" : "0";
		new GetNewPriceTask(getMyActivity(), new GetNewPriceTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mBtnNext.setEnabled(true);
				if (result == null) {
					return;
				} else {
					double newPrice = 0;
					try {
						newPrice = Double.parseDouble(result.get("newPrice").toString().replace(",", ""));
						mPay.setNewPrice(newPrice);
					} catch (Exception e) {
						Log.e(TAG, "shoppay");
					}
					Log.d(TAG, "getNewPrice");
					Message message = Message.obtain();
					if (mIsCardPay) {
						message.what = BANK_PAY;
					} else {
						message.what = PRODUCT_PAY;
					}
					mHandler.sendMessage(message);
				}
			}
		}).execute(mShop.getShopCode(), mEdtInputPrice.getText().toString(), mPay.getBatchCouponCode(), mPay.getCouponCount()+"",
				mPay.getPlatBonus()+"",mPay.getShopBonus()+"",String.valueOf(payType),drinkPrice);
	}
	
	/**
	 * 跳转
	 * @param couponType
	 */
	private void skipCouponSelectActivity (String couponType) {
		if (Util.isEmpty(mShop.getBatchCouponCode())) {
			if (TextUtils.isEmpty(mEdtInputPrice.getText())) {
				Util.getContentValidate(R.string.csm_money_null);
				return;
			}
			Intent intent = new Intent(getMyActivity(), CouponSelectActivity.class);
			intent.putExtra(CouponSelectActivity.SHOPE_CODE, mShop.getShopCode());
			intent.putExtra(CouponSelectActivity.COUPON_TYPE, couponType);
			intent.putExtra(CouponSelectActivity.CONSUME_AMOUNT,getInputMoney() + "");
			getMyActivity().startActivityForResult(intent, COUPONS_FLAG);
		}
	}
	
	/**
	 * 输入后
	 */
	@Override
	public void afterTextChanged(Editable s) {
		double inputMoney = getInputMoney() < 0 ? 0 : getInputMoney();;
		// 监测输入金额变化时优惠券金额的变化
		if (flag) {
			if (Util.isEmpty(mShop.getBatchCouponCode())) {
				mPay.setCouponPayMoney(0); // 优惠券的支付金额也清零
				mPay.setCouponCount(0);
				mPay.setBatchCouponCode("");
				if (mPay.getCouponType().equals(CustConst.Coupon.DISCOUNT)) {
					clearCouponInfo(mLyDiscountNum, mIvSkipDiscount, mTvCouponDiscountCan); // 清除折扣券
				} else if (mPay.getCouponType().equals(CustConst.Coupon.DEDUCT)){
					clearCouponInfo(mLyDeductNum, mIvSkipDeduct, mTvCouponDeductCan); // 清除抵扣券的数据
				}
			} else {
				if (mPay.getCouponType().equals(CustConst.Coupon.DISCOUNT)) {
					getTvDisCountMoney(inputMoney, mPay.getCouponDisCount());
				} else if (mPay.getCouponType().equals(CustConst.Coupon.DEDUCT)){
					mPay.setCouponPayMoney(mPay.getInsteadPrice()); // 优惠券的支付金额也清零
					mTvDeductNum.setText("1"); // 改变金额 就重新选择输入优惠券数量
					mTvDeductMoney.setText("减免" + (int)mPay.getInsteadPrice() + "元");
				}
			}
		}
		Calculate.getStrInputMoney (mNoDiscountPrice);
		if (mCkbNoDiscount.isChecked()) {
			if (getInputMoney(mNoDiscountPrice) > getInputMoney(mEdtInputPrice)) {
				mNoDiscountPrice.setText(mEdtInputPrice.getText().toString());
				if (TextUtils.isEmpty(mEdtInputPrice.getText()) || getInputMoney(mEdtInputPrice) == 0) {
					Util.showToastZH("请先输入消费金额");
				}
				Calculate.cursorLast(mNoDiscountPrice); // 光标位于最后
			}
		}
		// 监测输入金额  0 输入金额不能为零
		if (TextUtils.isEmpty(mEdtInputPrice.getText()) || "0".equals(mEdtInputPrice.getText().toString())) {
			Log.d(TAG, "flagisFlag >>>> 1111  >>>> ");
			mEdtPayPrice.setText("0");// 当输入金额为空的时候支付金额也为空
			mTvCardDiscount.setText("0"); // 初始化会员卡的值
			mTvBankCardCanDiscount.setText("0"); // 工行卡打折
			getBtnStatus(NO_CLICK);
		} else {
			Calculate.getStrInputMoney (mEdtInputPrice);
			if (!Util.isEmpty(mShop.getBatchCouponCode())) {
				if (flag) {
					int limitedCount = checkUseCount(getCurrentCouponNum(), mPay.getUserCount(), mPay.getLimitedNbr(), inputMoney, mPay.getAvailablePrice());
					ivDeductNumClick(limitedCount,mPay.getInsteadPrice(),mPay.getAvailablePrice());
				}
			} else {
				getBtnStatus(CAN_CLICK);
			}
			// 会员卡打折
			if (mPay.getCanUseCard() == IS_CAN) { 
				setCardDiscount(mPay.getCardDisCount(), mTvCardDiscount);
				Log.d(TAG, "discountMoney  >>> discountMoney  huiyuankai  >>> " );
			}
			getPayNewMoney();
		}
	}
	
	/**
	 * 输入钱
	 */
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	/**
	 * 输入中
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	/**
	 * 判断按钮是否可点击
	 * @param status  0---按钮处于不可点击的状态 1---按钮处于可以点击的状态
	 */
	private void getBtnStatus(int status) {
		if (NO_CLICK == status) { // 输入金额为空的时候 不能支付
			mBtnNext.setTextColor(getResources().getColor(R.color.tv_content_color));
			mBtnNext.setBackgroundResource(R.drawable.shape_paybill);
			mBtnNext.setEnabled(false);
		} else { // OK
			mBtnNext.setTextColor(getResources().getColor(R.color.white));
			mBtnNext.setEnabled(true);
			mBtnNext.setBackgroundResource(R.drawable.login_btn);
		}
	}
	
	/**
	 * 监听商家红包金额
	 */
	private TextWatcher bounsShopTextChange = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			double edtBounsMoney = getInputMoney(mEdtShopBouns);
			if (mPay.getPlatBonusPrice() <= 0) {
				Util.getContentValidate(R.string.use_bonus_zero);
				mPay.setShopBonus(0);
			} if (getInputMoney() <= 0) { // 可参与折扣的金额小于等于0
				Util.getContentValidate(R.string.no_discount);
				mPay.setShopBonus(0);
			} else {
					if (edtBounsMoney > mPay.getShopBonusPrice()) {
						mTvShopCanBouns.setText(0+"");
						Util.getContentValidate(R.string.insufficient_amount);
						mPay.setShopBonus(mPay.getShopBonusPrice());
						mEdtShopBouns.setText(Calculate.subZeroAndDot(mPay.getShopBonusPrice()+""));
						Calculate.cursorLast(mEdtShopBouns); // 光标位于最后
					} else {
						mTvShopCanBouns.setText(Calculate.ceilBigDecimal(Calculate.suBtraction(mPay.getShopBonusPrice(), edtBounsMoney)) + "");
						mPay.setShopBonus(edtBounsMoney);
					}
			}
			
			// 计算最后金额
			getPayNewMoney();
		}
	};	
	
	/**
	 * 监听平台红包金额
	 */
	private TextWatcher bounsPlatTextChange = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			Calculate.getStrInputMoney (mEdtPlatBouns);
			double edtBounsMoney = getInputMoney(mEdtPlatBouns);
			if (mPay.getPlatBonusPrice() <= 0) {
				Util.getContentValidate(R.string.use_huiquan_zero);
				mPay.setPlatBonus(0);
			} if (getInputMoney() <= 0) { // 可参与折扣的金额小于等于0
				Util.getContentValidate(R.string.no_discount);
				mPay.setPlatBonus(0);
			} else {
				if (edtBounsMoney > mPay.getPlatBonusPrice()) {
					mTvPlatCanBouns.setText(0+"");
					Util.getContentValidate(R.string.huiquan_insufficient_amount);
					mPay.setPlatBonus(mPay.getPlatBonusPrice());
					mEdtPlatBouns.setText(Calculate.subZeroAndDot(mPay.getPlatBonusPrice()+""));
					Calculate.cursorLast(mEdtPlatBouns); // 光标位于最后
				} else {
					mTvPlatCanBouns.setText(Calculate.ceilBigDecimal(Calculate.suBtraction(mPay.getPlatBonusPrice(), edtBounsMoney)) + "");
					mPay.setPlatBonus(edtBounsMoney);
				}
			}
			getPayNewMoney();
		}
	};	
	
	/**
	 * 最后支付金额
	 * @param inputMoney 输入金额
	 * @param payPrice 最后支付金额
	 */
	private void setPayMoney (double inputMoney,double payPrice) {
		if (inputMoney > mPay.getMinRealPay() ) {
			getBtnStatus(payPrice < mPay.getMinRealPay() ? NO_CLICK : CAN_CLICK);
		} else {
			if (inputMoney < 0) {
				getBtnStatus(NO_CLICK);
			} else {
				getBtnStatus(CAN_CLICK);
			}
		}
	}
	
	/**
	 * 计算最后的支付金额
	 */
	private void getPayNewMoney () {
		Log.d(TAG, "zhifu  >>>>  ");
		if (mPay.getCanDiscount() == IS_CAN) { 
			setICBCDiscount(mPay.getIcbcDisCount(), mTvBankCardCanDiscount);
		}
		double inputMoney = getInputMoney() < 0 ? 0 : getInputMoney(); // 输入金额
		double totalMoney = 0;
		if (mPay.getIsFirst() == IS_CAN) { // 首单减10元
			totalMoney = Calculate.add(mPay.getCouponPayMoney(),
					Calculate.add(ViewSolveUtils.getInputMoney(mTvCardDiscount),
					Calculate.add(mPay.getPlatBonus(),
					Calculate.add(mPay.getShopBonus(),
					Calculate.add(ViewSolveUtils.getInputMoney(mTvBankCardCanDiscount),getDouleData(mPay.getMealFirstDec())))))); 
		} else {
			totalMoney = Calculate.add(mPay.getCouponPayMoney(),
					Calculate.add(ViewSolveUtils.getInputMoney(mTvCardDiscount),
					Calculate.add(mPay.getPlatBonus(),
					Calculate.add(mPay.getShopBonus(),ViewSolveUtils.getInputMoney(mTvBankCardCanDiscount))))); 
		}
		double lastPayMoney = Calculate.suBtraction(inputMoney, totalMoney) < 0 ? 0 : Calculate.suBtraction(inputMoney, totalMoney);
		double newPayMoney = mCkbNoDiscount.isChecked() ?  Calculate.add(lastPayMoney, getInputMoney(mNoDiscountPrice)) : lastPayMoney;
		// 最小限额
		if (Util.isEmpty(mShop.getBatchCouponCode())) {
			if (mPay.getIsFirst() == IS_CAN ) { // 首单立减
				getBtnStatus(CAN_CLICK);
			} else {
				setPayMoney(inputMoney, newPayMoney);
			}
		} else {
			if (getInputMoney() < 0 || inputMoney < mPay.getAvailablePrice()) {
				Util.showToastZH("未达到优惠券使用下线");
				getBtnStatus(NO_CAN);
			} else {
				setPayMoney(inputMoney, newPayMoney); 
			}
		}
		
		Log.d(TAG, "newPayMoney  >>> " + newPayMoney);

		if (newPayMoney <= 0 ) {
			// 首单立减
			getBtnStatus(mPay.getIsFirst() == IS_CAN  ? CAN_CLICK : NO_CAN);
			if (mPay.getIsFirst() == IS_CAN) {
				mEdtPayPrice.setText("1");
			} else {
				mEdtPayPrice.setText("0");
			}
		} else {
			if (mPay.getIsFirst() == IS_CAN) {
				mEdtPayPrice.setText(newPayMoney <= 1 ? "1" : Calculate.floorBigDecimal(newPayMoney) + "");
			} else {
				mEdtPayPrice.setText(Calculate.floorBigDecimal(newPayMoney) + "");
			}
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(ShopPayBillFragment.class.getSimpleName());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(ShopPayBillFragment.class.getSimpleName()); // 统计页面
	}
}
