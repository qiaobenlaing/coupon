package com.huift.hfq.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.widget.TextView;
import com.huift.hfq.base.R;

public class TimeCountUtil extends CountDownTimer {

	private Activity mActivity;
	private TextView btn;//按钮
	
	/**
	 * 构造函数
	 * @param millisInFuture
	 * @param countDownInterval
	 */
	public TimeCountUtil( Activity mActivity,long millisInFuture, long countDownInterval,TextView btn) {
		super(millisInFuture, countDownInterval);
		this.mActivity = mActivity;
		this.btn = btn;
	}

	
	@SuppressLint("NewApi")
	@Override
	public void onTick(long millisUntilFinished) {
		btn.setClickable(false);//设置不能点击
		btn.setText(millisUntilFinished / 1000 + "秒可重新发送");//设置倒计时时间
		btn.setGravity(Gravity.CENTER);
		btn.setTextSize(12);
		//设置按钮为灰色，这时是不能点击的
//		btn.setBackground(mActivity.getResources().getDrawable(R.drawable.bg_back));
		btn.setBackgroundResource(R.drawable.bg_code);
//		btn.setWidth(10);
		//获取按钮的文字
		Spannable span = new SpannableString(btn.getText().toString());
		//讲倒计时时间显示为红色
		span.setSpan(new ForegroundColorSpan(Color.RED), 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		btn.setText(span);
		
	}
	
	@SuppressLint({ "NewApi", "ResourceAsColor" })
	@Override
	public void onFinish() {
		btn.setText("重新获取验证码");
		Spannable span = new SpannableString(btn.getText().toString());
		span.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		btn.setText(span);
		btn.setTextColor(R.color.white);
		btn.setGravity(Gravity.CENTER);
		btn.setTextSize(12);
		btn.setClickable(true);//重新获得点击
		btn.setBackgroundResource(R.drawable.bg_red_code);//还原背景色
	}
}
