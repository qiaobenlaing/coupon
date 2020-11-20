package com.huift.hfq.shop.fragment;

import android.app.Activity;
import android.app.Fragment;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.OrderInfo;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.TimestampUtil;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.ConfirmOrderActivity;
import com.huift.hfq.shop.activity.ScanActivity;
import com.huift.hfq.shop.model.SweepQrCodeTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 扫描输入金额
 * @author wensi.yu
 *
 */
public class ScanInputAmountFragment extends Fragment {
	
	private final static String TAG = "ScanInputAmountFragment";
	
	private final static String SCAN_TITLE= "输入金额";
	private final static double SCAN_INPUT_AMPUNT = 300.00;
	private final static double SCAN_INPUT_DIFF= 0.00;
	/** 设备型号*/
	public static final String DEVICEMODEL = "SQ";
	/** 消费金额*/
	private EditText mEtConsumptionamount;
	/** 不参与优惠金额*/
	private EditText mEtNodiscount;
	/** 消费金额*/
	private String mConsumptionamount;
	/** 不参与优惠金额*/
	private String mNodiscount;
	/** checkbox*/
	private CheckBox mCkNodiscount;
	/** 不参与优惠额*/
	private LinearLayout mRlNodiscount;
	/** 记录点击次数*/
	private int mClickNum;
	/** 设备型号*/
	private String mDeviceModel;
	
	private final static String SCAN_ACTION = "urovo.rcv.message";// 扫描结束action
	private TextView mShowScanResult;
	private Vibrator mVibrator;
	private ScanManager mScanManager;
	private SoundPool mSoundpool = null;
	private int mSoundid;
	private String mBarcodeStr;
	private boolean mIsScaning = false;
	/** 订单信息*/
	private OrderInfo mOrderInfo;
	
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
			mVibrator.vibrate(100);

			byte[] barcode = intent.getByteArrayExtra("barocode");
			// byte[] barcode = intent.getByteArrayExtra("barcode");
			int barocodelen = intent.getIntExtra("length", 0);
			Log.d(TAG, "barocodelen == " +barocodelen);
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
	
	public static ScanInputAmountFragment newInstance() {
		Bundle args = new Bundle();
		ScanInputAmountFragment fragment = new ScanInputAmountFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_scaninput_amount,container, false);
		ViewUtils.inject(this, view);
		Window window = getMyActivity().getWindow();
	    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		mVibrator = (Vibrator) getMyActivity().getSystemService(Context.VIBRATOR_SERVICE);
		init(view);
		ActivityUtils.add(getMyActivity());//订单完成
		return view;
	}
	
	private Activity getMyActivity(){
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;       
	}
	
	private void initScan() {
		mScanManager = new ScanManager();
		mScanManager.openScanner();
		mScanManager.switchOutputMode(0);
		mSoundpool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100); // MODE_RINGTONE
		mSoundid = mSoundpool.load("/etc/Scan_new.ogg", 1);
	}

	private void init(View view) {
		mDeviceModel = Build.MODEL; // 设备型号  
		Log.d(TAG, "设备型号 >>> " +mDeviceModel);
		TextView tvCampaignName = (TextView) view.findViewById(R.id.tv_mid_content);//标题
		tvCampaignName.setText(SCAN_TITLE);
		TextView tvSet = (TextView) view.findViewById(R.id.tv_msg);//添加
		tvSet.setVisibility(View.GONE);
		mEtConsumptionamount = (EditText) view.findViewById(R.id.et_scan_inputamount);//消费金额
		mEtNodiscount = (EditText) view.findViewById(R.id.et_scan_nodiscount);//不参与优惠金额
	    Button btnScanreceivables = (Button) view.findViewById(R.id.btn_scan_scanreceivables);//扫码收款
		mCkNodiscount = (CheckBox) view.findViewById(R.id.ck_scan_nodiscount);//选择不参与优惠的金额
		
		mRlNodiscount = (LinearLayout) view.findViewById(R.id.ly_scan_nodiscount);//不参与优惠额的一行
		if (!mCkNodiscount.isChecked()) {//默认不参与优惠金额为隐藏
			mRlNodiscount.setVisibility(View.GONE);
		} 
		
		if (mDeviceModel.startsWith(DEVICEMODEL)) {
			Log.d(TAG, "型号 pos。。。。。");
			mEtConsumptionamount.addTextChangedListener(amountChange);
			mEtNodiscount.addTextChangedListener(nodiscountChange);
		}  
		
		mCkNodiscount.setOnClickListener(btnClick);
		btnScanreceivables.setOnClickListener(btnClick);
		 
		Util.initDate(mEtConsumptionamount);//自动弹出软键盘
	}
	
	/**
	 * 消费金额的监听事件
	 */
	TextWatcher amountChange = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			mConsumptionamount = s.toString();
			Log.d(TAG, "consumptionamount after =="+mConsumptionamount);
			
		}
	};
	
	/**
	 * 不参与优惠金额的监听事件
	 */
	TextWatcher nodiscountChange = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			mNodiscount = s.toString();
			Log.d(TAG, "mNodiscount after=="+mNodiscount);
		}
	};
	
	/**
	 * 扫描二维码（pos）
	 */
	private void sweepQrCode (String payTypeScan) {
		
		new SweepQrCodeTask(getMyActivity(), new SweepQrCodeTask.Callback() {
			
			@Override
			public void getResult(net.minidev.json.JSONObject result) {
				if (null == result) {
					return;
				}
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
					intent.putExtra(ConfirmOrderFragment.USER_AMOUNT, mConsumptionamount);
					intent.putExtra(ConfirmOrderFragment.USER_NODISCOUNT, mNodiscount);
					startActivity(intent);
				}
			}
		}).execute(payTypeScan);
	}

	/**
	 * 扫码收款
	 */
	private OnClickListener btnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (!mDeviceModel.startsWith(DEVICEMODEL)) {
				Log.d(TAG, "型号 ohter。。。。。");
				mConsumptionamount = mEtConsumptionamount.getText().toString();
				mNodiscount = mEtNodiscount.getText().toString() ;
				DB.saveStr(ShopConst.InputAmout.INPUT_CONSUMPTION, mConsumptionamount);
				DB.saveStr(ShopConst.InputAmout.INPUT_NODISCOUNT, mNodiscount);
				Log.d(TAG, "consumptionamount=="+mConsumptionamount);
				Log.d(TAG, "nodiscount=="+mNodiscount);
			}
			
			switch (v.getId()) {
			case R.id.ck_scan_nodiscount://不参与优惠金额
				if (mCkNodiscount.isChecked()) {
					mRlNodiscount.setVisibility(View.VISIBLE);
					mEtNodiscount.requestFocus();
				} else {
					mRlNodiscount.setVisibility(View.GONE);
				}
				
				break;
			case R.id.btn_scan_scanreceivables://确定
				if (mDeviceModel.startsWith(DEVICEMODEL)) {
					Log.d(TAG, "型号 pos。。。。。");
					if (Util.isEmpty(mConsumptionamount)) {
						Util.getContentValidate(R.string.toast_consumptionamount_no);
						break;
					} else {
						if (Double.parseDouble(mConsumptionamount) > SCAN_INPUT_AMPUNT ) {
							Util.getContentValidate(R.string.toast_consumptionamount_max);
							break;
						} else {
							Util.getContentValidate(R.string.toast_consumptionamount_scan);
						}
					}
					
					if (!Util.isEmpty(mNodiscount)) {
						if (Double.parseDouble(mConsumptionamount) - Double.parseDouble(mNodiscount) < SCAN_INPUT_DIFF) {
							Util.getContentValidate(R.string.toast_consumptionamount_diff);
							break;
						}
					}
					
				} else {
					Log.d(TAG, "型号 ohter。。。。。");
					if (Util.isEmpty(mConsumptionamount)) {
						Util.getContentValidate(R.string.toast_consumptionamount_nothing);
						break;
					}
					
					if (Double.parseDouble(mConsumptionamount) > SCAN_INPUT_AMPUNT ) {
						Util.getContentValidate(R.string.toast_consumptionamount_max);
						break;
					} 
					
					if (!Util.isEmpty(mNodiscount)) {
						if (Double.parseDouble(mConsumptionamount) - Double.parseDouble(mNodiscount) < SCAN_INPUT_DIFF) {
							Util.getContentValidate(R.string.toast_consumptionamount_diff);
							break;
						}
					}
					
					Intent intent = new Intent(getMyActivity(), ScanActivity.class);
					startActivity(intent);
				}
				
				break;
			default:
				break;
			}
		}
	};
	
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
		if (mDeviceModel.startsWith(DEVICEMODEL)) {
			Log.d(TAG, "型号 pos。。。。。");
			initScan();
		} 
		mEtConsumptionamount.setText("");
		mEtNodiscount.setText("");
		IntentFilter filter = new IntentFilter();
		filter.addAction(SCAN_ACTION);
		getMyActivity().registerReceiver(mScanReceiver, filter);
		Util.initDate(mEtConsumptionamount);//自动弹出软键盘
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
	 * 返回
	 */
	@OnClick(R.id.layout_turn_in)
	public void trunIdenCode(View view){
		getMyActivity().finish();
	}
}
