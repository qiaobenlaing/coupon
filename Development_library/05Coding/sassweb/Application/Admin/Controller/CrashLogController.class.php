<?php
namespace Admin\Controller;
use Common\Model\CrashLogModel;

/**
 * Created by PhpStorm.
 * User: Huafei Ji
 * Date: 15-9-21
 * Time: 下午4:44
 */
class CrashLogController extends AdminBaseController{

    /**
     * 获得顾客端app崩溃日志列表
     */
    public function listClientCrashLog() {
        $crashLogMdl = new CrashLogModel();
        $crashLogList = $crashLogMdl->listClientCrashLog(I('get.'), $this->pager);
        $crashLogCount = $crashLogMdl->countClientCrashLog(I('get.'));
        $this->pager->setItemCount($crashLogCount);
        $assign = array(
            'title' => '顾客端app崩溃日志列表',
            'dataList' => $crashLogList,
            'get' => I('get.'),
            'pager' => $this->pager
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($crashLogList) ? '' : $this->fetch('CrashLog:listClientCrashLogWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 获得商家端app崩溃日志列表
     */
    public function listShopCrashLog() {
        $crashLogMdl = new CrashLogModel();
        $crashLogList = $crashLogMdl->listShopCrashLog(I('get.'), $this->pager);
        $crashLogCount = $crashLogMdl->countShopCrashLog(I('get.'));
        $this->pager->setItemCount($crashLogCount);
        $assign = array(
            'title' => '顾客端app崩溃日志列表',
            'dataList' => $crashLogList,
            'get' => I('get.'),
            'pager' => $this->pager
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($crashLogList) ? '' : $this->fetch('CrashLog:listShopCrashLogWidget');
            $this->ajaxSucc('', null, $html);
        }
    }
}