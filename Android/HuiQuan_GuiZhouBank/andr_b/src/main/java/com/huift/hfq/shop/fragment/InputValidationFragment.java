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
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.Activitys;
import com.huift.hfq.base.pojo.Coupon;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.ActivityVerificationActivity;
import com.huift.hfq.shop.activity.CouponVerificationActivity;
import com.huift.hfq.shop.model.GetCouponInfoByCodeTask;
import com.huift.hfq.shop.model.GetInfoByActCodeTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 输入验证码验证
 * @author qian.zhou
 */
public class InputValidationFragment extends Fragment {

	private final static String SCAN_ACTION = "urovo.rcv.message";// 扫描结束action
	/** 设备型号*/
	public static final String DEVICEMODEL = "SQ";
	/** 搜索的参数*/
	private String mSearch;
	/** 判断是验证代金券还是活动*/
	private String mIsGo;
	private ScanManager mScanManager;
	private SoundPool mSoundpool = null;
	private int mSoundid;
	private boolean mIsScaning = false;
	private Vibrator mVibrator;
	private String mBarcodeStr;
	/** 输入的验证码*/
	private EditText mEtInputValidation;
	/** 设备型号*/
	private String mDeviceModel;
	
	private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			mIsScaning = false;
			mSoundpool.play(mSoundid, 1, 1, 0, 0, 1);
			mEtInputValidation.setText("");
			mVibrator.vibrate(100);

			byte[] barcode = intent.getByteArrayExtra("barocode");
			// byte[] barcode = intent.getByteArrayExtra("barcode");
			int barocodelen = intent.getIntExtra("length", 0);
			byte temp = intent.getByteExtra("barcodeType", (byte) 0);
			mBarcodeStr = new String(barcode, 0, barocodelen);
			mEtInputValidation.setText(mBarcodeStr);
		}
	};
	
	public static InputValidationFragment newInstance() {
		Bundle args = new Bundle();
		InputValidationFragment fragment = new InputValidationFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_input_validation, container, false);
		ViewUtils.inject(this, v);
		mVibrator = (Vibrator) getMyActivity().getSystemService(Context.VIBRATOR_SERVICE);
		Util.addHomeActivity(getActivity());
		Util.addLoginActivity(getActivity());
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

	private void init(View v) {
		mDeviceModel = Build.MODEL; // 设备型号  
		mEtInputValidation = (EditText) v.findViewById(R.id.et_input_validation);//输入验证码
		ImageView ivGo = (ImageView) v.findViewById(R.id.iv_go);
		ivGo.setOnClickListener(isGoClick);
		mIsGo = DB.getStr(ShopConst.CouponVerification.IS_GO);
		Util.initDate(mEtInputValidation);///自动弹出软键盘
	}
	
	/**
	 * 点击查询
	 */
	private OnClickListener isGoClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_go:
				mSearch = mEtInputValidation.getText().toString();
				if (Util.isEmpty(mSearch)) {
					Util.getContentValidate(R.string.vailid_nothing);
					break;
				}
				if (mIsGo.equals(String.valueOf(Util.NUM_ONE))) {//代金券
					
					getCouponInfoByCode(mSearch);
					 
				} else if (mIsGo.equals(String.valueOf(Util.NUM_TWO))) {//活动页面
					
					getInfoByActCode(mSearch);
					
				} else if (mIsGo.equals(String.valueOf(Util.NUM_THIRD))) {//扫码页面
					
					Util.getContentValidate(R.string.scan_not_start);
				}
				break;
			default:
				break;
			}
		}
	};
	
	/**
	 * 根据输入优惠券验证码获得优惠券的信息
	 */
	public void getCouponInfoByCode(String search){
		new GetCouponInfoByCodeTask(getMyActivity(), new GetCouponInfoByCodeTask.Callback() {
			@Override
			public void getResult(Coupon coupon) {
				if (coupon != null) {
					if (coupon.getCode() == ErrorCode.SUCC) {
						Intent intent = new Intent(getMyActivity(), CouponVerificationActivity.class);
						intent.putExtra(CouponVerificationFragment.COUPON_OBG, coupon);
						startActivity(intent);
					} else {
						Util.showToastZH(coupon.getMsg());
					}
				}else {
					Util.showToastZH("请求失败，请稍后再试！");
				}
			}
		}).execute(search);
	}
	
	/**
	 * 根据用户活动验证码获得相关信息
	 */
	public void getInfoByActCode(String search){
		new GetInfoByActCodeTask(getActivity(), new GetInfoByActCodeTask.Callback() {
			@Override
			public void getResult(Object result) {
				if (result != null) {
					Activitys activitys = Util.json2Obj(result.toString(), Activitys.class);
					if (activitys != null) {
						Intent intent = new Intent(getActivity(), ActivityVerificationActivity.class);
						intent.putExtra(ActivityVerificationFragment.ACTIVITY_OBJ, activitys);
						startActivity(intent);
					}
				}
			}
		}).execute(search);
	}
	
	private void initScan() {
		mScanManager = new ScanManager();
		mScanManager.openScanner();
		mScanManager.switchOutputMode(0);
		mSoundpool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100); // MODE_RINGTONE
		mSoundid = mSoundpool.load("/etc/Scan_new.ogg", 1);
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
		if (mDeviceModel.startsWith(DEVICEMODEL)) {
			initScan();
		}
		mEtInputValidation.setText("");
		IntentFilter filter = new IntentFilter();
		filter.addAction(SCAN_ACTION);
		getMyActivity().registerReceiver(mScanReceiver, filter);
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	/**
	 * 点击事件
	 */
	@OnClick({ R.id.layout_turn_in })
	private void ivreturnClickTo(View v) {
		getActivity().finish();
	}
}
