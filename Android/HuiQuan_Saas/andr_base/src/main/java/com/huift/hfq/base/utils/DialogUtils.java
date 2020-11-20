package com.huift.hfq.base.utils;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.StringRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.huift.hfq.base.R;
import com.huift.hfq.base.Util;

public class DialogUtils {

	/**
	 * 回调方法的接口
	 */
	public  abstract class OnResultListener {
		public  void onOK(){};
		public  void onCancel(){};
	}

	/**
	 * 添加
	 *
	 */
	public abstract class OnResListener {
		public void onOk(String...params){};
		public void onCancel(){};
	}

	/**
	 * 对话框样式的重新设计
	 */
	public static void showDialog(Context context, String title, String text, String btnTextOk, String btnTextCal, final OnResultListener onResultListener){
		View view = LayoutInflater.from(context).inflate(R.layout.popupw_dialog, null);
		final PopupWindow mPopupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(context.getResources().getDrawable(android.R.color.transparent));
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.update();
		mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

		//设置
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialgo_title);//标题
		tvTitle.setText(title);
		TextView tvContent = (TextView) view.findViewById(R.id.tv_dialog_content);//内容
		tvContent.setText(text);

		Button btnOk = (Button) view.findViewById(R.id.btn_ok);//确定
		btnOk.setText(btnTextOk);
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onResultListener.onOK();
				mPopupWindow.dismiss();
			}
		});
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);//取消
		btnCancel.setText(btnTextCal);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onResultListener.onCancel();
				mPopupWindow.dismiss();
			}
		});
	}

	/**
	 * 对话框样式的重新设计
	 */
	public static void showSetStoreDialog(Activity activity,String shopName, String storeName, String btnok, String btncancel, final OnResultListener onResultListener){
		View view = LayoutInflater.from(activity).inflate(R.layout.popupw_uppstore_dialog, null);
		final PopupWindow mPopupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(activity.getResources().getDrawable(android.R.color.transparent));
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.update();
		mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
		SpannableString ss;
		ColorStateList redColor = ColorStateList.valueOf(0xFFFF0000);

		//设置
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_shop_name);//标题
		String title = shopName + "的店长是由" + storeName + "担任店长，您确定要替换吗?";
		int end1 = title.indexOf("的");
		int start2 = title.indexOf("由") + 1;
		int end2 = title.indexOf("担");
		ss = new SpannableString(title);
		ss.setSpan(new TextAppearanceSpan(null, 0, 0, redColor, null), 0, end1,
				Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		ss.setSpan(new TextAppearanceSpan(null, 0, 0, redColor, null), start2, end2,
				Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		tvTitle.setText(ss);

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
	 * 对话框样式的重新设计
	 */
	public static void showSetShopDialog(Activity activity,String tilte, String btnok, final OnResultListener onResultListener){
		View view = LayoutInflater.from(activity).inflate(R.layout.popupw_uppshop_dialog, null);
		final PopupWindow mPopupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(activity.getResources().getDrawable(android.R.color.transparent));
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.update();
		mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

		//设置
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_shop_content);//标题
		tvTitle.setText(tilte);

		Button btnOk = (Button) view.findViewById(R.id.btn_ok);//确定
		btnOk.setText(btnok);
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onResultListener.onOK();
				mPopupWindow.dismiss();
			}
		});
	}

	/**
	 *  只有确认按钮
	 * @param activity
	 * @param content 内容
	 * @param titleId 标题id
	 * @param okButtomId 按钮id
	 * @param onResultListener 确定按钮的事件
	 * @return
	 */
	public static void showDialogSingle(Activity activity, String content, @StringRes int titleId,@StringRes int okButtomId, final OnResultListener onResultListener){
		View view = LayoutInflater.from(activity).inflate(R.layout.popupw_dialog_single, null);
		final PopupWindow mPopupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(activity.getResources().getDrawable(android.R.color.transparent));
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.update();
		mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

		//设置的标题
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialgo_title);
		tvTitle.setText(Util.getString(titleId));
		// 内容
		TextView tvContent = (TextView) view.findViewById(R.id.tv_dialog_content);
		tvContent.setText(content);
		// 确定按钮
		Button btnOk = (Button) view.findViewById(R.id.btn_ok);
		btnOk.setText(Util.getString(okButtomId));

		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(null!=onResultListener){
					onResultListener.onOK();
				}
				mPopupWindow.dismiss();
			}
		});
	}

	/**
	 *编辑对话框(添加)
	 */
	public static void showDialogEditor(Activity activity, String title,String btnok, String btncancel, final OnResListener onResListener){
		View view = LayoutInflater.from(activity).inflate(R.layout.popupw_dialog_editor, null);
		final PopupWindow mPopupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(activity.getResources().getDrawable(android.R.color.transparent));
		mPopupWindow.setOutsideTouchable(true);
		//mPopupWindow.update();
		mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

		//设置
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialgo_title);//标题
		tvTitle.setText(title);
		final EditText tvName = (EditText) view.findViewById(R.id.et_dialog_name);//姓名
		final EditText tvPwd = (EditText) view.findViewById(R.id.et_dialog_pwd);//手机号码

		Button btnOk = (Button) view.findViewById(R.id.btn_ok);//确定
		btnOk.setText(btnok);
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String staffName = tvName.getText().toString();
				String staffPwd = tvPwd.getText().toString();
				Log.d("TAG", "staffName=="+staffName + "staffPwd=="+staffPwd);
				onResListener.onOk(new String []{staffName,staffPwd});
				mPopupWindow.dismiss();
			}
		});
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);//取消
		btnCancel.setText(btncancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onResListener.onCancel();
				mPopupWindow.dismiss();
			}
		});
	}

	/**
	 *编辑对话框(修改)
	 */
	public static void showDialogEditorUpdate(Activity activity, String title,String name,String phone,String btnok, String btncancel, final OnResListener onResListener){
		View view = LayoutInflater.from(activity).inflate(R.layout.popupw_dialog_editor, null);
		final PopupWindow mPopupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(activity.getResources().getDrawable(android.R.color.transparent));
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.update();
		mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

		//设置
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialgo_title);//标题
		tvTitle.setText(title);
		final EditText tvName = (EditText) view.findViewById(R.id.et_dialog_name);//姓名
		tvName.setText(name);
		final EditText tvPwd = (EditText) view.findViewById(R.id.et_dialog_pwd);//手机号码
		tvPwd.setText(phone);

		Button btnOk = (Button) view.findViewById(R.id.btn_ok);//确定
		btnOk.setText(btnok);
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String staffName = tvName.getText().toString();
				String staffPwd = tvPwd.getText().toString();
				Log.d("TAG", "staffName11=="+staffName + "staffPwd11=="+staffPwd);
				onResListener.onOk(new String []{staffName,staffPwd});
				mPopupWindow.dismiss();
			}
		});
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);//取消
		btnCancel.setText(btncancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onResListener.onCancel();
				mPopupWindow.dismiss();
			}
		});
	}

	/**
	 *输入餐桌号
	 */
	public static void showDialogTable(Activity activity, String title,String btnok, String btncancel, final OnResListener onResListener){
		View view = LayoutInflater.from(activity).inflate(R.layout.popupw_dialog_table, null);
		final PopupWindow mPopupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(activity.getResources().getDrawable(android.R.color.transparent));
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

		//设置
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialgo_title);//标题
		tvTitle.setText(title);
		final EditText tvTable = (EditText) view.findViewById(R.id.et_dailog_table);//餐桌号

		Button btnOk = (Button) view.findViewById(R.id.btn_ok);//确定
		btnOk.setText(btnok);
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String inputTable = tvTable.getText().toString();
				Log.d("TAG", "inputTable=="+inputTable);
				onResListener.onOk(new String []{inputTable});
				mPopupWindow.dismiss();
			}
		});
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);//取消
		btnCancel.setText(btncancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onResListener.onCancel();
				mPopupWindow.dismiss();
			}
		});
	}

}
