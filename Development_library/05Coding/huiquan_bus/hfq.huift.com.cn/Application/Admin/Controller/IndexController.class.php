<?php
namespace Admin\Controller;
use Common\Model\ConsumeOrderModel;
use Common\Model\DistrictModel;
use Common\Model\PreShopModel;
use Common\Model\ShopModel;
use Common\Model\ShopTypeRelModel;
use Common\Model\SmsModel;
use Common\Model\UserConsumeModel;
use Common\Model\UserCouponModel;
use Think\Controller;
class IndexController extends AdminBaseController {

    public function index(){
        $this->redirect('/Admin/BmStaff/homepage');
    }

    /**
     * 为所有没有惠圈入账商户号的商户设置惠圈入账商户号
     */
    public function addHqIcbcShopNbr() {
        $districtMdl = new DistrictModel();
        // 获得浙江省的所有城市和对应地区号
        $areaNbrList = $districtMdl->getCityList('浙江省');

        $shopMdl = new ShopModel();
        // 获得所有没有惠圈入账商户号，已经入驻的商户
        $shopList = $shopMdl->getShopList(array('hqIcbcShopNbr' => '', 'shopStatus' => \Consts::SHOP_ENTER_STATUS_YES), array('shopCode', 'city'));
        foreach($shopList as $shopK => $shop) {
            $areaNbr = '';
            foreach($areaNbrList as $areaK => $area) {
                if($shop['city'] == $area['name']) {
                    $areaNbr = $area['areaNbr'];
                    break;
                }
            }

            // 获得商户的惠圈入账商户号
            $hqIcbcShopNbr = $shopMdl->setHqIcbcShopNbr($areaNbr);
            // 设置商户的惠圈入账商户号
            $shopMdl->editShop(array('shopCode' => $shop['shopCode'], 'hqIcbcShopNbr' => $hqIcbcShopNbr, 'icbcCityNbr' => $areaNbr));
        }
    }

    /**
     * 测试二维码生成
     */
    public function testQrCode() {
        //导入类库
        Vendor('QRcode.phpqrcode');
        // 二维码数据
        $data = 'http://baomi.suanzi.cn/Api/Browser/posFaq';
        // 点的大小：1到10
        $matrixPointSize = 4;
        // 保存图片的途径
        $path = "./Public/qrcode/";
        // 生成的文件名
        $filename = $path.$matrixPointSize.'posFaq.png';
        // 纠错级别：L、M、Q、H
        $errorCorrectionLevel = 'L';

        // 启动
        \QRcode::png($data, $filename, $errorCorrectionLevel, $matrixPointSize, 2);
        \QRcode::png($data, false, $errorCorrectionLevel, $matrixPointSize, 2);
    }

    /**
     * 添加已退款金额中的值
     */
    public function addRefundAmount() {
        // 找到所有已退款的订单，
        $consumeOrderMdl = new ConsumeOrderModel();
        $orderList = $consumeOrderMdl->getConsumeOrderList(array('status' => \Consts::PAY_STATUS_REFUNDED), array('orderCode', 'orderAmount'));
        foreach($orderList as $order) {
            $consumeOrderMdl->where(array('orderCode' => $order['orderCode']))->save(array('refundAmount' => $order['orderAmount']));
        }
    }

    /**
     * 补充ShopTypeRel表中的数据
     */
    public function addShopTypeRel() {
        $shopMdl = new ShopModel();
        $shopList = $shopMdl->field(array('shopCode', 'type'))->select();
        $shopTypeRelMdl = new ShopTypeRelModel();
        $shopTypeRelMdl->addShopTypeRel($shopList);
    }

    /**
     * 补充已付款的账单中使用的优惠券
     */
    public function supplyUsedUserCouponCode() {
        $userConsumeMdl = new UserConsumeModel();
        $consumeList = $userConsumeMdl->field(array('consumeCode', 'couponUsed'))->where(array('status' => array('IN', array(\Consts::PAY_STATUS_PAYED, \Consts::PAY_STATUS_UNPAYED)), 'couponUsed' => array('GT', 0), 'usedUserCouponCode' => array('EXP', 'is null')))->select();
        $userCouponMdl = new UserCouponModel();
        foreach($consumeList as $v) {
            $userCouponList = $userCouponMdl->where(array('consumeCode' => $v['consumeCode']))->getField('userCouponCode', true);
            $stringUserCouponCode = implode('|', $userCouponList);
            $userConsumeMdl->where(array('consumeCode' => $v['consumeCode']))->save(array('usedUserCouponCode' => $stringUserCouponCode));
        }
    }

    public function getMsgCode() {
        $mo = I('get.mobile');
        $action = I('get.action');
        $smsMdl = new SmsModel();
        dump($smsMdl->getCode($action . $mo));
    }
}