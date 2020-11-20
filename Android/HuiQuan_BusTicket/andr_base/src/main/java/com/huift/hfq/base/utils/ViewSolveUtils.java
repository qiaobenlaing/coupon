package com.huift.hfq.base.utils;

import com.huift.hfq.base.Util;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * 控件的处理
 * @author liyanfang
 */
public class ViewSolveUtils {
	
	private static final String TAG = ViewSolveUtils.class.getSimpleName();
	private static final int HAVE_DATE = 1;
	private static final int NO_DATE = 0;
	private static final int LOADING = 2;
	private static final int page = 1;

	/**
	 * 没有数据的加载
	 * 
	 * @param listView
	 *            装载数据集合的视图
	 * @param lyView
	 *            没有数据加载的整个视图
	 * @param ivView
	 *            图片的没有数据加载的图片
	 * @param progView
	 *            没有数据的加载进度
	 * @param hasData
	 *            0 是没有数据 1 是有数据 2 正在加载
	 */
	public static void setNoData(View listView, View lyView, View ivView, View progView, int hasData) {
		if (hasData == HAVE_DATE) {
			listView.setVisibility(View.VISIBLE);
			lyView.setVisibility(View.GONE);
		} else if (hasData == LOADING) {
			listView.setVisibility(View.GONE);
			lyView.setVisibility(View.VISIBLE);
			ivView.setVisibility(View.GONE);
			progView.setVisibility(View.VISIBLE);
		} else {
			listView.setVisibility(View.GONE);
			lyView.setVisibility(View.VISIBLE);
			ivView.setVisibility(View.VISIBLE);
			progView.setVisibility(View.GONE);
		}
	}

	/**
	 * 判断数据是否大于1
	 * 
	 * @param listView
	 *            装载数据集合的视图
	 * @param lyView
	 *            没有数据加载的整个视图
	 * @param progView
	 *            没有数据的加载进度
	 * @param ivView
	 *            图片的没有数据加载的图片
	 * @param hasData
	 *            0 是没有数据 1 是有数据 2 正在加载
	 */
	public static void morePageOne(View listView, View lyView, View ivView, View progView, int page) {
		if (page > 1) {
			setNoData(listView, lyView, ivView, progView, HAVE_DATE);
		} else {
			setNoData(listView, lyView, ivView, progView, NO_DATE);
		}
	}

	/**
	 * 得到EditText的值
	 * 
	 * @param editText
	 *            EditText 控件
	 * @return EditText控件的值
	 */
	public static String getValue(EditText editText) {
		return editText.getText().toString();
	}

	/**
	 * 得带TextView的值
	 * 
	 * @param textView
	 *            TextView控件
	 * @return TextView控件的值
	 */
	public static String getValue(TextView textView) {
		return textView.getText().toString();
	}

	/**
	 * 设置progress的位置
	 * 
	 * @param activity
	 * @param view
	 *            显示的视图
	 */
	public static void setProgLocation(Activity activity, View view) {
		WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		int mScreenWidth = outMetrics.widthPixels;
		int mScreenHeight = outMetrics.heightPixels;

		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(144, 144);
		params.topMargin = (int) Math.floor(Calculate.suBtraction(Calculate.div(mScreenHeight, 2), 130));
		params.leftMargin = (int) Math.floor(Calculate.suBtraction(Calculate.div(mScreenWidth, 2), 130));
		view.setLayoutParams(params);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	/**
	 * 输入金额不为空判断 然后转换为double类型的数字
	 * @param editText  输入的金额
	 * @return 返回double的数据
	 */
	public static double getInputMoney(EditText editText) {
		String edtPrice = "";
		if (TextUtils.isEmpty(editText.getText())) {
			edtPrice = "0";
		} else {
			edtPrice = editText.getText().toString();
		}
		double inputprice = 0;
		try {
			if (edtPrice.lastIndexOf(".") == 0) {
				inputprice = 0;
			} else {
				inputprice = Double.parseDouble(edtPrice);
			}
		} catch (Exception e) {
			Log.e(TAG, "输入金额不为空判断  error >> " + e.getMessage());
		}
		return inputprice;
	}
	
	/**
	 * 输入金额不为空判断 然后转换为double类型的数字
	 * @param textView  输入的金额
	 * @return 返回double的数据
	 */
	public static double getInputMoney(TextView textView) {
		String edtPrice = "";
		if (TextUtils.isEmpty(textView.getText())) {
			edtPrice = "0";
		} else {
			edtPrice = textView.getText().toString();
		}
		double inputprice = 0;
		try {
			if (edtPrice.lastIndexOf(".") == 0) {
				inputprice = 0;
			} else {
				inputprice = Double.parseDouble(edtPrice);
			}
		} catch (Exception e) {
			Log.e(TAG, "输入金额不为空判断  error >> " + e.getMessage());
		}
		return inputprice;
	}
	
	/**
	 * 输入金额不为空判断 然后转换为double类型的数字
	 * @param textView  输入的金额
	 * @return 返回double的数据
	 */
	public static double getInputMoney(String textView) {
		double inputprice = 0;
		textView = Util.isEmpty(textView) ? "0" : textView;
		try {
			if (textView.lastIndexOf(".") == 0) {
				inputprice = 0;
			} else {
				inputprice = Double.parseDouble(textView);
			}
		} catch (Exception e) {
			Log.e(TAG, "输入金额不为空判断  error >> " + e.getMessage());
		}
		return inputprice;
	}
	
	/**
	 * 输入金额不为空判断 然后转换为double类型的数字
	 * @param textView  输入的金额
	 * @return 返回double的数据
	 */
	public static int getInputNum(String textView) {
		int inputprice = 0;
		textView = Util.isEmpty(textView) ? "0" : textView;
		try {
			if (textView.lastIndexOf(".") == 0) {
				inputprice = 0;
			} else {
				inputprice = Integer.parseInt(textView);
			}
		} catch (Exception e) {
			Log.e(TAG, "输入金额不为空判断  error >> " + e.getMessage());
		}
		return inputprice;
	}
	
	/**
	 * 判断输入的数是否是空的
	 * @param editText  输入的金额
	 * @return 返回int类型的数据
	 */
	public static int getInputNum(EditText editText) {
		String edtPrice = "";
		if (TextUtils.isEmpty(editText.getText())) {
			edtPrice = "0";
		} else {
			edtPrice = editText.getText().toString();
		}
		int inputprice = 0;
		try {
			if (edtPrice.lastIndexOf(".") == 0) {
				inputprice = 0;
			} else {
				inputprice = Integer.parseInt(edtPrice);
			}
		} catch (Exception e) {
			Log.e(TAG, "输入金额不为空判断  error >> " + e.getMessage());
		}
		return inputprice;
	}
	
}
