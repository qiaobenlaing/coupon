<?php
/**
 * Created by PhpStorm.
 * User: 10718
 * Date: 2018/3/26
 * Time: 10:49
 */
namespace Admin\Controller;
	use Think\Controller;
	use Common\Model\BankAccountModel;
    use Common\Model\BaseModel;
    use Common\Model\ClassRemarkImgModel;
    use Common\Model\ClassRemarkModel;
    use Common\Model\ClassWeekInfoModel;
    use Common\Model\ConsumeOrderModel;
    use Common\Model\CouponRuleModel;
    use Common\Model\MessageRecipientModel;
    use Common\Model\OrderCouponModel;
    use Common\Model\OrderProductModel;
    use Common\Model\PcAppLogModel;
    use Common\Model\PPRelModel;
    use Common\Model\ProductCategoryModel;
    use Common\Model\ProductModel;
    use Common\Model\RefundLogModel;
    use Common\Model\ShopAppLogModel;
    use Common\Model\ShopApplyEntryModel;
    use Common\Model\ShopClassModel;
    use Common\Model\ShopDecorationModel;
    use Common\Model\BatchCouponModel;
    use Common\Model\ShopDeliveryModel;
    use Common\Model\ShopHeaderModel;
    use Common\Model\ShopHonorModel;
    use Common\Model\ShopPhotoModel;
    use Common\Model\ShopRecruitModel;
    use Common\Model\ShopStaffRelModel;
    use Common\Model\ShopTeacherModel;
    use Common\Model\StudentStarModel;
    use Common\Model\StuStarWorkModel;
    use Common\Model\SubAlbumModel;
    use Common\Model\SystemParamModel;
    use Common\Model\TeacherWorkModel;
    use Common\Model\UserActCodeModel;
    use Common\Model\UserActivityModel;
    use Common\Model\FeedbackModel;
    use Common\Model\UserBonusModel;
    use Common\Model\UserCardModel;
    use Common\Model\ShopStaffModel;
    use Common\Model\ShopModel;
    use Common\Model\BankAccountLocalLogModel;
    use Common\Model\CardModel;
    use Common\Model\UserCouponModel;
    use Common\Model\BonusModel;
    use Common\Model\ActivityModel;
    use Common\Model\UserConsumeModel;
    use Common\Model\CommunicationModel;
    use Common\Model\UserMessageModel;
    use Common\Model\PosServerModel;
    use Common\Model\JpushModel;
    use Common\Model\MessageModel;
    use Common\Model\BonusStatisticsModel;
    use Common\Model\UserModel;
    use Common\Model\UtilsModel;
    use JPush\Exception\APIRequestException;
    use Common\Model\UserEnterShopInfoRecordModel;
    use Vendor\WeixinPay\WeixinNotify;
    use Vendor\WeixinPay\WeixinPayApi;

    class LcyController extends MwBaseController
{
    public function __construct() {
        parent::__construct();
        // 确保jsonrpc后面不被挂上一堆调试信息。
        C('SHOW_PAGE_TRACE', false);
    }

    //2019-06-17(乔本亮修改微信支付代金券，兑换券接口,代码的优化)
    public function Daipay(){
        //接口得到的参数有
        $userCode = I('post.userCode');
        $batchCouponCode = I('post.batchCouponCode');
        $consumeCode = I('post.consumeCode');

        //获取用户的openId 以及优惠券的功能说明function
        $userModel = new UserModel();
        $openId = $userModel->where(array('userCode'=>$userCode))->getField('openId');
        $batchModel = new BatchCouponModel();
        $function = $batchModel->where(array('batchCouponCode'=>$batchCouponCode))->getField('function');

        //获得订单编号和实际支付的金额
        $userConsumeModel = new UserConsumeModel();
        $orderInfo = $userConsumeModel->join('consumeorder ON consumeorder.orderCode=userconsume.orderCode')
            ->where(array('consumeCode'=>$consumeCode))
            ->field('ConsumeOrder.orderNbr,ConsumeOrder.orderAmount')->find();

        //封装数据请求微信支付接口(整理微信支付需要的数据)
        $data = array(
            'openId'=>$openId,
            'function'=>$function,
            'payPrice'=> $orderInfo['orderAmount']/100, //分转元
            'orderNbr'=>$orderInfo['orderNbr'],
            'consumeCode'=>$consumeCode
        );

        $weiXinPay = new WeixinPayApi();
        $jsapi  =   $weiXinPay->getJsApiParameters($data['payPrice'],$data['openId'],$data['orderNbr'],$data['consumeCode'],$data['function'],'');
        $return_data = array('jsapi'=>$jsapi);
        echo json_encode($return_data,JSON_UNESCAPED_UNICODE);
    }

    //2019-06-17(乔本亮修改微信支付抵扣券，折扣券接口,代码的优化)
	public function DiPay(){
        //接口得到的参数有
        $userCode = I('post.userCode');
        $orderNbr = I('post.orderNbr');
        $consumeCode = I('post.consumeCode');
        //获取用户的openId 以及优惠券的功能说明function
        $userModel = new UserModel();
        $openId = $userModel->where(array('userCode'=>$userCode))->getField('openId');

        $ConsumeOrderModel = new ConsumeOrderModel();
        $arr=$ConsumeOrderModel->field(array('realPayWeiXin','shopCode','orderNbr'))->where(array('orderNbr'=>$orderNbr))->find();
        $payPrice=$arr['realPayWeiXin'];
        $shopCode=$arr['shopCode'];
        $orderNbr=$arr['orderNbr'];
        $arrShopName=M('shop')->field('shopName')->where(array('shopCode'=>$shopCode))->find();
        $shopName=$arrShopName['shopName'];

        //封装数据请求微信支付接口(整理微信支付需要的数据)
        $data = array(
            'openId'=>$openId,
            'function'=>$shopName,
            'payPrice'=> $payPrice/100, //分转元
            'orderNbr'=>$orderNbr,
            'consumeCode'=>$consumeCode
        );

        $weiXinPay = new WeixinPayApi();
        $jsapi  =   $weiXinPay->getJsApiParameters($data['payPrice'],$data['openId'],$data['orderNbr'],$data['consumeCode'],$data['function'],'');
        $return_data = array('jsapi'=>$jsapi);
        echo json_encode($return_data,JSON_UNESCAPED_UNICODE);
    }

    //微信支付回调通知
    public function xiugai(){
        $notify = new WeixinNotify();
        $payback =   $notify->getResult();


//        $payback['result_code']='SUCCESS' ;
//        $payback['return_code']='SUCCESS';
//        $payback['attach'] = I('post.consumeCode');


        if($payback['result_code']==='SUCCESS' && $payback['return_code']==='SUCCESS'){
            $consumeCode = $payback['attach']; //附加的参数即为用户消费编号
            $userConsumeMdl     = new UserConsumeModel();
            // 判断支付账单中使用的优惠券是否可用
            $isCouponCanBeUse = $userConsumeMdl->isConsumeCouponCanBeUse($consumeCode);
            if($isCouponCanBeUse){
                $result =   $userConsumeMdl->bankcardPayConfirm2($consumeCode);
                if($result['code']==50000){
                    return true;
                }else{
                    return false;
                }
            }
        }else{
            return false;
        }
    }

    //微信退款
    public function weixinRefund() {
        $coupon_json = file_get_contents('php://input');
        $coupon_info = json_decode($coupon_json, true);
        $userCouponNbr = $coupon_info['userCouponNbr'];
        //根据userConponNbr查询到是该券是否可退
        $tuiCouponNbrArr = M('usercoupon')
            ->field('userCouponNbr')
            ->where(array('userCouponNbr' => array('in',$userCouponNbr),'status' => array('eq',1)))
            ->select();
        if(count($tuiCouponNbrArr) == 0) return false;
        $tuiCouponNbr = array();
        foreach ($tuiCouponNbrArr as $v){
            $tuiCouponNbr[] = $v['userCouponNbr'];
        }
        if(count($tuiCouponNbr) == 0) return false;
        $res = M('ordercoupon')
            ->field('a.orderCode,count(a.couponCode) couponNum,group_concat(a.couponCode) couponCode,a.costMoney,b.orderAmount,b.orderNbr,b.activityCode,b.clientCode,c.payPrice')
            ->alias('a')
            ->where(array('a.couponCode' => array('in',$tuiCouponNbr)))
            ->join('LEFT JOIN __CONSUMEORDER__ b ON a.orderCode = b.orderCode')
            ->join('LEFT JOIN __BATCHCOUPON__ c ON a.batchCouponCode = c.batchCouponCode')
            ->group('orderCode')
            ->select();
        $couponCount = M('ordercoupon')
            ->field('count(couponCode) totalCouponCount,orderCode')
            ->where(array('orderCode' => array('in',array_column($res,'orderCode')),'status' => array('eq',20)))
            ->group('orderCode')
            ->select();
        foreach ($res as $k => $v){
            foreach($couponCount as $v1){
                if($v['orderCode'] == $v1['orderCode'])
                    $res[$k]['totalCouponCount'] = $v1['totalCouponCount'];
            }
        }
        if(count($res) == 1){
            //退款的优惠券码属于一个订单
            $orderAmount = $res[0]['orderAmount'];
            $orderNbr = $res[0]['orderNbr'];
            $tuiPrice = $res[0]['costMoney'] * $res[0]['couponNum'];
            $weiXin = new WeixinPayApi();
            $weiXinInfo = $weiXin->Weixinrefund($orderNbr,$orderAmount,$tuiPrice);  //2019-06-17 乔本亮修改引入类的形式修改微信退款，
            if($weiXinInfo['return_code'] == 'SUCCESS' && $weiXinInfo['result_code'] == 'SUCCESS'){//更改状态
                M('usercoupon')->where(array('userCouponNbr' => array('in',$res[0]['couponCode'])))->setField('status',0);//用户优惠券
                M('ordercoupon')->where(array('couponCode' => array('in',$res[0]['couponCode'])))->setField('status',11);//订单优惠券表
                M('userconsume')->where(array("orderCode" => $res[0]['orderCode']))->setField('status',7);//用户消费记录表
                //订单部分退款
                if($res[0]['couponNum'] == $res[0]['totalCouponCount'])
                    M('consumeorder')->where(array("orderCode" => $res[0]['orderCode']))->setField('status',7);//订单表(全退）
                if($res[0]['couponNum'] < $res[0]['totalCouponCount'])
                    M('consumeorder')->where(array("orderCode" => $res[0]['orderCode']))->setField('status',8);//订单表（部分退）
                //如果有活动，更改让利状态,如果全退，更改状态，部分退，更改让利金额
                if(!empty($res[0]['activityCode'])){
                    if($res[0]['couponNum'] == $res[0]['totalCouponCount']) //订单里未退的全退的情况
                        M('amountdiscount')->where(array('orderCode' => $res[0]['orderCode']))->setField(array('status' => 3,'discount' => 0));
                    if($res[0]['couponNum'] < $res[0]['totalCouponCount']){//订单里面只退部分的情况,2019.07.16cm更改，amountdiscount表单里面的backdiscount的不再使用
                        $refundDiscount = ($res[0]['price']-$res[0]['costMoney']) * $res[0]['couponNum'];
                        M('amountdiscount')->where(array('orderCode' => $res[0]['orderCode']))->setDec('discount', $refundDiscount);
                    }
                }
                echo 'success';
//                echo json_encode(array('status' => 'success','msg' => '退款成功'));
                exit;
            }else{
                echo json_encode(array('status' => 'error','msg' => '退款失败'));
                exit;
            }
        }elseif (count($res) > 1){
            $refundSuccessOrderArr = array();
            $refundErrorOrderArr = array();
            $weiXin = new WeixinPayApi();
            //退款的优惠券码属于多个订单
            foreach ($res as &$v){
                $orderAmount = $v['orderAmount'];
                $orderNbr =$v['orderNbr'];
                $tuiPrice = $v['costMoney'] * $v['couponNum'];
                $weiXinInfo = $weiXin->Weixinrefund($orderNbr,$orderAmount,$tuiPrice);  //2019-06-17 乔本亮修改引入类的形式修改微信退款
                if($weiXinInfo['return_code'] == 'SUCCESS' && $weiXinInfo['result_code'] == 'SUCCESS'){
                    $refundSuccessOrderArr['orderCode'][] = $v['orderCode'];
                    $refundSuccessOrderArr['couponCode'] .= $v['couponCode'] . ',';
                    if($v['couponNum'] == $v['totalCouponCount']){//全退的订单
                        $refundSuccessOrderArr['allRefundOrderCode'][] = $v['orderCode'];
                        if(!empty($v['activityCode'])){//存在活动情况，让利更改
                            M('amountdiscount')->where(array('orderCode' => $v['orderCode']))->setField(array('status' => 3,'discount' => 0));
                        }
                    }
                    if($v['couponNum'] < $v['totalCouponCount']){//部分退的订单及金额
                        $refundSuccessOrderArr['notAllRefundOrderCode'][] = $v['orderCode'];
                        if(!empty($v['activityCode'])){//存在活动情况，让利更改
                            $refundDiscount = ($v['price']-$v['costMoney']) * $v['couponNum'];
                            M('amountdiscount')->where(array('orderCode' => $v['orderCode']))->setDec('discount', $refundDiscount);
                        }
                    }
                }else {
                    //退款失败的订单及优惠券码
                    $refundErrorOrderArr['orderCode'][] = $v['orderCode'];
                    $refundErrorOrderArr['couponCode'] .= $v['couponCode'] . ',';
                }
            }
            //更改退款成功的订单
//            M()->startTrans();
            M('usercoupon')->where(array('userCouponNbr' => array('in',$refundSuccessOrderArr['couponCode'])))->setField('status',0);//用户优惠券
            M('ordercoupon')->where(array('couponCode' => array('in',$refundSuccessOrderArr['couponCode'])))->setField('status' ,11);//订单优惠券表
            M('userconsume')->where(array("orderCode" => array('in',$refundSuccessOrderArr['orderCode'])))->setField("status" ,7);//用户消费记录表
            if(count($refundSuccessOrderArr['allRefundOrderCode']) > 0)
                M('consumeorder')->where(array("orderCode" => array('in',$refundSuccessOrderArr['allRefundOrderCode'])))->setField("status" ,7);//订单表(全退）
            if(count($refundSuccessOrderArr['notAllRefundOrderCode']) > 0)
                M('consumeorder')->where(array("orderCode" => array('in',$refundSuccessOrderArr['notAllRefundOrderCode'])))->setField("status" ,8);//订单表（部分退）
            if(count($refundErrorOrderArr) > 0){
                echo json_encode(array('msg' => '部分优惠券退款成功','status' => 'success','errorCouponCode' => $refundErrorOrderArr['couponCode']));
                exit;
            } else{
                echo 'success';
//                echo json_encode(array('msg' => '退款成功','status' => 'success'));
                exit;
            }
        }else{
            echo json_encode(array('msg' => '系统错误','status' => 'error'));
            exit;
        }
    }

    ////下面不需要看，为了以后参看卡券核销的功能实现，所以我没删
        function http_curl($url,$type='get',$res='json',$arr=''){
            //1.初始化curl
            $ch=curl_init();

            //2.设置curl的参数
            curl_setopt($ch, CURLOPT_URL, $url);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
            if($type=='post'){
                curl_setopt($ch, CURLOPT_POST, 1);
                curl_setopt($ch,CURLOPT_POSTFIELDS,$arr);
            }
            //3.采集
            $output=curl_exec($ch);
            //4.关闭curl
            curl_close($ch);
            if($res=='json'){
                return json_decode($output,true);
            }

        }

    //抵扣券折扣券核销 乔本亮（微信核销）
    private  function  consumeCouponCode($userCouponCode){

        //获取常量类定义的appid和secreat
        $appid  = C("AppID");
        $secret = C("AppSecret");

        //判断用户优惠券是否可用
        $userCouponInfo = D("UserCoupon")->where(array("userCouponCode"=>$userCouponCode,"status"=>1))
            ->field("userCouponCode,userCouponNbr,cardId,relationOpenid")->find();

        if(!$userCouponInfo){
            return false;
        }

        //检查优惠券是否可核销
        $access_token = S("access_token");
        if(!$access_token){
            $url ="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=".$appid."&secret=".$secret;
            $data =   json_decode($this->http_post_data($url));
            $access_token = $data->access_token;
            S("access_token",$access_token,7000);
        }

        $url ="https://api.weixin.qq.com/card/code/get?access_token=".$access_token;
        $data ='{
                "card_id" : "'.$userCouponInfo['cardId'].'",
               "code" : "'.$userCouponInfo['userCouponNbr'].'",
               "check_consume" : true
        }';

        $res  = $this->http_post_data($url,$data);

        $info = json_decode($res);

        //调用微信核销卡券接口
        if($info->errcode!==0){
            return false;// 优惠券状态存在异常
        }else{
            $url ="https://api.weixin.qq.com/card/code/consume?access_token=".$access_token;
            $data ='{
                "code":"'.$userCouponInfo['userCouponNbr'].'",
                 "card_id": "'.$userCouponInfo['cardId'].'"
            }';
            $res  = $this->http_post_data($url,$data);
            $info = json_decode($res);

            if($info->errcode!==0){
                return false;// 优惠券核销失败
            }
            return true;//核销成功（触发微信的核销事件）
        }
    }


    // 抓取https内容
    private function http_post_data($url,$data=array(),$useSSL = false,$outtime = 30){
        $ch = curl_init();
        curl_setopt ( $ch, CURLOPT_SAFE_UPLOAD, false);
        curl_setopt($ch, CURLOPT_URL, $url);
        //设置超时
        curl_setopt($ch, CURLOPT_TIMEOUT, $outtime);
        // ssl 方式提交
        if($useSSL){
            curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);
        }
        curl_setopt($ch, CURLOPT_HEADER, false);
        curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
        curl_setopt($ch, CURLOPT_REFERER, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
        //post提交方式
        if(!empty($data)){
            curl_setopt($ch, CURLOPT_POST, TRUE);
            curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
        }
        $result = curl_exec($ch);
        curl_close($ch);
        return $result;
    }


	//更新微信卡券会员信息
	public function updateVipInfo(){

			header('content-type:text/html;charset=utf-8');
			$balance=$_POST['total_fee'];
			$urlAccess="http://wx.huift.com.cn/WechatConfirm/transfer/getAccessToken?appid=HQ0000&rtype=1";
			$res=$this->http_curl($urlAccess,'post','json',$resp);
			$access_token=$res['access_token'];


			 $url="https://api.weixin.qq.com/card/membercard/updateuser?access_token=".$access_token;
			 $postArr=array(
			  "code"=> "133123456789",
			"card_id"=> "pboBC0WKty_7IXT5_nHEYdV68iEM",

    "record_bonus"=> urlencode("消费30元，获得3积分"),
    "bonus"=> 3000,
     "add_bonus"=> 30,
    "balance"=> $balance,
    "add_balance"=> $balance,
    "record_balance"=> urlencode("充值金额30元。"),
     "custom_field_value1"=> urlencode("白金"),
      "custom_field_value2"=> urlencode("8折"),
    "notify_optional"=> array(
        "is_notify_bonus"=> true,
        "is_notify_balance"=> true,
        "is_notify_custom_field1"=>true
    )
			 );
			 $postJson=urldecode(json_encode($postArr));
			$bb=$this->http_curl($url,'post','json',$postJson);

	}


}











