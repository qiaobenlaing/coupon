<?php
namespace Common\Model;
use \Common\Model\BaseModel;
use \Think\Cache\Driver\Memcache;
/**
 * Created by PhpStorm.
 * User: Jihuafei
 * Date: 15-7-27
 * Time: 下午5:19
 */
class ConsumeOrderModel extends BaseModel {
    protected $tableName = 'ConsumeOrder';

    /**
     * 获得某字段的总和
     * @param array $condition 条件
     * @param string $field 字段
     * @return int $sum
     */
    public function sumOrderFiled($condition, $field) {
        $sum = $this->where($condition)->sum($field);
        return $sum ? $sum : 0;
    }

    /**
     * 获得消费次数的统计信息
     * @param array $condition 条件
     * @return array $arrayTimesData 格式：{'name' => '2次', 'y' => 1}
     */
    public function getConsumptionStatistics($condition) {
        $data = $this
            ->field(array('COUNT(orderCode)' => 'orderCount', 'clientCode'))
            ->where($condition)
            ->group('clientCode')
            ->select();
        $a = array(); // 用于存储消费次数，不重复
        $b = array(); // 用户存储不同消费次数的用户数
        foreach($data as $v) {
            if(!in_array($v['orderCount'], $a)) {
                $a[] = $v['orderCount'];
                $b[$v['orderCount']] = 1;
            } else {
                $b[$v['orderCount']] += 1;
            }
        }
        ksort($b);
        $arrayTimesData = array();
        foreach($b as $k => $v) {
            $arrayTimesData[] = array('name' => $k . '次', 'y' => $v);
        }
        return $arrayTimesData;
    }

    /**
     * 检查未完成的其他类型的订单，若24小时内未完成支付，则自动取消订单
     */
    public function cancelOtherNotPayedOrder() {
        $time = 24 * 3600; // 24小时
        $orderList = $this
            ->field(array('orderTime', 'ConsumeOrder.orderCode', 'consumeCode'))
            ->join('UserConsume ON UserConsume.orderCode = ConsumeOrder.orderCode')
            ->where(array(
                'orderType' => \Consts::ORDER_TYPE_OTHER, // 订单类型其他订单
                'ConsumeOrder.status' => array('IN', array(\Consts::PAY_STATUS_UNPAYED, \Consts::PAY_STATUS_PAYING)), // 支付状态未支付，支付中
            ))
            ->order('orderTime asc')
            ->select();

        $userConsumeMdl = new UserConsumeModel();
        foreach($orderList as $order) {
            if(time() - strtotime($order['orderTime']) > $time) {
                // 取消订单
                $userConsumeMdl->cancelBankcardPay($order['consumeCode']);
            }
        }
    }

    /**
     * 计算已经退款的金额的详细
     * @param int $refundAmount 已经退款金额，单位：分
     * @param int $shopBonus 使用的商家红包，单位：分
     * @param int $platBonus 使用的平台红包，单位：分
     * @param int $realPay 实际支付金额，单位：分
     * @return array
     */
    public function getCouponOrderRefundDetail($refundAmount, $shopBonus, $platBonus, $realPay) {
        $bankcardRefund = $platBonusRefund = $shopBonusRefund = 0;
        if($refundAmount < $realPay) {
            $bankcardRefund = $refundAmount;
        } elseif($refundAmount >= $realPay && $refundAmount < $realPay + $platBonus) {
            $bankcardRefund = $realPay;
            $platBonusRefund = $bankcardRefund - $realPay;
        } elseif($refundAmount >= $realPay + $platBonus) {
            $bankcardRefund = $realPay;
            $platBonusRefund = $platBonus;
            $shopBonusRefund = $refundAmount - $realPay - $platBonus;
        }
        return array(
            'bankcardRefund' => $bankcardRefund,
            'platBonusRefund' => $platBonusRefund,
            'shopBonusRefund' => $shopBonusRefund,
        );
    }

    /**
     * 过滤条件
     * @param array $where
     */
    public function secondFilterForConsumeStatistics(&$where) {
        if($where['province']) {
            $where['Shop.province'] = $where['province'];
            unset($where['province']);
        }
        if($where['city']) {
            $where['Shop.city'] = $where['city'];
            unset($where['city']);
        }
        if($where['district']) {
            $where['Shop.district'] = $where['district'];
            unset($where['district']);
        }
    }

    /**
     * 获得统计信息
     * @param array $filterData 条件
     * @return array
     */
    public function sumConsumeStatistics($filterData) {
        $where = $this->filterWhere(
            $filterData, array('shopName' => 'like')
        );

        //判断是否为惠圈人员
        if($_SESSION['USER']['bank_id']!=-1){
            $where['Shop.bank_id'] = $_SESSION['USER']['bank_id'];
        }


        $this->secondFilterForConsumeStatistics($where);
        $this
            ->field(array(
                'SUM(orderAmount)' => 'orderAmount',
                'SUM(realPay)' => 'realPay',
                'COUNT(consumeCode)' => 'consumeCount',
                'SUM(couponUsed)' => 'couponUsed',
                'SUM(platBonus)' => 'platBonus',
                'SUM(shopBonus)' => 'shopBonus',
                'SUM(bankCardDeduction)' => 'bankCardDeduction',
                'SUM(couponDeduction)' => 'couponDeduction',
                'SUM(cardDeduction)' => 'cardDeduction',
                'SUM(firstDeduction)' => 'firstDeduction',
            ))
            ->join('UserConsume ON UserConsume.orderCode = ConsumeOrder.orderCode')
            ->join('User ON User.userCode = ConsumeOrder.clientCode')
            ->join('Shop ON Shop.shopCode = ConsumeOrder.shopCode')
            ->where($where);
        if(isset($page) && !empty($page)){
            $this->pager($page);
        }
        $sum = $this->find();
        $temp = array('orderAmount', 'realPay', 'platBonus', 'shopBonus', 'bankCardDeduction', 'couponDeduction', 'cardDeduction', 'perCustomerTransaction', 'firstDeduction');

        unset($where['type']);
        unset($where['User.mobileNbr']);
        unset($where['Shop.city']);

        //  计算客单价
        $sum['perCustomerTransaction'] = number_format($sum['orderAmount'] / $sum['consumeCount'], '2', '.', '');
        foreach($temp as $v) {
            $sum[$v] = $sum[$v] / \Consts::HUNDRED;
        }
        return $sum;
    }

    /**
     * 获得消费统计信息
     * @param array $filterData 条件
     * @param object $page
     * @return array $list
     */
    public function listConsumeStatistics($filterData, $page) {
        $where = $this->filterWhere(
            $filterData,
            array('shopName' => 'like')
        );

        //判断是否为惠圈人员
        if($_SESSION['USER']['bank_id']!=-1){
            $where['Shop.bank_id'] = $_SESSION['USER']['bank_id'];
        }

        $this->secondFilterForConsumeStatistics($where);
        $this
            ->field(array(
                'Shop.shopCode',
            	'Shop.shopId',
            	'Shop.shopName',
                'Shop.licenseNbr',
                'Shop.hqIcbcShopNbr',
                'SUM(orderAmount)' => 'orderAmount',
                'SUM(realPay)' => 'realPay',
                'COUNT(consumeCode)' => 'consumeCount',
                'SUM(couponUsed)' => 'couponUsed',
                'SUM(platBonus)' => 'platBonus',
                'SUM(shopBonus)' => 'shopBonus',
                'SUM(bankCardDeduction)' => 'bankCardDeduction',
                'SUM(couponDeduction)' => 'couponDeduction',
                'SUM(cardDeduction)' => 'cardDeduction',
                'SUM(firstDeduction)' => 'firstDeduction',
                'popularity',
                'repeatCustomers',
                'location',
            ))
            ->join('UserConsume ON UserConsume.orderCode = ConsumeOrder.orderCode')
            ->join('User ON User.userCode = ConsumeOrder.clientCode')
            ->join('Shop ON Shop.shopCode = ConsumeOrder.shopCode')
            ->where($where)
            ->group('ConsumeOrder.shopCode');
            if(isset($page) && !empty($page)){
            	$this->pager($page);
            }
            $list = $this->select();
        $temp = array('orderAmount', 'realPay', 'platBonus', 'shopBonus', 'bankCardDeduction', 'couponDeduction', 'cardDeduction', 'perCustomerTransaction', 'firstDeduction');

        unset($where['type']);
        unset($where['User.mobileNbr']);
        unset($where['Shop.city']);

        foreach($list as $k1 => $v1) {
            $where['location'] = $v1['location'];
            //  计算客单价
            $list[$k1]['perCustomerTransaction'] = number_format($v1['orderAmount'] / $v1['consumeCount'], '2', '.', '');
            foreach($temp as $v) {
                $list[$k1][$v] = $list[$k1][$v] / \Consts::HUNDRED;
            }
        }
        return $list;
    }

    /**
     * 获得消费统计信息
     * @param array $filterData 条件
     * @return int $count
     */
    public function countConsumeStatistics($filterData) {
        $where = $this->filterWhere(
            $filterData
        );

        //判断是否为惠圈人员
        if($_SESSION['USER']['bank_id']!=-1){
            $where['Shop.bank_id'] = $_SESSION['USER']['bank_id'];
        }

        $this->secondFilterForConsumeStatistics($where);
        $count = $this
            ->join('UserConsume ON UserConsume.orderCode = ConsumeOrder.orderCode')
            ->join('User ON User.userCode = ConsumeOrder.clientCode')
            ->join('Shop ON Shop.shopCode = ConsumeOrder.shopCode')
            ->where($where)
            ->count('DISTINCT(ConsumeOrder.shopCode)');
        return $count;
    }

    /**
     * 统计数量
     * @param array $condition 条件
     * @param array $joinTable 联合的表
     * @param string $field 要统计的字段
     * @return int $count
     */
    public function getOrderCount($condition, $joinTable, $field) {
        if($joinTable) {
            foreach($joinTable as  $v) {
                $this->join($v['joinTable'] . ' ON ' . $v['joinCon'], $v['joinType']);
            }
        }
        $count = $this->where($condition)->count($field);
        return $count;
    }

    /**
     * 统计消费金额的总和
     * @param array $condition 条件
     * @return int $orderAmountSum 总消费金额，单位：元
     */
    public function sumOrderAmount($condition) {

        $orderAmountSum = $this
            ->join('UserConsume ON UserConsume.orderCode = ConsumeOrder.orderCode')
            ->join('User ON User.userCode = ConsumeOrder.clientCode')
            ->join('Shop ON Shop.shopCode = ConsumeOrder.shopCode')
            ->where($condition)
            ->sum('orderAmount');
        $orderAmountSum = $orderAmountSum / (\Consts::HUNDRED);
        return $orderAmountSum;
    }

    /**
     * 增加字段的数值
     * @param array $where 条件
     * @param string $field 字段
     * @param number $value 数值
     * @return boolean 成功返回true，失败返回false
     */
    public function incField($where, $field, $value) {
        return $this->where($where)->setInc($field, $value) !== false ? true : false;
    }

    /**
     * 申请退款24小时内未做出回应则订单取消，退款。每分钟检查一次
     */
    public function checkRefundingOrder() {
        $time = 24 * 3600; // 24小时
        $orderList = $this->field(array('refundApplyTime', 'orderCode'))->where(array('orderType' => array('neq', C('ORDER_TYPE.OTHER')), 'status' => C('ORDER_STATUS.REFUNDING'), 'refundApplyRet' => C('REFUND_APPLY_RET.NO_DEAL')))->select();
        foreach($orderList as $order) {
            if(time() - strtotime($order['refundApplyTime']) > $time) {
                $ret = $this->agreeRefund($order['orderCode']);
//                var_dump($ret);
            }
        }
    }

    /**
     * “外卖”和“堂食餐前”未支付订单30min后自动取消订单。每分钟检查一次
     */
    public function checkPayBeforeFoodOrder() {
        $time = 30 * 60; // 30分钟
        $orderList = $this
            ->field(array('orderTime', 'orderCode'))
            ->where(array(
                'orderType' => array('neq', \Consts::ORDER_TYPE_OTHER),  // 订单类型不等于其他
                'status' => \Consts::PAY_STATUS_UNPAYED, // 支付状态为未支付
                'eatPayType' => C('EAT_PAY_TYPE.BEFORE'), // 堂食订单支付方式为餐前支付
                'orderStatus' => \Consts::CATERING_ORDER_STATUS_UNORDERED, // 餐饮订单状态为待下单
            ))
            ->select();
        foreach($orderList as $order) {
            if(time() - strtotime($order['orderTime']) > $time) {
                // 撤销订单
                $ret = $this->cancelConsumeOrder($order['orderCode']);
            }
        }
    }

    /**
     * 同意退款
     * @param string $orderCode 订单编码   //由直接退款，变成需要商户同意才可以退款
     * @return array
     */
   /* public function agreeRefund2($orderCode) {
        // 设置申请退款结果为商家同意退款
        $code = $this
            ->where(array('orderCode' => $orderCode))
            ->save(array('refundApplyRet' => C('REFUND_APPLY_RET.AGREE'))) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        if($code == C('SUCCESS')) {
            // 撤销订单
            $ret = $this->cancelConsumeOrder($orderCode);
			
			//创建缓存对象
			$mem = new Memcache;
			$orderCouponCodeList=$mem->get($orderCode);	
	
			$orderCouponMdl=D('OrderCoupon');
			$refundRet = $orderCouponMdl->refundOrderCoupon($orderCouponCodeList);
			
			if($refundRet === true) {
                    //以下是关于退款的推送 
                    // 获得用户购买的优惠券信息
					$userCouponInfo = $orderCouponMdl->getOrderCouponInfo(
                        array('OrderCoupon.orderCouponCode' => array('IN', $orderCouponCodeList)),
                        array('OrderCoupon.userCode', 'OrderCoupon.orderCode', 'BatchCoupon.couponType', 'BatchCoupon.shopCode'),
                        array(
                            array('joinTable' => 'UserCoupon', 'joinCondition' => 'UserCoupon.orderCouponCode = OrderCoupon.orderCouponCode', 'joinType' => 'inner'),
                            array('joinTable' => 'BatchCoupon', 'joinCondition' => 'BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode', 'joinType' => 'inner')
                        )
                    );

                    // 获得商户的信息
                    $shopMdl = new ShopModel();
                    $shopInfo = $shopMdl->getShopInfo($userCouponInfo['shopCode'], array('shopCode', 'shopName', 'icbcShopCode', 'icbcShopName'));

                    // 获得用户信息
                    $userCode = $userCouponInfo['userCode'];
                    $userMdl = new UserModel();
                    $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('mobileNbr', 'recomNbr'));

                    // 推送退款消息
                    $couponType = $userCouponInfo['couponType'] == \Consts::COUPON_TYPE_VOUCHER ? '代金券' : '兑换券';
                    $content = str_replace(array('{{userCount}}', '{{shopName}}', '{{couponType}}'), array(count($orderCouponCodeList), $shopInfo['shopName'], $couponType), C('PUSH_MESSAGE.ORDER_COUPON_REFUND'));
                    $jpushMdl = new JpushModel(\Consts::J_PUSH_APP_KEY_C, \Consts::J_PUSH_MASTER_SECRET_C);
                    $extra = array(
                        'webUrl' => "Browser/orderCouponRefund?orderCode=" . $userCouponInfo['orderCode'],
                    );
                    $jpushMdl->jPushByAction(array($userInfo['mobileNbr']), $content, $extra, C('PUSH_ACTION.PAY_COUPON_REFUND'));
                 //   $this->ajaxSucc();
                } 
			
		
			
            return $ret;
        }
        return $this->getBusinessCode($code);
    }*/
	/**
     * 同意退款
     * @param string $orderCode 订单编码
     * @return array
     */
	public function agreeRefund($orderCode) {
        // 设置申请退款结果为商家同意退款
        $code = $this
            ->where(array('orderCode' => $orderCode))
            ->save(array('refundApplyRet' => C('REFUND_APPLY_RET.AGREE'))) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        if($code == C('SUCCESS')) {
            // 撤销订单
            $ret = $this->cancelConsumeOrder($orderCode);
            return $ret;
        }
        return $this->getBusinessCode($code);
    }
	


    /**
     * 拒绝退款
     * @param string $orderCode 订单编码
     * @return array
     */
    public function rejectRefund($orderCode) {
        $code = $this->where(array('orderCode' => $orderCode))->save(array('status' => C('ORDER_STATUS.PAYED'), 'refundApplyRet' => C('REFUND_APPLY_RET.REJECT'))) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

    /**
     * 确定订单已送达，已完成
     * @param string $orderCode 订单编码
     * @param string $orderType 订单类型
     * @return array
     */
    public function servedConsumeOrder($orderCode, $orderType = '') {
        // 设置订单状态为已送达，设置订单送达时间
        $code = $this
            ->where(array('orderCode' => $orderCode))
            ->save(array('orderStatus' => \Consts::ORDER_STATUS_SERVED, 'arrivalTime' => date('Y-m-d H:i:s'))) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');

        if(empty($orderType)) {
            // 获得订单类型
            $orderType = $this
                ->where(array('orderCode' => $orderCode))
                ->getField('orderType');
        }
        $productMdl = new ProductModel();
        // 增加外卖或者堂食的销售总量
        $productMdl->addSalesVolumes($orderCode, $orderType);
        return $this->getBusinessCode($code);
    }

    /**
     * 检查已接单且（已支付或者未支付或者等待支付）的外卖订单和堂食订单。超过接单时间3小时30分钟的，自动修改订单状态为已送达
     */
    public function checkDeliveryOrder() {
        $time = 3.5 * 3600; // 3个半小时
        $orderList = $this
            ->field(array('receivedTime', 'orderCode', 'orderType'))
            ->where(array(
                'orderType' => array('neq',  \Consts::ORDER_TYPE_OTHER),
                'status' => array('IN', array(\Consts::PAY_STATUS_PAYED, \Consts::PAY_STATUS_UNPAYED, \Consts::PAY_STATUS_CAN_NOT_PAY)),
                'orderStatus' => \Consts::ORDER_STATUS_RECEIVED
            ))
            ->select();
        foreach($orderList as $order) {
            if(time() - strtotime($order['receivedTime']) > $time) {
                $this->servedConsumeOrder($order['orderCode'], $order['orderType']);
            }
        }
    }

    /**
     * 检查已下单的外卖订单和堂食订单，商户是否在10分钟内接单，若没有接单，则自动取消订单
     */
    public function checkOrderedOrder() {
        $time = 600; // 10分钟
        $orderList = $this
            ->field(array('orderTime', 'orderCode'))
            ->where(array(
                'orderType' => array('IN', \Consts::ORDER_TYPE_NO_TAKE_OUT, \Consts::ORDER_TYPE_TAKE_OUT),
                'orderStatus' => \Consts::ORDER_STATUS_ORDERED
            ))
            ->select();
        foreach($orderList as $order) {
            if(time() - strtotime($order['orderTime']) > $time) {
                // 撤销订单
                $ret = $this->cancelConsumeOrder($order['orderCode']);
            }
        }
    }

    /**
     * 取消商家没有及时接单的外卖订单和堂食订单
     * @param string $orderCode 订单编码
     * @return mixed
     */
    public function cancelOrderedFoodOrder($orderCode) {
        // TODO 取消订单，撤销用户交易
    }

    /**
     * 申请退款
     * @param array $data {'orderCode' => 'adfad', 'refundApplyTime' => '2015-10-12 10:12:12', 'refundReason' => '其他原因', 'refundMark' => ''}
     * @return string 代码
     */
    public function applyRefund($data) {
        $rules = array(
            array('refundReason', 'require', C('ORDER.REFUND_REASON_EMPTY')),
        );
        if($this->validate($rules)->create($data)) {
            $data['refundApplyTime'] = date('Y-m-d H:i:s');
            $ret = $this->where(array('orderCode' => $data['orderCode']))->save($data);
            if($ret !== false ) {
                $userConsumeMdl = new UserConsumeModel();
                $ret = $userConsumeMdl->updateConsumeStatus(array('orderCode' => $data['orderCode'], 'status' => array('IN', array(C('UC_STATUS.PAYED'), C('UC_STATUS.UNPAYED'), C('UC_STATUS.PAYING')))), array('status' => C('UC_STATUS.REFUNDING')));
            }
            return $ret === false ? C('API_INTERNAL_EXCEPTION') : C('SUCCESS');
        } else {
            return $this->getError();
        }
    }

    /**
     * 获得所有产品订单列表
     * @param array $condition 条件
     * @return array $orderList
     */
    public function listAllProductOrder($condition) {
        $condition = $this->filterWhere($condition);
        if(empty($condition['orderType'])) {
            $condition['orderType'] = array('NEQ', C('ORDER_TYPE.OTHER'));
        }
        $orderList =  $this
            ->field(array('ConsumeOrder.orderCode', 'orderTime', 'orderNbr', 'orderAmount', 'ConsumeOrder.status', 'ConsumeOrder.clientCode', 'orderStatus', 'receiver', 'receiverMobileNbr', 'deliveryAddress', 'mealNbr', 'ConsumeOrder.eatPayType'))
            ->join('Shop ON Shop.shopCode = ConsumeOrder.shopCode')
            ->join('User ON User.userCode = ConsumeOrder.clientCode')
//            ->join('UserConsume ON UserConsume.orderCode = ConsumeOrder.orderCode', 'LEFT')
            ->where($condition)
            ->order('orderTime desc')
            ->select();
        foreach($orderList as &$order) {
            $order['orderAmount'] = number_format($order['orderAmount'] / C('RATIO'), 2, '.', '');
        }
        return $orderList;
    }

    /**
     * 获得产品订单列表
     * @param array $condition 条件
     * @param object $page
     * @return array $orderList
     */
    public function listProductOrder($condition, $page) {
        $condition = $this->filterWhere($condition);
        $condition['orderType'] = array('NEQ', C('ORDER_TYPE.OTHER'));
        $condition['isFinishOrder'] = C('YES');
        $orderList =  $this
            ->field(array('ConsumeOrder.orderCode', 'orderTime', 'orderNbr', 'orderAmount', 'ConsumeOrder.status', 'orderStatus', 'isFinishOrder', 'shopName', 'Shop.logoUrl', 'nickName', 'User.avatarUrl', 'User.mobileNbr' => 'userMobileNbr', 'deduction', 'realPay', 'platBonus', 'shopBonus', 'UserConsume.status' => 'userConsumeStatus', 'isCard', 'consumeCode', 'couponUsed', 'bankCardDeduction', 'payType', 'usedCardCode', 'usedUserCouponCode'))
            ->join('Shop ON Shop.shopCode = ConsumeOrder.shopCode')
            ->join('User ON User.userCode = ConsumeOrder.clientCode')
            ->join('UserConsume ON UserConsume.orderCode = ConsumeOrder.orderCode', 'LEFT')
            ->where($condition)
            ->order('orderTime desc')
            ->pager($page)
            ->select();
        foreach($orderList as &$order) {
            $order['orderAmount'] = number_format($order['orderAmount'] / C('RATIO'), 2, '.', '');
        }
        return $orderList;
    }

    /**
     * 编辑外卖订单
     * @param array $data 订单详情
     * @return array 例：{'code' => '50000'}或者{'code' => '50000','orderCode' => '1234213adsf43242'}
     */
    public function editCouponOrder($data) {
        $rules = array(
            array('clientCode', 'require', C('USER.USER_CODE_EMPTY')),
            array('shopCode', 'require', C('SHOP.SHOP_CODE_EMPTY')),
            array('status', 'require', C('ORDER.STATUS_EMPTY')),
            array('orderAmount', 'require', C('ORDER.AMOUNT_EMPTY')),
            array('orderAmount', 'is_numeric', C('ORDER.AMOUNT_ERROR'), 0, 'function'),
            array('orderType', 'require', C('ORDER.ORDER_TYPE_EMPTY')),
        );
        if($this->validate($rules)->create($data)) {
            if($data['orderCode']) {
                $code = $this->where(array('orderCode' => $data['orderCode']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
                return $this->getBusinessCode($code);
            } else {
                $data['orderCode'] = $this->create_uuid();
                $data['orderNbr'] = $this->createOrderNbr($data['shopCode']);
                $data['orderTime'] = date('Y-m-d H:i:s');
                $data['status'] = \Consts::PAY_STATUS_UNPAYED;
                $data['orderType'] = \Consts::ORDER_TYPE_COUPON;
                $data['orderStatus'] = \Consts::ORDER_STATUS_ORDERED;
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
                return array(
                    'code' => $code,
                    'orderCode' => $code == C('SUCCESS') ? $data['orderCode'] : '',
                    'orderNbr' => $code == C('SUCCESS') ? $data['orderNbr'] : '',
                );
            }
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 编辑活动订单
     * @param array $condition 条件，一维索引数组
     * @param array $data 订单详情
     * @return array 例：{'code' => '50000'}或者{'code' => '50000','orderCode' => '1234213adsf43242'}
     */
    public function editActOrder($condition, $data) {
        $rules = array(
            array('clientCode', 'require', C('USER.USER_CODE_EMPTY')),
            array('shopCode', 'require', C('SHOP.SHOP_CODE_EMPTY')),
            array('orderAmount', 'require', C('ORDER.AMOUNT_EMPTY')),
            array('orderAmount', 'is_numeric', C('ORDER.AMOUNT_ERROR'), 0, 'function'),
            array('receiver', 'require', C('ORDER.RECEIVER_EMPTY')),
            array('receiverMobileNbr', 'require', C('ORDER.RECEIVER_MOBILE_NBR_EMPTY')),
        );
        if($this->validate($rules)->create($data)) {
            if($condition) {
                $code = $this->where($condition)->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
                return $this->getBusinessCode($code);
            } else {
                $data['orderCode'] = $this->create_uuid(); // 主键
                $data['orderNbr'] = $this->createOrderNbr($data['shopCode']); // 订单号
                $data['orderTime'] = date('Y-m-d H:i:s'); // 订单时间
                $data['status'] = \Consts::PAY_STATUS_UNPAYED; // 支付状态为未支付
                $data['orderType'] = \Consts::ORDER_TYPE_ACT; // 订单类型为活动订单
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
                return array(
                    'code' => $code,
                    'orderCode' => $code == C('SUCCESS') ? $data['orderCode'] : '',
                    'orderNbr' => $code == C('SUCCESS') ? $data['orderNbr'] : '',
                );
            }
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 编辑外卖订单
     * @param array $data 订单详情
     * @return array 例：{'code' => '50000'}或者{'code' => '50000','orderCode' => '1234213adsf43242'}
     */
    public function editTakeoutOrder($data) {
        $rules = array(
            array('clientCode', 'require', C('USER.USER_CODE_EMPTY')),
            array('shopCode', 'require', C('SHOP.SHOP_CODE_EMPTY')),
            array('status', 'require', C('ORDER.STATUS_EMPTY')),
            array('orderAmount', 'require', C('ORDER.AMOUNT_EMPTY')),
            array('orderAmount', 'is_numeric', C('ORDER.AMOUNT_ERROR'), 0, 'function'),
            array('orderType', 'require', C('ORDER.ORDER_TYPE_EMPTY')),
            array('orderStatus', 'require', C('ORDER.ORDER_STATUS_EMPTY')),
            array('receiver', 'require', C('ORDER.RECEIVER_EMPTY')),
            array('receiverMobileNbr', 'require', C('ORDER.RECEIVER_MOBILE_NBR_EMPTY')),
            array('deliveryAddress', 'require', C('ORDER.DELIVERY_ADDRESS_EMPTY')),
        );
        if($this->validate($rules)->create($data)) {
            if($data['orderCode']) {
                $code = $this->where(array('orderCode' => $data['orderCode']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
                return $this->getBusinessCode($code);
            } else {
                $data['orderCode'] = $this->create_uuid(); // 主键
                $data['orderNbr'] = $this->createOrderNbr($data['shopCode']); // 订单号
                $data['orderTime'] = date('Y-m-d H:i:s'); // 订单时间
                $data['status'] = \Consts::PAY_STATUS_UNPAYED; // 支付状态为未支付
                $data['orderType'] = \Consts::ORDER_TYPE_TAKE_OUT; // 订单类型为外卖订单
                $data['orderStatus'] = \Consts::ORDER_STATUS_UNORDERED; // 订单状态为未下单
                $data['mealNbr'] = $this->createMealNbr($data['shopCode']); // 堂食餐号
                $data['eatPayType'] = C('EAT_PAY_TYPE.BEFORE'); // 支付类型，餐前支付
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
                return array(
                    'code' => $code,
                    'orderCode' => $code == C('SUCCESS') ? $data['orderCode'] : '',
                );
            }
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 编辑堂食订单
     * @param array $data 订单详情
     * @return array 例：{'code' => '50000'}或者{'code' => '50000','orderCode' => '1234213adsf43242'}
     */
    public function editNotTakeoutOrder($data) {
        $rules = array(
            array('clientCode', 'require', C('USER.USER_CODE_EMPTY')),
            array('shopCode', 'require', C('SHOP.SHOP_CODE_EMPTY')),
            array('status', 'require', C('ORDER.STATUS_EMPTY')),
            array('orderAmount', 'require', C('ORDER.AMOUNT_EMPTY')),
            array('orderAmount', 'is_numeric', C('ORDER.AMOUNT_ERROR'), 0, 'function'),
            array('orderType', 'require', C('ORDER.ORDER_TYPE_EMPTY')),
        );
        if($this->validate($rules)->create($data)) {
            if($data['orderCode']) {
                $code = $this->where(array('orderCode' => $data['orderCode']))->save($data)  !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
                return $this->getBusinessCode($code);
            } else {
                $data['orderCode'] = $this->create_uuid(); // 订单编码
                $data['orderNbr'] = $this->createOrderNbr($data['shopCode']); // 订单号
                $data['orderTime'] = date('Y-m-d H:i:s'); // 订单时间
                $data['mealNbr'] = $this->createMealNbr($data['shopCode']); // 堂食餐号
                $data['orderType'] = \Consts::ORDER_TYPE_NO_TAKE_OUT; // 订单类型为堂食订单
                $data['orderStatus'] = \Consts::ORDER_STATUS_UNORDERED; // 订单状态为未下单
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
                return array(
                    'code' => $code,
                    'orderCode' => $code == C('SUCCESS') ? $data['orderCode'] : '',
                );
            }
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 设置堂食的餐号
     * @param string $shopCode 商家编码
     * @return string $mealNbr 餐号
     */
    private function createMealNbr($shopCode) {
        $serialNbr = $this->where(array('orderTime' => array('BETWEEN', array(date('Y-m-d 00:00:00', time()), date('Y-m-d 23:59:59', time()))), 'shopCode' => $shopCode, 'orderType' => array('IN', array(C('ORDER_TYPE.NO_TAKE_OUT'), C('ORDER_TYPE.TAKE_OUT')))))->count('orderCode');
        $serialNbr = sprintf('%03d', $serialNbr + 1);
        $mealNbr = $serialNbr;
        return $mealNbr;
    }

    /**
     * 第二次过滤条件
     * @param array $where
     * @return array $where
     */
    public function secondFilterWhere(&$where) {
        if($where['shopCode']){
            $where['Shop.shopCode'] = $where['shopCode'];
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

        if(isset($where['city'])){
            if($where['city'] == '义乌市') {
                $where['Shop.district'] = $where['city'];
            } else {
                $where['Shop.city'] = $where['city'];
            }
            unset($where['city']);
        }

        return $where;
    }

    /**
     * 管理端获得订单列表
     * @param array $filterData
     * @param object $page 偏移值
     * @return null|array 如果为空，返回null；如果不为空，返回二维数组array(array());
     */
    public function listConsumeOrder($filterData, $page) {
        $where = $this->filterWhere(
            $filterData,
            array('shopName' => 'like','mobileNbr' => 'like', 'orderNbr' => 'like', 'UserConsume.status' => 'not in'),
            $page);
        $where = $this->secondFilterWhere($where);

        //判断是否为惠圈人员
        if($_SESSION['USER']['bank_id']!=-1){
            $where['Shop.bank_id'] = $_SESSION['USER']['bank_id'];
        }

        $consumeOrderList =  $this
            ->field(array('orderTime', 'orderNbr', 'orderAmount', 'shopName', 'Shop.logoUrl', 'nickName', 'User.avatarUrl', 'User.mobileNbr' => 'userMobileNbr', 'orderType',"User.bank_id", 'orderStatus', 'ConsumeOrder.orderCode', 'orderConfirm', 'ConsumeOrder.status'))
            ->join('Shop ON Shop.shopCode = ConsumeOrder.shopCode', 'left')
            ->join('User ON User.userCode = ConsumeOrder.clientCode', 'left')
            ->where($where)
            ->order('orderTime desc')
            ->pager($page)
            ->select();
        // 'UserConsume.status' => 'userConsumeStatus', 'isCard', 'consumeCode', 'couponUsed', 'bankCardDeduction', 'payType', 'usedCardCode', 'deduction', 'realPay', 'platBonus', 'shopBonus', 'usedUserCouponCode', 'userCouponCode',
        $temArray = array('orderAmount');
        foreach($consumeOrderList as $k => $v) {
//            $consumeOrderList[$k]['realPay'] = $consumeOrderList[$k]['orderAmount'];
            foreach($temArray as $item) {
                $consumeOrderList[$k][$item] = number_format($consumeOrderList[$k][$item] / C('RATIO'), 2, '.', '');
            }
        }
        return $consumeOrderList;
    }

    /**
     * 获得支付记录总数
     * @param array $filterData
     * @return int $count
     */
    public function countConsumeOrder($filterData) {
        $where = $this->filterWhere(
            $filterData,
            array('shopName' => 'like', 'mobileNbr' => 'like', 'orderNbr' => 'like', 'UserConsume.status' => 'not in')
        );

        //判断是否为惠圈人员
        if($_SESSION['USER']['bank_id']!=-1){
            $where['Shop.bank_id'] = $_SESSION['USER']['bank_id'];
        }

        $where = $this->secondFilterWhere($where);
        $count = $this
            ->join('Shop ON Shop.shopCode = ConsumeOrder.shopCode', 'left')
            ->join('User ON User.userCode = ConsumeOrder.clientCode')
            ->where($where)
            ->count('ConsumeOrder.orderCode');
        return intval($count);
    }

    /**
     * 统计订单数量
     * @param array $filterData
     * @return null|int 如果为空，返回null；如果不为空，返回数字;
     */
    public function countOrder($filterData) {
        $where = $this->filterWhere($filterData);
        return $this
            ->join('User ON User.userCode = ConsumeOrder.clientCode')
            ->where($where)
            ->count('orderCode');
    }

    /**
     * 修改订单信息
     * @param array $where 查询条件。例{'orderCode' => 'xxxx', ...}
     * @param array $data 要更新的字段与对应值。例{'status' => '1', ...}
     * @return boolean $ret 成功返回true，失败返回false
     */
    public function updateConsumeOrder($where, $data) {
        $date = date('Y-m-d H:i:s');
        if(empty($data['orderStatus'])) {
            unset($data['orderStatus']);
        } else {
            switch($data['orderStatus']) {
                case C('FOOD_ORDER_STATUS.RECEIVED'): // 订单已接单
                    $data['receivedTime'] = $date; // 设置接单时间
                    break;
                case C('FOOD_ORDER_STATUS.DELIVERED'): // 订单已配送
                    $data['deliveryTime'] = $date; // 设置配送时间
                    break;
                case C('FOOD_ORDER_STATUS.SERVED'): // 订单已送达
                    $data['arrivalTime'] = $date; // 设置送达时间
                    break;
            }
        }

        if(empty($data['status']) && $data['status'] !== 0) {
            unset($data['status']);
        } elseif($data['status'] == C('UC_STATUS.REFUNDED')) { // 订单支付状态为已退款
            $data['refundTime'] =  date('Y-m-d H:i:s'); // 设置退款时间
        }
        $ret = $this->where($where)->save($data) !== false ? true : false;
        return $ret;
    }

    /**
     * 取消订单
     * @param string $orderCode 订单编码
     * @return array
     */
    public function cancelConsumeOrder($orderCode) {
        // 获得订单详情
        $orderInfo = $this->getOrderInfo(array('orderCode' => $orderCode));

        // 获得订单当前的支付记录
        $userConsumeMdl = new UserConsumeModel();
        $userConsumeInfo = $userConsumeMdl->getCurrConsumeInfoByOrderCode($orderCode);

        if(! empty($userConsumeInfo['consumeCode'])) { // 若存在当前支付记录，执行退款
            $refundRet = $userConsumeMdl->refund($userConsumeInfo['consumeCode']);
            $cancelData = array(
                'orderStatus' => \Consts::ORDER_STATUS_CANCELED, // 设置订单状态为已取消订单
                'cancelTime' => date('Y-m-d H:i:s'), // 设置订单取消时间
                'status' => \Consts::PAY_STATUS_REFUNDED, // 设置订单支付状态为已退款
                'refundTime' =>  date('Y-m-d H:i:s'), // 设置退款时间
                'refundApplyRet' => ! empty($orderInfo['refundReason']) ? C('REFUND_APPLY_RET.AGREE') : C('REFUND_APPLY_RET.NO_DEAL'), // // 退款理由不为空时，是用户申请退款，设置订单的申请退款结果为商户同意退款
            );
        } else {
            $refundRet = array('code' => C('SUCCESS'));
            $cancelData = array(
                'orderStatus' => \Consts::ORDER_STATUS_CANCELED, // 设置订单状态为已取消订单
                'cancelTime' => date('Y-m-d H:i:s'), // 设置订单取消时间
                'status' => \Consts::PAY_STATUS_CANCELED, // 设置订单支付状态为已退款
            );
        }

        if($refundRet['code'] ===  C('SUCCESS')) {
            if($orderInfo['orderStatus'] == \Consts::ORDER_STATUS_SERVED && $orderInfo['status'] == \Consts::PAY_STATUS_PAYED) { // 订单状态为已送达并且支付状态为已付款
                // 减少外卖或者堂食的销售总量
                $productMdl = new ProductModel();
                $productMdl->decSalesVolumes($orderCode, $orderInfo['orderType']);
            }

            // 保存订单信息
            $code = $this->where(array('orderCode' => $orderCode))->save($cancelData) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        } else {
            $code = C('API_INTERNAL_EXCEPTION');
        }
        return $this->getBusinessCode($code);
    }

    /**
     * 获得订单详情
     * @param $condition
     * @param array $field
     * @param array $joinTableArr
     * @param string $order
     * @return mixed
     */
    public function getOrderInfo($condition = array(), $field = array(), $joinTableArr = array(), $order = '') {
        if(empty($field)){
            $field = array('ConsumeOrder.*');
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
     * 生成订单号，20位
     * @param string $shopCode 商家编码
     * @return string $orderNbr
     */
    public function createOrderNbr($shopCode) {
        $shopMdl = new ShopModel();
        // 获得商家信息
        $shopInfo = $shopMdl->getShopInfo($shopCode, array('shopId'));

        $minNbr = 1;
        $maxNbr = 9999;
        $serialNbr = sprintf('%04d', mt_rand($minNbr, $maxNbr) + 1);
//        $serialNbr = $this->where(array('orderTime' => array('BETWEEN', array(date('Y-m-d 00:00:00', time()), date('Y-m-d 23:59:59', time()))), 'shopCode' => $shopCode))->count('orderCode');
//        $serialNbr = sprintf('%04d', $serialNbr + 1);
        $orderNbr = $shopInfo['shopId'] . date('ymd', time()) . $serialNbr; // 商户号+年月日+4位流水号
        $hasOrder = $this->hasOrder(array('orderNbr' => $orderNbr));
        if($hasOrder == false ) {
            return $orderNbr;
        } else {
            return $this->createOrderNbr($shopCode);
        }
    }

    /**
     * 判断是否有该订单
     * @param array $condition
     * @return boolean 有则返回true，没有则返回false
     */
    public function hasOrder($condition) {
        $orderCode = $this->where($condition)->getField('orderCode');
        return $orderCode ? true : false;
    }

    /**
     * 生成顾客消费订单
     * @param string $userCode 用户编码
     * @param number $price 消费金额 单位：分
     * @param string $shopCode 商家编码
     * @param int $payType 1-在线支付；2-POS支付；3-现金支付；4-未选择支付方式；5-券支付
     * @param int $orderType 订单类型。10-其他订单；20-堂食订单；21-外卖订单；
     * @param string $orderConfirm 是否确认。
     * @return array $ret
     */
    public function addConsumeOrder($userCode, $price, $shopCode, $payType = 0, $orderType = 10, $orderConfirm = \Consts::NO) {
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
            // 添加新订单
            $orderInfo = array(
                'orderCode' => $this->create_uuid(),
                'orderNbr' => $this->createOrderNbr($shopCode),
                'shopCode' => $shopCode,
                'clientCode' => $userCode,
                'orderTime' => date('Y-m-d H:i:s', time()),
                'status' => \Consts::PAY_STATUS_UNPAYED, // 支付状态：未支付
                'orderAmount' => $price,
                'orderType' => $orderType,
                'orderConfirm' => $orderConfirm,
            );
            if($orderType == C('ORDER_TYPE.TAKE_OUT')) {
                $orderInfo['orderStatus'] = C('FOOD_ORDER_STATUS.ORDERED');
            }
            if(in_array($payType, array(C('UC_PAY_TYPE.CASH'), C('UC_PAY_TYPE.COUPON')))){
                $orderInfo['status'] = \Consts::PAY_STATUS_PAYED;
            }
            $ret = $this->add($orderInfo);
            if($ret === false) {
                return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
            } else {
                $ret = $this->getBusinessCode(C('SUCCESS'));
                $ret['orderCode'] = $orderInfo['orderCode'];
                $ret['orderNbr'] = $orderInfo['orderNbr'];
                return $ret;
            }
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 查找是否有符合条件的未完成订单
     * @param 查询条件 $condition
     * @return array
     */
    public function isConsumeOrder($condition){
        $map['orderStatus']  = array('not in',array(C('FOOD_ORDER_STATUS.SERVED'),C('FOOD_ORDER_STATUS.CANCELED')));
        $map['orderType'] = C('ORDER_TYPE.NO_TAKE_OUT');
        $ret = $this
        ->join('Shop ON Shop.shopCode = ConsumeOrder.shopCode')
        ->join('User ON User.userCode = ConsumeOrder.clientCode')
        ->where($map)
        ->where($condition)
        ->find();
        return $ret;
    }

    /**
     * 获取订单列表
     * @param array $condition
     * @param array $field
     * @param array $joinTableArr
     * @param string $order
     * @param int $limit
     * @param int $page
     * @return array
     */
    public function getConsumeOrderList($condition = array(), $field = array(), $joinTableArr = array(), $order = '', $limit = 0, $page = 0){
        if(empty($field)){
            $field = array('ConsumeOrder.*');
        }
        $this->field($field);
        if($condition){
           $this->where($condition);
        }
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
        $orderList = $this->select();
        return $orderList;
    }

    /**
     * 获取订单列表
     * @param array $condition
     * @param array $joinTableArr
     * @return array
     */
    public function getConsumeOrderCount($condition = array(), $joinTableArr = array()){
        if($condition){
            $this->where($condition);
        }
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        $orderCount = $this->count('ConsumeOrder.orderCode');;
        return $orderCount;
    }
}