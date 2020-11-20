// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// 
package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.CardGradeSecondFragment;

import android.app.Fragment;

/**
 * 会员卡等级设置第二张卡
 * @author yanfang.li
 */
public class CardGradeSecondActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		
		return CardGradeSecondFragment.newInstance();
	}
	
}
