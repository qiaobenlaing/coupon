package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.TimeLimitFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 限时购
 * @author wensi.yu
 *
 */
public class TimeLimitActivity extends SingleFragmentActivity {

	private final static String TAG = TimeLimitActivity.class.getSimpleName();
	
	@Override
	protected Fragment createFragment() {
		return TimeLimitFragment.newInstance();
	}
}
