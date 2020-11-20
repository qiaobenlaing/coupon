package cn.suanzi.baomi.shop.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.fragment.MyImageShopFragment;

/**
 * 店铺形象
 * @author qian.zhou
 */
public class MyImageShopActivity extends Activity {
	/** Fragment管理器 */
	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imgshop);
		mFragmentManager = this.getFragmentManager();
		mFragmentTransaction = mFragmentManager.beginTransaction();
		mFragmentTransaction.replace(R.id.ly_shopdetail, MyImageShopFragment.newInstance());
		mFragmentTransaction.addToBackStack(null);
		mFragmentTransaction.commit();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
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
	
	public void onResume(){
    	super.onResume();
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
    }
}
