<?php
namespace Admin\Controller;
use Common\Model\StaffActionLogModel;
use Org\FirePHPCore\FP;

/**
 * Admin模块Controller基类。
 *
 * @author Weiping
 * @version 1.0.0
 */
class AdminBaseController extends MwBaseController {
	   public function __construct() {
        parent::__construct();
        // 确保jsonrpc后面不被挂上一堆调试信息。
        C('SHOW_PAGE_TRACE', false);
//        $this->pager = new Pager(I('page', 1, 'intval'), I('pageSize', Pager::DEFUALT_PAGE_SIZE, 'intval'));
    }

	/**
	 * Controller初始化。
	 * @deprecated 在TP3.2中已不推荐使用。
	 */
	protected function _initialize() {
		parent::_initialize();
        $menuUrl = '/' . MODULE_NAME . '/' . CONTROLLER_NAME . '/' . ACTION_NAME;
        //记录操作日志
        if(!in_array($menuUrl, C('NO_LOG_ACTION'))) {
            $getParams = I('get.');
            $postParams = I('post.');
            if($postParams) {
                $actionDes = C('ACTION_POST.'.$menuUrl);
            } else {
                $actionDes = C('ACTION_GET.'.$menuUrl);
            }

            $staffInfo = session('USER');
            if($staffInfo) {
                $actionData = array(
                    'staffCode' => $staffInfo['staffCode'],
                    'actionDes' => $actionDes
                );
                $StaffActionLogMdl = new StaffActionLogModel();
                $StaffActionLogMdl->addActionLog($actionData);
            }
        }

		// 登录验证
		//if (!APP_DEBUG) {
		$action = CONTROLLER_NAME . ':' . ACTION_NAME;
		 if($_SERVER['PATH_INFO']!='TestClient/getUserinfo'){
                if (!in_array($action, C('PUBLIC_ACTONS')) && !session('?USER')) {
                    $this->redirect('/Admin/BmStaff/login');
                } else if (session('?USER')) {
                    $this->user = session('USER');
                    $this->assign('user', session('USER'));
                }
		 }

        // menu和当前页面url
        if (!IS_AJAX) {
            $userInfo = $this->user;
            switch($userInfo['userLvl']) {
                case \Consts::HQ_STAFF_TYPE_ADMIN: // 管理员
                    $menu = C('MENU'); // 选择管理员菜单

                    //判断是否为惠圈人员 （乔本亮修改）
                    if($_SESSION['USER']['bank_id']!=-1){
                        foreach ($menu as $item => &$value){
                           if($value['id']=='bank' || $value['id'] == 'plat'){
                              unset($menu[$item]);
                           }
                        }
                    }


                    break;
                case \Consts::HQ_STAFF_TYPE_GROUND_PERSON: // 地推人员
                    $menu = C('GROUND_PERSON_MENU'); // 选择地推人员菜单
                    break;
                default:
                    $menu = C('GROUND_PERSON_MENU'); // 默认选择地推人员菜单
                    break;
            }

            $this->assign('menu', $menu);
            $this->assign('menuUrl', $menuUrl);
        }
		//}
	}

    /**
     * 拼接上一页的url的参数
     * @param array $data 一维关联数组
     * @param array $filterData 需要去掉字段
     * @return string
     */
    protected function setBackUrlParam($data, $filterData) {
        foreach($filterData as $v) {
            unset($data[$v]);
        }
        $urlParam = '';
        foreach($data as $k => $v) {
            $urlParam .= "$k=$v&";
        }
        return $urlParam;
    }

    /**
     * 修改列表中数字字段成文字字段
     * @author chenghao.feng
     * @param Array $map_keys 替换的字段 [字段名[数字 => 文字]]
     * @param Array $list 需要被修改字段的列表
     * @return mixed
     */
    protected function mapLists($map_keys, $list){
    	foreach ($list as $key => $item) {              //循环数据集，增加任务等级、状态、种类的中文字段
    		foreach($item as $map_key => $val){
    			if(isset($map_keys[$map_key])){
    				$list[$key][$map_key] = isset( $map_keys[$map_key][$val]) ? $map_keys[$map_key][$val] : '';
    			}
    		}
    	}
    	return $list;
    }

}
