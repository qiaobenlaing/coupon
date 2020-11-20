package com.huift.hfq.shop.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.data.Storage;
import com.huift.hfq.base.pojo.AppUpdate;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopApplication;
import com.huift.hfq.shop.ShopConst;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class UpdateService extends Service {

    private static final int TIMEOUT = 10 * 1000; // 超时
    private static final int DOWN_OK = 1;//下载完成标签
    private static final int DOWN_ERROR = 0;//下载失败标签
    private String appName, appUrl, appVersion;

    private static NotificationManager notificationManager; // 通知管理器
    private static final int NOTIFICATION_ID = 1;
    private Notification notification; // 通知
    private RemoteViews contentView;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_OK:
                    contentView.setTextViewText(R.id.notificationTitle, appName);
                    contentView.setTextViewText(R.id.notificationPercent, "文件下载完成，点击安装");
                    contentView.setViewVisibility(R.id.notificationProgress, View.GONE);
                    notification.tickerText = appName + " 下载完成";
                    if (getApkUri() != null) {
                        //安装应用意图
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//android7.0之后要加此权限
                        intent.setDataAndType(getApkUri(), "application/vnd.android.package-archive");
                        PendingIntent pendingIntent = PendingIntent.getActivity(UpdateService.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                        notification.contentIntent = pendingIntent;
                    }
                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                    notification.defaults = Notification.DEFAULT_SOUND;// 添加通知声音
                    notificationManager.notify(NOTIFICATION_ID, notification);
                    installApk();// 下载完成，自动安装
                    break;
                case DOWN_ERROR:
                    contentView.setTextViewText(R.id.notificationTitle, appName);
                    contentView.setTextViewText(R.id.notificationPercent, "文件下载失败，请重新下载");
                    contentView.setViewVisibility(R.id.notificationProgress, View.GONE);
                    notification.tickerText = appName + " 下载失败";
                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                    notification.defaults = Notification.DEFAULT_SOUND;// 添加通知声音
                    notificationManager.notify(NOTIFICATION_ID, notification);
                    break;
            }
            ShopApplication.isApkDownloading = false;
            stopSelf();
        }
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        appName = intent.getStringExtra("appName");
        appUrl = intent.getStringExtra("appUrl");
        appVersion = intent.getStringExtra("appVersion");
        if (isApkDownload()) {//已下载，直接安装
            installApk();
        } else {//未下载，开始下载
            ShopApplication.isApkDownloading = true;
            createNotification();
            startUpdate();
            Util.showToastZH("开始下载");
        }
        return START_NOT_STICKY;//服务被销毁后不重新创建
    }

    /**
     * 更新包是否已经下载
     *
     * @return
     */
    private boolean isApkDownload() {
        File apk = new File(getApkDirPath() + "/huiquan.apk");
        if (apk.exists()) {
            //获取安装包的信息
            PackageManager pm = getPackageManager();
            PackageInfo pkgInfo = pm.getPackageArchiveInfo(apk.getPath(), PackageManager.GET_ACTIVITIES);
            if (pkgInfo != null) {
                ApplicationInfo appInfo = pkgInfo.applicationInfo;
                String appName = pm.getApplicationLabel(appInfo).toString();// 得到应用名
                String packageName = appInfo.packageName; // 得到包名
                String version = pkgInfo.versionName; // 得到版本信息
                return packageName.equals(getPackageName()) && version.equals(appVersion);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 创建通知栏
     */
    public void createNotification() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//android 8.0 通知栏适配
            String channelId = "update"; //根据业务执行
            String channelName = "更新进度通知"; //这个是channelid 的解释，在安装的时候会展示给用户看
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(channel);
            // 初始化通知,android 8.0以上需要配置channel
            notification = new Notification.Builder(this, channelId).build();
        } else {
            notification = new Notification.Builder(this).getNotification();
        }
        //自定义Notification视图
        contentView = new RemoteViews(getPackageName(), R.layout.notification_item);
        contentView.setTextViewText(R.id.notificationTitle, appName + "—正在下载");
        contentView.setTextViewText(R.id.notificationPercent, "0%");
        contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);

        notification.icon = R.drawable.ic_launcher;
        notification.tickerText = "开始下载 " + appName;//通知栏滚动内容，安卓5.0版本以下有效
        notification.contentView = contentView;//设置contentView后，contentTitle及contentText则不会显示
        notification.flags = Notification.FLAG_NO_CLEAR;
        // 发送通知
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    /**
     * 开启线程下载更新
     */
    public void startUpdate() {
        // 启动线程下载更新
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    downloadUpdateFile(appUrl, getApkFile().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendMessage(handler.obtainMessage(DOWN_ERROR));
                }
            }
        }).start();
    }

    /**
     * 下载文件
     *
     * @return
     * @throws MalformedURLException
     */
    public void downloadUpdateFile(String down_url, @NonNull String filePath) throws Exception {
        URL url = new URL(down_url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setConnectTimeout(TIMEOUT);
        httpURLConnection.setReadTimeout(TIMEOUT);
        // 获取下载文件的size
        int totalSize = httpURLConnection.getContentLength();
        if (httpURLConnection.getResponseCode() == 404) {
            throw new Exception("fail!");
        }
        InputStream inputStream = httpURLConnection.getInputStream();
        FileOutputStream outputStream = new FileOutputStream(filePath, false);

        // 异步任务开始下载
        new UpdateAsyncTask(inputStream, outputStream).execute(totalSize);
    }

    /**
     * 开启线程下载apk
     *
     * @author ad
     */
    private class UpdateAsyncTask extends AsyncTask<Integer, Integer, Boolean> {

        private final InputStream in;

        private final FileOutputStream fos;

        public UpdateAsyncTask(InputStream inputStream, FileOutputStream outputStream) {
            super();
            in = inputStream;
            fos = outputStream;
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            int totalSize = params[0]; // 文件大小
            int downloadCount = 0; // 已下载大小
            int lastProgress = 0; // 上一次更新进度
            int currentProgress;//当前更新进度
            long lastTime = new Date().getTime();//上一次时间
            long currentTime;//当前时间

            byte[] buffer = new byte[1024];
            int readSize = 0;
            try {
                while ((readSize = in.read(buffer)) != -1) {
                    fos.write(buffer, 0, readSize);
                    // 计算已下载到的大小
                    downloadCount += readSize;
                    // 先计算已下载的百分比，然后跟上次比较是否有增加，同时比较时间间隔超过1s，有则更新通知进度
                    currentTime = new Date().getTime();
                    currentProgress = downloadCount * 100 / totalSize;
                    if (currentProgress > lastProgress && currentTime - lastTime > 1000) {
                        publishProgress(currentProgress);
                        lastProgress = currentProgress;
                        lastTime = currentTime;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
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
            // 刷新通知栏
            contentView.setTextViewText(R.id.notificationPercent, progress + "%");
            contentView.setProgressBar(R.id.notificationProgress, 100, progress, false);
            // show_view
            notificationManager.notify(NOTIFICATION_ID, notification);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                handler.sendMessage(handler.obtainMessage(DOWN_OK)); // 通知handler已经下载完成
            } else {
                handler.sendMessage(handler.obtainMessage(DOWN_ERROR)); // 通知handler下载出错
            }
        }
    }

    /**
     * 安装apk
     */
    private void installApk() {
        if (getApkUri() != null) {
            Intent installIntent = new Intent(Intent.ACTION_VIEW);
            installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//android7.0之后要加此权限
            installIntent.setDataAndType(getApkUri(), "application/vnd.android.package-archive");
            startActivity(installIntent);
        }
    }

    /**
     * 获取apk的Uri
     *
     * @return
     */
    private Uri getApkUri() {
        if (getApkFile() != null) {
            Uri apkUri = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                apkUri = FileProvider.getUriForFile(this, "com.huift.hfq.shop.FileProvider", getApkFile());
            } else {
                apkUri = Uri.fromFile(getApkFile());
            }
            return apkUri;
        } else {
            return null;
        }
    }

    /**
     * 获取更新apk文件
     *
     * @return
     */
    private File getApkFile() {
        File dir = new File(getApkDirPath());
        File file = new File(getApkDirPath() + "/huiquan.apk");
        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            return null;
        }
        return file;
    }

    // 获取apk的目录路径
    private String getApkDirPath() {
        return Storage.getExtDir(this) + "/download";
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        //通过任务管理器结束进程会调用此方法
        super.onTaskRemoved(rootIntent);
        Log.e("log_all", "onTaskRemoved");
        clearNotification();
        stopSelf();
    }

    /**
     * 清除通知
     */
    public static void clearNotification() {
        if (notificationManager != null) {
            notificationManager.cancel(NOTIFICATION_ID);
        }
    }

    /**
     * 检查版本信息
     */
    public static void show(final Context context) {
        final AppUpdate mAppUpdate = DB.getObj(ShopConst.Key.APP_UPP, AppUpdate.class);
        if (mAppUpdate == null) {
            return;
        }

        final Intent intent = new Intent(context, UpdateService.class);
        intent.putExtra("appName", Util.getString(R.string.app_name));
        intent.putExtra("appUrl", mAppUpdate.getUpdateUrl());
        intent.putExtra("appVersion", mAppUpdate.getVersionCode());

        if ("1".equals(mAppUpdate.getIsMustUpdate())) {//强制更新
            showUpdateDialog(context, "新版本:" + mAppUpdate.getVersionCode() + "\n" + mAppUpdate.getUpdateContent(), new DialogUtils().new OnResultListener() {

                @Override
                public void onOK() {
                    context.startService(intent);
                }
            });
        } else {//非强制
            DialogUtils.showDialog(context, Util.getString(R.string.soft_update_title),
                    "新版本:" + mAppUpdate.getVersionCode() + "\n" + mAppUpdate.getUpdateContent(), Util.getString(R.string.soft_update_updatebtn),
                    Util.getString(R.string.soft_update_later), new DialogUtils().new OnResultListener() {

                        @Override
                        public void onOK() {
                            context.startService(intent);
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
        }
    }

    /**
     * 强制更新弹窗
     *
     * @param onResultListener 确定按钮的事件
     * @return
     */
    public static void showUpdateDialog(Context context, String content, final DialogUtils.OnResultListener onResultListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.popupw_dialog_single, null);
        final PopupWindow mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mPopupWindow.setFocusable(false);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //设置的标题
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialgo_title);
        tvTitle.setText(R.string.soft_update_title);
        // 内容
        TextView tvContent = (TextView) view.findViewById(R.id.tv_dialog_content);
        tvContent.setText(content);
        // 确定按钮
        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        btnOk.setText(R.string.soft_update_updatebtn);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onResultListener) {
                    onResultListener.onOK();
                }
                mPopupWindow.dismiss();
            }
        });
    }
}
