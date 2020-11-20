package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.MyTwoCodeFragment;

/**
 * 我的二维码
 * @author qian.zhou
 */
public class MyTwoCodeActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return MyTwoCodeFragment.newInstance();
	}

}
