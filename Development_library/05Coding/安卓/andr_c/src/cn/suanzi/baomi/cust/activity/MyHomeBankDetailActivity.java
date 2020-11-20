package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.MyHomeBankDetailFragment;

/**
 * 银行卡详情
 * @author wensi.yu
 *
 */
public class MyHomeBankDetailActivity extends SingleFragmentActivity{

	private final static String TAG = MyHomeBankDetailActivity.class.getSimpleName();
	
	@Override
	protected Fragment createFragment() {
		return MyHomeBankDetailFragment.newInstance();
	}

}
