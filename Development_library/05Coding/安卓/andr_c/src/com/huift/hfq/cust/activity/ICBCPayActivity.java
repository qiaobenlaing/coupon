package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.ICBCPayFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;
/**
 * 登录Activity 单Fragment
 * 
 * @author Zhonghui.Dong
 */
public class ICBCPayActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return ICBCPayFragment.newInstance();
	}

}
