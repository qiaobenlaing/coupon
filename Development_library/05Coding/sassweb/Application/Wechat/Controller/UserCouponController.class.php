<?php
namespace Wechat\Controller;
use Common\Model\BatchCouponModel;
use Common\Model\CardModel;
use Common\Model\UserCardModel;
use Common\Model\UserModel;
use Common\Model\wxApiModel;
use Think\Controller;
use Common\Model\UserCouponModel;
/**
 * Created by PhpStorm.
 * User: Huafei Ji
 * Date: 15-7-1
 * Time: 上午11:42
 */
class UserCouponController extends WechatBaseController {

    /**
     * @var $wxApi wxApiModel
     */
    private $wxApi;

    public function _initialize(){
        parent::_initialize();
        $this->wxApi = new wxApiModel();
    }

    /**
     * 用户获得优惠券
     */
    public function getVirtualCoupon() {
        if(IS_AJAX) {
            $data = I('post.');
            $batchCouponCode = $data['batchCouponCode'];
            $userMdl = new UserModel();
            // 检查手机号码是否合法
            $mobileNbr = $data['mobileNbr'];
            $pattern= '/[A-Za-z]/';
            if(empty($mobileNbr) || strlen($mobileNbr) != 11 || preg_match($pattern, $mobileNbr) == 1) {
                $this->ajaxError('请输入正确手机号码');
            }
            // 检查用户是否注册
            $isUserRegisted = $userMdl->isUserRegisted($data['mobileNbr']);
            if($isUserRegisted['ret'] === true) {
                $userCode = $isUserRegisted['userCode'];
                $batchCouponMdl = new BatchCouponModel();
                $batchCouponInfo = $batchCouponMdl->getCouponInfo($batchCouponCode);
                if(!$batchCouponInfo['batchCouponCode']) {
                    $this->ajaxError(C('COUPON.NOT_EXIST'));
                }
                $shopCode = $batchCouponInfo['shopCode'];
                // 判断用户是否拥有发券商家的会员卡。若没有，则添加
                $userCardMdl = new UserCardModel();
                $ret = $userCardMdl->isUserHasShopCard($userCode, $shopCode);
                $cardMdl = new CardModel();
                $cardInfo = $cardMdl->getCardInfoByShopCodeAndCardLvl($shopCode, $cardLvl = C('CARD_LVL.WORST'));
                if($ret == false && $cardInfo) {
                    $ret = $userCardMdl->applyCard($cardInfo['cardCode'], $userCode);
                    if($ret['code'] == C('API_INTERNAL_EXCEPTION')) {
                        $this->ajaxError('操作失败');
                    }
                }
                $userCouponMdl = new UserCouponModel();
                $ret = $userCouponMdl->grabCoupon($batchCouponCode, $userCode, $sharedLvl = C('COUPON_SHARED_LVL.ALL'));
                if($ret['code'] === true || $ret['code'] == C('COUPON.LIMIT')) {
                    $this->ajaxSucc($ret['code']);
                } else {
                    $this->ajaxError($ret['code']);
                }
            } else {
                // 添加未注册的用户
                $userMdl = new UserModel();
                $addNotRegisteredUserRet = $userMdl->addNotRegisteredUser(array('userId' => $mobileNbr, 'mobileNbr' => $mobileNbr, 'status' => \Consts::USER_STATUS_NOT_REG));
                // 添加用户优惠券，状态冷冻
                $userCouponMdl = new UserCouponModel();
                $ret = $userCouponMdl->addFrozenCoupon($addNotRegisteredUserRet['userCode'], $batchCouponCode);
                if($ret['code'] == true || $ret['code'] == C('COUPON.LIMIT')) {
                    $this->ajaxSucc($ret['code']);
                } else {
                    $this->ajaxError($ret['code']);
                }

            }
        }
    }

    /**
     * 分享用户优惠券
     */
    public function share() {
        $userCouponMdl = new UserCouponModel();
        $this->assign('title', '用户的优惠券分享');
        $userCouponCode = I('get.userCouponCode');
        $userCouponInfo = $userCouponMdl->getCouponInfo($userCouponCode);
        $this->assign('userCouponInfo', $userCouponInfo);
        $wxJsPackage = $this->wxApi->getSignPackage();
        foreach($wxJsPackage as $k=>$v) {
            $this->assign($k, $v);
        }
        $this->display();
    }

}