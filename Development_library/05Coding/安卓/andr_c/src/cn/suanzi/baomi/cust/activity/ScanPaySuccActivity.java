package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.ScanPaySuccFragment;

/**
 * 扫描支付成功界面（推送界面）
 * @author yanfang.li
 */
public class ScanPaySuccActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return ScanPaySuccFragment.newInstance();
	}
}
