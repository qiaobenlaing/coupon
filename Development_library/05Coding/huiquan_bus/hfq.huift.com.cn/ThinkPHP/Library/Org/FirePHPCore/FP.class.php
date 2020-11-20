<?php
namespace Org\FirePHPCore;

use Org\FirePHPCore\FirePHP;

/**
 * FirePHP静态版
 *
 * @author Weiping
 * @version 1.0.0
 */
class FP {
	/**
	 * 打印调试信息到FirePHP
	 * @param unknown $obj
	 */
	static function dbg() {
		if (APP_DEBUG) {
			$firephp = FirePHP::getInstance(true);
			//$firephp->fb($obj); /* Defaults to FirePHP::LOG */
			$args = func_get_args();
			return call_user_func_array(array($firephp, 'fb'), $args);
		}
	}
}