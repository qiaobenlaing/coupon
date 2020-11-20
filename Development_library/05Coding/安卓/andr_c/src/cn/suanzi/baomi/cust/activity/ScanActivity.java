package cn.suanzi.baomi.cust.activity;

import java.io.IOException;
import java.util.Map;
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
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.Shop;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.ThreeDES;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.camera.CameraManager;
import cn.suanzi.baomi.cust.decoding.InactivityTimer;
import cn.suanzi.baomi.cust.decoding.ScanningHandler;
import cn.suanzi.baomi.cust.fragment.ScanSuccessFragment;
import cn.suanzi.baomi.cust.model.GrabCouponScanTask;
import cn.suanzi.baomi.cust.model.ScanCodeTask;
import cn.suanzi.baomi.cust.util.SkipActivityUtil;
import cn.suanzi.baomi.cust.view.ViewfinderView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

/**
 * 扫一扫主页
 * 
 * @author wensi.yu
 *
 */
public class ScanActivity extends FragmentActivity implements Callback {

	private static final String TAG = ScanActivity.class.getSimpleName();

	private final static String SCAN_TITLE ="扫一扫";
	/** 对象*/
	public final static String SHOP_OBJ ="shopobj";
	public final static String TYPE ="scanType";
	/** 判断是从哪个页面进来的*/
	public final static String SCAN_ONE ="scanOne";//店铺详情
	public final static String SCAN_TWO ="scanTwo";//点餐
	/** api返回的失败 0 */
	public static final int API_FAIL = 0;
	private final static String SHOP_URL_PATH = "";
	private final static String SHOP_URL_ID = "shopCode";
	private final static String NUM_TWO = "2";
	/**返回*/
	private ImageView mIvBanck;
	/**功能描述*/
	private TextView mTvFunction;
	
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
	/**标示**/
	private String qType = "";
	/**商店编码*/
	private String mShopCode;
	/**类型*/
	private String mScanType;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_scan);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		Util.addActivity(this);
		Util.addActActivity(this); // 买单 
		Util.addLoginActivity(this);//扫描失败
		init();
	}
	
	private void init() {
		//返回
		mIvBanck = (ImageView) findViewById(R.id.iv_turn_in);
		mIvBanck.setVisibility(View.VISIBLE);
		//功能
		mTvFunction = (TextView) findViewById(R.id.tv_mid_content);
		mTvFunction.setText(SCAN_TITLE);
		mIvBanck.setOnClickListener(ivListener);
		LinearLayout lyFlash = (LinearLayout) findViewById(R.id.layout_msg);
		ImageView msg = (ImageView) findViewById(R.id.iv_add);
		msg.setVisibility(View.VISIBLE);
		lyFlash.setOnClickListener(msgClick);
		
		CameraManager.init(getApplication());
		mViewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		mHasSurface = false;
		mInactivityTimer = new InactivityTimer(this);
		mScanType = this.getIntent().getStringExtra(TYPE);
		Log.d(TAG, "scanType===="+mScanType);
	}

	/**
	 * 闪光灯
	 */
	private OnClickListener msgClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			CameraManager.get().flashHandler();
		}
	};
	
	/**
	 * 点击返回
	 */
	private OnClickListener ivListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (mScanType.equals(SCAN_TWO)) {
				Log.d(TAG, "点餐。。。。。。11");
				H5ShopDetailActivity.setCurrentPage("backup");
				Util.exitAct();
			} else {
				Log.d(TAG, "other。。。。。。11");
				finish();
			}
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
            //surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		mDecodeFormats = null;
		mCharacterSet = null;
		mPlayBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			mPlayBeep = false;
		}//没有执行
			initBeepSound();
			mVibrate = true;
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
		 * 重写点击返回按钮方法 
		 */
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if (mScanType.equals(SCAN_TWO)) { // 点餐
					Log.d(TAG, "点餐。。。。。。22");
					H5ShopDetailActivity.setCurrentPage("backup");
					Util.exitAct();
				} else { // 除了点餐外的
					Log.d(TAG, "other。。。。。。22");
					finish();
				}
			}
			return super.onKeyDown(keyCode, event);
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
		 * 执行结果
		 * @param result
		 * @param barcode
		 */
		public void handleDecode(Result result, Bitmap barcode) {
			mInactivityTimer.onActivity();
			playBeepSoundAndVibrate();
			String resultString = result.getText();
			Log.d(TAG, "resultString=="+resultString);
			
			if (resultString == null || "".equals(resultString)) {
				Util.getContentValidate(R.string.toast_scanerror);
				return;
			}
			
			if (resultString.startsWith("http")) {//网页
				final Shop shop = (Shop) this.getIntent().getSerializableExtra(SHOP_OBJ);
				// 如果是商店链接，直接跳转到商店详情页
				if (resultString.contains(SHOP_URL_PATH)) {
					Log.d(TAG, "shopUrl===="+resultString);
					try {
			   			Map<String, String> params = Util.getUrlParams(resultString);
			   			/*Set<Entry<String,String>> entrySet = params.entrySet();
			   			Iterator<Entry<String, String>> iterator = entrySet.iterator();
			   			while(iterator.hasNext()){
			   				Entry<String, String> next = iterator.next();
			   				Log.d(TAG, "$$$$$$"+next.getKey()+"-----"+next.getValue());
			   			}*/
						mShopCode = params.get(SHOP_URL_ID);
						if (Util.isEmpty(mShopCode)) {
							Util.getContentValidate(R.string.toast_scan_shop);
							return;
						} 
						Log.d(TAG, "shopcode===="+mShopCode);
						if (null != mShopCode || Util.isEmpty(mShopCode)) {
							Log.d(TAG, "scanType11===="+mScanType);
							if (mScanType.equals(SCAN_ONE)) {//跳到店铺详情
								SkipActivityUtil.skipNewShopDetailActivity(ScanActivity.this, mShopCode);
							
							} else if (mScanType.equals(SCAN_TWO)) {//跳到订单扫描
								if (mShopCode.equals(shop.getShopCode())) {
									new ScanCodeTask(ScanActivity.this, new ScanCodeTask.Callback() {
										@Override
										public void getResult(int retCode) {
											if (retCode == ErrorCode.SUCC) {
												Intent intent = new Intent(ScanActivity.this, ScanSuccessActivity.class);
												intent.putExtra(ScanSuccessFragment.SHOP_OBJ, shop);
												startActivity(intent);
												finish();
											}
										}
									}).execute(mShopCode);
								} else if (!mShopCode.equals(shop.getShopCode())) {                        
									startActivity(new Intent(ScanActivity.this, ScanFailActivity.class));//商户不匹配
			     					
								} else {
									startActivity(new Intent(ScanActivity.this, ScanFailOtherActivity.class));//扫描失败
								}
							}
						}
						
					} catch (SzException e) {
						Util.getContentValidate(R.string.invalid_two_code);
						Intent intent = new Intent(ScanActivity.this, HomeActivity.class);
					    ScanActivity.this.finish();
					}     
				//其他页面的处理
				} else {
					Intent resultIntent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("result", resultString);
					//bundle.putParcelable("bitmap", barcode);
					resultIntent.putExtras(bundle);
					this.setResult(RESULT_OK, resultIntent);
					Log.i(TAG, "resultString 顾客端  扫面前===============:"+resultString);
					Log.i(TAG, "跳转网址");
					Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(resultString));
					startActivity(intent);
					ScanActivity.this.finish();
				}
			} else if (resultString.startsWith("{")){//json格式
				try {
					Log.i(TAG, "resultString  扫描===============:"+resultString);
					JSONObject obj = new JSONObject(resultString);
					qType = obj.getString("qType");
					String sCode = obj.getString("sCode");
					byte[] resultCode = ThreeDES.decryptMode(Util.KeyBytes, sCode);
					Log.i(TAG, "解码后=============="+new String(resultCode));
					String resultScan = new String(resultCode);
					String shopCode = obj.getString("shopCode");
					String sSrc = obj.getString("sSrc");
					Log.i(TAG, "shopCode======="+shopCode);
					Log.i(TAG, "sSrc======="+sSrc);
					Log.i(TAG, "qType======="+qType);
					if (resultScan.equals(sSrc)) {
						if ("qr001".equals(qType)) {
						    SkipActivityUtil.skipNewShopDetailActivity(ScanActivity.this, shopCode);
						    ScanActivity.this.finish();
						} else if ("qr002".equals(qType)) {
						    SkipActivityUtil.skipNewShopDetailActivity(ScanActivity.this, shopCode);
						    ScanActivity.this.finish();
						}
					} else {
						Util.getContentValidate(R.string.toast_noscan);
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			} else if (resultString.startsWith("coupon")){//优惠券
				String[] coupon = resultString.split("\\|");
				String couponCode = coupon[1];
				Log.d(TAG, "couponCodeScan,"+couponCode);
				if (null != couponCode || !Util.isEmpty(couponCode)) {
					new GrabCouponScanTask(ScanActivity.this, new GrabCouponScanTask.Callback() {
						@Override
						public void getResult(net.minidev.json.JSONObject result) {
							if (null == result) {
								return;
							} 
							int resultCode = Integer.parseInt(result.get("code").toString());
							Log.d(TAG, "resultCode==="+resultCode);
							if (ErrorCode.SUCC == resultCode) {
								Log.d(TAG, "进来了。。。。");
								SkipActivityUtil.skipNewShopDetailActivity(ScanActivity.this, mShopCode);
							} else {
								Log.d(TAG, "home");
								Intent intentHome = new Intent(ScanActivity.this,HomeActivity.class);
								startActivity(intentHome);
							    ScanActivity.this.finish();
							}
						}
					}).execute(couponCode,NUM_TWO);
				}
			} else {
				if (resultString.length() == Util.NUM_EIGTH){
					Util.getContentValidate(R.string.toast_scanagain); 
					ScanActivity.this.finish();
				} else {
					Util.getContentValidate(R.string.invalid_two_code);
					Intent intent = new Intent(ScanActivity.this, HomeActivity.class);
				    ScanActivity.this.finish();
				}
				
			}
		}

		/**
		 * 自动聚焦
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
				mHandler = new ScanningHandler(this, mDecodeFormats,mCharacterSet);
			}
		}
		
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
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
			return mHandler;
		}
		
		private void initBeepSound() {
			if (mPlayBeep && mMediaPlayer == null) {
				Log.i("aa", "***************8-1");
				// The volume on STREAM_SYSTEM is not adjustable, and users found it
				// too loud,
				// so we now play on the music stream.
				setVolumeControlStream(AudioManager.STREAM_MUSIC);
				mMediaPlayer = new MediaPlayer();
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mMediaPlayer.setOnCompletionListener(beepListener);
				AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
				try {
					mMediaPlayer.setDataSource(file.getFileDescriptor(),file.getStartOffset(), file.getLength());
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
			if (mPlayBeep==true && mMediaPlayer != null) {
				mMediaPlayer.start();
			}
			/***震动验证***/
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
}
