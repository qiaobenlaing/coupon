package com.huift.hfq.cust.fragment;


import net.minidev.json.JSONObject;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Activitys;
import com.huift.hfq.base.pojo.Bonus;
import com.huift.hfq.base.pojo.PromotionBuy;
import com.huift.hfq.base.pojo.PromotionPrice;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.Calculate;
import com.huift.hfq.cust.R;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huift.hfq.cust.activity.ICBCOnlinePayActivity;
import com.huift.hfq.cust.adapter.PromotionPrcieAdaapter;
import com.huift.hfq.cust.model.GetInfoPreActInfoTask;
import com.huift.hfq.cust.model.SubmitActOrderTask;
import com.umeng.analytics.MobclickAgent;
/**
 * 购买活动
 * @author yingchen
 */
public class BuyPromotionFragment extends Fragment implements OnClickListener{
	protected static final String TAG = BuyPromotionFragment.class.getSimpleName();
	/** 活动的编码*/
	public static final String ACTIVITY = "activityCode";
	
	/**活动内容*/
	private TextView mContentTextView;
	
	/**价格列表*/
	private ListView mPriceListView;
	
	/**订单总价格*/
	private TextView mTotalPriceTextView;
	
	/**订购人姓名*/
	private EditText mNameEditText;
	
	/**订购人电话*/
	private EditText mPhoneEditText;
	
	/**平台红包*/
	private LinearLayout mPlatBonudsLinearLayout;
	/**拥有平台红包金额*/
	private TextView mPlatBonusTextView;
	/**使用的平台红包*/
	private EditText mPlatBounsEditText;
	
	/**商家红包*/
	private LinearLayout mShopBonudLinearLayout;
	/**拥有商家红包金额*/
	private TextView mShopBonusTextView;
	
	/**使用的商家红包*/
	private EditText mShopBounsEditText;
	
	/**购买按钮*/
	private Button mBuyButtton;
	
	/**请求API返回的对象*/
	private PromotionBuy promotionBuy;
	
	/**总金额*/
	private double mTotalPrice = 0;
	
	private Bonus userBonusInfo;
	
	public static BuyPromotionFragment newInstance() {
		Bundle args = new Bundle();
		BuyPromotionFragment fragment = new BuyPromotionFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//****记录买单流程*********
		ActivityUtils.add(getActivity());
		View view = inflater.inflate(R.layout.fragment_buy_promotion, container, false);
		
		initView(view);
		
		initData();
		return view;
	}
	
	/**
	 * 初始化数据  获取活动购买详情
	 */
	public void  initData(){
		
		String actCode = getActivity().getIntent().getStringExtra(ACTIVITY);
		if(Util.isEmpty(actCode)){
			actCode = "";
		}
		new GetInfoPreActInfoTask(getActivity(), new GetInfoPreActInfoTask.CallBack() {
			
			@Override
			public void getResult(JSONObject result) {
				if(null!=result){
					promotionBuy = Util.json2Obj(result.toString(), PromotionBuy.class);
					showPriceSpec(promotionBuy.getActInfo());
					userBonusInfo = promotionBuy.getUserBonusInfo();
					showBonuds();
				}else{
					Util.showToastZH("服务器异常，请联系相关人员");
				}
			}
		}).execute(actCode);
	}
	/**
	 * 显示红包信息
	 * @param userBonusInfo
	 */
	private void showBonuds() {
		if(Double.parseDouble(userBonusInfo.getPlatBonus())>0){
			mPlatBonudsLinearLayout.setVisibility(View.VISIBLE);
			mPlatBonusTextView.setText(userBonusInfo.getPlatBonus()+"");
		}else{
			mPlatBonudsLinearLayout.setVisibility(View.GONE);
		}
		
		if(Double.parseDouble(userBonusInfo.getShopBonus())>0){
			mShopBonudLinearLayout.setVisibility(View.VISIBLE);
			mShopBonusTextView.setText(userBonusInfo.getShopBonus()+"");
		}else{
			mShopBonudLinearLayout.setVisibility(View.GONE);
		}
	}
	/**
	 * 显示价格的规格
	 */
	private void showPriceSpec(Activitys activitys){
		if(null == activitys.getFeeScale()||activitys.getFeeScale().size()== 0){
			Util.showToastZH("该活动未设置价格规格，请联系商家");
		}else{
			mContentTextView.setText(activitys.getTxtContent());
			mPriceListView.setAdapter(new PromotionPrcieAdaapter(activitys, getActivity(),new PromotionPrcieAdaapter.CallBack() {
				@Override
				public void getTotalPrice(double totalPrice) {
					mTotalPrice = totalPrice;
					mTotalPriceTextView.setText(String.valueOf(Calculate.ceilBigDecimal(mTotalPrice)));
				}
			}));	
			
		}
	}
	
	/**
	 * 初始化视图对象
	 * @param view
	 */
	private void initView(View view){
		//标题
		TextView title = (TextView) view.findViewById(R.id.tv_mid_content);
		title.setText("填写订单");
		
		//回退
		ImageView back = (ImageView) view.findViewById(R.id.iv_turn_in);
		back.setOnClickListener(this);
		
		mContentTextView = (TextView) view.findViewById(R.id.tv_promotion_content);
		 
		mPriceListView = (ListView) view.findViewById(R.id.lv_price);
		
		mTotalPriceTextView = (TextView) view.findViewById(R.id.tv_total_money);
		
		mNameEditText = (EditText) view.findViewById(R.id.et_promotion_name);
		
		mPhoneEditText = (EditText) view.findViewById(R.id.et_promotion_phone);
		
		mBuyButtton = (Button) view.findViewById(R.id.btn_promotion_buy);
		
		mBuyButtton.setOnClickListener(this);
		
		mPlatBonudsLinearLayout = (LinearLayout) view.findViewById(R.id.ly_plat_bouns);
		mPlatBonusTextView = (TextView) view.findViewById(R.id.plat_canbouns);
		mPlatBounsEditText = (EditText) view.findViewById(R.id.platform_bounsprice);
		useBonus(mPlatBounsEditText);
		
		mShopBonudLinearLayout = (LinearLayout) view.findViewById(R.id.ly_shop_bouns);
		mShopBonusTextView = (TextView) view.findViewById(R.id.shop_canbouns);
		mShopBounsEditText = (EditText) view.findViewById(R.id.shop_bounsprice);
		useBonus(mShopBounsEditText);
	}
	
	/**
	 * 使用红包
	 */
	private void useBonus(EditText editText) {
		
		
		editText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String inputBonus = s.toString();
				if("".equals(inputBonus)){
					inputBonus = "0";
				}
				//获取输入的金额
				double usePlatBonus= Double.parseDouble(inputBonus);
				if(usePlatBonus>Double.parseDouble(userBonusInfo.getPlatBonus())){
					Util.getContentValidate(R.string.huiquan_insufficient_amount);
					setPayButton(false);
				}else{
					double totalPrice = getTotalPrice();
					totalPrice -= usePlatBonus;
					mTotalPriceTextView.setText(Calculate.ceilBigDecimal(totalPrice)+"");
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}

	/**
	 * 去支付
	 */
	private void gotoPay(){
		//判断预定人姓名是否为空
		String name = mNameEditText.getText().toString().trim();
		if(Util.isEmpty(name)){
			Util.showToastZH("请输入预定人姓名");
			return;
		}
		
		//判断有效手机号是否为空
		String phone = mPhoneEditText.getText().toString().trim();
		if(Util.isEmpty(phone)){
			Util.showToastZH("请输入有效手机号");
			return;
		}
		if(Util.isPhone(getActivity(), phone)){
			return;
		}
		
		//判断金额
		String payTotalPriceStr = mTotalPriceTextView.getText().toString();
		
		if(Util.isEmpty(payTotalPriceStr)){
			payTotalPriceStr = "0";
		}
		double payPrice = Double.parseDouble(payTotalPriceStr);
		if(payPrice<promotionBuy.getMinRealPay()){
			Util.getContentValidate(R.string.pay_money_limit);
			return;
		}
		
		if(String.valueOf(Util.NUM_ZERO).equals(promotionBuy.getIsAcceptBankCard())){
			Util.getContentValidate(R.string.no_accept_icbc);
			return;
		}
		
		Activitys actInfo = promotionBuy.getActInfo();
		
		String json = new Gson().toJson(actInfo.getFeeScale());
		Log.d(TAG, "json=="+json);
		
		String platBounds = mPlatBounsEditText.getText().toString();
		if(Util.isEmpty(platBounds)){
			platBounds="0";
		}
		String shopBounds = mShopBounsEditText.getText().toString();
		if(Util.isEmpty(shopBounds)){
			shopBounds="0";
		}
		new SubmitActOrderTask(getActivity(), new SubmitActOrderTask.CallBack() {
			
			@Override
			public void getResult(String result) {
				if(null!=result){
					Log.d(TAG, "result==="+result);
					String[] split = result.split("\\|\\|");
					String orderNbr = split[0];
					String consumeCode = split[1];
					String realPay = split[2];
					Intent intent = new Intent(getActivity(), ICBCOnlinePayActivity.class);
					intent.putExtra(ICBCOnlinePayFragment.IS_BANK_PAY, true);
					intent.putExtra(ICBCOnlinePayFragment.SHOP_CODE, promotionBuy.getActInfo().getShopCode());
					intent.putExtra(ICBCOnlinePayFragment.SHOP_NAME, promotionBuy.getActInfo().getShopName());
					intent.putExtra(ICBCOnlinePayFragment.REAL_PAY, String.valueOf(realPay));
					intent.putExtra(ICBCOnlinePayFragment.REAL_CONSUMECODE, consumeCode);
					intent.putExtra(ICBCOnlinePayFragment.ORDERNBR, orderNbr);
					getActivity().startActivity(intent);
				}
			}
		}).execute(actInfo.getShopCode(),actInfo.getActivityCode(),json,name,phone,String.valueOf(mTotalPrice),
				platBounds,shopBounds);
		
		
	}
	
	/**
	 * 点击的灰调事件
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_turn_in: //回退
			getActivity().finish();
			break;
		
		case R.id.btn_promotion_buy:
			gotoPay();
			break;
		default:
			break;
		}
	}
	
	//获取总价格
	public double getTotalPrice(){
		double totalPrice = 0;
		for(int i=0;i<promotionBuy.getActInfo().getFeeScale().size();i++){
			PromotionPrice promotionPrice = promotionBuy.getActInfo().getFeeScale().get(i);
			totalPrice+=promotionPrice.getPrice()*promotionPrice.getNbr();
		}
		return totalPrice;
		
	}
	
	/**
	 * 设置支付按钮
	 */
	public void setPayButton(boolean clickStatus){
		if(clickStatus){
			mBuyButtton.setEnabled(true);
			mBuyButtton.setBackgroundResource(R.drawable.login_btn);
		}else{
			mBuyButtton.setEnabled(false);
			mBuyButtton.setBackgroundColor(Color.GRAY);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(BuyPromotionFragment.class.getSimpleName());
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(BuyPromotionFragment.class.getSimpleName()); // 统计页面
	}
}
