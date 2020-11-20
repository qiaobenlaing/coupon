package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.ScanInputAmountFragment;

/**
 * 扫描输入金额
 * @author wensi.yu
 *
 */
public class ScanInputAmountActivity extends SingleFragmentActivity{

	@Override
	protected Fragment createFragment() {
		return ScanInputAmountFragment.newInstance();
	}

}
