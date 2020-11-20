package com.huift.hfq.shop.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.OrderInfo;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.TimestampUtil;
import com.huift.hfq.shop.activity.ConfirmOrderActivity;
import com.huift.hfq.shop.model.SweepQrCodeTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 机器扫描
 * @author wensi.yu
 * 
 */
public class ScanBarCodeFragment extends Fragment {
	
	private final static String TAG = "ScanBarCodeFragment";

	private final static String SCAN_ACTION = "urovo.rcv.message";// 扫描结束action
	private final static String SCAN_TITLE = "扫付款码";
	private TextView mShowScanResult;
	private Vibrator mVibrator;
	private ScanManager mScanManager;
	private SoundPool mSoundpool = null;
	private int mSoundid;
	private String mBarcodeStr;
	private boolean mIsScaning = false;
	/** 订单信息*/
	private OrderInfo mOrderInfo;
	/** 正在处理*/
	private LinearLayout mNodate;

	
	private Handler mScanHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				String payTypeScan = (String) msg.obj;
				sweepQrCode(payTypeScan);
				break;

			default:
				break;
			}
		};
	};
	
	private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			mIsScaning = false;
			mSoundpool.play(mSoundid, 1, 1, 0, 0, 1);
			mShowScanResult.setText("");
			mVibrator.vibrate(100);

			byte[] barcode = intent.getByteArrayExtra("barocode");
			// byte[] barcode = intent.getByteArrayExtra("barcode");
			int barocodelen = intent.getIntExtra("length", 0);
			byte temp = intent.getByteExtra("barcodeType", (byte) 0);
			Log.d(TAG, "temp--------" + temp);
			mBarcodeStr = new String(barcode, 0, barocodelen);
			Log.d(TAG, "结果------"+mBarcodeStr);
			//mShowScanResult.setText(mBarcodeStr);
			if (mBarcodeStr.startsWith("payType")) {
				String[] payTpye = mBarcodeStr.split("\\:");
				final String payTypeCode = payTpye[1];
				Log.d(TAG, "payTpyeCode == " + payTypeCode);
				TimestampUtil.setRomdonValue(getActivity(),new TimestampUtil.CallBack() {
					
					@Override
					public void getNetTime(String netTime) {
						Message message = Message.obtain();
						String payTypeScan = payTypeCode + netTime ;
						message.what = 1;
						message.obj = payTypeScan;
						mScanHandler.sendMessage(message);
					}
				});
			} else {
				Util.getContentValidate(R.string.toast_scan_illegal);
			}
		}
	};
	
	public static ScanBarCodeFragment newInstance() {
		Bundle args = new Bundle();
		ScanBarCodeFragment fragment = new ScanBarCodeFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Window window = getMyActivity().getWindow();
	    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    View view = inflater.inflate(R.layout.fragment_scanbarcode,container, false);
		ViewUtils.inject(this, view);
	    mVibrator = (Vibrator) getMyActivity().getSystemService(Context.VIBRATOR_SERVICE);
	    init(view);
	    ActivityUtils.add(getMyActivity());//订单完成
	    Util.addLoginActivity(getMyActivity());
		return view;
	}

	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	private void init(View view) {
		LinearLayout ivBanck = (LinearLayout) view.findViewById(R.id.layout_turn_in);
		ivBanck.setVisibility(View.VISIBLE);
		TextView tvFunction = (TextView) view.findViewById(R.id.tv_mid_content);
		tvFunction.setText(SCAN_TITLE);
		mShowScanResult = (TextView) view.findViewById(R.id.scan_result);
		mNodate = (LinearLayout) view.findViewById(R.id.ly_nodate);//正在处理
	}

	private void initScan() {
		mScanManager = new ScanManager();
		mScanManager.openScanner();
		mScanManager.switchOutputMode(0);
		mSoundpool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100); // MODE_RINGTONE
		mSoundid = mSoundpool.load("/etc/Scan_new.ogg", 1);
	}
	
	/**
	 * 扫描二维码
	 */
	private void sweepQrCode (String payTypeScan) {
		
		mNodate.setVisibility(View.GONE);
		
		new SweepQrCodeTask(getMyActivity(), new SweepQrCodeTask.Callback() {
			
			@Override
			public void getResult(net.minidev.json.JSONObject result) {
				if (null == result) {
					return;
				}
				mNodate.setVisibility(View.GONE);
				mOrderInfo = Util.json2Obj(result.toString(), OrderInfo.class);
				//银行卡编码
				String bankAccountCode = null;
				if (!Util.isEmpty(mOrderInfo.getBankAccountCode())) {
					bankAccountCode = mOrderInfo.getBankAccountCode();
				} else {
					bankAccountCode = "";
				}
				Log.d(TAG, "bankAccountCode=="+bankAccountCode);
				//用户编码
				String userCode = null;
				if (!Util.isEmpty(mOrderInfo.getUserCode())) {
					userCode = mOrderInfo.getUserCode();
				} else {
					userCode = "";
				}
				Log.d(TAG, "userCode=="+userCode);
				if (result.get("code").toString().equals(String.valueOf(ErrorCode.SUCC))) {
					Intent intent = new Intent(getMyActivity(),ConfirmOrderActivity.class);
					intent.putExtra(ConfirmOrderFragment.BANK_ACCOUNTCODE, bankAccountCode);
					intent.putExtra(ConfirmOrderFragment.USER_CODE, userCode);
					startActivity(intent);
				}
			}
		}).execute(payTypeScan);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mScanManager != null) {
			mScanManager.stopDecode();
			mIsScaning = false;
		}
		getMyActivity().unregisterReceiver(mScanReceiver);
	}

	@Override
	public void onResume() {
		super.onResume();
		initScan();
		mShowScanResult.setText("");
		IntentFilter filter = new IntentFilter();
		filter.addAction(SCAN_ACTION);
		getMyActivity().registerReceiver(mScanReceiver, filter);
		mNodate.setVisibility(View.GONE);
	}

	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		mScanHandler.removeCallbacksAndMessages(null);
	}
	
	/**
	 * 点击返回按钮
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	public void btnAclist(View view) {
		getMyActivity().finish();
	}
}
