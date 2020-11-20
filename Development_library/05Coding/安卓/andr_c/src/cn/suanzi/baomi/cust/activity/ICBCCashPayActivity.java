package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.ICBCCashPayFragment;

public class ICBCCashPayActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return ICBCCashPayFragment.newInstance();
	}

}
