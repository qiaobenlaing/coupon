package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.ContentEnrollFragment;

import android.app.Fragment;

/**
 * 报名人数
 * @author wensi.yu
 *
 */
public class ContentEnrollActivity extends SingleFragmentActivity {

	private final static String TAG = "EnrollActivity";
	
	@Override
	protected Fragment createFragment() {
		return ContentEnrollFragment.newInstance();
	}

}
