package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.UserAccessFragment;

/**
 * 用户统计
 * @author qian.zhou
 */
public class UserAccessActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return UserAccessFragment.newInstance();
	}
}
