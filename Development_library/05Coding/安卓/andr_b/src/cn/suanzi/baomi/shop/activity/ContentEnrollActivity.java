package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.ContentEnrollFragment;

/**
 * 报名人数
 * @author wensi.yu
 *
 */
public class ContentEnrollActivity extends SingleFragmentActivity {

	private final static String TAG = "EnrollActivity";
	
	@Override
	protected Fragment createFragment() {
		return ContentEnrollFragment.newInstance();
	}

}
