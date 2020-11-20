<?php
namespace Common\Behavior;

/**
 * Layout行为类，用于控制Action View对应的模板
 *
 * @author Weiping
 * @version 1.0.0
 */
class LayoutBehavior extends \Think\Behavior {
	
	/**
	 * 行为执行入口
	 * 
	 * (non-PHPdoc)
	 * @see \Think\Behavior::run()
	 */
	public function run(&$param) {
		// 没有Referer或者不是从应用内部链接（suanzi.cn或knowway.com域名）过来的，显示侧滑菜单
		if (!$_SERVER['HTTP_REFERER'] ||
				(strpos($_SERVER['HTTP_REFERER'], 'suanzi.cn') === false
						&& strpos($_SERVER['HTTP_REFERER'], 'knowway.com') === false
						&& strpos($_SERVER['HTTP_REFERER'], 'zkt.cn') === false)) {
			//显示侧滑菜单
		}
	}
}