package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.SettledCommitSuccFragment;

import android.app.Fragment;

/**
 * 提交成功(返回到登陆页)
 * @author wensi.yu
 *
 */
public class SettledCommitSuccActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return SettledCommitSuccFragment.newInstance();
	}
	
}
