package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.MyRecommonedFragment;

import android.app.Fragment;

/**
 * 用户推荐
 * @author wensi.yu
 *
 */
public class MyRecommonedActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return MyRecommonedFragment.newInstance();
	}

}
