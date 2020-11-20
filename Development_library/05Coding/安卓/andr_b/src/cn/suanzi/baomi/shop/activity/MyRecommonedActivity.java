package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.MyRecommonedFragment;

/**
 * 用户推荐
 * @author wensi.yu
 *
 */
public class MyRecommonedActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return MyRecommonedFragment.newInstance();
	}

}
