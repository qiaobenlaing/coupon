<?php
namespace Common\Model;
use Think\Model;
use Common\Model\UserCardModel;
use Common\Model\BackgroundTemplateModel;

/**
 * card表
 * @author
 */
class CardModel extends BaseModel {
    protected $tableName = 'Card';
    private $discountRatio = 10;

    /**
     * 计算会员卡抵扣金额
     * @param string $baseAmount 总数基数
     * @param int $discount 折扣 百分数
     * @return int $cardDeduction 会员卡抵扣金额。单位：分
     */
    public function calCardDeduction($baseAmount, $discount) {
        $cardDeduction = $baseAmount - $baseAmount * $discount / C('RATIO');
        return $cardDeduction;
    }

    /**
     * 为searchCard、countSearchCard拼装$condition
     * @param string $searchWord 关键词
     * @param string $city 城市
     * @param string $userCode 用户编码
     * @param int $isFollowed 是否获得用户关注的商家。1-是，0-否
     * @return array $condition
     */
    public function getCondition($searchWord, $city, $userCode, $isFollowed) {
        $condition['Shop.status'] = C('SHOP_STATUS.ACTIVE');
        $subCondition['shopName'] = array('like','%'.$searchWord.'%');
        $subCondition['street'] = array('like', '%'.$searchWord.'%');
        $subCondition['_logic'] = 'or';
        $condition['_complex'] = $subCondition;
        if($city == '义乌市') {
            $condition['district'] = $city;
        } else {
            $condition['city'] = $city;
        }
        $condition['cardLvl'] = C('CARD_LVL.WORST');
        $shopFollowMdl = new ShopFollowingModel();
        $userFollowedShopList = $shopFollowMdl->getShopCode($userCode);
        if($isFollowed == C('YES')) {
            $condition['Shop.shopCode'] = array('IN', $userFollowedShopList);
        } else {
            $condition['Shop.shopCode'] = array('NOTIN', $userFollowedShopList);
        }
        return $condition;
    }

    /**
     * 顾客端搜索会员卡
     * @param string $searchWord 关键字，商店名称
     * @param string $city 城市
     * @param float $longitude 用户所在经度
     * @param float $latitude 用户所在维度
     * @param string $userCode 用户编码
     * @param object $page 页码
     * @param int $isFollowed 是否获得用户关注的商家。1-是，0-否
     * @return array
     */
    public function searchCard($searchWord, $city, $longitude, $latitude, $userCode, $page, $isFollowed) {
        if($longitude == '') {
            return $this->getBusinessCode(C('USER.LONGITUDE_EMPTY'));
        }
        if($latitude == '') {
            return $this->getBusinessCode(C('USER.LATITUDE_EMPTY'));
        };
        $condition = $this->getCondition($searchWord, $city, $userCode, $isFollowed);
        $field = array(
            'Shop.shopCode',
            'Shop.shopName',
            'country',
            'province',
            'city',
            'Shop.street',
            'sqrt(power(Shop.longitude-'.$longitude.',2) + power(Shop.latitude-'.$latitude.',2))' => 'distance',
            'logoUrl',
            'Shop.creditPoint',
            'Shop.type',
            'Shop.isOuttake',
            'Shop.isOrderOn',
            'cardCode',
        );
        $cardList = $this
            ->field($field)
            ->join('Shop ON Shop.shopCode = Card.shopCode')
            ->where($condition)
            ->order('distance asc, creditPoint desc')
            ->pager($page)
            ->select();
        if($cardList) {
            $userCardMdl = new UserCardModel();
            foreach($cardList as &$v) {
                $v['distance'] = intval($v['distance'] * C('PROPORTION'));
                //得到用户是否拥有商店的会员卡
                $v['isUserHasCard'] = $userCode ? $userCardMdl->isUserHasShopCard($userCode, $v['shopCode']) ? C('YES') : C('NO') : C('NO');
            }
            return $cardList;
        }
        return array();
    }

    /**
     * 顾客端统计会员卡总量
     * @param string $searchWord 关键字，商店名称
     * @param string $city 城市
     * @param float $longitude 用户所在经度
     * @param float $latitude 用户所在维度
     * @param string $userCode 用户编码
     * @param int $isFollowed 是否获得用户关注的商家。1-是，0-否
     * @return array
     */
    public function countSearchCard($searchWord, $city, $longitude, $latitude, $userCode, $isFollowed) {
        if($longitude == '') {
            return $this->getBusinessCode(C('USER.LONGITUDE_EMPTY'));
        }
        if($latitude == '') {
            return $this->getBusinessCode(C('USER.LATITUDE_EMPTY'));
        };
        $condition = $this->getCondition($searchWord, $city, $userCode, $isFollowed);
        $cardCount = $this
            ->join('Shop ON Shop.shopCode = Card.shopCode')
            ->where($condition)
            ->count('cardCode');
        return $cardCount ? $cardCount : 0;
    }

    /**
     * 分析优惠券，得到商户数量，会员数量，会员积分，增长数量，增长率，累计消费金额，消费次数
     * @param array $condition 条件
     * @return array
     */
    public function analysisCard($condition) {

        $condition = $this->filterWhere($condition, array('city' => 'like'));
        $where = array();

        //        判断是否为惠圈管理人员
        if($_SESSION['USER']['bank_id']!=-1){
            $where['Shop.bank_id'] = $_SESSION['USER']['bank_id'];
        }


        if($condition['time']) {
            $where['Card.createTime'] = array('EGT', date('Y-m-d H:i:s', time() - $condition['time'] * 86400));
        }
        if($condition['city']) {
            $where['Shop.city'] = $condition['city'];
        }
        if($condition['type']) {
            $shopTypeRelMdl = new ShopTypeRelModel();
            $shopCodeArr = $shopTypeRelMdl->getFieldList(array('typeValue' => $condition['type']), 'shopCode', array(array('joinTable' => 'ShopType', 'joinCondition' => 'ShopType.shopTypeId = ShopTypeRel.typeId', 'joinType' => 'inner')));
            if(empty($shopCodeArr)){
                $shopCodeArr = array('0');
            }
            $shopCodeArr = array_unique($shopCodeArr);
            $where['Shop.shopCode'] = array('IN', $shopCodeArr);
        }
        $data['shopCount'] = $this->countShop($where);
        if($condition['cardType']) {
            $where['cardName'] = $condition['cardType'];
        }
        if($condition['sex']) {
            $where['sex'] = $condition['sex'];
        }
        $data['vipCount'] = $this->countVip($where);

        $cardList = $this
            ->field(array('distinct(Card.cardCode)'))
            ->join('Shop ON Shop.shopCode = Card.shopCode')
            ->join('UserCard ON UserCard.cardCode = Card.cardCode')
            ->join('User ON User.userCode = UserCard.userCode')
            ->where($where)
            ->select();
        $cardCodeList = array();
        foreach($cardList as $v) {
            $cardCodeList[] = $v['cardCode'];
        }
        $vipPoints = $this->countVipPoints($where);
        $data['vipPoints'] = $vipPoints ? $vipPoints : 0;
        //增长数量

        //增长率
        //累计消费金额
        $cardActionLogMdl = new CardActionLogModel();
        $data['consumeAmount'] = $cardActionLogMdl->getALLConsumeAmount($cardCodeList);
        //消费次数
        $data['consumeCount'] = $cardActionLogMdl->getALLConsumeCount($cardCodeList);
        return $data;
    }

    /**
     * 统计发放会员卡的商家数量
     * @param array $where 条件
     * @return number
     */
    public function countShop($where) {
        return $this
            ->where($where)
            ->join('Shop ON Shop.shopCode = Card.shopCode')
            ->count('distinct(Shop.shopCode)');
    }

    /**
     * 统计领取总量和用总量
     * @param array $where 条件
     * @return number
     */
    public function countVip($where) {
        return $this
            ->where($where)
            ->join('Shop ON Shop.shopCode = Card.shopCode')
            ->join('UserCard ON UserCard.cardCode = Card.cardCode')
            ->join('User ON User.userCode = UserCard.userCode')
            ->count('userCardCode');
    }

    /**
     * 统计会员积分
     * @param array $where 条件
     * @return number
     */
    public function countVipPoints($where) {
        return $this
            ->where($where)
            ->join('Shop ON Shop.shopCode = Card.shopCode')
            ->join('UserCard ON UserCard.cardCode = Card.cardCode')
            ->join('User ON User.userCode = UserCard.userCode')
            ->sum('UserCard.point');
    }

    /**
     * 获得商家最低等级卡的信息
     * @param string $shopCode 商家编码
     * @param int $cardLvl 卡的等级。
     * @return array
     */
    public function getCardInfoByShopCodeAndCardLvl($shopCode, $cardLvl) {
        return $this->field(array('cardCode'))->where(array('shopCode' => $shopCode, 'cardLvl' => $cardLvl))->find();
    }
    /**
     * 获得会员卡编码列表
     * @param string $shopCode 商家编码
     * @return array 有数据返回一维数组，没有数据返回空
     */
    public function listCardCode($shopCode) {
        return $this->where(array('shopCode' => $shopCode))->getField('cardCode', true);
    }
    /**
     * 获得商家会员卡
     * @param string $shopCode 商家编码
     * @param object $page 页码
     * @param $firstCardLvl
     * @return array
     */
    public function getGeneralCardStastics($shopCode, $page, $firstCardLvl = 0) {
        if($firstCardLvl) {
            $limit = 1;
        } else {
            $limit = 0;
        }
        $cardList = $this->field(array(
            'cardName',
            'cardCode',
            'cardLvl',
            'discountRequire',
            'discount',
            'pointsPerCash',
            'outPointsPerCash',
            'pointLifeTime',
            'url'
        ))
            ->join('BackgroundTemplate on BackgroundTemplate.bgCode = Card.bgImgCode', 'LEFT')
            ->where(array(
                'shopCode' => $shopCode
            ))
            ->order('unix_timestamp(Card.createTime) desc')
            ->order('cardLvl asc')
            ->pager($page)
            ->limit($limit)
            ->select();
        if($cardList) {
            $userCardMdl = new UserCardModel();
            $userConsumeMdl = new UserConsumeModel();
            foreach($cardList as &$v) {
                $v['discount'] = $v['discount'] / $this->discountRatio;
                $cardCode = $v['cardCode'];
                $vipNbr = $userCardMdl->sumVip($cardCode);
                $v['vipNbr'] = $vipNbr;
                $userCodeList = $userCardMdl->listCardUserCode($cardCode);
                if($userCodeList) {
                    $consumeAmountCount = $userConsumeMdl->sumConsumeField(array('consumerCode' => array('IN', $userCodeList), 'location' => $shopCode, 'status' => C('UC_STATUS.PAYED')), 'deduction + realPay') / C('RATIO');
                    $consumeAmountCount = number_format($consumeAmountCount, 2, '.', '');
                    $v['consumeAmountCount'] = $consumeAmountCount ? $consumeAmountCount : 0; // 消费金额

                    $couponUsedAmount = $userConsumeMdl->countCouponUsed(array('consumerCode' => array('IN', $userCodeList), 'location' => $shopCode, 'status' => C('UC_STATUS.PAYED')));
                    $couponUsedAmount = number_format($couponUsedAmount, 0, '', '');
                    $v['couponUsedAmount'] = $couponUsedAmount ? $couponUsedAmount : 0; // 优惠券使用张数

                    $consumeTimes = $userConsumeMdl->countConsumeTimes(array('consumerCode' => array('IN', $userCodeList), 'location' => $shopCode, 'status' => C('UC_STATUS.PAYED')));
                    $consumeTimes = number_format($consumeTimes, 0, '', '');
                    $v['consumeTimes'] = $consumeTimes ? $consumeTimes : 0; // 消费次数

                    $points = $userCardMdl->countPoints(array('userCode' => array('IN', $userCodeList), 'cardCode' => $cardCode));
                    $points = number_format($points, 0, '', '');
                    $v['points'] = $points ? $points : 0; // 积分数
                }


            }
        }
        return $cardList;
    }

    /**
     * 管理端获得某种会员卡详情
     * @param string $cardCode 用户会员卡编码
     * @return array
     */
    public function getCardInfo($cardCode) {
        return $this
            ->field(array('Shop.shopCode', 'Shop.shopName', 'Card.*', 'BackgroundTemplate.url'))
            ->join('Shop on Shop.shopCode = Card.shopCode')
            ->join('BackgroundTemplate on BackgroundTemplate.bgCode = Card.bgImgCode', 'LEFT')
            ->where(array(
                'cardCode' => $cardCode
            ))
            ->find();
    }

    /**
     * 编辑会员卡
     * @param array $cardInfo 卡信息
     * @return array
     */
    public function editCard($cardInfo) {

        $rules = array(
            array('cardName', 'require', C('CARD.NAME_ERROR')),
            array('cardType', 'require', C('CARD.TYPE_ERROR')),
            array('cardType', 'number', C('CARD.TYPE_ERROR')),
            array('creatorCode', 'require', C('CARD.CREATOR_CODE_ERROR')),
            array('shopCode', 'require', C('SHOP.SHOP_CODE_ERROR')),
            array('discountRequire', 'require', C('CARD.DISCOUNT_REQUIRE_ERROR')),
            array('pointLifetime', 'require', C('CARD.POINT_LIFE_TIME_ERROR')),
            array('pointsPerCash', 'require', C('CARD.POINTS_PER_CASH_ERROR')),
            array('outPintsPerCash', 'require', C('CARD.OUT_POINTS_PER_CASH_ERROR')),
        );
        if ($this->validate($rules)->create($cardInfo) != false) {

            $backgroundTemplateMdl = new BackgroundTemplateModel();
            if ($cardInfo['url']) {
                $creatorCode = $cardInfo['creatorCode'] ? $cardInfo['creatorCode'] : $cardInfo['shopCode'];
                $bgCode = $backgroundTemplateMdl->addBackgroundTemplate($cardInfo['url'], $creatorCode, C('BACKGROUND_TYPE.CARD'));
                $cardInfo['bgImgCode'] = $bgCode == C('API_INTERNAL_EXCEPTION') ? '' : $bgCode;
            }
            unset($cardInfo['url']);

            $cardInfo['status'] = C('CARD_STATUS.ACTIVE');
            // 将以竖线分割的字符串转化成数组
            $cardInfo = $this->strToArray($cardInfo, array('cardName', 'cardLvl', 'discountRequire', 'discount', 'pointLifetime', 'pointsPerCash', 'outPointsPerCash'));
            if (! $this->isArrayNumber($cardInfo['cardLvl'])) {
                return $this->getBusinessCode(C('CARD.CARD_LVL_ERROR'));
            }
            if (! $this->isArrayNumber($cardInfo['discountRequire'])) {
                return $this->getBusinessCode(C('CARD.DISCOUNT_REQUIRE_ERROR'));
            }
            if (! $this->isArrayNumber($cardInfo['discount'])) {
                return $this->getBusinessCode(C('CARD.DISCOUNT_ERROR'));
            }
            if (! $this->isArrayNumber($cardInfo['pointLifeTime'])) {
                return $this->getBusinessCode(C('CARD.POINT_LIFE_TIME_ERROR'));
            }
            if (! $this->isArrayNumber($cardInfo['pointsPerCash'])) {
                return $this->getBusinessCode(C('CARD.POINTS_PER_CASH_ERROR'));
            }

            if(isset($cardInfo['cardCode']) && $cardInfo['cardCode']) {
                // 将数组转化回字符串
                $cardInfo = $this->arrayToStr($cardInfo, array('cardName', 'cardLvl', 'discountRequire', 'discount', 'pointLifetime', 'pointsPerCash', 'outPointsPerCash'));
                $code = $this->where(array('cardCode' => $cardInfo['cardCode']))->save($cardInfo) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            } else {
                /** @var $cardName*/
                /** @var $cardLvl*/
                /** @var $discountRequire*/
                /** @var $discount*/
                /** @var $pointLifetime*/
                /** @var $pointsPerCash*/
                /** @var $outPointsPerCash*/
                /** @var $pointsPerCash*/
                extract($cardInfo);
                $arrTmp = array(
                    'cardName',
                    'cardLvl',
                    'discountRequire',
                    'discount',
                    'pointLifetime',
                    'pointsPerCash',
                    'outPointsPerCash',
                    'pointsPerCash',
                );
                foreach($arrTmp as $v) {
                    unset($cardInfo[$v]);
                }
                foreach ($cardLvl as $k => $v) {
                    $cardInfo['cardName'] = $cardName[$k];
                    $cardInfo['cardLvl'] = $v;
                    if($v == C('CARD_LVL.BEST')) {
                        $cardInfo['forwardPoint'] = 0;
                    } else {
                        $cardInfo['forwardPoint'] = $discountRequire[$k+1];
                    }
                    $cardInfo['createTime'] = date('Y-m-d H:i:s', time());
                    $cardInfo['discountRequire'] = $discountRequire[$k];
                    $cardInfo['discount'] = $discount[$k];
                    $cardInfo['pointLifetime'] = $pointLifetime[$k];
                    $cardInfo['pointsPerCash'] = $pointsPerCash[$k];
                    $cardInfo['outPointsPerCash'] = $outPointsPerCash[$k];
                    $aCreateTime = $this->where(array('shopCode' => $cardInfo['shopCode'], 'cardLvl' => $v))->getField('createTime', true);
                    if($aCreateTime) {
                        $createTime = strtotime($aCreateTime[0]);
                        // 距上次添加或修改会员卡不到1小时，不得进行修改
                        // 测试时，定为2秒
                        $limitTime = 2;
                        if(time() - $createTime < $limitTime) {
                            return $this->getBusinessCode(C('CARD.TIME_LIMIT'));
                        }
                        $ret = $this->where(array('shopCode' => $cardInfo['shopCode'], 'cardLvl' => $v))->save($cardInfo);
                        if($ret === false) {
                            return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
                        }
                    } else {
                        // 没有会员卡则新增
                        $cardInfo['cardCode'] = $this->create_uuid();
                        if ($this->add($cardInfo) == false){
                            return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
                        }
                        unset($cardInfo['cardCode']);
                    }
                }
                $code = C('SUCCESS');
            }
            return $this->getBusinessCode($code);
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 将需要转化成数组的字符串元素转化为数组
     * @param array $cardInfo 会员卡信息
     * @param array $element 需要进行操作的元素数组。例如：array('cardName', 'cardLvl')
     * @return array $cardInfo 执行转化后的会员卡信息
     */
    private function strToArray($cardInfo, $element) {
        foreach($element as $v) {
            if($cardInfo[$v]) {
                $cardInfo[$v] = explode("|", $cardInfo[$v]);
            }
        }
        return $cardInfo;
    }

    /**
     * 将需要转化成字符串的数组元素转化为字符串
     * @param array $cardInfo 会员卡信息
     * @param array $element 需要进行操作的元素数组。例如：array('cardName', 'cardLvl')
     * @return array $cardInfo 执行转化后的会员卡信息
     */
    private function arrayToStr($cardInfo, $element) {
        foreach($element as $v) {
            if($cardInfo[$v]) {
                $cardInfo[$v] = $cardInfo[$v][0];
            }
        }
        return $cardInfo;
    }

    /**
     * 会员卡列表
     * @param array $filterData
     * @param object $page
     * @return null|array 如果为空，返回null；如果不为空，返回二维数组array(array());
     */
    public function listCard($filterData, $page) {
        $where = $this->filterWhere($filterData, array(
            'shopName' => 'like'
        ), $page);

//        判断是否为惠圈人员
        if($_SESSION['USER']['bank_id']!=-1){
            $where['Shop.bank_id'] = $_SESSION['USER']['bank_id'];
        }

        $this->secondFilterWhere($where);
        return $this->field(array(
            'shopName',
            'Card.cardCode' => 'cardCode',
            'cardName',
            'pointLifetime',
            'createTime',
            'discount',
            'Card.status' => 'status',
            'cardLvl',
            'discountRequire',
            'pointsPerCash',
            'outPointsPerCash',
        ))
            ->join('Shop ON Shop.shopCode = Card.shopCode', 'LEFT')
            ->where($where)
            ->order('createTime desc, shopName, cardLvl asc')
            ->pager($page)
            ->select();
    }


    /**
     * 会员卡总数
     *
     * @param array $filterData
     * @return null|int 如果为空，返回null；如果不为空，返回number;
     */
    public function countCard($filterData) {
        $where = $this->filterWhere($filterData, array(
            'shopName' => 'like'
        ));
//        判断是否为惠圈人员
        if($_SESSION['USER']['bank_id']!=-1){
            $where['Shop.bank_id'] = $_SESSION['USER']['bank_id'];
        }

        $this->secondFilterWhere($where);
        return $this
            ->join('Shop ON Shop.shopCode = Card.shopCode', 'LEFT')
            ->where($where)
            ->count();
    }

    /**
     * 第二次过滤$where中的条件
     * @param array $where
     * @return array
     */
    public function secondFilterWhere(&$where) {
        if($where['shopCode']){
            $where['Shop.shopCode'] = $where['shopCode'];
            unset($where['shopCode']);
        }
        if ($where['status'] || $where['status'] == '0') {
            $where['Card.status'] = $where['status'];
            unset($where['status']);
        }
        return $where;
    }

    /**
     * 管理端修改会员卡状态
     * @param string $cardCode
     * @param int $status
     * @return true|string 成功放回true;失败返回错误信息
     */
    public function changeCardStatus($cardCode, $status) {
        return $this->where(array('CardCode' => $cardCode))->data(array('status' => $status))->save() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
    }

    /**
     * 统计今日新增会员阿卡
     * @return 0|number 如果为空，返回0；如果不为空，返回number
     */
    public function todayAddCard() {
        $date = date('Y-m-d');
        $where['createTime'] = array('Like', '%'.$date.'%');
        return $this->where($where)->count();
    }

    /**
     * 判断数组里的元素是否都为数字
     * @param array $data 一维数组
     * @return bool 是返回true，不是返回false
     */
    private function isArrayNumber($data) {
        foreach ($data as $v) {
            if (!is_numeric((int)$v) || (int)$v < 0)
                return false;
        }
        return true;
    }
}