<?php
/**
 * Created by PhpStorm.
 * User: Jihuafei
 * Date: 15-12-16
 * Time: 下午6:06
 */
namespace Common\Model;
class OrderCouponModel extends BaseModel {
    protected $tableName = 'OrderCoupon';

    /**
     * 计算优惠券的消费金额详情包括（银行卡支付金额，平台红包，商家红包）
     * @param string $orderCouponCode 优惠券订单编码
     * @return array 格式：array('toSettleShopBonus' => $toSettleShopBonus,'toSettlePlatBonus' => $toSettlePlatBonus,'toSettleBankcardPay' => $toSettleBankcardPay,)
     */
    public function calCouponConsumeDetail($orderCouponCode) {

        $orderCouponInfo = $this
            ->field(array('orderCode', 'payPrice', 'OrderCoupon.batchCouponCode'))
            ->join('BatchCoupon ON BatchCoupon.batchCouponCode = OrderCoupon.batchCouponCode')
            ->where(array('orderCouponCode' => $orderCouponCode))
            ->find();
        $orderCode = $orderCouponInfo['orderCode']; // 订单编码

        // 获得该订单下购买的优惠券，已经被使用的数量
        $usedCount = $this->where(array('orderCode' => $orderCode, 'status' => \Consts::ORDER_COUPON_STATUS_USED))->count();

        // 获得优惠券的购买价格，单位：分
        $batchCouponMdl = new BatchCouponModel();
        $payPrice = $batchCouponMdl->getFieldInfo('BatchCoupon', array('batchCouponCode' => $orderCouponInfo['batchCouponCode']), 'payPrice');
        $toSettleAmount = $payPrice; // 需要结算的金额，单位：分

        // 获得已经使用了的优惠券的结算金额，单位：分
        $usedAmountMoney = $payPrice * $usedCount;

        // 获得订单的消费信息
        $userConsumeMdl = new UserConsumeModel();
        $userConsumeInfo = $userConsumeMdl->getConsumeInfo(
            array('UserConsume.orderCode' => $orderCode, 'UserConsume.status' => array('IN', array(\Consts::PAY_STATUS_PAYED, \Consts::PAY_STATUS_PART_REFUNDED))),
            array('realPay', 'UserConsume.platBonus', 'UserConsume.shopBonus')
        );
        $realPay = $userConsumeInfo['realPay']; // 实际支付金额，单位：分
        $platBonus = $userConsumeInfo['platBonus']; // 平台红包金额，单位：分
        $shopBonus = $userConsumeInfo['shopBonus']; // 商家红包金额，单位：分

        /** 结算顺序，商家红包，平台红包，银行卡支付金额*/
        $totalSettleAmount = $toSettleAmount + $usedAmountMoney; // 最终的总结算金额，单位：分
        if($totalSettleAmount <= $shopBonus) { // 最终的总结算金额 <= 商家红包金额
            // 结算商家红包金额
            $toSettleShopBonus = 0; // 要结算的商家红包金额
            if($usedAmountMoney < $shopBonus) { // 已结算的金额 < 商家红包金额
                $toSettleShopBonus = $totalSettleAmount - $usedAmountMoney; // 要结算的商家红包金额，单位：分
            }

            $toSettlePlatBonus = 0 ; // 要结算的平台红包金额，单位：分
            $toSettleBankcardPay = 0; // 要结算的银行支付金额，单位：分
        } elseif($totalSettleAmount > $shopBonus && $totalSettleAmount <= $shopBonus + $platBonus) { // 商家红包金额 < 最终的总结算金额 <= 商家红包金额 + 平台红包金额
            // 结算商家红包金额
            $toSettleShopBonus = 0; // 要结算的商家红包金额
            if($usedAmountMoney < $shopBonus) { // 已结算的金额 < 商家红包金额
                $toSettleShopBonus = $shopBonus - $usedAmountMoney; // 要结算的商家红包金额，单位：分
                $usedAmountMoney = $shopBonus; // 重新计算已结算金额，单位：分
            }

            // 结算平台红包金额
            $toSettlePlatBonus = 0; // 要结算的平台红包金额
            if($usedAmountMoney >= $realPay && $usedAmountMoney < $shopBonus + $platBonus) { // 商家红包金额 <= 已结算金额 < 商家红包金额 + 平台红包金额
                $toSettlePlatBonus = $totalSettleAmount - $usedAmountMoney; // 要结算的平台红包金额，单位：分
            }

            $toSettleBankcardPay = 0; // 要结算的银行支付金额，单位：分
        } else { // 总结算金额 > 商家红包金额 + 平台红包金额
            // 结算商家红包金额
            $toSettleShopBonus = 0; // 要结算的商家红包金额
            if($usedAmountMoney < $shopBonus) { // 已结算的金额 < 商家红包金额
                $toSettleShopBonus = $shopBonus - $usedAmountMoney; // 要结算的商家红包金额，单位：分
                $usedAmountMoney = $shopBonus; // 重新计算已结算金额，单位：分
            }

            // 结算平台红包金额
            $toSettlePlatBonus = 0; // 要结算的平台红包金额
            if($usedAmountMoney >= $shopBonus && $usedAmountMoney < $shopBonus + $platBonus) { // 商家红包金额 <= 已结算金额 < 商家红包金额 + 平台红包金额
                $toSettlePlatBonus = $shopBonus + $platBonus - $usedAmountMoney; // 要结算的平台红包金额，单位：分
                $usedAmountMoney = $shopBonus + $platBonus; // 重新计算已结算金额，单位：分
            }

            // 计算银行卡支付金额
            $toSettleBankcardPay = 0; // 要结算的银行支付金额，单位：分
            if($usedAmountMoney >= $shopBonus + $platBonus) {
                $toSettleBankcardPay = $totalSettleAmount - $usedAmountMoney; // 要结算的银行支付金额，单位：分
            }
        }
        return array(
            'toSettleShopBonus' => $toSettleShopBonus,
            'toSettlePlatBonus' => $toSettlePlatBonus,
            'toSettleBankcardPay' => $toSettleBankcardPay,
        );
    }

    /**
     * 退购买的券
     * @param array $orderCouponCodeList 订单优惠券编码列表 一维数组 例：{'adfasfasdf', 'adfadfasd', ...}
     * @param int $refundReason 退款原因
     * @param string $refundRemark 退款备注
     * @return boolean
     */
    public function couponOrderApplyRefund($orderCouponCodeList, $refundReason, $refundRemark) {

        M()->startTrans();
        // 设置订单中的优惠券状态为 申请退款，不可用
        $editOrderCouponRet = $this
            ->where(array('orderCouponCode' => array('IN', $orderCouponCodeList), 'status' => 20))
            ->save(array('refundReason' => $refundReason, 'refundRemark' => $refundRemark, 'applyRefundTime' => time(), 'status' =>12)) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');


      // 修改用户优惠券状态为不可用
        $userCouponMdl = new UserCouponModel;
        $editUserCouponRet = $userCouponMdl->editUserCoupon(array('orderCouponCode' => array('IN', $orderCouponCodeList)), array('status' => \Consts::USER_COUPON_STATUS_DISABLE));

        if($editOrderCouponRet == C('SUCCESS') && $editUserCouponRet['code'] == C('SUCCESS')) {
            $code = C('SUCCESS');
            M()->commit();
        } else {
            $code = C('API_INTERNAL_EXCEPTION');
            M()->rollback();
        }
        return $this->getBusinessCode($code);
    }

    /**
     * 用户购买的优惠券的退款
     * @param array $orderCouponCodeList 用户优惠券编码一维数组
     * @return boolean || string 成功返回true，失败返回错误信息
     */
    public function refundOrderCoupon($orderCouponCodeList) {
        // 获得用户购买的优惠券信息
        $userCouponInfo = $this->getOrderCouponInfo(
            array('OrderCoupon.orderCouponCode' => array('IN', $orderCouponCodeList)),
            array('OrderCoupon.userCode', 'payPrice', 'orderCode', 'BatchCoupon.couponType', 'UserCoupon.batchCouponCode', 'BatchCoupon.shopCode'),
            array(
                array('joinTable' => 'UserCoupon', 'joinCondition' => 'UserCoupon.orderCouponCode = OrderCoupon.orderCouponCode', 'joinType' => 'inner'),
                array('joinTable' => 'BatchCoupon', 'joinCondition' => 'BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode', 'joinType' => 'inner')
            )
        );

        // 获得商户的信息
        $shopMdl = new ShopModel();
        $shopInfo = $shopMdl->getShopInfo($userCouponInfo['shopCode'], array('shopCode', 'shopName', 'icbcShopCode', 'icbcShopName', 'hqIcbcShopNbr'));
        // 获得用户信息
        $userCode = $userCouponInfo['userCode'];
        $userMdl = new UserModel();
        $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('mobileNbr', 'recomNbr'));
        // 获得此订单的相关信息
        $userConsumeMdl = new UserConsumeModel();
        $userConsumeInfo = $userConsumeMdl->getCurrConsumeInfoByOrderCode($userCouponInfo['orderCode']);

        M()->startTrans();
        // 修改用户优惠券的状态为不可用
        $userCouponMdl = new UserCouponModel();
        $editUserCouponRet = $userCouponMdl->editUserCoupon(
            array('orderCouponCode' => array('IN', $orderCouponCodeList)),
            array('status' => \Consts::USER_COUPON_STATUS_DISABLE)
        );

        /**下面就是退钱的各种事项了*/
        // 获得要退的各部分金额
        $refundDetails = $this->calRefundMoney($orderCouponCodeList);

        // 计算并保存每一张退款的优惠券的退款具体金额，修改状态为已退款，不可用，添加退款时间
        $refundDetailsB = $refundDetails; // 用于下面计算每张券的退款金额明细
        $tem = array('bankcardAmount', 'platBonus', 'shopBonus');
        foreach($tem as $v) {
            $refundDetailsB[$v] = $refundDetailsB[$v] * \Consts::HUNDRED;
        }
        $saveCouponRefundDetailRet = $this->batchSaveCouponRefundDetail($refundDetailsB, $orderCouponCodeList, $userCouponInfo['payPrice']);


        // 退工行里的钱
        $icbcReqRet = true;

        if($refundDetails['bankcardAmount'] > 0) {

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
            $payTimeStamp = strtotime($userConsumeInfo['payedTime']); // 消费支付时间戳
            $todayStartStamp = strtotime(date('Y-m-d 00:00:00', time())); // 今天开始时间戳
            $todayEndStamp = strtotime(date('Y-m-d 23:59:59', time())); // 今天结束时间戳
            // 如果退款交易日期和消费交易日期是同一天，只能够调用当日的银行卡消费撤销交易，20270
            if($payTimeStamp >= $todayStartStamp && $payTimeStamp <= $todayEndStamp) {
                if($userConsumeInfo['orderAmount'] == ($refundDetails['bankcardAmount'] + $refundDetails['platBonus'] + $refundDetails['shopBonus']) * \Consts::HUNDRED) { // 要退款金额 等于 订单总金额
                    // 调用工行银行卡消费撤销交易，20270
                    $icbcRet = $icbcMdl->cancelPay($cpmtxsno, $shopInfo['sellerid'], $bankAccountInfo['bankCard'], $userInfo['mobileNbr'] . $bankAccountInfo['accountNbrLast4'], $refundDetails['bankcardAmount'], $bankAccountInfo['accountName'], $bankAccountInfo['idType'], $bankAccountInfo['idNbr'], $userConsumeInfo['orderNbr']);
                    //记录退款流水（当天不需要清算）
                    if($icbcRet['retcode'] == C('ICBC_PAY_SUCCESS')){
                        $refundLogMdl->editRefundLog(array(
                            'orderNbr' => $userConsumeInfo['orderNbr'],
                            'refundPrice' => $refundDetails['bankcardAmount'] * \Consts::HUNDRED,
                            'refundAccount' => $bankAccountLocalLogInfo['accountCode'],
                            'createTime' => date('Y-m-d H:i:s', time()),
                            'refundTime' => date('Y-m-d H:i:s', time()),
                        ));
                    }
                } else {
                    return C('REFUND.CAN_NOT_REFUND_APART');
                }
                $icbcReqRet = $icbcRet['retcode'] === C('ICBC_PAY_SUCCESS') ? true : false;
            } else { // 退款交易日期和消费交易日期不在同一天，调用工行信用卡退货交易接口，20240
                // 调用工行的信用卡退货交易，20240
//                $icbcRet = $icbcMdl->creditCardReturnGoods($cpmtxsno, $shopInfo['sellerid'], $bankAccountInfo['bankCard'], $userInfo['mobileNbr'] . $bankAccountInfo['accountNbrLast4'], $refundDetails['bankcardAmount'], $bankAccountInfo['accountName'], $bankAccountInfo['idType'], $bankAccountInfo['idNbr'], $userConsumeInfo['orderNbr'], date('Ymd', strtotime($userConsumeInfo['payedTime'])), '');
                //记录退款流水
                $refundLogRet = $refundLogMdl->editRefundLog(
                    array(
                        'orderNbr' => $userConsumeInfo['orderNbr'],
                        'refundPrice' => $refundDetails['bankcardAmount'] * \Consts::HUNDRED,
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
        if($refundDetails['platBonus'] > 0) {
            // 回滚用户的平台红包金额
            $incPlatRet = $bonusStatisticsMdl->incBonusValue($userCode, C('HQ_CODE'), $refundDetails['platBonus'] * \Consts::HUNDRED);
        }

        // 退商户红包
        $incShopRet = true;
        if($refundDetails['shopBonus'] > 0) {
            // 回滚用户的商户红包金额
            $incShopRet = $bonusStatisticsMdl->incBonusValue($userCode, $userCouponInfo['shopCode'], $refundDetails['shopBonus'] * \Consts::HUNDRED);
        }

        // 更新该笔订单的已退款金额
        $consumeOrderMdl = new ConsumeOrderModel();
        $updateRefundAmountRet = $consumeOrderMdl->incField(array('orderCode' => $userCouponInfo['orderCode']), 'refundAmount', ($refundDetails['bankcardAmount'] + $refundDetails['platBonus'] + $refundDetails['shopBonus']) * \Consts::HUNDRED);
        $payStatus = $userConsumeInfo['orderAmount'] == ($refundDetails['bankcardAmount'] + $refundDetails['platBonus'] + $refundDetails['shopBonus']) * \Consts::HUNDRED ? \Consts::PAY_STATUS_REFUNDED : \Consts::PAY_STATUS_PART_REFUNDED;



        // 更新订单的支付状态
        $updateConsumeOrderRet = $consumeOrderMdl->updateConsumeOrder(array('orderCode' => $userCouponInfo['orderCode']), array('status' => $payStatus));
        // 更新账单的支付状态
        $userConsumeMdl = new UserConsumeModel();
        $updateConsumeStatusRet = $userConsumeMdl->updateConsumeStatus(array('consumeCode' => $userConsumeInfo['consumeCode']), array('status' => $payStatus));

        // 回滚优惠券的数量
        $batchCouponMdl = new BatchCouponModel();
        $addRet = $batchCouponMdl->addRemaining($userCouponInfo['batchCouponCode'], 1);

        if($editUserCouponRet['code'] == C('SUCCESS') && $saveCouponRefundDetailRet == true && $updateRefundAmountRet == true && $icbcReqRet === true && $incPlatRet == true && $incShopRet == true && $updateConsumeOrderRet == true && $updateConsumeStatusRet == true && $addRet == true) {
            M()->commit();
            return true;
        } else {
            M()->rollback();
            return false;
        }
    }

    /**
     * 批量计算并保存优惠券的退款明细，计算并保存每一张退款的优惠券的退款具体金额，修改状态为已退款，不可用，添加退款时间
     * @param array $refundDetail 总的退款明细，格式：{'bankcardAmount' => 123, 'platBonus' => 12, 'shopBonus' => 1}
     * @param array $orderCouponCodeList 退款的订单优惠券编码一维数组
     * @param int $payPrice 优惠券价格
     * @return boolean
     */
    private function batchSaveCouponRefundDetail($refundDetail, $orderCouponCodeList, $payPrice) {


        $ret = false;
		if(!is_array($orderCouponCodeList)) {
			$orderCouponCodeList = array($orderCouponCodeList);
		}
        foreach($orderCouponCodeList as $orderCouponCode) {

            $bankcardRefund = 0;
            $platBonusRefund = 0;
            $shopBonusRefund = 0;
            if($refundDetail['bankcardAmount'] >= $payPrice) {
                $bankcardRefund = $payPrice;
                $platBonusRefund = 0;
                $shopBonusRefund = 0;
            } elseif($refundDetail['bankcardAmount'] < $payPrice && $refundDetail['bankcardAmount'] + $refundDetail['platBonus'] >= $payPrice) {
                $bankcardRefund = $refundDetail['bankcardAmount'];
                $platBonusRefund = $payPrice - $refundDetail['bankcardAmount'];
                $shopBonusRefund = 0;
            } elseif($refundDetail['bankcardAmount'] + $refundDetail['platBonus'] < $payPrice) {
                $bankcardRefund = $refundDetail['bankcardAmount'];
                $platBonusRefund = $refundDetail['platBonus'];
                $shopBonusRefund = $payPrice - $refundDetail['bankcardAmount'] - $refundDetail['platBonus'];
            }
            $refundDetail['bankcardAmount'] = $refundDetail['bankcardAmount'] - $bankcardRefund;
            $refundDetail['platBonus'] = $refundDetail['platBonus'] - $platBonusRefund;
            $refundDetail['shopBonus'] = $refundDetail['shopBonus'] - $shopBonusRefund;

            // 保存数据
            $ret = $this
                ->where(array('orderCouponCode' => $orderCouponCode))
                ->save(array('bankcardRefund' => $bankcardRefund, 'platBonusRefund' => $platBonusRefund, 'shopBonusRefund' => $shopBonusRefund, 'status' => \Consts::ORDER_COUPON_STATUS_REFUNDED_NOUSE, 'refundTime' => time())) !== false ? true : false;
            if($ret == false) {
                return $ret;
            }
        }
        return $ret;
    }

    /**
     * 计算优惠券将要退款的各部分金额
     * @param int $count 要退的优惠券的数量
     * @param int $unitPrice 优惠券的单价，单价：分
     * @param int $realPay 实际支付金额，单价：分
     * @param int $shopBonus 商户红包金额，单价：分
     * @param int $platBonus 平台红包金额，单价：分
     * @param int $refundedAmount 已经退款的金额，单价：分
     * @return array 各部分应退款金额，单位：元：{'bankcardAmount' => 23, 'platBonus' => 0, 'shopBonus' => 0}
     */
    public function calToRefundMoney($count, $unitPrice, $realPay, $shopBonus, $platBonus, $refundedAmount) {
        $toRefundAmount = $unitPrice * $count; // 要退的金额，单位：分
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
            'bankcardAmount' => $toRefundBankAmount / \Consts::HUNDRED,
            'platBonus' => $toRefundPlatBonus / \Consts::HUNDRED,
            'shopBonus' => $toRefundShopBonus / \Consts::HUNDRED,
        );
    }

    /**
     * 计算申请退款中的金额，分别为银行卡的金额，平台红包的金额，商户红包的金额
     * @param array $refundingOrderCouponCodeList 退款中的优惠券订单编码的一维索引数组
     * @return array $toRefundDetail 各部分应退款金额，单位：元：{'bankcardAmount' => 23, 'platBonus' => 0, 'shopBonus' => 0}
     */
    public function calRefundMoney($refundingOrderCouponCodeList) {
        // 得到每张券的价格，已经退的金额，订单实际支付金额，使用的平台太红包，使用的商户红包。单位：分
        $orderCouponInfo = $this
            ->field(array('payPrice', 'refundAmount', 'realPay', 'UserConsume.platBonus', 'UserConsume.shopBonus'))
            ->where(array('orderCouponCode' => $refundingOrderCouponCodeList[0], 'UserConsume.status' => array('IN', array(\Consts::PAY_STATUS_PAYED, \Consts::PAY_STATUS_PART_REFUNDED))))
            ->join('BatchCoupon ON BatchCoupon.batchCouponCode = OrderCoupon.batchCouponCode')
            ->join('ConsumeOrder ON ConsumeOrder.orderCode = OrderCoupon.orderCode')
            ->join('UserConsume ON UserConsume.orderCode = ConsumeOrder.orderCode')
            ->find();
        // 计算各部分要退的金额
        $toRefundDetail = $this->calToRefundMoney(count($refundingOrderCouponCodeList), $orderCouponInfo['payPrice'], $orderCouponInfo['realPay'], $orderCouponInfo['shopBonus'], $orderCouponInfo['platBonus'], $orderCouponInfo['refundAmount']);
        return $toRefundDetail;
    }

    /**
     * 获得用户在商户可用的兑换券，代金券的数量
     * @param string $userCode 用户编码
     * @param string $shopCode 商户编码
     * @return int
     */
    public function countNotUsedCoupon($userCode, $shopCode) {
        return $this
            ->join('BatchCoupon ON BatchCoupon.batchCouponCode = OrderCoupon.batchCouponCode')
            ->join('Shop ON Shop.shopCode = BatchCoupon.shopCode')
            ->where(array('Shop.shopCode' => $shopCode, 'OrderCoupon.userCode' => $userCode, 'BatchCoupon.couponType' => array('IN', array(\Consts::COUPON_TYPE_EXCHANGE, \Consts::COUPON_TYPE_VOUCHER)), 'OrderCoupon.status' => \Consts::ORDER_COUPON_STATUS_USE))
            ->count('OrderCoupon.orderCouponCode');
    }

    /**
     * 获购买的优惠券列表
     * @param array $condition 条件
     * @param array $field 要查询的字段。例{'couponCode'}
     * @return array $orderCouponList 二维数组
     */
    public function getOrderCouponList($condition, $field) {
        $orderCouponList = $this->field($field)->where($condition)->select();
        return $orderCouponList;
    }

    /**
     * 生成券码
     * @return string $couponCode
     */
    public function setCouponCode() {
        $startYear = 2015; // 开始的年份
        $thisYear = date('Y'); // 当前年份
        $yearth = sprintf('%03d', $thisYear - $startYear); // 第几年
        $thisMonth  = date('m'); // 当前月份
        $couponCode = $yearth . $thisMonth;

        $chars = '0123456789';
        $len = 6;
        for($i = 0; $i < $len; $i++) {
            $couponCode .= $chars[mt_rand(0, strlen($chars) - 1)];
        }

        // 判断生成的券码是否存在
        $orderCouponInfo = $this->field(array('orderCouponCode'))->where(array('couponCode'=> $couponCode))->find();
        if($orderCouponInfo) {
            return $this->setCouponCode();
        }
        return $couponCode;
    }

    /**
     * 订单支付完成后，给优惠券生成券码
     * @param string $orderCode 订单编码
     * @return boolean
     */
    public function addCouponCode($orderCode) {
        $orderCouponList = $this
            ->field(array('orderCouponCode', 'batchCouponCode', 'userCode', 'couponType'))
            ->where(array('orderCode' => $orderCode))
            ->select();
        foreach($orderCouponList as $k => $v) {
            $couponCode = $this->setCouponCode();
            // 保存券码和券的状态
            $ret = $this
                ->where(array('orderCouponCode' => $v['orderCouponCode']))
                ->save(array('couponCode' => $couponCode, 'status' => \Consts::ORDER_COUPON_STATUS_USE));
            if($ret === false) {
                return $ret;
            }
            // 将购买的优惠券保存在UserCoupon数据表中
            $orderCouponList[$k]['status'] = \Consts::USER_COUPON_STATUS_ACTIVE; // 状态

            $orderCouponList[$k]['userCouponType'] = $orderCouponList[$k]['couponType']; // 优惠券类型
            unset($orderCouponList[$k]['couponType']);

            $orderCouponList[$k]['userCouponNbr'] = $couponCode;
            $userCouponMdl = new UserCouponModel();
            $userCouponMdl->editUserCoupon(array(), $orderCouponList[$k]);
        }
        return true;
    }

    /**
     * 取消订单后，回滚优惠券的数量
     * @param string $orderCode 订单编码
     * @return boolean $ret
     */
    public function rollbackCouponRemaining($orderCode) {
        $con = array('orderCode' => $orderCode);
        $batchCouponCode = $this->where($con)->getField('batchCouponCode');
        $count = $this->where($con)->count('orderCouponCode');
        $batchCouponMdl = new BatchCouponModel();
        $ret = $batchCouponMdl->addRemaining($batchCouponCode, $count);
        return $ret;
    }



    /**
     * 删除数据
     * @param array $condition 条件
     * @return array
     */
    public function delOrderCoupon($condition) {
        $code = $this->where($condition)->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

    /**
     * 添加，修改订单购买的优惠券
     * @param array $condition 条件
     * @param array $data 数据
     * @return array
     */
    public function editOrderCoupon($condition, $data) {
        if($condition) {
            $code = $this->where($condition)->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        } else {
            $data['orderCouponCode'] = $this->create_uuid();
            $data['createTime'] = time();
            $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        }
        return $this->getBusinessCode($code);
    }

    /**
     * 获得优惠券列表
     * @param array $condition 条件
     * @param array $field 查询的字段
     * @param array $joinTableArr 关联的表。例：array('joinTable' => '连接表名', 'joinCondition' => '连接条件', 'joinType' => '连接方式')
     * @param string $order 排序规则
     * @param int $limit 查询条数限制
     * @param int $page 查询页码
     * @return array $shopList 商家列表，二维数组
     */
    public function listOrderCoupon($condition = array(), $field = array(), $joinTableArr = array(), $order = '', $limit = 0, $page = 0) {
        if(empty($field)){
            $field = array('OrderCoupon.*');
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
        $shopList = $this->select();
        return $shopList;
    }

    /**
     * 获得统计数量
     * @param array $condition 条件
     * @param array $joinTableArr 联合的表信息
     * @return int
     */
    public function countOrderCoupon($condition = array(), $joinTableArr = array()) {
        if($condition){
            $this->where($condition);
        }
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        return $this->count('orderCouponCode');
    }

    /**
     * 获得优惠券详情
     * @param array $condition 条件
     * @param array $field 查询的字段
     * @param array $joinTableArr 关联的表。例：array(array('joinTable' => '连接表名', 'joinCondition' => '连接条件', 'joinType' => '连接方式'))
     * @param string $order 排序规则
     * @return array $shopList 商家列表，二维数组
     */
    public function getOrderCouponInfo($condition = array(), $field = array(), $joinTableArr = array(), $order = '') {
        if(empty($field)){
            $field = array('Shop.*');
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
        return $this->find();
    }

    /**
     * 根据清算状态和买的券的状态获取某一个订单的优惠券清算金额
     * @param $orderCode
     * @param $liquidationStatus
     * @param $status
     * @return mixed
     */
    public function sumRealPrice($orderCode, $liquidationStatus, $status){
        $condition = array(
            'OrderCoupon.status' => $status,
            'OrderCoupon.liquidationStatus' => $liquidationStatus,
            'OrderCoupon.orderCode' => $orderCode
        );
        return $this->join('BatchCoupon on BatchCoupon.batchCouponCode = OrderCoupon.batchCouponCode', 'inner')->where($condition)->sum('bankCardAmount');
    }


    /**
     * 获得某个字段的数组
     * @param array $condition 条件
     * @param array $field 查询的字段
     * @param array $joinTableArr 关联的表。例：array('joinTable' => '连接表名', 'joinCondition' => '连接条件', 'joinType' => '连接方式')
     * @return array $fieldArr
     */
    public function listField($condition = array(), $field, $joinTableArr = array()) {
        if(empty($field)){
            $field = array('OrderCoupon.*');
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

        $fieldArr = $this->getField($field, true);
        return $fieldArr;
    }
}
