package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.MyHomeAboutFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 关于Activity 单Fragment
 * @author qian.zhou
 */
public class MyHomeAboutActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return MyHomeAboutFragment.newInstance();
	}
}
