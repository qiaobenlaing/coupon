package com.huift.hfq.cust.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.activity.H5ShopDetailActivity;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.model.AddNotTakeoutOrderTask;
import com.huift.hfq.cust.util.SkipActivityUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 扫码成功
 * @author wensi.yu
 *
 */
public class ScanSuccessFragment extends Fragment {

	private static final String TAG = "ScanSuccessFragment";
	
	/** 对象*/
	public final static String SHOP_OBJ ="shopobj";
	/** api返回的失败 0 */
	public static final int API_FAIL = 0;
	/** shop*/
	private Shop mShop;
	
	public static ScanSuccessFragment newInstance() {
		Bundle args = new Bundle();
		ScanSuccessFragment fragment = new ScanSuccessFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_scansuccess, container, false);
		ViewUtils.inject(this, v);
		Util.addActActivity(getMyActivity()); // 买单 
		Intent intent = getMyActivity().getIntent();
		mShop = (Shop) intent.getSerializableExtra(SHOP_OBJ);
		Log.d(TAG, "mShop===="+mShop);
		return v;
	}
	
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}
	
	/**
	 * 点击
	 * @param view
	 */
	@OnClick({R.id.btn_scan_succ_back,R.id.btn_scan_succ_ok})
	public void trunIdenCode(View view) {
		switch (view.getId()) {
		case R.id.btn_scan_succ_back://返回加菜
			Intent intent = new Intent(getMyActivity(), H5ShopDetailActivity.class);
			intent.putExtra(H5ShopDetailActivity.SHOP_CODE, mShop.getShopCode());
			intent.putExtra(H5ShopDetailActivity.PAY_RESULT_ORDERCODE, mShop.getOrderCode());
			intent.putExtra(H5ShopDetailActivity.TYPE, CustConst.HactTheme.ADD_MENU);
			startActivity(intent);
			ActivityUtils.finishAll();
			Util.exitAct();
			break;
			
		case R.id.btn_scan_succ_ok://确认下单
			addNotTakeoutOrder(mShop);
			H5ShopDetailActivity.setCurrentPage("orderok");
			break;

		default:
			break;
		}
	}
	
	/**
	 * 添加堂食
	 */
	private void addNotTakeoutOrder(final Shop shop) {
		String params[] = { shop.getOrderCode(), shop.getUserAddressId(), shop.getRemark() };
		new AddNotTakeoutOrderTask(getMyActivity(), new AddNotTakeoutOrderTask.Callback() {
			@Override
			public void getResult(int resultCode) {
				switch (resultCode) {
				case ErrorCode.SUCC:
					if (shop.getEatPayType() == 1 || shop.getEatPayType() == 0) {
						SkipActivityUtil.skipPayBillActivity(getMyActivity(), shop, shop.getOrderNbr(),ShopPayBillFragment.JOIN_DISCOUNT,ShopPayBillFragment.EAT);
						getMyActivity().finish();
					} else if (shop.getEatPayType() == 2) {
						Intent intent = new Intent(getMyActivity(), H5ShopDetailActivity.class);
						intent.putExtra(H5ShopDetailActivity.SHOP_CODE, shop.getShopCode());
						intent.putExtra(H5ShopDetailActivity.PAY_RESULT_ORDERCODE, shop.getOrderCode());
						intent.putExtra(H5ShopDetailActivity.TYPE, CustConst.HactTheme.ORDER_DETAIL);
						startActivity(intent);
						ActivityUtils.finishAll();
						Util.exitAct();
					}
					break;
				case API_FAIL:
					Util.getContentValidate(R.string.not_take_out);
					break;

				default:
					break;
				}
			}
		}).execute(params);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(ScanSuccessFragment.class.getSimpleName());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(ScanSuccessFragment.class.getSimpleName()); // 统计页面
	}
}
