package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.MyHomeAddFinishFragment;

/**
 * 绑定完成
 * 
 * @author wensi.yu 
 *
 */
public class MyHomeAddFinishActivity extends SingleFragmentActivity {

	private final static String TAG = MyHomeAddFinishActivity.class.getSimpleName();
	
	@Override
	protected Fragment createFragment() {
		return MyHomeAddFinishFragment.newInstance();
	}

}
