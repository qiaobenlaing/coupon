// ---------------------------------------------------------
// @author    
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.ResetPwdFragment;

/**
 * 用于重置密码
 * @author 
 * 
 */
public class ResetPwdActivity extends SingleFragmentActivity {
	private static final String TAG = "ResetPwdActivity";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG,"ResetPwdActivity Create");
	}
	
	@Override
	protected Fragment createFragment() {
		return ResetPwdFragment.newInstance();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "ResetPwdActivity Destroy");
	}
}
