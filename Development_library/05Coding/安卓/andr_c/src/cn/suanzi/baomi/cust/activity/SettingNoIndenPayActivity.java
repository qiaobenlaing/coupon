package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import android.view.KeyEvent;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.SettingNoIndenPayFragment;

/**
 * 设置免验证码支付
 * @author yingchen
 *
 */
public class SettingNoIndenPayActivity extends SingleFragmentActivity {

	private SettingNoIndenPayFragment fragment;
	@Override
	protected Fragment createFragment() {
		 fragment = SettingNoIndenPayFragment.newInstance();
		 return fragment;
	}

	
	/**
		 * 点击返回
		 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		fragment.onBack();
		return true;
	}
}
