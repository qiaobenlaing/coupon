<?php
namespace Wechat\Controller;
use Think\Controller;
use Common\Model\Pager;
use Think\Exception;

/**
 * 微信端Controller基类
 * 
 * @author Weiping Liu
 * @version 1.0.0
 */
class WechatBaseController extends Controller {

    /** @var array 当前登录用户, 如果未登录为null, 否则为用户信息(不带密码)的一维数组array(k => v) */
    protected $user;

	/**
	 * Controller初始化。
	 * @deprecated 在TP3.2中已不推荐使用。
	 */
	protected function _initialize() {
		\Think\Log::record('WechatBaseController::HTTP_USER_AGENT: ' . $_SERVER['HTTP_USER_AGENT'], 'DEBUG');
		// 非微信浏览器，模拟用户进行测试
		if ( strpos($_SERVER['HTTP_USER_AGENT'], 'MicroMessenger') === false ) {

		} else {

		}
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
	 * @param int $status [可选] 失败代码。默认为-1.
	 */
	protected function ajaxError($msg = '', $data = null, $html = '', $status = -1) {
		$this->ajaxReturn(array(
				'status' => $status,
				'msg' => $msg,
				'data' => $data,
				'html' => $html
		));
	}

    /**
     * 获得页数对象
     * @param int $page 页数
     * @return Object Pager
     */
    public function getPager($page){
        if(! isset($page) || $page === '')
            $page = 1;
        return new Pager($page, C('PAGESIZE'));
    }
	 
}