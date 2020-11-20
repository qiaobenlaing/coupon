<?php
/**
 * Created by PhpStorm.
 * User: Huafei Ji
 * Date: 15-7-17
 * Time: 下午4:15
 */
namespace Admin\Controller;
use Common\Model\UserBonusModel;
use Common\Model\UserConsumeModel;
use Org\FirePHPCore\FP;


class UserBonusController extends AdminBaseController {

    public function shopInfo() {
        $shopCode = I('get.shopCode');
        $this->assign('shopCode', $shopCode);

    }

    /**
     * 获得用户红包列表
     */
    public function listUserBonus() {
        $this->assign('title', '用户红包信息');
        $this->display();
    }

    public function listGrabBonus(){
        $userBonusMdl = new UserBonusModel();
        $grabBonusList = $userBonusMdl->listUserBonus(I('get.'), $this->pager);
        $grabBonusCount = $userBonusMdl->countUserBonus(I('get.'));
        $this->pager->setItemCount($grabBonusCount);
        $assign = array(
            'dataList' => $grabBonusList,
            'get' => I('get.'),
            'pager' => $this->pager
        );
        $assign['get']['action'] = 'listGrabBonus';
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display('listUserBonus');
        } else {
            $html = empty($grabBonusList) ? '' : $this->fetch('UserBonus:listGrabBonusWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    public function listUseBonus(){
        $userConsumeMdl = new UserConsumeModel();
        $useBonusList = $userConsumeMdl->listBonusConsume(I('get.'), $this->pager);
        $useBonusCount = $userConsumeMdl->countBonusConsume(I('get.'));
        $this->pager->setItemCount($useBonusCount);
        $assign = array(
            'dataList' => $useBonusList,
            'get' => I('get.'),
            'pager' => $this->pager
        );
        $assign['get']['action'] = 'listUseBonus';
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display('listUserBonus');
        } else {
            $html = empty($useBonusList) ? '' : $this->fetch('UserBonus:listUseBonusWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 获得某个用户红包列表
     */
    public function getUserBonusList() {
        $userBonusMdl = new UserBonusModel();
        $userBonusList = $userBonusMdl->listUserBonus(I('get.'), $this->pager);
        $userBonusCount = $userBonusMdl->countUserBonus(I('get.'));
        $this->pager->setItemCount($userBonusCount);
        $assign = array(
            'title' => '用户红包列表',
            'dataList' => $userBonusList,
            'get' => I('get.'),
            'pager' => $this->pager
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($userBonusList) ? '' : $this->fetch('UserBonus:UserInfoBonusWidget');
            $this->ajaxSucc('', null, $html);
        }
    }
}