package com.huift.hfq.shop.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;

public class MPosApkUtils {

	private static final String PACKAGE_NAME = MPosPluginHelper.componentName;
	private static final String APK_FILE_NAME = "mPos_shouji.apk";

	/** 检测Mpos插件并安装，如果未安装则调用安装 */
	public static boolean checkMPosPlugin(Context context) {
		if (!checkApkExist(context, PACKAGE_NAME)) {
			// install the uppay.apk
			if (retrieveApkFromAssets(context, APK_FILE_NAME, APK_FILE_NAME)) {
				String path = context.getFilesDir().getAbsolutePath();
				// 对内存中的UPPayPlugin.apk和其文件夹写读权限
				appsReadPermissions(path + File.separator, path + File.separator + APK_FILE_NAME);

				installApp(context, path + File.separator + APK_FILE_NAME);
				return false;
			} else {
				
				 AlertDialog dialog = new AlertDialog(context) { };
				 dialog.setTitle("温馨提示");
				 dialog.setMessage("获取支付控件失败，请先安装银联安全支付控件~");
				 
				 /*dialog.setButton(0, "确定", new
				 DialogInterface.OnClickListener() {
				 
				 @Override public void onClick(DialogInterface dialog, int which) { 
					 dialog.dismiss();
					 
				 } }); */
				 
				 dialog.show();
				 
				 dialog.setCancelable(false); return false;
				  

				/*new AlertDialog.Builder(context).setTitle("温馨提示").setMessage("获取支付控件失败，请先安装银联安全支付控件~")
						.setPositiveButton("确定", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();

							}
						}).setNegativeButton("取消", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();

							}
						}).show();

				return false;*/

			}
		} else {
			return true;
		}
	}

	public static boolean checkNeedUpdate(Context context) {
		try {
			context.getPackageManager().getApplicationInfo(PACKAGE_NAME, PackageManager.GET_UNINSTALLED_PACKAGES);
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(PACKAGE_NAME, 0);
			int versionCode = pi.versionCode;
			// check server version code
			return false;
		} catch (NameNotFoundException e) {
			return true;
		}
	}

	public static boolean downloadPlugin(Context context) {
		return true;
	}

	public static boolean checkApkExist(Context context, String packageName) {
		if (packageName == null || "".equals(packageName)) return false;
		try {
			context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	public static boolean retrieveApkFromAssetsSD(Context context, String srcfileName, String desFileName) {
		try {
			String slClientPath = Environment.getExternalStorageDirectory() + "/SLClinet/";
			FileOutputStream fileOutputStream = null;
			BufferedInputStream is = new BufferedInputStream(context.getAssets().open(srcfileName));
			if (is != null) {
				File dir = new File(slClientPath);
				dir.mkdir();
				File file = new File(slClientPath + desFileName);
				fileOutputStream = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				int ch = -1;
				while ((ch = is.read(buf)) != -1) {
					fileOutputStream.write(buf, 0, ch);
				}
			}
			is.close();
			fileOutputStream.flush();
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean retrieveApkFromAssets(Context context, String srcfileName, String desFileName) {
		boolean bRet = false;

		BufferedInputStream bis = null;
		DataOutputStream dos = null;
		try {
			InputStream is = context.getAssets().open(srcfileName);
			bis = new BufferedInputStream(is, 1024);
			dos = new DataOutputStream(context.openFileOutput(desFileName, Context.MODE_PRIVATE));
			byte[] buffer = new byte[1024];
			int length = -1;
			while ((length = bis.read(buffer)) != -1) {
				dos.write(buffer, 0, length);
			}
			dos.flush();
			is.close();
			bRet = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (dos != null) {
				try {
					dos.close();
				} catch (Exception e) {
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (Exception e) {
				}
			}
		}
		return bRet;
	}

	public static void installApp(Context context, String filePath) {
		if (filePath == null) { return; }
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file:" + filePath), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/** 获取SD卡 + baomi路径（不存在返回false） */
	public static String getSDPath() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory() + "/baomi/";
		} else {
			return null;
		}
	}

	/** 对内存中的dirPaht目录和file写 读权限 */
	private static void appsReadPermissions(String dirPath, String filePath) {
		// [文件夹705:drwx---r-x]
		String[] args1 = { "chmod", "705", dirPath };
		exec(args1);
		// [文件604:-rw----r--]
		String[] args2 = { "chmod", "604", filePath };
		exec(args2);
	}

	/** 执行Linux命令，并返回执行结果。 */
	public static String exec(String[] args) {
		String result = "";
		ProcessBuilder processBuilder = new ProcessBuilder(args);
		Process process = null;
		InputStream errIs = null;
		InputStream inIs = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int read = -1;
			process = processBuilder.start();
			errIs = process.getErrorStream();
			while ((read = errIs.read()) != -1) {
				baos.write(read);
			}
			baos.write('\n');
			inIs = process.getInputStream();
			while ((read = inIs.read()) != -1) {
				baos.write(read);
			}
			byte[] data = baos.toByteArray();
			result = new String(data);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (errIs != null) {
					errIs.close();
				}
				if (inIs != null) {
					inIs.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (process != null) {
				process.destroy();
			}
		}
		return result;
	}
}
