package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.ActivityManagerFragment;
 
/**
 * 活动管理界面
 * @author qian.zhou
 */
public class ActivityManagerActivity extends SingleFragmentActivity {
	private final static String TAG = ActivityManagerActivity.class.getSimpleName();
	@Override
	protected Fragment createFragment() {
		return ActivityManagerFragment.newInstance();
	}
}
