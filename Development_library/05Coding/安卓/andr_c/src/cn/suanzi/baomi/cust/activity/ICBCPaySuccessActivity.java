package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import android.view.KeyEvent;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.ICBCPaySuccessFragment;

public class ICBCPaySuccessActivity extends SingleFragmentActivity {
		
	private ICBCPaySuccessFragment mICBCPaySuccessFragment;
	@Override
	protected Fragment createFragment() {
		mICBCPaySuccessFragment = ICBCPaySuccessFragment.newInstance();
		return mICBCPaySuccessFragment;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			mICBCPaySuccessFragment.onBackPressedFragment();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
