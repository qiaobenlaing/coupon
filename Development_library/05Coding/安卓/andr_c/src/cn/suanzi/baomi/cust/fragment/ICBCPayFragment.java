package cn.suanzi.baomi.cust.fragment;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.Pay;
import cn.suanzi.baomi.base.pojo.Shop;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.base.utils.ActivityUtils;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.Calculate;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.activity.ICBCCardPayActivity;
import cn.suanzi.baomi.cust.activity.ICBCOnlinePayActivity;
import cn.suanzi.baomi.cust.model.AddOrderTask;
import cn.suanzi.baomi.cust.model.BankCardPayTask;
import cn.suanzi.baomi.cust.model.GetBestUserCardTask;
import cn.suanzi.baomi.cust.model.MposTask;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 工行快捷支付
 * 
 * @author Zhonghui.Dong
 * 
 */
public class ICBCPayFragment extends Fragment {
	private static final String TAG = ICBCPayFragment.class.getSimpleName();
	public static final String BANK_DISCOUNT = "bank_discount";
	public static final String PAY_OBJ = "payObj";
	public static final String SHOP_OBJ = "shopObj";
	public static final String N_BUY = "nbuy";
	public static final String SHOP_CODE = "shop_code";
	public static final String SHOP_NAME = "shop_name";
	public static final String SHOP_NEW_PRICE = "shop_new_price";
	public static final String COUPONS_CODE = "coupons_code";
	public static final String COUPONS_PRICE = "coupons_price";
	public static final String ORDER_CODE = "order_code";
	public static final String PAY_CARD = "0";
	public static final String PAY_MONEY = "1";
	public static final String NO_ORDER = "0";
	

	/*
	 * private String shopCode, shopName, price, couponsCode, couponsPrice; private String shopBonusPrice,
	 * platBonusPrice, bankAccountCode; private double newPrice;
	 */
	private String mUserCardCode;
	
	
	/** 获得一个用户信息对象 **/
	private UserToken mUserToken;
	private String mUserCode;
	/** 用户登录后获得的令牌 **/
	private String mTokenCode;

	@ViewInject(R.id.all_icbc_discount)
	private LinearLayout mAllDiscount;
	@ViewInject(R.id.iv_icbc_shop_arrow)
	private ImageView mArrowr;
	@ViewInject(R.id.shop_name)
	private TextView mTvShopName;
	@ViewInject(R.id.price)
	private TextView mTvPrice;
	@ViewInject(R.id.total_dcprice)
	private TextView mDcPrice;
	@ViewInject(R.id.new_price)
	private TextView mTvnewPrice;
	@ViewInject(R.id.bank_price)
	private TextView mBankDisCountTV;
	@ViewInject(R.id.card_pay)
	private Button mBtnCardPay;
	@ViewInject(R.id.coupons_price)
	private TextView mCouponsPrice;
	@ViewInject(R.id.card_dcprice)
	private TextView mCardDcprice;
	@ViewInject(R.id.bouns_price_shop)
	private TextView mBounShopPrice;
	@ViewInject(R.id.bouns_price_huiquan)
	private TextView mBounHuiquanPrice;
	private Pay mPay = null;
	private String mNBuy = "";
	private int mTime = 0;
	
	private double mBankCardDiscount;//银行优惠的钱
	/*
	 * @ViewInject(R.id.online_pay) private Button mbtnOnLinePay;
	 */
	/** 订单编码 */
	private String mOrderCode;

	/**
	 * 需要传递参数时有利于解耦
	 */
	
	/**标识是否显示折扣信息  默认不显示*/
	private boolean showDiscountFlag = false; 
	
	public static ICBCPayFragment newInstance() {
		Bundle args = new Bundle();
		ICBCPayFragment fragment = new ICBCPayFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_shop_icbc_pay, container, false);
		ViewUtils.inject(this, v);
		
		//****记录买单流程*********
		ActivityUtils.add(getMyActivity());
		Util.addActivity(getMyActivity());
		Util.addLoginActivity(getMyActivity());
		init(v);
		initData(v);
		// 优先采用联系人的图标，如果不存在则采用该应用的图标
		return v;
	}
	
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	private void initData(View v) {
		mBtnCardPay.setVisibility(View.GONE); // TODO 隐藏线上刷卡
		if (mPay != null) {
			mTvShopName.setText(mPay.getShopName());
			// 实际支付金额
			mTvPrice.setText(Util.saveTwoDecima(mPay.getPrice()) + "");
			mCouponsPrice.setText(Util.saveTwoDecima(mPay.getCouponInsteadPrice()) + "");
			mCardDcprice.setText(Util.saveTwoDecima(mPay.getCardInsteadPrice()) + "");
			
			// 商家红包红包金额抵扣Calculate.add(mPay.getShopBonusPrice(), mPay.getPlatBonusPrice())
			mBounShopPrice.setText(String.valueOf(Util.saveTwoDecima(mPay.getShopBonusPrice())));
			//惠圈红包
			mBounHuiquanPrice.setText(String.valueOf(Util.saveTwoDecima(mPay.getPlatBonusPrice())));
			
			// 折扣数
			double ddcPrice = Calculate.suBtraction(mPay.getPrice(), mPay.getNewPrice());
			mDcPrice.setText(Calculate.ceilBigDecimal(ddcPrice) + "");
			// 支付新金额
			mTvnewPrice.setText(Util.saveTwoDecima(mPay.getNewPrice()) + "");
		}
		
		//显示银行卡优惠信息
		mBankDisCountTV.setText(String.format("%.2f", mBankCardDiscount));//两位小数
	}

	private void init(View v) {
		mPay = (Pay) getMyActivity().getIntent().getSerializableExtra(PAY_OBJ);
		
		mBankCardDiscount = getMyActivity().getIntent().getDoubleExtra(BANK_DISCOUNT, 0.0);
		
		
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mUserCode = mUserToken.getUserCode();
		mTokenCode = mUserToken.getTokenCode();// 用户登录后获得的令牌
	}

	/**
	 * 生产订单号
	 */
	private void addOrder() {
		String params[] = { mPay.getNewPrice() + "", mPay.getShopCode() };
		// mbtnOnLinePay.setEnabled(false);
		new AddOrderTask(getMyActivity(), new AddOrderTask.Callback() {
			@Override
			public void getResult(String orderCode) {
				if (orderCode != null) {
					mOrderCode = orderCode;
				} else {
					return;
				}
			}
		}).execute(params);
		
	}

	/**
	 * 获取用户会员卡最高级别信息
	 */
	private void getBestUserCard() {
		new GetBestUserCardTask(getMyActivity(), new GetBestUserCardTask.Callback() {

			@Override
			public void getResult(JSONArray result) {
				if (result != null) {
					for (int i = 0; i < result.size(); i++) {
						JSONObject UserObj = (JSONObject) result.get(i);
						mUserCardCode = UserObj.get("userCardCode").toString();
					}
				}

			}
		}).execute(mPay.getShopCode());

	}

	@OnClick({ R.id.backup, R.id.card_pay, R.id.online_pay ,R.id.rl_all_discount})
	private void click(View v) {
		switch (v.getId()) {
		case R.id.backup:
			getMyActivity().finish();
			break;
		/*
		 * case R.id.online_pay: // 生产订单号 addOrder(); if (!Util.isEmpty(sOrderCode)) { getBankCardPay(); } else { //
		 * 正在生产订单 Util.getContentValidate(getActivity(), getString(R.string.orderimg));
		 * 
		 * } break;
		 */
		case R.id.card_pay:
			// if (Util.isEmpty(mOrderCode)) {
			mBtnCardPay.setEnabled(false);
			getMpos();
			/*
			 * } else { if ("0".equals(mOrderCode)) { break; } intent = new
			 * Intent(getActivity().getApplicationContext(), ICBCCardPayActivity.class);
			 * intent.putExtra(ICBCCardFragment.ORDER_CODE, mOrderCode); intent.putExtra(ICBCCardFragment.ORDER_STYPE,
			 * PAY_CARD); intent.putExtra(ICBCCardFragment.SHOP_PRICE, mPay.getNewPrice());
			 * intent.putExtra(ICBCCardFragment.SHOP_URL, mPay.getLogoUrl()); getActivity().startActivity(intent); }
			 */

			break;

		/*
		 * case R.id.cash_pay: addOrder(); if (Util.isEmpty(sOrderCode)) { // 正在生产订单
		 * Util.getContentValidate(getActivity(), getString(R.string.orderimg)); } else { intent = new
		 * Intent(getActivity().getApplicationContext(), ICBCCashPayActivity.class);
		 * getActivity().startActivity(intent); } break;
		 */

		case R.id.online_pay:
			// 友盟统计
			MobclickAgent.onEvent(getMyActivity(), "paydetail_card_pay");
			//在线支付的异步任务
			payOnlineTask();	
			break;
			
			
		case R.id.rl_all_discount: 
			//显示或者隐藏折扣信息
			showDiscountFlag = !showDiscountFlag;
			if(showDiscountFlag){  //展示打折信息
				mAllDiscount.setVisibility(View.VISIBLE);
				mArrowr.setImageResource(R.drawable.up_arrow);
			}else{  //隐藏折扣信息
				mAllDiscount.setVisibility(View.GONE);
				mArrowr.setImageResource(R.drawable.down_arrow);
			}
			break;
		}
	}

	/**
	 * mpos的支付
	 */
	private void getMpos() {
		String[] params = { mPay.getShopCode(), mPay.getCouponCode(), mPay.getPlatBonusPrice() + "",
				mPay.getShopBonusPrice() + "", mPay.getPrice() + "" };
		new MposTask(getMyActivity(), new MposTask.Callback() {

			@Override
			public void getResult(JSONObject result) {
				mBtnCardPay.setEnabled(true);
				if (result == null) {
					mOrderCode = NO_ORDER; // 返回订单编码为空
					// Util.getContentValidate(getActivity(), getString(R.string.pay_error));
				} else {
					try {
						mOrderCode = result.get("consumeCode").toString();
						Shop shop = (Shop) getMyActivity().getIntent().getSerializableExtra(SHOP_OBJ);
						Intent intent = new Intent(getMyActivity().getApplicationContext(), ICBCCardPayActivity.class);
						intent.putExtra(ICBCCardFragment.ORDER_CODE, mOrderCode);
						intent.putExtra(ICBCCardFragment.ORDER_STYPE, PAY_CARD);
						intent.putExtra(ICBCCardFragment.SHOP_URL, mPay.getLogoUrl());
						intent.putExtra(ICBCCardFragment.SHOP_CODE, mPay.getShopCode());
						intent.putExtra(ICBCCardFragment.SHOP_OBJ, shop);
						getMyActivity().startActivity(intent);
					} catch (Exception e) {
						mOrderCode = NO_ORDER;
					}
				}
			}
		}).execute(params);
	}
	
	
	/**
	 * 在线支付的异步
	 */
	public  void payOnlineTask(){
		
		
		new BankCardPayTask(getMyActivity(), new BankCardPayTask.Callback() {

			@Override
			public void getResult(String result) {
				if(result!=null){  //在线支付成功  result----返回的订单编码##订单编号
					String[] split = result.split("##");
					String consumeCode = split[0];
					String orderNbr = split[1];
					Intent intent = new Intent(getMyActivity().getApplicationContext(), ICBCOnlinePayActivity.class);
					//intent.putExtra(ICBCOnlinePayFragment.CONSUME_CODE, mOrderCode);//订单号-------
					intent.putExtra(ICBCOnlinePayFragment.SHOP_CODE, mPay.getShopCode());
					intent.putExtra(ICBCOnlinePayFragment.SHOP_NAME, mPay.getShopName());
					intent.putExtra(ICBCOnlinePayFragment.REAL_PAY, String.valueOf(mPay.getNewPrice()));
					intent.putExtra(ICBCOnlinePayFragment.REAL_CONSUMECODE, consumeCode);
					intent.putExtra(ICBCOnlinePayFragment.ORDERNBR, orderNbr);
					startActivity(intent);
				}else{
					//TODO
				}
				
			}
		}).execute(mUserCode,mPay.getShopCode(),String.valueOf(mPay.getPrice())
				,mPay.getCouponCode(),String.valueOf(mPay.getPlatBonusPrice()),
				String.valueOf(mPay.getShopBonusPrice()),mTokenCode);
	}

	/*
	 * private void getBankCardPay() {
	 * 
	 * new BankCardPayTask(getActivity(), new BankCardPayTask.Callback() {
	 * 
	 * @Override public void getResult(JSONObject result) { // mbtnOnLinePay.setEnabled(true); if (result != null) {
	 * String paySucc = ErrorCode.SUCC+""; String code = result.get("code").toString(); if (paySucc.equals(code)) {
	 * String consumeCode = result.get("consumeCode").toString(); // String realPay = (String) result.get("realPay");
	 * Intent intent = new Intent(getActivity().getApplicationContext(), ICBCOnlinePayActivity.class);
	 * intent.putExtra(ICBCOnlinePayFragment.SHOP_CODE, shopCode); intent.putExtra(ICBCOnlinePayFragment.SHOP_NAME,
	 * shopName); intent.putExtra(ICBCOnlinePayFragment.CONSUME_CODE, consumeCode); Log.d(TAG,
	 * "consumeCode="+consumeCode); intent.putExtra(ICBCOnlinePayFragment.REAL_PAY, newPrice+"");
	 * getActivity().startActivity(intent); } else { int failCode = Integer.parseInt(code); if (failCode ==
	 * ErrorCodes.INPUT_CSM_MONEY) { // 请输入消费金额 Util.getContentValidate(getActivity(),
	 * getString(R.string.input_csm_money)); } else if (failCode == ErrorCodes.ERROR_CSM_MONEY) { // 消费金额不正确
	 * Util.getContentValidate(getActivity(), getString(R.string.error_csm_money)); } else if (failCode ==
	 * ErrorCodes.NO_USE_BOUNS) { // 红包不可用 Util.getContentValidate(getActivity(), getString(R.string.no_use_bouns));
	 * 
	 * } else if (failCode == ErrorCodes.BOUNS_EXPIRED) { // 红包过期 Util.getContentValidate(getActivity(),
	 * getString(R.string.bouns_expired)); } else if (failCode == ErrorCodes.COUPON_EXPIRED) { // 优惠券过期
	 * Util.getContentValidate(getActivity(), getString(R.string.coupon_expired)); } else if (failCode ==
	 * ErrorCodes.NO_USE_COUPON) { // 优惠券不可用 Util.getContentValidate(getActivity(), getString(R.string.no_use_coupon));
	 * } else if (failCode == ErrorCodes.ERROR_ORDER_CODE) { // 订单编码有错误 Util.getContentValidate(getActivity(),
	 * getString(R.string.error_ordercode)); } else if (failCode == ErrorCodes.NO_USE_CARD) { // 用户会员卡不可用
	 * Util.getContentValidate(getActivity(), getString(R.string.no_use_card)); }
	 * 
	 * } } } }).execute(sOrderCode, mUserCardCode,couponsCode,bonusCode);
	 * 
	 * }
	 */

	/**
	 * 友盟统计
	 */
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(ICBCPayFragment.class.getSimpleName());
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(ICBCPayFragment.class.getSimpleName()); // 统计页面
	}
}
