<?php
/**
 * Created by PhpStorm.
 * User: Huafei Ji
 * Date: 16-3-3
 * Time: 下午2:36
 */
namespace Admin\Controller;

use Common\Model\UserConsumeModel;
use Common\Model\UserCouponModel;
use Common\Model\UtilsModel;

class UserConsumeController extends AdminBaseController {

    /**
     * 获得账单
     */
    public function listUserConsume() {
        $userConsumeMdl = new UserConsumeModel();
        $userConsumeList = $userConsumeMdl->listUserConsume(I('get.'), $this->pager, '', array('ConsumeOrder.orderAmount', 'shopName', 'User.mobileNbr' => 'userMobileNbr','User.userCode','User.nickName','User.realName','User.bank_id','deduction', 'realPay', 'platBonus', 'shopBonus', 'UserConsume.status' => 'userConsumeStatus', 'consumeCode', 'couponUsed', 'firstDeduction', 'usedUserCouponCode', 'consumeTime', 'realPay', 'UserConsume.payedTime', 'UserConsume.subsidySettlementStatus', 'UserConsume.couponUsed'));
        // 计算平台补贴金额
        $userCouponMdl = new UserCouponModel();
        foreach($userConsumeList as $k => $userConsume) {
            $couponPlatSubsidy = 0;
            if($userConsume['usedUserCouponCode']) {
                $usedUserCouponCodeArr = explode('|', $userConsume['usedUserCouponCode']);
                foreach($usedUserCouponCodeArr as $userCouponCode) {
                    $userCouponInfo = $userCouponMdl->getUserCouponInfoB(array('userCouponCode' => $userCouponCode), array('BatchCoupon.hqSubsidy', 'BatchCoupon.couponType'));
                    if($userCouponInfo['couponType'] == \Consts::COUPON_TYPE_DISCOUNT) {
                        $couponPlatSubsidy += $userConsume['orderAmount'] * $userCouponInfo['hqSubsidy']; // 优惠券平台补贴金额，单位：分
                    } else {
                        $couponPlatSubsidy += $userCouponInfo['hqSubsidy']; // 优惠券平台补贴金额，单位：分
                    }
                }
            }
            $userConsumeList[$k]['bank_name'] = getName("Bank","name","id='".$userConsumeList[$k]['bank_id']."'");
            $userConsumeList[$k]['hqSubsidy'] = $couponPlatSubsidy / \Consts::HUNDRED + $userConsume['platBonus'] + $userConsume['firstDeduction'];
        }

        $userConsumeCount = $userConsumeMdl->countUserConsume(I('get.'));
        $this->pager->setItemCount($userConsumeCount);
        $assign = array(
            'title' => '惠圈账单',
            'dataList' => $userConsumeList,
            'get' => I('get.'),
            'pager' => $this->pager,
        );
        $this->assign($assign);
        if(!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($userConsumeList) ? '' : $this->fetch('UserConsume:listUserConsumeWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 结算惠圈的补贴
     */
    public function settleHqSubsidy() {
        if(IS_AJAX) {
            $data = I('post.');
            $userConsumeMdl = new UserConsumeModel();
            $ret = $userConsumeMdl->settleHqSubsidy($data);
            if($ret['code'] === C('SUCCESS')) {
                $this->ajaxSucc($ret);
            } else {
                $this->ajaxError('操作失败，请重试');
            }
        } else {
            redirect('/Admin');
        }
    }
}