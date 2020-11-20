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

class LcyController extends MwBaseController
{
    public function __construct() {
        parent::__construct();
        // 确保jsonrpc后面不被挂上一堆调试信息。
        C('SHOW_PAGE_TRACE', false);
//        $this->pager = new Pager(I('page', 1, 'intval'), I('pageSize', Pager::DEFUALT_PAGE_SIZE, 'intval'));
    }

	//代金券，兑换券
	public function Daipay(){
		
		$user=D('User');
		$batchCoupon=D('BatchCoupon');
		$batchCouponCode=$_POST['batchCouponCode'];
		$userCode=$_POST['userCode'];
//
		$consumeCode=$_POST['consumeCode'];
		$arrOpenId=$user->field('openId')->where("userCode='$userCode'")->find();

		$openId=$arrOpenId['openId'];
		//根据consumeCode查询到orderCode
		$userConsume=D('UserConsume');
		$arrOrderCode=$userConsume->field('orderCode')->where("consumeCode='$consumeCode'")->find();
		$orderCode=$arrOrderCode['orderCode'];
		//根据orderCode查询到支付金额
		$consumeCodeModel=D('ConsumeOrder');
		$arrOrderAmount=$consumeCodeModel->field(array('orderAmount','orderNbr'))->where("orderCode='$orderCode'")->find();
		$payPrice=$arrOrderAmount['orderAmount'];
		$orderNbr=$arrOrderAmount['orderNbr'];
		$arr=$batchCoupon->field(array("function","payPrice"))->where("batchCouponCode='$batchCouponCode'")->find();
		$function=$arr['function'];

		$GLOBALS['consumeCode']=$consumeCode;
		$GLOBALS['openId']=$openId;
		$GLOBALS['function']=$function;
		$GLOBALS['payPrice']=$payPrice;
		$GLOBALS['orderNbr']=$orderNbr;

        $GLOBALS['batchCouponCode']    = $batchCouponCode;

		require_once "/data/wwwroot/hq.hkctsbus.com/huiquan/example/jsapi.php";
		
		
	}
	
	//抵扣券，折扣券
	public function DiPay(){
		$consumeCode=$_POST['consumeCode'];
		$orderNbr=$_POST['orderNbr'];
		$userCode=$_POST['userCode'];
	
		$user=D('User');
		$shop=D('Shop');
		$ConsumeOrder=D('ConsumeOrder');
			//根据userCode查询openId
			$arrOpenId=$user->field('openId')->where("userCode='$userCode'")->find();
			
			$openId=$arrOpenId['openId'];
			
		
			$arr=$ConsumeOrder->field(array('realPayWeiXin','shopCode','orderNbr'))->where("orderNbr='$orderNbr'")->find();
			$payPrice=$arr['realPayWeiXin'];
			$shopCode=$arr['shopCode'];
			$orderNbr=$arr['orderNbr'];
			$arrShopName=$shop->field('shopName')->where("shopCode='$shopCode'")->find();
			$shopName=$arrShopName['shopName'];
			
		
			$GLOBALS['openId']=$openId;
			$GLOBALS['function']=$shopName;
			$GLOBALS['payPrice']=$payPrice;
			$GLOBALS['consumeCode']=$consumeCode;
			$GLOBALS['orderNbr']=$orderNbr;

			require_once "/data/wwwroot/hq.hkctsbus.com/huiquan/example/jsapi.php";
		
	}

	
	/**
     * 微信确认退款
     */
	public function weixinRefund() {

		$userCouponModel=D('UserCoupon');
		$OrderCoupon=D('OrderCoupon');
		$consumeOrderModel=D('ConsumeOrder');
		$coupon_json = file_get_contents('php://input');

		$coupon_info = json_decode($coupon_json, true);
		$userCouponNbr=$coupon_info['userCouponNbr'];

		
		//根据userConponNbr查询到是该券是否可退
		

		
		foreach($userCouponNbr as $val){
         $arrStatus=$userCouponModel->field('status')->where("userCouponNbr='$val'")->find();
		
		 if($arrStatus['status']==1){
			 $tuiCouponNbr[]=$val;
			}
        }
	
		//可以退的优惠券个数
		$num= count($tuiCouponNbr);

		if($num==0){
			echo  "error";
			exit();
		}


		//获取订单号
		$arrOrderCode=$OrderCoupon->field('orderCode')->where("couponCode='$tuiCouponNbr[0]'")->find();
		
		
		$orderCode=$arrOrderCode['orderCode'];
		$orderList=$consumeOrderModel->field(array('orderAmount','orderNbr'))->where("orderCode='$orderCode'")->find();
	
		//根据orderNbr查询到订单总金额
		$orderAmount=$orderList['orderAmount'];
		$orderNbr=$orderList['orderNbr'];
		//根据orderCode查询到batchCouponde
		$arrBatchCouponCode=$OrderCoupon->field('batchCouponCode')->where("orderCode='$orderCode'")->find();
		$batchCouponCode=$arrBatchCouponCode['batchCouponCode'];
		
		//根据batchCouponCode查询到payPrice
		$batchCouponModel=D('BatchCoupon');
		$arrpayPrice=$batchCouponModel->field('payPrice')->where("batchCouponCode='$batchCouponCode'")->find();
		$payPrice=$arrpayPrice['payPrice'];
		$tuiPrice=$num*$payPrice;
		$GLOBALS['refund_fee']=$tuiPrice;
		$GLOBALS['total_fee']=$orderAmount;
		$GLOBALS['out_trade_no']=$orderNbr;
		$weixinInfo=require_once "/data/wwwroot/hq.hkctsbus.com/huiquan/example/refund.php";

		if($weixinInfo['return_code']=='SUCCESS'&&$weixinInfo['result_code']=='SUCCESS'){
			foreach($tuiCouponNbr as $val){
				$data->status=0;
				$userCouponModel->where("userCouponNbr='$val'")->save($data);
				$data2->status=11;
				$OrderCoupon->where("couponCode='$val'")->save($data2);
			}	
			echo "success";
		}else{
			echo "fail";
		}
	}
	
	
	//微信卡包支付参数
	public function weiPay(){

		$userCode=$_POST['userCode'];
		$payPrice=$_POST['payPrice'];
	
		$userModel=D('User');
		$arrOpenId=$userModel->field('openId')->where("userCode='$userCode'")->find();
		$openId=$arrOpenId['openId'];
		$GLOBALS['openId']=$openId;
		$GLOBALS['payPrice']=$payPrice;

		require_once "/data/wwwroot/hq.hkctsbus.com/huiquan/example/weiPay.php";
		
	}
	

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


	//判断优惠券是否可用
    public function xiugai(){

        $consumeCode        = $_POST['consumeCode'];
        $userConsumeMdl     = new UserConsumeModel();
        // 判断支付账单中使用的优惠券是否可用
        $isCouponCanBeUse = $userConsumeMdl->isConsumeCouponCanBeUse($consumeCode);

        if($isCouponCanBeUse) {

            //先判断是否在微信卡包
            //查询到用户优惠券信息
            $userCouponMdl = new UserCouponModel();
            //1.先判断用户是否添加到微信卡包(不存在 则不用管 存在则核销掉微信卡包的优惠券)
            $userCoupon = $userCouponMdl->where(array("consumeCode"=>$consumeCode))->field("userCode,userCouponNbr,userCouponCode,inWeixinCard")->find();

            if($userCoupon['inWeixinCard']!=1){

                $userConsumeMdl->bankcardPayConfirm2($consumeCode);

            }else{

                  $res = $this->consumeCouponCode($userCoupon['userCouponCode']);

                  if($res){

                        $userConsumeMdl->bankcardPayConfirm2($consumeCode);

                  }else{

                      $userCouponMdl->where(array("userCouponCode"=>$userCoupon['userCouponCode']))->save(array("state"=>0,"inWeixinCard"=>2));
                  }
            }
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











