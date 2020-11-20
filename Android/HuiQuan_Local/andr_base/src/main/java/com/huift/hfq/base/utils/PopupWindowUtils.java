package com.huift.hfq.base.utils;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.huift.hfq.base.R;

public class PopupWindowUtils {
	
	/**
	 * 回调方法的接口
	 */
	public interface OnResultListener {
		public void onOK();

		public void onCancel();
	}
	
	/**
	 * 对话框样式的重新设计
	 */
	public static void showDialog(Activity activity, String title, String text, String btnok, String btncancel, final OnResultListener onResultListener){
		View view = LayoutInflater.from(activity).inflate(R.layout.popupw_dialog, null);
		final PopupWindow mPopupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(activity.getResources().getDrawable(android.R.color.transparent));
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.update();
		mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
		
		//设置
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialgo_title);//标题
		tvTitle.setText(title);
		TextView tvContent = (TextView) view.findViewById(R.id.tv_dialog_content);//内容
		tvContent.setText(text);
		
		Button btnOk = (Button) view.findViewById(R.id.btn_ok);//确定
		btnOk.setText(btnok);
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onResultListener.onOK();
				mPopupWindow.dismiss();
			}
		});
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);//取消
		btnCancel.setText(btncancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onResultListener.onCancel();
				mPopupWindow.dismiss();
			}
		});
	}
	
	/**
	 * 对话框样式的重新设计（登录）
	 */
	public static PopupWindow showLoginDialog(Activity activity, String title, String text, String btnok, String btncancel, final OnResultListener onResultListener){
		View view = LayoutInflater.from(activity).inflate(R.layout.popupw_dialog, null);
		final PopupWindow mPopupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(null);
		mPopupWindow.update();
		mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
		
		//设置
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialgo_title);//标题
		tvTitle.setText(title);
		TextView tvContent = (TextView) view.findViewById(R.id.tv_dialog_content);//内容
		tvContent.setText(text);
		
		Button btnOk = (Button) view.findViewById(R.id.btn_ok);//确定
		btnOk.setText(btnok);
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onResultListener.onOK();
				mPopupWindow.dismiss();
			}
		});
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);//取消
		btnCancel.setText(btncancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onResultListener.onCancel();
				mPopupWindow.dismiss();
			}
		});
		
		return mPopupWindow;
	}
	
	
	/**
	 * 对话框样式的重新设计
	 */
	public static PopupWindow showDialog(Activity activity, String text,final OnResultListener onResultListener){
		View view = LayoutInflater.from(activity).inflate(R.layout.popupw_dialog, null);
		final PopupWindow mPopupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(activity.getResources().getDrawable(android.R.color.transparent));
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.update();
		mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
		
		//设置的内容
		TextView tvContent = (TextView) view.findViewById(R.id.tv_dialog_content);
		tvContent.setText(text);
		Button btnOk = (Button) view.findViewById(R.id.btn_ok);//确定
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onResultListener.onOK();
				mPopupWindow.dismiss();
			}
		});
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);//取消
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onResultListener.onCancel();
				mPopupWindow.dismiss();
			}
		});
		
		return mPopupWindow;
	}
	
	
}
