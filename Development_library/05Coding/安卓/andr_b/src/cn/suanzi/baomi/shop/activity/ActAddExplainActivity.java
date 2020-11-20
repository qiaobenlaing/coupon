package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.ActAddExplainFragment;

/**
 * @author wensi.yu
 * 添加营销活动说明
 */
public class ActAddExplainActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return ActAddExplainFragment.newInstance() ;
	}
}
