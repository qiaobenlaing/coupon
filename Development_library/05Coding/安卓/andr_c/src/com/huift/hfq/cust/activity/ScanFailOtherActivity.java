package com.huift.hfq.cust.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import com.huift.hfq.base.Util;
import com.huift.hfq.cust.R;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 扫描失败其他原因
 * @author wensi.yu
 *
 */
public class ScanFailOtherActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_scanfail);
		ViewUtils.inject(this);
		TextView failShop = (TextView) findViewById(R.id.tv_fail_other);
		failShop.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 点击
	 * @param view
	 */
	@OnClick({R.id.btn_scan_fail_back,R.id.btn_scan_fail_again})
	public void trunIdenCode(View view) {
		switch (view.getId()) {
		case R.id.btn_scan_fail_back://返回
			H5ShopDetailActivity.setCurrentPage("backup");
			Util.exit();
			break;
			
		case R.id.btn_scan_fail_again://重新扫描
			Util.exitLogin(); 
			break;

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
			H5ShopDetailActivity.setCurrentPage("backup");
			Util.exit();
		}
		return super.onKeyDown(keyCode, event);
	}
}
