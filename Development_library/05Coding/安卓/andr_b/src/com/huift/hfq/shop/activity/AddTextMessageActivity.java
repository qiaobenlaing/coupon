package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.AddTextMessageFragment;

import android.app.Fragment;

/**
 * 添加短信接收者
 * @author liyanfang
 */
public class AddTextMessageActivity extends SingleFragmentActivity {	
	
	@Override
	protected Fragment createFragment() {
		return AddTextMessageFragment.newInstance();
	}
}
