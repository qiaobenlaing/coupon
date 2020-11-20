package cn.suanzi.baomi.shop.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.fragment.BillClassFragment;
import cn.suanzi.baomi.shop.fragment.MyStaffManagementFragment;

/**
 * 账单的列表
 * @author wensi.yu
 *
 */
public class BillClassActivity extends Activity {

	private BillClassFragment mBillClassFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_billclass);
		FragmentManager mFragmentManager = getFragmentManager();
		mBillClassFragment = BillClassFragment.newInstance();
		FragmentTransaction trx= mFragmentManager.beginTransaction();
		trx.add(R.id.ly_billclass, mBillClassFragment);
		trx.commit();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
