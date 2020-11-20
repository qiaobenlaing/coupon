package com.huift.hfq.base.utils; 

import android.content.Context;
import android.util.Log;
import android.view.WindowManager;

/**
 * dp、sp 转换为 px 的工具类
 * @author yanfang.li
 */
public class SizeUtil {
	private static final String TAG = SizeUtil.class.getSimpleName();
	
	@SuppressWarnings("deprecation")
	public static int getScreenWidth() {
		WindowManager wm = (WindowManager) AppUtils.getContext().getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();
	}

	@SuppressWarnings("deprecation")
	public static int getScreenHeight() {
		WindowManager wm = (WindowManager) AppUtils.getContext().getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getHeight();
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(float dpValue) {
		final float scale = AppUtils.getContext().getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(float pxValue) {
		final float scale = AppUtils.getContext().getResources().getDisplayMetrics().density;
		Log.d(TAG, "showViewImgWidthscale =" + scale + ">>> dpValue = " +pxValue);
		return (int) (pxValue / scale + 0.5f);
	}
	
    /**
     * 将px值转换为sp值，保证文字大小不变
     * 
     * @param pxValue
     * @param fontScale
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */ 
    public static int px2sp(float pxValue) { 
        final float fontScale = AppUtils.getContext().getResources().getDisplayMetrics().scaledDensity; 
        return (int) (pxValue / fontScale + 0.5f); 
    } 
   
    /**
     * 将sp值转换为px值，保证文字大小不变
     * 
     * @param spValue
     * @param fontScale
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */ 
    public static int sp2px( float spValue) { 
        final float fontScale = AppUtils.getContext().getResources().getDisplayMetrics().scaledDensity; 
        return (int) (spValue * fontScale + 0.5f); 
    }
}
