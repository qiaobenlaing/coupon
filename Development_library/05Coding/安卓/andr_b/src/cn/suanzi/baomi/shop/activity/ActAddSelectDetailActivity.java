package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.ActAddSelectDetailFragment;

/**
 * @author  wensi.yu
 * 营销活动的预览活动详情
 */
public class ActAddSelectDetailActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return ActAddSelectDetailFragment.newInstance();
	}
}
