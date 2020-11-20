// ---------------------------------------------------------
// @author    
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.activity;

import android.app.Fragment;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.ResetPwdFragment;

/**
 * 用于重置密码
 * @author 
 * 
 */
public class ResetPwdActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return ResetPwdFragment.newInstance();
	}
	
}
