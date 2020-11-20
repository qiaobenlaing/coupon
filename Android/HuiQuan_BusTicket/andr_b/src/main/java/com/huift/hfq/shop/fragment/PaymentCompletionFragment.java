// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.OrderInfo;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.service.PrintBillService;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 扫码支付的确认订单界面
 * @author qian.zhou
 */
public class PaymentCompletionFragment extends Fragment {

	public  static final String ORDERINFO_OBJ = "orderInfo";
	private final static String PRNT_ACTION = "android.prnt.message";
	public static final String MSG_SUCC = "完成";
	/** 设备型号*/
	public static final String DEVICEMODEL = "SQ";
	/** 保存订单基本信息的对象*/
	private OrderInfo mOrderInfo;
	/** 得到订单的信息*/
	private String mMessgae;
	/** 设备型号*/
	private String mDeviceModel;


	private static final String[] mTempThresholdTable = {
			"80", "75", "70", "65", "60",
			"55", "50", "45", "40", "35",
			"30", "25", "20", "15", "10",
			"5", "0", "-5", "-10", "-15",
			"-20", "-25", "-30", "-35", "-40",
	};
	private final static String SPINNER_PREFERENCES_FILE = "SprinterPrefs";
	private final static String SPINNER_SELECT_POSITION_KEY = "spinnerPositions";
	private final static int DEF_SPINNER_SELECT_POSITION = 6;
	private final static String SPINNER_SELECT_VAULE_KEY = "spinnerValue";
	private final static String DEF_SPINNER_SELECT_VAULE = mTempThresholdTable[DEF_SPINNER_SELECT_POSITION];
	private int mSpinnerSelectPosition;
	private String mSpinnerSelectValue;

	private BroadcastReceiver mPrtReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			int ret = intent.getIntExtra("ret", 0);
			if(ret == -1){
				Util.getContentValidate(R.string.toast_scanprinter_paper);
			}
		}
	};

	/**
	 * 需要传递参数时有利于解耦
	 * @return PosPayFragment
	 */
	public static PaymentCompletionFragment newInstance() {
		Bundle args = new Bundle();
		PaymentCompletionFragment fragment = new PaymentCompletionFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_payment_completion, container, false);
		ViewUtils.inject(this, v);
		ActivityUtils.add(getMyActivity());//订单完成
		init(v);
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
	 * 初始化数据
	 */
	private void init(View v) {
		//标题
		TextView tvContent = (TextView) v.findViewById(R.id.tv_mid_content);
		tvContent.setText(R.string.confirm_order);
		TextView tvMsg = (TextView) v.findViewById(R.id.tv_msg);//完成
		tvMsg.setText(MSG_SUCC);
		Button btnScanPrint = (Button) v.findViewById(R.id.btn_pay_print);//打印
		tvMsg.setOnClickListener(MsgListener);
		btnScanPrint.setOnClickListener(MsgListener);
		//初始化数据
		ImageView ivUserLogo = (ImageView) v.findViewById(R.id.iv_userlogo);//用户头像
		TextView tvUserName = (TextView) v.findViewById(R.id.tv_username);//用户名称
		TextView tvOrderNbr = (TextView) v.findViewById(R.id.tv_ordernbr);//订单号
		TextView tvConsumerMoney = (TextView) v.findViewById(R.id.tv_consumer_money);//消费金额
		TextView tvCouponMoney = (TextView) v.findViewById(R.id.tv_coupon_money);//优惠金额
		TextView tvActualMoney = (TextView) v.findViewById(R.id.tv_actual_money);//实际金额
		//取值
		Intent intent = getActivity().getIntent();
		mOrderInfo = (OrderInfo) intent.getSerializableExtra(ORDERINFO_OBJ);
		if (null != mOrderInfo) {
			Util.showImage(getActivity(), mOrderInfo.getAvatarUrl(), ivUserLogo);
			tvUserName.setText(!Util.isEmpty(mOrderInfo.getNickName()) ? mOrderInfo.getNickName() : "");
			tvOrderNbr.setText(!Util.isEmpty(mOrderInfo.getOrderNbr()) ? mOrderInfo.getOrderNbr() : "");
			tvConsumerMoney.setText(!Util.isEmpty(mOrderInfo.getOrderAmount()) ? mOrderInfo.getOrderAmount() : "");
			tvCouponMoney.setText(!Util.isEmpty(mOrderInfo.getDeduction()) ? mOrderInfo.getDeduction() : "");
			tvActualMoney.setText(!Util.isEmpty(mOrderInfo.getRealPay()) ? mOrderInfo.getRealPay() : "");
		}
		mDeviceModel = Build.MODEL; // 设备型号
		if (mDeviceModel.startsWith(DEVICEMODEL)) {
			btnScanPrint.setVisibility(View.VISIBLE);
		} else {
			btnScanPrint.setVisibility(View.GONE);
		}
	}

	/**
	 * 点击完成
	 */
	OnClickListener MsgListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.tv_msg://完成
					ActivityUtils.finishAll();
					break;
				case R.id.btn_pay_print://打印
					scanPrint();
					if(mMessgae.length() > 0) {
						doprintwork(mMessgae);
					} else {
						Util.getContentValidate(R.string.scanPrint_input);
					}
					break;

				default:
					break;
			}
		}
	};

	/**
	 * 得到订单的信息
	 */
	private void scanPrint () {
		//商家名称
		String shopName = null;
		if (!Util.isEmpty(mOrderInfo.getShopName())) {
			shopName = mOrderInfo.getShopName();
		} else {
			shopName = "";
		}
		//支付时间
		String payTime = null;
		if (!Util.isEmpty(mOrderInfo.getPayedTime())) {
			payTime = mOrderInfo.getPayedTime();
		} else {
			payTime = "";
		}
		//用户名称
		String userName = null;
		if (!Util.isEmpty(mOrderInfo.getNickName())) {
			userName = mOrderInfo.getNickName();
		} else {
			userName = "";
		}
		//用户手机
		String userPhone = null;
		if (!Util.isEmpty(mOrderInfo.getReceiverMobileNbr())) {
			userPhone = mOrderInfo.getReceiverMobileNbr();
		} else {
			userPhone = "";
		}
		//订单号
		String orderNbr = null;
		if (!Util.isEmpty(mOrderInfo.getOrderNbr())) {
			orderNbr = mOrderInfo.getOrderNbr();
		} else {
			orderNbr = "";
		}
		//消费金额
		String consumerAmount = null;
		if (!Util.isEmpty(mOrderInfo.getOrderAmount())) {
			consumerAmount = mOrderInfo.getOrderAmount();
		} else {
			consumerAmount = "";
		}
		//优惠金额
		String noDiscount = null;
		if (!Util.isEmpty(mOrderInfo.getDeduction())) {
			noDiscount = mOrderInfo.getDeduction();
		} else {
			noDiscount = "";
		}
		//实际支付金额
		String realPay = null;
		if (!Util.isEmpty(mOrderInfo.getRealPay())) {
			realPay = mOrderInfo.getRealPay();
		} else {
			realPay = "";
		}

		printInfo (shopName,payTime,userName,userPhone,orderNbr,consumerAmount,noDiscount,realPay);
	}

	/**
	 * 需要打印的信息
	 */
	private void printInfo (String shopName,String payTime,String userName,String userPhone,String orderNbr,String consumerAmount,String noDiscount,String realPay ) {

		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append("                            惠圈 ");
		sb.append("\n");

		sb.append(        shopName);
		sb.append("\n");
		sb.append("\n");

		sb.append("**************************************************");
		sb.append("\n");
		sb.append("\n");

		sb.append("支付时间：");
		sb.append(payTime);
		sb.append("\n");

		sb.append("用户：");
		sb.append(userName);
		sb.append("\n");

		sb.append("用户：");
		sb.append(userPhone);
		sb.append("\n");

		sb.append("订单号：");
		sb.append(orderNbr);
		sb.append("\n");
		sb.append("\n");

		sb.append("消费金额：");
		sb.append(consumerAmount);
		sb.append("\n");

		sb.append("优惠金额：");
		sb.append(noDiscount);
		sb.append("\n");

		sb.append("-------------------------------------------------");
		sb.append("-------------------------------------------------");
		sb.append("\n");

		sb.append("实际支付：");
		sb.append(realPay);
		sb.append("\n");
		sb.append("\n");
		sb.append("\n");
		sb.append("\n");
		sb.append("\n");
		sb.append("\n");

		mMessgae = sb.toString();
	}

	/**
	 * 打印
	 */
	private void doprintwork(String msg) {
		Intent intentService = new Intent(getMyActivity(), PrintBillService.class);
		intentService.putExtra("SPRT", msg);
		getMyActivity().startService(intentService);
	}

	@Override
	public void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter();
		filter.addAction(PRNT_ACTION);
		getMyActivity().registerReceiver(mPrtReceiver, filter);
		readSpinnerPrefsState(getMyActivity());
	}

	@Override
	public void onPause() {
		super.onPause();
		getMyActivity().unregisterReceiver(mPrtReceiver);
		writeSpinnerPrefsState(getMyActivity());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	// read prefs to restore
	private boolean readSpinnerPrefsState(Context c){
		SharedPreferences sharedPrefs = c.getSharedPreferences(SPINNER_PREFERENCES_FILE, getMyActivity().MODE_PRIVATE);
		mSpinnerSelectPosition = sharedPrefs.getInt(SPINNER_SELECT_POSITION_KEY, DEF_SPINNER_SELECT_POSITION);
		mSpinnerSelectValue = sharedPrefs.getString(SPINNER_SELECT_VAULE_KEY, DEF_SPINNER_SELECT_VAULE);
		return (sharedPrefs.contains(SPINNER_SELECT_POSITION_KEY));
	}

	// write prefs to file for restroing
	private boolean writeSpinnerPrefsState(Context c){
		SharedPreferences sharedPrefs = c.getSharedPreferences(SPINNER_PREFERENCES_FILE, getMyActivity().MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putInt(SPINNER_SELECT_POSITION_KEY, mSpinnerSelectPosition);
		editor.putString(SPINNER_SELECT_VAULE_KEY, mSpinnerSelectValue);
		return (editor.commit());
	}

	/** 点击返回 **/
	@OnClick(R.id.layout_turn_in)
	private void ivuppBackClick(View v) {
		scanBack ();
	}

	public void scanBack () {
		ActivityUtils.finishAll();
	}
}
