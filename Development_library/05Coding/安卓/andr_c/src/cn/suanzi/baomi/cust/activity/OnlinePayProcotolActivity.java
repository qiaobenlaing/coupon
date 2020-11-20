package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.OnlinePayProcotolFragment;

/**
 * 在线支付协议
 * @author yingchen
 *
 */
public class OnlinePayProcotolActivity extends SingleFragmentActivity {
	
	private static final String TAG = OnlinePayProcotolActivity.class.getSimpleName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "LoginActivity create");
	}
	
	@Override
	protected Fragment createFragment() {
		return OnlinePayProcotolFragment.newInstance();
	}
}
