package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.MyHomeAboutFragment;

/**
 * 关于Activity 单Fragment
 * @author qian.zhou
 */
public class MyHomeAboutActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return MyHomeAboutFragment.newInstance();
	}
}
