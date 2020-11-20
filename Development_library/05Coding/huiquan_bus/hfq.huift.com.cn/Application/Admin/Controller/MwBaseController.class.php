<?php
namespace Admin\Controller;
use Think\Controller;
use Common\Model\Pager;
use Org\FirePHPCore\FP;

/**
 * Mirco Web(MW,微网站)前后端Controller基类
 * 
 * @author Weiping
 * @version 1.0.0
 */
class MwBaseController extends Controller {

    /** @var array 当前登录用户, 如果未登录为null, 否则为用户信息(不带密码)的一维数组array(k => v) */
    protected $user;
    /** @var  Pager 分页参数 */
	protected $pager;
	
	/**
	 * Controller初始化。
	 * @deprecated 在TP3.2中已不推荐使用。
	 */
	protected function _initialize() {
		// 模板处理
// 		$referer = $_SERVER['HTTP_REFERER'];
// 		// 没有Referer或者不是从应用内部链接（suanzi.cn或knowway.com或zkt.cn域名）过来的，显示侧滑菜单
// 		if (!$referer ||
// 				(stripos($referer, 'suanzi.cn') === false
// 						&& stripos($referer, 'knowway.com') === false
// 						&& stripos($referer, 'zkt.cn') === false)) {
// 			$this->assign('layout', 'layoutwechat');
// 		}

		// 翻页处理
		$this->pager = new Pager(I('page', 1, 'intval'), I('pageSize', Pager::DEFUALT_PAGE_SIZE, 'intval'));
	} 
	
	/**
	 * Ajax成功返回.
	 * @param string $msg [可选] 成功消息， 
	 * @param mixed $data [可选] 返回的数据，可为任意类型。
	 * @param string $html [可选] 返回的HTML.
	 */
	protected function ajaxSucc($msg = '', $data = null, $html = '') {
		$this->ajaxReturn(array(
				'status' => 0,
				'msg' => $msg,
				'data' => $data,
				'html' => $html
		));
	}

	/**
	 * Ajax失败返回.
	 * @param string $msg [可选] 成功消息，
	 * @param mixed $data [可选] 返回的数据，可为任意类型。
	 * @param string $html [可选] 返回的HTML.
	 * @param number status [可选] 失败代码。默认为-1. 
	 */
	protected function ajaxError($msg = '', $data = null, $html = '', $status = -1) {
		$this->ajaxReturn(array(
				'status' => $status,
				'msg' => $msg,
				'data' => $data,
				'html' => $html
		));
	}
	 
}