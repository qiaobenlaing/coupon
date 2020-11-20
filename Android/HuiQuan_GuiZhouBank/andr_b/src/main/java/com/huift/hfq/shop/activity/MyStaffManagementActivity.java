package com.huift.hfq.shop.activity;

import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.fragment.MyStaffManagementFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import com.huift.hfq.shop.R;

/**
 * 店员管理
 * @author wenis.yu
 *
 */
public class MyStaffManagementActivity extends Activity {

	private final static String TAG = "MyStaffManagementActivity";
	
	private MyStaffManagementFragment myStaffManagementFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stafflist);
		FragmentManager mFragmentManager = getFragmentManager();
		myStaffManagementFragment = MyStaffManagementFragment.newInstance();
		FragmentTransaction trx= mFragmentManager.beginTransaction();
		trx.add(R.id.ly_stafflist, myStaffManagementFragment);
		trx.commit();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}
	
	public void onResume(){
    	super.onResume();
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
    }
	
	/**
	 * 点击返回
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (myStaffManagementFragment.mShowEdit){
			myStaffManagementFragment.setEditInVisibile();
		} else {
			finish();
		}
		return true;
	}
}
