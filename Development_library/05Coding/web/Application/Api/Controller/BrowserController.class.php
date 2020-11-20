<?php
/**
 * Created by PhpStorm.
 * User: jihuafei
 * Date: 15-7-1
 * Time: 上午9:47
 */
namespace Api\Controller;
use Common\Model\BankAccountLocalLogModel;
use Common\Model\BankAccountModel;
use Common\Model\BaseModel;
use Common\Model\BatchCouponModel;
use Common\Model\BonusStatisticsModel;
use Common\Model\CmptxsnoLogModel;
use Common\Model\ConsumeOrderModel;
use Common\Model\DistrictModel;
use Common\Model\DownloadLogModel;
use Common\Model\IcbcModel;
use Common\Model\OpenGiftLogModel;
use Common\Model\OrderCouponModel;
use Common\Model\OrderProductModel;
use Common\Model\PrizeRuleModel;
use Common\Model\ProductCategoryModel;
use Common\Model\ProductModel;
use Common\Model\ShopBrandRelModel;
use Common\Model\ShopModel;
use Common\Model\ShopTypeModel;
use Common\Model\ShopTypeRelModel;
use Common\Model\SystemParamModel;
use Common\Model\UserActivityModel;
use Common\Model\UserAddressModel;
use Common\Model\UserCardModel;
use Common\Model\ShopFollowingModel;
use Common\Model\ShopDecorationModel;
use Common\Model\SubAlbumModel;
use Common\Model\ShopPhotoModel;
use Common\Model\CardModel;
use Common\Model\UserEnterShopInfoRecordModel;
use Common\Model\UserConsumeModel;
use Common\Model\UserCouponModel;
use Common\Model\UserGiftModel;
use Common\Model\UserModel;
use Common\Model\UZPLogModel;
use JPush\Exception\APIRequestException;
use Think\Controller;
use Common\Model\ActivityModel;
use Common\Model\BonusModel;
use Common\Model\Pager;
use Common\Model\SmsModel;
use Common\Model\ClientAppLogModel;
use Common\Model\wxApiModel;
use Common\Model\JpushModel;
use \Think\Cache\Driver\Memcache;
use Think\Log;

class BrowserController extends Controller {

    public function index() {
        echo '欢迎来到您的惠圈';
    }

    /**
     * 优惠券兑换结果界面
     */
    public function exchangeCouponRet() {
        $couponCode = I('get.couponCode');
        // 获得商户信息
        $orderCouponMdl = new OrderCouponModel();
        $usedCouponInfo = $orderCouponMdl->getOrderCouponInfo(array('couponCode' => $couponCode), array('batchCouponCode', 'userCode', 'couponCode'));
        $batchCouponMdl = new BatchCouponModel();
        $batchCouponInfo = $batchCouponMdl->getCouponBasicInfo(array('batchCouponCode' => $usedCouponInfo['batchCouponCode']), array('shopCode'));
        $shopMdl = new ShopModel();
        $shopInfo = $shopMdl->getShopInfo($batchCouponInfo['shopCode'], array('shopName'));
        $shopTypeRelMdl = new ShopTypeRelModel();
        $typeValueArr = $shopTypeRelMdl->getShopType($batchCouponInfo['shopCode']);
        if(empty($typeValueArr)){
            $typeValueArr = array(\Consts::SHOP_TYPE_ICBC, \Consts::SHOP_TYPE_PLAT);
        }else{
            $typeValueArr = array_unique(array_merge($typeValueArr, array(\Consts::SHOP_TYPE_ICBC, \Consts::SHOP_TYPE_PLAT)));
        }

        // 获得用户在该商家还有多少兑换券可用
        $notUsedCouponCount = $orderCouponMdl->countNotUsedCoupon($usedCouponInfo['userCode'], $batchCouponInfo['shopCode']);
        // 获得根据距离展示非同行业的商家，商家大致为5个
        $latitude = I('get.latitude'); // 纬度
        $longitude = I('get.longitude'); // 经度
        $shopMdl = new ShopModel();
        $shopList = $shopMdl->searchShop(array('typeValue' => array('NOTIN', $typeValueArr), 'latitude' => $latitude, 'longitude' => $longitude), array(), $this->getPager(1,5), array(), 'distance asc, Shop.popularity desc');

        foreach($shopList as $k => $shop) {
            $distance = $shop['distance'];
            if($distance <= 1000) {
                $shopList[$k]['unitOfLength'] = 'm';
            } elseif($distance > 1000 && $distance <= 100000) {
                $shopList[$k]['distance'] = round($shopList[$k]['distance'] / 1000);
                $shopList[$k]['unitOfLength'] = 'Km';
            } elseif($distance > 100000) {
                $shopList[$k]['distancePre'] = '>';
                $shopList[$k]['distance'] = 100;
                $shopList[$k]['unitOfLength'] = 'Km';
            }
        }
        $assign = array(
            'title' => '兑换结果',
            'notUsedCouponCount' => $notUsedCouponCount,
            'usedCouponInfo' => $usedCouponInfo,
            'shopInfo' => $shopInfo,
            'shopList' => $shopList,
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 用户点赞或者取消赞
     */
    public function zan() {
        if(IS_AJAX) {
            $data = I('post.');
            // 用户对商品赞的结果
            $isZan = $data['isZan'];
            unset($data['isZan']);
            $uZPLogMdl = new UZPLogModel();
            $productMdl = new ProductModel();
            if($isZan == C('NO')) { // 用户未赞，则点赞
                $ret = $uZPLogMdl->editLog($data);
                // 点赞数量+1
                $productMdl->incFiled('zanCount', $data['productId'], 1);
            } else { // 用户已赞，则取消点赞
                $ret = $uZPLogMdl->delLog($data);
                // 点赞数量-1
                $productMdl->decFiled('zanCount', $data['productId'], 1);
            }
            $productInfo = $productMdl->getProductInfo(array('productId' => $data['productId']));
            if($ret['code'] === C('SUCCESS')) {
                $this->ajaxSucc('', array('zanCount' => $productInfo['zanCount']));
            } else {
                $this->ajaxError();
            }
        }
    }

    /**
     * 取消订单
     */
    public function cancelOrder() {
        if(IS_AJAX) {
            $orderCode = I('post.orderCode');
            $consumeOrderMdl = new ConsumeOrderModel();
            $ret = $consumeOrderMdl->cancelConsumeOrder($orderCode);
            if($ret['code'] ===  C('SUCCESS')) {
                $this->ajaxSucc();
            } else {
                $this->ajaxError('', $ret['code']);
            }
        }
    }

    /**
     * 用户确认收货
     */
    public function confirmOrder(){
        if(IS_AJAX) {
            $orderCode = I('post.orderCode');
            $consumeOrderMdl = new ConsumeOrderModel();
            $ret = $consumeOrderMdl->servedConsumeOrder($orderCode);
            if($ret['code'] ===  C('SUCCESS')) {
                $this->ajaxSucc();
            } else {
                $this->ajaxError();
            }
        }
    }

    /**
     * 顾客拒绝支付
     */
    public function rejectPay(){
        if(IS_AJAX) {
            $orderCode = I('post.orderCode');
            $consumeOrderMdl = new ConsumeOrderModel();
            // 修改订单支付状态为不能支付
            $ret = $consumeOrderMdl->updateConsumeOrder(array('orderCode'=>$orderCode), array('status' => 0));
            if($ret) {
                $this->ajaxSucc();
            } else {
                $this->ajaxError();
            }
        }
    }

    /**
     * 编辑外卖订单
     * @return array $ret
     */
    public function editTakeoutOrder() {
        if(IS_AJAX) {
            $data = I('post.');
            $shopCode = $data['shopCode'];
            $productIdList = $data['productId'];
            $productUnitPriceList = $data['productUnitPrice'];
            $productNbrList = $data['productNbr'];
            $tem = array('productId', 'productUnitPrice', 'productNbr');
            foreach($tem as $v) {
                unset($data[$v]);
            }
            $shopMdl = new ShopModel();
            // 获得商家信息
            $shopInfo = $shopMdl->getShopInfo($shopCode, array('isOpenTakeout', 'deliveryStartTime', 'deliveryEndTime', 'deliveryFee'));
            // 若商家没有开启外卖，或当前时间不在商家营业时间内，提示商家已打烊
            if($shopInfo['isOpenTakeout'] == C('NO') || strtotime($shopInfo['deliveryStartTime']) > time() || strtotime($shopInfo['deliveryEndTime']) < time()) {
                $this->ajaxError(C('SHOP.CAN_NOT_TAKE_OUT'));
            }

            $productList = array();
            $orderAmount = $shopInfo['deliveryFee'];
            foreach($productUnitPriceList as $k => $productUnitPrice) {
                $productUnitPriceList[$k] = $productUnitPriceList[$k] * C('RATIO'); // 元化为分
                $orderAmount += $productNbrList[$k] * $productUnitPriceList[$k]; // 总价累加
                $productList[$k]['productId'] = $productIdList[$k]; // 设置产品ID
                $productList[$k]['productUnitPrice'] = $productUnitPriceList[$k]; // 设置产品单价
                $productList[$k]['productNbr'] = $productNbrList[$k]; // 设置产品数量
                $productList[$k]['availableNbr'] = $productNbrList[$k]; // 设置产品已上数量
            }

            $consumeOrderMdl = new ConsumeOrderModel();
            $data['orderAmount'] = $orderAmount;
            $data['actualOrderAmount'] = $orderAmount;
            $addTakeoutOrderRet = $consumeOrderMdl->editTakeoutOrder($data);

            if($addTakeoutOrderRet['code'] == C('SUCCESS')) {
                $orderProductMdl = new OrderProductModel();
                if(! empty($data['orderCode'])) {
                    // 修改订单产品清单
                    $modifyOrderProductRet = $orderProductMdl->modifyOrderProduct($data['orderCode'], $productList);
                    if($modifyOrderProductRet === true) {
                        $this->ajaxSucc(C('SUCCESS'), array('orderCode' => $data['orderCode']));
                    } else {
                        $this->ajaxError(C('API_INTERNAL_EXCEPTION'));
                    }
                } else {
                    $orderProductMdl->startTrans();
                    foreach($productList as &$product) {
                        $product['orderCode'] = $addTakeoutOrderRet['orderCode'];
                        $addOrderProductRet = $orderProductMdl->addOrderProduct($product);
                        if($addOrderProductRet['code'] !== C('SUCCESS')) {
                            $orderProductMdl->rollback();
                            $this->ajaxError(C('API_INTERNAL_EXCEPTION'));
                        }
                    }
                    $orderProductMdl->commit();
                    $this->ajaxSucc(C('SUCCESS'), array('orderCode' => $addTakeoutOrderRet['orderCode']));
                }
            } else {
                $this->ajaxError($addTakeoutOrderRet['code']);
            }
        }
    }

    /**
     * 添加外卖订单地址和备注
     * @return array $ret
     */
    public function addTakeoutOrderOtherInfo() {
        if(IS_AJAX) {
            $data = I('post.');
            $userAddressId = $data['userAddressId'];
            unset($data['userAddressId']);

            $userAddressMdl = new UserAddressModel();
            $userAddressInfo = $userAddressMdl->getUserAddressInfo($userAddressId);
            if(empty($userAddressInfo)) {
                $this->ajaxError(C('USER_ADDRESS.ID_ERROR'));
            }

            $consumeOrderMdl = new ConsumeOrderModel();
//            $data['orderStatus'] = C('FOOD_ORDER_STATUS.ORDERED');
            $data['receiver'] = $userAddressInfo['contactName'];
            $data['receiverMobileNbr'] = $userAddressInfo['mobileNbr'];
            $data['deliveryAddress'] = $userAddressInfo['street'] . $userAddressInfo['position'];
            $editTakeoutOrderRet = $consumeOrderMdl->editTakeoutOrder($data);
            if($editTakeoutOrderRet['code'] == C('SUCCESS')) {
                $this->ajaxSucc(C('SUCCESS'));
            } else {
                $this->ajaxError($editTakeoutOrderRet['code']);
            }
        }
    }

    /**
     * 添加堂食订单
     * @return array $ret
     */
    public function editNotTakeoutOrder() {
        if(IS_AJAX) {
            $data = I('post.');

            $shopCode = $data['shopCode'];
            $opendId = $data['openId'];
            $productIdList = $data['productId'];
            $productUnitPriceList = $data['productUnitPrice'];
            $productNbrList = $data['productNbr'];
            $tem = array('productId', 'productUnitPrice', 'productNbr', 'openId');
            foreach($tem as $v) {
                unset($data[$v]);
            }

            $shopMdl = new ShopModel();
            // 获得商家信息
            $shopInfo = $shopMdl->getShopInfo($shopCode, array('businessHours', 'eatPayType', 'isOpenEat'));
            $timeMeet = $shopMdl->isTimeMeet($shopInfo['businessHours']);

            // 若商家没有开启堂食，或当前时间不在商家营业时间内，提示商家已打烊
            if($shopInfo['isOpenEat'] == C('NO') || !$timeMeet) {
                $this->ajaxError(C('SHOP.SHOP_CLOSED'));
            }

            $productList = array();
            $orderAmount = 0;
            $actualOrderAmount = 0;
            foreach($productUnitPriceList as $k => $productUnitPrice) {
                $productUnitPriceList[$k] = $productUnitPriceList[$k] * C('RATIO');
                $orderAmount += $productNbrList[$k] * $productUnitPriceList[$k];
                $actualOrderAmount += $productNbrList[$k] * $productUnitPriceList[$k];
                $productList[$k]['productId'] = $productIdList[$k];
                $productList[$k]['productUnitPrice'] = $productUnitPriceList[$k];
                $productList[$k]['productNbr'] = $productNbrList[$k];
                $productList[$k]['availableNbr'] = $productNbrList[$k];
            }

            $consumeOrderMdl = new ConsumeOrderModel();
            $data['orderAmount'] = $orderAmount;
            $data['actualOrderAmount'] = $orderAmount;
            $data['eatPayType'] = $shopInfo['eatPayType'];
            if($shopInfo['eatPayType'] == C('EAT_PAY_TYPE.AFTER')) {
                $data['status'] = C('ORDER_STATUS.CAN_NOT_PAY');
            } else {
                $data['status'] = C('UC_STATUS.UNPAYED');
            }
            // 判断订单渠道
            if(!empty($opendId)) {
                $data['orderWay'] = C('ORDER_WAY.WX');
            }
            $addTakeoutOrderRet = $consumeOrderMdl->editNotTakeoutOrder($data);

            if($addTakeoutOrderRet['code'] == C('SUCCESS')) {
                $orderProductMdl = new OrderProductModel();
                if(! empty($data['orderCode'])) {
                    // 修改订单产品清单
                    $modifyOrderProductRet = $orderProductMdl->modifyOrderProduct($data['orderCode'], $productList);
                    if($modifyOrderProductRet === true) {
                        $this->ajaxSucc(C('SUCCESS'), array('orderCode' => $data['orderCode']));
                    } else {
                        $this->ajaxError(C('API_INTERNAL_EXCEPTION'));
                    }
                } else {
                    $orderProductMdl->startTrans();
                    foreach($productList as &$product) {
                        $product['orderCode'] = $addTakeoutOrderRet['orderCode'];
                        $addOrderProductRet = $orderProductMdl->addOrderProduct($product);
                        if($addOrderProductRet['code'] !== C('SUCCESS')) {
                            $orderProductMdl->rollback();
                            $this->ajaxError(C('API_INTERNAL_EXCEPTION'));
                        }
                    }
                    $orderProductMdl->commit();
                    $this->ajaxSucc(C('SUCCESS'), array('orderCode' => $addTakeoutOrderRet['orderCode']));
                }
            } else {
                $this->ajaxError($addTakeoutOrderRet['code']);
            }
        }
    }

    /**
     * 添加堂食订单备注
     * @return array $ret
     */
    public function addNotTakeoutOrderOtherInfo() {
        if(IS_AJAX) {
            $data = I('post.');
            unset($data['shopName']);
            unset($data['logoUrl']);
            unset($data['orderAmount']);
            $consumeOrderMdl = new ConsumeOrderModel();
            $consumeOrderInfo = $consumeOrderMdl->getOrderInfo(array('orderCode' => $data['orderCode']));

            $shopMdl = new ShopModel();
            // 获得商家信息
            $shopInfo = $shopMdl->getShopInfo($consumeOrderInfo['shopCode'], array('eatPayType'));

            $userMdl = new UserModel();
            $userInfo = $userMdl->getUserInfo(array('userCode' => $consumeOrderInfo['clientCode']), array('nickName', 'mobileNbr'));

            $eatPayType = $shopInfo['eatPayType'];
            $data['eatPayType'] = $eatPayType;
            if($eatPayType == C('EAT_PAY_TYPE.AFTER')) {
                $data['orderStatus'] = C('FOOD_ORDER_STATUS.ORDERED');
            }
            $data['receiver'] = $userInfo['nickName'];
            $data['receiverMobileNbr'] = $userInfo['mobileNbr'];
            $data['isFinishOrder'] = C('YES');
            $addTakeoutOrderRet = $consumeOrderMdl->editNotTakeoutOrder($data);
            if($addTakeoutOrderRet['code'] == C('SUCCESS')) {
                $this->ajaxSucc(C('SUCCESS'), '', $data['orderCode']);
            } else {
                $this->ajaxError($addTakeoutOrderRet['code']);
            }
        }
    }

    /**
     * 用于扫码领券的二维码生成地址
     */
    public function coupon() {
        $batchCouponCode = I('get.batchCouponCode');
        redirect('/Wechat/BatchCoupon/share?batchCouponCode=' . $batchCouponCode);
    }

    /**
     * 用于生成商家二维码的链接
     */
    public function shopQrCode() {
        $shopCode = I('get.id');
        // 获得商家信息
        $shopTypeRelMdl = new ShopTypeRelModel();
        $typeValueArr = $shopTypeRelMdl->getShopType($shopCode);
        if($typeValueArr && in_array(\Consts::SHOP_TYPE_FOOD, $typeValueArr)) {
            redirect('/Wechat/Order/GrantAuth/shopCode/'.$shopCode);
        } else {
            redirect('/Wechat/Index/cdownload');
        }
    }

    /**
     * 工银特惠H5页面
     */
    public function icbcAct() {
//        redirect('http://mp.weixin.qq.com/s?__biz=MzA4ODA1NjM1OA==&mid=247719670&idx=1&sn=597a350c347ff14556e64dd1974c9a4b&scene=18#rd');
        $title = '工银特惠';
        $this->assign('title', $title);
        $this->display();
    }

    /**
     * 在线支付协议
     */
    public function onlinePayProtocol() {
        $title = '在线支付协议';
        $this->assign('title', $title);
        $this->display();
    }

    /**
     * 注册活动页面
     */
    public function regAct() {
        $title = '注册活动';
        $this->assign('title', $title);
        $this->display();
    }

    /**
     * 注册成功
     */
    public function regSucc() {
        $title = '注册成功';
        $this->assign('title', $title);
        $userCode = I('get.userCode');
        $userMdl = new UserModel();
        $regInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('inviteCode'));
        $this->assign('regInfo', $regInfo);
        $this->display();
    }

    /**
     * 支付成功
     */
    public function paySucc() {
        $consumeCode = I('get.consumeCode');
        $payChanel = I('get.payChanel');
        $userConsumeMdl = new UserConsumeModel();
        // 获得消费信息（支付状态，赠送的优惠券，使用的优惠券数量）
        $userConsumeInfo = $userConsumeMdl->getConsumeInfo(array('consumeCode' => $consumeCode), array('UserConsume.status', 'userCouponCode', 'UserConsume.couponUsed', 'consumerCode', 'orderType', 'UserConsume.orderCode', 'location', 'ConsumeOrder.orderNbr', 'ConsumeOrder.orderAmount', 'realPay', 'deduction'));
        $userConsumeInfo['orderAmount'] = number_format($userConsumeInfo['orderAmount'] / \Consts::HUNDRED, 2);
        $userConsumeInfo['realPay'] = number_format($userConsumeInfo['realPay'] / \Consts::HUNDRED, 2);
        $userConsumeInfo['deduction'] = number_format($userConsumeInfo['deduction'] / \Consts::HUNDRED, 2);

        // 如果是购买优惠券，获得购买的优惠券列表，获得购买的优惠券的信息
        if($userConsumeInfo['orderType'] == \Consts::ORDER_TYPE_COUPON) {
            $orderCouponMdl = new OrderCouponModel();
            $orderCouponList = $orderCouponMdl->getorderCouponList(array('orderCode' => $userConsumeInfo['orderCode']), array('couponCode', 'batchCouponCode', 'orderCode'));
            $assign['orderCouponList'] = $orderCouponList;

            if($orderCouponList) {
                $batchCouponMdl = new BatchCouponModel();
                $batchCouponInfo = $batchCouponMdl->getCouponBasicInfo(array('batchCouponCode' => $orderCouponList[0]['batchCouponCode']), array('dayStartUsingTime', 'dayEndUsingTime', 'expireTime', 'couponType'));
                $assign['batchCouponInfo'] = $batchCouponInfo;
            }
        }

        // 获得用户信息（是否开启免验证码支付，拒绝次数）
        $userMdl = new UserModel();
        $userInfo = $userMdl->getUserInfo(array('userCode' => $userConsumeInfo['consumerCode']), array('freeValCodePay', 'rejectFreeValTimes'));

        // 设置此次消费没有赠送优惠券
        $userConsumeInfo['isSendCoupon'] = C('NO');
        $sendUserCouponCodeList = explode('|', $userConsumeInfo['userCouponCode']);

        // 获得赠送的优惠券数量
        $userConsumeInfo['sendCouponNbr'] = count($sendUserCouponCodeList);
        if(!empty($userConsumeInfo['userCouponCode'])) {
            // 设置此次消费赠送了优惠券
            $userConsumeInfo['isSendCoupon'] = C('YES');
            $userCouponMdl = new UserCouponModel();
            // 获得赠送的优惠券的信息
            $userCouponList = $userCouponMdl->getUserCouponList(
                array('userCouponCode' => array('IN', $sendUserCouponCodeList)),
                array(
                    'UserCoupon.batchCouponCode',
                    'userCouponCode',
                    'userCouponNbr',
                    'UserCoupon.status',
                    'discountPercent',
                    'insteadPrice',
                    'availablePrice',
                    'payPrice',
                    'function',
                    'couponType',
                    'BatchCoupon.startUsingTime',
                    'BatchCoupon.expireTime',
                    'dayStartUsingTime',
                    'dayEndUsingTime',
                    'BatchCoupon.batchNbr',
                    'BatchCoupon.remark',
                    'Shop.shopName',
                    'Shop.mobileNbr',
                    'Shop.street',
                    'Shop.logoUrl',
//                    'Shop.type', //如果要使用到这个字段，请用 ShopTypeRel 查询到
                    'Shop.shopCode',
                    'popularity',
                    'repeatCustomers',
                    'LEFT(city,2)' => 'city'
                )
            );
            $assign['userCouponList'] = $userCouponList;
        }

        // 获得商户的信息
        $shopMdl = new ShopModel();
        $shopInfo = $shopMdl->getShopInfo($userConsumeInfo['location'], array('logoUrl', 'shopCode', 'shopName'));

        $userConsumeInfo['hqPayChanel'] = $payChanel; // 平台支付通道：扫码支付

        $assign['title'] = '支付结果';
        $assign['userInfo'] = $userInfo;
        $assign['shopInfo'] = $shopInfo;
        $assign['userConsumeInfo'] = $userConsumeInfo;
        $assign['action'] = 'paySucc';
        $this->assign($assign);
        $this->display();
    }

    /**
     * 支付失败
     */
    public function payFail() {
        $errCode = I('get.errCode');
        $assign['retMsg'] = C('ICBC_ERROR_CODE_MSG.' . $errCode);
        $assign['title'] = '支付结果';
        $this->assign($assign);
        $this->display();
    }

    /**
     * 惠币介绍界面
     */
    public function huibiIntro() {
        $title = '惠币介绍';
        $this->assign('title', $title);
        $this->display();
    }

    /**
     * 显示优惠券使用规则
     * @param string $batchCouponCode 优惠券编码
     */
    public function couponRule($batchCouponCode) {
        $batchCouponMdl = new BatchCouponModel();
        $couponInfo = $batchCouponMdl->getCouponInfo($batchCouponCode);
        $this->assign('couponInfo', $couponInfo);
        $this->display();
    }

    /**
     * 客户端的商圈协议
     */
    public function cProtocol() {
        $title = '惠圈用户协议';
        $this->assign('title', $title);
        $this->display();
    }

    /**
     * 客户端的关于惠圈
     */
    public function cAbout() {
        $title = '关于惠圈';
        $this->assign('title', $title);
        $this->display();
    }

    /**
     * 客户端获得活动详情
     * @param string $actCode 活动编码
     */
    public function cGetActInfo($actCode) {
        $actMdl = new ActivityModel();
        $actInfo = $actMdl->getActInfo(array('activityCode' => $actCode), array('Activity.*', 'Shop.shopName'), array(array('joinTable' => 'Shop', 'joinCondition' => 'Shop.shopCode = Activity.shopCode', 'joinType' => 'INNER')));
        if(isset($actInfo['startTime']) && $actInfo['startTime']){
            $actInfo['startTime'] = str_replace('-', '.', str_replace('-0', '.', substr($actInfo['startTime'], 0, 10)));
        }
        if(isset($actInfo['endTime']) && $actInfo['endTime']){
            $actInfo['endTime'] = str_replace('-', '.', str_replace('-0', '.', substr($actInfo['endTime'], 0, 10)));
        }
        $assign = array(
            'title' => '活动详情',
            'actInfo' => $actInfo,
        );
        $this->assign($assign);
        $this->display();
    }

    public function cGetSpecialInfo() {
        $aMdl = new ActivityModel();
        $activityCode = I('get.activityCode');
        //通过活动编码查到的活动详情，前期设置平台主题活动功能无，所以先默认以下数据（简）
//        $specialInfo = $aMdl->getActInfo(array('activityCode' => $activityCode), array('Activity.*', 'Shop.shopName'), array(array('joinTable' => 'Shop', 'joinCondition' => 'Shop.shopCode = Activity.shopCode', 'joinType' => 'INNER')));
        $specialInfo = array(
            'title'=>'很高兴遇见你<br />遇见你的小甜心',
            'shortDes'=>'不管你在或不在，我都在这里，不管你理或不理会，我都会一直支持你。',
            'img'=>array('/Public/img/couponDefault/1.png', '/Public/img/couponDefault/2.png', '/Public/img/couponDefault/3.png', '/Public/img/couponDefault/4.png', '/Public/img/couponDefault/5.png', '/Public/img/couponDefault/6.png'),
            'batchCouponCode' => '0197fc3e-db90-6be4-07cb-e7bc02f94639',
        );
        $assign = array(
            'specialInfo' => $specialInfo,
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 抢红包活动页面
     */
    public function grabBonus($bonusCode = '') {
//        $bonusCode = '4db11572-b976-8484-9fc9-1fa253fde351';
        $bonusMdl = new BonusModel();
        $bonusInfo = $bonusMdl->getBonusInfo($bonusCode);
        $assign = array(
            'title' => '红包活动',
            'bonusCode' => $bonusCode,
            'bonusInfo' => $bonusInfo,
            'img' => ''
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 获得商家商品详情
     */
    public function shopProductInfo() {
        $userCode = I('get.userCode'); // 用户编码
        $shopCode = I('get.shopCode'); // 商户编码
        $type = I('get.type'); // 订单类型
        $openId = I('get.openId'); // 用户微信openId
        $productId = I('get.productId'); // 产品ID
        $productMdl = new ProductModel();
        $productInfo = $productMdl->getProductInfo(array('productId' => $productId));
        // 制定商品原价
        if(!empty($type)){
            if($type == \Consts::ORDER_TYPE_TAKE_OUT){
                $productInfo['originalPrice'] = $productInfo['notTakeoutPrice'];
            }elseif ($type == \Consts::ORDER_TYPE_TAKE_OUT) {
                $productInfo['originalPrice'] = $productInfo['takeoutPrice'];
            }
        }
        // 制定商品最终价格
        $productInfo['finalPrice'] = $productMdl->calProductFinalPrice($productInfo['originalPrice'], $productInfo['discount'], $productInfo['dropPrice']);
        // 设置商品是否有优惠
        $productInfo['isDiscount'] = $productInfo['finalPrice'] == $productInfo['originalPrice'] ? C('NO') : C('YES');

        $tem = array('takeoutPrice', 'notTakeoutPrice', 'originalPrice', 'dropPrice', 'finalPrice');
        foreach($tem as $v) {
            $productInfo[$v] = $productInfo[$v] / \Consts::HUNDRED;
        }

        $uZPLogMdl = new UZPLogModel();
        $logInfo = $uZPLogMdl->getLogInfo(array('productId' => $productInfo['productId'], 'userCode' => $userCode));
        $productInfo['isZan'] = $logInfo ? C('YES') : C('NO');

        $shopMdl = new ShopModel();
        // 获得商户信息
        $shopInfo = $shopMdl->getShopInfo($productInfo['shopCode'], array('isCatering'));

        $backup = I('backup'); // 回退页面，原生或H5
        $backAction = I('get.backAction'); // 回退H5的action
        $orderCode = I('get.orderCode', ''); // 订单编码
        $assign = array(
            'title' => '商品详情',
            'productInfo' => $productInfo,
            'userCode' => $userCode,
            'shopCode' => $shopCode,
            'shopInfo' => $shopInfo,
            'backup' => $backup,
            'backAction' => $backAction,
            'openId' => $openId,
            'type' => $type,
            'orderCode' => $orderCode,
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 获得商家商品列表
     */
    public function shopProductList() {
        $userCode = I('get.userCode'); // 用户编码
        $shopCode = I('get.shopCode'); // 商家编码
        $shopMdl = new ShopModel();
        // 获得商家信息
        $shopInfo = $shopMdl->getShopInfo($shopCode, array('Shop.shopCode', 'shopName', 'isCatering'));

        // 获得商家所有商品分类
        $pCateModel = new ProductCategoryModel();
        $pCateList = $pCateModel->getProductCategoryList(array('shopCode' => $shopCode));

        $productMdl = new ProductModel();
        $uZPLogMdl = new UZPLogModel();
        foreach($pCateList as &$v) {
            // 获得商品类别下的商品列表
            $productList = $productMdl->getProductList(array('categoryId' => $v['categoryId']));
            foreach($productList as &$product) {
                // 制定商品最终价格
                $product['finalPrice'] = $productMdl->calProductFinalPrice($product['originalPrice'] * \Consts::HUNDRED, $product['discount'], $product['dropPrice'] * \Consts::HUNDRED);
                $product['finalPrice'] = $product['finalPrice'] / \Consts::HUNDRED; // 最终价格，单位分化元
                // 设置商品是否有优惠
                $product['isDiscount'] = $product['finalPrice'] == $product['originalPrice'] ? C('NO') : C('YES');

                $logInfo = $uZPLogMdl->getLogInfo(array('productId' => $product['productId'], 'userCode' => $userCode));
                $product['isZan'] = $logInfo ? C('YES') : C('NO');
            }
            $v['productList'] = $productList;
        }

        $assign = array(
            'title' => '商品列表',
            'shopInfo' => $shopInfo,
            'pCateList' => $pCateList,
            'userCode' => $userCode,
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 商家端的商圈协议
     */
    public function sProtocol() {
        $title = '惠圈商户合作协议';
        $this->assign('title', $title);
        $this->display();
    }

    /**
     * 商家端的关于惠圈
     */
    public function sAbout() {
        $title = '关于惠圈';
        $this->assign('title', $title);
        $this->display();
    }

    /**
     * 商家端刷卡问题解答
     */
    public function posFaq() {
        $title = '刷卡问题解答';
        $this->assign('title', $title);
        $this->display();
    }

    /**
     * 商家端获得活动详情
     * @param string $actCode 活动编码
     * @param string $preContent json格式的字符串，储存预览时的活动信息
     */
    public function sGetActInfo($actCode = '', $preContent = '') {
        if($preContent != '') {
            $actInfo = json_decode($preContent, true);
        } else {
            $actMdl = new ActivityModel();
            $actInfo = $actMdl->getActInfo(array('activityCode' => $actCode), array('Activity.*', 'Shop.shopName'), array(array('joinTable' => 'Shop', 'joinCondition' => 'Shop.shopCode = Activity.shopCode', 'joinType' => 'INNER')));
        }
        if(isset($actInfo['startTime']) && $actInfo['startTime']){
            $actInfo['startTime'] = str_replace('-', '.', str_replace('-0', '.', substr($actInfo['startTime'], 0, 10)));
        }
        if(isset($actInfo['endTime']) && $actInfo['endTime']){
            $actInfo['endTime'] = str_replace('-', '.', str_replace('-0', '.', substr($actInfo['endTime'], 0, 10)));
        }
        $assign = array(
            'title' => '活动详情',
            'actInfo' => $actInfo,
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 计算订单产品总价
     * @param array $orderProductList 订单产品列表
     * @return int $amount
     */
    private function calOrderProductPrice($orderProductList) {
        $amount = 0;
        foreach($orderProductList as $v) {
            $amount += $v['productUnitPrice'] * $v['productNbr'];
        }
        return $amount;
    }

    /**
     * 客户端堂食点餐页面
     */
    public function cEatOrder() {
        $title = '堂食点餐';
        $shopCode = I('get.shopCode'); // 商户编码
        $userCode = I('get.userCode'); // 用户编码
        $openId = I('get.openId'); //
        $type = I('get.type'); //
        $shopMdl = new ShopModel();
        // 获得商家信息
        $shopInfo = $shopMdl->getShopInfo($shopCode, array('shopName, deliveryFee'));

        // 获得订单已经点的产品
        $orderCode = I('get.orderCode');
        $orderProductMdl = new OrderProductModel();
        $orderProductList = $orderProductMdl->getProductListByOrder($orderCode);
        $orderProductNbr = $orderProductMdl->sumOrderProductNbr($orderCode); // 已经点了的产品总数
        $orderProductPrice = $this->calOrderProductPrice($orderProductList); // 已经点了的产品总价

        // 获得商家产品类别
        $productCategoryMdl = new ProductCategoryModel();
        $categoryList = $productCategoryMdl->getProductCategoryList(array('shopCode' => $shopCode));
        $productMdl = new ProductModel();
        foreach($categoryList as $k => &$category) {
            $productList = $productMdl->getProductList(array('categoryId' => $category['categoryId'], 'productStatus' => array('NEQ', C('PRODUCT_STATUS.OFF_SHELF'))));
            if(empty($productList)) {
                unset($categoryList[$k]);
            }
            $category['productList'] = $productList;
            $category['orderedNbr'] = 0;
            foreach($orderProductList as $orderProduct) {
                if($category['categoryId'] == $orderProduct['categoryId']) {
                    $category['orderedNbr'] += $orderProduct['productNbr'];
                }
                foreach($category['productList'] as &$product) {
                    if($product['productId'] == $orderProduct['productId']) {
                        $product['orderNbr'] = $orderProduct['productNbr'];
                    } else  {
                        $product['orderNbr'] = $product['orderNbr'] > 0 ? $product['orderNbr'] : 0;
                    }
                }
            }
        }
        $userCouponMdl = new UserCouponModel();
        // 获得用户在该商家拥有的可用的抵扣优惠券
        $userReductionCouponList = $userCouponMdl->getUserDiffTypeCoupon($userCode, $shopCode, C('COUPON_TYPE.REDUCTION'), C('YES'));
        $userReductionCouponCodeList = array();
        foreach($userReductionCouponList as &$coupon) {
            $coupon['availablePrice'] = $coupon['availablePrice'] / C('RATIO');
            $coupon['insteadPrice'] = $coupon['insteadPrice'] / C('RATIO');
            $userReductionCouponCodeList[] = $coupon['batchCouponCode'];
        }

        // 获得用户该商家拥有的可用的折扣优惠券
        $userDiscountCouponList = $userCouponMdl->getUserDiffTypeCoupon($userCode, $shopCode, C('COUPON_TYPE.DISCOUNT'), C('YES'));
        $userDiscountCouponCodeList = array();
        foreach($userDiscountCouponList as &$coupon) {
            $coupon['availablePrice'] = $coupon['availablePrice'] / C('RATIO');
            $coupon['discountPercent'] = $coupon['discountPercent'] / C('DISCOUNT_RATIO');
            $userDiscountCouponCodeList[] = $coupon['batchCouponCode'];
        }

        $batchCouponMdl = new BatchCouponModel();
        // 获得该商家拥有的，可用的，用户没有的，抵扣优惠券
        $shopReductionCouponList = $batchCouponMdl->getShopDiffTypeCoupon($userCode, $shopCode, C('COUPON_TYPE.REDUCTION'), $userReductionCouponCodeList);

        // 获得该商家拥有的，可用的，用户没有的，折扣优惠券
        $shopDiscountCouponList = $batchCouponMdl->getShopDiffTypeCoupon($userCode, $shopCode, C('COUPON_TYPE.DISCOUNT'), $userDiscountCouponCodeList);
        //首单立减
        $userConsumeMdl = new UserConsumeModel();
        $isFirst = $userConsumeMdl->isFirst($userCode) ? C('YES') : C('NO');
        $assign = array(
            'title' => $title,
            'shopInfo' => $shopInfo,
            'shopCode' => $shopCode,
            'userCode' => $userCode,
            'orderCode' => $orderCode,
            'categoryList' => $categoryList,
            'type' => $type,
            'openId' => $openId,
            'userReductionCouponList' => $userReductionCouponList,
            'userDiscountCouponList' => $userDiscountCouponList,
            'shopReductionCouponList' => $shopReductionCouponList,
            'shopDiscountCouponList' => $shopDiscountCouponList,
            'orderProductList' => $orderProductList,
            'orderProductNbr' => $orderProductNbr,
            'orderProductPrice' => $orderProductPrice,
            'isFirst' => $isFirst,
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 客户端外卖点餐页面
     */
    public function cTakeawayOrder() {
        $title = '外卖点餐';
        $shopCode = I('get.shopCode');

        $shopMdl = new ShopModel();
        // 获得商家信息
        $shopInfo = $shopMdl->getShopInfo($shopCode, array('Shop.shopCode', 'shopName', 'popularity', 'takeoutRequirePrice', 'deliveryFee', 'isOpenTakeout', 'deliveryEndTime', 'deliveryStartTime', 'Shop.longitude' , 'Shop.latitude'));
        $shopInfo['takeoutRequirePrice'] = $shopInfo['takeoutRequirePrice'] / \Consts::HUNDRED;
        $shopInfo['deliveryFee'] = $shopInfo['deliveryFee'] / \Consts::HUNDRED;
        $shopInfo['deliveryStartTime'] = strtotime(date('Y-m-d ' . $shopInfo['deliveryStartTime']));
        $shopInfo['deliveryEndTime'] = strtotime(date('Y-m-d' . $shopInfo['deliveryEndTime']));
//        if($shopInfo['isOpenTakeout'] == C('YES') && (time() < $shopInfo['deliveryStartTime'] || time() > $shopInfo['deliveryEndTime'])) {
//            $shopInfo['isOpenTakeout'] = C('NO');
//        }
        $nowTime = time();

        // 获得订单已经点的产品
        $orderCode = I('get.orderCode');
        $orderProductMdl = new OrderProductModel();
        $orderProductList = $orderProductMdl->getProductListByOrder($orderCode);
        $orderProductNbr = $orderProductMdl->sumOrderProductNbr($orderCode); // 已经点了的产品总数
        $orderProductPrice = $this->calOrderProductPrice($orderProductList); // 已经点了的产品总价

        $productCategoryMdl = new ProductCategoryModel();
        // 获得商家产品类别
        $categoryList = $productCategoryMdl->getProductCategoryList(array('shopCode' => $shopCode));
        // 在产品类别数组开头插入掌柜推荐类别
        array_unshift($categoryList, array('categoryId' => \Consts::PRODUCT_CATEGORY_RECOM_ID, 'categoryName' => '掌柜推荐'));
        $productMdl = new ProductModel();
        foreach($categoryList as $k => $category) {
            if($categoryList[$k]['categoryId'] == \Consts::PRODUCT_CATEGORY_RECOM_ID) { // 掌柜推荐类别
                // 获得掌柜推荐的产品
                $productList = $productMdl->getRecommProductList($shopCode);
            } else {
                // 获得类目下的产品（状态不为下架，可外卖的商品）
                $productList = $productMdl->getProductList(array('categoryId' => $categoryList[$k]['categoryId'], 'productStatus' => array('NEQ', C('PRODUCT_STATUS.OFF_SHELF')), 'isTakenOut' => C('YES')));
                if(empty($productList)) { // 该类目下没有展示的商品
                    unset($categoryList[$k]); // 去除该类目
                }
            }
            $categoryList[$k]['productList'] = $productList;
            $categoryList[$k]['orderedNbr'] = 0;
            foreach($orderProductList as $orderProduct) {
                if($categoryList[$k]['categoryId'] == $orderProduct['categoryId']) {
                    $categoryList[$k]['orderedNbr'] += $orderProduct['productNbr'];
                }
                foreach($categoryList[$k]['productList'] as &$product) {
                    if($product['productId'] == $orderProduct['productId']) {
                        $product['orderNbr'] = $orderProduct['productNbr'];
                    } else  {
                        $product['orderNbr'] = $product['orderNbr'] > 0 ? $product['orderNbr'] : 0;
                    }
                }
            }
        }
        $userCode = I('get.userCode');
        $userCouponMdl = new UserCouponModel();
        // 获得用户在该商家拥有的可用的抵扣优惠券
        $userReductionCouponList = $userCouponMdl->getUserDiffTypeCoupon($userCode, $shopCode, C('COUPON_TYPE.REDUCTION'), C('YES'));
        $userReductionCouponCodeList = array();
        foreach($userReductionCouponList as &$coupon) {
            $coupon['availablePrice'] = $coupon['availablePrice'] / C('RATIO');
            $coupon['insteadPrice'] = $coupon['insteadPrice'] / C('RATIO');
            $userReductionCouponCodeList[] = $coupon['batchCouponCode'];
        }

        // 获得用户该商家拥有的可用的折扣优惠券
        $userDiscountCouponList = $userCouponMdl->getUserDiffTypeCoupon($userCode, $shopCode, C('COUPON_TYPE.DISCOUNT'), C('YES'));
        $userDiscountCouponCodeList = array();
        foreach($userDiscountCouponList as &$coupon) {
            $coupon['availablePrice'] = $coupon['availablePrice'] / C('RATIO');
            $coupon['discountPercent'] = $coupon['discountPercent'] / C('DISCOUNT_RATIO');
            $userDiscountCouponCodeList[] = $coupon['batchCouponCode'];
        }

        $batchCouponMdl = new BatchCouponModel();
        // 获得该商家拥有的，可用的，用户没有的，抵扣优惠券
        $shopReductionCouponList = $batchCouponMdl->getShopDiffTypeCoupon($userCode, $shopCode, C('COUPON_TYPE.REDUCTION'), $userReductionCouponCodeList);

        // 获得该商家拥有的，可用的，用户没有的，折扣优惠券
        $shopDiscountCouponList = $batchCouponMdl->getShopDiffTypeCoupon($userCode, $shopCode, C('COUPON_TYPE.DISCOUNT'), $userDiscountCouponCodeList);
        $difference = $shopInfo['takeoutRequirePrice'] - $orderProductPrice ;
        //加上配送费的总价
        $orderProductPrice = $shopInfo['deliveryFee']+$orderProductPrice;
        //首单立减
        $userConsumeMdl = new UserConsumeModel();
        $isFirst = $userConsumeMdl->isFirst($userCode) ? C('YES') : C('NO');
        $assign = array(
            'shopInfo' => $shopInfo,
            'title' => $title,
            'shopCode' => $shopCode,
            'userCode' => $userCode,
            'categoryList' => $categoryList,
            'nowTime' => $nowTime,
            'difference' => $difference,
            'orderCode' => $orderCode,
            'userReductionCouponList' => $userReductionCouponList,
            'userDiscountCouponList' => $userDiscountCouponList,
            'shopReductionCouponList' => $shopReductionCouponList,
            'shopDiscountCouponList' => $shopDiscountCouponList,
            'orderProductList' => $orderProductList,
            'orderProductNbr' => $orderProductNbr,
            'orderProductPrice' => $orderProductPrice,
            'isFirst' => $isFirst
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 客户端提交外卖订单
     */
    public function cOrderSubmit() {
        $userAddressId = I('get.userAddressId');
        $userAddressMdl = new UserAddressModel();
        $userAddressInfo = $userAddressMdl->getUserAddressInfo($userAddressId);

        $orderCode = I('get.orderCode');
        $consumeOrderMdl = new ConsumeOrderModel();
        $orderInfo = $consumeOrderMdl->getOrderInfo(array('orderCode' => $orderCode));
        $orderInfo['orderAmount'] = $orderInfo['orderAmount'] / C('RATIO');

        $orderProductMdl = new OrderProductModel();
        $orderProductList = $orderProductMdl->getProductListByOrder($orderCode);
        foreach ($orderProductList as $k => $product){
            $orderProductList[$k]['productTotalPrice'] = $product['productNbr']*$product['productUnitPrice'];
        }
        $shopMdl = new ShopModel();
        // 获得商家信息
        $shopInfo = $shopMdl->getShopInfo($orderInfo['shopCode'], array('deliveryFee', 'logoUrl', 'shopName', 'Shop.shopCode', 'longitude', 'latitude', 'deliveryDistance'));
        $shopInfo['deliveryFee'] = $shopInfo['deliveryFee'] / C('RATIO');

        $title = '提交订单';
        $assign = array(
            'title' => $title,
            'orderInfo' => $orderInfo,
            'orderProductList' => $orderProductList,
            'shopInfo' => $shopInfo,
            'userAddressInfo' => $userAddressInfo
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 客户端提交堂食订单
     */
    public function cEatSubmit() {
        $orderCode = I('get.orderCode');
        $openId = I('get.openId');
        $consumeOrderMdl = new ConsumeOrderModel();
        $orderInfo = $consumeOrderMdl->getOrderInfo(array('orderCode' => $orderCode));
        $orderInfo['orderAmount'] = $orderInfo['orderAmount'] / C('RATIO');

        $orderProductMdl = new OrderProductModel();
        $orderProductList = $orderProductMdl->getProductListByOrder($orderCode);
        foreach ($orderProductList as $k => $product){
            $orderProductList[$k]['productTotalPrice'] = $product['productNbr']*$product['productUnitPrice'];
        }
        $shopMdl = new ShopModel();
        // 获得商家信息
        $shopInfo = $shopMdl->getShopInfo($orderInfo['shopCode'], array('logoUrl', 'shopName', 'Shop.shopCode'));

        $title = '提交订单';
        $assign = array(
            'title' => $title,
            'orderInfo' => $orderInfo,
            'shopInfo' => $shopInfo,
            'openId' => $openId,
            'orderProductList' => $orderProductList,
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 客户端订单详情
     */
    public function cGetOrder() {
        $shopCode = I('get.shopCode'); // 商家编码
        $userCode = I('get.userCode'); // 用户编码
        $openId = I('get.openId'); // 用户微信openId
        $page = I('get.page'); // 页码
        $consumeOrderMdl = new ConsumeOrderModel();
        // 获得用户产品订单列表
        if($userCode) {
            $userProductOrderList = $consumeOrderMdl->listProductOrder(array('clientCode' => $userCode, 'ConsumeOrder.shopCode' => $shopCode), $this->getPager($page));
        } else {
            $userProductOrderList = array();
        }
        $title = '我的订单';
        $assign = array(
            'userCode' => $userCode,
            'shopCode' => $shopCode,
            'title' => $title,
            'openId' => $openId,
            'userProductOrderList' => $userProductOrderList,
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($userProductOrderList) ? '' : $this->fetch('Browser:cGetOrderWidget');
            $this->ajaxReturn(array('status' => 0, 'html' => $html));
        }
    }

    /**
     * 获得优惠券类型的名字
     * @param int $couponType 优惠券类型
     * @return string $couponName 优惠券类型名字
     */
    private function getCouponName($couponType) {
        switch($couponType) {
            case \Consts::COUPON_TYPE_EXCHANGE:
                $couponName = '兑换券';
                break;
            case \Consts::COUPON_TYPE_VOUCHER:
                $couponName = '代金券';
                break;
            default :
                $couponName = '优惠券';
        }
        return $couponName;
    }

    /**
     * 订单优惠券已退款页面
     */
    public function orderCouponRefunding() {
        $orderCode = I('get.orderCode');
        // 获得订单信息
        $consumeOrderMdl = new ConsumeOrderModel();
        $orderInfo = $consumeOrderMdl->getOrderInfo(array('orderCode' => $orderCode));

        // 如果是优惠券订单，获得订单购买的优惠券
        $orderCouponMdl = new OrderCouponModel();
        $orderCouponList = $orderCouponMdl->getOrderCouponList(array('orderCode' => $orderCode, 'status' => \Consts::ORDER_COUPON_STATUS_REFUNDING_NOUSE), array('couponCode', 'batchCouponCode', 'orderCouponCode', 'status', 'refundReason', 'refundRemark'));

        // 获得购买的优惠券的信息
        $batchCouponMdl = new BatchCouponModel();
        $batchCouponInfo = $batchCouponMdl->getCouponBasicInfo(array('batchCouponCode' => $orderCouponList[0]['batchCouponCode']), array('couponType', 'payPrice', 'insteadPrice'));
        $batchCouponInfo['insteadPrice'] = $batchCouponInfo['insteadPrice'] / \Consts::HUNDRED;
        $batchCouponInfo['payPrice'] = $batchCouponInfo['payPrice'] / \Consts::HUNDRED;
        $batchCouponInfo['couponName'] = $this->getCouponName($batchCouponInfo['couponType']);

        // 获得要退款的金额详情
        $refundingOrderCouponCodeList = array();
        foreach($orderCouponList as $v) {
            $refundingOrderCouponCodeList[] = $v['orderCouponCode'];
        }
        $toRefundAmountDetails = $orderCouponMdl->calRefundMoney($refundingOrderCouponCodeList);

        $shopMdl = new ShopModel();
        // 获得商家信息（商家名称，电话，配送费）
        $shopInfo = $shopMdl->getShopInfo($orderInfo['shopCode'], array('shopCode', 'shopName', 'tel', 'deliveryFee'));
        $backType = '';

        $assign = array(
            'title' => '退款中',
            'orderInfo' => $orderInfo,
            'shopInfo' => $shopInfo,
            'serviceTel'=> '400-04-95588',
            'orderCouponList' => $orderCouponList,
            'batchCouponInfo' => $batchCouponInfo,
            'backType' => $backType,
            'toRefundAmountDetails' => $toRefundAmountDetails,
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 订单优惠券已退款页面
     */
    public function orderCouponRefund() {
        $orderCode = I('get.orderCode');
        // 获得订单信息
        $consumeOrderMdl = new ConsumeOrderModel();
        $orderInfo = $consumeOrderMdl->getOrderInfo(array('orderCode'=>$orderCode));

        // 如果是优惠券订单，获得订单购买的优惠券
        $orderCouponMdl = new OrderCouponModel();
        $orderCouponList = $orderCouponMdl->getOrderCouponList(array('orderCode' => $orderCode, 'status' => \Consts::ORDER_COUPON_STATUS_REFUNDED_NOUSE), array('couponCode', 'batchCouponCode', 'orderCouponCode', 'status', 'refundReason', 'refundRemark'));

        // 获得购买的优惠券的信息
        $batchCouponMdl = new BatchCouponModel();
        $batchCouponInfo = $batchCouponMdl->getCouponBasicInfo(array('batchCouponCode' => $orderCouponList[0]['batchCouponCode']), array('couponType', 'payPrice', 'insteadPrice'));
        $batchCouponInfo['insteadPrice'] = $batchCouponInfo['insteadPrice'] / \Consts::HUNDRED;
        $batchCouponInfo['payPrice'] = $batchCouponInfo['payPrice'] / \Consts::HUNDRED;
        $batchCouponInfo['couponName'] = $this->getCouponName($batchCouponInfo['couponType']);

        // 获得支付记录
        $userConsumeMdl = new UserConsumeModel();
        $consumeInfo = $userConsumeMdl->getPayedConsumeInfoByOrderCode($orderCode);

        // 获得已退款的金额详情
        $refundedDetails = $consumeOrderMdl->getCouponOrderRefundDetail($orderInfo['refundAmount'], $consumeInfo['shopBonus'], $consumeInfo['platBonus'], $consumeInfo['realPay']);
        $tem = array('bankcardRefund', 'platBonusRefund', 'shopBonusRefund');
        foreach($tem as $v) {
            $refundedDetails[$v] = $refundedDetails[$v] / \Consts::HUNDRED;
        }

        $shopMdl = new ShopModel();
        // 获得商家信息（商家名称，电话，配送费）
        $shopInfo = $shopMdl->getShopInfo($orderInfo['shopCode'], array('shopCode', 'shopName', 'tel', 'deliveryFee'));
        $backType = '';

        $assign = array(
            'title' => '已退款',
            'orderInfo' => $orderInfo,
            'shopInfo' => $shopInfo,
            'serviceTel'=> '400-04-95588',
            'orderCouponList' => $orderCouponList,
            'batchCouponInfo' => $batchCouponInfo,
            'backType' => $backType,
            'refundedDetails' => $refundedDetails,
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 客户端申请退款
     */
    public function cApplyRefund() {
        if(IS_GET) {
            $orderCode = I('get.orderCode'); // 订单编码
            $consumeOrderMdl = new ConsumeOrderModel();
            // 获得订单信息
            $orderInfo = $consumeOrderMdl->getOrderInfo(array('orderCode' => $orderCode));
            $orderInfo['orderAmount'] = number_format($orderInfo['orderAmount'] / \Consts::HUNDRED, '2', '.', ''); // 订单消费金额，单位：元
            if($orderInfo['orderType'] == \Consts::ORDER_TYPE_COUPON) { // 购买优惠券的订单
                // 如果是优惠券订单，获得订单购买的优惠券
                $orderCouponMdl = new OrderCouponModel();
                $orderCouponList = $orderCouponMdl->getOrderCouponList(array('orderCode' => $orderCode, 'status' => \Consts::ORDER_COUPON_STATUS_USE), array('couponCode', 'batchCouponCode', 'orderCouponCode', 'status'));
                // 获得购买的优惠券的信息
                $batchCouponMdl = new BatchCouponModel();
                $batchCouponInfo = $batchCouponMdl->getCouponBasicInfo(array('batchCouponCode' => $orderCouponList[0]['batchCouponCode']), array('couponType', 'payPrice', 'insteadPrice', 'function'));
                $batchCouponInfo['insteadPrice'] = $batchCouponInfo['insteadPrice'] / \Consts::HUNDRED;
                $batchCouponInfo['payPrice'] = $batchCouponInfo['payPrice'] / \Consts::HUNDRED;
                $batchCouponInfo['couponName'] = $this->getCouponName($batchCouponInfo['couponType']);
            } elseif(in_array($orderInfo['orderType'], array(\Consts::ORDER_TYPE_NO_TAKE_OUT, \Consts::ORDER_TYPE_TAKE_OUT))) {
                $orderInfo['receiveTime'] = date('H:i', strtotime($orderInfo['orderTime']) + 10 * 60); // 接单截至时间
                $orderInfo['time'] = date('H:i', strtotime($orderInfo['orderTime'])); // 下单时间
                // 如果是外卖订单，堂食订单，获得订单购买的菜品
                $orderProductMdl = new OrderProductModel();
                $orderProductList = $orderProductMdl->getProductListByOrder($orderCode);
                foreach($orderProductList as &$orderProduct) {
                    $orderProduct['productUnitPrice'] = number_format($orderProduct['productUnitPrice'], '2', '.', '');
                }
                // 获得订单购买产品的总数
                $orderProductCount = 0;
                foreach($orderProductList as $product) {
                    $orderProductCount += $product['productNbr'];
                }
            }

            $shopMdl = new ShopModel();
            // 获得商家信息（商家名称，电话，配送费）
            $shopInfo = $shopMdl->getShopInfo($orderInfo['shopCode'], array('shopCode', 'shopName', 'tel', 'deliveryFee'));
            $shopInfo['deliveryFee'] = number_format($shopInfo['deliveryFee'] / \Consts::HUNDRED, '2', '.', '');

            // 获得支付记录
            $userConsumeMdl = new UserConsumeModel();
            $consumeInfo = $userConsumeMdl->getPayedConsumeInfoByOrderCode($orderCode);
            $tempb = array('realPay', 'deduction', 'shopBonus', 'platBonus', 'bankCardDeduction', 'cardDeduction', 'couponDeduction');
            foreach($tempb as $v) {
                $consumeInfo[$v] = number_format($consumeInfo[$v] / C('RATIO'), '2', '.', '');
            }

            // 获得使用的优惠券
            $userCouponInfo = array();
            if($consumeInfo['couponUsed'] > 0) {
                $userCouponMdl = new UserCouponModel();
                $userCouponInfo = $userCouponMdl->getUserCouponInfoB(
                    array('UserCoupon.consumeCode' => $consumeInfo['consumeCode']),
                    array('UserCoupon.userCouponCode', 'BatchCoupon.insteadPrice', 'couponType', 'discountPercent', 'availablePrice', 'UserCoupon.applyTime', 'BatchCoupon.validityPeriod', 'BatchCoupon.expireTime')
                );
            }
            $userCouponInfo = $userCouponInfo[0];
            $userCouponInfo['availablePrice'] = $userCouponInfo['availablePrice'] / \Consts::HUNDRED;
            $userCouponInfo['insteadPrice'] = $userCouponInfo['insteadPrice'] / \Consts::HUNDRED;
            $userCouponInfo['discountPercent'] = $userCouponInfo['discountPercent'] / C('DISCOUNT_RATIO');

            $backType = I('get.backType'); // 回退的页面类型，H5或者原型
            $assign = array(
                'title' => '申请退款',
                'orderInfo' => $orderInfo,
                'shopInfo' => $shopInfo,
                'serviceTel'=> '400-04-95588',
                'orderProductList' => $orderProductList,
                'orderProductCount' => $orderProductCount,
                'orderCouponList' => $orderCouponList,
                'batchCouponInfo' => $batchCouponInfo,
                'consumeInfo' => $consumeInfo,
                'userCouponInfo' => $userCouponInfo,
                'backType' => $backType,
            );
            $this->assign($assign);
            $this->display();
        } else {
            $data = I('post.');
            $data['refundApplyTime'] = date('Y-m-d H:i:s');
            $data['status'] = C('ORDER_STATUS.REFUNDING');
            $consumeOrderMdl = new ConsumeOrderModel();
            $ret = $consumeOrderMdl->applyRefund($data);
            if($ret == C('SUCCESS')) {
                $this->ajaxSucc();
            } else {
                $this->ajaxError($ret);
            }
        }
    }

   

	/**
     * 购买优惠券的确认退款
     */
	  public function couponOrderApplyRefund() {
        if(IS_AJAX) {
            $data = I('post.');
            if(empty($data['orderCouponCode'])) {
                $this->ajaxError('请选择优惠券');
            } else {
                $orderCouponMdl = new OrderCouponModel();
                $orderCouponCodeList = $data['orderCouponCode'];
                // 购买的优惠券的申请退款
                $applyRet = $orderCouponMdl->couponOrderApplyRefund($orderCouponCodeList, $data['refundReason'], $data['refundRemark']);

                // 购买的优惠券的退款
                $refundRet = $orderCouponMdl->refundOrderCoupon($orderCouponCodeList);
//                if($applyRet['code'] == C('SUCCESS') && $refundRet == true) {
                if($refundRet === true) {
                    /**以下是关于退款的推送 */
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
                    $this->ajaxSucc();
                } else {
                    switch($refundRet) {
                        case C('REFUND.CAN_NOT_REFUND_APART'):
                            $msg = '当日订单不能部分退款';
                            break;
                        default:
                            $msg = '申请退款失败，请重试';
                    }
                    $this->ajaxError($msg);
                }
            }
        }
    }
	
	
	
	
	
	

    /**
     * 计算要退的各部分金额
     */
    public function calToRefundMoney() {
        $count = I('post.count', 0); // 优惠券订单编码列表
        $orderCode = I('post.orderCode'); // 订单编码
        // 获得已经退款的金额
        $consumeOrderMdl = new ConsumeOrderModel();
        $orderInfo = $consumeOrderMdl->getOrderInfo(array('orderCode' => $orderCode), array('refundAmount'));
        $payPrice = I('post.payPrice'); // 优惠券价格
        $realPay = I('post.realPay'); // 订单实际支付金额，单位：元
        $platBonus = I('post.platBonus'); // 平台红包，单位：元
        $shopBonus = I('post.shopBonus'); // 商户红包，单位：元

        $orderCouponMdl = new OrderCouponModel();
        $toRefundAmountDetails = $orderCouponMdl->calToRefundMoney($count, $payPrice * \Consts::HUNDRED, $realPay * \Consts::HUNDRED, $shopBonus * \Consts::HUNDRED, $platBonus * \Consts::HUNDRED, $orderInfo['refundAmount']);
        $this->ajaxSucc('', $toRefundAmountDetails);
    }

    /**
     * 店铺详情
     */
    public function getShopInfo() {
        $shopCode = I('get.shopCode'); // 商家编码
        $userMdl = new UserModel();
        $userInfo = $userMdl->getUserInfo(array('userCode' => I('get.userCode')), array('userCode'));
        $userCode = $userInfo ? I('get.userCode') : ''; // 用户编码
        $openId = I('get.openId'); // 用户微信ID
        $shopMdl = new ShopModel();
        // 获得商家基本信息
        $shopInfo = $shopMdl->getShopInfo($shopCode, array('Shop.shopCode', 'Shop.shopName', 'Shop.longitude', 'Shop.latitude', 'Shop.country', 'Shop.province', 'Shop.city', 'Shop.district', 'Shop.street', 'Shop.tel', 'Shop.mobileNbr', 'Shop.creditPoint',
//            'Shop.type',  //如果要使用到这个字段，请用 ShopTypeRel 查询到
            'Shop.isOrderOn', 'Shop.logoUrl', 'Shop.shortDes', 'Shop.popularity', 'repeatCustomers', 'onlinePaymentDiscount', 'onlinePaymentDiscountUpperLimit', 'businessHours', 'Shop.isCatering', 'Shop.isOuttake', 'Shop.isOpenTakeout', 'Shop.isAcceptBankCard', 'Shop.isAllowPet', 'Shop.hasWifi', 'Shop.isFreeParking', 'Shop.shopStatus', 'Shop.isOpenEat', 'Shop.isSuspended'));
        $shopInfo['isFollowed'] = C('NO');
        // 是否显示惠支付按钮
        $shopInfo['showPayBtn'] = $shopMdl->isShowPayBtn($shopCode) ? C('YES') : C('NO');
        // 是否可以支付
        $shopInfo['ifCanPay'] = $shopMdl->ifCanPay($shopInfo['isSuspended'], $shopInfo['businessHours'], $shopInfo['isAcceptBankCard'], $shopCode) ? C('YES') : C('NO');
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
            if($userCoupon){
                foreach($userCoupon as $k=>$item){
                    if(stristr($item['shopName'], '惠圈平台')){
                        unset($userCoupon[$k]);
                    }
                }
            }
        }
        if($openId){
            $shopInfo['openId'] = $openId;
            $userMdl = new UserModel();
            $userInfo = $userMdl->listUser(array('wechatId' => $openId), $this->getPager(0));
            if($userInfo){
                $userInfo = $userInfo[0];
            }
        }
        // 获得商家装修
        $shopDecorationMdl = new ShopDecorationModel();
        $shopDecoration = $shopDecorationMdl->getShopDecoration($shopCode);

        // 获得商店商品图片10张
        $productMdl = new ProductModel();
        $shopPhotoList = $productMdl->getProductList(array('shopCode' => $shopCode), array('productName', 'productImg' => 'url', 'productId'), 'createTime desc', 10);

        // 获得商家可领取的优惠券
        $batchCouponMdl = new BatchCouponModel();
        $shopCoupon = $batchCouponMdl->listAvailabelCoupon($shopCode,
            array(
                'batchCouponCode',
                'couponName',
                'couponType',
                'payPrice',
                'totalVolume',
                'remaining',
                'nbrPerPerson',
                'insteadPrice',
                'discountPercent',
                'availablePrice',
                'endTakingTime',
                'createTime',
                'couponType',
                'function',
                'remark',
                'batchNbr',
                'startUsingTime',
                'expireTime',
                'dayStartUsingTime',
                'dayEndUsingTime',
                'remark',
                'city',
                'shopName',
                'validityPeriod'
            ), $userCode);
        foreach($shopCoupon as $k=>$v) {
            unset($shopCoupon[$k]['takenCount']);
            unset($shopCoupon[$k]['takenPercent']);
        }
        // 获得商家会员卡
//        $cardMdl = new CardModel();
//        $shopCard = $cardMdl->getGeneralCardStastics($shopCode, $this->getPager(0), 1);
        // 添加用户浏览商家详情记录
        $shopInfoRecord = new UserEnterShopInfoRecordModel();
        $shopInfoRecord->addRecord($shopCode, $userCode);
        // 商家人气数量+1
        $shopMdl->increaseShopPopularity($shopCode);
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
        $systemParamMdl = new SystemParamModel();
        // 获得是否开启首单立减
        $orderFirstInfo = $systemParamMdl->getParamValue('orderFirst');
        $shopInfo['isFirst'] = $orderFirstInfo['value'];

        $coMdl = new ConsumeOrderModel();
        if($userCode){
            $userConsumeMdl = new UserConsumeModel();
            $ret = $userConsumeMdl->isFirst($userCode);
            if(!$ret){
                $shopInfo['isFirst'] = 0;
            }
        }

        if(empty($userCode)){
            if(isset($userInfo) && !empty($userInfo)){
                $userCode = $userInfo['userCode'];
            }
        }
        //获得某用户提醒商家入驻
        $record = $shopInfoRecord->getBrowseQuantity(array('actionType' => 2, 'shopCode' => $shopCode, 'userCode' => $userCode), 'recordId');
        //获得提醒商家入驻的用户数量
        $countRecord = $shopInfoRecord->getBrowseQuantity(array('actionType' => 2, 'shopCode' => $shopCode), 'distinct(userCode)');
        //获得某用户提醒商家上商品
        $remind = $shopInfoRecord->getBrowseQuantity(array('actionType' => 3, 'shopCode' => $shopCode, 'userCode' => $userCode), 'recordId');
        //获得提醒商家上商品的用户数量
        $countRemind = $shopInfoRecord->getBrowseQuantity(array('actionType' => 3, 'shopCode' => $shopCode), 'distinct(userCode)');
        if($userCode){
            // 获得用户产品订单列表
            $unPayedOrderList = $coMdl->listProductOrder(array('clientCode' => $userCode, 'ConsumeOrder.shopCode' => $shopCode, 'ConsumeOrder.status' => C('ORDER_STATUS.UNPAYED'), 'ConsumeOrder.orderStatus' => array('IN', array(C('FOOD_ORDER_STATUS.RECEIVED'), C('FOOD_ORDER_STATUS.DELIVERED'), C('FOOD_ORDER_STATUS.SERVED')))), $this->getPager(0));
        }

        // 获得手机的操作系统
        $operationType = I('get.operationType');
        if(in_array($_SERVER['HTTP_HOST'], array('api.huiquan.suanzi.cn', 'web.huiquan.suanzi.cn', 'huiquan.suanzi.cn'))) {
            $isAppear = 1;
        }else{
            $isAppear = 0;
        }
        $assign = array(
            'title' => '店铺详情',
            'userCode' => $userCode,
            'shopInfo' => $shopInfo,
            'couponList' => array('shopCoupon' => $shopCoupon, 'userCoupon' => $userCoupon),
//            'shopCard' => $shopCard,
            'shopDecoration' => $shopDecoration,
            'shopPhotoList' => $shopPhotoList,
            'userInfo' => isset($userInfo) ? $userInfo : array(),
            'unPayedOrderList' => isset($unPayedOrderList) && $unPayedOrderList ? $unPayedOrderList : array(),
            'actList' => $actList,
            'record' => $record,
            'countRecord' => $countRecord,
            'operationType' => $operationType, // 获得手机的操作系统
            'isAppear' => $isAppear, //根据环境判断是否出现纠错
            'remind' => $remind,
            'countRemind' => $countRemind,
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 用户关注商家
     */
    public function followShop() {
        $userCode = I('post.userCode');
        $shopCode = I('post.shopCode');
        $shopFollowingMdl = new ShopFollowingModel();
        $ret = $shopFollowingMdl->followShop($userCode, $shopCode);
        if ($ret['code'] == C('SUCCESS')) {
            $this->ajaxSucc('操作成功!');
        } else {
            $this->ajaxError('', $ret);
        }
    }

    /**
     * 取消关注商家
     */
    public function cancelFollowShop() {
        $userCode = I('post.userCode');
        $shopCode = I('post.shopCode');
        $shopFollowingMdl = new ShopFollowingModel();
        $ret = $shopFollowingMdl->cancelFollowShop($userCode, $shopCode);
        if ($ret['code'] == C('SUCCESS')) {
            $this->ajaxSucc('操作成功!');
        } else {
            $this->ajaxError('', $ret);
        }
    }

    public function myCouponInfo() {
        $userCode = I('post.userCode');
        $batchCouponCode = I('post.batchCouponCode');
        $userCouponMdl = new UserCouponModel();
        $myReceivedCount = $userCouponMdl->countUserCoupon(array('UserCoupon.userCode' => $userCode, 'UserCoupon.batchCouponCode' => $batchCouponCode, 'UserCoupon.status' => C('USER_COUPON_STATUS.ACTIVE')));
        $userCoupon = $userCouponMdl->listUserCoupon(array('UserCoupon.userCode' => $userCode, 'UserCoupon.batchCouponCode' => $batchCouponCode, 'UserCoupon.status' => C('USER_COUPON_STATUS.ACTIVE')), $this->getPager(0), array('UserCoupon.userCouponCode', 'UserCoupon.userCouponNbr'));
        $res = array(
            'userCount' => $myReceivedCount,
            'userCoupon' => $userCoupon
        );
        $this->ajaxReturn($res);
    }

    /**
     * 抢优惠券
     */
    public function grabCoupon(){
        $userCode = I('post.userCode');
        $batchCouponCode = I('post.batchCouponCode');
        $batchCouponMdl = new BatchCouponModel();
        $batchCouponInfo = $batchCouponMdl->getCouponInfo($batchCouponCode);
        if(!$batchCouponInfo['batchCouponCode']) {
            $this->ajaxError('', array('code' => C('COUPON.NOT_EXIST')));
        }
        $shopCode = $batchCouponInfo['shopCode'];
        // 判断用户是否拥有商家的会员卡。若没有，则添加
        $userCardMdl = new UserCardModel();
        $checkUserCardRet = $userCardMdl->checkUserCard($userCode, $shopCode);
//        if(!$checkUserCardRet) {
//            $this->ajaxError(array('code' => C('CARD.ADD_USER_CARD_FAIL')));
//        }
        $userCouponMdl = new UserCouponModel();
        $ret = $userCouponMdl->grabCoupon($batchCouponCode, $userCode, 0);
        if ($ret['code'] == C('SUCCESS')) {
            $shopMdl = new ShopModel();
            // 获得商家信息
            $shop = $shopMdl->getShopInfo($batchCouponInfo['shopCode'], array('Shop.*'));
            $shop['city'] = mb_substr($shop['city'], 0, mb_strlen($shop['city'] - 1), "utf-8");

            $res = $userCouponMdl->getCouponInfo($ret['userCouponCode']);
            $res['startUsingTime'] = $this->formatDate1($res['startUsingTime']);
            $res['expireTime'] = $this->formatDate1($res['expireTime']);

            $res['dayStartUsingTime'] = substr($res['dayStartUsingTime'], 0, 5);
            $res['dayEndUsingTime'] = substr($res['dayEndUsingTime'], 0, 5);

            $res['availablePrice'] = $res['availablePrice'] / C('RATIO');
            $res['insteadPrice'] = $res['insteadPrice'] / C('RATIO');
            $res['discountPercent'] = $res['discountPercent'] / 10;

            if(in_array($res['couponType'], array(3,4,7,8))){ // 抵扣券，折扣券，兑换券，代金券
                $s1 = array(
                    'shopCode'=>$shop['shopCode'],
                    'shopName'=>$shop['shopName'],
                    'logoUrl'=>$shop['logoUrl'],
                    'couponType'=>$res['couponType'],
                    'batchCouponCode' => $res['batchCouponCode']
                );
            }else{
                $s1 = array(
                    'userCouponCode'=>$res['userCouponCode'],
                    'couponType'=>$res['couponType']
                );
            }
            $res['use'] = json_encode($s1);

            $s2 = array(
                'city' => $shop['city'],
                'couponType' => $res['couponType'],
                'availablePrice' => $res['availablePrice'],
                'insteadPrice' => $res['insteadPrice'],
                'discountPercent' => $res['discountPercent'],
                'function' => $res['function'],
                'shopName' => $shop['shopName'],
                'batchCouponCode' => $res['batchCouponCode']
            );
            $res['share'] = json_encode($s2);

            $res['canGrab'] = 1;
            $myReceivedCount = $userCouponMdl->countMyReceivedCoupon($batchCouponCode, $userCode);
            $res['userCount'] = $myReceivedCount;
            if($myReceivedCount >= $batchCouponInfo['nbrPerPerson']){
                $res['canGrab'] = 0;
            }
            $this->ajaxSucc('操作成功!', $res);
        } else {
            $this->ajaxError('', $ret);
        }
    }

    /**
     * 配送地址列表
     */
    public function getAddressList() {
        $userCode = I('get.userCode');
        $orderCode = I('get.orderCode');
        $userAddressId = I('get.userAddressId');
        $userAddressMdl = new UserAddressModel();
        $addressList = $userAddressMdl->getUserAddressList($userCode);
        $consumeOrderMdl = new ConsumeOrderModel();
        $orderInfo = $consumeOrderMdl->getOrderInfo(array('orderCode'=>$orderCode));
        $shopMdl = new ShopModel();
        // 获得商家信息
        $shopInfo = $shopMdl->getShopInfo($orderInfo['shopCode'], array('longitude', 'latitude', 'deliveryDistance'));
        $assign = array(
            'title' => '外卖配送地址',
            'userCode' => $userCode,
            'orderCode' => $orderCode,
            'userAddressId' => $userAddressId,
            'addressList' => $addressList,
            'shopInfo' => $shopInfo
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 编辑配送地址
     */
    public function editAddress(){
        $userAddressMdl = new UserAddressModel();
        $districtMdl = new DistrictModel();
        if (! IS_AJAX) {
            $data = I('get.');
            $userAddressInfo = $userAddressMdl->getUserAddressInfo($data['userAddressId']);
            if($userAddressInfo) {
                $city = $userAddressInfo['city'];
            } else {
                $city = $data['city'];
                $userAddressInfo['province'] = $districtMdl->getProvinceByCity($city);
                $userAddressInfo['city'] = $city;
            }

            $cityInfo = $districtMdl->getCityInfo(array('name' => array('LIKE', "%$city%")), array('*'));
            $districtList = $districtMdl->listDistrict($cityInfo['id']);

            $consumeOrderMdl = new ConsumeOrderModel();
            $orderInfo = $consumeOrderMdl->getOrderInfo(array('orderCode'=>$data['orderCode']), array('shopCode'));

            $shopMdl = new ShopModel();
            // 获得商家信息
            $shopInfo = $shopMdl->getShopInfo($orderInfo['shopCode'], array('longitude', 'latitude', 'deliveryDistance'));
            $assign = array(
                'title' => '编辑配送地址',
                'userCode' => $data['userCode'],
                'orderCode' => $data['orderCode'],
                'addressId' => $data['addressId'],
                'shopInfo' => $shopInfo,
                'addressInfo' => $userAddressInfo,
                'districtList' => $districtList
            );
            $this->assign($assign);
            $this->display();
        }else{
            $data = I('post.');
            $data['street'] = trim($data['street']);
            if(stristr($data['street'], $data['city']) !== false){
                $start = stripos($data['street'], $data['city']) + strlen($data['city']);
                $data['street'] = substr($data['street'], $start);
            }
            if(stristr($data['street'], $data['district']) !== false){
                $start = stripos($data['street'], $data['district']) + strlen($data['district']);
                $data['street'] = substr($data['street'], $start);
            }

//            $this->ajaxError('', $data);
            $ret = $userAddressMdl->editUserAddress($data);
            $ret['longitude'] = $data['longitude'];
            $ret['latitude'] = $data['latitude'];
            if ($ret['code'] == C('SUCCESS')) {
                $this->ajaxSucc('操作成功!', $ret);
            } else {
                $this->ajaxError('', $ret);
            }
        }
    }

    public function delAddress(){
        $userAddressId = I('post.userAddressId');
        $userAddressMdl = new UserAddressModel();
        $ret = $userAddressMdl->delUserAddress($userAddressId);
        if ($ret['code'] == C('SUCCESS')) {
            $this->ajaxSucc('操作成功!');
        } else {
            $this->ajaxError('', $ret);
        }
    }

    public function pay(){
        if(IS_GET) {
            $orderCode = I('get.orderCode');
            $consumeOrderMdl = new ConsumeOrderModel();
            $orderInfo = $consumeOrderMdl->getOrderInfo(array('orderCode'=>$orderCode));
            $orderInfo['orderAmount'] = $orderInfo['orderAmount'] / C('RATIO');

            $userCode = $orderInfo['clientCode'];
            $shopCode = $orderInfo['shopCode'];
            $orderAmount = $orderInfo['orderAmount'];

            $bsMdl = new BonusStatisticsModel();
            $shopBonus = $bsMdl->userTotalBonusValue($orderInfo['clientCode'], $orderInfo['shopCode']);
            $platBonus = $bsMdl->userTotalBonusValue($orderInfo['clientCode'], C('HQ_CODE'));

            $userCouponMdl = new UserCouponModel();
            $userCouponList = $userCouponMdl->listUserCouponWhenPay($userCode, $shopCode, $orderAmount);
            $platCouponList = $userCouponMdl->listPlatCouponWhenPay($userCode, $shopCode, $orderAmount);
            $userCouponList = array_merge_recursive($userCouponList, $platCouponList);
            foreach($userCouponList as &$userCoupon) {
                if($userCoupon['couponType'] == C('COUPON_TYPE.REDUCTION')) {
                    $userCoupon['couponDeduction'] = $userCoupon['insteadPrice'];
                } else {
                    $userCoupon['couponDeduction'] = $orderInfo['orderAmount'] * (1 - $userCoupon['discountPercent'] / C('DISCOUNT_RATIO'));
                }
            }

            $shopMdl = new ShopModel();
            // 获得商家信息
            $shopInfo = $shopMdl->getShopInfo($shopCode, array('shopName'));
            $assign = array(
                'title' => '支付',
                'orderInfo' => $orderInfo,
                'shopBonus' => $shopBonus,
                'platBonus' => $platBonus,
                'userCouponList' => $userCouponList,
                'shopInfo' => $shopInfo
            );
            $this->assign($assign);
            $this->display();
        } else {
            $data = I('post.');
            $orderCode = $data['orderCode'];
            $userConsumeMdl = new UserConsumeModel();
            $ret = $userConsumeMdl->bankcardPay($orderCode, $data['userCouponCode'], $data['platBonus'], $data['shopBonus']);
            if($ret['code'] == C('SUCCESS')) {

            } else {

            }
        }
    }

    /**
     * 订单详情页
     */
    public function unReceiveOrder(){
        $orderCode = I('get.orderCode');
        $openId = I('get.openId');
        $consumeOrderMdl = new ConsumeOrderModel();
        $orderInfo = $consumeOrderMdl->getOrderInfo(array('orderCode'=>$orderCode));
        $temp = array('orderAmount', 'actualOrderAmount');
        foreach($temp as $v) {
            $orderInfo[$v] = $orderInfo[$v] / \Consts::HUNDRED;
        }

        $orderInfo['receiveTime'] = date('H:i', strtotime($orderInfo['orderTime'])+10*60);
        $orderInfo['rTime'] = strtotime($orderInfo['receivedTime']);
        $orderInfo['cTime'] = strtotime($orderInfo['cancelTime']);

        $orderInfo['bTime']['y'] = date('Y', strtotime($orderInfo['receivedTime']) + 3.5 * 60 * 60);
        $orderInfo['bTime']['m'] = date('m', strtotime($orderInfo['receivedTime']) + 3.5 * 60 * 60);
        $orderInfo['bTime']['d'] = date('d', strtotime($orderInfo['receivedTime']) + 3.5 * 60 * 60);
        $orderInfo['bTime']['h'] = date('H', strtotime($orderInfo['receivedTime']) + 3.5 * 60 * 60);
        $orderInfo['bTime']['i'] = date('i', strtotime($orderInfo['receivedTime']) + 3.5 * 60 * 60);
        $orderInfo['bTime']['s'] = date('s', strtotime($orderInfo['receivedTime']) + 3.5 * 60 * 60);

        $temp = array('receivedTime', 'arrivalTime', 'orderTime', 'cancelTime');
        $today = date('m-d');
        foreach($temp as $v) {
            if($today == substr($orderInfo['orderTime'], 5, 5)) {
                $orderInfo[$v] = '今天' . date('H:i', strtotime($orderInfo[$v]));
            } else {
                $orderInfo[$v] = date('m-d H:i', strtotime($orderInfo[$v]));
            }
        }

        $shopMdl = new ShopModel();
        // 获得商家信息
        $shopInfo = $shopMdl->getShopInfo($orderInfo['shopCode'], array('Shop.shopName', 'Shop.tel', 'Shop.deliveryFee', 'Shop.eatPayType', 'Shop.shopCode', 'Shop.logoUrl'));
        $shopInfo['deliveryFee'] = $shopInfo['deliveryFee'] / C('RATIO');
        if($openId){
            $shopInfo['openId'] = $openId;
        }

        $userMdl = new UserModel();
        $userInfo = $userMdl->getUserInfo(array('userCode' => $orderInfo['clientCode']), array('nickName', 'mobileNbr'));

        $orderProductMdl = new OrderProductModel();
        $orderProductList = $orderProductMdl->getProductListByOrder($orderCode);
        foreach ($orderProductList as $k => $product){
            $orderProductList[$k]['productTotalPrice'] = $product['productNbr']*$product['productUnitPrice'];
        }

        $orderProductCount = 0;
        foreach($orderProductList as $product) {
            $orderProductCount += $product['productNbr'];
        }

        $userConsumeMdl = new UserConsumeModel();
        $consumeInfo = $userConsumeMdl->getPayedConsumeInfoByOrderCode($orderCode);
        $tempb = array('realPay', 'deduction', 'shopBonus', 'platBonus', 'bankCardDeduction', 'cardDeduction', 'couponDeduction');
        foreach($tempb as $v) {
            $consumeInfo[$v] = number_format($consumeInfo[$v] / C('RATIO'), '2', '.', '');
        }
        $title = '订单详情';
        switch($orderInfo['orderStatus']) {
            case '20':
                switch($orderInfo['status']) {
                    case '0':
                        $title = '待接单';
                        break;
                    case '1':
                        $title = '待支付';
                        break;
                    case '2':
                        $title = '待支付';
                        break;
                    case '3':
                        $title = '待接单';
                        break;
                    case '4':
                        $title = '待支付';
                        break;
                    case '5':
                        $title = '待支付';
                        break;
                    case '6':
                        $title = '退款中';
                        break;
                    case '7':
                        $title = '已退款';
                        break;
                }
                break;
            case '21':
                switch($orderInfo['status']) {
                    case '0':
                        $title = '待商家结算';
                        break;
                    case '1':
                        $title = '待支付';
                        break;
                    case '2':
                        $title = '待支付';
                        break;
                    case '3':
                        $title = '派送中';
                        break;
                    case '4':
                        $title = '待支付';
                        break;
                    case '5':
                        $title = '待支付';
                        break;
                    case '6':
                        $title = '退款中';
                        break;
                    case '7':
                        $title = '已退款';
                        break;
                }
                break;
            case '22':
                switch($orderInfo['status']) {
                    case '0':
                        $title = '待商家结算';
                        break;
                    case '1':
                        $title = '待支付';
                        break;
                    case '2':
                        $title = '待支付';
                        break;
                    case '3':
                        $title = '派送中';
                        break;
                    case '4':
                        $title = '待支付';
                        break;
                    case '5':
                        $title = '待支付';
                        break;
                    case '6':
                        $title = '退款中';
                        break;
                    case '7':
                        $title = '已退款';
                        break;
                }
                break;
            case '23':
                switch($orderInfo['status']) {
                    case '0':
                        $title = '待商家结算';
                        break;
                    case '1':
                        $title = '待支付';
                        break;
                    case '2':
                        $title = '待支付';
                        break;
                    case '3':
                        $title = '交易完成';
                        break;
                    case '4':
                        $title = '待支付';
                        break;
                    case '5':
                        $title = '待支付';
                        break;
                    case '6':
                        $title = '退款中';
                        break;
                    case '7':
                        $title = '已退款';
                        break;
                }
                break;
            case '24':
                $title = '已撤销';
                break;
            case '25':
                $title = '待下单';
                break;
        }

//        if($orderInfo['status'] == C('ORDER_STATUS.CANCELED')){
//            $title = '订单取消';
//        }elseif($orderInfo['status'] == C('ORDER_STATUS.REFUNDING')){
//            $title = '退款中';
//        }elseif($orderInfo['status'] == C('ORDER_STATUS.REFUNDED')){
//            $title = '退款成功';
//        }else{
//            if($orderInfo['orderStatus'] == C('FOOD_ORDER_STATUS.UNORDERED')){
//                $title = '待支付';
//            }elseif($orderInfo['orderStatus'] == C('FOOD_ORDER_STATUS.ORDERED')){
//                $title = '待接单';
//            }elseif($orderInfo['orderStatus'] > C('FOOD_ORDER_STATUS.ORDERED') && $orderInfo['orderStatus'] < C('FOOD_ORDER_STATUS.SERVED')){
//                if($orderInfo['orderType'] == C('ORDER_TYPE.NO_TAKE_OUT')){
//                    $title = '待收货';
//                }else{
//                    $title = '正在配送中';
//                }
//            }elseif($orderInfo['orderStatus'] == C('FOOD_ORDER_STATUS.SERVED')){
//                if($orderInfo['eatPayType'] == C('EAT_PAY_TYPE.AFTER')){
//                    $title = '待买单';
//                }else{
//                    $title = '订单完成';
//                }
//            }else{
//                $title = '订单详情';
//            }
//        }

        $assign = array(
            'title' => $title,
            'orderInfo' => $orderInfo,
            'userInfo' => $userInfo,
            'shopInfo' => $shopInfo,
            'serviceTel'=> '400-04-95588',
            'orderProductList' => $orderProductList,
            'orderProductCount' => $orderProductCount,
            'consumeInfo' => $consumeInfo,
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 大米活动下载页
     */
    public function riceDownload() {
        $userCode = I('get.userCode');
        $activityCode = I('get.activityCode');

        $userMdl = new UserModel();
        $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('inviteCode'));

        $activityMdl = new ActivityModel();
        $activityInfo = $activityMdl->getActInfo(array('activityCode' => $activityCode), array('Activity.*', 'Shop.shopName'), array(array('joinTable' => 'Shop', 'joinCondition' => 'Shop.shopCode = Activity.shopCode', 'joinType' => 'INNER')));

        $clientAppLogMdl = new ClientAppLogModel();
        $cAppInfo = $clientAppLogMdl->getNewestClientAppVersion();
        $assign = array(
            'title' => '活动分享页',
            'userInfo' => $userInfo,
            'activityInfo' => $activityInfo,
            'cDownloadUrl' => $cAppInfo['updateUrl'],
        );
        $this->assign($assign);
        $this->display();
    }
    /**
     * 游戏开始页
     */
    public function gameStart(){
        $activityCode = I('get.activityCode');
        $userCode = I('get.userCode');
        if($activityCode == '8afaf86d-483f-9434-2e0f-325e6efe6ea6'){
            $url = "/Api/Browser/riceActivity?activityCode=".$activityCode."&userCode=".$userCode;
            echo "<script language=\"javascript\">";
            echo "location.href=\"$url\"";
            echo "</script>";
            exit;
        }
        if(empty($activityCode)){
            $activityCode = 'f7711625-0a1c-a245-3add-3dc6202d65f4';
        }
        if(empty($userCode) || $userCode == '(null)'){
            $userCode = '';
        }
        $actMdl = new ActivityModel();
        $activityInfo = $actMdl->getActInfo(array('activityCode' => $activityCode), array('Activity.*', 'Shop.shopName'), array(array('joinTable' => 'Shop', 'joinCondition' => 'Shop.shopCode = Activity.shopCode', 'joinType' => 'INNER')));

        $ugMdl = new UserGiftModel();
        $oglMdl = new OpenGiftLogModel();
        $spMdl = new SystemParamModel();
        $sp = $spMdl->getParamValue('grabGiftNbr');
        $grabCount = $ugMdl->countUserGift(array('activityCode' => $activityCode, 'grabTime' => array('EGT', strtotime($activityInfo['startTime']))));
        $openCount = $oglMdl->countOpenLog(array('activityCode' => $activityCode, 'grabTime' => array('EGT', strtotime($activityInfo['startTime']))));

//        if($sp['value'] < ($grabCount + $openCount)){
//            $count = $grabCount + $openCount;
//        }else{
//            $count = $sp['value'];
//        }
        $count = (int)$sp['value'] + (int)$grabCount + (int)$openCount;


        $activityInfo['bTime'] = array(
            'y' => date('Y', strtotime($activityInfo['startTime'])),
            'm' => date('m', strtotime($activityInfo['startTime'])),
            'd' => date('d', strtotime($activityInfo['startTime'])),
            'h' => date('H', strtotime($activityInfo['startTime'])),
            'i' => date('i', strtotime($activityInfo['startTime'])),
            's' => date('s', strtotime($activityInfo['startTime'])),
        );
//        var_dump($activityInfo);
        $userGiftInfo = $ugMdl->listUserGift('', array('userCode' => $userCode, 'activityCode'=> $activityCode), $this->getPager(0));
        if(empty($userGiftInfo)){
            $userGiftInfo['id'] = 0;
        }else{
            $userGiftInfo = $userGiftInfo[0];
        }
//        var_dump($userGiftInfo);

        $sp = $spMdl->getParamValue('demandGiftNbr');
        $assign = array(
            'title' => '礼盒大作战',
            'activityInfo' => $activityInfo,
            'userCode' => $userCode,
            'userGiftInfo' => $userGiftInfo,
            'count' => $count,
            'demandGiftNbr' => $sp['value']
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 游戏页
     */
    public function game(){
        $userCode = I('get.userCode');
        $activityCode = I('get.activityCode');
        if (! IS_AJAX) {
            $actMdl = new ActivityModel();
            $activityInfo = $actMdl->getActInfo(array('activityCode' => $activityCode), array('Activity.*', 'Shop.shopName'), array(array('joinTable' => 'Shop', 'joinCondition' => 'Shop.shopCode = Activity.shopCode', 'joinType' => 'INNER')));
            $spMdl = new SystemParamModel();
            $prMdl = new PrizeRuleModel();
            $prizeRule = $prMdl->getPrizeRule('', array('startDay' => array('ELT', date('Y-m-d', time())), 'endDay' => array('EGT', date('Y-m-d', time()))));

            $ugMdl = new UserGiftModel();
            $oglMdl = new OpenGiftLogModel();
            $sp = $spMdl->getParamValue('grabGiftNbr');
            $grabCount = $ugMdl->countUserGift(array('activityCode' => $activityCode, 'grabTime' => array('EGT', strtotime($activityInfo['startTime']))));
            $openCount = $oglMdl->countOpenLog(array('activityCode' => $activityCode, 'grabTime' => array('EGT', strtotime($activityInfo['startTime']))));
//            if($sp['value'] < ($grabCount + $openCount)){
//                $count = $grabCount + $openCount;
//            }else{
//                $count = $sp['value'];
//            }
            $count = (int)$sp['value'] + (int)$grabCount + (int)$openCount;

            $sp = $spMdl->getParamValue('demandGiftNbr');

            $userGiftInfo = $ugMdl->listUserGift('', array('currentDay' => date('Y-m-d', time()), 'userCode' => $userCode, 'activityCode'=> $activityCode), $this->getPager(0));
            if(empty($userGiftInfo)){
                $userGiftInfo['id'] = 0;
            }else{
                $userGiftInfo = $userGiftInfo[0];
            }
            $assign = array(
                'title' => '礼盒大作战',
                'userCode' => $userCode,
                'userGiftInfo' => $userGiftInfo,
                'activityCode' => $activityCode,
                'demandNbr' => $sp['value'],
                'prizeRule' => $prizeRule,
                'count' => $count
            );

            $this->assign($assign);
            $this->display();
        }else{
            $data = I('post.');
            unset($data['isAjax']);
            $ugMdl = new UserGiftModel();
            $ret = $ugMdl->editUserGift($data);
            if ($ret['code'] == C('SUCCESS')) {
                $this->ajaxSucc('操作成功!', $ret);
            } else {
                $this->ajaxError('', $ret);
            }
        }
    }

    /**
     * 拆礼盒页
     */
    public function openBox(){
        $userGiftId = I('get.userGiftId');
        if (! IS_AJAX) {
            $ugMdl = new UserGiftModel();
            $userGiftInfo = $ugMdl->getUserGiftInfo($userGiftId);

            $assign = array(
                'title' => '快来帮TA拆礼盒，一起赢大奖！',
                'userGiftInfo' => $userGiftInfo,
            );
            $this->assign($assign);
            $this->display();
        }else{
            $data = I('post.');
            $userMdl = new UserModel();
            if($data['type'] == 1){
                $isNewUser = 0;
                $ret = $userMdl->getUserInfo(array('mobileNbr' => $data['mobileNbr']));
                if($ret){
                    $ret['code'] = C('SUCCESS');
                }else{
                    $ret['code'] = C('UPDATE_INFO.NOT_EXIST');
                }
            }else{
                $isNewUser = 1;
                if(empty($data['password'])){
                    $data['password'] = '123456';
                }
                $data['password'] = md5($data['password']);
                $ret = $userMdl->register($data['mobileNbr'], $data['validateCode'], $data['password'], '', '');
            }
            if ($ret['code'] == C('SUCCESS') || $ret['code'] == C('MOBILE_NBR.REPEAT')) {
                $oglMdl = new OpenGiftLogModel();
                $da = array(
                    'userGiftId' => $data['userGiftId'],
                    'userCode' => $ret['userCode'],
                    'openTime' => time(),
                    'isNewUser' => $isNewUser,
                );
                $res = $oglMdl->addOpenLog($da);
                if($res['code'] == C('SUCCESS')){
                    $this->ajaxSucc('操作成功!', $res);
                }else{
                    $this->ajaxError('', $res);
                }
            } else {
                $this->ajaxError('', $ret);
            }
        }
    }


    public function register(){
        if (! IS_AJAX) {
            $assign = array(
                'title' => '新用户注册',
            );
            $this->assign($assign);
            $this->display();
        }else{
            $data = I('post.');
            $userMdl = new UserModel();
            if(empty($data['password'])){
                $data['password'] = '123456';
            }
            $data['password'] = md5($data['password']);
            $ret = $userMdl->register($data['mobileNbr'], $data['validateCode'], $data['password'], '', '');
            if($ret['code'] == C('SUCCESS')) {
                $this->ajaxSucc('操作成功!', $ret);
            } else {
                $this->ajaxError('', $ret);
            }
        }
    }

    public function registerResult(){
        $assign = array(
            'title' => '注册成功！',
        );
        $this->assign($assign);
        $this->display();
    }

    public function getOpenResult(){
        $number = I('get.number');
        $activityCode = I('get.activityCode');
        $assign = array(
            'title' => '礼盒大作战，等你来挑战！',
            'number' => $number,
            'activityCode' => $activityCode
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 游戏详情页
     */
    public function gameInfo(){
        $activityCode = I('get.activityCode');
        $type = I('get.type');
        $actMdl = new ActivityModel();
        $activityInfo = $actMdl->getActInfo(array('activityCode' => $activityCode), array('Activity.*', 'Shop.shopName'), array(array('joinTable' => 'Shop', 'joinCondition' => 'Shop.shopCode = Activity.shopCode', 'joinType' => 'INNER')));

        $ugMdl = new UserGiftModel();
        $oglMdl = new OpenGiftLogModel();
        $spMdl = new SystemParamModel();
        $sp = $spMdl->getParamValue('grabGiftNbr');
        $grabCount = $ugMdl->countUserGift(array('activityCode' => $activityCode, 'grabTime' => array('EGT', strtotime($activityInfo['startTime']))));
        $openCount = $oglMdl->countOpenLog(array('activityCode' => $activityCode, 'grabTime' => array('EGT', strtotime($activityInfo['startTime']))));

//        if($sp['value'] < ($grabCount + $openCount)){
//            $count = $grabCount + $openCount;
//        }else{
//            $count = $sp['value'];
//        }
        $count = (int)$sp['value'] + (int)$grabCount + (int)$openCount;

        $assign = array(
            'title' => '礼盒大作战',
            'type' => $type,
            'activityInfo' => $activityInfo,
            'count' => $count
        );
        $this->assign($assign);
        $this->display();
    }

    public function getOpenLog(){
        $userGiftId = I('get.userGiftId');
        $ugMdl = new UserGiftModel();
        $userGiftInfo = $ugMdl->getUserGiftInfo($userGiftId);
        $actMdl = new ActivityModel();
        $activityInfo = $actMdl->getActInfo(array('activityCode' => $userGiftInfo['activityCode']), array('Activity.*', 'Shop.shopName'), array(array('joinTable' => 'Shop', 'joinCondition' => 'Shop.shopCode = Activity.shopCode', 'joinType' => 'INNER')));
        $oglMdl = new OpenGiftLogModel();
        $spMdl = new SystemParamModel();
        $sp = $spMdl->getParamValue('grabGiftNbr');
        $grabCount = $ugMdl->countUserGift(array('activityCode' => $userGiftInfo['activityCode'], 'grabTime' => array('EGT', strtotime($activityInfo['startTime']))));
        $openCount = $oglMdl->countOpenLog(array('activityCode' => $userGiftInfo['activityCode'], 'grabTime' => array('EGT', strtotime($activityInfo['startTime']))));
//        if($sp['value'] < ($grabCount + $openCount)){
//            $count = $grabCount + $openCount;
//        }else{
//            $count = $sp['value'];
//        }
        $count = (int)$sp['value'] + (int)$grabCount + (int)$openCount;

        $prMdl = new PrizeRuleModel();
        $prizeRule = $prMdl->getPrizeRule('', array('id' => $userGiftInfo['prizeRuleId']));
        if($prizeRule['prizeType'] == C('PRIZE_TYPE.COUPON')){
            $batchCouponMdl = new BatchCouponModel();
            $giftInfo = $batchCouponMdl->getCouponBasicInfo(array('batchCouponCode' => $prizeRule['giftCode']), array('function'));
            $prizeRule['function'] = $giftInfo['function'] ? ' ('.$giftInfo['function'].') ' : '';
        }

        $userGiftInfo['timeDown'] = 0;
        $userGiftInfo['bTime'] = array(
            'y' => date('Y', $userGiftInfo['grabTime'] + 3600 * $prizeRule['limitTime']),
            'm' => date('m', $userGiftInfo['grabTime'] + 3600 * $prizeRule['limitTime']),
            'd' => date('d', $userGiftInfo['grabTime'] + 3600 * $prizeRule['limitTime']),
            'h' => date('H', $userGiftInfo['grabTime'] + 3600 * $prizeRule['limitTime']),
            'i' => date('i', $userGiftInfo['grabTime'] + 3600 * $prizeRule['limitTime']),
            's' => date('s', $userGiftInfo['grabTime'] + 3600 * $prizeRule['limitTime']),
        );

        if(time() <= $userGiftInfo['grabTime'] + 3600 * $prizeRule['limitTime'] && time() >= $userGiftInfo['grabTime']){
            $userGiftInfo['timeDown'] = 1;
        }


        $openLog = $oglMdl->listOpenLog(array('OpenGiftLog.*', 'User.nickName', 'User.realName', 'User.mobileNbr'), array('userGiftId' => $userGiftId), $this->getPager(0));

        $param = array(
            'icon'=>"http://web.huiquan.suanzi.cn/Public/img/game/game_icon.png",
            'link'=>"http://".$_SERVER['HTTP_HOST']."/Api/Browser/gameShare?activityCode=".$userGiftInfo['activityCode'],
            'title'=>"我正在疯抢惠圈免费大餐，目前已有".$count."人参与此活动，你也来玩儿玩儿吧！",
            'content'=>"礼盒大作战，等你来挑战！"
        );

        $sp = $spMdl->getParamValue('demandGiftNbr');
        if($userGiftInfo['grabNbr'] > $sp['value']){
            $param['link'] = "http://".$_SERVER['HTTP_HOST']."/Api/Browser/gameShare?userGiftId=".$userGiftInfo['id'];
            $param['title'] = '快来帮我拆礼盒，跟我一起免费吃吃吃！';
            $param['content'] = '48小时内拆完就能吃0元大餐啦！';
        }

        $param = json_encode($param);
        $assign = array(
            'title' => '礼盒大作战',
            'userGiftInfo' => $userGiftInfo,
            'openLog' => $openLog,
//            'count' => $count,
            'prizeRule' => $prizeRule,
            'param' => $param
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 获取短信验证码
     */
    public function getValCode(){
        $mobileNbr= I('post.mobileNbr');
        $action = I('post.action');
        if($action != 1){
            $userMdl = new UserModel();
            $userInfo = $userMdl->getUserInfo(array('mobileNbr' => $mobileNbr));
            if($userInfo){
                $this->ajaxError('', array('code' => C('API_INTERNAL_EXCEPTION')));
            }
        }

        $commC = new CommController();
        $ret = $commC->getValCode($mobileNbr);
        if ($ret['code'] == C('SUCCESS')) {
            $this->ajaxSucc('操作成功!', $ret);
        } else {
            $this->ajaxError('', $ret);
        }
    }

    public function wechatRegister(){
        $mobileNbr = I('post.mobileNbr');
        $wechatId = I('post.wechatId');
        $validateCode = I('post.validateCode');

        $smsMdl = new SmsModel();
        $codeCom = $smsMdl->getCode('r' . $mobileNbr);

        if($validateCode != $codeCom) {
            $this->ajaxError('', array('code' => C('VALIDATE_CODE.ERROR')));
        }

        $userMdl = new UserModel();
        $userInfo = $userMdl->getUserInfo(array('mobileNbr' => $mobileNbr));
        if($userInfo){
            $ret = $userMdl->editUser(array('userCode' => $userInfo['userCode'], 'wechatId' => $wechatId));
            if($ret === C('SUCCESS')){
                $this->ajaxSucc('操作成功!');
            }else{
                $this->ajaxError('', array('code' => C('API_INTERNAL_EXCEPTION')));
            }
        }else{
            $addNotRegisteredUserRet = $userMdl->addNotRegisteredUser(array('userId' => $mobileNbr, 'mobileNbr' => $mobileNbr, 'status' => C('USER_STATUS.NOT_REG'), 'wechatId' => $wechatId));
            if($addNotRegisteredUserRet['ret'] == true){
                $this->ajaxSucc('操作成功!');
            }else{
                $this->ajaxError('', array('code' => C('API_INTERNAL_EXCEPTION')));
            }
        }

    }

    /**
     * 游戏分享页
     */
    public function gameShare(){
        $data = I('get.');
        $ugMdl = new UserGiftModel();

        $activityCode = $data['activityCode'];
        if(isset($data['userGiftId']) && !empty($data['userGiftId'])){
            $userGiftInfo = $ugMdl->getUserGiftInfo($data['userGiftId']);
            if(empty($activityCode)){
                $activityCode = $userGiftInfo['activityCode'];
            }
//            $oglMdl = new OpenGiftLogModel();
//            $userGiftInfo['count'] = $oglMdl->countOpenLog(array('userGiftId' => $data['userGiftId']));
        }
        $actMdl = new ActivityModel();
        $activityInfo = $actMdl->getActInfo(array('activityCode' => $activityCode), array('Activity.*', 'Shop.shopName'), array(array('joinTable' => 'Shop', 'joinCondition' => 'Shop.shopCode = Activity.shopCode', 'joinType' => 'INNER')));
        $activityInfo['max'] = $ugMdl->maxUserGift($activityCode);

        $oglMdl = new OpenGiftLogModel();
        $spMdl = new SystemParamModel();
        $sp = $spMdl->getParamValue('grabGiftNbr');
        $grabCount = $ugMdl->countUserGift(array('activityCode' => $activityCode, 'grabTime' => array('EGT', strtotime($activityInfo['startTime']))));
        $openCount = $oglMdl->countOpenLog(array('activityCode' => $activityCode, 'grabTime' => array('EGT', strtotime($activityInfo['startTime']))));

//        if($sp['value'] < ($grabCount + $openCount)){
//            $count = $grabCount + $openCount;
//        }else{
//            $count = $sp['value'];
//        }
        $count = (int)$sp['value'] + (int)$grabCount + (int)$openCount;

        $assign = array(
            'title' => '礼盒大作战，等你来挑战！',
            'activityCode' => $activityCode,
            'userGiftInfo' => isset($userGiftInfo) ? $userGiftInfo : array(),
            'activityInfo' => isset($activityInfo) ? $activityInfo : array(),
            'count' => $count
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 大米活动页
     */
    public function riceActivity(){
        $activityCode = I('get.activityCode');
        $userCode = I('get.userCode');
        if(empty($userCode) || $userCode == '(null)'){
            $userCode = '';
        }
        $activityMdl = new ActivityModel();
        $activityInfo = $activityMdl->getActInfo(array('activityCode' => $activityCode), array('activityImg', 'shopCode', 'startTime', 'endTime'));
        $userMdl = new UserModel();
        $userMdl->isUserSetInviteCode($userCode);

        $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('inviteCode'));
        $inviteList = $userMdl->listUser(array('userCode' => array('NEQ', $userCode), 'recomNbr' => $userInfo['inviteCode'],'registerTime' => array('GT', '2016-01-25 18:00:00')), $this->getPager(0), array('userCode'));
        $signBankAccountCount = 0;
        foreach($inviteList as $v){
            $bankAccountMdl = new BankAccountModel();
            //得到被邀请人第一次绑的卡号
            $bankCard = $bankAccountMdl->getFirstBankCard($v['userCode']);
            //获取这个被邀请人的这个卡号在他绑之前的绑卡人数
            if($bankCard && $bankCard['bankCard']){
                if($bankCard['createTime'] > '2015-12-25 00:00:00'){
                    $condition = array(
                        'BankAccount.userCode' => array('neq', $v['userCode']),
                        'BankAccount.bankCard' => $bankCard['bankCard'],
                        'BankAccount.createTime' => array('between', array('2015-12-25 00:00:00', $bankCard['createTime'])),
                        'BankAccount.status' => array('in', array(\Consts::BANKACCOUNT_STATUS_SIGNED, \Consts::BANKACCOUNT_STATUS_TERMINATE))
                    );
                    $bankAccountCount = $bankAccountMdl->getBankAccountCount($condition, array(), 'DISTINCT(userCode)');
                    if($bankAccountCount < 3){
                        $signBankAccountCount += 1;
                    }
                }
            }

            $bankAccountCount = $bankAccountMdl->getBankAccountCount(array('BankAccount.createTime' => array('between', array('2015-10-26 00:00:00', '2015-12-24 23:59:59')), 'BankAccount.userCode' => $v['userCode'], 'BankAccount.status' => array('in', array(\Consts::BANKACCOUNT_STATUS_SIGNED, \Consts::BANKACCOUNT_STATUS_TERMINATE))), array(), 'DISTINCT(userCode)');
            if($bankAccountCount > 0){
                $signBankAccountCount += 1;
            }
        }

        $receiveCount = ($signBankAccountCount - $signBankAccountCount % 5) / 5;
        if($receiveCount > 3){
            $receiveCount = 3;
        }
        $userCouponMdl = new UserCouponModel();
//        $batchCouponCodeArr = array('5a0e2630-0550-dfee-eb1e-4de423f118b8', '8cc4a256-1f2c-2e73-5023-bf5edd74e273', '33873970-e5ab-449d-91b5-938e5212b067');

//        $hasReceivedCount = 0;
//        $batchCouponCode = '';
//        foreach($batchCouponCodeArr as $v){
//            $ret = $userCouponMdl->hasUserCoupon(array('userCode' => $userCode, 'batchCouponCode' => $v));
//            if($ret == true){
//                $hasReceivedCount += 1;
//            }else{
//                if(empty($batchCouponCode)){
//                    $batchCouponCode = $v;
//                }
//            }
//        }
        $batchCouponCode = '713dc724-6603-a1d4-9006-8ade0aa14b3f';
        $hasReceivedCount = $userCouponMdl->countMyReceivedCoupon($batchCouponCode, $userCode);
        if(empty($hasReceivedCount)){
            $hasReceivedCount = 0;
        }
        $canReceiveCount = $receiveCount - $hasReceivedCount;
//        $canReceiveCount = 0;
//        $bsMdl = new BonusStatisticsModel();
//        $userBonus = $bsMdl->getUserBonusStatistics($userCode, C('HQ_CODE'));
//        if($userBonus && $userBonus['totalValue'] >= 5000){
//            $canReceiveCount = $receiveCount - $hasReceivedCount;
//        }
        $assign = array(
            'title' => '大米活动',
            'http_host' => $_SERVER['HTTP_HOST'],
            'userCode' => $userCode,
            'activityCode' => $activityCode,
            'activityInfo' => $activityInfo,
            'userInfo' => $userInfo,
            'signBankAccountCount' => $signBankAccountCount,
            'canReceiveCount' => $canReceiveCount,
            'hasReceivedCount' => $hasReceivedCount,
            'batchCouponCode' => $batchCouponCode
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 获得大米活动优惠券
     */
    public function riceCoupon(){
        $userCode = I('post.userCode'); // 用户编码
        $batchCouponCode = I('post.batchCouponCode'); // 优惠券编码
        $batchCouponMdl = new BatchCouponModel();
        $batchCouponInfo = $batchCouponMdl->getCouponInfo($batchCouponCode);
        if(empty($batchCouponInfo)) {
            $this->ajaxError('', array('code' => C('COUPON.NOT_EXIST')));
        }

        $bsMdl = new BonusStatisticsModel();
        $userBonus = $bsMdl->getUserBonusStatistics($userCode, C('HQ_CODE'));
        if(empty($userBonus)){
            $this->ajaxError('', array('code' => C('BONUS.NOT_AVAILABLE')));
        }
        if($userBonus['totalValue'] < 5000){
            $this->ajaxError('', array('code' => C('BONUS.USED')));
        }
        $userConsumeMdl = new UserConsumeModel();
        $ret = $userConsumeMdl->grabRiceCoupon($batchCouponCode, $userCode);
        if ($ret['code'] == C('SUCCESS')) {
//            $userCouponMdl = new UserCouponModel();
//            $batchCouponCodeArr = array('5a0e2630-0550-dfee-eb1e-4de423f118b8', '8cc4a256-1f2c-2e73-5023-bf5edd74e273', '33873970-e5ab-449d-91b5-938e5212b067');
//            $batchCouponCode = '';
//            foreach($batchCouponCodeArr as $v){
//                $ret = $userCouponMdl->hasUserCoupon(array('userCode' => $userCode, 'batchCouponCode' => $v));
//                if($ret != true){
//                    if(empty($batchCouponCode)){
//                        $batchCouponCode = $v;
//                    }
//                }
//            }
            $ret['batchCouponCode'] = '713dc724-6603-a1d4-9006-8ade0aa14b3f';
            $this->ajaxSucc('操作成功!', $ret);
        } else {
            $this->ajaxError('', $ret);
        }
    }

    /**
     * 抢优惠券页
     */
    public function grabBatchCoupon() {
        $batchCouponMdl = new BatchCouponModel();
        $batchCouponCode = I('get.batchCouponCode');
        $batchCouponInfo = $batchCouponMdl->getCouponInfo($batchCouponCode);
        $batchCouponInfo['isTaken'] = $batchCouponInfo['validityPeriod'] != 0 ? C('YES') : C('NO');
        $batchCouponInfo['isTaken'] = strtotime($batchCouponInfo['endTakingTime'] . ' 23:59:59') < time() ? C('NO') : C('YES');
        if($batchCouponInfo['isTaken'] == C('YES')) {
            if($batchCouponInfo['totalVolume'] == -1){
                $batchCouponInfo['isTaken'] = C('YES');
            }else{
                $batchCouponInfo['isTaken'] = $batchCouponInfo['remaining'] == 0 ? C('NO') : C('YES');
            }
        }

        $shopMdl = new ShopModel();
        // 获得商家信息
        $shopInfo = $shopMdl->getShopInfo($batchCouponInfo['shopCode'], array('logoUrl', 'shopName', 'LEFT(Shop.city,2)' => 'city'));
        $batchCouponInfo['logoUrl'] = $shopInfo['logoUrl'];
        $batchCouponInfo['shopName'] = $shopInfo['shopName'];

        $wxApiMdl = new wxApiModel();
        $wxJsPackage = $wxApiMdl->getSignPackage();

        $title = '【' . $shopInfo['city'] . '】';
        if($batchCouponInfo['couponType'] == C('COUPON_TYPE.REDUCTION')) {
            $title .= '满' . $batchCouponInfo['availablePrice'] . '元抵扣'  . $batchCouponInfo['insteadPrice'] . '元一张，我分享你一张' . $shopInfo['shopName'] . '的优惠券，到惠圈，惠生活！';
        } elseif($batchCouponInfo['couponType'] == C('COUPON_TYPE.DISCOUNT')) {
            $title .= '满' . $batchCouponInfo['availablePrice'] . '元打'  . $batchCouponInfo['discountPercent'] . '折一张，我分享你一张' . $shopInfo['shopName'] . '的优惠券，到惠圈，惠生活！';
        } else {
            $title .= $batchCouponInfo['function'] . '一张，我分享你一张' . $shopInfo['shopName'] . '的优惠券，到惠圈，惠生活！';
        }
        $assign = array(
            'title' => $title,
            'batchCouponInfo' => $batchCouponInfo,
            'batchCouponCode' => $batchCouponCode,
        );
        $assign = array_merge($assign, $wxJsPackage);
        $this->assign($assign);
        $this->display();
    }

    /**
     * 得到某些条件下的商家列表
     */
    public function getShopList() {
        $shopMdl = new ShopModel();
        $activityMdl = new ActivityModel();
        $batchCouponMdl = new BatchCouponModel();
        $userEnterShopInfoRecordMdl = new UserEnterShopInfoRecordModel();
        $page = I('get.page');
        $page = $page ? $page : $page;
        $brandId = I('get.brandId');
        $activityCode = I('get.activityCode');
        $activityInfo = $activityMdl->getActInfo(array('activityCode' => $activityCode), array('activityName', 'activityImg'));
        if($brandId){
            switch($brandId) {
                case 56: // 绿江南果业
                    $title = '绿江南果业';
                    $bannerImg = '/Public/img/banner-lvjn.png';
                    break;
                case 63: //雨欣蛋糕
                    $title = '雨欣蛋糕';
                    $bannerImg = '/Public/img/banner-yuxin.jpg';
                    break;
                default:
                    $title = '暖冬送优惠';
                    $bannerImg = '/Public/img/banner-chayu.png';
                    break;
            }
            $shopBrandRelMdl = new ShopBrandRelModel();
            $relList = $shopBrandRelMdl->arrCBR('shopCode', array('brandId' => (int)$brandId));
            if(empty($relList)){
                $relList = array('0');
            }
            $condition['shopCode'] = array('IN', $relList);
        }else{
            $shopTypeRelMdl = new ShopTypeRelModel();
            $title = isset($activityInfo['activityName']) ? $activityInfo['activityName'] : '丽水美食商家';
            $bannerImg = isset($activityInfo['activityImg']) ? $activityInfo['activityImg'] : '/Public/img/banner-lishui.jpg';
            if($activityCode == 'bc858550-0430-771a-a8b3-e700c633a17f'){ //开春银泰惠-满30立减15元
                $condition = array(
                    'shopCode' => array('IN', array('9dcbd9c7-0c1e-a82f-f959-de2fd25a7c9f','4d64735e-fe1f-978b-8b02-676379020a74','2e236171-1afb-a696-12d2-e7530c22b4e3','2a717863-03df-29b7-a07c-9f316470acc9','af958d73-0326-9964-f671-306665df8ef4','073ba155-b49b-320e-31d4-aab7b8b2b5bc','6ee695e5-b905-e907-eda6-2811991eb09f','0d6a7b9d-b0b5-d104-c697-f71134dd3962','515452a9-c25a-1775-a14e-bae91e685a6f','9779c61d-34b0-6cd1-28b1-127fafb75505'))
                );
            }elseif($activityCode == 'f06c06d7-34c3-5d4c-3caa-5aade7360b95'){ //湖州-买单特惠
                $shopCodeArr = $shopTypeRelMdl->getFieldList(array('typeValue' => \Consts::SHOP_TYPE_FOOD), 'shopCode', array(array('joinTable' => 'ShopType', 'joinCondition' => 'ShopType.shopTypeId = ShopTypeRel.typeId', 'joinType' => 'inner')));
                if(empty($shopCodeArr)){
                    $shopCodeArr = array('0');
                }
                $shopCodeArr = array_unique($shopCodeArr);
                $condition = array(
                    'shopCode' => array('IN', $shopCodeArr),
                    'city' => '湖州市',
                    'isAcceptBankCard' => 1,
                    'status' => 1,
                    'onlinePaymentDiscount' => array('lt', 100),
                );
            }elseif($activityCode == 'eb6192e2-a441-8273-023f-105140c8236c'){ //丽水-买单特惠
                $shopCodeArr = $shopTypeRelMdl->getFieldList(array('typeValue' => \Consts::SHOP_TYPE_FOOD), 'shopCode', array(array('joinTable' => 'ShopType', 'joinCondition' => 'ShopType.shopTypeId = ShopTypeRel.typeId', 'joinType' => 'inner')));
                if(empty($shopCodeArr)){
                    $shopCodeArr = array('0');
                }
                $shopCodeArr = array_unique($shopCodeArr);
                $condition = array(
                    'shopCode' => array('IN', $shopCodeArr),
                    'city' => '丽水市',
                    'isAcceptBankCard' => 1,
                    'status' => 1,
                    'onlinePaymentDiscount' => array('lt', 100),
                );
            }else{
                $shopCodeArr = $shopTypeRelMdl->getFieldList(array('typeValue' => \Consts::SHOP_TYPE_FOOD), 'shopCode', array(array('joinTable' => 'ShopType', 'joinCondition' => 'ShopType.shopTypeId = ShopTypeRel.typeId', 'joinType' => 'inner')));
                if(empty($shopCodeArr)){
                    $shopCodeArr = array('0');
                }
                $shopCodeArr = array_unique($shopCodeArr);
                $condition = array(
                    'shopCode' => array('IN', $shopCodeArr),
                    'city' => '丽水市',
                    'status' => 1
                );
            }
        }
        $shopList = $shopMdl->getShopList($condition, '', array(), '', 0, $page);
        if($shopList){
            foreach ($shopList as $key => $shop) {
                //设置商家是否有活动
                $shopActivity = $activityMdl->getActList(array('shopCode' => $shop['shopCode'], 'status' => C('ACTIVITY_STATUS.ACTIVE'), 'endTime' => array('EGT', date('Y-m-d H:i:s')), 'pos' => array('NEQ', C('ACT_POS.SCROLL'))), array('activityCode', 'activityName'), array(), 'createTime desc');
                if(!empty($shopActivity)){
                    $shopList[$key]['hasActivity'] = 1;
                }else{
                    $shopList[$key]['hasActivity'] = 0;
                }
                //设置商家是否有优惠券
                $shopBatchCoupon = $batchCouponMdl->listAvailabelCoupon($shop['shopCode'], '*');
                if(!empty($shopBatchCoupon)){
                    $shopList[$key]['hasBatchCoupon'] = 1;
                }else{
                    $shopList[$key]['hasBatchCoupon'] = 0;
                }
                //获得商家人气
                $shopList[$key]['shopPopularity'] = $userEnterShopInfoRecordMdl->getBrowseQuantity(array('shopCode' => $shop['shopCode']), 'recordId');
            }
        }

        $assign = array(
            'title' => $title,
            'bannerImg' => $bannerImg,
            'shopList' => $shopList,
            'get' => I('get.')
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($shopList) ? '' : $this->fetch('Browser:getShopListWidget');
            $this->ajaxReturn(array('status' => 0, 'html' => $html));
        }
    }

    protected function formatDate1($datetime) {
        $zeroDate = '';
        if($datetime == '0000-00-00 00:00:00') {
            return $zeroDate;
        }
        $time = strtotime($datetime);
        if($time !== false) {
            return date('Y.m.d', $time);
        } else {
            return $zeroDate;
        }
    }

    /**
     * 用户邀请商家入驻
     */
    public function inviteShopSettled() {
        $shopCode = I('get.shopCode');
        $userCode = I('get.userCode');
        $userEnterShopInfoRecordMdl = new UserEnterShopInfoRecordModel();
        $getInfo = $userEnterShopInfoRecordMdl->getRecord($userCode, $shopCode, $actionType = 2);
        if(empty($getInfo)){
            $ret = $userEnterShopInfoRecordMdl->addRecord($shopCode, $userCode, $actionType = 2);
            if($ret){
                $count = $userEnterShopInfoRecordMdl->getBrowseQuantity(array('actionType' => 2, 'shopCode' => $shopCode), 'distinct(userCode)');

                $userMdl = new UserModel();
                $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('nickName', 'mobileNbr'));
                if(empty($userInfo['nickName'])){
                    $userInfo['nickName'] = '用户'.substr($userInfo['mobileNbr'], 7);
                }
                // 推送消息
                $content = str_replace('{{userName}}', $userInfo['nickName'], C('PUSH_MESSAGE.INVITE_SHOP'));
                $content = str_replace('{{userCount}}', $count, $content);
                $receiver = explode('-', $shopCode);
                $jpushMdl = new JpushModel(C('SHOP_APP_KEY'), C('SHOP_MASTER_SECRET'));
                $jpushMdl->jPushByAction($receiver, $content, array(), C('PUSH_ACTION.INVITE_SHOP'));

                $this->ajaxSucc('', $count);
            }else{
                $this->ajaxError('邀请失败');
            }
        }else{
            $count = $userEnterShopInfoRecordMdl->getBrowseQuantity(array('actionType' => 2, 'shopCode' => $shopCode), 'distinct(userCode)');
            $this->ajaxSucc('', $count);
        }
    }

    /**
     * 用户提醒商家上商品
     */
    public function remindShop(){
        $shopCode = I('get.shopCode');
        $userCode = I('get.userCode');
        $userEnterShopInfoRecordMdl = new UserEnterShopInfoRecordModel();
        $getInfo = $userEnterShopInfoRecordMdl->getRecord($userCode, $shopCode, $actionType = 3);
        if(empty($getInfo)){
            $ret = $userEnterShopInfoRecordMdl->addRecord($shopCode, $userCode, $actionType = 3);
            if($ret){
                $count = $userEnterShopInfoRecordMdl->getBrowseQuantity(array('actionType' => 3, 'shopCode' => $shopCode), 'distinct(userCode)');
                $userMdl = new UserModel();
                $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('nickName', 'mobileNbr'));
                if(empty($userInfo['nickName'])){
                    $userInfo['nickName'] = '用户'.substr($userInfo['mobileNbr'], 7);
                }
                // 推送消息
                $content = str_replace('{{userName}}', $userInfo['nickName'], C('PUSH_MESSAGE.REMIND_SHOP'));
                $content = str_replace('{{userCount}}', $count, $content);
                $receiver = explode('-', $shopCode);
                $jpushMdl = new JpushModel(C('SHOP_APP_KEY'), C('SHOP_MASTER_SECRET'));
                $jpushMdl->jPushByAction($receiver, $content, array(), C('PUSH_ACTION.REMIND_SHOP'));

                $this->ajaxSucc('', $count);
            }else{
                $this->ajaxError('提醒失败');
            }
        }else{
            $count = $userEnterShopInfoRecordMdl->getBrowseQuantity(array('actionType' => 3, 'shopCode' => $shopCode), 'distinct(userCode)');
            $this->ajaxSucc('', $count);
        }
    }

    /**
     * 获得页数对象
     * @param int $page 页数
     * @param int $pageSize
     * @return Object Pager
     */
    public function getPager($page, $pageSize = 0){
        if(! isset($page) || $page === '')
            $page = 1;
        if(empty($pageSize)) {
            $pageSize = C('PAGESIZE');
        }
        return new Pager($page, $pageSize);
    }

    /**
     * Ajax成功返回.
     * @param string $msg [可选] 成功消息，
     * @param mixed $data [可选] 返回的数据，可为任意类型。
     * @param string $html [可选] 返回的HTML.
     */
    protected function ajaxSucc($msg = '', $data = null, $html = '') {
        $this->ajaxReturn(array(
            'status' => 0,
            'msg' => $msg,
            'data' => $data,
            'html' => $html
        ));
    }

    /**
     * Ajax失败返回.
     * @param string $msg [可选] 成功消息，
     * @param mixed $data [可选] 返回的数据，可为任意类型。
     * @param string $html [可选] 返回的HTML.
     * @param number $status [可选] 失败代码。默认为-1.
     */
    protected function ajaxError($msg = '', $data = null, $html = '', $status = -1) {
        $this->ajaxReturn(array(
            'status' => $status,
            'msg' => $msg,
            'data' => $data,
            'html' => $html
        ));
    }

    public function create_uuid($prefix = ""){    //可以指定前缀
        $str = md5(uniqid(mt_rand(), true));
        $uuid  = substr($str,0,8) . '-';
        $uuid .= substr($str,8,4) . '-';
        $uuid .= substr($str,12,4) . '-';
        $uuid .= substr($str,16,4) . '-';
        $uuid .= substr($str,20,12);
        //echo $prefix . $uuid;
        return $prefix.$uuid;
    }

    /**
     * 拒绝设置免验证码支付
     */
    public function rejectFreeVal() {
        if(IS_AJAX) {
            $userCode = I('post.userCode');
            $userMdl = new UserModel();
            // 拒绝次数增加1次
            $ret = $userMdl->incField(array('userCode' => $userCode), 'rejectFreeValTimes', 1);
            if($ret === true) {
                $this->ajaxSucc($userCode);
            } else {
                $this->ajaxError();
            }
        }
    }

    /**
     * 增加下载次数
     */
    public function addCDownloadTimes() {
        if(IS_AJAX) {
            $data = I('get.');
            $temp = array('cityId', 'activityNbr', 'operation');
            // 检查参数都有值
            foreach($temp as $v) {
                if(empty($data[$v])) {
                    $this->ajaxError();
                }
            }
            $downloadLogMdl = new DownloadLogModel();
            // 增加下载次数
            $downloadLogMdl->incField($data, 'downloadTimes', 1);
            $this->ajaxSucc($data);
        }
    }

    public function getActInfo(){
        $activityCode = I('get.activityCode');
        $userCode = I('get.userCode');
        $appType = I('get.appType');
        $isShared = I('get.isShared');
        $actMdl = new ActivityModel();
        if($appType == C('LOGIN_TYPE.USER')){
            $actMdl->incPageViews($activityCode); //增加浏览量
        }
        $activityInfo = $actMdl->getActInfo(array('activityCode' => $activityCode));
        if($activityInfo['refundRequired'] == C('ACTIVITY_REFUND_REQUIRED_VALUE.CASUAL')){
            $activityInfo['refundRequired'] = C('ACTIVITY_REFUND_REQUIRED_NAME.CASUAL');
        }elseif($activityInfo['refundRequired'] == C('ACTIVITY_REFUND_REQUIRED_VALUE.THE_DAY')){
            $activityInfo['refundRequired'] = C('ACTIVITY_REFUND_REQUIRED_NAME.THE_DAY');
        }elseif($activityInfo['refundRequired'] == C('ACTIVITY_REFUND_REQUIRED_VALUE.ONE_DAY')){
            $activityInfo['refundRequired'] = C('ACTIVITY_REFUND_REQUIRED_NAME.ONE_DAY');
        }elseif($activityInfo['refundRequired'] == C('ACTIVITY_REFUND_REQUIRED_VALUE.TWO_DAY')){
            $activityInfo['refundRequired'] = C('ACTIVITY_REFUND_REQUIRED_NAME.TWO_DAY');
        }
        if($userCode){
            $userActivityMdl = new UserActivityModel();
            $userActivityInfo = $userActivityMdl->getUserActInfo(array('activityCode' => $activityCode, 'userCode' => $userCode), array('userActivityCode', 'isCollected'));
        }
        $activityInfo['isCollected'] = isset($userActivityInfo['isCollected']) ? $userActivityInfo['isCollected'] : 0;
        $wxApi = new wxApiModel();
        $wxJsPackage = $wxApi->getSignPackage();
        $assign = array(
            'title' => $activityInfo['activityName'],
            'activityInfo' => $activityInfo,
            'userCode' => $userCode,
            'appType' => $appType,
            'isShared' => $isShared
        );
        $assign = array_merge($assign, $wxJsPackage);
        $this->assign($assign);
        $this->display();
    }

    /**
     * 取消/收藏活动
     */
    public function collectAct(){
        $activityCode = I('get.activityCode');
        $userCode = I('get.userCode');
        $collect = I('get.collect');
        $userActivityMdl = new UserActivityModel();
        $userActivityInfo = $userActivityMdl->getUserActInfo(array('activityCode' => $activityCode, 'userCode' => $userCode), array('userActivityCode', 'isCollected'));

        if($userActivityInfo){
            if($userActivityInfo['isCollected'] == $collect){
                if($collect == 1){
                    $this->ajaxError('您已经收藏过了！');
                }else{
                    $this->ajaxError('您已经取消收藏了！');
                }
            }else{
                $ret = $userActivityMdl->updateUserActivity(array('userActivityCode' => $userActivityInfo['userActivityCode']), array('isCollected' => $collect));
                if($ret['code'] == C('SUCCESS')){
                    if($collect == 1){
                        $this->ajaxSucc('收藏成功！');
                    }else{
                        $this->ajaxSucc('取消收藏成功！');
                    }
                }else{
                    $this->ajaxError('操作失败，请重试！');
                }
            }
        }else{
            $ret = $userActivityMdl->updateUserActivity(array(), array('userActivityCode' => $this->create_uuid(), 'activityCode' => $activityCode, 'userCode' => $userCode,'isCollected' => $collect));
            if($ret['code'] == C('SUCCESS')){
                $this->ajaxSucc('收藏成功！');
            }else{
                $this->ajaxError('操作失败，请重试！');
            }
        }
    }
}
