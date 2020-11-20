package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.MyHomeSettingFragment;

/**
 * 设置界面
 * @author qian.zhou
 */
public class MyHomeSettingActivity extends SingleFragmentActivity{
	@Override
	protected Fragment createFragment() {
		return MyHomeSettingFragment.newInstance();
	}
}
