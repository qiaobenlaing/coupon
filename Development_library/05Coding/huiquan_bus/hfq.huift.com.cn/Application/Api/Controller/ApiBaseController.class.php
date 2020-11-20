<?php
namespace Api\Controller;
use Think\Controller\JsonRpcController;
use Common\Model\Pager;
use Org\FirePHPCore\FP;

/**
 * Api Controller的基类
 * @package Common\Controller
 * @author Weiping
 */
class ApiBaseController extends JsonRpcController {
	/**
	 * 架构函数
	 * @access public
	 */
	public function __construct() {
		parent::__construct();
		// 确保jsonrpc后面不被挂上一堆调试信息。
		C('SHOW_PAGE_TRACE', false);
       // $this->pager = new Pager(I('page', 1, 'intval'), I('pageSize', Pager::DEFUALT_PAGE_SIZE, 'intval'));
	}

    /**
     * 获得页数对象
     * @param int $page 页数
     * @param int $pagesize 页面大小
     * @return Object Pager
     */
    public function getPager($page, $pagesize = 0) {
        if(empty($page)) $page = 0;
        $pagesize = empty($pagesize) ? C('PAGESIZE') : $pagesize;
        return new Pager($page, $pagesize);
    }
}