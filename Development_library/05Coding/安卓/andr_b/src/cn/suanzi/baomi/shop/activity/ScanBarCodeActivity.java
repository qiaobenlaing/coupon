package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import android.view.KeyEvent;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.ScanBarCodeFragment;

/**
 * 机器扫描
 * @author wensi.yu
 *
 */
public class ScanBarCodeActivity extends SingleFragmentActivity{

	@Override
	protected Fragment createFragment() {
		return ScanBarCodeFragment.newInstance();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

}
