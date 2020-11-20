package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.MyHomeBankDetailFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 银行卡详情
 * @author wensi.yu
 *
 */
public class MyHomeBankDetailActivity extends SingleFragmentActivity{

	private final static String TAG = MyHomeBankDetailActivity.class.getSimpleName();
	
	@Override
	protected Fragment createFragment() {
		return MyHomeBankDetailFragment.newInstance();
	}

}
