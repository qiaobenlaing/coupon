package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.ScanSuccessFragment;

import android.app.Fragment;
import android.view.KeyEvent;
import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.base.Util;

/**
 * 扫码成功
 * @author wenis.yu
 *
 */
public class ScanSuccessActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return ScanSuccessFragment.newInstance();
	}


	/**
	 * 重写点击返回按钮方法，点击一次土司提示，两次退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			H5ShopDetailActivity.setCurrentPage("backup");
			Util.exitAct();
		}
		return super.onKeyDown(keyCode, event);
	}
}
