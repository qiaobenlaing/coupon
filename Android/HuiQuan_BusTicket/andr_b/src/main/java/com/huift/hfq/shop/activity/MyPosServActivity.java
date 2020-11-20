package com.huift.hfq.shop.activity;
import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.MyPosServFragment;

import android.app.Fragment;

/**
 * POS服务显示界面
 * @author qian.zhou
 */
public class MyPosServActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return MyPosServFragment.newInstance();
	}
}
