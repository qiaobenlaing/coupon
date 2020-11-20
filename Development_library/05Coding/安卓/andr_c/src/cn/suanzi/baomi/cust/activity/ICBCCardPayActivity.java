package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import android.view.KeyEvent;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.fragment.ICBCCardFragment;

public class ICBCCardPayActivity extends SingleFragmentActivity {

	private static boolean payFlag = false;
	@Override
	protected Fragment createFragment() {
		return ICBCCardFragment.newInstance();
	}
	
	public static void setPayFlag () {
		payFlag = true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			if (!payFlag) {
				Util.getContentValidate(R.string.pay_return);
				return payFlag;
			} else {
				this.finish();
			}
			
		}
		return super.onKeyDown(keyCode, event);
	}

}
