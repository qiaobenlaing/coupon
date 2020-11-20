package cn.suanzi.baomi.cust.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.fragment.ShopDetailFragment;

import com.umeng.analytics.MobclickAgent;

public class ShopDetailActivity extends Activity {

	private static final String TAG = ShopDetailActivity.class.getSimpleName();
	/** Fragment管理器 */
	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;
	private ShopDetailFragment mDetailFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop);
		mDetailFragment = ShopDetailFragment.newInstance();
		mFragmentManager = this.getFragmentManager();
		mFragmentTransaction = mFragmentManager.beginTransaction();
		mFragmentTransaction.replace(R.id.ly_shopdetail, mDetailFragment);
		mFragmentTransaction.addToBackStack(null);
		mFragmentTransaction.commit();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (null != mDetailFragment) {
			mDetailFragment.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	/**
	 * 重写点击返回按钮方法，点击一次土司提示，两次退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	protected void onResume(){
		super.onResume();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		MobclickAgent.onResume(this);       //统计时长
	}

	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(this);
	}
}
