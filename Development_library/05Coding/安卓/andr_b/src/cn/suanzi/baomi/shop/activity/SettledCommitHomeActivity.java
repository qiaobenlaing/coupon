package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.SettledCommitHomeFragment;

/**
 * 提交成功(返回到首页)
 * @author wensi.yu
 *
 */
public class SettledCommitHomeActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return SettledCommitHomeFragment.newInstance();
	}

}
