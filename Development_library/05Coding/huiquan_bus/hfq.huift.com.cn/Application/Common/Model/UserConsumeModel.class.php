<?php
namespace Common\Model;
use Think\Model;
use Think\Log;
use Think\Exception;
use Common\Model\ConsumeOrder;

/**
 * userConsume表
 * @author Huafei Ji
 */
class UserConsumeModel extends BaseModel {
    protected $tableName = 'UserConsume';
    const ZERO = 0;
    private $reqBankSucc = '<package><pub><retcode>00000</retcode></pub></package>'; // 工行返回成功信息

    /**
     * 结算惠圈平台补贴金额
     * @param array $condition 查询需要清算的账单的条件
     * @return array $ret
     */
    public function settleHqSubsidy($condition) {
        $where = $this->filterWhere(
            $condition,
            array('shopName' => 'like', 'mobileNbr' => 'like', 'orderNbr' => 'like'),
            $page);
        $where = $this->filterUserConsumeWhere($where);
        $consumeCodeList = $this
            ->join('Shop ON Shop.shopCode = UserConsume.location', 'left')
            ->join('User ON User.userCode = UserConsume.consumerCode', 'left')
            ->join('ConsumeOrder ON ConsumeOrder.orderCode = UserConsume.orderCode')
            ->where($where)
            ->getField('consumeCode', true);
        if($consumeCodeList) {
            $code = $this->where(array('consumeCode' => array('IN', $consumeCodeList)))->save(array('subsidySettlementStatus' => \Consts::LIQUIDATION_STATUS_HAD)) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        } else {
            $code = C('SUCCESS');
        }
        return $this->getBusinessCode($code);
    }

    /**
     * 获得消费的顾客列表
     * @param array $condition {'UserConsume.location' => '', 'UserConsume.payedTime' => '', ...}
     * @param object $pager 分页
     * @return array $customerList 二维数组
     */
    public function getShopCustomer($condition, $pager) {
        $customerList = $this
            ->field(array(
                'User.avatarUrl',
                'User.nickName',
                'User.mobileNbr',
                'COUNT(UserConsume.consumeCode)' => 'consumptionNbr',
                'SUM(ConsumeOrder.orderAmount)' => 'consumptionAmount',
                'SUM(UserConsume.realPay)' => 'payAmount',
                'SUM(UserConsume.deduction)' => 'discountAmount',
                'MAX(payedTime)' => 'lastConsumeTime',
            ))
            ->join('User ON User.userCode = UserConsume.consumerCode')
            ->join('ConsumeOrder ON ConsumeOrder.orderCode = UserConsume.orderCode')
            ->where($condition)
            ->group('User.userCode')
            ->order('User.registerTime asc')
            ->pager($pager)
            ->select();
        return $customerList;
    }

    /**
     * 获得消费的顾客总数
     * @param array $condition {'UserConsume.location' => '', 'UserConsume.payedTime' => '', ...}
     * return int $count
     */
    public function countShopCustomer($condition) {
        $count = $this
            ->join('User ON User.userCode = UserConsume.consumerCode')
            ->join('ConsumeOrder ON ConsumeOrder.orderCode = UserConsume.orderCode')
            ->where($condition)
            ->count('DISTINCT(User.userCode)');
        return $count;
    }

    /**
     * 判断账单中使用的优惠券是否可用
     * @param string $consumeCode 账单编码
     * @return boolean 不可用返回false，可用返回true
     */
    public function isConsumeCouponCanBeUse($consumeCode) {
        // 获得使用优惠券的信息
        $userCouponMdl = new UserCouponModel();
        $userCouponInfo = $userCouponMdl->getUserCouponInfoB(
            array('UserCoupon.consumeCode' => $consumeCode),
            array('UserCoupon.userCouponCode', 'BatchCoupon.insteadPrice', 'couponType', 'discountPercent', 'availablePrice', 'UserCoupon.applyTime', 'BatchCoupon.validityPeriod', 'BatchCoupon.expireTime')
        );
        $isUserCouponExpire = $userCouponMdl->isUserCouponExpire($userCouponInfo['applyTime'], $userCouponInfo['validityPeriod'], $userCouponInfo['expireTime']);
        return $isUserCouponExpire ? false : true;
    }

    /**
     * 获得订单最后一条支付记录
     * @param string $orderCode 订单编码
     * @param string $field 查询字段
     * @return array 一维数组
     */
    public function getOrderCurrPayInfo($orderCode, $field) {
        $payInfo = $this->field($field)->where(array('orderCode' => $orderCode))->order('consumeTime desc')->find();
        return $payInfo;
    }

    /**
     * 是否为平台首单，不包括使用N元购的订单
     * @param string $userCode
     * @return boolean 是返回true,否返回false
     */
    public function isFirst($userCode) {
        $systemParamMdl = new SystemParamModel();
        $orderFirstInfo = $systemParamMdl->getParamValue('orderFirst');
        if($orderFirstInfo['value'] == C('NO')) {
            return false;
        }
        // 获得已付款的订单
        $userConsumeList = $this
            ->field(array('consumeCode'))
            ->where(array(
                'UserConsume.consumerCode' => $userCode,
                'UserConsume.status' => \Consts::PAY_STATUS_PAYED,
            ))
            ->select();
        if($userConsumeList) {
            $userCouponMdl = new UserCouponModel();
            foreach($userConsumeList as $v) {
                $userCouponInfo = $userCouponMdl->getUserCouponInfoB(
                    array('UserCoupon.consumeCode' => $v['consumeCode']),
                    array('UserCoupon.userCouponCode', 'BatchCoupon.insteadPrice', 'couponType', 'discountPercent', 'availablePrice', 'UserCoupon.applyTime', 'BatchCoupon.validityPeriod', 'BatchCoupon.expireTime')
                );

                if(! $userCouponInfo || $userCouponInfo['couponType'] != \Consts::COUPON_TYPE_N_PURCHASE) { // 若订单没有使用优惠券，或优惠券类型不是N元购。返回false
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    /**
     * 获得用户支付记录
     * @param array $condition
     * @param array $field
     * @return array
     */
    public function getUserPayList($condition, $field) {
        $userPayList = $this
            ->field($field)
            ->join('BankAccountLocalLog ON BankAccountLocalLog.consumeCode = UserConsume.consumeCode', 'left')
            ->join('BankAccount ON BankAccount.bankAccountCode = BankAccountLocalLog.accountCode', 'left')
            ->where($condition)
            ->order('consumeTime asc')
            ->select();
        $temArray = array('deduction', 'realPay', 'platBonus', 'shopBonus', 'bankCardDeduction', 'couponDeduction', 'cardDeduction', 'firstDeduction');
        foreach($userPayList as &$v) {
            foreach($temArray as $item) {
                $v[$item] = number_format($v[$item] / C('RATIO'), 2, '.', '');
            }
        }
        return $userPayList;
    }

    /**
     * 退款
     * @param string $consumeCode 用户支付编码
     * @return array 结果 例：{'code' => '50000'}
     */
    public function refund($consumeCode) {
        $userConsumeInfo = $this
            ->field(array('shopName', 'Shop.shopCode', 'Shop.mobileNbr' => 'shopMobileNbr', 'realPay', 'deduction', 'consumerCode', 'orderNbr', 'orderAmount', 'UserConsume.orderCode', 'payType', 'platBonus', 'shopBonus', 'couponUsed', 'isCard', 'UserConsume.status', 'UserConsume.payedTime'))
            ->join('ConsumeOrder ON ConsumeOrder.orderCode = UserConsume.orderCode')
            ->join('Shop ON Shop.shopCode = UserConsume.location')
            ->where(array('UserConsume.consumeCode' => $consumeCode))
            ->find();
        if(! $userConsumeInfo) {
            return array('code' => C('USER_CONSUME.CONSUME_NOT_EXIST'));
        }
        $realPay = $userConsumeInfo['realPay']; // 实际支付金额，单位：分
        $userCode = $userConsumeInfo['consumerCode']; // 用户编码
        $shopCode = $userConsumeInfo['shopCode']; // 商户编码
        $payType = $userConsumeInfo['payType']; // 支付类型
        $ret = true;
        $this->startTrans();
        // 设置账单支付状态为已退款
        $ret = $this->updateConsumeStatus(array('consumeCode' => $consumeCode), array('status' => \Consts::PAY_STATUS_REFUNDED));
        if($ret === false) {
            return array('code' => C('API_INTERNAL_EXCEPTION'));
        }

        //用户支付记录状态变为11.已退款，不可用
        $OrderCouponModel = new OrderCouponModel();
        $result =   $OrderCouponModel->where(array('orderCode'=>$userConsumeInfo['orderCode']))->save(array('status'=>11));
        if($result){
            $ret =  $OrderCouponModel->rollbackCouponRemaining($userConsumeInfo['orderCode']);
        }

        // 设置订单支付状态为已退款
        $consumeOrderMdl = new ConsumeOrderModel();
        $ret = $consumeOrderMdl->updateConsumeOrder(array('orderCode' => $userConsumeInfo['orderCode']), array('status' => \Consts::PAY_STATUS_REFUNDED));

        if($ret === false) {
            return array('code' => C('API_INTERNAL_EXCEPTION'));
        }

        $platBonus = $userConsumeInfo['platBonus'];
        $shopBonus = $userConsumeInfo['shopBonus'];
        $bonusStatisticsMdl = new BonusStatisticsModel();
        if($platBonus > 0) {
            // 回滚用户的平台红包金额
            $ret = $bonusStatisticsMdl->incBonusValue($userCode, C('HQ_CODE'), $platBonus);
            if($ret === false) {
                return array('code' => C('API_INTERNAL_EXCEPTION'));
            }
        }
        if($shopBonus > 0) {
            // 回滚用户的商户红包金额
            $ret = $bonusStatisticsMdl->incBonusValue($userCode, $shopCode, $shopBonus);
            if($ret === false) {
                return array('code' => C('API_INTERNAL_EXCEPTION'));
            }
        }

        $couponUsed = $userConsumeInfo['couponUsed'];
        if($couponUsed > 0) {
            // 回滚优惠券的使用行为
            $userCouponMdl = new UserCouponModel();
            $ret = $userCouponMdl->rollbackUserCouponAction($consumeCode);
            if($ret === false) {
                return array('code' => C('API_INTERNAL_EXCEPTION'));
            }
        }

        $isCard = $userConsumeInfo['isCard'];

        if($isCard == C('YES')) {
            // 回滚会员卡的使用行为
            $cardActionLogMdl = new CardActionLogModel();
            $ret = $cardActionLogMdl->rollbackCardAction($consumeCode);
            if($ret === false) {
                return array('code' => C('API_INTERNAL_EXCEPTION'));
            }
        }
        // 判断支付类型。在线支付，POS机支付，现金支付
//        if($payType == C('UC_PAY_TYPE.BANKCARD') && $userConsumeInfo['status'] == \Consts::PAY_STATUS_PAYED) {
//
//            // 获得使用的银行卡账号
//            $bankAccountLocalLogMdl = new BankAccountLocalLogModel();
//            $bankAccountLocalLogInfo = $bankAccountLocalLogMdl->bankAccountLogConsume($consumeCode);
//            $bankAccountCode = $bankAccountLocalLogInfo['bankAccountCode'];
//            // 回滚银行卡的支付行为。（修改银行卡支付记录的状态为已退款；回滚银行卡消费次数）
//            $ret = $bankAccountLocalLogMdl->rollbackBankAccountPayAction($consumeCode, $bankAccountCode);
//            if($ret === false) {
//                return array('code' => C('API_INTERNAL_EXCEPTION'));
//            }
//
//            // 获得用户信息
//            $userMdl = new UserModel();
//            $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('mobileNbr', 'recomNbr'));
//
//            // 获得公司方流水号
//            $cpmtxsnoLogMdl = new CmptxsnoLogModel();
//            $cpmtxsno = $cpmtxsnoLogMdl->getCmptxsno();
//            if($userConsumeInfo['realPay'] > 0) {
//                $bankAccountMdl = new BankAccountModel();
//                // 获得账号信息
//                $bankAccountInfo = $bankAccountMdl->getBankAccountInfo(array('bankAccountCode' => $bankAccountLocalLogInfo['accountCode']));
//                $shopMdl = new ShopModel();
//                // 获得商家信息（工行入账的商户号，工行入账的商户名称）
//                $shopInfo = $shopMdl->getShopInfo($userConsumeInfo['shopCode'], array('hqIcbcShopNbr'));
//
//                $payTimeStamp = strtotime($userConsumeInfo['payedTime']); // 消费支付时间戳
//                $todayStartStamp = strtotime(date('Y-m-d 00:00:00', time())); // 今天开始时间戳
//                $todayEndStamp = strtotime(date('Y-m-d 23:59:59', time())); // 今天结束时间戳
//
//                $icbcMdl = new IcbcModel();
//                $refundLogMdl = new RefundLogModel();
//                // 如果退款交易日期和消费交易日期是同一天，只能够调用当日的银行卡消费撤销交易，20270
//                if($payTimeStamp >= $todayStartStamp && $payTimeStamp <= $todayEndStamp) {
//                    // 调用工行银行卡消费撤销交易，20270
//                    $pubArr = $icbcMdl->cancelPay($cpmtxsno, $shopInfo['sellerid'], $bankAccountInfo['bankCard'], $userInfo['mobileNbr'] . $bankAccountInfo['accountNbrLast4'], $realPay / \Consts::HUNDRED, $bankAccountInfo['accountName'], $bankAccountInfo['idType'], $bankAccountInfo['idNbr'], $userConsumeInfo['orderNbr']);
//                    //记录退款流水（当天不需要清算）
//                    if($pubArr['retcode'] == C('ICBC_PAY_SUCCESS')){
//                        $refundLogMdl->editRefundLog(
//                            array(
//                                'orderNbr' => $userConsumeInfo['orderNbr'],
//                                'refundPrice' => $realPay,
//                                'refundAccount' => $bankAccountLocalLogInfo['accountCode'],
//                                'createTime' => date('Y-m-d H:i:s', time()),
//                                'refundTime' => date('Y-m-d H:i:s', time()),
//                            )
//                        );
//                    }
//                } else { // 退款交易日期和消费交易日期不在同一天，调用工行信用卡退货交易接口，20240
//                    // 调用工行的信用卡退货交易，20240
////                    $pubArr = $icbcMdl->creditCardReturnGoods($cpmtxsno, $shopInfo['sellerid'], $bankAccountInfo['bankCard'], $userInfo['mobileNbr'] . $bankAccountInfo['accountNbrLast4'], $realPay / \Consts::HUNDRED, $bankAccountInfo['accountName'], $bankAccountInfo['idType'], $bankAccountInfo['idNbr'], $userConsumeInfo['orderNbr'], date('Ymd', $payTimeStamp), '');
//                    $refundLogRet = $refundLogMdl->editRefundLog(
//                        array(
//                            'orderNbr' => $userConsumeInfo['orderNbr'],
//                            'refundPrice' => $realPay,
//                            'refundAccount' => $bankAccountLocalLogInfo['accountCode'],
//                            'LiquidationStatus' => \Consts::LIQUIDATION_STATUS_HAD_NOT
//                        )
//                    );
//                    $pubArr['retcode'] = $refundLogRet['code'] == C('SUCCESS') ? C('ICBC_PAY_SUCCESS') : $refundLogRet['code'];
//                }
//            } else {
//                // 支付等于0元时，不需要向银行请求
//                $ret = $this->reqBankSucc;
//                $ret =  simplexml_load_string($ret);
//                $pubArr = get_object_vars($ret->pub);
//            }
//        } else {
//            $ret = $this->reqBankSucc;
//            $ret =  simplexml_load_string($ret);
//            $pubArr = get_object_vars($ret->pub);
//            F("asdas",$pubArr);
//        }

        if($ret) {
            $this->commit();
            return array('code' => C('SUCCESS'));
        } else {
            $this->rollback();
//            $code = $pubArr['retcode'];
//            $retmsg = $pubArr['retmsg'];
            return array('code' => 20000);
        }
    }


    /**
     * 获得订单当前的支付记录的详情
     * @param string $orderCode 订单编码
     * @return array $consumeInfo 支付详情
     */
    public function getCurrConsumeInfoByOrderCode($orderCode) {
        $consumeInfo = $this
            ->field(array('consumeCode', 'couponUsed', 'realPay', 'deduction', 'shopBonus', 'platBonus', 'bankCardDeduction', 'payType', 'cardDeduction', 'couponDeduction', 'orderNbr', 'payedTime', 'orderAmount'))
            ->join('ConsumeOrder ON ConsumeOrder.orderCode = UserConsume.orderCode')
            ->where(array(
                'UserConsume.orderCode' => $orderCode, // 订单编码
                'UserConsume.status' => array('IN', array(\Consts::PAY_STATUS_PAYED, \Consts::PAY_STATUS_UNPAYED, \Consts::PAY_STATUS_PAYING, \Consts::PAY_STATUS_REFUNDING, \Consts::PAY_STATUS_PART_REFUNDED))) // 账单支付状态在（已支付，未支付，支付中，退款中，已部分退款）中
            )
            ->order('consumeTime desc') // 按时间倒序，找出最近的一条账单记录
            ->find();
        return $consumeInfo;
    }

    /**
     * 获得订单已支付的支付详情
     * @param string $orderCode 订单编码
     * @return array $consumeInfo 支付详情
     */
    public function getPayedConsumeInfoByOrderCode($orderCode) {
        $consumeInfo = $this
            ->field(array('consumeCode', 'couponUsed', 'realPay', 'deduction', 'shopBonus', 'platBonus', 'bankCardDeduction', 'payType', 'firstDeduction', 'consumerCode', 'cardDeduction', 'couponDeduction'))
            ->where(array('orderCode' => $orderCode, 'status' => array('EGT', C('UC_STATUS.PAYED'))))
            ->find();
        return $consumeInfo;
    }

    /**
     * 获得用户消费过的商户编码
     * @param string $userCode 用户编码
     * @return array $shopCodeList 一维数组
     */
    public function listConsumedShopCode($userCode) {
        $shopList = $this
            ->field(array('DISTINCT(location)'))
            ->where(array('status' => C('UC_STATUS.PAYED'), 'consumerCode' => $userCode))
            ->select();
        $shopCodeList = array();
        foreach($shopList as $v) {
            $shopCodeList[] = $v['location'];
        }
        return $shopCodeList;
    }

    /**
     * 计算优惠券抵扣金额和会员卡抵扣金额
     * @param array $condition 条件
     * @return float $couponDeduction 单位：分
     */
    public function calCouponCardDeduction($condition) {
        // 获得消费信息列表
        $consumeList = $this
            ->field(array('orderAmount', 'consumeCode', 'couponUsed', 'isCard'))
            ->join('ConsumeOrder ON ConsumeOrder.orderCode = UserConsume.orderCode')
            ->where($condition)
            ->select();
        $batchCouponMdl = new BatchCouponModel();
        $userCouponMdl = new UserCouponModel();
        $cardActionLogMdl = new CardActionLogModel();
        $cardMdl = new CardModel();
        $couponDeductionSum = $cardDeductionSum = 0; // 设置优惠券抵扣金额，会员卡抵扣金额都为0
        foreach($consumeList as $consume) {
            $consumeCode = $consume['consumeCode'];
            $couponDeduction = $cardDeduction = 0; // 设置优惠券抵扣金额，会员卡抵扣金额都为0

            if($consume['couponUsed'] > 0) { // 如果使用了优惠券，计算优惠券的抵扣金额
                // 获得使用的优惠券的信息
                $userCouponInfo = $userCouponMdl->getUserCouponInfoB(
                    array('UserCoupon.consumeCode' => $consumeCode),
                    array('UserCoupon.userCouponCode', 'BatchCoupon.insteadPrice', 'couponType', 'discountPercent', 'availablePrice', 'UserCoupon.applyTime', 'BatchCoupon.validityPeriod', 'BatchCoupon.expireTime')
                );

                // 计算使用单张优惠券抵扣的金额，单位：分
                $couponDeduction = $batchCouponMdl->calCouponDeduction($consume['orderAmount'], $userCouponInfo[0]['couponType'], $userCouponInfo[0]['insteadPrice'], $userCouponInfo[0]['discountPercent']);
                $couponDeduction = $couponDeduction * $consume['couponUsed'];
            }

            if($consume['isCard'] == 1) { // 如果使用会员卡，计算会员卡的抵扣金额
                $userCardInfo = $cardActionLogMdl->getConsumeCardInfo($consumeCode);
                $baseAmount = $consume['orderAmount'] - $couponDeduction;
                // 计算会员卡的抵扣金额，单位：分
                $cardDeduction = $cardMdl->calCardDeduction($baseAmount, $userCardInfo['discount']);
            }

            $couponDeductionSum += $couponDeduction;
            $cardDeductionSum += $cardDeduction;
        }
        return array('couponDeduction' => $couponDeductionSum, 'cardDeduction' => $cardDeductionSum);
    }

    /**
     * 改变用户消费状态
     * @param string $consumeCode 用户消费编码
     * @param int $status 状态。1-未付款，2-付款中，3-已付款，4-已取消付款，5-付款失败，6-退款中，7-退款成功
     * @return boolean 修改成功返回true，修改失败返回false
     */
    public function changeUserConsumeStatus($consumeCode, $status) {
        return $this->where(array('consumeCode' => $consumeCode))->save(array('status' => $status)) !== false ? true : false;
    }

    /**
     * 获得平台优惠券的使用数量
     * @param array $where 条件
     * @return int $platCouponConsumeAmount 单位：元
     */
    public function countPlatCouponConsumeAmount($where) {
        $platCouponConsumeAmount = $this
            ->join('UserCoupon ON UserCoupon.consumeCode = UserConsume.consumeCode')
            ->where($where)
            ->sum('realPay + deduction');
        return $platCouponConsumeAmount / C('RATIO');
    }

    /**
     * 获得在商家使用平台优惠券的抵扣总金额
     * @param array $where 条件
     * @return int $platCouponDeductionValue 单位：元
     */
    public function countPlatCouponDeductionValue($where) {
        $usedPlatCouponList = $this
            ->field(array(
                'insteadPrice',
                'discountPercent',
                'deduction',
                'realPay'
            ))
            ->join('UserCoupon ON UserCoupon.consumeCode = UserConsume.consumeCode')
            ->join('BatchCoupon ON BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
            ->where($where)
            ->select();
        $platCouponDeductionValue = 0;
        foreach($usedPlatCouponList as $v) {
            if(empty($v['insteadPrice'])) {
                $platCouponDeductionValue += ($v['deduction'] + $v['realPay']) * $v['discountPercent'] / C('RATIO');
            } else {
                $platCouponDeductionValue += $v['insteadPrice'];
            }
        }
        return $platCouponDeductionValue / C('RATIO');
    }

    /**
     * 获得平台优惠券的使用数量
     * @param array $where 条件
     * @return int $platCouponUsedAmount
     */
    public function countPlatCouponUsedAmount($where) {
        $platCouponUsedAmount = $this
            ->join('UserCoupon ON UserCoupon.consumeCode = UserConsume.consumeCode')
            ->where($where)
            ->count('UserConsume.consumeCode');
        return $platCouponUsedAmount;
    }




	/**
     * 在线确认支付（https协议传输）
     * @param string $consumeCode 用户消费记录编码
     * @param string $bankAccountCode 用户关联银行账户编码
     * @param int $valCode 验证码
     * @param int $isUseFirstDeduction 是否要使用首单立减
     * @param string $payChanel 支付通道
     * @return string $ret
     */
	  public function bankcardPayConfirm($consumeCode, $bankAccountCode, $valCode, $isUseFirstDeduction = 1, $payChanel = \Consts::PAY_CHANEL_NULL) {
        if(! $bankAccountCode) {
            return array('code' => C('BANK_ACCOUNT.CODE_EMPTY'));
        }
        // 获得消费信息
        $userConsumeInfo = $this
            ->field(array('shopName', 'Shop.shopCode', 'realPay', 'deduction', 'consumerCode', 'location', 'ConsumeOrder.orderCode', 'orderNbr', 'orderAmount', 'UserConsume.orderCode', 'orderType', 'ConsumeOrder.eatPayType', 'UserConsume.firstDeduction','UserConsume.couponDeduction', 'UserConsume.couponUsed', 'UserConsume.consumeTime', 'UserConsume.shopBonus', 'UserConsume.platBonus', 'UserConsume.identityCode'))
            ->join('ConsumeOrder ON ConsumeOrder.orderCode = UserConsume.orderCode')
            ->join('Shop ON Shop.shopCode = UserConsume.location')
            ->where(array('UserConsume.consumeCode' => $consumeCode, 'UserConsume.status' => C('UC_STATUS.UNPAYED')))
            ->find();

        // 判断消费信息是否存在
        if(! $userConsumeInfo) {
            return array('code' => C('USER_CONSUME.CONSUME_NOT_EXIST'));
        }

        $realPay = $userConsumeInfo['realPay']; // 实际支付金额，单位：分
        $userCode = $userConsumeInfo['consumerCode']; // 用户编码
        $shopName = $userConsumeInfo['shopName']; // 商家名称
        $shopCode = $userConsumeInfo['shopCode']; // 商家编码

        // 获得使用的优惠券的信息
        $userCouponMdl = new UserCouponModel();
        $usedCouponInfo = $userCouponMdl->getUserCouponInfoB(
            array('UserCoupon.consumeCode' => $consumeCode),
            array('UserCoupon.userCouponCode', 'BatchCoupon.insteadPrice', 'couponType', 'discountPercent', 'availablePrice', 'UserCoupon.applyTime', 'BatchCoupon.validityPeriod', 'BatchCoupon.expireTime')
        );

        if(!($usedCouponInfo && $usedCouponInfo['couponType'] == \Consts::COUPON_TYPE_N_PURCHASE)){  // N元购不享受首单立减
            // 获得是否为用户首单
            $userConsumeMdl = new UserConsumeModel();
            $isFirst = $userConsumeMdl->isFirst($userConsumeInfo['consumerCode']);
            // 如果是首单，但没有享受首单立减，则添加首单立减
            if($isFirst == true && $userConsumeInfo['firstDeduction'] == 0 && $isUseFirstDeduction == \Consts::YES) {
                $systemParamMdl = new SystemParamModel();
                // 获得首单立减金额，单位：分
                $mealFirstDecInfo = $systemParamMdl->getParamValue('mealFirstDec');
                $firstDeduction = $mealFirstDecInfo['value'] ? $mealFirstDecInfo['value'] : 0;
                $realPay = $realPay - $firstDeduction;

                // 获得最小支付金额，单位：分
                $minRealPayInfo = $systemParamMdl->getParamValue('minRealPay');
                $minRealPay = $minRealPayInfo['value'];
                if($realPay < $minRealPay) {
                    $realPay = $minRealPay;
                }
                $code = $this->where(array('consumeCode' => $consumeCode))->save(array('realPay' =>$realPay, 'firstDeduction' => $firstDeduction)) !== false ? true : false;
                if($code === false) {
                    return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
                }
            } elseif($isFirst == false && $userConsumeInfo['firstDeduction'] > 0) { // 如果不是首单，但是享受了首单立减，则取消首单立减
                $realPay = $realPay + $userConsumeInfo['firstDeduction'];
                $code = $this->where(array('consumeCode' => $consumeCode))->save(array('realPay' =>$realPay, 'firstDeduction' => 0)) !== false ? true : false;
                if($code === false) {
                    return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
                }
            }
        }
        // 获得商家信息（工行入账的商户号，工行入账的商户名称）
        $shopMdl = new ShopModel();
        $shopInfo = $shopMdl->getShopInfo($userConsumeInfo['shopCode'], array('icbcShopCode', 'icbcShopName', 'hqIcbcShopNbr', 'shopName'));
        // 获得用户信息
        $userMdl = new UserModel();
        $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('mobileNbr', 'recomNbr', 'nickName', 'avatarUrl'));

        if($userConsumeInfo['realPay'] > 0) {
            // 获得银行账户信息
            $bankAccountMdl = new BankAccountModel();
            $bankAccountInfo = $bankAccountMdl->getBankAccountInfo(array('bankAccountCode' => $bankAccountCode));
            // 调用工行银行卡消费交易API
            $icbcMdl = new IcbcModel();
            $pubArr = $icbcMdl->consumptionTransaction($userConsumeInfo['orderNbr'], $shopInfo['sellerid'], $bankAccountInfo['bankCard'], $realPay / (\Consts::HUNDRED), $bankAccountInfo['accountName'], $bankAccountInfo['idType'], $bankAccountInfo['idNbr'], '', $userInfo['mobileNbr'] . $bankAccountInfo['accountNbrLast4'], $valCode, '', '', '', $shopInfo['icbcShopName'], $shopInfo['shopName'], '');
        } else {
            // 支付小于等于0元时，不向银行请求，直接表示支付成功
            $ret = $this->reqBankSucc;
            $ret =  simplexml_load_string($ret);
            $pubArr = get_object_vars($ret->pub);
        }
        if($pubArr['retcode'] == C('ICBC_PAY_SUCCESS')) {
            M()->startTrans();
            // 添加银行卡消费记录，状态为已付款
            $bankAllMdl = new BankAccountLocalLogModel();
            $bankAccountLog = array(
                'accountCode' => $bankAccountCode,
                'consumeAmount' => $userConsumeInfo['deduction'] + $userConsumeInfo['realPay'],
                'actionTime' => date('Y-m-d H:i:s', time()),
                'location' => $userConsumeInfo['location'],
                'consumeCode' => $consumeCode,
                'status' => C('BALL_STATUS.PAYED'),
            );
            $retOfAddBankAccountLocalLog = $bankAllMdl->addBankAccountLocalLog($bankAccountLog);

            // 用户关联银行卡的消费次数+1
            $bankAccountMdl = new BankAccountModel();
            $incConsumeCountRet = $bankAccountMdl->incConsumeCount($bankAccountCode, 1);

            // 更新用户会员卡积分
            $cardActionLogMdl = new CardActionLogModel();
            $cardActionLogInfo = $cardActionLogMdl->getCardActionLogInfo(array('consumeCode' => $consumeCode));
            $userCardMdl = new UserCardModel();
            $retOfUpdatePoint = true;
            if($cardActionLogInfo) {
                $userCardInfo = $userCardMdl->getUserCardInfoByWhere(
                    array('UserCard.userCode' => $cardActionLogInfo['userCode'],'UserCard.cardCode' => $cardActionLogInfo['cardCode']),
                    array('UserCard.point','userCardCode')
                );
                $newUserCardPoint = $userCardInfo['point'] + $userCardInfo['pointsPerCash'] * ($userConsumeInfo['realPay'] + $userConsumeInfo['deduction']) / C('RATIO');
                $retOfUpdatePoint = $userCardMdl->updateUserCardPoint($newUserCardPoint, $userCardInfo['userCardCode']);
            }

            $orderType = $userConsumeInfo['orderType']; // 订单类型
            $consumeOrderMdl = new ConsumeOrderModel();
            if($orderType == \Consts::ORDER_TYPE_TAKE_OUT || ($orderType == \Consts::ORDER_TYPE_NO_TAKE_OUT && $userConsumeInfo['eatPayType'] == C('EAT_PAY_TYPE.BEFORE'))) { // 如果是外卖订单，或者是餐前支付的堂食订单，需要修改订单的订单状态为已下单
                // 修改订单的订单状态为已下单
                $ret = $consumeOrderMdl->updateConsumeOrder(array('orderCode' => $userConsumeInfo['orderCode']), array('orderStatus' => C('FOOD_ORDER_STATUS.ORDERED')));
            } elseif($orderType == \Consts::ORDER_TYPE_COUPON) { // 优惠券订单
                // 修改购买的优惠券状态为可用，并添加券码
                $orderCouponMdl = new OrderCouponModel();
                $ret = $orderCouponMdl->addCouponCode($userConsumeInfo['orderCode']);
            } elseif($orderType == \Consts::ORDER_TYPE_ACT) { // 活动订单
                // 修改活动码的状态为未验证，可用。并添加活动验证码
                $userActCodeMdl = new UserActCodeModel();
                $ret = $userActCodeMdl->addActCode($userConsumeInfo['orderCode']);
            }

            // 更新订单支付状态为已支付
            $upOrderStatusRet = $consumeOrderMdl->updateConsumeOrder(array('orderCode' => $userConsumeInfo['orderCode']), array('status' => \Consts::PAY_STATUS_PAYED));

            // 更新用户支付状态为已支付和支付完成时间
            $retUserConsume = $this->updateConsumeStatus(array('consumeCode' => $consumeCode), array('status' => \Consts::PAY_STATUS_PAYED, 'payedTime' => date('Y-m-d H:i:s')));
            if($retOfAddBankAccountLocalLog == true && $incConsumeCountRet == true && $retOfUpdatePoint == true && $upOrderStatusRet == true && $retUserConsume == true) {
                M()->commit();
// 获得用户在该商户的下单并且支付成功过的次数
                $orderCount = $consumeOrderMdl->countOrder(array('clientCode' => $userCode, 'shopCode' => $shopCode, 'ConsumeOrder.status' => array('IN', array(\Consts::PAY_STATUS_PAYED, \Consts::PAY_STATUS_REFUNDING, \Consts::PAY_STATUS_REFUNDED, \Consts::PAY_STATUS_PART_REFUNDED))));
                // 增加商家回头客人数
                $shopMdl->incRepeatCustomersTransaction($shopCode, $orderCount);

                // 判断用户是否有该商店的会员卡，没有则添加
                $userCardMdl->checkUserCard($userCode, $shopCode);

                // 执行平台给推荐人送优惠券的一系列动作（判断用户有无推荐人；若有，判断用户的推荐人是否已经得到奖励的优惠券；）
                $userCouponMdl->sendRecomClientCoupon($userInfo['recomNbr']);

                $batchCouponMdl = new BatchCouponModel();
                // 执行商家送优惠券的动作
                $sendCouponRet = $batchCouponMdl->sendCoupon($userCode, $shopCode, $userConsumeInfo['orderAmount']);
                // 如果赠送了优惠券，在支付记录中记录赠送的优惠券编码(可能多张，以“|”分隔)
                if($sendCouponRet['code'] == C('SUCCESS')) {
                    $this->where(array('consumeCode' => $consumeCode))->save(array('userCouponCode' => $sendCouponRet['userCouponCode']));
                }

                $stringUsedUserCouponCode = $this->where(array('consumeCode' => $consumeCode))->getField('usedUserCouponCode');
                $arrayUsedUserCouponCode = explode('|', $stringUsedUserCouponCode);
                // 给用户添加用户使用优惠券的消息
                if($arrayUsedUserCouponCode){
                    foreach($arrayUsedUserCouponCode as $userCouponCode){
                        $msgInfo = array(
                            'title' => str_replace('{{shopName}}', $shopName, C('MSG_TITLE_TDL.USE_COUPON')),
                            'content' => C('COUPON_MSG_TDL.USED'),
                            'createTime' => date('Y-m-d H:i:s'),
                            'senderCode' => $userConsumeInfo['shopCode'],
                            'type' => C('MESSAGE_TYPE.COUPON'),
                            'userCouponCode' => $userCouponCode,
                        );
                        $msgMdl = new MessageModel();
                        $msgMdl->addMsg($msgInfo, $userCode);
                    }

                    // 使用优惠券获得圈值记录，并更新用户的历史圈值和当前圈值
                    $userMdl = new UserModel();
                    $userMdl->addPointEarningLog($userCode, 20, '使用优惠券');
                }
                $smsMdl = new SmsModel();
                //买券用户短信
                if($userConsumeInfo['orderType'] == \Consts::ORDER_TYPE_COUPON){
                    $orderCouponMdl = new OrderCouponModel();
                    $joinTableArr = array(
                        array(
                            'joinTable' => 'BatchCoupon',
                            'joinCondition' => 'BatchCoupon.batchCouponCode = OrderCoupon.batchCouponCode',
                            'joinType' => 'inner'
                        ),
                    );
                    $orderCouponList = $orderCouponMdl->listOrderCoupon(array('orderCode' => $userConsumeInfo['orderCode'], 'status' => \Consts::ORDER_COUPON_STATUS_USE), array('couponCode', 'BatchCoupon.function', 'BatchCoupon.insteadPrice', 'BatchCoupon.couponType', 'BatchCoupon.expireTime'), $joinTableArr);
                    if($orderCouponList[0]['couponType'] == \Consts::COUPON_TYPE_EXCHANGE){
                        $function = $orderCouponList[0]['function'];
                        $insteadPrice = '';
                        $couponType = '兑换券';
                    }else{
                        $function = '';
                        $insteadPrice = $orderCouponList[0]['insteadPrice'] / \Consts::HUNDRED;
                        $insteadPrice .= '元';
                        $couponType = '代金券';
                    }
                    $couponCodeString = '';
                    for($couponIndex = 0; $couponIndex < count($orderCouponList); $couponIndex++){
                        if($couponCodeString){
                            $couponCodeString .= ', “'.$orderCouponList[$couponIndex]['couponCode'].'”';
                        }else{
                            $couponCodeString .= '“'.$orderCouponList[$couponIndex]['couponCode'].'”';
                        }

                    }
                    $expireTime = $orderCouponList[0]['expireTime'];
                    $message = str_replace(array('{{shopName}}', '{{userCount}}', '{{function}}', '{{insteadPrice}}', '{{couponType}}', '{{couponCodeString}}', '{{expireTime}}'), array($shopInfo['shopName'], count($orderCouponList), $function, $insteadPrice, $couponType, $couponCodeString, $expireTime), C('SEND_MESSAGE.PAY_COUPON'));
                    try{
                        $smsMdl->send($message, $userInfo['mobileNbr']);
                    }catch (RPCException $e){
                    }
                }else{
                    // 推送消息
                    $content = str_replace('{{realPay}}', $realPay, C('SHOP_PUSH_MSG_TDL.PAYED'));
                    $receiver = explode('-', $userConsumeInfo['shopCode']);
                    $jpushMdl = new JpushModel(C('SHOP_APP_KEY'), C('SHOP_MASTER_SECRET'));
                    $point = 0;
                    $cardActionLogMdl = new CardActionLogModel();
                    $cardAction = $cardActionLogMdl->getConsumeCardInfo($consumeCode);
                    if($cardAction){
                        $point = $cardAction['pointsPerCash'] * $cardAction['consumeAmount'] / C('RATIO');
                    }
                    $ccInfo = array(
                        'avatarUrl' => $userInfo['avatarUrl'],
                        'totalPay' => ($userConsumeInfo['orderAmount']) / C('RATIO'),
                        'realPay' => $userConsumeInfo['realPay'] / C('RATIO'),
                        'bonusPay' => ($userConsumeInfo['shopBonus'] + $userConsumeInfo['platBonus']) / C('RATIO'),
                        'couponPay' => $userConsumeInfo['couponDeduction'] / \Consts::HUNDRED,
                        'couponUsed' => $userConsumeInfo['couponUsed'],
                        'identityCode' => $userConsumeInfo['identityCode'],
                        'point' => $point,
                        'consumeTime' => $userConsumeInfo['consumeTime']
                    );
                    $jpushMdl->jPushByAction($receiver, $content, $ccInfo, C('PUSH_ACTION.CONSUME'));

                    if($payChanel == \Consts::PAY_CHANEL_QR_CODE){    //扫码支付推送给用户
                        $jpushMdl = new JpushModel(C('CLIENT_APP_KEY'), C('CLIENT_MASTER_SECRET'));
                        $ccInfo = array('webUrl' => 'Browser/paySucc?consumeCode='.$consumeCode.'&payChanel='.$payChanel);
                        $jpushMdl->jPushByAction(array($userInfo['mobileNbr']), '', $ccInfo, C('PUSH_ACTION.CONSUME'));
                    }

                    // 向商家发短信
                    //1.员工级别：店长或大店长，活跃状态，短信接收设置为开启状态
                    $shopStaffRelMdl = new ShopStaffRelModel();
                    $shopStaffArr = $shopStaffRelMdl->getStaffList(array('ShopStaffRel.shopCode' => $userConsumeInfo['shopCode'], 'ShopStaff.status' => \Consts::SHOP_STAFF_STATUS_ACTIVE, 'ShopStaff.userLvl' => array('gt', \Consts::SHOP_STAFF_LVL_EMPLOYEE), 'ShopStaff.isSendPayedMsg' => C('YES')), array('ShopStaff.mobileNbr'));
                    //2.店长设置的其他员工
                    $mrMdl = new MessageRecipientModel();
                    $mRecipient = $mrMdl->getMRecipientList(array('shopCode' => $userConsumeInfo['shopCode']), array('mobileNbr'));
                    $shopStaffArr = array_merge($shopStaffArr, $mRecipient);
                    if($shopStaffArr){
                        $shopStaffMobileArr = array();
                        foreach($shopStaffArr as $v){
                            if($v['mobileNbr'] && !in_array($v['mobileNbr'], $shopStaffMobileArr)){
                                array_push($shopStaffMobileArr, $v['mobileNbr']);
                            }
                        }
                        if($shopStaffMobileArr) {
                            $message = str_replace(
                                array('{{userName}}', '{{orderNbr}}', '{{realPay}}', '{{orderAmount}}', '{{deduction}}'),
                                array($userInfo['nickName'], substr($userConsumeInfo['orderNbr'],-5), number_format(($realPay / C('RATIO')), 2, '.', ''), number_format(($userConsumeInfo['orderAmount'] / C('RATIO')), 2, '.', ''), number_format(($userConsumeInfo['deduction'] / C('RATIO')), 2, '.', '')),
                                C('SEND_MESSAGE.S_PAYED')
                            );
                            try{
                                $smsMdl->send($message, $shopStaffMobileArr);
                            }catch (RPCException $e){
                                Log::write('---------SEND MESSAGE FAIL:'.$e->message.'---------', 'WARN', '', C('LOG_PATH').'sendMsg'.date('y_m_d').'.log');
                            }
                        }
                    }
                }
                $code = C('SUCCESS');
            } else {
                M()->rollback();
                $code = C('API_INTERNAL_EXCEPTION');
            }
			$userCouponModel=D('UserCoupon');
			$data->status=2;
			$arr=$userCouponModel->where("consumeCode='$consumeCode'")->save($data);
            return array('code' => $code, 'orderCode' => $userConsumeInfo['orderCode'],'batchCouponCode'=>$batchCouponCode);
        } else {
            $code = $pubArr['retcode'];
            $retmsg = C('ICBC_ERROR_CODE_MSG.' . $pubArr['retcode']) ? C('ICBC_ERROR_CODE_MSG.' . $pubArr['retcode']) : $pubArr['retmsg'];
            return array('code' => $code ? $code : '', 'retmsg' => $retmsg ? $retmsg : '');
        }
    }




    /**
     * 在线确认支付（https协议传输）不需要银行卡 验证码方式
     * @param string $consumeCode 用户消费记录编码
     * @param string $bankAccountCode 用户关联银行账户编码
     * @param int $valCode 验证码
     * @param int $isUseFirstDeduction 是否要使用首单立减
     * @param string $payChanel 支付通道
     * @return string $ret
     */
	  public function bankcardPayConfirm2($consumeCode, $isUseFirstDeduction = 1, $payChanel = \Consts::PAY_CHANEL_NULL) {

        // 获得消费信息
        $userConsumeInfo = $this
            ->field(array('shopName',
                'Shop.shopCode',
                'realPay',
                'deduction',
                'consumerCode',
                'location',
                'ConsumeOrder.orderCode',
                'orderNbr',
                'orderAmount',
                'UserConsume.orderCode',
                'orderType',
                'ConsumeOrder.eatPayType',
                'UserConsume.firstDeduction',
                'UserConsume.couponDeduction',
                'UserConsume.couponUsed',
                'UserConsume.consumeTime',
                'UserConsume.shopBonus',
                'UserConsume.platBonus',
                'UserConsume.identityCode'))
            ->join('ConsumeOrder ON ConsumeOrder.orderCode = UserConsume.orderCode')
            ->join('Shop ON Shop.shopCode = UserConsume.location')
            ->where(array('UserConsume.consumeCode' => $consumeCode, 'UserConsume.status' => C('UC_STATUS.UNPAYED')))
            ->find();
          // 判断消费信息是否存在
        if(! $userConsumeInfo) {
            return array('code' => C('USER_CONSUME.CONSUME_NOT_EXIST'));
        }
        $userCode = $userConsumeInfo['consumerCode']; // 用户编码
        $shopCode = $userConsumeInfo['shopCode']; // 商家编码
        $shopMdl = new ShopModel();
        M()->startTrans();
            // 更新用户会员卡积分
//            $cardActionLogMdl = new CardActionLogModel();
//            $cardActionLogInfo = $cardActionLogMdl->getCardActionLogInfo(array('consumeCode' => $consumeCode));
//            $userCardMdl = new UserCardModel();
//            $retOfUpdatePoint = true;
//            if($cardActionLogInfo) {
//                $userCardInfo = $userCardMdl->getUserCardInfoByWhere(
//                    array('UserCard.userCode' => $cardActionLogInfo['userCode'],'UserCard.cardCode' => $cardActionLogInfo['cardCode']),
//                    array('UserCard.point','userCardCode')
//                );
//                $newUserCardPoint = $userCardInfo['point'] + $userCardInfo['pointsPerCash'] * ($userConsumeInfo['realPay'] + $userConsumeInfo['deduction']) / C('RATIO');
//                $retOfUpdatePoint = $userCardMdl->updateUserCardPoint($newUserCardPoint, $userCardInfo['userCardCode']);
//            }
        $orderType = $userConsumeInfo['orderType']; // 订单类型
        $consumeOrderMdl = new ConsumeOrderModel();
        if($orderType == \Consts::ORDER_TYPE_COUPON) { // 订单为优惠券订单，修改购买的优惠券状态为可用，并添加券码
            $orderCouponMdl = new OrderCouponModel();
            $ret = $orderCouponMdl->addCouponCode($userConsumeInfo['orderCode']);
            //c如果该次购买参加活动，更改让利状态
            $activityCode = M('consumeorder')->field('activityCode')->where(array('orderCode'=>$userConsumeInfo['orderCode']))->find();
            if($activityCode && !empty($activityCode['activityCode'])){
                 M('amountdiscount')->where(array('orderCode' => $userConsumeInfo['orderCode']))->setField('status',2);
            }
        }
        //更新订单支付状态为已支付
        $upOrderStatusRet = $consumeOrderMdl->updateConsumeOrder(array('orderCode' => $userConsumeInfo['orderCode']), array('status' => \Consts::PAY_STATUS_PAYED));
        //更新用户支付状态为已支付和支付完成时间
        $retUserConsume = $this->updateConsumeStatus(array('consumeCode' => $consumeCode), array('status' => \Consts::PAY_STATUS_PAYED, 'payedTime' => date('Y-m-d H:i:s')));
        if($upOrderStatusRet == true && $retUserConsume == true) {
            M()->commit();
            //获得用户在该商户的下单并且支付成功过的次数
            $orderCount = $consumeOrderMdl->countOrder(array('clientCode' => $userCode, 'shopCode' => $shopCode, 'ConsumeOrder.status' => array('IN', array(\Consts::PAY_STATUS_PAYED, \Consts::PAY_STATUS_REFUNDING, \Consts::PAY_STATUS_REFUNDED, \Consts::PAY_STATUS_PART_REFUNDED))));
            //增加商家回头客人数
            $shopMdl->incRepeatCustomersTransaction($shopCode, $orderCount);
            //判断用户是否有该商店的会员卡，没有则添加
            //$userCardMdl->checkUserCard($userCode, $shopCode);
            $code = C('SUCCESS');
        } else {
            M()->rollback();
            $code = C('API_INTERNAL_EXCEPTION');
        }
        $userCouponModel=D('UserCoupon');
        $data['status']=2;
        $userCouponModel->where("consumeCode='$consumeCode'")->save($data);
        return array('code' => $code, 'orderCode' => $userConsumeInfo['orderCode']);
    }

    /**
     * 获取工银快捷支付验证码
     * @param string $consumeCode 用户消费记录编码
     * @param string $bankAccountCode 用户关联银行账户编码
     * @param string $mobileNbr 手机号码
     * @return array
     */
    public function getIcbcPayValCode($consumeCode, $bankAccountCode, $mobileNbr) {
        if(! $bankAccountCode) {
            return array('code' => C('BANK_ACCOUNT.CODE_EMPTY'));
        }
        // 获得支付记录信息
        $userConsumeInfo = $this
            ->field(array('location', 'deduction', 'realPay', 'ConsumeOrder.orderNbr'))
            ->join('ConsumeOrder ON ConsumeOrder.orderCode = UserConsume.orderCode')
            ->where(array('consumeCode' => $consumeCode, 'UserConsume.status' => C('UC_STATUS.UNPAYED')))
            ->find();
        if(! $userConsumeInfo) { // 如果支付记录不存在，返回错误
            return $this->getBusinessCode(C('USER_CONSUME.CONSUME_NOT_EXIST'));
        }

        $realPay = $userConsumeInfo['realPay']; // 实际支付金额，单位：分
        $userCode = $userConsumeInfo['consumerCode']; // 用户编码
        $shopName = $userConsumeInfo['shopName']; // 商家名称
        $shopCode = $userConsumeInfo['shopCode']; // 商家编码

        $userCouponMdl = new UserCouponModel();
        $usedCouponList = $userCouponMdl->getUserCouponInfoB(
            array('UserCoupon.consumeCode' => $consumeCode),
            array('UserCoupon.userCouponCode', 'BatchCoupon.insteadPrice', 'couponType', 'discountPercent', 'availablePrice', 'UserCoupon.applyTime', 'BatchCoupon.validityPeriod', 'BatchCoupon.expireTime')
        );

        if(!($usedCouponList && $usedCouponList[0]['couponType'] == C('COUPON_TYPE.N_PURCHASE'))){  // N元购不享受首单立减
            // 获得是否为用户首单
            $userConsumeMdl = new UserConsumeModel();
            $isFirst = $userConsumeMdl->isFirst($userConsumeInfo['consumerCode']);
            // 如果是首单，但没有享受首单立减，则添加首单立减
            if($isFirst == true && $userConsumeInfo['firstDeduction'] == 0) {
                $systemParamMdl = new SystemParamModel();
                // 获得首单立减金额，单位：分
                $mealFirstDecInfo = $systemParamMdl->getParamValue('mealFirstDec');
                $firstDeduction = $mealFirstDecInfo['value'] ? $mealFirstDecInfo['value'] : 0;
                $realPay = $realPay - $firstDeduction;

                // 获得最小支付金额，单位：分
                $minRealPayInfo = $systemParamMdl->getParamValue('minRealPay');
                $minRealPay = $minRealPayInfo['value'];
                if($realPay < $minRealPay) {
                    $realPay = $minRealPay;
                }

                $code = $this->where(array('consumeCode' => $consumeCode))->save(array('realPay' =>$realPay, 'firstDeduction' => $firstDeduction)) !== false ? true : false;
                if($code === false) {
                    return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
                }
            } elseif($isFirst == false && $userConsumeInfo['firstDeduction'] > 0) { // 如果不是首单，但是享受了首单立减，则取消首单立减
                $realPay = $realPay + $userConsumeInfo['firstDeduction'];
                $code = $this->where(array('consumeCode' => $consumeCode))->save(array('realPay' =>$realPay, 'firstDeduction' => 0)) !== false ? true : false;
                if($code === false) {
                    return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
                }
            }
        }

        $bankAccountMdl = new BankAccountModel();
        // 获得银行账户信息
        $bankAccountInfo = $bankAccountMdl->getBankAccountInfo(array('bankAccountCode' => $bankAccountCode));
        if( ! $bankAccountInfo) { // 如果银行账户不存在，返回错误
            return $this->getBusinessCode(C('BANK_ACCOUNT.CODE_ERROR'));
        }
        if($bankAccountInfo['mobileNbr'] != $mobileNbr) { // 银行账户的预留手机号与用户输入手机号不同，返回错误
            return $this->getBusinessCode(C('MOBILE_NBR.RESERVED_NOT_SAME'));
        }
        // 请求工行获取验证码的API
        $userMdl = new UserModel();
        // 获得用户信息（手机号码）
        $userInfo = $userMdl->getUserInfo(array('userCode' => $bankAccountInfo['userCode']), array('mobileNbr'));
        $shopMdl = new ShopModel();
        // 获得商家信息（工行入账的商户号）
        $shopInfo = $shopMdl->getShopInfo($userConsumeInfo['location'], array('icbcShopCode', 'hqIcbcShopNbr'));

        $cpmtxsnoLogMdl = new CmptxsnoLogModel();
        // 获得公司方流水号
        $cpmtxsno = $cpmtxsnoLogMdl->getCmptxsno();
        $icbcMdl = new IcbcModel();
        $pubArr = $icbcMdl->getMsgValCode($cpmtxsno, $shopInfo['sellerid'], $bankAccountInfo['bankCard'], $bankAccountInfo['accountName'], $realPay / (\Consts::HUNDRED), $bankAccountInfo['idType'], $bankAccountInfo['idNbr'], $mobileNbr, $userConsumeInfo['orderNbr'], $userInfo['mobileNbr'] . $bankAccountInfo['accountNbrLast4']);
        if($pubArr['retcode'] == C('ICBC_PAY_SUCCESS')) {
            return array('code' => C('SUCCESS'), 'valCode' => '');
        } else {
            return array('code' => $pubArr['retcode'] ? $pubArr['retcode'] : '', 'retmsg' => $pubArr['retmsg']);
        }
    }

    /**
     * 现金支付
     * @param string $userCode 用户编码
     * @param string $shopCode 商店编码
     * @param string $userCouponCode 用户优惠券编码
     * @param int $platBonus 消费金额 单位：元
     * @param int $shopBonus 消费金额 单位：元
     * @param int $price 消费金额 单位：元
     * @return array
     */
    public function cashPay($userCode, $shopCode, $userCouponCode, $platBonus, $shopBonus, $price) {
        $rules = array(
            array('userCode', 'require', C('USER.USER_CODE_EMPTY'), 1),
            array('shopCode', 'require', C('SHOP.SHOP_CODE_EMPTY'), 1),
            array('price', 'require', C('PAY.PRICE_EMPTY'), 1),
            array('price', 'is_numeric', C('PAY.PRICE_ERROR'), 1, 'function'),
        );
        $data = array(
            'userCode' => $userCode,
            'shopCode' => $shopCode,
            'price' => $price
        );

        if($this->validate($rules)->create($data)) {
            $userMdl = new UserModel();
            $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('nickName','realName','mobileNbr','avatarUrl'));
            if(!$userInfo){
                return $this->getBusinessCode(C('USER.USER_CODE_ERROR'));
            }
            $userName = $userInfo['realName']?$userInfo['realName']:$userInfo['nickName'];
            $shopMdl = new ShopModel();
            // 获得商家信息（商户名称，商户联系号码）
            $shopInfo = $shopMdl->getShopInfo($shopCode, array('Shop.shopName','Shop.mobileNbr'));
            if(!$shopInfo)
                return $this->getBusinessCode(C('SHOP.SHOP_CODE_ERROR'));

            $price = $price * C('RATIO');
            $realPay = $price;
            $platBonus = $platBonus * C('RATIO');
            $shopBonus = $shopBonus * C('RATIO');

            $consumeCode = $this->create_uuid();

            $userCardMdl = new UserCardModel();
            $userCard = $userCardMdl->getBestUserCard($userCode, $shopCode);
            if($userCard){
                $userCardCode = $userCard['userCardCode'];
                $field = array(
                    'UserCard.point',
                    'Card.discount',
                    'Card.discountRequire',
                    'Card.pointsPerCash',
                    'Card.cardCode',
                );
                $userCardInfo = $userCardMdl->getUserCardInfo($userCardCode, $field);
            }

            M()->startTrans();
            $consumeOrderMdl = new ConsumeOrderModel();
            $addOrder = $consumeOrderMdl->addConsumeOrder($userCode, $price, $shopCode, C('UC_PAY_TYPE.CASH'));
            if($addOrder['code'] != C('SUCCESS')){
                return $addOrder;
            }

            $userCouponMdl = new UserCouponModel();
            if($userCouponCode){
                $userCouponCodeArr = explode('|', $userCouponCode);
                // 判断用户优惠券是否可用
                foreach($userCouponCodeArr as $k=>$uc){
                    if(empty($uc)){
                        unset($userCouponCodeArr[$k]);
                    }else{
                        $isUserCouponCanUse = $userCouponMdl->isUserCouponCanBeUsed($uc, $price, $userCode);
                        if($isUserCouponCanUse !== true) {
                            return $this->getBusinessCode($isUserCouponCanUse);
                        }
                    }
                }
            }

            $retOfUpdatePoint = true;
            $retOfAddCardAction = true;

            if(isset($userCouponCodeArr) && $userCouponCodeArr){
                $userCouponInfo = $userCouponMdl->getCouponInfo($userCouponCodeArr[0], array('couponType', 'insteadPrice', 'payPrice', 'userCouponNbr'));
            }else{
                $userCouponInfo = array();
            }
            if($userCouponInfo && $userCouponInfo['couponType'] == C('COUPON_TYPE.N_PURCHASE')){ // N元购型优惠券的使用
                $realPay = $userCouponInfo['insteadPrice'];
                if(isset($userCardInfo) && $userCardInfo){
                    $userCardPoint = $userCardInfo['point'];
                    // 更新用户会员卡积分 (这个需要在商家端返回支付成功后再更新)
                    $newUserCardPoint = $userCardPoint + $userCardInfo['pointsPerCash'] * $price / C('RATIO');
                    $retOfUpdatePoint = $userCardMdl->updateUserCardPoint($newUserCardPoint, $userCardCode);

                    $cardActionLogMdl = new CardActionLogModel();
                    // 添加用户会员卡使用记录
                    $cardActionLog = array(
                        'logCode' => $this->create_uuid(),
                        'userCode' => $userCode,
                        'cardCode' => $userCardInfo['cardCode'],
                        'userCardCode' => $userCardCode,
                        'consumeAmount' => $price,
                        'actionTime' => date('Y-m-d H:i:s', time()),
                        'consumeCode' => $consumeCode,
                        'actionContent'=>'买单-现金支付',
                    );
                    $retOfAddCardAction = $cardActionLogMdl->addCardActionLog($cardActionLog);
                }
            }else{ // 其他类型优惠券或不使用优惠券
                $couponDeduction = 0;
                if(isset($userCouponCodeArr) && $userCouponCodeArr){
                    // 使用优惠券，得到实际付款金额
                    $realPay = $userCouponMdl->getRealPay($userCouponCodeArr[0], $realPay, count($userCouponCodeArr));
                    $couponDeduction = $price - $realPay;
                }

                $cardInsteadPrice = 0;
                if(isset($userCardInfo) && $userCardInfo) {
                    // 使用会员卡，得到实际付款金额
                    $userCardPoint = $userCardInfo['point'];
                    $cardActionLogMdl = new CardActionLogModel();
                    // 更新用户会员卡积分 (这个需要在商家端返回支付成功后再更新)
                    $newUserCardPoint = $userCardPoint + $userCardInfo['pointsPerCash'] * $price / C('RATIO');
                    $retOfUpdatePoint = $userCardMdl->updateUserCardPoint($newUserCardPoint, $userCardCode);
                    // 添加用户会员卡使用记录
                    $cardActionLog = array(
                        'logCode' => $this->create_uuid(),
                        'userCode' => $userCode,
                        'cardCode' => $userCardInfo['cardCode'],
                        'userCardCode' => $userCardCode,
                        'consumeAmount' => $price,
                        'actionTime' => date('Y-m-d H:i:s', time()),
                        'consumeCode' => $consumeCode,
                        'actionContent'=>'买单-现金支付',
                    );
                    $retOfAddCardAction = $cardActionLogMdl->addCardActionLog($cardActionLog);

                    if($userCardPoint >= $userCardInfo['discountRequire']){
                        $cardInsteadPrice = $realPay - $realPay * $userCardInfo['discount'] / 10;
                    }
                }
                $cardInsteadPrice = ceil($cardInsteadPrice);
                $realPay = $realPay - $cardInsteadPrice;

                $bsMdl = new BonusStatisticsModel();
                if($platBonus > 0){
                    // 使用平台红包
                    $platBonusRet = $bsMdl->reduceBonusStatistics($userCode, C('HQ_CODE'), $platBonus);
                    if($platBonusRet['code'] == C('SUCCESS')){
                        $realPay = $realPay - ceil($platBonus);
                    }else{
                        return $platBonusRet;
                    }
                }

                if($shopBonus > 0){
                    // 使用商家红包
                    $shopBonusRet = $bsMdl->reduceBonusStatistics($userCode, $shopCode, $shopBonus);
                    if($shopBonusRet['code'] == C('SUCCESS')){
                        $realPay = $realPay - ceil($shopBonus);
                    }else{
                        return $shopBonusRet;
                    }
                }
            }

            if($userCouponInfo){
                // 改变用户优惠券状态
                $userCouponAction = array(
                    'status' => C('USER_COUPON_STATUS.USED'),
                    'consumeCode' => $consumeCode
                );
                $retOfUseCoupon = $userCouponMdl->useCoupon($userCouponCodeArr, $userCouponAction);
                if( ! $retOfUseCoupon)
                    return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
            }

            // 添加消费记录
            $consumeLog = array(
                'consumeCode' => $consumeCode,
                'consumerCode' => $userCode,
                'isCard' => isset($cardInsteadPrice) && $cardInsteadPrice != 0 ? C('YES') : C('NO'),
                'couponUsed' => isset($userCouponCodeArr) ? count($userCouponCodeArr) : C('NO'),
                'consumeTime' => date('Y-m-d H:i:s', time()),
                'payedTime' => date('Y-m-d H:i:s', time()),
                'identityCode' => substr(md5(uniqid(mt_rand(), true)), 0, 12),
                'deduction' => ($price - $realPay),
                'realPay' => $realPay,
                'cardDeduction' => isset($cardInsteadPrice) ? $cardInsteadPrice : C('NO'),
                'couponDeduction' => isset($couponDeduction) ? $couponDeduction : C('NO'),
                'location' => $shopCode,
                'status' => C('UC_STATUS.PAYED'),
                'payType' => C('UC_PAY_TYPE.CASH'),
                'orderCode' => $addOrder['orderCode'],
                'platBonus' => $platBonus,
                'shopBonus' => $shopBonus,
                'usedCardCode' => isset($userCardInfo['cardCode']) ? $userCardInfo['cardCode'] : '',
                'usedUserCouponCode' => $userCouponCode,
            );
            $retOfAddUserConsume = $this->add($consumeLog) !== false ? true : false;
            if(! ($retOfAddUserConsume && $retOfUpdatePoint &&  $retOfAddCardAction)) {
                M()->rollback();
                return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
            } else {
                M()->commit();
                $ret = $this->getBusinessCode(C('SUCCESS'));
                $ret['consumeCode'] = $consumeCode;
                $ret['orderCode'] = $addOrder['orderCode'];

                //优惠券消息
                if($consumeLog['couponUsed']){
                    // 给用户添加用户使用优惠券的消息
                    foreach($userCouponCodeArr as $v){
                        $msgInfo = array(
                            'title' => str_replace('{{shopName}}', $shopInfo['shopName'], C('MSG_TITLE_TDL.USE_COUPON')),
                            'content' => C('COUPON_MSG_TDL.USED'),
                            'createTime' => date('Y-m-d H:i:s'),
                            'senderCode' => $shopCode,
                            'type' => C('MESSAGE_TYPE.COUPON'),
                            'userCouponCode' => $v,
                        );
                        $msgMdl = new MessageModel();
                        $msgMdl->addMsg($msgInfo, $userCode);
                    }

                    // 使用优惠券获得圈值记录，并更新用户的历史圈值和当前圈值
                    $userMdl = new UserModel();
                    $userMdl->addPointEarningLog($userCode, 20, '使用优惠券');
                }

                // 满就送，派发一张优惠券
                $batchCouponMdl = new BatchCouponModel();
                $sendCouponRet = $batchCouponMdl->sendCoupon($userCode, $shopCode, $price);
                if($sendCouponRet['code'] == C('SUCCESS')){
                    $this->where(array('consumeCode'=>$consumeCode))->save(array('userCouponCode'=>$sendCouponRet['userCouponCode']));
                }

                // 推送消息：给商家推送买单成功
                $content = str_replace('{{realPay}}', number_format(($realPay / C('RATIO')), 2, '.', ''), C('SHOP_PUSH_MSG_TDL.PAYED'));
                $receiver = explode('-', $shopCode);
                $jpushMdl = new JpushModel(C('SHOP_APP_KEY'), C('SHOP_MASTER_SECRET'));
                $point = 0;
                $cardActionLogMdl = new CardActionLogModel();
                $cardAction = $cardActionLogMdl->getConsumeCardInfo($consumeCode);
                if($cardAction){
                    $point = $cardAction['pointsPerCash'] * $cardAction['consumeAmount'] / C('RATIO');
                }
                $ccInfo = array(
                    'avatarUrl' => $userInfo['avatarUrl'],
                    'totalPay' => $price / C('RATIO'),
                    'realPay' => $consumeLog['realPay'] / C('RATIO'),
                    'bonusPay' => ($consumeLog['shopBonus'] + $consumeLog['platBonus']) / C('RATIO'),
                    'couponPay' => $consumeLog['couponDeduction'] / \Consts::HUNDRED,
                    'couponUsed' => $consumeLog['couponUsed'],
                    'identityCode' => $consumeLog['identityCode'],
                    'point' => $point,
                    'consumeTime' => $consumeLog['consumeTime']
                );
                $jpushMdl->jPushByAction($receiver, $content, $ccInfo, C('PUSH_ACTION.CONSUME'));

                // 推送消息：给用户推送买的兑换券或代金券的使用
                if($userCouponInfo && in_array($userCouponInfo['couponType'], array(\Consts::COUPON_TYPE_EXCHANGE, \Consts::COUPON_TYPE_VOUCHER)) && $userCouponInfo['payPrice'] > 0){
                    $userCount = isset($userCouponCodeArr) ? count($userCouponCodeArr) : 1;
                    $couponType = $userCouponInfo['couponType'] == \Consts::COUPON_TYPE_EXCHANGE ? '兑换券' : '代金券';
                    $content = str_replace(array('{{userCount}}', '{{shopName}}', '{{couponType}}'), array($userCount, $shopInfo['shopName'], $couponType), C('PUSH_MESSAGE.PAY_COUPON_USE'));
                    $jpushMdl = new JpushModel(C('CLIENT_APP_KEY'), C('CLIENT_MASTER_SECRET'));
                    $extra = array(
                        'couponCode' => $userCouponInfo['userCouponNbr'],
                    );
                    $jpushMdl->jPushByAction(array($userInfo['mobileNbr']), $content, $extra, C('PUSH_ACTION.PAY_COUPON_USE'));
                }

                // 向商家发短信
                //1.员工级别：店长或大店长，活跃状态，短信接收设置为开启状态
                $shopStaffRelMdl = new ShopStaffRelModel();
                $shopStaffArr = $shopStaffRelMdl->getStaffList(array('ShopStaffRel.shopCode' => $shopCode, 'ShopStaff.status' => \Consts::SHOP_STAFF_STATUS_ACTIVE, 'ShopStaff.userLvl' => array('gt', \Consts::SHOP_STAFF_LVL_EMPLOYEE), 'ShopStaff.isSendPayedMsg' => C('YES')), array('ShopStaff.mobileNbr'));
                //2.店长设置的其他员工
                $mrMdl = new MessageRecipientModel();
                $mRecipient = $mrMdl->getMRecipientList(array('shopCode' => $shopCode), array('mobileNbr'));
                $shopStaffArr = array_merge($shopStaffArr, $mRecipient);
                if($shopStaffArr){
                    $shopStaffMobileArr = array();
                    foreach($shopStaffArr as $v){
                        if($v['mobileNbr'] && !in_array($v['mobileNbr'], $shopStaffMobileArr)){
                            array_push($shopStaffMobileArr, $v['mobileNbr']);
                        }
                    }

                    if($shopStaffMobileArr){
                        $smsMdl = new SmsModel();
                        try{
                            $sMessage = str_replace(array('{{userName}}', '{{orderNbr}}', '{{realPay}}', '{{orderAmount}}', '{{deduction}}'), array($userName, substr($addOrder['orderNbr'],-5), number_format(($realPay / C('RATIO')), 2, '.', ''), number_format(($price / C('RATIO')), 2, '.', ''), number_format(($consumeLog['deduction'] / C('RATIO')), 2, '.', '')), C('SEND_MESSAGE.S_PAYED'));
                            $smsMdl->send($sMessage, $shopStaffMobileArr); // 商家店长支付成功短信
                        }catch (RPCException $e){
                            Log::write('---------SEND MESSAGE FAIL:'.$e->message.'---------', 'WARN', '', C('LOG_PATH').'sendMsg'.date('y_m_d').'.log');
                        }
                    }
                }
                // 判断用户是否有该商店的会员卡，没有则添加
                $checkUserCardRet = $userCardMdl->checkUserCard($userCode, $shopCode);
                return $ret;
            }
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * POS机支付
     * @param string $userCode 用户编码
     * @param string $shopCode 商店编码
     * @param string $userCouponCode 用户优惠券编码
     * @param int $platBonus 消费金额 单位：元
     * @param int $shopBonus 消费金额 单位：元
     * @param int $price 消费金额 单位：元
     * @return array
     */
    public function posPay($userCode, $shopCode, $userCouponCode, $platBonus, $shopBonus, $price) {
        $rules = array(
            array('userCode', 'require', C('USER.USER_CODE_EMPTY'), 1),
            array('shopCode', 'require', C('SHOP.SHOP_CODE_EMPTY'), 1),
            array('price', 'require', C('PAY.PRICE_EMPTY'), 1),
            array('price', 'is_numeric', C('PAY.PRICE_ERROR'), 1, 'function'),
        );
        $data = array(
            'userCode' => $userCode,
            'shopCode' => $shopCode,
            'price' => $price
        );
        if($this->validate($rules)->create($data)) {
            $userMdl = new UserModel();
            $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('mobileNbr'));
            if(!$userInfo)
                return $this->getBusinessCode(C('USER.USER_CODE_ERROR'));

            $shopMdl = new ShopModel();
            // 获得商家信息（商户名称）
            $shopInfo = $shopMdl->getShopInfo($shopCode, array('shopName'));
            if(!$shopInfo)
                return $this->getBusinessCode(C('SHOP.SHOP_CODE_ERROR'));

            $price = $price * C('RATIO');
            $realPay = $price;
            $platBonus = $platBonus * C('RATIO');
            $shopBonus = $shopBonus * C('RATIO');

            $consumeCode = $this->create_uuid();

            $userCardMdl = new UserCardModel();
            $userCard = $userCardMdl->getBestUserCard($userCode, $shopCode);
            if($userCard){
                $userCardCode = $userCard['userCardCode'];
                $field = array(
                    'UserCard.point',
                    'Card.discount',
                    'Card.discountRequire',
                    'Card.pointsPerCash',
                    'Card.cardCode',
                );
                $userCardInfo = $userCardMdl->getUserCardInfo($userCardCode, $field);
            }

            M()->startTrans();
            $consumeOrderMdl = new ConsumeOrderModel();
            $addOrder = $consumeOrderMdl->addConsumeOrder($userCode, $price, $shopCode);
            if($addOrder['code'] != C('SUCCESS')){
                return $addOrder;
            }

            $userCouponMdl = new UserCouponModel();
            if($userCouponCode){
                $userCouponCodeArr = explode('|', $userCouponCode);
                // 判断用户优惠券是否可用
                foreach($userCouponCodeArr as $k=>$uc){
                    if(empty($uc)){
                        unset($userCouponCodeArr[$k]);
                    }else{
                        $isUserCouponCanUse = $userCouponMdl->isUserCouponCanBeUsed($uc, $price, $userCode);
                        if($isUserCouponCanUse !== true) {
                            return $this->getBusinessCode($isUserCouponCanUse);
                        }
                    }
                }
            }

            if(isset($userCouponCodeArr) && $userCouponCodeArr){
                $userCouponInfo = $userCouponMdl->getCouponInfo($userCouponCodeArr[0], array('couponType', 'insteadPrice'));
            }else{
                $userCouponInfo = array();
            }
            if($userCouponInfo && $userCouponInfo['couponType'] == C('COUPON_TYPE.N_PURCHASE')){ // N元购型优惠券的使用
                $realPay = $userCouponInfo['insteadPrice'];
            }else{ // 其他类型优惠券或不使用优惠券
                $couponDeduction = 0;
                if(isset($userCouponCodeArr) && $userCouponCodeArr){
                    // 使用优惠券，得到实际付款金额
                    $realPay = $userCouponMdl->getRealPay($userCouponCodeArr[0], $realPay, count($userCouponCodeArr));
                    $couponDeduction = $price - $realPay;
                }

                $cardInsteadPrice = 0;
                if(isset($userCardInfo) && $userCardInfo) {
                    // 使用会员卡，得到实际付款金额
                    $userCardPoint = $userCardInfo['point'];
                    if($userCardPoint >= $userCardInfo['discountRequire']){
                        $cardInsteadPrice = $realPay - $realPay * $userCardInfo['discount'] / 10;
                    }
                }
                $cardInsteadPrice = ceil($cardInsteadPrice);
                $realPay = $realPay - $cardInsteadPrice;

                $bsMdl = new BonusStatisticsModel();
                if($platBonus > 0){
                    // 使用平台红包
                    $platBonusRet = $bsMdl->reduceBonusStatistics($userCode, C('HQ_CODE'), $platBonus);
                    if($platBonusRet['code'] == C('SUCCESS')){
                        $realPay = $realPay - ceil($platBonus);
                    }else{
                        return $platBonusRet;
                    }
                }

                if($shopBonus > 0){
                    // 使用商家红包
                    $shopBonusRet = $bsMdl->reduceBonusStatistics($userCode, $shopCode, $shopBonus);
                    if($shopBonusRet['code'] == C('SUCCESS')){
                        $realPay = $realPay - ceil($shopBonus);
                    }else{
                        return $shopBonusRet;
                    }
                }
            }

            if($userCouponInfo){
                // 改变用户优惠券状态
                $userCouponAction = array(
                    'status' => C('USER_COUPON_STATUS.USED'),
                    'consumeCode' => $consumeCode
                );
                $retOfUseCoupon = $userCouponMdl->useCoupon($userCouponCodeArr, $userCouponAction);
                if( ! $retOfUseCoupon)
                    return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
            }

            // 添加消费记录
            $consumeLog = array(
                'consumeCode' => $consumeCode,
                'consumerCode' => $userCode,
                'isCard' => isset($cardInsteadPrice) && $cardInsteadPrice != 0 ? C('YES') : C('NO'),
                'couponUsed' => isset($userCouponCodeArr) ? count($userCouponCodeArr) : C('NO'),
                'consumeTime' => date('Y-m-d H:i:s', time()),
                'identityCode' => substr(md5(uniqid(mt_rand(), true)), 0, 12),
                'deduction' => ($price - $realPay),
                'realPay' => $realPay,
                'cardDeduction' => isset($cardInsteadPrice) ? $cardInsteadPrice : C('NO'),
                'couponDeduction' => isset($couponDeduction) ? $couponDeduction : C('NO'),
                'location' => $shopCode,
                'status' => C('UC_STATUS.UNPAYED'),
                'payType' => C('UC_PAY_TYPE.POS'),
                'orderCode' => $addOrder['orderCode'],
                'platBonus' => $platBonus,
                'shopBonus' => $shopBonus,
                'usedCardCode' => isset($userCardInfo['cardCode']) ? $userCardInfo['cardCode'] : '',
            );
            $retOfAddUserConsume = $this->add($consumeLog) !== false ? true : false;
            if(! $retOfAddUserConsume) {
                M()->rollback();
                return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
            } else {
                M()->commit();
                $ret = $this->getBusinessCode(C('SUCCESS'));
                $ret['consumeCode'] = $consumeCode;
                $ret['orderNbr'] = $addOrder['orderNbr'];
                $ret['realPay'] = number_format(($realPay / C('RATIO')), 2,'.', '');
                return $ret;
            }
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 取消支付
     * @param $consumeCode
     * @return array
     */
    public function cancelPay($consumeCode){
        $consumeInfo = $this->where(array('consumeCode' => $consumeCode))->find();

        if($consumeInfo['status'] == C('UC_STATUS.CANCELED')){
            return $this->getBusinessCode(C('SUCCESS'));
        }

        M()->startTrans();
        //红包
        $bsMdl = new BonusStatisticsModel();
        $ret1 = true;
        if($consumeInfo['platBonus'] > 0){
            $ret1 = $bsMdl->updateBonusStatistics($consumeInfo['consumerCode'], C('HQ_CODE'), $consumeInfo['platBonus']);
        }
        $ret2 = true;
        if($consumeInfo['shopBonus'] > 0){
            $ret2 = $bsMdl->updateBonusStatistics($consumeInfo['consumerCode'], $consumeInfo['location'], $consumeInfo['shopBonus']);
        }

        //优惠券
        $ucMdl = new UserCouponModel();
        $ret3 = true;
        if($consumeInfo['couponUsed']){
            $data = array(
                'status'=>C('USER_COUPON_STATUS.ACTIVE'),
                'consumeCode'=>''
            );
            $ret3 = $ucMdl->updateUserCouponStatus(array('consumeCode'=>$consumeCode), $data);
        }

        $coMdl = new ConsumeOrderModel();
        // 修改订单的支付状态为已取消付款
        $ret4 = $coMdl->updateConsumeOrder(array('orderCode' => $consumeInfo['orderCode']), array('status' => C('ORDER_STATUS.CANCELED')));
        // 修改支付记录为已取消付款
        $ret5 = $this->where(array('consumeCode' => $consumeCode))->save(array('status' => C('UC_STATUS.CANCELED'))) !== false ? true : false;

        if($ret1 && $ret2 && $ret3 && $ret4 && $ret5) {
            M()->commit();
            $ret = $this->getBusinessCode(C('SUCCESS'));
            $ret['consumeCode'] = $consumeCode;
            return $ret;
        } else {
            M()->rollback();
            return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
        }

    }

    /**
     * 支付结果
     * @param $consumeCode
     * @param string $serialNbr 流水号
     * @param string $result 支付结果。SUCCESS:成功；FAIL:失败
     * @return array
     */
    public function setPayResult($consumeCode, $serialNbr, $result) {
        $consumeInfo = $this->field(array('UserConsume.*', 'ConsumeOrder.orderNbr', 'ConsumeOrder.orderAmount'))->join('ConsumeOrder ON ConsumeOrder.orderCode = UserConsume.orderCode')->where(array('consumeCode' => $consumeCode))->find();
        $coMdl = new ConsumeOrderModel();
        $userCardMdl = new UserCardModel();
        $cardActionLogMdl = new CardActionLogModel();
        $shopMdl = new ShopModel();
        $userMdl = new UserModel();
        $batchCouponMdl = new BatchCouponModel();

        if($consumeInfo['status'] == C('UC_STATUS.PAYED')){
            return $this->getBusinessCode(C('SUCCESS'));
        }

        if($serialNbr){
            $this->where(array('consumeCode'=>$consumeCode))->save(array('serialNbr'=>$serialNbr));
        }

        if($result == 'SUCCESS'){
            // 付款成功
            M()->startTrans();
            // 修改订单的支付状态为已支付
            $ret1 = $coMdl->updateConsumeOrder(array('orderCode' => $consumeInfo['orderCode']), array('status' => C('ORDER_STATUS.PAYED')));
            // 修改支付记录的状态为已支付
            $ret2 = $this->where(array('consumeCode' => $consumeCode))->save(array('status' => C('UC_STATUS.PAYED'), 'payedTime' => date('Y-m-d H:i:s', time()))) !== false ? true : false;

            $retOfUpdatePoint = true;
            $retOfAddCardAction = true;
            $userCard = $userCardMdl->getBestUserCard($consumeInfo['consumerCode'], $consumeInfo['location']);
            if($userCard){
                $userCardCode = $userCard['userCardCode'];
                $field = array(
                    'UserCard.point',
                    'Card.discount',
                    'Card.discountRequire',
                    'Card.pointsPerCash',
                    'Card.cardCode',
                );
                $userCardInfo = $userCardMdl->getUserCardInfo($userCardCode, $field);
                if($userCardInfo) {
                    $userCardPoint = $userCardInfo['point'];

                    // 更新用户会员卡积分 (这个需要在商家端返回支付成功后再更新)
                    $newUserCardPoint = $userCardPoint + $userCardInfo['pointsPerCash'] * ($consumeInfo['deduction'] + $consumeInfo['realPay']) / C('RATIO');
                    $retOfUpdatePoint = $userCardMdl->updateUserCardPoint($newUserCardPoint, $userCardCode);
                    // 添加用户会员卡使用记录
                    $cardActionLog = array(
                        'logCode' => $this->create_uuid(),
                        'userCode' => $consumeInfo['consumerCode'],
                        'cardCode' => $userCardInfo['cardCode'],
                        'userCardCode' => $userCardCode,
                        'consumeAmount' => ($consumeInfo['deduction'] + $consumeInfo['realPay']),
                        'actionTime' => date('Y-m-d H:i:s', time()),
                        'consumeCode' => $consumeCode,
                    );
                    $retOfAddCardAction = $cardActionLogMdl->addCardActionLog($cardActionLog);
                }
            }
            // 获得商家信息（商户名称，商户联系号码）
            $shop = $shopMdl->getShopInfo($consumeInfo['location'], array('Shop.shopName','Shop.mobileNbr'));
            $user = $userMdl->getUserInfo(array('userCode' => $consumeInfo['consumerCode']), array('nickName','realName','mobileNbr','avatarUrl'));
            $userName = $user['realName']?$user['realName']:$user['nickName'];

            //优惠券消息
            if($consumeInfo['couponUsed']){
                // 给用户添加用户使用优惠券的消息
                $userCouponMdl = new UserCouponModel();
                $usedCouponList = $userCouponMdl->getUserCouponInfoB(
                    array('UserCoupon.consumeCode' => $consumeCode),
                    array('UserCoupon.userCouponCode', 'BatchCoupon.insteadPrice', 'couponType', 'discountPercent', 'availablePrice', 'UserCoupon.applyTime', 'BatchCoupon.validityPeriod', 'BatchCoupon.expireTime')
                );

                foreach($usedCouponList as $v){
                    $msgInfo = array(
                        'title' => str_replace('{{shopName}}', $shop['shopName'], C('MSG_TITLE_TDL.USE_COUPON')),
                        'content' => C('COUPON_MSG_TDL.USED'),
                        'createTime' => date('Y-m-d H:i:s'),
                        'senderCode' => $consumeInfo['location'],
                        'type' => C('MESSAGE_TYPE.COUPON'),
                        'userCouponCode' => $v['userCouponCode'],
                    );
                    $msgMdl = new MessageModel();
                    $msgMdl->addMsg($msgInfo, $consumeInfo['consumerCode']);
                }

                // 使用优惠券获得圈值记录，并更新用户的历史圈值和当前圈值
                $userMdl = new UserModel();
                $userMdl->addPointEarningLog($consumeInfo['consumerCode'], 20, '使用优惠券');
            }

            // 满就送， 派发一张优惠券
            $price = ($consumeInfo['deduction'] + $consumeInfo['realPay']);
            $sendCouponRet = $batchCouponMdl->sendCoupon($consumeInfo['consumerCode'], $consumeInfo['location'], $price);

            if($ret1 && $ret2 && $retOfUpdatePoint && $retOfAddCardAction) {
                M()->commit();
                // TODO 添加商家回头客人数
                $ret = $this->getBusinessCode(C('SUCCESS'));
                $ret['hasSendCoupon'] = 0;
                $ret['userCouponCode'] = '';

                // 推送消息
                $realPay = number_format(($consumeInfo['realPay'] / C('RATIO')),2,'.','');
                $content = str_replace('{{realPay}}', $realPay, C('SHOP_PUSH_MSG_TDL.PAYED'));
                $receiver = explode('-', $consumeInfo['location']);
                $jpushMdl = new JpushModel(C('SHOP_APP_KEY'), C('SHOP_MASTER_SECRET'));
                $point = 0;
                $cardActionLogMdl = new CardActionLogModel();
                $cardAction = $cardActionLogMdl->getConsumeCardInfo($consumeCode);
                if($cardAction){
                    $point = $cardAction['pointsPerCash'] * $cardAction['consumeAmount'] / C('RATIO');
                }
                $ccInfo = array(
                    'avatarUrl' => $user['avatarUrl'],
                    'totalPay' => ($consumeInfo['orderAmount']) / C('RATIO'),
                    'realPay' => $consumeInfo['realPay'] / C('RATIO'),
                    'bonusPay' => ($consumeInfo['shopBonus'] + $consumeInfo['platBonus']) / C('RATIO'),
                    'couponPay' => $consumeInfo['couponDeduction'] / \Consts::HUNDRED,
                    'couponUsed' => $consumeInfo['couponUsed'],
                    'identityCode' => $consumeInfo['identityCode'],
                    'point' => $point,
                    'consumeTime' => $consumeInfo['consumeTime']
                );
                $jpushMdl->jPushByAction($receiver, $content, $ccInfo, C('PUSH_ACTION.CONSUME'));

                if($sendCouponRet['code'] == C('SUCCESS')){
                    $ret['hasSendCoupon'] = 1;
                    $ret['userCouponCode'] = $sendCouponRet['userCouponCode'];
                    $this->where(array('consumeCode'=>$consumeCode))->save(array('userCouponCode'=>$sendCouponRet['userCouponCode']));
                }

                // 向商家发短信
                //1.员工级别：店长或大店长，活跃状态，短信接收设置为开启状态
                $shopStaffRelMdl = new ShopStaffRelModel();
                $shopStaffArr = $shopStaffRelMdl->getStaffList(array('ShopStaffRel.shopCode' => $consumeInfo['location'], 'ShopStaff.status' => \Consts::SHOP_STAFF_STATUS_ACTIVE, 'ShopStaff.userLvl' => array('gt', \Consts::SHOP_STAFF_LVL_EMPLOYEE), 'ShopStaff.isSendPayedMsg' => C('YES')), array('ShopStaff.mobileNbr'));
                //2.店长设置的其他员工
                $mrMdl = new MessageRecipientModel();
                $mRecipient = $mrMdl->getMRecipientList(array('shopCode' => $consumeInfo['location']), array('mobileNbr'));
                $shopStaffArr = array_merge($shopStaffArr, $mRecipient);
                if($shopStaffArr){
                    $shopStaffMobileArr = array();
                    foreach($shopStaffArr as $v){
                        if($v['mobileNbr'] && !in_array($v['mobileNbr'], $shopStaffMobileArr)){
                            array_push($shopStaffMobileArr, $v['mobileNbr']);
                        }
                    }

                    if($shopStaffMobileArr){
                        $smsMdl = new SmsModel();
                        try{
                            $sMessage = str_replace(array('{{userName}}', '{{orderNbr}}', '{{realPay}}', '{{orderAmount}}', '{{deduction}}'), array($userName, substr($consumeInfo['orderNbr'],-5), number_format(($realPay / C('RATIO')), 2, '.', ''), number_format((($consumeInfo['deduction'] + $consumeInfo['realPay']) / C('RATIO')), 2, '.', ''), number_format(($consumeInfo['deduction'] / C('RATIO')), 2, '.', '')), C('SEND_MESSAGE.S_PAYED'));
                            $smsMdl->send($sMessage, $shopStaffMobileArr); // 商家店长支付成功短信
                        }catch (RPCException $e){
                            Log::write('---------SEND MESSAGE FAIL:'.$e->message.'---------', 'WARN', '', C('LOG_PATH').'sendMsg'.date('y_m_d').'.log');
                        }
                    }
                }

                // 判断用户是否有该商店的会员卡，没有则添加
                $checkUserCardRet = $userCardMdl->checkUserCard($consumeInfo['consumerCode'], $consumeInfo['location']);
                return $ret;
            } else {
                M()->rollback();
                return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
            }
        } elseif($result == 'FAIL') { // 付款失败
            $consumeInfo = $this->where(array('consumeCode' => $consumeCode))->find();
            if($consumeInfo['status'] == C('UC_STATUS.FAIL')){
                return $this->getBusinessCode(C('SUCCESS'));
            }

            M()->startTrans();
            $coMdl = new ConsumeOrderModel();
            // 修改订单的支付状态为支付失败
            $ret1 = $coMdl->updateConsumeOrder(array('orderCode' => $consumeInfo['orderCode']), array('status' => C('ORDER_STATUS.FAIL')));
            // 修改支付记录的状态为支付失败
            $ret2 = $this->where(array('consumeCode' => $consumeCode))->save(array('status' => C('UC_STATUS.FAIL'))) !== false ? true : false;

            if($ret1 && $ret2) {
                M()->commit();
                $ret = $this->getBusinessCode(C('SUCCESS'));
                $ret['consumeCode'] = $consumeCode;
                return $ret;
            } else {
                M()->rollback();
                return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
            }
        }
    }

    /**
     * 更新保存账单的信息
     * @param array $where 条件
     * @param array $data 数据
     * @return boolean 成功发挥true，失败返回false
     */
    public function updateConsumeStatus($where, $data) {
        return $this->where($where)->save($data) !== false ? true : false;
    }

    /**
     * 统计支付次数
     * @param array $where 筛选条件
     * @param array $joinTable 联合的表
     * @return int $consumeTimes 次数
     */
    public function countConsumeTimes($where, $joinTable = array()) {
        if($joinTable) {
            foreach($joinTable as $v) {
                $this->join($v['joinTable'] . ' ON ' . $v['joinCon'], $v['joinType']);
            }
        }
        $consumeTimes = $this->where($where)->count('consumeCode');
        return $consumeTimes ? $consumeTimes : 0;
    }

    /**
     * 统计已经使用的优惠券数量
     * @param array $where 筛选条件
     * @return int $couponUsedAmount
     */
    public function countCouponUsed($where) {
        $couponUsedAmount = $this->where($where)->sum('couponUsed');
        return $couponUsedAmount ? $couponUsedAmount : 0;
    }

    /**
     * 获得消费信息某个字段的总和
     * @param array $where 筛选条件
     * @param string $field 需要统计的字段
     * @param array $joinTable 联合的表
     * @return float $amount 单位：分
     */
    public function sumConsumeField($where, $field, $joinTable = array()) {
        if($joinTable) {
            foreach($joinTable as $v) {
                $this->join($v['joinTable'] . ' ON ' . $v['joinCon'], $v['joinType']);
            }
        }
        $amount = $this->where($where)->sum($field);
        return $amount ? $amount : 0;
    }

    /**
     * 获得用户的消费信息
     * @param array $where
     * @return array
     */
    public function countUserConsumeInfo($where) {
        $consumeTimes =$this->where($where)->count('consumeCode');//消费次数
        $consumeAmount = $this->where($where)->sum('deduction + realPay');//消费总金额
        $lastActionTime = $this->where($where)->max('consumeTime');//最后一次消费时间
        return array(
            'consumeTimes' => $consumeTimes ? $consumeTimes : 0,
            'consumeAmount' => $consumeAmount ? $consumeAmount  / C('RATIO') : 0,
            'lastActionTime' => $lastActionTime ? $lastActionTime : '',
        );
    }

    /**
     * 线上银行卡支付
     * @param string $orderCode 用户订单编码
     * @param string $userCouponCode 使用的用户优惠券编码，多个编码之间使用“|” 分隔
     * @param float $platBonus 平台红包金额，单位：元
     * @param float $shopBonus 商户红包金额，单位：元
     * @param float $noDiscountPrice 不参与优惠的金额，单位：元
     * @param int $isUseUserCard 是否要使用会员卡
     * @param int $isUseBankCard 是否要使用工行卡折扣
     * @return array {'code', 'consumeCode', 'realPay'}
     */
    public function bankCardPay($orderCode, $userCouponCode, $platBonus, $shopBonus, $noDiscountPrice = 0.0, $isUseUserCard = 1, $isUseBankCard = 1) {

        $consumeOrderMdl = new ConsumeOrderModel();
        // 获得未支付的订单的信息
        $orderInfo = $consumeOrderMdl->getOrderInfo(array('orderCode' => $orderCode, 'status' => \Consts::PAY_STATUS_UNPAYED));
        if(!$orderInfo['orderCode'])
            return $this->getBusinessCode(C('ORDER.NOT_EXIST'));

        M()->startTrans();
        $firstDeduction = 0;

        // 修改订单状态为付款中
        $retOfUpdateOrderStatus = $consumeOrderMdl->updateConsumeOrder(array('orderCode' => $orderCode), array('status' => \Consts::PAY_STATUS_PAYING));

        if(! $retOfUpdateOrderStatus)
            return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));

        $userCode = $orderInfo['clientCode']; // 用户编码
        $noDiscountPrice = $noDiscountPrice * \Consts::HUNDRED; // 酒水，元转化为分
        $orderAmount = $orderInfo['orderAmount']; // 订单消费金额，单位：分
        $canDiscountAmount = $orderAmount - $noDiscountPrice; // 可折扣金额，单位：分
        $realPay = $canDiscountAmount; // 设置实际支付金额等于订单消费金额， 单位：分
        $platBonus = $platBonus * \Consts::HUNDRED; // 平台红包，元转化为分
        $shopBonus = $shopBonus * \Consts::HUNDRED; // 商家红包，元转化为分

        $consumeCode = $this->create_uuid(); // 设置支付编码

        $userCouponMdl = new UserCouponModel();

        if($userCouponCode){
            $userCouponCodeArr = explode('|', $userCouponCode);
            // 判断用户优惠券是否可用
            foreach($userCouponCodeArr as $k => $uc){
                if(empty($uc)){
                    unset($userCouponCodeArr[$k]);
                }else{
                    // 判断该优惠券是否可用
                    $isUserCouponCanUse = $userCouponMdl->isUserCouponCanBeUsed($uc, $canDiscountAmount / \Consts::HUNDRED, $userCode);
					if($isUserCouponCanUse !== true) {
                        return $this->getBusinessCode($isUserCouponCanUse);
                    }
                }
            }
        }
        if(isset($userCouponCodeArr) && $userCouponCodeArr){
            $userCouponInfo = $userCouponMdl->getCouponInfo($userCouponCodeArr[0], array('couponType', 'insteadPrice'));
        }else{
            $userCouponInfo = array();
        }
            $couponDeduction = 0;
            if(isset($userCouponCodeArr) && $userCouponCodeArr){
                // 使用优惠券，得到实际付款金额
                $realPay = $userCouponMdl->getRealPay($userCouponCodeArr[0], $realPay, count($userCouponCodeArr));
                $couponDeduction = $canDiscountAmount - $realPay;
            }

//           $cardInsteadPrice = 0;
//            if($userCardInfo) {
//                if($userCardInfo['point'] >= $userCardInfo['discountRequire'] && $userCardInfo['discount'] > 0 && $isUseUserCard == \Consts::YES){
//                    // 使用会员卡，得到实际付款金额
//                    $cardInsteadPrice = $realPay - $realPay * $userCardInfo['discount'] / \Consts::HUNDRED;
//                }
//
//                // 添加使用会员卡的记录
//                $cardActionLogMdl = new CardActionLogModel();
//                $cardActionLogInfo = array(
//                    'logCode' => $this->create_uuid(), // 设置log编码
//                    'userCode' => $userCode, // 用户编码
//                    'cardCode' => $userCardInfo['cardCode'], // 使用的会员卡编码
//                    'userCardCode' => $userCardInfo['userCardCode'], // 使用的用户会员卡编码
//                    'actionContent' => '买单-在线支付', // 使用情形
//                    'consumeAmount' => $orderAmount, // 消费金额，单位：分
//                    'actionTime' => date('Y-m-d h:i:s'), // 使用时间
//                    'consumeCode' => $consumeCode, // 消费编码
//                );
//                $cardActionLogMdl->addCardActionLog($cardActionLogInfo);
//            }
//            $cardInsteadPrice = ceil($cardInsteadPrice);
//            $realPay = $realPay - $cardInsteadPrice;



        if($userCouponInfo) {
            // 改变用户优惠券状态
            $userCouponAction = array(
                'status' => C('USER_COUPON_STATUS.USED'),
                'consumeCode' => $consumeCode
            );
            $retOfUseCoupon = $userCouponMdl->useCoupon($userCouponCodeArr, $userCouponAction);
            if( ! $retOfUseCoupon)
                return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
        }

        $realPay = $realPay < 0 ? 0 : $realPay; // 实际支付金额，单位：分
        $realPay = $realPay + $noDiscountPrice;

        //添加支付记录
        $consumeLog = array(
            'consumeCode' => $consumeCode,
            'consumerCode' => $userCode,
            'isCard' => isset($cardInsteadPrice) && $cardInsteadPrice != 0 ? C('YES') : C('NO'),
            'couponUsed' => isset($userCouponCodeArr) ? count($userCouponCodeArr) : C('NO'),
            'consumeTime' => date('Y-m-d H:i:s', time()),
            'identityCode' => substr(md5(uniqid(mt_rand(), true)), 0, 12),
            'deduction' => $orderAmount - $realPay,
            'realPay' => $realPay, // 实际支付金额，单位：分
            'location' => $orderInfo['shopCode'],
            'status' => \Consts::PAY_STATUS_UNPAYED, // 支付状态：未支付
            'payType' => C('UC_PAY_TYPE.BANKCARD'),
            'orderCode' => $orderCode, // 订单编码
            'platBonus' => $platBonus,
            'shopBonus' => $shopBonus,
            'cardDeduction' => isset($cardInsteadPrice) ? $cardInsteadPrice : C('NO'),
            'couponDeduction' => isset($couponDeduction) ? $couponDeduction : C('NO'),
            'firstDeduction' => $firstDeduction,
            'noDiscountPrice' => $noDiscountPrice,
            'usedUserCouponCode' => $userCouponCode, // 使用的用户优惠券编码，多个编码之间使用“|” 分隔
        );
        $retOfAddUserConsume = $this->add($consumeLog) !== false ? true : false;

        if(! $retOfAddUserConsume) {
            M()->rollback();
            return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
        } else {
            M()->commit();
            $ret = $this->getBusinessCode(C('SUCCESS'));
            $ret['consumeCode'] = $consumeCode;
            $ret['realPay'] = number_format(($realPay / \Consts::HUNDRED), 2, '.', '');
            return $ret;
        }
    }
    public function bankCardPayForGz($orderCode,$realPay){
        $consumeOrderMdl = new ConsumeOrderModel();
        // 获得未支付的订单的信息
        $orderInfo = $consumeOrderMdl->getOrderInfo(array('orderCode' => $orderCode, 'status' => \Consts::PAY_STATUS_UNPAYED));
        if(!$orderInfo['orderCode'])
            return $this->getBusinessCode(C('ORDER.NOT_EXIST'));
        M()->startTrans();
        // 修改订单状态为付款中
        $retOfUpdateOrderStatus = $consumeOrderMdl->updateConsumeOrder(array('orderCode' => $orderCode), array('status' => \Consts::PAY_STATUS_PAYING));
        if(! $retOfUpdateOrderStatus)
            return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
        $userCode = $orderInfo['clientCode']; // 用户编码
        $orderAmount = $orderInfo['orderAmount']; // 订单消费金额，单位：分
        $consumeCode = $this->create_uuid(); // 设置支付编码
        //添加支付记录
        $consumeLog = array(
            'consumeCode' => $consumeCode,
            'consumerCode' => $userCode,
            'isCard' => isset($cardInsteadPrice) && $cardInsteadPrice != 0 ? C('YES') : C('NO'),
            'couponUsed' => isset($userCouponCodeArr) ? count($userCouponCodeArr) : C('NO'),
            'consumeTime' => date('Y-m-d H:i:s', time()),
            'identityCode' => substr(md5(uniqid(mt_rand(), true)), 0, 12),
            'deduction' => $orderAmount - $realPay,
            'realPay' => $realPay, // 实际支付金额，单位：分
            'location' => $orderInfo['shopCode'],
            'status' => \Consts::PAY_STATUS_UNPAYED, // 支付状态：未支付
            'payType' => C('UC_PAY_TYPE.BANKCARD'),
            'orderCode' => $orderCode, // 订单编码
            'cardDeduction' => isset($cardInsteadPrice) ? $cardInsteadPrice : C('NO'),
            'couponDeduction' => isset($couponDeduction) ? $couponDeduction : C('NO'),
            //'usedUserCouponCode' => $userCouponCode, // 使用的用户优惠券编码，多个编码之间使用“|” 分隔
        );
        $retOfAddUserConsume = $this->add($consumeLog) !== false ? true : false;
        if(! $retOfAddUserConsume) {
            M()->rollback();
            return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
        } else {
            M()->commit();
            $ret = $this->getBusinessCode(C('SUCCESS'));
            $ret['consumeCode'] = $consumeCode;
            $ret['realPay'] = number_format(($realPay / \Consts::HUNDRED), 2, '.', '');
            return $ret;
        }
    }

    /**
     * 取消线上银行卡支付
     * @param string $consumeCode 订单编码
     * @return array $ret
     */
    public function cancelBankcardPay($consumeCode) {
        M()->startTrans();
        /** 判断该笔支付是否可以取消支付*/
        // 获得该笔消费记录
        $consumeInfo = $this
            ->field(array('UserConsume.orderCode', 'platBonus', 'shopBonus', 'consumerCode', 'location', 'couponUsed', 'isCard', 'orderType', 'UserConsume.status'))
            ->join('ConsumeOrder ON ConsumeOrder.orderCode = UserConsume.orderCode')
            ->where(array('consumeCode' => $consumeCode))
            ->find();
        if(! in_array($consumeInfo['status'], array(\Consts::PAY_STATUS_UNPAYED, \Consts::PAY_STATUS_PAYING)))
            return $this->getBusinessCode(C('PAY.CONSUME_CANCELED'));
        // 修改账单支付状态为已经取消支付
        $upConsumeStatusRet = $this->where(array('consumeCode' => $consumeCode))->save(array('status' => \Consts::PAY_STATUS_CANCELED)) !== false ? true : false;
        if($upConsumeStatusRet == false)
            return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));

        // 删除会员卡使用记录
//        $userCardMdl = new UserCardModel();
//        $userCardInfo = $userCardMdl->getBestUserCard($consumeInfo['consumerCode'], $consumeInfo['location']);
//        if($userCardInfo) {
//            $cardActionLogMdl = new CardActionLogModel();
//            $delCardActionLogRet = $cardActionLogMdl->delCardActionLog(array('consumeCode' => $consumeCode));
//            if($delCardActionLogRet == false)
//                return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
//        }
        //修改用户优惠券状态为可用
        if($consumeInfo['couponUsed'] != 0) {
            $userCouponMdl = new UserCouponModel();
            $upUserCouponStatusRet = $userCouponMdl->updateUserCouponStatus(array('consumeCode' => $consumeCode), array('status' => C('USER_COUPON_STATUS.ACTIVE'), 'consumeCode' => ''));
            if($upUserCouponStatusRet == false)
                return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
        }
        //c如果该次购买参加活动，更改活动码状态,更改让利状态
        $activityCode = M('consumeorder')->field('activityCode,clientCode')->where(array('orderCode'=>$consumeInfo['orderCode']))->find();
        if($activityCode && !empty($activityCode['activityCode'])){
           $res =  M('amountdiscount')->where(array('orderCode' => $consumeInfo['orderCode']))->save(array('status'=> 0));
        }

        if($consumeInfo['orderType'] == \Consts::ORDER_TYPE_OTHER) {
            //设置订单的支付状态为取消付款，设置取消订单的时间
            $data = array('status' => \Consts::PAY_STATUS_CANCELED, 'cancelTime' => date('Y-m-d H:i:s'));
        } elseif($consumeInfo['orderType'] == \Consts::ORDER_TYPE_COUPON) {
            //设置订单的支付状态为取消付款，订单状态为已撤单，取消订单时间
            $data = array('status' => \Consts::PAY_STATUS_CANCELED, 'orderStatus' => \Consts::ORDER_STATUS_CANCELED, 'cancelTime' => date('Y-m-d H:i:s'));
            // 回滚优惠券的数量
            $orderCouponMdl = new OrderCouponModel();
            $orderCouponMdl->rollbackCouponRemaining($consumeInfo['orderCode']);
        } else {
            //设置订单的支付状态为未支付
            $data = array('status' => \Consts::PAY_STATUS_UNPAYED);
        }
        $consumeOrderMdl = new ConsumeOrderModel();
        // 更新订单的信息
        $upOrderRet = $consumeOrderMdl->updateConsumeOrder(array('orderCode' => $consumeInfo['orderCode']), $data);

        if($upOrderRet == false) {
            M()->rollback();
            $code = C('API_INTERNAL_EXCEPTION');
        } else {
            M()->commit();
            $code = C('SUCCESS');
        }
        return $this->getBusinessCode($code);
    }

    /**
     * 产品订单的取消线上银行卡支付
     * @param string $consumeCode 订单编码
     * @return array $ret
     */
    public function pOCancelBankcardPay($consumeCode) {
        M()->startTrans();
        // 该支付是否已经取消
        $consumeInfo = $this
            ->field(array('orderCode', 'platBonus', 'shopBonus', 'consumerCode', 'location', 'couponUsed', 'isCard'))
            ->where(array('consumeCode' => $consumeCode, 'status' => array('NEQ', C('UC_STATUS.CANCELED'))))
            ->find();
        if(!$consumeInfo)
            return $this->getBusinessCode(C('PAY.CONSUME_CANCELED'));

        $isSqlSucc = true;
        // 修改支付状态为已经取消支付
        $upConsumeStatusRet = $this->where(array('consumeCode' => $consumeCode))->save(array('status' => C('UC_STATUS.CANCELED'))) !== false ? true : false;
        if($upConsumeStatusRet == false) $isSqlSucc = false;

        // 删除会员卡使用记录
        $userCardMdl = new UserCardModel();
        $userCardInfo = $userCardMdl->getBestUserCard($consumeInfo['consumerCode'], $consumeInfo['location']);
        if($userCardInfo) {
            $cardActionLogMdl = new CardActionLogModel();
            $delCardActionLogRet = $cardActionLogMdl->delCardActionLog(array('consumeCode' => $consumeCode));
            if($delCardActionLogRet == false) $isSqlSucc = false;
        }
        //修改用户优惠券状态为可用
        if($consumeInfo['couponUsed'] != 0) {
            $userCouponMdl = new UserCouponModel();
            $upUserCouponStatusRet = $userCouponMdl->updateUserCouponStatus(array('consumeCode' => $consumeCode), array('status' => C('USER_COUPON_STATUS.ACTIVE'), 'consumeCode' => ''));
            if($upUserCouponStatusRet == false) $isSqlSucc = false;
        }
        //用户商家红包回滚
        $bsMdl = new BonusStatisticsModel();
        if($consumeInfo['shopBonus'] != 0) {
            $rollBackShopBonusRet = $bsMdl->updateBonusStatistics($consumeInfo['consumerCode'], $consumeInfo['location'], $consumeInfo['shopBonus']);
            if($rollBackShopBonusRet == false) $isSqlSucc = false;
        }
        //用户平台红包回滚
        if($consumeInfo['platBonus'] != 0) {
            $rollBackPlatBonusRet = $bsMdl->updateBonusStatistics($consumeInfo['consumerCode'], C('HQ_CODE'), $consumeInfo['platBonus']);
            if($rollBackPlatBonusRet == false) $isSqlSucc = false;
        }

        if($isSqlSucc == false) {
            M()->rollback();
            $code = C('API_INTERNAL_EXCEPTION');
        } else {
            M()->commit();
            $code = C('SUCCESS');
        }
        return $this->getBusinessCode($code);
    }

    /**
     * 获得首页优惠券使用和E支付买单消息
     * @param string $shopCode 商家编码
     * @return array
     */
    public function listConsumingMsg($shopCode) {
        $limitTime = 24*60*60;//1天前
        $ConsumingMsgList = array();
        $consumeList = $this
            ->where(array(
                'location' => $shopCode,
                'consumeTime' => array('EGT', date('Y-m-d H:i:s', time() - $limitTime)),
                'status' => C('UC_STATUS.PAYED')
            ))
            ->order('consumeTime desc')
            ->select();
        if($consumeList) {
            $cardALMdl = new CardActionLogModel();
            $userCouponMdl = new UserCouponModel();
            $bankALMdl = new BankAccountLocalLogModel();
            foreach($consumeList as $v){
                $consumeCode  = $v['consumeCode'];
                $identityCode = $v['identityCode']; //标志码
                $consumeCardInfo = $cardALMdl->getConsumeCardInfo($consumeCode);
                $point = $consumeCardInfo ? $consumeCardInfo['pointsPerCash'] * $consumeCardInfo['consumeAmount'] / 100 : 0; //积分
                $totalAmount = $v['deduction'] / 100 + $v['realPay'] / 100; // 消费金额
                $actionDate = $v['consumeTime']; //消费日期
                $consumeMsgType = 1;//在线消费消息
                $couponMsgType = 2;//优惠券使用消息
                $bankAccountLog = $bankALMdl->bankAccountLogConsume($consumeCode);
                $eConsumeMsgInfo = array(
                    'consumeAmount' => $bankAccountLog ? $bankAccountLog['consumeAmount'] / 100 : $v['realPay'] / 100, //支付金额
                    'identityCode' => $identityCode, //标志码
                    'deduction' => $v['deduction'] / 100, //抵扣金额
                    'point' => $point, //积分
                    'totalAmount' => $totalAmount, // 消费金额
                    'actionDate' => $actionDate, //消费日期
                    'msgType' => $consumeMsgType
                );
                $ConsumingMsgList[] = $eConsumeMsgInfo;

                $couponConsumeInfo = $userCouponMdl->getUserCouponInfoB(
                    array('UserCoupon.consumeCode' => $consumeCode),
                    array('UserCoupon.userCouponCode', 'BatchCoupon.insteadPrice', 'couponType', 'discountPercent', 'availablePrice', 'UserCoupon.applyTime', 'BatchCoupon.validityPeriod', 'BatchCoupon.expireTime')
                );

                if($couponConsumeInfo) {
                    $couponConsumeMsgInfo = array(
                        'couponUsed' => count($couponConsumeInfo), // 使用数量
                        'identityCode' => $identityCode, // 标志码
                        'deduction' => count($couponConsumeInfo) * $couponConsumeInfo[0]['insteadPrice'] / 100, // 抵扣金额
                        'point' => $point, // 积分
                        'totalAmount' => $totalAmount, // 消费金额
                        'actionDate' => $actionDate, // 消费日期
                        'msgType' => $couponMsgType
                    );
                    $ConsumingMsgList[] = $couponConsumeMsgInfo;
                }
            }
        }
        return $ConsumingMsgList;
    }

    /**
     * 获取某一商家的消费金额走势信息
     * @param string $shopCode 商家编码
     * @return array
     */
    public function listConsumeTrend($shopCode) {
        $consumeTrendList = $this
            ->field(array(
                'sum(UserConsume.deduction + UserConsume.realPay) / ' . C('RATIO') => 'fee',
                'from_unixtime(unix_timestamp(consumeTime), "%Y-%m")' => 'month'
            ))
            ->where(array('location' => $shopCode, 'status' => C('UC_STATUS.PAYED')))
            ->group('month')
            ->select();
        foreach($consumeTrendList as &$v) {
            $v['fee'] = number_format($v['fee'], 2, '.', '');
        }
        return $consumeTrendList;
    }

    /**
     * 获取支付记录具体信息
     * @param array $condition 查询条件。例：{'consumeCode' => 'xxxxx', ...}
     * @param array $field 查询字段。例：{'consumeCode', 'UserConsume.location', ...}
     * @return array $consumeInfo
     */
    /**
     * 获取支付记录具体信息
     * @param array $condition 查询条件。例：{'consumeCode' => 'xxxxx', ...}
     * @param array $field 查询字段。例：{'consumeCode', 'UserConsume.location', ...}
     * @return array $consumeInfo
     */
    public function getConsumeInfo($condition, $field) {
        return $this->field($field)
            ->join('User ON User.userCode = UserConsume.consumerCode', 'LEFT')
            ->join('Shop ON Shop.shopCode = UserConsume.location', 'LEFT')
            ->join('OrderCoupon ON OrderCoupon.orderCode = UserConsume.orderCode','LEFT')
            ->join('ConsumeOrder ON ConsumeOrder.orderCode = UserConsume.orderCode', 'LEFT')
            ->join('BatchCoupon ON BatchCoupon.batchCouponCode = OrderCoupon.batchCouponCode')
            ->where($condition)
            ->find();
    }

    /**
     * 在某商家，平台红包使用总额
     * @param $where
     * @return float
     */
    public function platBonusConsumeAmount($where){
        $where['platBonus'] = array('GT', 0);
        $platBonus = $this
            ->where($where)
            ->sum('platBonus');
        return $platBonus / C('RATIO');
    }

    /**
     * 使用平台红包带来的消费总额
     * @param $where
     * @return float
     */
    public function platBonusConsumeTotalAmount($where) {
        $where['platBonus'] = array('GT', 0);
        $platBonusConsumeAmount = $this
            ->where($where)
            ->sum('realPay + deduction');
        return $platBonusConsumeAmount / C('RATIO');
    }

    /**
     * 商家红包使用总额
     * @param $where
     * @return float
     */
    public function shopBonusConsumeAmount($where){
        $where['shopBonus'] = array('GT', 0);
        $shopBonus = $this
            ->where($where)
            ->sum('shopBonus');
        return $shopBonus / C('RATIO');
    }

    /**
     * 使用商家红包带来的消费总额
     * @param $where
     * @return float
     */
    public function shopBonusConsumeTotalAmount($where) {
        $where['shopBonus'] = array('GT', 0);
        $shopBonusConsumeAmount = $this
            ->where($where)
            ->sum('realPay + deduction');
        return $shopBonusConsumeAmount / C('RATIO');
    }

    /**
     * 管理端获取用户使用红包记录
     * @param array $filterData
     * @param Object $page 偏移值
     * @return null|array 如果为空，返回null；如果不为空，返回二维数组array(array());
     */
    public function listBonusConsume($filterData, $page) {
        unset($filterData['bonusName']);
        $where = $this->filterWhere(
            $filterData,
            array('shopName' => 'like', 'mobileNbr' => 'like'),
            $page);
        $where = $this->secondFilterWhere($where);
        $where['UserConsume.status'] = C('UC_STATUS.PAYED');
        $where['_string'] = '(platBonus > 0 OR shopBonus > 0)';
        $userBonusList = $this
            ->field(array(
                'Shop.shopName',
                'UserConsume.consumeTime',
                'platBonus',
                'shopBonus',
                'deduction',
                'realPay',
                'User.realName',
                'User.nickName',
                'User.mobileNbr',
            ))
            ->join('Shop ON Shop.shopCode = UserConsume.location', 'LEFT')
            ->join('User ON UserConsume.consumerCode=User.userCode', 'LEFT')
            ->where($where)
            ->order('consumeTime desc')
            ->pager($page)
            ->select();
//        echo $this->getLastSql();
        foreach($userBonusList as &$v) {
            $v['platBonus'] = number_format($v['platBonus'] / C('RATIO'), 2, '.', '');
            $v['shopBonus'] = number_format($v['shopBonus'] / C('RATIO'), 2, '.', '');
            $v['price'] = number_format(($v['deduction'] + $v['realPay']) / C('RATIO'), 2, '.', '');
            unset($v['deduction']);
            unset($v['realPay']);
        }
        return $userBonusList;
    }

    /**
     * 管理端获得用户使用红包记录总数
     * @param array $filterData
     * @return int
     */
    public function countBonusConsume($filterData) {
        unset($filterData['bonusName']);
        $where = $this->filterWhere(
            $filterData,
            array('shopName' => 'like', 'mobileNbr' => 'like')
        );
        $where = $this->secondFilterWhere($where);
        $where['UserConsume.status'] = C('UC_STATUS.PAYED');
        $where['_string'] = '(platBonus > 0 OR shopBonus > 0)';
        return $this
            ->join('Shop ON Shop.shopCode = UserConsume.location', 'LEFT')
            ->join('User ON UserConsume.consumerCode=User.userCode', 'LEFT')
            ->where($where)
            ->count();
    }

    /**
     * 第二次过滤条件
     * @param array $where
     * @return array
     */
    public function secondFilterWhere(&$where) {
        if($where['userCode']) {
            $where['User.userCode'] = $where['userCode'];
            unset($where['userCode']);
        }
        if($where['mobileNbr']) {
            $where['User.mobileNbr'] = $where['mobileNbr'];
            unset($where['mobileNbr']);
        }
        if(isset($where['realName'])){
            $subWhere['User.realName'] = array('like', '%'.$where['realName'].'%');
            $subWhere['User.nickName'] = array('like', '%'.$where['realName'].'%');
            $subWhere['_logic'] = 'OR';
            $where['_complex'] = $subWhere;
            unset($where['realName']);
        }
        return $where;
    }

    /**
     * 实物券 or 体验券买单
     * @param string $userCode 用户编码
     * @param string $shopCode 商户编码
     * @param string $userCouponCode 用户优惠券编码
     * @param string $appType 应用类型 0：商家端，1：顾客端，3：PC端
     * @return array
     */
    public function zeroPay($userCode, $shopCode, $userCouponCode, $appType){
        $rules = array(
            array('userCode', 'require', C('USER.USER_CODE_EMPTY'), 1),
            array('shopCode', 'require', C('SHOP.SHOP_CODE_EMPTY'), 1),
            array('userCouponCode', 'require', C('USER_COUPON.USER_COUPON_CODE_EMPTY'), 1),
        );
        $data = array(
            'userCode' => $userCode,
            'shopCode' => $shopCode,
            'userCouponCode' => $userCouponCode
        );
        if($this->validate($rules)->create($data)) {
            $userMdl = new UserModel();
            $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('nickName','realName','mobileNbr', 'avatarUrl'));
            if(!$userInfo)
                return $this->getBusinessCode(C('USER.USER_CODE_ERROR'));

            $shopMdl = new ShopModel();
            // 获得商家信息（商户名称）
            $shopInfo = $shopMdl->getShopInfo($shopCode, array('Shop.shopName','Shop.mobileNbr'));

            if(!$shopInfo)
                return $this->getBusinessCode(C('SHOP.SHOP_CODE_ERROR'));
            $orderConfirm = \Consts::NO;
            if(in_array($appType, array(C('LOGIN_TYPE.SHOP'), C('LOGIN_TYPE.PC')))){
                $orderConfirm = \Consts::YES;
            }
            $consumeCode = $this->create_uuid();
            $price = 0;
            M()->startTrans();
            $consumeOrderMdl = new ConsumeOrderModel();
            $addOrder = $consumeOrderMdl->addConsumeOrder($userCode, $price, $shopCode, C('UC_PAY_TYPE.COUPON'), \Consts::ORDER_TYPE_OTHER, $orderConfirm);
            if($addOrder['code'] != C('SUCCESS')){
                return $addOrder;
            }
            $userCouponMdl = new UserCouponModel();
            // 判断用户优惠券是否可用
            $isUserCouponCanUse = $userCouponMdl->isUserCouponCanBeUsed($userCouponCode, $price, $userCode);
            if($isUserCouponCanUse !== true) {
                return $this->getBusinessCode($isUserCouponCanUse);
            }
            // 改变用户优惠券状态
            $userCouponAction = array(
                'status' => \Consts::USER_COUPON_STATUS_USED, // 优惠券状态为已使用
                'consumeCode' => $consumeCode, // 消费编码
                'useCouponShopCode' => $shopCode, //优惠券核销店铺
                'useTime' => date('Y-m-d H:i:s',time()),//优惠券核销时间
            );
            // 使用优惠券
            $retOfUseCoupon = $userCouponMdl->useCoupon(array($userCouponCode), $userCouponAction);

            if( ! $retOfUseCoupon)
                return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
            // 获得使用的优惠券的信息
            $userCouponInfo = $userCouponMdl->getCouponInfo($userCouponCode, array('couponType', 'insteadPrice', 'payPrice', 'userCouponNbr'));

            // 添加消费记录
            $consumeLog = array(
                'consumeCode' => $consumeCode,
                'consumerCode' => $userCode,
                'isCard' => C('NO'),
                'couponUsed' => C('YES'),
                'consumeTime' => date('Y-m-d H:i:s', time()),
                'payedTime' => date('Y-m-d H:i:s', time()),
                'identityCode' => substr(md5(uniqid(mt_rand(), true)), 0, 12),
                'deduction' => 0,
                'realPay' => 0,
                'cardDeduction' => C('NO'),
                'couponDeduction' => $userCouponInfo['insteadPrice'],
                'location' => $shopCode,
                'status' => C('UC_STATUS.PAYED'),
                'payType' => C('UC_PAY_TYPE.COUPON'),
                'orderCode' => $addOrder['orderCode'],
                'platBonus' => 0,
                'shopBonus' => 0,
                'usedCardCode' => '',
                'usedUserCouponCode' => $userCouponCode
            );

            // 如果使用的优惠券是兑换券或者代金券的话，设置清算状态为未清算
//            if(in_array($userCouponInfo['couponType'], array(\Consts::COUPON_TYPE_EXCHANGE, \Consts::COUPON_TYPE_VOUCHER))) {
//                $consumeLog['liquidationStatus'] = \Consts::LIQUIDATION_STATUS_HAD_NOT;
//            }

            $retOfAddUserConsume = $this->add($consumeLog) !== false ? true : false;
//            $retOfAddUserConsume = true;
            if(! $retOfAddUserConsume) {
                M()->rollback();
                return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
            } else {
                M()->commit();



                // TODO 添加商家回头客人数
                $ret = $this->getBusinessCode(C('SUCCESS'));
                $ret['consumeCode'] = $consumeCode;
                $ret['hasSendCoupon'] = 0;
                $ret['userCouponCode'] = '';
				$ret['orderCode']=$addOrder['orderCode'];

//                $userName = $userInfo['realName']?$userInfo['realName']:$userInfo['nickName'];

                //优惠券消息
//                $userCoupon = $userCouponMdl->getUserCouponName($consumeCode);

                // 给用户添加用户使用优惠券的消息
//                $msgInfo = array(
//                    'title' => str_replace('{{shopName}}', $shopInfo['shopName'], C('MSG_TITLE_TDL.USE_COUPON')),
//                    'content' => C('COUPON_MSG_TDL.USED'),
//                    'createTime' => date('Y-m-d H:i:s'),
//                    'senderCode' => $shopCode,
//                    'type' => C('MESSAGE_TYPE.COUPON'),
//                    'userCouponCode' => $userCouponCode
//                );
//                $msgMdl = new MessageModel();
//                $msgMdl->addMsg($msgInfo, $userCode);

                // 使用优惠券获得圈值记录，并更新用户的历史圈值和当前圈值
                $userMdl = new UserModel();
                $userMdl->addPointEarningLog($userCode, 20, '使用优惠券');

                // 推送消息
//                $content = str_replace('{{realPay}}', 0, C('SHOP_PUSH_MSG_TDL.PAYED'));
//                $receiver = explode('-', $shopCode);
//                $jpushMdl = new JpushModel(C('SHOP_APP_KEY'), C('SHOP_MASTER_SECRET'));
//                $ccInfo = array(
//                    'avatarUrl' => $userInfo['avatarUrl'],
//                    'totalPay' => \Consts::NO,
//                    'realPay' => \Consts::NO,
//                    'bonusPay' => \Consts::NO,
//                    'couponPay' => $userCouponInfo['insteadPrice'] / \Consts::HUNDRED,
//                    'couponUsed' => \Consts::YES,
//                    'identityCode' => $consumeLog['identityCode'],
//                    'point' => 0,
//                    'consumeTime' => $consumeLog['consumeTime']
//                );
//                $jpushMdl->jPushByAction($receiver, $content, $ccInfo, C('PUSH_ACTION.CONSUME'));
//
//                // 向商家发短信
//                //1.员工级别：店长或大店长，活跃状态，短信接收设置为开启状态
//                $shopStaffRelMdl = new ShopStaffRelModel();
//                $shopStaffArr = $shopStaffRelMdl->getStaffList(array('ShopStaffRel.shopCode' => $shopCode, 'ShopStaff.status' => \Consts::SHOP_STAFF_STATUS_ACTIVE, 'ShopStaff.userLvl' => array('gt', \Consts::SHOP_STAFF_LVL_EMPLOYEE), 'ShopStaff.isSendPayedMsg' => C('YES')), array('ShopStaff.mobileNbr'));
//                //2.店长设置的其他员工
//                $mrMdl = new MessageRecipientModel();
//                $mRecipient = $mrMdl->getMRecipientList(array('shopCode' => $shopCode), array('mobileNbr'));
//                $shopStaffArr = array_merge($shopStaffArr, $mRecipient);
//                if($shopStaffArr){
//                    $shopStaffMobileArr = array();
//                    foreach($shopStaffArr as $v){
//                        if($v['mobileNbr'] && !in_array($v['mobileNbr'], $shopStaffMobileArr)){
//                            array_push($shopStaffMobileArr, $v['mobileNbr']);
//                        }
//                    }
//                    if($shopStaffMobileArr){
//                        $smsMdl = new SmsModel();
//                        try{
//                            $sMessage = str_replace(array('{{userName}}', '{{orderNbr}}', '{{realPay}}', '{{orderAmount}}', '{{deduction}}'), array($userName, substr($addOrder['orderNbr'],-5), 0, 0, 0), C('SEND_MESSAGE.S_PAYED'));
//                            $smsMdl->send($sMessage, $shopStaffMobileArr); // 商家店长支付成功短信
//                        }catch (RPCException $e){
//                            Log::write('---------SEND MESSAGE FAIL:'.$e->message.'---------', 'WARN', '', C('LOG_PATH').'sendMsg'.date('y_m_d').'.log');
//                        }
//                    }
//                }



                // 推送消息：给用户推送买的兑换券或代金券的使用
//                if($userCouponInfo && in_array($userCouponInfo['couponType'], array(\Consts::COUPON_TYPE_EXCHANGE, \Consts::COUPON_TYPE_VOUCHER)) && $userCouponInfo['payPrice'] > 0){
//                    $userCount = 1;
//                    $couponType = $userCouponInfo['couponType'] == \Consts::COUPON_TYPE_EXCHANGE ? '兑换券' : '代金券';
//                    $content = str_replace(array('{{userCount}}', '{{shopName}}', '{{couponType}}'), array($userCount, $shopInfo['shopName'], $couponType), C('PUSH_MESSAGE.PAY_COUPON_USE'));
//                    $jpushMdl = new JpushModel(C('CLIENT_APP_KEY'), C('CLIENT_MASTER_SECRET'));
//                    $extra = array(
//                        'couponCode' => $userCouponInfo['userCouponNbr'],
//                    );
//                    $jpushMdl->jPushByAction(array($userInfo['mobileNbr']), $content, $extra, C('PUSH_ACTION.PAY_COUPON_USE'));
//                }

                // 判断用户是否有该商家会员卡，没有则添加会员卡
                $userCardMdl = new UserCardModel();
                $userCardMdl->checkUserCard($userCode, $shopCode);
                return $ret;
            }
        } else {
            return $this->getValidErrorCode();
        }
    }
    public function zeroPayForGzBank($userCode, $shopCode, $userCouponCode, $appType){
        $rules = array(
            array('userCode', 'require', C('USER.USER_CODE_EMPTY'), 1),
            array('shopCode', 'require', C('SHOP.SHOP_CODE_EMPTY'), 1),
            array('userCouponCode', 'require', C('USER_COUPON.USER_COUPON_CODE_EMPTY'), 1),
        );
        $data = array(
            'userCode' => $userCode,
            'shopCode' => $shopCode,
            'userCouponCode' => $userCouponCode
        );
        if($this->validate($rules)->create($data)) {
            $userMdl = new UserModel();
            $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('nickName','realName','mobileNbr', 'avatarUrl'));
            if(!$userInfo)
                return $this->getBusinessCode(C('USER.USER_CODE_ERROR'));

            $shopMdl = new ShopModel();
            // 获得商家信息（商户名称）
            $shopInfo = $shopMdl->getShopInfo($shopCode, array('Shop.shopName','Shop.mobileNbr'));
            if(!$shopInfo)
                return $this->getBusinessCode(C('SHOP.SHOP_CODE_ERROR'));
            $orderConfirm = \Consts::NO;
            if(in_array($appType, array(C('LOGIN_TYPE.SHOP'), C('LOGIN_TYPE.PC')))){
                $orderConfirm = \Consts::YES;
            }
            // $consumeCode = $this->join("ordercoupon ON ordercoupon.orderCode = userconsume.orderCode")->where(array('ordercoupon.orderCouponCode'=>$orderCouponCode))->getField('userconsume.consumeCode');
            $consumeCode = $this->create_uuid();
            $price = 0;
            M()->startTrans();
            $consumeOrderMdl = new ConsumeOrderModel();
            $addOrder = $consumeOrderMdl->addConsumeOrder($userCode, $price, $shopCode, C('UC_PAY_TYPE.COUPON'), \Consts::ORDER_TYPE_OTHER, $orderConfirm);
            if($addOrder['code'] != C('SUCCESS')){
                return $addOrder;
            }

            $userCouponMdl = new UserCouponModel();
            // 判断用户优惠券是否可用
            $isUserCouponCanUse = $userCouponMdl->isUserCouponCanBeUsed($userCouponCode, $price, $userCode);
            if($isUserCouponCanUse !== true) {
                return $this->getBusinessCode($isUserCouponCanUse);
            }

            // 改变用户优惠券状态
            $userCouponAction = array(
               // 'status' => \Consts::USER_COUPON_STATUS_USED, // 优惠券状态为已使用
                'consumeCode' => $consumeCode, // 消费编码
                'useCouponShopCode' => $shopCode, //优惠券核销店铺
                'useTime' => date('Y-m-d H:i:s',time()),//优惠券核销时间
            );
            // 使用优惠券
            $retOfUseCoupon = $userCouponMdl->useCoupon(array($userCouponCode), $userCouponAction);
            if( ! $retOfUseCoupon)
                return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
            // 获得使用的优惠券的信息
            $userCouponInfo = $userCouponMdl->getCouponInfo($userCouponCode, array('couponType', 'insteadPrice', 'payPrice', 'userCouponNbr'));

            // 添加消费记录
            $consumeLog = array(
                'consumeCode' => $consumeCode,
                'consumerCode' => $userCode,
                'isCard' => C('NO'),
                'couponUsed' => C('YES'),
                'consumeTime' => date('Y-m-d H:i:s', time()),
                'payedTime' => date('Y-m-d H:i:s', time()),
                'identityCode' => substr(md5(uniqid(mt_rand(), true)), 0, 12),
                'deduction' => 0,
                'realPay' => 0,
                'cardDeduction' => C('NO'),
                'couponDeduction' => $userCouponInfo['insteadPrice'],
                'location' => $shopCode,
                'status' => C('UC_STATUS.PAYED'),
                'payType' => C('UC_PAY_TYPE.COUPON'),
                'orderCode' => $addOrder['orderCode'],
                'platBonus' => 0,
                'shopBonus' => 0,
                'usedCardCode' => '',
                'usedUserCouponCode' => $userCouponCode
            );

            // 如果使用的优惠券是兑换券或者代金券的话，设置清算状态为未清算
//            if(in_array($userCouponInfo['couponType'], array(\Consts::COUPON_TYPE_EXCHANGE, \Consts::COUPON_TYPE_VOUCHER))) {
//                $consumeLog['liquidationStatus'] = \Consts::LIQUIDATION_STATUS_HAD_NOT;
//            }

            $retOfAddUserConsume = $this->add($consumeLog) !== false ? true : false;
//            $retOfAddUserConsume = true;
            if(! $retOfAddUserConsume) {
                M()->rollback();
                return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
            } else {
                M()->commit();



                // TODO 添加商家回头客人数
                $ret = $this->getBusinessCode(C('SUCCESS'));
                $ret['consumeCode'] = $consumeCode;
                $ret['hasSendCoupon'] = 0;
                $ret['userCouponCode'] = '';
                $ret['orderCode']=$addOrder['orderCode'];

//                $userName = $userInfo['realName']?$userInfo['realName']:$userInfo['nickName'];

                //优惠券消息
//                $userCoupon = $userCouponMdl->getUserCouponName($consumeCode);

                // 给用户添加用户使用优惠券的消息
//                $msgInfo = array(
//                    'title' => str_replace('{{shopName}}', $shopInfo['shopName'], C('MSG_TITLE_TDL.USE_COUPON')),
//                    'content' => C('COUPON_MSG_TDL.USED'),
//                    'createTime' => date('Y-m-d H:i:s'),
//                    'senderCode' => $shopCode,
//                    'type' => C('MESSAGE_TYPE.COUPON'),
//                    'userCouponCode' => $userCouponCode
//                );
//                $msgMdl = new MessageModel();
//                $msgMdl->addMsg($msgInfo, $userCode);

                // 使用优惠券获得圈值记录，并更新用户的历史圈值和当前圈值
                $userMdl = new UserModel();
                $userMdl->addPointEarningLog($userCode, 20, '使用优惠券');

                // 推送消息
//                $content = str_replace('{{realPay}}', 0, C('SHOP_PUSH_MSG_TDL.PAYED'));
//                $receiver = explode('-', $shopCode);
//                $jpushMdl = new JpushModel(C('SHOP_APP_KEY'), C('SHOP_MASTER_SECRET'));
//                $ccInfo = array(
//                    'avatarUrl' => $userInfo['avatarUrl'],
//                    'totalPay' => \Consts::NO,
//                    'realPay' => \Consts::NO,
//                    'bonusPay' => \Consts::NO,
//                    'couponPay' => $userCouponInfo['insteadPrice'] / \Consts::HUNDRED,
//                    'couponUsed' => \Consts::YES,
//                    'identityCode' => $consumeLog['identityCode'],
//                    'point' => 0,
//                    'consumeTime' => $consumeLog['consumeTime']
//                );
//                $jpushMdl->jPushByAction($receiver, $content, $ccInfo, C('PUSH_ACTION.CONSUME'));
//
//                // 向商家发短信
//                //1.员工级别：店长或大店长，活跃状态，短信接收设置为开启状态
//                $shopStaffRelMdl = new ShopStaffRelModel();
//                $shopStaffArr = $shopStaffRelMdl->getStaffList(array('ShopStaffRel.shopCode' => $shopCode, 'ShopStaff.status' => \Consts::SHOP_STAFF_STATUS_ACTIVE, 'ShopStaff.userLvl' => array('gt', \Consts::SHOP_STAFF_LVL_EMPLOYEE), 'ShopStaff.isSendPayedMsg' => C('YES')), array('ShopStaff.mobileNbr'));
//                //2.店长设置的其他员工
//                $mrMdl = new MessageRecipientModel();
//                $mRecipient = $mrMdl->getMRecipientList(array('shopCode' => $shopCode), array('mobileNbr'));
//                $shopStaffArr = array_merge($shopStaffArr, $mRecipient);
//                if($shopStaffArr){
//                    $shopStaffMobileArr = array();
//                    foreach($shopStaffArr as $v){
//                        if($v['mobileNbr'] && !in_array($v['mobileNbr'], $shopStaffMobileArr)){
//                            array_push($shopStaffMobileArr, $v['mobileNbr']);
//                        }
//                    }
//                    if($shopStaffMobileArr){
//                        $smsMdl = new SmsModel();
//                        try{
//                            $sMessage = str_replace(array('{{userName}}', '{{orderNbr}}', '{{realPay}}', '{{orderAmount}}', '{{deduction}}'), array($userName, substr($addOrder['orderNbr'],-5), 0, 0, 0), C('SEND_MESSAGE.S_PAYED'));
//                            $smsMdl->send($sMessage, $shopStaffMobileArr); // 商家店长支付成功短信
//                        }catch (RPCException $e){
//                            Log::write('---------SEND MESSAGE FAIL:'.$e->message.'---------', 'WARN', '', C('LOG_PATH').'sendMsg'.date('y_m_d').'.log');
//                        }
//                    }
//                }



                // 推送消息：给用户推送买的兑换券或代金券的使用
//                if($userCouponInfo && in_array($userCouponInfo['couponType'], array(\Consts::COUPON_TYPE_EXCHANGE, \Consts::COUPON_TYPE_VOUCHER)) && $userCouponInfo['payPrice'] > 0){
//                    $userCount = 1;
//                    $couponType = $userCouponInfo['couponType'] == \Consts::COUPON_TYPE_EXCHANGE ? '兑换券' : '代金券';
//                    $content = str_replace(array('{{userCount}}', '{{shopName}}', '{{couponType}}'), array($userCount, $shopInfo['shopName'], $couponType), C('PUSH_MESSAGE.PAY_COUPON_USE'));
//                    $jpushMdl = new JpushModel(C('CLIENT_APP_KEY'), C('CLIENT_MASTER_SECRET'));
//                    $extra = array(
//                        'couponCode' => $userCouponInfo['userCouponNbr'],
//                    );
//                    $jpushMdl->jPushByAction(array($userInfo['mobileNbr']), $content, $extra, C('PUSH_ACTION.PAY_COUPON_USE'));
//                }

                // 判断用户是否有该商家会员卡，没有则添加会员卡
                $userCardMdl = new UserCardModel();
                $userCardMdl->checkUserCard($userCode, $shopCode);
                return $ret;
            }
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * @param $consumeCode
     * @param $userCouponCode
     * @param $platBonus
     * @param $shopBonus
     * @param $price
     * @param $payType
     * @return array
     */
    public function updatePay($consumeCode, $userCouponCode, $platBonus, $shopBonus, $price, $payType) {
        $rules = array(
            array('consumeCode', 'require', C('USER.USER_CODE_EMPTY'), 1),
            array('price', 'require', C('PAY.PRICE_EMPTY'), 1),
            array('price', 'is_numeric', C('PAY.PRICE_ERROR'), 1, 'function'),
        );
        $data = array(
            'consumeCode' => $consumeCode,
            'price' => $price
        );
        if($this->validate($rules)->create($data)) {
            $consumeInfo = $this->getConsumeInfo(array('consumeCode' => $consumeCode), array('UserConsume.location,UserConsume.consumerCode,UserConsume.orderCode'));

            $price = $price * C('RATIO');
            $realPay = $price;
            $platBonus = $platBonus * C('RATIO');
            $shopBonus = $shopBonus * C('RATIO');

            $userCardMdl = new UserCardModel();
            $userCard = $userCardMdl->getBestUserCard($consumeInfo['consumerCode'], $consumeInfo['location']);
            if($userCard){
                $userCardCode = $userCard['userCardCode'];
                $field = array(
                    'UserCard.point',
                    'Card.discount',
                    'Card.discountRequire',
                    'Card.pointsPerCash',
                    'Card.cardCode',
                );
                $userCardInfo = $userCardMdl->getUserCardInfo($userCardCode, $field);
            }

            M()->startTrans();
            $consumeOrderMdl = new ConsumeOrderModel();
            //修改订单的支付状态为未支付，修改订单的实际消费金额
            $updateOrder = $consumeOrderMdl->updateConsumeOrder(array('orderCode' => $consumeInfo['orderCode']), array('orderAmount' => $price, 'status' => C('ORDER_STATUS.UNPAYED')));

            $userCouponMdl = new UserCouponModel();
            if($userCouponCode) {
                // 判断用户优惠券是否可用
                $isUserCouponCanUse = $userCouponMdl->isUserCouponCanBeUsed($userCouponCode, $price, $consumeInfo['consumerCode']);
                if($isUserCouponCanUse !== true) {
                    return $this->getBusinessCode($isUserCouponCanUse);
                }
            }

            $userCouponInfo = $userCouponMdl->getCouponInfo($userCouponCode, array('couponType', 'insteadPrice'));
            if($userCouponInfo && $userCouponInfo['couponType'] == C('COUPON_TYPE.N_PURCHASE')){ // N元购型优惠券的使用
                $realPay = $userCouponInfo['insteadPrice'];
            }else{ // 其他类型优惠券或不使用优惠券
                if($userCouponInfo) {
                    // 使用优惠券，得到实际付款金额
                    $realPay = $userCouponMdl->getRealPay($userCouponCode, $realPay, 1);
                }

                $cardInsteadPrice = 0;
                if(isset($userCardInfo) && $userCardInfo) {
                    // 使用会员卡，得到实际付款金额
                    $userCardPoint = $userCardInfo['point'];
                    if($userCardPoint >= $userCardInfo['discountRequire'] && $userCardInfo['discount'] > 0){
                        $cardInsteadPrice = $realPay - $realPay * $userCardInfo['discount'] / 10;
                    }
                }
                $cardInsteadPrice = ceil($cardInsteadPrice);
                $realPay = $realPay - $cardInsteadPrice;

                $bsMdl = new BonusStatisticsModel();
                if($platBonus > 0){
                    // 使用平台红包
                    $platBonusRet = $bsMdl->reduceBonusStatistics($consumeInfo['consumerCode'], C('HQ_CODE'), $platBonus);
                    if($platBonusRet['code'] == C('SUCCESS')){
                        $realPay = $realPay - $platBonus;
                    }else{
                        return $platBonusRet;
                    }
                }

                if($shopBonus > 0){
                    // 使用商家红包
                    $shopBonusRet = $bsMdl->reduceBonusStatistics($consumeInfo['consumerCode'], $consumeInfo['location'], $shopBonus);
                    if($shopBonusRet['code'] == C('SUCCESS')){
                        $realPay = $realPay - $shopBonus;
                    }else{
                        return $shopBonusRet;
                    }
                }
            }

            if($userCouponInfo){
                // 改变用户优惠券状态
                $userCouponAction = array(
                    'status' => C('USER_COUPON_STATUS.USED'),
                    'consumeCode' => $consumeCode
                );
                $retOfUseCoupon = $userCouponMdl->useCoupon(array($userCouponCode), $userCouponAction);
                if( ! $retOfUseCoupon)
                    return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
            }

            // 添加消费记录
            $consumeLog = array(
                'consumeCode' => $consumeCode,
                'isCard' => isset($cardInsteadPrice) &&  $cardInsteadPrice != 0 ? C('YES') : C('NO'),
                'couponUsed' => isset($userCouponAction) ? C('YES') : C('NO'),
                'consumeTime' => date('Y-m-d H:i:s', time()),
//                'identityCode' => substr(md5(uniqid(mt_rand(), true)), 0, 12),
                'deduction' => ($price - $realPay),
                'realPay' => $realPay,
                'status' => C('UC_STATUS.UNPAYED'),
                'payType' => $payType,
                'platBonus' => $platBonus,
                'shopBonus' => $shopBonus,
                'usedCardCode' => isset($userCardInfo['cardCode']) ? $userCardInfo['cardCode'] : '',
            );
            $retOfAddUserConsume = $this->save(array('consumeCode'=>$consumeCode), $consumeLog) !== false ? true : false;
            if(! ($retOfAddUserConsume && $updateOrder)) {
                M()->rollback();
                return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
            } else {
                M()->commit();
                $ret = $this->getBusinessCode(C('SUCCESS'));
                $ret['consumeCode'] = $consumeCode;
                $ret['realPay'] = number_format(($realPay / C('RATIO')), 2,'.', '');
                return $ret;
            }
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 大米活动实物券的领用
     * @param $batchCouponCode
     * @param $userCode
     * @return array
     */
    public function grabRiceCoupon($batchCouponCode, $userCode){
        // 领券
        $userCouponMdl = new UserCouponModel();
        $ret = $userCouponMdl->grabCoupon($batchCouponCode, $userCode, 0, 1);
        if($ret['code'] == C('SUCCESS')){ // 领券失败，红包加回，去除消费记录
            // 先查询领券用户的红包金额是否符合条件（通过该用户推荐码注册惠圈，且绑卡成功可获得10元平台红包，5人即可领用一张大米兑换券）
            $bsMdl = new BonusStatisticsModel();
            $bonusRet = $bsMdl->reduceBonusStatistics($userCode, C('HQ_CODE'), 5000);
            // 红包条件满足的情况下，记录该次消费记录，消费50元红包的消费记录
            $consumeLog = array(
                'consumeCode' => $this->create_uuid(),
                'consumerCode' => $userCode,
                'isCard' => 0,
                'couponUsed' => 0,
                'consumeTime' => date('Y-m-d H:i:s', time()),
                'identityCode' => substr(md5(uniqid(mt_rand(), true)), 0, 12),
                'deduction' => 0,
                'realPay' => 0,
                'cardDeduction' => 0,
                'couponDeduction' => 0,
                'location' => C('HQ_CODE'),
                'status' => C('UC_STATUS.PAYED'),
                'payType' => C('UC_PAY_TYPE.NULL'),
                'orderCode' => '',
                'platBonus' => 5000,
                'shopBonus' => 0,
                'usedCardCode' => '',
            );
            $consumeRet = $this->add($consumeLog);
        }
        return $ret;
    }

    /**
     * 第二次过滤条件
     * @param array $where
     * @return array $where
     */
    public function filterUserConsumeWhere(&$where) {
        if($where['shopCode']){
            $where['UserConsume.location'] = $where['shopCode'];
            unset($where['shopCode']);
        }
        if($where['status']){
            if($where['status'] == 100){
                $where['status'] = 0;
            }
            $where['ConsumeOrder.status'] = $where['status'];
            unset($where['status']);
        }
        if($where['mobileNbr']){
            $where['User.mobileNbr'] = $where['mobileNbr'];
            unset($where['mobileNbr']);
        }
        if($where['orderNbr']){
            $where['ConsumeOrder.orderNbr'] = $where['orderNbr'];
            unset($where['orderNbr']);
        }
        if(isset($where['lastFourOfOrderNbr']) && $where['lastFourOfOrderNbr']){
            $where['ConsumeOrder.orderNbr'] = array('like', "%{$where['lastFourOfOrderNbr']}");
            unset($where['lastFourOfOrderNbr']);
        }

        if(isset($where['search']) && $where['search']){
            $subCondition['User.mobileNbr'] = array('like','%'.$where['search'].'%');
            $subCondition['ConsumeOrder.orderNbr'] = array('like', "%{$where['search']}");
            $subCondition['ConsumeOrder.mealNbr'] = array('like', '%'.$where['search'].'%');
            $subCondition['_logic'] = 'or';
            $where['_complex'] = $subCondition;
            unset($where['search']);
        }

        if ($where['orderTimeStart'] && $where['orderTimeEnd']) {
            $where['orderTimeEnd'] = $where['orderTimeEnd'] . ' 23:59:59';
            $where['ConsumeOrder.orderTime'] = array('BETWEEN', array($where['orderTimeStart'], $where['orderTimeEnd']));
        } elseif ($where['orderTimeStart'] && !$where['orderTimeEnd']) {
            $where['ConsumeOrder.orderTime'] = array('EGT', $where['orderTimeStart']);
        } elseif (! $where['orderTimeStart'] && $where['orderTimeEnd']) {
            $where['orderTimeEnd'] = $where['orderTimeEnd'] . ' 23:59:59';
            $where['ConsumeOrder.orderTime'] = array('ELT', $where['orderTimeEnd']);
        }
        unset($where['orderTimeStart']);
        unset($where['orderTimeEnd']);

        if($where['city']){
            $where['Shop.city'] = $where['city'];
            unset($where['city']);
        }

        return $where;
    }

    /**
     * 管理端获得订单支付列表
     * @param array $filterData
     * @param object $page 偏移值
     * @param string $sortType 排序方式
     * @param array $field 要查询的字段
     * @return null|array 如果为空，返回null；如果不为空，返回二维数组array(array());
     */
    public function listUserConsume($filterData, $page, $sortType = '', $field = array()) {
        $where = $this->filterWhere(
            $filterData,
            array('shopName' => 'like', 'mobileNbr' => 'like', 'orderNbr' => 'like'),
            $page);
        $where = $this->filterUserConsumeWhere($where);
        if(empty($sortType)) {
            $sortType = 'consumeTime desc, orderTime desc';
        }
        if(empty($field)) {
            $field = array('orderTime', 'orderNbr', 'orderAmount', 'shopName', 'Shop.logoUrl', 'Shop.city', 'hqIcbcShopNbr', 'nickName', 'User.avatarUrl', 'User.mobileNbr' => 'userMobileNbr', 'deduction', 'realPay', 'platBonus', 'shopBonus', 'UserConsume.status' => 'userConsumeStatus', 'isCard', 'consumeCode', 'couponUsed', 'bankCardDeduction', 'firstDeduction', 'payType', 'usedCardCode', 'usedUserCouponCode', 'orderType', 'orderStatus', 'ConsumeOrder.orderCode', 'userCouponCode', 'orderConfirm', 'consumeTime', 'couponDeduction', 'cardDeduction', 'realPay', 'ConsumeOrder.orderTime', 'UserConsume.payedTime');
        }

        //判断是否是惠圈管理人员
        if(isset($_SESSION['USER']['bank_id'])&& $_SESSION['USER']['bank_id']!=-1){
            $where['Shop.bank_id'] = $_SESSION['USER']['bank_id'];
        }

        $userConsumeList =  $this
            ->field($field)
            ->join('Shop ON Shop.shopCode = UserConsume.location', 'left')
            ->join('User ON User.userCode = UserConsume.consumerCode', 'left')
            ->join('ConsumeOrder ON ConsumeOrder.orderCode = UserConsume.orderCode')
            ->where($where)
            ->order($sortType)
            ->pager($page)
            ->select();
        $temArray = array('deduction', 'realPay', 'platBonus', 'shopBonus', 'orderAmount', 'bankCardDeduction', 'couponDeduction', 'cardDeduction', 'firstDeduction');
        foreach($userConsumeList as $k => $v) {
            $districtMdl = new DistrictModel();
            $info = $districtMdl->getCityInfo(array('name' => array('like', '%'.$v['city'].'%')), array('areaNbr'));
            $userConsumeList[$k]['areaNbr'] = isset($info['areaNbr']) ? $info['areaNbr'] : '';
            foreach($temArray as $item) {
                if(isset($userConsumeList[$k][$item])) {
                    $userConsumeList[$k][$item] = number_format($v[$item] / C('RATIO'), 2, '.', '');
                }
            }
        }
        return $userConsumeList;
    }

    /**
     * 获得支付记录总数
     * @param array $filterData
     * @return null|int 如果为空，返回null；如果不为空，返回number;
     */
    public function countUserConsume($filterData) {
        $where = $this->filterWhere(
            $filterData,
            array('shopName' => 'like', 'mobileNbr' => 'like', 'orderNbr' => 'like')
        );
        $where = $this->filterUserConsumeWhere($where);

        //判断是否是惠圈管理人员
        if(isset($_SESSION['USER']['bank_id'])&& $_SESSION['USER']['bank_id']!=-1){
                $where['Shop.bank_id'] = $_SESSION['USER']['bank_id'];
        }

        return $this
            ->join('Shop ON Shop.shopCode = UserConsume.location')
            ->join('User ON User.userCode = UserConsume.consumerCode')
            ->join('ConsumeOrder ON ConsumeOrder.orderCode = UserConsume.orderCode')
            ->where($where)
            ->count('UserConsume.consumeCode');
    }

    /**
     * 获取某一天商家流入资金清算
     */
    public function shopFundSettlement($areaNbr, $date, $export = 1){
        $condition = array(
            'UserConsume.payedTime' => array('like', "$date%"),
            'UserConsume.status' => C('UC_STATUS.PAYED'), //支付成功
            'UserConsume.liquidationStatus' => \Consts::LIQUIDATION_STATUS_HAD_NOT, //未清算
            'UserConsume.payType' => array('in', array(C('UC_PAY_TYPE.BANKCARD'), C('UC_PAY_TYPE.COUPON'))), //线上银行卡支付
            'ConsumeOrder.orderType' => array('NOTIN', array(\Consts::ORDER_TYPE_COUPON, \Consts::ORDER_TYPE_ACT)), //订单类型不是买券的
            '_string' => '((Shop.hqIcbcShopNbr is not null AND Shop.hqIcbcShopNbr <> "") AND (Shop.addCardNo is not null AND Shop.addCardNo <> "") AND (Shop.addCardUserName is not null AND Shop.addCardUserName <> ""))',
        );
        if($areaNbr){
            $condition['Shop.hqIcbcShopNbr'] = array('like', "{$areaNbr}%");
        }
        $this->field(array('Shop.shopCode', 'Shop.hqIcbcShopNbr', 'Shop.addCardNo', 'Shop.addCardUserName', 'UserConsume.payedTime', 'UserConsume.realPay', 'UserConsume.consumeCode', 'Shop.shopName', 'UserConsume.payType'));
        $this->join('Shop on Shop.shopCode = UserConsume.location');
        $this->join('ConsumeOrder on ConsumeOrder.orderCode = UserConsume.orderCode');
        $this->where($condition);
        $log = $this->select();
        $records = 0;
        $totalAmount = 0;
        $result = array();
        foreach($log as $v){
            if($v['realPay'] > 0){
                $totalAmount += $v['realPay'];
                if(isset($result[$v['shopCode']])){
                    $result[$v['shopCode']]['dueAmt'] += $v['realPay'];
                }else{
                    $records += 1;
                    $result[$v['shopCode']] = array(
                        'cmpyTransferId' => $v['hqIcbcShopNbr'],
                        'acctNo' => $v['addCardNo'],
                        'acctName' => $v['addCardUserName'],
                        'dueAmt' => $v['realPay'],
                        'settleDate' => date('Ymd', strtotime($v['payedTime'])),
                        'shopName' => $v['shopName'],
                        'payedTime' => date('Y-m-d', strtotime($v['payedTime'])),
                        'varInfo' => '清分'
                    );
                }
            }

            if($export){
                if($v['realPay'] > 0){
                    //将这笔账单的清算状态改为清算中
                    $this->where(array('consumeCode'=>$v['consumeCode']))->save(array('liquidationStatus'=>\Consts::LIQUIDATION_STATUS_ING));
                }else{
                    //将这笔账单的清算状态改为已清算
                    $this->where(array('consumeCode'=>$v['consumeCode']))->save(array('liquidationStatus'=>\Consts::LIQUIDATION_STATUS_HAD));
                }
            }

            //清算使用的代金券和兑换券的钱
            if($v['payType'] == C('UC_PAY_TYPE.COUPON')){
                $userCouponMdl = new UserCouponModel();
                $orderCouponCode = $userCouponMdl->getUserConsumeCouponList($v['consumeCode'], array('distinct(orderCouponCode)'));
                if($orderCouponCode){
                    $orderCouponCode = $orderCouponCode[0]['orderCouponCode'];
                    //清算已使用的未清算买券的账单(先清算商家红包，再清算平台红包，最后清算卡)
                    $orderCouponMdl = new OrderCouponModel();
                    $orderCouponJoinTableArr = array(
                        array(
                            'joinTable' => 'UserConsume',
                            'joinCondition' => 'OrderCoupon.orderCode = UserConsume.orderCode',
                            'joinType' => 'inner'
                        ),
                    );
                    $orderCouponCondition = array(
                        'OrderCoupon.orderCouponCode' => $orderCouponCode,
                        'UserConsume.status' => C('UC_STATUS.PAYED'), //支付成功
                    );
                    $orderCouponInfo = $orderCouponMdl->getOrderCouponInfo($orderCouponCondition, array('OrderCoupon.orderCode', 'UserConsume.platBonus', 'UserConsume.shopBonus'), $orderCouponJoinTableArr);

                    $auditingAmount = $orderCouponMdl->sumRealPrice($orderCouponInfo['orderCode'], \Consts::LIQUIDATION_STATUS_HAD_NOT, \Consts::ORDER_COUPON_STATUS_USED); //获取该笔订单需要清算的金额
                    if($auditingAmount > 0){
                        $totalAmount += $auditingAmount;
                        if(isset($result[$v['shopCode']])){
                            $result[$v['shopCode']]['dueAmt'] += $auditingAmount;
                        }else{
                            $records += 1;
                            $result[$v['shopCode']] = array(
                                'cmpyTransferId' => $v['hqIcbcShopNbr'],
                                'acctNo' => $v['addCardNo'],
                                'acctName' => $v['addCardUserName'],
                                'dueAmt' => $auditingAmount,
                                'settleDate' => date('Ymd', strtotime($v['payedTime'])),
                                'shopName' => $v['shopName'],
                                'payedTime' => date('Y-m-d', strtotime($v['payedTime'])),
                                'varInfo' => '清分'
                            );
                        }
                    }
                    if($export){
                        //将这笔买的券的清算状态改为清算中
                        $condition = array(
                            'status' => \Consts::ORDER_COUPON_STATUS_USED,
                            'liquidationStatus' => \Consts::LIQUIDATION_STATUS_HAD_NOT,
                            'orderCode' => $v['orderCode'],
                        );
                        $orderCouponMdl->editOrderCoupon($condition, array('liquidationStatus'=>\Consts::LIQUIDATION_STATUS_ING));
                    }
                }
            }
        }

        //活动清算
        $userActCodeMdl = new UserActCodeModel();
        $actCondition = array(
            'UserActCode.status' => \Consts::ACT_CODE_STATUS_USED,
            'UserActCode.liquidationStatus' => \Consts::LIQUIDATION_STATUS_HAD_NOT,
            'UserActCode.valTime' => array('like', "$date%"),
            '_string' => '((Shop.hqIcbcShopNbr is not null AND Shop.hqIcbcShopNbr <> "") AND (Shop.addCardNo is not null AND Shop.addCardNo <> "") AND (Shop.addCardUserName is not null AND Shop.addCardUserName <> ""))',
        );
        if($areaNbr){
            $actCondition['Shop.hqIcbcShopNbr'] = array('like', "{$areaNbr}%");
        }
        $userActCodeList = $userActCodeMdl->getActCodeList(
            $actCondition,
            array(
                'UserActCode.userActCodeId',
                'UserActCode.bankCardAmount',
                'UserActCode.valTime',
                'Shop.shopCode',
                'Shop.shopName',
                'Shop.hqIcbcShopNbr',
                'Shop.addCardNo',
                'Shop.addCardUserName',
            ),
            array(
                array(
                    'joinTable' => 'UserActivity',
                    'joinCondition' => 'UserActivity.userActivityCode = UserActCode.userActCode',
                    'joinType' => 'inner',
                ),
                array(
                    'joinTable' => 'Activity',
                    'joinCondition' => 'Activity.activityCode = UserActivity.activityCode',
                    'joinType' => 'inner',
                ),
                array(
                    'joinTable' => 'Shop',
                    'joinCondition' => 'Shop.shopCode = Activity.shopCode',
                    'joinType' => 'inner',
                ),
            )
        );
        if($userActCodeList){
            foreach($userActCodeList as $act){
                if($act['bankCardAmount'] > 0){
                    $totalAmount += $act['bankCardAmount'];
                    if(isset($result[$act['shopCode']])){
                        $result[$act['shopCode']]['dueAmt'] += $act['bankCardAmount'];
                    }else{
                        $records += 1;
                        $result[$act['shopCode']] = array(
                            'cmpyTransferId' => $act['hqIcbcShopNbr'],
                            'acctNo' => $act['addCardNo'],
                            'acctName' => $act['addCardUserName'],
                            'dueAmt' => $act['bankCardAmount'],
                            'settleDate' => date('Ymd', strtotime($act['valTime'])),
                            'shopName' => $act['shopName'],
                            'payedTime' => date('Y-m-d', strtotime($act['valTime'])),
                            'varInfo' => '清分'
                        );
                    }
                }
                if($export){
                    if($act['bankCardAmount'] > 0){
                        //将清算状态改为清算中
                        $userActCodeMdl->updateUserActCode(array('userActCodeId'=>$act['userActCodeId']), array('liquidationStatus'=>\Consts::LIQUIDATION_STATUS_ING));
                    }else{
                        //将清算状态改为已清算
                        $userActCodeMdl->updateUserActCode(array('userActCodeId'=>$act['userActCodeId']), array('liquidationStatus'=>\Consts::LIQUIDATION_STATUS_HAD));
                    }
                }
            }
        }

        //退款清算
        $refundLogMdl = new RefundLogModel();
        $refundCondition = array(
            'RefundLog.liquidationStatus' => \Consts::LIQUIDATION_STATUS_HAD_NOT,
            'RefundLog.createTime' => array('like', "$date%"),
            '_string' => '((Shop.hqIcbcShopNbr is not null AND Shop.hqIcbcShopNbr <> "") AND (Shop.addCardNo is not null AND Shop.addCardNo <> "") AND (Shop.addCardUserName is not null AND Shop.addCardUserName <> ""))',
        );
        if($areaNbr){
            $refundCondition['Shop.hqIcbcShopNbr'] = array('like', "{$areaNbr}%");
        }
        $refundLogList = $refundLogMdl->getRefundLogList(
            $refundCondition,
            array(
                'RefundLog.refundCode',
                'RefundLog.orderNbr',
                'RefundLog.createTime',
                'RefundLog.refundPrice',
                'RefundLog.refundAccount',
                'Shop.shopCode',
                'Shop.shopName',
                'Shop.hqIcbcShopNbr',
                'Shop.addCardNo',
                'Shop.addCardUserName',
                'BankAccount.accountName',
                'BankAccount.bankCard',
                'ConsumeOrder.orderCode'
            ),
            array(
                array(
                    'joinTable' => 'ConsumeOrder',
                    'joinCondition' => 'ConsumeOrder.orderNbr = RefundLog.orderNbr',
                    'joinType' => 'inner',
                ),
                array(
                    'joinTable' => 'Shop',
                    'joinCondition' => 'Shop.shopCode = ConsumeOrder.shopCode',
                    'joinType' => 'inner',
                ),
                array(
                    'joinTable' => 'BankAccount',
                    'joinCondition' => 'BankAccount.bankAccountCode = RefundLog.refundAccount',
                    'joinType' => 'inner',
                ),
            )
        );
        if($refundLogList){
            $userConsumeMdl = new UserConsumeModel();
            foreach($refundLogList as $refund){
                if($refund['refundPrice'] > 0){
                    $userConsumeInfo = $userConsumeMdl->getOrderCurrPayInfo($refund['orderCode'], array('consumeCode', 'liquidationStatus'));
                    if($userConsumeInfo['liquidationStatus'] == \Consts::LIQUIDATION_STATUS_HAD_NOT){ //如果退款订单未清算
                        $totalAmount += $refund['refundPrice'];
                        if(isset($result[$refund['refundAccount']])){
                            $result[$refund['refundAccount']]['dueAmt'] += $refund['refundPrice'];
                        }else{
                            $records += 1;
                            $result[$refund['refundAccount']] = array(
                                'cmpyTransferId' => $refund['hqIcbcShopNbr'],
                                'acctNo' => $refund['bankCard'],
                                'acctName' => $refund['accountName'],
                                'dueAmt' => $refund['refundPrice'],
                                'settleDate' => date('Ymd', strtotime($refund['createTime'])),
                                'shopName' => $refund['shopName'],
                                'payedTime' => date('Y-m-d', strtotime($refund['createTime'])),
                                'varInfo' => '退货'
                            );
                        }
                        if($export){
                            //将这笔账单的清算状态改为不需要清算
                            $this->where(array('consumeCode'=>$userConsumeInfo['consumeCode']))->save(array('liquidationStatus'=>\Consts::LIQUIDATION_STATUS_NO_NEED));
                            //将这笔退款记录的清算状态改为清算中
                            $refundLogMdl->editRefundLog(array('refundCode'=>$refund['refundCode'], 'liquidationStatus'=>\Consts::LIQUIDATION_STATUS_ING));
                        }
                    }else{
                        if(isset($result[$refund['shopCode']]) && $result[$refund['shopCode']]['dueAmt'] >= $refund['refundPrice']){
                            // 当天要清算给商户的钱大于等于要退给用户的钱,则在商户清算的钱里减去用户要退的钱
                            $rest = $result[$refund['shopCode']]['dueAmt'] - $refund['refundPrice'];
                            if($rest == 0){
                                unset($result[$refund['shopCode']]);
                                $records -=1;
                            }else{
                                $result[$refund['shopCode']]['dueAmt'] = $rest;
                            }

                            if(isset($result[$refund['refundAccount']])){
                                $result[$refund['refundAccount']]['dueAmt'] += $refund['refundPrice'];
                            }else{
                                $records += 1;
                                $result[$refund['refundAccount']] = array(
                                    'cmpyTransferId' => $refund['hqIcbcShopNbr'],
                                    'acctNo' => $refund['bankCard'],
                                    'acctName' => $refund['accountName'],
                                    'dueAmt' => $refund['refundPrice'],
                                    'settleDate' => date('Ymd', strtotime($refund['createTime'])),
                                    'shopName' => $refund['shopName'],
                                    'payedTime' => date('Y-m-d', strtotime($refund['createTime'])),
                                    'varInfo' => '退货'
                                );
                            }

                            if($export){
                                //将这笔退款记录的清算状态改为清算中
                                $refundLogMdl->editRefundLog(array('refundCode'=>$refund['refundCode'], 'liquidationStatus'=>\Consts::LIQUIDATION_STATUS_ING));
                            }
                        }
                    }
                }else{
                    if($export){
                        //将这笔退款记录的清算状态改为已清算
                        $refundLogMdl->editRefundLog(array('refundCode'=>$refund['refundCode'], 'liquidationStatus'=>\Consts::LIQUIDATION_STATUS_HAD));
                    }
                }
            }
        }

        return array(
            'records' => $records,
            'totalAmount' => $totalAmount,
            'log' => $result
        );
    }



}
