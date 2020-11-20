// ---------------------------------------------------------
// @author    Weiping Liu
// @version   1.0.0
// @copyright 版权所有 (c) 2014 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.base;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.huift.hfq.base.utils.AppUtils;

/**
 * 含有单个Fragement的Activity
 * 
 * @author Weiping Liu
 * @version 1.0.0 
 */
public abstract class SingleFragmentActivity extends Activity {

	protected abstract Fragment createFragment();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_singlefragent);
		AppUtils.setActivity(this);// 保存当前的activity
		AppUtils.setContext(getApplicationContext()); // 保存context
		FragmentManager fm = getFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragment_container); // 主要所有Fragment的id都必须是这个
		if (fragment == null) {
			fragment = createFragment();
			fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
		}
	}
	
	protected void onResume(){
		super.onResume();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext()); // 保存context
	}
	
	public void finish(){
		Util.homeActivityList.remove(this);
		Util.activityLoginList.remove(this);
//		Util.activityList.remove(this);
		Util.activityRecommonedList.remove(this);
		Util.actList.remove(this);
		super.finish();
	}
}
