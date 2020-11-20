package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.ActIcBcDetailFragment;

/**
 * 查看工行特惠的活动详情
 * @author wensi.yu
 */
public class ActIcBcDetailActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return ActIcBcDetailFragment.newInstance();
	}		
}
