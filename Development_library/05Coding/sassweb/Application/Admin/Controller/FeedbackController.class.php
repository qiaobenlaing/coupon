<?php
/**
 * Feedback Controller
 *
 * User: jiangyufeng
 * Date: 2015-05-19
 */
namespace Admin\Controller;
use Common\Model\CommunicationModel;
use Common\Model\UserModel;
use Common\Model\ShopModel;
use Org\FirePHPCore\FP;


class FeedbackController extends AdminBaseController {

    /**
     * 检查是否有新的未读消息
     */
    public function checkNewMsg() {
        if(IS_AJAX) {
            $cMdl = new CommunicationModel();
            $count = $cMdl->checkPlatMsg();
            if($count === false) {
                $this->ajaxError();
            } else {
                $this->ajaxSucc($count);
            }
        }
    }
    /**
     * 获得商家反馈
     */
    public function listShopFeedback() {
        $cMdl = new CommunicationModel();
        $feedback = $cMdl->getMsgGroup('userCode', C('HQ_CODE'), 'shopCode', $this->pager);
        foreach($feedback as $k=>$v){
            $feedback[$k]['unreadCount'] = $cMdl->countUnreadMsg(C('HQ_CODE'), $v['shopCode'], C('COMMUNICATION_APP.SHOP'));
        }
        $totalNum = $cMdl->countMsgGroup('userCode', C('HQ_CODE'), 'shopCode');
        $this->pager->setItemCount($totalNum);
        $assign = array(
            'title' => '商家建议与反馈',
            'dataList' => $feedback,
            'nowTime' => time(),
            'hqCode' => C('HQ_CODE'),
            'get' => I('get.'),
            'pager' => $this->pager,
            'tableHeader' => array('sender' => '商家名称', 'number' => '商家联系方式', 'url' => 'getShopFeedback?shopCode=')
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display('Feedback/listFeedback');
        } else {
            $html = empty($feedback) ? '' : $this->fetch('Feedback:listFeedbackWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 获得用户反馈
     */
    public function listUserFeedback() {
        $cMdl = new CommunicationModel();
        $shopMdl = new ShopModel();
        $feedback = $cMdl->getMsgGroup('shopCode', C('HQ_CODE'), 'userCode', $this->pager);
        foreach($feedback as $k=>$v){
            $feedback[$k]['unreadCount'] = $cMdl->countUnreadMsg($v['userCode'], C('HQ_CODE'), C('COMMUNICATION_APP.USER'));
            $feedback[$k]['errorImg'] = explode('|', $v['errorImg']);
            array_pop($feedback[$k]['errorImg']);
            $errorShopName = $shopMdl->getShortDes($v['toShopCode']);
            $feedback[$k]['errorShopName'] = $errorShopName['shopName'];
        }
        $totalNum = $cMdl->countMsgGroup('shopCode', C('HQ_CODE'), 'userCode');
        $this->pager->setItemCount($totalNum);
        $assign = array(
            'title' => '用户建议与反馈',
            'dataList' => $feedback,
            'nowTime' => time(),
            'hqCode' => C('HQ_CODE'),
            'get' => I('get.'),
            'pager' => $this->pager,
            'tableHeader' => array('sender' => '用户昵称', 'number' => '手机号码', 'url' => 'getUserFeedback?userCode=')
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display('Feedback/listFeedback');
        } else {
            $html = empty($feedback) ? '' : $this->fetch('Feedback:listFeedbackWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    public function getShopFeedback() {
        $cMdl = new CommunicationModel();
        $shopCode = I('get.shopCode');
        $type = I('get.type');
        $cMdl->readMsg(C('HQ_CODE'), $shopCode, C('COMMUNICATION_APP.SHOP'));
        $msgList = $cMdl->getMsg(C('HQ_CODE'), $shopCode, $this->pager);
        $totalCount = $cMdl->getMsgCount(C('HQ_CODE'), $shopCode);
        $this->pager->setItemCount((int)$totalCount);
        $this->assign(array('shopCode'=>$shopCode, 'type'=>$type, 'feedback'=>$msgList, 'title'=>'反馈详情', 'pager' => $this->pager));
        if (!IS_AJAX) {
            $this->display('Feedback/getFeedback');
        } else {
            $html = empty($msgList) ? '' : $this->fetch('Feedback:getFeedbackWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 获得用户反馈详情
     */
    public function getUserFeedback() {
        $cMdl = new CommunicationModel();
        $shopMdl = new ShopModel();
        $userCode = I('get.userCode'); // 用户编码
        $type = I('get.type'); // 反馈者类型，用户或者商户
        // 将未读消息的阅读状态改为已读
        $cMdl->readMsg($userCode, C('HQ_CODE'), C('COMMUNICATION_APP.USER'));
        // 获得消息列表，分页
        $msgList = $cMdl->getMsg($userCode, C('HQ_CODE'), $this->pager);
        foreach($msgList as $k=>$v){
            $msgList[$k]['errorImg'] = explode('|', $v['errorImg']);
            array_pop($msgList[$k]['errorImg']);
            $errorShopName = $shopMdl->getShortDes($v['toShopCode']);
            $msgList[$k]['errorShopName'] = $errorShopName['shopName'];
        }
        $totalCount = $cMdl->getMsgCount($userCode, C('HQ_CODE'));
        $this->pager->setItemCount((int)$totalCount);
        // 获得用户信息
        $userMdl = new UserModel();
        $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('mobileNbr', 'nickName'));
        $assign = array(
            'userCode' => $userCode,
            'userInfo' => $userInfo,
            'type' => $type,
            'feedback' => $msgList,
            'title' => '用户' . $userInfo['mobileNbr'] . '的反馈详情',
            'pager' => $this->pager
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display('Feedback/getFeedback');
        } else {
            $html = empty($msgList) ? '' : $this->fetch('Feedback:getFeedbackWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    public function replyFeedback() {
        $cMdl = new CommunicationModel();
        if(IS_AJAX) {
            $targetCode = I('post.targetCode');
            $type = I('post.type');
            $message = I('post.message');
            if($type == 'SHOP'){
                $ret = $cMdl->sendMsg($targetCode, C('HQ_CODE'), '', $message, 1);
            }else{
                $ret = $cMdl->sendMsg(C('HQ_CODE'), $targetCode, '', $message, 0);
            }
            if($ret['code'] === C('SUCCESS')) {
                $this->ajaxSucc();
            } else {
                $this->ajaxError($ret);
            }
        }
    }

}