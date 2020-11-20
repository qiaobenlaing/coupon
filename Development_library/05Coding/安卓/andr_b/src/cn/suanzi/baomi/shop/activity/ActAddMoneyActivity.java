package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.ActAddMoneyFragment;

/**
 * 添加红包
 * 
 * @author wensi.yu
 *
 */
public class ActAddMoneyActivity extends SingleFragmentActivity {

	private final static String TAG = "ActAddMoneyActivity";
	
	@Override
	protected Fragment createFragment() {
		return ActAddMoneyFragment.newInstance();
	}
}
