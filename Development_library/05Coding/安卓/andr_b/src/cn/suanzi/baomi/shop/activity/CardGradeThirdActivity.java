// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// 
package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.CardGradeThirdFragment;

/**
 * 会员卡等级设置第三张卡
 * @author yanfang.li
 */
public class CardGradeThirdActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		
		return CardGradeThirdFragment.newInstance();
	}
	
}
