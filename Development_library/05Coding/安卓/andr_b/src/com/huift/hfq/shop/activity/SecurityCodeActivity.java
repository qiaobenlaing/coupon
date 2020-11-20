package com.huift.hfq.shop.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.fragment.InputValidationFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 验证码
 * @author qian.zhou
 */
public class SecurityCodeActivity extends Activity {
	/** Fragment管理器 */
	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;
	private RadioButton mBtnCouponVer, mBtnActivityVer;
	/** 判断是代金券页面还是活动页面*/
	private String mIsGo = "1";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_security_code);
        ViewUtils.inject(this);
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
        init();
    }
	
	/**
	 * 更换页面
	 * @param id
	 * @param fragment
	 */
	public void changeFragment(Activity activity, int id, Fragment fragment) {
		mFragmentManager = activity.getFragmentManager();
		mFragmentTransaction = mFragmentManager.beginTransaction();
		mFragmentTransaction.replace(id, fragment);
		mFragmentTransaction.addToBackStack(null);
		mFragmentTransaction.commit();
	}
	
	// 初始化方法
	private void init() {
		DB.saveStr(ShopConst.CouponVerification.IS_GO, mIsGo);
		TextView tvTitle = (TextView)findViewById(R.id.tv_mid_content);
		tvTitle.setText(R.string.identify_code);
		mBtnCouponVer = (RadioButton) findViewById(R.id.rbtn_couponorvoucher_verification);
		mBtnActivityVer = (RadioButton) findViewById(R.id.rbtn_activity_verification);
		// 给兑换券和代金券按钮一个默认背景颜色
		changeFragment(SecurityCodeActivity.this, R.id.linear_cardlist_content, new InputValidationFragment());
	}
	
	/**
	 * 添加fragment的跳转
	 * @param v
	 */
	@OnClick({ R.id.rbtn_couponorvoucher_verification, R.id.rbtn_activity_verification })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rbtn_couponorvoucher_verification:// 兑换券和代金券页面
			changeFragment(SecurityCodeActivity.this, R.id.linear_cardlist_content, new InputValidationFragment());
			mBtnCouponVer.setEnabled(false);
			mBtnActivityVer.setEnabled(true);
			mIsGo = "1";
			DB.saveStr(ShopConst.CouponVerification.IS_GO, mIsGo);
			break;
		case R.id.rbtn_activity_verification:// 活动验证页面
			changeFragment(SecurityCodeActivity.this, R.id.linear_cardlist_content, new InputValidationFragment());
			mBtnActivityVer.setEnabled(false);
			mBtnCouponVer.setEnabled(true);
			mIsGo = "2";
			DB.saveStr(ShopConst.CouponVerification.IS_GO, mIsGo);
			break;
		/*case R.id.rbtn_scancode_verification://扫码验证页面
			changeFragment(SecurityCodeActivity.this, R.id.linear_cardlist_content, new InputValidationFragment());
			mBtnScancodeVer.setEnabled(false);
			mBtnCouponVer.setEnabled(true);
			mBtnActivityVer.setEnabled(true);
			mIsGo = "3";
			DB.saveStr(ShopConst.CouponVerification.IS_GO, mIsGo);
			break;*/
		default:
			break;
		}
	}
	
	/**
	 * 重写点击返回按钮方法，点击一次土司提示，两次退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 点击事件
	 */
	@OnClick({ R.id.layout_turn_in })
	private void ivreturnClickTo(View v) {
		finish();
	}
}
