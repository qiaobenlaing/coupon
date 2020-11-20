package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.MyHomeAddValidateFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 短信验证
 * 
 * @author wensi.yu
 *
 */
public class MyHomeAddValidateActivity extends SingleFragmentActivity {

	private final static String TAG = MyHomeAddValidateActivity.class.getSimpleName();
	
	@Override
	protected Fragment createFragment() {
		return MyHomeAddValidateFragment.newInstance();
	}

}
