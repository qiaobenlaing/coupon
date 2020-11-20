<?php
namespace Common\Model;
use Think\Model;
/**
 * batchCoupon表
 * @author
 */
class BatchCouponModel extends BaseModel {
    protected $tableName = 'BatchCoupon';
    const NO_LIMIT = 0;
    private $reduceNbr = 1;
    private $discountRatio = 10;

    /**
     * 获得优惠券类型
     * @return array $couponType 二维数组，格式：array(array('name' => '优惠券类型中文描述', 'value' => '券类型的值'), ...)
     */
    public function getCouponType() {
        $couponType = array();
        foreach(C('SHOP_NORMAL_COUPON') as $k => $v){
            $couponType[] = array(
                'name'  => C('COUPON_TYPE_NAME.'.$k),
                'value' => $v
            );
        }
        return $couponType;
    }

    /**
     * 判断用户是否可以购买该优惠券
     * @param string $userCode 用户编码
     * @param string $batchCouponCode 优惠券编码
     * @param int $couponNbr 用户要购买的数量
     * @return boolean|string 可以购买返回true，不可以购买返回错误信息
     */
    public function isUserCanBuyCoupon($userCode, $batchCouponCode, $couponNbr) {
        $batchCouponInfo = $this->getCouponBasicInfo(array('batchCouponCode' => $batchCouponCode), array('endTakingTime', 'dayEndUsingTime', 'nbrPerPerson', 'remaining', 'totalVolume'));
        // 判断当前时间是否已经过了优惠券的最后购买时间
        if(strtotime(substr($batchCouponInfo['endTakingTime'], 0, 11) . $batchCouponInfo['dayEndUsingTime']) < time()) {
            return array('code' => C('COUPON.EXPIRED'));
        }
        // 判断优惠券是否还有剩余数量
        if($batchCouponInfo['totalVolume'] > -1) {
            if($batchCouponInfo['remaining'] <= 0){
                return array('code' => C('COUPON.BEEN_TOKEN_OVER'));
            }
            if($batchCouponInfo['remaining'] < $couponNbr){
                return array('code' => C('COUPON.REST_ERROR'), 'remaining' => $batchCouponInfo['remaining']);
            }
        }

        // 判断该用户已经购买的数量加用户要购买的优惠券的数量是否已经达到了优惠券限制的个人购买数量
        $orderCouponMdl = new OrderCouponModel();
        $hadCount = $orderCouponMdl->countOrderCoupon(array('batchCouponCode' => $batchCouponCode, 'userCode' => $userCode, 'status' => array('IN', array(\Consts::ORDER_COUPON_STATUS_USE, \Consts::ORDER_COUPON_STATUS_USED))));
        if($batchCouponInfo['nbrPerPerson'] != self::NO_LIMIT && $hadCount + $couponNbr > $batchCouponInfo['nbrPerPerson']) {
            return array('code' => C('COUPON.OVER_NBR_PER_PERSON'));
        }

        return array('code' => C('SUCCESS'));
    }

    /**
     * 获得用户在该商户拥有的某种类型的优惠券
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param string $couponType 优惠券类型。1-N元购；3-抵扣券；4-折扣券；5-实物券；6-体验券; 7-兑换券; 8-代金券
     * @param array $userCouponCodeList 用户已经用户的优惠券编码列表。一维数组
     * @return array $shopCouponList 用户优惠券。二维数组
     */
    public function getShopDiffTypeCoupon($userCode, $shopCode, $couponType, $userCouponCodeList) {
        $sql = '(validityPeriod = 0) OR (BatchCoupon.startUsingTime <= NOW() AND BatchCoupon.expireTime >= NOW() AND BatchCoupon.validityPeriod = -1)';
        $condition = array(
            'shopCode' => $shopCode,
            'couponType' => $couponType,
            'isAvailable' => C('YES'),
            'isSend' => C('NO'),
            '_string' => $sql
        );
        if($userCouponCodeList) {
            $condition['batchCouponCode'] = array('NOTIN', $userCouponCodeList);
        }

        $shopCouponList = $this
            ->field(array('batchCouponCode', 'availablePrice', 'insteadPrice', 'discountPercent', 'payPrice'))
            ->where($condition)
            ->select();
        $target = array('availablePrice', 'insteadPrice', 'payPrice');
        foreach($shopCouponList as &$userCoupon) {
            $userCoupon = $this->dividedByHundred($userCoupon, $target);
            $userCoupon['discountPercent'] = $userCoupon['discountPercent'] / C('DISCOUNT_RATIO');
        }
        return $shopCouponList;
    }

    /**
     * 计算单张优惠券的抵扣金额
     * @param int $orderAmount 计算抵扣金额的基数，单位：分
     * @param int $couponType 优惠券类型
     * @param int $insteadPrice 抵扣金额。单位：分
     * @param int $discountPercent 折扣。百分数
     * @return int $couponDeduction 优惠券抵扣金额，单位：分
     */
    public function calCouponDeduction($orderAmount, $couponType, $insteadPrice, $discountPercent) {
        $couponDeduction = 0;
        if(in_array($couponType, array(\Consts::COUPON_TYPE_REDUCTION, \Consts::COUPON_TYPE_VOUCHER, \Consts::COUPON_TYPE_NEW_CLIENT_REDUCTION, \Consts::COUPON_TYPE_SEND_INVITER))) { // 抵扣券，代金券，送新用户的抵扣券，送邀请人的抵扣券
            $couponDeduction = $insteadPrice;
        } elseif($couponType == C('COUPON_TYPE.DISCOUNT')) { // 折扣券
            $couponDeduction = ($orderAmount - $orderAmount * $discountPercent / \Consts::HUNDRED);
        }
        return $couponDeduction;
    }

    /**
     * 获得送邀请人的优惠券
     *  @return array
     */
    public function getSendInviterCoupon() {
        return $this->field(array('batchCouponCode', 'insteadPrice', 'validityPeriod', 'couponType'))->where(array('couponType' => C('COUPON_TYPE.SEND_INVITER')))->find();
    }

    /**
     * 获得送新注册用户的优惠券
     * @return array
     */
    public function getNewClientCoupon() {
        return $this->field(array('batchCouponCode', 'validityPeriod', 'couponType'))->where(array('couponType' => C('COUPON_TYPE.NEW_CLIENT_REDUCTION')))->find();
    }

    /**
     * 商家送的优惠券。就高原则：满送要求价格较高的先送
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param int $orderAmount 消费金额 单位：分
     * @return array {'code', 'userCouponCode'}
     */
    public function sendCoupon($userCode, $shopCode, $orderAmount) {
        // 获得商家可送给用户的优惠券
        $batchCouponList = $this->getMaxSendCouponInfo($userCode, $shopCode, $orderAmount);

        if($batchCouponList) {
            $batchCouponCode = $batchCouponList[0]['batchCouponCode']; // 取可送优惠券数组中的第一个元素
            // 获得优惠券详情
            $batchCouponInfo = $this->getCouponInfo($batchCouponCode);
            // 计算要送的数量
            $sendCount = number_format(($orderAmount - $orderAmount % ($batchCouponInfo['sendRequired'] * \Consts::HUNDRED)) / ($batchCouponInfo['sendRequired'] * \Consts::HUNDRED));
            $remaining = $batchCouponInfo['remaining']; // 优惠券剩余数量
            if($batchCouponInfo['totalVolume'] == -1) { // 如果优惠券发行总量是无限量
                $remaining = $sendCount;
            }
            // 最终得到要送的券的数量
            $count = UtilsModel::getMinNbr(array($sendCount, $remaining, $batchCouponInfo['limitedSendNbr'], $batchCouponInfo['nbrPerPerson']));

            //满就送，送多少张
            $userCoupon = array();
            for($i = 0; $i < $count; $i++){
                M()->startTrans();
                // 优惠券数量减一
                $decRemainingRet = $this->decRemaining($batchCouponCode, 1);
                // 给用户添加优惠券
                $userCouponMdl = new UserCouponModel();
                $addUserCouponRet = $userCouponMdl->addUserSendCoupon($batchCouponCode, $userCode);
                if($decRemainingRet && $addUserCouponRet['code'] == C('SUCCESS')) {
                    M()->commit();
                    $userCoupon[] = array(
                        'userCouponCode' => $addUserCouponRet['userCouponCode'],
                        'userCouponNbr' => $addUserCouponRet['userCouponNbr'],
                    );
                } else {
                    M()->rollback();
                }
            }

            if(count($userCoupon) > 0){
                $name = '优惠券';
                if($batchCouponInfo['couponType'] == \Consts::COUPON_TYPE_N_PURCHASE){
                    $name = C('COUPON_TYPE_NAME.N_PURCHASE');
                }elseif($batchCouponInfo['couponType'] == \Consts::COUPON_TYPE_REDUCTION){
                    $name = C('COUPON_TYPE_NAME.REDUCTION');
                }elseif($batchCouponInfo['couponType'] == \Consts::COUPON_TYPE_DISCOUNT){
                    $name = C('COUPON_TYPE_NAME.DISCOUNT');
                }elseif($batchCouponInfo['couponType'] == \Consts::COUPON_TYPE_PHYSICAL){
                    $name = C('COUPON_TYPE_NAME.PHYSICAL');
                }elseif($batchCouponInfo['couponType'] == \Consts::COUPON_TYPE_EXPERIENCE){
                    $name = C('COUPON_TYPE_NAME.EXPERIENCE');
                }elseif($batchCouponInfo['couponType'] == \Consts::COUPON_TYPE_EXCHANGE){
                    $name = C('COUPON_TYPE_NAME.EXCHANGE');
                }elseif($batchCouponInfo['couponType'] == \Consts::COUPON_TYPE_VOUCHER){
                    $name = C('COUPON_TYPE_NAME.VOUCHER');
                }
                $userCouponCodeArr = array();
                // 给用户发短信，告知得到商家赠送的优惠券
                $userMdl = new UserModel();
                $shopMdl = new ShopModel();
                // 获得商家信息
                $shop = $shopMdl->getShopInfo($batchCouponInfo['shopCode'], array('Shop.shopName'));
                $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('mobileNbr'));
                if(count($userCoupon) > 1){
                    foreach($userCoupon as $v){
                        $userCouponCodeArr[] = $v['userCouponCode'];
                    }
                    // 设置送多张优惠券的短信消息
                    $msg = str_replace(array('{{shopName}}', '{{batchNbr}}', '{{couponType}}', '{{function}}', '{{sendCount}}'), array($shop['shopName'], $batchCouponInfo['batchNbr'], $name, $batchCouponInfo['function'], count($userCoupon)), C('SEND_MESSAGE.SEND_COUPON_MORE_ONE'));
                }else{
                    $userCouponCodeArr[] = $userCoupon[0]['userCouponCode'];
                    // 设置送一张优惠券的短信消息
                    $msg = str_replace(array('{{shopName}}', '{{couponType}}', '{{function}}', '{{userCouponNbr}}'), array($shop['shopName'], $name, $batchCouponInfo['function'], $userCoupon[0]['userCouponNbr']), C('SEND_MESSAGE.SEND_COUPON'));
                }
                $smsMdl = new SmsModel();
                // 发送短信
                $smsMdl->send($msg, $userInfo['mobileNbr']);

                return array('code' => C('SUCCESS'), 'userCouponCode' => implode('|', $userCouponCodeArr));
            } else {
                return array('code' => C('API_INTERNAL_EXCEPTION'), 'userCouponCode' => '');
            }
        } else {
            //返回没有可送的优惠券
            return array('code' => C('COUPON.NO_SEND_COUPON'), 'userCouponCode' => '');
        }
    }

    /**
     * 查看商家是否具有该类的优惠券
     * @param string $shopCode 商家编码
     * @param string $couponType 优惠券类型。
     * @return boolean 有返回true，没有返回false
     */
    public function isShopHasTheCoupon($shopCode, $couponType) {
        $sql = '(totalVolume = -1) OR (totalVolume > 0 AND remaining > 0)';
        $couponInfo = $this
            ->field(array('batchCouponCode'))
            ->where(array(
                'shopCode' => $shopCode,
                'couponType' => $couponType,
                'isAvailable' => C('YES'),
                'endTakingTime' => array('EGT', date('Y-m-d H:i:s', time())),
                'expireTime' => array('EGT', date('Y-m-d H:i:s', time())),
                '_string' => $sql
            ))
            ->find();
        return $couponInfo ? true : false;
    }

    /**
     * 获得优惠券基本信息
     * @param array $condition 条件
     * @param array $field 字段
     * @return array
     */
    public function getCouponBasicInfo($condition, $field) {
        return $this->field($field)->where($condition)->find();
    }

    /**
     * 启用/停用优惠券
     * @param string $batchCouponCode 优惠券编码
     * @param int $isAvailable 优惠券状态 1-启用；0-停用
     * @return array $ret
     */
    public function changeCouponStatus($batchCouponCode, $isAvailable) {
        return $this
            ->where(array('batchCouponCode' => $batchCouponCode))
            ->save(array('isAvailable' => $isAvailable)) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
    }

    /**
     * 获得商家一批优惠券详情
     * @param string $batchCouponCode 商店编码
     * @return array 没有数据时返回空数组array()
     */
    public function sGetCouponInfo($batchCouponCode) {
        $couponInfo = $this->field(array(
            'batchCouponCode',
            'batchNbr',
            'totalVolume',
            'remaining',
            'insteadPrice',
            'discountPercent',
            'couponType',
            'function',
            'payPrice',
            'startTakingTime',
            'endTakingTime',
            'startUsingTime',
            'expireTime',
            'availablePrice',
            'isSend',
            'sendRequired',
            'isAvailable',
            'dayStartUsingTime',
            'dayEndUsingTime',
            'validityPeriod',
            'remark',
            'limitedSendNbr'
        ))
            ->where(array('batchCouponCode' => $batchCouponCode))
            ->find();
        if($couponInfo) {
            $couponInfo['isExpire'] = C('YES');
            if(substr($couponInfo['expireTime'], 0, 10) != '0000-00-00' && strtotime($couponInfo['expireTime']) < time()) {
                $couponInfo['isExpire'] = C('NO');
            }

            $target = array('insteadPrice', 'payPrice', 'availablePrice', 'sendRequired');
            $couponInfo = $this->dividedByHundred($couponInfo, $target);
            $couponInfo['discountPercent']  = $couponInfo['discountPercent'] / $this->discountRatio;

            if($couponInfo['totalVolume'] == -1){
                $couponInfo['takenCount'] = abs($couponInfo['remaining']);
            }else{
                $couponInfo['takenCount'] = $couponInfo['totalVolume'] - $couponInfo['remaining'];
                $couponInfo['takenPercent'] = round($couponInfo['takenCount'] / $couponInfo['totalVolume'] * C('RATIO'), 2);
            }

            $userCouponMdl = new UserCouponModel();
            $couponInfo['usedCount'] = $userCouponMdl->countUsedCoupon($couponInfo['batchCouponCode']);
            $couponInfo['usedPercent'] = round($couponInfo['usedCount'] / $couponInfo['takenCount'] * C('RATIO'), 2);

            $tempArray = array('startTakingTime', 'endTakingTime', 'startUsingTime', 'expireTime');
            foreach($tempArray as $v) {
//                $couponInfo[$v] = substr($couponInfo[$v], 0, 10);
                $couponInfo[$v] = $this->formatDate1($couponInfo[$v]);
            }
            $couponInfo['dayStartUsingTime'] = substr($couponInfo['dayStartUsingTime'], 0, 5);
            $couponInfo['dayEndUsingTime'] = substr($couponInfo['dayEndUsingTime'], 0, 5);
            unset($couponInfo['remaining']);
            return $couponInfo;
        }
        return array();
    }

    /**
     * 获得商家最近一批优惠券详情
     * @param string $shopCode 商店编码
     * @return array 没有数据时返回空数组array()
     */
    public function getRecentCouponInfo($shopCode) {
        $condition = array();
        if($shopCode) {
            $condition['BatchCoupon.shopCode'] = $shopCode;
        }
        $couponInfo = $this->field(array(
            'batchCouponCode',
            'batchNbr',
            'totalVolume',
            'remaining',
            'insteadPrice',
            'discountPercent',
            'function',
            'payPrice',
            'couponType',
            'function',
            'createTime',
            'availablePrice',
            'expireTime',
            'isSend',
            'isAvailable',
            'validityPeriod',
            'shopName',
            'city'
        ))
            ->join('Shop on Shop.shopCode = BatchCoupon.shopCode')
            ->order('createTime desc')
            ->where($condition)
            ->find();
        if($couponInfo) {
            $couponInfo['city'] = $this->formatCity($couponInfo['city']);
            $tempArray = array('insteadPrice', 'availablePrice', 'payPrice');
            $couponInfo = $this->dividedByHundred($couponInfo, $tempArray);
            $couponInfo['discountPercent'] = $couponInfo['discountPercent'] / $this->discountRatio;

            if($couponInfo['totalVolume'] == -1){
                $couponInfo['takenCount'] = abs($couponInfo['remaining']);
            }else{
                $couponInfo['takenCount'] = $couponInfo['totalVolume'] - $couponInfo['remaining'];
                $couponInfo['takenPercent'] = round($couponInfo['takenCount'] / $couponInfo['totalVolume'] * C('RATIO'), 2);
            }

            $couponInfo['isExpire'] = C('YES');
            if(substr($couponInfo['expireTime'], 0, 10) != '0000-00-00' && strtotime($couponInfo['expireTime']) < time()) {
                $couponInfo['isExpire'] = C('NO');
            }
            $unsetData = array('remaining', 'createTime', 'expireTime');
            foreach($unsetData as $v) {
                unset($couponInfo[$v]);
            }
        }
        return $couponInfo ? $couponInfo : array();
    }

    /**
     * 增加剩余数量
     * @param string $batchCouponCode 优惠券编码
     * @param int $nbr 数量
     * @return boolean $ret 成功返回true，失败返回false
     */
    public function addRemaining($batchCouponCode, $nbr) {
        return $this->where(array('batchCouponCode' => $batchCouponCode))->setInc('remaining', $nbr) !== false ? true : false;
    }

    /**
     * 减少剩余数量
     * @param string $batchCouponCode 优惠券编码
     * @param int $nbr 数量
     * @return boolean $ret 成功返回true，失败返回false
     */
    public function decRemaining($batchCouponCode, $nbr) {
        return $this->where(array('batchCouponCode' => $batchCouponCode))->setDec('remaining', $nbr) !== false ? true : false;
    }

    /**
     * 设置筛选条件
     * @param string $shopCode 商家编码
     * @param string $time 时间范围。1-最近一个月；2-最近一个季度；3-最近一年
     * @return array $condition
     */
    public function setLSCCondition($shopCode, $time) {
        $condition = array();
        if($shopCode) {
            $condition['BatchCoupon.shopCode'] = $shopCode;
        }
//        if($time) {
//            if($time == 1) {$time = strtotime('-1 month');}
//            if($time == 2) {$time = strtotime('-3 month');}
//            if($time == 3) {$time = strtotime('-1 year');}
//            $condition['unix_timestamp(BatchCoupon.createTime)'] = array('EGT', $time);
//        }
        return $condition;
    }

    /**
     * 获得商家发布的优惠券列表
     * @param string $shopCode 商店编码
     * @param string $time 时间范围。1-最近一个月；2-最近一个季度；3-最近一年
     * @param object $page 页码
     * @return array 没有数据时返回空数组array()
     */
    public function listShopCoupon($shopCode, $time, $page) {
        $condition = $this->setLSCCondition($shopCode, $time);
        $couponList = $this->field(array(
            'batchCouponCode',
            'batchNbr',
            'totalVolume',
            'remaining',
            'insteadPrice',
            'couponType',
            'function',
            'payPrice',
            'availablePrice',
            'discountPercent',
            'expireTime',
            'isSend',
            'isAvailable',
            'shopName',
            'city',
            'validityPeriod',
        ))
            ->join('Shop on Shop.shopCode = BatchCoupon.shopCode')
            ->where($condition)
            ->order('expireTime desc, BatchCoupon.createTime desc')
            ->pager($page)
            ->select();
        $tempArr = array('insteadPrice', 'payPrice', 'availablePrice');
        foreach($couponList as &$v) {
            $v = $this->dividedByHundred($v, $tempArr);
            $v['discountPercent'] = $v['discountPercent'] / $this->discountRatio;

            if($v['totalVolume'] == -1){
                $v['takenCount'] = abs($v['remaining']);
            }else{
                $v['takenCount'] = $v['totalVolume'] - $v['remaining'];
                $v['takenPercent'] = round($v['takenCount'] / $v['totalVolume'] * C('RATIO'), 2);
            }

            $v['isExpire'] = C('YES');
            if(substr($v['expireTime'], 0, 10) != '0000-00-00' && strtotime($v['expireTime']) < time()) {
                $v['isExpire'] = C('NO');
            }

            $v['city'] = $this->formatCity($v['city']);

            unset($v['remaining']);
            unset($v['expireTime']);
        }
        return $couponList;
    }

    /**
     * 统计商家发布的优惠券总数
     * @param string $shopCode 商店编码
     * @param string $time 时间范围。1-最近一个月；2-最近一个季度；3-最近一年
     * @return array 没有数据时返回空数组array()
     */
    public function countShopCoupon($shopCode, $time) {
        $condition = $this->setLSCCondition($shopCode, $time);
        return $this
            ->join('Shop on Shop.shopCode = BatchCoupon.shopCode')
            ->join('BackgroundTemplate on BackgroundTemplate.bgCode = BatchCoupon.bgImgCode', 'LEFT')
            ->where($condition)
            ->count();
    }

    /**
     * 删除商家优惠券
     * @param array $condition 条件
     * @return boolean 成功返回true，失败返回false
     */
    public function delBatchCoupon($condition) {
        return $this->where($condition)->delete() !== false ? true : false;
    }

    /**
     * 优惠券对账
     * @param string $shopCode 商家编码
     * @param string $startDate 开始时间
     * @param string $endDate 结束时间
     * @return array
     */
    public function getCouponBill($shopCode, $startDate, $endDate) {
        $couponBillList = array();
        $userCouponMdl = new UserCouponModel();
        foreach(C('SHOP_NORMAL_COUPON') as $v) {
            if($v == C('COUPON_TYPE.TODAY_BIGGEST')) break;
            $data['couponType'] = $v;
            $couponCodeList = $this
                ->where(array('couponType' => $v, 'shopCode' => $shopCode))
                ->getField('batchCouponCode', true);
            $data['usedCount'] = $couponCodeList ? $userCouponMdl->countReceivedCoupon($couponCodeList, $startDate, $endDate) : 0;
            $data['usedAmount'] = $couponCodeList ? $userCouponMdl->countCouponDeductionValue($couponCodeList, $startDate, $endDate) : 0;
            $data['hqAmount'] = 0;
            $couponBillList[] = $data;
        }
        return $couponBillList;
    }

    /**
     * 分析某个商户的优惠券
     * @param array $condition 条件
     * @return array
     */
    public function analysisShopCoupon($condition) {
        $totalVolume = $this->where($condition)->sum('totalVolume');
        $couponCodeList = $this->where($condition)->getField('batchCouponCode', true);
        $userCouponMdl = new UserCouponModel();
        $takeAmount = $couponCodeList ? $userCouponMdl->countReceiveCoupon($couponCodeList) : 0;
        $usedAmount = $couponCodeList ? $userCouponMdl->countReceivedCoupon($couponCodeList, $startDate = '', $endDate = '') : 0;
        $consumeAmount = $couponCodeList ? $userCouponMdl->countConsumeAmount($couponCodeList) : 0;
        $couponDeductionValue =  $couponCodeList ? $userCouponMdl->countCouponDeductionValue($couponCodeList, $startDate = '', $endDate = '') : 0;
        $usedPercent = $usedAmount / $takeAmount;
        $platCouponCodeList = $this->where(array('shopCode' => C('HQ_CODE')))->getField('batchCouponCode', true);
        if($platCouponCodeList) {
            $where['UserCoupon.batchCouponCode'] = array('IN', $platCouponCodeList);
        }
        if($condition['createTime']) {
            $where['consumeTime'] = $condition['createTime'];
        }
        $where['location'] = $condition['shopCode'];
        $where['UserConsume.status'] = C('UC_STATUS.PAYED');
        $userConsumeMdl = new UserConsumeModel();
        $platCouponUsedAmount = $platCouponCodeList ? $userConsumeMdl->countPlatCouponUsedAmount($where) : 0;
        $platCouponDeductionValue = $platCouponCodeList ? $userConsumeMdl->countPlatCouponDeductionValue($where) : 0;
        $platCouponConsumeAmount = $platCouponCodeList ? $userConsumeMdl->countPlatCouponConsumeAmount($where) : 0;
        return array(
            'totalVolume' => $totalVolume ? $totalVolume : 0, // 商户优惠券发放数量
            'takeAmount' => $takeAmount, // 商户优惠券领用数量
            'usedAmount' => $usedAmount, // 商户优惠券使用数量
            'consumeAmount' => $consumeAmount, // 带来的消费总额
            'couponDeductionValue' => $couponDeductionValue, // 优惠券抵扣金额
            'usedPercent' => round($usedPercent * C('RATIO'), 2), // 使用率
            'platCouponUsedAmount' => $platCouponUsedAmount, // 在商家使用平台优惠券的数量
            'platCouponDeductionValue' => $platCouponDeductionValue, // 在商家使用平台优惠券的抵扣总金额
            'platCouponConsumeAmount' => $platCouponConsumeAmount, // 在商家使用平台优惠券的消费总额
            // TODO
        );
    }

    /**
     * 过滤条件
     * @param array $filterWhere
     */
    private function secondFilterGroupByShop(&$filterWhere) {
        if($filterWhere['city']) {
            $filterWhere['Shop.city'] = $filterWhere['city']; // 发券商户所在城市
            unset($filterWhere['city']);
        }
        if($filterWhere['type']) {
            $filterWhere['Shop.type'] = $filterWhere['type']; // 商户类型
            unset($filterWhere['type']);
        }
        $filterWhere['couponBelonging'] = array('EQ', \Consts::COUPON_BELONG_SHOP); // 优惠券归属商户
        if($filterWhere['UserConsume.consumeTime']) {
            // 获得符合条件的优惠券编码一维索引数组
            $batchCouponList = $this
                ->field(array('DISTINCT(BatchCoupon.batchCouponCode)'))
                ->join('Shop ON Shop.shopCode = BatchCoupon.shopCode')
                ->join('UserCoupon ON UserCoupon.batchCouponCode = BatchCoupon.batchCouponCode', 'left')
                ->join('UserConsume ON UserConsume.consumeCode = UserCoupon.consumeCode', 'left')
                ->where($filterWhere)
                ->select();
            $batchCouponCodeList = array();
            foreach($batchCouponList as $v) {
                $batchCouponCodeList[] = $v['batchCouponCode'];
            }
            if($batchCouponCodeList) {
                $filterWhere['BatchCoupon.batchCouponCode'] = array('IN', $batchCouponCodeList);
            }
        }
    }

    /**
     * 按商户分组获得优惠券信息
     * @param array $filterWhere 筛选条件 一维关联数组
     * @param object $page 页码对象
     * @return array 二维数组
     */
    public function listBatchCouponGroupByShop($filterWhere, $page) {
        $condition = $this->filterWhere($filterWhere, array('city' => 'like', 'shopName' => 'like'));
        $this->secondFilterGroupByShop($condition);

        $consumeTime = $condition['UserConsume.consumeTime'];
        unset($condition['UserConsume.consumeTime']);


        //        判断是否为惠圈管理人员
        if($_SESSION['USER']['bank_id']!=-1){
            $condition['Shop.bank_id'] = $_SESSION['USER']['bank_id'];
        }

        // 获得符合条件的所有优惠券
        $batchCouponList = $this
            ->field(array('shopName', 'batchNbr', 'SUM(totalVolume)' => 'totalVolume', 'Shop.city', 'Shop.shopCode'))
            ->join('Shop ON Shop.shopCode = BatchCoupon.shopCode')
            ->where($condition)
            ->group('Shop.shopCode')
            ->order('BatchCoupon.createTime desc,shopName, batchNbr desc')
            ->pager($page)
            ->select();
        foreach($batchCouponList as $k => $v) {
            $condition['BatchCoupon.shopCode'] = $v['shopCode'];
            if($condition['couponType'] == \Consts::COUPON_TYPE_REDUCTION) {
                $couponList = $this->field(array('insteadPrice', 'batchCouponCode', 'totalVolume'))->join('Shop ON Shop.shopCode = BatchCoupon.shopCode')->where($condition)->select();
                $totalUsedCount = 0;
                $totalCouponDeduction = 0;
                $totalOriginSubsidy = 0;
                foreach($couponList as $coupon) {
                    $con = $condition;
                    $con['BatchCoupon.batchCouponCode'] = $coupon['batchCouponCode'];
                    $con['UserCoupon.status'] = \Consts::USER_COUPON_STATUS_USED; // 用户优惠券状态为已使用
                    $con['UserConsume.status'] = \Consts::PAY_STATUS_PAYED; // 支付状态为已支付
                    if($consumeTime) {
                        $con['UserConsume.consumeTime'] = $consumeTime;
                    }
                    $usedCount = $this->countTakeAndUsed($con); // 获得已使用数量
                    $totalUsedCount += $usedCount;
                    $couponDeduction = $usedCount * $coupon['insteadPrice']; // 抵扣金额
                    $totalCouponDeduction += $couponDeduction;
                    $originSubsidy = $coupon['totalVolume'] * $coupon['insteadPrice'];// 应补贴金额
                    $totalOriginSubsidy += $originSubsidy;
                }
                $batchCouponList[$k]['usedCount'] = $totalUsedCount; // 获得已使用数量
                $batchCouponList[$k]['totalSubsidy'] = $totalCouponDeduction / \Consts::HUNDRED; // 获得已使用的优惠券的抵扣金额
                $batchCouponList[$k]['totalOriginSubsidy'] = $totalOriginSubsidy / \Consts::HUNDRED; // 应补贴金额
            } else {
                $con = $condition;
                $con['UserCoupon.status'] = \Consts::USER_COUPON_STATUS_USED; // 用户优惠券状态为已使用
                $con['UserConsume.status'] = \Consts::PAY_STATUS_PAYED; // 支付状态为已支付
                if($consumeTime) {
                    $con['UserConsume.consumeTime'] = $consumeTime;
                }
                $batchCouponList[$k]['usedCount'] = $this->countTakeAndUsed($con); // 获得已使用数量
            }
        }
        return $batchCouponList;
    }

    /**
     * 获得总数
     * @param array $filterWhere 筛选条件 一维关联数组
     * @return int 数量
     */
    public function countBatchCouponGroupByShop($filterWhere) {
        $condition = $this->filterWhere($filterWhere, array('shopName' => 'like'));
        $this->secondFilterGroupByShop($condition);

        unset($condition['UserConsume.consumeTime']);

        //        判断是否为惠圈管理人员
        if($_SESSION['USER']['bank_id']!=-1){
            $condition['Shop.bank_id'] = $_SESSION['USER']['bank_id'];
        }

        // 获得符合条件的所有优惠券
        $batchCouponCount = $this
            ->join('Shop ON Shop.shopCode = BatchCoupon.shopCode')
            ->where($condition)
            ->count('DISTINCT(Shop.shopCode)');
        return $batchCouponCount;
    }

    /**
     * 分析优惠券
     * @param array $condition 条件，一维关联数组
     * @return array 二维数组
     */
    public function analysisCoupon($condition) {

        //        判断是否为惠圈管理人员
        if($_SESSION['USER']['bank_id']!=-1){
            $condition['Shop.bank_id'] = $_SESSION['USER']['bank_id'];
        }

        // 过滤条件
        $condition = $this->filterWhere($condition, array('city' => 'like', 'shopName' => 'like'));
        $this->secondFilterGroupByShop($condition);

        $consumeTime = $condition['UserConsume.consumeTime'];
        unset($condition['UserConsume.consumeTime']);

        $data['shopCount'] = $this->countShop($condition); // 商店数量
        $data['totalVolume'] = $this->countTotalVolume($condition); // 获得发行总量
        $data['takeCount'] = $this->countTakeAndUsed($condition); // 获得领用总量
        $condition['UserCoupon.status'] = \Consts::USER_COUPON_STATUS_USED; // 用户优惠券状态为已使用
        $condition['UserConsume.status'] = \Consts::PAY_STATUS_PAYED; // 支付状态为已支付
        if($consumeTime) {
            $condition['UserConsume.consumeTime'] = $consumeTime;
        }
        $data['usedCount'] = $this->countTakeAndUsed($condition); // 获得已使用数量

        $usedCountGroupBySex = $this->countUsedNbrGroupBySex($condition);
        foreach($usedCountGroupBySex as $v) {
            $usedCount = empty($v['nbr']) ? 0 : $v['nbr'];
            switch($v['sex']) {
                case \Consts::USER_SEX_M: // 男性
                    $data['mUsedCount'] = $usedCount; // 男性使用量
                    break;
                case \Consts::USER_SEX_F: // 女性
                    $data['fUsedCount'] = $usedCount; // 女性使用量
                    break;
                case \Consts::USER_SEX_U: // 未知性别
                    $data['uUsedCount'] = $usedCount; // 未知性别使用量
                    break;
            }
        }
        return $data;
    }

    /**
     * 统计商家数量
     * @param array $where 条件
     * @return number
     */
    public function countShop($where) {
        return $this
            ->where($where)
            ->join('Shop ON Shop.shopCode = BatchCoupon.shopCode')
            ->count('distinct(Shop.shopCode)');
    }

    /**
     * 统计发行总量
     * @param array $where 条件
     * @return int $totalVolume
     */
    public function countTotalVolume($where) {
        $totalVolume = $this
            ->where($where)
            ->join('Shop ON Shop.shopCode = BatchCoupon.shopCode')
            ->sum('totalVolume');
        return $totalVolume ? $totalVolume : 0;
    }

    /**
     * 统计领取总量和用总量
     * @param array $where 条件
     * @return number
     */
    public function countTakeAndUsed($where) {
        return $this
            ->where($where)
            ->join('Shop ON Shop.shopCode = BatchCoupon.shopCode')
            ->join('UserCoupon ON UserCoupon.batchCouponCode = BatchCoupon.batchCouponCode')
            ->join('UserConsume ON UserConsume.consumeCode = UserCoupon.consumeCode', 'left')
            ->count('UserCoupon.userCouponCode');
    }

    /**
     * 根据用户性别分组统计优惠券的使用量
     * @param array $where 条件
     * @return number
     */
    public function countUsedNbrGroupBySex($where) {
        return $this
            ->field(array('COUNT(UserCoupon.userCouponCode)' => 'nbr', 'User.sex'))
            ->where($where)
            ->join('Shop ON Shop.shopCode = BatchCoupon.shopCode')
            ->join('UserCoupon ON UserCoupon.batchCouponCode = BatchCoupon.batchCouponCode')
            ->join('UserConsume ON UserConsume.consumeCode = UserCoupon.consumeCode', 'left')
            ->join('User ON User.userCode = UserCoupon.userCode')
            ->group('User.sex')
            ->select();
    }

    function  volume(){
       $volune =  intval($_REQUEST['totalVolume']);
        if($volune==-1){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 编辑优惠券
     * @param array $couponInfo 优惠券信息
     * @return string
     */
    public function editBatchCoupon($couponInfo) {
        $rules = array(
            array('couponName', 'require', C('COUPON.NAME_ERROR')),
            array('couponType', 'require', C('COUPON.TYPE_ERROR')),
            array('couponType', 'number', C('COUPON.TYPE_ERROR')),
            array('startUsingTime', 'require', C('COUPON.START_USING_TIME_ERROR')),
            array('creatorCode', 'require', C('COUPON.CREATOR_CODE_ERROR')),
            array('shopCode', 'require', C('SHOP.SHOP_CODE_ERROR')),
            array('totalVolume', 'require', C('COUPON.TOTAL_VOLUME_ERROR')),
            array('totalVolume', 'is_numeric', C('COUPON.TOTAL_VOLUME_ERROR'), 1, 'function'),
            array('totalVolume','volume','必须小于微信卡包的发行总量',1,'callback',3),
            array('expireTime', 'require', C('COUPON.EXPIRE_TIME_ERROR')),
            array('startTakingTime', 'require', C('COUPON.START_TAKING_DATE_ERROR')),
            array('endTakingTime', 'require', C('COUPON.END_TAKING_DATE_ERROR')),
            array('industryCategory', 'require', C('COUPON.INDUSTRY_CATEGORY_ERROR')),
            array('industryCategory', 'number', C('COUPON.INDUSTRY_CATEGORY_ERROR')),
            array('availablePrice', 'require', C('COUPON.AVAILABLE_PRICE_ERROR')),
            array('limitedNbr', 'require', C('COUPON.LIMITED_NBR_ERROR')),
            array('limitedNbr', 'number', C('COUPON.LIMITED_NBR_ERROR')),
            array('nbrPerPerson', 'require', C('COUPON.NBR_PER_PERSON_ERROR')),
            array('nbrPerPerson', 'number', C('COUPON.NBR_PER_PERSON_ERROR')),
            array('couponBelonging', 'require', C('COUPON.BELONGING_EMPTY'))
        );
        if(in_array($couponInfo['couponType'],array(\Consts::COUPON_TYPE_N_PURCHASE, \Consts::COUPON_TYPE_REDUCTION, \Consts::COUPON_TYPE_DISCOUNT, \Consts::COUPON_TYPE_PHYSICAL, \Consts::COUPON_TYPE_EXPERIENCE, \Consts::COUPON_TYPE_EXCHANGE, \Consts::COUPON_TYPE_VOUCHER,)
        )) {
            if($couponInfo['totalVolume'] > 0){
                $rules[] = array('nbrPerPerson', array(0, $couponInfo['totalVolume']), C('COUPON.NBR_PER_PERSON_GT_TOTAL_VOLUME'), 0, 'between');
            }
            $couponInfo['validityPeriod'] = -1;// 表示不设置有效天数，设置有效日期
            if($couponInfo['expireTime'] == '0000-00-00') {
                $couponInfo['validityPeriod'] = 0;// 表示设置永久有效
            }
        }

        switch($couponInfo['couponType']) {
            case \Consts::COUPON_TYPE_N_PURCHASE:
                $rules[] = array('insteadPrice', 'require', C('COUPON.INSTEAD_PRICE_ERROR'));
                $rules[] = array('insteadPrice', 'is_numeric', C('COUPON.INSTEAD_PRICE_ERROR'), '0', 'function');
                $rules[] = array('insteadPrice', '0', C('COUPON.INSTEAD_PRICE_ERROR'), '0', 'notequal');
                break;
            case \Consts::COUPON_TYPE_REDUCTION:
                $rules[] = array('insteadPrice', 'require', C('COUPON.INSTEAD_PRICE_ERROR'));
                $rules[] = array('insteadPrice', 'is_numeric', C('COUPON.INSTEAD_PRICE_ERROR'), '0', 'function');
                $rules[] = array('insteadPrice', 0, C('COUPON.INSTEAD_PRICE_ERROR'), '0', 'notequal');
                break;
            case \Consts::COUPON_TYPE_DISCOUNT:
                $rules[] = array('discountPercent', 'require', C('COUPON.DISCOUNT_PERCENT_ERROR'));
                $rules[] = array('discountPercent', 'is_numeric', C('COUPON.DISCOUNT_PERCENT_ERROR'), '0', 'function');
                $rules[] = array('discountPercent', '0', C('COUPON.DISCOUNT_PERCENT_ERROR'), '0', 'notequal');
                break;
            case \Consts::COUPON_TYPE_PHYSICAL:
                $rules[] = array('function', 'require', C('COUPON.FUNCTION_ERROR'));
                break;
            case \Consts::COUPON_TYPE_EXPERIENCE:
                $rules[] = array('function', 'require', C('COUPON.FUNCTION_ERROR'));
                break;
            case \Consts::COUPON_TYPE_EXCHANGE:
                $rules[] = array('function', 'require', C('COUPON.FUNCTION_ERROR'));
                $rules[] = array('payPrice', 'require', C('COUPON.PAY_PRICE_ERROR'));
                $rules[] = array('payPrice', 'is_numeric', C('COUPON.PAY_PRICE_ERROR'), '0', 'function');
                break;
            case \Consts::COUPON_TYPE_VOUCHER:
                $rules[] = array('function', 'require', C('COUPON.FUNCTION_ERROR'));
                $rules[] = array('insteadPrice', 'require', C('COUPON.INSTEAD_PRICE_ERROR'));
                $rules[] = array('insteadPrice', 'is_numeric', C('COUPON.INSTEAD_PRICE_ERROR'), '0', 'function');
                $rules[] = array('payPrice', 'require', C('COUPON.PAY_PRICE_ERROR'));
                $rules[] = array('payPrice', 'is_numeric', C('COUPON.PAY_PRICE_ERROR'), '0', 'function');
                break;
        }


        if($this->validate($rules)->create($couponInfo) != false) {


            //非满就送，重置每单限送
            if($couponInfo['isSend'] != 1){
                $couponInfo['limitedSendNbr'] = 0;
            }

            // 满就减抵扣金额不能大于可使用金额
            if($couponInfo['couponType'] == C('COUPON_TYPE.REDUCTION')) {

                if($couponInfo['insteadPrice'] > $couponInfo['availablePrice']) {

                    return $this->getBusinessCode(C('COUPON.INSTEAD_PRICE_ERROR'));

                }
            }

            // 添加优惠券背景图片
            $backgroundTemplateMdl = new BackgroundTemplateModel();
            if($couponInfo['url']) {
                F("url",$couponInfo['url']);
                $creatorCode = $couponInfo['creatorCode'] ? $couponInfo['creatorCode'] : $couponInfo['shopCode'];
                $bgCode = $backgroundTemplateMdl->addBackgroundTemplate($couponInfo['url'], $creatorCode, C('BACKGROUND_TYPE.COUPON'));
                $couponInfo['bgImgCode'] = $bgCode == C('API_INTERNAL_EXCEPTION') ? '' : $bgCode;
            }
            unset($couponInfo['url']);
            // 各种价格单位：元化单位：分
            $couponInfo = $this->byHundred($couponInfo, array('insteadPrice', 'availablePrice', 'canTakePrice', 'sendRequired', 'payPrice'));

            $linkedShop = $couponInfo['linkedShop'];
            unset($couponInfo['linkedShop']);

            if(empty($couponInfo['dayEndUsingTime'])) {
                $couponInfo['dayEndUsingTime'] = '23:59:59';
            } else {
                $couponInfo['dayEndUsingTime'] = substr($couponInfo['dayEndUsingTime'], 0, 5) . ':59';
            }


            if(isset($couponInfo['batchCouponCode']) && $couponInfo['batchCouponCode']) {
                // 统计优惠券领取数量
                $userCouponMdl = new UserCouponModel();
                $takenCount = $userCouponMdl->countCouponTakenNbr($couponInfo['batchCouponCode']);
                if($couponInfo['totalVolume'] == -1){
                    $couponInfo['remaining'] = -$takenCount;
                }else{
                    $couponInfo['remaining'] = $couponInfo['totalVolume'] - $takenCount;
                }
                $code = $this
                    ->where(array('batchCouponCode' => $couponInfo['batchCouponCode']))
                    ->save($couponInfo) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            } else {
                $batchCount = 1;
                if(isset($couponInfo['batchCount'])){
                    $batchCount = $couponInfo['batchCount'];
                    unset($couponInfo['batchCount']);
                }

                $startTakingTime = strtotime($couponInfo['startTakingTime']);
                $endTakingTime = strtotime($couponInfo['endTakingTime']);
                $startUsingTime = strtotime($couponInfo['startUsingTime']);
                $expireTime = strtotime($couponInfo['expireTime']);
                M()->startTrans();
                for($i = 0; $i < $batchCount; $i++){
                    $couponInfo['startTakingTime'] = date('Y-m-d H:i:s', $startTakingTime + 86400 * $i);
                    $couponInfo['endTakingTime'] = date('Y-m-d H:i:s', $endTakingTime + 86400 * $i);
                    $couponInfo['startUsingTime'] = date('Y-m-d H:i:s', $startUsingTime + 86400 * $i);
                    $couponInfo['expireTime'] = date('Y-m-d H:i:s', $expireTime + 86400 * $i);

                    $couponInfo['batchCouponCode'] = $this->create_uuid();
                    $couponInfo['createTime'] = date('Y-m-d H:i:s', time());
                    $couponInfo['batchNbr'] = $this->setBatchNbr($couponInfo['shopCode'], $couponInfo['couponType']);
                    if($couponInfo['totalVolume'] == -1){
                        $couponInfo['remaining'] = 0;
                    }else{
                        $couponInfo['remaining'] = $couponInfo['totalVolume'];
                    }
                    $ret = $this->add($couponInfo);
                    if($ret === false){
                        M()->rollback();
                        $code = C('API_INTERNAL_EXCEPTION');
                        break;
                    }
                }
                M()->commit();
                $code = C('SUCCESS');
            }
            if($code == C('API_INTERNAL_EXCEPTION')) {
                return $this->getBusinessCode($code);
            }
            // 发商家广播
            $msgMdl = new MessageModel();
            $msgInfo = C('SHOP_BROADCASTING.ISSUE_COUPON');
            $msgMdl->shopBroadcasting($couponInfo['shopCode'], $msgInfo['TITLE'], $msgInfo['CONTENT']);

            if($couponInfo['shopCode'] == C('HQ_CODE') && $linkedShop) {
                $pCouponMdl = new PCouponShopModel();
                $code = $pCouponMdl->addLinkedShop($couponInfo['batchCouponCode'], $linkedShop) ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            return array('code' => $code, 'batchCouponCode' => $couponInfo['batchCouponCode'], 'batchNbr' => $couponInfo['batchNbr'] ? $couponInfo['batchNbr'] : '');
//            return $this->getBusinessCode($code);
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 产生优惠券批次号，一位券类别+两位年份+两位流水
     * @param string $shopCode 商家编码
     * @param string $couponType 优惠券类型。1-N元购，3-抵扣券，4-折扣券，5-实物券，6-体验券
     * @return string $batchNbr
     */
    private function setBatchNbr($shopCode, $couponType) {
        $thisYearStart = date('Y-01-01 00:00:00', time());
        $thisYearEnd = date('Y-12-31 23:59:59', time());
        $maxBatchNbr = $this
            ->where(array('shopCode' => $shopCode, 'createTime' => array('BETWEEN', array($thisYearStart, $thisYearEnd)), 'couponType' => $couponType))
            ->max('batchNbr');
        $serialNbr = sprintf('%02d', intval(substr($maxBatchNbr, 3)) + 1);
        $batchNbr =  $couponType . date('y', time()) . $serialNbr;
        return $batchNbr;
    }

    /**
     * 获取商家发行的可领的优惠券列表
     * @param string $shopCode 商店编码
     * @param int $couponType 优惠券类型. 1-N元购，3-抵扣券，4-折扣券，5-实物券，6-体验券
     * @param string $time 时间范围。1-最近一个月；2-最近一个季度；3-最近一年
     * @param object $page 页码
     * @return array 没有数据时返回空数组array()
     */
    public function shopGetCouponList($shopCode, $couponType, $time, $page) {
        $condition = array();
        if($shopCode) {
            $condition['BatchCoupon.shopCode'] = $shopCode;
        }
        if($time) {
            if($time == 1) {$time = strtotime('-1 month');}
            if($time == 2) {$time = strtotime('-3 month');}
            if($time == 3) {$time = strtotime('-1 year');}
            $condition['unix_timestamp(BatchCoupon.createTime)'] = array('EGT', $time);
        }
        $condition['couponType'] = $couponType;
        $couponList = $this->field(array(
            'batchCouponCode',
            'batchNbr',
            'totalVolume',
            'insteadPrice',
            'payPrice',
            'endTakingTime',
            'expireTime',
            'BackgroundTemplate.url',
            'BatchCoupon.isAvailable'
        ))
            ->join('Shop on Shop.shopCode = BatchCoupon.shopCode')
            ->join('BackgroundTemplate on BackgroundTemplate.bgCode = BatchCoupon.bgImgCode', 'LEFT')
            ->where($condition)
            ->order('BatchCoupon.batchNbr desc')
            ->pager($page)
            ->select();
        if($couponList) {
            $userCouponMdl = new UserCouponModel();
            foreach($couponList as &$v) {
                $v['insteadPrice']  = number_format($v['insteadPrice'] / C('RATIO'), 2, '.', '');
                $v['payPrice']  = number_format($v['payPrice'] / C('RATIO'), 2, '.', '');
                $usedCouponInfo = $userCouponMdl->usedCouponInfo($v['batchCouponCode']);
                if($usedCouponInfo) {
                    $deductionPrice = $usedCouponInfo['nbrOfDeductionPrice'];
                    $totalPrice = $usedCouponInfo['totalPrice'];
                } else {
                    $deductionPrice = $totalPrice = 0;
                }
                $v['isExpire'] = substr($v['expireTime'], 0, 10) != '0000-00-00' && strtotime($v['expireTime']) >= time() ? C('YES') : C('NO');
                $v['deductionPrice'] = $deductionPrice;
                $v['totalPrice'] = $totalPrice;
                $v['usedCoupon'] = $userCouponMdl->countReceivedCoupon(array($v['batchCouponCode']), $startDate = '', $endDate = '');
                unset($v['endTakingTime']);
                unset($v['expireTime']);
            }
            return $couponList;
        }
        return array();
    }

    /**
     * 获取商家发行的可领的优惠券列表
     * @param string $shopCode 商店编码
     * @param int $couponType 优惠券类型 1-N元购，2-满就送，3-抵扣券，4-折扣券，5-实物券，6-体验券
     * @param string $time 时间范围。1-最近一个月；2-最近一个季度；3-最近一年
     * @return array 没有数据时返回空数组array()
     */
    public function countShopGetCouponList($shopCode, $couponType, $time) {
        $condition = array();
        if($shopCode) {
            $condition['BatchCoupon.shopCode'] = $shopCode;
        }
        if($time) {
            if($time == 1) {$time = strtotime('-1 month');}
            if($time == 2) {$time = strtotime('-3 month');}
            if($time == 3) {$time = strtotime('-1 year');}
            $condition['unix_timestamp(BatchCoupon.createTime)'] = array('EGT', $time);
        }
        $condition['couponType'] = $couponType;
        return $this
            ->join('Shop on Shop.shopCode = BatchCoupon.shopCode')
            ->join('BackgroundTemplate on BackgroundTemplate.bgCode = BatchCoupon.bgImgCode', 'LEFT')
            ->where($condition)
            ->count();
    }

    /**
     * 获取优惠券列表，如果shopCode不为空，则为获取商家发行的可领的优惠券列表
     * @param string $shopCode 商店编码
     * @param string $time 时间范围。1-最近一个月；2-最近一个季度；3-最近一年
     * @param number $longitude 经度
     * @param number $latitude 纬度
     * @param int $orderRule 排序规则。默认按距离排序，由近到远1-距离；2-好评度，等等
     * @param object $page 页码
     * @return array
     */
    public function getCouponList($shopCode, $time, $longitude, $latitude, $orderRule = 1, $page) {
        $condition = array();
        if($shopCode){
            $condition['BatchCoupon.shopCode'] = $shopCode;
        }
        if($time){
            if($time == 1) {$time = strtotime('-1 month');}
            if($time == 2) {$time = strtotime('-3 month');}
            if($time == 3) {$time = strtotime('-1 year');}
            $condition['unix_timestamp(BatchCoupon.createTime)'] = array('GT', $time);
        }
        $order = 'distance desc';
        if($orderRule != 1){
            $order = 'Shop.creditPoint desc';
        }
        $condition['unix_timestamp(expireTime)'] = array('GT','unix_timestamp(now())');
        $userCouponMdl = new UserCouponModel();
        $couponList = $this->field(array(
            'BatchCoupon.*',
            'Shop.creditPoint',
            'Shop.type',
            "sqrt(power(Shop.longitude-'.$longitude.',2) + power(Shop.latitude-'.$latitude.', 2))" => 'distance'
        ))
            ->join('Shop on Shop.shopCode = BatchCoupon.shopCode')
            ->where($condition)
            ->order($order)
            ->pager($page)
            ->select();
        if($couponList){
            foreach($couponList as $k => $v){
                if(strtotime($v['endTakingTime']) <= time()){
                    $couponList[$k]['restCouponNbr'] = 0;
                }else{
                    $countReceived = $userCouponMdl->countReceivedCoupon(array($v['batchCouponCode']), $startDate = '', $endDate = '');
                    if($v['totalVolume'] == -1){
                        $couponList[$k]['restCouponNbr'] = $v['totalVolume'] - $countReceived;
                    }else{
                        $couponList[$k]['restCouponNbr'] = 100;
                    }
                }
            }
            return $couponList;
        }
        return array();
    }

    /**
     * 获得商店可以领取的优惠券
     * @param string $shopCode 商店编码
     * @param array $field 查询字段
     * @param string $userCode 用户编码
     * @return array
     */
    public function listAvailabelCoupon($shopCode, $field, $userCode = '',$longitude,$latitude) {
        $sql = '(validityPeriod = 0) OR (UNIX_TIMESTAMP(CONCAT(LEFT(BatchCoupon.expireTime, 11),dayEndUsingTime)) >= UNIX_TIMESTAMP() AND BatchCoupon.validityPeriod = -1) AND ((totalVolume = -1) OR (totalVolume > 0 AND remaining > 0))'; // 有效的优惠券
		$batchCoupon=D('BatchCoupon'); //创建数据库对象
	   $couponList = $batchCoupon		
           // ->field($field)
		   ->field(array(
		   'batchCouponCode',
		   'couponName',
		   'totalVolume',
		   'remaining',
		   'insteadPrice',
		   'discountPercent',
		   'availablePrice',
		   'endTakingTime',
		   'BatchCoupon.createTime',
		   'couponType',
		   'function',
		   'batchNbr',
		   'startUsingTime',
		   'BackgroundTemplate.url',
		   'expireTime',
		   'dayStartUsingTime',
		   'dayEndUsingTime',
		   'BatchCoupon.remark','city',
		   'shopName',
		   'BatchCoupon.payPrice',
		   'BatchCoupon.shopCode',		 
		   'sqrt(power(Shop.longitude-".$longitude.",2) + power(Shop.latitude-".$latitude.",2))'=>'distance',	//添加经纬度	  
		   'Shop.logoUrl',
		   'CollectCoupon.isCollect', //添加是否收藏
		   'CollectCoupon.isGet'	//添加是否领取
		   ))
            ->join('Shop ON Shop.shopCode = BatchCoupon.shopCode')
           ->join('BackgroundTemplate on BackgroundTemplate.bgCode = BatchCoupon.bgImgCode')
			->join('CollectCoupon on CollectCoupon.collectCouponCode=BatchCoupon.batchCouponCode','LEFT') //添加其余表关联
            ->where(array(
                'BatchCoupon.shopCode' => $shopCode,
                'startTakingTime' => array('ELT', date('Y-m-d H:i:s')),
                'endTakingTime' => array('EGT', date('Y-m-d H:i:s')),
                'isAvailable' => C('YES'),
                'couponType' => array('NOTIN', array(\Consts::COUPON_TYPE_SEND_INVITER, \Consts::COUPON_TYPE_NEW_CLIENT_REDUCTION)),
                'isSend' => C('NO'),
                '_string' => $sql,
            ))
			->group('batchCouponCode')
            ->order('createTime')
            ->select();
        $coupon = array();
        if($couponList) {
            $userCouponMdl = new UserCouponModel();
            $tempDate = array('endTakingTime', 'createTime', 'startUsingTime', 'expireTime');
            foreach($couponList as $v) {
                $v['city'] = $this->formatCity($v['city']);
                if($v['totalVolume'] == -1){
                    $v['takenCount'] = abs($v['remaining']);
                }else{
                    $v['takenCount'] = $v['totalVolume'] - $v['remaining'];
                    $v['takenPercent'] = round($v['takenCount'] / $v['totalVolume'] * C('RATIO'), 2);
                }

                $v['discountPercent'] = $v['discountPercent'] / $this->discountRatio;
                $v['insteadPrice'] = $v['insteadPrice'] / \Consts::HUNDRED;
                $v['availablePrice'] = $v['availablePrice'] / \Consts::HUNDRED;
                $v['payPrice'] = $v['payPrice'] / \Consts::HUNDRED;
                // 判断优惠券是否过期
                $v['isExpire'] =  substr($v['expireTime'], 0, 10) != '0000-00-00' && strtotime($v['expireTime']) < time() ? C('NO') : C('YES');

                foreach($tempDate as $date) {
                    $v[$date] = $this->formatDate1($v[$date]);
                }
                $v['dayStartUsingTime'] = substr($v['dayStartUsingTime'], 0, 5);
                $v['dayEndUsingTime'] = substr($v['dayEndUsingTime'], 0, 5);


                if($userCode) {
                    $userCouponMdl = new UserCouponModel();
                    $v['hasCoupon'] = $userCouponMdl->countMyReceivedCoupon($v['batchCouponCode'], $userCode);
                    if($userCouponMdl->isUserCanGrabTheCoupon($userCode, $v['batchCouponCode'])){
                        $coupon[] = $v;
                    }
                } else {
                    $coupon[] = $v;
                }
            }
        }
		
        return $coupon;
    }

    /**
     * 管理端获得商家优惠券列表
     * @param array $filterData
     * @param int $type 优惠券类型
     * @param object $page 偏移值
     * @return null|array 如果为空，返回null；如果不为空，返回二维数组array(array());
     */
    public function listBatchCoupon($filterData, $type, $page) {
        $where = $this->filterWhere(
            $filterData,
            array('couponName' => 'like', 'shopName' => 'like', 'batchNbr' => 'like'),
            $page);

//        判断是否为惠圈管理人员
        if($_SESSION['USER']['bank_id']!=-1){
            $where['bank_id'] = $_SESSION['USER']['bank_id'];
        }

        if($where['shopCode']){
            $where['Shop.shopCode'] = $where['shopCode'];
            unset($where['shopCode']);
        }
        if($where['city'] == '义乌市') {
            $where['district'] = $where['city'];
            unset($where['city']);
        }
        $where['couponBelonging'] = array('EQ', $type);
        $couponList =  $this
            ->field(array('batchCouponCode', 'Shop.shopCode','Shop.bank_id','shopName', 'couponName', 'totalVolume', 'expireTime', 'startUsingTime', 'createTime', 'isAvailable', 'couponType', 'startTakingTime', 'endTakingTime', 'remaining', 'isUniversal', 'batchNbr', 'nbrPerPerson', 'insteadPrice', 'discountPercent', 'function', 'payPrice', 'remark', 'validityPeriod', 'isSend'))
            ->join('Shop ON Shop.shopCode = BatchCoupon.shopCode','LEFT')
            ->where($where)
            ->order('createTime desc,shopName, batchNbr desc')
            ->pager($page)
            ->select();	
        foreach($couponList as &$v) {
            $v['discountPercent'] = $v['discountPercent'] / $this->discountRatio;
            $v['insteadPrice'] = $v['insteadPrice'] / C('RATIO');
            $v['payPrice'] = $v['payPrice'] / C('RATIO');
        }
        return $couponList;
    }

    /**
     * 管理端总数
     * @param array $filterData
     * @param $type
     * @return null|int 如果为空，返回null；如果不为空，返回number;
     */
    public function countBatchCoupon($filterData, $type) {
        $where = $this->filterWhere(
            $filterData,
            array('couponName' => 'like', 'shopName' => 'like', 'batchNbr' => 'like')
        );
        if($where['shopCode']){
            $where['Shop.shopCode'] = $where['shopCode'];
            unset($where['shopCode']);
        }
        if($where['city'] == '义乌市') {
            $where['district'] = $where['city'];
            unset($where['city']);
        }
        if($type) {
            $where['couponBelonging'] = array('eq', $type);
        }

        //        判断是否为惠圈管理人员
        if($_SESSION['USER']['bank_id']!=-1){
            $where['bank_id'] = $_SESSION['USER']['bank_id'];
        }

        return $this
            ->join('Shop ON Shop.shopCode = BatchCoupon.shopCode', 'LEFT')
            ->where($where)
			//->fetchsql(true)
            ->count('batchCouponCode');
	
    }

    /**
     * 获得商家优惠券的统计信息
     * @param string $shopCode 商家编码
     * @return array
     */
    public function getAllCoupon($shopCode){
        $nbrOfBatch = $this->where(array('shopCode' => $shopCode))->count('batchCouponCode');
        $nbrOfDeductionPrice = $totalPrice = $totalPersonAmount = $restOfCoupon = 0;
        $batchCouponList = $this
            ->field('batchCouponCode, expireTime, endTakingTime, totalVolume')
            ->where(array('shopCode' => $shopCode, 'couponType' => array('in', C('SHOP_NORMAL_COUPON'))))
            ->select();
        if($batchCouponList){
            $userCouponMdl = new UserCouponModel();
            foreach($batchCouponList as $v) {
                $coupon = $userCouponMdl->usedCouponInfo($v['batchCouponCode']);
                if($coupon){
                    $nbrOfDeductionPrice += $coupon['nbrOfDeductionPrice'];
                    $totalPrice += $coupon['totalPrice'];
                    $totalPersonAmount += $coupon['totalPersonAmount'];
                }
                if(strtotime($v['expireTime']) > time()){
                    $restOfCoupon += $userCouponMdl->countActiveCoupon($v['batchCouponCode']);
//                    if(strtotime($v['endTakingTime']) > time()){
//                        $countReceived = $userCouponMdl->countReceivedCoupon(array($v['batchCouponCode']));
//                        $restOfCoupon += $v['totalVolume'] - $countReceived;
//                    }
                }
            }
        }
        return array(
            'nbrOfBatch' => $nbrOfBatch,  //发放优惠券的批次总数
            'nbrOfDeductionPrice' => $nbrOfDeductionPrice ? $nbrOfDeductionPrice : 0,  //共抵扣的金额
            'totalPrice' => $totalPrice ? $totalPrice : 0,  //带来的消费金额
            'totalPersonAmount' => $totalPersonAmount,  //带来的消费人次
            'restOfCoupon' => $restOfCoupon,  //当前已被领取且未使用的优惠券
        );
    }

    /**
     * 获得商家不同类型优惠券的统计信息
     * @param string $shopCode 商家编码
     * @return array
     */
    public function getCouponInfoByType($shopCode) {
        $shopMdl = new ShopModel();
        $shopInfo = $shopMdl->getOneShopInfo(array('shopCode' => $shopCode), array('isAcceptBankCard'));
        $couponInfo = array();
        $where = array(
            'shopCode' => $shopCode,
            '_string' => '((totalVolume = -1) OR (totalVolume > 0 AND remaining > 0))'
        );
        $couponType = C('SHOP_NORMAL_COUPON');
        foreach($couponType as $k =>$v) {
            if(in_array($v, array(7,8)) && in_array($_SERVER['HTTP_HOST'], array('web.huiquan.suanzi.cn', 'api.huiquan.suanzi.cn', 'huiquan.suanzi.cn'))){
                continue;
            }
            $where['couponType'] = $v;
            $batchNbrAmt = $this->where($where)->count();
            $where['totalVolume'] = array('GT', 0);
            $restNbr = $this->where($where)->sum('remaining');
            if(empty($restNbr)){
                $restNbr = 0;
                $where['totalVolume'] = array('EQ', -1);
                $ret = $this->field(array('batchCouponCode'))->where($where)->find();
                if($ret){
                    $restNbr = 100;
                }
            }
            $info = array(
                'couponType' => $v,
                'couponImg' => C('COUPON_BG.'.$k),
                'batchNbrAmt' => $batchNbrAmt,
                'restNbr' => $restNbr ? $restNbr : 0,
                'isAcceptBankCard' => empty($shopInfo['isAcceptBankCard'])? C('NO'):C('YES'),
            );
            $couponInfo[] = $info;
        }
        return $couponInfo;
    }

    /**
     * 判断优惠券是否可以领用
     * @param string $batchCouponCode 优惠券编码
     * @param number $receivedCount 该优惠券已经被领取的数量
     * @param number $myReceivedCount 顾客已经领取的该优惠券的数量
     * @param  $type
     * @return boolean||int 可以领用，返回true；不能领用，返回错误代码
     */
    public function isCouponCanBeGet($batchCouponCode, $receivedCount, $myReceivedCount, $type = 0) {
        $batchCouponInfo = $this
            ->field(array('nbrPerPerson', 'totalVolume', 'endTakingTime', 'isAvailable'))
            ->where(array('batchCouponCode' => $batchCouponCode))
            ->find();
        if($type != C('YES')){
            if($batchCouponInfo['isAvailable'] == C('NO')) {
                return C('COUPON.NOT_EXIST');
            }
        }

        // 判断优惠券是否已经过最后可领取期
        if(strtotime($batchCouponInfo['endTakingTime']) <= time()) {
            return C('COUPON.EXPIRED');
        }
        // 判断优惠券是否已被领完
        if($batchCouponInfo['totalVolume'] > 0 && $receivedCount >= $batchCouponInfo['totalVolume']) {
            return C('COUPON.BEEN_TOKEN_OVER');
        }
        // 判断个人是否已经达到优惠券的个人领取上限
        if($batchCouponInfo['nbrPerPerson'] != self::NO_LIMIT && $myReceivedCount >= $batchCouponInfo['nbrPerPerson']) {
            return C('COUPON.LIMIT');
        }
        return true;
    }

    /**
     * 判断个人是否达到优惠券的个人领取上限
     * @param string $batchCouponCode 优惠券编码
     * @param string $myReceivedCount 个人已经领取的数量
     * @return number||boolean 如果达到，返回错误代码；如果没有达到，返回false
     */
    public function isReachPersonalLimit($batchCouponCode, $myReceivedCount) {
        $batchCouponInfo = $this->field(array('nbrPerPerson'))->where(array('batchCouponCode' => $batchCouponCode))->find();
        if($batchCouponInfo['nbrPerPerson'] != self::NO_LIMIT && $myReceivedCount >= $batchCouponInfo['nbrPerPerson']) {
            return C('COUPON.LIMIT');
        }
        return false;
    }

    /**
     * 判断优惠券是否过期
     * @param string $batchCouponCode 优惠券编码
     * @return number||boolean 如果过期，返回错误代码；如果没有过期，返回false
     */
    public function isCouponExpired($batchCouponCode) {
        $batchCouponInfo = $this->field(array('expireTime'))->where(array('batchCouponCode' => $batchCouponCode))->find();
        if(substr($batchCouponInfo['expireTime'], 0, 10) != '0000-00-00' && strtotime($batchCouponInfo['expireTime']) <= time()) {
            return C('COUPON.EXPIRED');
        }
        return false;
    }

    /**
     * 统计今日新增会员阿卡
     * @return 0|number 如果为空，返回0；如果不为空，返回number
     */
    public function todayAddBatchCoupon() {
        $date = date('Y-m-d');
        $where['createTime'] = array('Like', '%'.$date.'%');
        return $this->where($where)->count();
    }

    /**
     * 获取优惠券信息
     * @param string $batchCouponCode 优惠券编码
     * @param array $field 查询字段
     * @return array||null
     */
    public function getCouponInfo($batchCouponCode, $field = array()) {
        if(empty($field)){
            $field = array('BatchCoupon.*', 'BackgroundTemplate.url');
        }
        $couponInfo = $this
            ->field($field)
            ->join('BackgroundTemplate on BackgroundTemplate.bgCode = BatchCoupon.bgImgCode', 'LEFT')
            ->where(array('batchCouponCode' => $batchCouponCode))
            ->find();
        $couponInfo = $this->dividedByHundred($couponInfo, array('availablePrice', 'insteadPrice', 'canTakePrice', 'sendRequired', 'payPrice'));
        if(isset($couponInfo['exRuleList'])){
            $exRuleList = array();
            if($couponInfo['exRuleList']) {
                $exRuleCodes = (array)json_decode($couponInfo['exRuleList']);
                if($exRuleCodes) {
                    $crMdl = new CouponRuleModel();
                    $where['ruleCode'] = array('in', $exRuleCodes);
                    $exRuleList = $crMdl->listCouponRule($where, array('ruleCode', 'ruleDes', 'creatorCode', 'createTime'));
                }
            }
            $couponInfo['exRuleList'] = $exRuleList;
        }

        if(isset($couponInfo['discountPercent'])){
            $couponInfo['discountPercent'] = $couponInfo['discountPercent'] / $this->discountRatio;
        }
        $tempArray = array('endTakingTime', 'startTakingTime', 'startUsingTime', 'expireTime');
        foreach($tempArray as $v) {
            if(isset($couponInfo[$v])){
                $couponInfo[$v] = substr($couponInfo[$v], 0, 10);
            }
        }
        return $couponInfo;
    }

    /**
     * 根据条件获取优惠券的某些字段信息
     * @param array $condition 条件
     * @return array 二维数组
     */
    public function listBatchCouponCode($condition = array()){
        $couponList = $this->where($condition)->getField('batchCouponCode', true);
        return $couponList;
    }

    /**
     * 优惠券剩余数量减1
     * @param string $batchCouponCode 优惠券编码
     * @return boolean 成功返回true,失败返回false
     */
    public function reduceCoupon($batchCouponCode) {
        return $this->where(array('batchCouponCode' => $batchCouponCode))->setDec('remaining', $this->reduceNbr) !== false ? true : false;
    }

    /**
     * 为listCoupon,countCoupon构造$condition
     * @param string $city 城市
     * @param string $searchWord 关键词
     * @param int $couponType 优惠券类型 0-所有类型（除去限时购）， 1-N元购，2-满就送，3-抵扣券，4-折扣券，5-实物券，6-体验券
     * @return array $condition
     */
    public function getCondition($city, $searchWord, $couponType) {	
	//	$batchCoupon=D("BatchCoupon");
    //   $CollectCouponModel=D('CollectCoupon');
	   //查询满足条件的所有优惠券编码
	// $aaa=  $CollectCouponModel->field("collectCouponCode")->where("userCode='$userCode' And isGet=1")->select();
	
	//$ccc=[];
	//foreach($aaa as $value){
		
	//	 $ccc[]=$value['collectCouponCode'];
	// }
	
	
		
	  $sql = '((totalVolume = -1) OR (totalVolume > 0 AND remaining > 0))';

	  $condition = array(			
            'expireTime' => array('EGT', date('Y-m-d H:i:s', time())),
            'startTakingTime' => array('ELT', date('Y-m-d H:i:s', time())),
            'endTakingTime' => array('EGT', date('Y-m-d H:i:s', time())),
            'isAvailable' => C('YES'),
            'isSend' => C('NO'),
            '_string' => $sql,	
			//'batchCouponCode'=>array('not in',array('9e079f2c-afaa-44ea-2f52-95d4fcdb7bbf','4b0726f2-f750-b72d-8b12-04c76004bdc6','49bfbfb5-1557-058c-732b-5b06a7e5b66f'))
			//'batchCouponCode'=>array('not in',$ccc),			
        );
	
		  
        if($city == '义乌市') {
            $condition['district'] = $city;
        } else {
            $condition['city'] = $city;
        }
//        if($couponType == C('COUPON_TYPE.DISCOUNT')) {
//            $subCondition['dayStartUsingTime'] = array('NEQ', '00:00:00');
//            $subCondition['dayEndUsingTime'] = array('NEQ', '23:59:59');
//        }
        $subCondition['shopName'] = array('like','%'.$searchWord.'%');
        $subCondition['street'] = array('like', '%'.$searchWord.'%');
        $subCondition['_logic'] = 'or';
        $condition['_complex'] = $subCondition;

		
        if($couponType != 0) {
            $condition['couponType'] = $couponType;
        } else {
            $condition['couponType'] = array('IN', array(
                \Consts::COUPON_TYPE_N_PURCHASE,
                \Consts::COUPON_TYPE_REDUCTION,
                \Consts::COUPON_TYPE_PHYSICAL,
                \Consts::COUPON_TYPE_EXPERIENCE,
                \Consts::COUPON_TYPE_DISCOUNT,
                \Consts::COUPON_TYPE_EXCHANGE,
                \Consts::COUPON_TYPE_VOUCHER,
            ));
        }
        $condition['Shop.status'] = C('SHOP_STATUS.ACTIVE');
	  
        return $condition;
    }
	


    /**
     * 根据优惠券类型，商家名称，顾客所在位置经度，纬度，页码获得优惠券列表信息
     * @param int $couponType 优惠券类型。1-N元购，2-满就送，3-抵扣券，4-折扣券，5-实物券，6-体验券
     * @param string $searchWord 检索关键字，商家名字或者地名
     * @param number $longitude 用户所在经度
     * @param number $latitude 用户所在纬度
     * @param object $page 分页
     * @param string $city 城市
     * @param string $userCode 用户编码
     * @return array $couponList 有数据返回二维数组，没有数据返回空数组array()
     */
    public function listCoupon($couponType, $searchWord, $longitude, $latitude, $page, $city, $userCode = '') {
	
       $condition = $this->getCondition($city, $searchWord, $couponType);
		//$CollectCouponModel=D('CollectCoupon');
		//查询没有领取优惠券编码		
			$couponList= $this->field(array(
            'batchCouponCode',
            'insteadPrice',
            'discountPercent',
            'availablePrice',
            'function',
            'payPrice',
            'totalVolume',
            'remaining',
            'startUsingTime',
            'expireTime',
            'BatchCoupon.remark',
            'Shop.shopCode',
            'Shop.shopName',
            'Shop.mobileNbr',
            'Shop.street',
          //  'nbrPerPerson',
            'sqrt(power(Shop.longitude-'.$longitude.',2) + power(Shop.latitude-'.$latitude.', 2))' => 'distance',
            'logoUrl',
            'url',
            'couponName',
            'BatchCoupon.remark',
            'BatchCoupon.batchNbr',
            'BatchCoupon.couponType',
            'exRuleList',
            'dayStartUsingTime',
            'dayEndUsingTime',
			'123'=>'nbrPerPerson'
			//'CollectCoupon.isCollect',
          //  'CollectCoupon.isGet'
        ))
            ->join('Shop on Shop.shopCode = BatchCoupon.shopCode')
            ->join('BackgroundTemplate on BackgroundTemplate.bgCode = BatchCoupon.bgImgCode')
			//->join('CollectCoupon on CollectCoupon.collectCouponCode=BatchCoupon.batchCouponCode','LEFT')
            ->where($condition )
			//->group('collectCouponCode')
            ->order('distance asc, BatchCoupon.createTime desc')
            ->pager($page)
            ->select();
	
        if($couponList) {
            $couponRuleMdl = new CouponRuleModel();
            $userCouponMdl = new UserCouponModel();
            foreach($couponList as $k => &$v) {
                if(isset($v['shopName'])){
                    $v['shopName'] = htmlspecialchars_decode($v['shopName'], ENT_QUOTES);
                }
                $v['startUsingTime'] = $this->formatDate1($v['startUsingTime']);
//                $v['startUsingTime'] = $v['startUsingTime'] == '2018.01.01' ? '2015.08.08' : $v['startUsingTime'];
                $v['expireTime'] = $this->formatDate1($v['expireTime']);
                $v['discountPercent'] = $v['discountPercent'] / $this->discountRatio;
                $v['distance'] = intval($v['distance'] * C('PROPORTION'));
//                $countReceived = $userCouponMdl->countReceivedCoupon(array($v['batchCouponCode']), $startDate = '', $endDate = '');
                if($userCode) {
                    if($v['payPrice'] > 0){
                        $orderCouponMdl = new OrderCouponModel();
                        $countMyReceived = $orderCouponMdl->countOrderCoupon(array('batchCouponCode' => $v['batchCouponCode'], 'userCode' => $userCode, 'status' => array('IN', array(\Consts::ORDER_COUPON_STATUS_USE, \Consts::ORDER_COUPON_STATUS_USED))));
                        $countMyActiveReceived = $orderCouponMdl->countOrderCoupon(array('batchCouponCode' => $v['batchCouponCode'], 'userCode' => $userCode, 'status' => \Consts::ORDER_COUPON_STATUS_USE));
                        $couponList[$k]['isMyReceived'] = $countMyReceived?$countMyReceived:C('NO');
                        $couponList[$k]['countMyReceived'] = $countMyReceived;
                        $couponList[$k]['countMyActiveReceived'] = $countMyActiveReceived;
                    }else{
                        $countMyReceived = $userCouponMdl->countMyReceivedCoupon($v['batchCouponCode'],$userCode);
                        $countMyActiveReceived = $userCouponMdl->countActiveCoupon($v['batchCouponCode'],$userCode);
                        $couponList[$k]['isMyReceived'] = 0;
                        $couponList[$k]['countMyReceived'] = 0;
                        $couponList[$k]['countMyActiveReceived'] = $countMyActiveReceived ? $countMyActiveReceived : C('NO');
                        if($countMyReceived){
                            $couponList[$k]['isMyReceived'] = 1;
                            $couponList[$k]['countMyReceived'] = $countMyReceived;
                        }
                    }

                }
                if($v['totalVolume'] == -1){
                    $couponList[$k]['restCouponNbr'] = 100;
                }else{
                    $couponList[$k]['restCouponNbr'] = $v['totalVolume'] - $v['remaining'];
                }
                $couponList[$k]['insteadPrice'] = $v['insteadPrice'] / C('RATIO');
                $couponList[$k]['availablePrice'] = $v['availablePrice'] / C('RATIO');
                $couponList[$k]['payPrice'] = $v['payPrice'] / C('RATIO');

                if($v['exRuleList']) {
                    $v['exRuleList'] = json_decode($v['exRuleList']);
                    $exRuleDesList = $couponRuleMdl->listCouponRule(array('ruleCode' => array('IN', $v['exRuleList'])), array('ruleDes'));
                    $v['exRuleDes'] = $exRuleDesList[0]['ruleDes'];
                } else {
                    $v['exRuleDes'] = '';
                }
                unset($v['exRuleList']);
            }
            
			//return $couponList;
			//$a[]=array($couponList,$collectCoupon);
			// return $a;
		
	return array(
	'CouponListInfo'=>$couponList,
//	'CollectInfo'=>$collectCoupon,
        );
		}
	   return array();
    }



    /**
     * 根据优惠券类型，商家名称，顾客所在位置经度，纬度，页码获得优惠券列表信息(乔本亮927添加)
     * @param int $couponType 优惠券类型。1-N元购，2-满就送，3-抵扣券，4-折扣券，5-实物券，6-体验券
     * @param string $searchWord 检索关键字，商家名字或者地名
     * @param number $longitude 用户所在经度
     * @param number $latitude 用户所在纬度
     * @param object $page 分页
     * @param string $city 城市
     * @param string $userCode 用户编码
     * @return array $couponList 有数据返回二维数组，没有数据返回空数组array()
     */
    public function listCoupon2($couponType, $searchWord, $longitude, $latitude, $page, $city, $userCode = '',$zoneId) {

        $condition = $this->getCondition($city, $searchWord, $couponType);
        //$CollectCouponModel=D('CollectCoupon');
        //查询没有领取优惠券编码
        $couponList= $this->field(array(
            'batchCouponCode',
            'insteadPrice',
            'discountPercent',
            'availablePrice',
            'function',
            'payPrice',
            'totalVolume',
            'remaining',
            'startUsingTime',
            'expireTime',
            'BatchCoupon.remark',
            'Shop.shopCode',
            'Shop.shopName',
            'Shop.mobileNbr',
            'Shop.street',
            //  'nbrPerPerson',
            'sqrt(power(Shop.longitude-'.$longitude.',2) + power(Shop.latitude-'.$latitude.', 2))' => 'distance',
            'logoUrl',
            'url',
            'couponName',
            'BatchCoupon.remark',
            'BatchCoupon.batchNbr',
            'BatchCoupon.couponType',
            'exRuleList',
            'dayStartUsingTime',
            'dayEndUsingTime',
            '123'=>'nbrPerPerson'
            //'CollectCoupon.isCollect',
            //  'CollectCoupon.isGet'
        ))
            ->join('Shop on Shop.shopCode = BatchCoupon.shopCode and Shop.bank_id='.$zoneId)
            ->join('BackgroundTemplate on BackgroundTemplate.bgCode = BatchCoupon.bgImgCode')
            //->join('CollectCoupon on CollectCoupon.collectCouponCode=BatchCoupon.batchCouponCode','LEFT')
            ->where($condition )
            //->group('collectCouponCode')
            ->order('distance asc, BatchCoupon.createTime desc')
            ->pager($page)
            ->select();

        if($couponList) {
            $couponRuleMdl = new CouponRuleModel();
            $userCouponMdl = new UserCouponModel();
            foreach($couponList as $k => &$v) {
                if(isset($v['shopName'])){
                    $v['shopName'] = htmlspecialchars_decode($v['shopName'], ENT_QUOTES);
                }
                $v['startUsingTime'] = $this->formatDate1($v['startUsingTime']);
//                $v['startUsingTime'] = $v['startUsingTime'] == '2018.01.01' ? '2015.08.08' : $v['startUsingTime'];
                $v['expireTime'] = $this->formatDate1($v['expireTime']);
                $v['discountPercent'] = $v['discountPercent'] / $this->discountRatio;
                $v['distance'] = intval($v['distance'] * C('PROPORTION'));
//                $countReceived = $userCouponMdl->countReceivedCoupon(array($v['batchCouponCode']), $startDate = '', $endDate = '');
                if($userCode) {
                    if($v['payPrice'] > 0){
                        $orderCouponMdl = new OrderCouponModel();
                        $countMyReceived = $orderCouponMdl->countOrdgetShopInfoerCoupon(array('batchCouponCode' => $v['batchCouponCode'], 'userCode' => $userCode, 'status' => array('IN', array(\Consts::ORDER_COUPON_STATUS_USE, \Consts::ORDER_COUPON_STATUS_USED))));
                        $countMyActiveReceived = $orderCouponMdl->countOrderCoupon(array('batchCouponCode' => $v['batchCouponCode'], 'userCode' => $userCode, 'status' => \Consts::ORDER_COUPON_STATUS_USE));
                        $couponList[$k]['isMyReceived'] = $countMyReceived?$countMyReceived:C('NO');
                        $couponList[$k]['countMyReceived'] = $countMyReceived;
                        $couponList[$k]['countMyActiveReceived'] = $countMyActiveReceived;
                    }else{
                        $countMyReceived = $userCouponMdl->countMyReceivedCoupon($v['batchCouponCode'],$userCode);
                        $countMyActiveReceived = $userCouponMdl->countActiveCoupon($v['batchCouponCode'],$userCode);
                        $couponList[$k]['isMyReceived'] = 0;
                        $couponList[$k]['countMyReceived'] = 0;
                        $couponList[$k]['countMyActiveReceived'] = $countMyActiveReceived ? $countMyActiveReceived : C('NO');
                        if($countMyReceived){
                            $couponList[$k]['isMyReceived'] = 1;
                            $couponList[$k]['countMyReceived'] = $countMyReceived;
                        }
                    }

                }
                if($v['totalVolume'] == -1){
                    $couponList[$k]['restCouponNbr'] = 100;
                }else{
                    $couponList[$k]['restCouponNbr'] = $v['totalVolume'] - $v['remaining'];
                }
                $couponList[$k]['insteadPrice'] = $v['insteadPrice'] / C('RATIO');
                $couponList[$k]['availablePrice'] = $v['availablePrice'] / C('RATIO');
                $couponList[$k]['payPrice'] = $v['payPrice'] / C('RATIO');

                if($v['exRuleList']) {
                    $v['exRuleList'] = json_decode($v['exRuleList']);
                    $exRuleDesList = $couponRuleMdl->listCouponRule(array('ruleCode' => array('IN', $v['exRuleList'])), array('ruleDes'));
                    $v['exRuleDes'] = $exRuleDesList[0]['ruleDes'];
                } else {
                    $v['exRuleDes'] = '';
                }
                unset($v['exRuleList']);
            }

            //return $couponList;
            //$a[]=array($couponList,$collectCoupon);
            // return $a;

            return array(
                'CouponListInfo'=>$couponList,
//                'CollectInfo'=>$collectCoupon,
            );
        }
        return array();
    }



    /**
     * 统计总记录数
     * @param int $couponType 优惠券类型。1-N元购，2-满就送，3-抵扣券，4-折扣券，5-实物券，6-体验券
     * @param string $searchWord 检索关键字，商家名字或者地名
     * @param number $longitude 用户所在经度
     * @param number $latitude 用户所在纬度
     * @param string $city 城市
     * @return int
     */
    public function countCoupon($couponType, $searchWord, $longitude, $latitude, $city) {
        $condition = $this->getCondition($city, $searchWord, $couponType);
        $couponCount = $this
            ->join('Shop on Shop.shopCode = BatchCoupon.shopCode')
            ->join('BackgroundTemplate on BackgroundTemplate.bgCode = BatchCoupon.bgImgCode')
            ->where($condition)
            ->count('batchCouponCode');
        return $couponCount;
    }

    /**
     * 统计总记录数（乔本亮0927修改）
     * @param int $couponType 优惠券类型。1-N元购，2-满就送，3-抵扣券，4-折扣券，5-实物券，6-体验券
     * @param string $searchWord 检索关键字，商家名字或者地名
     * @param number $longitude 用户所在经度
     * @param number $latitude 用户所在纬度
     * @param string $city 城市
     * @return int
     */
    public function countCoupon2($couponType, $searchWord, $longitude, $latitude, $city,$zoneId) {
        $condition = $this->getCondition($city, $searchWord, $couponType);
        $couponCount = $this
            ->join('Shop on Shop.shopCode = BatchCoupon.shopCode and Shop.bank_id='.$zoneId)
            ->join('BackgroundTemplate on BackgroundTemplate.bgCode = BatchCoupon.bgImgCode')
            ->where($condition)
            ->count('batchCouponCode');
        return $couponCount;
    }


    /**
     * 获得滚屏优惠券的信息
     * @param string $batchCouponCode 优惠券编码
     * @return array
     */
    public function cGetScrollInfo($batchCouponCode){
        return $this
            ->field(array('batchCouponCode', 'totalVolume', 'startTakingTime', 'endTakingTime'))
            ->where(array('batchCouponCode'=>$batchCouponCode))
            ->find();
    }

    /**
     * 买单后，通过消费金额获取某商家折扣从多到少的优惠券
     * @param string $shopCode 商家编码
     * @param int $price 单位：分
     * @return array {'userCouponCode'}
     */
    public function getBestCouponByConsumePrice($shopCode, $price){
        $sql = '((totalVolume = -1) OR (totalVolume > 0 AND remaining > 0))';
        $condition = array(
            'expireTime' => array('EGT', date('Y-m-d H:i:s', time())),
            'endTakingTime' => array('EGT', date('Y-m-d H:i:s', time())),
            'startTakingTime' => array('ELT', date('Y-m-d H:i:s', time())),
            'sendRequired' => array('ELT', $price),
            'shopCode' => $shopCode,
            'isSend' => C('YES'),
            '_string' => $sql
        );
        return $this
            ->field(array('batchCouponCode'))
            ->where($condition)
            ->order('insteadPrice desc, discountPercent asc')
            ->select();
    }

    /**
     * 获取某个时间段内某商家某种类型的优惠券是否有设置满就送优惠券
     * @param $shopCode
     * @param $startUsingTime
     * @param $expireTime
     * @return array
     */
    public function getSendCouponByTime($shopCode, $startUsingTime, $expireTime) {
        return array('canSetSendCoupon'=>C('YES'));
        $condition['shopCode'] = $shopCode;
        $condition['startUsingTime'] = array('EGT', $startUsingTime);
        $condition['expireTime'] = array('ELT', $expireTime);
        $condition['isSend'] = C('YES');
        $coupon = $this->field(array('batchCouponCode'))->where($condition)->order('createTime desc')->find();
        if($coupon){
            return array('canSetSendCoupon'=>C('NO'));
        }
        return array('canSetSendCoupon'=>C('YES'));
    }

    /**
     * 获取某用户可以得到的商家可送的优惠券的信息
     * @param string $userCode 用户编码
     * @param string $shopCode 商户编码
     * @param int $orderAmount 消费金额，单位：分
     * @return mixed
     */
    public function getMaxSendCouponInfo($userCode, $shopCode, $orderAmount){
        $batchCouponCodeList = array();
        if($userCode){
            $userCouponMdl = new UserCouponModel();
            // 获得商户所有可送的优惠券列表
            $userCouponList = $this
                ->field(array('batchCouponCode', 'nbrPerPerson'))
                ->where(array('shopCode' => $shopCode, 'isSend' => C('YES'), 'limitedSendNbr' => array('GT', 0)))
                ->select();
            // 获得用户已经领取的该商家的可送的优惠券
            foreach($userCouponList as $v) {
                // 获得用户拥有的该优惠券的数量
                $userCount = $userCouponMdl->countMyReceivedCoupon($v['batchCouponCode'], $userCode);
                if($v['nbrPerPerson'] > 0 && $userCount >= $v['nbrPerPerson']) {
                    $batchCouponCodeList[] = $v['batchCouponCode'];
                }
            }
        }

        $condition = array(
            'shopCode' => $shopCode,
            'expireTime' => array('GT', date('Y-m-d H:i:s')),
            'startUsingTime' => array('ELT', date('Y-m-d H:i:s')),
            'isSend' => C('YES'),
            'limitedSendNbr' => array('GT', 0),
            '_string' => '((totalVolume = -1) OR (totalVolume > 0 AND remaining > 0))',
        );
        if($orderAmount > 0) {
            $condition['sendRequired'] = array('ELT', $orderAmount);
        }
        if(!empty($batchCouponCodeList)) {
            $condition['batchCouponCode'] = array('notin', $batchCouponCodeList);
        }
        // 获得商户可送的优惠券。已满多少元可送，降序排序
        $batchCouponList = $this
            ->field(array('batchCouponCode', 'sendRequired', 'couponType', 'discountPercent', 'insteadPrice', 'availablePrice', 'function', 'batchNbr'))
            ->where($condition)
            ->order('sendRequired desc')
            ->select();
        foreach($batchCouponList as $k=>$v) {
            $batchCouponList[$k]['discountPercent'] = $v['discountPercent'] / C('TEN');
            $batchCouponList[$k]['insteadPrice'] = $v['insteadPrice'] / C('HUNDRED');
            $batchCouponList[$k]['availablePrice'] = $v['availablePrice'] / C('HUNDRED');
        }
        return $batchCouponList;
    }

    /**
     * 付款时获得用户未领取的的优惠券列表
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param float $consumeAmount 消费金额
     * @param int $couponType 优惠券类型
     * @return array $userCouponList 有数据返回二维数组，没有数据返回空数组
     */
    public function listUserCouponWhenPay($userCode, $shopCode, $consumeAmount, $couponType) {
        if(empty($couponType)){
            $couponTypeArr = array(\Consts::COUPON_TYPE_DISCOUNT, \Consts::COUPON_TYPE_REDUCTION);
        }else{
            $couponTypeArr = array($couponType);
        }
        $sql = '(validityPeriod = 0) OR (startUsingTime <= NOW() AND expireTime >= NOW() AND validityPeriod = -1) AND ((totalVolume = -1) OR (totalVolume > 0 AND remaining > 0))';
        $where = array(
            'shopCode' => $shopCode,
            'dayStartUsingTime' => array('ELT', date('H:i:s', time())),
            'dayEndUsingTime' => array(array('GT', date('H:i:s', time())), array('EQ', '00:00:00'), 'or'),
            'couponType' => array('IN', $couponTypeArr),
            'isAvailable' => C('YES'),
            'isSend' => C('NO'),
            '_string' => $sql
        );
        if($consumeAmount > 0) {
            $where['availablePrice'] = array('ELT', $consumeAmount * C('RATIO'));
        }

        $couponList = $this
            ->field(array(
                'insteadPrice',
                'discountPercent',
                'availablePrice',
                'limitedNbr',
                'batchNbr',
                'function',
                'couponType',
                'batchCouponCode',
                'startUsingTime',
                'expireTime',
                'dayStartUsingTime',
                'dayEndUsingTime',
                'isSend',
                'nbrPerPerson'
            ))
            ->where($where)
            ->order('availablePrice asc, insteadPrice DESC, discountPercent DESC')
            ->select();
        $coupon = array();
        foreach($couponList as$v) {
            $userCouponMdl = new UserCouponModel();
            $count = $userCouponMdl->countMyReceivedCoupon($v['batchCouponCode'], $userCode);
            if($v['nbrPerPerson'] != self::NO_LIMIT && $count >= $v['nbrPerPerson']) {
                continue;
            }else{
                //转化日期格式
                $v['startUsingTime'] = $this->formatDate1($v['startUsingTime']);
                $v['expireTime'] = $this->formatDate1($v['expireTime']);

                $v['discountPercent'] = $v['discountPercent'] / $this->discountRatio;
                $v['insteadPrice'] = $v['insteadPrice'] / C('RATIO');
                $v['availablePrice'] = $v['availablePrice'] / C('RATIO');
                $v['dayStartUsingTime'] = substr($v['dayStartUsingTime'], 0, 5);
                $v['dayEndUsingTime'] = substr($v['dayEndUsingTime'], 0, 5);

                $coupon[] = $v;
            }
        }

        return $coupon;
    }
}