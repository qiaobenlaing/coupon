<?php
namespace Wechat\Controller;
use Common\Model\ActivityModel;
use Common\Model\BatchCouponModel;
use Common\Model\CardModel;
use Common\Model\ShopDecorationModel;
use Common\Model\ShopModel;
use Common\Model\wxApiModel;
use Think\Controller;
/**
 * Created by PhpStorm.
 * User: huafei.ji
 * Date: 15-8-15
 * Time: 上午10:00
 */
class ShopController extends WechatBaseController {
    /**
     * @var $wxApi wxApiModel
     */
    private $wxApi;

    public function _initialize(){
        parent::_initialize();
        $this->wxApi = new wxApiModel();
    }

    /**
     * 分享商店页面
     */
    public function share(){
        $shopCode = I('get.shopCode');
        // 获得商家基本信息
        $shopMdl = new ShopModel();
        // 获得商家信息
        $shopInfo = $shopMdl->getShopInfo($shopCode, array('Shop.shopCode', 'Shop.shopName', 'Shop.longitude', 'Shop.latitude', 'Shop.country', 'Shop.province', 'Shop.city', 'Shop.street', 'Shop.tel', 'Shop.mobileNbr', 'Shop.creditPoint', 'Shop.type', 'Shop.isOuttake', 'Shop.isOrderOn', 'Shop.logoUrl', 'Shop.shortDes'));
//        if($userCode){
//            $userCardMdl = new UserCardModel();
//            $shopInfo['hasCard'] = $userCardMdl->isUserHasShopCard($userCode, $shopCode) ? C('YES') : C('NO');
//            $shopFollowingMdl = new ShopFollowingModel();
//            $shopInfo['isFollowed'] = $shopFollowingMdl->isUserFollowedShop($userCode, $shopCode) ? C('YES') : C('NO');
//        }

        // 获得商家装修
        $shopDecorationMdl = new ShopDecorationModel();
        $shopDecoration = $shopDecorationMdl->getShopDecoration($shopCode);
        // 获得商家可领取的优惠券
        $batchCouponMdl = new BatchCouponModel();
        $shopCoupon = $batchCouponMdl->listAvailabelCoupon($shopCode,
            array(
                'batchCouponCode',
                'couponName',
                'insteadPrice',
                'discountPercent',
                'industryCategory',
                'availablePrice',
                'endTakingTime',
                'createTime',
                'payPrice',
                'remaining',
                'totalVolume',
                'nbrPerPerson',
            ), $userCode = '');
        // 获得商家会员卡
        $cardMdl = new CardModel();
        $shopCard = $cardMdl->getGeneralCardStastics($shopCode, $this->getPager(0), 1);
        // 获得商家活动
//        $activityMdl = new ActivityModel();
//        $shopActivity = $activityMdl->getActivityList('', $shopCode,'');
        $wxJsPackage = $this->wxApi->getSignPackage();
        $assign = $wxJsPackage;
        $assign['title'] = '商家详情';
        $assign['shopInfo'] = $shopInfo;
//        $assign['shopActivity'] = $shopActivity;
        $assign['shopCoupon'] = $shopCoupon;
        $assign['shopCard'] = $shopCard;
        $assign['shopDecoration'] = $shopDecoration;
        $this->assign($assign);
        $this->display();
    }
}