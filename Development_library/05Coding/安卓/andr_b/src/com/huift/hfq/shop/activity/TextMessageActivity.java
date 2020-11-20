package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.TextMessageFragment;

import android.app.Fragment;

/**
 * 设置短信接收
 * @author liyanfang
 */
public class TextMessageActivity extends SingleFragmentActivity {	
	
	@Override
	protected Fragment createFragment() {
		return TextMessageFragment.newInstance();
	}
}
