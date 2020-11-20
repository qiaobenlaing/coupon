<?php
namespace Common\Model;
use Think\Model;
/**
 * userCoupon表
 * @author jihuafei
 */
class UserCouponModel extends BaseModel {
    const NO_LIMIT = 0;
    protected $tableName = 'UserCoupon';
    private $discountRatio = 10;

    /**
     * 添加，修改用户优惠券信息
     * @param array $condition 条件
     * @param array $data 数据
     * @return array
     */
    public function editUserCoupon($condition, $data) {
        if($condition) {
            $code = $this->where($condition)->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        } else {
            $data['userCouponCode']  = $this->create_uuid();
            $data['applyTime'] = date('Y-m-d H:i:s');
            if(empty($data['userCouponNbr'])) {
                $data['userCouponNbr'] = $this->setUserCouponNbr($data['batchCouponCode']);
            }
            $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        }
        return $this->getBusinessCode($code);
    }

    /**
     * 获得用户优惠券列表
     * @param array $where 条件
     * @param array $field 字段
     * @return array
     */
    public function getUserCouponList($where, $field) {
        $where = $this->filterWhere($where);
        $userCouponList = $this
            ->field($field)
            ->join('BatchCoupon on BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
            ->join('Shop on Shop.shopCode = BatchCoupon.shopCode')
            ->where($where)
            ->select();
        foreach($userCouponList as &$userCouponInfo) {
            if($userCouponInfo['status'] != C('USER_COUPON_STATUS.USED')) {
                if($userCouponInfo['validityPeriod'] > 0){
                    $time = strtotime($userCouponInfo['applyTime']) + $userCouponInfo['validityPeriod'] * 86400;
                    if(time() > $time){
                        // 已过期
                        $userCouponInfo['status'] = C('USER_COUPON_STATUS.EXPIRED');
                    }
                }elseif($userCouponInfo['validityPeriod'] < 0){
                    if($userCouponInfo['expireTime'] < date('Y-m-d h:i:s', time())){
                        // 已过期
                        $userCouponInfo['status'] = C('USER_COUPON_STATUS.EXPIRED');
                    }
                }else{
                    if (strtotime($userCouponInfo['startUsingTime']) <= time() && strtotime($userCouponInfo['expireTime']) >= time() && date('H:i:s', time()) >= $userCouponInfo['dayStartUsingTime'] && date('H:i:s', time()) <= $userCouponInfo['dayEndUsingTime']) {
                        // 可使用
                        $userCouponInfo['status'] = C('USER_COUPON_STATUS.ACTIVE');
                    } else {
                        // 待使用
                        $userCouponInfo['status'] = C('USER_COUPON_STATUS.TOBE_ACTIVE');
                    }
                }
            }

            $userCouponInfo['insteadPrice'] = $userCouponInfo['insteadPrice'] / C('RATIO');
            $userCouponInfo['availablePrice'] = $userCouponInfo['availablePrice'] / C('RATIO');
            $userCouponInfo['payPrice'] = $userCouponInfo['payPrice'] / C('RATIO');
            $userCouponInfo['discountPercent'] = $userCouponInfo['discountPercent'] / $this->discountRatio;
            $userCouponInfo['startUsingTime'] = $this->formatDate1($userCouponInfo['startUsingTime']);
            $userCouponInfo['expireTime'] = $this->formatDate1($userCouponInfo['expireTime']);
            $userCouponInfo['dayStartUsingTime'] = substr($userCouponInfo['dayStartUsingTime'], 0, 5);
            $userCouponInfo['dayEndUsingTime'] = substr($userCouponInfo['dayEndUsingTime'], 0, 5);
            $userCouponInfo['city'] = $this->formatCity($userCouponInfo['city']);
        }
        return $userCouponList;
    }

    /**
     * 判断是否有该优惠券
     * @param array $condition 条件
     * @return boolean 有返回true，没有返回false
     */
    public function hasUserCoupon($condition) {
        $userCouponCode = $this->where($condition)->getField('userCouponCode');
        return $userCouponCode ? true : false;
    }

    /**
     * 回滚优惠券的使用行为
     * @param string $consumeCode 支付编码
     * @return boolean 成功返回true，失败返回false
     */
    public function rollbackUserCouponAction($consumeCode) {
        return $this->where(array('consumeCode' => $consumeCode))->save(array('status' => C('USER_COUPON_STATUS.ACTIVE'), 'consumeCode' => '')) !== false ? true : false;
    }

    public function getUserConsumeCouponList($consumeCode, $field){
        return $this->field($field)->where(array('consumeCode'=>$consumeCode))->select();
    }

    /**
     * 获得用户在该商户拥有的某种类型的优惠券
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param string $couponType 优惠券类型。1-N元购；3-抵扣券；4-折扣券；5-实物券；6-体验券
     * @param int $isDistinct 是否获取不同的值。1-是，0-否
     * @return array $userCouponList 用户优惠券。二维数组
     */
    public function getUserDiffTypeCoupon($userCode, $shopCode, $couponType, $isDistinct) {
        if($isDistinct == C('YES')) {
            $field = array('DISTINCT(UserCoupon.batchCouponCode)' => 'batchCouponCode', 'availablePrice', 'insteadPrice', 'discountPercent');
        } else {
            $field = array('UserCoupon.batchCouponCode', 'availablePrice', 'insteadPrice', 'discountPercent');
        }
        $sql = '(validityPeriod = 0) OR (BatchCoupon.startUsingTime <= NOW() AND BatchCoupon.expireTime >= NOW() AND BatchCoupon.validityPeriod = -1) OR ((' . time() . ' - UNIX_TIMESTAMP(UserCoupon.applyTime)) / 86400 <= BatchCoupon.validityPeriod  AND BatchCoupon.validityPeriod > 0)';
        $userCouponList = $this
            ->field($field)
            ->where(array(
                'userCode' => $userCode,
                'shopCode' => $shopCode,
                'couponType' => $couponType,
                'UserCoupon.status' => C('USER_COUPON_STATUS.ACTIVE'),
                '_string' => $sql
            ))
            ->join('BatchCoupon ON BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
            ->select();
        return $userCouponList;
    }

    /**
     * 获得用户拥有过优惠券的商户编码
     * @param string $userCode 用户编码
     * @return array $shopCodeList 一维数组
     */
    public function listHadCouponShopCode($userCode) {
        $shopList = $this
            ->field(array('DISTINCT(shopCode)'))
            ->where(array('userCode' => $userCode))
            ->join('BatchCoupon ON BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
            ->select();
        $shopCodeList = array();
        foreach($shopList as $v) {
            $shopCodeList[] = $v['shopCode'];
        }
        return $shopCodeList;
    }

    /**
     * 统计优惠券被领取的数量
     * @param string $batchCouponCode 优惠券编码
     * @return int $nbr 被领用数量
     */
    public function countCouponTakenNbr($batchCouponCode) {
        $nbr = $this->where(array('batchCouponCode' => $batchCouponCode))->count('userCouponCode');
        return $nbr;
    }

    /**
     * 执行送推荐人优惠券的一系列动作
     * @param string $recomNbr 推荐码
     * @return boolean 推荐人不存在或者已发放或者发放成功返回true，未发放返回false
     */
    public function sendRecomClientCoupon($recomNbr) {
        $systemParamMdl = new SystemParamModel();
        $paramInfo = $systemParamMdl->getParamValue('inviteStatus');
        if($paramInfo['value'] == C('NO')) {
            return true;
        }
        $userMdl = new UserModel();
        $recomeClientInfo = $userMdl->getUserInfo(array('inviteCode' => $recomNbr), array('userCode'));
        $userCode = $recomeClientInfo['userCode'];
        if($recomeClientInfo) {
            $userCouponInfo = $this->field(array('userCouponCode'))->join('BatchCoupon ON BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')->where(array('userCode' => $userCode, 'couponType' => C('COUPON_TYPE.SEND_INVITER')))->find();
            if(empty($userCouponInfo)) {
                return $this->addSendInviterCoupon($userCode) !== false ? true : false;
            }
            return true;
        }
        return true;
    }

    /**
     * 获得因邀请码奖励的优惠券总额
     * @param string $userCode 用户编码
     * @return int
     */
    public function countUserRewardCoupon($userCode) {
        $userRewardCouponAmount = $this
            ->join('BatchCoupon ON BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
            ->where(array('userCode' => $userCode, 'couponType' => C('COUPON_TYPE.SEND_INVITER')))
            ->sum('insteadPrice');
        return $userRewardCouponAmount / C('RATIO');
    }

    /**
     * 向邀请人送4元优惠券
     * @param string $userCode 用户编码
     * @return boolean
     */
    public function addSendInviterCoupon($userCode) {
        $batchCouponMdl = new BatchCouponModel();
        $batchCouponInfo = $batchCouponMdl->getSendInviterCoupon();
        if($batchCouponInfo) {
            $valTime = $this->calValidTime($batchCouponInfo);
            $batchCouponCode = $batchCouponInfo['batchCouponCode'];
            $userCouponCode = $this->create_uuid();
            $userCouponData = array(
                'userCouponCode' => $userCouponCode,
                'userCode' => $userCode,
                'batchCouponCode' => $batchCouponCode,
                'status' => C('USER_COUPON_STATUS.ACTIVE'),
                'applyTime' => date('Y-m-d H:i:s', time()),
                'userCouponNbr' => $this->setUserCouponNbr($batchCouponCode),
                'userCouponType' => $batchCouponInfo['couponType'],
                'startUsingTime' => $valTime['startUsingTime'],
                'expireTime' => $valTime['expireTime'],
            );
            $ret = $this->add($userCouponData);
            if($ret !== false) {
                // 添加优惠券领用消息
                $batchCouponInfo = $batchCouponMdl->getCouponInfo($batchCouponCode);
                $msgInfo = array(
                    'msgCode' => $this->create_uuid(),
                    'title' => C('MSG_TITLE_TDL.GRAB_COUPON'),
                    'content' => C('COUPON_MSG_TDL.GRAB'),
                    'createTime' => date('Y-m-d H:i:s'),
                    'senderCode' => $batchCouponInfo['shopCode'],
                    'type' => C('MESSAGE_TYPE.COUPON'),
                    'userCouponCode' => $userCouponData['userCouponCode'],
                );
                $msgMdl = new MessageModel();
                $msgMdl ->addMsg($msgInfo, $userCode);
                return array('code' => C('SUCCESS'), 'userCouponCode' => $userCouponCode);
            } else {
                return array('code' => C('API_INTERNAL_EXCEPTION'), 'userCouponCode' => '');
            }
        }
    }

    /**
     * 为新用户添加奖励优惠券（10元抵扣券）
     * @param string $userCode 用户编码
     * @return array {'code', 'userCouponCode'}
     */
    public function addNewClientCoupon($userCode) {
        $batchCouponMdl = new BatchCouponModel();
        $batchCouponInfo = $batchCouponMdl->getNewClientCoupon();
        if($batchCouponInfo) {
            $systemParamMdl = new SystemParamModel();
            $isOpenRegAct = $systemParamMdl->getParamValue('isOpenRegAct');
            if($isOpenRegAct['value'] == C('NO')) {
                return array('code' => C('COUPON.REG_COUPON_CAN_NOT_BE_SENT'), 'userCouponCode' => '');
            }
            $valTime = $this->calValidTime($batchCouponInfo);
            $batchCouponCode = $batchCouponInfo['batchCouponCode'];
            $userCouponCode = $this->create_uuid();
            $userCoupon = array(
                'userCouponCode' => $userCouponCode,
                'userCode' => $userCode,
                'batchCouponCode' => $batchCouponCode,
                'status' => C('USER_COUPON_STATUS.ACTIVE'),
                'applyTime' => date('Y-m-d H:i:s', time()),
                'userCouponNbr' => $this->setUserCouponNbr($batchCouponCode),
                'userCouponType' => $batchCouponInfo['couponType'],
                'startUsingTime' => $valTime['startUsingTime'],
                'expireTime' => $valTime['expireTime'],
            );
            $ret = $this->add($userCoupon);
            if($ret !== false) {
                // 添加优惠券领用消息
                $batchCouponInfo = $batchCouponMdl->getCouponInfo($batchCouponCode);
                $msgInfo = array(
                    'msgCode' => $this->create_uuid(),
                    'title' => C('MSG_TITLE_TDL.GRAB_COUPON'),
                    'content' => C('COUPON_MSG_TDL.GRAB'),
                    'createTime' => date('Y-m-d H:i:s'),
                    'senderCode' => $batchCouponInfo['shopCode'],
                    'type' => C('MESSAGE_TYPE.COUPON'),
                    'userCouponCode' => $userCoupon['userCouponCode'],
                );
                $msgMdl = new MessageModel();
                $msgMdl ->addMsg($msgInfo, $userCode);
                return array('code' => C('SUCCESS'), 'userCouponCode' => $userCouponCode);
            } else {
                return array('code' => C('API_INTERNAL_EXCEPTION'), 'userCouponCode' => '');
            }
        } else {
            return array('code' => C('COUPON.NO_REG_SEND_COUPON'), 'userCouponCode' => '');
        }
    }

    /**
     * 给用户添加商家赠送的优惠券
     * @param string $batchCouponCode 优惠券编码
     * @param string $userCode 用户编码
     * @return array
     */
    public function addUserSendCoupon($batchCouponCode, $userCode) {
        $data = array(
            'userCouponCode' => $this->create_uuid(),
            'userCode' => $userCode,
            'batchCouponCode' => $batchCouponCode,
            'applyTime' => date('Y-m-d H:i:s'),
            'status' => C('USER_COUPON_STATUS.ACTIVE'),
            'userCouponNbr' => $this->setUserCouponNbr($batchCouponCode)
        );
        $ret = $this->add($data);
        if($ret !== false) {
            // 添加赠送了优惠券的用户消息
            $batchCouponMdl = new BatchCouponModel();
            $batchCouponInfo = $batchCouponMdl->getCouponInfo($batchCouponCode);
            $msgInfo = array(
                'msgCode' => $this->create_uuid(),
                'title' => C('MSG_TITLE_TDL.GRAB_COUPON'),
                'content' => C('COUPON_MSG_TDL.GRAB'),
                'createTime' => date('Y-m-d H:i:s'),
                'senderCode' => $batchCouponInfo['shopCode'],
                'type' => C('MESSAGE_TYPE.COUPON'),
                'userCouponCode' => $data['userCouponCode'],
            );
            $msgMdl = new MessageModel();
            $ret = $msgMdl ->addMsg($msgInfo, $userCode);

//            $name = '优惠券';
//            if($batchCouponInfo['couponType'] == C('COUPON_TYPE.N_PURCHASE')){
//                $name = C('COUPON_TYPE_NAME.N_PURCHASE');
//            }elseif($batchCouponInfo['couponType'] == C('COUPON_TYPE.REDUCTION')){
//                $name = C('COUPON_TYPE_NAME.REDUCTION');
//            }elseif($batchCouponInfo['couponType'] == C('COUPON_TYPE.DISCOUNT')){
//                $name = C('COUPON_TYPE_NAME.DISCOUNT');
//            }elseif($batchCouponInfo['couponType'] == C('COUPON_TYPE.PHYSICAL')){
//                $name = C('COUPON_TYPE_NAME.PHYSICAL');
//            }elseif($batchCouponInfo['couponType'] == C('COUPON_TYPE.EXPERIENCE')){
//                $name = C('COUPON_TYPE_NAME.EXPERIENCE');
//            }
//
//            // 给用户发短信，告知得到一张商家赠送的优惠券
//            $userMdl = new UserModel();
//            $shopMdl = new ShopModel();
//            $shop = $shopMdl->getShopInfo($batchCouponInfo['shopCode'], array('Shop.shopName'));
//            $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('mobileNbr'));
//            $msg =  str_replace('{{shopName}}', $shop['shopName'], C('SEND_MESSAGE.SEND_COUPON'));
//            $msg =  str_replace('{{couponType}}', $name, $msg);
//            $msg =  str_replace('{{function}}', $batchCouponInfo['function'], $msg);
//            $msg =  str_replace('{{userCouponNbr}}', $data['userCouponNbr'], $msg);
//            $smsMdl = new SmsModel();
//            $smsMdl->send($msg, $userInfo['mobileNbr']);
            return array('code' => C('SUCCESS'), 'userCouponCode' => $data['userCouponCode'], 'userCouponNbr' => $data['userCouponNbr']);
        } else {
            return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
        }
    }

    /**
     * 检查所有冻结的优惠券，若超时则释放
     */
    public function checkFrozenCoupon() {
        $limitTime = 2 * 60 * 60;//两个小时
        $userFrozenCouponList = $this
            ->field(array('userCouponCode', 'batchCouponCode', 'applyTime'))
            ->where(array('status' => C('USER_COUPON_STATUS.FROZEN')))
            ->select();
        foreach($userFrozenCouponList as $userFrozenCoupon) {
            if(time() - strtotime($userFrozenCoupon['applyTime']) > $limitTime) {
                $this->where(array('userCouponCode' => $userFrozenCoupon['userCouponCode']))->delete();
                $batchCouponMdl = new BatchCouponModel();
                $batchCouponMdl->addRemaining($userFrozenCoupon['batchCouponCode'], 1);
            }
        }
    }

    /**
     * 用户注册时，检查冻结的用户优惠券
     * @param string $userCode 用户编码
     */
    public function checkFrozenUserCoupon($userCode = '') {
        $limitTime = 2 * 60 * 60;//两个小时
        $condition['status'] = C('USER_COUPON_STATUS.FROZEN');
        if(!empty($userCode)) {
            $condition['userCode'] = $userCode;
        }
        $userCouponList = $this->field(array('userCouponCode', 'batchCouponCode', 'applyTime'))->where($condition)->select();
        foreach($userCouponList as $userCoupon) {
            if(time() - strtotime($userCoupon['applyTime']) > $limitTime) {
                $this->where(array('userCouponCode' => $userCoupon['userCouponCode']))->delete();
                $batchCouponMdl = new BatchCouponModel();
                $batchCouponMdl->addRemaining($userCoupon['batchCouponCode'], 1);
            } else {
                $this->where(array('userCouponCode' => $userCoupon['userCouponCode']))->save(array('status' => C('USER_COUPON_STATUS.ACTIVE')));
            }
        }
    }

    /**
     * 添加冷冻状态的优惠券
     * @param string $userCode 用户编码
     * @param string $batchCouponCode 优惠券编码
     * @return boolean 成功返回true，失败返回false
     */
    public function addFrozenCoupon($userCode, $batchCouponCode) {
        // 诺亚方舟影院9.9N元购和19.9N元购只能享受其中一种。抢这两种券时加以判断
        $n99Code = 'd2af466d-baa8-42fd-b34f-9c82c18e6e37';
        $n199Code = '7d63ecc8-4f9a-1765-b554-46a2e25e1df3';
        if($batchCouponCode == $n199Code || $batchCouponCode == $n99Code) {
            $batchCouponCode = $batchCouponCode == $n199Code ? $n99Code : $n199Code;
            $isUserHasTheCouponRet = $this->isUserHasTheCoupon($batchCouponCode, $userCode);
            if($isUserHasTheCouponRet == true) {
                return $this->getBusinessCode(C('COUPON.PRIVILEGE_REPEAT'));
            }
        }

        // 判断用户是否已经领取
        $userCouponInfo = $this->field(array('userCouponCode'))->where(array('status' => C('USER_COUPON_STATUS.FROZEN'), 'userCode' => $userCode, 'batchCouponCode' => $batchCouponCode))->find();
        if(! $userCouponInfo) {
            M()->startTrans();
            $addRet = $this->add(array('userCouponCode' => $this->create_uuid(), 'userCode' => $userCode, 'batchCouponCode' => $batchCouponCode, 'status' => C('USER_COUPON_STATUS.FROZEN'), 'applyTime' => date('Y-m-d H:i:s', time()), 'userCouponNbr' => $this->setUserCouponNbr($batchCouponCode)));
            $batchCouponMdl = new BatchCouponModel();
            $decRemainingRet = $batchCouponMdl->decRemaining($batchCouponCode, 1);
            if($addRet !== false && $decRemainingRet !== false) {
                M()->commit();
                return $this->getBusinessCode(C('SUCCESS'));
            } else {
                M()->rollback();
                return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
            }
        } else {
            return $this->getBusinessCode(C('COUPON.LIMIT'));
        }
    }

    /**
     * 用户在商家拥有的有效的优惠券
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @return array $userCouponList
     */
    public function listUserCouponInShop($userCode, $shopCode,$longitude,$latitude) {
        $nowTime = time();
        $sql = '(validityPeriod = 0) OR (BatchCoupon.expireTime >= NOW() AND BatchCoupon.validityPeriod = -1) OR ((' . $nowTime . ' - UNIX_TIMESTAMP(UserCoupon.applyTime)) / 86400 <= BatchCoupon.validityPeriod  AND BatchCoupon.validityPeriod > 0)';
        $userCouponList = $this
            ->field(array(
                'userCouponCode',
                'userCouponNbr',
                'UserCoupon.batchCouponCode',
                'BatchCoupon.batchNbr',
                'BatchCoupon.couponType',
                'availablePrice',
                'insteadPrice',
                'discountPercent',
                'function',
                'payPrice',
                'BatchCoupon.startUsingTime',
                'BatchCoupon.expireTime',
                'dayStartUsingTime',
                'dayEndUsingTime',
                'remark',
                'city',
                'shopName',
                'validityPeriod',
                'BatchCoupon.shopCode',
				//'sqrt(power(Shop.longitude-'.$longitude.',2) + power(Shop.latitude-'.$latitude.', 2))' => 'distance',
				'CollectCoupon.isCollect',
				'CollectCoupon.isGet'

            ))
            ->join('BatchCoupon ON BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
            ->join('Shop ON Shop.shopCode = BatchCoupon.shopCode')
			->join('CollectCoupon on CollectCoupon.collectCouponCode=BatchCoupon.batchCouponCode','LEFT')
            ->where(array(
                //'userCode' => $userCode,
				'UserCoupon.userCode' => $userCode,
                'BatchCoupon.shopCode' => $shopCode,
                'UserCoupon.status' => C('USER_COUPON_STATUS.ACTIVE'),
                '_string' => $sql
            ))
            ->select();
        foreach($userCouponList as &$v) {
            $v['startUsingTime'] = $this->formatDate1($v['startUsingTime']);
            $v['expireTime'] = $this->formatDate1($v['expireTime']);

            $v['dayStartUsingTime'] = substr($v['dayStartUsingTime'], 0, 5);
            $v['dayEndUsingTime'] = substr($v['dayEndUsingTime'], 0, 5);

            $v['availablePrice'] = $v['availablePrice'] / C('RATIO');
            $v['insteadPrice'] = $v['insteadPrice'] / C('RATIO');
            $v['payPrice'] = $v['payPrice'] / C('RATIO');
            $v['discountPercent'] = $v['discountPercent'] / $this->discountRatio;
            $v['city'] = $this->formatCity($v['city']);
            $v['userCoupon'] = C('NO'); // 表示显示该券时，是否需要合并显示。1表示不需要合并显示；0表示需要合并显示
            if(in_array($v['couponType'], array(C('COUPON_TYPE.N_PURCHASE'), C('COUPON_TYPE.PHYSICAL'), C('COUPON_TYPE.EXPERIENCE')))){
                $v['userCoupon'] = C('YES');
                $result[$v['userCouponCode']][] = $v;
            }else{
                $result[$v['batchCouponCode']][] = $v;
            }
        }
        $res = array();
        if(isset($result) && $result){
            foreach($result as $r){
                if($r[0]['userCoupon'] != C('YES')){
                    if(count($r) > 1){
                        unset($r[0]['userCouponCode']);
                        unset($r[0]['userCouponNbr']);
                    }
                }
                $r[0]['userCount'] = count($r); // 领取的数量
                $res[] = $r[0];
            }
        }
        return $res;
    }

    /**
     * 释放用户优惠券
     * @param string $userCouponCode 用户优惠券编码
     * @return boolean 成功返回true；失败返回false
     */
    public function releaseUserCoupon($userCouponCode) {
        $ret = $this
            ->where(array('userCouponCode' => $userCouponCode))
            ->save(array('status' => \Consts::USER_COUPON_STATUS_ACTIVE, 'consumeCode' => '')) !== false ? true : false;
        return $ret;
    }

    /**
     * 优惠券领取人员
     * @param string $batchCouponCode 优惠券编码
     * @param object $page
     * @return array $couponList
     */
    public function listGrabCoupon($batchCouponCode, $page) {
        $couponList = $this
            ->field(array('avatarUrl', 'nickName', 'UserCoupon.applyTime', 'couponType', 'insteadPrice', 'discountPercent', 'function', 'mobileNbr', 'UserCoupon.status'))
            ->join('BatchCoupon ON BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
            ->join('User ON User.userCode = UserCoupon.userCode')
            ->where(array('UserCoupon.batchCouponCode' => $batchCouponCode))
            ->pager($page)
            ->select();
        foreach($couponList as &$couponItem) {
            $couponItem['insteadPrice'] = $couponItem['insteadPrice'] / \Consts::HUNDRED;
            $couponItem['discountPercent'] = $couponItem['discountPercent'] / $this->discountRatio;
        }
        return $couponList;
    }

    /**
     * 优惠券领取人员总数
     * @param string $batchCouponCode 优惠券编码
     * @return int $couponCount
     */
    public function countGrabCoupon($batchCouponCode) {
        $couponCount = $this
            ->where(array('batchCouponCode' => $batchCouponCode))
            ->join('User ON User.userCode = UserCoupon.userCode')
            ->count('userCouponCode');
        return $couponCount;
    }

    /**
     * 统计优惠券的使用数量，订单需要已付款
     * @param string $batchCouponCode 优惠券编码
     * @return int $usedCount
     */
    public function countUsedCoupon($batchCouponCode) {
        $usedCount = $this
            ->join('UserConsume ON UserConsume.consumeCode = UserCoupon.consumeCode')
            ->where(array('batchCouponCode' => $batchCouponCode, 'UserCoupon.status' => \Consts::USER_COUPON_STATUS_USED, 'UserConsume.status' => \Consts::PAY_STATUS_PAYED))
            ->count('UserCoupon.userCouponCode');
        return $usedCount;
    }

    /**
     * 删除用户优惠券
     * @param string $userCouponCode 用户优惠券编码
     * @return boolean 成功返回true；失败返回false
     */
    public function delUserCoupon($userCouponCode) {
        $userCouponInfo = $this->field(array('batchCouponCode'))->where(array('userCouponCode' => $userCouponCode))->find();
        M()->startTrans();
        $delUserCouponRet = $this->where(array('userCouponCode' => $userCouponCode))->delete() !== false ? true : false;
        $batchCouponMdl = new BatchCouponModel();
        $addRemainingRet = $batchCouponMdl->addRemaining($userCouponInfo['batchCouponCode'], 1);
        if($delUserCouponRet && $addRemainingRet) {
            M()->commit();
            return true;
        } else {
            M()->rollback();
            return false;
        }
    }

    /**
     * 获得优惠券的使用详细信息
     * @param string $userCouponCode 优惠券编码
     * @return array $consumeInfo 消费的详细信息
     */
    public function getConsumeInfo($userCouponCode) {
        $consumeInfo = $this
            ->field(array(
                'shopName',
                'consumeTime',
                'deduction',
                'realPay',
                'UserConsume.status',
                'platBonus',
                'shopBonus',
                'orderNbr',
                'orderAmount'
            ))
            ->join('UserConsume ON UserConsume.consumeCode = UserCoupon.consumeCode')
            ->join('ConsumeOrder ON ConsumeOrder.orderCode = UserConsume.orderCode')
            ->join('Shop ON Shop.shopCode = UserConsume.location')
            ->where(array('UserCoupon.userCouponCode' => $userCouponCode))
            ->find();
        $tempArray = array('deduction', 'realPay', 'platBonus', 'shopBonus', 'orderAmount');
        foreach($tempArray as $v) {
            $consumeInfo[$v] = $consumeInfo[$v] / C('RATIO');
        }
        return $consumeInfo;
    }

    /**
     * 获得优惠券抵扣的总金额
     * @param array $couponCodeList 优惠券编码列表
     * @param string $startDate 开始时间
     * @param string $endDate 结束时间
     * @return double $couponDeductionValue 抵扣总额，单位：元
     */
    public function countCouponDeductionValue($couponCodeList, $startDate, $endDate) {
        $endDate = date('Y-m-d 23:59:59', strtotime($endDate));
        $where = array(
            'UserCoupon.batchCouponCode' => array('IN', $couponCodeList),
            'UserCoupon.status' => C('USER_COUPON_STATUS.USED'),
            'UserConsume.status' => C('UC_STATUS.PAYED')
        );
        if($startDate && !$endDate) {
            $where['consumeTime'] = array('EGT', $startDate);
        }
        if(!$startDate && $endDate) {
            $where['consumeTime'] = array('ELT', $endDate);
        }
        if($startDate && $endDate) {
            $where['consumeTime'] = array('between', array($startDate, $endDate));
        }
        $usedCouponList = $this
            ->field(array(
                'orderAmount',
                'insteadPrice',
                'discountPercent',
            ))
            ->join('UserConsume ON UserConsume.consumeCode = UserCoupon.consumeCode')
            ->join('ConsumeOrder ON UserConsume.orderCode = ConsumeOrder.orderCode')
            ->join('BatchCoupon ON BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
            ->where($where)
            ->select();
        $couponDeductionValue = 0;
        foreach($usedCouponList as $v) {
            if(empty($v['insteadPrice'])) {
                $couponDeductionValue += $v['orderAmount'] * $v['discountPercent'] / C('RATIO');
            } else {
                $couponDeductionValue += $v['insteadPrice'];
            }
        }
        return $couponDeductionValue / C('RATIO');
    }

    /**
     * 获得优惠券带来的消费总额
     * @param array $couponCodeList 优惠券编码列表
     * @return double $consumeAmount 消费总额，单位：元
     */
    public function countConsumeAmount($couponCodeList) {
        $consumeAmount = $this
            ->join('UserConsume ON UserConsume.consumeCode = UserCoupon.consumeCode')
            ->join('ConsumeOrder ON UserConsume.orderCode = ConsumeOrder.orderCode')
            ->where(array(
                'batchCouponCode' => array('IN', $couponCodeList),
                'UserCoupon.status' => C('USER_COUPON_STATUS.USED'),
                'UserConsume.status' => C('UC_STATUS.PAYED')
            ))
            ->sum('orderAmount');
        return $consumeAmount / C('RATIO');
    }

    /**
     * 更新用户优惠券的状态
     * @param array $where 条件
     * @param array $data 数据
     * @return boolean
     */
    public function updateUserCouponStatus($where, $data) {
        $userCouponList = $this->field(array('orderCouponCode'))->where($where)->select();
        if($userCouponList) {
            $ret = $this->where($where)->save($data) !== false ? true : false;
            if($ret) {
                $orderCouponCodeList = array();
                foreach($userCouponList as $v) {
                    if($v['orderCouponCode']) {
                        $orderCouponCodeList[] = $v['orderCouponCode'];
                    }
                }
                if($orderCouponCodeList) {
                    // 如果是购买的券，修改订单优惠券的状态
                    $orderCouponMdl = new OrderCouponModel();
                    $editOrderCouponStatus = $orderCouponMdl->editOrderCoupon(array('orderCouponCode' => array('IN', $orderCouponCodeList)), array('status' => \Consts::ORDER_COUPON_STATUS_USE));
                    $ret = $editOrderCouponStatus['code'] == C('SUCCESS') ? true : false;
                }
            }
            return $ret;
        }
        return true;
    }

    /**
     * 得到实际需付金额
     * @param string $userCouponCode 用户优惠券编码
     * @param int $realPay 实际付款金额，单位：分
     * @param int $nbrCoupon 优惠券张数
     * @return int $realPay 实际付款金额，单位：分
     */
    public function getRealPay($userCouponCode, $realPay, $nbrCoupon) {
        $userCouponInfo = $this
            ->field(array('insteadPrice', 'discountPercent','couponType'))
            ->join('BatchCoupon ON BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
            ->where(array('userCouponCode' => $userCouponCode))
            ->find();
        if($userCouponInfo['couponType'] == C('COUPON_TYPE.DISCOUNT')) {
            $couponDeduction  = $realPay - $realPay * $userCouponInfo['discountPercent'] / C('RATIO');
        }else{
            $couponDeduction = $userCouponInfo['insteadPrice'] * $nbrCoupon;
        }
        $couponDeduction = ceil($couponDeduction);
        return ($realPay - $couponDeduction);
    }

    /**
     * 判断用户的优惠券是否可用
     * @param string $userCouponCode 用户优惠券编码
     * @param int $price 消费金额，单位：元
     * @param string $userCode 用户编码
     * @return boolean||string 能用则返回true，不能用返回错误代码
     */
    public function isUserCouponCanBeUsed($userCouponCode, $price, $userCode = '') {
        $userCouponInfo = $this
            ->field(array('BatchCoupon.startUsingTime', 'BatchCoupon.expireTime', 'dayStartUsingTime', 'dayEndUsingTime', 'availablePrice', 'BatchCoupon.couponType', 'applyTime', 'validityPeriod', 'userCode'))
            ->join('BatchCoupon ON BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
            ->where(array('userCouponCode' => $userCouponCode, 'status' => C('USER_COUPON_STATUS.ACTIVE')))
            ->find();
        if(! $userCouponInfo)  {
            return C('COUPON.NOT_AVAILABLE');
        } elseif($userCouponInfo) {
            if($userCouponInfo['userCode'] != $userCode) {
                return C('COUPON.NOT_AVAILABLE');
            }
            if($userCouponInfo['couponType'] == C('COUPON_TYPE.REDUCTION') || $userCouponInfo['couponType'] == C('COUPON_TYPE.DISCOUNT')) {
                if($userCouponInfo['availablePrice'] > $price * C('RATIO')) {
                    return C('COUPON.NOT_AVAILABLE');
                }
            }
            if(strtotime($userCouponInfo['expireTime']) < time() && $userCouponInfo['expireTime'] != '0000-00-00 00:00:00') {
                return C('COUPON.EXPIRED');
            }
            if((strtotime($userCouponInfo['dayStartUsingTime']) > time() || strtotime($userCouponInfo['startUsingTime']) > time()) && $userCouponInfo['validityPeriod'] == -1) {
                return C('COUPON.NOT_AVAILABLE');
            }
        }
        return true;
    }

    /**
     * 付款时获得用户可使用的平台优惠券列表
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param float $consumeAmount 消费金额
     * @return array $platCouponList 有数据返回二维数组，没有数据返回空数组
     */
    public function listPlatCouponWhenPay($userCode, $shopCode, $consumeAmount) {
        $nowTime = time();
        $sql = '(validityPeriod = 0) OR (BatchCoupon.startUsingTime <= NOW() AND BatchCoupon.expireTime >= NOW() AND
BatchCoupon.validityPeriod = -1) OR ((' . $nowTime . ' - UNIX_TIMESTAMP(UserCoupon.applyTime)) / 86400 <=
BatchCoupon.validityPeriod)';
        $time = date('H:i:s');
        $where = array(
            'userCode' => $userCode,
            'shopCode' => C('HQ_CODE'),
            'dayStartUsingTime' => array('ELT', $time),
            'dayEndUsingTime' => array(array('GT', $time), array('EQ', '00:00:00'), 'or'),
            'UserCoupon.status' => C('USER_COUPON_STATUS.ACTIVE'),
            'BatchCoupon.couponType' => array('IN', array(C('COUPON_TYPE.DISCOUNT'), C('COUPON_TYPE.REDUCTION'), C('COUPON_TYPE.NEW_CLIENT_REDUCTION'), C('COUPON_TYPE.SEND_INVITER'))),
            '_string' => $sql
        );
        if($consumeAmount > 0) {
            $where['availablePrice'] = array('ELT', $consumeAmount * C('RATIO'));
        }
        $platCouponList = $this
            ->field(array(
                'userCouponCode',
                'insteadPrice',
                'discountPercent',
                'UserCoupon.expireTime',
                'availablePrice' => 'availablePrice',
                'couponType',
                'isUniversal',
                'BatchCoupon.batchCouponCode',
                'BatchCoupon.exRuleList'
            ))
            ->join('BatchCoupon ON BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
            ->where($where)
            ->order('insteadPrice DESC, discountPercent DESC, expireTime asc')
            ->select();
        foreach($platCouponList as $k => &$v) {
            if($v['isUniversal'] == C('NO')) {
                $pCouponShopMdl = new PCouponShopModel();
                $isLinkedShop = $pCouponShopMdl->isLinkedShop($v['batchCouponCode'], $shopCode);
                if(!$isLinkedShop) {
                    unset($platCouponList[$k]);
                    continue;
                }
            }

            $exRuleList = (array)json_decode($v['exRuleList']);
            unset($v['exRuleList']);
            $v['exRuleDes'] = '';
            if($exRuleList){
                $couponRuleMdl = new CouponRuleModel();
                $exRuleDes = $couponRuleMdl->listCouponRule(array('ruleCode' => array('IN', $exRuleList)), array('ruleDes'));
                $v['exRuleDes'] = $exRuleDes[0]['ruleDes'];
            }
            //转化日期格式
            $v['expireTime'] = $this->formatDate1($v['expireTime']);

            $v['discountPercent'] = $v['discountPercent'] / $this->discountRatio;
            $v['insteadPrice'] = $v['insteadPrice'] / C('RATIO');
            $v['availablePrice'] = $v['availablePrice'] / C('RATIO');
        }
        return $platCouponList ? $platCouponList : array();
    }

    /**
     * 付款时获得用户可使用的优惠券列表
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param float $consumeAmount 消费金额
     * @return array $userCouponList 有数据返回二维数组，没有数据返回空数组
     */
    public function listUserCouponWhenPay($userCode, $shopCode, $consumeAmount) {
        $nowTime = time();
        $sql = '(validityPeriod = 0) OR (BatchCoupon.startUsingTime <= NOW() AND BatchCoupon.expireTime >= NOW() AND
BatchCoupon.validityPeriod = -1) OR ((' . $nowTime . ' - UNIX_TIMESTAMP(UserCoupon.applyTime)) / 86400 <=
BatchCoupon.validityPeriod)';
        $where = array(
            'userCode' => $userCode,
            'shopCode' => $shopCode,
            'dayStartUsingTime' => array('ELT', date('H:i:s', time())),
            'dayEndUsingTime' => array(array('GT', date('H:i:s', time())), array('EQ', '00:00:00'), 'or'),
            'UserCoupon.status' => C('USER_COUPON_STATUS.ACTIVE'),
            'BatchCoupon.couponType' => array('IN', array(C('COUPON_TYPE.DISCOUNT'), C('COUPON_TYPE.REDUCTION'))),
            '_string' => $sql
        );
        if($consumeAmount > 0) {
            $where['availablePrice'] = array('ELT', $consumeAmount * C('RATIO'));
        }
        $userCouponList = $this
            ->field(array(
                'userCouponCode',
                'insteadPrice',
                'discountPercent',
                'UserCoupon.expireTime',
                'availablePrice' => 'availablePrice',
                'couponType',
                'BatchCoupon.exRuleList',
                'BatchCoupon.couponType'
            ))
            ->join('BatchCoupon ON BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
            ->where($where)
            ->order('insteadPrice DESC, discountPercent DESC, expireTime asc')
            ->select();
        foreach($userCouponList as &$v) {
            $exRuleList = (array)json_decode($v['exRuleList']);
            unset($v['exRuleList']);
            $v['exRuleDes'] = '';
            if($exRuleList){
                $couponRuleMdl = new CouponRuleModel();
                $exRuleDes = $couponRuleMdl->listCouponRule(array('ruleCode' => array('IN', $exRuleList)), array('ruleDes'));
                $v['exRuleDes'] = $exRuleDes[0]['ruleDes'];
            }
            //转化日期格式
            $v['expireTime'] = $this->formatDate1($v['expireTime']);

            $v['discountPercent'] = $v['discountPercent'] / $this->discountRatio;
            $v['insteadPrice'] = $v['insteadPrice'] / C('RATIO');
            $v['availablePrice'] = $v['availablePrice'] / C('RATIO');
        }
        return $userCouponList ? $userCouponList : array();
    }

    /**
     * 判断用户是否能抢该优惠券
     * @param string $userCode 用户编码
     * @param string $batchCouponCode 优惠券编码
     * @return boolean 有返回true，没有返回false
     */
    public function isUserCanGrabTheCoupon($userCode, $batchCouponCode) {
        $count = $this->where(array('userCode' => array('eq',$userCode), 'batchCouponCode' => array('eq',$batchCouponCode),'status' => array('in',array(1,2))))->count('userCouponCode');
        $batchCouponMdl = new BatchCouponModel();
        $couponInfo = $batchCouponMdl->getCouponInfo($batchCouponCode);
        if($couponInfo['nbrPerPerson'] != self::NO_LIMIT && $count >= $couponInfo['nbrPerPerson']){
            return false;
        }
        return true;
    }
    /**
     * 获得某个时间段优惠券已使用的数量
     * @param array $batchCouponCodeList 优惠券编码
     * @param string $startDate 开始日期
     * @param string $endDate 结束日期
     * @return int
     */
    public function countReceivedCoupon($batchCouponCodeList, $startDate, $endDate) {
        $endDate = date('Y-m-d 23:59:59', strtotime($endDate));
        $where = array(
            'batchCouponCode' => array('IN', $batchCouponCodeList),
            'UserCoupon.status' => C('USER_COUPON_STATUS.USED'),
            'UserConsume.status' => C('UC_STATUS.PAYED')
        );
        if($startDate && !$endDate) {
            $where['consumeTime'] = array('EGT', $startDate);
        }
        if(!$startDate && $endDate) {
            $where['consumeTime'] = array('ELT', $endDate);
        }
        if($startDate && $endDate) {
            $where['consumeTime'] = array('between', array($startDate, $endDate));
        }
        return $this
            ->join('UserConsume ON UserConsume.consumeCode = UserCoupon.consumeCode')
            ->where($where)
            ->count('UserCoupon.userCouponCode');
    }

    /**
     * 获得优惠券被领用的数量
     * @param array $batchCouponCodeList 优惠券编码
     * @return int
     */
    public function countReceiveCoupon($batchCouponCodeList){
        return $this->where(array('batchCouponCode' => array('IN', $batchCouponCodeList)))->count('userCouponCode');
    }

    /**
     * 某种优惠券可用的数量
     * @param string $batchCouponCode 优惠券编码
     * @param string $userCode 用户编码
     * @return int
     */
    public function countActiveCoupon($batchCouponCode, $userCode = ''){
        $condition = array('batchCouponCode' => $batchCouponCode, 'status' => C('USER_COUPON_STATUS.ACTIVE'));
        if($userCode){
            $condition['userCode'] = $userCode;
        }
        return $this->where($condition)->count('userCouponCode');
    }

    /**
     * 获得某种优惠券被某个用户领用的数量
     * @param string $batchCouponCode 优惠券编码
     * @param string $userCode 用户编码
     * @return number
     */
    public function countMyReceivedCoupon($batchCouponCode, $userCode){
        return $this
            ->where(array('batchCouponCode' => $batchCouponCode, 'userCode' => $userCode))
            ->count('userCouponCode');
    }

    /**
     * 生成用户优惠券券号
     * @param string $batchCouponCode 优惠券编码
     * @return string $userCouponNbr
     */
    private function setUserCouponNbr($batchCouponCode) {
        $batchCouponMdl = new BatchCouponModel();
        $batchCouponInfo = $batchCouponMdl->getCouponBasicInfo(array('batchCouponCode' => $batchCouponCode), array('batchNbr'));

        $minNbr = 1;
        $maxNbr = 99999;
        $serialNbr = sprintf('%05d', mt_rand($minNbr, $maxNbr));
//        $maxUserCouponNbr = $this->where(array('batchCouponCode' => $batchCouponCode))->max('userCouponNbr');
//        $serialNbr = sprintf('%05d', intval(substr($maxUserCouponNbr, -5)) + 1);
        $userCouponNbr = $batchCouponInfo['batchNbr'] . $serialNbr;
        $hasUserCoupon = $this->hasUserCoupon(array('batchCouponCode' => $batchCouponCode, 'userCouponNbr' => $userCouponNbr));
        if($hasUserCoupon == false) {
            return $userCouponNbr;
        } else {
            return $this->setUserCouponNbr($batchCouponCode);
        }
    }

    /**
     * 计算用户优惠券的有效使用时间
     * @param array $batchCouponInfo 优惠券信息
     * @return array {'startUsingTime', 'expireTime'}
     */
    private function calValidTime($batchCouponInfo) {
        if($batchCouponInfo['validityPeriod'] == '-1') {
            $startUsingTime = $batchCouponInfo['startUsingTime'];
            $expireTime = $batchCouponInfo['expireTime'] . ' 23:59:59';
        } elseif($batchCouponInfo['validityPeriod'] == '0') {
            $startUsingTime = $expireTime = '0000-00-00 00:00:00';
        } else {
            $startUsingTime = date('Y-m-d H:i:s');
            $expireTime = date('Y-m-d H:i:s', time() + $batchCouponInfo['validityPeriod'] * 24 * 60 * 60);
        }
        return array('startUsingTime' => $startUsingTime, 'expireTime' => $expireTime);
    }

    /**
     * 判断用户是否拥有该优惠券
     * @param string $batchCouponCode 优惠券编码
     * @param string $userCode 用户编码
     * @return boolean 有则返回true，没有则返回false
     */
    public function isUserHasTheCoupon($batchCouponCode, $userCode) {
        $userCouponInfo = $this->field(array('userCouponCode'))->where(array('userCode' => $userCode, 'batchCouponCode' => $batchCouponCode))->find();
        return empty($userCouponInfo) ? false : true;
    }

    /**
     * 领用/抢优惠券
     * @param string $batchCouponCode 优惠券编码
     * @param string $userCode 用户编码
     * @param number $sharedLvl 分享程度。0-所有人可见；1-朋友可见；2-其他人不可见
     * @param $type
     * @return array
     */
    public function grabCoupon($batchCouponCode, $userCode, $sharedLvl, $type = 0) {
        // 诺亚方舟影院9.9N元购和19.9N元购只能享受其中一种。抢这两种券时加以判断
        $n99Code = 'd2af466d-baa8-42fd-b34f-9c82c18e6e37'; // 诺亚方舟影院9.9元N元购优惠券编码
        $n199Code = '7d63ecc8-4f9a-1765-b554-46a2e25e1df3'; // 诺亚方舟影院19.9元N元购优惠券编码
        $asBatchCouponCode99 = '639cecd8-ca64-bc09-72fd-ffc45d822779'; // 爱尚影院9.9元N元购优惠券编码
        $asBatchCouponCode199 = '8f92b218-bbf4-15e8-f89e-7307643d2535'; // 爱尚影院19.9元N元购优惠券编码
        $ydsdBatchCouponCode99 = '4b6c5052-bdef-b0a9-6a91-56d7c31aae11'; // 银都时代电影9.9元N元购优惠券编码
        $ydsdBatchCouponCode199 = 'f94446d3-6545-c139-60ab-6b160f0f11d6'; // 银都时代电影19.9元N元购优惠券编码

        if($batchCouponCode == $n199Code || $batchCouponCode == $n99Code) { // 诺亚方舟影院9.9元N元购和19.9元N元购只能享受其中一种。抢这两种券时加以判断
            $anotherBatchCouponCode = $batchCouponCode == $n199Code ? $n99Code : $n199Code;
        } elseif($batchCouponCode == $asBatchCouponCode99 || $batchCouponCode == $asBatchCouponCode199) { // 爱尚影院9.9元N元购和19.9元N元购只能享受其中一种。抢这两种券时加以判断
            $anotherBatchCouponCode = $batchCouponCode == $asBatchCouponCode99 ? $asBatchCouponCode199 : $asBatchCouponCode99;
        } elseif($batchCouponCode == $ydsdBatchCouponCode99 || $batchCouponCode == $ydsdBatchCouponCode199) { // 银都时代电影9.9元N元购和19.9元N元购只能享受其中一种。抢这两种券时加以判断
            $anotherBatchCouponCode = $batchCouponCode == $ydsdBatchCouponCode99 ? $ydsdBatchCouponCode199 : $ydsdBatchCouponCode99;
        }
        if(!empty($anotherBatchCouponCode)) {
            $isUserHasTheCouponRet = $this->isUserHasTheCoupon($anotherBatchCouponCode, $userCode);
            if($isUserHasTheCouponRet == true) {
                return $this->getBusinessCode(C('COUPON.PRIVILEGE_REPEAT'));
            }
        }

        M()->startTrans();
        $receivedCount = $this->countReceiveCoupon(array($batchCouponCode)); // 获得优惠券被领用的数量
        $myReceivedCount = $this->countMyReceivedCoupon($batchCouponCode, $userCode); // 获得某种优惠券被某个用户领用的数量
        $batchCouponMdl = new BatchCouponModel();

        // 判断顾客是否可领取优惠券
        $ret = $batchCouponMdl->isCouponCanBeGet($batchCouponCode, $receivedCount, $myReceivedCount, $type);

        if($ret !== true) {
            return $this->getBusinessCode($ret);
        }

        $batchCouponInfo = $batchCouponMdl->getCouponInfo($batchCouponCode);

        $valTime = $this->calValidTime($batchCouponInfo);

        $userCoupon = array(
            'userCouponCode' => $this->create_uuid(),
            'userCode' => $userCode,
            'batchCouponCode' => $batchCouponCode,
            'status' => C('USER_COUPON_STATUS.ACTIVE'),
            'applyTime' => date('Y-m-d H:i:s', time()),
            'sharedLvl' => $sharedLvl,
            'userCouponNbr' => $this->setUserCouponNbr($batchCouponCode),
            'userCouponType' => $batchCouponInfo['couponType'],
            'startUsingTime' => $valTime['startUsingTime'],
            'expireTime' => $valTime['expireTime'],
        );
        $ret = $this->add($userCoupon);
		$CollectCouponModel=D('CollectCoupon');

		//查询优惠券每个人可以领取多少个
		$batchCouponModel=D('BatchCoupon');
		$personCount=$batchCouponModel->field('nbrPerPerson')->where("batchCouponCode='$batchCouponCode'")->find();

        if($ret !== false) {
		//查询客户是否领取过这个优惠券这个记录，如果没有进行add操作
		$couponCount=$CollectCouponModel->where("collectCouponCode='$batchCouponCode' AND userCode='$userCode'")->count();

		$arr=array(
            'collectCouponCode'=>$batchCouponCode,
			'userCode'=>$userCode,
			'isGetCount'=>$personCount['nbrPerPerson']-1
        );
		if($couponCount<1){
			 $CollectCouponModel->add($arr);
		}

		//修改isGet为1
		$CollectCouponModel->isGet='1';
		$CollectCouponModel->where("collectCouponCode='$batchCouponCode'AND userCode='$userCode'")->save();

		//查询客户取过某一优惠券数量
		$userCouponCount=$this->where("batchCouponCode='$batchCouponCode' AND userCode='$userCode'")->count();

		//客户剩余可领取数量
		$isGetCount=$personCount['nbrPerPerson']-$userCouponCount;

		//查询商家发布优惠券剩余数量
		$remainingCount=$batchCouponModel->where("batchCouponCode='$batchCouponCode'")->getField('remaining');

		//判断用户是否还可领
		if($isGetCount>0 && $remainingCount>0){
			//修改CollectCoupon表优惠券剩余数量
            $CollectCouponModel->where(array('userCode'=>$userCode,'batchCouponCode'=>$batchCouponCode))->setDec("isGetCount");

		}else{
			//在CollectCoupon修改客户剩余领取数量
            $CollectCouponModel->where("userCode='$userCode' and collectCouponCode='$batchCouponCode'")->setDec('isGetCount');
		}

            // 优惠券剩余数量减1
            $batchCouponMdl->reduceCoupon($batchCouponCode);
            // 添加优惠券领用消息
            $msgInfo = array(
                'msgCode' => $this->create_uuid(),
                'title' => C('MSG_TITLE_TDL.GRAB_COUPON'),
                'content' => C('COUPON_MSG_TDL.GRAB'),
                'createTime' => date('Y-m-d H:i:s'),
                'senderCode' => $batchCouponInfo['shopCode'],
                'type' => C('MESSAGE_TYPE.COUPON'),
                'userCouponCode' => $userCoupon['userCouponCode'],
            );
            $msgMdl = new MessageModel();
            $ret = $msgMdl ->addMsg($msgInfo, $userCode);
            if( $ret !== false) {
                M()->commit();
                return array(
                    'code' => C('SUCCESS'),
                    'userCouponCode' => $userCoupon['userCouponCode'],
                    'userCouponNbr' => $userCoupon['userCouponNbr'],
                    'userCount' => $this->countActiveCoupon($batchCouponCode, $userCode),
					'isCount'=>$isGetCount
                );
            } else {
                M()->rollback();
                return $this->getBusinessCode(C('COUPON.BEEN_TOKEN_OVER'));
            }
        } else{
            return $this->getBusinessCode(C('COUPON.API_INTERNAL_EXCEPTION'));
        }
    }

    /**
     * 为getMyAvailableCoupon，countMyAvailableCoupon方法拼接$condition
     * @param number $status 优惠券状态。1-有效的；2-已使用；3-已失效；
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @return array $condition
     */
    public function getMyAvailableCouponCondition($status, $userCode, $shopCode) {
        $nowTime = time();
        $condition = array();
        $condition['UserCoupon.userCode'] = $userCode;
        if($status == 1) {
            $sql = '(validityPeriod = 0) OR (BatchCoupon.expireTime >= NOW() AND BatchCoupon.validityPeriod = -1) OR ((' . $nowTime . ' - UNIX_TIMESTAMP(UserCoupon.applyTime)) / 86400 <= BatchCoupon.validityPeriod) ';
            $condition['UserCoupon.status'] = C('USER_COUPON_STATUS.ACTIVE');
            $condition['_string'] = $sql;

        }

        /*if($status == 2) {
            $condition['_string'] = '(UserCoupon.status = ' . \Consts::USER_COUPON_STATUS_USED . ') OR (UserCoupon.status = ' . \Consts::USER_COUPON_STATUS_DISABLE .' AND OrderCoupon.status = ' . \Consts::ORDER_COUPON_STATUS_REFUNDED_NOUSE . ')';
        }*/
		if($status == 2) {
            $condition['_string'] = 'UserCoupon.status = ' . \Consts::USER_COUPON_STATUS_USED . ' OR (UserCoupon.status = ' . \Consts::USER_COUPON_STATUS_USED .' AND OrderCoupon.status = ' . \Consts::ORDER_COUPON_STATUS_REFUNDED_NOUSE . ')';
        }
        if($status == 3) {
            $condition['validityPeriod'] = array('neq', '0');
            $sql = '(BatchCoupon.expireTime <= NOW() AND BatchCoupon.validityPeriod = -1) OR ((' . $nowTime . ' - UNIX_TIMESTAMP(UserCoupon.applyTime)) / 86400 > BatchCoupon.validityPeriod) AND BatchCoupon.validityPeriod != -1';
            $condition['_string'] = $sql;
        }
        if($shopCode){
            $condition['BatchCoupon.shopCode'] = $shopCode;
        }
        return $condition;
    }

    /**
     * 获取某一用户的所有可用的优惠券，如果 $shopCode 不为空，则为获取某一用户拥有某一商家的所有可用优惠券
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param number $status 优惠券状态。1-有效的；2-已使用；3-已失效；
     * @param object $page 页码
     * @param double $longitude 经度
     * @param double $latitude 纬度
     * @return array
     */
    public function getMyAvailableCoupon($userCode, $shopCode, $status, $page, $longitude, $latitude,$city,$zoneId){
        $condition = $this->getMyAvailableCouponCondition($status, $userCode, $shopCode,$city);
		$condition['Shop.city']=$city;
		$condition['Shop.bank_id'] = $zoneId;
        $field = array(
            'userCouponCode',
            'userCouponNbr',
            'UserCoupon.sharedLvl',
            'UserCoupon.status',
            'UserCoupon.batchCouponCode',
            'BatchCoupon.discountPercent',
            'BatchCoupon.insteadPrice',
            'BatchCoupon.availablePrice',
            'function',
            'BatchCoupon.startUsingTime',
            'BatchCoupon.expireTime',
            'BatchCoupon.batchNbr',
            'BatchCoupon.remark',
            'BatchCoupon.couponType',
            'Shop.shopCode',
            'Shop.shopName',
            'Shop.mobileNbr',
            'Shop.street',
            'Shop.logoUrl',
            'dayStartUsingTime',
            'dayEndUsingTime',
            'BatchCoupon.exRuleList',
            'city',
			'BackgroundTemplate.url',
			'UserCoupon.isCollect',
			'UserCoupon.isGet'
        );
        if($longitude && $latitude) {
            $field['sqrt(power(Shop.longitude-'.$longitude.',2) + power(Shop.latitude-'.$latitude.',2))'] = 'distance';
        }
        $myAvailableCoupon = $this
            ->field($field)
            ->join('BatchCoupon on BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
            ->join('BackgroundTemplate on BackgroundTemplate.bgCode = BatchCoupon.bgImgCode')
            ->join('OrderCoupon ON OrderCoupon.orderCouponCode = UserCoupon.orderCouponCode', 'left')
            ->join('Shop on Shop.shopCode = BatchCoupon.shopCode')
            ->where($condition)
            ->order('applyTime desc')
            ->pager($page)
            ->select();
        if($myAvailableCoupon) {
            foreach($myAvailableCoupon as &$v) {
                $v['startUsingTime'] = $this->formatDate1($v['startUsingTime']);
                $v['expireTime'] = $this->formatDate1($v['expireTime']);

                $v['dayStartUsingTime'] = substr($v['dayStartUsingTime'], 0, 5);
                $v['dayEndUsingTime'] = substr($v['dayEndUsingTime'], 0, 5);

                $v['city'] = $this->formatCity($v['city']);

                $exRuleList = (array)json_decode($v['exRuleList']);
                unset($v['exRuleList']);
                $v['exRuleDes'] = '';
                if($exRuleList){
                    $couponRuleMdl = new CouponRuleModel();
                    $exRuleDes = $couponRuleMdl->listCouponRule(array('ruleCode' => array('IN', $exRuleList)), array('ruleDes'));
                    $v['exRuleDes'] = $exRuleDes[0]['ruleDes'];
                }
                $v['availablePrice'] = $v['availablePrice'] / C('RATIO');
                $v['insteadPrice'] = $v['insteadPrice'] / C('RATIO');
                $v['distance'] = intval($v['distance'] * C('PROPORTION'));
                $v['discountPercent'] = $v['discountPercent'] / $this->discountRatio;
//                $v['dayStartUsingTime'] = substr($v['dayStartUsingTime'], 0, 5);
//                $v['dayEndUsingTime'] = substr($v['dayEndUsingTime'], 0, 5);
            }
        }
        return $myAvailableCoupon ? $myAvailableCoupon : array();
    }

    /**
     * 统计总记录数
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param number $status 优惠券状态。1-可用，有效的；0-不可用，历史的；
     * @return array
     */
    public function countMyAvailableCoupon($userCode, $shopCode, $status,$zoneId){
        $condition = $this->getMyAvailableCouponCondition($status, $userCode, $shopCode);
        $condition['Shop.bank_id'] = $zoneId;
        $couponCount = $this
            ->join('BatchCoupon on BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
            ->join('OrderCoupon ON OrderCoupon.orderCouponCode = UserCoupon.orderCouponCode', 'left')
            ->join('Shop on Shop.shopCode = BatchCoupon.shopCode')
            ->where($condition)
            ->count('userCouponCode');
        return $couponCount;
    }

    /**
     * 获得用户优惠券信息
     * @param array $condition 支付编码
     * @param array $field 要查询的字段，例：{'couponType', ...}
     * @return array $userCouponInfo
     */
    public function getUserCouponInfoB($condition, $field){
        $userCouponInfo = $this
            ->field($field)
            ->join('BatchCoupon on BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
            ->where($condition)
            ->find();

        return $userCouponInfo;
    }

    /**
     * 获得顾客已经领取的优惠券详细信息
     * @param string $userCouponCode 用户优惠券编码
     * @return array
     */
    public function getUserCouponInfo($userCouponCode) {
        $userCouponInfo = $this
            ->field(array(
                'UserCoupon.batchCouponCode',
                'UserCoupon.applyTime',
                'userCouponCode',
                'userCouponNbr',
                'UserCoupon.status',
                'discountPercent',
                'insteadPrice',
                'availablePrice',
                'function',
                'couponType',
                'BatchCoupon.startUsingTime',
                'BatchCoupon.expireTime',
                'dayStartUsingTime',
                'dayEndUsingTime',
                'BatchCoupon.batchNbr',
                'BatchCoupon.remark',
                'BatchCoupon.validityPeriod',
				'BatchCoupon.payPrice',
                'Shop.shopName',
                'Shop.mobileNbr',
                'Shop.tel',
                'Shop.street',
                'Shop.logoUrl',
                'Shop.type',
                'Shop.shopCode',
                'popularity',
                'repeatCustomers',
                'LEFT(city,2)' => 'city',
				//'ConsumeOrder.status'=>'refundStatus'
            ))
            ->join('BatchCoupon on BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
            ->join('Shop on Shop.shopCode = BatchCoupon.shopCode')
		//	->join('ConsumeOrder on ConsumeOrder.shopCode=Shop.shopCode')
            ->where(array('UserCoupon.userCouponCode' => $userCouponCode))
			//->fetchsql(true)
            ->find();

        if($userCouponInfo['status'] != C('USER_COUPON_STATUS.USED')) {
            if($userCouponInfo['validityPeriod'] > 0){
                $time = strtotime($userCouponInfo['applyTime']) + $userCouponInfo['validityPeriod'] * 86400;
                if(time() > $time){
                    // 已过期
                    $userCouponInfo['status'] = C('USER_COUPON_STATUS.EXPIRED');
                }
            }else{
                if($userCouponInfo['expireTime'] < date('Y-m-d h:i:s', time())){
                    // 已过期
                    $userCouponInfo['status'] = C('USER_COUPON_STATUS.EXPIRED');
                }
                if (strtotime($userCouponInfo['startUsingTime']) <= time() && strtotime($userCouponInfo['expireTime']) >= time() && date('H:i:s', time()) >= $userCouponInfo['dayStartUsingTime'] && date('H:i:s', time()) <= $userCouponInfo['dayEndUsingTime']) {
                    // 可使用
                    $userCouponInfo['status'] = C('USER_COUPON_STATUS.ACTIVE');
                } else {
                    // 待使用
                    $userCouponInfo['status'] = C('USER_COUPON_STATUS.TOBE_ACTIVE');
                }
            }
        }

        $userCouponInfo['insteadPrice'] = $userCouponInfo['insteadPrice'] / C('RATIO');
        $userCouponInfo['availablePrice'] = $userCouponInfo['availablePrice'] / C('RATIO');
        $userCouponInfo['discountPercent'] = $userCouponInfo['discountPercent'] / $this->discountRatio;
        $userCouponInfo['startUsingTime'] = $this->formatDate1($userCouponInfo['startUsingTime']);
        $userCouponInfo['expireTime'] = $this->formatDate1($userCouponInfo['expireTime']);
        $userCouponInfo['dayStartUsingTime'] = substr($userCouponInfo['dayStartUsingTime'], 0, 5);
        $userCouponInfo['dayEndUsingTime'] = substr($userCouponInfo['dayEndUsingTime'], 0, 5);
        $userCouponInfo['city'] = $this->formatCity($userCouponInfo['city']);

//         获得商家装修
        $shopDecorationMdl = new ShopDecorationModel();
        $userCouponInfo['shopDecoration'] = $shopDecorationMdl->getShopDecoration($userCouponInfo['shopCode']);

	   $batchCouponModel=D('BatchCoupon');
	   $userCouponModel=D('UserCoupon');
	   //根据用户领取userCouponCode查询batchCouponCode
	   $userCouponCod=$userCouponModel->field(array('batchCouponCode','userCode'))->where("userCouponCode='$userCouponCode'")->find();

	   $batchCouponCode=$userCouponCod['batchCouponCode'];//优惠券编码
	   $userCode=$userCouponCod['userCode'];//用户编码

	   //查询优惠券每个人可以领取多少个
		$personCount=$batchCouponModel->field('nbrPerPerson')->where("batchCouponCode='$batchCouponCode'")->find();

		//查询客户是否领取过优惠券
		$userCouponCount=$userCouponModel->where("batchCouponCode='$batchCouponCode' AND userCode='$userCode'")->count();

	   //客户剩余可领取数量
	   $isGetCount=$personCount['nbrPerPerson']-$userCouponCount;

	   //查询商家发布优惠券剩余数量
		$remainingCount=$batchCouponModel->field('remaining')->where("batchCouponCode='$batchCouponCode'")->find();

		//判断用户可领优惠券和商家剩余数量比
		if($isGetCount>$remainingCount['remaining']){
			$isGetCount=$remainingCount['remaining'];
			if($isGetCount<0){
				$isGetCount=0;
			}
		}

	   if($userCouponInfo){
		   return array($userCouponInfo,'isCount'=>$isGetCount);
	   }else{
		   return array();
	   }
    }

    /**
     * 设置优惠券的分享状态
     * @param string $userCouponCode 优惠券编码
     * @param string $userCode 用户编码
     * @param number $sharedLvl 分享等级
     * @return int
     */
    public function updateCouponSharedStatus($userCouponCode, $userCode, $sharedLvl){
        $couponInfo = $this->field(array('UserCoupon.userCode', 'BatchCoupon.expireTime'))->where(array('userCouponCode' => $userCouponCode))->join('BatchCoupon on BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')->find();
        if($userCode != $couponInfo['userCode'])
            return $this->getBusinessCode(C('COUPON.BEEN_TOKEN_AWAY'));
        if(time() > strtotime($couponInfo['expireTime']) && substr($couponInfo['expireTime'], 0, 10) != '0000-00-00')
            return $this->getBusinessCode(C('COUPON.EXPIRED'));
        $code = $this->where(array('userCouponCode' => $userCouponCode))->save(array('sharedLvl' => $sharedLvl)) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

    /**
     * 获得用户优惠券的信息
     * @param string $userCouponCode 用户优惠券编码
     * @param array $field 查询的字段
     * @return array
     */
    public function getCouponInfo($userCouponCode, $field = array()) {
        $field = empty($field) ? array('UserCoupon.*','BatchCoupon.*') : $field;
        $couponInfo = $this
//            ->field(array('UserCoupon.*','BatchCoupon.*'))
            ->field($field)
            ->join('BatchCoupon on BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
            ->where(array('userCouponCode' => $userCouponCode))
            ->find();
//        $couponInfo = $this->dividedByHundred($couponInfo, array('availablePrice', 'insteadPrice', 'canTakePrice'));
        return $couponInfo;
    }

    /**
     * 获得每批次优惠券消费走势信息
     * @param $shopCode
     * @return array
     */
    public function listCouponConsumeTrend($shopCode){
        $condition['BatchCoupon.shopCode'] = $shopCode;
        $condition['UserCoupon.status'] = C('USER_COUPON_STATUS.USED');
        $condition['UserConsume.status'] = C('UC_STATUS.PAYED');
        $couponConsumeTrend =  $this
            ->field(array('BatchCoupon.batchNbr', 'sum(UserConsume.deduction + UserConsume.realPay)' => 'amount'))
            ->join('BatchCoupon on BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
            ->join('UserConsume on UserConsume.consumeCode = UserCoupon.consumeCode','left')
            ->where($condition)
            ->group('BatchCoupon.batchNbr')
            ->order('BatchCoupon.createTime asc')
            ->select();
        foreach($couponConsumeTrend as &$v) {
            unset($v['createTime']);
            $v['amount'] = $v['amount'] ? $v['amount'] / C('RATIO'): 0;
        }
        return $couponConsumeTrend;
    }

    /**
     * 获得每批次消费人次信息
     * @param $shopCode
     * @return array
     */
    public function listCouponPersonTrend($shopCode){
        $condition['BatchCoupon.shopCode'] = $shopCode;
        $condition['UserCoupon.status'] = C('USER_COUPON_STATUS.USED');
        $condition['UserConsume.status'] = C('UC_STATUS.PAYED');
        return $this
            ->field(array('BatchCoupon.batchNbr', 'count(UserCoupon.userCouponCode)' => 'amount'))
            ->join('BatchCoupon on BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
            ->join('UserConsume on UserConsume.consumeCode = UserCoupon.consumeCode','left')
            ->where($condition)
            ->group('BatchCoupon.batchNbr')
            ->order('unix_timestamp(BatchCoupon.createTime) asc')
            ->select();
    }

    /**
     * 使用优惠券
     * @param array $userCouponCodeArr 用户领取的优惠券编码
     * @param array $data 例如：array('status'=>2,'consumeCode'=>$consumeCode)
     * @return boolean
     */
    public function useCoupon($userCouponCodeArr, $data){
        $orderCouponMdl = new OrderCouponModel();
        foreach($userCouponCodeArr as $v){
            $userCouponInfo = $this->where(array('userCouponCode' => $v))->find();
            $ret1 = $this->where(array('userCouponCode' => array('eq', $v)))->save($data) !== false ? true : false;
            $ret2['code'] = C('SUCCESS');
            // 如果是花钱购买的优惠券，修改订单优惠券的状态为已使用，保存券
            if(!empty($userCouponInfo['orderCouponCode'])){
                // 计算该券的结算金额详情包括（银行支付金额，平台红包金额，商户红包金额）
                $couponConsumeDetail = $orderCouponMdl->calCouponConsumeDetail($userCouponInfo['orderCouponCode']);

                $ret2 = $orderCouponMdl->editOrderCoupon(
                    array('orderCouponCode' => $userCouponInfo['orderCouponCode']),
                    array(
                        'OrderCoupon.shopBonus' => $couponConsumeDetail['toSettleShopBonus'],
                        'OrderCoupon.platBonus' => $couponConsumeDetail['toSettlePlatBonus'],
                        'bankcardAmount' => $couponConsumeDetail['toSettleBankcardPay'],
                        'OrderCoupon.status' => \Consts::ORDER_COUPON_STATUS_USED
                    )
                );
            }
            if($ret1 === false || $ret2['code'] != C('SUCCESS')){
                return false;
            }
        }
        return true;
    }

    /**
     * 获得已经使用的优惠券的相关信息
     * @param string $batchCouponCode 优惠券编码
     * @return array 没有数据时返回空数组，有数据返回{'nbrOfDeductionPrice' => 抵扣总金额，'totalPrice' => 消费总金额，'totalPersonAmount' => 消费总人次}
     */
    public function usedCouponInfo($batchCouponCode){
        $consumeList = $this
            ->field(array('userCode', 'UserConsume.consumeCode', 'deduction / '.C('RATIO') => 'deduction', 'realPay / '.C('RATIO') => 'realPay'))
            ->join('UserConsume on UserConsume.consumeCode = UserCoupon.consumeCode')
            ->where(array(
                'UserCoupon.batchCouponCode' => $batchCouponCode,
                'UserCoupon.status' => C('USER_COUPON_STATUS.USED'),
                'UserConsume.status' => C('UC_STATUS.PAYED')
            ))
            ->select();
        if($consumeList) {
            $batchCouponMdl = new BatchCouponModel();
            $batchCouponInfo = $batchCouponMdl->getCouponInfo($batchCouponCode);
            $discountPercent = $batchCouponInfo['discountPercent'];
            $insteadPrice = $batchCouponInfo['insteadPrice'];
            $nbrOfDeductionPrice = 0;
            $totalPrice = 0;
            $userCodeArr = array();
            foreach($consumeList as $v) {
                if($discountPercent == 0) {
                    $nbrOfDeductionPrice = $nbrOfDeductionPrice + $insteadPrice;
                } else {
                    $nbrOfDeductionPrice = $nbrOfDeductionPrice + ($v['deduction'] + $v['realPay'] * $discountPercent);
                }
                $totalPrice = $totalPrice + $v['deduction'] + $v['realPay'];
                if(!in_array($v['userCode'], $userCodeArr)) {
                    $userCodeArr[] = $v['userCode'];
                }
            }
            return array(
                'nbrOfDeductionPrice' => $nbrOfDeductionPrice,
                'totalPrice' => $totalPrice,
                'totalPersonAmount' => count($userCodeArr),
            );
        }
        return array();
    }

    /**
     * 获得用户使用某个商家的优惠券的数量
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @return number
     */
    public function countUserCouponUsed($userCode, $shopCode) {
        $batchCouponMdl = new BatchCouponModel();
        $batchCouponCodeList = $batchCouponMdl->listBatchCouponCode(array('shopCode' => $shopCode));
        if($batchCouponCodeList) {
            $couponUseAmount = $this->where(array('userCode' => $userCode, 'batchCouponCode' => array('IN', $batchCouponCodeList), 'status' => C('USER_COUPON_STATUS.USED')))->count('userCouponCode');
            return $couponUseAmount ? $couponUseAmount : 0;
        }
        return 0;
    }

    /**
     * 获得某一批次优惠券的消费信息
     * @param string $batchCouponCode 优惠券编码
     * @param object $page 页码
     * @return array
     */
    public function getCouponConsumeList($batchCouponCode, $page) {
        return $this->field(array(
            'UserCoupon.userCouponCode' => 'couponNbr',
            'insteadPrice / '.C('RATIO') => 'insteadPrice',
            'User.nickName' => 'userName',
            '(UserConsume.realPay + UserConsume.deduction) / '.C('RATIO') => 'consumeAmount',
            'UserConsume.consumeTime'
        ))
            ->join('BatchCoupon on BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
            ->join('User on User.userCode = UserCoupon.userCode')
            ->join('UserConsume on UserConsume.consumeCode = UserCoupon.consumeCode')
            ->where(array(
                'UserCoupon.batchCouponCode' => $batchCouponCode,
                'UserCoupon.status' => C('USER_COUPON_STATUS.USED')))
            ->pager($page)
            ->select();
    }

    /**
     * 统计某一批次优惠券的消费记录总数
     * @param string $batchCouponCode 优惠券编码
     * @return int
     */
    public function countCouponConsume($batchCouponCode) {
        return $this
            ->join('BatchCoupon on BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
            ->join('User on User.userCode = UserCoupon.userCode')
            ->join('UserConsume on UserConsume.consumeCode = UserCoupon.consumeCode')
            ->where(array(
                'UserCoupon.batchCouponCode' => $batchCouponCode,
                'UserCoupon.status' => C('USER_COUPON_STATUS.USED')))
            ->count();
    }

    /**
     * 获得顾客还未使用且有效的优惠券的数量
     * @param string $userCode 用户编码
     * @return number $notUsedCouponNbr
     */
    public function countUserNotUsedCoupon($userCode) {
        $nowTime = time();
        $sql = '(validityPeriod = 0) OR (BatchCoupon.startUsingTime <= NOW() AND BatchCoupon.expireTime >= NOW() AND BatchCoupon.validityPeriod = -1) OR ((' . $nowTime . ' - UNIX_TIMESTAMP(UserCoupon.applyTime)) / 86400 <= BatchCoupon.validityPeriod)';
        $notUsedCouponNbr = $this
            ->where(array(
                'userCode' => $userCode,
                'UserCoupon.status' => C('USER_COUPON_STATUS.ACTIVE'),
                '_string' => $sql
            ))
            ->join('BatchCoupon on BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
            ->join('Shop on Shop.shopCode = BatchCoupon.shopCode')
            ->count('userCouponCode');
        return $notUsedCouponNbr;
    }

    /**
     * 管理端获得所有用户的优惠券列表
     * @param array $filterData
     * @param Object $page 偏移值
     * @param $field
     * @return null|array 如果为空，返回null；如果不为空，返回二维数组array(array());
     */
    public function listUserCoupon($filterData, $page, $field = '') {
        $where = $this->filterWhere(
            $filterData,
            array('shopName' => 'like', 'realName' => 'like', 'batchNbr' => 'like', 'userCouponNbr' => 'like'),
            $page);

        $where = $this->secondFilterWhere($where);

        //   判断是否为惠圈人员
        if($_SESSION['USER']['bank_id']!=-1){
            $where['Shop.bank_id'] = $_SESSION['USER']['bank_id'];
        }

        if(empty($field)){
            $field = array(
                'UserCoupon.sharedLvl',
                'UserCoupon.applyTime',
                'UserCoupon.userCode',
                'UserCoupon.userCouponCode',
                'UserCoupon.inWeixinCard',
                'UserCoupon.userCouponNbr',
                'UserCoupon.consumeCode',
                'BatchCoupon.couponBelonging',
                'BatchCoupon.couponName',
                'BatchCoupon.expireTime',
                'BatchCoupon.startUsingTime',
                'UserCoupon.status' => 'status',
                'BatchCoupon.couponType',
                'Shop.shopName',
                'User.realName',
                'UserConsume.consumeTime',
                'User.mobileNbr',
                'BatchCoupon.insteadPrice',
                'BatchCoupon.discountPercent',
                'BatchCoupon.payPrice',
                'BatchCoupon.batchNbr',
                'UserConsume.status' => 'consumeStatus',
                'BatchCoupon.function',
                'BatchCoupon.validityPeriod',
                'UserCoupon.orderCouponCode',
                'OrderCoupon.status' => 'orderCouponStatus',
            );
        }
        $userCouponList = $this
            ->field($field)
            ->join('BatchCoupon ON BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode', 'INNER')
            ->join('Shop ON Shop.shopCode = BatchCoupon.shopCode', 'INNER')
            ->join('User ON User.userCode = UserCoupon.userCode', 'LEFT')
            ->join('UserConsume ON UserConsume.consumeCode = UserCoupon.consumeCode', 'LEFT')
            ->join('OrderCoupon ON OrderCoupon.orderCouponCode = UserCoupon.orderCouponCode', 'LEFT')
            ->where($where)
            ->order('applyTime DESC, User.mobileNbr')
            ->pager($page)
            ->select();
        foreach($userCouponList as &$v) {
            $v['insteadPrice'] = $v['insteadPrice'] / C('RATIO');
            $v['payPrice'] = $v['payPrice'] / C('RATIO');
            $v['discountPercent'] =  $v['discountPercent'] / $this->discountRatio;

            // 判断用户优惠券是否过期
            $v['isExpired'] = $this->isUserCouponExpire($v['applyTime'], $v['validityPeriod'], $v['expireTime']) == true ? \Consts::NO : \Consts::YES;
        }
        return $userCouponList;
    }

    /**
     * 判断用户优惠券是否已经过期
     * @param string $applyTime 用户得到优惠券的日期时间，格式：yyyy-mm-dd hh:ii:ss
     * @param int $validityPeriod 优惠券的有效期，单位：天
     * @param string $expireTime 优惠券过期的日期时间，格式：yyyy-mm-dd hh:ii:ss
     * @return boolean 过期返回true，没有过期返回false
     */
    public function isUserCouponExpire($applyTime, $validityPeriod, $expireTime) {
        $ret = false;
        if($validityPeriod > 0) {
            $time = strtotime($applyTime) + $validityPeriod * 86400;
            if(time() > $time) {
                $ret = true;
            }
        } elseif($validityPeriod < 0) {
            if(strtotime($expireTime) < time()){
                $ret = true;
            }
        }
        return $ret;
    }

    /**
     * 管理端获得用户的优惠券总数
     * @param array $filterData
     * @return int
     */
    public function countUserCoupon($filterData) {
        $where = $this->filterWhere(
            $filterData,
            array('shopName' => 'like', 'realName' => 'like', 'batchNbr' => 'like', 'mobileNbr' => 'like', 'userCouponNbr' => 'like')
        );

        $where = $this->secondFilterWhere($where);

        //   判断是否为惠圈人员
        if($_SESSION['USER']['bank_id']!=-1){
            $where['Shop.bank_id'] = $_SESSION['USER']['bank_id'];
        }

        return $this
            ->join('BatchCoupon ON BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode', 'INNER')
            ->join('Shop ON Shop.shopCode = BatchCoupon.shopCode', 'LEFT')
            ->join('User ON User.userCode = UserCoupon.userCode', 'LEFT')
            ->where($where)
            ->count();
    }

    /**
     * 第二次过滤条件
     * @param array $where
     * @return array
     */
    public function secondFilterWhere(&$where) {
        if ($where['status'] || $where['status'] == '0') {
            $where['UserCoupon.status'] = $where['status'];
            unset($where['status']);
        }
        if($where['userCode']) {
            $where['User.userCode'] = $where['userCode'];
            unset($where['userCode']);
        }
        if($where['mobileNbr']) {
            $where['User.mobileNbr'] = $where['mobileNbr'];
            unset($where['mobileNbr']);
        }
        if($where['couponType']) {
            $where['BatchCoupon.couponType'] = $where['couponType'];
            unset($where['couponType']);
        }
        if ($where['applyTimeStart'] && $where['applyTimeEnd']) {
            $where['applyTime'] = array('BETWEEN', array($where['applyTimeStart'].' 00:00:00', $where['applyTimeEnd'].' 23:59:59'));
        } elseif ($where['applyTimeStart'] && !$where['applyTimeEnd']) {
            $where['applyTime'] = array('EGT', $where['applyTimeStart'].' 00:00:00');
        } elseif (!$where['applyTimeStart'] && $where['applyTimeEnd']) {
            $where['applyTime'] = array('ELT', $where['applyTimeEnd'].' 23:59:59');
        }
        unset($where['applyTimeStart']);
        unset($where['applyTimeEnd']);
        return $where;
    }

    /**
     * 判断索取者是否可以索取被索取者的优惠券
     * @param string $sellerCode 被索取者编码
     * @param string $buyerCode 索取者编码
     * @param string $batchCouponCode 优惠券编码
     * @return boolean||number 可以，返回true；不可以，返回错误代码
     */
    public function isUserCouponCanBeExtort($sellerCode, $buyerCode, $batchCouponCode) {
        // 判断被索取者是否拥有有效的优惠券。
        $sellerCouponList = $this->field(array('userCouponCode'))->where(array('userCode' => $sellerCode, 'batchCouponCode' => $batchCouponCode, 'status' => C('USER_COUPON_STATUS.ACTIVE')))->select();
        if(! $sellerCouponList) {
            return C('COUPON.OTHER_SIDE_NO_THE_COUPONS');
        }
        // 判断索取者是否已经达到该优惠券的领用上限。
        $myReceivedCoupon = $this->countMyReceivedCoupon($batchCouponCode, $buyerCode);
        $batchCouponMdl = new BatchCouponModel();
        $ret = $batchCouponMdl->isReachPersonalLimit($batchCouponCode, $myReceivedCoupon);
        if($ret !== false) {
            return $ret;
        }
        return true;
    }

    /**
     * 管理端修改活动状态
     * @param string $couponCode
     * @param int $status
     * @return true|string 成功放回true;失败返回错误信息
     */
    public function changeCouponStatus($userCouponCode, $status) {
        return $this->where(array('userCouponCode' => $userCouponCode))->data(array('status' => $status))->save() ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
    }

    /**
     * 判断用户的优惠券是否可以转增给他人，是否更新优惠券的拥有者
     * @param string $userCode 用户编码
     * @param string $batchCouponCode 优惠券编码
     * @param string $buyerCode 被赠与者编码
     * @return boolean||string 如果可以，true；如果不可以，返回错误代码
     */
    public function isUserCouponCanBeSent($userCode, $batchCouponCode, $buyerCode) {
        // 判断用户是否拥有有效的优惠券
        $userCouponInfo = $this->field(array('userCouponCode'))->where(array('userCode' => $userCode, 'UserCoupon.batchCouponCode' => $batchCouponCode, 'status' => C('USER_COUPON_STATUS.ACTIVE')))->join('BatchCoupon on BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')->find();
        if(! $userCouponInfo) {
            return C('COUPON.USED');
        }
        // 判断该优惠券是否已经过期
        $batchCouponMdl = new BatchCouponModel();
        $ret = $batchCouponMdl->isCouponExpired($batchCouponCode);
        if($ret !== false) {
            return $ret;
        }
        // 更新优惠券的拥有者
        $ret = $this->updateUserCode($userCouponInfo['userCouponCode'], $buyerCode);
        return  $ret == true ? true : $ret;
    }

    /**
     * 更新用户编码
     * @param string $userCouponCode 优惠券编码
     * @param string $userCode 用户编码
     * @return boolean||number 更新成功，返回true；更新失败，返回错误代码
     */
    public function updateUserCode($userCouponCode, $userCode) {
        return  $this->where(array('userCouponCode' => $userCouponCode))->save(array('userCode' => $userCode)) !== false ? true : C('API_INTERNAL_EXCEPTION');
    }

    /**
     * 根据领的优惠券号查询用户等信息
     * @param array $condition {'UserCoupon.userCouponNbr' => $userCouponNbr, 'Shop.shopCode' => $shopCode}
     * @param array $field
     * @return mixed
     */
    public function getUserCouponByUserCouponNbr($condition, $field){
        return $this
            ->field($field)
            ->join('User on User.userCode = UserCoupon.userCode')
            ->join('BatchCoupon on BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
            ->join('Shop on Shop.shopCode = BatchCoupon.shopCode')
            ->where($condition)
            ->find();
    }

    /**
     * 付款时获得用户可使用的平台优惠券列表
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param float $consumeAmount 消费金额
     * @param string $batchCouponCode 商家优惠券编码
     * @param int $couponType 优惠券类型: 3 => 抵扣券，4 => 折扣券
     * @return array $platCouponList 有数据返回二维数组，没有数据返回空数组
     */
    public function listPlatCouponWhenPay1($userCode, $shopCode, $consumeAmount, $couponType, $batchCouponCode = '') {
        if($couponType == C('COUPON_TYPE.REDUCTION')){
            $couponTypeArr = array(C('COUPON_TYPE.REDUCTION'), C('COUPON_TYPE.NEW_CLIENT_REDUCTION'), C('COUPON_TYPE.SEND_INVITER'));
        }elseif($couponType == C('COUPON_TYPE.DISCOUNT')){
            $couponTypeArr = array(C('COUPON_TYPE.DISCOUNT'));
        }else{
            $couponTypeArr = array(C('COUPON_TYPE.DISCOUNT'), C('COUPON_TYPE.REDUCTION'), C('COUPON_TYPE.NEW_CLIENT_REDUCTION'), C('COUPON_TYPE.SEND_INVITER'));
        }
        $nowTime = time();
        $sql = '(validityPeriod = 0) OR (BatchCoupon.startUsingTime <= NOW() AND BatchCoupon.expireTime >= NOW() AND
BatchCoupon.validityPeriod = -1) OR ((' . $nowTime . ' - UNIX_TIMESTAMP(UserCoupon.applyTime)) / 86400 <=
BatchCoupon.validityPeriod)';
        $time = date('H:i:s');
        $where = array(
            'userCode' => $userCode,
            'shopCode' => C('HQ_CODE'),
            'dayStartUsingTime' => array('ELT', $time),
            'dayEndUsingTime' => array(array('GT', $time), array('EQ', '00:00:00'), 'or'),
            'UserCoupon.status' => C('USER_COUPON_STATUS.ACTIVE'),
            'BatchCoupon.couponType' => array('IN', $couponTypeArr),
            '_string' => $sql
        );
        if($consumeAmount > 0) {
            $where['availablePrice'] = array('ELT', $consumeAmount * C('RATIO'));
        }
        if($batchCouponCode){
            $where['BatchCoupon.batchCouponCode'] = $batchCouponCode;
        }
        $platCouponList = $this
            ->field(array(
                'userCouponCode',
                'insteadPrice',
                'discountPercent',
                'availablePrice',
                'limitedNbr',
                'couponType',
                'isUniversal',
                'BatchCoupon.batchCouponCode',
                'BatchCoupon.batchNbr',
                'BatchCoupon.function',
//                'BatchCoupon.exRuleList',
                'BatchCoupon.startUsingTime',
                'BatchCoupon.expireTime',
                'BatchCoupon.dayStartUsingTime',
                'BatchCoupon.dayEndUsingTime',
                'BatchCoupon.isSend'
            ))
            ->join('BatchCoupon ON BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
            ->where($where)
            ->order('availablePrice asc,insteadPrice DESC, discountPercent DESC, UserCoupon.expireTime asc')
            ->select();
        foreach($platCouponList as $k => &$v) {
            if($v['isUniversal'] == C('NO')) {
                $pCouponShopMdl = new PCouponShopModel();
                $isLinkedShop = $pCouponShopMdl->isLinkedShop($v['batchCouponCode'], $shopCode);
                if(!$isLinkedShop) {
                    unset($platCouponList[$k]);
                    continue;
                }
            }

//            $exRuleList = (array)json_decode($v['exRuleList']);
//            unset($v['exRuleList']);
//            $v['exRuleDes'] = '';
//            if($exRuleList){
//                $couponRuleMdl = new CouponRuleModel();
//                $exRuleDes = $couponRuleMdl->listCouponRule(array('ruleCode' => array('IN', $exRuleList)), array('ruleDes'));
//                $v['exRuleDes'] = $exRuleDes[0]['ruleDes'];
//            }
            //转化日期格式
            $v['startUsingTime'] = $this->formatDate1($v['startUsingTime']);
            $v['expireTime'] = $this->formatDate1($v['expireTime']);

            $v['discountPercent'] = $v['discountPercent'] / $this->discountRatio;
            $v['insteadPrice'] = $v['insteadPrice'] / C('RATIO');
            $v['availablePrice'] = $v['availablePrice'] / C('RATIO');
            $v['dayStartUsingTime'] = substr($v['dayStartUsingTime'], 0, 5);
            $v['dayEndUsingTime'] = substr($v['dayEndUsingTime'], 0, 5);

            $result[$v['batchCouponCode']][] = $v;
        }
        $res = array();
        if(isset($result) && $result){
            foreach($result as $r){
                unset($r[0]['userCouponCode']);
                $r[0]['userCount'] = count($r);
                $res[] = $r[0];
            }
        }

        return $res;
    }

    /**
     * 付款时获得用户可使用的优惠券列表
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param float $consumeAmount 消费金额
     * @param int $couponType 优惠券类型
     * @param string $batchCouponCode 商家优惠券编码
     * @return array $userCouponList 有数据返回二维数组，没有数据返回空数组
     */
    public function listUserCouponWhenPay1($userCode, $shopCode, $consumeAmount, $couponType, $batchCouponCode = '') {
        if(empty($couponType)){
            $couponTypeArr = array(\Consts::COUPON_TYPE_DISCOUNT, \Consts::COUPON_TYPE_REDUCTION);
        }else{
            $couponTypeArr = array($couponType);
        }
        $nowTime = time();
        $sql = '(validityPeriod = 0) OR (BatchCoupon.startUsingTime <= NOW() AND BatchCoupon.expireTime >= NOW() AND BatchCoupon.validityPeriod = -1) OR ((' . $nowTime . ' - UNIX_TIMESTAMP(UserCoupon.applyTime)) / 86400 <= BatchCoupon.validityPeriod)';
        $where = array(
            'userCode' => $userCode,
            'shopCode' => $shopCode,
            'dayStartUsingTime' => array('ELT', date('H:i:s', time())),
            'dayEndUsingTime' => array(array('GT', date('H:i:s', time())), array('EQ', '00:00:00'), 'or'),
            'UserCoupon.status' => \Consts::USER_COUPON_STATUS_ACTIVE,
            'BatchCoupon.couponType' => array('IN', $couponTypeArr),
            '_string' => $sql
        );
        if($consumeAmount > 0) {
            $where['availablePrice'] = array('ELT', $consumeAmount * C('RATIO'));
        }
        if($batchCouponCode){
            $where['BatchCoupon.batchCouponCode'] = $batchCouponCode;
        }
        $userCouponList = $this
            ->field(array(
                'userCouponCode',
                'insteadPrice',
                'discountPercent',
                'availablePrice',
                'limitedNbr',
//                'BatchCoupon.exRuleList',
                'BatchCoupon.batchNbr',
                'BatchCoupon.function',
                'BatchCoupon.couponType',
                'BatchCoupon.batchCouponCode',
                'BatchCoupon.startUsingTime',
                'BatchCoupon.expireTime',
                'BatchCoupon.dayStartUsingTime',
                'BatchCoupon.dayEndUsingTime',
                'BatchCoupon.isSend'
            ))
            ->join('BatchCoupon ON BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
            ->where($where)
            ->order('availablePrice asc, insteadPrice DESC, discountPercent DESC, UserCoupon.expireTime asc')

            ->select();

        foreach($userCouponList as &$v) {
//            $exRuleList = (array)json_decode($v['exRuleList']);
//            unset($v['exRuleList']);
//            $v['exRuleDes'] = '';
//            if($exRuleList){
//                $couponRuleMdl = new CouponRuleModel();
//                $exRuleDes = $couponRuleMdl->listCouponRule(array('ruleCode' => array('IN', $exRuleList)), array('ruleDes'));
//                $v['exRuleDes'] = $exRuleDes[0]['ruleDes'];
//            }
            //转化日期格式
            $v['startUsingTime'] = $this->formatDate1($v['startUsingTime']);
            $v['expireTime'] = $this->formatDate1($v['expireTime']);

            $v['discountPercent'] = $v['discountPercent'] / $this->discountRatio;
            $v['insteadPrice'] = $v['insteadPrice'] / C('RATIO');
            $v['availablePrice'] = $v['availablePrice'] / C('RATIO');
            $v['dayStartUsingTime'] = substr($v['dayStartUsingTime'], 0, 5);
            $v['dayEndUsingTime'] = substr($v['dayEndUsingTime'], 0, 5);
            $result[$v['batchCouponCode']][] = $v;
        }
        $res = array();
        if(isset($result) && $result){
            foreach($result as $r){
                unset($r[0]['userCouponCode']);
                $r[0]['userCount'] = count($r);
                $res[] = $r[0];
            }
        }

        return $res;
    }

    /**
     * 获取买单是用户最先领用的用户优惠券编码串，以“|”分隔
     * @param $userCode
     * @param $batchCouponCode
     * @param $nbrCoupon
     * @return string
     */
    public function getAvailableUserCouponCode($userCode, $batchCouponCode, $nbrCoupon){
        $userCouponCode = $this->where(array('userCode' => $userCode, 'batchCouponCode' => $batchCouponCode, 'status'=> C('USER_COUPON_STATUS.ACTIVE')))->order('applyTime asc')->limit($nbrCoupon)->getField('userCouponCode', true);
        return $userCouponCode ? implode('|', $userCouponCode) : '';
    }

    /**
     * 获取某一用户的所有可用的优惠券，如果 $shopCode 不为空，则为获取某一用户拥有某一商家的所有可用优惠券
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param number $status 优惠券状态。1-有效的；2-已使用；3-已失效；
     * @param object $page 页码
     * @param double $longitude 经度
     * @param double $latitude 纬度
     * @return array
     */
    public function listUserCouponByStatus($userCode, $shopCode, $status, $page, $longitude, $latitude){
        $condition = $this->getMyAvailableCouponCondition($status, $userCode, $shopCode);
        $field = array(
            'BatchCoupon.batchCouponCode',
            'BatchCoupon.discountPercent',
            'BatchCoupon.insteadPrice',
            'BatchCoupon.availablePrice',
            'BatchCoupon.exRuleList',
            'BatchCoupon.couponType',
            'BatchCoupon.payPrice',
            'function',
            'dayStartUsingTime',
            'dayEndUsingTime',
            'BatchCoupon.startUsingTime',
            'BatchCoupon.expireTime',
            'BatchCoupon.batchNbr',
            'BatchCoupon.remark',
            'BatchCoupon.couponType',
            'Shop.shopCode',
            'Shop.shopName',
            'Shop.mobileNbr',
            'Shop.street',
            'Shop.logoUrl',
            'Shop.city',
            'OrderCoupon.status'
        );
        if($longitude && $latitude) {
            $field['sqrt(power(Shop.longitude-'.$longitude.',2) + power(Shop.latitude-'.$latitude.',2))'] = 'distance';
        }
        $couponList = $this
            ->field($field)
            ->join('BatchCoupon on BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
            ->join('OrderCoupon ON OrderCoupon.orderCouponCode = UserCoupon.orderCouponCode', 'left')
            ->join('Shop on Shop.shopCode = BatchCoupon.shopCode')
            ->where($condition)
            ->order('expireTime asc, distance asc')
            ->group('BatchCoupon.batchCouponCode')
            ->pager($page)
            ->select();
        if($couponList) {
            foreach($couponList as $k => $v) {
                $couponList[$k]['startUsingTime'] = $this->formatDate1($couponList[$k]['startUsingTime']);
                $couponList[$k]['expireTime'] = $this->formatDate1($couponList[$k]['expireTime']);

                $couponList[$k]['dayStartUsingTime'] = substr($couponList[$k]['dayStartUsingTime'], 0, 5);
                $couponList[$k]['dayEndUsingTime'] = substr($couponList[$k]['dayEndUsingTime'], 0, 5);

                $couponList[$k]['city'] = $this->formatCity($couponList[$k]['city']);

                $exRuleList = (array)json_decode($couponList[$k]['exRuleList']);
                unset($couponList[$k]['exRuleList']);
                $couponList[$k]['exRuleDes'] = '';
                if($exRuleList){
                    $couponRuleMdl = new CouponRuleModel();
                    $exRuleDes = $couponRuleMdl->listCouponRule(array('ruleCode' => array('IN', $exRuleList)), array('ruleDes'));
                    $couponList[$k]['exRuleDes'] = $exRuleDes[0]['ruleDes'];
                }
                $couponList[$k]['availablePrice'] = $couponList[$k]['availablePrice'] / C('RATIO');
                $couponList[$k]['insteadPrice'] = $couponList[$k]['insteadPrice'] / C('RATIO');
                $couponList[$k]['payPrice'] = $couponList[$k]['payPrice'] / C('RATIO');
                $couponList[$k]['distance'] = intval($couponList[$k]['distance'] * C('PROPORTION'));
                $couponList[$k]['discountPercent'] = $couponList[$k]['discountPercent'] / $this->discountRatio;
                $couponList[$k]['userCount'] = $this->countUserCouponByStatus($userCode, $shopCode, $status, $couponList[$k]['batchCouponCode']);
                $couponList[$k]['status'] = $couponList[$k]['status'] ? $couponList[$k]['status'] : '30';
            }
        }
        return $couponList ? $couponList : array();
    }

    /**
     * 统计总记录数
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param number $status 优惠券状态。1-可用，有效的；0-不可用，历史的；
     * @param string $batchCouponCode 批次优惠券编码
     * @return array
     */
    public function countUserCouponByStatus($userCode, $shopCode, $status, $batchCouponCode = ''){
        $condition = $this->getMyAvailableCouponCondition($status, $userCode, $shopCode);
        if($batchCouponCode){
            $condition['BatchCoupon.batchCouponCode'] = $batchCouponCode;
            $couponCount = $this
                ->join('BatchCoupon on BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
                ->join('OrderCoupon ON OrderCoupon.orderCouponCode = UserCoupon.orderCouponCode', 'left')
                ->join('Shop on Shop.shopCode = BatchCoupon.shopCode')
                ->where($condition)
                ->count('userCouponCode');
        }else{
            $couponCount = $this
                ->join('BatchCoupon on BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode')
                ->join('OrderCoupon ON OrderCoupon.orderCouponCode = UserCoupon.orderCouponCode', 'left')
                ->join('Shop on Shop.shopCode = BatchCoupon.shopCode')
                ->where($condition)
                ->count('Distinct(UserCoupon.batchCouponCode)');
        }
        return $couponCount;
    }
}
