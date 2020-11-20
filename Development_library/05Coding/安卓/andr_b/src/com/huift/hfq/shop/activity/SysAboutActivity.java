package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.ChooseStoreFragment;

import android.app.Fragment;

/**
 *  关于苞米商家端
 * @author qian.zhou
 */
public class SysAboutActivity extends SingleFragmentActivity {
	private static final String TAG = "SysAboutActivity";
	@Override
	protected Fragment createFragment() {
		return ChooseStoreFragment.newInstance();
	}

}
