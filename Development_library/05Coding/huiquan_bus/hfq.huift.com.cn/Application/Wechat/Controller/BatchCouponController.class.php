<?php
namespace Wechat\Controller;
use Common\Model\ClientAppLogModel;
use Common\Model\ShopModel;
use Common\Model\wxApiModel;
use Think\Controller;
use Common\Model\BatchCouponModel;
/**
 * Created by PhpStorm.
 * User: Huafei Ji
 * Date: 15-7-1
 * Time: 上午11:28
 */
class BatchCouponController extends WechatBaseController{
    /**
     * @var $wxApi wxApiModel
     */
    private $wxApi;

    public function _initialize(){
        parent::_initialize();
        $this->wxApi = new wxApiModel();
    }

    /**
     * 分享商家优惠券
     */
    public function share() {
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
        $wxJsPackage = $this->wxApi->getSignPackage();
        $assign = $wxJsPackage;

        $clientAppLogMdl = new ClientAppLogModel();
        $cAppInfo = $clientAppLogMdl->getNewestClientAppVersion();
        $assign['cDownloadUrl'] = $cAppInfo['updateUrl'];

        $title = '【' . $shopInfo['city'] . '】';
        if($batchCouponInfo['couponType'] == \Consts::COUPON_TYPE_REDUCTION) {
            $title .= '满' . $batchCouponInfo['availablePrice'] . '元抵扣'  . $batchCouponInfo['insteadPrice'] . '元一张，我分享你一张' . $shopInfo['shopName'] . '的优惠券，到惠圈，惠生活！';
        } elseif($batchCouponInfo['couponType'] == C('COUPON_TYPE.DISCOUNT')) {
            $title .= '满' . $batchCouponInfo['availablePrice'] . '元打'  . $batchCouponInfo['discountPercent'] . '折一张，我分享你一张' . $shopInfo['shopName'] . '的优惠券，到惠圈，惠生活！';
        } else {
            $title .= $batchCouponInfo['function'] . '一张，我分享你一张' . $shopInfo['shopName'] . '的优惠券，到惠圈，惠生活！';
        }

        $assign['title'] = $title;
        $assign['batchCouponInfo'] = $batchCouponInfo;
        $this->assign($assign);
        $this->display();
    }
}