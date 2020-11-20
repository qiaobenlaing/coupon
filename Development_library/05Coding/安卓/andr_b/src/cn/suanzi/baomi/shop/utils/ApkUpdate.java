package cn.suanzi.baomi.shop.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.shop.R;

public class ApkUpdate {
	private final static String user_agent = null;
	private final static String accept_type ="*/*";
	public final static String APKNAME = "yks.apk";
	/** 下载中 */
    private static final int DOWNLOAD = 1;
    /** 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /** 下载出错*/
    private static final int DOWNLOAD_ERROR = 0;
    private static final int time_out = 5000;
    /** 下载保存路径 */
    private String mSavePath;
    /** 记录进度条数量 */
    private int progress;
    /** 是否取消更新 */
    private boolean cancelUpdate = false;
    
    private ProgressDialog pBar;

    private Activity mActivity;
    /** 更新进度条 */
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;
    
    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
            // 正在下载
            case DOWNLOAD:
                // 设置进度条位置
            	Log.d("TAG", "progress="+progress);
                mProgress.setProgress(progress);
                break;
            case DOWNLOAD_FINISH:
                // 安装文件
                installApk();
                break;
            case DOWNLOAD_ERROR:
            	// 安装文件
            	installApk();
            	break;
            default:
                break;
            }
        };
    };
    
    /**
     * 构造函数
     * @param context
     */
    public ApkUpdate(Activity activity)
    {
        this.mActivity = activity;
    }
    
    /**
     * 检测软件更新
     */
    public void checkUpdate()
    {
    	// 更新
        if (isUpdate()) 
        {
            // 显示提示对话框
            showNoticeDialog();
        } else
        {
        	// 不用更新
           return ;
        }
    }
    
    /**
     * 判断是否要更新
     * @return
     */
    public boolean isUpdate() {
    	boolean flag = false ;
    	 String vesion = Util.getAppVersionCode(mActivity);
         String vesionStr = vesion.replace(".", "");
         int oldVersionCode = 0;
         int newVersionCode = 101;
         if (Util.isEmpty(vesionStr)) {
        	  return false;
         } else {
         	try {
         		oldVersionCode= Integer.parseInt(vesionStr);
 			} catch (Exception e) {
 				return false;
 			}
         }
         // 服务器上新版本比现在app的版本高的话就提示升级
         if (oldVersionCode < newVersionCode) {
         	 flag = true;
         }
    	return flag;
    }
    
    /**
     * 显示软件更新对话框
     */
    public void showNoticeDialog()
    {
        // 构造对话框
        AlertDialog.Builder builder = new Builder(mActivity);
        builder.setTitle(R.string.soft_update_title);
        builder.setMessage(R.string.soft_update_info);
        // 更新
        builder.setPositiveButton(R.string.soft_update_updatebtn, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                // 显示下载对话框
                showDownloadDialog();
            }
        });
        // 稍后更新
        builder.setNegativeButton(R.string.soft_update_later, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.setCancelable(false);
        noticeDialog.setCanceledOnTouchOutside(false);
        noticeDialog.show();
        noticeDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
		     @Override
		     public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		    	 if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_BACK) {
		    		 dialog.dismiss();
		         }
		         return false;
		     }
		 });
    }
    
    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog()
    {
        // 构造软件下载对话框
        AlertDialog.Builder builder = new Builder(mActivity);
        builder.setTitle(R.string.soft_updating);
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mActivity);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        builder.setView(v);
        // 取消更新
        builder.setNegativeButton(R.string.soft_update_cancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                // 设置取消状态
                cancelUpdate = true;
            }
        });
        mDownloadDialog = builder.create();
        mDownloadDialog.setCanceledOnTouchOutside(false);
        mDownloadDialog.show();
        // 现在文件
        downloadApk();
    }
    /**
     * 下载apk文件
     */
    private void downloadApk()
    {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }
    
    /**
     * 下载文件线程
     */
    private class downloadApkThread extends Thread
    {
        @Override
        public void run()
        {
            try
            {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    mSavePath = sdpath + "download";
                    URL url = new URL("http://softfile.3g.qq.com:8080/msoft/179/24659/43549/qq_hd_mini_1.4.apk");//TODO
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("User-Agent", user_agent);
					conn.setUseCaches(false);
					conn.setConnectTimeout(time_out);
					conn.setRequestProperty("Accept", accept_type);  
					conn.setAllowUserInteraction(true);
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists())
                    {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, "huiquan.apk");//TODO
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do
                    {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0)
                        {
                            // 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (IOException e)
            {
            	// 下载完成
                mHandler.sendEmptyMessage(DOWNLOAD_ERROR );
                e.printStackTrace();
            }
            // 取消下载对话框显示
            mDownloadDialog.dismiss();
        }
    };
    
    /**
     * 安装APK文件
     */
    private void installApk()
    {
        File apkfile = new File(mSavePath, "huiquan.apk");// TODO
        if (!apkfile.exists())
        {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mActivity.startActivity(i);
    }

}
