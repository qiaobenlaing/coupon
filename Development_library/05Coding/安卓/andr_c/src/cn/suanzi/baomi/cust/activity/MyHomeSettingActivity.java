package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.MyHomeSettingFragment;

public class MyHomeSettingActivity extends SingleFragmentActivity{

	@Override
	protected Fragment createFragment() {
		return MyHomeSettingFragment.newInstance();
	}
}
