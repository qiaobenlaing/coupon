package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.MyHomeAddFinishFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 绑定完成
 * 
 * @author wensi.yu 
 *
 */
public class MyHomeAddFinishActivity extends SingleFragmentActivity {

	private final static String TAG = MyHomeAddFinishActivity.class.getSimpleName();
	
	@Override
	protected Fragment createFragment() {
		return MyHomeAddFinishFragment.newInstance();
	}

}
