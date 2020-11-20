<?php
/**
 * Coupon Controller
 *
 * Staff: jiangyufeng
 * Date: 2015-05-19
 */
namespace Admin\Controller;
use Common\Model\StaffActionLogModel;
use Org\FirePHPCore\FP;

class StaffActionLogController extends AdminBaseController {
    
    /**
     * 操作日志查看
     */
    public function listActionLog() {
        $staffActionLogMdl = new StaffActionLogModel();
        $this->pager->setPageSize(30);
        $listActionLog = $staffActionLogMdl->listActionLog(I('get.'), $this->pager);
        $countActionLog = $staffActionLogMdl->countActionLog(I('get.'));
        $this->pager->setItemCount($countActionLog);
        $assign = array(
            'title' => '操作日志查看',
            'dataList' => $listActionLog,
            'get' => I('get.'),
            'nowTime' => time(),
            'pager' => $this->pager,
        );
        $this->assign($assign);
        if (!IS_AJAX) {
			$this->display();
		} else {
			$html = empty($listActionLog) ? '' : $this->fetch('StaffActionLog:listActionLogWidget');
			$this->ajaxSucc('', null, $html);
		}
    }
    
    public function editStaffActionLog() {
        $BmStaffMdl = new \Common\Model\BmStaffModel();
    }
    
    /**
     * 操作日志删除
     */
    public function delActionLog() {
        $staffActionLogMdl = new \Common\Model\StaffActionLogModel();
        $logCode =I('get.logCode');
        $res = $staffActionLogMdl->delActionLog($logCode);
        if ($res === true) {
            $this->redirect('/Admin/StaffActionLog/listActionLog');
        } else {
            $this->ajaxError($res);
        }
    }
} 

