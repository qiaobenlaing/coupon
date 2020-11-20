// ---------------------------------------------------------
// @author    
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.RegisterFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

/**
 * 用于注册的Activity
 * @author 
 */
public class RegisterActivity extends SingleFragmentActivity {
	private static final String TAG = "RegisterActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"RegisterActivity Create");
	}

	@Override
	protected Fragment createFragment() {
		return RegisterFragment.newInstance();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "RegisterActivity Destroy");
	}
}
