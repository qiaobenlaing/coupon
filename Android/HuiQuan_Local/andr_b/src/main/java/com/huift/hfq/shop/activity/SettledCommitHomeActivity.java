package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.SettledCommitHomeFragment;

import android.app.Fragment;

/**
 * 提交成功(返回到首页)
 * @author wensi.yu
 *
 */
public class SettledCommitHomeActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return SettledCommitHomeFragment.newInstance();
	}

}
