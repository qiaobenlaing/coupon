package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.BigTwoCodeFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 我的付款二维码
 * @author yanfang.li
 *
 */
public class BigTwoCodeActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return BigTwoCodeFragment.newInstance();
	}
}
