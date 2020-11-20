package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.ActRefundFragment;

/**
 * 活动退款
 * @author yanfang.li
 */
public class ActRefundActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return ActRefundFragment.newInstance();
	}
}
