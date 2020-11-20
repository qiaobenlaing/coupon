<?php
namespace Wechat\Controller;
use Think\Controller;
use Common\Model\wxApiModel;
use Common\Model\BonusModel;
/**
 * Created by PhpStorm.
 * User: Huafei Ji
 * Date: 15-7-1
 * Time: 上午11:10
 */
class BonusController extends WechatBaseController {
    /**
     * @var $wxApi wxApiModel
     */
    private $wxApi;

    public function _initialize(){
        parent::_initialize();
        $this->wxApi = new wxApiModel();
    }

    /**
     * 分享抢红包
     */
    public function share(){
        $bonusMdl = new BonusModel();
        $bonusCode = I('get.bonusCode');
        $bonusInfo = $bonusMdl->getBonusInfo($bonusCode);
        $wxJsPackage = $this->wxApi->getSignPackage();
        $assign = $wxJsPackage;
        $assign['title'] = '红包活动分享';
        $assign['bonusInfo'] = $bonusInfo;
        $this->assign($assign);
        $this->display();
    }
}