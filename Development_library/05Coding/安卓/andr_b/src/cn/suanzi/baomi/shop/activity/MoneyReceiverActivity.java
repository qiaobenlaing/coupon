package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.MoneyReceiverFragment;

/**
 * 红包领取人数
 * @author wensi.yu
 *
 */
public class MoneyReceiverActivity extends SingleFragmentActivity {

	private final static String TAG = "MoneyReceiverActivity";
	
	@Override
	protected Fragment createFragment() {
		return MoneyReceiverFragment.newInstance();
	}

}
