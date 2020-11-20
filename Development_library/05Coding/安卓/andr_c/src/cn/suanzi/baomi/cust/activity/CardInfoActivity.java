package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.CardInfoFragment;

/**
 * 会员卡列表
 * @author qian.zhou
 */
public class CardInfoActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return CardInfoFragment.newInstance();
	}
}
