<?php
/**
 * Coupon Controller
 *
 * User: jiangyufeng
 * Date: 2015-05-19
 */
namespace Admin\Controller;
use Common\Model\UserActionLogModel;
use Org\FirePHPCore\FP;

class UserActionLogController extends AdminBaseController {
    
    /**
     * 平台操作员维护
     */
    public function listUserActionLog() {
        $userActionLogMdl = new UserActionLogModel();
        $this->assign('title', '平台操作员维护');
        $listUserActionLog = $userActionLogMdl->listUserActionLog(I('get.'), $this->pager);
        $this->assign('dataList', $listUserActionLog);
        $this->assign('get', I('get.'));
        $this->assign('nowTime', time());
        $countUserActionLog = $userActionLogMdl->countUserActionLog(I('get.'));
        $this->pager->setItemCount($countUserActionLog);
        $this->assign('pager', $this->pager);
        if (!IS_AJAX) {
			$this->display();
		} else {
			$html = empty($listUserActionLog) ? '' : $this->fetch('UserActionLog:listUserActionLogWidget');
	// 		FP::dbg($html);
			$this->ajaxSucc('', null, $html);
		}
    }
    
    /**
     * 操作日志查看
     */
    public function listActionLog() {
        $userActionLogMdl = new \Common\Model\UserActionLogModel();
        $this->assign('title', '操作日志查看');
        $listActionLog = $userActionLogMdl->listActionLog(I('get.'), $this->pager);
        $this->assign('dataList', $listActionLog);
        $this->assign('get', I('get.'));
        $this->assign('nowTime', time());
        $countActionLog = $userActionLogMdl->countActionLog(I('get.'));
        $this->pager->setItemCount($countActionLog);
        $this->assign('pager', $this->pager);
        if (!IS_AJAX) {
			$this->display();
		} else {
			$html = empty($listActionLog) ? '' : $this->fetch('UserActionLog:listActionLogWidget');
	// 		FP::dbg($html);
			$this->ajaxSucc('', null, $html);
		}
    }
} 
