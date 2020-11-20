package com.huift.hfq.base.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

import com.huift.hfq.base.Const;
import com.huift.hfq.base.Util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

public class Tools {

	private static final String TAG = Tools.class.getSimpleName();
	
	public static final String APP_ICON = "/ic_launcher.png";
	
	/**
	 *
	 * @param drawable
	 *            drawable
	 * @return bitmap bitmap
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {

		BitmapDrawable bd = (BitmapDrawable) drawable;
		return bd.getBitmap();

	}

	/**
	 * save bitmap to .jpg file
	 * 
	 * @param mBitmap
	 *            bitmap
	 * @param bitName
	 *            file path , must end with .jpg like /sdcard/baomi/tmp/1.jpg
	 */
	public static boolean savBitmapToJpg(Bitmap mBitmap, String bitName) {
		File f = new File(bitName);
		FileOutputStream fOut;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		mBitmap.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	public static Bitmap savBitmapToJpg1(String bitName) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
	       //开始读入图片，此时把options.inJustDecodeBounds 设回true了
	       newOpts.inJustDecodeBounds = true;
	       newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;
	       Bitmap bitmap = BitmapFactory.decodeFile(bitName,newOpts);//此时返回bm为空
	       newOpts.inJustDecodeBounds = false;
	       int w = newOpts.outWidth;
	       int h = newOpts.outHeight;
	       //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
	       float hh = 800f;//这里设置高度为800f
	       float ww = 480f;//这里设置宽度为480f
	       //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
	       int be = 1;//be=1表示不缩放
	       if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
	           be = (int) (newOpts.outWidth / ww);
	       } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
	           be = (int) (newOpts.outHeight / hh);
	       }
	       if (be <= 0)
	           be = 1;
	       newOpts.inSampleSize = be;//设置缩放比例
	       //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
	      return  bitmap = BitmapFactory.decodeFile(bitName, newOpts);
	}
	
	public static Bitmap convertToBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int)scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }
	
	/**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return bitmap
	 */
	public static Bitmap bitmapToRound(Bitmap bitmap) {

		if (bitmap == null) {
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}
		Bitmap output = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888); // use Bitmap.Config.RGB_565 for low
											// memory take
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	/**
	 * copy assets's file to sdcard
	 * 
	 * @param context
	 *            context
	 * @param toPath
	 *            path file copy to
	 * @param fileName
	 *            file name
	 * @return true if success or return false
	 */
	public static boolean copyDataToSD(Context context, String toPath,
			String fileName) {

		try {
			InputStream myInput;
			OutputStream myOutput;
			myOutput = new FileOutputStream(toPath);
			myInput = context.getAssets().open(fileName);
			byte[] buffer = new byte[1024];
			int length = myInput.read(buffer);
			while (length > 0) {
				myOutput.write(buffer, 0, length);
				length = myInput.read(buffer);
			}

			myOutput.flush();
			myInput.close();
			myOutput.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static String getFilePath(Context context) {
		File f = context.getExternalFilesDir(null);
		if (f == null) {
			f = Environment.getExternalStorageDirectory();
			if (f == null) {
				f = context.getFilesDir();
			} else {
				f = new File(f.getAbsolutePath() + "/suanzi/");
				f.mkdirs();
			}
		}
		return f == null ? null : f.getAbsolutePath();
	}
	
	public static void showCouponShare (Activity activity, String url,String describe,String title,String couponCode,String filePath,String logoUrl) {
		title = title + "我分享你一张优惠券，手快有，手慢无";
		url = Const.SHARE_URL + url + couponCode ;
		showShare(activity, url, describe, title,filePath,logoUrl);
	}

	/**
	 * 分享推荐码
	 * @param activity 上下文
	 * @param url 分享的URL
	 * @param describe 分享的描述
	 * @param title 分享的标题
	 * @param couponCode 分享Code
	 */
	public static void showRecommShare (Activity activity, String url,String describe,String title,String recommCode,String filePath,String logoUrl) {
		url = Const.SHARE_URL + url + recommCode ;
		Log.d(TAG, " showRecommShare == "+ url);
		showShare(activity, url, describe, title,filePath,logoUrl);
	}
	
	/**
	 * 分享商家
	 * @param activity
	 * @param url
	 * @param describe
	 * @param title
	 * @param shopCode
	 */
	public static void showShopShare (Activity activity, String url,String describe,String title,String shopCode,String filePath,String logoUrl) {

		url = Const.SHARE_URL + url + shopCode ;
		showShare(activity, url, describe, title,filePath,logoUrl);
	}
	
	/**
	 * 分享游戏
	 * @param activity
	 * @param url
	 * @param describe
	 * @param title
	 * @param shopCode
	 */
	public static void showGrameShare (Activity activity, String url,String describe,String title,String filePath,String logoUrl) {
		
		showShare(activity, url, describe, title,filePath,logoUrl);
	}

	/**
	 * 分享商家活动
	 * @param activity
	 * @param url
	 * @param describe
	 * @param title
	 * @param shopCode
	 */
	public static void showActivityShare (Activity activity, String url,String describe,String title,String activityCode,String filePath,String logoUrl) {
		url = Const.SHARE_URL + url + activityCode ;
		showShare(activity, url, describe, title,filePath,"");
	}

	public static void showShare(Activity activity, String url,String describe,String title,String filePath,String logoUrl) {
		
		Log.e("share", "share url is >>> " + getFilePath(activity) );
		if (activity == null) {
			return;
		}
		String appVesionCode = Util.getAppVersionCode(activity);
		if (url.contains("?")) {
			url = url + "&appver=" + appVesionCode;
		} else {
			url = url + "?appver=" + appVesionCode;
		}
		Log.d(TAG, "share url is >>> " + url);
		if (!Util.isEmpty(filePath)) {
			if (!Tools.copyDataToSD(activity, filePath, "ic_launcher.png")) {
				Toast.makeText(activity, "error", Toast.LENGTH_LONG).show();
				return;
			}
			
			if (!new File(filePath).exists()) {
				Toast.makeText(activity, "error", Toast.LENGTH_LONG).show();
				return;
			}
		}

		ShareSDK.initSDK(activity);
		OnekeyShare oks = new OnekeyShare();
		
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
//		 getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(title);
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl( url);
		// text是分享文本，所有平台都需要这个字段
		oks.setText( describe);
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath(filePath);// 确保SDcard下面存在此张图片
		//test 
		oks.setImageUrl(logoUrl);
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(url);
		// 分享的主题
		oks.setTheme(OnekeyShareTheme.CLASSIC);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
//		oks.setComment(url);
		// site是分享此内容的网站名称，仅在QQ空间使用
//		oks.setSite();
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(url);

		// 启动分享GUI
		oks.show(activity);
	}

}
