package cn.suanzi.baomi.cust.fragment;

import java.lang.reflect.Type;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.BatchCoupon;
import cn.suanzi.baomi.base.pojo.Citys;
import cn.suanzi.baomi.base.pojo.Shop;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.base.utils.ActivityUtils;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.view.XListView;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.activity.CouponQrcodeActivity;
import cn.suanzi.baomi.cust.activity.PayMoneyActivity;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.model.GetUserCouponInfoTask;
import cn.suanzi.baomi.cust.model.ZeroPayTask;
import cn.suanzi.baomi.cust.util.SkipActivityUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

public class ICBCPaySuccessFragment extends Fragment {

	public static final String TAG = ICBCPaySuccessFragment.class.getSimpleName();
	public static final String SUCCESS = "success";
	public static final String SHOP_OBJ = "shopObj";
	public static final boolean SUCCESS_TRUE = true;
	public static final boolean SUCCESS_FALSE = false;
	public static final String SHOP_CODE = "shopcode";
	public static final String USER_COUPON_CODE = "userCouponCode";
	private boolean mInitPara;
	
	@ViewInject(R.id.success_wrong)
	private View mSuccessWrongView;
	
	@ViewInject(R.id.success_right)
	private View mSuccessRightView;
	
	@ViewInject(R.id.listView)
	private XListView mListView;
	
	@ViewInject(R.id.rl_send_coupon)
	private RelativeLayout mRlSendCoupon;
	
	private View mView;
	
	/** 获得一个用户信息对象 **/
	private UserToken mUserToken;
	/** 用户编码 **/
	private String mUserCode;
	/** 商店编码 */
	private String mShopCode;
	private String mLongitude;// 经度
	private String mLatitude;// 纬度
	private String mUserCouponCode;
	private Type jsonType = new TypeToken<BatchCoupon>() {}.getType();


	
	/**
	 * 需要传递参数时有利于解耦
	 */
	public static ICBCPaySuccessFragment newInstance() {
		Bundle args = new Bundle();
		ICBCPaySuccessFragment fragment = new ICBCPaySuccessFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_shop_icbc_pay_success, container, false);
		ViewUtils.inject(this, mView);
		
		//****记录买单流程*********
		ActivityUtils.add(getMyActivity());
		Util.addLoginActivity(getMyActivity());
		init(mView);
		return mView;
	}
	
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	private void init(View v) {
		mShopCode = getMyActivity().getIntent().getStringExtra(SHOP_CODE);
		mUserCouponCode = getMyActivity().getIntent().getStringExtra(USER_COUPON_CODE);
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
			mUserCode = mUserToken.getUserCode();// 用户编码
		} else {
			mUserCode = "";
		}
		SharedPreferences preferences = getMyActivity()
				.getSharedPreferences(CustConst.Key.CITY_OBJ, Context.MODE_PRIVATE);
		String cityName = preferences.getString(CustConst.Key.CITY_NAME, null);
		Log.d("TAG", "取出DB的定位城市为 ：：：：：：： " + cityName);
		if (Util.isEmpty(cityName)) {
			Citys citys = DB.getObj(HomeFragment.CITYS, Citys.class);
			mLongitude = String.valueOf(citys.getLongitude());
			mLatitude = String.valueOf(citys.getLatitude());
		} else {
			mLongitude = preferences.getString(CustConst.Key.CITY_LONG, null);
			mLatitude = preferences.getString(CustConst.Key.CITY_LAT, null);
		}
		mInitPara = getMyActivity().getIntent().getBooleanExtra(SUCCESS, false);
		
		if (mInitPara) {
			mSuccessWrongView.setVisibility(View.GONE);
			mSuccessRightView.setVisibility(View.VISIBLE);
			mRlSendCoupon.setVisibility(View.VISIBLE);
			if (null != mUserCouponCode || !Util.isEmpty(mUserCouponCode)) {
				getUserCouponInfo(); // 获得我已经拥有的优惠券
			}
		} else {
			mSuccessWrongView.setVisibility(View.VISIBLE);
			mSuccessRightView.setVisibility(View.GONE);
			mRlSendCoupon.setVisibility(View.GONE);
		}
		//##################################################
		mRlSendCoupon.setVisibility(View.GONE);

		
	}

	/**
	 * 获得我已经拥有的优惠券
	 */
	private void getUserCouponInfo() {
		new GetUserCouponInfoTask(getMyActivity(), new GetUserCouponInfoTask.Callback() {

			@Override
			public void getResult(JSONObject result) {
				if (result == null) {
					return;
				} else {
					Gson gson = new Gson();
					BatchCoupon batchCoupon = gson.fromJson(result.toString(),jsonType);
					if (null != batchCoupon) {
						getSendCoupon(batchCoupon);
					}
				}
			}
		}).execute(mUserCouponCode);
	}

	/**
	 * 满就送的优惠券
	 * @param batchCoupon 
	 */
	private void getSendCoupon(final BatchCoupon batchCoupon) {

		// 商家Logo
		String logoUrl = batchCoupon.getLogoUrl();
		ImageView ivCardImage = ((ImageView) mView.findViewById(R.id.iv_couponpic));
		RelativeLayout ryleft = (RelativeLayout) mView.findViewById(R.id.left_content);
		Util.showImage(getMyActivity(), logoUrl, ivCardImage);// 商家图片显示图片

		// 店铺名称
		((TextView) mView.findViewById(R.id.tv_coupon_logo)).setText(batchCoupon.getShopName());

		// 折
		TextView tvSymbol = (TextView) mView.findViewById(R.id.tv_symbol);
		TextView tvMoney = (TextView) mView.findViewById(R.id.tv_money);
		TextView tvCouponEffet = (TextView) mView.findViewById(R.id.tv_coupon_effet); // 有效期
		TextView tvCouponDate = (TextView) mView.findViewById(R.id.tv_coupon_effet2);
		TextView tvInsteadPrice = (TextView) mView.findViewById(R.id.tv_coupon_price);
		LinearLayout tvRight = (LinearLayout) mView.findViewById(R.id.right_content);
		LinearLayout lySendCoupon = (LinearLayout) mView.findViewById(R.id.ly_sendcoupon);
		
		
		// 说明优惠券
		String canMoney = "";
		String insteadPrice = "";
		String couponType = "";
		if (CustConst.Coupon.DEDUCT.equals(batchCoupon.getCouponType())) { // 抵扣券
			couponType = getString(R.string.coupon_deduct);
			canMoney = "满" + batchCoupon.getAvailablePrice() + "元立减" + batchCoupon.getInsteadPrice() + "元";
			tvSymbol.setVisibility(View.GONE);
			tvMoney.setVisibility(View.VISIBLE);
			tvRight.setBackgroundResource(R.drawable.full_send);
			insteadPrice = batchCoupon.getInsteadPrice();
			tvInsteadPrice.setTextSize(24);
		} else if (CustConst.Coupon.DISCOUNT.equals(batchCoupon.getCouponType())) { // 折扣券
			tvInsteadPrice.setTextSize(24);
			couponType = getString(R.string.coupon_discount);
			canMoney = "满" + batchCoupon.getAvailablePrice() + "元打" + batchCoupon.getDiscountPercent() + "折";
			insteadPrice = batchCoupon.getDiscountPercent() + "";
			tvSymbol.setVisibility(View.VISIBLE);
			tvMoney.setVisibility(View.GONE);
			tvRight.setBackgroundResource(R.drawable.full_cut);
			insteadPrice = batchCoupon.getDiscountPercent();
		} else if (CustConst.Coupon.N_BUY.equals(batchCoupon.getCouponType())) { // N元购
			couponType = getString(R.string.n_buy);
			canMoney = batchCoupon.getFunction();
			insteadPrice = batchCoupon.getInsteadPrice();
			tvSymbol.setVisibility(View.GONE);
			tvMoney.setVisibility(View.VISIBLE);
			tvRight.setBackgroundResource(R.drawable.n_buy);
			tvInsteadPrice.setTextSize(24);
			// 实物券和体验券
		} else if (CustConst.Coupon.REAL_COUPON.equals(batchCoupon.getCouponType())) {// 实物券
			canMoney = batchCoupon.getFunction();
			tvSymbol.setVisibility(View.GONE);
			tvMoney.setVisibility(View.GONE);
			tvRight.setBackgroundResource(R.drawable.real_coupon);
			insteadPrice = getString(R.string.real_coupon);
			tvInsteadPrice.setTextSize(16);
		} else if (CustConst.Coupon.EXPERIENCE.equals(batchCoupon.getCouponType())) {
			canMoney = batchCoupon.getFunction();
			tvSymbol.setVisibility(View.GONE);
			tvMoney.setVisibility(View.GONE);
			tvRight.setBackgroundResource(R.drawable.experience_coupon);
			insteadPrice = batchCoupon.getInsteadPrice();
			insteadPrice = getString(R.string.experience);
			tvInsteadPrice.setTextSize(16);

		}
		// 抵用金额
		tvInsteadPrice.setText(insteadPrice);
		// 达到多少金额可用
		((TextView) mView.findViewById(R.id.tv_coupon_gd)).setText(canMoney);
		// 优惠券类型
		((TextView) mView.findViewById(R.id.tv_couponedraw)).setText(couponType);

		// 有效时间
		if ((Util.isEmpty(batchCoupon.getStartUsingTime()) && Util.isEmpty(batchCoupon.getExpireTime()))) {
			tvCouponDate.setText(getString(R.string.no_limit_time));
		} else {
			Log.d(TAG, "startDate=" + batchCoupon.getStartUsingTime());
			Log.d(TAG, "endDatestr=" + batchCoupon.getExpireTime());
			tvCouponDate.setText(batchCoupon.getStartUsingTime()+" - "+batchCoupon.getExpireTime());
		}
		// 使用时间
		tvCouponEffet.setText(batchCoupon.getDayStartUsingTime() + " - " + batchCoupon.getDayEndUsingTime());

		// 使用说明
		String remark = batchCoupon.getRemark() + "";
		if (remark.equals("") || remark.equals("null")) {
			mView.findViewById(R.id.tv_coupon_use).setVisibility(View.VISIBLE);
			mView.findViewById(R.id.tv_coupon_use1).setVisibility(View.VISIBLE);
			((TextView) mView.findViewById(R.id.tv_coupon_use1)).setText("暂无说明");
		} else {
			mView.findViewById(R.id.tv_coupon_use).setVisibility(View.VISIBLE);
			mView.findViewById(R.id.tv_coupon_use1).setVisibility(View.VISIBLE);
			((TextView) mView.findViewById(R.id.tv_coupon_use1)).setText(remark);
		}
		// 距离
		TextView tvDistance = (TextView) mView.findViewById(R.id.tv_distance);
		tvDistance.setVisibility(View.GONE); 
		if (!Util.isEmpty(batchCoupon.getDistance())) {

			String distanceSrc = batchCoupon.getDistance().replace(",", "").replace(".", "");
			Log.d(TAG, "===" + distanceSrc);
			String distatnceSimple = "";
			int distance = Integer.parseInt(distanceSrc);
			if (distance > 100000) {
				distatnceSimple = ">100 Km";
			} else {
				if (distance >= 1000) {
					distatnceSimple = String.valueOf(distance / 1000) + "Km";
				} else {
					distatnceSimple = String.valueOf(distance) + " M";
				}
			}
			tvDistance.setText(distatnceSimple);
		}

		CheckBox chMoreShow = (CheckBox) mView.findViewById(R.id.ckb_coupon_up);
		Button share = (Button) mView.findViewById(R.id.iv_share);
		final RelativeLayout rlContent = (RelativeLayout) mView.findViewById(R.id.hideOrShow);

		// 分享
//		share.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// 友盟统计
//				HashMap<String, String> map = new HashMap<String, String>();
//				map.put("shopcode", batchCoupon.getShopCode());
//				map.put("shopName", batchCoupon.getShopName());
//				MobclickAgent.onEvent(getMyActivity(), "coupon_share", map);
//				String describe = batchCoupon.getShopName() + getString(R.string.share_draw);
//				Tools.showCouponShare(getMyActivity(), "BatchCoupon/share?batchCouponCode=", describe, "", batchCoupon.getBatchCouponCode());
//			}
//		});

		// 让更多内容显示
		chMoreShow.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					rlContent.setVisibility(View.VISIBLE);
				} else {
					rlContent.setVisibility(View.GONE);
				}
			}
		});
		
		// 使用优惠券
		lySendCoupon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if (CustConst.Coupon.REAL_COUPON.equals(batchCoupon.getCouponType())
						|| CustConst.Coupon.EXPERIENCE.equals(batchCoupon.getCouponType())) {
					if (null == mShopCode) {
						return;
					} 
					zeroPay(batchCoupon.getShopCode(), batchCoupon.getUserCouponCode(), batchCoupon);
				} else if (CustConst.Coupon.DEDUCT.equals(batchCoupon.getCouponType())
						|| CustConst.Coupon.DISCOUNT.equals(batchCoupon.getCouponType())){
					Shop shop = (Shop) getMyActivity().getIntent().getSerializableExtra(SHOP_OBJ);
					SkipActivityUtil.skipPayBillActivity(getMyActivity(), shop, "",ShopPayBillFragment.JOIN_DISCOUNT,ShopPayBillFragment.NOT_MEAL);
				} else {
					Intent intent = new Intent(getMyActivity(), PayMoneyActivity.class);
					intent.putExtra(PayMoneyActivity.BATCH_CPOUPON, batchCoupon);
					getMyActivity().startActivity(intent);
				}
				
			}
		});

	}
	
	/**
	 * 实物券和体验券
	 */
	private void zeroPay(String shopCode, String userCouponCode, final BatchCoupon coupon) {
		String parems[] = { shopCode, userCouponCode };
		new ZeroPayTask(getMyActivity(), new ZeroPayTask.Callback() {

			@Override
			public void getResult(int code ,JSONObject result) {
				 if (code == ErrorCode.FAIL) { 
					return;
				} else if (code == ErrorCode.SUCC) {
					String consumeCode = result.get("consumeCode").toString();
					Intent intent = new Intent(getMyActivity(), CouponQrcodeActivity.class);
					intent.putExtra(CouponQrcodeActivity.BATCH_CPOUPON, coupon);
					intent.putExtra(CouponQrcodeActivity.CONSUMER_CODE, consumeCode);
					getMyActivity().startActivity(intent);
				} else {
					return;
				}
			}
		}).execute(parems);
	}
	
	@OnClick({ R.id.backup ,R.id.success_right,R.id.success_wrong})
	private void click(View v) {
		switch (v.getId()) {
		case R.id.backup:
			//getActivity().finish();
			break;
		case R.id.success_right:
			//清除所有买单流程的Activity---回到主页
			ActivityUtils.finishAll();
			break;
		case R.id.success_wrong:
			//清除所有买单流程的Activity---回到主页
			ActivityUtils.finishAll();
			break;
		}
	}
	
	
	/**
	 * 点击返回键 fragment中所做的操作  结束所有界面  返回到主页面
	 */
	public void onBackPressedFragment(){
		ActivityUtils.finishAll();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(ICBCPaySuccessFragment.class.getSimpleName());
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(ICBCPaySuccessFragment.class.getSimpleName()); // 统计页面
	}
}
