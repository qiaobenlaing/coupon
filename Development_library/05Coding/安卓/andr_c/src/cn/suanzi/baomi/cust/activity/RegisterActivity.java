package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.RegisterFragment;

/**
 * 用于注册的Activity
 * 
 * @author Wei.Fang
 * 
 */
public class RegisterActivity extends SingleFragmentActivity {

	private static final String TAG = RegisterActivity.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "RegisterActivity Create");
		/*setContentView(R.layout.activity_register);
		*//** Fragment管理器 *//*
		FragmentManager mFragmentManager = getFragmentManager();
		FragmentTransaction trx= mFragmentManager.beginTransaction();
		trx.add(R.id.ly_register, new RegisterFragment());
		trx.commit();
		AppUtils.setActivity(this);*/
	}

	@Override
	protected Fragment createFragment() {
		return RegisterFragment.newInstance();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "RegisterActivity Destroy");
	}
	
}
