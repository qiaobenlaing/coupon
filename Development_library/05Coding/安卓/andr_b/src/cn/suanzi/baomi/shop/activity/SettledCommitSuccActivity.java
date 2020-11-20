package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.SettledCommitSuccFragment;


/**
 * 提交成功(返回到登陆页)
 * @author wensi.yu
 *
 */
public class SettledCommitSuccActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return SettledCommitSuccFragment.newInstance();
	}
	
}
