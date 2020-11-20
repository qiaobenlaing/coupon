package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.ActAddFragment;

/**
 * @author wensi.yu
 * 添加营销活动主题，时间，地点
 */
public class ActAddActivity extends SingleFragmentActivity {	
	
	@Override
	protected Fragment createFragment() {
		return ActAddFragment.newInstance();
	}
}
