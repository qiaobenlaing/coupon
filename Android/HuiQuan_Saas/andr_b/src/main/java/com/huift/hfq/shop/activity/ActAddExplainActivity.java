package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.ActAddExplainFragment;

import android.app.Fragment;

/**
 * @author wensi.yu
 * 添加营销活动说明
 */
public class ActAddExplainActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return ActAddExplainFragment.newInstance() ;
	}
}
