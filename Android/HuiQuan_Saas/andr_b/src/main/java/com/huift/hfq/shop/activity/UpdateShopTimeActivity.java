package com.huift.hfq.shop.activity;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.fragment.UpdateShopTimeFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import com.huift.hfq.shop.R;

/**
 * 修改商家的营业时间
 * @author qian.zhou
 */
public class UpdateShopTimeActivity extends Activity {
	
private UpdateShopTimeFragment mShopTimeFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upp_shoptimelist);
		/** Fragment管理器 */
		FragmentManager mFragmentManager = getFragmentManager();
		mShopTimeFragment = UpdateShopTimeFragment.newInstance();
		FragmentTransaction trx= mFragmentManager.beginTransaction();
		trx.add(R.id.ly_timelist, mShopTimeFragment);
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
		finish();
		return true;
	}
}
