package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.MyAddManagerFragment;

import android.app.Fragment;

/**
 * 添加员工信息
 * @author qian.zhou 
 */
public class AddStaffActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return MyAddManagerFragment.newInstance();
	}
}
