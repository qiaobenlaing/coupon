package cn.suanzi.baomi.cust.activity;

import java.io.File;

import net.minidev.json.JSONObject;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import cn.jpush.android.api.JPushInterface;
import cn.suanzi.baomi.base.Const;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.data.Storage;
import cn.suanzi.baomi.base.model.GetGuideInfoTask;
import cn.suanzi.baomi.base.model.GuideImgModel;
import cn.suanzi.baomi.base.model.LoginTask;
import cn.suanzi.baomi.base.pojo.GuideImg;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.ImageDownloadCallback;
import cn.suanzi.baomi.base.utils.LocationUtil;
import cn.suanzi.baomi.base.utils.SaveImageUtil;
import cn.suanzi.baomi.base.view.StatusBarView;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.model.AddCrashLogTask;

/**
 * 用于显示广告和检查更新
 * 
 * @author Wei.Fang
 * 
 */
public class StartActivity extends Activity {

	private final static String TAG = StartActivity.class.getSimpleName();
	/** 缓存图片*/
	private final static String SAVE_IMG_KEY = "guideImgKey";
	private Context mContext;
	private String mLastedCrashLogPath = Storage.getExtDir().getAbsolutePath() + "/lastedCrash.log";;
	/** 欢迎图片 */
	private ImageView mIvStartBg;
	/** 图片的url*/
	private String mImgUrl;
	/** 启动页的图片*/
	private Bitmap mBitmap;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				String url = (String) msg.obj;
				addCrashLog(url);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StatusBarView.statusBar(this);
		setContentView(R.layout.activity_start);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		Drawable img = SaveImageUtil.getImg(SAVE_IMG_KEY, "guideImgName");
		mIvStartBg = (ImageView) findViewById(R.id.iv_start_bg);
		mIvStartBg.setImageDrawable(img);
		mContext = getApplicationContext();
		init();
	}
	
	private void init() {
		// 检查并上传崩溃日志
		uploadCrashLog();
		// 获得数据库里的数据
		getDbGuideImgData();
		// 定位
		LocationUtil.getLocationClient();
		getGuideInfo(); // 欢迎页的图片
		initData();
	}

	/**
	 * 出事数据
	 */
	private void initData() {
		
		// 判断是否为第一次使用
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		boolean isFirstRun = true;
		try {
			isFirstRun = sharedPreferences.getBoolean(DB.Key.CUST_IS_FIRST_RUN, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Editor editor = sharedPreferences.edit();
		// 跳转时间
		int startTime = 0;
		if (Util.isEmpty(mImgUrl)) {
			startTime = Const.START_PAGE_SHOW_MSEC * 3;
		} else {
			startTime = Const.START_PAGE_SHOW_MSEC ;
		}
		if (isFirstRun) {// 若为第一次使用
			editor.putBoolean(DB.Key.CUST_IS_FIRST_RUN, false);// 修改信息
			editor.commit();
			// 延时2000ms后跳转至GuideActivity
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent intent = new Intent(mContext, GuideActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);
					finish();
				}
			}, startTime);
		} else {// 非第一次使用
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// 传过来的registerid
					String regId = null;
					if ("".equals(regId) && regId == null) {
						regId = "";
					} else {
						regId = DB.getStr(CustConst.JPush.JPUSH_REGID);
						Log.d(TAG, "RegisterSave=" + regId);
					}
					// 登录的状态
					String loginStatus = StartActivity.this.getIntent().getStringExtra(LoginTask.ALL_LOGIN);
					// 得到登陆的用户名和密码
					SharedPreferences mSharedPreferences = getSharedPreferences(CustConst.LoginSave.LOGIN_KEEP,Context.MODE_PRIVATE);
					String mobileNbr = mSharedPreferences.getString("mobileNbr", "");
					String password = mSharedPreferences.getString("password", "");

					UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
					Log.d(TAG, userToken + "userToken");
					if (userToken == null) {
						DB.saveBoolean(DB.Key.CUST_LOGIN, false);
						startActivity(new Intent(StartActivity.this, HomeActivity.class));
						finish();
					} else {
						String userName = userToken.getMobileNbr();
						String userPwd = userToken.getPassword();
						if (mobileNbr.equals(userName) && password.equals(userPwd)) {
							// 判断是否联网
							if (Util.isNetworkOpen(StartActivity.this)) {
								new LoginTask(StartActivity.this, new LoginTask.Callback() {
									@Override
									public void getResult(int result) {
										// 当返回错误信息时
										if (result == ErrorCode.FAIL) {
											startActivity(new Intent(StartActivity.this, HomeActivity.class));
											finish();
										}
									}
								}, HomeActivity.class, loginStatus).execute(mobileNbr, password, regId);
							} else {
								DB.saveBoolean(DB.Key.CUST_LOGIN, false);
								startActivity(new Intent(StartActivity.this, HomeActivity.class));
								finish();
							}
						} else {
							Intent intent = new Intent(mContext, HomeActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							mContext.startActivity(intent);
							finish();
						}
					}
				}
			}, startTime);
		}
	}

	/**
	 * 获得缓存数据
	 */
	private void getDbGuideImgData () {
		GuideImg guideImg = GuideImgModel.getGuideById(GuideImgModel.ID);
		if (null != guideImg && !Util.isEmpty(guideImg.getGuideImg())) {
			Log.d(TAG, "我进来了*************2");
			mImgUrl = guideImg.getGuideImg();
			Util.showGuideImage(StartActivity.this, mImgUrl, mIvStartBg);
		} else {
			Log.d(TAG, "我进来了*************3");
			mIvStartBg.setImageResource(R.drawable.startpage);
		}
	}
	
//	/**
//	 * 得到bitMap的图片
//	 */
//	private void getBitMap(final String url) {
//		Util.getLocalOrNetBitmap(Const.IMG_URL + url, new ImageDownloadCallback() {
//			@Override
//			public void success(final Bitmap bitmap) {
//				mBitmap = bitmap;
//				Log.d(TAG, "mBitmap:" + mBitmap);
//			}
//			@Override
//			public void fail() {
//				mBitmap = null;
//			}
//		});
//	}
	
	/**
	 * 获得引导页的图片
	 */
	private void getGuideInfo() {
		new GetGuideInfoTask(this, new GetGuideInfoTask.Callback() {

			@Override
			public void getResult(JSONObject result) {
				if (null != result) {
					Log.d(TAG, "图片:" + result.toString());
					try {
						mImgUrl = result.get("value").toString();
						if (!Util.isEmpty(mImgUrl)) {
							Log.d(TAG, "test ☆☆☆☆☆……☆2_1");
							GuideImgModel.saveGuideImg(new GuideImg(GuideImgModel.ID, mImgUrl));
							Util.showGuideImage(StartActivity.this, mImgUrl, mIvStartBg);
						} else {
							mIvStartBg.setImageResource(R.drawable.startpage);
						}
					} catch (Exception e) {
						Log.e(TAG, "获取欢迎页的图片 error ：  " + e.getMessage());					}
				}
			}
		}).execute(GetGuideInfoTask.ANDR_C);
	}

	/**
	 * 检查并上传崩溃日志
	 */
	public void uploadCrashLog() {
		Log.d(TAG, "检查并上传崩溃日志");
		String crashFileDirPath = Storage.getExtDir() + "/log/";
		File crashFileDir = new File(crashFileDirPath);
		if (crashFileDir != null && crashFileDir.exists()) {
			// 获取目录下的所有文件
			final File[] listFiles = crashFileDir.listFiles();
			if (listFiles.length <= 0) {
				Log.d(TAG, "崩溃日志数量为0");
			} else {
				Log.d(TAG, "崩溃日志数量为" + listFiles.length);
				// 上传崩溃日志
				// 判断产生的日志是否相同
				File file = new File(mLastedCrashLogPath);
				if (file.exists()) { // 不是第一次上传日志文件 需要判断日志文件是否相同
					Log.d(TAG, "比较崩溃日志文件");
					boolean isSame = Util.judgeTwoFile(file, listFiles[listFiles.length - 1]);
					if (isSame) { // 崩溃日志文件相同 不上传
						Log.d(TAG, "崩溃日志文件相同");
						deleteLocalCrashFile();
						return;
					}
				}
				Util.getCrashLogUpload(StartActivity.this, listFiles[listFiles.length - 1].getAbsolutePath(),
						new Util.onUploadFinish() {
							@Override
							public void getImgUrl(String url) {
								// 保存上传的错误日志
								Util.saveFile(mLastedCrashLogPath, listFiles[listFiles.length - 1]);

								Log.d(TAG, "上传日志返回的url" + url);
								Message message = Message.obtain();
								message.what = 0;
								message.obj = url;
								handler.sendMessage(message);
							}
						});
			}
		}
	}

	/**
	 * 添加崩溃日志
	 */
	public void addCrashLog(String url) {
		Log.d(TAG, "添加崩溃日志");
		new AddCrashLogTask(StartActivity.this, new AddCrashLogTask.Callback() {
			@Override
			public void getResult(boolean result) {
				if (result) {
					Log.d(TAG, "上传崩溃日志成功");
					// 删除本地崩溃日志
					deleteLocalCrashFile();
				} else {
					Log.d(TAG, "上传崩溃日志失败");
				}
			}
		}).execute(url);
	}

	/**
	 * 删除本地崩溃日志
	 */
	public void deleteLocalCrashFile() {
		Log.d(TAG, "删除本地崩溃日志");
		String crashFileDirPath = Storage.getExtDir() + "/log/";
		File crashFileDir = new File(crashFileDirPath);
		if (crashFileDir != null && crashFileDir.exists()) {
			File[] listFiles = crashFileDir.listFiles();

			if (listFiles.length <= 0) {
				Log.d(TAG, "崩溃日志数量为0");
			} else {
				// 删除文件
				Log.d(TAG, "开始删除崩溃日志");
				for (int i = 0; i < listFiles.length; i++) {
					listFiles[i].delete();
					Log.d(TAG, "删除崩溃日志" + i);
				}
			}
		}
	}

	@Override
	protected void onResume() {
		JPushInterface.onResume(this);
		super.onResume();
		MobclickAgent.onPageStart(StartActivity.class.getSimpleName()); // 统计页面
	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
		MobclickAgent.onPageEnd(StartActivity.class.getSimpleName()); // 统计页面
	}
}
