package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.MyTwoCodeFragment;

import android.app.Fragment;

/**
 * 我的二维码
 * @author qian.zhou
 */
public class MyTwoCodeActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return MyTwoCodeFragment.newInstance();
	}

}
