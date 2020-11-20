package cn.suanzi.baomi.shop.activity;

import java.io.IOException;
import java.util.Vector;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.TimeUtilsDate;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SetPayResultTask;
import cn.suanzi.baomi.base.pojo.OrderInfo;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.base.utils.ActivityUtils;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.ThreeDES;
import cn.suanzi.baomi.base.utils.TimestampUtil;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.camera.CameraManager;
import cn.suanzi.baomi.shop.decoding.InactivityTimer;
import cn.suanzi.baomi.shop.decoding.ScanningHandler;
import cn.suanzi.baomi.shop.fragment.BillClassFragment;
import cn.suanzi.baomi.shop.fragment.ConfirmOrderFragment;
import cn.suanzi.baomi.shop.fragment.MyOrderManagerFragment;
import cn.suanzi.baomi.shop.model.ScanTask;
import cn.suanzi.baomi.shop.model.SweepQrCodeTask;
import cn.suanzi.baomi.shop.utils.MPosPluginHelper;
import cn.suanzi.baomi.shop.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/***
 * 扫一扫主页
 * @author wensi.yu
 */
public class ScanActivity extends FragmentActivity implements Callback {

	private static final String TAG = ScanActivity.class.getSimpleName();

	private final static String SCAN_TITLE_FLAG = "扫一扫";
	private final static String SCAN_TITLE = "扫付款码";
	/** 实物券和体验券的消费方式*/
	private final static String C_COMSUME = "3";
	/** 标示*/
	public final static String INTENG_FLAG = "1";
	/** 从哪个页面进入的标志*/
	public final static String FLAG = "flag";
	private ScanningHandler mHandler;
	private ViewfinderView mViewfinderView;
	private boolean mHasSurface;
	private Vector<BarcodeFormat> mDecodeFormats;
	private String mCharacterSet;
	private InactivityTimer mInactivityTimer;
	private MediaPlayer mMediaPlayer;
	private boolean mPlayBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean mVibrate;
	private String mTrace;
	/** 点单号 */
	private String mConsumeCode;
	/** 得到的金额 */
	private String mResultMoney;
	/** 订单信息*/
	private OrderInfo mOrderInfo;
	/** 跳转的标志*/
	private String mIntentFlag;

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
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_scan);
		ViewUtils.inject(this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		Util.addActivity(this);//手机取消
		init();
	}
	
	private void init() {
		ActivityUtils.add(this);//订单完成
		LinearLayout ivBanck = (LinearLayout) findViewById(R.id.layout_turn_in);
		ivBanck.setVisibility(View.VISIBLE);
		TextView tvFunction = (TextView) findViewById(R.id.tv_mid_content);
		mIntentFlag = this.getIntent().getStringExtra(INTENG_FLAG);
		if ("1".equals(mIntentFlag)) {
			tvFunction.setText(SCAN_TITLE_FLAG);
		} else {
			tvFunction.setText(SCAN_TITLE);
		}
		LinearLayout lyFlash = (LinearLayout) findViewById(R.id.layout_msg);
		TextView tvFlash = (TextView) findViewById(R.id.tv_msg);
		tvFlash.setVisibility(View.VISIBLE);
		lyFlash.setOnClickListener(flashClick);
		
		CameraManager.init(getApplication());
		mViewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		mHasSurface = false;
		mInactivityTimer = new InactivityTimer(this);
	}

	/**
	 * 闪光灯
	 */
	OnClickListener flashClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			CameraManager.get().flashHandler();
		}
	};
	
	@Override
	protected void onResume() {
		super.onResume();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (mHasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			// surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		mDecodeFormats = null;
		mCharacterSet = null;
		mPlayBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			mPlayBeep = false;
		}// 没有执行
		initBeepSound();
		mVibrate = true;
		mViewfinderView.setVisibility(View.VISIBLE);
	}

	/**
	 * 暂停
	 */
	@Override
	protected void onPause() {
		super.onPause();
		if (mHandler != null) {
			mHandler.quitSynchronously();
			mHandler = null;
		}
		CameraManager.get().closeDriver();
	}

	/**
	 * 销毁
	 */
	@Override
	protected void onDestroy() {
		mInactivityTimer.shutdown();
		super.onDestroy();
	}
	
	/**
	 * 扫描结果进行解码
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		mInactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText().replaceAll(" ", "");
		Log.d(TAG, "扫描的结果=" + resultString);
		if (resultString.equals("")) {
			Util.getContentValidate(R.string.toast_scan_nothing);
			ScanActivity.this.finish();
		} else {
			// 判断是否联网
			if (Util.isNetworkOpen(ScanActivity.this)) {
				Log.d(TAG, "有网络");
				if (resultString.startsWith("http")) {
					Log.d(TAG, "结果-------11");
					Intent resultIntent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("result", resultString);
					// bundle.putParcelable("bitmap", barcode);
					resultIntent.putExtras(bundle);
					this.setResult(RESULT_OK, resultIntent);
					Log.i(TAG, "resultString==" + resultString);
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(resultString));
					startActivity(intent);
					ScanActivity.this.finish();
				} else if (resultString.startsWith("{")) {
					Log.d(TAG, "结果-------22");
					try {
						JSONObject jsonObject = new JSONObject(resultString);
						String sSrc = jsonObject.getString("sSrc");
						String payType = jsonObject.getString("payType");
						String qType = jsonObject.getString("qType");
						Log.d(TAG, "qType===" + qType);
						String sCode = jsonObject.getString("sCode");
						byte[] resultCode = ThreeDES.decryptMode(Util.KeyBytes, sCode);
						String resultScan = new String(resultCode);
						Log.d(TAG, "resultScan===" + resultScan);
						// 订单号
						mConsumeCode = jsonObject.getString("ordernbr");
						Log.d(TAG, "consumeCode==" + mConsumeCode);
						// 需要令牌认证
						UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
						String tokenCode = userToken.getTokenCode();
						final String shopCode = userToken.getShopCode();
						if (resultScan.equals(sSrc)) {
							if ("qr002".equals(qType)) {
								if (Integer.parseInt(payType) == 0) {// 刷卡支付   
									new ScanTask(this, new ScanTask.Callback() {
										@Override
										public void getResult(net.minidev.json.JSONObject mResult) {
											mResultMoney = mResult.get("realPay").toString();
											Log.d(TAG, "得到的金额==" + mResultMoney);
											// 查询的shopCode
											String shopCodeScan = mResult.get("shopCode").toString(); // 商家编码
											String shopId = mResult.get("shopId").toString(); // 商家号
											// 判断是否是本家商店的扫描
											if (shopCode.equals(shopCodeScan)) {
												mTrace = TimeUtilsDate.getNowString("yyyyMMddHHmiss");
												MPosPluginHelper.consume(ScanActivity.this, mResultMoney, mTrace, "", "",mConsumeCode, "",shopId);
												ScanActivity.this.finish();
											} else {
												Util.getContentValidate(R.string.toast_pay_error);
												Intent intent = new Intent(ScanActivity.this, HomeActivity.class);
												startActivity(intent);
												ScanActivity.this.finish();
											}
										}
									}).execute(mConsumeCode, tokenCode);
								} else if (Integer.parseInt(payType) == 1) {// 现金支付
									Util.getContentValidate(R.string.toast_scan_ok);
									Intent intent = new Intent(this, HomeActivity.class);
									startActivity(intent);
									ScanActivity.this.finish();
								} else if (Integer.parseInt(payType) == 3) {//优惠券
									setPayResult("SUCCESS");
								} else{
									Util.getContentValidate(R.string.toast_scan_error);
								}
							} else {
								Util.getContentValidate(R.string.toast_scan);
							} 
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				} else if (resultString.startsWith("payType")) {//扫码收款
					Log.d(TAG, "结果-------33");
					String[] payTpye = resultString.split("\\:");
					final String payTypeCode = payTpye[1];
					Log.d(TAG, "payTpyeCode == " + payTypeCode);
					TimestampUtil.setRomdonValue(this,new TimestampUtil.CallBack() {
						
						@Override
						public void getNetTime(String netTime) {
							if (null == netTime) {
								return;
							}
							Message message = Message.obtain();
							String payTypeScan = payTypeCode + netTime ;
							message.what = 1;
							message.obj = payTypeScan;
							mScanHandler.sendMessage(message);
						}
					});
				} else if (resultString.length() == Util.NUM_TEN) {//扫描条形码
					Log.d(TAG, "结果-------44");
					Log.d(TAG, "条码的结果=="+resultString);
					String flag = getIntent().getStringExtra(FLAG);
					Log.d(TAG, "flag == " +flag);
					if (flag.equals(String.valueOf(Util.NUM_ONE))) {
						Intent intent = new Intent();
						intent.putExtra(BillClassFragment.BAR_CODE_RESULT, resultString);
						this.setResult(BillClassFragment.BAR_CODE_SUCC,intent);
						finish();
					} else if (flag.equals(String.valueOf(Util.NUM_TWO))) {
						Intent intent = new Intent();
						intent.putExtra(MyOrderManagerFragment.BAR_CODE_RESULT, resultString);
						this.setResult(MyOrderManagerFragment.BAR_CODE_SUCC,intent);
						finish();
					} else if (flag.equals(String.valueOf(Util.NUM_THIRD))) {
						Intent intent = new Intent(ScanActivity.this, ScanResultActivity.class);
						intent.putExtra("resultString", resultString);
						this.startActivity(intent);
						ScanActivity.this.finish();
					}
					
				} else {
					Log.d(TAG, "结果-------55");
					if (resultString.length() == Util.NUM_EIGTH) {
						Util.getContentValidate(R.string.toast_scanagain);
						ScanActivity.this.finish();
					} else {
						Intent intent = new Intent(ScanActivity.this, ScanResultActivity.class);
						intent.putExtra("resultString", resultString);
						this.startActivity(intent);
						ScanActivity.this.finish();
					}
				}
			} else {
				Log.d(TAG, "没有网络");
				Intent intent = new Intent(this, HomeActivity.class);
				startActivity(intent);
				ScanActivity.this.finish();
			}
		}
	}
	
	/**
	 * 扫描二维码
	 */
	private void sweepQrCode (String payTypeScan) {
		
		new SweepQrCodeTask(this, new SweepQrCodeTask.Callback() {
			
			@Override
			public void getResult(net.minidev.json.JSONObject result) {
				if (null == result) {
					return;
				}
				mViewfinderView.setVisibility(View.GONE);
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
					Intent intent = new Intent(ScanActivity.this,ConfirmOrderActivity.class);
					intent.putExtra(ConfirmOrderFragment.BANK_ACCOUNTCODE, bankAccountCode);
					intent.putExtra(ConfirmOrderFragment.USER_CODE, userCode);
					ScanActivity.this.startActivity(intent);
				}
			}
		}).execute(payTypeScan);
	}
	
	/**
	 * 实物券和体验券的支付
	 */
	private void setPayResult(final String payResult) {
		
		new SetPayResultTask(ScanActivity.this, new SetPayResultTask.OnResultListener() {

			@Override
			public void onSuccess() {
				Util.getContentValidate(R.string.jaoyi_succ);
				finish();
			}

			@Override
			public void onError() {

			}
		}).execute(mConsumeCode, "", payResult);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		mScanHandler.removeCallbacksAndMessages(null);
	}

	/**
	 * 自动聚焦
	 * 
	 * @param surfaceHolder
	 */
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (mHandler == null) {
			mHandler = new ScanningHandler(this, mDecodeFormats, mCharacterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		if (!mHasSurface) {
			mHasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mHasSurface = false;
	}

	public ViewfinderView getViewfinderView() {
		return mViewfinderView;
	}

	public void drawViewfinder() {
		mViewfinderView.drawViewfinder();
	}

	public Handler getHandler() {
		Log.d("aa", "☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆");
		return mHandler;
	}

	private void initBeepSound() {
		if (mPlayBeep && mMediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.setOnCompletionListener(beepListener);
			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try {
				mMediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mMediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mMediaPlayer.prepare();
			} catch (IOException e) {
				mMediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (mPlayBeep == true && mMediaPlayer != null) {
			mMediaPlayer.start();
		}
		
		if (mVibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	/**
	 * 点击返回按钮
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	public void btnAclist(View view) {
		finish();
	}
}
