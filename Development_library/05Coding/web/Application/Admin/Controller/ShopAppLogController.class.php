<?php
/**
 * Created by PhpStorm.
 * User: Huafei Ji
 * Date: 15-7-23
 * Time: 下午5:25
 */
namespace Admin\Controller;
use Common\Model\ShopAppLogModel;
class ShopAppLogController extends AdminBaseController {

    /**
     * 获取更新列表
     */
    public function listShopAppLog() {
        $shopAppLogMdl = new ShopAppLogModel();
        $cardList = $shopAppLogMdl->listUpdateLog(I('get.'), $this->pager);
        $cardCount = $shopAppLogMdl->countUpdatelog(I('get.'));
        $this->pager->setItemCount($cardCount);
        $assign = array(
            'title' => '商家端APP更新记录',
            'dataList' => $cardList,
            'get' => I('get.'),
            'pager' => $this->pager
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($cardList) ? '' : $this->fetch('Card:listCardWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 编辑更新记录
     */
    public function editShopAppLog() {
        $shopAppLogMdl = new ShopAppLogModel();
        if(IS_GET) {
            $logCode = I('get.logCode');
            $logInfo = $shopAppLogMdl->getUpdateLogInfo($logCode);
            $assign = array(
                'title' => '编辑商家端APP更新记录',
                'logInfo' => $logInfo,
                'page' => I('get.page'),
            );
            $this->assign($assign);
            $this->display();
        }else {
            $data = I('post.');
            $userInfo = session('USER');
            $staffCode = $userInfo['staffCode'];
            $data['creatorCode'] = $staffCode;
            $data['createTime'] = date('Y-m-d H:i:s');
            $ret = $shopAppLogMdl->editUpdateLog($data);
            if ($ret === true) {
                $this->ajaxSucc('添加成功');
            } else {
                $this->ajaxError($ret);
            }
        }
    }
}