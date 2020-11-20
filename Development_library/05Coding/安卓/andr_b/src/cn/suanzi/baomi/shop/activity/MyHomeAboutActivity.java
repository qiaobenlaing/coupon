package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.MyHomeAboutFragment;

public class MyHomeAboutActivity  extends SingleFragmentActivity{
	private final static String TAG = "MyHomeAboutActivity";
	@Override
	protected Fragment createFragment() {
		return MyHomeAboutFragment.newInstance();
	}
}
