package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.ScanPrinterFragment;

/**
 * 打印
 * @author wensi.yu
 *
 */
public class ScanPrinterActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return ScanPrinterFragment.newInstance();
	}

}
