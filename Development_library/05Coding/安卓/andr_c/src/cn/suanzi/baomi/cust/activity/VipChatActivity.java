// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// 
package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.VipChatFragment;

/**
 * 会员和管理员交流的界面
 * @author yanfang.li
 */
public class VipChatActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return VipChatFragment.newInstance();
	}
	
}
