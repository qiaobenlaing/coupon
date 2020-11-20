package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import android.view.KeyEvent;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.ICBCOnlinePayFragment;

public class ICBCOnlinePayActivity extends SingleFragmentActivity {
	private ICBCOnlinePayFragment  mICBCOnlinePayFragment;

	@Override
	protected Fragment createFragment() {
		mICBCOnlinePayFragment=  ICBCOnlinePayFragment.newInstance();
		return mICBCOnlinePayFragment;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (null != mICBCOnlinePayFragment) {
				mICBCOnlinePayFragment.onBackPressedFragment();
			}
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
