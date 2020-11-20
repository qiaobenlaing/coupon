<?php
namespace Api\Controller;
use Common\Model\CouponRuleModel;
use Common\Model\OrderCouponModel;
use Think\Controller;
use Common\Model\BatchCouponModel;
use Common\Model\UserConsumeModel;
use Common\Model\UserCouponModel;

class GzBankController extends Controller{

    public function __construct() {
        parent::__construct();
        // 确保jsonrpc后面不被挂上一堆调试信息。
        C('SHOW_PAGE_TRACE', false);
        header("Content-Type: text/html;charset=utf-8");
//        $this->pager = new Pager(I('page', 1, 'intval'), I('pageSize', Pager::DEFUALT_PAGE_SIZE, 'intval'));
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

    /**
     * 根据优惠券的验证码获得优惠券信息
     * @param string $couponCode 优惠券验证码
     * @return array $couponInfo 优惠券信息
     */
    public function getCouponInfoByCode() {

        //验签
        $info = $this->signData();
        if($info['status']!=1){
            $yanqian['check'] = $info;
            $fail = $this->decodeUnicode(json_encode($yanqian));
            echo $fail;
            die();
        }

        $couponCode = I("post.couponCode");

        $orderCouponMdl = new OrderCouponModel();
        $userCouponInfo = $orderCouponMdl->getOrderCouponInfo(
            array('couponCode' => $couponCode, 'OrderCoupon.status' => array('neq', \Consts::ORDER_COUPON_STATUS_UNPAY_NOUSE)),
            array('OrderCoupon.orderCouponCode', 'couponCode', 'OrderCoupon.status', 'function', 'payPrice', 'insteadPrice', 'BatchCoupon.startUsingTime', 'BatchCoupon.exRuleList','BatchCoupon.expireTime', 'dayStartUsingTime', 'dayEndUsingTime', 'UserCoupon.userCode', 'userCouponCode', 'BatchCoupon.remark', 'BatchCoupon.availablePrice','UserCoupon.applyTime', 'BatchCoupon.validityPeriod','BatchCoupon.couponType','BatchCoupon.couponBelonging','BatchCoupon.shopCode','BatchCoupon.availablePrice'),
            array(
                array('joinTable' => 'UserCoupon', 'joinCondition' => 'UserCoupon.orderCouponCode = OrderCoupon.orderCouponCode', 'joinType' => 'inner'),
                array('joinTable' => 'BatchCoupon', 'joinCondition' => 'BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode', 'joinType' => 'inner'),
            )
        );

        if(!empty($userCouponInfo['exRuleList'])){
            $CouponRuleModel = new CouponRuleModel();
            $couponRuleArr =  json_decode( $userCouponInfo['exRuleList']);
            unset($userCouponInfo['exRuleList']);
            $rules  =  $userCouponInfop['exRuleList'] =  $CouponRuleModel->where(array("ruleCode"=>array("IN",$couponRuleArr)))->field('ruleDes')->select();
            $userCouponInfo['exRuleList'] = $rules;
        }

        if($userCouponInfo) {
            // 判断优惠券是否过期
            $userCouponMdl = new UserCouponModel();
            $isExpire = $userCouponMdl->isUserCouponExpire($userCouponInfo['applyTime'], $userCouponInfo['validityPeriod'], $userCouponInfo['expireTime']);
            $userCouponInfo['isExpire'] = $isExpire == true ? \Consts::NO : \Consts::YES;
        }

        if($userCouponInfo){
            $yanqian['check'] = $info;
            $yewu['code'] = 50000;
            $userCouponInfo = $this->decodeUnicode(json_encode(array_merge($yanqian,$yewu,$userCouponInfo)));
        }else{
            $yanqian['check'] = $info;
            $yewu['code'] = 20000;
            $userCouponInfo = $this->decodeUnicode(json_encode(array_merge($yanqian,$yewu)));
        }
        echo $userCouponInfo;
    }

    /**
     * 验证使用兑换券和代金券
     * @param string $userCode 用户编码
     * @param string $shopCode 商户编码
     * @param string $userCouponCode 用户优惠券编码
     * @return array
     */
//    public function useCoupon() {
//
//        //验签
//        $info = $this->signData();
//        if($info['status']!=1){
//            $yanqian['check'] = $info;
//            $fail = $this->decodeUnicode(json_encode($yanqian));
//            echo $fail;
//            die();
//        }
//
//        $userCode = I("post.userCode");
//        $userCouponCode =I("post.userCouponCode");
//        $orderCouponCode = I("post.orderCouponCode");
//        $shopCode = I('post.shopCode');
//        //根据第三方的商户code得到商户对应关联表中的对应的数据表shopcode
////        $shopCode = M('shoplink')->where(array('thirdShopCode' => $shopCode))->find();
////        if(!shopCode){
////            $yanqian['check'] = $info;
////            $resinfo = json_encode(array_merge($yanqian,array('code' => C('SHOP.NOT_EXIST'))));
////            echo $this->decodeUnicode($resinfo);
////            exit;
////        }
//        // 得到用户使用的优惠券属于的商家
//        $userCouponMdl = new UserCouponModel();
//        $userCouponInfo = $userCouponMdl->getUserCouponInfoB(array('userCouponCode' => $userCouponCode), array('UserCoupon.batchCouponCode'));
//        $batchCouponMdl = new BatchCouponModel();
//        $batchCouponInfo = $batchCouponMdl->getCouponBasicInfo(array('batchCouponCode' => $userCouponInfo['batchCouponCode']), array('shopCode,isUniversal'));
//        if($batchCouponInfo){
//            //优惠券既不是属于商家且不属于平台，或者优惠券属于平台，但不满足所有店铺能使用的限制条件
//            if(($batchCouponInfo['shopCode'] != $shopCode && $batchCouponInfo['shopCode'] !=  C('HQ_CODE')) || ($batchCouponInfo['shopCode'] == C('HQ_CODE') && $batchCouponInfo['isUniversal'] != 1)){
//                $yanqian['check'] = $info;
//                $resinfo = json_encode(array_merge($yanqian,array('code' => C('USER_COUPON.CAN_NOT_USE'))));
//                echo $this->decodeUnicode($resinfo);
//                exit;
//            }
//        }
//
//        $ucMdl = new UserConsumeModel();
//        $appType = C('LOGIN_TYPE.SHOP');
//        $userCoupon = $userCouponMdl->where(array("userCouponCode"=>$userCouponCode))->field("userCouponCode,userCouponNbr,inWeixinCard")->find();
//        if(count($userCoupon)<0){
//            $yanqian['check'] = $info;
//            $resinfo = json_encode(array_merge($yanqian,array('code' => C('USER_COUPON.CAN_NOT_USE'))));
//            echo $this->decodeUnicode($resinfo);
//            exit;
//        }
//
//        $result = $ucMdl->zeroPay($userCode, $shopCode, $userCouponCode, $appType);
//        $yanqian['check'] = $info;
//        $res = json_encode(array_merge($yanqian,$result));
//        echo $this->decodeUnicode($res);
//    }

    /**
     * 验证使用兑换券和代金券
     * @param string $userCode 用户编码
     * @param string $shopCode 商户编码
     * @param string $userCouponCode 用户优惠券编码
     * @return array
     */
    public function useCoupon() {

        //验签
        $info = $this->signData();
        if($info['status']!=1){
            $yanqian['check'] = $info;
            $fail = $this->decodeUnicode(json_encode($yanqian));
            echo $fail;
            die();
        }
        $totalPrice = I('post.price');//未使用优惠券前的初始总价
        $couponCode = I("post.couponCode");//优惠券验证码
        $shopCode = I('post.shopCode');//核销优惠券店铺code
        //参数判断
        if(empty($totalPrice) || empty($couponCode) || empty($shopCode)){
            $yanqian['check'] = $info;
            $resInfo = json_encode(array_merge($yanqian,array('code' => 90000)));
            echo $this->decodeUnicode($resInfo);
            exit;
        }
        //根据第三方的商户code得到商户对应关联表中的对应的数据表shopcode
        $shopCode = M('shoplink')->where(array('thirdShopCode' => $shopCode))->getField('shopCode');
        if(!$shopCode){
            $yanqian['check'] = $info;
            $resInfo = json_encode(array_merge($yanqian,array('code' => C('SHOP.NOT_EXIST'))));
            echo $this->decodeUnicode($resInfo);
            exit;
        }
        //对优惠券进行初步判断
        $isCouPonCanBeUseInfo = M('usercoupon')
            ->where(array('userCouponNbr' => array('eq',$couponCode),'status' => array('eq',1)))
            ->find();
        if(!$isCouPonCanBeUseInfo){
            $yanqian['check'] = $info;
            $resInfo = json_encode(array_merge($yanqian,array('code' => C('COUPON.NOT_EXIST'))));
            echo $this->decodeUnicode($resInfo);
            exit;
        }

        //获取优惠券相关的信息，orderCode，orderCouponCode,consumeCode,userCouponCode,batchCouponCode,userCode
        $orderCouponMdl = new OrderCouponModel();
        $userCouponInfo = $orderCouponMdl->getOrderCouponInfo(
            array('couponCode' => $couponCode, 'OrderCoupon.status' => array('neq', \Consts::ORDER_COUPON_STATUS_UNPAY_NOUSE)),
            array('OrderCoupon.orderCouponCode','OrderCoupon.orderCode', 'UserCoupon.userCode', 'UserCoupon.userCouponCode', 'BatchCoupon.batchCouponCode','BatchCoupon.remark','BatchCoupon.shopCode','BatchCoupon.isUniversal','BatchCoupon.insteadPrice', 'BatchCoupon.availablePrice','BatchCoupon.couponType','BatchCoupon.exRuleList','UserConsume.consumeCode','User.isHaveCard'),
            array(
                array('joinTable' => 'UserCoupon', 'joinCondition' => 'UserCoupon.orderCouponCode = OrderCoupon.orderCouponCode', 'joinType' => 'inner'),
                array('joinTable' => 'BatchCoupon', 'joinCondition' => 'BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode', 'joinType' => 'inner'),
                array('joinTable' => 'UserConsume', 'joinCondition' => 'OrderCoupon.orderCode = UserConsume.orderCode', 'joinType' => 'inner'),
                array('joinTable' => 'User', 'joinCondition' => 'UserCoupon.userCode = User.userCode', 'joinType' => 'inner')
            )
        );
        if(empty($userCouponInfo)){
            $yanqian['check'] = $info;
            $resInfo = json_encode(array_merge($yanqian,array('code' => C('COUPON.NOT_EXIST'))));
            echo $this->decodeUnicode($resInfo);
            exit;
        }
        //优惠券属于商家自己，或者属于平台且满足所有商家都能使用条件才能使用优惠券，不然优惠券不能使用
        if($userCouponInfo && !empty($userCouponInfo['shopCode'])){
            //优惠券既不是属于商家且不属于平台，或者优惠券属于平台，但不满足所有店铺能使用的限制条件
            if(($userCouponInfo['shopCode'] != $shopCode && $userCouponInfo['shopCode'] !=  C('HQ_CODE')) || ($userCouponInfo['shopCode'] == C('HQ_CODE') && $userCouponInfo['isUniversal'] != 1)){
                $yanqian['check'] = $info;
                $resinfo = json_encode(array_merge($yanqian,array('code' => C('USER_COUPON.CAN_NOT_USE'))));
                echo $this->decodeUnicode($resinfo);
                exit;
            }
        }
        $userCouponMdl = new UserCouponModel();
        $ucMdl = new UserConsumeModel();
        $appType = C('LOGIN_TYPE.SHOP');
        $userCoupon = $userCouponMdl->where(array("userCouponCode"=>$userCouponInfo['userCouponCode']))->field("userCouponCode,userCouponNbr,inWeixinCard")->find();
        if(count($userCoupon)<0){
            $yanqian['check'] = $info;
            $resinfo = json_encode(array_merge($yanqian,array('code' => C('USER_COUPON.CAN_NOT_USE'))));
            echo $this->decodeUnicode($resinfo);
            exit;
        }

        $priceArr = $this->getRelPay($totalPrice,$userCouponInfo['couponType'],$userCouponInfo['insteadPrice'],$userCouponInfo['availablePrice'],$userCouponInfo['exRuleList'],$userCouponInfo['isHaveCard']);
        //消费总价不满足使用优惠券的条件,或不是持卡人不能使用折扣代金券
        if($priceArr['status'] == 'error'){
            $yanqian['check'] = $info;
            $resinfo = json_encode(array_merge($yanqian,array('code' => C('COUPON.AVAILABLE_PRICE_ERROR'))));
            echo $this->decodeUnicode($resinfo);
            exit;
        }
        //根据优惠券类型获取最新总价，并存入各个数据表
        $newTotalPrice = $priceArr['relPay'];//优惠后价格
        $discountTotalPrice = $priceArr['discountPrice'];//优惠额度

        $result = $ucMdl->zeroPayForGzBank($userCouponInfo['userCode'],$shopCode, $userCouponInfo['userCouponCode'], $appType);

        if($result['code'] == C('SUCCESS')){
            //核销成功,
            //将数据添加到各表中，
            //订单表
            M()->startTrans();
            $orderArr = array(
                'orderCode' => $result['orderCode'],
                'status' => C('ORDER_STATUS.PAYING'),
                'orderAmount' => $totalPrice,//订单总价
            );
            $resConsumeOrder = M('consumeorder')->save($orderArr);
            //用户消费记录
            $userConsumeArr = array(
                'consumeCode' => $result['consumeCode'],
                'couponUsed' => 1,//该次付款使用的优惠券数量
                'deduction' => $discountTotalPrice,//抵扣金额
                'realPay' => $newTotalPrice,//实付金额
                'status' => C('UC_STATUS.PAYING'),//支付状态,
            );
            $resUserConsume = M('userconsume')->save($userConsumeArr);

            //预订单关联表
            $preOrderCode = $ucMdl->create_uuid();
            $preOrderArr = array(
                'preOrderCode' => $preOrderCode,
                'orderCode' => $result['orderCode'],
                'orderCouponCode' => $userCouponInfo['orderCouponCode'],
                'consumeCode' => $result['consumeCode'],
                'userCouponCode' => $userCouponInfo['userCouponCode'],
                'batchCouponCode' => $userCouponInfo['batchCouponCode'],
                'shopCode' => $shopCode,
                'userCode' => $userCouponInfo['userCode'],
                'status' => 2,
                'createTime' => date('Y-m-d H:i:s'),
            );
            $resPreOrder = M('preorder')->add($preOrderArr);
            if($resUserConsume && $resConsumeOrder && $resPreOrder){
                M()->commit();
            }else{
                M()->rollback();
            }
            $yanqian['check'] = $info;
            $returnResult['code'] = $result['code'];
            $returnResult['relPay'] = $newTotalPrice;//实付价格
            $returnResult['discountPrice'] = $discountTotalPrice;//享受的优惠
            $returnResult['preOrderCode'] = $preOrderCode;
            $res = json_encode(array_merge($yanqian,$returnResult));
            echo $this->decodeUnicode($res);
        }else{
            $yanqian['check'] = $info;
            $res = json_encode(array_merge($yanqian,$result));
            echo $this->decodeUnicode($res);
        }

    }


    /** 对unicode码进行解码
     * @param $str
     * @return null|string|string[]
     */
    private   function decodeUnicode($str)
    {
        return preg_replace_callback('/\\\\u([0-9a-f]{4})/i',
            create_function(
                '$matches',
                'return mb_convert_encoding(pack("H*", $matches[1]), "UTF-8", "UCS-2BE");'
            ),
            $str);
    }

    /*计算使用优惠券后的总价
     * $totalPrice 初始总额
     * $couponType 优惠券类型
     * $insteadPrice 抵用金额
     * $availablePrice 达到多少金额可用
     * $exRuleList 附加规则，有代表的是折扣代金券
     * */
    private function getRelPay($totalPrice,$couponType,$insteadPrice,$availablePrice,$exRuleList,$isHaveCard){
        if($availablePrice){
            //判断是否满足使用优惠券条件
            if($totalPrice < $availablePrice)
                return array('status' => 'error');
        }

        if(empty($exRuleList)){
            //一般代金券
            if($totalPrice >= $availablePrice){
                $relPay = $totalPrice - $insteadPrice;
                $discountPrice = $insteadPrice;
            }else{
                $relPay = $totalPrice;
                $discountPrice = 0;
            }
        }else{
            //折扣代金券
//            //如果不是持卡人，则不能使用折扣代金券(因为只有持卡人才能领特殊券，故放弃判断）
//            if($isHaveCard != 1)
//                return array('status' => 'error');
            $relPay = $totalPrice * 70/100;
            $discountPrice = $totalPrice - $relPay;
            if($discountPrice > 150*100){
                $relPay = $totalPrice - 150*100;
                $discountPrice = 150*100;
            }
        }
        return array('relPay' => $relPay,'discountPrice' => $discountPrice,'status' => 'success');
    }
    //回调更改订单状态
    public function callBackChangeData(){
        //验签
        $info = $this->signData();
        if($info['status']!=1){
            $yanqian['check'] = $info;
            $fail = $this->decodeUnicode(json_encode($yanqian));
            echo $fail;
            die();
        }
        $preOrderCode = I('preOrderCode');
        if($preOrderCode){
            $preOrderData = M('preorder')->where(array('preOrderCode' => $preOrderCode))->find();
            M()->startTrans();
           $resConsumeOrder = M('consumeorder')->where(array('orderCode' => $preOrderData['orderCode']))->setField('status',3);
           $resUserConsume = M('userconsume')->where(array('consumeCode' => $preOrderData['consumeCode']))->setField('status',3);
           $resPreOrder = M('preorder')->where(array('preOrderCode'=> $preOrderCode))->setField('status',1);
            $resUserOrder = M('usercoupon')->where(array('userCouponCode'=> $preOrderData['userCouponCode']))->setField('status',2);
           if($resConsumeOrder && $resUserConsume && $resPreOrder && $resUserOrder){
               M()->commit();
               $yanqian['check'] = $info;
               $yanqian['returnResult'] = 90001;
               $res = json_encode($yanqian);
               echo $this->decodeUnicode($res);
           }else{
               M()->rollback();
               $yanqian['check'] = $info;
               $yanqian['returnResult'] = 90002;
               $res = json_encode($yanqian);
               echo $this->decodeUnicode($res);
           }

        }
    }

}
