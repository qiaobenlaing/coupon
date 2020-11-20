<?php

/**
 * 顾客端API Controller
 * User: Weiping
 * Date: 2015-04-20
 * Time: 23:49
 */
namespace Api\Controller;
use Common\Model\ActivityModel;
use Common\Model\BankAccountLocalLogModel;
use Common\Model\BatchCouponModel;
use Common\Model\BankAccountModel;
use Common\Model\BonusModel;
use Common\Model\BonusStatisticsModel;
use Common\Model\CardActionLogModel;
use Common\Model\CardModel;
use Common\Model\CityBrandRelModel;
use Common\Model\CityIndexModuleRelModel;
use Common\Model\CityShopTypeModel;
use Common\Model\ClassClickInfoModel;
use Common\Model\ClassRemarkImgModel;
use Common\Model\ClassRemarkModel;
use Common\Model\ClassWeekInfoModel;
use Common\Model\ClientActModuleModel;
use Common\Model\ClientAppLogModel;
use Common\Model\CmptxsnoLogModel;
use Common\Model\DistrictModel;
use Common\Model\FeedbackModel;
use Common\Model\indexModule;
use Common\Model\IndexModuleModel;
use Common\Model\JpushModel;
use Common\Model\MessageModel;
use Common\Model\OrderCouponModel;
use Common\Model\OrderProductModel;
use Common\Model\ProductCategoryModel;
use Common\Model\ProductModel;
use Common\Model\ShopClassModel;
use Common\Model\ShopHeaderModel;
use Common\Model\ShopHonorModel;
use Common\Model\ShopModel;
use Common\Model\ShopDecorationModel;
use Common\Model\ShopPhotoModel;
use Common\Model\ShopRecruitModel;
use Common\Model\ShopSignInfoModel;
use Common\Model\ShopTeacherModel;
use Common\Model\ShopTradingAreaRelModel;
use Common\Model\ShopBrandRelModel;
use Common\Model\ShopTypeModel;
use Common\Model\ShopTypeRelModel;
use Common\Model\StudentClassModel;
use Common\Model\StudentStarModel;
use Common\Model\SubAlbumModel;
use Common\Model\SubModuleModel;
use Common\Model\SystemParamModel;
use Common\Model\TeacherWorkModel;
use Common\Model\UserActCodeModel;
use Common\Model\UserActivityModel;
use Common\Model\UserAddressLogModel;
use Common\Model\UserAddressModel;
use Common\Model\UserCardModel;
use Common\Model\UserCouponModel;
use Common\Model\UserEnterShopInfoRecordModel;
use Common\Model\UserModel;
use Common\Model\UserSettingModel;
use Common\Model\ShopFollowingModel;
use Common\Model\CouponSaleLogModel;
use Common\Model\UserBonusModel;
use Common\Model\UserConsumeModel;
use Common\Model\CommunicationModel;
use Common\Model\UserMessageModel;
use Common\Model\IcbcModel;
use Common\Model\ConsumeOrderModel;
use JPush\Exception\APIRequestException;
use Common\Model\ShopQueryModel;
use Common\Model\UtilsModel;
use Common\Model\CollectCouponModel;
use Common\Model\CollectShopModel;

class ClientController extends ApiBaseController {

    /**
     * 课程报名
     * @param string $userCode 用户编码
     * @param string $classCode 课程编码
     * @param string $shopCode 商家编码
     * @param string $studentBirthday 学员出生年月日，格式：yyyy-mm-dd
     * @param string $studentGrade 学员年级
     * @param string $studentName 学员姓名
     * @param string $studentSchool 学员学校
     * @param string $studentSex 学员性别，'M':男；'F'：女；'U':未知
     * @param string $studentTel 学员联系电话
     * @return array
     */
    public function signClass($userCode, $classCode, $shopCode, $studentBirthday, $studentGrade, $studentName, $studentSchool, $studentSex, $studentTel) {
        // 判断是否选择了课程
        if(empty($classCode)) {
            return array('code' => C('SIGN_CLASS.CLASS_EMPTY'));
        }

        // 添加商家课程报名信息
        $shopSignInfo = array('shopCode' => $shopCode, 'userCode' => $userCode, 'studentName' => $studentName, 'studentSex' => $studentSex, 'studentBirthday' => $studentBirthday, 'studentSchool' => $studentSchool, 'studentGrade' => $studentGrade, 'studentTel' => $studentTel);
        $shopSignInfoMdl = new ShopSignInfoModel();
        M()->startTrans();
        $editShopSignRet = $shopSignInfoMdl->editShopSignInfo($shopSignInfo);
        if($editShopSignRet['code'] == C('SUCCESS')) {
            // 添加学员报名的课程
            $studentClassMdl = new StudentClassModel();
            $arrClassCode = explode('|', $classCode);
            foreach($arrClassCode as $v) {
                $studentClassInfo = array(
                    'shopSignCode' => $editShopSignRet['signCode'],
                    'classCode' => $v,
                );
                $editStuClassRet = $studentClassMdl->editStudentClass($studentClassInfo);
                if($editStuClassRet['code'] !== C('SUCCESS')) {
                    M()->rollback();
                    return $editStuClassRet;
                }
            }
        }
        M()->commit();
        return $editShopSignRet;
    }

    /**
     * 发表课程评价
     * @param string $classCode 课程编码
     * @param number $effectLvl 效果星级，0到5
     * @param number $envLvl 环境星级，0到5
     * @param string $remark 评价描述
     * @param string $remarkImg 评价图片，图片URL，多个URL之间使用竖线“|”隔开
     * @param number $teacherLvl 师资星级，0到5
     * @param string $userCode 用户编码
     * @param number $wholeLvl 总体星级，0到5
     * @return array
     */
    public function addClassRemark($classCode, $effectLvl, $envLvl, $remark, $remarkImg, $teacherLvl, $userCode, $wholeLvl) {
        $classRemarkMdl = new ClassRemarkModel();
        // 判断用户是否有权限评价课程
        $ifUserCanRemark = $classRemarkMdl->ifUserCanRemark($userCode, $classCode);
        if(!$ifUserCanRemark) return array('code' => C('CLASS_REMARK.NO_PERMISSION'));

        $data = array('classCode' => $classCode, 'effectLvl' => $effectLvl, 'envLvl' => $envLvl, 'remark' => $remark, 'teacherLvl' => $teacherLvl, 'userCode' => $userCode, 'wholeLvl' => $wholeLvl);
        M()->startTrans();
        $editClassRemarkRet = $classRemarkMdl->editClassRemark($data);
        if($editClassRemarkRet['code'] == C('SUCCESS')) {
            // 保存评价的图片信息
            $arrRemarkImg = explode('|', $remarkImg);
            $classRemarkImgMdl = new ClassRemarkImgModel();
            foreach($arrRemarkImg as $img) {
                $addImgRet = $classRemarkImgMdl->editClassRemarkImg(array('remarkCode' => $editClassRemarkRet['remarkCode'], 'remarkImgUrl' => $img));
                if($addImgRet['code'] == C('API_INTERNAL_EXCEPTION')) {
                    M()->rollback();
                }
            }
            M()->commit();
        }
        return $editClassRemarkRet;
    }

    /**
     * 我的页面是否显示推荐好友得豪礼模块
     * @return array
     */
    public function ifShowRecommend() {
        return array('isShow' => '0');
    }

    /**
     * 获得名师列表
     * @param string $shopCode 商家编码
     * @param int $page 页码
     * @return array
     */
    public function listShopTeacher($shopCode, $page) {
        $shopTeacherMdl = new ShopTeacherModel();
        $shopTeacherList = $shopTeacherMdl->getShopTeacherList(array('shopCode' => $shopCode), array('teachCourse', 'teacherImgUrl', 'teacherName', 'teacherTitle'), array(), '', \Consts::PAGESIZE, $page);
        $shopTeacherCount = $shopTeacherMdl->countShopTeacher(array('shopCode' => $shopCode), array());
        $nextPage = UtilsModel::getNextPage($shopTeacherCount, $page);
        return array(
            'count' => count($shopTeacherList),
            'nextPage' => $nextPage,
            'page' => $page,
            'list' => $shopTeacherList,
            'totalCount' => $shopTeacherCount,
        );
    }

    /**
     * 获得名师详情
     * @param string $teacherCode 教师编码
     * @return array $shopTeacherInfo
     */
    public function getShopTeacherInfo($teacherCode) {
        $shopTeacherMdl = new ShopTeacherModel();
        $shopTeacherInfo = $shopTeacherMdl->getShopTeacherInfo(array('teacherCode' => $teacherCode), array('teachCourse', 'teacherImgUrl', 'teacherName', 'teacherTitle', 'teacherInfo'), array());
        $teacherWorkMdl = new TeacherWorkModel();
        $shopTeacherInfo['teacherWork'] = $teacherWorkMdl->getTeacherWorkList(array('teacherCode' => $teacherCode), array('workUrl'));
        return $shopTeacherInfo;
    }

    /**
     * 获得课程详情
     * @param string $classCode 课程编码
     * @param string $userCode 用户编码
     * @return array $classInfo
     */
    public function getClassInfo($classCode, $userCode) {
        $shopClassMdl = new ShopClassModel();
        $classInfo = $shopClassMdl->getShopClassInfo(
            array('ShopClass.classCode' => $classCode),
            array('ShopClass.classCode', 'ShopClass.classInfo', 'ShopClass.className', 'ShopClass.classUrl', 'ShopClass.learnEndDate', 'ShopClass.learnMemo', 'ShopClass.learnNum', 'ShopClass.learnStartDate', 'ShopClass.signEndDate', 'ShopClass.signStartDate', 'ShopClass.studentNum', 'ShopTeacher.teacherName', 'Shop.tel' => 'hotline'),
            array(array('joinTable' => 'ShopTeacher', 'joinCondition' => 'ShopTeacher.teacherCode = ShopClass.teacherCode', 'joinType' => 'inner'), array('joinTable' => 'Shop', 'joinCondition' => 'Shop.shopCode = ShopClass.shopCode', 'joinType' => 'inner'))
        );
        // 获得课程一周上课信息
        $classWeekInfoMdl = new ClassWeekInfoModel();
        $classWeekInfo = $classWeekInfoMdl->getClassWeekList(array('classCode' => $classCode), array('weekName', 'startTime', 'endTime'));
        $classTime = '';
        foreach($classWeekInfo as $classWeek) {
            $classTime .= $classWeek['weekName'] . $classWeek['startTime'] . $classWeek['endTime'];
        }
        $classInfo['classTime'] = $classTime;
        // 获得课程点赞数量
        $classClickInfoMdl = new ClassClickInfoModel();
        $classInfo['clickNbr'] = $classClickInfoMdl->countClickNbr(array('classCode' => $classCode));
        // 获得用户是否对该课程点赞
        $userClickClassInfo = $classClickInfoMdl->getClickInfo(array('classCode' => $classCode, 'userCode' => $userCode), array('clickCode'));
        $classInfo['isUserClick'] = $userClickClassInfo ? C('YES') : C('NO');
        return $classInfo;
    }

    /**
     * 用户对课程点赞
     * @param string $classCode 课程编码
     * @param string $userCode 用户编码
     * @return array
     */
    public function praiseClass($classCode, $userCode) {
        $classClickMdl = new ClassClickInfoModel();
        $ret = $classClickMdl->editClassClick(array('classCode' => $classCode, 'userCode' => $userCode));
        return $ret;
    }

    /**
     * 用户取消对课程点赞
     * @param string $classCode 课程编码
     * @param string $userCode 用户编码
     * @return array
     */
    public function cancelPraiseClass($classCode, $userCode) {
        $classClickMdl = new ClassClickInfoModel();
        $ret = $classClickMdl->delClassClick(array('classCode' => $classCode, 'userCode' => $userCode));
        return $ret;
    }

    /**
     * 获得教育类商家的课程列表
     * @param string $shopCode 商家编码
     * @return array $shopClass
     */
    public function listShopClass($shopCode) {
        $shopClassMdl = new ShopClassModel();
        $shopClass = $shopClassMdl->getShopClassList(
            array('ShopClass.shopCode' => $shopCode),
            array('ShopClass.classCode', 'ShopClass.className', 'ShopClass.learnStartDate', 'ShopClass.learnEndDate', 'ShopTeacher.teacherName', 'ShopClass.learnFee', 'ShopClass.learnNum'),
            array(array('joinTable' => 'ShopTeacher', 'joinCondition' => 'ShopTeacher.teacherCode = ShopClass.teacherCode', 'joinType' => 'inner'))
        );
        $classWeekInfoMdl = new ClassWeekInfoModel();
        foreach($shopClass as $k => $class) {
            $shopClass[$k]['learnFee'] = $shopClass[$k]['learnFee'] / \Consts::HUNDRED;
            $shopClass[$k]['classWeekInfo'] = $classWeekInfoMdl->getClassWeekList(array('classCode' => $class['classCode']), array('weekName', 'startTime', 'endTime'));
        }
        return $shopClass;
    }

    /**
     * 获得活动
     * @param int $pos 活动所属位置信息。
     * @return array $actInfo
     */
    public function getAct($pos) {
        $actMdl = new ActivityModel();
        $actInfo = $actMdl->getActInfo(array('pos' => $pos, 'status' => C('ACTIVITY_STATUS.ACTIVE')), array('activityCode', 'webUrl', 'activityLogo'));
        return $actInfo;
    }

    /**
     * 添加外卖订单
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param string $productList 产品清单。json格式字符串
     * @return array $ret
     */
    public function addTakeoutOrder($userCode, $shopCode, $productList) {
        $shopMdl = new ShopModel();
        // 获得商家信息
        $shopInfo = $shopMdl->getShopInfo($shopCode, array('isOpenTakeout', 'deliveryStartTime', 'deliveryEndTime', 'deliveryFee'));
        if($shopInfo['isOpenTakeout'] == C('NO') || strtotime($shopInfo['deliveryStartTime']) > time() || strtotime($shopInfo['deliveryEndTime']) < time()) {
            return array('code' => C('SHOP.CAN_NOT_TAKE_OUT'));
        }

        $productList = json_decode($productList, true);
        $orderAmount = $shopInfo['deliveryFee'];
        foreach($productList as &$product) {
            $product['productUnitPrice'] = $product['productUnitPrice'] * C('RATIO');
            $orderAmount += $product['productNbr'] * $product['productUnitPrice'];
        }

        $consumeOrderMdl = new ConsumeOrderModel();
        $orderInfo = array(
            'clientCode' => $userCode,
            'shopCode' => $shopCode,
            'status' => C('UC_STATUS.UNPAYED'),
            'orderAmount' => $orderAmount,
            'orderType' => C('ORDER_TYPE.TAKE_OUT'),
            'orderStatus' => C('FOOD_ORDER_STATUS.UNORDERED'),
        );
        $addTakeoutOrderRet = $consumeOrderMdl->editTakeoutOrder($orderInfo);

        if($addTakeoutOrderRet['code'] == C('SUCCESS')) {
            $orderProductMdl = new OrderProductModel();
            $orderProductMdl->startTrans();
            foreach($productList as &$product) {
                $product['orderCode'] = $addTakeoutOrderRet['orderCode'];
                $addOrderProductRet = $orderProductMdl->addOrderProduct($product);
                if($addOrderProductRet['code'] !== C('SUCCESS')) {
                    $orderProductMdl->rollback();
                    return array('code' => '20000');
                }
            }
            $orderProductMdl->commit();
            return array('code' => '50000', 'orderCode' => $addTakeoutOrderRet['orderCode']);
        } else {
            return $addTakeoutOrderRet;
        }
    }

    /**
     * 添加外卖订单地址和备注
     * @param string $orderCode 订单编码
     * @param int $userAddressId 用户地址ID
     * @param string $remark 备注
     * @return array $ret
     */
    public function addTakeoutOrderOtherInfo($orderCode, $userAddressId, $remark) {
        $userAddressMdl = new UserAddressModel();
        $userAddressInfo = $userAddressMdl->getUserAddressInfo($userAddressId);
        if(empty($userAddressInfo)) {
            return array('code' => C('USER_ADDRESS.ID_ERROR'));
        }

        $consumeOrderMdl = new ConsumeOrderModel();
        $orderInfo = array(
            'orderCode' => $orderCode,
//            'orderStatus' => C('FOOD_ORDER_STATUS.ORDERED'),
            'receiver' => $userAddressInfo['contactName'],
            'receiverMobileNbr' => $userAddressInfo['mobileNbr'],
            'deliveryAddress' => $userAddressInfo['street'] . $userAddressInfo['position'],
            'remark' => $remark,
            'isFinishOrder' => C('YES'),
            'orderTime' => date('Y-m-d H:i:s'),
        );
        $editTakeoutOrderRet = $consumeOrderMdl->editTakeoutOrder($orderInfo);
        return $editTakeoutOrderRet;
    }

    /**
     * 添加堂食订单
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param string $productList 产品清单。json格式字符串
     * @return array $ret
     */
    public function addNotTakeoutOrder($userCode, $shopCode, $productList) {
        $shopMdl = new ShopModel();
        // 获得商家信息
        $shopInfo = $shopMdl->getShopInfo($shopCode, array('isOpenTakeout', 'deliveryStartTime', 'deliveryEndTime', 'deliveryFee'));

        $productList = json_decode($productList, true);
        $orderAmount = $shopInfo['deliveryFee'];
        foreach($productList as &$product) {
            $product['productUnitPrice'] = $product['productUnitPrice'] * C('RATIO');
            $orderAmount += $product['productNbr'] * $product['productUnitPrice'];
        }

        $consumeOrderMdl = new ConsumeOrderModel();
        $orderInfo = array(
            'clientCode' => $userCode,
            'shopCode' => $shopCode,
            'status' => C('UC_STATUS.UNPAYED'),
            'orderAmount' => $orderAmount,
            'orderType' => C('ORDER_TYPE.NO_TAKE_OUT'),
            'orderStatus' => C('FOOD_ORDER_STATUS.UNORDERED'),
        );
        $addTakeoutOrderRet = $consumeOrderMdl->editNotTakeoutOrder($orderInfo);

        if($addTakeoutOrderRet['code'] == C('SUCCESS')) {
            $orderProductMdl = new OrderProductModel();
            $orderProductMdl->startTrans();
            foreach($productList as &$product) {
                $product['orderCode'] = $addTakeoutOrderRet['orderCode'];
                $addOrderProductRet = $orderProductMdl->addOrderProduct($product);
                if($addOrderProductRet['code'] !== C('SUCCESS')) {
                    $orderProductMdl->rollback();
                    return array('code' => '20000');
                }
            }
            $orderProductMdl->commit();
            return array('code' => '50000', 'orderCode' => $addTakeoutOrderRet['orderCode']);
        } else {
            return $addTakeoutOrderRet;
        }
    }

    /**
     * 添加堂食订单备注
     *  @param string $orderCode 订单编码
     * @param string $remark 备注
     * @return array $ret
     */
    public function addNotTakeoutOrderOtherInfo($orderCode, $remark) {
        $consumeOrderMdl = new ConsumeOrderModel();
        $consumeOrderInfo = $consumeOrderMdl->getOrderInfo(array('orderCode' => $orderCode));

        $shopMdl = new ShopModel();
        // 获得商家信息
        $shopInfo = $shopMdl->getShopInfo($consumeOrderInfo['shopCode'], array('eatPayType'));

        $userMdl = new UserModel();
        $userInfo = $userMdl->getUserInfo(array('userCode' => $consumeOrderInfo['clientCode']), array('nickName', 'mobileNbr'));

        $eatPayType = $shopInfo['eatPayType'];
        $orderInfo = array(
            'orderCode' => $orderCode,
            'remark' => $remark,
            'eatPayType' => $eatPayType,
            'isFinishOrder' => C('YES'),
            'receiver' => $userInfo['nickName'],
            'receiverMobileNbr' => $userInfo['mobileNbr'],
            'orderTime' => date('Y-m-d H:i:s'),
        );
        if($eatPayType == C('EAT_PAY_TYPE.AFTER')) {
            $orderInfo['orderStatus'] = C('FOOD_ORDER_STATUS.ORDERED');
        }
        $addTakeoutOrderRet = $consumeOrderMdl->editNotTakeoutOrder($orderInfo);
        return $addTakeoutOrderRet;
    }

    /**
     * 提交优惠券（兑换券和代金券）订单
     * @param string $userCode 用户编码
     * @param string $shopCode 商户编码
     * @param string $batchCouponCode 购买的优惠券编码
     * @param int $couponNbr 购买的优惠券数量
     * @param float $platBonus 平台红包，单位：元
     * @param float $shopBonus 商户红包，单位：元
     * @return array
     */
    public function addCouponOrder($userCode, $shopCode, $batchCouponCode, $couponNbr, $platBonus, $shopBonus) {
        // 获得在线支付的商户号
        $shopMdl = new ShopModel();
        $shopInfo = $shopMdl->getShopInfo($shopCode, array('icbcShopCode', 'hqIcbcShopNbr'));

        F("shopInfoSql",$shopMdl->getLastSql());
        F("info","userCode:".$userCode."shopCode:".$shopCode."batchCouponCode:".$batchCouponCode."couponNbr:".$couponNbr."platBonus"."platBonus".$platBonus."shopBonus:".$shopBonus);

        // 判断用户是否可以购买该优惠券
        $batchCouponMdl = new BatchCouponModel();
        $isUserCanBuyCoupon = $batchCouponMdl->isUserCanBuyCoupon($userCode, $batchCouponCode, $couponNbr);

        F("isUserCanBuyCouponSql",$batchCouponMdl->getLastSql());

        if($isUserCanBuyCoupon['code'] != C('SUCCESS')) {
            return $isUserCanBuyCoupon;
        }
        // 获得要购买的优惠券的信息
        $batchCouponInfo = $batchCouponMdl->getCouponBasicInfo(array('batchCouponCode' => $batchCouponCode), array('payPrice', 'couponType'));
        // 添加买券订单
        $orderInfo = array(
            'shopCode' => $shopCode,
            'clientCode' => $userCode,
            'orderAmount' => $batchCouponInfo['payPrice'] * $couponNbr,
        );

        F("batchCouponInfoSql",$batchCouponMdl->getLastSql());
        F("orderInfo",$orderInfo);

        $consumeOrderMdl = new ConsumeOrderModel();
        $ret = $consumeOrderMdl->editCouponOrder($orderInfo);

        if($ret['code'] == C('SUCCESS')) {
            $orderCode = $ret['orderCode']; // 订单编码
            $orderNbr = $ret['orderNbr']; // 订单号
            $orderCouponMdl = new OrderCouponModel();
            // 设置清算状态
            $liquidationStatus = strlen($shopInfo['sellerid']) == 10 ? \Consts::LIQUIDATION_STATUS_HAD_NOT : \Consts::LIQUIDATION_STATUS_NO_NEED;
            // 保存该笔订单买了哪些券
            for($i = 0; $i < $couponNbr; $i++) {
                $orderCouponInfo = array(
                    'orderCode' => $orderCode, // 订单编码
                    'batchCouponCode' => $batchCouponCode, // 优惠券编码
                    'userCode' => $userCode, // 用户编码
                    'status' => \Consts::ORDER_COUPON_STATUS_UNPAY_NOUSE, // 状态为订单未付款，不可用
                    'liquidationStatus' => $liquidationStatus,
                    'couponType' => $batchCouponInfo['couponType'], // 优惠券的类型
                );
                // 保存优惠券
                $editOrderCouponRet = $orderCouponMdl->editOrderCoupon(array(), $orderCouponInfo);
                if($editOrderCouponRet['code'] != C('SUCCESS')) { // 保存优惠券出错
                    // 删除之前保存的优惠券
                    $orderCouponMdl->delOrderCoupon(array('orderCode' => $orderCode));
                    return $editOrderCouponRet;
                }
            }
            // 减少优惠券的剩余数量
            $batchCouponMdl->decRemaining($batchCouponCode, $couponNbr);

            $userConsumeMdl = new UserConsumeModel();
            // 生成在线支付记录
            $ret = $userConsumeMdl->bankcardPay($orderCode, '', $platBonus, $shopBonus);
            if($ret['code'] == C('SUCCESS')) {
                $ret['orderNbr'] = $orderNbr;
            }
            return $ret;
        } else {
            return $ret;
        }
    }

    /**
     * 获得填写活动报名订单时需要的数据
     * @param string $userCode 用户编码
     * @param string $actCode 活动编码
     * @return array
     */
    public function getInfoPreActInfo($userCode, $actCode) {
        // 获得活动信息
        $actMdl = new ActivityModel();
        $actInfo = $actMdl->getActInfo(
            array('activityCode' => $actCode),
            array('activityCode', 'txtContent', 'Activity.shopCode', 'feeScale', 'registerNbrRequired', 'Shop.shopName'),
            array(array('joinTable' => 'Shop', 'joinCondition' => 'Shop.shopCode = Activity.shopCode', 'joinType' => 'inner'))
        );

        // 获得用户已经报名的数量
        $userActMdl = new UserActivityModel();
        $totalNbr = $userActMdl->getUserActFieldInfo(array('userCode' => $userCode, 'activityCode' => $actCode), 'totalNbr');

        // 获得用户还能够报名的数量
        $actInfo['userRemaining'] = $actInfo['registerNbrRequired'] - $totalNbr;

        // 获得用户红包信息
        $bonusStatisticsMdl = new BonusStatisticsModel();
        $userShopBonusInfo = $bonusStatisticsMdl->getUserBonusStatistics($userCode, $actInfo['shopCode']);
        $userPlatBonusInfo = $bonusStatisticsMdl->getUserBonusStatistics($userCode, C('HQ_CODE'));

        //最小支付金额
        $systemParamMdl = new SystemParamModel();
        $paramInfo = $systemParamMdl->getParamValue('minRealPay');

        // 获得商户的信息
        $shopMdl = new ShopModel();
        $shopInfo = $shopMdl->getShopInfo($actInfo['shopCode'], array('isAcceptBankCard'));

        return array(
            'actInfo' => $actInfo,
            'userBonusInfo' => array(
                'platBonus' => $userPlatBonusInfo['totalValue'] ? $userPlatBonusInfo['totalValue'] / \Consts::HUNDRED: 0,
                'shopBonus' => $userShopBonusInfo['totalValue'] ? $userShopBonusInfo['totalValue'] / \Consts::HUNDRED: 0,
            ),
            'minRealPay' => $paramInfo['value'] / C('RATIO'), //最小支付金额，单位：元
            'isAcceptBankCard' => $shopInfo['isAcceptBankCard'] //是否受理银行卡
        );
    }

    /**
     * 提交活动报名订单
     * @param string $userCode 用户编码
     * @param string $shopCode 商户编码
     * @param string $actCode 活动编码
     * @param string $orderInfo 订单详情。Json格式字符串，例如：[{"id": "1","nbr":"2","price":"70"},{"id": "2","nbr":"1","price":"50"}]。其中id是票种id；nbr是数量；price是票的价格，单位：元。
    其中id是票种id；nbr是数量；price是票的价格，单位：元。
     * @param string $bookingName 联系人姓名
     * @param string $mobileNbr 联系人有效电话
     * @param float $orderAmount 订单价格，单位：元
     * @param float $platBonus 平台红包，单位：元
     * @param float $shopBonus 商户红包，单位：元
     * @return array 一维关联数组 {'code' => '结果码'}
     */
    public function submitActOrder($userCode, $shopCode, $actCode, $orderInfo, $bookingName, $mobileNbr, $orderAmount, $platBonus, $shopBonus) {
        $orderInfo = json_decode($orderInfo, true); // json格式字符串转数组
        $totalNbr = 0; // 购买的票的总数
        foreach($orderInfo as $v) {
            $totalNbr += $v['nbr']; // 累加票的数量
        }
        $orderAmount = $orderAmount * \Consts::HUNDRED; // 订单金额，单位元转化为分

        // 判断用户是否可以报名该活动
        $actMdl = new ActivityModel();
        $isUserCanSignUpTheAct = $actMdl->isUserCanSignUpTheAct($userCode, $actCode, $totalNbr);
        if($isUserCanSignUpTheAct['code'] !== true) {
            return $isUserCanSignUpTheAct;
        }

        M()->startTrans();
        // 添加活动订单
        $orderData = array(
            'shopCode' => $shopCode, // 商家编码
            'clientCode' => $userCode, // 用户编码
            'orderAmount' => $orderAmount, // 订单金额，单位：分
            'receiver' => $bookingName, // 联系人姓名
            'receiverMobileNbr' => $mobileNbr, // 联系人电话
            'actualOrderAmount' => $orderAmount, // 订单最初消费金额
        );
        $consumeOrderMdl = new ConsumeOrderModel();
        $editActOrderRet = $consumeOrderMdl->editActOrder(array(), $orderData);
        $orderCode = $editActOrderRet['orderCode']; // 订单编码

        /**如果用户没有报名过该活动，则新添加报名记录；如果用户已经报名过该活动，则更新购买总票数即可 */
        $userActMdl = new UserActivityModel();
        $userActInfo = $userActMdl->getUserActInfo(array('userCode' => $userCode, 'activityCode' => $actCode));
        if(empty($userActInfo)) {
            // 添加新的用户活动记录
            $userActData = array(
                'activityCode' => $actCode, // 活动编码
                'userCode' => $userCode, // 用户编码
                'totalNbr' => $totalNbr, // 票的总数
            );
            $editUserActRet = $userActMdl->editUserAct(array(), $userActData);
            $userActCode = $editUserActRet['userActCode']; // 用户活动编码
        } else {
            // 更新用户要购买的票总数
            $incTotalNbrRet = $userActMdl->incField(array('userCode' => $userCode, 'activityCode' => $actCode), 'totalNbr', $totalNbr);
            $editUserActRet['code'] = $incTotalNbrRet != true ? C('API_INTERNAL_EXCEPTION') : C('SUCCESS');
            $userActCode = $userActInfo['userActivityCode']; // 用户活动编码
        }

        // 保存用户订单中购买的票的详情
        $editUserActCodeRet['code'] = C('SUCCESS');
        $userActCodeMdl = new UserActCodeModel();
        foreach($orderInfo as $v) {
            for($i = 0; $i < $v['nbr']; $i++) {
                $userActCodeData = array(
                    'userActCode' => $userActCode, // 用户活动编码
                    'scaleId' => $v['id'], // 票种id
                    'price' => $v['price'] * \Consts::HUNDRED, // 票的价格，单位：分
                    'orderCode' => $orderCode, // 订单编码
                    'liquidationStatus' => \Consts::LIQUIDATION_STATUS_HAD_NOT, // 清算状态
                );
                $editUserActCodeRet = $userActCodeMdl->updateUserActCode(array(), $userActCodeData);
            }
        }

        // 生成在线支付记录
        $userConsumeMdl = new UserConsumeModel();
        $bankcardPayRet = $userConsumeMdl->bankcardPay($orderCode, '', $platBonus, $shopBonus);

        if($editActOrderRet['code'] != C('SUCCESS') || $editUserActRet['code'] != C('SUCCESS') || $editUserActCodeRet['code'] != C('SUCCESS') || $bankcardPayRet['code'] != C('SUCCESS')) {
            M()->rollback();
            $code = C('API_INTERNAL_EXCEPTION');
            $orderNbr = '';
            $consumeCode = '';
            $realPay = 0;
        } else {
            M()->commit();
            $code = C('SUCCESS');
            $orderNbr = $editActOrderRet['orderNbr'];
            $consumeCode = $bankcardPayRet['consumeCode'];
            $realPay = $bankcardPayRet['realPay'];
        }
        return array('code' => $code, 'orderNbr' => $orderNbr, 'consumeCode' => $consumeCode, 'realPay' => $realPay);
    }

    /**
     * 删除用户地址
     * @param int $userAddressId 用户地址ID
     * @return array $ret
     */
    public function delUserAddress($userAddressId) {
        $userAddressMdl = new UserAddressModel();
        $ret = $userAddressMdl->delUserAddress($userAddressId);
        return $ret;
    }

    /**
     * 修改用户地址
     * @param string $userCode 用户编码
     * @param string $contactName 联系人姓名
     * @param string $mobileNbr 联系人手机号
     * @param string $province 省份
     * @param string $city 城市
     * @param string $district 区或县
     * @param string $street 具体地址
     * @param int $userAddressId 用户地址ID
     * @return array $ret 例如：{'code': '50000'}
     */
    public function editUserAddress($userCode, $contactName, $mobileNbr, $province, $city, $district, $street, $userAddressId) {
        $data = array(
            'userCode' => $userCode,
            'contactName' => $contactName,
            'mobileNbr' => $mobileNbr,
            'province' => $province,
            'city' => $city,
            'district' => $district,
            'street' => $street,
            'userAddressId' => $userAddressId,
        );
        $userAddressMdl = new UserAddressModel();
        $ret = $userAddressMdl->editUserAddress($data);
        return $ret;
    }

    /**
     * 添加用户地址
     * @param string $userCode 用户编码
     * @param string $contactName 联系人姓名
     * @param string $mobileNbr 联系人手机号
     * @param string $province 省份
     * @param string $city 城市
     * @param string $district 区或县
     * @param string $street 具体地址
     * @return array $ret 例如：{'code': '50000'}
     */
    public function addUserAddress($userCode, $contactName, $mobileNbr, $province, $city, $district, $street) {
        $data = array(
            'userCode' => $userCode,
            'contactName' => $contactName,
            'mobileNbr' => $mobileNbr,
            'province' => $province,
            'city' => $city,
            'district' => $district,
            'street' => $street
        );
        $userAddressMdl = new UserAddressModel();
        $ret = $userAddressMdl->editUserAddress($data);
        return $ret;
    }

    /**
     * 获得用户地址详情
     * @param int $userAddressId 用户地址ID
     * @return array $userAddressInfo
     */
    public function getUserAddressInfo($userAddressId) {
        $userAddressMdl = new UserAddressModel();
        $userAddressInfo = $userAddressMdl->getUserAddressInfo($userAddressId);
        return $userAddressInfo;
    }

    /**
     * 获得用户地址列表
     * @param string $userCode 用户编码
     * @return array $userAddressList
     */
    public function getUserAddressList($userCode) {
        $userAddressMdl = new UserAddressModel();
        $userAddressList = $userAddressMdl->getUserAddressList($userCode);
        return $userAddressList;
    }

    /**
     * 获得商户菜单详情
     * @param int $productId 产品ID
     * @return array $productInfo
     */
    public function getProductInfo($productId) {
        $productMdl = new ProductModel();
        $productInfo = $productMdl->getProductInfo(array('productId' => $productId));
        return $productInfo;
    }

    /**
     * 获得商户菜单列表
     * @param string $shopCode 商家编码
     * @return array $categoryList
     */
    public function getProductList($shopCode) {
        $productCategoryMdl = new ProductCategoryModel();
        $categoryList = $productCategoryMdl->getProductCategoryList(array('shopCode' => $shopCode));
        $productMdl = new ProductModel();
        foreach($categoryList as &$category) {
            $category['productList'] = $productMdl->getProductList(array('categoryId' => $category['categoryId'], 'productStatus' => array('NEQ', C('PRODUCT_STATUS.OFF_SHELF'))));
        }
        return $categoryList;
    }

    /**
     * 获得顾客端系统参数
     * @return array
     */
    public function getSystemParam() {
        $systemParamMdl = new SystemParamModel();
        $paramList = $systemParamMdl->listAllClientSystemParam();
        $systemParam = array();
        foreach($paramList as $param) {
            $systemParam[$param['param']] = $param['value'];
        }
        return $systemParam;
    }

    /**
     * 获得我的邀请码
     * @param string $userCode 用户编码
     * @return array
     */
    public function getUserInviteCode($userCode) {
        $userMdl = new UserModel();
        // 检查用户是否设置邀请码，若没有则设置
        $ret = $userMdl->isUserSetInviteCode($userCode);
        // 统计推荐的人数
        $recomPersonCount = $userMdl->countRecomPerson($userCode);
        $userBonus = new UserBonusModel();
        $bonusAmount = $userBonus->countUserRewardBonus($userCode);
        $userCouponMdl = new UserCouponModel();
        $couponAmount = $userCouponMdl->countUserRewardCoupon($userCode);
        $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('inviteCode'));
//        $bonusMdl = new BonusModel();
//        $rewardBonusInfo = $bonusMdl->getRewardBonus();
//        $batchCouponMdl = new BatchCouponModel();
//        $rewardCouponInfo = $batchCouponMdl->getSendInviterCoupon();
        $systemParamMdl = new SystemParamModel();
        $paramInfo = $systemParamMdl->getParamValue('bonusReward');
        $reward = $paramInfo['value'] / C('RATIO');
        return array(
            'recomCount' => $recomPersonCount,
            'bonusAmount' => $bonusAmount,
            'couponAmount' => $couponAmount,
            'inviteCode' => $userInfo['inviteCode'],
            'reward' => $reward,
            'imgUrl' => '/Public/img/inviteBonus.png',
            'shareTitle' => '用我的推荐码注册惠圈，享优惠喽',
            'shareContent' => '成功邀请好友送红包，上不封顶，想拿多少就拿多少，可叠加使用！',
            'rules' => '
1、推荐好友下载“惠圈”APP，并使用本人邀请码进行注册，签约工行发行的银行卡惠支付即可获得10元奖励；
2、推荐好友数量不限，每个注册用户只能使用一个邀请码；
3、同一手机、同一惠圈账号、同一签约的工行卡视为同一用户；
4、所获奖励可以在惠圈上所有商户进行消费，但必须使用工行快捷支付，实际支付金额不能低于1元；
5、好友安装并使用惠圈APP时所在地须在惠圈开通城市；
6、推荐好友所获得奖励不能用于出售、交换等其他通途；
7、如果您的惠圈账号存在异常或被终止服务，则无法继续使用惠圈账户内的零钱；
8、本规则最终解释权归惠圈所有'
        );
    }

    /**
     * 获得商家对工行银行卡的打折力度和抵扣金额上限
     * @param string $shopCode 商家编码
     * @return array $shopInfo
     */
    public function getShopBankCardDiscount($shopCode) {
        $shopMdl = new ShopModel();
        // 获得商家信息（工行卡折扣，工行卡抵扣金额上限）
        $shopInfo = $shopMdl->getShopInfo($shopCode, array('isAcceptBankCard', 'onlinePaymentDiscount', 'onlinePaymentDiscountUpperLimit'));
        return $shopInfo;
    }

    /**
     * 获得首页的活动模块
     * @return array $moduleList
     */
    public function listActModule() {
        $clientAMMdl = new ClientActModuleModel();
        $moduleList = $clientAMMdl->listClientActModule(array(), $this->getPager(1));
        $moduleInfo = array();
        foreach($moduleList as $index => $module) {
            foreach($module as $keyName => $v) {
                $moduleInfo[$keyName . ($index + 1)] = $v;
            }
        }
        return $moduleInfo;
    }

    /**
     * 获得首页的活动模块
     * @param string $city 城市
     * @return array $moduleList
     */
    public function getClientHomePage($city) {
        $clientAMMdl = new ClientActModuleModel();
        $moduleList = $clientAMMdl->listClientActModule(array(), $this->getPager(1));
        foreach($moduleList as $index => &$module) {
            unset($module['moduleCode']);
            unset($module['sortNbr']);
        }

        $districtMdl = new DistrictModel();
        $cityInfo = $districtMdl->getCityInfo(array('name' => array('like', "%$city%")), array('id'));
        $cityShopTypeMdl = new CityShopTypeModel();
        $shopTypeList = $cityShopTypeMdl->listCityShopType($cityInfo['id']);
        return array(
            'moduleList' => $moduleList,
            'shopTypeList' => $shopTypeList
        );
    }

    /**
     * 猜你喜欢
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param float $longitude 用户所在经度
     * @param float $latitude 用户所在维度
     * @return array $shopList
     */
    public function guessYouLikeShop($userCode, $shopCode, $longitude, $latitude) {
        $shopMdl = new ShopModel();
        $shopList = $shopMdl->guessYouLikeShop($userCode, $shopCode, $longitude, $latitude);
        return $shopList;
    }

    /**
     * 用户注册
     * @param int $mobileNbr 手机号码
     * @param number $validateCode 验证码
     * @param string $password 密码 md5加密后
     * @param int $recomNbr 推荐人手机号
     * @param string $deviceNbr 设备号
     * @return array $ret
     */
    public function register($mobileNbr, $validateCode, $password, $recomNbr, $deviceNbr) {
        $userMdl = new UserModel();
        $ret = $userMdl->register($mobileNbr, $validateCode, $password, $recomNbr, $deviceNbr);
        return $ret;
    }

    /**
     * 修改个人信息
     * @param number $mobileNbr 用户手机号码
     * @param array $updateInfo 修改信息，包括：姓名，昵称，所在省份，所在城市，邮箱，头像，个性签名，性别
     * @return array $ret
     */
    public function updateUserInfo($mobileNbr, $updateInfo) {
        $userMdl = new UserModel();
        $updateInfo['mobileNbr'] = $mobileNbr;
        $ret = $userMdl->updateUserInfo($updateInfo);
        return $ret;
    }

    /**
     * 获取个人信息
     * @param string $userCode 用户编码
     * @return array $userInfo 一维数组或空数组
     */
    public function getUserInfo($userCode) {
        $userMdl = new UserModel();
        $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('User.*'));
        $isUserSetPayPwd = $this->isUserSetPayPwd($userCode);
        $userInfo['isUserSetPayPwd'] = $isUserSetPayPwd['code'];
        return $userInfo ? $userInfo : array();
    }

    /**
     * 修改顾客免验证码支付
     * @param string $userCode 用户编码
     * @param int $freeValCodePay 300以下免验证码支付。0-关闭；1-开启；
     * @param string $pwd 输入的用户的登陆密码
     * @return array
     */
    public function editFreeVal($userCode, $freeValCodePay, $pwd) {
        $userMdl = new UserModel();
        if($freeValCodePay == C('YES')) { // 当设置免验证码支付，为开启时
            // 验证密码是否正确
            $valPwdRet = $userMdl->valPwd($userCode, $pwd);
            if(!$valPwdRet) {
                // 返回密码不正确
                return array('code' => C('PWD.WRONG'));
            }
        }
        $data = array('userCode' => $userCode, 'freeValCodePay' => $freeValCodePay);
        // 保存设置
        $ret = $userMdl->editUser($data);
        return array('code' => $ret);
    }

    /**
     * 获得用户的设置信息
     * @param string $userCode 用户编码
     * @return array $userSettingInfo
     */
    public function getUserSetting($userCode) {
        $userSettingMdl = new UserSettingModel();
        $userSettingInfo = $userSettingMdl->getUserSetting($userCode);
        return $userSettingInfo;
    }
    /**
     * 保存用户设置
     * @param string $userCode 用户编码
     * @param number $isBroadcastOn 是否接受广播消息。1-是；0-否
     * @param number $isMsgBingOn 是否开启消息提醒声音 。1-是；0-否
     * @param number $isCouponMsgOn 是否接受优惠券推送消息。1-是；0-否
     * @return array
     */
    public function updateUserSetting($userCode, $isBroadcastOn, $isMsgBingOn, $isCouponMsgOn) {
        $userSettingMdl = new UserSettingModel();
        $ret = $userSettingMdl->updateUserSetting($userCode, $isBroadcastOn, $isMsgBingOn, $isCouponMsgOn);
        return $ret;
    }

    /**
     * 用户是否设置了支付密码
     * @param string $userCode 用户编码
     * @return array $ret
     */
    public function isUserSetPayPwd($userCode) {
        $userMdl = new UserModel();
        $ret = $userMdl->isUserSetPayPwd($userCode);
        return $ret;
    }

    /**
     * 设置支付密码
     * @param string $userCode 用户编码
     * @param string $payPwd 支付密码，md5加密
     * @param string $confirmPayPwd 确认密码，md5加密
     * @return array $ret
     */
    public function setPayPwd($userCode, $payPwd, $confirmPayPwd) {
        $userMdl = new UserModel();
        $ret = $userMdl->setPayPwd($userCode, $payPwd, $confirmPayPwd);
        return $ret;
    }

    /**
     * 验证支付密码是否正确
     * @param string $userCode 用户编码
     * @param string $payPwd 支付密码，md5加密
     * @return array $ret
     */
    public function validatePayPwd($userCode, $payPwd) {
        $userMdl = new UserModel();
        $ret = $userMdl->validatePayPwd($userCode, $payPwd);
        return $ret;
    }

    /**
     * 验证设置或找回支付密码时的验证码
     * @param string $userCode 用户编码
     * @param string $mobileNbr 手机号码
     * @param string $valCode 验证码
     * @return array 例：{'code' => 1}
     */
    public function valSSPValCode($userCode, $mobileNbr, $valCode) {
        $code = \Consts::NO;
        $userMdl = new UserModel();
        $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('mobileNbr'));
        // 判断手机号码和用户手机号码是否一致，
        if($userInfo['mobileNbr'] == $mobileNbr) {
            // 判断验证是否正确
            if(! empty($valCode) && $valCode == S(\Consts::MSG_VAL_ACTION_SET_PAY_PWD . $mobileNbr . 'validateCode')) {
                $code = \Consts::YES;
            }
        }
        return array('code' => $code);
    }


    /**
     * 得到查询子列表
     * @param string $city 城市
     * @return array
     */
    public function listSearchWords($city) {
        $shopQueryMdl = new ShopQueryModel();
        $districtMdl = new DistrictModel();
        $cityId= $districtMdl->getCityInfo(array('name' => $city), array('id'));

        $districtCity = $districtMdl->listDistrict($cityId['id']);

        $subModule = array(
            array(
                'queryName' => '全部',
                'moduleValue' => \IndexModule::INITIAL
				
            )
        );

        // 查询商家时，选择“附近”时的子列表
        $distanceArr = array(
            array('name' => '200米', 'value' => 200, 'moduleValue' => \IndexModule::DISTANCE), // 距离
            array('name' => '500米', 'value' => 500, 'moduleValue' => \IndexModule::DISTANCE), // 距离
            array('name' => '1000米', 'value' => 1000, 'moduleValue' => \IndexModule::DISTANCE), // 距离
            array('name' => '2000米', 'value' => 2000, 'moduleValue' => \IndexModule::DISTANCE), // 距离
        );
        $subModule[1]['subList'] = $distanceArr;
        $subModule[1]['moduleValue'] = \IndexModule::DISTANCE;
        $subModule[1]['queryName'] = "附近";

        $subModuleMdl = new SubModuleModel();
        $tradingArea = $subModuleMdl->getSubModuleList(array('SubModule.id' => 'value','SubModule.title' => 'name'), array('hide' => C('NO'), 'parentModuleId' => \IndexModule::SHOP_TRADING_AREA, 'SubModule.cityId' => $cityId['id'])); // $indexModuleValue 的值来自 IndexModule 表
        foreach($tradingArea as $tk => $tv){
            $tradingArea[$tk]['moduleValue'] = \IndexModule::SHOP_TRADING_AREA; // 商圈
        }
        $subModule[2]['subList'] = $tradingArea;
        $subModule[2]['moduleValue'] = \IndexModule::SHOP_TRADING_AREA;
        $subModule[2]['queryName'] = "热门商圈";
        foreach ($districtCity as $k => $v){
            $subModule[$k+3]['queryName'] = $v['name'];
            $subModule[$k+3]['moduleValue'] = \IndexModule::DISTRICT; // 区
			
        }
		/* $i=0;
		 foreach ($districtCity as $k => $v){
			 $subModule[$k+3]['queryName'] = $v['name'];				
				 $subModule[$k+3]['area']=$i++;	 
	   
        }*/
		 
		
        // 获得行业列表
        $cityShopTypeMdl = new CityShopTypeModel();
        $cityShopTypeList = $cityShopTypeMdl->listCityShopType($cityId['id']);
        $type = array(
            array(
                'queryName' => '所有',
                'value' => 0,
                'focusedUrl' => '/Public/img/android/all_type_focused.png',
                'notFocusedUrl' => '/Public/img/android/all_type_unfocused.png'
            )
        );
        foreach($cityShopTypeList as $v){
            $type[] = array(
                'queryName' => $v['typeZh'],
                'value' => $v['typeValue'],
                'focusedUrl' => $v['focusedUrl'],
                'notFocusedUrl' => $v['notFocusedUrl']
            );
        }

        // 获得智能排序列表
        $intelligentSorting = $shopQueryMdl->getSubField(array('value', 'queryName'), array('field' => 30, 'isDisplay' => C('YES')));
        array_unshift($intelligentSorting,
            array(
                'value' => 0,
                'queryName' => '智能排序',
            )
        );

        // 获得筛选商家的条件
        $filter = $shopQueryMdl->getSubField(array('value', 'queryName', 'focusedUrl'), array('field' => 40, 'isDisplay' => C('YES')));
        array_unshift($filter,
            array(
                'value' => 0,
                'queryName' => '全部',
                'focusedUrl' => '/Public/img/android/all_shop.png'
            )
        );

        $result['circle'] = array(
            'zh' => '商圈',
            'list' => $subModule,
        );
        $result['type'] = array(
            'zh' => '行业',
            'list' => $type,
        );
        $result['intelligentSorting'] = array(
            'zh' => '智能排序',
            'list' => $intelligentSorting,
        );
        $result['filter'] = array(
            'zh' => '筛选',
            'list' => $filter,
        );
//        var_dump($result['type']);
        return $result;
    }

    public function getSearchShopInfo($city, $field, $indexModuleValue){
        $districtMdl = new DistrictModel();
        $cityInfo = $districtMdl->getCityInfo(array('name' => array('like', "%$city%")), array('id'));

        $subModuleMdl = new SubModuleModel();
        return $subModuleMdl->getSubModuleList($field, array('hide' => C('NO'), 'parentModuleId' => $indexModuleValue, 'SubModule.cityId' => $cityInfo['id'])); // $indexModuleValue 的值来自 IndexModule 表
    }

    /**
     * 用户搜索商店
     * @param string $searchWord 搜索关键字：商店名字
     * @param int $type 商店类型 0-所有类型；1-美食；2-咖啡；3-健身；4-娱乐；5-服装；6-其他；
     * @param float $longitude 用户所在经度
     * @param float $latitude 用户所在维度
     * @param string $userCode 用户编码
     * @param int $page 页码
     * @param string $city 城市
     * @param int $moduleValue 模块值：-2-区；-1-距离；3-商圈； 4-品牌；
     * @param string $content 内容。区域ID；距离，单位：米；商圈ID；品牌ID；
     * @param int $order 排序. 1：离我最近；2人气最高
     * @param int $filter 筛选 3为工行折扣， 4为优惠活动， 5为商户活动
     * @return array $shopList 二维数组，没有数据返回空数组array()
     */
    public function searchShop($searchWord, $type, $longitude, $latitude, $userCode, $page, $city, $moduleValue = 0, $content = '', $order = 0, $filter = 0,$queryName) {
     
		$shopMdl = new ShopModel();
        $shopTypeRelMdl = new ShopTypeRelModel();
        // 获得商户类型为工行或者平台的商户
        $arrNotInShopCode = $shopTypeRelMdl->getFieldList(array('typeValue' => array('IN', array(\Consts::SHOP_TYPE_ICBC, \Consts::SHOP_TYPE_PLAT))), 'shopCode', array(array('joinTable' => 'ShopType', 'joinCondition' => 'ShopType.shopTypeId = ShopTypeRel.typeId', 'joinType' => 'inner')));
        if(empty($arrNotInShopCode)){
            $arrNotInShopCode = array('0');
        }
        $arrNotInShopCode = array_unique($arrNotInShopCode); // 移除数组中重复的值

        $condition = array(
            'searchWord' => $searchWord,
            'longitude' => $longitude,
            'latitude' => $latitude,
            'userCode' => $userCode ? $userCode : '',
        );
        if($city == '义乌市') {
            $condition['district'] = $city;
        } else {
            $condition['city'] = $city;
        }
        if($type > 0){
            $shopTypeMdl = new ShopTypeModel();
            $shopTypeIdArr = $shopTypeMdl->getFieldValue(array('typeValue' => $type), 'shopTypeId');
            $condition['ShopTypeRel.typeId'] = array('IN', $shopTypeIdArr);
        }
        if($filter == \SearchShopFilter::BANK_DISCOUNT){ // 工行折扣
            $condition['onlinePaymentDiscount'] = array('NEQ', 100);
            $condition['isAcceptBankCard'] = C('YES');
        }elseif($filter == \SearchShopFilter::COUPON){ // 优惠活动
            $codeArr = $shopMdl->getShopFieldList('shopCode', array('shopCode' => array('NOTIN', $arrNotInShopCode), 'Shop.isCompany' => C('NO'), 'Shop.status' => C('SHOP_STATUS.ACTIVE')));
            foreach($codeArr as $c){
                $batchCouponMdl = new BatchCouponModel();
                $couponList = $batchCouponMdl->listAvailabelCoupon($c,
                    array('batchCouponCode'));
                if($couponList){
                    $infoArr[] = $c;
                }
            }
        }elseif($filter == \SearchShopFilter::ACTIVITY){ // 商户活动
            $codeArr = $shopMdl->getShopFieldList('shopCode', array('shopCode' => array('NOTIN', $arrNotInShopCode), 'Shop.isCompany' => C('NO'), 'Shop.status' => C('SHOP_STATUS.ACTIVE')));
            foreach($codeArr as $c){
                $actMdl = new ActivityModel();
                $hasAct = $actMdl->isShopHasAct($c);
                if($hasAct){
                    $infoArr[] = $c;
                }
            }
        }

        if($moduleValue == \IndexModule::SHOP_TRADING_AREA){ //商圈
            $shopTradingAreaRelMdl = new ShopTradingAreaRelModel();
            $relList = $shopTradingAreaRelMdl->arrSTAR('shopCode', array('subModuleId' => (int)$content));
        }elseif($moduleValue == \IndexModule::BRAND){ //品牌
            // 获得品牌下的商户列表
            $shopBrandRelMdl = new ShopBrandRelModel();
            $relList = $shopBrandRelMdl->arrCBR('shopCode', array('brandId' => (int)$content));
        }elseif($moduleValue == \IndexModule::DISTANCE){ //距离
            $condition['distance'] = array('elt', (int)$content / C('PROPORTION'));
        }elseif($moduleValue == \IndexModule::DISTRICT){ //区
         //   $condition['Shop.district'] = array('like', "%$content%");	 
			 $districtModel=D("District");
			$district=$districtModel->field("name")
									->where("name='$queryName'")
									->select();
				/* foreach($district as $value){
					 if($value['name']!=null){
						 return 3;
						  $condition['Shop.district'] = array('like', $value['name']); 
					 }else{
						 return 2;
						 $condition['Shop.district'] = array('like',"%$content%");
					 }
						
				 }*/
				 foreach($district as $value){
					 if($value['name']!=null){
							$a[]=$value['name'];				//存储得到的数据		
					 }
						
				 }
				 $isHave=$districtModel->where("name='$queryName'")->count();
				 if($isHave<1){
					 //$condition['Shop.district'] =implode(" ",$a);
					
					 $condition['Shop.district'] = array('like', "%$content%");
				 }else{
					
					$condition['Shop.district'] =implode(" ",$a);//取数组的数据转换成字符串
				 }
				 	
				
			
        }
        if(isset($infoArr)){
            $shopCodeArr = $infoArr;
            if(isset($relList)){
                $shopCodeArr = array_unique(array_merge($shopCodeArr, $relList));
            }
        }else{
            if(isset($relList)){
                $shopCodeArr = $relList;
            }
        }

        if(isset($shopCodeArr)){
            if(empty($shopCodeArr)){
                $shopCodeArr = array('0');
            }
            $condition['Shop.shopCode'] = array('IN', $shopCodeArr);
        }

        if($order == \SearchShopOrder::DISTANCE){
            $order = 'distance asc, r.createTime desc, Shop.isAcceptBankCard desc, Shop.onlinePaymentDiscount asc, Shop.sortNbr desc, Shop.shopStatus desc, Shop.createDate asc, Shop.creditPoint desc';
        }elseif($order == \SearchShopOrder::POPULARITY){
            $order = 'Shop.popularity desc, r.createTime desc, Shop.isAcceptBankCard desc, Shop.onlinePaymentDiscount asc, Shop.sortNbr desc, Shop.shopStatus desc, distance asc, Shop.createDate asc, Shop.creditPoint desc';
        }else{
            $order = 'r.createTime desc, Shop.isAcceptBankCard desc, Shop.onlinePaymentDiscount asc, Shop.sortNbr desc, Shop.shopStatus desc, distance asc, Shop.createDate asc, Shop.creditPoint desc';
        }

        // 联合表的信息
        $joinTableArr = array(
            array(
                'joinTable' => '(select createTime,shopCode from BatchCoupon where ((validityPeriod = 0) OR (expireTime >= NOW() AND validityPeriod = -1)) AND ((totalVolume = -1) OR (totalVolume > 0 AND remaining > 0)) AND isAvailable = 1 AND isSend = 0 AND couponType in (1,3,4,5,6,7,8) AND startTakingTime <= NOW() AND endTakingTime >= NOW() group by shopCode) r',
                'joinCondition' => 'r.shopCode = Shop.shopCode',
                'joinType' => 'LEFT')
        );
        $shopList = $shopMdl->searchShop($condition, '', $this->getPager($page), $joinTableArr, $order);
        foreach($shopList as $k => $v){
            $productMdl = new ProductModel();
            $shopList[$k]['hasNewProduct'] = 0;
            $newProductList = $productMdl->getNewProduct(array('productImg'), array('productStatus' => array('NEQ', C('PRODUCT_STATUS.OFF_SHELF')), 'shopCode' => $v['shopCode']), 4);
            if($newProductList){
                $shopList[$k]['hasNewProduct'] = 1;
                $shopList[$k]['newProductList'] = $newProductList;
            }
        }
        $shopCount = $shopMdl->countSearchShop($condition, $joinTableArr);
        return array(
            'totalCount' => $shopCount,
            'shopList' => $shopList,
            'page' => $page,
            'nextPage' => UtilsModel::getNextPage($shopCount, $page),
            'count' => count($shopList),
        );
    }

    /**
     * 获得商店详情
     * @param string $shopCode 商店编码
     * @param string $userCode 用户编码
     * @return array $shopInfo 商店信息
     */
	
    public function getShopInfo($shopCode, $userCode = '',$longitude,$latitude) {
        $shopMdl = new ShopModel();
        // 获得商家基本信息
        $shopInfo = $shopMdl->getShopInfo($shopCode, array('Shop.shopCode', 'Shop.shopName', 'Shop.longitude', 'Shop.latitude', 'Shop.district', 'Shop.street', 'Shop.tel', 'Shop.isOuttake', 'Shop.isOrderOn', 'Shop.popularity', 'onlinePaymentDiscount', 'businessHours', 'Shop.isCatering', 'Shop.city', 'Shop.isSuspended', 'Shop.shortDes', 'Shop.isAcceptBankCard'));
        $shopInfo['isFollowed'] = C('NO');
        $userCoupon = array();
        if($userCode) {
            $userCardMdl = new UserCardModel();
            $shopInfo['hasCard'] = $userCardMdl->isUserHasShopCard($userCode, $shopCode) ? C('YES') : C('NO');
            $shopFollowingMdl = new ShopFollowingModel();
            $shopInfo['isFollowed'] = $shopFollowingMdl->isUserFollowedShop($userCode, $shopCode) ? C('YES') : C('NO');
            $userCouponMdl = new UserCouponModel();
            $userCoupon = $userCouponMdl->listUserCouponInShop($userCode, $shopCode);
            $userPlatCoupon = $userCouponMdl->listUserCouponInShop($userCode, C('HQ_CODE'));
            $userCoupon = array_merge_recursive($userCoupon, $userPlatCoupon);
        }
        // 获得商家装修的图片
        $shopDecorationMdl = new ShopDecorationModel();
        $shopDecoration = $shopDecorationMdl->getShopDecoration($shopCode);
        $shopInfo['decImgCount'] = count($shopDecoration); // 商家装修图片数量
        // 是否显示惠支付按钮
        $shopInfo['showPayBtn'] = $shopMdl->isShowPayBtn($shopCode) ? C('YES') : C('NO');
        // 是否可以支付
        $shopInfo['ifCanPay'] = $shopMdl->ifCanPay($shopInfo['isSuspended'], $shopInfo['businessHours'], $shopInfo['isAcceptBankCard'], $shopCode) ? C('YES') : C('NO');
        // 获得最近访问
        $userEnterShopMdl = new UserEnterShopInfoRecordModel();
        $recentVisitor = $userEnterShopMdl->getShopRecentVisitor($shopCode, 20);
        // 获得商店商品图片10张
        $productMdl = new ProductModel();
        $shopPhotoList = $productMdl->getProductList(array('shopCode' => $shopCode), array('productName', 'productImg' => 'url', 'originalPrice', 'dropPrice', 'discount', 'productId', 'des', 'recommendLevel', 'notTakeoutPrice'), 'createTime desc', 0);
        foreach($shopPhotoList as $k => $v) {
            if($v['notTakeoutPrice'] > 0) {
                $finalPrice = $v['notTakeoutPrice'] * \Consts::HUNDRED;
            } else {
                // 制定商品最终价格
                $finalPrice = $productMdl->calProductFinalPrice($v['originalPrice'] * \Consts::HUNDRED, $v['discount'], $v['dropPrice'] * \Consts::HUNDRED);
            }
            $shopPhotoList[$k]['finalPrice'] = $finalPrice / \Consts::HUNDRED; // 最终价格，单位分化元
        }
        // 获得商家可领取的优惠券
        $batchCouponMdl = new BatchCouponModel();
        $shopCoupon = $batchCouponMdl->listAvailabelCoupon($shopCode,
            array('batchCouponCode','couponName','totalVolume','remaining','insteadPrice','discountPercent','availablePrice','endTakingTime','createTime','couponType','function','batchNbr','startUsingTime','expireTime','dayStartUsingTime','dayEndUsingTime','remark','city','shopName','BatchCoupon.payPrice','BatchCoupon.shopCode','sqrt(power(Shop.longitude-'.$longitude.',2) + power(Shop.latitude-'.$latitude.', 2))' => 'distance','Shop.logoUrl','CollectCoupon.isCollect','CollectCoupon.isGet',
            ), $userCode,$longitude,$latitude);//添加经纬度
        foreach($shopCoupon as $k => $v) {
            unset($shopCoupon[$k]['takenCount']);
            unset($shopCoupon[$k]['takenPercent']);
        }

        // 添加用户浏览商家详情记录
        $shopInfoRecord = new UserEnterShopInfoRecordModel();
        $shopInfoRecord->addRecord($shopCode, $userCode);

        // 获得商家的所有有效活动
        $actMdl = new ActivityModel();
        $activityList = $actMdl->getActList(array('pos' => array('NEQ', C('ACT_POS.SCROLL')), 'status' => C('ACTIVITY_STATUS.ACTIVE'), 'endTime' => array('EGT', date('Y-m-d H:i:s'))), array('activityCode', 'limitedParticipators'));
        $activityCodeArr = array('0');
        foreach($activityList as $v){
            if($v['limitedParticipators'] > 0){
                if($v['limitedParticipators'] > $v['participators']){
                    $activityCodeArr[] = $v['activityCode'];
                }
            }else{
                $activityCodeArr[] = $v['activityCode'];
            }
        }
        $actList = $actMdl->getActList(array('shopCode' => $shopCode, 'activityCode' => array('IN', $activityCodeArr)), array('activityCode', 'activityLogo', 'activityImg', 'activityName', 'txtContent', 'richTextContent', 'startTime', 'endTime', 'limitedParticipators', 'totalPayment', 'feeScale'), array(), 'startTime asc');

        // 获得附近其他商家
        if($shopInfo['longitude'] && $shopInfo['latitude']){
            $condition = array(
                'longitude' => $shopInfo['longitude'],
                'latitude' => $shopInfo['latitude'],
                'city' => $shopInfo['city'],
                'userCode' => $userCode,
                'Shop.shopCode' => array('neq', $shopCode)
            );
            $field = array(
                'Shop.shopCode',
                'Shop.shopName',
                'Shop.street',
                'sqrt(power(Shop.longitude-'.$shopInfo['longitude'].',2) + power(Shop.latitude-'.$shopInfo['latitude'].',2))' => 'distance',
                'Shop.logoUrl',
                'ShopType.typeValue' => 'type',
                'Shop.popularity',
                'Shop.isCatering',
            );
            $aroundShop = $shopMdl->searchShop($condition, $field, $this->getPager(1, 5));
        }else{
            $aroundShop = array();
        }

        // 获得商户荣誉墙
        $shopHonorMdl = new ShopHonorModel();
        $shopHonor = $shopHonorMdl->getShopHonorList(array('shopCode' => $shopCode), array('honorUrl'), array(), 'uploadTime desc');

        // 获得名师堂
        $shopTeacherMdl = new ShopTeacherModel();
        $shopTeacher = $shopTeacherMdl->getShopTeacherList(array('shopCode' => $shopCode), array('teachCourse', 'teacherImgUrl', 'teacherName', 'teacherTitle'), array());

        // 获得每日/周/月之星
        $studentStarMdl = new StudentStarModel();
        $studentStar = $studentStarMdl->getStudentStarList(array('shopCode' => $shopCode), array('starCode', 'starUrl', 'starInfo'), array(), 'iptTime desc', 1);

        // 获得商家的招生启示
        $shopRecruitMdl = new ShopRecruitModel();
        $shopRecruitInfo = $shopRecruitMdl->getShopRecruitInfo(array('shopCode' => $shopCode), array('recruitUrl', 'recruitCode'));

        // 获得商家的课程表
        $shopClassMdl = new ShopClassModel();
        $shopClass = $shopClassMdl->getShopClassList(
            array('ShopClass.shopCode' => $shopCode),
            array('ShopClass.classCode', 'ShopClass.className', 'ShopClass.learnStartDate', 'ShopClass.learnEndDate', 'ShopTeacher.teacherName'),
            array(array('joinTable' => 'ShopTeacher', 'joinCondition' => 'ShopTeacher.teacherCode = ShopClass.teacherCode', 'joinType' => 'inner'))
        );
        $classWeekInfoMdl = new ClassWeekInfoModel();
        foreach($shopClass as $k => $class) {
            $shopClass[$k]['classWeekInfo'] = $classWeekInfoMdl->getClassWeekList(array('classCode' => $class['classCode']), array('weekName', 'startTime', 'endTime'));
        }

        // 获得校长之语
        $shopHeaderMdl = new ShopHeaderModel();
        $shopHeader = $shopHeaderMdl->getShopHeaderInfo(array('shopCode' => $shopCode), array('expModel', 'imgUrl', 'txtMemo'));

        // 商家人气数量+1
        $shopMdl->increaseShopPopularity($shopCode);

        return array(
            'shopInfo' => $shopInfo,
            'couponList' => array('shopCoupon' => $shopCoupon, 'userCoupon' => $userCoupon),
            'shopDecoration' => $shopDecoration,
            'shopPhotoList' => $shopPhotoList,
            'actList' => $actList,
            'recentVisitor' => $recentVisitor, // 最近访问者
            'aroundShop' => $aroundShop, // 附近商户
            'shopHonor' => $shopHonor, // 荣誉墙
            'shopTeacher' => $shopTeacher, // 名师堂
            'studentStar' => $studentStar ? $studentStar[0] : array('starCode' => '', 'starUrl' => '', 'starInfo' => ''), // 每日/周/月之星
            'shopRecruitInfo' => $shopRecruitInfo ? $shopRecruitInfo : array('recruitUrl' => '', 'recruitCode' => ''),
            'shopClass' => $shopClass,
            'shopHeader' => $shopHeader ? $shopHeader : array('expModel' => 0, 'imgUrl' => '', 'txtMemo' => ''), // 校长之语
        );
    }

    /**
     * 获得商户优惠券和用户在该商户的优惠券信息
     * @param string $shopCode 商店编码
     * @param string $userCode 用户编码
     * @return array 优惠券信息
     */
    public function getShopUserCoupon($shopCode, $userCode) {
        // 获得商家基本信息
        $userCoupon = array();
        if($userCode) {
            $userCardMdl = new UserCardModel();
            $shopInfo['hasCard'] = $userCardMdl->isUserHasShopCard($userCode, $shopCode) ? C('YES') : C('NO');
            $shopFollowingMdl = new ShopFollowingModel();
            $shopInfo['isFollowed'] = $shopFollowingMdl->isUserFollowedShop($userCode, $shopCode) ? C('YES') : C('NO');
            $userCouponMdl = new UserCouponModel();
            $userCoupon = $userCouponMdl->listUserCouponInShop($userCode, $shopCode);
            $userPlatCoupon = $userCouponMdl->listUserCouponInShop($userCode, C('HQ_CODE'),$longitude,$latitude);
            $userCoupon = array_merge_recursive($userCoupon, $userPlatCoupon);
        }
        // 获得商家可领取的优惠券
        $batchCouponMdl = new BatchCouponModel();
        $shopCoupon = $batchCouponMdl->listAvailabelCoupon($shopCode,
            array(
                'batchCouponCode',
                'couponName',
                'totalVolume',
                'remaining',
                'insteadPrice',
                'discountPercent',
                'availablePrice',
                'endTakingTime',
                'createTime',
                'couponType',
                'function',
                'batchNbr',
                'startUsingTime',
                'expireTime',
                'dayStartUsingTime',
                'dayEndUsingTime',
                'remark',
                'city',
                'shopName',
                'BatchCoupon.payPrice',
                'BatchCoupon.shopCode',
            ), $userCode);
        foreach($shopCoupon as $k => $v) {
            unset($shopCoupon[$k]['takenCount']);
            unset($shopCoupon[$k]['takenPercent']);
        }
        return array(
            'shopCoupon' => $shopCoupon,
            'userCoupon' => $userCoupon,
        );
    }

    /**
     * 获得产品相册
     * @param string $shopCode 商家编码
     * @param string $page 页码。从1开始
     * @return array $albumList
     */
    public function cGetShopProductAlbum($shopCode, $page) {
        $subAlbumMdl = new SubAlbumModel();
        $subAlbumList = $subAlbumMdl->cListSubAlbum($shopCode, $this->getPager($page));
        $subAlbumCount = $subAlbumMdl->cCountSubAlbum($shopCode);
        $shopPhotoMdl = new ShopPhotoModel();
        foreach($subAlbumList as &$v) {
            $photoList = $shopPhotoMdl->listPhoto($v['code']);
//            $v['photoList'] = $photoList;
            $v['photoCount'] = count($photoList);
        }
        return array(
            'totalCount' => $subAlbumCount,
            'subAlbumList' => $subAlbumList,
            'page' => $page,
            'count' => count($subAlbumList)
        );
    }

    /**
     * 获得子相册图片
     * @param string $code 子相册编码
     * @return array $photoList
     */
    public function cGetSubAlbumPhoto($code) {
        $shopPhotoMdl = new ShopPhotoModel();
        $photoList = $shopPhotoMdl->getSubAlbumPhoto($code);
        return $photoList;
    }

    /**
     * 获取某家商家的装饰信息
     * @param string $shopCode 商家编码
     * @param string $page 页码。从1开始
     * @return array $shopDecoList
     */
    public function cGetShopDecoration($shopCode, $page){
        $shopDecorationMdl = new ShopDecorationModel();
        $shopDecoList = $shopDecorationMdl->listShopDecoration($shopCode, $this->getPager($page));
        $shopDecoCount = $shopDecorationMdl->countShopDecoration($shopCode);
        return array(
            'totalCount' => $shopDecoCount,
            'shopDecoList' => $shopDecoList,
            'page' => $page,
            'count' => count($shopDecoList)
        );
    }

    /**
     * 用户关注商家
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @return array $ret
     */
    public function followShop($userCode, $shopCode) {
        $shopFollowingMdl = new ShopFollowingModel();
        $ret = $shopFollowingMdl->followShop($userCode, $shopCode);
        return $ret;
    }

    /**
     * 取消关注商家
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @return array
     */
    public function cancelFollowShop($userCode, $shopCode) {
        $shopFollowingMdl = new ShopFollowingModel();
        $ret = $shopFollowingMdl->cancelFollowShop($userCode, $shopCode);
        return $ret;
    }

    /**
     * 获得关注
     * @param string $userCode 用户编码
     * @param double $longitude 用户经度
     * @param double $latitude 用户纬度
     * @param int $page 页码 从1开始
     * @return array
     */
    public function listFollowedShop($userCode, $longitude, $latitude, $page) {
        $userConsumeMdl = new UserConsumeModel();
        $isFirst = $userConsumeMdl->isFirst($userCode) == true ? C('YES') : C('NO');
        $shopMdl = new ShopModel();
        $shopList = $shopMdl->listUserShop($userCode, $longitude, $latitude, C('YES'), $this->getPager($page));
        foreach($shopList as &$shop) {
            $shop['isFirst'] = $isFirst;
            // 是否有工行折扣
            $shop['hasIcbcDiscount'] = C('NO');
            if($shop['isAcceptBankCard'] == C('YES')){
                $shop['hasIcbcDiscount'] = $shop['onlinePaymentDiscount'] == 100 ? C('NO') : C('YES');
            }
            $shop['hasCoupon'] = empty($shop['couponType']) ? C('NO') : C('YES');
        }
        $shopCount = $shopMdl->countUserShop($userCode, C('YES'));
        return array(
            'totalCount' => $shopCount,
            'shopList' => $shopList,
            'page' => $page,
            'count' => count($shopList),
        );
    }

    /**
     * 获得关注0.2
     * @param string $userCode 用户编码
     * @param double $longitude 用户经度
     * @param double $latitude 用户纬度
     * @param int $page 页码 从1开始
     * @return array
     */
    public function listFollowedShopB($userCode, $longitude, $latitude, $page) {
        // 获得用户关注的商户
        $shopFlMdl = new ShopFollowingModel();
        $userFlShopList = $shopFlMdl->listUserFollowShop($userCode);
        $userFlShopCodeList = array();
        foreach($userFlShopList as $shop) {
            $userFlShopCodeList[] = $shop['shopCode'];
        }
        if($userFlShopCodeList) {
            $condition = array('Shop.shopCode' => array('IN', $userFlShopCodeList), 'longitude' => $longitude, 'latitude' => $latitude, 'userCode' => $userCode);
            $shopMdl = new ShopModel();
            $shopList = $shopMdl->searchShop($condition, '', $this->getPager($page), '', 'Shop.sortNbr desc, distance asc, Shop.creditPoint desc');
            $shopCount = $shopMdl->countSearchShop($condition, '');
        } else {
            $shopList = array();
            $shopCount = 0;
        }
        return array(
            'totalCount' => $shopCount,
            'shopList' => $shopList,
            'page' => $page,
            'count' => count($shopList),
            'nextPage' => UtilsModel::getNextPage($shopCount, $page),
        );
    }

    /**
     * 获得足迹
     * @param string $userCode 用户编码
     * @param double $longitude 用户经度
     * @param double $latitude 用户纬度
     * @param int $page 页码 从1开始
     * @return array
     */
    public function listFootprint($userCode, $longitude, $latitude, $page) {
        $userConsumeMdl = new UserConsumeModel();
        $isFirst = $userConsumeMdl->isFirst($userCode) == true ? C('YES') : C('NO');
        $shopMdl = new ShopModel();
        $shopList = $shopMdl->listUserShop($userCode, $longitude, $latitude, C('NO'), $this->getPager($page));
        foreach($shopList as &$shop) {
            $shop['isFirst'] = $isFirst;
            // 是否有工行折扣
            $shop['hasIcbcDiscount'] = C('NO');
            if($shop['isAcceptBankCard'] == C('YES')){
                $shop['hasIcbcDiscount'] = $shop['onlinePaymentDiscount'] == 100 ? C('NO') : C('YES');
            }
            $shop['hasCoupon'] = empty($shop['couponType']) ? C('NO') : C('YES');
        }
        $shopCount = $shopMdl->countUserShop($userCode, C('NO'));
        return array(
            'totalCount' => $shopCount,
            'shopList' => $shopList,
            'page' => $page,
            'count' => count($shopList),
        );
    }

    /**
     * 获得足迹0.2
     * @param string $userCode 用户编码
     * @param double $longitude 用户经度
     * @param double $latitude 用户纬度
     * @param int $page 页码 从1开始
     * @return array
     */
    public function listFootprintB($userCode, $longitude, $latitude, $page) {
        // 获得我拥有过优惠券和我消费过的商户
        $userCouponMdl = new UserCouponModel();
        $hadCouponShopCodeList = $userCouponMdl->listHadCouponShopCode($userCode);
        $userConsumeMdl = new UserConsumeModel();
        $consumedShopCodeList = $userConsumeMdl->listConsumedShopCode($userCode);
        $shopCodeList = array_merge($hadCouponShopCodeList, $consumedShopCodeList);

        if($shopCodeList) {
            $condition = array('Shop.shopCode' => array('IN', $shopCodeList), 'longitude' => $longitude, 'latitude' => $latitude, 'userCode' => $userCode);
            $shopMdl = new ShopModel();
            $shopList = $shopMdl->searchShop($condition, array(), $this->getPager($page), '', 'Shop.sortNbr desc, distance asc, Shop.creditPoint desc, CONVERT(shopName USING gbk) asc');
            $shopCount = $shopMdl->countSearchShop($condition, '');
        } else {
            $shopList = array();
            $shopCount = 0;
        }
        return array(
            'totalCount' => $shopCount,
            'shopList' => $shopList,
            'page' => $page,
            'count' => count($shopList),
            'nextPage' => UtilsModel::getNextPage($shopCount, $page),
        );
    }

    /**
     * 获得附近
     * @param string $city 城市
     * @param double $longitude 用户经度
     * @param double $latitude 用户纬度
     * @param int $page 页码 从1开始
     * @param string $userCode
     * @return array
     */
    public function listNearShop($city, $longitude, $latitude, $page, $userCode) {
        $userConsumeMdl = new UserConsumeModel();
        $isFirst = $userConsumeMdl->isFirst($userCode) == true ? C('YES') : C('NO');
        $shopMdl = new ShopModel();
        $shopList = $shopMdl->listNearShop($city, $longitude, $latitude, $this->getPager($page));
        foreach($shopList as &$shop) {
            $shop['isFirst'] = $isFirst;
            // 是否有工行折扣
            $shop['hasIcbcDiscount'] = C('NO');
            if($shop['isAcceptBankCard'] == C('YES')){
                $shop['hasIcbcDiscount'] = $shop['onlinePaymentDiscount'] == 100 ? C('NO') : C('YES');
            }
            $shop['hasCoupon'] = empty($shop['couponType']) ? C('NO') : C('YES');
        }
        $shopCount = $shopMdl->CountNearShop($city, $city);
        return array(
            'totalCount' => $shopCount,
            'shopList' => $shopList,
            'page' => $page,
            'count' => count($shopList),
        );
    }

    /**
     * 获得附近0.2
     * @param string $city 城市
     * @param double $longitude 用户经度
     * @param double $latitude 用户纬度
     * @param int $page 页码 从1开始
     * @param string $userCode
     * @return array
     */
    public function listNearShopB($city, $longitude, $latitude, $page, $userCode) {
        $condition = array('longitude' => $longitude, 'latitude' => $latitude, 'userCode' => $userCode);
        if($city == '义乌市') {
            $condition['district'] = $city;
        } else {
            $condition['city'] = $city;
        }
        $shopMdl = new ShopModel();
        $shopList = $shopMdl->searchShop($condition, '', $this->getPager($page), '', 'Shop.sortNbr desc, distance asc, Shop.creditPoint desc');
        $shopCount = $shopMdl->countSearchShop($condition, '');
        return array(
            'totalCount' => $shopCount,
            'shopList' => $shopList,
            'page' => $page,
            'count' => count($shopList),
            'nextPage' => UtilsModel::getNextPage($shopCount, $page),
        );
    }

    /**
     * 获取顾客已关注的或者未关注的商家的会员卡列表
     * @param string $userCode 用户编码
     * @param number $longitude 顾客所在位置经度
     * @param number $latitude 顾客素在位置纬度
     * @param number $isFollowed 顾客是否关注的商店。1-是；2-否
     * @param int $page 分页
     * @return array $cardList
     */
    public function getCardList($userCode, $longitude, $latitude, $isFollowed, $page) {
        $userCardMdl = new UserCardModel();
        $cardList = $userCardMdl->getCardList($userCode, $longitude, $latitude, $isFollowed, $this->getPager($page));
        $cardCount = $userCardMdl->clientCountUserCard($userCode, $longitude, $latitude, $isFollowed);
        return array(
            'totalCount' => $cardCount,
            'cardList' => $cardList,
            'page' => $page,
            'count' => count($cardList),
        );
    }

    /**
     * 获得顾客在某商店拥有的最高级的会员卡
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @return array $userCardInfo
     */
    public function getBestUserCard($userCode, $shopCode) {
        $userCardMdl = new UserCardModel();
        $userCardInfo = $userCardMdl->getBestUserCard($userCode, $shopCode);
        return $userCardInfo;
    }

    /**
     * 用户的会员卡详情
     * @param string $userCardCode 会员卡编码
     * @return array $userCardInfo
     */
    public function getUserCardInfo($userCardCode) {
        $userCardMdl = new UserCardModel();
        $field = array(
            'qrCode',
            'barCode',
            'point',
            'cardNbr',
            'Shop.shopCode',
            'Shop.logoUrl',
            'shopName',
            'realName',
            'cardName',
            'cardLvl',
            'discount',
            'outPointsPerCash',
            'discountRequire'
        );
        $userCardInfo = $userCardMdl->getUserCardInfo($userCardCode, $field);
        $userCardInfo['pointDeduction'] = $userCardInfo['outPointsPerCash'] != 0 ? sprintf("%.2f", $userCardInfo['point'] / $userCardInfo['outPointsPerCash']) : 0;
        unset($userCardInfo['outPointsPerCash']);
        return $userCardInfo ? $userCardInfo : array();
    }

    /**
     * 用户申请会员卡
     * @param string $cardCode 卡编号
     * @param string $userCode 用户编码
     * @return array $ret
     */
    public function applyCard($cardCode, $userCode) {
        $userCardMdl = new UserCardModel();
        $ret = $userCardMdl->applyCard($cardCode, $userCode);
        return $ret;
    }

    /**
     * 搜索会员卡
     * @param string $searchWord 搜索关键字，匹配商店名或者地址
     * @param string $city 城市
     * @param double $longitude 用户经度
     * @param double $latitude 用户纬度
     * @param string $userCode 用户编码
     * @param int $page 页码 ，从1开始
     * @param int $isFollowed 是否获得用户关注的商家
     * @return array
     */
    public function searchCard($searchWord, $city, $longitude, $latitude, $userCode, $page, $isFollowed) {
        $cardMdl = new CardModel();
        $cardList = $cardMdl->searchCard($searchWord, $city, $longitude, $latitude, $userCode, $this->getPager($page), $isFollowed);
        $cardCount = $cardMdl->countSearchCard($searchWord, $city, $longitude, $latitude, $userCode, $isFollowed);
        return array(
            'totalCount' => $cardCount,
            'cardList' => $cardList,
            'page' => $page,
            'count' => count($cardList),
        );
    }

    /**
     * 获得顾客还未使用且有效的优惠券的数量
     * @param string $userCode 用户编码
     * @return array $ret
     */
    public function countUserNotUsedCoupon($userCode) {
        $userCouponMdl = new UserCouponModel();
        $ret['notUsedCouponNbr'] = $userCouponMdl->countUserNotUsedCoupon($userCode);
        return $ret;
    }

    /**
     * 根据优惠券类型，商家名称，顾客所在位置经度，纬度，页码获得优惠券列表信息
     * @param int $couponType 优惠券类型。1-N元购，3-抵扣券，4-折扣券，5-实物券，6-体验券, 7-兑换券, 8-代金券
     * @param string $searchWord 检索关键字，商家名称或者地名
     * @param number $longitude 用户所在经度
     * @param number $latitude 用户所在纬度
     * @param int $page 分页
     * @param string $city 城市
     * @param string $userCode 用户编码
     * @return array $couponList 有数据返回二维数组，没有数据返回空数组array()
     */
    public function listCoupon($couponType, $searchWord, $longitude, $latitude, $page, $city, $userCode = '') {
        $batchCouponMdl = new BatchCouponModel();
        $couponList = $batchCouponMdl->listCoupon($couponType, $searchWord, $longitude, $latitude, $this->getPager($page), $city, $userCode);
	 $couponCoupon = $batchCouponMdl->countCoupon($couponType, $searchWord, $longitude, $latitude, $city);
       return array(
            'totalCount' => $couponCoupon,
            'couponList' => $couponList,
            'page' => $page,
            'count' => count($couponList),
            'nextPage' => UtilsModel::getNextPage($couponCoupon, $page),
        );
		
		
    }

    /**
     * 获取用户已领的优惠券列表
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param number $status 优惠券状态。1-有效的；2-已使用；3-已失效；
     * @param number $page 页码
     * @param double $longitude 经度
     * @param double $latitude 纬度
     * @return array $userCouponList
     */
    public function getMyAvailableCoupon($userCode, $shopCode, $status, $page, $longitude, $latitude) {
        $userCouponMdl = new UserCouponModel();
        $userCouponList = $userCouponMdl->getMyAvailableCoupon($userCode, $shopCode, $status, $this->getPager($page), $longitude, $latitude);
        $userCouponCount = $userCouponMdl->countMyAvailableCoupon($userCode, $shopCode, $status);
        return array(
            'totalCount' => $userCouponCount,
            'userCouponList' => $userCouponList,
            'page' => $page,
            'count' => count($userCouponList),
        );
    }

    /**
     * 获取用户各状态的优惠券列表
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param number $status 优惠券状态。1-有效的；2-已使用；3-已失效；
     * @param number $page 页码
     * @param double $longitude 经度
     * @param double $latitude 纬度
     * @return array $userCouponList
     */
    public function listUserCouponByStatus($userCode, $shopCode, $status, $page, $longitude, $latitude) {
        $userCouponMdl = new UserCouponModel();
        $userCouponList = $userCouponMdl->listUserCouponByStatus($userCode, $shopCode, $status, $this->getPager($page), $longitude, $latitude, 1);
        $userCouponCount = $userCouponMdl->countUserCouponByStatus($userCode, $shopCode, $status);
        $result = array(
            'totalCount' => $userCouponCount,
            'userCouponList' => $userCouponList,
            'page' => $page,
            'nextPage' => UtilsModel::getNextPage($userCouponCount, $page),
            'count' => count($userCouponList),
        );
        return $result;
    }

    /**
     * 获得顾客已经领取的优惠券详细信息
     * @param string $userCouponCode 用户优惠券编码
     * @return array $ret
     */
    public function getUserCouponInfo($userCouponCode) {
        $userCouponMdl = new UserCouponModel();
        $userCouponInfo = $userCouponMdl->getUserCouponInfo($userCouponCode);
        return $userCouponInfo ? $userCouponInfo : array();
    }

    /**
     * 某种优惠券被使用的数量
     * @param string $batchCouponCode 优惠券编码
     * @return array $receivedCoupon
     */
    public function countReceivedCoupon($batchCouponCode) {
        $userCouponMdl = new UserCouponModel();
        $receivedCoupon = $userCouponMdl->countReceivedCoupon(array($batchCouponCode), $startDate = '', $endDate = '');
        return array('receivedCoupon' => $receivedCoupon);
    }

    /**
     * 修改优惠券分享状态
     * @param string $userCouponCode 用户优惠券编码
     * @param string $userCode 用户编码
     * @param number $sharedLvl 分享级别。0-所有人可见，1-朋友可见，2-其它人不可见
     * @return array $ret
     */
    public function updateCouponSharedStatus($userCouponCode, $userCode, $sharedLvl) {
        $userCouponMdl = new UserCouponModel();
        $ret = $userCouponMdl->updateCouponSharedStatus($userCouponCode, $userCode, $sharedLvl);
        return $ret;
    }

    /**
     * 领用/抢优惠券
     * @param string $batchCouponCode 优惠券编码
     * @param string $userCode 用户编码
     * @param number $sharedLvl 分享程度。0-所有人可见；1-朋友可见；2-其他人不可见
     * @return array $ret
     */
    public function grabCoupon($batchCouponCode, $userCode, $sharedLvl) {
        if(!isset($userCode) || $userCode == '') {
            return array('code' => C('USER.USER_CODE_EMPTY'));
        }
        $batchCouponMdl = new BatchCouponModel();
        $batchCouponInfo = $batchCouponMdl->getCouponInfo($batchCouponCode);
        if(!$batchCouponInfo['batchCouponCode']) {
            return array('code' => C('COUPON.NOT_EXIST'));
        }
        $shopCode = $batchCouponInfo['shopCode'];
        // 判断用户是否拥有商家的会员卡。若没有，则添加
        $userCardMdl = new UserCardModel();
        $checkUserCardRet = $userCardMdl->checkUserCard($userCode, $shopCode);
        if(!$checkUserCardRet) {
            return array('code' => C('CARD.ADD_USER_CARD_FAIL'));
        }
        $userCouponMdl = new UserCouponModel();
        $ret = $userCouponMdl->grabCoupon($batchCouponCode, $userCode, $sharedLvl);
        return $ret;
    }

    /**
     * 索取优惠券请求（https协议传输）
     * @param string $sellerCode 出让/被索取者编码
     * @param string $buyerCode 受让/被索取者编码
     * @param string $batchCouponCode 出让的优惠券编码
     * @param number $price 价格
     * @return array $ret
     */
    public function extortCouponRequest($sellerCode, $buyerCode, $batchCouponCode, $price) {
        $couponSaleLogMdl = new CouponSaleLogModel();
        $ret = $couponSaleLogMdl->extortCouponRequest($sellerCode, $buyerCode, $batchCouponCode, $price);
        return $ret;
    }

    /**
     * 索取优惠券应答（https协议传输）
     * @param string $logCode 优惠券交易记录编码
     * @param number $result 是否同意。1-同意，-1-不同意
     * @return array $ret
     */
    public function extortCouponReply($logCode, $result) {
        $couponSaleLogMdl = new CouponSaleLogModel();
        $ret = $couponSaleLogMdl->extortCouponReply($logCode, $result);
        return $ret;
    }

    /**
     * 转让优惠券（https协议传输）
     * @param string $sellerCode 出让/被索取者编码
     * @param string $buyerCode 受让/被索取者编码
     * @param string $batchCouponCode 出让的优惠券编码
     * @param number $price 价格
     * @return array $ret
     */
    public function transferCoupon($sellerCode, $buyerCode, $batchCouponCode, $price) {
        $couponSaleLogMdl = new CouponSaleLogModel();
        $ret = $couponSaleLogMdl->transferCoupon($sellerCode, $buyerCode, $batchCouponCode, $price);
        return $ret;
    }

    /**
     * 领取别人转让的优惠券（https协议传输）
     * @param string $logCode 优惠券交易记录编码
     * @param string $buyerCode 用户编码
     * @return array $ret
     */
    public function receiveCoupon($logCode, $buyerCode) {
        $couponSaleLogMdl = new CouponSaleLogModel();
        $ret = $couponSaleLogMdl->receiveCoupon($logCode, $buyerCode);
        return $ret;
    }

    /**
     * 获得平台发行的距离当前时间最近的红包信息
     * return array $bonusInfo 红包信息
     */
    public function getPlateBonus() {
        $bonusMdl = new BonusModel();
        $bonusInfo = $bonusMdl->getPlateBonus();
        return $bonusInfo ? $bonusInfo : array();
    }

    /**
     * 顾客获得抢到的某个商家的红包f
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @return array $bonusList
     */
    public function listUserShopBonus($userCode, $shopCode) {
        $userBonusMdl = new UserBonusModel();
        $bonusList = $userBonusMdl->listUserShopBonus($userCode, $shopCode);
        return $bonusList;
    }

    /**
     * 抢红包
     * @param string $userCode 顾客编码
     * @param string $bonusCode 红包编码
     * @return array $ret
     */
    public function grabBonus($userCode, $bonusCode) {
        $bonusMdl = new BonusModel();
        $ret = $bonusMdl->grabBonus($userCode, $bonusCode);
        return $ret;
    }

    /**
     * 获取首页的滚动信息
     * @return array $activityList
     */
    public function getScrollInfo() {
        $activityMdl = new ActivityModel();
        $activityList = $activityMdl->getScrollInfo();
        return $activityList ? $activityList : array();
    }

    /**
     * 获取活动列表
     * @param $longitude
     * @param $latitude
     * @param $page
     * @return array
     */
    public function getActList($longitude, $latitude, $page){
        $activityMdl = new ActivityModel();
        $order = 'Activity.createTime desc, shopName asc';

        $condition['pos'] = array('NEQ', C('ACT_POS.SCROLL'));
        $condition['Activity.status'] = C('ACTIVITY_STATUS.ACTIVE');
        $condition['endTime'] = array('GT', date('Y-m-d H:i:s'));
        $activityList = $activityMdl->getActList($condition, array('activityCode', 'limitedParticipators'));
        $activityCodeArr = array('0');
        foreach($activityList as $v){
            if($v['limitedParticipators'] > 0){
                if($v['limitedParticipators'] > $v['participators']){
                    $activityCodeArr[] = $v['activityCode'];
                }
            }else{
                $activityCodeArr[] = $v['activityCode'];
            }
        }
        $condition['activityCode'] = array('IN', $activityCodeArr);
        $field = array('activityCode', 'activityLogo', 'activityImg', 'activityName', 'txtContent', 'richTextContent', 'Activity.startTime', 'Activity.endTime', 'limitedParticipators', 'Shop.shopCode', 'shopName', 'feeScale', 'totalPayment');
        if($longitude && $latitude) {
            $field['format(sqrt(power(Shop.longitude-' . $longitude . ',2) + power(Shop.latitude-' . $latitude . ', 2)), 2) *'.C('PROPORTION')] = 'distance';
            $order = 'Activity.createTime desc, distance asc, shopName asc';
        }
        $joinTableArr = array(
            array('joinTable' => 'Shop', 'joinCondition' => 'Shop.shopCode = Activity.shopCode', 'joinType' => 'inner')
        );
        $activityList = $activityMdl->getActList($condition, $field, $joinTableArr, $order, \Consts::PAGESIZE, $page);
        $activityCount = $activityMdl->countActivity($condition, $joinTableArr);
        return array(
            'totalCount' => $activityCount,
            'activityList' => $activityList,
            'page' => $page,
            'nextPage' => UtilsModel::getNextPage($activityCount, $page),
            'count' => count($activityList),
        );
    }

    /**
     * 获取用户活动列表
     * @param $userCode
     * @param $type array(1 => '进行中', 2 => '已完成', 3 => '收藏')
     * @param $page
     * @return array
     */
    public function getUserActList($userCode, $type, $page){
        $userActCodeMdl = new UserActCodeModel();
        $userActivityMdl = new UserActivityModel();
        if($type != 3){
            if($type == 2){
                $userActivityCodeArr = $userActCodeMdl->getActCodeField(array('status' => array('IN', array( \Consts::ACT_CODE_STATUS_USED, \Consts::ACT_CODE_STATUS_REQUEST_REFUND, \Consts::ACT_CODE_STATUS_REFUND))), 'userActCode');
            }else{
                $userActivityCodeArr = $userActCodeMdl->getActCodeField(array('status' => array('IN', array(\Consts::ACT_CODE_STATUS_ACTIVE))), 'userActCode');
            }
            if($userActivityCodeArr){
                $userActivityCodeArr = array_unique($userActivityCodeArr);
                $condition['userActivityCode'] = array('IN', $userActivityCodeArr);
            }else{
                $condition['userActivityCode'] = array('IN', array('0'));
            }
        }else{
            $condition['UserActivity.isCollected'] = \Consts::YES;
        }
        $condition['pos'] = array('NEQ', C('ACT_POS.SCROLL'));
        $condition['userCode'] = $userCode;
        $condition['Activity.status'] = array('GT', C('ACTIVITY_STATUS.DELETE'));
        $joinTableArr = array(
            array('joinTable' => 'Activity', 'joinCondition' => 'Activity.activityCode = UserActivity.activityCode', 'joinType' => 'inner'),
        );
        $field = array('Activity.activityCode', 'activityLogo', 'activityImg', 'activityName', 'txtContent', 'richTextContent', 'Activity.startTime', 'Activity.endTime', 'limitedParticipators', 'feeScale', 'Activity.totalPayment', 'UserActivity.userActivityCode');
        $userActivityList = $userActivityMdl->getUserActList($condition, $field, $joinTableArr, '', \Consts::PAGESIZE, $page);
        $userActivityCount = $userActivityMdl->countUserActList($condition, $joinTableArr);
        $nextPage = UtilsModel::getNextPage($userActivityCount, $page);
        return array(
            'totalCount' => $userActivityCount,
            'list' => $userActivityList,
            'page' => $page,
            'nextPage' => $nextPage,
        );
    }

    /**
     * 用户活动报名详情
     * @param $userActivityCode
     * @return array
     */
    public function getUserActivityInfo($userActivityCode){
        $userActivityMdl = new UserActivityModel();
        $userActivityInfo = $userActivityMdl->getUserActInfo(array('userActivityCode' => $userActivityCode), array('activityCode'));
        $activityMdl = new ActivityModel();
        $activityInfo = $activityMdl->getActInfo(array('activityCode' => $userActivityInfo['activityCode']), array('activityCode', 'activityLogo', 'activityImg', 'activityName', 'txtContent', 'richTextContent', 'Activity.startTime', 'Activity.endTime', 'limitedParticipators', 'feeScale', 'totalPayment'));
        $userActCodeMdl = new UserActCodeModel();
        $userActCodeList = $userActCodeMdl->getActCodeList(array('userActCode' => $userActivityCode), array('userActCodeId','userActCode'=>'userActivityCode','status','scaleId','orderCode','actCode'), array(), '');
        $shareArr = array(
            'title' => $activityInfo['activityName'],
            'icon' => '/Public/img/avatar.jpg',
            'content' => $activityInfo['txtContent'],
            'link' => 'http://'.$_SERVER['HTTP_HOST'].'/Api/Browser/getActInfo?isShared=1&activityCode='.$activityInfo['activityCode']
        );
        return array(
            'activityInfo' => $activityInfo,
            'userActCodeList' => $userActCodeList,
            'shareArr' => $shareArr
        );
    }

    /**
     * 活动报名
     * @param string $userCode 用户编码
     * @param string $activityCode 活动编码
     * @param int $adultM 大人男性参与人数
     * @param int $adultF 大人女性参与人数
     * @param int $kidM 小孩男性参与人数
     * @param int $kidF 小孩女性参与人数
     * @return array $ret
     */
    public function joinActivity($userCode, $activityCode, $adultM, $adultF, $kidM, $kidF) {
        $userActivityMdl = new UserActivityModel();
        $ret = $userActivityMdl->joinActivity($userCode, $activityCode, $adultM, $adultF, $kidM, $kidF);
        return $ret;
    }

    /**
     * 判断用户是否报名该活动
     * @param string $userCode 用户编码
     * @param string $actCode 活动编码
     * @return int $isUserJoinAct
     */
    public function isUserJoinAct($userCode, $actCode) {
        $userActMdl = new UserActivityModel();
        $isUserJoinAct = $userActMdl->isUserJoinAct($userCode, $actCode);
        $code = $isUserJoinAct ? C('YES') : C('NO');
        return array('code' => $code);
    }

    /**
     * 退出活动
     * @param string $userActCode 用户活动编码
     * @return array $ret
     */
    public function exitAct($userActCode) {
        $userActMdl = new UserActivityModel();
        $ret = $userActMdl->exitAct($userActCode);
        return $ret;
    }

    /**
     * 获得用户活动详情
     * @param string $userActCode 用户活动编码
     * @return array $userActInfo
     */
    public function getUserActInfo($userActCode) {
        $userActMdl = new UserActivityModel();
        $userActInfo = $userActMdl->getUserActInfo(array('userActivityCode' => $userActCode));
        return $userActInfo;
    }

    /**
     * 用户活动验证码
     * @param string $actCode 用户活动验证码
     * @return array 一维关联数组
     */
    public function getUserActCodeInfo($actCode) {
        // 获得用户活动码的详情
        $userActCodeMdl = new UserActCodeModel();
        $userActCodeInfo = $userActCodeMdl->getActCodeInfo(
            array('actCode' => $actCode),
            array('UserActCode.status', 'ConsumeOrder.orderNbr', 'UserActCode.price', 'ConsumeOrder.refundAmount', 'UserActCode.orderCode', 'UserActCode.actCode', 'UserActCode.refundRemark', 'UserActCode.refundReason'),
            array(array('joinTable' => 'ConsumeOrder', 'joinCondition' => 'ConsumeOrder.orderCode = UserActCode.orderCode', 'joinType' => 'inner'))
        );
        $userActCodeInfo['toRefundAmount'] = $userActCodeInfo['price']; // 退款金额，单位：分
        // 获得订单的支付详情
        $userConsumeMdl = new UserConsumeModel();
        $orderInfo = $userConsumeMdl->getCurrConsumeInfoByOrderCode($userActCodeInfo['orderCode']);
        // 计算退款各部分金额
        $toRefundDetail = $userActCodeMdl->caltoRefundDetail($userActCodeInfo['toRefundAmount'], $orderInfo['realPay'], $orderInfo['shopBonus'], $orderInfo['platBonus'], $userActCodeInfo['refundAmount']);

        unset($userActCodeInfo['price']);
        unset($userActCodeInfo['refundAmount']);

        $userActCodeInfo['bankcardRefund'] = $toRefundDetail['bankcardRefund']; // 工行金额
        $userActCodeInfo['platBonusRefund'] = $toRefundDetail['platBonusRefund']; // 平台红包
        $userActCodeInfo['shopBonusRefund'] = $toRefundDetail['shopBonusRefund']; // 商户红包
        $userActCodeInfo['refundExplain'] = '3-10个工作日退回'; // 退款说明
        $userActCodeInfo['hotLine'] = '400-04-95588'; // 热线电话

        // 退款原因
        $sltReason = array(
            array('id' => 1, 'text' => '计划有变，没时间参加活动'),
            array('id' => 2, 'text' => '后悔了不想参加活动了'),
            array('id' => 3, 'text' => '不小心买的多了'),
            array('id' => 4, 'text' => '其他原因'),
        );
        foreach($sltReason as $k => $v) {
            $sltReason[$k]['selected'] = $v['id'] == $userActCodeInfo['refundReason'] ? \Consts::YES : \Consts::NO;
        }
        $userActCodeInfo['sltReason'] = $sltReason;
        $userActCodeInfo['toRefundAmount'] =  $userActCodeInfo['toRefundAmount'] / \Consts::HUNDRED; // 退款金额，单位：分

        return $userActCodeInfo;
    }

    /**
     * 活动码申请退款
     * @param string $orderCode 订单编码
     * @param string $actCode 活动验证码
     * @param float $bankcardRefund 工行卡金额，单位：元
     * @param float $platBonusRefund 平台红包金额，单位：元
     * @param float $shopBonusRefund 商户红包金额，单位：元
     * @param int $refundReason 退款原因。1-计划有变，没时间参加活动；2-后悔了不想参加活动了；3-不小心买的多了；4-其他原因；
     * @param string $refundRemark 退款备注
     * @return array
     */
    public function actCodeApplyRefund($orderCode, $actCode, $bankcardRefund, $platBonusRefund, $shopBonusRefund, $refundReason, $refundRemark) {
        $userActCodeMdl = new UserActCodeModel();
        $userActCodeInfo = $userActCodeMdl->getActCodeInfo(array('orderCode' => $orderCode, 'actCode' => $actCode, 'status' => \Consts::ACT_CODE_STATUS_ACTIVE), array('price', 'buyTime'));
        if($userActCodeInfo) {
            // 判断用户是否可以退款
            $isAllowedRefund = $userActCodeMdl->isAllowedRefund($actCode);
            if($isAllowedRefund) {
                // 执行用户活动码的退款
                $ret = $userActCodeMdl->actCodeRefund($actCode, $orderCode, $bankcardRefund * \Consts::HUNDRED, $platBonusRefund * \Consts::HUNDRED, $shopBonusRefund * \Consts::HUNDRED, $refundReason, $refundRemark);
                $code = $ret === true ? C('SUCCESS') : $ret;
            } else {
                $code = C('USER_ACT_CODE.CAN_NOT_REFUND');
            }
        } else {
            $code = C('USER_ACT_CODE.CAN_NOT_REFUND');
        }
        return array('code' => $code);
    }

    /**
     * 修改用户报名活动信息
     * @param string $userActCode 用户活动编码
     * @param int $adultM 成年人男性数量
     * @param int $adultF 成年人女性数量
     * @param int $kidM 小孩男性数量
     * @param int $kidF 小孩女性数量
     * @return array $ret
     */
    public function editUserActInfo($userActCode, $adultM, $adultF, $kidM, $kidF) {
        $userActMdl = new UserActivityModel();
        $userActInfo = array(
            'adultM' => $adultM,
            'adultF' => $adultF,
            'kidM' => $kidM,
            'kidF' => $kidF,
        );
        $ret = $userActMdl->editUserAct(array('userActivityCode' => $userActCode), $userActInfo);
        return $ret;
    }

    /**
     * 获取各类短消息，包括优惠券，会员卡和商家广播信息
     * @param string $userCode 用户编码
     * @param number $type 消息类型。0-商家消息；1-会员卡消息；2-优惠券消息；
     * @param number $page 分页，从1开始
     * @return array $messageList 消息列表
     */
    public function getMessageList($userCode, $type, $page) {
        $userMessageMdl = new UserMessageModel();
        $messageList = $userMessageMdl->getMessageList($userCode, $type, $this->getPager($page));
        $messageCount = $userMessageMdl->countMessage($userCode, $type, 2);
        $userCouponMdl = new UserCouponModel();
        $userMsgCodeList = array(); // 用户消息编码
        foreach($messageList as $k => $msg) {
            if($type == C('MESSAGE_TYPE.COUPON')) {
                // 如果消息类型是优惠券消息，获得优惠券消息的优惠券信息
                $userCouponInfo = $userCouponMdl->getUserCouponInfoB(array('userCouponCode' => $msg['userCouponCode']), array('UserCoupon.batchCouponCode', 'BatchCoupon.couponType'));
                $messageList[$k]['batchCouponCode'] = $userCouponInfo['batchCouponCode'] ? $userCouponInfo['batchCouponCode'] : '';
                $messageList[$k]['couponType'] = $userCouponInfo['couponType'] ? $userCouponInfo['couponType'] : '';
            } else {
                $messageList[$k]['batchCouponCode'] = '';
                $messageList[$k]['couponType'] = '';
            }
            $userMsgCodeList[] = $msg['userMsgCode'];
        }
        // 将获得的消息的阅读状态改为已读
        if($userMsgCodeList) {
            $userMessageMdl->where(array('userMsgCode' => array('IN', $userMsgCodeList)))->save(array('readingStatus' => C('MESSAGE_READING_TYPE.READ')));
        }

        return array(
            'totalCount' => $messageCount,
            'messageList' => $messageList,
            'page' => $page,
            'count' => count($messageList),
        );
    }

    /**
     * 获取用户银行卡列表
     * @param string $userCode 用户编码
     * @param int $page 分页
     * @return array $bankAccountList
     */
    public function getBankAccountList($userCode, $page) {
        $bankAccountMdl = new BankAccountModel();
        $bankAccountList = $bankAccountMdl->getBankAccountList($userCode, $this->getPager($page));
        $bankAccountCount = $bankAccountMdl->countBankAccount($userCode);
        return array(
            'totalCount' => $bankAccountCount,
            'bankAccountList' => $bankAccountList,
            'page' => $page,
            'count' => count($bankAccountList),
        );
    }

    /**
     * 添加银行卡
     * @param string $userCode 用户编码
     * @param string $accountName 账号姓名
     * @param string $idType 证件类型。0-身份证，1-护照，2-军官证，3-士兵证，4-港澳通行证，5-临时身份证，6-户口簿，7-其他，9-警官证，12-外国人居留证
     * @param string $idNbr 证件号
     * @param string $accountNbrPre6 银行卡卡号前6位
     * @param string $accountNbrLast4 银行卡卡号后4位
     * @param string $mobileNbr 手机号
     * @return array $ret
     */
    public function addBankAccount($userCode, $accountName, $idType, $idNbr, $accountNbrPre6, $accountNbrLast4, $mobileNbr) {
        $bankAccountMdl = new BankAccountModel();
        // 获得该银行卡当前处于被绑定状态的次数
        $signedCount = $bankAccountMdl->countSignedCard(array('accountNbrPre6' => $accountNbrPre6, 'accountNbrLast4' => $accountNbrLast4, 'status' => \Consts::BANKACCOUNT_STATUS_SIGNED));
        $systemParamMdl = new SystemParamModel();
        // 获得一张银行卡处于绑定状态时，可被多少用户拥有
        $paramInfo = $systemParamMdl->getParamValue('userNbrUnderOneBankcard');
        if($signedCount >= $paramInfo['value']) { // 银行卡当前处于被绑定状态的次数 大于等于 要求值时，返回错误
            return array('code' => C('BANK_ACCOUNT.OVER_LIMIT'));
        }
        // 保存账户信息
        $ret = $bankAccountMdl->addBankAccount($userCode, $accountName, $idType, $idNbr, $accountNbrPre6, $accountNbrLast4, $mobileNbr);
        return $ret;
    }

    /**
     * 添加银行卡(改)
     * @param string $userCode 用户编码
     * @param string $accountName 账号姓名
     * @param string $idType 证件类型。0-身份证，1-护照，2-军官证，3-士兵证，4-港澳通行证，5-临时身份证，6-户口簿，7-其他，9-警官证，12-外国人居留证
     * @param string $idNbr 证件号
     * @param string $bankCard 银行卡卡号
     * @param string $mobileNbr 手机号
     * @return array $ret
     */
    public function addBankAccountModify($userCode, $accountName, $idType, $idNbr, $bankCard, $mobileNbr) {
        $bankAccountMdl = new BankAccountModel();
        // 获得该银行卡当前处于被绑定状态的用户数
        $signedCount = $bankAccountMdl->countSignedCard(array('bankCard' => $bankCard, 'status' => \Consts::BANKACCOUNT_STATUS_SIGNED));
        $systemParamMdl = new SystemParamModel();
        // 获得一张银行卡处于绑定状态时，可被多少用户拥有
        $paramInfo = $systemParamMdl->getParamValue('userNbrUnderOneBankcard');
        if($signedCount >= $paramInfo['value']) { // 银行卡当前处于被绑定状态的次数 大于等于 要求值时，返回错误
            return array('code' => C('BANK_ACCOUNT.OVER_LIMIT'));
        }
        // 保存账户信息
        $ret = $bankAccountMdl->addBankAccountModify($userCode, $accountName, $idType, $idNbr, $bankCard, $mobileNbr);
        return $ret;
    }

    /**
     * 获取绑定工银快捷支付验证码
     * @param string $orderNbr 绑定银行卡的订单编码
     * @return array $ret
     */
    public function getSignCardValCode($orderNbr) {
        $bankAccountMdl = new BankAccountModel();
        // 根据订单号，获得未绑定状态下的账户信息
        $bankAccountInfo = $bankAccountMdl->getBankAccountInfo(array('orderNbr' => $orderNbr, 'status' => \Consts::BANKACCOUNT_STATUS_NO_SIGN));
        if(empty($bankAccountInfo)) {
            return array('code' => C('BANK_ACCOUNT.ORDER_NBR'));
        }

        // 判断当前用户是否绑定过相同卡号的账户
        $signedBankAccountInfo = $bankAccountMdl->getBankAccountInfo(array(
            'userCode' => $bankAccountInfo['userCode'],
            'accountNbrPre6' => $bankAccountInfo['accountNbrPre6'],
            'accountNbrLast4' => $bankAccountInfo['accountNbrLast4'],
            'status' => \Consts::BANKACCOUNT_STATUS_SIGNED,
        ));
        if($signedBankAccountInfo) {
            return array('code' => C('BANK_ACCOUNT.REPEAT'));
        } else {
            $userMdl = new UserModel();
            $userInfo = $userMdl->getUserInfo(array('userCode' => $bankAccountInfo['userCode']), array('mobileNbr'));
            $cpmtxsnoLogMdl = new CmptxsnoLogModel();
            $cpmtxsno = $cpmtxsnoLogMdl->getCmptxsno();
            // 请求工行验证码
            $icbcMdl = new IcbcModel();
            $pubArr = $icbcMdl->getMsgValCode($cpmtxsno, '1100', $bankAccountInfo['bankCard'], $bankAccountInfo['accountName'], 0, $bankAccountInfo['idType'], $bankAccountInfo['idNbr'], $bankAccountInfo['mobileNbr'], $orderNbr, $userInfo['mobileNbr'] . $bankAccountInfo['accountNbrLast4']);
            if($pubArr['retcode'] == C('ICBC_PAY_SUCCESS')) {
                return array('code' => C('SUCCESS'), 'valCode' => '');
            } else {
                $retmsg = $pubArr['retmsg'];
                // 保存错误信息
                $bankAccountMdl->editBankAccount(array('orderNbr' => $orderNbr), array('errMsg' => $retmsg));
                return array('code' => $pubArr['retcode'] ? $pubArr['retcode'] : '', 'retmsg' => $retmsg);
            }
        }
    }

    /**
     * 取消添加银行卡（https协议传输）
     * @param string $orderNbr 签订协议订单号
     * @return array $ret
     */
    public function cancelBankAccount($orderNbr) {
        $bankAccountMdl = new BankAccountModel();
        // 根据订单号，获得未绑定状态下的账户信息
        $bankAccountInfo = $bankAccountMdl->getBankAccountInfo(array('orderNbr' => $orderNbr, 'status' => \Consts::BANKACCOUNT_STATUS_NO_SIGN));
        if(empty($bankAccountInfo)) {
            return array('code' => C('BANK_ACCOUNT.ORDER_NBR'));
        }
        // 取消添加银行卡
        $ret = $bankAccountMdl->cancelBankAccount(array('orderNbr' => $orderNbr));
        return $ret;
    }

    /**
     * 确认绑定银行卡（https协议传输）
     * @param string $orderNbr 签订协议订单号
     * @param string $valCode 验证码
     * @param string $sellerId 商户号。1.单独签约送1100；2.签约与支付共用验证码时送商户号，目前送12位商户号，清算功能投入后送10位商户号
     * @return array $ret
     */
    public function signBankAccount($orderNbr, $valCode, $sellerId = '1100') {
        $sellerId = $sellerId ? $sellerId : '1100';
        $bankAccountMdl = new BankAccountModel();
        // 根据订单号，获得未绑定状态下的账户信息
        $bankAccountInfo = $bankAccountMdl->getBankAccountInfo(array('orderNbr' => $orderNbr, 'status' => \Consts::BANKACCOUNT_STATUS_NO_SIGN));
        if(empty($bankAccountInfo)) {
            return array('code' => C('BANK_ACCOUNT.ORDER_NBR'));
        }

        // 判断当前用户是否绑定过相同卡号的账户
        $signedBankAccountInfo = $bankAccountMdl->getBankAccountInfo(array(
            'userCode' => $bankAccountInfo['userCode'],
            'bankCard' => $bankAccountInfo['bankCard'],
            'status' => \Consts::BANKACCOUNT_STATUS_SIGNED,
        ));
        if($signedBankAccountInfo) {
            return array('code' => C('BANK_ACCOUNT.REPEAT'));
        }

        // 获得用户信息
        $userMdl = new UserModel();
        $userInfo = $userMdl->getUserInfo(array('userCode' => $bankAccountInfo['userCode']), array('mobileNbr', 'recomNbr'));
        // 请求工行银行卡支付协议签订交易的API
        $icbcMdl = new IcbcModel();
        $pubArr = $icbcMdl->paymentAgreement($orderNbr, $sellerId, $bankAccountInfo['bankCard'], $bankAccountInfo['accountName'], $bankAccountInfo['idType'], $bankAccountInfo['idNbr'], $bankAccountInfo['mobileNbr'], $userInfo['mobileNbr'] . $bankAccountInfo['accountNbrLast4'], $valCode);
        if($pubArr['retcode'] == C('ICBC_PAY_SUCCESS')) {
            // 更新用户银行卡状态为已签订协议，更新最后一次操作时间
            $code = $bankAccountMdl->updateBankAccount(array(
                'bankAccountCode' => $bankAccountInfo['bankAccountCode'],
                'status' => \Consts::BANKACCOUNT_STATUS_SIGNED,
                'lastOperationTime' => date('Y-m-d H:i:s')
            )) == true ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');

            // 绑定银行卡时送绑卡用户50圈值，并更新历史圈值和当前圈值
            $addUserPointRet = $userMdl->addPointEarningLog($bankAccountInfo['userCode'], 50, '绑定工行卡');

            // 获得用户邀请人的信息
            $inviterInfo = $userMdl->getUserInfo(array('inviteCode' => $userInfo['recomNbr']), array('userCode'));
            if($inviterInfo && ! empty($userInfo['recomNbr'])) { // 如果邀请人存在
                $bonusMdl = new UserBonusModel();

                /** 用户第一次签约银行卡，且该银行卡被签约过（状态为已签约，或已解约）的次数（注：同一用户视为1次）小于等于3时，送该用户的邀请人红包 */
                // 获得用户签约银行卡的次数，（状态为已签约，或已解约）
//                $userSignedCount = $bankAccountMdl->countSignedCard(array(
//                    'userCode' => $bankAccountInfo['userCode'],
//                    'status' => array('IN', array(\Consts::BANKACCOUNT_STATUS_SIGNED, \Consts::BANKACCOUNT_STATUS_TERMINATE)),
//                ));
//                // 判断该银行卡被绑定过次数,状态为已签约，或已解约
//                $bankcardSignedCount = $bankAccountMdl->getBankAccountCount(array(
//                    'BankAccount.bankCard' => $bankAccountInfo['bankCard'],
//                    'BankAccount.status' => array('IN', array(\Consts::BANKACCOUNT_STATUS_SIGNED, \Consts::BANKACCOUNT_STATUS_TERMINATE)),
//                ), array(), 'DISTINCT(userCode)');
//                if($userSignedCount == 1 && $bankcardSignedCount <= 3) {
//                    // 给邀请人赠送红包
//                    $bonusMdl->sendInviterBonus($inviterInfo['userCode']);
//                }
                // 送邀请人50圈值
                $userMdl->addPointEarningLog($inviterInfo['userCode'], 50, '被邀请人绑定工行卡');
            }
        } else {
            $code = $pubArr['retcode'];
        }
        $retmsg = C('ICBC_ERROR_CODE_MSG.' . $pubArr['retcode']);
        // 保存工行返回的中文信息
        $bankAccountMdl->editBankAccount(array('orderNbr' => $orderNbr), array('errMsg' => $retmsg));
        return array('code' => $code ? $code : '', 'retmsg' => $retmsg ? $retmsg : ($pubArr['retmsg'] ? $pubArr['retmsg'] : ''));
    }

    /**
     * 确认解除绑定银行卡（https协议传输）
     * @param string $bankAccountCode 用户银行账号编码
     * @return array $ret
     */
    public function terminateBankAccount($bankAccountCode) {
        $bankAccountMdl = new BankAccountModel();
        // 获得账户信息
        $bankAccountInfo = $bankAccountMdl->getBankAccountInfo(array('bankAccountCode' => $bankAccountCode, 'status' => \Consts::BANKACCOUNT_STATUS_SIGNED));
        if(empty($bankAccountInfo)) {
            return array('code' => C('BANK_ACCOUNT.CODE_ERROR'));
        }
        // 若已经解约，返回提示代码
        if($bankAccountInfo['status'] == \Consts::BANKACCOUNT_STATUS_TERMINATE) {
            return array('code' => C('BANK_ACCOUNT.TERMINATED'));
        }
        // 调用工行交易协议解除API
        $userMdl = new UserModel();
        $userInfo = $userMdl->getUserInfo(array('userCode' => $bankAccountInfo['userCode']), array('mobileNbr'));
        $cpmtxsnoLogMdl = new CmptxsnoLogModel();
        $cpmtxsno = $cpmtxsnoLogMdl->getCmptxsno();
        $reqData = array(
            // 公共
            'txcode' => \Consts::TXCODE_TERMINATE_CONTRACT, // 交易码：支付协议解除交易
            'cmptxsno' => $cpmtxsno, // 公司方流水号(总长不超过20位)，保证流水号的唯一性
            'sellerid' => '1100',//工行提供的一级商户代码，默认送1100
            // 私有
            'cardno' => $bankAccountInfo['bankCard'], // 银行卡号（完整卡号或者卡号前6位，后4位拼接而成）
            'username' => $bankAccountInfo['accountName'], // 账户户名
            'idtype' => $bankAccountInfo['idType'], // 证件类型
            'idno' => $bankAccountInfo['idNbr'], // 证件号
            'mobilephone' => $bankAccountInfo['mobileNbr'], // 手机号码
            'userno' => $userInfo['mobileNbr'] . $bankAccountInfo['accountNbrLast4'], // 用户号
        );
        $icbcMdl = new IcbcModel();
        $ret = $icbcMdl->processPay($reqData);
        $pubArr = get_object_vars($ret->pub);
        if($ret->pub->retcode == C('ICBC_PAY_SUCCESS')) { // 银行返回交易成功
            // 更新银行账户的状态为已解约
            $code = $bankAccountMdl->updateBankAccount(array(
                'bankAccountCode' => $bankAccountInfo['bankAccountCode'],
                'status' => \Consts::BANKACCOUNT_STATUS_TERMINATE,
                'lastOperationTime' => date('Y-m-d H:i:s', time())
            )) == true ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        } else {
            $code = $pubArr['retcode'];
        }
        $retmsg = C('ICBC_ERROR_CODE_MSG.' . $pubArr['retcode']);
        return array('code' => $code, 'retmsg' => $retmsg ? $retmsg : ($pubArr['retmsg'] ? $pubArr['retmsg'] : ''));
    }

    /**
     * 银行卡详情
     * @param string $bankAccountCode 用户银行卡编码
     * @return array $bankAccountInfo 用户银行卡信息，没有数据时返会空数组array()
     */
    public function getBankAccountInfo($bankAccountCode) {
        $bankAccountMdl = new BankAccountModel();
        $bankAccountInfo = $bankAccountMdl->getBankAccountInfo(array('bankAccountCode' => $bankAccountCode));
        return $bankAccountInfo ? $bankAccountInfo : array();
    }

    /**
     * 付款时获得用户可使用的优惠券列表
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param float $consumeAmount 消费金额 单位：元
     * @return array $userCouponList
     */
    public function listUserCoupon($userCode, $shopCode, $consumeAmount) {
        $userCouponMdl = new UserCouponModel();
        $userCouponList = $userCouponMdl->listUserCouponWhenPay($userCode, $shopCode, $consumeAmount);
        $platCouponList = $userCouponMdl->listPlatCouponWhenPay($userCode, $shopCode, $consumeAmount);
        $userCouponList = array_merge_recursive($userCouponList, $platCouponList);
        $result['coupon'] = $userCouponList;

        $result['card'] = array(
            'canUseCard'=>C('NO'),
            'discount'=>C('NO')
        );

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
            $userCardPoint = $userCardInfo['point'];
            if($userCardPoint >= $userCardInfo['discountRequire']){
                $result['card']['canUseCard'] = C('YES');
                $result['card']['discount'] = $userCardInfo['discount'];
            }
        }

        $shopMdl = new ShopModel();
        // 获得商家的工行卡打折相关信息
        $result['icbc'] = $shopMdl->getShopInfo($shopCode, array('onlinePaymentDiscount', 'onlinePaymentDiscountUpperLimit'));
        $systemParamMdl = new SystemParamModel();
        $paramInfo = $systemParamMdl->getParamValue('minRealPay');
        $result['minRealPay'] = $paramInfo['value'] / C('RATIO');

        $mealFirstDec = $systemParamMdl->getParamValue('mealFirstDec');
        $result['mealFirstDec'] = $mealFirstDec['value'] / C('RATIO');
        $userConsumeMdl = new UserConsumeModel();
        $result['isFirst'] = $userConsumeMdl->isFirst($userCode) == true ? C('YES') : C('NO');
        return $result;
    }

    /**
     * 付款时获得用户可使用的红包列表
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param float $consumeAmount 消费金额，单位：元
     * @return array $userCouponList
     */
    public function listUserBonus($userCode, $shopCode, $consumeAmount) {
        $userBonusMdl = new UserBonusModel();
        $userBonusList = $userBonusMdl->listUserBonusWhenPay($userCode, $shopCode, $consumeAmount);
        return $userBonusList;
    }


    /**
     * 选择优惠券或者红包后返回实际需付款金额。优惠券和红包二选一
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param float $consumeAmount 消费金额，单位：元
     * @param string $userCouponCode 用户优惠券编码
     * @param string $platBonus 使用的平台红包金额，单位：元
     * @param float $shopBonus 使用的商家红包金额，单位：元
     * @param int $payType 支付类型：1-线上银行卡支付;2-POS机支付;3-现金支付
     * @return float $newPrice
     */
    public function getNewPrice($userCode, $shopCode, $consumeAmount, $userCouponCode, $platBonus, $shopBonus, $payType) {
        $userConsumeMdl = new UserConsumeModel();
        $isFirst = $userConsumeMdl->isFirst($userCode);
        $firstDeduction = 0;

        // 元化成分
        $newPrice = $consumeAmount * C('RATIO');
        $platBonus = $platBonus * C('RATIO');
        $shopBonus = $shopBonus * C('RATIO');

        $userCouponMdl = new UserCouponModel();
        $userCouponInfo = $userCouponMdl->getCouponInfo($userCouponCode, array('couponType', 'insteadPrice', 'discountPercent'));
        if($userCouponInfo && $userCouponInfo['couponType'] == C('COUPON_TYPE.N_PURCHASE')) {
            return array('code'=> C('SUCCESS'), 'newPrice' => number_format($userCouponInfo['insteadPrice'] / C('RATIO'), 2, '.', ''), 'cardInsteadPrice' => 0, 'couponInsteadPrice' => 0);
        }
        $couponInsteadPrice = 0;
        //优惠券
        if($userCouponInfo) {
            if($userCouponInfo['couponType'] == C('COUPON_TYPE.DISCOUNT')) {
                $couponInsteadPrice = $newPrice - $newPrice * $userCouponInfo['discountPercent'] / C('RATIO');
            }else{
                $couponInsteadPrice = $userCouponInfo['insteadPrice'];
            }
        }
        $couponInsteadPrice = UtilsModel::phpCeil($couponInsteadPrice);
        $newPrice = $newPrice - $couponInsteadPrice;

        $cardInsteadPrice = 0;
        $userCardMdl = new UserCardModel();
        $userCard = $userCardMdl->getBestUserCard($userCode, $shopCode);
        if($userCard) {
            $userCardCode = $userCard['userCardCode'];
            $field = array(
                'UserCard.point',
                'Card.discount',
                'Card.discountRequire',
                'Card.cardCode',
            );
            $userCardInfo = $userCardMdl->getUserCardInfo($userCardCode, $field);
            $userCardPoint = $userCardInfo['point'];
            if($userCardPoint >= $userCardInfo['discountRequire']) {
                $cardInsteadPrice = $newPrice - $newPrice * $userCardInfo['discount'] / 10;
            }
        }
        $cardInsteadPrice = UtilsModel::phpCeil($cardInsteadPrice);
        $newPrice = $newPrice - $cardInsteadPrice;

        $bsMdl = new BonusStatisticsModel();
        if($platBonus > 0){
            $platBonusRet = $bsMdl->getUserBonusStatistics($userCode, C('HQ_CODE'));
            if($platBonusRet && $platBonusRet['totalValue'] >= $platBonus) {
                $newPrice = $newPrice - UtilsModel::phpCeil($platBonus);
            } else {
                return array('code'=>C('BONUS.PLAT_UPPER_ERROR'), 'cardInsteadPrice' => $cardInsteadPrice, 'couponInsteadPrice' => $couponInsteadPrice);
            }
        }

        if($shopBonus > 0){
            $shopBonusRet = $bsMdl->getUserBonusStatistics($userCode, $shopCode);
            if($shopBonusRet && $shopBonusRet['totalValue'] >= $shopBonus){
                $newPrice = $newPrice - UtilsModel::phpCeil($platBonus);
            }else{
                return array('code'=>C('BONUS.SHOP_UPPER_ERROR'), 'cardInsteadPrice' => $cardInsteadPrice, 'couponInsteadPrice' => $couponInsteadPrice);
            }
        }

        // 在线支付则计算商家对银行卡的打折
        $bankCardDeduction = 0;
        if($payType == C('UC_PAY_TYPE.BANKCARD')) {
            $shopMdl = new ShopModel();
            // 获得商家信息
            $shopInfo = $shopMdl->getShopInfo($shopCode, array('onlinePaymentDiscount', 'onlinePaymentDiscountUpperLimit'));
            // 商家对银行卡的折扣
            if($shopInfo['onlinePaymentDiscount'] < 10) {
                $bankCardDeduction = $newPrice - $newPrice * $shopInfo['onlinePaymentDiscount'] / C('DISCOUNT_RATIO');
                if($shopInfo['onlinePaymentDiscountUpperLimit'] != 0) {
                    // 判断抵扣额是否大于商店设置的上限
                    if($bankCardDeduction > $shopInfo['onlinePaymentDiscountUpperLimit'] * C('RATIO')) {
                        $bankCardDeduction = $shopInfo['onlinePaymentDiscountUpperLimit'] * C('RATIO');
                    }
                }
                $bankCardDeduction = UtilsModel::phpCeil($bankCardDeduction);
                $newPrice = $newPrice - $bankCardDeduction;
            }
        }

        $firstDeduction = 0;
        if($isFirst == true) {
            $systemParamMdl = new SystemParamModel();
            $paramInfo = $systemParamMdl->getParamValue('mealFirstDec');
            $firstDeduction = $paramInfo['value'] ? $paramInfo['value'] : 0; // 首单立减金额
            // 减去首单立减的金额
            $newPrice = $newPrice - $firstDeduction;

            $paramInfo = $systemParamMdl->getParamValue('minRealPay');
            $minRealPay = $paramInfo['value']; // 最小付款金额
            if($newPrice < $minRealPay) {
                // 最终付款金额小于最小付款金额时，最终付款金额等于最小付款金额
                $newPrice = $minRealPay;
            }
        }

        $temp = array('newPrice', 'cardInsteadPrice', 'couponInsteadPrice', 'bankCardDeduction', 'firstDeduction');
        foreach($temp as $v) {
            $$v = number_format($$v / C('RATIO'), 2, '.', '');
        }

        return array(
            'code' => C('SUCCESS'),
            'newPrice' => $newPrice < 0 ? 0 : $newPrice,
            'cardInsteadPrice' => $cardInsteadPrice,
            'couponInsteadPrice' => $couponInsteadPrice,
            'bankCardDeduction' => $bankCardDeduction,
            'firstDeduction' => $firstDeduction,
        );
    }


    /**
     * 买单查询出的信息
     * @param $userCode
     * @param $shopCode
     * @param $consumeAmount
     * @param $batchCouponCode
     * @return array
     */
    public function listUserPayInfo($userCode, $shopCode, $consumeAmount, $batchCouponCode){
        //满就送信息
        $result['hasSendCoupon'] = C('NO');
        $result['sendCoupon'] = '';
        $batchCouponMdl = new BatchCouponModel();
        $sendCouponRet = $batchCouponMdl->getMaxSendCouponInfo($userCode, $shopCode, $consumeAmount * C('RATIO'));
        if($sendCouponRet){
            $result['hasSendCoupon'] = C('YES');
            if(count($sendCouponRet) < 2){
                $sendCouponRet = $sendCouponRet[0];
                if($sendCouponRet['couponType'] == C('COUPON_TYPE.N_PURCHASE')){
                    $result['sendCoupon'] = '送￥'.$sendCouponRet['insteadPrice'].$sendCouponRet['function'].'优惠券';
                }elseif($sendCouponRet['couponType'] == C('COUPON_TYPE.REDUCTION')){
                    $result['sendCoupon'] = '每满'.$sendCouponRet['availablePrice'].'元送'.$sendCouponRet['insteadPrice'].'元优惠券';
                }elseif($sendCouponRet['couponType'] == C('COUPON_TYPE.DISCOUNT')){
                    $result['sendCoupon'] = '满'.$sendCouponRet['availablePrice'].'元送'.$sendCouponRet['discountPercent'].'折优惠券';
                }else{
                    $result['sendCoupon'] = '送'.$sendCouponRet['function'].'优惠券';
                }
            }else{
                if($consumeAmount > 0){
                    $sendCouponRet = $sendCouponRet[0];
                    if($sendCouponRet['couponType'] == C('COUPON_TYPE.N_PURCHASE')){
                        $result['sendCoupon'] = '送￥'.$sendCouponRet['insteadPrice'].$sendCouponRet['function'].'优惠券';
                    }elseif($sendCouponRet['couponType'] == C('COUPON_TYPE.REDUCTION')){
                        $result['sendCoupon'] = '每满'.$sendCouponRet['availablePrice'].'元送'.$sendCouponRet['insteadPrice'].'元优惠券';
                    }elseif($sendCouponRet['couponType'] == C('COUPON_TYPE.DISCOUNT')){
                        $result['sendCoupon'] = '满'.$sendCouponRet['availablePrice'].'元送'.$sendCouponRet['discountPercent'].'折优惠券';
                    }else{
                        $result['sendCoupon'] = '送'.$sendCouponRet['function'].'优惠券';
                    }
                }else{
                    $result['sendCoupon'] = '成功消费一定金额送对应的优惠券';
                }
            }
        }

        //活动信息
//        $result['hasShopAct'] = C('NO');
//        $result['shopAct'] = '';
//        $result['hasPlatAct'] = C('NO');
//        $result['platAct'] = '';

        //会员卡信息
        $result['card'] = array(
            'canUseCard' => C('NO'),
            'discount' => C('NO')
        );
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
            $userCardPoint = $userCardInfo['point'];
            if($userCardPoint >= $userCardInfo['discountRequire']){
                $result['card']['canUseCard'] = C('YES');
                $result['card']['discount'] = $userCardInfo['discount'];
            }
        }

        //红包信息
        $result['bonus'] = array(
            'canUseShopBonus' => C('YES'),
            'shopBonus' => C('NO'),
            'canUsePlatBonus' => C('YES'),
            'platBonus' => C('NO'),
        );
        $bsMdl = new BonusStatisticsModel();
        $platBonusRet = $bsMdl->getUserBonusStatistics($userCode, C('HQ_CODE'));
        if($platBonusRet && $platBonusRet['totalValue'] > C('NO')) {
            $result['bonus']['platBonus'] = number_format($platBonusRet['totalValue'] / C('RATIO'), 2, '.', '');
        } else {
            $result['bonus']['canUsePlatBonus'] = C('NO');
        }
        $shopBonusRet = $bsMdl->getUserBonusStatistics($userCode, $shopCode);
        if($shopBonusRet && $shopBonusRet['totalValue'] > C('NO')) {
            $result['bonus']['shopBonus'] = number_format($shopBonusRet['totalValue'] / C('RATIO'), 2, '.', '');
        } else {
            $result['bonus']['canUseShopBonus'] = C('NO');
        }

        //优惠券信息
        $result['coupon'] = array(
            'reduction' => C('NO'), //抵扣券
            'minReduction' => -1, // 达到多少金额可用(抵扣券)
            'discount' => C('NO'), //折扣券
            'minDiscount' => -1 // 达到多少金额可用(折扣券)
        );
        $userCouponMdl = new UserCouponModel();
        if($batchCouponCode){ // 选中优惠券进行买单（限单张使用）
            $userCouponList = $userCouponMdl->listUserCouponWhenPay1($userCode, $shopCode, $consumeAmount, C('NO'), $batchCouponCode);
            $platCouponList = $userCouponMdl->listPlatCouponWhenPay1($userCode, $shopCode, $consumeAmount, C('NO'), $batchCouponCode);
            $userCouponList = array_merge_recursive($userCouponList, $platCouponList);
            $result['coupon']['couponInfo'] = $userCouponList[0] ? $userCouponList[0] : array();
        }else{ // 从工行卡按钮进去的买单
            //商家发行的用户已领的抵扣券
            $userReductionCouponList = $userCouponMdl->listUserCouponWhenPay1($userCode, $shopCode, $consumeAmount, C('COUPON_TYPE.REDUCTION'));
            $reductionCoupon = -1; // 最小的“达到多少金额可用”
            if($userReductionCouponList){
                $reductionCoupon = $userReductionCouponList[0]['availablePrice'];
            }
            //平台发行的用户已领的抵扣券
            $platReductionCouponList = $userCouponMdl->listPlatCouponWhenPay1($userCode, $shopCode, $consumeAmount, C('COUPON_TYPE.REDUCTION'));
            if($platReductionCouponList && $platReductionCouponList[0]['availablePrice'] < $reductionCoupon){
                $reductionCoupon = $platReductionCouponList[0]['availablePrice'];
            }
            $reduction = array_merge_recursive($userReductionCouponList, $platReductionCouponList);
            $result['coupon']['reduction'] = count($reduction) > 0 ? C('YES') : C('NO');

            //商家发行的用户已领的折扣券
            $userDiscountCouponList = $userCouponMdl->listUserCouponWhenPay1($userCode, $shopCode, $consumeAmount, C('COUPON_TYPE.DISCOUNT'));
            $discountCoupon = -1; // 最小的“达到多少金额可用”
            if($userDiscountCouponList){
                $discountCoupon = $userDiscountCouponList[0]['availablePrice'];
            }
            //平台发行的用户已领的折扣券
            $platDiscountCouponList = $userCouponMdl->listPlatCouponWhenPay1($userCode, $shopCode, $consumeAmount, C('COUPON_TYPE.DISCOUNT'));
            if($platDiscountCouponList && $platDiscountCouponList[0]['availablePrice'] < $discountCoupon){
                $discountCoupon = $platDiscountCouponList[0]['availablePrice'];
            }
            $discount = array_merge_recursive($userDiscountCouponList, $platDiscountCouponList);
            $result['coupon']['discount'] = count($discount) > 0 ? C('YES') : C('NO');

            // 如果用户没有领取任何可用的优惠券，则列出可用的用户未领取的商家优惠券
            if($result['coupon']['reduction'] == C('NO')){
                $coupon = $batchCouponMdl->listUserCouponWhenPay($userCode, $shopCode, $consumeAmount, C('COUPON_TYPE.REDUCTION'));
                if($coupon){
                    $reductionCoupon = $coupon[0]['availablePrice'];
                }
                $result['coupon']['reduction'] = count($coupon) > 0 ? C('YES') : C('NO');
            }
            if($result['coupon']['discount'] == C('NO')){
                $coupon = $batchCouponMdl->listUserCouponWhenPay($userCode, $shopCode, $consumeAmount, C('COUPON_TYPE.DISCOUNT'));
                if($coupon){
                    $discountCoupon = $coupon[0]['availablePrice'];
                }
                $result['coupon']['discount'] = count($coupon) > 0 ? C('YES') : C('NO');
            }
            $result['coupon']['minReduction'] = $reductionCoupon;
            $result['coupon']['minDiscount'] = $discountCoupon;
        }

        //工行折扣，首单立减，最小支付等信息
        $shopMdl = new ShopModel();
        // 获得商家对使用工行卡的折扣的相关信息
        $result['icbc'] = $shopMdl->getShopInfo($shopCode, array('onlinePaymentDiscount', 'onlinePaymentDiscountUpperLimit'));
        $result['icbc']['canDiscount'] = C('NO');
        if($result['icbc']['onlinePaymentDiscount'] < 10 && $result['icbc']['onlinePaymentDiscount'] > 0){
            $result['icbc']['canDiscount'] = C('YES');
        }
        $systemParamMdl = new SystemParamModel();
        $paramInfo = $systemParamMdl->getParamValue('minRealPay');
        $result['minRealPay'] = $paramInfo['value'] / C('RATIO');
        $mealFirstDec = $systemParamMdl->getParamValue('mealFirstDec');
        $result['mealFirstDec'] = $mealFirstDec['value'] / C('RATIO');
        $userConsumeMdl = new UserConsumeModel();
        $result['isFirst'] = $userConsumeMdl->isFirst($userCode) == true ? C('YES') : C('NO');
        return $result;
    }

    /**
     * 获取用户买单的时候的优惠券列表，以批次为为主要信息，加了用户有某一批次的张数
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param int $consumeAmount 消费金额
     * @param int $couponType 优惠券类型 3:抵扣券,4:折扣券
     * @return array
     */
    public function listUserCouponWhenPay($userCode, $shopCode, $consumeAmount, $couponType){
        $userCouponMdl = new UserCouponModel();
        $userCouponList = $userCouponMdl->listUserCouponWhenPay1($userCode, $shopCode, $consumeAmount, $couponType);
        $platCouponList = $userCouponMdl->listPlatCouponWhenPay1($userCode, $shopCode, $consumeAmount, $couponType);
        $userCouponList = array_merge_recursive($userCouponList, $platCouponList);
//        var_dump($userCouponList);
        return $userCouponList;
    }

    /**
     * 获取用户买单的时候的未领取的可用的商家列表
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param int $consumeAmount 消费金额
     * @param int $couponType 优惠券类型 3:抵扣券,4:折扣券
     * @return array
     */
    public function listUserNoGrabCouponWhenPay($userCode, $shopCode, $consumeAmount, $couponType){
        $batchCouponMdl = new BatchCouponModel();
        return $batchCouponMdl->listUserCouponWhenPay($userCode, $shopCode, $consumeAmount, $couponType);
    }

    /**
     * 选择优惠券或者红包后返回实际需付款金额。优惠券和红包二选一
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param float $consumeAmount 消费金额，单位：元
     * @param string $batchCouponCode 优惠券编码
     * @param int $nbrCoupon 优惠券张数
     * @param string $platBonus 使用的平台红包金额，单位：元
     * @param float $shopBonus 使用的商家红包金额，单位：元
     * @param int $payType 支付类型：1-线上银行卡支付;2-POS机支付;3-现金支付
     * @param float $noDiscountPrice 不参与优惠的金额，单位：元
     * @return float $newPrice
     */
    public function getNewPriceForAndroid($userCode, $shopCode, $consumeAmount, $batchCouponCode, $nbrCoupon, $platBonus, $shopBonus, $payType, $noDiscountPrice) {
        $userConsumeMdl = new UserConsumeModel();
        $isFirst = $userConsumeMdl->isFirst($userCode); // 是否为首单
        if(is_null($noDiscountPrice) || $noDiscountPrice == '(null)' || empty($noDiscountPrice)){
            $noDiscountPrice = 0;
        }

        // 元化成分
        $newPrice = ($consumeAmount - $noDiscountPrice) * C('RATIO');
        $platBonus = $platBonus * C('RATIO');
        $shopBonus = $shopBonus * C('RATIO');

        $couponInsteadPrice = 0;
        if($batchCouponCode && $nbrCoupon > C('NO')){
            $userCouponMdl = new UserCouponModel();
            $userCouponList = $userCouponMdl->listUserCouponWhenPay1($userCode, $shopCode, ($consumeAmount - $noDiscountPrice), C('NO'), $batchCouponCode);
            $platCouponList = $userCouponMdl->listPlatCouponWhenPay1($userCode, $shopCode, ($consumeAmount - $noDiscountPrice), C('NO'), $batchCouponCode);

            $userCouponList = array_merge_recursive($userCouponList, $platCouponList);

            if($userCouponList){
                $userCouponInfo = $userCouponList[0];
                if($userCouponInfo['couponType'] == C('COUPON_TYPE.REDUCTION') ){
                    $count = number_format(($newPrice - $newPrice % ($userCouponInfo['availablePrice'] * C('RATIO'))) / ($userCouponInfo['availablePrice'] * C('RATIO')));
                    $canUse = UtilsModel::getMinNbr(array($userCouponInfo['limitedNbr'], $userCouponInfo['userCount'], $count));
                    if($nbrCoupon > $canUse){
                        return array('code' => C('COUPON.USE_LIMIT_NBR'));
                    }
                }else{
                    if($nbrCoupon > 1){
                        return array('code' => C('COUPON.USE_LIMIT_NBR'));
                    }
                }
                if($userCouponInfo['couponType'] == C('COUPON_TYPE.N_PURCHASE')) {
                    return array('code'=> C('SUCCESS'), 'newPrice' => $userCouponInfo['insteadPrice'], 'cardInsteadPrice' => 0, 'couponInsteadPrice' => 0);
                }elseif($userCouponInfo['couponType'] == C('COUPON_TYPE.DISCOUNT')) {
                    $couponInsteadPrice = $newPrice - $newPrice * $userCouponInfo['discountPercent'] / 10;
                }else{
                    $couponInsteadPrice = $userCouponInfo['insteadPrice'] * C('RATIO') * $nbrCoupon;
                }
            } else {
                return array('code' => C('COUPON.NOT_AVAILABLE'));
            }
        }
        $couponInsteadPrice = UtilsModel::phpCeil($couponInsteadPrice);

        $newPrice = $newPrice - $couponInsteadPrice;

        $cardInsteadPrice = 0;
        $userCardMdl = new UserCardModel();
        $userCard = $userCardMdl->getBestUserCard($userCode, $shopCode);
        if($userCard) {
            $userCardCode = $userCard['userCardCode'];
            $field = array(
                'UserCard.point',
                'Card.discount',
                'Card.discountRequire',
                'Card.cardCode',
            );
            $userCardInfo = $userCardMdl->getUserCardInfo($userCardCode, $field);
            $userCardPoint = $userCardInfo['point'];
            if($userCardPoint >= $userCardInfo['discountRequire']) {
                $cardInsteadPrice = $newPrice - $newPrice * $userCardInfo['discount'] / 10;
            }
        }
        $cardInsteadPrice = UtilsModel::phpCeil($cardInsteadPrice);
        $newPrice = $newPrice - $cardInsteadPrice;

        $bsMdl = new BonusStatisticsModel();
        if($platBonus > 0){
            $platBonusRet = $bsMdl->getUserBonusStatistics($userCode, C('HQ_CODE'));
            if($platBonusRet && $platBonusRet['totalValue'] >= $platBonus) {
                $newPrice = $newPrice - UtilsModel::phpCeil($platBonus);
            } else {
                return array('code'=>C('BONUS.PLAT_UPPER_ERROR'), 'cardInsteadPrice' => $cardInsteadPrice, 'couponInsteadPrice' => $couponInsteadPrice);
            }
        }

        if($shopBonus > 0){
            $shopBonusRet = $bsMdl->getUserBonusStatistics($userCode, $shopCode);
            if($shopBonusRet && $shopBonusRet['totalValue'] >= $shopBonus){
                $newPrice = $newPrice - UtilsModel::phpCeil($platBonus);
            }else{
                return array('code'=>C('BONUS.SHOP_UPPER_ERROR'), 'cardInsteadPrice' => $cardInsteadPrice, 'couponInsteadPrice' => $couponInsteadPrice);
            }
        }

        // 在线支付则计算商家对银行卡的打折
        $bankCardDeduction = 0;
        if($payType == C('UC_PAY_TYPE.BANKCARD')) {
            $shopMdl = new ShopModel();
            // 获得商家信息
            $shopInfo = $shopMdl->getShopInfo($shopCode, array('onlinePaymentDiscount', 'onlinePaymentDiscountUpperLimit'));
            // 商家对银行卡的折扣
            if($shopInfo['onlinePaymentDiscount'] < 10 && $shopInfo['onlinePaymentDiscount'] > 0) {
                $bankCardDeduction = $newPrice - $newPrice * $shopInfo['onlinePaymentDiscount'] / C('DISCOUNT_RATIO');
                if($shopInfo['onlinePaymentDiscountUpperLimit'] != 0) {
                    // 判断抵扣额是否大于商店设置的上限
                    if($bankCardDeduction > $shopInfo['onlinePaymentDiscountUpperLimit'] * C('RATIO')) {
                        $bankCardDeduction = $shopInfo['onlinePaymentDiscountUpperLimit'] * C('RATIO');
                    }
                }
                $bankCardDeduction = UtilsModel::phpCeil($bankCardDeduction);
                $newPrice = $newPrice - $bankCardDeduction;
            }
        }

        $systemParamMdl = new SystemParamModel();
        $paramInfo = $systemParamMdl->getParamValue('minRealPay');
        $minRealPay = $paramInfo['value'];

        if($isFirst == true) {
            $systemParamMdl = new SystemParamModel();
            $paramInfo = $systemParamMdl->getParamValue('mealFirstDec');
            $firstDeduction = $paramInfo['value'] ? $paramInfo['value'] : 0; // 10元
            $newPrice = $newPrice - $firstDeduction;
            $newPrice = $newPrice < 0 ? 0 : $newPrice;
            $newPrice = $newPrice + $noDiscountPrice * C('RATIO');
            if($newPrice < $minRealPay){
                $newPrice = $minRealPay;
            }
        }else{
            $price = $consumeAmount * C('RATIO');
            if($noDiscountPrice > 0 && $newPrice < 0){
                return array('code' => C('ORDER.MIN_DISCOUNT_PAY_ERROR'));
            }
            $newPrice = $newPrice + $noDiscountPrice * C('RATIO');
            if($price > $minRealPay && $minRealPay > $newPrice){
                return array('code' => C('ORDER.MIN_PAY_ERROR'));
            }
        }

        $temp = array('newPrice', 'cardInsteadPrice', 'couponInsteadPrice', 'bankCardDeduction');
        foreach($temp as $v) {
            $$v = number_format($$v / C('RATIO'), 2, '.', '');
        }
        return array('code'=> C('SUCCESS'), 'newPrice' => $newPrice, 'cardInsteadPrice' => $cardInsteadPrice, 'couponInsteadPrice' => $couponInsteadPrice, 'bankCardDeduction' => $bankCardDeduction);
    }

    /**
     * 生成顾客消费订单
     * @param string $userCode 用户编码
     * @param number $price 消费金额 单位：元
     * @param string $shopCode 商家编码
     * @return array $ret
     */
    public function addOrder($userCode, $price, $shopCode) {
        $consumeOrderMdl = new ConsumeOrderModel();
        $ret = $consumeOrderMdl->addConsumeOrder($userCode, $price * C('RATIO'), $shopCode);
        return $ret;
    }

    /**
     * 取消订单
     * @param string $orderCode 订单编码
     * @return array $ret
     */
    public function cancelOrder($orderCode) {
        $consumeOrderMdl = new ConsumeOrderModel();
        // 获得订单详情
        $orderInfo = $consumeOrderMdl->getOrderInfo(array('orderCode' => $orderCode));

        if(!in_array($orderInfo['orderStatus'], array(\Consts::ORDER_STATUS_ORDERED, \Consts::ORDER_STATUS_UNORDERED))) { // 用户主动取消订单时，订单状态如果不在已下单和待下单状态则不能取消订单
            return array('code' => C('ORDER.NOT_EQ_UNRECEIVED'));
        } else {
            $ret = $consumeOrderMdl->cancelConsumeOrder($orderCode);
            return $ret;
        }
    }

    /**
     * 在线支付。
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param float $orderAmount 消费金额
     * @param string $userCouponCode 用户优惠券编码
     * @param float $platBonus 平台红包金额，单位：元
     * @param float $shopBonus 商户红包金额，单位：元
     * @return array $ret
     */
    public function bankcardPay($userCode, $shopCode, $orderAmount, $userCouponCode, $platBonus, $shopBonus) {
        $consumeOrderMdl = new ConsumeOrderModel();
        // 为该笔支付生成相应的订单
        $ret = $consumeOrderMdl->addConsumeOrder($userCode, $orderAmount * C('RATIO'), $shopCode);
        $orderNbr = $ret['orderNbr'];
        if($ret['code'] == C('SUCCESS')) {
            $userConsumeMdl = new UserConsumeModel();
            // 生成在线支付记录
            $ret = $userConsumeMdl->bankcardPay($ret['orderCode'], $userCouponCode, $platBonus, $shopBonus);
            if($ret['code'] == C('SUCCESS')) {
                $ret['orderNbr'] = $orderNbr;
            }
        }
        return $ret;
    }

    /**
     * 在线支付。多张优惠券的使用
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param float $orderAmount 消费金额
     * @param string $batchCouponCode 优惠券编码
     * @param int $nbrCoupon 优惠券张数
     * @param string $platBonus 平台红包金额，单位：元
     * @param string $shopBonus 商户红包金额，单位：元
     * @param float $noDiscountPrice 不参与优惠的金额
     * @return array $ret
     */
    public function bankcardPayForAndroid($userCode, $shopCode, $orderAmount, $batchCouponCode, $nbrCoupon, $platBonus, $shopBonus, $noDiscountPrice) {
        if(is_null($noDiscountPrice) || $noDiscountPrice == '(null)' || empty($noDiscountPrice)){
            $noDiscountPrice = 0;
        }
        $consumeOrderMdl = new ConsumeOrderModel();
        // 为该笔支付生成相应的订单
        $ret = $consumeOrderMdl->addConsumeOrder($userCode, $orderAmount * \Consts::HUNDRED, $shopCode);
        $orderNbr = $ret['orderNbr'];
        if($ret['code'] == C('SUCCESS')) {
            $userCouponCode = '';
            if($batchCouponCode){
                $userCouponMdl = new UserCouponModel();
                // 获取买单时用户最先领用的用户优惠券编码串，以“|”分隔
                $userCouponCode = $userCouponMdl->getAvailableUserCouponCode($userCode, $batchCouponCode, $nbrCoupon);
            }
            $userConsumeMdl = new UserConsumeModel();
            // 生成在线支付记录
            $ret = $userConsumeMdl->bankcardPay($ret['orderCode'], $userCouponCode, $platBonus, $shopBonus, $noDiscountPrice);
            if($ret['code'] == C('SUCCESS')) {
                $ret['orderNbr'] = $orderNbr;
            }
        }
        return $ret;
    }

    /**
     * 取消在线支付（https协议传输）
     * @param string $consumeCode 订单编码
     * @return array $ret
     */
    public function cancelBankcardPay($consumeCode) {
        $userConsumeMdl = new UserConsumeModel();
        $ret = $userConsumeMdl->cancelBankcardPay($consumeCode);
        return $ret;
    }

    /**
     * 产品订单的在线支付。多张优惠券的使用
     * @param string $orderCode 用户编码
     * @param string $batchCouponCode 优惠券编码
     * @param int $nbrCoupon 优惠券张数
     * @param string $platBonus 平台红包金额，单位：元
     * @param string $shopBonus 商户红包金额，单位：元
     * @param float $noDiscountPrice 不参与优惠的金额
     * @return array $ret
     */
    public function pOBankcardPayForAndroid($orderCode, $batchCouponCode, $nbrCoupon, $platBonus, $shopBonus, $noDiscountPrice) {
        if(is_null($noDiscountPrice) || $noDiscountPrice == '(null)' || empty($noDiscountPrice)){
            $noDiscountPrice = 0;
        }
        $consumeOrderMdl = new ConsumeOrderModel();
        // 获得订单信息
        $orderInfo = $consumeOrderMdl->getOrderInfo(array('orderCode' => $orderCode), array('orderNbr', 'clientCode'));
        $orderNbr = $orderInfo['orderNbr'];
        $userCouponMdl = new UserCouponModel();
        // 获取买单时用户最先领用的用户优惠券编码串，以“|”分隔
        $userCouponCode = $userCouponMdl->getAvailableUserCouponCode($orderInfo['clientCode'], $batchCouponCode, $nbrCoupon);
        $userConsumeMdl = new UserConsumeModel();
        // 生成在线支付记录
        $ret = $userConsumeMdl->bankcardPay($orderCode, $userCouponCode, $platBonus, $shopBonus, $noDiscountPrice);
        if($ret['code'] == C('SUCCESS')) {
            $ret['orderNbr'] = $orderNbr;
        }
        return $ret;
    }

    /**
     * 产品订单的在线支付
     * @param string $orderCode 用户编码
     * @param string $userCouponCode 用户优惠券编码
     * @param string $platBonus 平台红包金额，单位：元
     * @param string $shopBonus 商户红包金额，单位：元
     * @return array $ret
     */
    public function pOBankcardPay($orderCode, $userCouponCode, $platBonus, $shopBonus) {
        $consumeOrderMdl = new ConsumeOrderModel();
        // 获得订单信息
        $orderInfo = $consumeOrderMdl->getOrderInfo(array('orderCode' => $orderCode), array('orderNbr'));
        $orderNbr = $orderInfo['orderNbr'];
        $userConsumeMdl = new UserConsumeModel();
        // 生成在线支付记录
        $ret = $userConsumeMdl->bankcardPay($orderCode, $userCouponCode, $platBonus, $shopBonus);
        if($ret['code'] == C('SUCCESS')) {
            $ret['orderNbr'] = $orderNbr;
        }
        return $ret;
    }

    /**
     * 产品订单的取消在线支付
     * @param string $consumeCode 订单编码
     * @return array $ret
     */
    public function pOCancelBankcardPay($consumeCode) {
        $userConsumeMdl = new UserConsumeModel();
        $ret = $userConsumeMdl->cancelBankcardPay($consumeCode);
        return $ret;
    }

    /**
     * 获取可以使用银行卡列表
     * @param string $userCode 用户编码
     * @return array $bankCardList
     */
    public function listAllBankCard($userCode) {
        $bankAccountMdl = new BankAccountModel();
        $bankCardList = $bankAccountMdl->getBankAccountList($userCode, $this->getPager(0));
        return $bankCardList;
    }

    /**
     * 获取工银快捷支付验证码
     * @param string $consumeCode 用户消费记录编码
     * @param string $bankAccountCode 用户关联银行账户编码
     * @param string $mobileNbr 手机号码
     * @return array
     */
    public function getIcbcPayValCode($consumeCode, $bankAccountCode, $mobileNbr) {
        $userConsumeMdl = new UserConsumeModel();
        $ret = $userConsumeMdl->getIcbcPayValCode($consumeCode, $bankAccountCode, $mobileNbr);
        return $ret;
    }

    /**
     * 在线支付确认支付（https协议传输）
     * @param string $consumeCode 用户消费记录编码
     * @param string $bankAccountCode 用户关联银行账户编码
     * @param int $valCode 验证码
     * @return string $ret
     */
   /* public function bankcardPayConfirm($consumeCode, $bankAccountCode, $valCode) {
        $userConsumeMdl = new UserConsumeModel();
        // 判断支付账单中使用的优惠券是否可用
        $isCouponCanBeUse = $userConsumeMdl->isConsumeCouponCanBeUse($consumeCode);
        if($isCouponCanBeUse) {
            $ret = $userConsumeMdl->bankcardPayConfirm($consumeCode, $bankAccountCode, $valCode);
        } else {
            $ret = array('code' => C('API_INTERNAL_EXCEPTION'));
        }
        return $ret;
    }*/
	
	public function bankcardPayConfirm($consumeCode) {
        $userConsumeMdl = new UserConsumeModel();
        // 判断支付账单中使用的优惠券是否可用
        $isCouponCanBeUse = $userConsumeMdl->isConsumeCouponCanBeUse($consumeCode);
        if($isCouponCanBeUse) {
            $ret = $userConsumeMdl->bankcardPayConfirm($consumeCode);
        } else {
            $ret = array('code' => C('API_INTERNAL_EXCEPTION'));
        }
        return $ret;
    }
	

    /**
     * 绑定银行卡并且获得支付交易的验证码
     * @param string $userCode 用户编码
     * @param string $accountName 账号姓名
     * @param string $idType 证件类型。0-身份证，1-护照，2-军官证，3-士兵证，4-港澳通行证，5-临时身份证，6-户口簿，7-其他，9-警官证，12-外国人居留证
     * @param string $idNbr 证件号
     * @param string $accountNbrPre6 银行卡卡号前6位
     * @param string $accountNbrLast4 银行卡卡号后4位
     * @param string $mobileNbr 手机号
     * @param string $consumeCode 消费记录编码
     * @return array
     */
    public function getPayValCodeQuickly($userCode, $accountName, $idType, $idNbr, $accountNbrPre6, $accountNbrLast4, $mobileNbr, $consumeCode) {
        $userConsumeMdl = new UserConsumeModel();
        $userConsumeInfo = $userConsumeMdl->getConsumeInfo(array('consumeCode' => $consumeCode), array('orderNbr', 'location'));
        $orderNbr = $userConsumeInfo['orderNbr'];
        // 添加银行卡
        $bankAccountMdl = new BankAccountModel();
        $addBankAccountRet = $bankAccountMdl->addBankAccount($userCode, $accountName, $idType, $idNbr, $accountNbrPre6, $accountNbrLast4, $mobileNbr, $orderNbr);
        if($addBankAccountRet['code'] == C('SUCCESS')) {
            //请求验证码
            $getIcbcPayValCodeRet = $this->getIcbcPayValCode($consumeCode, $addBankAccountRet['bankAccountCode'], $mobileNbr);
            if($getIcbcPayValCodeRet['code'] == C('SUCCESS')) {
                return array('code' => C('SUCCESS'), 'valCode' => $getIcbcPayValCodeRet['valCode'] ? $getIcbcPayValCodeRet['valCode'] : '', 'bankAccountCode' => $addBankAccountRet['bankAccountCode']);
            } else {
                return $getIcbcPayValCodeRet;
            }
        } else {
            return $addBankAccountRet;
        }
    }

    /**
     * 绑定银行卡并且获得支付交易的验证码(改)
     * @param string $userCode 用户编码
     * @param string $accountName 账号姓名
     * @param string $idType 证件类型。0-身份证，1-护照，2-军官证，3-士兵证，4-港澳通行证，5-临时身份证，6-户口簿，7-其他，9-警官证，12-外国人居留证
     * @param string $idNbr 证件号
     * @param string $bankCard 银行卡卡号
     * @param string $mobileNbr 手机号
     * @param string $consumeCode 消费记录编码
     * @return array
     */
    public function getPayValCodeQuicklyModify($userCode, $accountName, $idType, $idNbr, $bankCard, $mobileNbr, $consumeCode) {
        $userConsumeMdl = new UserConsumeModel();
        $userConsumeInfo = $userConsumeMdl->getConsumeInfo(array('consumeCode' => $consumeCode), array('orderNbr', 'location'));
        $orderNbr = $userConsumeInfo['orderNbr'];
        // 添加银行卡
        $bankAccountMdl = new BankAccountModel();
        $addBankAccountRet = $bankAccountMdl->addBankAccountModify($userCode, $accountName, $idType, $idNbr, $bankCard, $mobileNbr, $orderNbr);
        if($addBankAccountRet['code'] == C('SUCCESS')) {
            //请求验证码
            $getIcbcPayValCodeRet = $this->getIcbcPayValCode($consumeCode, $addBankAccountRet['bankAccountCode'], $mobileNbr);
            if($getIcbcPayValCodeRet['code'] == C('SUCCESS')) {
                return array('code' => C('SUCCESS'), 'valCode' => $getIcbcPayValCodeRet['valCode'] ? $getIcbcPayValCodeRet['valCode'] : '', 'bankAccountCode' => $addBankAccountRet['bankAccountCode']);
            } else {
                return $getIcbcPayValCodeRet;
            }
        } else {
            return $addBankAccountRet;
        }
    }

    /**
     * 绑定银行卡并且支付
     * @param string $consumeCode 支付编码
     * @param string $bankAccountCode 银行账户编码
     * @param string $valCode 验证码
     * @return array
     */
    public function signAndPay($consumeCode, $bankAccountCode, $valCode) {
        $userConsumeMdl = new UserConsumeModel();
        $userConsumeInfo = $userConsumeMdl->getConsumeInfo(array('consumeCode' => $consumeCode), array('orderNbr', 'location'));
        $orderNbr = $userConsumeInfo['orderNbr'];
        $shopMdl = new ShopModel();
        // 获得商家信息（工行入账的商户号）
        $shopInfo = $shopMdl->getShopInfo($userConsumeInfo['location'], array('icbcShopCode', 'hqIcbcShopNbr'));
        // 绑定银行卡
        $signBankAccountRet = $this->signBankAccount($orderNbr, $valCode, $shopInfo['sellerid']);
        if($signBankAccountRet['code'] == C('SUCCESS')) {
            // 在线确认支付
            $bankcardPayConfirmRet = $this->bankcardPayConfirm($consumeCode, $bankAccountCode, $valCode);
            return $bankcardPayConfirmRet;
        } else {
            return $signBankAccountRet;
        }
    }

    /**
     * POS机支付（https协议传输）
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param string $userCouponCode 用户优惠券编码
     * @param int $platBonus  平台红包金额
     * @param int $shopBonus  商家红包金额
     * @param int $price   支付金额
     * @return array $ret
     */
    public function posPay($userCode, $shopCode, $userCouponCode, $platBonus, $shopBonus, $price) {
        $userConsumeMdl = new UserConsumeModel();
        $ret = $userConsumeMdl->posPay($userCode, $shopCode, $userCouponCode, $platBonus, $shopBonus, $price);
        return $ret;
    }

    /**
     * 现金支付
     * @param string $userCode  用户编码
     * @param string $shopCode  商家编码
     * @param string $userCouponCode 用户优惠券编码
     * @param float $platBonus  平台红包金额，单位：元
     * @param float $shopBonus  商家红包金额，单位：元
     * @param float $price   支付金额，单位：元
     * @return array
     */
    public function cashPay($userCode, $shopCode, $userCouponCode, $platBonus, $shopBonus, $price) {
        $userConsumeMdl = new UserConsumeModel();
        $ret = $userConsumeMdl->cashPay($userCode, $shopCode, $userCouponCode, $platBonus, $shopBonus, $price);
        return $ret;
    }

    /**
     * 发送消息
     * @param $shopCode
     * @param $userCode
     * @param $message
     * @return string
     */
    public function sendMsg($shopCode, $userCode, $message){
        $communicationMdl = new CommunicationModel();
        $ret = $communicationMdl->sendMsg($shopCode, $userCode, '', $message, 1);
        $ret['datetime'] = date('Y.m-d H:i:s');
        return $ret;
    }

    /**
     * 阅读消息
     * @param string $userCode
     * @param $shopCode
     * @return string
     */
    public function readMsg($userCode, $shopCode){
        $communicationMdl = new CommunicationModel();
        $ret = $communicationMdl->readMsg($userCode, $shopCode, C('COMMUNICATION_APP.SHOP'));
        return $ret;
    }

    /**
     * 获得未读消息数量
     * @param $userCode
     * @param $shopCode
     * @return int
     */
    public function countUnreadMsg($userCode, $shopCode){
        $communicationMdl = new CommunicationModel();
        $ret = $communicationMdl->countUnreadMsg($userCode, $shopCode, C('COMMUNICATION_APP.SHOP'));
        return $ret;
    }

    /**
     * 消息记录
     * @param $userCode
     * @param $shopCode
     * @param $page
     * @return array
     */
    public function getMsg($userCode, $shopCode, $page){
        $communicationMdl = new CommunicationModel();
        $msgList = $communicationMdl->getMsg($userCode, $shopCode, $this->getPager($page));
        $totalCount = $communicationMdl->getMsgCount($userCode, $shopCode);
        return array(
            'totalCount'=>$totalCount,
            'msgList'=>$msgList,
            'page'=>$page,
            'count' => count($msgList)
        );
    }

    /**
     * 所有商家发的最新一条记录
     * @param $userCode
     * @param $page
     * @return array
     */
    public function getMsgGroup($userCode, $page){
        $communicationMdl = new CommunicationModel();
        $ret = $communicationMdl->getMsgGroup('userCode', $userCode, 'shopCode', $this->getPager($page));
        foreach($ret as &$v) {
            $v['unreadCount'] = $this->countUnreadMsg($v['userCode'], $v['shopCode']);
        }
        $totalCount = $communicationMdl->countMsgGroup('userCode', $userCode, 'shopCode');
        return array(
            'totalCount' => $totalCount,
            'ret' => $ret,
            'page' => $page,
            'count' => count($ret),
        );
    }

    /**
     * 获取已经开通的城市
     * @return array $cityList
     */
    public function listZhejiangCity(){
        $districtMdl = new DistrictModel();
        $cityList = $districtMdl->getCityList('浙江');
        return $cityList;
    }

    /**
     * 获取已经开通的城市
     * @return array $cityList
     */
    public function listOpenCity(){
        $districtMdl = new DistrictModel();
        $cityList = $districtMdl->listOpenCity();
        return $cityList;
    }

    /**
     * 某种优惠券被某个用户领取的数量
     * @param $batchCouponCode
     * @param $userCode
     * @return number
     */
    public function countMyReceivedCoupon($batchCouponCode,$userCode){
        $userCouponMdl = new UserCouponModel();
        return $userCouponMdl->countMyReceivedCoupon($batchCouponCode,$userCode);
    }

    /**
     * 获取用户拥有的各商家红包总额
     * @param $userCode
     * @return mixed
     */
    public function getMyBonus($userCode){
        $bsMdl = new BonusStatisticsModel();
        $ret = $bsMdl->getMyBonus($userCode);
        return $ret;
    }

    /**
     * 买单时获得用户拥有某商家的红包总额和平台红包总额
     * @param $userCode
     * @param $shopCode
     * @return array
     */
    public function userTotalBonusValue($userCode, $shopCode){
        $bsMdl = new BonusStatisticsModel();
        $platBonus = $bsMdl->userTotalBonusValue($userCode, C('HQ_CODE'));
        $shopBonus = $bsMdl->userTotalBonusValue($userCode, $shopCode);
        return array(
            'platBonus' => $platBonus ? (float)$platBonus : 0,
            'shopBonus' => $shopBonus ? (float)$shopBonus : 0
        );
    }

    /**
     * 获得最新的版本信息
     * @return array
     */
    public function getNewestClientAppVersion(){
        $calMdl = new ClientAppLogModel();
//        var_dump($calMdl->getNewestClientAppVersion());
        return $calMdl->getNewestClientAppVersion();
    }

    /**
     * 获取消费结果
     * @param $consumeCode
     * @return array
     */
    public function getPayResult($consumeCode){
        $ucMdl = new UserConsumeModel();
        $ret = $ucMdl->getConsumeInfo(array('consumeCode' => $consumeCode), array('UserConsume.status','UserConsume.userCouponCode'));
        $ret['userCouponCode'] = (string)$ret['userCouponCode'];
        return $ret;
    }

    /**
     * 实物券 or 体验券买单
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param string $userCouponCode 用户优惠券编码
     * @param string $appType 应用类型 0：商家端，1：顾客端，3：PC端
     * @return array
     */
    public function zeroPay($userCode, $shopCode, $userCouponCode, $appType){
        if(is_null($appType)){
            $appType = C('LOGIN_TYPE.USER');
        }
        $ucMdl = new UserConsumeModel();
        return $ucMdl->zeroPay($userCode, $shopCode, $userCouponCode, $appType);
    }


    /**
     * 获得用户的支付记录列表
     * @param string $userCode
     * @param int $isFinish  1 => 支付成功，0 => 支付失败和未支付
     * @param int $page
     * @return array
     */
    public function getUserOrderList($userCode,  $isFinish, $page){
        $userConsumeMdl = new UserConsumeModel();
        $condition['UserConsume.consumerCode'] = $userCode;
        if($isFinish == 1) { // 支付成功
            $condition['UserConsume.status'] = $condition['ConsumeOrder.status'] = \Consts::PAY_STATUS_PAYED;
        } else { // 支付失败和未支付
            $condition['UserConsume.status'] = $condition['ConsumeOrder.status'] = array('IN', array(\Consts::PAY_STATUS_UNPAYED, \Consts::PAY_STATUS_FAIL, \Consts::PAY_STATUS_PAYING));
        }
        $userConsumeList = $userConsumeMdl->listUserConsume($condition, $this->getPager($page));
        foreach($userConsumeList as &$v) {
            $v['orderNbr'] = substr($v['orderNbr'], -4);
        }
        $userConsumeCount = $userConsumeMdl->countUserConsume($condition);
        return array(
            'orderList' => $userConsumeList,
            'totalCount' => $userConsumeCount,
            'page' => $page,
            'count' => count($userConsumeList),
        );
    }

    /**
     * 修改订单状态
     * @param $consumeCode
     */
    public function gotoPay($consumeCode){
        $userConsumeMdl = new UserConsumeModel();
        $consumeInfo = $userConsumeMdl->getConsumeInfo(array('consumeCode' => $consumeCode), array('ConsumeOrder.orderCode'));
        $consumeOrderMdl = new ConsumeOrderModel();

        // 修改支付记录状态为未支付
        $ret1 = $userConsumeMdl->updateConsumeStatus(array('consumeCode' => $consumeCode), array('status' => C('UC_STATUS.UNPAYED')));
        // 修改订单支付状态为未支付
        $ret2 = $consumeOrderMdl->updateConsumeOrder(array('orderCode' => $consumeInfo['orderCode']), array('status' => C('ORDER_STATUS.UNPAYED')));
        if($ret1 && $ret2) {
            M()->commit();
            $ret['code'] = C('SUCCESS');
            $ret['consumeCode'] = $consumeCode;
            return $ret;
        } else {
            M()->rollback();
            $ret['code'] = C('API_INTERNAL_EXCEPTION');
            return $ret;
        }
    }

    /**
     * 订单详情
     * @param string $consumeCode 消费编码
     * @return array
     */
    public function getConsumeInfo($consumeCode) {
        $userConsumeMdl = new UserConsumeModel();
        $consumeInfo = $userConsumeMdl->getConsumeInfo(array('consumeCode' => $consumeCode), array('realPay', 'deduction', 'couponUsed', 'shopBonus', 'bankCardDeduction', 'platBonus', 'usedUserCouponCode', 'isCard', 'consumerCode', 'location', 'Shop.shopName','ConsumeOrder.orderNbr','ConsumeOrder.orderAmount','ConsumeOrder.orderTime', 'Shop.shopCode', 'firstDeduction', 'couponDeduction', 'cardDeduction', 'UserConsume.status', 'payType'));
        $userConsumeInfo = array();
        if($consumeInfo) {
            $userConsumeInfo = array(
                'shopCode' => $consumeInfo['shopCode'],
                'shopName' => $consumeInfo['shopName'] ? $consumeInfo['shopName'] : '',
                'orderNbr' => substr($consumeInfo['orderNbr'], -10, 6) . ' ' . substr($consumeInfo['orderNbr'], -4),
                'consumeTime' => $consumeInfo['orderTime'],
                'orderAmount' => $consumeInfo['orderAmount'],
                'realPay' => $consumeInfo['realPay'],
                'couponUsed' => $consumeInfo['couponUsed'],
                'couponDeduction' => $consumeInfo['couponDeduction'],
                'cardDeduction' => $consumeInfo['cardDeduction'],
                'bonusDeduction' => $consumeInfo['shopBonus'] + $consumeInfo['platBonus'],
                'shopBonusDeduction' => $consumeInfo['shopBonus'],
                'platBonusDeduction' => $consumeInfo['platBonus'],
                'bankCardDeduction' => $consumeInfo['bankCardDeduction'],
                'firstDeduction' => $consumeInfo['firstDeduction'],
                'deduction' => $consumeInfo['deduction'],
                'status' => $consumeInfo['status'],
                'couponType' => '',
                'insteadPrice' => '',
                'availablePrice' => '',
                'discountPercent' => '',
                'function' => '',
            );
            $orderAmount = $consumeInfo['orderAmount'];
            $couponInsteadPrice = 0;
            if($consumeInfo['couponUsed'] != C('NO')) {
                $userCouponMdl = new UserCouponModel();
                // 获得优惠券信息
                if(!empty($consumeInfo['usedUserCouponCode'])) {
                    $arrUsedUserCouponCode = explode('|', $consumeInfo['usedUserCouponCode']);
                    $coupon = $userCouponMdl->getCouponInfo($arrUsedUserCouponCode[0], array('couponType', 'insteadPrice', 'availablePrice', 'discountPercent'));
                } else {
                    $coupon = $userCouponMdl->getUserCouponInfoB(
                        array('UserCoupon.consumeCode' => $consumeCode),
                        array('userCouponCode', 'couponType', 'discountPercent', 'insteadPrice', 'availablePrice', 'function', 'batchNbr', 'payPrice', 'UserCoupon.batchCouponCode')
                    );
                }
                // 关于大米活动，若使用的券是 兑换大米的实物券，则修改平台使用的红包为50元
                if(in_array($coupon['batchCouponCode'], array('5a0e2630-0550-dfee-eb1e-4de423f118b8', '8cc4a256-1f2c-2e73-5023-bf5edd74e273', '33873970-e5ab-449d-91b5-938e5212b067', '713dc724-6603-a1d4-9006-8ade0aa14b3f'))) {
                    $userConsumeInfo['platBonusDeduction'] = 5000;
                }

                $userConsumeInfo['couponType'] = $coupon['couponType'] ? $coupon['couponType'] : '';
                $userConsumeInfo['insteadPrice'] = $coupon['insteadPrice'] ? $coupon['insteadPrice'] : 0;
                $userConsumeInfo['availablePrice'] = $coupon['availablePrice'] ? $coupon['availablePrice'] : 0;
                $userConsumeInfo['discountPercent'] = $coupon['discountPercent'] ? $coupon['discountPercent']  / C('DISCOUNT_RATIO'): 10;
                $userConsumeInfo['function'] = $coupon['function'] ? $coupon['function'] : '';
                // 抵扣券或折扣券
                if(in_array($coupon['couponType'], array(C('COUPON_TYPE.DISCOUNT'), C('COUPON_TYPE.REDUCTION'), C('COUPON_TYPE.NEW_CLIENT_REDUCTION')))) {
                    // 优惠券抵扣金额
                    if($coupon['discountPercent'] > 0) {
                        $couponInsteadPrice = $orderAmount - $orderAmount * $coupon['discountPercent'] / C('RATIO');
                    } else {
                        $couponInsteadPrice = $coupon['insteadPrice'];
                    }
                    if(!empty($consumeInfo['couponDeduction'])) {
                        $couponInsteadPrice = $consumeInfo['couponDeduction'];
                    }
                } else {
                    //其他 如实物券或体验券或 N 元购(需额外数据再加)
                    $couponInsteadPrice = 0;
                }
            }
            // 优惠券抵扣金额
            $userConsumeInfo['couponDeduction'] = $couponInsteadPrice;
            $newPrice = $orderAmount - $couponInsteadPrice;

            $cardInsteadPrice = 0;
            if($consumeInfo['isCard'] == C('YES')) {
                $userCardMdl = new UserCardModel();
                $userCardInfo = $userCardMdl->getBestUserCard($consumeInfo['consumerCode'], $consumeInfo['location']);
                // $userCardInfo['discount']为百分数，计算时注意
                if($userCardInfo['discount'] > 0) {
                    // 会员卡抵扣金额
                    $cardInsteadPrice = $newPrice - $newPrice * $userCardInfo['discount'] / C('RATIO');
                }
                if(!empty($consumeInfo['cardDeduction'])) {
                    $cardInsteadPrice = $consumeInfo['cardDeduction'];
                }
            }
            $userConsumeInfo['cardDeduction'] = $cardInsteadPrice;

            $temp = array('orderAmount', 'realPay', 'couponDeduction', 'cardDeduction', 'bonusDeduction', 'shopBonusDeduction', 'platBonusDeduction', 'bankCardDeduction', 'deduction');
            foreach($temp as $v) {
                $userConsumeInfo[$v] = number_format($userConsumeInfo[$v] / C('RATIO'), 2, '.', '');
            }
            $temp = array('insteadPrice', 'availablePrice', 'firstDeduction');
            foreach($temp as $v) {
                $userConsumeInfo[$v] = $userConsumeInfo[$v] / C('RATIO');
            }
        }
        return $userConsumeInfo;
    }


    /**
     * 获得用户各类型信息的未读数量
     * @param $userCode
     * @return array
     */
    public function countAllTypeMsg($userCode){
        $userMessageMdl = new UserMessageModel();
        $communicationMdl = new CommunicationModel();
        $result = array(
            'shop'=>$userMessageMdl->countMessage($userCode, C('MESSAGE_TYPE.SHOP'), 0),
            'card'=>$userMessageMdl->countMessage($userCode, C('MESSAGE_TYPE.CARD'), 0),
            'coupon'=>$userMessageMdl->countMessage($userCode, C('MESSAGE_TYPE.COUPON'), 0),
            'communication'=>$communicationMdl->countUnreadMsg($userCode, '', C('COMMUNICATION_APP.SHOP')),
            'feedback'=>$communicationMdl->countUnreadMsg($userCode, C('HQ_CODE'), C('COMMUNICATION_APP.SHOP')),
        );
        return $result;
    }

    /**
     * 设置用户的某种类型的消息为已读
     * @param string $userCode 用户编码
     * @param int $type 消息类型。0-商家消息；1-会员卡消息；2-优惠券消息；
     * @return array
     */
    public function readMessage($userCode, $type){
        $userMsgMdl = new UserMessageModel();
        $condition = array('receiverCode' => $userCode, 'type' => $type, 'readingStatus' => C('NO'));
        $ret = $userMsgMdl->readMsg($condition);
        return $ret;
    }

    /**
     * 用户扫描商家二维码
     * @param $userCode
     * @param $shopCode
     * @return array $ret
     */
    public function scanCode($userCode, $shopCode) {
        $UserEnterShopInfoRecordMdl = new UserEnterShopInfoRecordModel();
        $ret = $UserEnterShopInfoRecordMdl->addRecord($shopCode, $userCode, $actionType = 1);
        if($ret == true){
            $code = C('SUCCESS');
        }else{
            $code = C('API_INTERNAL_EXCEPTION');
        }
        return array(
            'code' => $code
        );
    }

    /**
     * 获得用户距离上次扫描商家二维码是否超过90分钟
     * @param $userCode
     * @param $shopCode
     * @return array $ret
     */
    public function getUserShopRecord($userCode, $shopCode) {
        $limitTime = 30; // 单位：秒
        $UserEnterShopInfoRecordMdl = new UserEnterShopInfoRecordModel();
        $userEnterShopInfoRecordInfo = $UserEnterShopInfoRecordMdl->getRecord($userCode, $shopCode, $actionType = 1);
        if(!empty($userEnterShopInfoRecordInfo)){
            $timeDiff = time() - strtotime($userEnterShopInfoRecordInfo['enterTime']);
            if($timeDiff >= $limitTime){
                $code = C('YES');
            }else {
                $code = C('NO');
            }
        }else {
            $code = C('YES');
        }
        return array(
            'code' => $code
        );
    }

    /**
     * 返回错误信息
     */
    public function errorInfo() {
        $array = array(
            array('value' => 1, 'info' => '商家店名'),
            array('value' => 2, 'info' => '商家地址'),
            array('value' => 3, 'info' => '地址经纬度'),
            array('value' => 4, 'info' => '商家联系方式'),
            array('value' => 5, 'info' => '商家图片'),
            array('value' => 6, 'info' => '产品图片'),
        );
        return $array;
    }

    /**
     * 用户给平台提供商家错误信息
     * @param string $userCode  用户编码
     * @param string $errorInfo 错误信息 1-商家店名，2-商家地址，3-地址经纬度，4-商家联系方式，5-商家图片，6-商品/服务图片
     * @param string $errorImg 错误图片url
     * @param string $message 错误内容
     * @param string $toShopCode 被反馈的商户编码
     * @return array
     */
    public function addErrorInformation($userCode, $errorInfo, $errorImg, $message, $toShopCode) {
        $communicationMdl = new CommunicationModel();
        $result = $communicationMdl->addErrorInfo($userCode, C('HQ_CODE'), '', $message, 1, $errorInfo, $errorImg, 20, $toShopCode);
        return $result;
    }

    /**
     * 获取首页各模块
     * @param string $city 用户所在城市
     * @return array
     */
    public function getHomeInfo($city){
        // 根据城市名获得城市 ID
        $districtMdl = new DistrictModel();
        $cityInfo = $districtMdl->getCityInfo(array('name' => array('like', "%$city%")), array('id'));
        // 获取对应城市的 C 端首页显示模块（选项卡模块除外）
        $cimrMdl = new CityIndexModuleRelModel();
        $indexModuleList = $cimrMdl->listCityIndexModuleRel(array('indexModuleId', 'moduleValue', 'template'), array('hide' => C('NO'), 'cityId' => $cityInfo['id'], 'moduleValue' => array('neq', \IndexModule::HOME_TAB)));
        foreach($indexModuleList as $k => $v){
            $indexModuleList[$k]['modulePosition'] = $k + 1; // 模块位置
            if(empty($v['template'])){
                $indexModuleList[$k]['template'] = ''; // 模块显示模板
            }
            if($v['moduleValue'] == \IndexModule::SHOP_TYPE){ // 2 代表分类（来自 indexModule 表）
                $cityShopTypeMdl = new CityShopTypeModel();
                $cityShopTypeList = $cityShopTypeMdl->listCityShopType($cityInfo['id']); // 获取对应城市的分类模块的子模块
                foreach($cityShopTypeList as $ck => $cv){
                    $indexModuleList[$k]['subList'][] = array(
                        'imgUrl' => $cv['shopTypeImg'],
                        'title' => $cv['typeZh'],
                        'linkType' => 1,
                        'content' => $cv['typeValue'],
                        'subModulePosition' => $ck + 1     // 子模块位置
                    );
                }
            }elseif($v['moduleValue'] == \IndexModule::SHOP_TRADING_AREA){   // 3 代表商圈（来自 indexModule 表）
                $subModuleMdl = new SubModuleModel();
                $subModuleList = $subModuleMdl->getSubModuleList(array('SubModule.id','SubModule.title', 'SubModule.imgUrl', 'imgSize', 'imgRate', 'screenRate'), array('hide' => C('NO'), 'parentModuleId' => $v['indexModuleId'], 'SubModule.cityId' => $cityInfo['id'])); // 获取对应城市的商圈模块的子模块
                foreach($subModuleList as $sk => $sv){
                    $sv['subModulePosition'] = $sk + 1;
                    $shopTradingAreaRelMdl = new ShopTradingAreaRelModel();
                    $relList = $shopTradingAreaRelMdl->listShopTradingAreaRel('', array('subModuleId' => $sv['id']));
                    if(count($relList) == 1){
                        $sv['linkType'] = 0;
                        $sv['content'] = 'Browser/getShopInfo?shopCode='.$relList[0]['shopCode'];
                        $sv['shopCode'] = $relList[0]['shopCode'];
                    }else{
                        $sv['linkType'] = 1;
                        $sv['content'] = $sv['id'];
                    }
                    unset($sv['id']);
                    $size = explode('|', $sv['imgSize']);
                    $sv['imgWidth'] = $size[0] ? $size[0] : 80;
                    $sv['imgHeight'] = $size[1] ? $size[1] : 80;
                    unset($sv['imgSize']);
                    $indexModuleList[$k]['subList'][] = $sv;
                }
            }elseif($v['moduleValue'] == \IndexModule::BRAND){ // 4代表品牌（来自 indexModule 表）
                $cityBrandRelMdl = new CityBrandRelModel();
                $cityBrandList = $cityBrandRelMdl->listRel(array('CityBrandRel.*', 'brandName', 'brandLogo'), array('cityId' => $cityInfo['id'])); // 获取对应城市的品牌模块的子模块
                foreach($cityBrandList as $ck => $cv){
                    $cv['subModulePosition'] = $ck + 1;
                    $cv['title'] = $cv['brandName'];
                    $cv['showImg'] = $cv['imgUrl'];
                    $cv['imgUrl'] = $cv['brandLogo'];
                    if($cv['linkType'] == \Consts::BRAND_LINK_TYPE_H5) { // 跳转类型是H5页面
                        $cv['content'] .= $cv['cityBrandRelId'];
                    } else { // 其他跳转类型
                        $shopBrandRelMdl = new ShopBrandRelModel();
                        $relList = $shopBrandRelMdl->arrCBR('shopCode', array('brandId' => $cv['brandId']));
                        if(count($relList) == 1){
                            $cv['linkType'] = 0;
                            $cv['content'] = 'Browser/getShopInfo?shopCode='.$relList[0];
                            $cv['shopCode'] = $relList[0];
                        }else{
                            $cv['linkType'] = 1;
                            $cv['content'] = $cv['brandId'];
                        }
                    }

                    unset($cv['brandId']);
                    unset($cv['cityBrandRelId']);
                    unset($cv['brandName']);
                    unset($cv['brandLogo']);
                    unset($cv['exButtonList']);
                    unset($cv['bgUrl']);
                    unset($cv['cityId']);
                    $indexModuleList[$k]['subList'][] = $cv;
                }
            }elseif($v['moduleValue'] == \IndexModule::SHOP_LIST){ // 6代表商户列表（来自 indexModule 表）
                $indexModuleList[$k]['moduleTitle'] = '推荐商家';
                $indexModuleList[$k]['moduleTitleColor'] = '#000000';
            }else{
                $subModuleMdl = new SubModuleModel();
                if($v['moduleValue'] == \IndexModule::SCROLLING){ // 1代表滚屏（来自 indexModule 表）
                    $subModuleList = $subModuleMdl->getScrollList(array('activityImg', 'SubModule.activityCode', 'Activity.webUrl', 'Activity.imgRate'), array('hide' => C('NO'), 'parentModuleId' => $v['indexModuleId'], 'SubModule.cityId' => $cityInfo['id']));
                    foreach($subModuleList as $ak => $av){
                        $indexModuleList[$k]['subList'][] = array(
                            'imgUrl' => $av['activityImg'],
                            'linkType' => 0,
                            'imgRate' => $av['imgRate'],
                            'content' => $av['webUrl'].$av['activityCode'],
                            'subModulePosition' => $ak + 1
                        );
                    }
                }else{
                    $subModuleList = $subModuleMdl->getSubModuleList(array('SubModule.id', 'title', 'titleColor', 'subTitle', 'subTitleColor', 'linkType', 'content', 'bgColor', 'imgUrl', 'imgPosition', 'imgSize', 'imgRate', 'screenRate'), array('hide' => C('NO'), 'parentModuleId' => $v['indexModuleId'], 'SubModule.cityId' => $cityInfo['id']));
                    foreach($subModuleList as $sk => $sv){
                        $sv['subModulePosition'] = $sk + 1;
                        $size = explode('|', $sv['imgSize']);
                        $sv['imgWidth'] = $size[0] ? $size[0] : 80;
                        $sv['imgHeight'] = $size[1] ? $size[1] : 80;
                        if($sv['linkType'] == 0){
                            $sv['content'] .= 'actId='.$sv['id'];
                        }
                        $sv['content'] = htmlspecialchars_decode($sv['content']);
                        unset($sv['id']);
                        unset($sv['imgSize']);
                        $indexModuleList[$k]['subList'][] = $sv;
                    }
                }
            }
            unset($indexModuleList[$k]['indexModuleId']);
        }
        return $indexModuleList;
    }


    /**
     * C端首页下方的商家列表
     * @param string $userCode 用户编码
     * @param float $longitude 用户所在经度
     * @param float $latitude 用户所在纬度
     * @param int $page 页码
     * @param string $city 用户所在城市
     * @return array 详细字段
     * array(
     *  'totalCount' => '根据搜索条件查询出的所有记录数',
     *  'shopList' => '根据页码查询出的商家列表',
     *  'page' => '查询页',
     *  'nextPage' => '查询页的下一页',
     *  'count' => '获取查询页的记录数',
     * )
     */
    public function getHomeShopList($userCode, $longitude, $latitude, $page, $city){
        $shopMdl = new ShopModel();

        // 查询信息
        $condition = array(
            'longitude' => $longitude, // 用户所在经度
            'latitude' => $latitude, // 用户所在纬度
            'userCode' => $userCode, // 用户编码
            'shopStatus' => \Consts::SHOP_ENTER_STATUS_YES, // 商户入驻状态为已经入驻
        );
        if($city == '义乌市') {
            $condition['district'] = $city;
        } else {
            $condition['city'] = $city;
        }

        // 联合表的信息
        $joinTableArr = array(
            array(
                'joinTable' => '(select createTime,shopCode from BatchCoupon where ((validityPeriod = 0) OR (expireTime >= NOW() AND validityPeriod = -1)) AND ((totalVolume = -1) OR (totalVolume > 0 AND remaining > 0)) AND isAvailable = 1 AND isSend = 0 AND couponType in (1,3,4,5,6,7,8) AND startTakingTime <= NOW() AND endTakingTime >= NOW() group by shopCode) r',
                'joinCondition' => 'r.shopCode = Shop.shopCode',
                'joinType' => 'LEFT')
        );
        $shopList =$shopMdl->searchShop($condition, '', $this->getPager($page), $joinTableArr, 'r.createTime desc, Shop.isAcceptBankCard desc, Shop.onlinePaymentDiscount asc, Shop.sortNbr desc, Shop.shopStatus desc, distance asc, Shop.createDate asc, Shop.creditPoint desc');
        $shopCount = $shopMdl->countSearchShop($condition, $joinTableArr);
        return array(
            'totalCount' => $shopCount,
            'shopList' => $shopList,
            'page' => $page,
            'nextPage' => UtilsModel::getNextPage($shopCount, $page),
            'count' => count($shopList),
        );
    }

    /**
     * 获取批次优惠券详情，不登陆的时候使用，不需要token验证
     * @param $batchCouponCode
     * @param $longitude
     * @param $latitude
     * @return array
     */
    public function getBatchCouponInfo($batchCouponCode, $longitude, $latitude){
        $batchCouponMdl = new BatchCouponModel();
        $shopMdl = new ShopModel();
        $shopDecorationMdl = new ShopDecorationModel();

        //批次优惠券详情
        $batchCouponInfo = $batchCouponMdl->getCouponInfo($batchCouponCode, array('BatchCoupon.batchCouponCode', 'BatchCoupon.couponType', 'BatchCoupon.batchNbr', 'BatchCoupon.remark', 'BatchCoupon.startUsingTime', 'BatchCoupon.expireTime', 'BatchCoupon.discountPercent', 'BatchCoupon.insteadPrice', 'BatchCoupon.availablePrice', 'BatchCoupon.dayStartUsingTime', 'BatchCoupon.dayEndUsingTime', 'BatchCoupon.function', 'BatchCoupon.payPrice', 'BatchCoupon.shopCode'));
        $batchCouponInfo['dayStartUsingTime'] = substr($batchCouponInfo['dayStartUsingTime'], 0, 5);
        $batchCouponInfo['dayEndUsingTime'] = substr($batchCouponInfo['dayEndUsingTime'], 0, 5);

        //商家详情
        $shopInfo = $shopMdl->getShopInfo($batchCouponInfo['shopCode'], array('shopCode', 'shopName', 'businessHours', 'type', 'logoUrl', 'popularity', 'repeatCustomers', 'city'));
        $shopTypeRelMdl = new ShopTypeRelModel();
        $shopTypeArr = $shopTypeRelMdl->getShopType($shopInfo['shopCode']);
        $shopTypeArr = array_unique(array_merge($shopTypeArr, array(\Consts::SHOP_TYPE_ICBC, \Consts::SHOP_TYPE_PLAT)));

        if($shopInfo['businessHours']){
            $businessHours = '';
            foreach($shopInfo['businessHours'] as $sv){
                if(count($shopInfo['businessHours']) == 0 && $sv['open'] == '00:00' && $sv['close'] == '23:59'){
                    $businessHours = '全天营业';
                }else{
                    if($businessHours){
                        $businessHours .= ' / '.$sv['open'].' - '.$sv['close'];
                    }else{
                        $businessHours = $sv['open'].' - '.$sv['close'];
                    }
                }
            }
            $shopInfo['businessHoursString'] = $businessHours;
        }else{
            $shopInfo['businessHoursString'] = '未知';
        }
        unset($shopInfo['businessHours']);
        // 获得商家装修
        $shopInfo['shopImg'] = $shopDecorationMdl->getShopDecoration($batchCouponInfo['shopCode']);

        //推荐显示商家
        $recomShop = array();
        if($longitude && $latitude){
            $shopCondition = array(
                'ShopType.typeValue' => array('NOTIN', $shopTypeArr),
                'Shop.isCompany' => C('NO'),
                'Shop.city' => array('like', '%'.$shopInfo['city'].'%'),
                'Shop.status' => C('SHOP_STATUS.ACTIVE'),
            );
            $joinTableArr = array(
                array('joinTable' => 'ShopTypeRel', 'joinCondition' => 'ShopTypeRel.shopCode = Shop.shopCode', 'joinType' => 'inner'),
                array('joinTable' => 'ShopType', 'joinCondition' => 'ShopType.shopTypeId = ShopTypeRel.typeId', 'joinType' => 'inner'),
            );
            $recomShop = $shopMdl->getShopList($shopCondition, array('Shop.shopCode', 'Shop.shopName', 'ShopType.typeValue' => 'type', 'Shop.logoUrl', 'Shop.popularity', 'Shop.repeatCustomers', 'sqrt(power(Shop.longitude-'.$longitude.',2) + power(Shop.latitude-'.$latitude.',2))' => 'distance'), $joinTableArr, $order = 'distance asc', $limit = 5, $page = 0);
        }

        return array(
            'batchCouponInfo' => $batchCouponInfo,
            'shopInfo' => $shopInfo,
            'recomShop' => $recomShop,
        );
    }

    /**
     * 获取批次优惠券详情，登陆的时候使用，需要token验证
     * @param $batchCouponCode
     * @param $userCode
     * @param $longitude
     * @param $latitude
     * @return array
     */
    public function getBatchCouponInfoHasUser($batchCouponCode, $userCode, $longitude, $latitude){
        $batchCouponMdl = new BatchCouponModel();
        $shopMdl = new ShopModel();
        $shopDecorationMdl = new ShopDecorationModel();
        $userCouponMdl = new UserCouponModel();
        $orderCouponMdl = new OrderCouponModel();

        //批次优惠券详情
        $batchCouponInfo = $batchCouponMdl->getCouponInfo($batchCouponCode, array('BatchCoupon.batchCouponCode', 'BatchCoupon.couponType', 'BatchCoupon.batchNbr', 'BatchCoupon.remark', 'BatchCoupon.startUsingTime', 'BatchCoupon.expireTime', 'BatchCoupon.discountPercent', 'BatchCoupon.insteadPrice', 'BatchCoupon.availablePrice', 'BatchCoupon.dayStartUsingTime', 'BatchCoupon.dayEndUsingTime', 'BatchCoupon.function', 'BatchCoupon.payPrice', 'BatchCoupon.shopCode'));
        $batchCouponInfo['dayStartUsingTime'] = substr($batchCouponInfo['dayStartUsingTime'], 0, 5);
        $batchCouponInfo['dayEndUsingTime'] = substr($batchCouponInfo['dayEndUsingTime'], 0, 5);

        //商家详情
        $shopInfo = $shopMdl->getShopInfo($batchCouponInfo['shopCode'], array('shopCode', 'shopName', 'type', 'businessHours', 'logoUrl', 'popularity', 'repeatCustomers', 'city'));
        $shopTypeRelMdl = new ShopTypeRelModel();
        $shopTypeArr = $shopTypeRelMdl->getShopType($shopInfo['shopCode']);
        $shopTypeArr = array_unique(array_merge($shopTypeArr, array(\Consts::SHOP_TYPE_ICBC, \Consts::SHOP_TYPE_PLAT)));

        if($shopInfo['businessHours']){
            $businessHours = '';
            foreach($shopInfo['businessHours'] as $sv){
                if(count($shopInfo['businessHours']) == 0 && $sv['open'] == '00:00' && $sv['close'] == '23:59'){
                    $businessHours = '全天营业';
                }else{
                    if($businessHours){
                        $businessHours .= ' / '.$sv['open'].' - '.$sv['close'];
                    }else{
                        $businessHours = $sv['open'].' - '.$sv['close'];
                    }
                }
            }
            $shopInfo['businessHoursString'] = $businessHours;
        }else{
            $shopInfo['businessHoursString'] = '未知';
        }
        unset($shopInfo['businessHours']);
        // 获得商家装修
        $shopInfo['shopImg'] = $shopDecorationMdl->getShopDecoration($batchCouponInfo['shopCode']);

        //用户领取该批次的优惠券明细
        if($batchCouponInfo['payPrice'] > 0){
            $couponCondition = array(
                'OrderCoupon.batchCouponCode' => $batchCouponCode,
                'OrderCoupon.userCode' => $userCode,
                'OrderCoupon.status' => array('NOTIN', array(\Consts::ORDER_COUPON_STATUS_UNPAY_NOUSE))
            );
            $joinTableArr = array(
                array(
                    'joinTable' => 'BatchCoupon',
                    'joinCondition' => 'BatchCoupon.batchCouponCode = OrderCoupon.batchCouponCode',
                    'joinType' => 'inner'
                ),
                array(
                    'joinTable' => 'UserCoupon',
                    'joinCondition' => 'UserCoupon.orderCouponCode = OrderCoupon.orderCouponCode',
                    'joinType' => 'inner'
                )
            );
            $userCouponList = $orderCouponMdl->listOrderCoupon($couponCondition, array('OrderCoupon.orderCouponCode', 'OrderCoupon.couponCode' => 'userCouponNbr', 'BatchCoupon.couponType', 'orderCode', 'BatchCoupon.insteadPrice', 'BatchCoupon.payPrice', 'UserCoupon.userCouponCode', 'OrderCoupon.status' => 'orderCouponStatus'), $joinTableArr, 'OrderCoupon.createTime desc', 0, 0);
        }else{
            $couponCondition = array(
                'UserCoupon.batchCouponCode' => $batchCouponCode,
                'UserCoupon.userCode' => $userCode,
                'UserCoupon.status' => array('IN', array(\Consts::USER_COUPON_STATUS_ACTIVE, \Consts::USER_COUPON_STATUS_TOBE_ACTIVE))
            );
            $userCouponList = $userCouponMdl->listUserCoupon($couponCondition, $this->getPager(0), array('UserCoupon.userCouponCode', 'UserCoupon.userCouponNbr', 'BatchCoupon.couponType', 'BatchCoupon.insteadPrice', 'BatchCoupon.payPrice'));
        }

        //推荐显示商家
        $recomShop = array();
        if($longitude && $latitude){
            $shopCondition = array(
                'ShopType.typeValue' => array('NOTIN', $shopTypeArr),
                'Shop.isCompany' => C('NO'),
                'Shop.city' => array('like', '%'.$shopInfo['city'].'%'),
                'Shop.status' => C('SHOP_STATUS.ACTIVE'),
            );
            $joinTableArr = array(
                array('joinTable' => 'ShopTypeRel', 'joinCondition' => 'ShopTypeRel.shopCode = Shop.shopCode', 'joinType' => 'inner'),
                array('joinTable' => 'ShopType', 'joinCondition' => 'ShopType.shopTypeId = ShopTypeRel.typeId', 'joinType' => 'inner'),
            );
            $recomShop = $shopMdl->getShopList($shopCondition, array('Shop.shopCode', 'Shop.shopName', 'ShopType.typeValue' => 'type', 'Shop.logoUrl', 'Shop.popularity', 'Shop.repeatCustomers', 'sqrt(power(Shop.longitude-'.$longitude.',2) + power(Shop.latitude-'.$latitude.',2))' => 'distance'), $joinTableArr, $order = 'distance desc', $limit = 5, $page = 0);
        }

        return array(
            'batchCouponInfo' => $batchCouponInfo,
            'shopInfo' => $shopInfo,
            'userCouponList' => $userCouponList,
            'recomShop' => $recomShop,
        );
    }


    /**
     * 买券时获取一系列信息
     * @param $batchCouponCode 批次优惠券编码
     * @param $userCode 用户编码
     */
    public function getInfoWhenCouponPay($batchCouponCode, $userCode){
        $batchCouponMdl = new BatchCouponModel();
        // 优惠券信息
        $result['batchCouponInfo'] = $batchCouponMdl->getCouponInfo($batchCouponCode, array('BatchCoupon.*'));
        $shopMdl = new ShopModel();
        $shopInfo = $shopMdl->getOneShopInfo(array('shopCode' => $result['batchCouponInfo']['shopCode']), array('shopName', 'isAcceptBankCard'));
        $result['batchCouponInfo']['shopName'] = $shopInfo['shopName'];

        //红包信息
        $result['bonus'] = array(
            'canUseShopBonus' => C('YES'),
            'shopBonus' => C('NO'),
            'canUsePlatBonus' => C('YES'),
            'platBonus' => C('NO'),
        );
        $bsMdl = new BonusStatisticsModel();
        $platBonusRet = $bsMdl->getUserBonusStatistics($userCode, C('HQ_CODE'));
        if($platBonusRet && $platBonusRet['totalValue'] > C('NO')) {
            $result['bonus']['platBonus'] = number_format($platBonusRet['totalValue'] / C('RATIO'), 2, '.', '');
        } else {
            $result['bonus']['canUsePlatBonus'] = C('NO');
        }
        $shopBonusRet = $bsMdl->getUserBonusStatistics($userCode, $result['batchCouponInfo']['shopCode']);
        if($shopBonusRet && $shopBonusRet['totalValue'] > C('NO')) {
            $result['bonus']['shopBonus'] = number_format($shopBonusRet['totalValue'] / C('RATIO'), 2, '.', '');
        } else {
            $result['bonus']['canUseShopBonus'] = C('NO');
        }

        //最小支付金额
        $systemParamMdl = new SystemParamModel();
        $paramInfo = $systemParamMdl->getParamValue('minRealPay');
        $result['minRealPay'] = $paramInfo['value'] / C('RATIO');

        //是否受理银行卡
        $result['isAcceptBankCard'] = $shopInfo['isAcceptBankCard'];
        return $result;
    }

    /**
     * 记录用户每次打开App的位置
     * @param $city 定位城市
     * @param $deviceNbr 加密的设备号
     * @param $mobileNbr 用户手机号
     * @return array
     */
    public function recordUserAddress($city, $deviceNbr, $mobileNbr){
        if(empty($city)){return array('code' => C('API_INTERNAL_EXCEPTION'));}
        $userAddressLogMdl = new UserAddressLogModel();
        return $userAddressLogMdl->editUserAddressLog(
            array(
                'city' => $city,
                'deviceNbr' => $deviceNbr,
                'mobileNbr' => $mobileNbr
            )
        );
    }

    /**
     * 获取首页选项卡列表
     * @return array
     */
    public function getHomeTabList(){
        $city = '湖州';
        // 根据城市名获得城市 ID
        $districtMdl = new DistrictModel();
        $cityInfo = $districtMdl->getCityInfo(array('name' => array('like', "%$city%")), array('id'));
        // 获取对应城市的 C 端选项卡模块
        $cimrMdl = new CityIndexModuleRelModel();
        $indexModuleList = $cimrMdl->listCityIndexModuleRel(array('indexModuleId', 'moduleValue'), array('hide' => C('NO'), 'cityId' => $cityInfo['id'], 'moduleValue' => array('eq', \IndexModule::HOME_TAB)));
        if(empty($indexModuleList)){
            return array();
        }
        $subModuleMdl = new SubModuleModel();
        $subModuleList = $subModuleMdl->getSubModuleList(array('title', 'titleColor' => 'notFocusedColor', 'subTitleColor' => 'focusedColor', 'imgRate', 'screenRate', 'notFocusedUrl', 'focusedUrl', 'bgColor'), array('hide' => C('NO'), 'parentModuleId' => $indexModuleList[0]['indexModuleId'], 'SubModule.cityId' => $cityInfo['id']));
        if($subModuleList){
            foreach($subModuleList as $sk => $sv){
                $subModuleList[$sk]['subModulePosition'] = $sk + 1;
            }
        }
        return $subModuleList;
    }

    /**
     * 用户对商家操作
     * @param string $shopCode 商家编码
     * @param string $userCode 用户编码
     * @param int $actionType 邀请商家入驻(2)、提醒商家上商品(3)
     * @return array(
                   'code'  => 错误代码 50500:用户编码为空（请登录）; 78001:重复操作; 20000:操作失败; 50000:成功;
                   'count' => 提醒数量 (错误代码是 78001 和 50000 的时候才会返回)
     *         )
     */
    public function remindToShop($shopCode, $userCode, $actionType) {
        if(empty($userCode)){
            return array('code' => C('USER.USER_CODE_EMPTY'));
        }
        $userEnterShopInfoRecordMdl = new UserEnterShopInfoRecordModel();
        $getInfo = $userEnterShopInfoRecordMdl->getRecord($userCode, $shopCode, $actionType);
        if($getInfo){
            $count = $userEnterShopInfoRecordMdl->getBrowseQuantity(array('actionType' => $actionType, 'shopCode' => $shopCode), 'distinct(userCode)');
            return array('code' => C('REMIND_TO_SHOP.REPEAT'), 'count' => $count);
        }
        $ret = $userEnterShopInfoRecordMdl->addRecord($shopCode, $userCode, $actionType);
        if($ret){
            // 推送消息
            $count = $userEnterShopInfoRecordMdl->getBrowseQuantity(array('actionType' => $actionType, 'shopCode' => $shopCode), 'distinct(userCode)');
            $userMdl = new UserModel();
            $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('nickName', 'mobileNbr'));
            if(empty($userInfo['nickName'])){
                $userInfo['nickName'] = '用户'.substr($userInfo['mobileNbr'], 7);
            }
            if($actionType == 2){
                $content = str_replace('{{userName}}', $userInfo['nickName'], C('PUSH_MESSAGE.INVITE_SHOP'));
            }elseif($actionType == 3){
                $content = str_replace('{{userName}}', $userInfo['nickName'], C('PUSH_MESSAGE.REMIND_SHOP'));
            }
            if(isset($content)){
                $content = str_replace('{{userCount}}', $count, $content);
                $receiver = explode('-', $shopCode);
                $jpushMdl = new JpushModel(C('SHOP_APP_KEY'), C('SHOP_MASTER_SECRET'));
                $jpushMdl->jPushByAction($receiver, $content, array(), C('PUSH_ACTION.INVITE_SHOP'));
            }
            return array('code' => C('SUCCESS'), 'count' => $count);
        }else{
            return array('code' => C('API_INTERNAL_EXCEPTION'));
        }
    }
}
