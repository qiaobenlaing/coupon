package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.EditAcitityContextFragment;

/**
 * 编辑活动
 * @author qian.zhou
 */
public class EditAcitityContextActivity extends SingleFragmentActivity {

	private final static String TAG = EditAcitityContextActivity.class.getSimpleName();
	
	@Override
	protected Fragment createFragment() {
		return EditAcitityContextFragment.newInstance();
	}
}
