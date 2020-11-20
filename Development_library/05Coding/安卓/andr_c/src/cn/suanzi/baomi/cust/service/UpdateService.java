package cn.suanzi.baomi.cust.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.AppUpdate;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.DialogUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.activity.HomeActivity;
import cn.suanzi.baomi.cust.application.CustConst;

public class UpdateService extends Service {

	private static final String TAG = "UpdateService";
	private static final int TIMEOUT = 5 * 1000; // 超时
	private static final int DOWN_OK = 1;
	private static final int DOWN_ERROR = 0;
	private static final int UPP_APP = 1;
	private String mAppName, mUrls;
	private static Context sContext;

	private NotificationManager mNotificationManager; // 通知管理器
	private Notification mNotification; // 通知

	/** 通知更新 */
	private Intent mUpdateIntent;
	private PendingIntent mPendingIntent;

	private int mNnotificationId = 1;

	private RemoteViews mContentView;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			mAppName = intent.getStringExtra("appName");
			mUrls = intent.getStringExtra("url");
			createFile(mAppName);
			createNotification();
			startUpdate();
		} catch (Exception e) {
			Log.e(TAG, "onStartCommand  error:" + e.getMessage());
		}

		return super.onStartCommand(intent, flags, startId);
	}

	private boolean createFile(String director) {
		File file = getFile(director);
		if (file.exists()) {
			return true;
		} else {
			if (!file.mkdirs()) { return false; }
			return true;
		}
	}

	private File getFile(String director) {
		// File file = new File(Environment.getExternalStorageDirectory()
		// + File.separator + director);

		File file = new File(Environment.getExternalStorageDirectory(), director);

		// File extDir = Environment.getExternalStorageDirectory();
		// String filename = "downloadedMusic.mp3";
		// File fullFilename = new File(extDir, filename);
		return file;
	}

	// 获取文件的保存路径
	public File getFile() throws Exception {
		String SavePath = getSDCardPath() + "/App";
		File path = new File(SavePath);
		File file = new File(SavePath + "/huiquan.apk");
		if (!path.exists()) {
			path.mkdirs();
		}
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}

	// 获取SDCard的目录路径功能
	private String getSDCardPath() {
		File sdcardDir = null;
		// 判断SDCard是否存在
		boolean sdcardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if (sdcardExist) {
			sdcardDir = Environment.getExternalStorageDirectory();
		}
		return sdcardDir.toString();
	}

	/***
	 * 创建通知栏
	 */
	public void createNotification() {
		/*
		 * 自定义Notification视图
		 */
		mContentView = new RemoteViews(getPackageName(), R.layout.notification_item);
		mContentView.setTextViewText(R.id.notificationTitle, mAppName + "—正在下载");
		mContentView.setTextViewText(R.id.notificationPercent, "0%");
		mContentView.setProgressBar(R.id.notificationProgress, 100, 0, false);

		mUpdateIntent = new Intent(this, HomeActivity.class);
		mUpdateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		mPendingIntent = PendingIntent.getActivity(this, 0, mUpdateIntent, 0);

		// 初始化通知
		mNotification = new Notification(R.drawable.ic_launcher, "开始下载 " + mAppName, System.currentTimeMillis());
		mNotification.flags |= Notification.FLAG_NO_CLEAR;
		mNotification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE; // 设置通知铃声和振动提醒
		mNotification.contentView = mContentView;
		mNotification.contentIntent = mPendingIntent;
		// 发送通知
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(mNnotificationId, mNotification);
		// 清除通知铃声
		mNotification.defaults = 0;
	}

	/***
	 * 开启线程下载更新
	 */
	public void startUpdate() {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// 添加通知声音
				mNotification.defaults |= Notification.DEFAULT_SOUND;
				switch (msg.what) {
				case DOWN_OK:
					Log.d(TAG, "自动更新 >>>>> " +DOWN_OK);
					installApk();
					break;
				case DOWN_ERROR:
					mNotification.tickerText = mAppName + " 下载失败";
					mNotification.setLatestEventInfo(UpdateService.this, mAppName, " 下载失败", mPendingIntent);
				}
				mNotification.flags = Notification.FLAG_AUTO_CANCEL;
				mNotificationManager.notify(mNnotificationId, mNotification);
				stopService(mUpdateIntent);
				stopSelf();
			}
		};
		// 启动线程下载更新
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					downloadUpdateFile(mUrls, getFile().toString(), handler);
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendMessage(handler.obtainMessage(DOWN_ERROR));
				}
			}
		}).start();
	}

	/**
	 * 点击安装
	 */
	public void installApk() {
		Log.d(TAG, "下载完成==");
		// 下载完成，点击安装
		Uri uri = null;
		try {
			uri = Uri.fromFile(getFile());
		} catch (Exception e) {
			Log.e(TAG, "手动点击安装更新 >>> 安装失败:" + e.getMessage());
		}
		// 安装应用意图  
		Intent intent = new Intent(Intent.ACTION_VIEW);  
		intent.setDataAndType(uri, "application/vnd.android.package-archive");  
		mPendingIntent = PendingIntent.getActivity(UpdateService.this, 0, intent, 0);
		mNotification.tickerText = mAppName + " 下载完成";
		mNotification.setLatestEventInfo(UpdateService.this, mAppName, "下载完成", mPendingIntent);
		
	}

	/**
	 * 自动安装
	 */
	public void autoInstallApk() {
		if (null != sContext) {
			Log.d(TAG, "下载完成==");
			// 下载完成，点击安装
			Uri uri = null;
			try {
				uri = Uri.fromFile(getFile());
			} catch (Exception e) {
				Log.e(TAG, "自动更新 >>> 安装失败:" + e.getMessage());
			}
			Log.d(TAG, "自动更新 uri = " +uri);
			// 安装应用意图
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(uri, "application/vnd.android.package-archive");
			sContext.startActivity(intent);
		}
	}

	/***
	 * 下载文件
	 * 
	 * @return
	 * @throws MalformedURLException
	 */
	public void downloadUpdateFile(String down_url, String file, Handler handler) throws Exception {
		int totalSize; // 文件总大小
		InputStream inputStream;
		FileOutputStream outputStream; 
		URL url = new URL(down_url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setConnectTimeout(TIMEOUT);
		httpURLConnection.setReadTimeout(TIMEOUT);
		// 获取下载文件的size 
		totalSize = httpURLConnection.getContentLength();
		Log.d(TAG, "totalSize >>> " +totalSize);
		if (httpURLConnection.getResponseCode() == 404) { throw new Exception("fail!"); }
		inputStream = httpURLConnection.getInputStream();
		outputStream = new FileOutputStream(file, false);

		// 异步任务开始下载
		new UpdateAsyncTask(inputStream, outputStream, handler).execute(totalSize);
	}

	private class UpdateAsyncTask extends AsyncTask<Integer, Integer, Boolean> {

		private InputStream in;

		private FileOutputStream fos;

		private Handler handler;

		public UpdateAsyncTask(InputStream inputStream, FileOutputStream outputStream, Handler handler) {
			super();
			in = inputStream;
			fos = outputStream;
			this.handler = handler;
		}

		@Override
		protected Boolean doInBackground(Integer... params) {
			int totalSize = params[0]; // 下载总大小
			int downloadCount = 0; // 已下载大小
			int updateProgress = 0; // 更新进度
			int updateStep = 5; // 更新进度步进

			byte buffer[] = new byte[1024];
			int readsize = 0;
			try {
				while ((readsize = in.read(buffer)) != -1) {
					fos.write(buffer, 0, readsize);
					// 计算已下载到的大小
					downloadCount += readsize;
					// 先计算已下载的百分比，然后跟上次比较是否有增加，有则更新通知进度
					int now = downloadCount * 100 / totalSize;
					if (updateProgress < now) {
						updateProgress = now;
						Log.d(TAG, "update: " + updateProgress + "%");
						publishProgress(updateProgress);
					}
				}
			} catch (Exception e) {
				Log.e(TAG, "download err===>\n " + e.getMessage());
				return false;
			} finally {
				try {
					fos.close();
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return true;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			int progress = values[0];
			// 改变通知栏
			mContentView.setTextViewText(R.id.notificationPercent, progress + "%");
			mContentView.setProgressBar(R.id.notificationProgress, 100, progress, false);
			// show_view
			mNotificationManager.notify(mNnotificationId, mNotification);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				autoInstallApk();
				handler.sendMessage(handler.obtainMessage(DOWN_OK)); // 通知handler已经下载完成
			} else {
				handler.sendMessage(handler.obtainMessage(DOWN_ERROR)); // 通知handler下载出错
			}
			super.onPostExecute(result);
		}

	}

	/**
	 * 检查版本信息
	 */
	public static void show(final Context context, final String url, int uppType) {
		if (null != AppUtils.getActivity()) {
			Log.d(TAG, "showUrl >>> " +url);
			/*
			 * View v=
			 * LayoutInflater.from(context).inflate(R.layout.version_update_line
			 * , null); TextView textView=(TextView)
			 * v.findViewById(R.id.version_upp_text); //TextView textView=new
			 * TextView(context); textView.setText(update.description);
			 */
			sContext = context;
			if (uppType == UPP_APP) { // 必须更新
				DialogUtils.showDialogSingle(AppUtils.getActivity(), getStrings(R.string.soft_needupdate_info),
						R.string.soft_update_title, R.string.soft_update_updatebtn,
						new DialogUtils().new OnResultListener() {

							@Override
							public void onOK() {
								AppUpdate mAppUpdate = DB.getObj(CustConst.Key.APP_UPP, AppUpdate.class); // 保存跟新的对象
								startUpdateService(AppUtils.getContext(), url);
								mAppUpdate.setCanUpdate(0);
								DB.saveObj(CustConst.Key.APP_UPP, mAppUpdate); // 保存跟新的对象
							}
						});

			} else {
				DialogUtils.showDialog(AppUtils.getActivity(), getStrings(R.string.soft_update_title),
						getStrings(R.string.soft_update_info), getStrings(R.string.soft_update_updatebtn),
						getStrings(R.string.soft_update_later), new DialogUtils().new OnResultListener() {

							@Override
							public void onOK() {
								AppUpdate mAppUpdate = DB.getObj(CustConst.Key.APP_UPP, AppUpdate.class); // 保存跟新的对象
								startUpdateService(AppUtils.getContext(), url);
								mAppUpdate.setCanUpdate(0);
								DB.saveObj(CustConst.Key.APP_UPP, mAppUpdate); // 保存跟新的对象
							}

							@Override
							public void onCancel() {
								SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(sContext);
								Editor editor = sharedPreferences.edit();
								editor.putBoolean(DB.Key.CUST_CANCEL_UPDATE, false);
								editor.commit();
							}
						});
			}

		}
	}

	/**
	 * 启动升级服务下载升级
	 */
	public static void startUpdateService(Context context, String url) {
		Intent intent  = null;
		if (null != context) {
			intent = new Intent(context, UpdateService.class);
			intent.putExtra("appName", Util.getString(R.string.app_name));
		} else {
			intent = new Intent(AppUtils.getContext(), UpdateService.class);
			intent.putExtra("appName", Util.getString(R.string.app_name));
		}
		intent.putExtra("url", url);
		context.startService(intent);
	}

	private static String getStrings(int id) {
		return sContext.getResources().getString(id);
	}
}
