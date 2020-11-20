<?php
/**
 * userCard表
 * @author
 */
namespace Common\Model;
use Think\Model;
use Common\Model\ActivityModel;
use Common\Model\CardActionLogModel;
class UserCardModel extends BaseModel {
    protected $tableName = 'UserCard';
    private $discountRatio = 10;

    /**
     * 减少用户会员卡的积分
     * @param string $userCardCode 用户会员卡编码
     * @param int $point 几分数
     * @return boolean 成功返回true，失败返回false
     */
    public function decPoint($userCardCode, $point) {
        return $this->where(array('userCardCode' => $userCardCode))->setDec('point', $point) !== false ? true : false;
    }

    /**
     * 判断用户是否拥有商家的会员卡。若没有，则添加
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @return boolean 有会员卡或者添加会员卡成功或者商家没有会员卡返回true，添加会员卡失败返回false
     */
    public function checkUserCard($userCode, $shopCode) {
        $isUserHasShopCardRet = $this->isUserHasShopCard($userCode, $shopCode);
        if(!$isUserHasShopCardRet) {
            $cardMdl = new CardModel();
            $cardInfo = $cardMdl->getCardInfoByShopCodeAndCardLvl($shopCode, $cardLvl = C('CARD_LVL.WORST'));
            if($cardInfo['cardCode']) {
                $ret = $this->applyCard($cardInfo['cardCode'], $userCode);
                if($ret['code'] == C('API_INTERNAL_EXCEPTION')) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
        return $isUserHasShopCardRet;
    }

    /**
     * 获得用户会员卡详情
     * @param array $where 条件
     * @param array $field 字段
     * @return array
     */
    public function getUserCardInfoByWhere($where, $field) {
        return $this
            ->field($field)
            ->where($where)
            ->find();
    }

    /**
     * 生成用户会员卡的卡号，18位(乔本亮修改)
     * @return string $userCardNbr
     */
    public function createUserCardNbr() {
        $year = date('Y', time());
        $userCardCount = $this->count('userCardCode');
        $strlen = strlen($userCardCount);
        $pre = '';
        for($i = 0; $i < 6 - $strlen; $i++) {
            $pre = '0' . $pre;
        }
//        return substr($year, 2) . $pre . ($userCardCount + 1);
        // 需保留的卡号
        $arrTmp = array('0000000000', '1111111111','2222222222','3333333333','4444444444','5555555555','6666666666','7777777777', '8888888888', '9999999999');
        $chars = '0123456789';
        $length = 10;
        $userCardNbr = substr($year, 2) . $pre . ($userCardCount + 1);
        for($i = 0; $i < $length; $i++) {
            $userCardNbr .= substr($chars, mt_rand(0, 9), 1);
        }
        $data = $this->where(array('cardNbr' => $userCardNbr))->getField('userCardCode');
        if($data || in_array($userCardNbr, $arrTmp)) {
            $this->createUserCardNbr();
        } else {
            return $userCardNbr;
        }
    }

    /**
     * 统计积分
     * @param array $where 查询条件
     * @return int $points
     */
    public function countPoints($where) {
        $points = $this->where($where)->sum('point');
        return $points ? $points : 0;
    }
    /**
     * 获得拥有该会员卡的用户的用户编码列表
     * @param string $cardCode 会员卡编码
     * @return array $userCodeList 无数据则返回空数组
     */
    public function listCardUserCode($cardCode) {
        $userCodeList = $this->where(array('cardCode' => $cardCode))->getField('userCode', true);
        return $userCodeList ? $userCodeList : array();
    }
    /**
     * 为用户添加该商户的会员卡
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @return boolean 添加成功返回true,添加失败返回false;
     */
//    public function addUserCard($userCode, $shopCode) {
//        $cardMdl = new CardModel();
//        $cardInfo = $cardMdl->getCardInfoByShopCodeAndCardLvl($shopCode, $cardLvl = C('CARD_LVL.WORST'));
//        $ret = $this->applyCard($cardInfo['cardCode'], $userCode);
//        return $ret['code'] == C('SUCCESS') ? true : false;
//    }
    /**
     * 获得顾客在某商店拥有的最高级的会员卡
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @return array $userCardInfo
     */
    public function getBestUserCard($userCode, $shopCode) {
        $userCardInfo = $this
            ->field(array('userCardCode', 'cardName', 'Card.cardLvl', 'UserCard.point', 'Card.discount', 'Card.discountRequire', 'Card.pointsPerCash', 'Card.cardCode'))
            ->join('Card on Card.cardCode = UserCard.cardCode')
            ->where(array('UserCard.userCode' => $userCode, 'Card.shopCode' => $shopCode))
            ->order('cardLvl desc')
            ->find();
        return $userCardInfo;
    }

    /**
     * 获取顾客已关注或者未关注商家的会员卡列表
     * @param string $userCode 用户编码
     * @param number $longitude 顾客所在位置经度
     * @param number $latitude 顾客素在位置纬度
     * @param number $isFollowed 顾客是否关注的商店。1-是；2-否
     * @param object $page 页码
     * @return array
     */
    public function getCardList($userCode, $longitude, $latitude, $isFollowed, $page){
        $shopFollowingMdl = new ShopFollowingModel();
        $shopCodeList = $shopFollowingMdl->getShopCode($userCode);
        if( ! $shopCodeList && $isFollowed == C('YES')) return array();
        if($isFollowed == C('YES')) $condition['Card.shopCode'] = array('IN', $shopCodeList);
        if($isFollowed == C('NO') && $shopCodeList) $condition['Card.shopCode'] = array('NOT IN', $shopCodeList);
        $condition['Card.status'] = C('CARD_STATUS.ACTIVE');
        if($userCode){
            $condition['UserCard.userCode'] = $userCode;
        }
        $followCardList = $this->field(array(
            'userCardCode',
            'UserCard.cardCode',
            'cardName',
            'cardNbr',
            'qrCode',
            'barCode',
            'point',
            'cardType',
            'cardLvl',
            'discount',
            'Shop.shopCode',
            'Shop.shopName',
            'Shop.street',
            'Shop.tel',
            'Shop.mobileNbr',
            'sqrt(power(Shop.longitude - '.$longitude.',2) + power(Shop.latitude - '.$latitude.',2))' => 'distance',
            'Shop.logoUrl'
        ))
            ->join('Card on Card.cardCode = UserCard.cardCode')
            ->join('Shop on Shop.shopCode = Card.shopCode')
//            ->join('ShopFollowing on ShopFollowing.shopCode = Shop.shopCode')
            ->where($condition)
            ->pager($page)
            ->order('cardLvl asc, distance desc')
            ->select();
        if($followCardList) {
            $activityMdl = new ActivityModel();
            foreach($followCardList as $k => &$v){
                $v['discount'] = $v['discount'] / $this->discountRatio;
                $condition = array(
                    'shopCode' => $v['shopCode'],
                    'status' => C('ACTIVITY_STATUS.ACTIVE'),
                    'endTime' => array('GT', date('Y-m-d H:i:s', time()))
                );
                $activity = $activityMdl->getActInfo($condition, array('activityName'), array(), $order = 'createTime desc');
                $followCardList[$k]['activityName'] = $activity['activityName'];
            }
            return $followCardList;
        }
        return array();
    }

    /**
     * 获取顾客已关注或者未关注商家的会员卡记录总数
     * @param string $userCode 用户编码
     * @param number $longitude 顾客所在位置经度
     * @param number $latitude 顾客素在位置纬度
     * @param number $isFollowed 顾客是否关注的商店。1-是；2-否
     * @return int
     */
    public function clientCountUserCard($userCode, $longitude, $latitude, $isFollowed) {
        $shopFollowingMdl = new ShopFollowingModel();
        $shopCodeList = $shopFollowingMdl->getShopCode($userCode);
        if( ! $shopCodeList  && $isFollowed == C('YES')) return 0;
        if($isFollowed == C('YES')) $condition['Card.shopCode'] = array('IN', $shopCodeList);
        if($isFollowed == C('NO') && $shopCodeList) $condition['Card.shopCode'] = array('NOT IN', $shopCodeList);
        $condition['Card.status'] = C('CARD_STATUS.ACTIVE');
        if($userCode){
            $condition['UserCard.userCode'] = $userCode;
        }
        $count = $this
            ->join('Card on Card.cardCode = UserCard.cardCode')
            ->join('Shop on Shop.shopCode = Card.shopCode')
            ->where($condition)
            ->count('userCardCode');
        return $count;
    }

    /**
     * 申请会员卡
     * @param string $cardCode 会员编码
     * @param string $userCode 用户编码
     * @return array
     */
    public function applyCard($cardCode, $userCode) {
        if($cardCode == null || $cardCode == '') return $this->getBusinessCode(C('CARD.CARD_CODE_EMPTY'));
        if($userCode == null || $userCode == '') return $this->getBusinessCode(C('USER.USER_CODE_EMPTY'));
        $userCardInfo = $this
            ->field(array('userCardCode'))
            ->where(array('userCode' => $userCode, 'cardCode' => $cardCode))
            ->find();
        if($userCardInfo) {
            return $this->getBusinessCode(C('CARD.REPEAT'));
        }
        $userCardCode = $this->create_uuid();
        $cardNbr = $this->createUserCardNbr();
        $data = array(
            'userCardCode' => $userCardCode,
            'userCode' => $userCode,
            'cardCode' => $cardCode,
            'cardNbr' => $cardNbr,
            'status' => C('CARD_STATUS.ACTIVE'),
            'applyTime'=> date('Y-m-d H:i:s', time())
        );
        $addUserCardRet = $this->add($data);
        if($addUserCardRet !== false) {
            $cardMdl = new CardModel();
            $cardInfo = $cardMdl->getCardInfo($cardCode);
            $shopMdl = new ShopModel();
            $retOfAddNbr = $shopMdl->addVipNbr($cardInfo['shopCode']);
            if(!$retOfAddNbr) {
                return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
            }
            $msgMdl = new MessageModel();
            // 给用户发消息
            $userMsgTitle = str_replace('{{shopName}}', $cardInfo['shopName'], C('MSG_TITLE_TDL.APPLY_CARD'));
            $userMsgContent = str_replace('{{cardName}}', $cardInfo['cardName'], C('CARD_MSG_TDL.GET'));
            $msgInfo = array(
                'msgCode' => $this->create_uuid(),
                'title' => $userMsgTitle,
                'content' => $userMsgContent,
                'createTime' => date('Y-m-d H:i:s'),
                'senderCode' => $cardInfo['shopCode'],
                'type' => C('MESSAGE_TYPE.CARD'),
            );
            $addUserMsgRet = $msgMdl->addMsg($msgInfo, $userCode);
            if(!$addUserMsgRet) {
                return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
            }
            // 给商家发消息
            $userMdl = new UserModel();
            $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('nickName'));
            $shopMsgTitle = str_replace('{{shopName}}', $cardInfo['shopName'], C('MSG_TITLE_TDL.SHOP_SEND_CARD'));
            $shopMsgContent = str_replace(array('{{nickName}}', '{{cardName}}'), array($userInfo['nickName'], $cardInfo['cardName']), C('CARD_MSG_TDL.SHOP_SEND'));
            $msgInfo = array(
                'msgCode' => $this->create_uuid(),
                'title' => $shopMsgTitle,
                'content' => $shopMsgContent,
                'createTime' => date('Y-m-d H:i:s'),
                'senderCode' => $userCode,
                'type' => C('MESSAGE_TYPE.CARD'),
            );
            $code = $msgMdl->addMsg($msgInfo, $cardInfo['shopCode']) ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        } else {
            $code = C('API_INTERNAL_EXCEPTION');
        }
        return $this->getBusinessCode($code);
    }

    /**
     * 获得用户某张会员卡详情
     * @param string $userCardCode 用户会员卡编码
     * @param array $field 要获取的字段值
     * @return array
     */
    public function getUserCardInfo($userCardCode, $field) {
        $userCardInfo = $this->field($field)
            ->join('Card on Card.cardCode = UserCard.cardCode')
            ->join('Shop on Shop.shopCode = Card.shopCode')
            ->join('User on User.userCode = UserCard.userCode')
            ->where(array('userCardCode' => $userCardCode))
            ->find();
        if($userCardInfo['discount']) $userCardInfo['discount'] = $userCardInfo['discount'] / $this->discountRatio;
        return $userCardInfo;
    }

    /**
     * 获得用户某张会员卡一些字段信息
     * @param string $userCode 用户编码
     * @param string $cardCode 会员卡编码
     * @param array $field 要获取的字段值
     * @return array
     */
    public function getUserCardInfoByUC($userCode, $cardCode, $field) {
        return $this->field($field)
            ->join('Card on Card.cardCode = UserCard.cardCode')
            ->join('Shop on Shop.shopCode = Card.shopCode')
            ->join('User on User.userCode = UserCard.userCode')
            ->where(array('UserCard.userCode' => $userCode, 'UserCard.cardCode' => $cardCode))
            ->find();
    }

    /**
     * 某种会员卡的统计信息
     * @param array $cardList 会员卡列表
     * @return array
     */
    public function countCard($cardList){
        $cardActionLogMdl = new CardActionLogModel();
        $oneMonthAgo = strtotime(date('Y-m-d 0:0:0',strtotime('-1 month')));
        $nbrOfVip = $nbrOfNewVip = $amountOfConsumption = $amountOfPoint = $amountOfPoint = 0;
        $cardCodeList = array();
        foreach($cardList as $v) {
            $cardCode = $v['cardCode'];
            $nbrOfVip += $this->where(array('cardCode' => $cardCode))->count('userCardCode');
            $nbrOfNewVip += $this->where('cardCode = "'.$cardCode.'" AND unix_timestamp(applyTime)>='.$oneMonthAgo)->count('userCardCode');
            $amountOfConsumption += $cardActionLogMdl->countConsumeAmount($cardCode);
            $amountOfPoint += $this->where(array('cardCode' => $cardCode))->sum('point');
            $cardCodeList[] = $cardCode;
        }
        $userCardList = $this->field(array('userCode', 'cardCode'))->where(array('cardCode' => array('IN', $cardCodeList)))->select();
        $cardActionLogMdl = new CardActionLogModel();
        $amountOfExpiringPoint = 0;
        foreach($userCardList as $v) {
            $amountOfExpiringPoint += $cardActionLogMdl->countToExpiredPoints($v['userCode'], $v['cardCode']);
        }
        return array(
            'nbrOfVip' => $nbrOfVip,  // 总会员数
            'nbrOfNewVip' => $nbrOfNewVip,  // 近一个月新增会员数
            'amountOfConsumption' => $amountOfConsumption,  // 总消费金额
            'amountOfPoint' => $amountOfPoint,  // 总积分
            'amountOfExpiringPoint' => $amountOfExpiringPoint,  // 三个月内到期积分
        );
    }
    /**
     * 获得某种会员卡会员的优惠券使用数量和抵扣总额
     * @param string $cardCode 会员卡编码
     * @return array
     */
    public function countCardTotalData($cardCode){
        $cardVip = $this
            ->field(array(
                'UserCard.userCode',
                'Card.shopCode',
            ))
            ->join('User on User.userCode = UserCard.userCode', 'left')
            ->join('Card on Card.cardCode = UserCard.cardCode')
            ->where(array('UserCard.cardCode' => $cardCode))
            ->order('applyTime DESC')
            ->select();
        $totalCouponUseAmount = $totalDeductionPrice = 0;
        if($cardVip) {
            $userCouponMdl = new UserCouponModel();
            $cardActionLogMdl = new CardActionLogModel();
            foreach($cardVip as $v){
                $totalCouponUseAmount += $userCouponMdl->countUserCouponUsed($v['userCode'], $v['shopCode']);//使用优惠券的张数
                $totalDeductionPrice += $cardActionLogMdl->countDeductionPrice($cardCode, $v['userCode']);//抵扣金额
            }
        }
        return array(
            'totalCouponUseAmount' => $totalCouponUseAmount,
            'totalDeductionPrice' => $totalDeductionPrice,
        );
    }

    /**
     * 获得某种会员卡的会员信息列表
     * @param string $cardCode 会员卡编码
     * @param string $userName 会员姓名
     * @param int $orderType 排序类型：1-办卡时间；2-消费时间；3-消费金额；4-消费次数。默认按会员消费金额、消费次数依次排序
     * @param object $page 页码
     * @return array
     */
    public function listCardVip($cardCode, $userName, $orderType, $page){
        $where['UserCard.cardCode'] = $cardCode;
        if($userName){
            $where['User.realName'] = array('LIKE', "%$userName%");
        }
        $cardVip = $this
            ->field(array(
                'UserCard.cardNbr',
                'UserCard.userCode',
                'UserCard.cardCode',
                'UserCard.applyTime',
                'User.nickName',
                'User.avatarUrl',
                'Card.shopCode',
            ))
            ->join('User on User.userCode = UserCard.userCode', 'LEFT')
            ->join('Card on Card.cardCode = UserCard.cardCode')
            ->where($where)
            ->order('applyTime DESC')
            ->pager($page)
            ->select();
        if($cardVip) {
            $userCouponMdl = new UserCouponModel();
            $cardActionLogMdl = new CardActionLogModel();
            $userConsumeMdl = new UserConsumeModel();
            foreach($cardVip as &$v){
                $ucInfo = $userConsumeMdl->countUserConsumeInfo(array('consumerCode' =>$v['userCode'], 'location' => $v['shopCode'], 'status' => C('UC_STATUS.PAYED')));
                $v['consumeTimes'] = $ucInfo['consumeTimes'];//消费次数
                $v['consumePriceAmount'] = $ucInfo['consumeAmount'];//总消费金额
                $v['lastActionTime'] = $ucInfo['lastActionTime'];//上次消费时间
                $v['couponUseAmount'] = $userCouponMdl->countUserCouponUsed($v['userCode'], $v['shopCode']);//使用优惠券的张数
                $v['deductionPrice'] = $cardActionLogMdl->countDeductionPrice($v['cardCode'], $v['userCode']);//抵扣金额

                unset($v['applyTime']);
                unset($v['cardCode']);
                unset($v['shopCode']);
            }
            // 按消费时间排序规则
            if($orderType == C('VIP_ORDER_TYPE.ACTION_TIME')) {
                usort($cardVip, function($a, $b){
                    $aLastActionTime = $a['lastActionTime'] == null ? 0 : strtotime($a['lastActionTime']);
                    $bLastActionTime = $b['lastActionTime'] == null ? 0 : strtotime($b['lastActionTime']);
                    if($aLastActionTime == $bLastActionTime) return 0;
                    return $aLastActionTime < $bLastActionTime ? 1 : -1;
                });
            }
            foreach($cardVip as &$v){
                unset($v['lastActionTime']);
            }
            // 按消费金额、消费次数排序
            if($orderType == C('VIP_ORDER_TYPE.CONSUME_AMOUNT')) {
                usort($cardVip, function($a, $b){
                    $aConsumePriceAmount = $a['consumePriceAmount'];
                    $aConsumeTimes = $a['consumeTimes'];
                    $bConsumePriceAmount = $b['consumePriceAmount'];
                    $bConsumeTimes = $b['consumeTimes'];
                    if($aConsumePriceAmount == $bConsumePriceAmount) {
                        if($aConsumeTimes == $bConsumeTimes) return 0;
                        return $aConsumeTimes < $bConsumeTimes ? 1 : -1;
                    }
                    return $aConsumePriceAmount < $bConsumePriceAmount ? 1 : -1;
                });
            }
            // 按消费次数、消费金额排序
            if($orderType == C('VIP_ORDER_TYPE.CONSUME_TIMES')) {
                usort($cardVip, function($a, $b){
                    $aConsumeTimes = $a['consumeTimes'];
                    $aConsumePriceAmount = $a['consumePriceAmount'];
                    $bConsumeTimes = $b['consumeTimes'];
                    $bConsumePriceAmount = $b['consumePriceAmount'];
                    if($aConsumeTimes == $bConsumeTimes) {
                        if($aConsumePriceAmount == $bConsumePriceAmount) return 0;
                        return $aConsumePriceAmount < $bConsumePriceAmount ? 1 : -1;
                    }
                    return $aConsumeTimes < $bConsumeTimes ? 1 : -1;
                });
            }
            return $cardVip;
        }
        return array();
    }

    /**
     * 统计某种会员卡的会员数量
     * @param $cardCode
     * @param $userName
     * @return int
     */
    public function countCardVip($cardCode, $userName) {
        $where['UserCard.cardCode'] = $cardCode;
        if($userName){
            $where['User.realName'] = array('LIKE', "%$userName%");
        }
        $cardVipCount = $this
            ->join('User on User.userCode = UserCard.userCode')
            ->join('Card on Card.cardCode = UserCard.cardCode')
            ->where($where)
            ->count();
        return $cardVipCount;
    }

    /**
     * 获取会员增长走势信息
     * @param string $shopCode 商家编码
     * @return array
     */
    public function listIncreasingTrend($shopCode){
        return $this->field(array('count(userCardCode)' => 'nbr', 'from_unixtime(unix_timestamp(applyTime),"%Y-%m")' => 'month'))
            ->join('Card on Card.CardCode = UserCard.cardCode')
            ->where(array('Card.shopCode' => $shopCode))
            ->group('month')
//            ->order('month')
            ->select();
    }

    /**
     * 升级用户会员卡
     * @param number $newPoint 新的积分
     * @param string $userCardCode 用户会员卡编码
     * @return boolean 成功返回true，失败返回false
     */
    public function updateUserCardPoint($newPoint, $userCardCode){
        $userCardMdl = new UserCardModel();
        $field = array(
            'UserCard.cardCode',
            'Card.forwardPoint',
            'Card.cardLvl',
            'Card.shopCode',
            'User.userCode'
        );
        $userCardInfo = $userCardMdl->getUserCardInfo($userCardCode, $field);

        $cardList = $this->field(array('cardCode', 'cardLvl', 'forwardPoint'))
            ->table('Card')
            ->where(array('shopCode' => $userCardInfo['shopCode']))
            ->select();

        $cl = array();
        foreach($cardList as $v){
            $cl[$v['cardLvl']] = $v;
        }

        if($newPoint >= $userCardInfo['forwardPoint']) {
            $nextLvl = $userCardInfo['cardLvl'] + 1;
            while(isset($cl[$nextLvl])){
                $nextCardCode = $cl[$nextLvl]['cardCode'];
                if($newPoint >= $cl[$nextLvl]['forwardPoint']){
                    $nextLvl++;
                }else{
                    break;
                }
            }
            if(isset($nextCardCode)) {
                $ret = $this->where(array('userCardCode' => $userCardCode))->save(array('point'=>$newPoint,'cardCode'=>$nextCardCode)) !== false ? true : false;
                if(!$ret) return false;
                // 给用户发消息
                $cardMdl = new CardModel();
                $cardInfo = $cardMdl->getCardInfo($nextCardCode);
                $msgMdl = new MessageModel();
                $userMsgTitle = str_replace('{{shopName}}', $cardInfo['shopName'], C('MSG_TITLE_TDL.APPLY_CARD'));
                $userMsgContent = str_replace('{{cardName}}', $cardInfo['cardName'], C('CARD_MSG_TDL.GET'));
                $msgInfo = array(
                    'msgCode' => $this->create_uuid(),
                    'title' => $userMsgTitle,
                    'content' => $userMsgContent,
                    'createTime' => date('Y-m-d H:i:s'),
                    'senderCode' => $cardInfo['shopCode'],
                    'type' => C('MESSAGE_TYPE.CARD'),
                );
                $addUserMsgRet = $msgMdl->addMsg($msgInfo, $userCardInfo['userCode']);
                if(!$addUserMsgRet) return false;
                // 给商家发消息
                $userMdl = new UserModel();
                $userInfo = $userMdl->getUserInfo(array('userCode' => $userCardInfo['userCode']), array('nickName'));
                $shopMsgTitle = str_replace('{{shopName}}', $cardInfo['shopName'], C('MSG_TITLE_TDL.SHOP_SEND_CARD'));
                $shopMsgContent = str_replace(array('{{nickName}}', '{{cardName}}'), array($userInfo['nickName'], $cardInfo['cardName']), C('CARD_MSG_TDL.SHOP_SEND'));
                $msgInfo = array(
                    'msgCode' => $this->create_uuid(),
                    'title' => $shopMsgTitle,
                    'content' => $shopMsgContent,
                    'createTime' => date('Y-m-d H:i:s'),
                    'senderCode' => $userCardInfo['userCode'],
                    'type' => C('MESSAGE_TYPE.CARD'),
                );
                $addShopMsgRet = $msgMdl->addMsg($msgInfo, $cardInfo['shopCode']);
                if(!$addShopMsgRet) return false;
                return true;
            }
        }
        return $this->where(array('userCardCode' => $userCardCode))->save(array('point'=>$newPoint)) !== false ? true : false;
    }

    /**
     * 判断用户是否拥有该商店的会员卡
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @return boolean 有返回true，没有或者商家没有会员卡返回false
     */
    public function isUserHasShopCard($userCode, $shopCode) {
        $cardMdl = new CardModel();
        $cardCodeList = $cardMdl->listCardCode($shopCode);
        if($cardCodeList) {
            return $this->field('cardNbr')->where(array('userCode' => $userCode, 'cardCode' => array('IN', $cardCodeList)))->select() ? true : false;
        }
        return false;
    }

    /**
     * 判断用户是否拥有该商店的会员卡(乔本亮添加)
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @return boolean 有看用户是否添加到微信卡包(false没有 1.有 2.没有)
     */
    public function isWeiXinCard($userCode, $shopCode) {
        $cardMdl = new CardModel();
        $cardCodeList = $cardMdl->listCardCode($shopCode);
        if($cardCodeList) {
            $finCard =   $this->field('cardNbr,inWeixinCard')->where(array('userCode' => $userCode, 'cardCode' => array('IN', $cardCodeList)))->find();
            if($finCard['inWeixinCard']==1){
                return 1;
            }elseif($finCard['inWeixinCard']==2){
                return 2;
            }else{
                return false;
            }
        }
        return false;
    }

    /**
     * 商家获得会员详细信息
     * @param string $userCardCode 用户会员卡
     * @return array
     */
    public function getVipInfo($userCardCode) {
        $vipInfo = $this
            ->field(array('UserCard.userCode','cardCode', 'nickName', 'avatarUrl', 'cardNbr', 'point', 'applyTime'))
            ->join('User on User.userCode = UserCard.userCode')
            ->where(array('userCardCode' => $userCardCode))
            ->find();
        $userCode = $vipInfo['userCode'];
        $cardCode = $vipInfo['cardCode'];
        unset($vipInfo['userCode']);
        unset($vipInfo['cardCode']);
        $cardActionLogMdl = new CardActionLogModel();
        $vipInfo['toExpiredPoints'] = $cardActionLogMdl->countToExpiredPoints($userCode, $cardCode);
        return $vipInfo;
    }


    /**
     * 统计拥有某张会员卡的会员数量
     * @param string $cardCode 会员卡编码
     * @return int 数量
     */
    public function sumVip($cardCode) {
        return $this->where(array('cardCode' => $cardCode))->count('UserCardCode');
    }

    /**
     * 获得所有用户会员卡列表
     * @param array $filterData
     * @param object $page
     * @return array $userCardList 有数据返回二维数组，没有数据返回空数组array()
     */
    public function getUserCardList($filterData, $page) {
        $where = $this->filterWhere(
            $filterData,
            array('realName' => 'like', 'mobileNbr' => 'like'),
            $page);
        $where = $this->secondFilterWhere($where);
        $userCardList = $this
            ->field(array(
                'userCardCode',
                'UserCard.status',
                'applyTime',
                'User.userCode',
                'point',
                'cardName',
                'cardLvl',
                'shopName',
                'Card.shopCode'
            ))
            ->join('User ON User.userCode = UserCard.userCode')
            ->join('Card ON Card.cardCode = UserCard.cardCode')
            ->join('Shop ON Shop.shopCode = Card.shopCode')
            ->where($where)
            ->order('cardLvl desc, point desc')
            ->pager($page)
            ->select();
        if($userCardList) {
            $shopFollowingMdl = new ShopFollowingModel();
            foreach ($userCardList as &$v) {
                $v['isUserFollowedShop'] = $shopFollowingMdl->isUserFollowedShop($v['userCode'], $v['shopCode']) ? C('YES') : C('NO');
            }
        }
        return $userCardList;
    }

    /**
     * 获得所有用户会员卡列表
     * @param array $filterData
     * @param object $page
     * @return array $userCardList 有数据返回二维数组，没有数据返回空数组array()
     */
    public function listUserCard($filterData, $page) {
        $where = $this->filterWhere(
            $filterData,
            array('realName' => 'like', 'mobileNbr' => 'like', 'shopName' => 'like'),
            $page);
        $where = $this->secondFilterWhere($where);
        $userCardList = $this
            ->field(array(
                'userCardCode',
                'UserCard.status',
                'inWeixinCard',
                'applyTime',
                'realName',
                'nickName',
                'User.mobileNbr',
                'User.userCode',
                'sex',
                'point',
                'cardName',
                'cardLvl',
                'shopName',
                'Shop.shopCode',
            ))
            ->join('User ON User.userCode = UserCard.userCode')
            ->join('Card ON Card.cardCode = UserCard.cardCode')
            ->join('Shop ON Shop.shopCode = Card.shopCode')
            ->where($where)
            ->order('cardLvl, applyTime desc')
            ->pager($page)
            ->select();
        if($userCardList) {
            $shopFollowingMdl = new ShopFollowingModel();
            foreach ($userCardList as &$v) {
                $v['isUserFollowedShop'] = $shopFollowingMdl->isUserFollowedShop($v['userCode'], $v['shopCode']) ? C('YES') : C('NO');
            }
        }
        return $userCardList;
    }

    /**
     * 获得所有用户会员卡列表
     * @param array $filterData
     * @return array 有数据返回二维数组，没有数据返回空数组array()
     */
    public function countUserCard($filterData) {
        $where = $this->filterWhere(
            $filterData,
            array('realName' => 'like', 'mobileNbr' => 'like', 'shopName' => 'like')
        );
        $where = $this->secondFilterWhere($where);
        return $this
            ->join('User ON User.userCode = UserCard.userCode')
            ->join('Card ON Card.cardCode = UserCard.cardCode')
            ->join('Shop ON Shop.shopCode = Card.shopCode')
            ->where($where)
            ->count('User.mobileNbr');
    }


    /*
     *通过userCode查询该用户的所有会员卡列表
     * @param array $userCode
     *  * @return array 有数据返回二维数组，没有数据返回空数组array()
     */
    public function  getUserList($userCode,$inWeixinCard){

        if($inWeixinCard<3 &&$inWeixinCard>0){
            $filter = array("UserCard.userCode"=>$userCode,"inWeixinCard"=>$inWeixinCard);
        }else{
            $filter = array("UserCard.userCode"=>$userCode);
        }
         return $this->field(array(
            'userCardCode',
            'UserCard.status',
            'applyTime',
            'realName',
            'nickName',
            'User.mobileNbr',
            'User.userCode',
            'sex',
            'point',
            'cardName',
            'cardLvl',
            'shopName',
            'Shop.shopCode',
             'Shop.logoUrl',
        ))
            ->join('User ON User.userCode = UserCard.userCode')
            ->join('Card ON Card.cardCode = UserCard.cardCode')
            ->join('Shop ON Shop.shopCode = Card.shopCode')
            ->where($filter)
            ->order('cardLvl, applyTime desc')
            ->select();
    }

    /**
     * 第二次过滤条件
     * @param array $where
     * @return array
     */
    public function secondFilterWhere(&$where) {
        if($where['mobileNbr']) {
            $where['User.mobileNbr'] = $where['mobileNbr'];
            unset($where['mobileNbr']);
        }
        if($where['userCode']) {
            $where['User.userCode'] = $where['userCode'];
            unset($where['userCode']);
        }
        return $where;
    }
}