<?php
/**
 * Created by PhpStorm.
 * User: Huafei Ji
 * Date: 15-7-7
 * Time: 下午4:47
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
use Think\Controller;

class IndexController extends Controller {

    public function __construct()
    {
        header("content-Type: text/html; charset=Utf-8");
        parent::__construct();
        C('SHOW_PAGE_TRACE', false);
    }

    //获取验签参数
    public function signData(){
        $data = I('post.');
        $dataInfo  = M('thrid_api')->where(array("app_key"=>$data['fromApp']))->field('app_value')->find();
       if(!$dataInfo){
            return  array("status"=>2,"info"=>'请求参数错误,未找到fromApp');
       }else{
           $result = $this->verifySign($dataInfo['app_value'],$data);
            return $result;
       }
    }

    //验签
    /**
     * 后台验证sign是否合法
     * @param  [type] $secret [description]
     * @param  [type] $data   [description]
     * @return [type]         [description]
     */
    private  function verifySign($secret, $data) {

        // 验证参数中是否有签名
        if (!isset($data['signData']) || !$data['signData']) {
            return array("status"=>2,"info"=>'发送的数据签名不存在');
        }
        if (!isset($data['timestamp']) || !$data['timestamp']) {
            return array("status"=>2,"info"=>'发送的数据参数不合法');
        }
        // 验证请求， 10分钟失效
        if ((time() - $data['timestamp']) > 600) {
            return array("status"=>2,"info"=> '验证失效， 请重新发送请求');
        }
        $sign = $data['signData'];
        unset($data['signData']);
        ksort($data);
        $params = http_build_query($data);
        // $secret是通过key在api的数据库中查询得到
        $sign2 = md5($params ."&value=".$secret);
        $sign2 = strtoupper($sign2); //转为大写
        if ($sign == $sign2) {
            return array("status"=>1,"info"=>'验签成功');
        } else {
            return array("status"=>2,"info"=>'验签失败');
        }
    }


    //第三方领券接口
    public function thirdGetBatch(){

        //验签
        $info = $this->signData();
        if($info['status']!=1){
            echo json_encode($info);
            die();
        }

        $userCode = I("post.userCode");
        $batchCouponCode = I("post.batchCouponCode");
        //判断用户/优惠券不为空
        if(!isset($userCode) || $userCode == '') {
            echo json_encode(array_merge(array('code' => C('USER.USER_CODE_EMPTY')),$info));
            die();
        }
        if(!isset($batchCouponCode) || $batchCouponCode==''){
            echo json_encode(array_merge(array('code' => C('USER.CREATOR_CODE_ERROR')),$info));
            die();
        }
        //先查优惠券所属商户编号
        M()->startTrans();
        $batchCouponModel = new BatchCouponModel();
        $shopCode =$batchCouponModel ->where(array("batchCouponCode"=>$batchCouponCode))->field("shopCode")->find();
        $orderInfo =  $this->addCouponOrder($userCode,$shopCode['shopCode'],$batchCouponCode,1,0,0);
        if($orderInfo['code']==50000){
            $orderCodeArr =   $this->bankcardPayConfirm($orderInfo['consumeCode'],"","");
            if($orderCodeArr['code']==50000){

                $result =   D("OrderCoupon")->join("UserCoupon on UserCoupon.orderCouponCode = OrderCoupon.orderCouponCode")
                    ->where(array("OrderCoupon.orderCode"=>$orderCodeArr['orderCode']))
                    ->field("UserCoupon.userCouponCode,UserCoupon.userCode,UserCoupon.batchCouponCode,UserCoupon.userCouponNbr")->find();

                //qrCode 字段
                $result['qrCode']   = $result['userCouponNbr'];

                //添加第三方支付记录
                $data['consumeCode']    = $orderInfo['consumeCode'];
                $data['orderNbr']       = $orderInfo['orderNbr'];
                $data['orderCode']      = $orderCodeArr['orderCode'];
                $data['userCode']       = $result['userCode'];
                $data['batchCouponCode'] = $result['batchCouponCode'];
                $data['userCouponNbr']  = $result['userCouponNbr'];
                $data['userCouponCode'] = $result['userCouponCode'];
                $data['from']             = I("post.fromApp");
                $data['createTime']      =time();
                $res =     D("thrid_api_info")->add($data);

                if($res){
                    M()->commit();
                    echo json_encode(array_merge(array("code"=>50000),$result,$info));
                    die();
                }else{
                    M()->rollback();
                    echo json_encode(array_merge(array("code"=>20000),$info));
                    die();
                }

            }else{
                M()->rollback();
                echo json_encode(array_merge($orderCodeArr,$info));
                die();
            }
        }else{
            M()->rollback();
            if($orderInfo['code']==80238){
                $orderInfo['code'] = 80222;
            }
            echo json_encode(array_merge($orderInfo,$info));
            die();
        }
    }

    //第三方退券接口
    public function thirdReturnBatch(){

        //验签
        $info = $this->signData();
        if($info['status']!=1){
            echo json_encode($info);
            die();
        }


        $reson = I("post.reson");
        $userCouponNbr = I("post.userCouponNbr");
        $userCouponCode = D("UserCoupon")->where(array("userCouponNbr"=>$userCouponNbr))
            ->join("OrderCoupon ON OrderCoupon.orderCouponCode =UserCoupon.orderCouponCode")->field("OrderCoupon.orderCode,UserCoupon.userCouponCode")->find();

        $data =array("status"=>0,"remarkReson"=>$reson);
        $result1 =  D("UserCoupon")->where(array('userCouponCode'=>$userCouponCode['userCouponCode']))->data($data)->save();

         if($result1){
             $consumeOrderMdl = new ConsumeOrderModel();
             $result1 = $consumeOrderMdl->agreeRefund($userCouponCode['orderCode']);
         }

        if($result1){
            echo json_encode(array_merge(array("code"=>50000),$info));
        }else{
            echo json_encode(array_merge(array("code"=>20000),$info));
        }
    }

    //转码
    private  function characet($data){
        if( !empty($data) ){
            $fileType = mb_detect_encoding($data , array('UTF-8','GBK','LATIN1','BIG5')) ;
            if( $fileType != 'UTF-8'){
                $data = mb_convert_encoding($data ,'utf-8' , $fileType);
            }
        }
        return $data;
    }

    //解码
    private function  utfzwen($utf8string){
        $a=urldecode($utf8string);
        $string =  mb_convert_encoding($a, 'GB2312', 'UTF-8');

        return $string;
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
    private function addCouponOrder($userCode, $shopCode, $batchCouponCode, $couponNbr, $platBonus, $shopBonus) {
        //根据shopCode查询商家名
        $Shop=D('Shop');
        $shopNameArr=$Shop->field('shopName')->where("shopCode='$shopCode'")->find();
        $shopName=$shopNameArr['shopName'];
        // 获得在线支付的商户号
        $shopMdl = new ShopModel();
        $shopInfo = $shopMdl->getShopInfo($shopCode, array('icbcShopCode', 'hqIcbcShopNbr'));

        // 判断用户是否可以购买该优惠券
        $batchCouponMdl = new BatchCouponModel();
        $isUserCanBuyCoupon = $batchCouponMdl->isUserCanBuyCoupon($userCode, $batchCouponCode, $couponNbr);
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
            $ret['shopName']=$shopName;
            return $ret;
        } else {
            return $ret;
        }
    }


    /**
     * 在线支付确认支付（https协议传输）
     * @param string $consumeCode 用户消费记录编码
     * @param string $bankAccountCode 用户关联银行账户编码
     * @param int $valCode 验证码
     * @return string $ret
     */
    private function bankcardPayConfirm($consumeCode, $bankAccountCode, $valCode) {
        $userConsumeMdl = new UserConsumeModel();
        // 判断支付账单中使用的优惠券是否可用
        $isCouponCanBeUse = $userConsumeMdl->isConsumeCouponCanBeUse($consumeCode);

        if($isCouponCanBeUse) {
            if(empty($bankAccountCode)&&empty($valCode)){

                $ret = $userConsumeMdl->bankcardPayConfirm2($consumeCode);
            }else{
                $ret = $userConsumeMdl->bankcardPayConfirm($consumeCode, $bankAccountCode, $valCode);
            }
        } else {

            $ret = array('code' => C('API_INTERNAL_EXCEPTION'));
        }
        return $ret;
    }

    /**
     * 把ShopPhoto里的照片转移到Product里
     */
    public function shopPhotoToProduct() {
        $shopPhotoMdl = new ShopPhotoModel();
        $shopPhotoList = $shopPhotoMdl
            ->field(array('ShopPhoto.url', 'ShopPhoto.title', 'ShopPhoto.price', 'ShopPhoto.des', 'ShopPhoto.createTime', 'SubAlbum.name' => 'subAlbumName', 'SubAlbum.shopCode'))
            ->join('SubAlbum ON SubAlbum.code = ShopPhoto.subAlbumCode')
            ->where(array())
            ->select();
        $productCategoryMdl = new ProductCategoryModel();
        $productMdl = new ProductModel();
        foreach($shopPhotoList as $photo) {
            $categoryInfo = $productCategoryMdl->getProductCategoryInfo(array('categoryName' => $photo['subAlbumName'], 'shopCode' => $photo['shopCode']), array('categoryId'));
            if(!$categoryInfo) {
                $categoryInfo = $productCategoryMdl->editProductCategory(array('categoryName' => $photo['subAlbumName'], 'shopCode' => $photo['shopCode']));
            }
            $productInfo = $productMdl->getProductInfo(array('shopCode' => $photo['shopCode'], 'productName' => $photo['title'], 'categoryId' => $categoryInfo['categoryId']));
            if(empty($productInfo['productId'])) {
                $productMdl->editProduct(array(
                    'shopCode' => $photo['shopCode'],
                    'createTime' => $photo['createTime'],
                    'productImg' => $photo['url'],
                    'productName' => $photo['title'],
                    'originalPrice' => $photo['price'],
                    'des' => $photo['des'],
                    'categoryId' => $categoryInfo['categoryId']
                ));
            }
        }
    }
}
