<?php
namespace Api\Controller;
use Common\Model\BatchCouponModel;
use Common\Model\ConsumeOrderModel;
use Common\Model\DistrictModel;
use Common\Model\JpushModel;
use Common\Model\OrderCouponModel;
use Common\Model\ShopModel;
use Common\Model\UserCouponModel;
use Common\Model\UserModel;
use Common\Model\UtilsModel;
use Think\Controller;
use Think\Log;

/**
 * Created by PhpStorm.
 * User: huafei.ji
 * Date: 15-9-9
 * Time: 下午4:15
 */
class RefreshController extends Controller {
    /**
     * 更新所有商家的虚拟人气值，每天23点59分执行
     */
    public function updateShopVirtualPopularity() {
        $shopMdl = new ShopModel();
        $shopMdl->updateShopVirtualPopularity();
    }

    /**
     * 检查冻结状态的优惠券，每分钟检查一次
     */
    public function checkFrozenCoupon() {
        $userCouponMdl = new UserCouponModel();
        $userCouponMdl->checkFrozenCoupon();
    }

    /**
     * 检查已下单的外卖订单和堂食订单，商户是否在10分钟内接单，若没有接单，则自动取消订单。每分钟检查一次
     */
    public function checkOrderedOrder() {
        $consumeOrderMdl = new ConsumeOrderModel();
        $consumeOrderMdl->checkOrderedOrder();
    }

    /**
     * 检查已接单且（已支付或者未支付或者等待支付）的外卖订单和堂食订单。超过接单时间3小时30分钟的，自动修改订单状态为已送达。每分钟检查一次
     */
    public function checkDeliveryOrder() {
        $consumeOrderMdl = new ConsumeOrderModel();
        $consumeOrderMdl->checkDeliveryOrder();
    }

    /**
     * “外卖”和“堂食餐前”未支付订单30min后自动取消订单。每分钟检查一次
     */
    public function checkPayBeforeFoodOrder() {
        $consumeOrderMdl = new ConsumeOrderModel();
        $consumeOrderMdl->checkPayBeforeFoodOrder();
    }

    /**
     * 申请退款24小时内未做出回应则订单取消，退款。每分钟检查一次
     */
    public function checkRefundingOrder() {
        $consumeOrderMdl = new ConsumeOrderModel();
        $consumeOrderMdl->checkRefundingOrder();
    }

    /**
     * 给3天后即将过期的买了代金券或兑换券的用户推送通知,10分钟执行一次
     */
    public function pushToBeExpiredMsg(){
        $now = time();
        $startTime = date('Y-m-d H:i:s', $now + 3 * 86400 - 10 * 60 + 1);
        $endTime = date('Y-m-d H:i:s', $now + 3 * 86400);
        $orderCouponMdl = new OrderCouponModel();
        $condition = array(
            'BatchCoupon.expireTime' => array('between', array($startTime, $endTime)),
            'BatchCoupon.payPrice' => array('gt', 0),
            'BatchCoupon.validityPeriod' => -1,
            'BatchCoupon.isAvailable' => C('YES'),
            'BatchCoupon.isSend' => C('NO'),
            'BatchCoupon.couponType' => array('IN', array(\Consts::COUPON_TYPE_EXCHANGE, \Consts::COUPON_TYPE_VOUCHER)),
//            'User.status' => C('USER_STATUS.ACTIVE'),
            'OrderCoupon.status' => \Consts::ORDER_COUPON_STATUS_USE
        );
        $joinTableArr = array(
            array(
                'joinTable' => 'BatchCoupon',
                'joinCondition' => 'BatchCoupon.batchCouponCode = OrderCoupon.batchCouponCode',
                'joinType' => 'inner'
            ),
            array(
                'joinTable' => 'User',
                'joinCondition' => 'User.userCode = OrderCoupon.userCode',
                'joinType' => 'inner'
            ),
            array(
                'joinTable' => 'Shop',
                'joinCondition' => 'Shop.shopCode = BatchCoupon.shopCode',
                'joinType' => 'inner'
            ),
        );
        $userCouponList = $orderCouponMdl->listOrderCoupon($condition, array('User.userCode', 'User.mobileNbr', 'Shop.shopCode', 'Shop.shopName', 'BatchCoupon.couponType', 'BatchCoupon.batchCouponCode', 'OrderCoupon.orderCouponCode'), $joinTableArr);
        $receiver = array();
        foreach($userCouponList as $v){
            if(isset($receiver[$v['userCode']][$v['batchCouponCode']])){
                $receiver[$v['userCode']][$v['batchCouponCode']]['userCount'] += 1;
                $receiver[$v['userCode']][$v['batchCouponCode']]['orderCouponCode'] .= '|'.$v['orderCouponCode'];
            }else{
                $receiver[$v['userCode']][$v['batchCouponCode']] = $v;
                $receiver[$v['userCode']][$v['batchCouponCode']]['userCount'] = 1;
                unset($receiver[$v['userCode']][$v['batchCouponCode']]['batchCouponCode']);
            }
        }
        if($receiver){
            $jpushMdl = new JpushModel(C('CLIENT_APP_KEY'), C('CLIENT_MASTER_SECRET'));
            foreach($receiver as $user){
                foreach($user as $uk => $uv){
                    $content = str_replace('{{userCount}}', $uv['userCount'], C('PUSH_MESSAGE.COUPON_TO_BE_EXPIRED'));
                    $content = str_replace('{{shopName}}', $uv['shopName'], $content);
                    $couponType = $uv['couponType'] == \Consts::COUPON_TYPE_EXCHANGE ? '兑换券' : '代金券';
                    $content = str_replace('{{couponType}}', $couponType, $content);
                    $extra = array(
                        'batchCouponCode' => $uk,
                        'couponType' => $uv['couponType'],
                    );
                    $jpushMdl->jPushByAction(array($uv['mobileNbr']), $content, $extra, C('PUSH_ACTION.COUPON_TO_BE_EXPIRED'));
                }
            }
        }
    }

    /**
     * 给过期的买了代金券或兑换券的用户推送通知和退款,10分钟执行一次
     */
    public function pushRefundMsg(){
        $orderCouponMdl = new OrderCouponModel();
        $condition = array(
            'BatchCoupon.expireTime' => array('elt', date('Y-m-d H:i:s', time())),
            'BatchCoupon.payPrice' => array('gt', 0),
            'BatchCoupon.validityPeriod' => -1,
            'BatchCoupon.isAvailable' => C('YES'),
            'BatchCoupon.isSend' => C('NO'),
            'BatchCoupon.couponType' => array('IN', array(\Consts::COUPON_TYPE_EXCHANGE, \Consts::COUPON_TYPE_VOUCHER)),
//            'User.status' => C('USER_STATUS.ACTIVE'),
            'OrderCoupon.status' => \Consts::ORDER_COUPON_STATUS_USE
        );
        $joinTableArr = array(
            array(
                'joinTable' => 'BatchCoupon',
                'joinCondition' => 'BatchCoupon.batchCouponCode = OrderCoupon.batchCouponCode',
                'joinType' => 'inner'
            ),
            array(
                'joinTable' => 'User',
                'joinCondition' => 'User.userCode = OrderCoupon.userCode',
                'joinType' => 'inner'
            ),
            array(
                'joinTable' => 'Shop',
                'joinCondition' => 'Shop.shopCode = BatchCoupon.shopCode',
                'joinType' => 'inner'
            ),
        );
        $userCouponList = $orderCouponMdl->listOrderCoupon($condition, array('User.userCode', 'User.mobileNbr', 'Shop.shopCode', 'Shop.shopName', 'BatchCoupon.couponType', 'BatchCoupon.batchCouponCode', 'OrderCoupon.orderCouponCode'), $joinTableArr);
        $receiver = array();
        foreach($userCouponList as $v){
            if(isset($receiver[$v['userCode']][$v['batchCouponCode']])){
                $receiver[$v['userCode']][$v['batchCouponCode']]['userCount'] += 1;
                $receiver[$v['userCode']][$v['batchCouponCode']]['orderCouponCode'] .= '|'.$v['orderCouponCode'];
            }else{
                $receiver[$v['userCode']][$v['batchCouponCode']] = $v;
                $receiver[$v['userCode']][$v['batchCouponCode']]['userCount'] = 1;
                unset($receiver[$v['userCode']][$v['batchCouponCode']]['batchCouponCode']);
            }
        }
        if($receiver){
            $jpushMdl = new JpushModel(C('CLIENT_APP_KEY'), C('CLIENT_MASTER_SECRET'));
            foreach($receiver as $user){
                foreach($user as $uv){
                    $orderCouponCodeArr = explode("|", $uv['orderCouponCode']); //TODO 该返回用于退券和退款(用户)
                    $content = str_replace('{{userCount}}', $uv['userCount'], C('PUSH_MESSAGE.PAY_COUPON_REFUND'));
                    $content = str_replace('{{shopName}}', $uv['shopName'], $content);
                    $couponType = $uv['couponType'] == \Consts::COUPON_TYPE_EXCHANGE ? '兑换券' : '代金券';
                    $content = str_replace('{{couponType}}', $couponType, $content);
                    $jpushMdl->jPushByAction(array($uv['mobileNbr']), $content, array(), C('PUSH_ACTION.PAY_COUPON_REFUND'));
                }
            }
        }
    }

    /**
     * 检查未完成的其他类型的订单，若24小时内未完成支付，则自动取消订单
     */
    public function cancelOtherNotPayedOrder() {
        $consumeOrderMdl = new ConsumeOrderModel();
        $consumeOrderMdl->cancelOtherNotPayedOrder();
    }

    /**
     * 将用户手机号归属地是空的记录补上（每天 23：59 执行）
     * @return bool
     */
    public function updateUserAddress(){
        $userMdl = new UserModel();
        $userList = $userMdl->getUserList('mobileBelonging is null OR mobileBelonging = ""', array('userCode', 'province', 'city', 'mobileBelonging', 'mobileNbr'));
        foreach($userList as $v){
            if($v['mobileNbr']){
                $ret = UtilsModel::getPhoneArea($v['mobileNbr']);
                if($ret['ret'] === true){
                    if(empty($v['province']) && empty($v['city'])){
                        $data['province'] = $ret['data']['province'];
                        if(!in_array($ret['data']['province'], array('北京', '上海', '天津', '重庆', ''))){
                            $data['province'] = $ret['data']['province'].'省';
                        }
                        $data['city'] = $ret['data']['city'].'市';
                    }
                    if(in_array($ret['data']['province'], array('北京', '上海', '天津', '重庆'))){
                        $data['mobileBelonging'] = $ret['data']['city'].'市';
                    }else{
                        $data['mobileBelonging'] = $ret['data']['province'].'省'.$ret['data']['city'].'市';
                    }
                    $userMdl->where(array('userCode' => $v['userCode']))->save($data);
                }
            }
        }
        return true;
    }
}