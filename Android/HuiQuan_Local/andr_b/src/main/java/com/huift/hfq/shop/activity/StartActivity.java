// ---------------------------------------------------------
// @author    
// @version   1.0.0
// @createTime 2015.5.4
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.activity;

import com.huift.hfq.base.Const;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.GetGuideInfoTask;
import com.huift.hfq.base.model.GuideImgModel;
import com.huift.hfq.base.model.LoginTask;
import com.huift.hfq.base.pojo.GuideImgb;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.ImageDownloadCallback;
import com.huift.hfq.base.utils.LocationUtil;
import com.huift.hfq.base.view.StatusBarView;
import com.huift.hfq.shop.ShopConst;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import com.huift.hfq.shop.R;
import cn.jpush.android.api.JPushInterface;
 
/**
 * 用于显示广告和检查更新
 * @author 
 */
public class StartActivity extends Activity {
	
	private final static String TAG = "StartActivity";
	
	private Handler mhander;
	/** 登录的用户名和密码*/
	private String mUserName;
	private String mUserPwd;
	/** 欢迎页图片*/
	private ImageView mIvStart;
	/** 欢迎页图片路径*/
	private String mStartUrl;
	private Bitmap mBitmap;
	
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		StatusBarView.statusBar(this);
		//添加
		Util.addActivity(StartActivity.this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		init();
	}
	
	private void init() {
		mIvStart =  (ImageView) findViewById(R.id.iv_start_bg);
		
		getGuideImgData();//得到缓存的数据
		getGuideInfo();//获得欢迎页图片
		mhander = new Handler();
        //TODO  获取网络图片源并设置
        // 只在进来的时候 提示更新app
//		DB.saveBoolean(Key.SHOP_IS_CANCEL_UPDATE, true);
		// 定位
		LocationUtil.getLocationClient();
        //TODO   将SharedPrefrence抽离出来
        /** 判断是否为第一次使用 */
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = sharedPreferences.getBoolean(DB.Key.SHOP_IS_FIRST_RUN, true);
        Editor editor = sharedPreferences.edit(); 
        if (isFirstRun) {//若为第一次使用
        	editor.putBoolean(DB.Key.SHOP_IS_FIRST_RUN, false);//修改信息
        	editor.commit();
            //延时2000ms后跳转至GuideActivity
            new Handler().postDelayed(new Runnable() {
			    @Override
			    public void run() {
					Intent  intent = new Intent(StartActivity.this, GuideActivity.class);
					StartActivity.this.startActivity(intent);
					StartActivity.this.finish();
			    }
            }, Const.START_PAGE_SHOW_MSEC);
       } else {//非第一次使用
        	 //延时2000ms后跳转至LoginActivity
        	 new Handler().postDelayed(new Runnable() {
				@Override
     			public void run() {
					//得到登录的用户名和密码
					SharedPreferences mSharedPreferences = getSharedPreferences(ShopConst.LoginSave.LOGIN_KEEP, Context.MODE_PRIVATE);
					String mobileNbr = mSharedPreferences.getString("mobileNbr", "");
					String password = mSharedPreferences.getString("password", "");
					Log.d(TAG, "mobileNbr========="+mobileNbr);
					Log.d(TAG, "password========="+password);
					UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
					Log.d(TAG, userToken+"=userToken");
					//传过来的registerid
					String regId = null;
					if ("".equals(regId) && regId == null) {
						regId = "";
					} else {
						regId = DB.getStr(ShopConst.RegisterSave.JPUSH_REGID);
						Log.i(TAG, "RegisterSave="+regId);
					}
					//登录的状态
					String loginStatus = StartActivity.this.getIntent().getStringExtra(LoginTask.ALL_LOGIN);
					
					if (Util.isEmpty(mobileNbr) || Util.isEmpty(password)) {
						startActivity(new Intent(StartActivity.this, LoginActivity.class));
						finish();
					} else {
						if (userToken == null) {
							startActivity(new Intent(StartActivity.this, LoginActivity.class));
							finish();
						} else {
							mUserName = userToken.getMobileNbr();
							mUserPwd = userToken.getPassword();
							if (mobileNbr.equals(mUserName) && password.equals(mUserPwd)) {
								//是否联网状态
								if (Util.isNetworkOpen(StartActivity.this)) {
									loginStatus = Const.Login.ANDR_B_HOMEFRAGMENT;
									// 执行登录
									new LoginTask(StartActivity.this, new LoginTask.Callback() {
										@Override
										public void getResult(int result) {
											//当返回错误信息时
											if (result == ErrorCode.FAIL) {
												startActivity(new Intent(StartActivity.this, LoginActivity.class));
												finish();
											}
										}
									}, HomeActivity.class,loginStatus).execute(mobileNbr,password,regId);
								} else {
									startActivity(new Intent(StartActivity.this, LoginActivity.class));
									finish();
									Toast.makeText(StartActivity.this, R.string.toast_login_internet,Toast.LENGTH_SHORT).show();
								}
							} else {
								mhander.postDelayed(new Runnable() {   
									@Override
									public void run() {
										if (Util.isNetworkOpen(StartActivity.this)) {
											Intent  intent = new Intent(StartActivity.this, LoginActivity.class);
											StartActivity.this.startActivity(intent);
											finish();
										} else {
											Intent intent = new Intent(StartActivity.this, LoginActivity.class);
											StartActivity.this.startActivity(intent);
											finish();
										}
									}
								}, 3000);
							}
						}
					} 
     			}
     		}, Const.START_PAGE_SHOW_MSEC);
        }
	}

	/**
	 * 获取欢迎页图片
	 */
	public void getGuideInfo () {
		new GetGuideInfoTask(StartActivity.this, new GetGuideInfoTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				/*if (result != null) {
					try {
						mStartUrl = result.get("value").toString();
						if (!Util.isEmpty(mStartUrl)) {
							Util.showImage(StartActivity.this, mStartUrl, mIvStart);
							Log.d(TAG, "imageurl>>>>>" + mStartUrl);
							String url = Const.IMG_URL + mStartUrl;
							saveGuideBitmapData(url);
						} else {
							mIvStart.setImageResource(R.drawable.startpage);
						}
						
					} catch (Exception e) {
						Log.d(TAG, "图片路径错误");
					}
				}*/
				mIvStart.setImageResource(R.drawable.startpage);
			}
		}).execute(GetGuideInfoTask.ANDR_B);
	}
	
	/**
	 * 获取缓存的欢迎页图片路径
	 */
	public void getGuideImgData(){
		GuideImgb guideImgb = GuideImgModel.getGuidebById(GuideImgModel.ID);
		if (guideImgb != null && !Util.isEmpty(guideImgb.getGuideImg())) {
			mStartUrl = guideImgb.getGuideImg();
			Util.showImage(StartActivity.this, mStartUrl, mIvStart);
			Log.d(TAG, "图片路径》》》11》" + mStartUrl);
		} else if (guideImgb != null && guideImgb.getBitmap() != null) { 
			mBitmap = guideImgb.getBitmap();
			mIvStart.setImageBitmap(mBitmap);
			Log.d(TAG, "图片路径》》》22》" + mBitmap);
		} else {
			mIvStart.setImageResource(R.drawable.startpage);
			Log.d(TAG, "图片路径》》》33》" );
		}
	}
	
	/**
	 * 将图片路径缓存已bitmap的形式缓存
	 */
	public void saveGuideBitmapData(final String url){
		Util.getLocalOrNetBitmap(url, new ImageDownloadCallback() {
			@Override
			public void success(Bitmap bitmap) {
				Log.d(TAG, "imageurl>>>>22>");
				mBitmap = bitmap;
				GuideImgModel.saveGuideImg_b(new GuideImgb(GuideImgModel.ID, url, bitmap));
			}
			@Override
			public void fail() {
				mBitmap = null;
			}
		});
	}
	
	@Override
	protected void onResume() {
		JPushInterface.onResume(this);
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
	}
}

