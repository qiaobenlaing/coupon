package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.EnrollFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 报名
 * @author wensi.yu
 *
 */
public class EnrollActivity extends SingleFragmentActivity {

	private final static String TAG = EnrollActivity.class.getSimpleName();
	
	@Override
	protected Fragment createFragment() {
		return EnrollFragment.newInstance();
	}
	
}
