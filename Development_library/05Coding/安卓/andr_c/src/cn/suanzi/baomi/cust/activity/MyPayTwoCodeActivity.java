package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.MyPayTwoCodeFragment;

/**
 * 我的付款二维码
 * @author yanfang.li
 *
 */
public class MyPayTwoCodeActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return MyPayTwoCodeFragment.newInstance();
	}
}
