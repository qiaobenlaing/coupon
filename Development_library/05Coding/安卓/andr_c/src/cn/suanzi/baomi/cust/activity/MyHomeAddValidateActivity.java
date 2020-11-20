package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.MyHomeAddValidateFragment;

/**
 * 短信验证
 * 
 * @author wensi.yu
 *
 */
public class MyHomeAddValidateActivity extends SingleFragmentActivity {

	private final static String TAG = MyHomeAddValidateActivity.class.getSimpleName();
	
	@Override
	protected Fragment createFragment() {
		return MyHomeAddValidateFragment.newInstance();
	}

}
