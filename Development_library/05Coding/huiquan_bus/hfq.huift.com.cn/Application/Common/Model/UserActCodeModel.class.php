<?php
namespace Common\Model;
use Think\Model;
/**
 * activity表
 * @author
 */
class UserActCodeModel extends BaseModel {
    protected $tableName = 'UserActCode';

    /**
     * 计算订单中已经结算的金额
     * @param string $orderCode 订单编码
     * @return int $settledAmount 已经计算的金额，单位：分
     */
    public function getSettledAmount($orderCode) {
        $settledAmount = 0;
        $valedUserActCodeList = $this->field(array('price'))->where(array('orderCode' => $orderCode, 'status' => \Consts::USER_ACT_CODE_STATUS_USED))->select();
        foreach($valedUserActCodeList as $v) {
            $settledAmount += $v['price'];
        }
        return $settledAmount;
    }

    /**
     * 计算使用某个活动码时，计算结算金额的明细
     * @param int $settledAmount 已经结算的金额，单位：分
     * @param int $toSettleAmount 要结算的金额，单位：分
     * @param int $shopBonus 商家红包，单位：分
     * @param int $platBonus 平台红包，单位：分
     * @return array 单位：分。格式：['bankcardAmount' => '实际支付金额', 'platBonus' => '平台红包', 'shopBonus' => '商户红包']
     */
    public function calToSettleDetail($settledAmount, $toSettleAmount, $shopBonus, $platBonus) {
        $totalSettleAmount = $settledAmount + $toSettleAmount; // 最终的总结算金额
        if($totalSettleAmount <= $shopBonus) { // 最终的总结算金额 <= 商家红包金额
            // 结算商家红包金额
            $toSettleShopBonus = 0; // 要结算的商家红包金额
            if($settledAmount < $shopBonus) { // 已结算的金额 < 商家红包金额
                $toSettleShopBonus = $totalSettleAmount - $settledAmount; // 要结算的商家红包金额，单位：分
            }

            $toSettlePlatBonus = 0 ; // 要结算的平台红包金额，单位：分
            $toSettleBankcardPay = 0; // 要结算的银行支付金额，单位：分
        } elseif($totalSettleAmount > $shopBonus && $totalSettleAmount <= $shopBonus + $platBonus) { // 商家红包金额 < 最终的总结算金额 <= 商家红包金额 + 平台红包金额
            // 结算商家红包金额
            $toSettleShopBonus = 0; // 要结算的商家红包金额
            if($settledAmount < $shopBonus) { // 已结算的金额 < 商家红包金额
                $toSettleShopBonus = $shopBonus - $settledAmount; // 要结算的商家红包金额，单位：分
                $settledAmount = $shopBonus; // 重新计算已结算金额，单位：分
            }

            // 结算平台红包金额
            $toSettlePlatBonus = 0; // 要结算的平台红包金额
            if($settledAmount >= $shopBonus && $settledAmount < $shopBonus + $platBonus) { // 商家红包金额 <= 已结算金额 < 商家红包金额 + 平台红包金额
                $toSettlePlatBonus = $totalSettleAmount - $settledAmount; // 要结算的平台红包金额，单位：分
            }

            $toSettleBankcardPay = 0; // 要结算的银行支付金额，单位：分
        } else { // 总结算金额 > 商家红包金额 + 平台红包金额
            // 结算商家红包金额
            $toSettleShopBonus = 0; // 要结算的商家红包金额
            if($settledAmount < $shopBonus) { // 已结算的金额 < 商家红包金额
                $toSettleShopBonus = $shopBonus - $settledAmount; // 要结算的商家红包金额，单位：分
                $settledAmount = $shopBonus; // 重新计算已结算金额，单位：分
            }

            // 结算平台红包金额
            $toSettlePlatBonus = 0; // 要结算的平台红包金额
            if($settledAmount >= $shopBonus && $settledAmount < $shopBonus + $platBonus) { // 商家红包金额 <= 已结算金额 < 商家红包金额 + 平台红包金额
                $toSettlePlatBonus = $shopBonus + $platBonus - $settledAmount; // 要结算的平台红包金额，单位：分
                $settledAmount = $shopBonus + $platBonus; // 重新计算已结算金额，单位：分
            }

            // 计算银行卡支付金额
            $toSettleBankcardPay = 0; // 要结算的银行支付金额，单位：分
            if($settledAmount >= $shopBonus + $platBonus) {
                $toSettleBankcardPay = $totalSettleAmount - $settledAmount; // 要结算的银行支付金额，单位：分
            }
        }
        return array(
            'bankcardAmount' => $toSettleBankcardPay,
            'platBonus' => $toSettlePlatBonus,
            'shopBonus' => $toSettleShopBonus,
        );
    }

    /**
     * 订单支付完成后，保存活动验证码，购买时间，修改状态为未验证，可用
     * @param string $orderCode 订单编码
     * @return boolean 成功返回true，失败返回false
     */
    public function addActCode($orderCode) {
        $userActCodeList = $this->field(array('userActCodeId'))->where(array('orderCode' => $orderCode))->select();
        foreach($userActCodeList as $v) {
            $actCode = $this->setActCode();
            $ret = $this
                ->where(array('userActCodeId' => $v['userActCodeId']))
                ->save(array('status' => \Consts::USER_ACT_CODE_STATUS_USE, 'actCode' => $actCode, 'buyTime' => time())) !== false ?  true : false;
            if($ret == false) {
                return $ret;
            }
        }
        return true;
    }

    /**
     * 获得活动验证码列表
     * @param array $condition
     * @param array $field
     * @param array $joinTableArr
     * @param string $order
     * @param int $limit
     * @param int $page
     * @return mixed
     */
    public function getActCodeList($condition = array(), $field = array(), $joinTableArr = array(), $order = '', $limit = 0, $page = 0) {
        if(empty($field)){
            $field = array('UserActCode.*');
        }
        $this->field($field);
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        if($order){
            $this->order($order);
        }
        if($limit){
            $this->limit($limit);
        }
        if($page){
            $this->page($page);
        }
        $actCodeList = $this->select();
        return $actCodeList;
    }

    /**
     * 获得查询数量
     * @param array $condition
     * @param array $joinTableArr
     * @return mixed
     */
    public function countActCodeList($condition = array(), $joinTableArr = array()) {
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        return $this->count('distinct(UserActCode.userActCodeId)');
    }

    /**
     * 获取活动验证码详情
     * @param array $condition
     * @param array $field
     * @param array $joinTableArr
     * @param string $order
     * @return array|mixed
     */
    public function getActCodeInfo($condition, $field = array(), $joinTableArr = array(), $order = '') {
        if(empty($field)){
            $field = array('UserActCode.*');
        }
        $this->field($field);
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        if($order){
            $this->order($order);
        }
        $activityInfo = $this->find();
        return $activityInfo;
    }

    /**
     * 获取某一字段的数组形式
     * @param array $condition
     * @param $field
     * @param array $joinTableArr
     * @return mixed
     */
    public function getActCodeField($condition = array(), $field, $joinTableArr = array()) {
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        return $this->getField($field, true);
    }

    /**
     * 修改
     * @param array $where 条件，一维关联数组
     * @param array $data 数据，一维关联数组
     * @return array
     */
    public function updateUserActCode($where, $data){
        if($where){
            $code = $this->where($where)->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        }else{
            $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        }
        return $this->getBusinessCode($code);
    }

    /**
     * 生成活动验证码
     * @return string
     */
    public function setActCode(){
        $year = (int)date('Y');
        $month = date('m');
        $actCode = $year - 2016;
        $actCode = str_pad($actCode, 2, '0', STR_PAD_LEFT);
        $actCode .= $month;
//        $randArr = array('0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z');
        $randArr = array('0','1','2','3','4','5','6','7','8','9');
        for($i = 0; $i < 6; $i++){
            $random_keys = array_rand($randArr, 1);
            $actCode .= $randArr[$random_keys];
        }
        $userActCodeInfo = $this->getActCodeInfo(array('actCode' => $actCode), array('userActCodeId'));
        if($userActCodeInfo){
            return $this->setActCode();
        }
        return $actCode;
    }

    /**
     * 计算优惠券将要退款的各部分金额
     * @param int $toRefundAmount 需要退款的金额，单价：分
     * @param int $realPay 实际支付金额，单价：分
     * @param int $shopBonus 商户红包金额，单价：分
     * @param int $platBonus 平台红包金额，单价：分
     * @param int $refundedAmount 已经退款的金额，单价：分
     * @return array 各部分应退款金额，单位：元：{'bankcardRefund' => 23, 'platBonusRefund' => 0, 'shopBonusRefund' => 0}
     */
    public function calToRefundDetail($toRefundAmount, $realPay, $shopBonus, $platBonus, $refundedAmount) {
        /** 退款优先级由高到低为，银行卡的钱，平台红包，商家红包 */
        $totalRefundAmount = $toRefundAmount + $refundedAmount; // 最终，一共退款的金额，单位：分
        if($totalRefundAmount <= $realPay) { // 一共要退的金额 <= 实际支付的金额
            // 退银行卡的支付金额
            $toRefundBankAmount = 0; // 要退的银行卡金额
            if($refundedAmount < $realPay) { // 已经退款的金额 < 实际支付金额
                $toRefundBankAmount = $totalRefundAmount - $refundedAmount; // 要退的银行卡金额
            }

            $toRefundPlatBonus = 0; // 要退的平台红包金额
            $toRefundShopBonus = 0; // 要退的商户红包金额

        } elseif($totalRefundAmount > $realPay && $toRefundAmount <= $realPay + $platBonus) { // 实际支付的金额 < 一共要退的金额 <= 实际支付的金额 + 平台红包金额
            // 退银行卡的支付金额
            $toRefundBankAmount = 0; // 要退的银行卡金额
            if($refundedAmount < $realPay) { // 已经退款的金额 < 实际支付金额
                $toRefundBankAmount = $realPay - $refundedAmount;
                $refundedAmount = $realPay; // 重新计算已经退款的金额，单位:分
            }

            // 退平台红包
            $toRefundPlatBonus = 0; // 要退的平台红包金额
            if($refundedAmount >= $realPay && $refundedAmount < $realPay + $platBonus) { // 实际支付金额 <= 已经退款的金额 < 实际支付的金额 + 平台红包金额
                $toRefundPlatBonus = $totalRefundAmount - $refundedAmount; // 要退的平台红包金额
            }

            $toRefundShopBonus = 0;
        } else { // 一共要退的金额 > 实际支付的金额 + 平台红包金额
            // 退银行卡的支付金额
            $toRefundBankAmount = 0; // 要退的银行卡金额
            if($refundedAmount < $realPay) { // 已经退款的金额 < 实际支付金额
                $toRefundBankAmount = $realPay - $refundedAmount;
                $refundedAmount = $realPay; // 重新计算已经退款的金额，单位:分
            }

            // 退平台红包
            $toRefundPlatBonus = 0; // 要退的平台红包金额
            if($refundedAmount >= $realPay && $refundedAmount < $realPay + $platBonus) { // 实际支付金额 <= 已经退款的金额 < 实际支付的金额 + 平台红包金额
                $toRefundPlatBonus = $realPay + $platBonus - $refundedAmount; // 要退的平台红包金额
                $refundedAmount = $realPay + $platBonus; // 重新计算已经退款的金额，单位:分
            }

            // 退商户红包
            $toRefundShopBonus = 0; // 要退的商家红包金额
            if($refundedAmount >= $realPay + $platBonus && $refundedAmount < $realPay + $platBonus + $shopBonus) { // 实际支付金额 + 平台红包金额 <= 已经退款的金额 < 实际支付金额 + 平台红包金额 + 商户红包金额
                $toRefundShopBonus = $totalRefundAmount - $refundedAmount; // 要退的商家红包金额
            }
        }
        return array(
            'bankcardRefund' => $toRefundBankAmount / \Consts::HUNDRED,
            'platBonusRefund' => $toRefundPlatBonus / \Consts::HUNDRED,
            'shopBonusRefund' => $toRefundShopBonus / \Consts::HUNDRED,
        );
    }

    /**
     * 活动验证码是否允许退款
     * @param string $actCode 活动验证码
     * @return boolean 可以退款返回true，不能够退款返回false
     */
    public function isAllowedRefund($actCode) {
        // 获得用户活动的相关信息
        $userActInfo = $this
            ->field(array('UserActCode.buyTime', 'Activity.refundRequired', 'Activity.startTime'))
            ->join('UserActivity ON UserActivity.userActivityCode = UserActCode.userActCode')
            ->join('Activity ON Activity.activityCode = UserActivity.activityCode')
            ->where(array('actCode' => $actCode, 'UserActCode.status' => \Consts::USER_ACT_CODE_STATUS_USE))
            ->find();
        if($userActInfo) {
            switch($userActInfo['refundRequired']) {
                case C('ACTIVITY_REFUND_REQUIRED_VALUE.CASUAL'): // 随意退、过期退
                    $isAllowedRefund = true;
                    break;
                case C('ACTIVITY_REFUND_REQUIRED_VALUE.THE_DAY'): // 活动开始当天以及活动开始后不退
                    // 判断今天是不是活动开始当天
                    $isAllowedRefund = time() < strtotime(substr($userActInfo['startTime'], 0, 10)) ? true : false;
                    break;
                case C('ACTIVITY_REFUND_REQUIRED_VALUE.ONE_DAY'): // 活动开始前24小时内不退
                    $isAllowedRefund = strtotime($userActInfo['startTime']) - time() < 24 * 3600 ?  false : true;
                    break;
                case C('ACTIVITY_REFUND_REQUIRED_VALUE.TWO_DAY'): // 活动开始前48小时内不退
                    $isAllowedRefund = strtotime($userActInfo['startTime']) - time() < 48 * 3600 ?  false : true;
                    break;
                default:
                    $isAllowedRefund = true;
                    break;
            }
        } else {
            $isAllowedRefund = false;
        }
        return $isAllowedRefund;
    }

    /**
     * 活动验证码退款
     * @param string $actCode 活动验证码
     * @param string $orderCode 订单编码
     * @param float $bankcardRefund 工行卡金额，单位：分
     * @param float $platBonusRefund 平台红包金额，单位：分
     * @param float $shopBonusRefund 商户红包金额，单位：分
     * @param int $refundReason 退款原因。1-计划有变，没时间参加活动；2-后悔了不想参加活动了；3-不小心买的多了；4-其他原因；
     * @param string $refundRemark 退款备注
     * @return boolean 成功返回true，失败返回错误码
     */
    public function actCodeRefund($actCode, $orderCode, $bankcardRefund, $platBonusRefund, $shopBonusRefund, $refundReason, $refundRemark) {
        // 获得用户购买的活动码信息
        $userActCodeInfo = $this->getActCodeInfo(
            array('UserActCode.actCode' => $actCode),
            array('UserActivity.userCode', 'Activity.shopCode', 'UserActCode.userActCode'),
            array(
                array('joinTable' => 'UserActivity', 'joinCondition' => 'UserActivity.userActivityCode = UserActCode.userActCode', 'joinType' => 'inner'),
                array('joinTable' => 'Activity', 'joinCondition' => 'Activity.activityCode = UserActivity.activityCode', 'joinType' => 'inner')
            )
        );
        // 获得商户的信息
        $shopMdl = new ShopModel();
        $shopInfo = $shopMdl->getShopInfo($userActCodeInfo['shopCode'], array('shopCode', 'shopName', 'hqIcbcShopNbr'));
        // 获得用户信息
        $userCode = $userActCodeInfo['userCode'];
        $userMdl = new UserModel();
        $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('mobileNbr', 'recomNbr'));
        // 获得此订单的相关信息
        $userConsumeMdl = new UserConsumeModel();
        $userConsumeInfo = $userConsumeMdl->getCurrConsumeInfoByOrderCode($orderCode);

        M()->startTrans();
        /**下面就是退钱的各种事项了*/
        // 退工行里的钱
        $icbcReqRet = true;
        if($bankcardRefund > 0) {
            // 获得公司方流水号
            $cpmtxsnoLogMdl = new CmptxsnoLogModel();
            $cpmtxsno = $cpmtxsnoLogMdl->getCmptxsno();
            // 获得使用的银行卡账号信息
            $bankAccountLocalLogMdl = new BankAccountLocalLogModel();
            $bankAccountLocalLogInfo = $bankAccountLocalLogMdl->bankAccountLogConsume($userConsumeInfo['consumeCode']);
            $bankAccountMdl = new BankAccountModel();
            $bankAccountInfo = $bankAccountMdl->getBankAccountInfo(array('bankAccountCode' => $bankAccountLocalLogInfo['accountCode']));

            $icbcMdl = new IcbcModel();
            $refundLogMdl = new RefundLogModel();
            $payTimeStamp = strtotime($userConsumeInfo['payedTime']); // 消费交易时间戳
            $todayStartStamp = strtotime(date('Y-m-d 00:00:00', time())); // 今天开始时间戳
            $todayEndStamp = strtotime(date('Y-m-d 23:59:59', time())); // 今天结束时间戳
            // 如果退款交易日期和消费交易日期是同一天，只能够调用当日的银行卡消费撤销交易，20270
            if($payTimeStamp >= $todayStartStamp && $payTimeStamp <= $todayEndStamp) {
                if($userConsumeInfo['orderAmount'] == (string)($bankcardRefund + $platBonusRefund + $shopBonusRefund)) { // 要退款金额 等于 订单总金额
                    // 调用工行银行卡消费撤销交易，20270
                    $icbcRet = $icbcMdl->cancelPay($cpmtxsno, $shopInfo['sellerid'], $bankAccountInfo['bankCard'], $userInfo['mobileNbr'] . $bankAccountInfo['accountNbrLast4'], $bankcardRefund / \Consts::HUNDRED, $bankAccountInfo['accountName'], $bankAccountInfo['idType'], $bankAccountInfo['idNbr'], $userConsumeInfo['orderNbr']);
                    //记录退款流水（当天不需要清算）
                    if($icbcRet['retcode'] == C('ICBC_PAY_SUCCESS')){
                        $refundLogMdl->editRefundLog(
                            array(
                                'orderNbr' => $userConsumeInfo['orderNbr'],
                                'refundPrice' => $bankcardRefund,
                                'refundAccount' => $bankAccountLocalLogInfo['accountCode'],
                                'createTime' => date('Y-m-d H:i:s', time()),
                                'refundTime' => date('Y-m-d H:i:s', time()),
                            )
                        );
                    }
                } else {
                    $icbcRet['retcode'] = false;
                    return C('REFUND.CAN_NOT_REFUND_APART');
                }
                $icbcReqRet = $icbcRet['retcode'] === C('ICBC_PAY_SUCCESS') ? true : false;
            } else { // 退款交易日期和消费交易日期不在同一天，调用工行信用卡退货交易接口，20240
                // 调用工行的信用卡退货交易，20240
//                $icbcRet = $icbcMdl->creditCardReturnGoods($cpmtxsno, $shopInfo['sellerid'], $bankAccountInfo['bankCard'], $userInfo['mobileNbr'] . $bankAccountInfo['accountNbrLast4'], $bankcardRefund / \Consts::HUNDRED, $bankAccountInfo['accountName'], $bankAccountInfo['idType'], $bankAccountInfo['idNbr'], $userConsumeInfo['orderNbr'], $userConsumeInfo['payedTime'], '');
                //记录退款流水
                $refundLogRet = $refundLogMdl->editRefundLog(
                    array(
                        'orderNbr' => $userConsumeInfo['orderNbr'],
                        'refundPrice' => $bankcardRefund,
                        'refundAccount' => $bankAccountLocalLogInfo['accountCode'],
                        'LiquidationStatus' => \Consts::LIQUIDATION_STATUS_HAD_NOT
                    )
                );
                $icbcReqRet = $refundLogRet['code'] == C('SUCCESS') ? true : false;
            }
        }

        $bonusStatisticsMdl = new BonusStatisticsModel();
        // 退平台红包
        $incPlatRet = true;
        if($platBonusRefund > 0) {
            // 回滚用户的平台红包金额
            $incPlatRet = $bonusStatisticsMdl->incBonusValue($userCode, C('HQ_CODE'), $platBonusRefund);
        }

        // 退商户红包
        $incShopRet = true;
        if($shopBonusRefund > 0) {
            // 回滚用户的商户红包金额
            $incShopRet = $bonusStatisticsMdl->incBonusValue($userCode, $userActCodeInfo, $shopBonusRefund);
        }

        // 更新该笔订单的已退款金额
        $consumeOrderMdl = new ConsumeOrderModel();
        $updateRefundAmountRet = $consumeOrderMdl->incField(array('orderCode' => $orderCode), 'refundAmount', $bankcardRefund + $platBonusRefund + $shopBonusRefund);
        $payStatus = $userConsumeInfo['orderAmount'] == (string)($bankcardRefund + $platBonusRefund + $shopBonusRefund) ? \Consts::PAY_STATUS_REFUNDED : \Consts::PAY_STATUS_PART_REFUNDED;
        // 更新订单的支付状态
        $updateConsumeOrderRet = $consumeOrderMdl->updateConsumeOrder(array('orderCode' => $orderCode), array('status' => $payStatus));
        // 更新账单的支付状态
        $userConsumeMdl = new UserConsumeModel();
        $updateConsumeStatusRet = $userConsumeMdl->updateConsumeStatus(array('consumeCode' => $userConsumeInfo['consumeCode']), array('status' => $payStatus));

        // 保存活动码的退款信息
        $saveRefundRet = $this
            ->where(array('actCode' => $actCode))
            ->save(array(
                'refundReason' => $refundReason,
                'refundRemark' => $refundRemark,
                'refundTime' => time(),
                'bankcardRefund' => $bankcardRefund,
                'platBonusRefund' => $platBonusRefund,
                'shopBonusRefund' => $shopBonusRefund,
                'status' => \Consts::ACT_CODE_STATUS_REFUND
            ));
        // 保存
        if($icbcReqRet == true && $incPlatRet == true && $incShopRet == true && $updateRefundAmountRet == true && $updateConsumeOrderRet == true && $updateConsumeStatusRet == true && $saveRefundRet !== false) {
            M()->commit();
            return true;
        } else {
            M()->rollback();
            return C('API_INTERNAL_EXCEPTION');
        }
    }
}