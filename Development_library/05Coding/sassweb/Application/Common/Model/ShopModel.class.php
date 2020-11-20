<?php
namespace Common\Model;
use Think\Model;
use Common\Model\SmsModel;
use Think\Page;

/**
 * shop表
 * @author
 */
class ShopModel extends BaseModel {
    const NO_TYPE = 0;
    const LIMIT_DISTANCE = 500;
    protected $tableName = 'Shop';

    public static $shopTypeArray = array(
        1 => '美食',
        2 => '咖啡',
        3 => '健身',
        4 => '娱乐',
        5 => '服装',
        6 => '其他',
        11 => '平台',
        21 => '工商银行',
    );

    /**
     * 商家是否可以支付
     * @param string $isSuspended 是否暂停营业。1-暂停营业，0-没有暂停营业
     * @param array $businessHours 营业时间，格式：{{'open' => '08:00', 'close' => '11:00'}, ...}
     * @param string $isAcceptBankCard 是否开启惠支付
     * @param string $shopCode 商家编码
     * @return boolean $ret 可以支付返回true，不可以支付返回false
     */
    public function ifCanPay($isSuspended, $businessHours, $isAcceptBankCard = '1', $shopCode) {
        $ret = false;
        // 如果开启惠支付
        if($isAcceptBankCard == \Consts::YES) {
            // 如果没有暂停营业
            if($isSuspended != true) {
                // 判断商户是否发行过优惠券
                $batchCouponMdl = new BatchCouponModel();
                $batchCouponInfo = $batchCouponMdl->getCouponBasicInfo(array('shopCode' => $shopCode), array('batchCouponCode'));
                if($batchCouponInfo) {
                    // 判断在不在营业时间
                    $time = time();
                    foreach($businessHours as $v) {
                        if(strtotime($v['open']) <= $time && $time <= strtotime($v['close'])) {
                            $ret = true;
                        }
                    }
                } else {
                    $ret = true;
                }
            }
        }
        return $ret;
    }

    /**
     * 是否显示惠支付按钮
     * @param string $shopCode 商家编码
     * @return boolean 显示返回true，不显示返回false
     */
    public function isShowPayBtn($shopCode) {
        $shopMdl = new ShopModel();
        $shopInfo = $shopMdl->getShopInfo($shopCode, array('Shop.shopStatus'));
        if($shopInfo['shopStatus'] == \Consts::SHOP_ENTER_STATUS_NO) return false;
        // 需要屏蔽使用惠支付按钮的商户
        $arrHideShopCode = array(
            '2f7a5b30-ea05-18ce-b467-019a7293d4f2', // 诺亚方舟
            '151be9d3-dae8-a77a-a928-32a43572704e', // 东方商厦华庭购物中心
            '9c9ab43a-d093-a6d0-06a3-f65195172607', // 8588咖啡烘焙
            'ef2c630a-7537-17b0-04c0-e01688edfe03', // 戴梦得购物中心
            '2383e415-151a-4c7d-e77b-02c69e5770e0', // 戴梦得购物中心体育店
            'f3f7b158-bb37-b915-ee24-2a52aa6cf8fd', // 彭氏雪莲
            'e1f0257c-708c-a5d1-1d6d-f556812d6b41', // 彭氏雪莲美容养生
            'e5eaee34-f191-c4a8-1af1-8dbd900eb22e', // 彭氏雪莲养生会所
            '348b5ae3-87b9-7694-c6eb-de2744c6563d', // 美尔雅美容养生会所
            '0150be1a-64ef-dd28-9754-f0b61728ab84', // 嘉兴老西安
        );
        return in_array($shopCode, $arrHideShopCode) ? false : true;
    }
    /**
     * 增加商户回头客人数的事务
     * @param string $shopCode 商家编码
     * @param int $orderCount 该顾客在该商户已经有效的订单
     * @return boolean 成功返回true，失败返回false
     */
    public function incRepeatCustomersTransaction($shopCode, $orderCount) {
        return $orderCount == 2 ? $this->incRepeatCustomers($shopCode, 1) : true;
    }

    /**
     * 增加商户回头客人数的事务
     * @param string $shopCode 商家编码
     * @param int $number 数量
     * @return boolean 成功返回true，失败返回false
     */
    public function incRepeatCustomers($shopCode, $number) {
        return $this->where(array('shopCode' => $shopCode))->setInc('repeatCustomers', $number) !== false ? true : false;
    }

    /**
     * 获得某个字段的列表
     * @param string $field 字段
     * @param array $condition 条件
     * @return array $list 一维数组
     */
    public function getShopFieldList($field, $condition) {
        $list = $this->where($condition)->getField($field, true);
        return $list;
    }

    /**
     * 获得商家列表
     * @param array $condition 条件。例：{'shopName' => '美食坊', 'city' => '杭州市', ...}
     * @param array $field 查询的字段。例：{'shopName', 'city', 'shopCode', ...}
     * @param array $joinTableArr 关联的表。例：array('joinTable' => '连接表名', 'joinCondition' => '连接条件', 'joinType' => '连接方式')
     * @param string $order 排序规则
     * @param int $limit 查询条数限制
     * @param int $page 查询页码
     * @return array $shopList 商家列表，二维数组
     */
    public function getShopList($condition = array(), $field = array(), $joinTableArr = array(), $order = '', $limit = 0, $page = 0) {
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
     * 获得商家列表
     * @param array $condition 条件。例：{'shopName' => '美食坊', 'city' => '杭州市', ...}
     * @param array $field 查询的字段。例：{'shopName', 'city', 'shopCode', ...}
     * @param array $joinTableArr 关联的表。例：array('joinTable' => '连接表名', 'joinCondition' => '连接条件', 'joinType' => '连接方式')
     * @param string $order 排序规则
     * @return array $shopList 商家列表，二维数组
     */
    public function getOneShopInfo($condition, $field = array(), $joinTableArr = array(), $order = '') {
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
     * 获得一个随机的商户
     * @return array
     */
    public function getShopInfoRandomly() {
        $shopTypeRelMdl = new ShopTypeRelModel();
        $shopCodeArr = $shopTypeRelMdl->getFieldList(array('typeValue' => array('IN', array(\Consts::SHOP_TYPE_ICBC, \Consts::SHOP_TYPE_PLAT))), 'shopCode', array(array('joinTable' => 'ShopType', 'joinCondition' => 'ShopType.shopTypeId = ShopTypeRel.typeId', 'joinType' => 'inner')));
        if(empty($shopCodeArr)){
            $shopCodeArr = array('0');
        }
        return $this->getOneShopInfo(array('status' => C('SHOP_STATUS.ACTIVE'), 'shopCode' => array('NOTIN', $shopCodeArr)), array('logoUrl', 'shopCode'), array(), 'rand()');
    }

    /**
     * 获得所有商家
     * @param array $field 要选择的字段。例：{'shopName'}
     * @return array $shopList
     */
//    public function listAllShop($field) {
//        $shopType = array(
//            \Consts::SHOP_TYPE_UNKNOWN, // 未知
//            \Consts::SHOP_TYPE_FOOD, // 美食
//            \Consts::SHOP_TYPE_BEAUTY, // 丽人
//            \Consts::SHOP_TYPE_FITNESS, // 健身
//            \Consts::SHOP_TYPE_ENTERTAINMENT, // 娱乐
//            \Consts::SHOP_TYPE_CLOTHING, // 服装
//            \Consts::SHOP_TYPE_OTHER, // 其他
//        );
//        $shopList = $this
//            ->field($field)
//            ->where(array('type' => array('IN', $shopType)))
//            ->order('createDate desc')
//            ->select();
//        return $shopList;
//    }

    /**
     * 检查商家是否设置邀请码，若没有则设置一个邀请码
     * @param string $shopCode 商家编码
     * @return boolean 商家有邀请码或设置成功返回true，设置失败返回false
     */
    public function isShopSetInviteCode($shopCode) {
        $inviteCode = $this->where(array('shopCode' => $shopCode))->getField('inviteCode');
        if(empty($inviteCode)) {
            $inviteCode = $this->setShopInviteCode();
            return $this->where(array('shopCode' => $shopCode))->save(array('inviteCode' => $inviteCode)) !== false ? true : false;
        }
        return true;
    }

    /**
     * 根据用户邀请码获得商家信息
     * @param string $inviteCode 用户邀请码
     * @return array {'shopCode'}或者array()
     */
    public function getShopInfoByInviteCode($inviteCode) {
        return $this->getOneShopInfo(array('inviteCode' => $inviteCode), array('shopCode'));
    }

    /**
     * 获得用户关注或者足迹
     * @param double $longitude 用户经度
     * @param double $latitude 用户纬度
     * @param string $city 城市
     * @param object $page 页码对象
     * @return array $shopList
     */
    public function listNearShop($city, $longitude, $latitude, $page) {
        if(empty($longitude)) {
            return $this->getBusinessCode(C('USER.LONGITUDE_EMPTY'));
        }
        if(empty($latitude)) {
            return $this->getBusinessCode(C('USER.LATITUDE_EMPTY'));
        };
        $field = array(
            'shopCode',
            'shopName',
            'country',
            'province',
            'city',
            'district',
            'street',
            'sqrt(power(Shop.longitude-'.$longitude.',2) + power(Shop.latitude-'.$latitude.',2))' => 'distance',
            'logoUrl',
            'type',
            'popularity',
            'isAcceptBankCard',
            'onlinePaymentDiscount'
        );
        $condition = array(
            'isCompany' => C('NO'),
            'status' => C('SHOP_STATUS.ACTIVE'),
            'type' => array('notin', array(\Consts::SHOP_TYPE_ICBC, \Consts::SHOP_TYPE_PLAT))
        );
        if($city == '义乌市') {
            $condition['district'] = $city;
        } else {
            $condition['city'] = $city;
        }
        $shopList = $this
            ->field($field)
            ->where($condition)
            ->order('distance asc')
            ->pager($page)
            ->select();
        $batchCouponMdl = new BatchCouponModel();
        foreach($shopList as &$v) {
            if(isset($v['shopName'])){
                $v['shopName'] = htmlspecialchars_decode($v['shopName'], ENT_QUOTES);
            }
            $v['distance'] = intval($v['distance'] * C('PROPORTION'));
            $shopCode = $v['shopCode'];
            // 上新
            $shopPhotoMdl = new ShopPhotoModel();
            $v['hasNew'] = $shopPhotoMdl->isShopHasNew($v['shopCode']) ? C('YES') : C('NO');
            $v['newPhotoList'] = $shopPhotoMdl->getNewestPhoto($shopCode);

            // 商店是否有活动
            $v['actCode'] = '';
            $v['actName'] = '';
            $actMdl = new ActivityModel();
            $hasAct = $actMdl->isShopHasAct($v['shopCode']) ? C('YES') : C('NO');
            if($hasAct){
                $newActInfo = $actMdl->getActInfo(array('shopCode' => $shopCode), array('activityCode', 'activityName', 'createTime'), array(), 'createTime desc');
                $v['actCode'] = $newActInfo['activityCode'];
                $v['actName'] =  $newActInfo['activityName'];
            }

            // 检查商店是否存在某类优惠券
            $v['couponType'] = array();
            $tmp = array(C('COUPON_TYPE.N_PURCHASE'), C('COUPON_TYPE.REDUCTION'), C('COUPON_TYPE.DISCOUNT'), C('COUPON_TYPE.PHYSICAL'), C('COUPON_TYPE.EXPERIENCE'));
            foreach($tmp as $couponType) {
                if($batchCouponMdl->isShopHasTheCoupon($shopCode, $couponType)) {
                    $v['couponType'][] = $couponType;
                }
            }
        }
        return $shopList;
    }

    /**
     * 统计用户关注或者足迹
     * @param string $city 城市
     * @return int $shopCount
     */
    public function CountNearShop($city) {
        $condition = array(
            'isCompany' => C('NO'),
            'status' => C('SHOP_STATUS.ACTIVE'),
            'type' => array('notin', array(\Consts::SHOP_TYPE_ICBC, \Consts::SHOP_TYPE_PLAT))
        );
        if($city == '义乌市') {
            $condition['district'] = $city;
        } else {
            $condition['city'] = $city;
        }
        $shopCount = $this
            ->where($condition)
            ->count('Shop.shopCode');
        return $shopCount;
    }

    /**
     * 获得用户关注或者足迹
     * @param string $userCode 用户编码
     * @param double $longitude 用户经度
     * @param double $latitude 用户纬度
     * @param string $isFollowed 是否关注 1-关注，0-不关注
     * @param object $page 页码对象
     * @return array $shopList
     */
    public function listUserShop($userCode, $longitude, $latitude, $isFollowed, $page) {
        if(empty($longitude)) {
            return $this->getBusinessCode(C('USER.LONGITUDE_EMPTY'));
        }
        if(empty($latitude)) {
            return $this->getBusinessCode(C('USER.LATITUDE_EMPTY'));
        };
        $field = array(
            'Shop.shopCode',
            'shopName',
            'country',
            'province',
            'city',
            'district',
            'Shop.street',
            'sqrt(power(Shop.longitude-'.$longitude.',2) + power(Shop.latitude-'.$latitude.',2))' => 'distance',
            'logoUrl',
            'Shop.type',
            'isAcceptBankCard',
            'popularity',
            'onlinePaymentDiscount'
        );
        if($isFollowed == C('YES')) {
            // 获得我关注的商户
            $shopList = $this
                ->field($field)
                ->where(array(
                    'isCompany' => C('NO'),
                    'status' => C('SHOP_STATUS.ACTIVE'),
                    'ShopFollowing.userCode' => $userCode
                ))
                ->join('ShopFollowing ON ShopFollowing.shopCode = Shop.shopCode')
                ->order('distance asc')
                ->pager($page)
                ->select();
        } else {
            // 获得我拥有过优惠券和我消费过的商户
            $userCouponMdl = new UserCouponModel();
            $hadCouponShopCodeList = $userCouponMdl->listHadCouponShopCode($userCode);
            $userConsumeMdl = new UserConsumeModel();
            $consumedShopCodeList = $userConsumeMdl->listConsumedShopCode($userCode);
            $shopCodeList = array_merge($hadCouponShopCodeList, $consumedShopCodeList);
            $condition['Shop.shopCode'] = array('IN', $shopCodeList);
            $condition['Shop.isCompany'] = C('NO');
            $condition['Shop.status'] = C('SHOP_STATUS.ACTIVE');
            $condition['Shop.type'] = array('NOTIN', array(\Consts::SHOP_TYPE_ICBC, \Consts::SHOP_TYPE_PLAT));
            $shopList = $this
                ->field($field)
                ->where($condition)
                ->order('distance asc')
                ->pager($page)
                ->select();
        }
        $batchCouponMdl = new BatchCouponModel();
        foreach($shopList as &$v) {
            if(isset($v['shopName'])){
                $v['shopName'] = htmlspecialchars_decode($v['shopName'], ENT_QUOTES);
            }
            $v['distance'] = intval($v['distance'] * C('PROPORTION'));
            $shopCode = $v['shopCode'];
            // 上新
            $shopPhotoMdl = new ShopPhotoModel();
            $v['hasNew'] = $shopPhotoMdl->isShopHasNew($v['shopCode']) ? C('YES') : C('NO');
            $v['newPhotoList'] = $shopPhotoMdl->getNewestPhoto($shopCode);
            // 商店是否有活动
            $v['actCode'] = '';
            $v['actName'] = '';
            $actMdl = new ActivityModel();
            $hasAct = $actMdl->isShopHasAct($v['shopCode']) ? C('YES') : C('NO');
            if($hasAct){
                $newActInfo = $actMdl->getActInfo(array('shopCode' => $shopCode), array('activityCode', 'activityName', 'createTime'), array(), 'createTime desc');
                $v['actCode'] = $newActInfo['activityCode'];
                $v['actName'] =  $newActInfo['activityName'];
            }




            // 检查商店是否存在某类优惠券
            $v['couponType'] = array();
            $tmp = array(C('COUPON_TYPE.N_PURCHASE'), C('COUPON_TYPE.REDUCTION'), C('COUPON_TYPE.DISCOUNT'), C('COUPON_TYPE.PHYSICAL'), C('COUPON_TYPE.EXPERIENCE'));
            foreach($tmp as $couponType) {
                if($batchCouponMdl->isShopHasTheCoupon($shopCode, $couponType)) {
                    $v['couponType'][] = $couponType;
                }
            }
        }
        return $shopList;
    }

    /**
     * 统计用户关注或者足迹
     * @param string $userCode 用户编码
     * @param string $isFollowed 是否关注 1-关注，0-不关注
     * @return int $shopCount
     */
    public function countUserShop($userCode, $isFollowed) {
        if($isFollowed == C('YES')) {
            $shopCount = $this
                ->where(array(
                    'status' => C('SHOP_STATUS.ACTIVE'),
                    'ShopFollowing.userCode' => $userCode
                ))
                ->join('ShopFollowing ON ShopFollowing.shopCode = Shop.shopCode')
                ->count('Shop.shopCode');
        } else {
            // 获得我拥有过优惠券和我消费过的商户
            $userCouponMdl = new UserCouponModel();
            $hadCouponShopCodeList = $userCouponMdl->listHadCouponShopCode($userCode);
            $userConsumeMdl = new UserConsumeModel();
            $consumedShopCodeList = $userConsumeMdl->listConsumedShopCode($userCode);
            $shopCodeList = array_merge($hadCouponShopCodeList, $consumedShopCodeList);
            $condition['Shop.shopCode'] = array('IN', $shopCodeList);
            $condition['Shop.status'] = C('SHOP_STATUS.ACTIVE');
            $condition['Shop.type'] = array('NOTIN', array(\Consts::SHOP_TYPE_ICBC, \Consts::SHOP_TYPE_PLAT));
            $shopCount = $this
                ->where($condition)
                ->count('Shop.shopCode');
        }
        return $shopCount;
    }

    /**
     * 增加商店的回头客数量，数量+1
     * @param string $shopCode 商家编码
     * @return boolean 成功返回true，失败返回false
     */
    public function increaseShopRepeatCustomers($shopCode) {
        return $this->where(array('shopCode' => $shopCode))->setInc('repeatCustomers') !== false ? true : false;
    }

    /**
     * 增加商店的人气值，数量+1
     * @param string $shopCode 商家编码
     * @return boolean 成功返回true，失败返回false
     */
    public function increaseShopPopularity($shopCode) {
        return $this->where(array('shopCode' => $shopCode))->setInc('popularity') !== false ? true : false;
    }

    /**
     * 删除商家
     * @param string $shopCode 商家编码
     * @return boolean 成功返回true，失败返回false
     */
    public function delShop($shopCode) {
        return $this->where(array('shopCode' => $shopCode))->delete() !== false ? true : false;
    }

    /**
     * 猜你喜欢，获得消费商家同类型的发
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param float $longitude 用户所在经度
     * @param float $latitude 用户所在维度
     * @return array $shopList
     */
    public function guessYouLikeShop($userCode, $shopCode, $longitude, $latitude) {
        $consumeShopInfo = $this->field(array('type', 'city'))->where(array('shopCode' => $shopCode))->find();
        $city = $consumeShopInfo['city'];
        $type = $consumeShopInfo['type'];
        if($longitude == '') {
            return $this->getBusinessCode(C('USER.LONGITUDE_EMPTY'));
        }
        if($latitude == '') {
            return $this->getBusinessCode(C('USER.LATITUDE_EMPTY'));
        };
        $condition['status'] = C('SHOP_STATUS.ACTIVE');
        if($type && $type != self::NO_TYPE) {
            $condition['type'] = $type;
        } else {
            $condition['type'] = array('NOTIN', array(\Consts::SHOP_TYPE_ICBC, \Consts::SHOP_TYPE_PLAT));
        }
        $condition['city'] = array('like', '%'.$city.'%');
        $condition['shopCode'] = array('NEQ', $shopCode);
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
        );

        $shopList = $this->field($field)
            ->where($condition)
            ->order('distance asc, creditPoint desc')
            ->limit('10')
            ->select();
        if($shopList) {
            $batchCouponMdl = new BatchCouponModel();
            $userCouponMdl = new UserCouponModel();
            $userCardMdl = new UserCardModel();
            foreach($shopList as &$v) {
                if(isset($v['shopName'])){
                    $v['shopName'] = htmlspecialchars_decode($v['shopName'], ENT_QUOTES);
                }
                $v['distance'] = intval($v['distance'] * C('PROPORTION'));
                // 得到商户最近一批优惠券
                $couponList = $batchCouponMdl->listAvailabelCoupon($v['shopCode'],
                    array(
                        'batchCouponCode',
                        'insteadPrice',
                        'discountPercent',
                        'createTime',
                    ));

                $v['batchCouponCode'] = $couponList ? $couponList[0]['batchCouponCode'] : '';
                $v['insteadPrice'] = $couponList ? $couponList[0]['insteadPrice'] : '';
                $v['discountPercent'] = $couponList ? $couponList[0]['discountPercent'] : '';
                // 得到用户是否可以抢优惠券
                if($couponList) {
                    $v['isUserCanGrab'] = $userCode ? $userCouponMdl->isUserCanGrabTheCoupon($userCode, $couponList[0]['batchCouponCode']) ? C('YES') : C('NO') : C('YES');
                } else {
                    $v['isUserCanGrab'] = C('NO');
                }
                //得到用户是否拥有商店的会员卡
                $v['isUserHasCard'] = $userCode ? $userCardMdl->isUserHasShopCard($userCode, $v['shopCode']) ? C('YES') : C('NO') : C('NO');
            }
            return $shopList;
        }
        return array();
    }

    /**
     * 根据手机号码查询商家
     * @param string $mobileNbr 手机号码
     * @return array $shopInfo
     */
    public function getShopInfoByMobileNbr($mobileNbr) {
        return $this->getOneShopInfo(array('mobileNbr' => $mobileNbr), array('shopCode', 'status', 'isActivated'));
    }

    /**
     * 商家会员人数加1
     * @param string $shopCode 商家编码
     * @return boolean 成功返回true，失败返回false
     */
    public function addVipNbr($shopCode) {
        return $this->where(array('shopCode' => $shopCode))->setInc('nbrOfMember') !== false ? true : false;
    }

    /**
     * 获得的商家，根据商家名称模糊查询
     * @param string $shopName 商家名字
     * @return array $shopList
     */
    public function getShopByNameLike($shopName) {
        $shopList = $this->field(array('shopName', 'shopCode'))->where(array('shopName' => array('LIKE', "%$shopName%")))->select();
        return $shopList;
    }

    /**
     * 商家注册
     * @param number $mobileNbr 手机号码
     * @param number $validateCode 验证码
     * @param string $password 密码
     * @param string $licenseNbr 营业执照号码
     * @return string
     */
    public function register($mobileNbr, $validateCode, $password, $licenseNbr){
        $smsMdl = new SmsModel();
        //$codeCom = $smsMdl->getCode('r'.$mobileNbr);
        
        $rules = array(
            array('mobileNbr', 'require', C('MOBILE_NBR.EMPTY')),
            array('mobileNbr', 'number', C('MOBILE_NBR.ERROR')),
            array('mobileNbr', C('MOBILE_NBR.LENGTH'), C('MOBILE_NBR.ERROR'), 0, 'length'),
            array('mobileNbr', '', C('MOBILE_NBR.REPEAT'), 0, 'unique'),
            array('password', 'require', C('PWD.EMPTY')),
            array('validateCode',  'require', C('VALIDATE_CODE.EMPTY')),
            array('licenseNbr', 'require', C('LICENSE_NBR.EMPTY')),
//            array('licenseNbr', 15, C('LICENSE_NBR.ERROR'), 0, 'length'),
        );
        $data = array(
            'mobileNbr' => $mobileNbr,
            'password' => $password,
            'validateCode' => $validateCode,
            'licenseNbr' => $licenseNbr,
        );
		$codeCom = 123456;
        if($this->validate($rules)->create($data) != false){
            if($validateCode != $codeCom){
                return $this->getBusinessCode(C('VALIDATE_CODE.ERROR'));
            }
            M()->startTrans();
            $shopCode = $this->create_uuid();
            $data = array(
                'shopCode' => $shopCode,
                'mobileNbr' => $mobileNbr,
                'licenseNbr' => $licenseNbr,
                'status' => C('SHOP_STATUS.DISABLE'),
                'createDate' => date('Y-m-d H:i:s', time()),
                'shopId' => $this->setShopId(),
            );
            if($this->add($data)){
                $staffInfo = array(
                    'mobileNbr' => $mobileNbr,
                    'password' => $password,
                    'status' => C('STAFF_STATUS.DISABLE'),
                    'shopCode' => $shopCode,
                    'registerTime' => date('Y-m-d H:i:s',time()),
                    'userLvl' => C('STAFF_LVL.MANAGER'),
                );
                $shopStaffMdl = new ShopStaffModel();
                $ret = $shopStaffMdl->addStaff($staffInfo);
                $code = $ret['code'];
                if($code == C('SUCCESS')) {
                    $staff = $shopStaffMdl->getShopStaffInfo(array('mobileNbr' => $mobileNbr));
                    $this->where(array('shopCode' => $shopCode))->save(array('ownerCode' => $staff['staffCode']));
                    M()->commit();
                } else {
                    M()->rollback();
                }
            } else {
                $code = C('API_INTERNAL_EXCEPTION');
            }
            return $this->getBusinessCode($code);
        }else{
            return $this->getValidErrorCode();
        }
    }

    /**
     * 商户端激活
     * @param number $mobileNbr 手机号码
     * @param number $validateCode 验证码
     * @param string $password MD5后的密码
     * @return array $ret
     */
    public function activate($mobileNbr, $validateCode, $password,$bank_id) {
        $smsMdl = new SmsModel();
        // 获得缓存中激活的验证码
        //$codeCom = $smsMdl->getCode('r' . $mobileNbr);
		$codeCom = 123456;
        $rules = array(
            array('mobileNbr', 'require', C('MOBILE_NBR.EMPTY')),
            array('mobileNbr', 'number', C('MOBILE_NBR.ERROR')),
            array('mobileNbr', C('MOBILE_NBR.LENGTH'), C('MOBILE_NBR.ERROR'), 0, 'length'),
            array('password', 'require', C('PWD.EMPTY')),
            array('validateCode', 'require', C('VALIDATE_CODE.EMPTY')),
        );
        $data = array(
            'mobileNbr' => $mobileNbr,
            'password' => $password,
            'validateCode' => $validateCode,
            'bank_id' =>$bank_id,
        );
        if ($this->validate($rules)->create($data) != false) {
            if($validateCode != $codeCom) {
                return $this->getBusinessCode(C('VALIDATE_CODE.ERROR'));
            }
            $shopStaffMdl = new ShopStaffModel();
            // 根据员工手机号和商圈信息，获得员工的信息
            $shopStaffInfo = $shopStaffMdl->getShopStaffInfo(array('mobileNbr' => $mobileNbr,"bank_id"=>$bank_id));

            //判断员工已经激活则直接返回提示并退出
            if($shopStaffInfo['status']==\Consts::SHOP_STAFF_STATUS_ACTIVE){
                return array("code"=>"50060","msg"=>"该员工已经激活");
            }

            $staffCode = $shopStaffInfo['staffCode'];
            $staffUpdateData = array(
                'staffCode' => $staffCode, // 员工编码
                'password' =>  md5(substr($staffCode, 0, 6) . $password), // 密码
                'status' => \Consts::SHOP_STAFF_STATUS_ACTIVE, // 状态为启用
                'mobileNbr' => $mobileNbr, // 员工手机号码
            );
            M()->startTrans();
            // 将店员状态改为启用
            $ret = $shopStaffMdl->updateStaff($staffUpdateData);
            $code = $ret['code'];
            if($code == C('SUCCESS')) {
                // 启用店员所属的商家
                $shopStaffRelMdl = new ShopStaffRelModel();
                $shopList = $shopStaffRelMdl->getShopListByStaffCode($staffCode, array('Shop.shopCode'));
                if($shopList) {
                    $shopCodeList = array();
                    foreach($shopList as $shop) {
                        $shopCodeList[] = $shop['shopCode'];
                    }
                    $code = $this->where(array('shopCode' => array('IN', $shopCodeList), 'isActivated' => C('NO')))->save(array('status' => C('SHOP_STATUS.ACTIVE'), 'isActivated' => C('YES'))) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
                }
                if($code == C('SUCCESS')) {
                    M()->commit();
                } else {
                    M()->rollback();
                }
                return $this->getBusinessCode($code);
            }
            return $this->getBusinessCode($code);
        }else{
            return $this->getValidErrorCode();
        }
    }

    /**
     * 搜索商家
     * @param array $condition 查询条件
     * @param array $field 返回字段
     * @param object $page 分页
     * @param array $joinTableArr join表
     * @param string $order 排序
     * @return array
     */
    public function searchShop($condition, $field, $page, $joinTableArr = array(), $order = 'distance asc, Shop.creditPoint desc',$zoneId){

        //乔本亮添加zone_id
        if(isset($zoneId) && !empty($zoneId)){
            $condition['Shop.bank_id'] = $zoneId;
        }

        if((isset($condition['longitude']) && empty($condition['longitude'])) || !isset($condition['longitude'])) {
            return $this->getBusinessCode(C('USER.LONGITUDE_EMPTY'));
        }else{
            $longitude = $condition['longitude'];
            unset($condition['longitude']);
        }
        if((isset($condition['latitude']) && empty($condition['latitude'])) || !isset($condition['latitude'])) {
            return $this->getBusinessCode(C('USER.LATITUDE_EMPTY'));
        }else{
            $latitude = $condition['latitude'];
            unset($condition['latitude']);
        }
        if(isset($condition['searchWord'])){
            $subCondition['Shop.shopName'] = array('like','%'.$condition['searchWord'].'%');
            $subCondition['Shop.street'] = array('like', '%'.$condition['searchWord'].'%');
            $subCondition['_logic'] = 'or';
            $condition['_complex'] = $subCondition;
        }
        unset($condition['searchWord']);
        if(!(isset($condition['type']) && $condition['type'] && $condition['type'] != self::NO_TYPE) || !isset($condition['type'])) {
            $condition['type'] = array('NOTIN', array(\Consts::SHOP_TYPE_ICBC, \Consts::SHOP_TYPE_PLAT));
        }
        if(isset($condition['city'])){
            $city = $condition['city'];
            unset($condition['city']);
            $condition['Shop.city'] = array('like', '%'.$city.'%');
        }
        $userCode = '';
        if(isset($condition['userCode'])){
            $userCode = $condition['userCode'];
            unset($condition['userCode']);
        }
        if(isset($condition['distance'])){
            $condition['sqrt(power(Shop.longitude-'.$longitude.',2) + power(Shop.latitude-'.$latitude.',2))'] = $condition['distance'];
            unset($condition['distance']);
        }
        $condition['Shop.isCompany'] = C('NO'); // 不是公司类型的商户
        $condition['Shop.status'] = C('SHOP_STATUS.ACTIVE'); // 启用状态的商户

        if(empty($field)){
            $field = array(
                'Shop.shopCode',
                'Shop.shopName',
                'Shop.country',
                'Shop.province',
                'Shop.city',
                'Shop.street',
                'sqrt(power(Shop.longitude-'.$longitude.',2) + power(Shop.latitude-'.$latitude.',2))' => 'distance',
                'Shop.logoUrl',
                'Shop.creditPoint',
                'ShopType.typeValue' => 'type',
                'Shop.isOuttake',
                'Shop.isOrderOn',
                'Shop.popularity',
                'Shop.isAcceptBankCard',
                'Shop.onlinePaymentDiscount',
                'Shop.shopStatus',
                'Shop.createDate',
                'Shop.isSuspended',
                'Shop.isCatering'
				//'Shop.isCollect'
            );
        }
        $this->field($field);
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        $this->join('ShopTypeRel on ShopTypeRel.shopCode = Shop.shopCode', 'inner');
        $this->join('ShopType on ShopType.shopTypeId = ShopTypeRel.typeId', 'inner');
        $this->where($condition);
        if(empty($order)){
            $order = 'distance asc, Shop.creditPoint desc';
        }
        $this->group('Shop.shopCode');
        $this->order($order);
        $this->pager($page);
        $shopList = $this->select();
        if($shopList) {
            $batchCouponMdl = new BatchCouponModel();
            $userCouponMdl = new UserCouponModel();
            $userCardMdl = new UserCardModel();
            $userConsumeMdl  = new UserConsumeModel();
            $isFirst = $userConsumeMdl->isFirst($userCode) == true ? C('YES') : C('NO');
            foreach($shopList as &$v) {
                if(isset($v['shopName'])){
                    $v['shopName'] = htmlspecialchars_decode($v['shopName'], ENT_QUOTES);
                }
                // 商户是否为新上的（7天内注册，且已入驻）
                $v['isNewShop'] = C('NO');
                if($v['shopStatus'] == \Consts::SHOP_ENTER_STATUS_YES && strtotime($v['createDate']) >= (time() - 7 * 86400)){
                    $v['isNewShop'] = C('YES');
                }

                $v['distance'] = intval($v['distance'] * C('PROPORTION'));
                // 得到商户最近一批优惠券
                $couponList = $batchCouponMdl->listAvailabelCoupon($v['shopCode'],
                    array(
                        'batchCouponCode',
                        'insteadPrice',
                        'discountPercent',
                        'createTime',
                    ));

                $v['batchCouponCode'] = $couponList ? $couponList[0]['batchCouponCode'] : '';
                $v['insteadPrice'] = $couponList ? $couponList[0]['insteadPrice'] : '';
                $v['discountPercent'] = $couponList ? $couponList[0]['discountPercent'] : '';
                $v['hasCoupon'] = $couponList ? C('YES') : C('NO');
                // 得到用户是否可以抢优惠券
                if($couponList) {
                    $v['isUserCanGrab'] = $userCode ? $userCouponMdl->isUserCanGrabTheCoupon($userCode, $couponList[0]['batchCouponCode']) ? C('YES') : C('NO') : C('YES');
                } else {
                    $v['isUserCanGrab'] = C('NO');
                }
                //得到用户是否拥有商店的会员卡
                $v['isUserHasCard'] = $userCode ? $userCardMdl->isUserHasShopCard($userCode, $v['shopCode']) ? C('YES') : C('NO') : C('NO');
                // 商店是否有活动
                $actMdl = new ActivityModel();
                $v['hasAct'] = $actMdl->isShopHasAct($v['shopCode']) ? C('YES') : C('NO');
                // 商店是否有上新
                $shopPhotoMdl = new ShopPhotoModel();
                $v['hasNew'] = $shopPhotoMdl->isShopHasNew($v['shopCode']) ? C('YES') : C('NO');
                $v['isFirst'] = $isFirst;

                // 是否有工行折扣
                $v['hasIcbcDiscount'] = C('NO');
                if($v['isAcceptBankCard'] == C('YES')){
                    $v['hasIcbcDiscount'] = $v['onlinePaymentDiscount'] == 100 ? C('NO') : C('YES');
                }
                $v['onlinePaymentDiscount'] = $v['onlinePaymentDiscount'] / C('DISCOUNT_RATIO');

                //获取查询出来的商家是否有新上的产品，有则列出新上的产品图片，每个产品展示一张，最多展示 4 个产品
                $productMdl = new ProductModel();
                $v['hasNewProduct'] = 0;
                $newProductList = $productMdl->getNewProduct(array('productImg', 'productId', 'shopCode'), array('productStatus' => array('NEQ', C('PRODUCT_STATUS.OFF_SHELF')), 'productImg' => array('neq', ''), 'shopCode' => $v['shopCode'], 'unix_timestamp(createTime)' => array('egt', time() - 7 * 86400)), 4);
                foreach($newProductList as $pk => $pv){
                    $newProductList[$pk]['linkType'] = 0;
                    $newProductList[$pk]['content'] = 'Browser/shopProductInfo?productId='.$pv['productId'].'&shopCode='.$pv['shopCode'];
                }
                if($newProductList){
                    $v['hasNewProduct'] = 1;
                    $v['newProductList'] = $newProductList;
                }
            }
            return $shopList;
        }
        return array();
    }


    /**
     * @param $condition
     * @param array $joinTableArr
     * @return array
     */
    public function countSearchShop($condition, $joinTableArr = array(),$zoneId) {

        //乔本亮添加zone_id
        if(isset($zoneId) && !empty($zoneId)){
            $condition['Shop.bank_id'] = $zoneId;
        }

        if((isset($condition['longitude']) && empty($condition['longitude'])) || !isset($condition['longitude'])) {
            return $this->getBusinessCode(C('USER.LONGITUDE_EMPTY'));
        }else{
            $longitude = $condition['longitude'];
            unset($condition['longitude']);
        }
        if((isset($condition['latitude']) && empty($condition['latitude'])) || !isset($condition['latitude'])) {
            return $this->getBusinessCode(C('USER.LATITUDE_EMPTY'));
        }else{
            $latitude = $condition['latitude'];
            unset($condition['latitude']);
        }
        if(isset($condition['searchWord'])){
            $subCondition['Shop.shopName'] = array('like','%'.$condition['searchWord'].'%');
            $subCondition['Shop.street'] = array('like', '%'.$condition['searchWord'].'%');
            $subCondition['_logic'] = 'or';
            $condition['_complex'] = $subCondition;
            unset($condition['searchWord']);
        }
        if(!(isset($condition['type']) && $condition['type'] && $condition['type'] != self::NO_TYPE) || !isset($condition['type'])) {
            $condition['type'] = array('NOTIN', array(\Consts::SHOP_TYPE_ICBC, \Consts::SHOP_TYPE_PLAT));
        }
        if(isset($condition['city'])){
            $city = $condition['city'];
            unset($condition['city']);
            $condition['Shop.city'] = array('like', '%'.$city.'%');
        }
        if(isset($condition['userCode'])){
            $userCode = $condition['userCode'];
            unset($condition['userCode']);
        }
        if(isset($condition['distance'])){
            $condition['sqrt(power(Shop.longitude-'.$longitude.',2) + power(Shop.latitude-'.$latitude.',2))'] = $condition['distance'];
            unset($condition['distance']);
        }
        $condition['Shop.isCompany'] = C('NO');
        $condition['Shop.status'] = C('SHOP_STATUS.ACTIVE');
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        $this->join('ShopTypeRel on ShopTypeRel.shopCode = Shop.shopCode', 'inner');
        $this->join('ShopType on ShopType.shopTypeId = ShopTypeRel.typeId', 'inner');
        $this->where($condition);
        $shopCount = $this->count('DISTINCT(Shop.shopCode)');
        return $shopCount;
    }

    /**
     * 获得商家详情
     * @param string $shopCode 商家编码
     * @param array $field 查询字段
     * @return array
     */
    public function getShopInfo($shopCode, $field) {
        $shopInfo = $this->getOneShopInfo(array('Shop.shopCode' => $shopCode), $field);

        if(isset($shopInfo['shopName'])){
            $shopInfo['shopName'] = htmlspecialchars_decode($shopInfo['shopName'], ENT_QUOTES);
        }
        if(isset($shopInfo['onlinePaymentDiscountUpperLimit'])) {
            $shopInfo['onlinePaymentDiscountUpperLimit'] = $shopInfo['onlinePaymentDiscountUpperLimit'] / C('RATIO');// 商家对在线支付的抵扣金额上限，分转化为元
        }
        if(isset($shopInfo['onlinePaymentDiscount'])) {
            $shopInfo['onlinePaymentDiscount'] = $shopInfo['onlinePaymentDiscount'] / C('DISCOUNT_RATIO'); // 商家对工行卡支付的折扣，百分转化为折
        }
        if(isset($shopInfo['shopOpeningTime'])) {
            $shopInfo['shopOpeningTime'] = substr($shopInfo['shopOpeningTime'], 0, 5); // 设置商家开门时间格式为小时：分钟
        }
        if(isset($shopInfo['shopClosedTime'])) {
            $shopInfo['shopClosedTime'] = substr($shopInfo['shopClosedTime'], 0, 5); // 设置商家开门时间格式为小时:分钟
        }
        if(isset($shopInfo['businessHours'])){
            $shopInfo['businessHours'] = json_decode($shopInfo['businessHours'], true);
            if(is_null($shopInfo['businessHours'])){
                $shopInfo['businessHours'] = array();
            }
        }

        if(isset($shopInfo['hqIcbcShopNbr'])) {
            $shopInfo['sellerid'] = $shopInfo['hqIcbcShopNbr'];  // 设置sellerid为10为商户号，该商户号平台根据工行提供的地区号生成。向商户中间账户打钱
        }

//        // 如果商户有12位商户号，则返回12号商户号，否则返回10位商户号
//        if(isset($shopInfo['icbcShopCode']) && ! empty($shopInfo['icbcShopCode'])) {
//            $shopInfo['sellerid'] = $shopInfo['icbcShopCode']; // 设置sellerid为12位商户号，该商户号由工行提供。直接向商户账户中打钱
//        } else {
//            if(isset($shopInfo['hqIcbcShopNbr'])) {
//                $shopInfo['sellerid'] = $shopInfo['hqIcbcShopNbr'];  // 设置sellerid为10为商户号，该商户号平台根据工行提供的地区号生成。向商户中间账户打钱
//            }
//        }
        return $shopInfo;
    }

    /**
     * 获取周边商家信息
     * @param string $shopCode 商家编码
     * @return array
     */
    public function getAroundShopInfo($shopCode){
        $batchCouponMdl = new BatchCouponModel();
        $cardMdl = new CardModel();
        $activityMdl = new ActivityModel();
        $shop = $this->field(array('type','longitude','latitude'))->where(array('shopCode' => $shopCode))->find();
        if(!$shop) {
            return $this->getBusinessCode(C('SHOP.NOT_EXIST'));
        }
        $longitude = $shop['longitude'];
        $latitude = $shop['latitude'];
        $subSql = $this->field(array('shopCode', 'type', "format(sqrt(power(longitude-$longitude,2)+power(latitude-$latitude,2)), 2) *".C('PROPORTION') => 'distance',))->where(array('status' => 1))->buildSql();
        $aroundShop = $this->table($subSql.' a')->field('a.*')->where(array('a.distance' => array('ELT', self::LIMIT_DISTANCE)))->select();
//        $nbrOfAct = $nbrOfActTonghang = $nbrOfCoupon = $nbrOfCouponTonghang = $nbrOfCard = $nbrOfCardTonghang = 0;
        $nbrOfAct = $nbrOfActTonghang = $nbrOfShop = $nbrOfShopTonghang = 0;
        if($aroundShop){
            foreach($aroundShop as $k => $v){
                if($v['shopCode'] == $shopCode){
                    unset($aroundShop[$k]);
                    continue;
                }
                $filterData = array('Activity.shopCode' => $shopCode, 'Activity.status' => C('ACTIVITY_STATUS.ACTIVE'));
                $activity = $activityMdl->getActivityList($filterData, new Pager(0));
//                $coupon = $batchCouponMdl->getCouponList($v['shopCode'], '', '', '', '');
//                $card = $cardMdl->getGeneralCardStastics($v['shopCode'], $page = 1);
                if(!empty($activity)) {$nbrOfAct++;}
//                if(!empty($coupon)){$nbrOfCoupon++;}
//                if(!empty($card)){$nbrOfCard++;}
                $nbrOfShop++;
                if($shop['type'] == $v['type']){
                    if(!empty($activity)) {$nbrOfAct++;$nbrOfActTonghang++;}
                    $nbrOfShopTonghang++;
//                    if(!empty($coupon)){$nbrOfCoupon++;$nbrOfCouponTonghang++;}
//                    if(!empty($card)){$nbrOfCard++;$nbrOfCardTonghang++;}
                }
            }
        }
        return array(
            'nbrOfAct' => $nbrOfAct,  //开展活动商家数
            'nbrOfActTonghang' => $nbrOfActTonghang,  //开展活动商家的同行数
            'nbrOfShop' => $nbrOfShop,  // 合作的商家
            'nbrOfShopTonghang' => $nbrOfShopTonghang,  // 合作的商家的同行数
//            'nbrOfCoupon' => $nbrOfCoupon,  //优惠券发放商家数
//            'nbrOfCouponTonghang' => $nbrOfCouponTonghang,  //优惠券发行商家的同行数
//            'nbrOfCard' => $nbrOfCard,  //会员卡合作商家数
//            'nbrOfCardTonghang' => $nbrOfCardTonghang,  //会员卡合作商家的同行数
        );
    }

    /**
     * 修改商家信息
     * @param string $shopCode 商家编码
     * @param array $updateInfo 更新信息
     * @return string
     */
    public function updateShop($shopCode, $updateInfo) {
        $rules = array(
            array('shopName', 'require', C('SHOP.NAME_ERROR')),
            array('country', 'require', C('SHOP.COUNTRY_ERROR')),
            array('province', 'require', C('SHOP.PROVINCE_ERROR')),
            array('city', 'require', C('SHOP.CITY_ERROR')),
            array('street', 'require', C('SHOP.STREET_ERROR')),
            array('tel', 'require', C('SHOP.TEL_ERROR')),
            array('mobileNbr', 'require', C('SHOP.MOBILE_NBR_ERROR')),
//            array('shopOpeningTime', 'require', C('SHOP.OPENING_TIME_ERROR')),
//            array('shopClosedTime', 'require', C('SHOP.CLOSED_TIME_ERROR')),
            array('businessHours', 'require', C('SHOP.BUSINESS_HOURS')),
            array('isCatering', 'require', C('SHOP.IS_CATERING_EMPTY')),
            array('isOuttake', 'require', C('SHOP.IS_OUTTAKE_EMPTY')),
            array('isOpenTakeout', 'require', C('SHOP.IS_OPEN_TAKEOUT_EMPTY')),
            array('eatPayType', 'require', C('SHOP.EAT_PAY_TYPE_EMPTY')),
            array('takeoutRequirePrice', 'require', C('SHOP.TAKEOUT_REQUIRE_PRICE_EMPTY')),
            array('deliveryFee', 'require', C('SHOP.DELIVERY_FEE_EMPTY')),
            array('deliveryDistance', 'require', C('SHOP.DELIVERY_DISTANCE_EMPTY')),
            array('deliveryStartTime', 'require', C('SHOP.DELIVERY_START_TIME_EMPTY')),
            array('deliveryEndTime', 'require', C('SHOP.DELIVERY_END_TIME_EMPTY')),
            array('isOpenEat', 'require', C('SHOP.IS_OPEN_EAT_EMPTY')),
            array('logoUrl', 'require', C('SHOP.LOGO_ERROR')),
            array('shortDes', 'require', C('SHOP.SHORT_DES_ERROR')),
        );
        if($this->validate($rules)->create($updateInfo) != false) {
            $temp = array('takeoutRequirePrice', 'deliveryFee');
            foreach($temp as $v) {
                if(isset($updateInfo[$v])) {
                    $updateInfo[$v] = $updateInfo[$v] * C('RATIO');
                }
            }
            if(isset($updateInfo['deliveryDistance'])) {
                $updateInfo['deliveryDistance'] = $updateInfo['deliveryDistance'] * C('THOUSAND');
            }
            $code = $this->where(array('shopCode' => $shopCode))->save($updateInfo) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            return $this->getBusinessCode($code);
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 编辑商店简介
     * @param string $shopCode 商店编码
     * @param string $shortDes 商店简介
     * @return array
     */
    public function updateShopShortDes($shopCode, $shortDes) {
        $rules = array(
            array('shopDes', 'require', C('SHOP.SHORT_DES_ERROR')),
        );
        $data = array(
            'shortDes' => $shortDes,
        );
        if($this->validate($rules)->create($data) != false) {
            $code = $this->where(array('shopCode' => $shopCode))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            return $this->getBusinessCode($code);
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 获得商店的简介
     * @param string $shopCode 商家编码
     * @return array (商家编码，商家简介)
     */
    public function getShortDes($shopCode) {
        return $this
            ->field(array('shopCode', 'shortDes', 'logoUrl', 'shopName'))
            ->where(array('shopCode' => $shopCode))
            ->find();
    }

    /**
     * 管理端商户列表
     * @param array $filterData
     * @param object $page 偏移值
     * @return null|array 如果为空，返回null；如果不为空，返回二维数组array(array());
     */
    public function listShop($filterData, $page) {
        $where = $this->filterWhere(
            $filterData,
            array('shopName' => 'like', 'mobileNbr' => 'like', 'shopId' => 'like', 'licenseNbr' => 'like', 'hqIcbcShopNbr' => 'like'),
            $page);
        $this->secondFilterWhere($where);

        //判断是否为惠圈管理人员
        if($_SESSION['USER']['bank_id']!=-1) {
            $where['bank_id'] = $_SESSION['USER']['bank_id'];
        }

        $this->field(array(
            'Shop.shopCode' => 'shopCode',
            'shopName',
            'icbcShopName',
            'licenseNbr',
            'hqIcbcShopNbr',
            'Shop.type',
            'mobileNbr',
            'wechatPublic',
            'onlinePaymentDiscount',
            'creditPoint',
            'count(activityCode)' => 'countActivity',
            'nbrOfMember',
            'isContractOn',
            'country',
            'province',
            'city',
            'street',
            'district',
            'Shop.status' =>'status',
            'createDate',
            'shopId',
            'popularity',
            'repeatCustomers',
            'isAcceptBankCard',
            'ownerCode',
            'brandId',
            'shopStatus',
            'addCardNo',
            'bank_id',
            'addCardUserName',
        ))
            ->join('Activity ON Shop.shopCode = Activity.shopCode', 'LEFT')
            ->join('ShopTypeRel ON ShopTypeRel.shopCode = Shop.shopCode', 'LEFT')
            ->group('Shop.shopCode')
            ->where($where);

        if($page){
            $this->pager($page);
        }
        $res = $this->group('Shop.shopCode')
            ->order('createDate DESC')
            ->select();
        return $res;
    }

    /**
     * 管理端商户总数
     * @param array $filterData
     * @return null|int 如果为空，返回null；如果不为空，返回number;
     */
    public function countShop($filterData) {

        $where = $this->filterWhere(
            $filterData,
            array('shopName' => 'like', 'mobileNbr' => 'like', 'shopId' => 'like', 'licenseNbr' => 'like', 'hqIcbcShopNbr' => 'like'),
            $page);

        $this->secondFilterWhere($where);

        //判断是否为惠圈管理人员
        if($_SESSION['USER']['bank_id']!=-1) {
            $where['bank_id'] = $_SESSION['USER']['bank_id'];
        }

        return $this
            ->field(array('Shop.shopCode' => 'shopCode', 'shopName', 'mobileNbr', 'wechatPublic', 'creditPoint', 'count(activityCode)' => 'countActivity', 'nbrOfMember', 'isContractOn', 'street', 'Shop.status' =>'status'))
            ->join('Activity ON Shop.shopCode = Activity.shopCode', 'LEFT')
            ->join('ShopTypeRel ON ShopTypeRel.shopCode = Shop.shopCode', 'LEFT')
            ->where($where)
            ->count('distinct(Shop.shopCode)');
    }

    /**
     * 第二次过滤$where中的条件
     * @param array $where
     * @return array
     */
    public function secondFilterWhere(&$where) {
        if ($where['status'] || $where['status'] == '0') {
            $where['Shop.status'] = $where['status'];
            unset($where['status']);
        }
        if ($where['typeId'] || $where['typeId'] == '0') {
            $where['ShopTypeRel.typeId'] = $where['typeId'];
            unset($where['typeId']);
        }
        if ($where['createDateStart'] && $where['createDateEnd']) {
            $where['Shop.createDate'] = array('BETWEEN', array($where['createDateStart'].' 00:00:00', $where['createDateEnd'].' 23:59:59'));
        } elseif ($where['createDateStart'] && !$where['createDateEnd']) {
            $where['Shop.createDate'] = array('EGT', $where['createDateStart'].' 00:00:00');
        } elseif (!$where['createDateStart'] && $where['createDateEnd']) {
            $where['Shop.createDate'] = array('ELT', $where['createDateEnd'].' 23:59:59');
        }
        unset($where['createDateStart']);
        unset($where['createDateEnd']);
        return $where;
    }

    /**
     * 管理端修改商户状态
     * @param string $shopCode
     * @param int $status
     * @return true|string 成功放回true;失败返回false
     */
    public function changeShopStatus($shopCode, $status) {
        if($status == 0) {
            $this->table('ShopStaff')->where(array('shopCode' => $shopCode))->data(array('status' => $status))->save();
        }
        return $this->where(array('ShopCode' => $shopCode))->data(array('status' => $status))->save() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
    }


    /**
     * 更新商店被关注量
     * @param string $shopCode 商家编码
     * @return boolean
     */
    public function updateFollowedCount($shopCode) {
        return $this->where(array('shopCode' => $shopCode))->setInc('followedCount');
    }

    /**
     * 管理端获得查询字段
     * @param string $keywords
     * @return null|array 如果为空，返回null；如果不为空，返回二维数组array(array());
     */
    public function getKeywords($keywords) {
        $where['shopName'] = array('like', "%$keywords%");
        return $this->field('shopName')->where($where)->select();

    }

    /**
     * 获取所有商家的编码，名字
     * @return null|array 如果为空，返回null；如果不为空，返回二维数组array(array());
     */
    public function getShopName(){
        return $this
            ->field(array('shopCode', 'shopName', 'createDate', 'city'))
            ->where(array('status' => C('SHOP_STATUS.ACTIVE'), 'shopStatus' => \Consts::SHOP_ENTER_STATUS_YES))
            ->order('createDate')
            ->select();
    }


    /**
     * 获取所有商家的编码，名字
     * @return null|array 如果为空，返回null；如果不为空，返回二维数组array(array());
     */
    public function getManagerShopName(){

        //判断是否是惠圈平台人员
        if($_SESSION['USER']['bank_id']!=-1){
            $where['bank_id'] = $_SESSION['USER']['bank_id'];
        }

        $where['status'] = C('SHOP_STATUS.ACTIVE');
        $where['shopStatus'] = \Consts::SHOP_ENTER_STATUS_YES;

        return $this
            ->field(array('shopCode', 'shopName', 'createDate', 'city'))
            ->where($where)
            ->order('createDate')
            ->select();
    }


    /**
     * 根据优惠券编码获得商家名字
     * @param string $batchCouponCode 优惠券编码
     * @return string 商家名字
     */
    public function getShopNameBybatchCouponCode($batchCouponCode) {
        $shopInfo = $this->field(array('shopName'))->where(array('batchCouponCode' => $batchCouponCode))->join('BatchCoupon on BatchCoupon.shopCode = Shop.shopCode')->select();
        return $shopInfo['shopName'];
    }

    /**
     * 设置商户的邀请码
     * @return string $inviteCode
     */
    public function setShopInviteCode() {
        $chars = 'abcdefghijklmnopqrstuvwxyz0123456789';
        $inviteCode = '';
        $inviteLen = 5;
        for($i = 0; $i < $inviteLen; $i++) {
            $nbr = rand(0, strlen($chars) - 1);
            $char = substr($chars, $nbr, 1);
            $inviteCode .= $char;
        }
        $userMdl = new UserModel();
        $userInfo = $userMdl->getUserInfo(array('inviteCode' => $inviteCode), array('userCode'));
        $shopMdl = new ShopModel();
        $shopInfo = $shopMdl->getShopInfoByInviteCode($inviteCode);
        if(empty($userInfo) && empty($shopInfo)) {
            return $inviteCode;
        } else {
            $this->setShopInviteCode();
        }
    }

    /**
     * 设置商户的惠圈入账商户号
     * @param string $areaNbr 地区号
     * @return string $hqIcbcShopNbr 惠圈入账商户号 10位
     */
    public function setHqIcbcShopNbr($areaNbr) {
        $chars = '0123456789'; // 随机数串
        $length = 10 - strlen($areaNbr); // 产生随机数的长度
        // 拼接入账商户号
        $hqIcbcShopNbr = $areaNbr;
        for($i = 0; $i < $length; $i++) {
            $nbr = rand(0, strlen($chars) - 1);
            $char = substr($chars, $nbr, 1);
            $hqIcbcShopNbr .= $char;
        }
        // 检查数据库中是否已经存在该入账商户号。若存在，则重新生成。
        $hqIcbcShopNbrArr = $this->where(array('hqIcbcShopNbr' => array('NEQ', '')))->getField('hqIcbcShopNbr', true);
        if (in_array($hqIcbcShopNbr, $hqIcbcShopNbrArr)) {
            return $this->setHqIcbcShopNbr($areaNbr);
        } else {
            return $hqIcbcShopNbr;
        }
    }

    function shoNameLength(){
        $shopInfo =     D("Shop")->where(array("shopCode"=>$_REQUEST['shopCode']))->find();
        $icount = strlen($shopInfo['shopName']);

        if($icount<4||$icount>=27){
            return false;
        }else{
            return true;
        }
    }

    function shoNameLength2(){
        $shopInfo =     D("PreShop")->where(array("shopCode"=>$_REQUEST['shopCode']))->find();
        $icount = strlen($shopInfo['shopName']);
        if($icount<4||$icount>=27){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 管理端添加或者编辑商家
     * @param array $data 商家信息
     * @return array {'code' => '', 'shopCode' => ''}
     */
    public function editShop2($data) {

        //判断是否为惠圈管理人员
        if($_SESSION['USER']['bank_id']!=-1) {
            $data['bank_id'] = $_SESSION['USER']['bank_id'];
        }

        $rules = array(
//            array('shopName', 'require', C('SHOP.NAME_ERROR')),
            array('shopName','shoNameLength2',C("SHOP.NAME_LENGTH_BIG"),1,'callback','2'),//检查字段
            array('country', 'require', C('SHOP.COUNTRY_ERROR')),
            array('province', 'require', C('SHOP.PROVINCE_ERROR')),
            array('city', 'require', C('SHOP.CITY_ERROR')),
            array('street', 'require', C('SHOP.STREET_ERROR')),
            array('type', 'require', C('SHOP.TYPE_ERROR')),
            array('licenseNbr', 'require', C('LICENSE_NBR.ERROR')),
            array('licenseExpireTime', 'require', C('SHOP.LICENSE_EXPIRE_TIME_ERROR')),
            array('licenseExpireTime', '0000-00-00', C('SHOP.LICENSE_EXPIRE_TIME_ERROR'), 0, 'notequal'),
            array('mobileNbr', 'require', C('SHOP.MOBILE_NBR_ERROR')),
            array('mobileNbr', C('MOBILE_NBR.LENGTH'), C('MOBILE_NBR.ERROR'), 0, 'length'),
            array('bank_id', 'require', "所属商圈必须填写"),
            array("logoUrl","require","商家LOGO必须上传"),
//            array('shopClosedTime', 'require', C('SHOP.CLOSED_TIME_ERROR')),
            array('businessHours', 'require', C('SHOP.BUSINESS_HOURS')),
            array('shopOwner', 'require', C('SHOP.SHOP_OWNER_ERROR')),
            array('icbcCityNbr', 'require', C('SHOP.ICBC_CITY_NBR_EMPTY')),
//            array('protocolUrl', 'require', C('SHOP.PROTOCOL_Url_EMPTY')),
            array('addCardNo', 'require', C('SHOP.ADD_CARD_NO_EMPTY')),
            array('addCardNoConfirm', 'addCardNo', C('SHOP.ADD_CARD_NO_CONFIRM_ERROR'), 0, 'confirm'),
            array('addCardUserName', 'require', C('SHOP.ADD_CARD_USER_NAME_EMPTY')),
            array('addCardUserNameConfirm', 'addCardUserName', C('SHOP.ADD_CARD_USER_NAME_CONFIRM_ERROR'), 0, 'confirm'),
        );

        if($this->validate($rules)->create($data) != false) {
            // 剔除不需要保存到数据库的字段
            unset($data['addCardNoConfirm']); // 剔除 确认商户入账账户 字段
            unset($data['addCardUserNameConfirm']); // 剔除 确认账户户名 字段

            // 获得地区号
            if(isset($data['icbcCityNbr'])) {

                // 判断地区号和商户所在地区是否相对应
                $districtMdl = new DistrictModel();
                $cityInfo = $districtMdl->getCityInfo(array('areaNbr' => $data['icbcCityNbr']), array('name'));

                // 义乌市归属金华市。特别区分义乌市
                if(($cityInfo['name'] != $data['city'] && $cityInfo['name'] != '义乌市') || ($cityInfo['name'] == '义乌市' && $cityInfo['name'] != $data['district'])) {
                    return $this->getBusinessCode(C('SHOP.ICBC_CITY_NBR_ERROR'));
                }

                $icbcCityNbr = $data['icbcCityNbr']; // 工行地区号
                $icbcCityNbrLen = strlen($icbcCityNbr); // 地区号长度
                unset($data['icbcCityNbr']); // 剔除 地区号 字段
                if(empty($data['hqIcbcShopNbr']) || substr($data['hqIcbcShopNbr'], 0, $icbcCityNbrLen) != $icbcCityNbr) { // 商户的内部户商户号没有设置或者为空，或商户的工行地区号发生改变。
                    // 设置商户的内部户商户号
                    $data['hqIcbcShopNbr'] = $this->setHqIcbcShopNbr($icbcCityNbr);
                }
            }

            $shopDesMdl = new ShopDecorationModel();
            foreach($data['decoration_title'] as $dk => $dv) {
                if($dv || $data['decoration_des'][$dk]) {
                    $shopDesMdl->updateShopDecoration($dk, array('title' => $dv, 'shortDes' => $data['decoration_des'][$dk]));
                }
            }

            if(! empty($data['shopCode'])) {
                $code = $this->where(array('shopCode' => $data['shopCode']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            } else {
                $data['shopCode'] = $this->create_uuid();
                $data['createDate'] = date('Y-m-d H:i:s'); // 设置创建时间
                $data['shopId'] = $this->setShopId(); // 设置商家在惠圈平台的商家号
                $data['inviteCode'] = $this->setShopInviteCode(); // 设置商家邀请码
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            if($code === C('API_INTERNAL_EXCEPTION')) {
                return $this->getBusinessCode($code);
            }

            $bmStaffInfo = session('USER');
            if(!empty($data['shopCode'])) {
                $cardMdl = new CardModel();
                // 获得商家的会员卡
                $cardCodeList = $cardMdl->listCardCode($data['shopCode']);
                if(! $cardCodeList) { // 商家的会员卡不存在
                    $cardInfo = array(
                        'cardName' => '银卡|金卡|白金卡',
                        'cardType' => C('CARD_TYPE.VIP'),
                        'cardLvl' => '1|2|3',
                        'creatorCode' => $bmStaffInfo['staffCode'],
                        'shopCode' => $data['shopCode'],
                        'discountRequire' => '999999|9999999|99999999',
                        'discount' => '100|100|100',
                        'pointLifetime' => '0|0|0',
                        'pointsPerCash' => '0|0|0',
                        'outPointsPerCash' => '0|0|0',
                    );
                    // 保存商家的会员卡
                    $editCardRet = $cardMdl->editCard($cardInfo);
                    $code = $editCardRet['code'];
                }
            }
            return array('code' => $code, 'shopCode' => $data['shopCode']);
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 管理端添加或者编辑商家
     * @param array $data 商家信息
     * @return array {'code' => '', 'shopCode' => ''}
     */
    public function editShop($data) {

        //判断是否为惠圈管理人员
        if($_SESSION['USER']['bank_id']!=-1) {
            $data['bank_id'] = $_SESSION['USER']['bank_id'];
        }

        $rules = array(
//            array('shopName', 'require', C('SHOP.NAME_ERROR')),
            array('shopName','shoNameLength',C("SHOP.NAME_LENGTH_BIG"),1,'callback','2'),//检查字段
            array('country', 'require', C('SHOP.COUNTRY_ERROR')),
            array('province', 'require', C('SHOP.PROVINCE_ERROR')),
            array('city', 'require', C('SHOP.CITY_ERROR')),
            array('street', 'require', C('SHOP.STREET_ERROR')),
            array('type', 'require', C('SHOP.TYPE_ERROR')),
            array('licenseNbr', 'require', C('LICENSE_NBR.ERROR')),
            array('licenseExpireTime', 'require', C('SHOP.LICENSE_EXPIRE_TIME_ERROR')),
            array('licenseExpireTime', '0000-00-00', C('SHOP.LICENSE_EXPIRE_TIME_ERROR'), 0, 'notequal'),
            array('mobileNbr', 'require', C('SHOP.MOBILE_NBR_ERROR')),
            array('mobileNbr', C('MOBILE_NBR.LENGTH'), C('MOBILE_NBR.ERROR'), 0, 'length'),
            array('bank_id', 'require', "所属商圈必须填写"),
            array("logoUrl","require","商家LOGO必须上传"),
//            array('shopClosedTime', 'require', C('SHOP.CLOSED_TIME_ERROR')),
            array('businessHours', 'require', C('SHOP.BUSINESS_HOURS')),
            array('shopOwner', 'require', C('SHOP.SHOP_OWNER_ERROR')),
            array('icbcCityNbr', 'require', C('SHOP.ICBC_CITY_NBR_EMPTY')),
//            array('protocolUrl', 'require', C('SHOP.PROTOCOL_Url_EMPTY')),
            array('addCardNo', 'require', C('SHOP.ADD_CARD_NO_EMPTY')),
            array('addCardNoConfirm', 'addCardNo', C('SHOP.ADD_CARD_NO_CONFIRM_ERROR'), 0, 'confirm'),
            array('addCardUserName', 'require', C('SHOP.ADD_CARD_USER_NAME_EMPTY')),
            array('addCardUserNameConfirm', 'addCardUserName', C('SHOP.ADD_CARD_USER_NAME_CONFIRM_ERROR'), 0, 'confirm'),
        );

        if($this->validate($rules)->create($data) != false) {
            // 剔除不需要保存到数据库的字段
            unset($data['addCardNoConfirm']); // 剔除 确认商户入账账户 字段
            unset($data['addCardUserNameConfirm']); // 剔除 确认账户户名 字段

            // 获得地区号
            if(isset($data['icbcCityNbr'])) {

                // 判断地区号和商户所在地区是否相对应
                $districtMdl = new DistrictModel();
                $cityInfo = $districtMdl->getCityInfo(array('areaNbr' => $data['icbcCityNbr']), array('name'));

                // 义乌市归属金华市。特别区分义乌市
                if(($cityInfo['name'] != $data['city'] && $cityInfo['name'] != '义乌市') || ($cityInfo['name'] == '义乌市' && $cityInfo['name'] != $data['district'])) {
                    return $this->getBusinessCode(C('SHOP.ICBC_CITY_NBR_ERROR'));
                }

                $icbcCityNbr = $data['icbcCityNbr']; // 工行地区号
                $icbcCityNbrLen = strlen($icbcCityNbr); // 地区号长度
                unset($data['icbcCityNbr']); // 剔除 地区号 字段
                if(empty($data['hqIcbcShopNbr']) || substr($data['hqIcbcShopNbr'], 0, $icbcCityNbrLen) != $icbcCityNbr) { // 商户的内部户商户号没有设置或者为空，或商户的工行地区号发生改变。
                    // 设置商户的内部户商户号
                    $data['hqIcbcShopNbr'] = $this->setHqIcbcShopNbr($icbcCityNbr);
                }
            }

            $shopDesMdl = new ShopDecorationModel();
            foreach($data['decoration_title'] as $dk => $dv) {
                if($dv || $data['decoration_des'][$dk]) {
                    $shopDesMdl->updateShopDecoration($dk, array('title' => $dv, 'shortDes' => $data['decoration_des'][$dk]));
                }
            }

            if(! empty($data['shopCode'])) {
                $code = $this->where(array('shopCode' => $data['shopCode']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            } else {
                $data['shopCode'] = $this->create_uuid();
                $data['createDate'] = date('Y-m-d H:i:s'); // 设置创建时间
                $data['shopId'] = $this->setShopId(); // 设置商家在惠圈平台的商家号
                $data['inviteCode'] = $this->setShopInviteCode(); // 设置商家邀请码
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            if($code === C('API_INTERNAL_EXCEPTION')) {
                return $this->getBusinessCode($code);
            }

            $bmStaffInfo = session('USER');
            if(!empty($data['shopCode'])) {
                $cardMdl = new CardModel();
                // 获得商家的会员卡
                $cardCodeList = $cardMdl->listCardCode($data['shopCode']);
                if(! $cardCodeList) { // 商家的会员卡不存在
                    $cardInfo = array(
                        'cardName' => '银卡|金卡|白金卡',
                        'cardType' => C('CARD_TYPE.VIP'),
                        'cardLvl' => '1|2|3',
                        'creatorCode' => $bmStaffInfo['staffCode'],
                        'shopCode' => $data['shopCode'],
                        'discountRequire' => '999999|9999999|99999999',
                        'discount' => '100|100|100',
                        'pointLifetime' => '0|0|0',
                        'pointsPerCash' => '0|0|0',
                        'outPointsPerCash' => '0|0|0',
                    );
                    // 保存商家的会员卡
                    $editCardRet = $cardMdl->editCard($cardInfo);
                    $code = $editCardRet['code'];
                }
            }
            return array('code' => $code, 'shopCode' => $data['shopCode']);
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 生成商户号
     * @return string $shopId 商家号
     */
    public function setShopId() {
        $shopId  = $this->where(array('createDate' => array('BETWEEN', array(date('Y-m-d 00:00:00', time()), date('Y-m-d 23:59:59', time())))))->max('shopId');
        if(!$shopId) {
            $serialNbr = sprintf('%04d', 1);
        } else {
            $serialNbr = intval(substr($shopId, 6)) + 1;
            $serialNbr = sprintf('%04d', $serialNbr);
        }
        $shopId = date('ymd', time()) . $serialNbr;
        return $shopId;
    }

    /**
     * 获取商家城市
     * @return array
     */
    public function getShopCity(){
        $shopCity = $this->field(array('city'))->select();
        $city = array();
        if($shopCity){
            foreach($shopCity as $v){
                if($v['city']){
                    if(!in_array($v['city'], $city)){
                        $city[] = $v['city'];
                    }
                }
            }
        }
        return $city;
    }

    /**
     * 获得商家的各类型数量
     * @retrun array
     */
    public function analysisShopType($condition) {
        return $this->where($condition)->count('shopCode');
    }

    /**
     * 设置商家头像
     * @param string $shopCode 商家编码
     * @param string $logoUrl 图像URL
     * @return string
     */
    public function setShopLogo($shopCode, $logoUrl){
        return $this->where(array('shopCode' => $shopCode))->save(array('logoUrl' => $logoUrl)) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
    }

    /**
     * 后台根据条件修改商户信息
     * @param array $condition 修改条件
     * @param array $setMessage 修改内容
     * @return array
     */
    public function setShopFeild($condition, $setMessage){
        $result = $this->where($condition)->save($setMessage);
        if($result){
            return array('code' => C('SUCCESS'));
        }else{
            return array('code' => C('API_INTERNAL_EXCEPTION'));
        }
    }

    /**
     * 获得平台主题活动的商家
     * @param $shopCodeArr
     * @return array
     */
    public function cGetShopByActTheme($shopCodeArr){
        $shopList = array();
        foreach($shopCodeArr as $v){
            $where['Shop.shopCode'] = $v;
            $shopList[] = $this
                ->field(array('Shop.shopCode', 'Shop.shopName', 'Shop.logoUrl', 'BatchCoupon.batchCouponCode'))
                ->join('BatchCoupon ON Shop.shopCode = BatchCoupon.shopCode', 'LEFT')
                ->where($where)
                ->order('createTime desc')
                ->find();
        }
        return $shopList;
    }

    /**
     * 根据商户号查找商家
     * @param $shopId
     * @param $field
     * @return mixed
     */
    public function getShopByShopId($shopId, $field){
        return $this->field($field)->where(array('shopId'=>$shopId))->find();
    }

    /**
     * 更新所有商家的虚拟人气值
     */
    public function updateShopVirtualPopularity() {
        $shopList = $this->field(array('shopCode'))->select();
        foreach($shopList as $shop) {
            $nbr = rand(0, 100);
            $this->where(array('shopCode' => $shop['shopCode']))->setInc('virtualPopularity', $nbr);
        }
    }

}