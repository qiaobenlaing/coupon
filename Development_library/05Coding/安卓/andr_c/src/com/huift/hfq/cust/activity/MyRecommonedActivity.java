package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.MyRecommonedFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 推荐好友
 * @author wensi.yu
 *
 */
public class MyRecommonedActivity extends SingleFragmentActivity {
	
	private static final String TAG = MyRecommonedActivity.class.getSimpleName();

	@Override
	protected Fragment createFragment() {
		return MyRecommonedFragment.newInstance();
	}
}
