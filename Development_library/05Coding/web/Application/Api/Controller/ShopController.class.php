<?php
	 /**
     * 收藏商家列表
     */
    /**
     * 商店端API Controller
     * User: Weiping
     * Date: 2015-04-20
     * Time: 23:49
     */
namespace Api\Controller;
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
use \Think\Cache\Driver\Memcache;
use  Org\aliyun\api_demo\WxPayData;

class ShopController extends ApiBaseController {
    private $discountRatio = 10;
    // 一天少一秒的时间，单位：秒。
    private $dayLessOneSecond = 86399;


    public function collectShop($userCode,$longitude,$latitude){

		$CollectShopModel=D('CollectShop');
	   $shop=D('Shop');
        //查询优惠券编码和客户是否收藏
        $colle=$CollectShopModel->field(array('isCollect'))
            ->where("userCode='$userCode'")
            ->select();

			 $isHave=$CollectShopModel->where("isCollect=1 AND userCode='$userCode'")->count();
			if($isHave<1){
				return "null";
			}else{
        foreach ($colle as $value){//1收藏  2没收藏
		$shopCode=$value['collectCouponCode'];
         if($value['isCollect']==1){

			 $h=$shop->field(array(
		    'CollectShop.shopCode',
			'shopName',
			'type',
			'country',
			'province',
			'city',
			'district',
			'logoUrl',
			 'sqrt(power(Shop.longitude-'.$longitude.',2) + power(Shop.latitude-'.$latitude.',2))' => 'distance',
			'street',
			'CollectShop.isCollect',
			))
			 ->join('CollectShop on CollectShop.shopCode=Shop.shopCode','Left')
			->where("userCode='$userCode'And isCollect=1")
			->select();
			$d[]=$h;
		 }
        }
			return array_unique($d);
			}


	}


	/**
     * 显示商家是否收藏,客户点击收藏商家按钮
     */
    public function showCollect($shopCode,$userCode){
		 $arr=array(
            'userCode'=>$userCode,
            'shopCode'=>$shopCode,
			'registerTime' => date('Y-m-d H:i:s', time())
        );
        $CollectShopModel=D('CollectShop');
		//查询是客户是否收藏过这家店铺，如果没有进行add操作
		$shopCount=  $CollectShopModel->where("userCode='$userCode' AND shopCode='$shopCode'")->count();
		if($shopCount<1){
			 $CollectShopModel->add($arr);
		}

        //查询客户type属性，看是否已经收藏这个商店

        $isCollect=$CollectShopModel->field("isCollect")
            ->where("userCode='$userCode' AND shopCode='$shopCode'")
            ->select();


        foreach ($isCollect as $value){//1收藏  0没收藏
            if($value['isCollect']==1){
					$CollectShopModel->isCollect='0';
					$CollectShopModel->where("userCode='$userCode' AND shopCode='$shopCode'")->save();
					$isCollect=0;
            }else{
				$CollectShopModel->isCollect='1';
				$CollectShopModel->where("userCode='$userCode' AND shopCode='$shopCode'")->save();
                $isCollect=1;
            }
			return $isCollect;
        }


    }

	/**
     * 显示客户是否收藏，客户点击收藏优惠券
     */
	public function collectCoupon($batchCouponCode,$userCode){

		$arr=array(
            'collectCouponCode'=>$batchCouponCode,
			'userCode'=>$userCode,
			'registerTime' => date('Y-m-d H:i:s', time()),
        );


		$CollectCouponModel=D('CollectCoupon');

		//查询客户是否收藏过这个优惠券，如果没有进行add操作
		$couponCount=$CollectCouponModel->where("collectCouponCode='$batchCouponCode' AND userCode='$userCode'")->count();
		if($couponCount<1){
			 $CollectCouponModel->add($arr);
		}

		//查询isCollect是否为1收藏
		$isCollect=$CollectCouponModel->field("isCollect")
									->where("collectCouponCode='$batchCouponCode'AND userCode='$userCode'")
									->select();




			foreach($isCollect as $value){
				if($value['isCollect']==1){
					$CollectCouponModel->isCollect='0';
					$CollectCouponModel->where("collectCouponCode='$batchCouponCode'")->save();
					$isCollect=0;
				}else{
					$CollectCouponModel->isCollect='1';
					$CollectCouponModel->where("collectCouponCode='$batchCouponCode'")->save();
					$isCollect=1;
				}
				return $isCollect;
			}

	}



	 /**
     * 展示优惠券收藏列表
     * @param string $shopCode
     * @return array
     */
	public function showCoupon($userCode){
		 $CollectCouponModel=D('CollectCoupon');
		 $batchCoupon=D('BatchCoupon');
        //查询优惠券编码和客户是否收藏
        $colle=$CollectCouponModel->field(array('isCollect','collectCouponCode'))
            ->where("userCode='$userCode'")
            ->select();

			 $isHave=$CollectCouponModel->where("isCollect=1 AND userCode='$userCode'")->count();
			if($isHave<1){
				return "null";
			}else{
        foreach ($colle as $value){//1收藏  2没收藏
		$collectCouponCode=$value['collectCouponCode'];
         if($value['isCollect']==1){

			 $h=$batchCoupon->field(array(
		    'CollectCoupon.collectCouponCode',
            'insteadPrice',
            'discountPercent',
            'availablePrice',
            'function',
            'payPrice',
            'totalVolume',
            'remaining',
            'startUsingTime',
            'expireTime',
            'BatchCoupon.remark',
            'Shop.shopCode',
            'Shop.shopName',
            'Shop.mobileNbr',
            'Shop.street',
            'nbrPerPerson',
            'logoUrl',
            'url',
            'couponName',
            'BatchCoupon.remark',
            'BatchCoupon.batchNbr',
            'BatchCoupon.couponType',
            'exRuleList',
            'dayStartUsingTime',
            'dayEndUsingTime',
			'CollectCoupon.isCollect',
			'CollectCoupon.isGet'
			))
			->join('Shop on Shop.shopCode = BatchCoupon.shopCode')
		->join('BackgroundTemplate on BackgroundTemplate.bgCode = BatchCoupon.bgImgCode')
		->join('CollectCoupon on CollectCoupon.collectCouponCode=BatchCoupon.batchCouponCode','Left')
		->where("collectCouponCode='$collectCouponCode' And userCode='$userCode'")
        ->select();
		//分转换成元
		$h[0]['insteadPrice']=$h[0]['insteadPrice']/100;
		$h[0]['availablePrice']=$h[0]['availablePrice']/100;
		$h[0]['payPrice']=$h[0]['payPrice']/100;
		$h[0]['discountPercent']=$h[0]['payPrice']/10;//折扣百分比

		$d[]=$h;
			}
        }
		return $d;
			}
	}


    /**
     *    查询客户领取优惠券流水号(乔本亮修改)
     */
    public function getUserCouponNbrByQiao($userCode,$batchCouponCode){
        $UserCouponModel=D('UserCoupon');
        $ConsumeOrderModel=D('ConsumeOrder');

        $userCouponNbr = array();
        $orderNbrInfo = array();
        if(!empty($userCode)&&!empty($batchCouponCode)){
            $userCouponNbr=$UserCouponModel->field(array('userCouponNbr','status','userCouponCode',"inWeixinCard"))
                ->where("userCode='$userCode' And UserCoupon.batchCouponCode='$batchCouponCode'")
                ->select();
        }

        if(!empty($userCouponNbr)){
            $orderCouponModel=D('OrderCoupon');
            foreach($userCouponNbr as $val){
                $userCouponNbr=$val['userCouponNbr'];
                $ArrorderCode=$orderCouponModel->field('orderCode')->where("couponCode='$userCouponNbr'")->find();
                $orderCode=$ArrorderCode['orderCode'];
                $ArrOrderNbr=$ConsumeOrderModel->field('orderNbr')->where("orderCode='$orderCode'")->find();
                $orderNbr=$ArrOrderNbr['orderNbr'];
                $val['orderNbr']=$orderNbr;
                $orderNbrInfo[] =$val;
            }
        }

        return $orderNbrInfo;
    }



	 /**
     *    查询客户领取优惠券流水号
     */
	public function getUserCouponNbr($userCode,$batchCouponCode){


        $UserCouponModel=D('UserCoupon');
        $ConsumeOrderModel=D('ConsumeOrder');
        $userCouponNbr=$UserCouponModel->field(array('userCouponNbr','status','userCouponCode',"inWeixinCard"))
            ->where("userCode='$userCode' And UserCoupon.batchCouponCode='$batchCouponCode'")
            ->select();

        $orderCouponModel=D('OrderCoupon');
        foreach($userCouponNbr as $val){
           $userCouponNbr=$val['userCouponNbr'];
           $ArrorderCode=$orderCouponModel->field('orderCode')->where("couponCode='$userCouponNbr'")->find();
           $orderCode=$ArrorderCode['orderCode'];
           $ArrOrderNbr=$ConsumeOrderModel->field('orderNbr')->where("orderCode='$orderCode'")->find();
           $orderNbr=$ArrOrderNbr['orderNbr'];
           $val['orderNbr']=$orderNbr;
           $orderNbrInfo[]=$val;
        }

        return $orderNbrInfo;
	}

	/**
     *    查询优惠券是否收藏，领取
     */
	public function isCollectGet($userCode){

            $CollectCouponModel=D('CollectCoupon');
            $collectCoupon=$CollectCouponModel->field(
                array(
                    'collectCouponCode',
                    'isCollect',
                    'isGet',
                    'isGetCount'
                ))
                ->where("userCode='$userCode'")
                ->select();

		return $collectCoupon;
	}

	/**
	 * 	登陆
	 */
	public function BankLogin($IcbcApp,$WeiApp){
		$userModel=D('User');
		if(!empty($IcbcApp)){
			$c=$IcbcApp;
			$couponCount=$userModel->where("IcbcApp='$IcbcApp'")->count();
		}else{
			$b=$WeiApp;
			$couponCount=$userModel->where("WeiApp='$WeiApp'")->count();
		}
		$nickNameRand=rand(100000000,999999999);
		$nickName='hft_'.$nickNameRand;
		if($couponCount<1){
			include ('Common/Model/CollectCouponModel.class.php');
			$user=new UserModel();
			$userCode=$user->create_uuid();
			$arr=array(
            'userCode'=>$userCode,
			'registerTime' => date('Y-m-d H:i:s', time()),
			'userId'=>$userCode,
			'nickName'=>$nickName,
			'IcbcApp'=>$c,
			'WeiApp'=>$b,
			);
			$userModel->add($arr);
			return array(
			'userCode'=>$userCode,
			'mobileNbr'=>-1,
			'nickName'=>$nickName
			);
		}else{
			if(!empty($IcbcApp)){
				$arrUserCode=$userModel->field(array('userCode','mobileNbr','nickName'))->where("IcbcApp='$IcbcApp'")->find();
			$userCode=$arrUserCode['userCode'];
			$mobileNbr=$arrUserCode['mobileNbr'];
			$nickName=$arrUserCode['nickName'];
			return array(
			'userCode'=>$userCode,
			'mobileNbr'=>$mobileNbr,
			'nickName'=>$nickName
			);
			}else{
			$arrUserCode=$userModel->field(array('userCode','mobileNbr','nickName'))->where("WeiApp='$WeiApp'")->find();
			$userCode=$arrUserCode['userCode'];
			$mobileNbr=$arrUserCode['mobileNbr'];
			$nickName=$arrUserCode['nickName'];
			return array(
			'userCode'=>$userCode,
			'mobileNbr'=>$mobileNbr,
			'nickName'=>$nickName

			);
			}

		}
	}

	/**
	 *绑定手机号
	 */
	  public function bindMobile($userCode,$mobileNbr,$Vcode){
        $UserModel=D('User');

        if($Vcode==111111){
            //判断手机号是否已经绑定
            $isMobile=$UserModel->where("mobileNbr='$mobileNbr'")->count();
            if($isMobile<1){
                $UserModel->mobileNbr=$mobileNbr;
                $UserModel->where("userCode='$userCode'")->save();
                return array(
                    'code'=>50000,
                    'mobileNbr'=>$mobileNbr
                );
            }else{
                return array(
                    'error'=>'手机号已经被绑定'
                );
            }
        }else{
            return array(
                'code'=>20000,
                'error'=>'验证码错误'
            );
        }


    }



	/**
     * 验证码手机号登陆
     */
	public function mobileLogin($mobileNbr,$Vcode){

		$zhengze= "/^1[34578]\d{9}$/";
		if(preg_match($zhengze,$mobileNbr)){
			$UserModel=D('User');
		//查询是否存在这条手机号记录
		$isCount=$UserModel->where("mobileNbr='$mobileNbr'")->count();
		if($isCount==1){
			if($Vcode==111111){
				$arrUserCode=$UserModel->field('userCode')->where("mobileNbr='$mobileNbr'")->find();
				return array(
				"Code"=>50000,
				"userCode"=>$arrUserCode['userCode'],
				"mobileNbr"=>$mobileNbr
				);

			}else{
				return array(
					"error"=>"验证码错误"
				);
			}

		}else{
			return array(
			"error"=>"手机号不存在"
			);
		}
	}else{
		return "手机号不合法";
	}



	}





	/**
	 *用户H5平台注册
	 */
	public function registerMobile($mobileNbr,$Vcode){
		 $UserModel=D('User');
		//查询是否存在这条手机号记录
		$isCount=$UserModel->where("mobileNbr='$mobileNbr'")->count();
		if($isCount<1){
			if($Vcode==111111){
				$user=new UserModel();
				$userCode=$user->create_uuid();
				$arr=array(
				'mobileNbr'=>$mobileNbr,
				'registerTime' => date('Y-m-d H:i:s', time()),
				'userCode'=>$userCode,
				'userId'=>$userCode
				);
				//添加用户
				$UserModel->add($arr);
				//查询用户userCode
				$arrUserCode=$UserModel->where("mobileNbr='$mobileNbr'")->find();
				return array(
				"code"=>50000,
				"success"=>"注册成功!",
				"userCode"=>$arrUserCode['userCode']
				);

			}else{
				return array(
				"error"=>"验证码错误"
				);
			}
		}else{
			return array(
				"error"=>"您已是我们平台用户"
			);
		}
	}


	/**
	 *H5发送验证码
	 */
	public function send($mobileNbr){

	$mem = new Memcache;
	import('Org.aliyun.api_demo.SmsDemo');
    $sms=new  \SmsDemo();
    $sms->sendSms($mobileNbr);
    return array(
	"Vcode"=>$mem->get('vcode1')
	);

}

    /**
     *获得优惠券详情
     */
    public function  getBachCouponInfo($batchCouponCode,$userCode){
        $BatchCouponModel=D('BatchCoupon');
        $BatchCouponInfo =$BatchCouponModel->where("batchCouponCode='$batchCouponCode'")->find();

        //获取用户领取的该优惠券的数量
        $listUserCoupon =      D("UserCoupon")->where(array("userCode"=>$userCode,"batchCouponCode"=>$batchCouponCode))->field("userCode,batchCouponCode")->select();
        $countMyReceived = count($listUserCoupon);

        //根据shopCode查找商家装饰信息、
        $ShopDecorationModel=D('ShopDecoration');
        $shopCode=$BatchCouponInfo['shopCode'];
        $ShopDecoration=$ShopDecorationModel
            ->field(array(
                'Shop.shopName',
                'Shop.popularity',
                'Shop.city',
                'Shop.street',
                'decorationCode',
                'ShopDecoration.shopCode',
                'ShopDecoration.type',
                'ShopDecoration.shortDes',
                'detailDes',
                'imgUrl',
                'audioUrl',
                'createTime',
                'title',
                'Shop.tel'
            ))
            ->join('Shop on Shop.shopCode = ShopDecoration.shopCode','right')
            ->where("Shop.shopCode='$shopCode'")
            ->find();


        //将分转换成元
        $BatchCouponInfo['insteadPrice']=$BatchCouponInfo['insteadPrice']/100;
        $BatchCouponInfo['availablePrice']=$BatchCouponInfo['availablePrice']/100;
        $BatchCouponInfo['canTakePrice']=$BatchCouponInfo['canTakePrice']/100;
        $BatchCouponInfo['payPrice']=$BatchCouponInfo['payPrice']/100;
        $BatchCouponInfo['discountPercent']=$BatchCouponInfo['discountPercent']/10;

        return array("BatchCouponInfo"=>$BatchCouponInfo, "ShopDecoration"=>$ShopDecoration, "countMyReceived"=>$countMyReceived);
    }


	/**
	 *H5用户查询头像
	 */
	public function getUserHeadImage($userCode){
		$userModel=D('User');
		$imgInfo=$userModel->field('avatarUrl')->where("userCode='$userCode'")->find();
		return $imgInfo;

	}

	/**
	 *根据orderCode生成报文
	 */
	public function getConsumeOrderInfo($orderNbr){
		$ConsumeOrder=D('ConsumeOrder');
		$OrderInfo=$ConsumeOrder->field(array('orderCode','orderNbr','status'))->where("orderNbr='$orderNbr'")->find();
		return $OrderInfo;
	}

	/**
	 *接收支付通知修改订单状态（已支付）
	 */
	public function getPayNotice($Code){


		if($Code==50000){
			return 'success';
		}
	}


	public function addPayCode($orderCode){
		if(isset($orderCode)){
			$ConsumeOrder=D('ConsumeOrder');
		$user=new UserModel();
		$payCode=$user->create_uuid();
		$data->payCode=$payCode;
		$arr=$ConsumeOrder->where("orderCode='$orderCode'")->save($data);
		return $payCode;
		}else{
			return error;
		}

	}




	public function getOrderNbrBo($orderCode,$payCode){

			$ZhiFuModel=D('ZhiFu');
			$arr=array(
			"orderCode"=>$orderCode,
			"payCode"=>$payCode
			);
			$ZhiFuModel->add($arr);
			return 50000;
	}

	public function  getPayCode($orderCode){
		$ZhiFuModel=D('ZhiFu');
		$arr=$ZhiFuModel->field('payCode')->where("orderCode='$orderCode'")->find();

		return $arr['payCode'];
	}

	public function  getSuccess($payCode,$status){

		//根据payCode查询到orderCode
			$ConsumeOrder=D('ConsumeOrder');
			$UserCoupon=D('UserCoupon');
			$UserConsumeModel=D('UserConsume');
			$BatchCoupon=D('BatchCoupon');

			$arrOrderCode=$ConsumeOrder->field('orderCode')->where("payCode='$payCode'")->find();
			$orderCode=$arrOrderCode['orderCode'];

			$arr=$UserConsumeModel->field('consumeCode')->where("orderCode='$orderCode'")->find();
			$consumeCode=$arr['consumeCode'];

		if($status=='success'){
			 $userConsumeMdl = new UserConsumeModel();
			  $isCouponCanBeUse = $userConsumeMdl->isConsumeCouponCanBeUse($consumeCode);
			  if($isCouponCanBeUse) {
			 $userConsumeMdl->bankcardPayConfirm2($consumeCode);
			  } else {

            $ret = array('code' => C('API_INTERNAL_EXCEPTION'));
        }

			return array(
			"consumeCode"=>$consumeCode,
			"info"=>"success"
			);
		}else if($status==5){

			$data->status=2;
			$arr=$ConsumeOrder->where("orderCode='$orderCode'")->save($data);
			$data->status=1;
			$UserCoupon->where("consumeCode='$consumeCode'")->save($data);
			//取消支付订单
			$userConsumeMdl = new UserConsumeModel();

			$userConsumeMdl->cancelBankcardPay($consumeCode);
			return array(
			"consumeCode"=>$consumeCode,
			"info"=>"error"
			);
		}else if ($status==2){
			return "支付中";
		}
	}



	public function  isSuccess($orderCode){
		$ConsumeOrder=D('ConsumeOrder');
		$arr=$ConsumeOrder->field('status')->where("orderCode='$orderCode'")->find();
		if($arr['status']==3){
			return "success";
		}else if($arr['status']==2){
			return "支付中";
		}else if($arr['status']==5){
			return "支付失败";
		}
	}


	 /**
     * H5购买优惠券的确认退款
     */
     public function couponOrderApplyRefundH5($userCouponNbr,$refundReason,$refundRemark) {

		 $orderCoupon=D('OrderCoupon');

		 //根据userCouponNbr查询orderCouponCodeList
		$arrorderCouponCode= $orderCoupon->field('orderCouponCode')->where("couponCode='$userCouponNbr'")->find();

		$orderCouponCodeList=$arrorderCouponCode['orderCouponCode'];
			//查询是否退款过
			$arrstatus=$orderCoupon->field('status')->where("orderCouponCode='$orderCouponCodeList'")->find();
			if($arrstatus['status']==11){
				return "已经退款过了";
			}else{

			if(empty($orderCouponCodeList)) {

				return "请选择优惠券";
            } else {
                $orderCouponMdl = new OrderCouponModel();

                // 购买的优惠券的申请退款
                $applyRet = $orderCouponMdl->couponOrderApplyRefund($orderCouponCodeList,$refundReason,$refundRemark);

                // 购买的优惠券的退款
                $refundRet = $orderCouponMdl->refundOrderCoupon($orderCouponCodeList);

//                if($applyRet['code'] == C('SUCCESS') && $refundRet == true) {
                if($refundRet === true) {
                   return "退款成功";
                } else {
                    switch($refundRet) {
                        case C('REFUND.CAN_NOT_REFUND_APART'):
                            $msg = '当日订单不能部分退款';
                            break;
                        default:
                            $msg = '申请退款失败，请重试';
                    }
					return $msg;
                }
					}


				}



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




		  //获取用户基本信息
	function  getUserInfo($openId,$access_token){
		//根据openId查询有没有客户信息


		$userModel=D('User');
		$user=new UserModel();
		$OpenIdCount=$userModel->where("openId='$openId'")->find();

		if(!empty($OpenIdCount)){


			$UserInfo=$userModel->where("openId='$openId'")->find();
			return $UserInfo;
		}else{

			if(!isset($access_token)){

		//$userModel=D('User');
		$nickNameRand=rand(100000000,999999999);
		$nickName='hft_'.$nickNameRand;
		//$user=new UserModel();
		$userCode=$user->create_uuid();
		$arr=array(
            'userCode'=>$userCode,
			'registerTime' => date('Y-m-d H:i:s', time()),
			'userId'=>$userCode,
			'nickName'=>$nickName,
			'openId'=>$openId
			);
			$userModel->add($arr);
			$UserInfo=$userModel->where("openId='$openId'")->find();
			return $UserInfo;
		}else{

		//session("openId",$openId);
		 header('content-type:text/html;charset=utf-8');
		  $url="https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=".$access_token;
		//查询是否有openId
		$User=D('User');
		$OpenIdCount=$User->where("openId='$openId'")->count();
		$user=new UserModel();
		$userCode=$user->create_uuid();
		if($OpenIdCount<1){
			 $postArr2=array(
            'user_list'=>array(
                array(
                    'openid'=>$openId,
                    'lang'=>"zh_CN"
					)
				)
			);

			$postJson=urldecode(json_encode($postArr2));
			$res2=$this->http_curl($url,'post','json',$postJson);

			$arr=array(
            'userCode'=>$userCode,
			'registerTime' => date('Y-m-d H:i:s', time()),
			'userId'=>$userCode,
			'nickName'=>$res2['user_info_list']['0']['nickname'],
			'province'=>$res2['user_info_list']['0']['province'],
			'city'=>$res2['user_info_list']['0']['city'],
			'avatarUrl'=>$res2['user_info_list']['0']['headimgurl'],
			'sex'=>$res2['user_info_list']['0']['sex'],
			'openId'=>$openId
			);
			$user->add($arr);

		}

		$UserInfo=$User->where("openId='$openId'")->find();
		return $UserInfo;
		}



		}





    }
	//代金券，兑换券
	 function  weipay($batchCouponCode,$userCode){
		 $batchCoupon=D('BatchCoupon');
		 $user=D('User');
		 $arrOpenId=$user->field('openId')->where("userCode='$userCode'")->find();
		 $openId=$arrOpenId['openId'];
		 $arr=$batchCoupon->field(array("function","payPrice"))->where("batchCouponCode='$batchCouponCode'")->find();

		 return  array(
		 'openId'=>$openId,
		 'info'=>$arr
		 );
	 }

	//优惠券轮播图
	function couponSwiper($city){
         $arrCouponImgInfo = S("arrCouponImgInfo".$city);
         if(!$arrCouponImgInfo){
             $arrCouponImgInfo=D('Swiper')->field(array('url','title','activityWeb'))->where("city='$city'")->select();
             S("arrCouponImgInfo".$city,json_encode($arrCouponImgInfo));
         }
		return $arrCouponImgInfo;
	}



    /**
     * 获得惠圈账本
     * @param string $shopCode 商家编码
     * @return array
     */
    public function getHqBook($shopCode) {
        if(empty($shopCode)) {
            $empty = 0;
            return array(
                'consumptionAmount' => $empty,
                'consumptionCount' => $empty,
                'realPayAmount' => $empty,
                'realPayUnliquidatedAmount' => $empty,
                'hqSubsidyAmount' => $empty,
                'hqSubsidyUnliquidatedAmount' => $empty,
                'shopSubsidyAmount' => $empty,
                'refundAmount' => $empty,
                'payedUnconsumedAmount' => $empty,
                'incomeAmount' => $empty,
            );
        }
        $con = array('ConsumeOrder.shopCode' => $shopCode);
        $consumeOrderMdl = new ConsumeOrderModel();
//        $con['ConsumeOrder.status'] = array('IN', array(\Consts::PAY_STATUS_PAYED, \Consts::PAY_STATUS_REFUNDING, \Consts::PAY_STATUS_REFUNDED, \Consts::PAY_STATUS_PART_REFUNDED));
        $con['ConsumeOrder.status'] = array('NOTIN',\Consts::PAY_STATUS_REFUNDED);

        $consumptionAmount = $consumeOrderMdl->sumOrderAmount($con); // 消费金额

        $consumptionCount = $consumeOrderMdl->getOrderCount($con, array(), 'DISTINCT(ConsumeOrder.clientCode)'); // 消费人次

        $con['UserConsume.status'] = array('IN', array(\Consts::PAY_STATUS_PAYED, \Consts::PAY_STATUS_REFUNDING, \Consts::PAY_STATUS_REFUNDED, \Consts::PAY_STATUS_PART_REFUNDED));
        $joinTable = array(
            array('joinTable' => 'ConsumeOrder', 'joinCon' => 'ConsumeOrder.orderCode = UserConsume.orderCode', 'joinType' => 'inner'),
        );
        $userConsumeMdl = new UserConsumeModel();
        $realPayAmount = $userConsumeMdl->sumConsumeField($con, 'realPay', $joinTable); // 顾客支付金额

        $con['UserConsume.liquidationStatus'] = array('IN', array(\Consts::LIQUIDATION_STATUS_ING, \Consts::LIQUIDATION_STATUS_HAD_NOT));
        $realPayUnliquidatedAmount = $userConsumeMdl->sumConsumeField($con, 'realPay', $joinTable); // 支付未清算金额

        $hqSubsidyAmount = 0; // 平台补贴金额
        $hqSubsidyUnliquidatedAmount = 0; // 平台补贴未清算金额
        $shopSubsidyAmount = 0; // 商家让利金额
        // 计算平台补贴金额和平台补贴未清算金额和商家让利金额
        $userConsumeList = $userConsumeMdl->listUserConsume(array('UserConsume.location' => $shopCode, 'UserConsume.status' => \Consts::PAY_STATUS_PAYED), $this->getPager(0), '', array('UserConsume.couponUsed', 'UserConsume.usedUserCouponCode', 'UserConsume.platBonus', 'UserConsume.shopBonus', 'UserConsume.subsidySettlementStatus'));
        $userCouponMdl = new UserCouponModel();
        foreach($userConsumeList as $v) {
            if($v['couponUsed'] > 0) {
                $arrUsedUserCouponCode = explode('|', $v['usedUserCouponCode']);
                foreach($arrUsedUserCouponCode as $userCouponCode) {
                    $userCouponInfo = $userCouponMdl->getUserCouponInfoB(array('userCouponCode' => $userCouponCode), array('BatchCoupon.hqSubsidy', 'BatchCoupon.shopSubsidy'));
                    $hqSubsidyAmount = $hqSubsidyAmount + $userCouponInfo['hqSubsidy'] + $v['platBonus'] + $v['firstDeduction'];
                    $shopSubsidyAmount = $shopSubsidyAmount + $userCouponInfo['shopSubsidy'] + $v['shopBonus'];
                    if($v['subsidySettlementStatus'] != \Consts::LIQUIDATION_STATUS_HAD) {
                        $hqSubsidyUnliquidatedAmount += $userCouponInfo['hqSubsidy'];
                    }
                }
            }
        }

        // 计算退款金额
        $refundLogMdl = new RefundLogModel();
        $refundAmount = $refundLogMdl->sumRefundAmount(
            array('RefundLog.liquidationStatus' => array('IN', array(\Consts::LIQUIDATION_STATUS_HAD, \Consts::LIQUIDATION_STATUS_NO_NEED)), 'ConsumeOrder.shopCode' => $shopCode),
            array(array('joinTable' => 'ConsumeOrder', 'joinCondition' => 'ConsumeOrder.orderNbr = RefundLog.orderNbr', 'joinType' => 'inner'))
        );

        // 获得买券订单中支付未消费金额
        $orderCouponAmount = $consumeOrderMdl->getConsumeOrderList(
            array('ConsumeOrder.shopCode' => $shopCode, 'ConsumeOrder.status' => array('IN', array(\Consts::PAY_STATUS_PART_REFUNDED, \Consts::PAY_STATUS_PAYED)), 'OrderCoupon.status' => \Consts::ORDER_COUPON_STATUS_USE),
            array('SUM(BatchCoupon.payPrice)' => 'orderCouponAmount'),
            array(array('joinTable' => 'OrderCoupon', 'joinCondition' => 'OrderCoupon.orderCode = ConsumeOrder.orderCode', 'joinType' => 'inner'), array('joinTable' => 'BatchCoupon', 'joinCondition' => 'BatchCoupon.batchCouponCode = OrderCoupon.batchCouponCode', 'joinType' => 'inner')),'', 0, 0
        );
        // 获得买券订单中支付未消费金额
        $orderActAmount = $consumeOrderMdl->getConsumeOrderList(
            array('ConsumeOrder.shopCode' => $shopCode, 'ConsumeOrder.status' => array('IN', array(\Consts::PAY_STATUS_PART_REFUNDED, \Consts::PAY_STATUS_PAYED)), 'UserActCode.status' => \Consts::USER_ACT_CODE_STATUS_USE),
            array('SUM(UserActCode.price)' => 'orderActAmount'),
            array(array('joinTable' => 'UserActCode', 'joinCondition' => 'UserActCode.orderCode = ConsumeOrder.orderCode', 'joinType' => 'inner')),'', 0, 0
        );
        $payedUnconsumedAmount = $orderCouponAmount[0]['orderCouponAmount'] + $orderActAmount[0]['orderActAmount']; // 支付未消费金额

        $incomeAmount = $realPayAmount + $hqSubsidyAmount - $refundAmount; // 收入金额

        //核验订单笔数
        $hasConfirm = D("UserConsume")
            ->join("OrderCoupon ON OrderCoupon.orderCode = UserConsume.orderCode")
            ->join("ConsumeOrder ON ConsumeOrder.orderCode = OrderCoupon.orderCode")
            ->where(array("ConsumeOrder.shopCode"=>$shopCode,"OrderCoupon.status"=>\Consts::ORDER_COUPON_STATUS_USED))
            ->field('ConsumeOrder.shopCode,OrderCoupon.orderCode,OrderCoupon.status,UserConsume.realPay')
            ->count("OrderCoupon.orderCode");

        //核验金额
        $hasConfirmMoney =  D("UserConsume")
            ->join("OrderCoupon ON OrderCoupon.orderCode = UserConsume.orderCode")
            ->join("ConsumeOrder ON ConsumeOrder.orderCode = OrderCoupon.orderCode")
            ->where(array("ConsumeOrder.shopCode"=>$shopCode,"OrderCoupon.status"=>\Consts::ORDER_COUPON_STATUS_USED))
            ->field("UserConsume.realPay,UserConsume.deduction")
            ->sum("deduction + realPay");


        return array(
            'couponHasUsed'  => $hasConfirm , //已核销订单
            'hasUsedMoney'   => $hasConfirmMoney/ \Consts::HUNDRED, // 已核销金额
            'consumptionAmount' => $consumptionAmount,
            'consumptionCount' => $consumptionCount,
            'realPayAmount' => $realPayAmount / \Consts::HUNDRED,
            'realPayUnliquidatedAmount' => $realPayUnliquidatedAmount / \Consts::HUNDRED,
            'hqSubsidyAmount' => $hqSubsidyAmount / \Consts::HUNDRED,
            'hqSubsidyUnliquidatedAmount' => $hqSubsidyUnliquidatedAmount / \Consts::HUNDRED,
            'shopSubsidyAmount' => $shopSubsidyAmount / \Consts::HUNDRED,
            'refundAmount' => $refundAmount / \Consts::HUNDRED,
            'payedUnconsumedAmount' => $payedUnconsumedAmount / \Consts::HUNDRED,
            'incomeAmount' => $incomeAmount / \Consts::HUNDRED,
        );
    }

    /**
     * 获得商家端惠圈账本内的时间
     * @param int $timeLimit 时间限制。1-今天；2-最近一周；3-最近一月；4-全部
     * @return array $time
     */
    private function filterShopBillTimeLimit($timeLimit) {
        $endTime = date('Y-m-d 23:59:59');
//        switch($timeLimit) {
//            case 1: // 今天
//                $time = array('BETWEEN', array(date('Y-m-d 00:00:00'), $endTime));
//                break;
//            case 2: // 最近一周
//                $time = array('BETWEEN', array(date('Y-m-d 00:00:00', time() - 6 * 86400), $endTime));
//                break;
//            case 3: // 最近一月
//                $time = array('BETWEEN', array(date('Y-m-d 00:00:00', time() - 29 * 86400), $endTime));
//                break;
//            default:
//                $time = array();
//                break;
//        }
        if( !empty($timeLimit) || $timeLimit !=''){
                //开始时间
                $timeArr = explode("_",$timeLimit);
                $benginTimeStamp = strtotime($timeArr[0]);
                $endTimeStamp  = strtotime($timeArr[1]);
                $time = array('BETWEEN', array(date("Y-m-d 00:00:00",$benginTimeStamp),date("Y-m-d 23:59:59",$endTimeStamp)));
        }else{
            $time = array('BETWEEN', array(date('Y-m-d 00:00:00'), $endTime));
        }
        return $time;
    }

    /**
     * 顾客清单，退款清单，消费未结算账单，补贴未结算账单，支付结算对账，补贴结算账单，账单查询
     * @param string $shopCode 商家编码
     * @param int $page 页码，第一页值为1
     * @param int $timeLimit 时间限制。1-今天；2-最近一周；3-最近一月；4-全部
     * @param int $billType 账单类型。1-顾客清单；2-退款清单；3-消费未结算账单；4-补贴未结算账单；5-支付结算对账；6-补贴结算对账；7-账单查询
     * @param string $searchWord 订单号或者手机号
     * @return array $data
     */
    public function getBillList($shopCode, $page, $timeLimit, $billType, $searchWord) {
        $time = $this->filterShopBillTimeLimit($timeLimit);
        if($billType == 1) { // 顾客清单
            $data = $this->getCustomerList($shopCode, $page, $time);
        } elseif($billType == 2) { // 退款清单
            $data = $this->getRefundList($shopCode, $page, $time);
        } elseif($billType == 3) { // 消费未结算账单
            $data = $this->getUnliquidatedList($shopCode, $page, $time);
        } elseif($billType == 4) { // 补贴未结算账单
            $data = $this->getUnliquidatedSubsidyList($shopCode, $page, $time);
        } elseif($billType == 5) { // 支付结算对账
            $data = $this->getLiquidatedList($shopCode, $page);
        } elseif($billType == 6) { // 补贴结算对账
            $data = $this->getLiquidatedSubsidyList($shopCode, $page);
        } elseif($billType == 7) { // 账单查询
            $data = $this->getUserConsumeList($shopCode, $page, $time, $searchWord);
        } else {
            $data = array(
                'totalCount' => 0,
                'dataList' => array(),
                'nextPage' => 1,
                'page' => 1,
                'count' => 0,
            );
        }
        return $data;
    }

    /**
     * 获得统计信息（顾客清单，退款清单，消费未结算账单，补贴未结算账单，支付结算对账，补贴结算账单，账单查询）
     * @param string $shopCode 商家编码
     * @param int $timeLimit 时间限制。1-今天；2-最近一周；3-最近一月；4-全部
     * @param int $billType 账单类型。1-顾客清单；2-退款清单；3-消费未结算账单；4-补贴未结算账单；5-支付结算对账；6-补贴结算对账；7-账单查询
     * @param string $searchWord 订单号或者手机号
     * @return array $data
     */
    public function getBillStatistics($shopCode, $timeLimit, $billType, $searchWord) {
        $time = $this->filterShopBillTimeLimit($timeLimit);
        if($billType == 1) { // 顾客清单
            $data = $this->getCustomerStatistics($shopCode, $time);
        } elseif($billType == 2) { // 退款清单
            $data = $this->getRefundStatistics($shopCode, $time);
        } elseif($billType == 3) { // 消费未结算账单
            $data = $this->getUnliquidatedStatistics($shopCode, $time);
        } elseif($billType == 4) { // 补贴未结算账单
            $data = $this->getUnliquidatedSubsidyStatistics($shopCode, $time);
        } elseif($billType == 5) { // 支付结算对账
            $data = $this->getLiquidatedStatistics($shopCode);
        } elseif($billType == 6) { // 补贴结算对账
            $data = $this->getLiquidatedSubsidyStatistics($shopCode);
        } elseif($billType == 7) { // 账单查询
            $data = $this->getUserConsumeStatistics2($shopCode, $time, $searchWord);


        } else {
            $data = array();
        }
        return $data;
    }

    /**
     * 获得顾客清单
     * @param string $shopCode 商家编码
     * @param int $page 页码，第一页值为1
     * @param array $time
     * @return array
     */
    private function getCustomerList($shopCode, $page, $time) {
        if($time) $condition['UserConsume.payedTime'] = $time;
        $condition['UserConsume.location'] = $shopCode; // 商家编码
        $condition['UserConsume.status'] = \Consts::PAY_STATUS_PAYED; // 已支付
        $userConsumeMdl = new UserConsumeModel();
        // 获得列表数据
        $dataList = $userConsumeMdl->getShopCustomer($condition, $this->getPager($page));
        foreach($dataList as $k => $v) {
            $dataList[$k]['mobileNbr'] = substr($dataList[$k]['mobileNbr'], 7);
            $dataList[$k]['consumptionAmount'] = $dataList[$k]['consumptionAmount'] / \Consts::HUNDRED;
            $dataList[$k]['payAmount'] = $dataList[$k]['payAmount'] / \Consts::HUNDRED;
            $dataList[$k]['discountAmount'] = $dataList[$k]['discountAmount'] / \Consts::HUNDRED;
        }
        // 获得总记录数
        $totalCount = $userConsumeMdl->countShopCustomer($condition);
        // 获得下一页页码
        $nextPage = UtilsModel::getNextPage($totalCount, $page);
        return array(
            'totalCount' => $totalCount,
            'dataList' => $dataList,
            'nextPage' => $nextPage,
            'page' => $page,
            'count' => count($dataList),
        );
    }

    /**
     * 获得顾客清单统计信息
     * @param string $shopCode 商家编码
     * @param array $time
     * @return array
     */
    private function getCustomerStatistics($shopCode, $time) {
        // 设置时间限制
        if($time) $condition['UserConsume.payedTime'] = $time;
        $condition['UserConsume.location'] = $shopCode; // 商家编码
        $condition['UserConsume.status'] = \Consts::PAY_STATUS_PAYED; // 已支付
        $userConsumeMdl = new UserConsumeModel();
        // 获得统计数据
        $consumptionNbr = $userConsumeMdl->countConsumeTimes($condition);
        $consumptionAmount = $userConsumeMdl->sumConsumeField($condition, 'deduction + realPay');
        $payAmount = $userConsumeMdl->sumConsumeField($condition, 'realPay');
        $discountAmount = $userConsumeMdl->sumConsumeField($condition, 'deduction');
        return array(
            'consumptionNbr' => $consumptionNbr, // 消费次数
            'consumptionAmount' => $consumptionAmount / \Consts::HUNDRED, // 消费金额
            'payAmount' => $payAmount / \Consts::HUNDRED, // 支付金额
            'discountAmount' => $discountAmount / \Consts::HUNDRED, // 优惠金额
        );
    }

    /**
     * 获得退款清单
     * @param string $shopCode 商家编码
     * @param int $page 页码，第一页值为1
     * @param array $time
     * @return array
     */
    private function getRefundList($shopCode, $page, $time) {
        // 设置时间限制
        if($time) $condition['RefundLog.refundTime'] = $time;
        $condition['RefundLog.LiquidationStatus'] = array('IN', array(\Consts::LIQUIDATION_STATUS_HAD, \Consts::LIQUIDATION_STATUS_NO_NEED)); // 清算状态属于已清算和不需要清算
        $condition['ConsumeOrder.shopCode'] = $shopCode; // 商家编码
        $condition['ConsumeOrder.status'] = \Consts::PAY_STATUS_REFUNDED; // 支付状态为已退款
        $condition['UserConsume.status'] = \Consts::PAY_STATUS_REFUNDED; // 支付状态为已退款
        $refundLogMdl = new RefundLogModel();
        $joinTableArr = array(
            array('joinTable' => 'ConsumeOrder', 'joinCondition' => 'ConsumeOrder.orderNbr = RefundLog.orderNbr', 'joinType' => 'inner'),
            array('joinTable' => 'User', 'joinCondition' => 'User.userCode = ConsumeOrder.clientCode', 'joinType' => 'inner'),
            array('joinTable' => 'UserConsume', 'joinCondition' => 'UserConsume.orderCode = ConsumeOrder.orderCode', 'joinType' => 'inner'),
        );
        // 获得列表数据
        $dataList = $refundLogMdl->getRefundLogList(
            $condition,
            array('User.avatarUrl', 'User.nickName', 'User.mobileNbr', 'ConsumeOrder.orderAmount' => 'consumptionAmount','ConsumeOrder.orderNbr', 'ConsumeOrder.orderCode', 'UserConsume.consumeCode', 'RefundLog.refundPrice' => 'refundAmount', 'RefundLog.refundTime'),
            $joinTableArr, 'RefundLog.refundTime desc', 10, $page
        );
        $userConsumeMdl = new UserConsumeModel();
        foreach($dataList as $k => $v) {
            $dataList[$k]['mobileNbr'] = substr($dataList[$k]['mobileNbr'], 7);
            $dataList[$k]['consumptionAmount'] = $dataList[$k]['consumptionAmount'] / \Consts::HUNDRED;
            $dataList[$k]['refundAmount'] = $dataList[$k]['refundAmount'] / \Consts::HUNDRED;
            $payInfo = $userConsumeMdl->getOrderCurrPayInfo($v['orderCode'], array('UserConsume.realPay', 'UserConsume.payedTime'));
            $dataList[$k]['payAmount'] = $payInfo['realPay'] / \Consts::HUNDRED;
            $dataList[$k]['consumptionTime'] = $payInfo['payedTime'];
        }
        // 获得总记录数
        $totalCount = $refundLogMdl->countRefundLog($condition, $joinTableArr);
        // 获得下一页页码
        $nextPage = UtilsModel::getNextPage($totalCount, $page);
        return array(
            'totalCount' => $totalCount,
            'dataList' => $dataList,
            'nextPage' => $nextPage,
            'page' => $page,
            'count' => count($dataList),
        );
    }

    /**
     * 获得退款清单统计信息
     * @param string $shopCode 商家编码
     * @param array $time
     * @return array
     */
    private function getRefundStatistics($shopCode, $time) {
        // 设置时间限制
        if($time) $condition['RefundLog.refundTime'] = $time;
        $condition['RefundLog.LiquidationStatus'] = array('IN', array(\Consts::LIQUIDATION_STATUS_HAD, \Consts::LIQUIDATION_STATUS_NO_NEED)); // 清算状态属于已清算和不需要清算
        $condition['ConsumeOrder.shopCode'] = $shopCode; // 商家编码
        $condition['ConsumeOrder.status'] = \Consts::PAY_STATUS_REFUNDED; // 支付状态为已退款
        $refundLogMdl = new RefundLogModel();
        $joinTableArr = array(
            array('joinTable' => 'ConsumeOrder', 'joinCondition' => 'ConsumeOrder.orderNbr = RefundLog.orderNbr', 'joinType' => 'inner'),
        );
        // 获得统计数据
        $refundAmount = $refundLogMdl->sumRefundAmount($condition, $joinTableArr);
        $consumptionAmount = $refundLogMdl->sumOrderAmount($condition);
        return array(
            'consumptionAmount' => $consumptionAmount / \Consts::HUNDRED, // 消费金额
            'refundAmount' => $refundAmount / \Consts::HUNDRED, // 退款金额
        );
    }

    /**
     * 获得消费未结算
     * @param string $shopCode 商家编码
     * @param int $page 页码，第一页值为1
     * @param array $time
     * @return array
     */
    private function getUnliquidatedList($shopCode, $page, $time) {
        // 设置时间限制
        if($time) $condition['UserConsume.payedTime'] = $time;
        $condition['UserConsume.location'] = $shopCode; // 商家编码
        $condition['UserConsume.status'] = \Consts::PAY_STATUS_PAYED; // 支付状态为已支付
        $condition['UserConsume.liquidationStatus'] = array('IN', array(\Consts::LIQUIDATION_STATUS_HAD_NOT, \Consts::LIQUIDATION_STATUS_ING)); // 清算状态在未清算和清算中
        $userConsumeMdl = new UserConsumeModel();
        $dataList = $userConsumeMdl->listUserConsume($condition, $this->getPager($page), '', array('User.avatarUrl', 'User.nickName', 'User.mobileNbr', 'ConsumeOrder.orderAmount' => 'consumptionAmount','ConsumeOrder.orderNbr', 'UserConsume.payedTime' => 'consumptionTime', 'UserConsume.realPay' => 'payAmount', 'UserConsume.consumeCode'));
        foreach($dataList as $k => $v) {
            $dataList[$k]['mobileNbr'] = substr($dataList[$k]['mobileNbr'], 7);
            $dataList[$k]['consumptionAmount'] = $dataList[$k]['consumptionAmount'] / \Consts::HUNDRED;
            $dataList[$k]['payAmount'] = $dataList[$k]['payAmount'] / \Consts::HUNDRED;
            unset($dataList[$k]['areaNbr']);
        }
        $totalCount = $userConsumeMdl->countUserConsume($condition);
        $nextPage = UtilsModel::getNextPage($totalCount, $page); // 下一页页码
        return array(
            'totalCount' => $totalCount,
            'dataList' => $dataList,
            'nextPage' => $nextPage,
            'page' => $page,
            'count' => count($dataList),
        );
    }

    /**
     * 获得消费未结算统计信息
     * @param string $shopCode 商家编码
     * @param array $time
     * @return array
     */
    private function getUnliquidatedStatistics($shopCode, $time) {
        // 设置时间限制
        if($time) $condition['UserConsume.payedTime'] = $time;
        $condition['UserConsume.location'] = $shopCode; // 商家编码
        $condition['UserConsume.status'] = \Consts::PAY_STATUS_PAYED; // 支付状态为已支付
        $condition['UserConsume.liquidationStatus'] = array('IN', array(\Consts::LIQUIDATION_STATUS_HAD_NOT, \Consts::LIQUIDATION_STATUS_ING)); // 清算状态在未清算和清算中
        $userConsumeMdl = new UserConsumeModel();
        $consumptionAmount = $userConsumeMdl->sumConsumeField($condition, 'deduction + realPay');
        return array(
            'consumptionAmount' => $consumptionAmount / \Consts::HUNDRED, // 消费金额
        );
    }

    /**
     * 获得补贴未结算
     * @param string $shopCode 商家编码
     * @param int $page 页码，第一页值为1
     * @param array $time
     * @return array
     */
    private function getUnliquidatedSubsidyList($shopCode, $page, $time) {
        // 设置时间限制
        if($time) $condition['UserConsume.payedTime'] = $time;
        $condition['UserConsume.location'] = $shopCode; // 商家编码
        $condition['UserConsume.status'] = \Consts::PAY_STATUS_PAYED; // 支付状态为已支付
        $condition['UserConsume.subsidySettlementStatus'] = array('IN', array(\Consts::LIQUIDATION_STATUS_HAD_NOT, \Consts::LIQUIDATION_STATUS_ING)); // 补贴清算状态在未清算和清算中

        $subCon['_logic'] = 'OR';
        $subCon['couponUsed'] = array('GT', 0); // 使用了优惠券
        $subCon['platBonus'] = array('GT', 0); // 使用了平台红包
        $subCon['firstDeduction'] = array('GT', 0); // 享用了首单立减
        $condition['_complex'] = $subCon;
        $userConsumeMdl = new UserConsumeModel();
        $dataList = $userConsumeMdl->listUserConsume($condition, $this->getPager($page), '', array('User.avatarUrl', 'User.nickName', 'User.mobileNbr', 'ConsumeOrder.orderAmount' => 'consumptionAmount','ConsumeOrder.orderNbr', 'UserConsume.payedTime' => 'consumptionTime', 'UserConsume.realPay' => 'payAmount', 'UserConsume.consumeCode', 'UserConsume.usedUserCouponCode', 'UserConsume.platBonus', 'UserConsume.firstDeduction'));
        $unsetArr = array('areaNbr', 'usedUserCouponCode', 'platBonus', 'firstDeduction');
        foreach($dataList as $k => $v) {
            $dataList[$k]['mobileNbr'] = substr($dataList[$k]['mobileNbr'], 7);
            $dataList[$k]['consumptionAmount'] = $dataList[$k]['consumptionAmount'] / \Consts::HUNDRED;
            $dataList[$k]['payAmount'] = $dataList[$k]['payAmount'] / \Consts::HUNDRED;
            // 计算获得补贴金额
            $userCouponMdl = new UserCouponModel();
            if($v['usedUserCouponCode']) {
                $arrUserCouponCode = explode('|', $v['usedUserCouponCode']);
                $subsidyAmount = 0;
                foreach($arrUserCouponCode as $userCouponCode) {
                    $userCouponInfo = $userCouponMdl->getUserCouponInfoB(array('UserCoupon.userCouponCode' => $userCouponCode), array('BatchCoupon.hqSubsidy', 'BatchCoupon.couponType'));
                    if($userCouponInfo['couponType'] == \Consts::COUPON_TYPE_DISCOUNT) {
                        $couponPlatSubsidy = $v['consumptionAmount'] * $userCouponInfo['hqSubsidy']; // 优惠券平台补贴金额，单位：分
                    } else {
                        $couponPlatSubsidy = $userCouponInfo['hqSubsidy']; // 优惠券平台补贴金额，单位：分
                    }
                    $subsidyAmount += $couponPlatSubsidy;
                }
            } else {
                $subsidyAmount = 0;
            }
            $dataList[$k]['subsidyAmount'] = $subsidyAmount / \Consts::HUNDRED + $v['platBonus'] + $v['firstDeduction']; // 平台补贴金额：优惠券平台补贴+平台红包+首单立减

            foreach($unsetArr as $unsetItem) {
                unset($dataList[$k][$unsetItem]);
            }
        }
        $totalCount = $userConsumeMdl->countUserConsume($condition);
        $nextPage = UtilsModel::getNextPage($totalCount, $page); // 下一页页码
        return array(
            'totalCount' => $totalCount,
            'dataList' => $dataList,
            'nextPage' => $nextPage,
            'page' => $page,
            'count' => count($dataList),
        );
    }

    /**
     * 获得补贴未结算统计信息
     * @param string $shopCode 商家编码
     * @param array $time
     * @return array
     */
    private function getUnliquidatedSubsidyStatistics($shopCode, $time) {
        // 设置时间限制
        if($time) $condition['UserConsume.payedTime'] = $time;
        $condition['UserConsume.location'] = $shopCode; // 商家编码
        $condition['UserConsume.status'] = \Consts::PAY_STATUS_PAYED; // 支付状态为已支付
        $condition['UserConsume.subsidySettlementStatus'] = array('IN', array(\Consts::LIQUIDATION_STATUS_HAD_NOT, \Consts::LIQUIDATION_STATUS_ING)); // 补贴清算状态在未清算和清算中

        $subCon['_logic'] = 'OR';
        $subCon['couponUsed'] = array('GT', 0); // 使用了优惠券
        $subCon['platBonus'] = array('GT', 0); // 使用了平台红包
        $subCon['firstDeduction'] = array('GT', 0); // 享用了首单立减
        $condition['_complex'] = $subCon;

        $userConsumeMdl = new UserConsumeModel();
        $consumptionAmount = $userConsumeMdl->sumConsumeField($condition, 'deduction + realPay'); // 消费金额
        $subsidyAmount = $userConsumeMdl->sumConsumeField($condition, 'firstDeduction + platBonus'); // 平台补贴金额，单位：分。首单立减金额 + 平台红包金额
        $dataList = $userConsumeMdl->listUserConsume($condition, $this->getPager(0), '', array('UserConsume.usedUserCouponCode', 'ConsumeOrder.orderAmount'));
        $userCouponMdl = new UserCouponModel();
        foreach($dataList as $v) {
            if($v['usedUserCouponCode']) {
                $arrUserCouponCode = explode('|', $v['usedUserCouponCode']);
                foreach($arrUserCouponCode as $userCouponCode) {
                    $userCouponInfo = $userCouponMdl->getUserCouponInfoB(array('UserCoupon.userCouponCode' => $userCouponCode), array('BatchCoupon.hqSubsidy', 'BatchCoupon.couponType'));
                    if($userCouponInfo['couponType'] == \Consts::COUPON_TYPE_DISCOUNT) {
                        $couponPlatSubsidy = $v['orderAmount'] * $userCouponInfo['hqSubsidy']; // 优惠券平台补贴金额，单位：分
                    } else {
                        $couponPlatSubsidy = $userCouponInfo['hqSubsidy']; // 优惠券平台补贴金额，单位：分
                    }
                    $subsidyAmount += $couponPlatSubsidy;
                }
            }
        }
        return array(
            'consumptionAmount' => $consumptionAmount / \Consts::HUNDRED,
            'subsidyAmount' => $subsidyAmount / \Consts::HUNDRED,
        );
    }

    /**
     * 获得支付已结算对账
     * @param string $shopCode 商家编码
     * @param int $page 页码，第一页值为1
     * @return array
     */
    private function getLiquidatedList($shopCode, $page) {
        // 设置时间限制，最近5天的清算日期
        $startTime = date('Y-m-d 00:00:00', time() - 5 * 86400); // 开始时间
        $endTime = date('Y-m-d 23:59:59', time() - 86400); // 结束时间
        $condition['UserConsume.payedTime'] = array('BETWEEN', array($startTime, $endTime));
        $condition['UserConsume.location'] = $shopCode; // 商家编码
        $condition['UserConsume.status'] = \Consts::PAY_STATUS_PAYED; // 支付状态为已支付
        $condition['UserConsume.liquidationStatus'] = \Consts::LIQUIDATION_STATUS_HAD; // 清算状态为已清算
        $userConsumeMdl = new UserConsumeModel();
        $dataList = $userConsumeMdl->listUserConsume($condition, $this->getPager($page), '', array('User.avatarUrl', 'User.nickName', 'User.mobileNbr', 'ConsumeOrder.orderAmount' => 'consumptionAmount','ConsumeOrder.orderNbr', 'UserConsume.payedTime' => 'consumptionTime', 'UserConsume.realPay' => 'payAmount', 'UserConsume.consumeCode'));
        foreach($dataList as $k => $v) {
            $dataList[$k]['mobileNbr'] = substr($dataList[$k]['mobileNbr'], 7);
            $dataList[$k]['consumptionAmount'] = $dataList[$k]['consumptionAmount'] / \Consts::HUNDRED;
            $dataList[$k]['payAmount'] = $dataList[$k]['payAmount'] / \Consts::HUNDRED;
            unset($dataList[$k]['areaNbr']);
        }
        $totalCount = $userConsumeMdl->countUserConsume($condition);
        $nextPage = UtilsModel::getNextPage($totalCount, $page); // 下一页页码
        return array(
            'totalCount' => $totalCount,
            'dataList' => $dataList,
            'nextPage' => $nextPage,
            'page' => $page,
            'count' => count($dataList),
        );
    }

    /**
     * 获得支付已结算统计数据
     * @param string $shopCode 商家编码
     * @return array
     */
    private function getLiquidatedStatistics($shopCode) {
        // 设置时间限制，最近5天的清算日期
        $startTime = date('Y-m-d 00:00:00', time() - 5 * 86400); // 开始时间
        $endTime = date('Y-m-d 23:59:59', time() - 86400); // 结束时间
        $condition['UserConsume.payedTime'] = array('BETWEEN', array($startTime, $endTime));
        $condition['UserConsume.location'] = $shopCode; // 商家编码
        $condition['UserConsume.status'] = \Consts::PAY_STATUS_PAYED; // 支付状态为已支付
        $condition['UserConsume.liquidationStatus'] = \Consts::LIQUIDATION_STATUS_HAD; // 清算状态为已清算
        $userConsumeMdl = new UserConsumeModel();
        $consumptionNbr = $userConsumeMdl->countConsumeTimes($condition);
        $consumptionAmount = $userConsumeMdl->sumConsumeField($condition, 'deduction + realPay');
        $payAmount = $userConsumeMdl->sumConsumeField($condition, 'realPay');
        return array(
            'consumptionNbr' => $consumptionNbr, // 消费次数
            'consumptionAmount' => $consumptionAmount / \Consts::HUNDRED, // 消费金额
            'payAmount' => $payAmount / \Consts::HUNDRED, // 支付金额
            'startDate' => date('Y年m月d日', strtotime($startTime)), // 清算开始日期
            'endDate' => date('Y年m月d日', strtotime($endTime)), // 清算结束日期
        );
    }

    /**
     * 获得补贴结算对账
     * @param string $shopCode 商家编码
     * @param int $page 页码，第一页值为1
     * @return array
     */
    private function getLiquidatedSubsidyList($shopCode, $page) {
        // 设置时间限制，最近5天的清算日期
        $startTime = date('Y-m-d 00:00:00', time() - 5 * 86400); // 开始时间
        $endTime = date('Y-m-d 23:59:59', time() - 86400); // 结束时间
        $condition['UserConsume.payedTime'] = array('BETWEEN', array($startTime, $endTime));
        $condition['UserConsume.location'] = $shopCode; // 商家编码
        $condition['UserConsume.status'] = \Consts::PAY_STATUS_PAYED; // 支付状态为已支付
        $condition['UserConsume.subsidySettlementStatus'] = \Consts::LIQUIDATION_STATUS_HAD; // 清算状态为已清算
        $userConsumeMdl = new UserConsumeModel();
        $dataList = $userConsumeMdl->listUserConsume($condition, $this->getPager($page), '', array('User.avatarUrl', 'User.nickName', 'User.mobileNbr', 'ConsumeOrder.orderAmount' => 'consumptionAmount','ConsumeOrder.orderNbr', 'UserConsume.payedTime' => 'consumptionTime', 'UserConsume.realPay' => 'payAmount', 'UserConsume.consumeCode', 'UserConsume.platBonus', 'UserConsume.firstDeduction'));
        $unsetArr = array('areaNbr', 'usedUserCouponCode', 'platBonus', 'firstDeduction');
        foreach($dataList as $k => $v) {
            $dataList[$k]['mobileNbr'] = substr($dataList[$k]['mobileNbr'], 7);
            $dataList[$k]['consumptionAmount'] = $dataList[$k]['consumptionAmount'] / \Consts::HUNDRED;
            $dataList[$k]['payAmount'] = $dataList[$k]['payAmount'] / \Consts::HUNDRED;
            unset($dataList[$k]['areaNbr']);
//            $subsidyAmount = $v['platBonus'] + $v['firstDeduction'];
            // 计算获得补贴金额
            $userCouponMdl = new UserCouponModel();
            if($v['usedUserCouponCode']) {
                $arrUserCouponCode = explode('|', $v['usedUserCouponCode']);
                $subsidyAmount = 0;
                foreach($arrUserCouponCode as $userCouponCode) {
                    $userCouponInfo = $userCouponMdl->getUserCouponInfoB(array('UserCoupon.userCouponCode' => $userCouponCode), array('BatchCoupon.hqSubsidy', 'BatchCoupon.couponType'));
                    if($userCouponInfo['couponType'] == \Consts::COUPON_TYPE_DISCOUNT) {
                        $couponPlatSubsidy = $v['consumptionAmount'] * $userCouponInfo['hqSubsidy']; // 优惠券平台补贴金额，单位：分
                    } else {
                        $couponPlatSubsidy = $userCouponInfo['hqSubsidy']; // 优惠券平台补贴金额，单位：分
                    }
                    $subsidyAmount += $couponPlatSubsidy;
                }
            } else {
                $subsidyAmount = 0;
            }
            $dataList[$k]['subsidyAmount'] = $subsidyAmount / \Consts::HUNDRED + $v['platBonus'] + $v['firstDeduction']; // 平台补贴金额：优惠券平台补贴+平台红包+首单立减
            foreach($unsetArr as $unsetItem) {
                unset($dataList[$k][$unsetItem]);
            }
        }
        $totalCount = $userConsumeMdl->countUserConsume($condition);
        $nextPage = UtilsModel::getNextPage($totalCount, $page); // 下一页页码
        return array(
            'totalCount' => $totalCount,
            'dataList' => $dataList,
            'nextPage' => $nextPage,
            'page' => $page,
            'count' => count($dataList),
        );
    }

    /**
     * 获得补贴结算统计数据
     * @param string $shopCode 商家编码
     * @return array
     */
    private function getLiquidatedSubsidyStatistics($shopCode) {
        // 设置时间限制，最近5天的清算日期
        $startTime = date('Y-m-d 00:00:00', time() - 5 * 86400); // 开始时间
        $endTime = date('Y-m-d 23:59:59', time() - 86400); // 结束时间
        $condition['UserConsume.payedTime'] = array('BETWEEN', array($startTime, $endTime));
        $condition['UserConsume.location'] = $shopCode; // 商家编码
        $condition['UserConsume.status'] = \Consts::PAY_STATUS_PAYED; // 支付状态为已支付
        $condition['UserConsume.subsidySettlementStatus'] = \Consts::LIQUIDATION_STATUS_HAD; // 清算状态为已清算
        $userConsumeMdl = new UserConsumeModel();
        $consumptionNbr = $userConsumeMdl->countConsumeTimes($condition);
        $consumptionAmount = $userConsumeMdl->sumConsumeField($condition, 'deduction + realPay');
        $payAmount = $userConsumeMdl->sumConsumeField($condition, 'realPay');

        $subsidyAmount = $userConsumeMdl->sumConsumeField($condition, 'firstDeduction + platBonus'); // 平台补贴金额，单位：分。首单立减金额 + 平台红包金额
        $dataList = $userConsumeMdl->listUserConsume($condition, $this->getPager(0), '', array('UserConsume.usedUserCouponCode', 'ConsumeOrder.orderAmount'));
        $userCouponMdl = new UserCouponModel();
        foreach($dataList as $v) {
            if($v['usedUserCouponCode']) {
                $arrUserCouponCode = explode('|', $v['usedUserCouponCode']);
                foreach($arrUserCouponCode as $userCouponCode) {
                    $userCouponInfo = $userCouponMdl->getUserCouponInfoB(array('UserCoupon.userCouponCode' => $userCouponCode), array('BatchCoupon.hqSubsidy', 'BatchCoupon.couponType'));
                    if($userCouponInfo['couponType'] == \Consts::COUPON_TYPE_DISCOUNT) {
                        $couponPlatSubsidy = $v['orderAmount'] * $userCouponInfo['hqSubsidy']; // 优惠券平台补贴金额，单位：分
                    } else {
                        $couponPlatSubsidy = $userCouponInfo['hqSubsidy']; // 优惠券平台补贴金额，单位：分
                    }
                    $subsidyAmount += $couponPlatSubsidy;
                }
            }
        }

        return array(
            'consumptionNbr' => $consumptionNbr, // 消费次数
            'consumptionAmount' => $consumptionAmount / \Consts::HUNDRED, // 消费金额
            'payAmount' => $payAmount / \Consts::HUNDRED, // 支付金额
            'subsidyAmount' => $subsidyAmount / \Consts::HUNDRED,
            'startDate' => date('Y年m月d日', strtotime($startTime)), // 清算开始日期
            'endDate' => date('Y年m月d日', strtotime($endTime)), // 清算结束日期
        );
    }

    /**
     * 获得账单查询
     * @param string $shopCode 商家编码
     * @param int $page 页码，第一页值为1
     * @param array $time
     * @param string $searchWord 搜索字段，订单号或者手机号
     * @return array
     */
    private function getUserConsumeList($shopCode, $page, $time, $searchWord) {
        // 设置时间限制
        if($time) $condition['UserConsume.payedTime'] = $time;
        $condition['UserConsume.location'] = $shopCode; // 商家编码
        $condition['UserConsume.status'] = \Consts::PAY_STATUS_PAYED; // 支付状态为已支付
        $subCon['ConsumeOrder.orderNbr'] = array('LIKE', "%$searchWord%");
        $subCon['User.mobileNbr'] = array('LIKE', "%$searchWord%");
        $subCon['_logic'] = 'OR';
        $condition['_complex'] = $subCon;
        $userConsumeMdl = new UserConsumeModel();
        $dataList = $userConsumeMdl->listUserConsume($condition, $this->getPager($page), '', array('User.avatarUrl', 'User.nickName', 'User.mobileNbr', 'ConsumeOrder.orderAmount' => 'consumptionAmount','ConsumeOrder.orderNbr', 'UserConsume.payedTime' => 'consumptionTime', 'UserConsume.realPay' => 'payAmount', 'UserConsume.deduction' => 'discountAmount', 'UserConsume.consumeCode'));
            $sql = $userConsumeMdl->getLastSql();
        F("sqsqsl",$sql);
        foreach($dataList as $k => $v) {
            $dataList[$k]['mobileNbr'] = substr($dataList[$k]['mobileNbr'], 7);
            $dataList[$k]['consumptionAmount'] = $dataList[$k]['consumptionAmount'] / \Consts::HUNDRED;
            $dataList[$k]['payAmount'] = $dataList[$k]['payAmount'] / \Consts::HUNDRED;
            $dataList[$k]['discountAmount'] = $dataList[$k]['discountAmount'] / \Consts::HUNDRED;
            unset($dataList[$k]['areaNbr']);
        }
        $totalCount = $userConsumeMdl->countUserConsume($condition);
        $nextPage = UtilsModel::getNextPage($totalCount, $page); // 下一页页码
        return array(
            'totalCount' => $totalCount,
            'dataList' => $dataList,
            'nextPage' => $nextPage,
            'page' => $page,
            'count' => count($dataList),
        );
    }


    /**
     * 获得账单查询统计数据(乔本亮修改)
     * @param string $shopCode 商家编码
     * @param array $time
     * @param string $searchWord 搜索字段，订单号或者手机号
     * @return array
     */
    private function getUserConsumeStatistics2($shopCode, $time, $searchWord) {
        // 设置时间限制
        if($time) $condition['UserConsume.payedTime'] = $time;
        $condition['UserConsume.location'] = $shopCode; // 商家编码
        $condition['UserConsume.status'] = \Consts::PAY_STATUS_PAYED; // 支付状态为已支付
        $subCon['ConsumeOrder.orderNbr'] = array('LIKE', "%$searchWord%");
        $subCon['User.mobileNbr'] = array('LIKE', "%$searchWord%");
        $subCon['_logic'] = 'OR';
        $condition['_complex'] = $subCon;
        $userConsumeMdl = new UserConsumeModel();
        // 获得统计数据
        $joinTable = array(
            array('joinTable' => 'ConsumeOrder', 'joinCon' => 'ConsumeOrder.orderCode = UserConsume.orderCode', 'joinType' => 'inner'),
            array('joinTable' => 'User', 'joinCon' => 'User.userCode = UserConsume.consumerCode', 'joinType' => 'inner'),
        );
        $consumptionNbr = $userConsumeMdl->countConsumeTimes($condition, $joinTable);
        $consumptionAmount = $userConsumeMdl->sumConsumeField($condition, 'deduction + realPay', $joinTable);
        $payAmount = $userConsumeMdl->sumConsumeField($condition, 'realPay', $joinTable);

        //核验订单笔数
        $hasConfirm = D("UserConsume")
            ->join("OrderCoupon ON OrderCoupon.orderCode = UserConsume.orderCode")
            ->join("ConsumeOrder ON ConsumeOrder.orderCode = OrderCoupon.orderCode")
            ->where(array("ConsumeOrder.shopCode"=>$shopCode,"OrderCoupon.status"=>\Consts::ORDER_COUPON_STATUS_USED,"UserConsume.payedTime"=>$condition['UserConsume.payedTime']))
            ->field('ConsumeOrder.shopCode,OrderCoupon.orderCode,OrderCoupon.status,UserConsume.realPay')->count("OrderCoupon.orderCode");

        //核验金额
        $hasConfirmMoney =  D("UserConsume")
            ->join("OrderCoupon ON OrderCoupon.orderCode = UserConsume.orderCode")
            ->join("ConsumeOrder ON ConsumeOrder.orderCode = OrderCoupon.orderCode")
            ->where(array("ConsumeOrder.shopCode"=>$shopCode,"OrderCoupon.status"=>\Consts::ORDER_COUPON_STATUS_USED,"UserConsume.payedTime"=>$condition['UserConsume.payedTime']))
            ->field("UserConsume.realPay,UserConsume.deduction")
            ->sum("deduction + realPay");

        return array(
            'consumptionNbr' => $consumptionNbr, // 消费次数
            'couponHasUsed'  => $hasConfirm , //已核销订单
            'hasUsedMoney'   => $hasConfirmMoney/ \Consts::HUNDRED, // 已核销金额
            'consumptionAmount' => $consumptionAmount / \Consts::HUNDRED, // 消费金额
            'payAmount' => $payAmount / \Consts::HUNDRED, // 支付金额
        );
    }

    /**
     * 获得账单查询统计数据
     * @param string $shopCode 商家编码
     * @param array $time
     * @param string $searchWord 搜索字段，订单号或者手机号
     * @return array
     */
    private function getUserConsumeStatistics($shopCode, $time, $searchWord) {
        // 设置时间限制
        if($time) $condition['UserConsume.payedTime'] = $time;
        $condition['UserConsume.location'] = $shopCode; // 商家编码
        $condition['UserConsume.status'] = \Consts::PAY_STATUS_PAYED; // 支付状态为已支付
        $subCon['ConsumeOrder.orderNbr'] = array('LIKE', "%$searchWord%");
        $subCon['User.mobileNbr'] = array('LIKE', "%$searchWord%");
        $subCon['_logic'] = 'OR';
        $condition['_complex'] = $subCon;
        $userConsumeMdl = new UserConsumeModel();
        // 获得统计数据
        $joinTable = array(
            array('joinTable' => 'ConsumeOrder', 'joinCon' => 'ConsumeOrder.orderCode = UserConsume.orderCode', 'joinType' => 'inner'),
            array('joinTable' => 'User', 'joinCon' => 'User.userCode = UserConsume.consumerCode', 'joinType' => 'inner'),
        );
        $consumptionNbr = $userConsumeMdl->countConsumeTimes($condition, $joinTable);
        $consumptionAmount = $userConsumeMdl->sumConsumeField($condition, 'deduction + realPay', $joinTable);
        $payAmount = $userConsumeMdl->sumConsumeField($condition, 'realPay', $joinTable);
        return array(
            'consumptionNbr' => $consumptionNbr, // 消费次数
            'consumptionAmount' => $consumptionAmount / \Consts::HUNDRED, // 消费金额
            'payAmount' => $payAmount / \Consts::HUNDRED, // 支付金额
        );
    }

    /**
     * 提交入驻申请
     * @param string $shopCode 商家编码
     * @param string $shopName 商家姓名
     * @param string $tel 联系电话
     * @param string $startTime 开始时间
     * @param string $endTime 结束时间
     * @param string $street 街道地址
     * @param string $mobileNbr 手机号码
     * @return array
     */
    public function applyEntry($shopCode, $shopName, $tel, $startTime, $endTime, $street, $mobileNbr) {
        $condition = array();
        $data = array(
            'shopCode' => $shopCode,
            'shopName' => $shopName,
            'tel' => $tel,
            'startTime' => $startTime,
            'endTime' => $endTime . '59',
            'street' => $street,
            'mobileNbr' => $mobileNbr,
        );
        $shopApplyEntryMdl = new ShopApplyEntryModel();
        $ret = $shopApplyEntryMdl->editApplyEntry($condition, $data);
        return $ret;
    }

    /**
     * 保存店长设置
     * @param string $staffCode 店长编码
     * @param string $shopCode 商户编码 多个商家编码以"|"分隔
     * @return string
     */
    public function editOwner($staffCode, $shopCode) {
        $shopStaffRelMdl = new ShopStaffRelModel();
        M()->startTrans();
        // 删除店长原先的所有关系
        $ret = $shopStaffRelMdl->delShopStaffRel(array('staffCode' => $staffCode));
        if($ret['code'] != C('SUCCESS')) {
            M()->rollback();
            return $ret;
        }
        $shopCodeArr = explode('|', $shopCode);
        foreach($shopCodeArr as $v) {
            if($v){
                $staffInfo = $shopStaffRelMdl->getStaffInfoByShopCode($v, C('STAFF_LVL.MANAGER'));
                if($staffInfo) {
                    // 删除选择商家的原先店长
                    $shopStaffRelMdl->delRel($staffInfo['id']);
                }
                // 添加新的关系
                $data = array('shopCode' => $v, 'staffCode' => $staffCode);
                $ret = $shopStaffRelMdl->editShopStaffRel($data);
                if($ret['code'] != C('SUCCESS')){
                    M()->rollback();
                    return $ret;
                }
            }
        }
        M()->commit();
        return $ret;
    }

    /**
     * 大店长获得门店设置
     * @param string $staffCode 店长编码
     * @param int $page 页码，从1开始
     * @return array
     */
    public function getStoreBelong($staffCode, $page) {
        $shopStaffMdl = new ShopStaffModel();
        // 获得店长信息
        $managerInfo = $shopStaffMdl->getShopStaffInfo(array('staffCode' => $staffCode), array('realName', 'mobileNbr', 'parentCode', 'access'));
        $shopStaffRelMdl = new ShopStaffRelModel();
        if($managerInfo['parentCode']) {
            // 获得大店长信息
            $bigMngInfo = $shopStaffMdl->getShopStaffInfo(array('staffCode' => $managerInfo['parentCode']), array('realName', 'mobileNbr', 'staffCode', 'access'));
        } else {
            // 获得店长的商户
            $mngShopInfo = $shopStaffRelMdl->getShopInfoByStaffCode($staffCode, array('Shop.shopCode', 'Shop.parentCode'));
            // 获得该商户的大店长
            $bigMngInfo = $shopStaffRelMdl->getStaffInfoByShopCode($mngShopInfo['shopCode'], C('STAFF_LVL.BIG_MANAGER'));
        }
        // 获得大店长下的所有商户
        $shopList = $shopStaffRelMdl->getShopListByStaffCode($bigMngInfo['staffCode'], array('Shop.shopCode'));
        $shopCodeList = array();
        foreach($shopList as $shop) {
            $shopCodeList[] = $shop['shopCode'];
        }
        if($shopCodeList) {
            $condition = array('Shop.shopCode' => array('IN', $shopCodeList));
            $shopMdl = new ShopModel();
            $shopCount = $shopMdl->countShop($condition);
            $shopList = $shopMdl->listShop($condition, $this->getPager($page, 16));
        } else {
            $shopCount = 0;
            $shopList =array();
        }
        foreach($shopList as &$shop) {
            $manager = $shopStaffRelMdl->getStaffInfoByShopCode($shop['shopCode'], C('STAFF_LVL.MANAGER'));
            $shop['isOwner'] = $manager['staffCode'] == $staffCode ? C('YES') : C('NO');
            $shop['id'] = $manager['id'] ? $manager['id'] : '';
            $shop['managerName'] = $manager['realName'] ? $manager['realName'] : '';
        }
        return array(
            'manager' => $managerInfo,
            'totalCount' => $shopCount,
            'shopList' => $shopList,
            'page' => $page,
            'count' => count($shopList)
        );
    }

    /**
     * 获得店员管理
     * @param string $shopCode 商家编码
     * @param int $page 页码，从1开始
     * @return array
     */
    public function getClerkAdmin($shopCode, $page) {
        $shopStaffRelMdl = new ShopStaffRelModel();
        // 获得店长信息
        $staffInfo = $shopStaffRelMdl->getStaffInfoByShopCode($shopCode, C('STAFF_LVL.MANAGER'));
        $shopStaffMdl = new ShopStaffModel();
        $managerInfo = $shopStaffMdl->getShopStaffInfo(array('staffCode' => $staffInfo['staffCode'], 'userLvl' => C('STAFF_LVL.MANAGER')), array('realName', 'mobileNbr', 'brandId', 'access'));
        // 获得普通店员信息
        $condition = array('shopCode' => $shopCode, 'userLvl' => C('STAFF_LVL.EMPLOYEE'));
        $staffCount = $shopStaffMdl->countShopStaff($condition);
        $staffList = $shopStaffMdl->listShopStaff($condition, $this->getPager($page));
        return array(
            'managerInfo' => $managerInfo,
            'totalCount' => $staffCount,
            'staffList' => $staffList,
            'page' => $page,
            'count' => count($staffList)
        );
    }

    /**
     * 添加，修改店长和店员
     * @param string $staffCode 店员帮忙
     * @param string $mobileNbr 店员手机号
     * @param string $realName 店员姓名
     * @param int $userLvl 店员类型,1-员工；2-店长；
     * @param string $shopCode 商家编码
     * @param string $brandId 品牌ID
     * @return array
     */
    public function editStaff($staffCode, $mobileNbr, $realName, $userLvl, $shopCode, $brandId) {
        $postData = array(
            'staffCode' => $staffCode,
            'mobileNbr' => $mobileNbr,
            'realName' => $realName,
            'userLvl' => $userLvl,
//            'brandId' => $brandId
        );
        $shopStaffMdl = new ShopStaffModel();
        $editStaffRet = $shopStaffMdl->editShopStaff($postData);
        if($editStaffRet['code'] == C('SUCCESS') && empty($staffCode)) {
            if($userLvl == C('STAFF_LVL.EMPLOYEE')) {
                $shopStaffRelMdl = new ShopStaffRelModel();
                $data = array(
                    'staffCode' => $editStaffRet['staffCode'],
                    'shopCode' => $shopCode,
                );
                $ret = $shopStaffRelMdl->editShopStaffRel($data);
            }
            return $editStaffRet;
        }
        return $editStaffRet;
    }

    /**
     * 添加，修改店长和店员0.2
     * @param string $staffCode 店员帮忙
     * @param string $mobileNbr 店员手机号
     * @param string $realName 店员姓名
     * @param int $userLvl 店员类型,1-员工；2-店长；
     * @param string $shopCode 商家编码
     * @param string $parentCode 父编码
     * @return array
     */
    public function editStaffB($staffCode, $mobileNbr, $realName, $userLvl, $shopCode, $parentCode) {
        $postData = array(
            'staffCode' => $staffCode,
            'mobileNbr' => $mobileNbr,
            'realName' => $realName,
            'userLvl' => $userLvl,
            'parentCode' => $parentCode,
        );
        $shopStaffMdl = new ShopStaffModel();
        $editStaffRet = $shopStaffMdl->editShopStaff($postData);
        if($editStaffRet['code'] == C('SUCCESS') && empty($staffCode)) {
            if($userLvl == C('STAFF_LVL.EMPLOYEE')) {
                $shopStaffRelMdl = new ShopStaffRelModel();
                $data = array(
                    'staffCode' => $editStaffRet['staffCode'],
                    'shopCode' => $shopCode,
                );
                $ret = $shopStaffRelMdl->editShopStaffRel($data);
            }
            return $editStaffRet;
        }
        return $editStaffRet;
    }

    /**
     * 获得店长管理
     * @param string $staffCode 店员编码。大店长或者店长的编码
     * @param int $page 页码，从1开始
     * @return array $staffList
     */
    public function getManAdmin($staffCode, $page) {
        $shopStaffMdl = new ShopStaffModel();
        $shopStaffRelMdl = new ShopStaffRelModel();
        // 获得店员的信息
        $staffInfo = $shopStaffMdl->getShopStaffInfo(array('staffCode' => $staffCode), array('userLvl', 'realName', 'mobileNbr', 'staffCode', 'shopCode', 'brandId', 'parentCode', 'access'));
        if($staffInfo['userLvl'] == C('STAFF_LVL.BIG_MANAGER')) { // 大店长
            $bManager = $staffInfo;
            // 获得大店长下的店长
            $condition = array('ShopStaff.parentCode' => $staffCode, 'userLvl' => C('STAFF_LVL.MANAGER'));
            $manager = $shopStaffMdl->listShopStaff($condition, $this->getPager($page));
            $managerCount = $shopStaffMdl->countShopStaff($condition);
        } elseif($staffInfo['userLvl'] == C('STAFF_LVL.MANAGER')) { // 店长
            // 获得该商户的大店长
            $bManager = $shopStaffMdl->getShopStaffInfo(array('staffCode' => $staffInfo['parentCode']), array('staffCode', 'mobileNbr', 'realName', 'wechatId', 'userLvl', 'status', 'avatarUrl', 'brandId', 'access'));
            $manager = array($staffInfo);
            $managerCount = 1;
        } else {
            $manager = array();
            $managerCount = 0;
        }

        // 获得店长管理的商店
        foreach($manager as &$staff) {
            $staff['shopList'] = $shopStaffRelMdl->getShopListByStaffCode($staff['staffCode'], array('Shop.shopCode', 'shopName', 'logoUrl', 'status'));
        }
        return array(
            'bManager' => $bManager ? $bManager : array(),
            'totalCount' => $managerCount,
            'manager' => $manager,
            'page' => $page,
            'count' => count($manager)
        );
    }

    /**
     * 大店长，店长获得所拥有的商户
     * @param string $staffCode 店员编码
     * @param int $page 页码，从1开始
     * @return array $shopList
     */
    public function getStaffShopList($staffCode, $page) {
        $shopStaffRelMdl = new ShopStaffRelModel();
        $shopList = $shopStaffRelMdl->getShopListByStaffCode($staffCode, array('Shop.shopCode'));
        if(empty($shopList)) {
            $shopList = array();
            $shopCount = 0;
        } else {
            $shopCodeList = array();
            foreach($shopList as $shop) {
                $shopCodeList[] = $shop['shopCode'];
            }
            $condition = array('Shop.shopCode' => array('IN', $shopCodeList));
            $shopMdl = new ShopModel();
            $shopList = $shopMdl->listShop($condition, $this->getPager($page));
            $shopCount = $shopMdl->countShop($condition);
        }
        return array(
            'totalCount' => $shopCount,
            'shopList' => $shopList,
            'page' => $page,
            'count' => count($shopList),
            'nextPage' => $page + 1,
        );
    }

    /**
     * 处理退款
     * @param string $orderCode 订单编码
     * @param int $isAgree 是否同意。0-不同意；1-同意；
     * @return array $ret
     */
    public function dealRefund($orderCode, $isAgree) {
        $consumeOrderMdl = new ConsumeOrderModel();
        if($isAgree == C('YES')) {
            $ret = $consumeOrderMdl->agreeRefund($orderCode);
        } else {
            $ret = $consumeOrderMdl->rejectRefund($orderCode);
        }
        return $ret;
    }

    /**
     * 提交结算
     * @param string $orderCode 订单编码
     * @param float $actualOrderAmount 结算金额。单位：元
     * @param array $orderProductList 勾选的订单产品ID列表:{1, 22, 333, 12}
     * @return array
     */
    public function submitEnd($orderCode, $actualOrderAmount, $orderProductList) {
        $consumeOrderMdl = new ConsumeOrderModel();
        // 判断订单是否能够提交结算
        $orderInfo = $consumeOrderMdl->getOrderInfo(array('orderCode' => $orderCode, 'status' => array('IN', array(\Consts::PAY_STATUS_CAN_NOT_PAY, \Consts::PAY_STATUS_UNPAYED))), array('orderCode'));
        if(!$orderInfo) {
            return array('code' => C('ORDER.CAN_NOT_SETTLEMENT'));
        }

        if(!is_array($orderProductList)) {
            $orderProductList = json_decode($orderProductList, true);
        }
        $orderProductIdList = array();
        $orderProductMdl = new OrderProductModel();
        foreach($orderProductList as $orderProductId) {
            // 列表内的产品，全部已上。
            $ret = $orderProductMdl->allServed($orderProductId);
            $orderProductIdList[] = $orderProductId;
        }

        $condition = array('orderCode' => $orderCode);
        if(!empty($orderProductIdList)) {
            $condition['orderProductId'] = array('NOTIN', $orderProductIdList);
        }
        $unavailableProductList = $orderProductMdl->getOrderProductList($condition);
        foreach($unavailableProductList as $orderProduct) {
            $orderProductId = $orderProduct['orderProductId'];
            // 列表内的产品，全部未上。
            $orderProductMdl->allNoServed($orderProductId);
        }

        // 得到实际价格，修改订单实际价格
        if(empty($actualOrderAmount)) {
            $actualOrderAmount = $orderProductMdl->calActualOrderAmount($orderCode);
        } else {
            $actualOrderAmount = $actualOrderAmount * \Consts::HUNDRED;
        }

        // 修改支付状态为未支付，修改订单实际金额，修改订单状态为已送达
        $code = $consumeOrderMdl->updateConsumeOrder(
            array('orderCode' => $orderCode),
            array('orderAmount' => $actualOrderAmount, 'status' => \Consts::PAY_STATUS_UNPAYED, 'orderStatus' => \Consts::ORDER_STATUS_SERVED)
        ) === true ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return array('code' => $code);
    }

    /**
     * 获得时间区间
     * @param string $type 类型，D-日，W-周，M-月
     * @param string $value 日的格式：xx-xx(月-日)；周的格式：数字；月的格式：数字；
     * @return array 时间区间。{'startDate' => '', 'endDate' => ''}
     */
    private function getTimeInterval($type, $value) {
        if($type == 'D') { // 查询某天的订单
            if(strlen($value) == strlen('yyyy-mm-dd')) {
                $date = $value;
            } else {
                $thisYear = date('Y', time()); // 当前年份yyyy
                $date = "$thisYear-$value"; // 日期 yyyy-mm-dd
            }
            $startDate = "$date 00:00:00";
            $endDate = "$date 23:59:59";
        } elseif($type == 'W') { // 查询某一周订单
            $currWeek = date('W'); // 当前是第几周
            $weekDiff = $currWeek - $value;
            $currweek = date('w'); // 当天是周几
            $currMonUnix = strtotime(date('Y-m-d 00:00:00',strtotime( '+'. 1 - $currweek .' days' )));
            $chooseMonUnix = $currMonUnix - $weekDiff * 7 * 86400;
            $startDate = date('Y-m-d H:i:s', $chooseMonUnix);
            $endDate = date('Y-m-d 23:59:59', strtotime("$startDate +6 day"));
        } else {
            $value = sprintf('%02d', $value);
            $startDate = $startDate = date('Y-' . $value . '-01 00:00:00');
            $endDate = date('Y-m-d 23:59:59', strtotime("$startDate +1 month -1 day"));
        }
        return array('startDate' => $startDate, 'endDate' => $endDate);
    }

    /**
     * 获得门店订单和外卖订单
     * @param string $shopCode 商家编码
     * @param string $keyWard 关键词。订单号后4位/用户手机号/餐号
     * @param int $value 日的格式：xx-xx(月-日)；周的格式：数字；月的格式：数字；
     * @param string $unit D-日，W-周，M-月
     * @param int $status 状态。0-未选择，所有；1-未处理；2-待付款；3-配送中；4-交易成功；5-交易取消；
     * @param int $orderType 订单类型 20-堂食订单；21-外卖订单；
     * @return array
     */
    public function getOrder($shopCode, $keyWard, $value, $unit, $status, $orderType) {
        $date = $this->getTimeInterval($unit, $value);
        $condition = array(
            'ConsumeOrder.shopCode' => $shopCode,
            'orderTime' => array('BETWEEN', array($date['startDate'], $date['endDate'])),
            'orderType' => $orderType,
            'isFinishOrder' => C('YES'),
        );
        if(! empty($keyWard)) {
            $subCondition = array(
                'orderNbr' => array('LIKE', "%$keyWard"),
                'User.mobileNbr' => array('LIKE', "%$keyWard%"),
                'mealNbr' => array('LIKE', "%$keyWard%"),
                '_logic' => 'OR'
            );
            $condition['_complex'] = $subCondition;
        }
        $untreatedOrder = $unpayOrder = $deliveryOrder = $succOrder = $cancelOrder = array();
        $consumeOrderMdl = new ConsumeOrderModel();
        $untreatedString = '(ConsumeOrder.orderStatus = ' . C('FOOD_ORDER_STATUS.ORDERED') . ' AND ConsumeOrder.status IN (' . C('ORDER_STATUS.UNPAYED') . ',' . C('ORDER_STATUS.CAN_NOT_PAY') . ')) OR (ConsumeOrder.status = ' . C('ORDER_STATUS.REFUNDING') . ')';
        switch($status) {
            case '0':
                // 未处理
                $condition['_string'] = $untreatedString;
                $untreatedOrder = $consumeOrderMdl->listAllProductOrder($condition);
                unset($condition['_string']);

                // 待付款
                $condition['ConsumeOrder.status'] = array('IN', array(C('ORDER_STATUS.UNPAYED'), C('ORDER_STATUS.CAN_NOT_PAY')));
                $condition['orderStatus'] = array('NOTIN', array(C('FOOD_ORDER_STATUS.ORDERED'), C('FOOD_ORDER_STATUS.CANCELED')));
                $unpayOrder = $consumeOrderMdl->listAllProductOrder($condition);

                // 配送中
                $condition['ConsumeOrder.status'] = array('IN', array(C('ORDER_STATUS.PAYED')));
                $condition['orderStatus'] = array('IN', array(C('FOOD_ORDER_STATUS.DELIVERED'), C('FOOD_ORDER_STATUS.RECEIVED')));
                $deliveryOrder = $consumeOrderMdl->listAllProductOrder($condition);

                // 交易成功
                $condition['ConsumeOrder.status'] = C('ORDER_STATUS.PAYED');
                $condition['orderStatus'] = array('EQ', C('FOOD_ORDER_STATUS.SERVED'));
                $succOrder = $consumeOrderMdl->listAllProductOrder($condition);

                // 交易失败
                unset($condition['ConsumeOrder.status']);
                $condition['orderStatus'] = array('EQ', C('FOOD_ORDER_STATUS.CANCELED'));
                $cancelOrder = $consumeOrderMdl->listAllProductOrder($condition);
                return $cancelOrder;
                break;
            case '1':
                // 未处理
                $condition['_string'] = $untreatedString;
                $untreatedOrder = $consumeOrderMdl->listAllProductOrder($condition);
                break;
            case '2':
                // 待付款
                $condition['ConsumeOrder.status'] = array('IN', array(C('ORDER_STATUS.UNPAYED'), C('ORDER_STATUS.CAN_NOT_PAY')));
                $condition['orderStatus'] = array('NOTIN', array(C('FOOD_ORDER_STATUS.ORDERED'), C('FOOD_ORDER_STATUS.CANCELED')));
                $unpayOrder = $consumeOrderMdl->listAllProductOrder($condition);
                break;
            case '3':
                // 配送中
                $condition['ConsumeOrder.status'] = array('IN', array(C('ORDER_STATUS.PAYED')));
                $condition['orderStatus'] = array('IN', array(C('FOOD_ORDER_STATUS.DELIVERED'), C('FOOD_ORDER_STATUS.RECEIVED')));
                $deliveryOrder = $consumeOrderMdl->listAllProductOrder($condition);
                break;
            case '4':
                // 交易成功
                $condition['ConsumeOrder.status'] = C('ORDER_STATUS.PAYED');
                $condition['orderStatus'] = array('EQ', C('FOOD_ORDER_STATUS.SERVED'));
                $succOrder = $consumeOrderMdl->listAllProductOrder($condition);
                break;
            case '5':
                // 交易失败
                $condition['orderStatus'] = array('EQ', C('FOOD_ORDER_STATUS.CANCELED'));
                $cancelOrder = $consumeOrderMdl->listAllProductOrder($condition);
                break;
        }
        return array(
            array('title' => '未处理', 'orderList' => $untreatedOrder, 'count' => count($untreatedOrder)),
            array('title' => '待付款', 'orderList' => $unpayOrder, 'count' => count($unpayOrder)),
            array('title' => '配送中', 'orderList' => $deliveryOrder, 'count' => count($deliveryOrder)),
            array('title' => '交易成功', 'orderList' => $succOrder, 'count' => count($succOrder)),
            array('title' => '交易失败', 'orderList' => $cancelOrder, 'count' => count($cancelOrder)),
        );
    }

    /**
     * 获得门店订单和外卖订单
     * @param string $shopCode 商家编码
     * @param string $keyWard 关键词。订单号后4位/用户手机号/餐号
     * @param int $value 日的格式：mm-dd(月-日)或yyyy-mm-dd(年-月-日)；周的格式：数字；月的格式：数字；
     * @param string $unit D-日，W-周，M-月
     * @param int $status 状态。0-未选择，所有；0-未选择，所有；20-已下单；21-已接单；22-已派送；23-已撤单；24-待下单；
     * @param int $orderType 订单类型 20-堂食订单；21-外卖订单；
     * @return array
     */
    public function getOrderB($shopCode, $keyWard, $value, $unit, $status, $orderType) {
        $date = $this->getTimeInterval($unit, $value);
        $condition = array(
            'ConsumeOrder.shopCode' => $shopCode,
            'orderTime' => array('BETWEEN', array($date['startDate'], $date['endDate'])),
            'orderType' => $orderType,
            'isFinishOrder' => C('YES'),
        );
        if(! empty($keyWard)) {
            $subCondition = array(
                'orderNbr' => array('LIKE', "%$keyWard"),
                'User.mobileNbr' => array('LIKE', "%$keyWard%"),
                'mealNbr' => array('LIKE', "%$keyWard%"),
                '_logic' => 'OR',
            );
            $condition['_complex'] = $subCondition;
        }
        $orderedOrder = $receivedOrder = $deliveryOrder = $servedOrder = $canceledOrder = array();
        $consumeOrderMdl = new ConsumeOrderModel();
        switch($status) {
            case '0':
                $condition['ConsumeOrder.orderStatus'] = \Consts::CATERING_ORDER_STATUS_ORDERED; // 订单状态为已下单
                $orderedOrder = $consumeOrderMdl->listAllProductOrder($condition);

                $condition['ConsumeOrder.orderStatus'] = \Consts::CATERING_ORDER_STATUS_RECEIVED; // 订单状态为已接单
                $receivedOrder = $consumeOrderMdl->listAllProductOrder($condition);

                $condition['ConsumeOrder.orderStatus'] = \Consts::CATERING_ORDER_STATUS_DELIVERED; // 订单状态为已配送
                $deliveryOrder = $consumeOrderMdl->listAllProductOrder($condition);

                $condition['ConsumeOrder.orderStatus'] = \Consts::CATERING_ORDER_STATUS_SERVED; // 订单状态为送达
                $servedOrder = $consumeOrderMdl->listAllProductOrder($condition);

                $condition['ConsumeOrder.orderStatus'] = \Consts::CATERING_ORDER_STATUS_CANCELED; // 订单状态为已撤销
                $condition['ConsumeOrder.status'] = array('NEQ', \Consts::PAY_STATUS_UNPAYED); // 支付状态不为未付款
                $canceledOrder = $consumeOrderMdl->listAllProductOrder($condition);
                break;
            case \Consts::CATERING_ORDER_STATUS_ORDERED:
                $condition['ConsumeOrder.orderStatus'] = \Consts::CATERING_ORDER_STATUS_ORDERED; // 订单状态为已下单
                $orderedOrder = $consumeOrderMdl->listAllProductOrder($condition);
                break;
            case \Consts::CATERING_ORDER_STATUS_RECEIVED:
                $condition['ConsumeOrder.orderStatus'] = \Consts::CATERING_ORDER_STATUS_RECEIVED; // 订单状态为已接单
                $receivedOrder = $consumeOrderMdl->listAllProductOrder($condition);
                break;
            case \Consts::CATERING_ORDER_STATUS_DELIVERED:
                $condition['ConsumeOrder.orderStatus'] = \Consts::CATERING_ORDER_STATUS_DELIVERED; // 订单状态为已配送
                $deliveryOrder = $consumeOrderMdl->listAllProductOrder($condition);
                break;
            case \Consts::CATERING_ORDER_STATUS_SERVED:
                $condition['ConsumeOrder.orderStatus'] = \Consts::CATERING_ORDER_STATUS_SERVED; // 订单状态为送达
                $servedOrder = $consumeOrderMdl->listAllProductOrder($condition);
                break;
            case \Consts::CATERING_ORDER_STATUS_CANCELED:
                $condition['ConsumeOrder.orderStatus'] = \Consts::CATERING_ORDER_STATUS_CANCELED; // 订单状态为已撤销
                $condition['ConsumeOrder.status'] = array('NEQ', \Consts::PAY_STATUS_UNPAYED); // 支付状态不为未付款
                $canceledOrder = $consumeOrderMdl->listAllProductOrder($condition);
                break;
        }
        return array(
            array('orderStatus' => \Consts::CATERING_ORDER_STATUS_ORDERED, 'title' => '已下单', 'orderList' => $orderedOrder, 'count' => count($orderedOrder)),
            array('orderStatus' => \Consts::CATERING_ORDER_STATUS_RECEIVED, 'title' => '已接单', 'orderList' => $receivedOrder, 'count' => count($receivedOrder)),
            array('orderStatus' => \Consts::CATERING_ORDER_STATUS_DELIVERED, 'title' => '已派送', 'orderList' => $deliveryOrder, 'count' => count($deliveryOrder)),
            array('orderStatus' => \Consts::CATERING_ORDER_STATUS_SERVED, 'title' => '已送达', 'orderList' => $servedOrder, 'count' => count($servedOrder)),
            array('orderStatus' => \Consts::CATERING_ORDER_STATUS_CANCELED, 'title' => '已撤单', 'orderList' => $canceledOrder, 'count' => count($canceledOrder)),
        );
    }

    /**
     * 订单新增菜品
     * @param string $orderCode 订单编码
     * @param array $productList 产品列表
     * @return array $ret
     */
    public function addNewOrderProduct($orderCode, $productList) {
        $consumeOrderMdl = new ConsumeOrderModel();
        // 获得订单的信息（支付状态）
        $orderInfo =  $consumeOrderMdl->getOrderInfo(array('orderCode' => $orderCode), array('status'));
        // 支付状态为支付中，已付款，已取消订单，退款申请中，退款成功的订单，不能添加新的菜品
        if(in_array($orderInfo['status'], array(\Consts::PAY_STATUS_PAYING, \Consts::PAY_STATUS_PAYED, \Consts::PAY_STATUS_CANCELED, \Consts::PAY_STATUS_REFUNDING, \Consts::PAY_STATUS_REFUNDED))) {
            return array('code' => C('ORDER_PRODUCT.CAN_NOT_ADD_NEW_PRODUCT'));
        }
        $orderProductMdl = new OrderProductModel();
        $orderProductIdList = array();
        $orderProductMdl->startTrans();
        $productMdl = new ProductModel();
        $consumeOrderMdl = new ConsumeOrderModel();
        foreach($productList as &$product) {
            // 获得商品的信息
            $productInfo = $productMdl->getProductInfo(array('productId' => $product['productId']));
            $product['productUnitPrice'] = $productInfo['notTakeoutPrice'];
            $product['orderCode'] = $orderCode;
            $product['isNewlyAdd'] = C('YES'); // 设置为新增商品
            $ret = $orderProductMdl->addOrderProduct($product);
            if($ret['code'] !== C('SUCCESS')) {
                $orderProductMdl->rollback();
                return array('code' => C('API_INTERNAL_EXCEPTION'));
            }
            $value = $product['productUnitPrice'] * $product['productNbr'];
            // 更新订单总价
            $consumeOrderMdl->incField(array('orderCode' => $orderCode), 'orderAmount', $value);
            // 更新订单实际总价
            $consumeOrderMdl->incField(array('orderCode' => $orderCode), 'actualOrderAmount', $value);
            $orderProductIdList[] = $ret['orderProductId'];
        }
        $orderProductMdl->commit();
        return array('code' => C('SUCCESS'), 'orderProductList' => $orderProductIdList);
    }

    /**
     * 修改订单中产品信息
     * @param int $orderProductId 订单产品ID
     * @param int $availableNbr 已上数量
     * @return array
     */
    public function updateOrderProductInfo($orderProductId, $availableNbr) {
        $orderProductMdl = new OrderProductModel();
        $orderProductInfo = $orderProductMdl->getOrderProductInfo(array('orderProductId' => $orderProductId), array('productNbr', 'orderCode'));
        if($availableNbr > $orderProductInfo['productNbr']) {
            return array('code' => C('ORDER_PRODUCT.AVAILABLE_OVER'));
        }
        $data = array(
            'orderProductId' => $orderProductId,
            'availableNbr' => $availableNbr,
            'unavailableNbr' => $orderProductInfo['productNbr'] - $availableNbr,
        );
        $ret = $orderProductMdl->editOrderProduct($data);
        if($ret['code'] == C('SUCCESS')) {
            $orderCode = $orderProductInfo['orderCode'];
            $actualOrderAmount = $orderProductMdl->calActualOrderAmount($orderCode);
            $consumeOrderMdl = new ConsumeOrderModel();
            // 修改订单的实际消费金额
            $result = $consumeOrderMdl->updateConsumeOrder(array('orderCode' => $orderCode), array('orderAmount' => $actualOrderAmount));
            $ret['code'] = $result === true ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        }
        return $ret;
    }

    /**
     * 修改订单
     * @param string $orderCode 商家编码
     * @param int $orderStatus 订单状态。20-已下单，21-已接单，22-已派送，23-已送达，24-已撤销,25-待下单
     * @param int $status 支付状态。1-未支付;2-支付中;3-已支付;4-已取消订单;5-支付失败;6-退款申请中;7-退款成功;
     * @param string $tableNbr 桌号
     * @return array
     */
    public function updateProductOrderStatus($orderCode, $orderStatus, $status, $tableNbr = '') {
        $consumeOrderMdl = new ConsumeOrderModel();
        // 当订单状态是已撤销，或者支付状态是退款成功时，则执行撤销订单操作
        if($orderStatus == \Consts::ORDER_STATUS_CANCELED || $status == \Consts::PAY_STATUS_REFUNDED) {
            // 撤销订单
            $ret = $consumeOrderMdl->cancelConsumeOrder($orderCode);
            return $ret;
        }
        // 保存订单的订单状态和支付状态
        $ret = $consumeOrderMdl->updateConsumeOrder(array('orderCode' => $orderCode), array('orderStatus' => $orderStatus, 'status' => $status, 'tableNbr' => $tableNbr ? $tableNbr : ''));
        $code = $ret === true ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return array('code' => $code);
    }

    /**
     * 获得产品订单详情
     * @param string $orderCode 订单编码
     * @param string $isNewlyAdd 是否查询新增的产品。0-查询所有的，1-只查询新增，''或null查询所有的
     * @return array $orderInfo
     */
    public function getProductOrderInfo($orderCode, $isNewlyAdd) {
        $consumeOrderMdl = new ConsumeOrderModel();
        $orderInfo = $consumeOrderMdl->getOrderInfo(array('orderCode' => $orderCode));
        $orderInfo['orderAmount'] = $orderInfo['orderAmount'] / C('RATIO');
        $orderInfo['actualOrderAmount'] = $orderInfo['actualOrderAmount'] / C('RATIO');
        $orderInfo['receiveTime'] = date('H:i', strtotime($orderInfo['orderTime']) + 10 * 60);

        $orderProductMdl = new OrderProductModel();
        $orderInfo['orderProductAmount'] = $orderProductMdl->sumOrderProductNbr($orderCode); // 已经点了的产品总数
        $orderInfo['oldAmount'] = $orderProductMdl->sumOrderProductNbr($orderCode, 0); // 旧的点了的产品总数
        $orderInfo['newAmount'] = $orderProductMdl->sumOrderProductNbr($orderCode, 1); // 新的点了的产品总数
        $orderInfo['productList'] = $orderProductMdl->getProductListByOrder($orderCode, $isNewlyAdd);

        $userConsumeMdl = new UserConsumeModel();
        $consumeInfo = $userConsumeMdl->getPayedConsumeInfoByOrderCode($orderCode);
        $tempb = array('realPay', 'deduction', 'shopBonus', 'platBonus', 'bankCardDeduction', 'cardDeduction', 'couponDeduction', 'firstDeduction');
        foreach($tempb as $v) {
            $consumeInfo[$v] = number_format($consumeInfo[$v] / C('RATIO'), '2', '.', '');
            $orderInfo[$v] = $consumeInfo[$v];
        }
        $orderInfo['consumeCode'] = $consumeInfo['consumeCode'];
        $orderInfo['payType'] = $consumeInfo['payType'] ? $consumeInfo['payType'] : '';
        $orderInfo['couponUsed'] = $consumeInfo['couponUsed'] ? $consumeInfo['couponUsed'] : '';

        $userCouponInfo = array();
        if($consumeInfo['couponUsed'] >= C('YES')) { // 如果使用了优惠券，获得使用的优惠券的信息
            $userCouponMdl = new UserCouponModel();
            $userCouponInfo = $userCouponMdl->getUserCouponInfoB(
                array('UserCoupon.consumeCode' => $consumeInfo['consumeCode']),
                array('UserCoupon.userCouponCode', 'BatchCoupon.insteadPrice', 'couponType', 'discountPercent', 'availablePrice', 'UserCoupon.applyTime', 'BatchCoupon.validityPeriod', 'BatchCoupon.expireTime')
            );
        }
        $userCouponInfo = $userCouponInfo[0];
        $userCouponInfo['availablePrice'] = $userCouponInfo['availablePrice'] / C('RATIO');
        $userCouponInfo['insteadPrice'] = $userCouponInfo['insteadPrice'] / C('RATIO');
        $userCouponInfo['discountPercent'] = $userCouponInfo['discountPercent'] / C('DISCOUNT_RATIO');
        $orderInfo['availablePrice'] = $userCouponInfo['availablePrice'] ? $userCouponInfo['availablePrice'] : '';
        $orderInfo['insteadPrice'] = $userCouponInfo['insteadPrice'] ? $userCouponInfo['insteadPrice'] : '';
        $orderInfo['discountPercent'] = $userCouponInfo['discountPercent'] ? $userCouponInfo['discountPercent'] : '';
        $orderInfo['couponType'] = $userCouponInfo['couponType'] ? $userCouponInfo['couponType'] : '';

        // 获得桌号管理的状态
        $shopMdl = new ShopModel();
        $tableNbrSwitch = $shopMdl->getShopFieldList('tableNbrSwitch', array('shopCode' => $orderInfo['shopCode']));
        $orderInfo['tableNbrSwitch'] = $tableNbrSwitch[0];
        return $orderInfo;
    }

    /**
     * 获得产品订单列表
     * @param string $shopCode 商家编码
     * @param int $orderStatus 20-已下单，21-已接单，22-已派送，23-已送达，24-已撤销,25-待下单
     * @param int $orderType 10-其他订单，20-堂食订单，21-外卖订单
     * @return array $orderList
     */
    public function listProductOrder($shopCode, $orderStatus, $orderType) {
        $consumeOrderMdl = new ConsumeOrderModel();
        $orderList = $consumeOrderMdl->listAllProductOrder(array('ConsumeOrder.shopCode' => $shopCode, 'orderStatus' => $orderStatus, 'orderType' => $orderType));
        $orderProductMdl = new OrderProductModel();
        foreach($orderList as $k => $order) {
            $order[$k]['productList'] = $orderProductMdl->getProductListByOrder($order['orderCode']);
        }
        return $orderList;
    }

    /**
     * 根据类别获得产品
     * @param int $categoryId 类别ID
     * @param string $shopCode 商家编码
     * @return array $productList
     */
    public function listProductByCategory($categoryId, $shopCode) {
        $productMdl = new ProductModel();
        $productList = $productMdl->getProductList(array('categoryId' => $categoryId, 'shopCode' => $shopCode));
        $pPRelMdl = new PPRelModel();
        foreach($productList as $k => $product) {
            if($product['productType'] == \Consts::PRODUCT_TYPE_PACKAGE) {
                $productList[$k]['pkgContent'] = $pPRelMdl->getPkgPList($product['productId']);
            } else {
                $productList[$k]['pkgContent'] = array();
            }
        }
        return $productList;
    }

    /**
     * 删除产品
     * @param int $productId 产品ID
     * @return array $ret
     */
    public function delProduct($productId) {
        $orderProductMdl = new OrderProductModel();
        $hasOrdered = $orderProductMdl->isProductHadOrdered($productId);
        if($hasOrdered == true) {
            return array('code' => C('PRODUCT.CAN_NOT_DELETE'));
        }
        $productMdl = new ProductModel();
        $ret = $productMdl->delProduct(array('productId' => $productId));
        return $ret;
    }

    /**
     * 修改产品状态
     * @param int $productId 产品ID
     * @param int $productStatus 产品状态。1-上架，2-下架，3-售罄
     * @return array $ret
     */
    public function editProductStatus($productId, $productStatus) {
        $productMdl = new ProductModel();
        $ret = $productMdl->changeProductStatus(array('productId' => $productId), $productStatus);
        return $ret;
    }

    /**
     * 修改产品
     * @param string $productName 产品名称
     * @param int $categoryId 产品所属类别ID
     * @param string $productImg 产品图片URL
     * @param int $notTakeoutPrice 产品堂食单价,单位：元
     * @param int $takeoutPrice 产品外卖单价,单位：元
     * @param int $recommendLevel 产品推荐度,可取的值：0，1，2，3
     * @param int $spicyLevel 产品辣度,可取的值：0，1，2，3
     * @param string $unit 产品单位
     * @param int $isTakenOut 是否可外送,0-不可以，1-可以
     * @param string $des 产品描述
     * @param string $shopCode 商店编码
     * @param int $sortNbr 排序号
     * @param int $productId 产品ID
     * @param int $productType 产品类型。1-单品，2-套餐
     * @param int $originalPrice 产品原价。单位：分
     * @param int $dropPrice 产品减价幅度。单位：分
     * @param int $discount 产品折扣。单位：折
     * @param int $sexLimit 性别限制。0-无限制，1-男， 2-女
     * @param int $ageLimit 年龄限制。0-无限制，1 - 18岁以上
     * @param int $isBooking 是否预约。0-否，1-是
     * @param int $pId 套餐中单品ID。以"|"分隔，例:"12|21|22"。
     * @param int $pNbr 套餐中单品数量。以"|"分隔，例:"12|21|22"。
     * @return array $ret {'code'}
     */
    public function editProduct($productName, $categoryId, $productImg, $notTakeoutPrice, $takeoutPrice, $recommendLevel, $spicyLevel, $unit, $isTakenOut, $des, $shopCode, $sortNbr, $productId, $productType, $originalPrice, $dropPrice, $discount, $sexLimit, $ageLimit, $isBooking, $pId, $pNbr) {
        $data = array(
            'productName' => $productName,
            'categoryId' => $categoryId,
            'productImg' => $productImg,
            'notTakeoutPrice' => $notTakeoutPrice,
            'takeoutPrice' => $takeoutPrice,
            'recommendLevel' => $recommendLevel,
            'spicyLevel' => $spicyLevel,
            'unit' => $unit,
            'isTakenOut' => $isTakenOut,
            'des' => $des,
            'shopCode' => $shopCode,
            'sortNbr' => $sortNbr,
            'productId' => $productId,
            'productType' => $productType ? $productType : C('PRODUCT_TYPE.SINGLE'),
            'originalPrice' => $originalPrice ? $originalPrice : 0,
            'dropPrice' => $dropPrice ? $dropPrice : 0,
            'discount' => $discount ? $discount : C('HUNDRED'),
            'sexLimit' => $sexLimit ? $sexLimit : 0,
            'ageLimit' => $ageLimit  ? $ageLimit : 0,
            'isBooking' => $isBooking  ? $isBooking : 0,
        );
        $productMdl = new ProductModel();
        $ret = $productMdl->editProduct($data);
        if($ret['code'] === C('SUCCESS') && $productType == \Consts::PRODUCT_TYPE_PACKAGE) { // 修改成功，且商品类型为套餐
            $packageId = $data['productId'];
            $pIdArr = explode('|', $pId);
            $pNbr = explode('|', $pNbr);
            $pPRelMdl = new PPRelModel();
            foreach($pIdArr as $k => $v) {
                // 获得套餐中某一单品的信息
                $pPRelInfo = $pPRelMdl->getPPRelInfo(array('productId' => $v, 'packageId' => $packageId));
                if($pPRelInfo) { // 套餐中该单品已存在
                    $condition = array('id' => $pPRelInfo['id']); // 设置更新信息的条件
                } else {
                    $condition = array();
                }
                $data = array(
                    'productId' => $v,
                    'productNbr' => $pNbr[$k],
                    'packageId' => $packageId,
                );
                // 添加套餐的单品
                $pPRelMdl->editPPRel($condition, $data);
            }
        }
        return $ret;
    }

    /**
     * 添加产品
     * @param string $productName 产品名称
     * @param int $categoryId 产品所属类别ID
     * @param string $productImg 产品图片URL
     * @param int $notTakeoutPrice 产品堂食单价,单位：元
     * @param int $takeoutPrice 产品外卖单价,单位：元
     * @param int $recommendLevel 产品推荐度,可取的值：0，1，2，3
     * @param int $spicyLevel 产品辣度,可取的值：0，1，2，3
     * @param string $unit 产品单位
     * @param int $isTakenOut 是否可外送,0-不可以，1-可以
     * @param string $des 产品描述
     * @param string $shopCode 商店编码
     * @param int $sortNbr 排序号
     * @param int $productType 产品类型。1-单品，2-套餐
     * @param int $originalPrice 产品原价。单位：分
     * @param int $dropPrice 产品减价幅度。单位：分
     * @param int $discount 产品折扣。单位：折
     * @param int $sexLimit 性别限制。0-无限制，1-男， 2-女
     * @param int $ageLimit 年龄限制。0-无限制，1 - 18岁以上
     * @param int $isBooking 是否预约。0-否，1-是
     * @param int $pId 套餐中单品ID。以"|"分隔，例:"12|21|22"。
     * @param int $pNbr 套餐中单品数量。以"|"分隔，例:"12|21|22"。
     * @return array $ret {'code', 'productId'}
     */
    public function addProduct($productName, $categoryId, $productImg, $notTakeoutPrice, $takeoutPrice, $recommendLevel, $spicyLevel, $unit, $isTakenOut, $des, $shopCode, $sortNbr, $productType, $originalPrice, $dropPrice, $discount, $sexLimit, $ageLimit, $isBooking, $pId, $pNbr) {
        $data = array(
            'productName' => $productName,
            'categoryId' => $categoryId,
            'productImg' => $productImg,
            'notTakeoutPrice' => $notTakeoutPrice,
            'takeoutPrice' => $takeoutPrice,
            'recommendLevel' => $recommendLevel,
            'spicyLevel' => $spicyLevel,
            'unit' => $unit,
            'isTakenOut' => $isTakenOut,
            'des' => $des,
            'shopCode' => $shopCode,
            'sortNbr' => $sortNbr,
            'productStatus' => C('PRODUCT_STATUS.ON_SHELF'),
            'productType' => $productType ? $productType : C('PRODUCT_TYPE.SINGLE'),
            'originalPrice' => $originalPrice ? $originalPrice : 0,
            'dropPrice' => $dropPrice ? $dropPrice : 0,
            'discount' => $discount ? $discount * C('TEN') : C('HUNDRED'),
            'sexLimit' => $sexLimit ? $sexLimit : 0,
            'ageLimit' => $ageLimit  ? $ageLimit : 0,
            'isBooking' => $isBooking  ? $isBooking : 0,
        );
        $productMdl = new ProductModel();
        $ret = $productMdl->editProduct($data);
        if($ret['code'] === C('SUCCESS') && $productType == \Consts::PRODUCT_TYPE_PACKAGE) { // 添加成功，且商品类型为套餐
            $packageId = $ret['productId']; // 设置套餐ID
            $pIdArr = explode('|', $pId);
            $pNbr = explode('|', $pNbr);
            $pPRelMdl = new PPRelModel();
            foreach($pIdArr as $k => $v) {
                $data = array(
                    'productId' => $v,
                    'productNbr' => $pNbr[$k],
                    'packageId' => $packageId,
                );
                // 添加套餐的单品
                $pPRelMdl->editPPRel(array(), $data);
            }
        }
        return $ret;
    }

    /**
     * 删除产品类别
     * @param int $categoryId 类别ID
     * @return array $ret
     */
    public function delProductCategory($categoryId) {
        $productMdl = new ProductModel();
        // 获得类别中，上架或售罄的商品
        $productList = $productMdl->getProductList(array('categoryId' => $categoryId, 'productStatus' => array('NEQ', C('PRODUCT_STATUS.OFF_SHELF'))));
        if(!empty($productList)) {
            return array('code' => C('PRODUCT_CATEGORY.CAN_NOT_DELETE'));
        }
        $productCategoryMdl = new ProductCategoryModel();
        $ret = $productCategoryMdl->delProductCategory($categoryId);
        return $ret;
    }

    /**
     * 获得产品类别列表
     * @param string $shopCode 商家编码
     * @return array $categoryList 类别列表
     */
    public function listProductCategory($shopCode) {
        $productCategoryMdl = new ProductCategoryModel();
        $categoryList = $productCategoryMdl->getProductCategoryList(
            array('shopCode' => $shopCode),
            array('categoryId', 'categoryName')
        );
        return $categoryList;
    }

    /**
     * 修改产品类别
     * @param string $categoryName 产品类别名称
     * @param $categoryId
     * @return array $ret {'code'}
     */
    public function editProductCategory($categoryName, $categoryId) {
        $data = array('categoryName' => $categoryName, 'categoryId' => $categoryId);
        $productCategoryMdl = new ProductCategoryModel();
        $ret = $productCategoryMdl->editProductCategory($data);
        return $ret;
    }

    /**
     * 添加产品类别
     * @param string $categoryName 产品类别名称
     * @param string $shopCode 商家编码
     * @return array $ret {'code'}
     */
    public function addProductCategory($categoryName, $shopCode) {
        $data = array('categoryName' => $categoryName, 'shopCode' => $shopCode);
        $productCategoryMdl = new ProductCategoryModel();
        $ret = $productCategoryMdl->editProductCategory($data);
        return $ret;
    }

    /**
     * 获得商户是否有产品
     * @param string $shopCode 商户编码
     * @return array
     */
    public function isHasProduct($shopCode) {
        $productMdl = new ProductModel();
        $ret = $productMdl->getShopProduct(array('shopCode' => $shopCode, 'productStatus' => C('PRODUCT_STATUS.ON_SHELF')));
        if(empty($ret)){
            return array('code' => C('NO'));
        }else{
            return array('code' => C('YES'));
        }
    }
    /**
     * 获得商家端系统参数
     * @return array
     */
    public function getSystemParam() {
        $systemParamMdl = new SystemParamModel();
        $paramList = $systemParamMdl->listAllShopSystemParam();
        $systemParam = array();
        foreach($paramList as $param) {
            $systemParam[$param['param']] = $param['value'];
        }
        return $systemParam;
    }

    /**
     * 商户注册
     * @param number $mobileNbr 手机号码
     * @param number $validateCode 验证码
     * @param string $password MD5后的密码
     * @param string $licenseNbr 营业执照编号
     * @return array $ret
     */
    public function register($mobileNbr, $validateCode, $password, $licenseNbr) {
        $shopMdl = new ShopModel();
        $ret = $shopMdl->register($mobileNbr, $validateCode, $password, $licenseNbr);
        return $ret;
    }

    /**
     * 商户端激活
     * @param number $mobileNbr 手机号码
     * @param number $validateCode 验证码
     * @param string $password MD5后的密码
     * @return array $ret
     */
    public function activate($mobileNbr, $validateCode, $password) {
        // 兼容密码没有加密的情况
        if(strlen($password) != 32) {
            $password = md5($password);
        }
        $shopMdl = new ShopModel();
        $ret = $shopMdl->activate($mobileNbr, $validateCode, $password);
        return $ret;
    }

    /**
     * 获得周边商家的信息,商家周边500米
     * @param string $shopCode 商家编码
     * @return array $result 一维数组或空数组
     */
    public function getAroundShopInfo($shopCode) {

        $shopMdl = new ShopModel();
        $result = $shopMdl->getAroundShopInfo($shopCode);
        return $result ? $result : array();
    }

    /**
     * 获得首页优惠券使用和E支付买单消息
     * @param string $shopCode
     * @return array $result 首页E支付买单消息和优惠券使用
     */
    public function listConsumingMsg($shopCode) {
        $userConsumeMdl = new UserConsumeModel();
        $result = $userConsumeMdl->listConsumingMsg($shopCode);
        return $result ? $result : array();
    }

    /**
     * 获得会员卡列表
     * @param string $shopCode 商家编码
     * @return array $cardList 为空时返回空数组
     */
    public function getGeneralCardStastics($shopCode) {
        $cardMdl = new CardModel();
        $cardList = $cardMdl->getGeneralCardStastics($shopCode, $this->getPager(0));
        return $cardList ? $cardList : array();
    }

    /**
     * 获得商店会员卡的统计信息
     * @param string $shopCode 商家的编码
     * @return array $result
     */
    public function countCard($shopCode) {
        $cardList = $this->getGeneralCardStastics($shopCode, $page = 0);
        if(!$cardList) return array();
        $userCardMdl = new UserCardModel();
        $result = $userCardMdl->countCard($cardList);
        return $result;
    }

    /**
     * 商店获得本店的所有的会员列表
     * @param string $cardCode 会员卡编码
     * @param string $userName 会员姓名
     * @param number $orderType 排序类型：1-办卡时间；2-消费时间；3-消费金额；4-消费次数。默认按会员消费金额、消费次数 依次排序
     * @param number $page 页码
     * @return array $cardVipList
     */
    public function listCardVip($cardCode, $userName, $orderType, $page) {
        $pager = $this->getPager($page);
        $userCardMdl = new UserCardModel();
        $cardVipList = $userCardMdl->listCardVip($cardCode, $userName, $orderType, $pager);
        $cardVipCount = $userCardMdl->countCardVip($cardCode, $userName);
        $totalData = $userCardMdl->countCardTotalData($cardCode);
        return array(
            'totalCount' => $cardVipCount,
            'cardVipList' => $cardVipList,
            'page' => $page,
            'count' => count($cardVipList),
            'totalCouponUseAmount' => $totalData['totalCouponUseAmount'],
            'totalDeductionPrice' => $totalData['totalDeductionPrice'],
        );
    }

    /**
     * 获得会员详细信息
     * @param string $userCardCode 用户会员卡编码
     * @return array
     */
    public function getVipInfo($userCardCode) {
        $userCardMdl  = new UserCardModel();
        $vipInfo = $userCardMdl->getVipInfo($userCardCode);
        return $vipInfo ? $vipInfo : array();
    }

    /**
     * 获取会员卡增长走势信息
     * @param string $shopCode 商家编码
     * @return array $increasingTrendList 一维数组
     */
    public function listIncreasingTrend($shopCode) {
        $userCardMdl = new UserCardModel();
        $increasingTrendList = $userCardMdl->listIncreasingTrend($shopCode);
        return $increasingTrendList ? $increasingTrendList : array();
    }

    /**
     * 添加或者编辑会员卡
     * @param string $cardName 卡的名字，一个类不同等级的卡可能有不同的名字。数组数据，以竖线“|”分割元素
     * @param number $cardType 类型:1000-会员卡；2000-储值卡
     * @param string $cardLvl 卡的等级，一个类型的卡可能分为不同级别。数组数据，以竖线“|”分割元素
     * @param string $creatorCode 创建者编码
     * @param string $shopCode 创建该卡的商家的编码
     * @param string $url 卡样即背景图片
     * @param string $discountRequire 需要多少积分才可以享受折扣。数组数据，以竖线“|”分割元素
     * @param string $discount 可享受折扣。数组数据，以竖线“|”分割元素
     * @param number $pointLifetime 积分有效期。单位：天
     * @param number $pointsPerCash 每消费1元积多少积分
     * @param int $outPointsPerCash 每多少积分值1元
     * @param string $remark 备注
     * @return array $ret
     */
    public function editCard($cardName, $cardType, $cardLvl, $creatorCode, $shopCode, $url, $discountRequire, $discount, $pointLifetime, $pointsPerCash,$outPointsPerCash, $remark) {
        $cardInfo = array(
            'creatorCode' => $creatorCode,
            'shopCode' => $shopCode,
            'cardName' => $cardName,
            'cardType' => $cardType,
            'cardLvl' => $cardLvl,
            'url' => $url,
            'discountRequire' => $discountRequire,
            'discount' => $discount * $this->discountRatio,
            'pointLifetime' => $pointLifetime,
            'pointsPerCash' => $pointsPerCash,
            'outPointsPerCash' => $outPointsPerCash,
            'remark' => $remark
        );
        $cardMdl = new CardModel();
        $ret = $cardMdl->editCard($cardInfo);
        return $ret;
    }

    /**
     * 获取消费金额走势信息
     * @param string $shopCode 商店编码
     * @return array $consumeTrendList
     */
    public function listConsumeTrend($shopCode) {
        $userConsumeMdl = new UserConsumeModel();
        $consumeTrendList = $userConsumeMdl->listConsumeTrend($shopCode);
        if($consumeTrendList == null)
            return array();
        return $consumeTrendList ? $consumeTrendList : array();
    }

    /**
     * 商家端首页
     * @param string $shopCode 商家编码
     * @return array 多维数组
     */
    public function getCouponHomePage($shopCode) {
        $batchCouponMdl = new BatchCouponModel();
        $couponStatistics = $batchCouponMdl->getAllCoupon($shopCode);
        $diffTypeCouponStatistics = $batchCouponMdl->getCouponInfoByType($shopCode);
        $recentCouponInfo = $batchCouponMdl->getRecentCouponInfo($shopCode);
        $userCouponMdl = new UserCouponModel();
        $CouponConsumptionTrendInfo = $userCouponMdl->listCouponConsumeTrend($shopCode);
        $CouponConsumptionPersonTrend = $userCouponMdl->listCouponPersonTrend($shopCode);
        return array(
            'couponStatistics' => $couponStatistics,
            'diffTypeCouponStatistics' => $diffTypeCouponStatistics,
            'CouponConsumptionTrendInfo' => $CouponConsumptionTrendInfo,
            'CouponConsumptionPersonTrend' => $CouponConsumptionPersonTrend,
            'recentCouponInfo' => $recentCouponInfo
        );
    }

    /**
     * 获得商家优惠券的统计信息
     * @param string $shopCode 商家编码
     * @return array $result
     */
    public function getAllCoupon($shopCode) {
        $batchCouponMdl = new BatchCouponModel();
        $result = $batchCouponMdl->getAllCoupon($shopCode);
        return $result;
    }

    /**
     * 获得不同类型优惠券的统计信息
     * @param string $shopCode 商家编码
     * @return array $ret (共几批，剩余多少张)
     */
    public function getCouponInfoByType($shopCode) {
        $batchCouponMdl = new BatchCouponModel();
        $ret = $batchCouponMdl->getCouponInfoByType($shopCode);
        return $ret;
    }

    /**
     * 获得优惠券列表
     * @param string $shopCode 商家编码
     * @param int $couponType 优惠券类型 1-N元购，2-满就送，3-抵扣券，4-折扣券，5-实物券，6-体验券, 7-兑换券, 8-代金券
     * @param number $time 时间。1-最近一个月；2-最近一个季度；3-最近一年
     * @param number $page 页码
     * @return array $couponList
     */
    public function getShopCouponList($shopCode, $couponType, $time, $page) {
        if(! isset($couponType) || empty($couponType)) {
            return array('code' => '');
        }
        $batchCouponMdl = new BatchCouponModel();
        $couponList = $batchCouponMdl->shopGetCouponList($shopCode, $couponType, $time, $this->getPager($page));
        $couponCount = $batchCouponMdl->countShopGetCouponList($shopCode, $couponType, $time);
        return array(
            'totalCount' => $couponCount,
            'couponList' => $couponList,
            'page' => $page,
            'count' => count($couponList),
        );
    }

    /**
     * 获得商家发布的优惠券列表
     * @param string $shopCode 商家编码
     * @param number $time 时间。1-最近一个月；2-最近一个季度；3-最近一年
     * @param number $page 页码
     * @return array $couponList
     */
    public function listShopCoupon($shopCode, $time, $page) {
        $batchCouponMdl = new BatchCouponModel();
        $couponList = $batchCouponMdl->listShopCoupon($shopCode, $time, $this->getPager($page));
        $couponCount = $batchCouponMdl->countShopCoupon($shopCode, $time);
        return array(
            'totalCount' => $couponCount,
            'couponList' => $couponList,
            'page' => $page,
            'count' => count($couponList),
        );
    }

    /**
     * 优惠券领取人员
     * @param string $batchCouponCode 优惠券编码
     * @param int $page 页码，从1开始
     * @return array $couponList
     */
    public function listGrabCoupon($batchCouponCode, $page) {
        $userCouponMdl = new UserCouponModel();
        $couponList = $userCouponMdl->listGrabCoupon($batchCouponCode, $this->getPager($page));
        $couponCount = $userCouponMdl->countGrabCoupon($batchCouponCode);
        return array(
            'totalCount' => $couponCount,
            'couponList' => $couponList,
            'page' => $page,
            'count' => count($couponList),
        );
    }

    /**
     * 获得优惠券详情
     * @param string $batchCouponCode 优惠券编码
     * @return array
     */
    public function getCouponInfo($batchCouponCode) {
        $batchCouponMdl = new BatchCouponModel();
        $couponInfo = $batchCouponMdl->sGetCouponInfo($batchCouponCode);
        return $couponInfo;
    }

    /**
     * 根据优惠券的验证码获得优惠券信息
     * @param string $couponCode 优惠券验证码
     * @return array $couponInfo 优惠券信息
     */
    public function getCouponInfoByCode($couponCode) {

        $orderCouponMdl = new OrderCouponModel();
        $userCouponInfo = $orderCouponMdl->getOrderCouponInfo(
            array('couponCode' => $couponCode, 'OrderCoupon.status' => array('neq', \Consts::ORDER_COUPON_STATUS_UNPAY_NOUSE)),
            array('OrderCoupon.orderCouponCode', 'couponCode', 'OrderCoupon.status', 'function', 'payPrice', 'insteadPrice', 'BatchCoupon.startUsingTime', 'BatchCoupon.expireTime', 'dayStartUsingTime', 'dayEndUsingTime', 'UserCoupon.userCode', 'userCouponCode', 'BatchCoupon.remark', 'UserCoupon.applyTime', 'BatchCoupon.validityPeriod'),
            array(
                array('joinTable' => 'UserCoupon', 'joinCondition' => 'UserCoupon.orderCouponCode = OrderCoupon.orderCouponCode', 'joinType' => 'inner'),
                array('joinTable' => 'BatchCoupon', 'joinCondition' => 'BatchCoupon.batchCouponCode = UserCoupon.batchCouponCode', 'joinType' => 'inner')
            )
        );
        if($userCouponInfo) {
            // 判断优惠券是否过期
            $userCouponMdl = new UserCouponModel();
            $isExpire = $userCouponMdl->isUserCouponExpire($userCouponInfo['applyTime'], $userCouponInfo['validityPeriod'], $userCouponInfo['expireTime']);
            $userCouponInfo['isExpire'] = $isExpire == true ? \Consts::NO : \Consts::YES;
        }
        return $userCouponInfo ? $userCouponInfo : array();
    }

    /**
     * 验证使用兑换券和代金券
     * @param string $userCode 用户编码
     * @param string $shopCode 商户编码
     * @param string $userCouponCode 用户优惠券编码
     * @return array
     */
    public function useCoupon($userCode, $shopCode, $userCouponCode) {

        // 判断用户使用的优惠券是否属于该商家
        $userCouponMdl = new UserCouponModel();
        $userCouponInfo = $userCouponMdl->getUserCouponInfoB(array('userCouponCode' => $userCouponCode), array('UserCoupon.batchCouponCode'));
        $batchCouponMdl = new BatchCouponModel();
        $batchCouponInfo = $batchCouponMdl->getCouponBasicInfo(array('batchCouponCode' => $userCouponInfo['batchCouponCode']), array('shopCode'));

        if($batchCouponInfo['shopCode'] != $shopCode) {
            return array('code' => C('USER_COUPON.CAN_NOT_USE'));
        }

        $ucMdl = new UserConsumeModel();
        $appType = C('LOGIN_TYPE.SHOP');

         //--------------------------------------------------------------------------------- 乔本亮（添加扫码之前微信核销）
         //1.先判断用户是否添加到微信卡包(不存在 则直接在系统核销)
         $userCoupon = $userCouponMdl->where(array("userCouponCode"=>$userCouponCode))->field("userCouponCode,userCouponNbr,inWeixinCard")->find();

         if(count($userCoupon)<0){
             return array('code' => C('USER_COUPON.CAN_NOT_USE'));
         }

        if($userCoupon['inWeixinCard']!=1){
            $result = $ucMdl->zeroPay2($userCode, $shopCode, $userCouponCode, $appType);
            return $result;
        }else{
            //2.如果存在则先在微信核销
            $res =       $this->consumeCouponCode($userCouponCode);
            if($res['code']==50000){
                $result = $ucMdl->zeroPay2($userCode, $shopCode, $userCouponCode, $appType);
                return $result;
            }else{
                return array("code"=>20000,"state"=>false);
            }
        }
    }

    //乔本亮（微信核销）
    private  function  consumeCouponCode($userCouponCode){

        //获取常量类定义的appid和secreat
        $appid  = C("AppID");
        $secret = C("AppSecret");

        //判断用户优惠券是否可用
        $userCouponInfo = D("UserCoupon")->where(array("userCouponCode"=>$userCouponCode,"status"=>1))
            ->field("userCouponCode,userCouponNbr,cardId,relationOpenid")->find();

        if(!$userCouponInfo){
            return array("code"=>80227,"state"=>false);    // 优惠券不可用
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
            return array("code"=>80240,"state"=>false); // 优惠券状态存在异常
        }else{
            $url ="https://api.weixin.qq.com/card/code/consume?access_token=".$access_token;
            $data ='{
                "code":"'.$userCouponInfo['userCouponNbr'].'",
                 "card_id": "'.$userCouponInfo['cardId'].'"
            }';
            $res  = $this->http_post_data($url,$data);
            $info = json_decode($res);

            if($info->errcode!==0){
                return array("code"=>80241,"state"=>false);    // 优惠券核销失败
            }
            return array("code"=>50000,"state"=>true);//核销成功（触发微信的核销事件）
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
    //--------------------------------------------------------------------------------- 乔本亮（添加扫码之后微信核销）

    /**
     * 启用/停用优惠券
     * @param string $batchCouponCode 优惠券编码
     * @param int $isAvailable 优惠券状态 1-启用；0-停用
     * @return array $ret
     */
    public function changeCouponStatus($batchCouponCode, $isAvailable) {
        $batchCouponMdl = new BatchCouponModel();
        $ret = $batchCouponMdl->changeCouponStatus($batchCouponCode, $isAvailable);
        return array('code' => $ret);
    }

    /**
     * 获得商家发行的某一批次的优惠券的使用信息列表
     * @param string $batchCouponCode 优惠券编码
     * @param number $page 页码
     * @return array $ret 为空时，返回空数组
     */
    public function getCouponConsumeList($batchCouponCode, $page) {
        $userCoupon = new UserCouponModel();
        $couponConsumeList = $userCoupon->getCouponConsumeList($batchCouponCode, $this->getPager($page));
        $couponConsumeCount = $userCoupon->countCouponConsume($batchCouponCode);
        return  array(
            'totalCount' => $couponConsumeCount,
            'couponConsumeList' => $couponConsumeList,
            'page' => $page,
            'count' => count($couponConsumeList),
        );
    }
    /**
     * 获取每批次优惠券消费走势信息
     * @param string $shopCode 商家编码
     * @return array $couponConsumeTrendList
     */
    public function listCouponConsumeTrend($shopCode) {
        $userCouponMdl = new UserCouponModel();
        $couponConsumeTrendList = $userCouponMdl->listCouponConsumeTrend($shopCode);
        return $couponConsumeTrendList ? $couponConsumeTrendList : array(array('batchNbr' => 0, 'amount' => 0));
    }

    /**
     * 获得每批次消费人次信息
     * @param string $shopCode 商家编码
     * @return array $couponPersonTrendList
     */
    public function listCouponPersonTrend($shopCode) {
        $userCouponMdl = new UserCouponModel();
        $couponPersonTrendList = $userCouponMdl->listCouponPersonTrend($shopCode);
        return $couponPersonTrendList ? $couponPersonTrendList : array(array('batchNbr' => 0, 'amount' => 0));
    }

    /**
     * 添加优惠券
     * @param string $shopCode 商店编码
     * @param int $couponType 优惠券类型。1、N元购，3、抵扣券，4、折扣券，5、实物券，6、体验券, 7-兑换券, 8-代金券
     * @param int $totalVolume 总发行量
     * @param string $startUsingTime 优惠券开始使用时间
     * @param string $expireTime 优惠券失效时间
     * @param string $dayStartUsingTime 每日开始使用时间
     * @param string $dayEndUsingTime 每日结束使用时间
     * @param string $startTakingTime 优惠券开始领用时间
     * @param string $endTakingTime 最后可领用日期
     * @param int $isSend 是否满就送。1-是，0-否
     * @param float $sendRequired 每满多少金额送一张优惠券，单位：元
     * @param string $remark 优惠券说明
     * @param string $creatorCode 创建者编码。登录的商家员工编码
     * @param float $discountPercent 打折数额。0-10之间。除折扣券外，其他类型优惠券传0
     * @param float $insteadPrice 抵用金额。单位：元。
     * @param int $availablePrice 达到多少金额可用。单位：元。
     * @param string $function 每张可以干什么。折扣券，抵扣券，N元购传空字符串
     * @param int $limitedNbr 每单限用多少张。单位：张。 默认 1张
     * @param int $nbrPerPerson 每人可领多少张。单位：张。 默认 1张
     * @param int $limitedSendNbr 每单最多送多少张。单位：张。 默认 1张
     * @param int $payPrice 得到一张优惠券需要付多少钱。单位：元。 默认 0，表示不需要购买，可直接领取
     * @return array $ret
     */
    public function addBatchCoupon($shopCode, $couponType, $totalVolume, $startUsingTime, $expireTime, $dayStartUsingTime, $dayEndUsingTime, $startTakingTime, $endTakingTime, $isSend, $sendRequired, $remark, $creatorCode, $discountPercent, $insteadPrice, $availablePrice, $function, $limitedNbr = 1, $nbrPerPerson = 1, $limitedSendNbr = 1, $payPrice = 0) {
        $shopMdl = new ShopModel();
        // 获得商家信息
        $shopInfo = $shopMdl->getShopInfo($shopCode, array('type', 'shopName'));
        switch($couponType) {
            case \Consts::COUPON_TYPE_N_PURCHASE:
                $couponTypeName = C('COUPON_TYPE_NAME.N_PURCHASE');
                break;
            case \Consts::COUPON_TYPE_REDUCTION:
                $couponTypeName = C('COUPON_TYPE_NAME.REDUCTION');
                break;
            case \Consts::COUPON_TYPE_DISCOUNT:
                $couponTypeName = C('COUPON_TYPE_NAME.DISCOUNT');
                break;
            case \Consts::COUPON_TYPE_PHYSICAL:
                $couponTypeName = C('COUPON_TYPE_NAME.PHYSICAL');
                break;
            case \Consts::COUPON_TYPE_EXPERIENCE:
                $couponTypeName = C('COUPON_TYPE_NAME.EXPERIENCE');
                break;
            case \Consts::COUPON_TYPE_EXCHANGE:
                $couponTypeName = C('COUPON_TYPE_NAME.EXCHANGE');
                break;
            case \Consts::COUPON_TYPE_VOUCHER:
                $couponTypeName = C('COUPON_TYPE_NAME.VOUCHER');
                break;
            default:
                $couponTypeName = '优惠券';
                break;
        }
        $couponName = $shopInfo['shopName'] . '-' . $couponTypeName;
        if(is_null($limitedNbr)){$limitedNbr = 1;}
        if(is_null($nbrPerPerson)){$nbrPerPerson = 1;}
        if(is_null($limitedSendNbr)){$limitedSendNbr = 1;}
        if(is_null($payPrice)){$payPrice = 0;}
        if($couponType == C('COUPON_TYPE.EXCHANGE')){
            if(empty($payPrice)){
                $payPrice = $insteadPrice;
            }
        }
        $isAvailable = \Consts::YES;
        if($isSend == \Consts::YES){
            $payPrice = 0;
            $isAvailable = \Consts::NO;
        }
        $batchCouponInfo = array(
            'shopCode' => $shopCode,
            'couponType' => $couponType,
            'totalVolume' => $totalVolume,
            'startUsingTime' => $startUsingTime,
            'expireTime' => $expireTime == '0000-00-00' ? $expireTime : date('Y-m-d H:i:s', strtotime($expireTime) + $this->dayLessOneSecond),
            'dayStartUsingTime' => $dayStartUsingTime,
            'dayEndUsingTime' => $dayEndUsingTime,
            'startTakingTime' => $startUsingTime,
            'endTakingTime' => $expireTime == '0000-00-00' ? $expireTime : date('Y-m-d H:i:s', strtotime($expireTime) + $this->dayLessOneSecond),
            'isSend' => $isSend,
            'sendRequired' => $sendRequired,
            'limitedSendNbr' => $limitedSendNbr,
            'remark' => $remark,
            'creatorCode' => $creatorCode,
            'discountPercent' => $discountPercent * $this->discountRatio,
            'insteadPrice' => $insteadPrice,
            'availablePrice' => $availablePrice,
            'function' => $function,
            'couponName' => $couponName,
            'couponBelonging' => C('COUPON_BELONG.SHOP'),
            'url' => str_replace('{{couponType}}', $couponType, C('COUPON_DEFAULT_IMG')),
            'industryCategory' => $shopInfo['type'],
            'limitedNbr' => $limitedNbr,
            'nbrPerPerson' => $nbrPerPerson,
            'payPrice' => $payPrice,
            'isAvailable' => $isAvailable,
        );
        $batchCouponMdl = new BatchCouponModel();
        $ret = $batchCouponMdl->editBatchCoupon($batchCouponInfo);

        return $ret;
    }

    /**
     * 优惠券对账
     * @param string $shopCode 商家编码
     * @param string $startDate 开始时间
     * @param string $endDate 结束时间
     * @return array
     */
    public function getCouponBill($shopCode, $startDate, $endDate) {
        $batchCouponMdl = new BatchCouponModel();
        $couponBillList = $batchCouponMdl->getCouponBill($shopCode, $startDate, $endDate);
        return $couponBillList;
    }

    /**
     * 获得银行卡刷卡对账的统计列表，按日统计
     * @param string $shopCode 商家编码
     * @param int $page 页码
     * @param int $time 时间范围。1-最近一个周；2-最近一个月；3-最近一年
     * @return array $bankCardCountBillList
     */
    public function listBankCardCountBill($shopCode, $page, $time) {
        //TODO
        $bankAccountLocalLogMdl = new BankAccountLocalLogModel();
        $bankCardCountBillList = $bankAccountLocalLogMdl->listBankCardCountBill($shopCode, $time, $this->getPager($page));
        $bankCardCountBillCount = $bankAccountLocalLogMdl->countBankCardCountBill($shopCode, $time);
        return array(
            'totalCount' => $bankCardCountBillCount,
            'bankCardCountBillList' => $bankCardCountBillList,
            'page' => $page,
            'count' => count($bankCardCountBillList),
        );
    }

    /**
     * 获得某日银行卡刷卡对账的统计信息
     * @param string $shopCode 商家编码
     * @param string $actionTime 日期
     * @return array $bankCardCountBillInfo
     */
    public function getBankCardCountBill($shopCode, $actionTime) {
        $bankAccountLocalLogMdl = new BankAccountLocalLogModel();
        $bankCardCountBillInfo = $bankAccountLocalLogMdl->getBankCardCountBill($shopCode, $actionTime);
        return $bankCardCountBillInfo;
    }

    /**
     * 获得某张银行卡刷卡对账列表
     * @param string $shopCode 商家编码
     * @param int $page 页码
     * @param string $datetime 日期
     * @return array $bankCardBillList
     */
    public function listBankCardBill($shopCode, $page, $datetime) {
        //TODO
        $bankAccountLocalLogMdl = new BankAccountLocalLogModel();
        $bankCardBillList = $bankAccountLocalLogMdl->listBankCardBill($shopCode, $datetime, $this->getPager($page));
        $bankCardBillCount = $bankAccountLocalLogMdl->countBankCardBill($shopCode, $datetime);
        return array(
            'totalCount' => $bankCardBillCount,
            'bankCardBillList' => $bankCardBillList,
            'page' => $page,
            'count' => count($bankCardBillList),
        );
    }

    /**
     * 商家店员列表
     * @param string $shopCode 商家编码
     * @param int $page 页码
     * @return array $staffList
     */
    public function listStaff($shopCode, $page) {
        $shopStaffMdl = new ShopStaffModel();
        $staffList = $shopStaffMdl->listShopStaff(array('ShopStaff.shopCode'=>$shopCode), $this->getPager($page));
        $staffCount = $shopStaffMdl->countShopStaff(array('ShopStaff.shopCode'=>$shopCode));
        return array(
            'totalCount' => $staffCount,
            'staffList' => $staffList,
            'page' => $page,
            'count' => count($staffList),
        );
    }

    /**
     * 判断员工是否有添加，修改，删除员工的权限
     * @param string $staffCode 员工编码
     * @return array $ret {'code'}
     */
    public function isStaffHasPerms($staffCode) {
        $shopStaffMdl = new ShopStaffModel();
        $ret = $shopStaffMdl->isStaffHasPerms($staffCode);
        return $ret;
    }

    /**
     * 添加店员
     * @param number $mobileNbr 手机号码
     * @param string $realName 员工姓名
     * @param int $userLvl 账号类型。1-员工类；2-店长老板类；
     * @param string $shopCode 商家编码
     * @return array $ret
     */
    public function addStaff($mobileNbr, $realName, $userLvl, $shopCode) {
        $ret = $this->editStaffB('', $mobileNbr, $realName, $userLvl, $shopCode, '');
        return $ret;
    }

    /**
     * 修改店员信息
     * @param number $mobileNbr 手机号码
     * @param string $realName 姓名
     * @param int $type 账号类型。1-员工类；2-店长老板类；
     * @param string $staffCode 编码
     * @return number $ret
     */
    public function updateStaff($mobileNbr, $realName, $type, $staffCode) {
        $ret = $this->editStaffB($staffCode, $mobileNbr, $realName, $type, '', '');
        return $ret;
    }

    /**
     * 删除员工
     * @param string $staffCode 员工编码
     * @return array $ret
     */
    public function delStaff($staffCode) {
        $shopStaffMdl = new ShopStaffModel();
        $ret = $shopStaffMdl->delStaff($staffCode);
        return $ret;
    }

    /**
     * 获得商家的推荐码
     * @param string $shopCode 商家编码
     * @param int $month 月份
     * @return array {'inviteCode', 'regNbr', 'regACNbr'}
     */
    public function getShopInviteCode($shopCode, $month) {
        $shopMdl = new ShopModel();
        $ret = $shopMdl->isShopSetInviteCode($shopCode);
        // 获得商家信息（商家编码，商家邀请码）
        $shopInfo = $shopMdl->getShopInfo($shopCode, array('Shop.shopCode', 'inviteCode'));
        $userMdl = new UserModel();
        $regNbr = $userMdl->countRegNbrUsedShopInviteCode($shopInfo['inviteCode'], $month);
        $regACNbr = $userMdl->countRegACNbrUsedShopInviteCode($shopInfo['inviteCode'], $month);
        return array(
            'inviteCode' => $shopInfo['inviteCode'],
            'regNbr' => $regNbr,
            'regACNbr' => $regACNbr,
        );
    }

    /**
     * 获得商铺的基本信息
     * @param string $shopCode 商家编码
     * @return array $shopBasicInfo
     */
    public function sGetShopBasicInfo($shopCode) {
        $shopMdl = new ShopModel();
        // 获得商家信息
        $shopBasicInfo = $shopMdl->getShopInfo($shopCode, array('Shop.shopCode', 'Shop.shopName', 'Shop.longitude', 'Shop.latitude', 'Shop.country', 'Shop.province', 'Shop.city', 'Shop.street', 'Shop.tel', 'Shop.mobileNbr', 'Shop.creditPoint', 'Shop.type', 'Shop.isOuttake', 'Shop.isOrderOn', 'Shop.shopOpeningTime', 'Shop.shopClosedTime', 'Shop.businessHours', 'Shop.businessHours', 'Shop.logoUrl', 'Shop.shortDes', 'Shop.shopStatus', 'popularity', 'repeatCustomers', 'isCatering', 'isOuttake', 'isOpenTakeout', 'eatPayType', 'takeoutRequirePrice', 'deliveryFee', 'deliveryDistance', 'deliveryStartTime', 'deliveryEndTime', 'isOrderOn', 'Shop.isAcceptBankCard', 'hasWifi', 'isFreeParking', 'isAllowPet', 'isOpenEat', 'tableNbrSwitch'));
        $shopBasicInfo['takeoutRequirePrice'] = $shopBasicInfo['takeoutRequirePrice'] / \Consts::HUNDRED;
        $shopBasicInfo['deliveryFee'] = $shopBasicInfo['deliveryFee'] / \Consts::HUNDRED;
        $shopBasicInfo['deliveryDistance'] = $shopBasicInfo['deliveryDistance'] / \Consts::THOUSAND;
        return $shopBasicInfo;
    }

    /**
     * 获得商铺信息
     * @param string $shopCode 商家编码
     * @return array $shopInfo
     */
    public function sGetShopInfo($shopCode) {
        // 获得商家基本信息
        $shopMdl = new ShopModel();
        // 获得商家信息
        $shopInfo = $shopMdl->getShopInfo($shopCode, array('Shop.shopCode', 'Shop.shopName', 'Shop.longitude', 'Shop.latitude', 'Shop.country', 'Shop.province', 'Shop.city', 'Shop.street', 'Shop.tel', 'Shop.mobileNbr',  'Shop.type', 'Shop.isOuttake', 'Shop.isOrderOn', 'shopOpeningTime', 'shopClosedTime', 'businessHours', 'Shop.logoUrl', 'Shop.shortDes', 'popularity', 'repeatCustomers', 'isOpenTakeout', 'isOpenEat', 'isCatering', 'tableNbrSwitch'));
        // 获得商家装修
        $shopDecorationMdl = new ShopDecorationModel();
        $shopDecoration = $shopDecorationMdl->getShopDecoration($shopCode);
        // 获得商店产品相册10张
        $subAlbumMdl = new SubAlbumModel();
        $subAlbumCodeList = $subAlbumMdl->getSubAlbumCodeList($shopCode);
        $shopPhotoList = array();
        if($subAlbumCodeList) {
            $shopPhotoMdl = new ShopPhotoModel();
            $shopPhotoList = $shopPhotoMdl->getShopInfoPhoto(array('subAlbumCode' => array('IN', $subAlbumCodeList)));
        }
        // 获得商家可领取的优惠券
        $batchCouponMdl = new BatchCouponModel();
        $shopCoupon = $batchCouponMdl->listAvailabelCoupon($shopCode,
            array(
                'batchCouponCode',
                'couponName',
                'insteadPrice',
                'discountPercent',
                'payPrice',
                'industryCategory',
                'availablePrice',
                'endTakingTime',
                'createTime',
                'remaining',
                'couponType',
                'function',
                'batchNbr',
                'totalVolume',
                'couponType',
                'function',
                'expireTime'
            ));
        // 获得商家会员卡
        $cardMdl = new CardModel();
        $shopCard = $cardMdl->getGeneralCardStastics($shopCode, $this->getPager(0), 1);
        // 获得商家的所有有效活动
        $actMdl = new ActivityModel();
        $actList = $actMdl->getActList(array('shopCode' => $shopCode, 'status' => C('ACTIVITY_STATUS.ACTIVE'), 'endTime' => array('EGT', date('Y-m-d H:i:s'))), array('activityCode', 'activityName'), array(), 'createTime desc');
        return array(
            'shopInfo' => $shopInfo,
            'shopCoupon' => $shopCoupon,
            'shopCard' => $shopCard,
            'shopDecoration' => $shopDecoration,
            'shopProductPhoto' => $shopPhotoList,
            'actList' => $actList,
        );
    }

    /**
     * 添加子相册
     * @param string $shopCode 商家编码
     * @param string $name 商家名字
     * @return array $ret
     */
    public function addSubAlbum($shopCode, $name) {
        $subAlbumMdl = new SubAlbumModel();
        $data = array(
            'shopCode' => $shopCode,
            'name' => $name,
            'belonging' => '1',
            'createTime' => date('Y-m-d h:i:s', time())
        );
        $ret = $subAlbumMdl->editSubAlbum($data);

        // 添加产品分组
        $productCategoryMdl = new ProductCategoryModel();
        $categoryInfo = $productCategoryMdl->getProductCategoryInfo(array('categoryName' => $name, 'shopCode' => $shopCode), array('categoryId'));
        if(!$categoryInfo) {
            $categoryInfo = $productCategoryMdl->editProductCategory(array('categoryName' => $name, 'shopCode' => $shopCode));
        }

        return $ret;
    }

    /**
     * 添加子相册
     * @param string $code 子相册编码
     * @param string $name 商家名字
     * @return array $ret
     */
    public function updateSubAlbum($code, $name) {
        $subAlbumMdl = new SubAlbumModel();
        $data = array(
            'code' => $code,
            'name' => $name,
        );
        $ret = $subAlbumMdl->editSubAlbum($data);
        return $ret;
    }

    /**
     * 删除子相册
     * @param int $code 子相册编码
     * @return array
     */
    public function delSubAlbum($code) {
        $subAlbumMdl = new SubAlbumModel();
        $ret = $subAlbumMdl->delSubAlbum($code);
        return $ret;
    }

    /**
     * 获得产品相册所有子相册
     * @param string $shopCode 商家编码
     * @return array $albumList
     */
    public function listSubAlbum($shopCode) {
        $subAlbumMdl = new SubAlbumModel();
        $subAlbumList = $subAlbumMdl->listSubAlbum($shopCode);
        return $subAlbumList;
    }

    /**
     * 获得某子相册的信息
     * @param string $code 子相册编码
     * @return array $albumList
     */
    public function getSubAlbumInfo($code) {
        $subAlbumMdl = new SubAlbumModel();
        $subAlbumInfo = $subAlbumMdl->getSubAlbumInfo($code);
        return $subAlbumInfo;
    }

    /**
     * 获得产品相册
     * @param string $shopCode 商家编码
     * @return array $albumList
     */
    public function getShopProductAlbum($shopCode) {
        $subAlbumMdl = new SubAlbumModel();
        $subAlbumList = $subAlbumMdl->listSubAlbum($shopCode);
        $shopPhotoMdl = new ShopPhotoModel();
        foreach($subAlbumList as &$v) {
            $photoList = $shopPhotoMdl->listPhoto($v['code']);
//            $v['photoList'] = $photoList;
            $v['photoCount'] = count($photoList);
        }
        return $subAlbumList;
    }

    /**
     * 添加子相册图片
     * @param string $subAlbumCode 子相册编码
     * @param string $url 图片url
     * @param string $title 图片标题
     * @param float $price 图片标价，单位元
     * @param string $des 图片描述
     * @return array $ret
     */
    public function addSubAlbumPhoto($subAlbumCode, $url, $title, $price, $des) {
        $shopPhotoMdl = new ShopPhotoModel();
        $data = array(
            'subAlbumCode' => $subAlbumCode,
            'url' => $url,
            'title' => $title,
            'price' => $price * C('RATIO'),
            'des' => $des,
            'createTime' => date('Y-m-d h:i:s')
        );
        $ret = $shopPhotoMdl->editSubAlbumPhoto($data);

        // 添加产品图片
        $subAlbumMdl = new SubAlbumModel();
        $subAlbumInfo = $subAlbumMdl->getSubAlbumInfo($subAlbumCode);
        $productCategoryMdl = new ProductCategoryModel();
        $categoryInfo = $productCategoryMdl->getProductCategoryInfo(array('categoryName' => $subAlbumInfo['name'], 'shopCode' => $subAlbumInfo['shopCode']), array('categoryId'));
        if(!$categoryInfo) {
            $categoryInfo = $productCategoryMdl->editProductCategory(array('categoryName' => $subAlbumInfo['name'], 'shopCode' => $subAlbumInfo['shopCode']));
        }
        $productMdl = new ProductModel();
        $productMdl->editProduct(array(
            'shopCode' => $subAlbumInfo['shopCode'],
            'createTime' => date('Y-m-d h:i:s'),
            'productImg' => $url,
            'productName' => $title,
            'originalPrice' => $price * C('RATIO'),
            'des' => $des,
            'categoryId' => $categoryInfo['categoryId']
        ));

        return $ret;
    }

    /**
     * 修改子相册图片
     * @param string $subAlbumCode 子相册编码
     * @param string $url 图片url
     * @param string $title 图片标题
     * @param float $price 图片标价，单位元
     * @param string $des 图片描述
     * @param int $code 图片编码
     * @return array $ret
     */
    public function updateSubAlbumPhoto($subAlbumCode, $url, $title, $price, $des, $code) {
        $shopPhotoMdl = new ShopPhotoModel();
        $data = array(
            'subAlbumCode' => $subAlbumCode,
            'url' => $url,
            'title' => $title,
            'price' => $price * C('RATIO'),
            'des' => $des,
            'code' => $code
        );
        $ret = $shopPhotoMdl->editSubAlbumPhoto($data);
        return $ret;
    }

    /**
     * 获得子相册图片
     * @param string $code 子相册编码
     * @return array $photoList
     */
    public function getSubAlbumPhoto($code) {
        $shopPhotoMdl = new ShopPhotoModel();
        $photoList = $shopPhotoMdl->getSubAlbumPhoto($code);
        return $photoList;
    }

    /**
     * 删除商家图片
     * @param string $code 图片编码
     * @return array
     */
    public function delSubAlbumPhoto($code) {
        $shopPhotoMdl = new ShopPhotoModel();
        $ret = $shopPhotoMdl->delPhoto($code);
        return $ret;
    }

    /**
     * 修改商铺信息(第一期由管理端设置)
     * @param string $shopCode 商家编码
     * @param string $updateKey 更新字段 多个字段以竖线分隔
     * @param string $updateValue 更新信息 多个字段以竖线分隔 字段和值位置一一对应
     * @return string $ret
     */
    public function updateShop($shopCode, $updateKey, $updateValue) {
        $shopMdl = new ShopModel();
        $keyArr = explode('|', $updateKey);
        $valueArr = explode('|', $updateValue);
        $updateInfo = array();
        foreach($keyArr as $k => $v){
            if($v == 'businessHours'){
                if(!empty($valueArr[$k])){
                    $timeData = explode(';', $valueArr[$k]);
                    foreach($timeData as $td){
                        if($td){
                            $time = explode(',', $td);
                            $updateInfo[$v][] = array(
                                'open' => $time[0],
                                'close' => $time[1]
                            );
                        }
                    }
                }
                $updateInfo[$v] = json_encode($updateInfo[$v]);
            }else{
                if(!empty($v) && (!empty($valueArr[$k]) || $valueArr[$k] == 0)){
                    $updateInfo[$v] = $valueArr[$k];
                }
            }
        }
        if(isset($updateInfo) && $updateInfo){
            $ret = $shopMdl->updateShop($shopCode, $updateInfo);
        }else{
            $ret = array('code' => C('SUCCESS'));
        }
        return $ret;
    }

    /**
     * 添加/修改商店简介
     * @param string $shopCode 商家编码
     * @param string $shortDes 商店简介
     * @return array
     */
    public function updateShopShortDes($shopCode, $shortDes) {
        $shopMdl = new ShopModel();
        return $shopMdl->updateShopShortDes($shopCode, $shortDes);
    }

    /**
     * 获得商户的配送方案
     * @param string $shopCode 商家编码
     * @return array deliveryList
     */
    public function listShopDelivery($shopCode) {
        $shopDeliveryMdl =new ShopDeliveryModel();
        $deliveryList = $shopDeliveryMdl->listShopDelivery(array('shopCode' => $shopCode));
        return $deliveryList;
    }

    /**
     * 添加，修改配送方案
     * @param int $deliveryId 配送方案ID
     * @param string $shopCode 商家编码
     * @param int $deliveryDistance 配送范围，单位：米
     * @param int $requireMoney 起送价，单位：元
     * @param int $deliveryFee 配送费，单位：元
     * @return array
     */
    public function editShopDelivery($deliveryId, $shopCode, $deliveryDistance, $requireMoney, $deliveryFee) {
        $data = array(
            'deliveryId' => $deliveryId, // 配送方案ID
            'shopCode' => $shopCode, // 商家编码
            'deliveryDistance' => $deliveryDistance, // 配送范围，单位：米
            'requireMoney' => $requireMoney * \Consts::HUNDRED, // 起送价，单位：元
            'deliveryFee' => $deliveryFee * \Consts::HUNDRED, // 配送费，单位：元
        );
        $shopDeliveryMdl =new ShopDeliveryModel();
        $ret = $shopDeliveryMdl->editShopDelivery($data);
        return $ret;
    }

    /**
     * 批量添加，修改配送方案
     * @param array $deliveryList 配送方案列表
     * @return array
     */
    public function editShopDeliveryBatch($deliveryList) {
        $ret = array();
        foreach($deliveryList as $delivery) {
            $ret = $this->editShopDelivery($delivery['deliveryId'], $delivery['shopCode'], $delivery['deliveryDistance'], $delivery['requireMoney'], $delivery['deliveryFee']);
        }
        return $ret;
    }

    /**
     * 批量添加，修改配送方案，给android用的
     * @param int $deliveryId 配送方案ID。多个值使用“|”分隔
     * @param string $shopCode 商家编码。多个值使用“|”分隔
     * @param int $deliveryDistance 配送范围，单位：米。多个值使用“|”分隔
     * @param int $requireMoney 起送价，单位：元。多个值使用“|”分隔
     * @param int $deliveryFee 配送费，单位：元。多个值使用“|”分隔
     * @return array
     */
    public function editShopDeliveryBatchAd($deliveryId, $shopCode, $deliveryDistance, $requireMoney, $deliveryFee) {
        $deliveryIdArr = explode('|', substr(trim($deliveryId), 0, strlen(trim($deliveryId)) - 1));
        $shopCodeArr = explode('|', substr(trim($shopCode), 0, strlen(trim($shopCode)) - 1));
        $deliveryDistanceArr = explode('|', substr(trim($deliveryDistance), 0, strlen(trim($deliveryDistance)) - 1));
        $requireMoneyArr = explode('|', substr(trim($requireMoney), 0, strlen(trim($requireMoney)) - 1));
        $deliveryFeeArr = explode('|', substr(trim($deliveryFee), 0, strlen(trim($deliveryFee)) - 1));
        $ret = '';
        foreach($deliveryIdArr as $k => $v) {
            $ret = $this->editShopDelivery($v, $shopCodeArr[$k], $deliveryDistanceArr[$k], $requireMoneyArr[$k], $deliveryFeeArr[$k]);
        }
        return $ret;
    }

    /**
     * 删除商家配送方案
     * @param int $deliveryId 商户配送方案ID
     * @return array
     */
    public function delShopDelivery($deliveryId) {
        $shopDeliveryMdl = new ShopDeliveryModel();
        $delRet = $shopDeliveryMdl->delShopDelivery(array('deliveryId' => $deliveryId));
        return $delRet;
    }

    /**
     * 获得活动列表
     * @param string $shopCode 商家编码
     * @param int $page 页码
     * @return array $activityList
     */
    public function sGetActList($shopCode, $page) {
        $activityMdl = new ActivityModel();
        $filterData = array('shopCode' => $shopCode, 'pos' => array('NEQ', C('ACT_POS.SCROLL')), 'status' => array('GT', C('ACTIVITY_STATUS.DELETE')));
        $activityList = $activityMdl->getActList($filterData, array('activityCode', 'activityLogo', 'activityImg', 'activityName', 'txtContent', 'richTextContent', 'startTime', 'endTime', 'limitedParticipators', 'feeScale', 'totalPayment', 'status'), array(), 'createTime desc', \Consts::PAGESIZE, $page);
        $activityCount = $activityMdl->countActList($filterData);
        return array(
            'totalCount' => $activityCount,
            'activityList' => $activityList,
            'page' => $page,
            'nextPage' => UtilsModel::getNextPage($activityCount, $page),
            'count' => count($activityList),
        );
    }

    /**
     * 获得商铺活动详情
     * @param string $activityCode 活动编码
     * @return array $activityInfo
     */
    public function sGetActInfo($activityCode) {
        $activityMdl = new ActivityModel();
        $activityInfo = $activityMdl->getActInfo(array('activityCode' => $activityCode), array('activityCode', 'activityLogo', 'activityImg', 'activityName', 'txtContent', 'richTextContent', 'startTime', 'endTime', 'limitedParticipators', 'feeScale', 'totalPayment', 'status', 'pageviews'));
        $userActivityMdl = new UserActivityModel();
        $activityInfo['collectNbr'] = $userActivityMdl->countUserActList(array('activityCode' => $activityCode, 'isCollected' => \Consts::YES));
        $activityInfo['shareArr'] = array(
            'title' => $activityInfo['activityName'],
            'icon' => '/Public/img/avatar.jpg',
            'content' => $activityInfo['txtContent'],
            'link' => 'http://'.$_SERVER['HTTP_HOST'].'/Api/Browser/getActInfo?isShared=1&activityCode='.$activityInfo['activityCode']
        );
        return $activityInfo;
    }

    /**
     * 获得活动详情
     * @param $activityCode
     * @return array
     */
    public function getActivityInfo($activityCode){
        $activityMdl = new ActivityModel();
        return $activityMdl->getActInfo(array('activityCode' => $activityCode));
    }

    /**
     * 获得活动报名名单
     * @param string $activityCode 活动编码
     * @param int $page 页码
     * @return array
     */
    public function listActParticipant($activityCode, $page) {
        $userActivityMdl = new UserActivityModel();
        $userActCodeMdl = new UserActCodeModel();
        $userActivityCodeArr = $userActCodeMdl->getActCodeField(
            array(
                'UserActCode.status' => array('IN', array(\Consts::ACT_CODE_STATUS_ACTIVE,\Consts::ACT_CODE_STATUS_USED)),
                'UserActivity.activityCode' => $activityCode,
                'totalNbr' => array('gt', \Consts::NO)
            ),
            'userActCode',
            array(
                array(
                    'joinTable' => 'UserActivity',
                    'joinCondition' => 'UserActivity.userActivityCode = UserActCode.userActCode',
                    'joinType' => 'inner')
            )
        );
        $userActivityCodeArr = array_unique($userActivityCodeArr);
        if(empty($userActivityCodeArr)){$userActivityCodeArr = array('0');}
        $joinTableArr = array(
            array(
                'joinTable' => 'Activity',
                'joinCondition' => 'UserActivity.activityCode = Activity.activityCode',
                'joinType' => 'inner'
            ),
            array(
                'joinTable' => 'User',
                'joinCondition' => 'UserActivity.userCode = User.userCode',
                'joinType' => 'inner'
            )
        );
        $participantList = $userActivityMdl->getUserActList(array('UserActivity.userActivityCode' => array('IN', $userActivityCodeArr)), array('User.mobileNbr', 'User.avatarUrl', 'User.nickName', 'User.realName', 'Activity.activityCode', 'Activity.feeScale', 'userActivityCode'), $joinTableArr, 'UserActivity.createTime desc', \Consts::PAGESIZE, $page);
        $participantCount = $userActivityMdl->countUserActList(array('UserActivity.userActivityCode' => array('IN', $userActivityCodeArr)), $joinTableArr);
        foreach($participantList as $k => $v){
            $participantList[$k]['totalNbr'] = 0;
            $condition = array(
                'userActCode' => $v['userActivityCode'],
                'status' => array('IN', array(\Consts::ACT_CODE_STATUS_ACTIVE,\Consts::ACT_CODE_STATUS_USED)),
            );
            $feeScale = $v['feeScale'];
            unset($participantList[$k]['feeScale']);
            foreach($feeScale as $fv){
                $condition['scaleId'] = $fv['id'];
                $count = $userActCodeMdl->countActCodeList($condition);
                if($count > 0){
                    $participantList[$k]['totalNbr'] += $count;
                    $fv['count'] = $count;
                    $participantList[$k]['feeScale'][] = $fv;
                }
            }
        }
        return array(
            'totalCount' => $participantCount,
            'participantList' => $participantList,
            'page' => $page,
            'count' => count($participantList),
        );
    }

    /**
     * 删除活动
     * @param $activityCode
     * @return bool
     */
    public function delActivity($activityCode){
        $activityMdl = new ActivityModel();
        return $activityMdl->delActivity($activityCode);
    }

    /**
     * 改变活动状态
     * @param string $activityCode
     * @param string $status array(0=>'禁用/未发布', 1=>'启用/发布', 2=>'停止报名', 3=>'取消')
     * @return string
     */
    public function changeActivityStatus($activityCode, $status){
        $activityMdl = new ActivityModel();
        $code = $activityMdl->changeActivityStatus($activityCode, $status);
        return array('code' => $code);
    }

    /**
     * 获得活动类型
     */
    public function getActType(){
        $actTypeList = array();
        foreach(C('ACTIVITY_TYPE') as $k => $v){
            $actTypeList[] = array(
                'value' => $v,
                'name' => C('ACTIVITY_TYPE_NAME.'.$k)
            );
        }
        return $actTypeList;
    }

    /**
     * 获得活动退款的种类
     * @return array 二维数组
     */
    public function getActRefundChoice(){
        $refundChoice = array();
        foreach(C('ACTIVITY_REFUND_REQUIRED_VALUE') as $k => $v) {
            $refundChoice[] = array(
                'refundValue' => $v,
                'refundName' => C('ACTIVITY_REFUND_REQUIRED_NAME.' . $k)
            );
        }
        return $refundChoice;
    }

    /**
     * 添加商铺活动
     * @param string $activityName 活动主题。活动名称
     * @param string $startTime 活动开始时间
     * @param string $endTime 活动结束时间
     * @param string $activityLocation 活动地点
     * @param string $txtContent 活动说明
     * @param string $limitedParticipators 活动参与人数上限
     * @param string $isPrepayRequired 是否需要预付费。1-需要；0-不需要
     * @param string $prePayment 预付金额
     * @param string $isRegisterRequired 是否需要报名。1-需要；0-不需要
     * @param string $activityImg 活动图片
     * @param string $activityLogo 活动方形图片
     * @param string $shopCode 发起活动的商家编码
     * @param string $creatorCode 活动发起人编码
     * @param int $activityBelonging 活动归属 1-平台活动；2-工行活动；3-商家活动
     * @param int $actType 活动类型 1-聚会，2-运动，3-户外，4-亲子，5-体验课，6-音乐
     * @param int $contactName 联系人
     * @param int $contactMobileNbr 联系方式
     * @param array $feeScale 收费规格
     * @param int $refundRequired 退款要求
     * @param int $registerNbrRequired 单人报名人数限制
     * @param int $dayOfBooking 活动类型 提前预约天数
     * @return array $ret
     */
    public function addActivity($activityName, $startTime, $endTime, $activityLocation, $txtContent, $limitedParticipators, $isPrepayRequired, $prePayment, $isRegisterRequired, $activityImg, $activityLogo, $shopCode, $creatorCode, $activityBelonging, $actType, $contactName, $contactMobileNbr, $feeScale, $refundRequired, $registerNbrRequired, $dayOfBooking) {
        if(empty($endTime)){
            $endTime = date('Y-m-d H:i:s', strtotime($startTime) + 4 * 3600);
        }
        $uStartTime = strtotime($startTime);
        $uEndTime = strtotime($endTime);
        $duration = ($uEndTime - $uStartTime) / 60;
        $isPrepayRequired = \Consts::NO;
        $prePayment = \Consts::NO;
        $totalPayment = \Consts::NO;
        $isRegisterRequired = \Consts::YES;
        $feeScale = json_decode($feeScale, true);
        $feeScaleArr = array();
        $id = 1;
        if(count($feeScale) > 0){
            foreach($feeScale as $v){
                $price = $v['price'] * \Consts::HUNDRED;
                if(!empty($v['des']) || !empty($price)){
                    $feeScaleArr[] = array(
                        'id' => $id,
                        'des' => $v['des'],
                        'price' => $price
                    );
                    if($totalPayment < $price){
                        $totalPayment = $price;
                    }
                    $id++;
                }
            }
        }
        if($totalPayment <= 0){
            $isRegisterRequired = \Consts::NO;
            $limitedParticipators = \Consts::NO;
            $dayOfBooking = \Consts::NO;
            $registerNbrRequired = 5;
            $refundRequired = \Consts::NO;
        }
        if($registerNbrRequired > 5){
            $registerNbrRequired = 5;
        }
        $activityInfo = array(
            'duration' => $duration,
            'activityName' => $activityName,
            'startTime' => $startTime,
            'endTime' => $endTime,
            'activityLocation' => $activityLocation,
            'richTextContent' => $txtContent,
            'txtContent' => '',
            'limitedParticipators' => $limitedParticipators,
            'isPrepayRequired' => $isPrepayRequired,
            'prePayment' => $prePayment * C('RATIO'),
            'totalPayment' => $totalPayment,
            'isRegisterRequired' => $isRegisterRequired,
            'activityImg' => $activityImg,
            'activityLogo' => $activityLogo,
            'shopCode' => $shopCode,
            'creatorCode' => $creatorCode,
            'activityBelonging' => $activityBelonging,
            'actType' => $actType,
            'contactName' => $contactName,
            'contactMobileNbr' => $contactMobileNbr,
            'feeScale' => count($feeScaleArr) > 0 ? json_encode($feeScaleArr) : '',
            'refundRequired' => $refundRequired,
            'registerNbrRequired' => $registerNbrRequired,
            'dayOfBooking' => $dayOfBooking,
            'status' => C('ACTIVITY_STATUS.DISABLE'),
        );

        $activityMdl = new ActivityModel();
        $ret = $activityMdl->editActivity($activityInfo);
        return $ret;
    }

    /**
     * 新增 / 编辑活动
     * @param string $activityCode 活动编码
     * @param $updateData
     * @return array
     */
    public function editActivity($activityCode, $updateData){
        $updateData = json_decode($updateData, true);
        $uStartTime = strtotime($updateData['startTime']);
        $uEndTime = strtotime($updateData['endTime']);
        $updateData['duration'] = ($uEndTime - $uStartTime) / 60;
        $updateData['isPrepayRequired'] = \Consts::NO;
        $updateData['prePayment'] = \Consts::NO;
        $updateData['totalPayment'] = \Consts::NO;
        $updateData['isRegisterRequired'] = \Consts::YES;
//        if(isset($updateData['richTextContent']) && $updateData['richTextContent']){$updateData['txtContent'] = substr($updateData['richTextContent'], 0,20);}
        $feeScale = json_decode($updateData['feeScale'], true);
        $feeScaleArr = array();
        if(count($feeScale) > 0){
            foreach($feeScale as $v){
                $feeScaleId[] = $v['id'];
                $feeScaleDes[] = $v['des'];
                $feeScalePrice[] = $v['price'];
            }
            $totalPayment = UtilsModel::getMaxNbr($feeScalePrice);
            $updateData['totalPayment'] = $totalPayment * \Consts::HUNDRED;
            $feeScaleArr = array();
            $maxNbr = UtilsModel::getMaxNbr($feeScaleId);
            foreach($feeScaleId as $k => $id){
                $price = $feeScalePrice[$k] * \Consts::HUNDRED;
                if(!empty($feeScaleDes[$k]) || $price >= 0){
                    if(empty($id)){
                        $maxNbr += 1;
                        $id = $maxNbr;
                    }
                    $feeScaleArr[] = array(
                        'id' => $id,
                        'des' => $feeScaleDes[$k],
                        'price' => $price
                    );
                }
            }
        }
        $updateData['feeScale'] = count($feeScaleArr) > 0 ? json_encode($feeScaleArr) : '';
        if($updateData['totalPayment'] <= 0){
            $updateData['isRegisterRequired'] = \Consts::NO;
            $updateData['limitedParticipators'] = \Consts::NO;
            $updateData['dayOfBooking'] = \Consts::NO;
            $updateData['registerNbrRequired'] = 5;
            $updateData['refundRequired'] = \Consts::NO;
        }
        if($updateData['registerNbrRequired'] > 5){
            $updateData['registerNbrRequired'] = 5;
        }
        if(empty($activityCode)){
            $updateData['status'] = C('ACTIVITY_STATUS.DISABLE');
        }else{
            $updateData['activityCode'] = $activityCode;
        }
        $activityMdl = new ActivityModel();
        $ret = $activityMdl->editActivity($updateData);
        return $ret;
    }

    /**
     * 根据用户活动验证码获得相关信息
     * @param string $actCode 用户活动验证码
     * @return array $userActInfo 一维关联数组
     */
    public function getInfoByActCode($actCode) {
        // 获得用户活动码的信息
        $baseMdl = new BaseModel();
        $userActCodeInfo = $baseMdl->getTableInfo('UserActCode', array('actCode' => $actCode, 'status' => array('neq', \Consts::USER_ACT_CODE_STATUS_UNPAY_NOUSE)), array('userActCode', 'status', 'orderCode', 'userActCodeId', 'actCode'));
        // 获得用户活动信息
        $userActMdl = new UserActivityModel();
        $userActInfo = $userActMdl->getUserActInfo(
            array('userActivityCode' => $userActCodeInfo['userActCode']),
            array('Activity.activityName', 'Activity.startTime', 'Activity.endTime', 'Activity.activityLocation'),
            array(array('joinTable' => 'Activity', 'joinCondition' => 'Activity.activityCode = UserActivity.activityCode', 'joinType' => 'inner'))
        );
        if($userActInfo) {
            // 获得参与活动的联系人姓名
            $userActInfo['name'] = $baseMdl->getFieldInfo('ConsumeOrder', array('orderCode' => $userActCodeInfo['orderCode']), 'receiver');
            $userActInfo['status'] = $userActCodeInfo['status']; // 用户活动码状态
            $userActInfo['userActCode'] = $userActCodeInfo['userActCode']; // 用户活动编码
            $userActInfo['actCode'] = $userActCodeInfo['actCode']; // 用户活动验证码
            $userActInfo['userActCodeId'] = $userActCodeInfo['userActCodeId']; // 用户活动码Id
        }
        return $userActInfo ? $userActInfo : array();
    }

    /**
     * 验证用户活动码
     * @param string $actCode 用户活动码
     * @param int $userActCodeId 用户活动吗ID
     * @param string $shopCode 商家编码
     * @return array {'code' => '结果码'}
     */
    public function valUserActCode($actCode, $userActCodeId, $shopCode) {
        // 获得用户活动码相关信息
        $userActCodeMdl = new UserActCodeModel();
        $userActCodeInfo = $userActCodeMdl->getActCodeInfo(array('actCode' => $actCode, 'userActCodeId' => $userActCodeId), array('price', 'orderCode'));
        if($userActCodeInfo) {
            // 判断活动码是否属于这家店
            $orderMdl = new ConsumeOrderModel();
            $orderInfo = $orderMdl->getOrderInfo(array('orderCode' => $userActCodeInfo['orderCode']), array('shopCode'));
            if($orderInfo['shopCode'] != $shopCode) {
                return array('code' => '20000');
            }
            // 获得活动订单中已经计算的金额，单位：分
            $settledAmount = $userActCodeMdl->getSettledAmount($userActCodeInfo['orderCode']);

            // 获得订单支付记录的信息
            $userConsumeMdl = new UserConsumeModel();
            $userConsumeInfo = $userConsumeMdl->getCurrConsumeInfoByOrderCode($userActCodeInfo['orderCode']);

            // 计算该活动码需要计算的各部分金额明细
            $toSettleDetail = $userActCodeMdl->calToSettleDetail($settledAmount, $userActCodeInfo['price'], $userConsumeInfo['shopBonus'], $userConsumeInfo['platBonus']);
            // 保存结算信息，修改用户活动码为已验证
            $toSettleDetail['status'] = \Consts::USER_ACT_CODE_STATUS_USED;
            $toSettleDetail['valTime'] = date('Y-m-d H:i:s');
            $ret = $userActCodeMdl->updateUserActCode(array('userActCodeId' => $userActCodeId), $toSettleDetail);
            return $ret;
        } else {
            return array('code' => '20000');
        }
    }

    /**
     * 获取某家商家的装饰信息
     * @param string $shopCode 商家编码
     * @return array $shopDecoList
     */
    public function getShopDecoration($shopCode){
        $shopDecorationMdl = new ShopDecorationModel();
        $shopDecoList = $shopDecorationMdl->getShopDecoration($shopCode);
        return $shopDecoList ? $shopDecoList : array();
    }

    /**
     * 添加商家装饰图片信息
     * @param string $shopCode 商店编码
     * @param string $imgUrl 图片url,多个元素以竖线“|”分割
     * @param string $title 照片标题，多个元素以竖线“|”分割
     * @return array
     */
    public function addShopDecImg($shopCode, $imgUrl, $title) {
        $shopDecMdl = new ShopDecorationModel();
        $ret = $shopDecMdl->addShopDecImg($shopCode, $imgUrl, $title);
        return $ret;
    }

    /**
     * 将商家装饰图片其中一张设置为主图
     * @param string $shopCode 商店编码
     * @param string $decorationCode 装饰编码
     * @return array
     */
    public function setMainShopDecImg($shopCode, $decorationCode) {
        $shopDecMdl = new ShopDecorationModel();
        $ret = $shopDecMdl->setMainShopDecImg($shopCode, $decorationCode);
        return $ret;
    }

    /**
     * 修改商家装饰信息
     * @param string $decorationCode 装饰编码
     * @param string $imgUrl 图片URL
     *  @param string $title 照片标题
     * @return array
     */
    public function updateShopDecoration($decorationCode, $imgUrl, $title) {
        $shopDecMdl = new ShopDecorationModel();
//        $decoration = $shopDecMdl->getOneDecoration($decorationCode);
//        if($decoration['type'] == 1){
        $decInfo = array('imgUrl' => $imgUrl, 'title' => $title);
//        }else{
//            $decInfo = array('imgUrl' => $imgUrl, 'type' => $type);
//        }
        $ret = $shopDecMdl->updateShopDecoration($decorationCode, $decInfo);
        return $ret;
    }

    /**
     * 删除商家装饰信息
     * @param string $decorationCode 装饰编码
     * @return array
     */
    public function delShopDec($decorationCode) {
        $shopDecMdl = new ShopDecorationModel();
        $ret = $shopDecMdl->delShopDecoration($decorationCode);
        return $ret;
    }

    /**
     * 获得红包列表信息
     * @param string $shopCode 商家编码
     * @param int $page 页码，从1开始
     * @return array $bonusList
     */
    public function listBonus($shopCode, $page) {
        $bonusMdl = new BonusModel();
        $bonusList = $bonusMdl->getBonusList(array('Bonus.shopCode' => $shopCode), $this->getPager($page));
        $bonusCount = $bonusMdl->countShopBonus(array('shopCode' => $shopCode));
        return array(
            'totalCount' => $bonusCount,
            'bonusList' => $bonusList,
            'page' => $page,
            'count' => count($bonusList),
        );
    }

    /**
     * 获得某商家某一批次红包领取信息
     * @param string $bonusCode 红包编码
     * @param int $page 页码，从1开始
     * @return array $bonusList
     */
    public function listGrabBonus($bonusCode, $page) {
        $userBonusMdl = new UserBonusModel();
        $bonusList = $userBonusMdl->listGrabUserBonus($bonusCode, $this->getPager($page));
        $bonusCount = $userBonusMdl->countGrabUserBonus($bonusCode);
        return array(
            'totalCount' => $bonusCount,
            'bonusList' => $bonusList,
            'page' => $page,
            'count' => count($bonusList),
        );
    }

    /**
     * 获得红包列表信息
     * @param string $bonusCode 红包编码
     * @return array $bonusList
     */
    public function getBonusInfo($bonusCode) {
        $bonusMdl = new BonusModel();
        $bonusInfo = $bonusMdl->sGetBonusInfo($bonusCode);
        return $bonusInfo;
    }

    /**
     * 添加红包
     * @param int $bonusBelonging 红包归属。1-商家红包；2-平台红包
     * @param string $shopCode 商家编码
     * @param string $creatorCode 创建者编码
     * @param int $upperPrice 金额区间（上限）
     * @param int $lowerPrice 金额区间（下限）
     * @param int $totalValue 发行红包总额
     * @param int $nbrPerDay 每天限制发红包数量
     * @param int $totalVolume 红包发行总数量
     * @param int $validityPeriod 红包有效期
     * @param int $startTime 抢红包开始时间
     * @param int $endTime 抢红包结束时间
     * @return array $ret
     */
    public function addBonus($bonusBelonging, $shopCode, $creatorCode, $upperPrice, $lowerPrice, $totalValue, $nbrPerDay, $totalVolume, $validityPeriod, $startTime, $endTime) {
        $bonusMdl = new BonusModel();
        $bonusInfo = array(
            'bonusBelonging' => $bonusBelonging,
            'shopCode' => $shopCode,
            'creatorCode' => $creatorCode,
            'upperPrice' => $upperPrice,
            'lowerPrice' => $lowerPrice,
            'totalValue' => $totalValue,
            'nbrPerDay' => $nbrPerDay,
            'totalVolume' => $totalVolume,
            'validityPeriod' => $validityPeriod,
            'bonusName' => '红包',
            'startTime' => $startTime,
            'endTime' => date('Y-m-d H:i:s',strtotime($endTime) + 24 * 60 *60 -1),
        );
        $ret = $bonusMdl->editBonus($bonusInfo);
        return $ret;
    }

    /**
     * 启用/停用红包
     * @param string $bonusCode 红包编码
     * @param int $status 红包状态 1-启用；0-停用
     * @return array $ret
     */
    public function changeBonusStatus($bonusCode, $status) {
        $bonusMdl = new BonusModel();
        $ret = $bonusMdl->changeBnousStatus($bonusCode, $status);
        return array('code' => $ret);
    }

    /**
     * 获得最近一周的各类消息包括优惠券消息，会员卡消息，商家广播
     * @param string $shopCode 商家编码
     * @param int $page 页码。从1开始
     * @return array $msgList
     */
    public function listMsg($shopCode, $page) {
        $userMsgMdl = new UserMessageModel();
        $msgList = $userMsgMdl->sListMsg($shopCode, $this->getPager($page));
        $msgCount = $userMsgMdl->sCountMsg($shopCode);
        return array(
            'couponMsg' => array(
                'totalCount' => $msgCount['couponMsgCount'],
                'couponMsgList' => $msgList['couponMsgList'],
                'page' => $page,
                'count' => count($msgList['couponMsgList']),
            ),
            'cardMsg' => array(
                'totalCount' => $msgCount['cardMsgCount'],
                'cardMsgList' => $msgList['cardMsgList'],
                'page' => $page,
                'count' => count($msgList['cardMsgList']),
            ),
            'shopMsg' => array(
                'totalCount' => $msgCount['shopMsgCount'],
                'shopMsgList' => $msgList['shopMsgList'],
                'page' => $page,
                'count' => count($msgList['shopMsgList']),
            ),
        );
    }

    /**
     * 发送消息
     * @param string $shopCode
     * @param string $userCode
     * @param string $staffCode
     * @param string $message
     * @return string
     */
    public function sendMsg($shopCode, $userCode, $staffCode, $message){
        $communicationMdl = new CommunicationModel();
        $ret = $communicationMdl->sendMsg($shopCode, $userCode, $staffCode, $message, 0);
        return $ret;
    }

    /**
     * 阅读消息
     * @param $userCode
     * @param $shopCode
     * @return string
     */
    public function readMsg($userCode, $shopCode){
        $communicationMdl = new CommunicationModel();
        $ret = $communicationMdl->readMsg($userCode, $shopCode, C('COMMUNICATION_APP.USER'));
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
        $ret = $communicationMdl->countUnreadMsg($userCode, $shopCode, C('COMMUNICATION_APP.USER'));
        return $ret;
    }

    /**
     * 消息记录
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param int $page 页码
     * @param string $staffCode 商家员工编码
     * @return array
     */
    public function getMsg($userCode, $shopCode, $page, $staffCode){
        $communicationMdl = new CommunicationModel();

        $msgList = $communicationMdl->getMsg($userCode, $shopCode, $this->getPager($page), $staffCode);
        $totalCount = $communicationMdl->getMsgCount($userCode, $shopCode, $staffCode);
        return array(
            'totalCount'=>$totalCount,
            'msgList'=>$msgList,
            'page'=>$page,
            'count' => count($msgList)
        );
    }

    /**
     * 所有会员发的最新一条记录
     * @param string $shopCode 商店编码
     * @param int $page 页码
     * @return array
     */
    public function getMsgGroup($shopCode, $page){
        $communicationMdl = new CommunicationModel();
        $ret = $communicationMdl->getMsgGroup('shopCode', $shopCode, 'userCode', $this->getPager($page));
        foreach($ret as &$v) {
            $v['unreadCount'] = $this->countUnreadMsg($v['userCode'], $v['shopCode']);
        }
        $totalCount = $communicationMdl->countMsgGroup('shopCode', $shopCode, 'userCode');
        return array(
            'totalCount' => $totalCount,
            'ret' => $ret,
            'page' => $page,
            'count' => count($ret),
        );
    }

    /**
     * 商户申请POS服务
     * @param string $shopCode 商家编码
     * @param int $type 服务类型。1-申请POS机，2-耗材配送，3-故障报修，4-其他
     * @param string $remark 服务说明
     * @return array
     */
    public function applyPosServer($shopCode, $type, $remark) {
        $posServerMdl = new PosServerModel();
        $serverInfo = array(
            'shopCode' => $shopCode,
            'type' => $type,
            'remark' => $remark
        );
        $ret = $posServerMdl->applyPosServer($serverInfo);
        return $ret;
    }


    /**
     * 获取消费信息
     * @param string $consumeCode 支付编码
     * @return array
     */
    public function getConsumeInfo($consumeCode) {
        $userConsumeMdl = new UserConsumeModel();
        // 获得账单信息
        $consumeInfo = $userConsumeMdl->getConsumeInfo(array('consumeCode' => $consumeCode), array('realPay', 'deduction', 'couponUsed', 'UserConsume.shopBonus', 'bankCardDeduction', 'UserConsume.platBonus', 'usedUserCouponCode', 'isCard', 'consumerCode', 'location', 'Shop.shopName','ConsumeOrder.orderNbr','ConsumeOrder.orderAmount','ConsumeOrder.orderTime', 'Shop.shopCode', 'firstDeduction', 'couponDeduction', 'cardDeduction',
            'UserConsume.status', 'User.mobileNbr', 'avatarUrl', 'nickName', 'OrderCoupon.userCode', 'OrderCoupon.batchCouponCode','OrderCoupon.couponCode',

            'BatchCoupon.batchCouponCode','BatchCoupon.function','payType', 'noDiscountPrice', 'consumeTime','OrderCoupon.status'));

        $consumeInfo['orderNbr'] = substr($consumeInfo['orderNbr'], -10, 6) . ' ' . substr($consumeInfo['orderNbr'], -4); // 截取订单号
        $consumeInfo['bonusDeduction'] = $consumeInfo['shopBonus'] + $consumeInfo['platBonus']; // 红包抵扣金额，单位：分
        $consumeInfo['shopBonusDeduction'] = $consumeInfo['shopBonus']; // 商户红包抵扣金额，单位：分
        $consumeInfo['platBonusDeduction'] = $consumeInfo['platBonus']; // 平台红包抵扣金额，单位：分
        $consumeInfo['userMobileNbr'] = $consumeInfo['mobileNbr']; // 消费者手机号

        $userCouponMdl = new UserCouponModel();
        // 获得使用的优惠券信息
        if(!empty($consumeInfo['usedUserCouponCode'])) {
            $coupon = $userCouponMdl->getCouponInfo($consumeInfo['usedUserCouponCode'], array('couponType', 'insteadPrice', 'availablePrice', 'discountPercent', 'batchNbr', 'payPrice', 'orderCouponCode'));
        } else {
            $coupon = $userCouponMdl->getUserCouponInfoB(
                array('UserCoupon.consumeCode' => $consumeCode),
                array('userCouponCode', 'couponType', 'discountPercent', 'insteadPrice', 'availablePrice', 'function', 'batchNbr', 'payPrice', 'orderCouponCode', 'userCouponNbr')
            );
        }
        $consumeInfo['couponType'] = $coupon['couponType'] ? $coupon['couponType'] : '';
        $consumeInfo['insteadPrice'] = $coupon['insteadPrice'] ? $coupon['insteadPrice'] : 0;
        $consumeInfo['availablePrice'] = $coupon['availablePrice'] ? $coupon['availablePrice'] : 0;
        $consumeInfo['discountPercent'] = $coupon['discountPercent'] ? $coupon['discountPercent']  / C('DISCOUNT_RATIO') : 10;
//        $consumeInfo['function'] = $coupon['function'] ? $coupon['function'] : '';
        $consumeInfo['batchNbr'] = $coupon['batchNbr'] ? $coupon['batchNbr'] : '';
        $consumeInfo['payPrice'] = $coupon['payPrice'] ? $coupon['payPrice'] : '';

        // 如果是兑换券或者代金券，获得该券的消费金额明细包括（商家红包，平台红包，和实际支付金额）
        if(in_array($coupon['couponType'], array(\Consts::COUPON_TYPE_EXCHANGE, \Consts::COUPON_TYPE_VOUCHER))) {
            $orderCouponMdl = new OrderCouponModel();
            $orderCouponUsedInfo = $orderCouponMdl->getOrderCouponInfo(
                array('orderCouponCode' => $coupon['orderCouponCode']),
                array('bankcardAmount', 'platBonus', 'shopBonus', 'couponCode')
            );
//            $consumeInfo['orderAmount'] = $orderCouponUsedInfo['bankcardAmount'] + $orderCouponUsedInfo['platBonus'] + $orderCouponUsedInfo['shopBonus']; // 总消费金额
//            $consumeInfo['realPay'] = $orderCouponUsedInfo['bankcardAmount']; // 实际支付金额
            $consumeInfo['platBonus'] = $orderCouponUsedInfo['platBonus'] ? $orderCouponUsedInfo['platBonus'] : 0; // 平台红包金额
            $consumeInfo['shopBonus'] = $orderCouponUsedInfo['shopBonus'] ? $orderCouponUsedInfo['shopBonus'] : 0; // 商户红包金额
            $consumeInfo['couponCode'] = $orderCouponUsedInfo['couponCode'] ? $orderCouponUsedInfo['couponCode'] : $coupon['userCouponNbr']; // 券码
        }

        $temp = array('orderAmount', 'realPay', 'couponDeduction', 'cardDeduction', 'bonusDeduction', 'shopBonusDeduction', 'platBonusDeduction', 'bankCardDeduction', 'deduction', 'noDiscountPrice', 'payPrice', 'platBonus', 'shopBonus');
        foreach($temp as $v) {
            $consumeInfo[$v] = number_format($consumeInfo[$v] / \Consts::HUNDRED, 2, '.', '');
        }

        $temp = array('insteadPrice', 'availablePrice', 'firstDeduction');
        foreach($temp as $v) {
            $consumeInfo[$v] = $consumeInfo[$v] / \Consts::HUNDRED;
        }

        $consumeInfo['status'] = $this->OrderStatus($consumeInfo['status']);
        return $consumeInfo;
    }

    /**使用状态
     * @param $status
     * @return string
     */
    private function  OrderStatus($status){
        switch ($status){
            case 10:
                $text = '订单未付款，不可用';
                break;
            case 11:
                $text = '已退款，不可用';
                break;
            case 12:
                $text = '申请退款，不可用';
                break;
            case 20:
                $text = '可用';
                break;
            case 30:
                $text = '已使用';
                break;
            default:
                $text = '未知';
                break;
        }
        return $text;
    }

    /**
     * 获得最新的版本信息
     * @return array
     */
    public function getNewestShopAppVersion() {
        $salMdl = new ShopAppLogModel();
        return $salMdl->getNewestShopAppVersion();
    }

    /**
     * 获得PC端最新的版本信息
     * @return array
     */
    public function getNewestPcAppVersion() {
        $palMdl = new PcAppLogModel();
        $palInfo = $palMdl->getNewestPcAppVersion();
        return $palInfo ? $palInfo : array();
    }


    /**
     * 获得某一消费的实际付款金额
     * @param $consumeCode
     * @return mixed
     */
    public function getRealPay($consumeCode) {
        $ucMdl = new UserConsumeModel();
        $consumeInfo = $ucMdl->getConsumeInfo(array('consumeCode' => $consumeCode), array('UserConsume.realPay', 'UserConsume.location'=>'shopCode', 'Shop.shopName', 'Shop.shopId', 'User.mobileNbr'));
        $consumeInfo['realPay'] =  $consumeInfo['realPay'] / C('RATIO');
        return $consumeInfo;
    }

    /**
     * 获取某个时间段内某商家的优惠券是否有设置满就送优惠券
     * @param string $shopCode 商家编码
     * @param $startUsingTime
     * @param $expireTime
     * @return array
     */
    public function getSendCouponByTime($shopCode, $startUsingTime, $expireTime){
        $batchCouponMdl = new BatchCouponModel();
        $startUsingTime = date('Y-m-d H:i:s', strtotime($startUsingTime));
        $expireTime = date('Y-m-d H:i:s', strtotime($expireTime) + $this->dayLessOneSecond);
        return $batchCouponMdl->getSendCouponByTime($shopCode, $startUsingTime, $expireTime);
    }

    /**
     * 获得银行卡对账信息
     * @param string $shopCode 商家编码
     * @param int $page 页码，从1开始
     * @return array
     */
    public function getAccount($shopCode, $page) {
        if(empty($shopCode)) {
            $userConsumeList = array();
            $userConsumeCount = 0;
        } else {
            $userConsumeMdl = new UserConsumeModel();
            $condition = array(
                'ConsumeOrder.shopCode' => $shopCode, // 消费商户
                'UserConsume.status' => array('NOT IN', array(\Consts::PAY_STATUS_UNPAYED, \Consts::PAY_STATUS_PAYING,  \Consts::PAY_STATUS_CANCELED, \Consts::PAY_STATUS_FAIL, \Consts::PAY_STATUS_CAN_NOT_PAY)), // 支付状态
//                'payType' => C('UC_PAY_TYPE.BANKCARD'), // 支付类型为银行卡支付
                'orderType' => array('notin', array(\Consts::ORDER_TYPE_COUPON)), // 订单类型
            );
            // 获得账单信息
            $userConsumeList = $userConsumeMdl->listUserConsume($condition, $this->getPager($page));
            $userCouponMdl = new UserCouponModel();
            foreach($userConsumeList as $k => $order) {
                $userConsumeList[$k]['orderNbr'] = substr($order['orderNbr'], -4); // 获得订单号后4位
                $userConsumeList[$k]['isFinish'] = $condition['ConsumeOrder.status'] == \Consts::PAY_STATUS_PAYED ? \Consts::YES : \Consts::NO; // 订单是否支付完成
                // 获得订单使用的优惠券的信息
                $usedCouponInfo = $userCouponMdl->getUserCouponInfoB(
                    array('UserCoupon.consumeCode' => $order['consumeCode']),
                    array('UserCoupon.userCouponCode', 'BatchCoupon.insteadPrice', 'couponType', 'discountPercent', 'availablePrice', 'UserCoupon.applyTime', 'BatchCoupon.validityPeriod', 'BatchCoupon.expireTime')
                );
                if($usedCouponInfo['couponType'] == \Consts::COUPON_TYPE_EXCHANGE) {
                    $userConsumeList[$k]['usedCouponName'] = '兑换券';
                } elseif($usedCouponInfo['couponType'] == \Consts::COUPON_TYPE_VOUCHER) {
                    $userConsumeList[$k]['usedCouponName'] = '代金券';
                } else {
                    $userConsumeList[$k]['usedCouponName'] = '';
                }

            }
            // 获得账单总数
            $userConsumeCount = $userConsumeMdl->countUserConsume($condition);
        }

        return array(
            'orderList' => $userConsumeList,
            'totalCount' => $userConsumeCount,
            'page' => $page,
            'count' => count($userConsumeList)
        );
    }

    /**
     * 获得商家订单列表
     * @param string $shopCode 商家编码
     * @param string $isFinish  1 => 支付成功，0 => 支付失败和未支付
     * @param int $page
     * @return array
     */
    public function getShopOrderList($shopCode, $isFinish, $page){
        $userConsumeMdl = new UserConsumeModel();
        $condition['ConsumeOrder.shopCode'] = $shopCode;
        if($isFinish == 1) {
            $condition['ConsumeOrder.status'] = \Consts::PAY_STATUS_PAYED; // 已付款
        }else{
            $condition['ConsumeOrder.status'] = array(array('EQ', \Consts::PAY_STATUS_UNPAYED), array('EQ',\Consts::PAY_STATUS_FAIL), 'or');
        }
        $condition['ConsumeOrder.orderType'] = array('neq', \Consts::ORDER_TYPE_COUPON); //订单类型不是买券订单

        $userConsumeList = $userConsumeMdl->listUserConsume($condition, $this->getPager($page));
        foreach($userConsumeList as &$order) {
            $order['orderNbr'] = substr($order['orderNbr'], -4);
        }
        $userConsumeCount = $userConsumeMdl->countUserConsume($condition);
        return array(
            'orderList' => $userConsumeList,
            'totalCount' => $userConsumeCount,
            'page' => $page,
            'count' => count($userConsumeList)
        );
    }

    /**
     * 获取商家是否支持门店和外卖订单
     * @param string $shopCode 商家编码
     * @return array
     */
    public function getShopOrderType($shopCode) {
        $shopMdl = new ShopModel();
        // 获得商家信息
        $shopInfo = $shopMdl->getShopInfo($shopCode, array('Shop.isCatering', 'Shop.isOuttake'));
        if($shopInfo['isOuttake'] == C('NO')) {
            $consumeOrderMdl = new ConsumeOrderModel();
            $shopInfo['isOuttake'] = $consumeOrderMdl->hasOrder(array('shopCode' => $shopCode, 'orderType' => C('ORDER_TYPE.TAKE_OUT'), 'status' => array('neq', C('FOOD_ORDER_STATUS.UNORDERED')))) == true ? C('YES') : C('NO');
        }
        return $shopInfo;
    }

    /**
     * 获取商家门店和外卖订单数量
     * @param string $shopCode 商家编码
     * @return array {'eatIn(堂食未处理订单数量)', 'takeout(外卖未处理订单)'}
     */
    public function countOrderByType($shopCode) {
        $consumeOrderMdl = new ConsumeOrderModel();
        $condition['ConsumeOrder.shopCode'] = $shopCode; // 设置商家编码
        $condition['ConsumeOrder.isFinishOrder'] = C('YES'); // 设置是否完成下单为已完成下单
        $condition['_string'] = '(ConsumeOrder.orderStatus = ' . C('FOOD_ORDER_STATUS.ORDERED') . ' AND ConsumeOrder.status IN (' . \Consts::PAY_STATUS_UNPAYED . ',' . \Consts::PAY_STATUS_CAN_NOT_PAY . ',' . \Consts::PAY_STATUS_PAYED . ')) OR (ConsumeOrder.status = ' . \Consts::PAY_STATUS_REFUNDING . ')'; // (订单状态为已下单 AND 订单支付状态 in (未付款，不能付款，已付款)) OR (订单支付状态为退款中)
        $condition['ConsumeOrder.orderType'] = C('ORDER_TYPE.NO_TAKE_OUT'); // 设置订单类型为堂食订单
        // 得到堂食未处理订单数量
        $eatIn = $consumeOrderMdl->countOrder($condition);
        $condition['ConsumeOrder.orderType'] = C('ORDER_TYPE.TAKE_OUT'); // 设置订单类型为外卖订单
        // 得到外卖未处理订单数量
        $takeOut = $consumeOrderMdl->countOrder($condition);
        return array(
            'eatIn' => $eatIn, // 堂食未处理订单数量
            'takeOut' => $takeOut, // 外卖未处理订单
        );
    }

    /**
     * 搜索商家门店或外卖订单列表
     * @param $shopCode
     * @param $orderType
     * @param $search
     * @param $page
     * @return array
     */
//    public function getShopOrderListByType($shopCode, $orderType, $search, $page){
//        $consumeOrderMdl = new ConsumeOrderModel();
//        $condition['ConsumeOrder.shopCode'] = $shopCode;
//        $condition['ConsumeOrder.orderType'] = $orderType;
//        $condition['search'] = $search;
//        $orderList = $consumeOrderMdl->listConsumeOrder($condition, $this->getPager($page));
//        $orderCount = $consumeOrderMdl->countConsumeOrder($condition);
//        return array(
//            'orderList' => $orderList,
//            'totalCount' => $orderCount,
//            'page' => $page,
//            'count' => count($orderList)
//        );
//    }



    /**
     * 获得商家各类型信息的未读数量
     * @param $shopCode
     * @return array
     */
    public function countAllTypeMsg($shopCode){
        $userMessageMdl = new UserMessageModel();
        $communicationMdl = new CommunicationModel();
        $result = array(
            'shop'=>$userMessageMdl->countMessage($shopCode, C('MESSAGE_TYPE.SHOP'), 0),
            'card'=>$userMessageMdl->countMessage($shopCode, C('MESSAGE_TYPE.CARD'), 0),
            'coupon'=>$userMessageMdl->countMessage($shopCode, C('MESSAGE_TYPE.COUPON'), 0),
            'communication'=>$communicationMdl->countUnreadMsg('', $shopCode, C('COMMUNICATION_APP.USER')),
            'feedback'=>$communicationMdl->countUnreadMsg(C('HQ_CODE'), $shopCode, C('COMMUNICATION_APP.USER')),
        );
        return $result;
    }

    /**
     * 设置商家的某种类型的消息为已读
     * @param string $shopCode 商家编码
     * @param int $type 消息类型。0-商家消息；1-会员卡消息；2-优惠券消息；
     * @return array
     */
    public function readMessage($shopCode, $type){
        $userMsgMdl = new UserMessageModel();
        $condition = array('receiverCode' => $shopCode, 'type' => $type, 'readingStatus' => C('NO'));
        $ret = $userMsgMdl->readMsg($condition);
        return $ret;
    }

    /** PC 端支付流程 START */

    /** 1.订单确认 */
    /**
     * step1:得到订单详情
     * @param string $lastFourOfOrderNbr 订单号后4位
     * @param string $shopCode 商家编码
     * @return array
     */
    public function getOrderInfoByPC($lastFourOfOrderNbr, $shopCode){
        $userConsumeMdl = new UserConsumeModel();
        if(strlen($lastFourOfOrderNbr) == 20) {
            $condition = array(
                'orderNbr' => $lastFourOfOrderNbr,
                'shopCode' => $shopCode,
            );
        } else {
            $condition = array(
                'lastFourOfOrderNbr' => $lastFourOfOrderNbr,
                'shopCode' => $shopCode,
                'dayofmonth(orderTime)' => date('j',time()), // 日，前面不补0
                'month(orderTime)' => date('n', time()),
                'year(orderTime)' => date('Y',time())
            );
        }

        $order = $userConsumeMdl->listUserConsume($condition, $this->getPager(0));
        if($order){
            $order[0]['code'] = C('SUCCESS');
            return $order[0];
        }else{
            return array('code'=>C('ORDER.NOT_EXIST'));
        }
    }

    /**
     * step2:订单确认
     * @param string $consumeCode 支付记录编码
     * @param string $mobileNbr 用户手机号码
     * @return array
     */
    public function orderConfirm($consumeCode, $mobileNbr) {
        $userConsumeMdl = new UserConsumeModel();
        $consumeOrderMdl = new ConsumeOrderModel();
        // 获得订单的信息
        $consumeInfo = $userConsumeMdl->getConsumeInfo(array('consumeCode' => $consumeCode), array('ConsumeOrder.orderCode', 'ConsumeOrder.clientCode'));
        // 验证用户手机号码是否正确
        $userMdl = new UserModel();
        $userInfo = $userMdl->getUserInfo(array('userCode' => $consumeInfo['clientCode']), array('mobileNbr'));
        if($userInfo['mobileNbr'] != $mobileNbr) {
            return array('code' => C('MOBILE_NBR.ERROR'));
        }
        M()->startTrans();
        // 修改支付记录的支付状态为已支付
        $ret1 = $userConsumeMdl->changeUserConsumeStatus($consumeCode, C('UC_STATUS.PAYED'));
        // 修改订单的支付状态为已支付，订单确认状态为已确认
        $ret2 = $consumeOrderMdl->updateConsumeOrder(
            array('orderCode' => $consumeInfo['orderCode']),
            array('status' => C('ORDER_STATUS.PAYED'), 'orderConfirm' => C('YES'))
        );
        if($ret1 && $ret2) {
            M()->commit();
            return array('code' => C('SUCCESS'));
        } else {
            M()->rollback();
            return array('code' => C('API_INTERNAL_EXCEPTION'));
        }
    }

    /** 2.买单 */
    /**
     * step1:根据领取的优惠券号查询需要的信息
     */
    public function pcPay($userCouponNbr, $shopCode) {
        $userCouponMdl = new UserCouponModel();
        $condition = array(
            'UserCoupon.userCouponNbr' => $userCouponNbr,
            'Shop.shopCode' => $shopCode
        );
        // 获得优惠券信息
        $userCouponInfo = $userCouponMdl->getUserCouponByUserCouponNbr($condition, array('UserCoupon.userCouponCode', 'UserCoupon.status', 'User.userCode', 'User.nickName', 'BatchCoupon.batchCouponCode', 'BatchCoupon.couponType','BatchCoupon.insteadPrice', 'BatchCoupon.discountPercent', 'BatchCoupon.function', 'BatchCoupon.insteadPrice', 'Shop.shopCode', 'availablePrice'));
        if(empty($userCouponInfo)) {
            return array('code' => C('COUPON.NOT_EXIST'));
        }

        //优惠券
        $couponInsteadPrice = C('NO');
        $couponDiscountPercent = C('NO');
        if($userCouponInfo['couponType'] == C('COUPON_TYPE.DISCOUNT')) {
            $couponDiscountPercent = $userCouponInfo['discountPercent'];
        } else {
            $couponInsteadPrice = number_format(($userCouponInfo['insteadPrice'] / C('RATIO')), 2, '.', '');
        }

        $isCard = C('NO');
        $canUseCard = C('NO');
        $cardDiscount = C('NO');
        $userCardMdl = new UserCardModel();
        // 获得用户在商家拥有的最高等级的会员卡
        $userCard = $userCardMdl->getBestUserCard($userCouponInfo['userCode'], $userCouponInfo['shopCode']);
        if($userCard) {
            $userCardCode = $userCard['userCardCode'];
            $field = array(
                'UserCard.point',
                'Card.discount',
                'Card.discountRequire',
                'Card.cardCode',
            );
            $userCardInfo = $userCardMdl->getUserCardInfo($userCardCode, $field);
            $isCard = C('YES');
            $cardDiscount = $userCardInfo['discount'];
            if($userCardInfo['point'] >= $userCardInfo['discountRequire']){
                $canUseCard = C('YES');
            }
        }

        $platBonus = 0; // 平台红包为0
        $shopBonus = 0; // 商户红包为0
        $bsMdl = new BonusStatisticsModel();
        $platBonusRet = $bsMdl->getUserBonusStatistics($userCouponInfo['userCode'], C('HQ_CODE'));
        if($platBonusRet){
            $platBonus = number_format(($platBonusRet['totalValue'] / C('RATIO')), 2, '.', '');
        }
        $shopBonusRet = $bsMdl->getUserBonusStatistics($userCouponInfo['userCode'], $userCouponInfo['shopCode']);
        if($shopBonusRet){
            $shopBonus = number_format(($shopBonusRet['totalValue'] / C('RATIO')), 2, '.', '');
        }

        $ret = array(
            'code'=> C('SUCCESS'),
            'userCode' => $userCouponInfo['userCode'],
            'userCouponCode' => $userCouponInfo['userCouponCode'],
            'status' => $userCouponInfo['status'],
            'nickName' => $userCouponInfo['nickName'],
            'couponType' => $userCouponInfo['couponType'],
            'couponInsteadPrice' => $couponInsteadPrice,
            'couponDiscountPercent' => $couponDiscountPercent,
            'function' => $userCouponInfo['function'],
            'isCard' => $isCard,
            'canUseCard' => $canUseCard,
            'cardDiscount' => $cardDiscount,
            'platBonus' => $platBonus,
            'shopBonus' => $shopBonus,
            'availablePrice' => number_format(($userCouponInfo['availablePrice'] / C('RATIO')), 2, '.', ''),
        );
        return $ret;
    }

    /** PC 端支付流程 END */


    /**
     * 查询员工接收短信的设置
     * @param $mobileNbr
     * @return array
     */
    public function getShopStaffSetting($mobileNbr){
        $shopStaffMdl = new ShopStaffModel();
        return $shopStaffMdl->getShopStaffInfo(array('mobileNbr' => $mobileNbr), array('staffCode', 'isSendPayedMsg'));
    }

    /**
     * 修改员工接收短信的设置
     * @param $staffCode
     * @param $setting
     * @return array
     */
    public function updateShopStaffSetting($staffCode, $setting){
        $shopStaffMdl = new ShopStaffModel();
        return $shopStaffMdl->updateStaff(array('staffCode'=>$staffCode, 'isSendPayedMsg'=>$setting));
    }

    /**
     * 获得商户总浏览量
     * @param  $shopCode
     * @return number
     */
    public function getShopAllBrowseQuantity($shopCode) {
        $userEnterShopInfoRecordMdl = new UserEnterShopInfoRecordModel();
        return $userEnterShopInfoRecordMdl->getBrowseQuantity(array('shopCode' => $shopCode, 'actionType' => 0), 'userCode');
    }

    /**
     * 获得商户今天总浏览量
     * @param  $shopCode
     * @return number
     */
    public function getShopTodayBrowseQuantity($shopCode) {
        $userEnterShopInfoRecordMdl = new UserEnterShopInfoRecordModel();
        return $userEnterShopInfoRecordMdl->getBrowseQuantity(array('shopCode' => $shopCode, 'DATE_FORMAT(enterTime,"%Y-%m-%d")' => date('Y-m-d'), 'actionType' => 0), 'userCode');
    }

    /**
     * 获得最近一周每天的商户总浏览量
     * @param  $shopCode
     * @return string
     */
    public function getShopDayBrowseQuantity($shopCode) {
        $userEnterShopInfoRecordMdl = new UserEnterShopInfoRecordModel();
        for ($i=6; $i>=0; $i--){
            $result['count'] = $userEnterShopInfoRecordMdl->getBrowseQuantity(array('shopCode' => $shopCode, 'DATE_FORMAT(enterTime,"%Y-%m-%d")' => date('Y-m-d', strtotime('-'.$i.' days')), 'actionType' => 0), 'userCode');
            $result['day'] = date('Y-m-d', strtotime('-'.$i.' days'));
            $ret[] = $result;
        }
        return $ret;
    }

    /**
     * 扫二维码
     * @param string $validateString => $userCode + 十六进制的卡号（前六）+ 十六进制的卡号（后四） + 加密的时间戳（二维码生成时间）+ 加密的时间戳（二维码扫码成功的时间）【加密方式：将时间戳转为十六进制，不足10位，补足10位，随机位为[g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z]】
     * @return array
     */
    public function sweepQrCode($validateString){
        //十进制转换成十六进制
        if(empty($validateString) || strlen($validateString) != 65){
            return array('code' => C('QR_PAY_ERROR_CODE.SWEEP_FAIL'));
        }
        //将加密的时间戳转换回来（加密方式：时间戳转为十六进制，不足10位，补足10位，随机位为[g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z]）
        $generateTime = strtolower(substr($validateString, 45, 10));
        $sweepTime = strtolower(substr($validateString, 55, 10));
        $find = '/[ghijklmnopqrstuvwxyz]+/';
        $generateTime = preg_replace($find, '', $generateTime);
        $sweepTime = preg_replace($find, '', $sweepTime);
        $generateTime = hexdec($generateTime); //十六进制转换成十进制
        $sweepTime = hexdec($sweepTime); //十六进制转换成十进制
        if($sweepTime < $generateTime){
            return array('code' => C('QR_PAY_ERROR_CODE.QR_SAFE'));
        }
        if($sweepTime > $generateTime + 60){
            return array('code' => C('QR_PAY_ERROR_CODE.QR_EXPIRED'));
        }
        $userMdl = new UserModel();
        $userCode = substr($validateString, 0, 36);
        $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('mobileNbr'));
        if(empty($userInfo)){
            return array('code' => C('QR_PAY_ERROR_CODE.USER_NOT_EXIST'));
        }
        $bankAccountMdl = new BankAccountModel();
        $bankCard = substr($validateString, 36, 9);
        $accountNbrPre6 = hexdec(substr($bankCard, 0, 5)); //十六进制转换成十进制
        $accountNbrLast4 = hexdec(substr($bankCard, 5, 4)); //十六进制转换成十进制
        $accountNbrLast4 = preg_replace($find, '', $accountNbrLast4);
        $accountNbrLast4 = str_pad($accountNbrLast4, 4, '0', STR_PAD_LEFT);
        $bankAccountInfo = $bankAccountMdl->getBankAccountInfo(array('userCode' => $userCode, 'accountNbrPre6' => $accountNbrPre6, 'accountNbrLast4' => $accountNbrLast4, 'status' => \Consts::BANKACCOUNT_STATUS_SIGNED), array('bankAccountCode'));
        if(empty($bankAccountInfo)){
            return array('code' => C('QR_PAY_ERROR_CODE.BANK_CARD_NOT_EXIT'));
        }
//        if($bankAccountInfo['status'] != \Consts::BANKACCOUNT_STATUS_SIGNED){
//            return array('code' => C('QR_PAY_ERROR_CODE.BANK_CARD_NOT_USE'));
//        }
        return array('code' => C('SUCCESS'), 'userCode' => $userCode, 'bankAccountCode' => $bankAccountInfo['bankAccountCode']);
    }

    /**
     * 获取最优的支付结果
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param float $orderAmount 消费金额 单位：元
     * @param float $noDiscountPrice 消费金额 单位：元
     * @return array
     */
    public function getOptimalPay($userCode, $shopCode, $orderAmount, $noDiscountPrice){
        if($orderAmount * \Consts::HUNDRED > 30000){
            return array('code' => C('QR_PAY_ERROR_CODE.CONSUME_AMOUNT_LIMIT'));
        }
        if(is_null($noDiscountPrice) || $noDiscountPrice == '(null)' || empty($noDiscountPrice)){
            $noDiscountPrice = 0;
        }

        $systemParamMdl = new SystemParamModel();
        $userConsumeMdl = new UserConsumeModel();
        $consumeOrderMdl = new ConsumeOrderModel();
        $userCardMdl = new UserCardModel();
        $bsMdl = new BonusStatisticsModel();
        $shopMdl = new ShopModel();
        $userCouponMdl = new UserCouponModel();
        $batchCouponMdl = new BatchCouponModel();

        //最小支付金额
        $paramInfo = $systemParamMdl->getParamValue('minRealPay');
        $minRealPay = $paramInfo['value'];

        //首单立减
        $isFirst = $userConsumeMdl->isFirst($userCode);

        //元化分
        $newPrice = ($orderAmount - $noDiscountPrice) * \Consts::HUNDRED;
        $price = $orderAmount * C('RATIO');

        // 为该笔支付生成相应的订单
        $ret = $consumeOrderMdl->addConsumeOrder($userCode, $orderAmount * \Consts::HUNDRED, $shopCode);
        $orderNbr = $ret['orderNbr'];
        $ret['code'] = C('SUCCESS');
        if($ret['code'] == C('SUCCESS')) {
            //优惠券抵扣金额
            $couponInsteadPrice = 0;
            $batchCouponCode = '';
            $couponType = '';
            $couponString = '';
            $nbrCoupon = 0;
            //得到用户领取的可用的抵扣券和折扣券
            $userCouponList = $userCouponMdl->listUserCouponWhenPay1($userCode, $shopCode, ($orderAmount - $noDiscountPrice), '');
            $platCouponList = $userCouponMdl->listPlatCouponWhenPay1($userCode, $shopCode, ($orderAmount - $noDiscountPrice), '');
            $userCouponList = array_merge_recursive($userCouponList, $platCouponList);
            if(empty($userCouponList)){
                //得到用户未领取的商家可用的抵扣券和折扣券(默认使用一张)
                $shopCouponList = $batchCouponMdl->listUserCouponWhenPay($userCode, $shopCode, ($orderAmount - $noDiscountPrice), '');
                if($shopCouponList){
                    foreach($shopCouponList as $sv){
                        if($sv['couponType'] == \Consts::COUPON_TYPE_DISCOUNT){
                            $coupon = $newPrice - $newPrice * $sv['discountPercent'] / 10;
                        }else{
                            $coupon = $sv['insteadPrice'] * C('RATIO');
                        }
                        if($couponInsteadPrice < $coupon){
                            $coupon = UtilsModel::phpCeil($coupon);
                            $realPrice = $newPrice - $coupon;
                            if($noDiscountPrice > 0 && $realPrice < 0){
                                continue;
                            }
                            $realPrice = $realPrice + $noDiscountPrice * \Consts::HUNDRED;
                            if($price > $minRealPay && $minRealPay > $realPrice){
                                continue;
                            }else{
                                $couponInsteadPrice = $coupon;
                                $batchCouponCode = $sv['batchCouponCode'];
                                $nbrCoupon = 1;
                                $couponType = $sv['couponType'];
                                switch($couponType){
                                    case 3:
                                        $couponString = '满'.$sv['availablePrice'].'减'.$sv['insteadPrice'];
                                        break;
                                    case 4:
                                        $couponString = '满'.$sv['availablePrice'].'打'.$sv['discountPercent'].'折';
                                        break;
                                    default:
                                        $couponString = '';
                                        break;
                                }
                            }
                        }
                    }
                    if($batchCouponCode){
                        $userCouponMdl->grabCoupon($batchCouponCode, $userCode, C('COUPON_SHARED_LVL.ALL'));
                    }
                }
            }else{
                foreach($userCouponList as $uv){
                    if($uv['couponType'] == \Consts::COUPON_TYPE_DISCOUNT){
                        $canUsed = 1; //折扣券默认使用一张
                    }else{
                        $count = number_format(($newPrice - $newPrice % ($uv['availablePrice'] * C('RATIO'))) / ($uv['availablePrice'] * C('RATIO')));
                        $canUsed = UtilsModel::getMinNbr(array($count, $uv['userCount'], $uv['limitedNbr'])); //抵扣券使用最大张数
                    }
                    for($i = 0; $i < $canUsed; $i++){
                        if($uv['couponType'] == \Consts::COUPON_TYPE_DISCOUNT){
                            $coupon = $newPrice - $newPrice * $uv['discountPercent'] / 10;
                        }else{
                            $coupon = $uv['insteadPrice'] * C('RATIO') * ($canUsed - $i);
                        }
                        if($couponInsteadPrice < $coupon){
                            $coupon = UtilsModel::phpCeil($coupon);
                            $realPrice = $newPrice - $coupon;
                            $isContinue = UtilsModel::isContinueToDiscount($price, $realPrice, $noDiscountPrice, $minRealPay);
                            if($isContinue == false){
                                continue;
                            }else{
                                $couponInsteadPrice = $coupon;
                                $batchCouponCode = $uv['batchCouponCode'];
                                $nbrCoupon = $canUsed;
                                $couponType = $uv['couponType'];
                                switch($couponType){
                                    case 3:
                                        $couponString = '满'.$uv['availablePrice'].'减'.$uv['insteadPrice'];
                                        break;
                                    case 4:
                                        $couponString = '满'.$uv['availablePrice'].'打'.$uv['discountPercent'].'折';
                                        break;
                                    default:
                                        $couponString = '';
                                        break;
                                }
                                break;
                            }
                        }
                    }
                }
            }
            $couponInsteadPrice = UtilsModel::phpCeil($couponInsteadPrice);
            $newPrice = $newPrice - $couponInsteadPrice;

            //会员卡抵扣金额
            $cardInsteadPrice = 0;
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
            $isContinue = UtilsModel::isContinueToDiscount($price, $newPrice, $noDiscountPrice, $minRealPay);
            if($isContinue == false){
                $newPrice = $newPrice + $cardInsteadPrice;
                $cardInsteadPrice = 0;
            }

            //商家红包
            $shopBonus = 0;
            $shopBonusRet = $bsMdl->getUserBonusStatistics($userCode, $shopCode);
            $platBonusRet = $bsMdl->getUserBonusStatistics($userCode, C('HQ_CODE'));
            if($shopBonusRet && $shopBonusRet['totalValue'] > 0){
                $shopBonus = UtilsModel::phpCeil($shopBonusRet['totalValue']);
                $shopBonus = UtilsModel::getMinNbr(array($shopBonus, $newPrice));
                for($i = 0; $i < $shopBonus; $i++){
                    $newPrice = $newPrice - $shopBonus + $i;
                    $isContinue = UtilsModel::isContinueToDiscount($price, $newPrice, $noDiscountPrice, $minRealPay);
                    if($isContinue == false){
                        $newPrice = $newPrice + $shopBonus - $i;
                    }else{
                        $shopBonus = $shopBonus - $i;
                        break;
                    }
                }
            }

            //平台红包
            $platBonus = 0;
            if($platBonusRet && $platBonusRet['totalValue'] > 0) {
                $platBonus = UtilsModel::phpCeil($platBonusRet['totalValue']);
                $platBonus = UtilsModel::getMinNbr(array($platBonus, $newPrice));
                for($i = 0; $i < $platBonus; $i++){
                    $newPrice = $newPrice - $platBonus + $i;
                    $isContinue = UtilsModel::isContinueToDiscount($price, $newPrice, $noDiscountPrice, $minRealPay);
                    if($isContinue == false){
                        $newPrice = $newPrice + $platBonus - $i;
                    }else{
                        $platBonus = $platBonus - $i;
                        break;
                    }
                }
            }

            // 在线支付则计算商家对银行卡的打折
            $bankCardDeduction = 0;
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
            $isContinue = UtilsModel::isContinueToDiscount($price, $newPrice, $noDiscountPrice, $minRealPay);
            if($isContinue == false){
                $newPrice = $newPrice + $bankCardDeduction;
                $bankCardDeduction = 0;
            }

            // 首单立减
            if($isFirst == true) {
                $paramInfo = $systemParamMdl->getParamValue('mealFirstDec');
                $firstDeduction = $paramInfo['value'] ? $paramInfo['value'] : 0; // 10元
                $newPrice = $newPrice - $firstDeduction;
                $newPrice = $newPrice < 0 ? 0 : $newPrice;
                $newPrice = $newPrice + $noDiscountPrice * C('RATIO');
                if($newPrice < $minRealPay){
                    $newPrice = $minRealPay;
                }
            }else{
                $newPrice = $newPrice + $noDiscountPrice * C('RATIO');
            }

            $userCouponCode = '';
            if($batchCouponCode){
                // 获取买单时用户最先领用的用户优惠券编码串，以“|”分隔
                $userCouponCode = $userCouponMdl->getAvailableUserCouponCode($userCode, $batchCouponCode, $nbrCoupon);
            }
            // 生成在线支付记录
            $isUseUserCard = $cardInsteadPrice > 0 ? \Consts::YES : \Consts::NO;
            $isUseBankCard = $bankCardDeduction > 0 ? \Consts::YES : \Consts::NO;
            $ret = $userConsumeMdl->bankcardPay($ret['orderCode'], $userCouponCode, number_format($platBonus / C('RATIO'), 2, '.', ''), number_format($shopBonus / C('RATIO'), 2, '.', ''), $noDiscountPrice, $isUseUserCard, $isUseBankCard);
            if($ret['code'] == C('SUCCESS')) {
                $temp = array('newPrice', 'cardInsteadPrice', 'couponInsteadPrice', 'bankCardDeduction', 'shopBonus', 'platBonus');
                foreach($temp as $v) {
                    $$v = number_format($$v / C('RATIO'), 2, '.', '');
                }
//                $ret['orderNbr'] = $orderNbr;
                $ret['newPrice'] = $newPrice;
                $ret['firstDeduction'] = isset($firstDeduction) ? $firstDeduction / \Consts::HUNDRED : 0;
                $ret['coupon'] = array(
                    'couponInsteadPrice' => $couponInsteadPrice,
                    'couponType' => $couponType,
                    'couponString' => $couponString,
                    'useNbr' => $nbrCoupon,
                );
                $ret['card'] = array(
                    'cardInsteadPrice' => $cardInsteadPrice,
                    'cardDiscount' => isset($userCardInfo['discount']) ? $userCardInfo['discount'] : 0
                );
                $ret['bonus'] = array(
                    'shopBonus' => $shopBonus,
                    'platBonus' => $platBonus,
                    'userShopBonus' => isset($shopBonusRet['totalValue']) ? $shopBonusRet['totalValue'] / \Consts::HUNDRED : 0,
                    'userPlatBonus' => isset($platBonusRet['totalValue']) ? $platBonusRet['totalValue'] / \Consts::HUNDRED : 0
                );
                $ret['bankCard'] = array(
                    'bankCardDeduction' => $bankCardDeduction,
                    'bankCardDiscount' => $shopInfo['onlinePaymentDiscount']
                );
                $userMdl = new UserModel();
                $ret['userInfo'] = $userMdl->getUserInfo(array('userCode' => $userCode), array('nickName', 'avatarUrl'));
            }
        }
        return $ret;
    }

    /**
     * 在线支付确认支付（https协议传输）
     * @param string $consumeCode 用户消费记录编码
     * @param string $bankAccountCode 用户关联银行账户编码
     * @param int $isUseFirstDeduction 是否要使用首单立减
     * @param int $payChanel 支付通道
     * @return string $ret
     */
    public function bankcardPayConfirm($consumeCode, $bankAccountCode, $isUseFirstDeduction, $payChanel) {
        $userConsumeMdl = new UserConsumeModel();
        $ret = $userConsumeMdl->bankcardPayConfirm($consumeCode, $bankAccountCode, '', $isUseFirstDeduction, $payChanel);
        if($ret['code'] == C('SUCCESS')){
            $ret['consumeInfo'] = $userConsumeMdl->getConsumeInfo(array('UserConsume.consumeCode' => $consumeCode), array('User.nickName', 'User.avatarUrl', 'ConsumeOrder.orderNbr', 'ConsumeOrder.orderAmount', 'UserConsume.realPay', 'UserConsume.deduction', 'Shop.shopName', 'UserConsume.payedTime'));
            $temp = array('orderAmount', 'realPay', 'deduction');
            foreach($temp as $v) {
                $ret['consumeInfo'][$v] = number_format($ret['consumeInfo'][$v] / C('RATIO'), 2, '.', '');
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
     * 验证用户密码是否正确
     * @param string $staffCode 用户编码
     * @param string $pwd 密码(md5加密后)
     * @return array {'code'}
     */
    public function valPwd($staffCode, $pwd) {
        // 获得用户的登录密码
        $shopStaffMdl = new ShopStaffModel();
        $staffInfo = $shopStaffMdl->getShopStaffInfo(array('staffCode' => $staffCode), array('password'));
        // 判断登录密码是否正确
        $code = md5(substr($staffCode, 0, 6) . $pwd) === $staffInfo['password'] ? \Consts::YES : \Consts::NO;
        return array('code' => $code);
    }

    /**
     * 修改员工权限
     * @param string $staffCode 员工编码
     * @param string $access 权限之间以“|”分隔
     * @return array
     */
    public function updateShopStaffAccess($staffCode, $access){
        $shopStaffMdl = new ShopStaffModel();
        return $shopStaffMdl->updateStaff(array('staffCode' => $staffCode, 'access' => $access));
    }

    /**
     * PC端订单查询
     * @param string $staffCode 登陆的员工编码
     * @param string $startTime 开始时间 (YYYY-mm-dd)
     * @param string $endTime 结束时间 (YYYY-mm-dd)
     * @param string $shopName 商家名
     * @param int $orderStatus 订单状态：0-不能支付，1-未支付，2-支付中，3-已支付，4-已取消订单（用于其他订单），5-支付失败，6-退款申请中，7-退款成功
     * @param string $orderNbr 订单号
     * @param string $mobileNbr 消费者手机号
     * @param int $page 页码 从 1 开始，
     * @return array
     */
    public function listConsumeOrder($staffCode, $startTime, $endTime, $shopName, $orderStatus, $orderNbr, $mobileNbr, $page){
        //获取用户所属的门店
        $shopStaffRelMdl = new ShopStaffRelModel();
        $shopList = $shopStaffRelMdl->getShopListByStaffCode($staffCode, array('Shop.shopCode'));
        if(empty($shopList)){
            return array();
        }
        $shopCodeArr = array();
        foreach($shopList as $shop){
            if(!in_array($shop['shopCode'], $shopCodeArr)){
                array_push($shopCodeArr, $shop['shopCode']);
            }
        }
        //处理查询条件
        if($startTime && $endTime){
            $condition['ConsumeOrder.orderTime'] = array('Between', array($startTime.' 00:00:00', $endTime.' 23:59:59'));
        }elseif($startTime && !$endTime){
            $condition['ConsumeOrder.orderTime'] = array('egt', $startTime.' 00:00:00');
        }elseif(!$startTime && $endTime){
            $condition['ConsumeOrder.orderTime'] = array('elt', $endTime.' 23:59:59');
        }
        if($shopName){
            $condition['Shop.shopName'] = array('like', '%'.$shopName.'%');
        }
        if($orderStatus){
            $condition['ConsumeOrder.status'] = array('eq', $orderStatus);
        }
        if($orderNbr){
            $condition['ConsumeOrder.orderNbr'] = array('like', '%'.$orderNbr.'%');
        }
        if($mobileNbr){
            $condition['User.mobileNbr'] = array('like', '%'.$mobileNbr.'%');
        }
        $condition['ConsumeOrder.shopCode'] = array('IN', $shopCodeArr);
        $consumeOrderMdl = new ConsumeOrderModel();
        $orderList = $consumeOrderMdl->getConsumeOrderList($condition,
            array(
                'ConsumeOrder.orderCode', //订单编码
                'ConsumeOrder.orderNbr', //订单号
                'ConsumeOrder.orderTime', //订单生成时间
                'FORMAT(ConsumeOrder.orderAmount / 100, 2)'                             => 'orderAmount', //消费金额
                'ConsumeOrder.status', //订单支付状态：0-不能支付，1-未支付，2-支付中，3-已支付，4-已取消订单（用于其他订单），5-支付失败，6-退款申请中，7-退款成功
                'Shop.shopName', //商家名
                'User.mobileNbr', //消费者手机号
                'User.realName', //消费者名字
                'UserConsume.consumeCode', //支付编码
                'FORMAT(UserConsume.noDiscountPrice / 100, 2)'                          => 'noDiscountPrice', //不参与优惠的金额
                'FORMAT(UserConsume.deduction / 100, 2)'                                => 'deduction', //总优惠金额
                'FORMAT(UserConsume.realPay / 100, 2)'                                  => 'realPay', //实际支付金额
                'UserConsume.payType', //支付类型：1-线上银行卡支付，2-POS机支付，3-现金支付，4-未选择支付方式，5-实物券或体验券支付
                'FORMAT(UserConsume.platBonus / 100, 2)'                                => 'platBonus', //平台红包使用量
                'FORMAT(UserConsume.shopBonus / 100, 2)'                                => 'shopBonus', //商家红包使用量
                'FORMAT(UserConsume.bankCardDeduction / 100, 2)'                        => 'bankDeduction', //银行卡优惠的金额 or 银行优惠的金额
                'FORMAT(UserConsume.firstDeduction / 100, 2)'                           => 'firstDeduction', //首单立减金额
                'FORMAT(UserConsume.cardDeduction / 100, 2)'                            => 'cardDeduction', //会员卡优惠的金额
                'FORMAT(UserConsume.couponDeduction / 100, 2)'                          => 'couponDeduction', //优惠券优惠的金额
                'UserConsume.payedTime', //支付时间
                'FORMAT((UserConsume.platBonus + UserConsume.firstDeduction) / 100, 2)' => 'platDeduction', //平台优惠的金额
                'FORMAT((UserConsume.shopBonus + UserConsume.cardDeduction + UserConsume.couponDeduction) / 100, 2)' => 'shopDeduction', //商家优惠的金额
            ),
            array(
                array(
                    'joinTable' => 'UserConsume',
                    'joinCondition' => 'UserConsume.orderCode = ConsumeOrder.orderCode',
                    'joinType' => 'inner',
                ),
                array(
                    'joinTable' => 'User',
                    'joinCondition' => 'User.userCode = ConsumeOrder.clientCode',
                    'joinType' => 'left',
                ),
                array(
                    'joinTable' => 'Shop',
                    'joinCondition' => 'Shop.shopCode = ConsumeOrder.shopCode',
                    'joinType' => 'inner',
                ),
            ),
            'ConsumeOrder.orderTime desc',
            \Consts::PAGESIZE,
            $page
        );
        return $orderList;
    }

    /**
     * 订单统计
     * @param string $staffCode 登陆的员工编码
     * @param string $startTime 开始时间 (YYYY-mm-dd)
     * @param string $endTime 结束时间 (YYYY-mm-dd)
     * @param string $shopName 商家名
     * @param int $orderStatus 订单状态：0-不能支付，1-未支付，2-支付中，3-已支付(默认值)，4-已取消订单，5-支付失败，6-退款申请中，7-退款成功
     * @param string $orderNbr 订单号
     * @param string $mobileNbr 消费者手机号
     * @param string $type 按照类型统计：1：门店（默认值），2状态
     * @return array
     */
    public function consumeOrderStatistics($staffCode, $startTime, $endTime, $shopName, $orderStatus, $orderNbr, $mobileNbr, $type){
        if(is_null($type) || empty($type)){$type = 1;}
        if(is_null($orderStatus) || empty($orderStatus)){$orderStatus = 3;}
        //获取用户所属的门店
        $shopStaffRelMdl = new ShopStaffRelModel();
        $shopList = $shopStaffRelMdl->getShopListByStaffCode($staffCode, array('Shop.shopCode', 'Shop.shopName'));
        if(empty($shopList)){
            return array();
        }
        $shopCodeArr = array();
        $shopArr = array();
        foreach($shopList as $shop){
            if(!in_array($shop['shopCode'], $shopCodeArr)){
                array_push($shopCodeArr, $shop['shopCode']);
            }
            $shopArr[$shop['shopCode']] = $shop['shopName'];
        }
        //处理查询条件
        if($startTime && $endTime){
            $condition['ConsumeOrder.orderTime'] = array('Between', array($startTime.' 00:00:00', $endTime.' 23:59:59'));
        }elseif($startTime && !$endTime){
            $condition['ConsumeOrder.orderTime'] = array('egt', $startTime.' 00:00:00');
        }elseif(!$startTime && $endTime){
            $condition['ConsumeOrder.orderTime'] = array('elt', $endTime.' 23:59:59');
        }
        if($shopName){
            $condition['Shop.shopName'] = array('like', '%'.$shopName.'%');
        }
        if($orderStatus && $type == 1){
            $condition['ConsumeOrder.status'] = array('eq', $orderStatus);
        }
        if($orderNbr){
            $condition['ConsumeOrder.orderNbr'] = array('like', '%'.$orderNbr.'%');
        }
        if($mobileNbr){
            $condition['User.mobileNbr'] = array('like', '%'.$mobileNbr.'%');
        }
        $consumeOrderMdl = new ConsumeOrderModel();
        $data = array();
        if($type == 1){ //按门店统计
            foreach($shopCodeArr as $shop){
                $condition['ConsumeOrder.shopCode'] = $shop;
                $orderList = $consumeOrderMdl->getOrderInfo($condition,
                    array(
                        'count(ConsumeOrder.orderCode)'                       => 'orderCount', //成交笔数
                        'FORMAT(sum(ConsumeOrder.orderAmount) / 100, 2)'      => 'orderAmount', //消费金额
                        'FORMAT(sum(UserConsume.deduction) / 100, 2)'         => 'deduction', //总优惠金额
                        'FORMAT(sum(UserConsume.realPay) / 100, 2)'           => 'realPay', //实际支付金额
                        'FORMAT(sum(UserConsume.platBonus) / 100, 2)'         => 'platBonus', //平台红包使用量
                        'FORMAT(sum(UserConsume.shopBonus) / 100, 2)'         => 'shopBonus', //商家红包使用量
                        'FORMAT(sum(UserConsume.bankCardDeduction) / 100, 2)' => 'bankCardDeduction', //银行卡优惠的金额 OR 银行优惠的金额
                        'FORMAT(sum(UserConsume.firstDeduction) / 100, 2)'    => 'firstDeduction', //首单立减金额
                        'FORMAT(sum(UserConsume.cardDeduction) / 100, 2)'     => 'cardDeduction', //会员卡优惠的金额
                        'FORMAT(sum(UserConsume.couponDeduction) / 100, 2)'   => 'couponDeduction', //优惠券优惠的金额
                        'FORMAT(sum(UserConsume.platBonus + UserConsume.firstDeduction) / 100, 2)' => 'platDeduction', //平台优惠的金额
                        'FORMAT(sum(UserConsume.shopBonus + UserConsume.cardDeduction + UserConsume.couponDeduction) / 100, 2)' => 'shopDeduction', //商家优惠的金额
                    ),
                    array(
                        array(
                            'joinTable' => 'UserConsume',
                            'joinCondition' => 'UserConsume.orderCode = ConsumeOrder.orderCode',
                            'joinType' => 'inner',
                        ),
                    )
                );
                if($orderList['orderAmount']){
                    $orderList['shopCode'] = $shop;
                    $orderList['shopName'] = $shopArr[$shop];
                    $orderList['status'] = $orderStatus;
                    $data[] = $orderList;
                }
            }
        }else{ //按状态统计
            $statusArr = array(
                '1', //未支付
                '3', //已支付
                '4', //已取消订单
                '5', //支付失败
                '7', //退款
            );
            $condition['ConsumeOrder.shopCode'] = array('IN', $shopCodeArr);
            foreach($statusArr as $sta){
                $condition['ConsumeOrder.status'] = array('eq', $sta);
                $orderList = $consumeOrderMdl->getOrderInfo($condition,
                    array(
                        'count(ConsumeOrder.orderCode)'                       => 'orderCount', //成交笔数
                        'FORMAT(sum(ConsumeOrder.orderAmount) / 100, 2)'      => 'orderAmount', //消费金额
                        'FORMAT(sum(UserConsume.deduction) / 100, 2)'         => 'deduction', //总优惠金额
                        'FORMAT(sum(UserConsume.realPay) / 100, 2)'           => 'realPay', //实际支付金额
                        'FORMAT(sum(UserConsume.platBonus) / 100, 2)'         => 'platBonus', //平台红包使用量
                        'FORMAT(sum(UserConsume.shopBonus) / 100, 2)'         => 'shopBonus', //商家红包使用量
                        'FORMAT(sum(UserConsume.bankCardDeduction) / 100, 2)' => 'bankCardDeduction', //银行卡优惠的金额 OR 银行优惠的金额
                        'FORMAT(sum(UserConsume.firstDeduction) / 100, 2)'    => 'firstDeduction', //首单立减金额
                        'FORMAT(sum(UserConsume.cardDeduction) / 100, 2)'     => 'cardDeduction', //会员卡优惠的金额
                        'FORMAT(sum(UserConsume.couponDeduction) / 100, 2)'   => 'couponDeduction', //优惠券优惠的金额
                        'FORMAT(sum(UserConsume.platBonus + UserConsume.firstDeduction) / 100, 2)' => 'platDeduction', //平台优惠的金额
                        'FORMAT(sum(UserConsume.shopBonus + UserConsume.cardDeduction + UserConsume.couponDeduction) / 100, 2)' => 'shopDeduction', //商家优惠的金额
                    ),
                    array(
                        array(
                            'joinTable' => 'UserConsume',
                            'joinCondition' => 'UserConsume.orderCode = ConsumeOrder.orderCode',
                            'joinType' => 'inner',
                        ),
                    )
                );
                if($orderList['orderAmount']){
                    $orderList['status'] = $sta;
                    $data[] = $orderList;
                }
            }
        }
        return $data;
    }

    /**
     * 获得某个商家的短信接收人
     * @param string $shopCode 商家编码
     * @return array
     */
    public function getMRecipientList($shopCode){
        $shopStaffRelMdl = new ShopStaffRelModel();
        $shopStaffArr = $shopStaffRelMdl->getStaffList(array('ShopStaffRel.shopCode' => $shopCode, 'ShopStaff.status' => \Consts::SHOP_STAFF_STATUS_ACTIVE, 'ShopStaff.userLvl' => array('gt', \Consts::SHOP_STAFF_LVL_EMPLOYEE), 'ShopStaff.isSendPayedMsg' => C('YES')), array('ShopStaff.staffCode' => 'recipientId', 'ShopStaff.mobileNbr', 'ShopStaff.realName' => 'staffName'));
        $mrMdl = new MessageRecipientModel();
        $mRecipient = $mrMdl->getMRecipientList(array('shopCode' => $shopCode), array('id' => 'recipientId', 'staffName', 'mobileNbr'), array(), 'createTime asc');
        return array_merge($shopStaffArr, $mRecipient);
    }

    /**
     * 增加短信接收人
     * @param string $shopCode 商家编码
     * @param string $creatorCode 创建人编码
     * @param string $staffName 员工姓名
     * @param string $mobileNbr 员工手机号
     * @param string $validateCode 验证码
     * @return array
     */
    public function addMRecipient($shopCode, $creatorCode, $staffName, $mobileNbr, $validateCode){
        $mrMdl = new MessageRecipientModel();
        return $mrMdl->editMRecipient(
            array(
                'staffName' => $staffName,
                'mobileNbr' => $mobileNbr,
                'creatorCode' => $creatorCode,
                'shopCode' => $shopCode,
                'validateCode' => $validateCode
            )
        );
    }

    /**
     * 删除短信接收人
     * @param int $recipientId
     * @return array
     */
    public function delMRecipient($recipientId){
        if(strlen($recipientId) == 36 && $recipientId <= (int)$recipientId){
            $shopStaffMdl = new ShopStaffModel();
            return $shopStaffMdl->updateStaff(array('staffCode' => $recipientId, 'isSendPayedMsg' => C('NO')));
        }else{
            $mrMdl = new MessageRecipientModel();
            return $mrMdl->delMRecipient(array('id' => $recipientId));
        }
    }

    /**
     * 查询商家订单列表
     * @param string $shopCode 商家编码
     * @param string $keyWord 关键字查询
     * @param string $date 日期 格式：YYYY-MM-DD
     * @param int $page 从1开始
     * @return array(
     *             'orderList' => 订单列表 ,
     *             'totalCount' => 总记录数,
     *             'page' => 当前页码,
     *             'nextPage' => 下一页,
     *             'count' => 当前页记录数,
     *         )
     */
    public function getOrderListForB($shopCode, $keyWord, $date, $page){
        $page = empty($page) ? 1 : $page;
        $date = empty($date) ? date('Y-m-d') : $date;
        $consumeOrderMdl = new ConsumeOrderModel();
        $condition['ConsumeOrder.shopCode'] = $shopCode;
        if(!empty($keyWord)) {
            $subCondition = array(
                'orderNbr' => array('LIKE', "%$keyWord"),
                'User.mobileNbr' => array('LIKE', "%$keyWord%"),
                '_logic' => 'OR',
            );
            $condition['_complex'] = $subCondition;
        }
        $condition['ConsumeOrder.orderTime'] = array('like', $date."%");
        $joinTableArr = array(
            array(
                'joinTable' => 'User',
                'joinCondition' => 'User.userCode = ConsumeOrder.clientCode',
                'joinType' => 'left',
            ),
            array(
                'joinTable' => '(select * from (select * from UserConsume order by consumeTime desc) r group by r.orderCode) newConsume',
                'joinCondition' => 'newConsume.orderCode = ConsumeOrder.orderCode',
                'joinType' => 'left',
            ),
        );
        $orderList = $consumeOrderMdl->getConsumeOrderList($condition, array('ConsumeOrder.orderCode', 'orderTime', 'orderNbr', 'orderAmount', 'ConsumeOrder.status', 'User.avatarUrl', 'User.nickName', 'User.mobileNbr', 'ConsumeOrder.orderConfirm', 'newConsume.consumeCode', 'newConsume.realPay', 'newConsume.deduction', 'newConsume.payedTime'), $joinTableArr, 'ConsumeOrder.orderTime desc', \Consts::PAGESIZE, $page);
        if($orderList){
            foreach($orderList as $k => $v){
                $orderList[$k]['orderAmount'] = number_format($v['orderAmount'] / \Consts::HUNDRED, 2, '.', '');
                $orderList[$k]['mobileNbr'] = substr($v['mobileNbr'], -4);
                $orderList[$k]['realPay'] = number_format($v['realPay'] / \Consts::HUNDRED, 2, '.', '');
                $orderList[$k]['deduction'] = number_format($v['deduction'] / \Consts::HUNDRED, 2, '.', '');
            }
        }
        $totalCount = $consumeOrderMdl->getConsumeOrderCount($condition, $joinTableArr);
        return array(
            'orderList' => $orderList,
            'totalCount' => $totalCount,
            'page' => $page,
            'nextPage' => UtilsModel::getNextPage($totalCount, $page),
            'count' => count($orderList),
        );
    }

    /**
     * 获取开班列表
     * @param string $shopCode 商家编码
     * @param int $page 班级列表：默认是1，依次递增；课程表：直接传0
     * @return array
     */
    public function getShopClassList($shopCode, $page){
        $shopClassMdl = new ShopClassModel();
        $joinTableArr = array(
            array(
                'joinTable' => 'ShopTeacher',
                'joinCondition' => 'ShopTeacher.teacherCode = ShopClass.teacherCode',
                'joinType' => 'left'
            )
        );
        $limit = \Consts::SHOP_CLASS_PAGE_SIZE;
        if($page > 0){
            $shopClassList = $shopClassMdl->getShopClassList(
                array('ShopClass.shopCode' => $shopCode),
                array('ShopClass.*', 'ShopTeacher.teacherName'),
                $joinTableArr,
                'ShopClass.learnStartDate asc, ShopClass.learnEndDate asc',
                $limit,
                $page
            );
        }else{
            $shopClassList = $shopClassMdl->getShopClassList(
                array('ShopClass.shopCode' => $shopCode),
                array('ShopClass.classCode', 'ShopClass.className', 'ShopClass.learnStartDate', 'ShopClass.learnEndDate', 'ShopClass.teacherCode', 'ShopTeacher.teacherName'),
                $joinTableArr,
                'ShopClass.learnStartDate asc, ShopClass.learnEndDate asc'
            );
        }
        if($shopClassList){
            $classWeekInfoMdl = new ClassWeekInfoModel();
            foreach($shopClassList as $k => $v){
                if(isset($v['learnFee'])){$shopClassList[$k]['learnFee'] = number_format($v['learnFee'] / \Consts::HUNDRED, 2);}
                $shopClassList[$k]['classWeekInfo'] = $classWeekInfoMdl->getClassWeekList(array('classCode' => $v['classCode']), array(), array(), 'weekName asc, startTime asc, endTime asc');
            }
        }
        if($page > 0){
            $totalCount = $shopClassMdl->countShopClass(array('ShopClass.shopCode' => $shopCode), $joinTableArr);
            return array(
                'shopClassList' => $shopClassList,
                'totalCount' => $totalCount,
                'count' => count($shopClassList),
                'page' => $page,
                'nextPage' => UtilsModel::getNextPage($totalCount, $page, $limit)
            );
        }
        return $shopClassList;
    }

    /**
     * 获取开班详情
     * @param string $classCode 班级编码
     * @return mixed
     */
    public function getShopClassInfo($classCode){
        $shopClassMdl = new ShopClassModel();
        $joinTableArr = array(
            array(
                'joinTable' => 'ShopTeacher',
                'joinCondition' => 'ShopTeacher.teacherCode = ShopClass.teacherCode',
                'joinType' => 'left'
            )
        );
        $shopClassInfo = $shopClassMdl->getShopClassInfo(array('ShopClass.classCode' => $classCode), array('ShopClass.*' , 'ShopTeacher.teacherName'), $joinTableArr);
        $shopClassInfo['learnFee'] = number_format($shopClassInfo['learnFee'] / \Consts::HUNDRED, 2);
        $classWeekInfoMdl = new ClassWeekInfoModel();
        $shopClassInfo['classWeekInfo'] = $classWeekInfoMdl->getClassWeekList(array('classCode' => $classCode), array(), array(), 'weekName asc, startTime asc, endTime asc');
        return $shopClassInfo;
    }

    /**
     * 新增开班
     * @param string $className 班级名称
     * @param string $learnStartDate 学习开始时间
     * @param string $learnEndDate 学习结束时间
     * @param string $learnMemo 适合描述
     * @param string $learnFee 报名费用
     * @param string $learnNum 所学课时
     * @param string $teacherCode 教师编码
     * @param string $signStartDate 报名开始时间
     * @param string $signEndDate 报名结束时间
     * @param string $classInfo 课程简介
     * @param string $classUrl 课程形象图
     * @param string $classWeekInfo 上课时间
     * @return array
     */
    public function addShopClass($className, $learnStartDate, $learnEndDate, $learnMemo, $learnFee, $learnNum, $teacherCode, $signStartDate, $signEndDate, $classInfo, $classUrl, $classWeekInfo){
        $classWeekInfo = json_decode($classWeekInfo, true);
        if(empty($classWeekInfo)){
            return array('code' => C('EDUCATION_SHOP_ERROR_CODE.CLASS_WEEK_INFO_EMPTY'));
        }
        $shopClassMdl = new ShopClassModel();
        $shopTeacherMdl = new ShopTeacherModel();
        $shopTeacherInfo = $shopTeacherMdl->getShopTeacherInfo(array('teacherCode' => $teacherCode));
        M()->startTrans();
        $shopClassRet = $shopClassMdl->editShopClass(array(
            'className' => $className,
            'shopCode' => $shopTeacherInfo['shopCode'],
            'learnStartDate' => $learnStartDate,
            'learnEndDate' => $learnEndDate,
            'learnMemo' => $learnMemo,
            'learnFee' => $learnFee * \Consts::HUNDRED,
            'learnNum' => $learnNum,
            'teacherCode' => $teacherCode,
            'signStartDate' => $signStartDate,
            'signEndDate' => $signEndDate,
            'classInfo' => $classInfo,
            'classUrl' => $classUrl,
        ));
        if($shopClassRet['code'] != C('SUCCESS')){
            return $shopClassRet;
        }
        $classWeekInfoMdl = new ClassWeekInfoModel();
        $classWeekInfoRet = $classWeekInfoMdl->delClassWeekInfo(array('classCode' => $shopClassRet['classCode']));
        if($classWeekInfoRet['code'] != C('SUCCESS')){
            return $classWeekInfoRet;
        }
        foreach($classWeekInfo as $cv){
            $cv['classCode'] = $shopClassRet['classCode'];
            if(empty($cv['endTime'])){
                $cv['endTime'] = date('H:i', strtotime($cv['startTime']) + $cv['duration'] * 60);
            }
            unset($cv['duration']);
            $classWeekInfoRet = $classWeekInfoMdl->editClassWeek($cv);
            if($classWeekInfoRet['code'] != C('SUCCESS')){
                return $classWeekInfoRet;
            }
        }
        M()->commit();
        return $shopClassRet;
    }

    /**
     * 删除开班
     * @param string $classCode 班级编码
     * @return array
     */
    public function delShopClass($classCode){
        $shopClassMdl = new ShopClassModel();
        //TODO 如果有人报名或报过名则不能删除
//        return array('code' => C('EDUCATION_SHOP_ERROR_CODE.SHOP_CLASS_DELETE_ERROR'));
        M()->startTrans();
        $shopClassRet = $shopClassMdl->delShopClass(array('classCode' => $classCode));
        if($shopClassRet['code'] != C('SUCCESS')){
            return $shopClassRet;
        }

        $classWeekInfoMdl = new ClassWeekInfoModel();
        $classWeekInfoRet = $classWeekInfoMdl->delClassWeekInfo(array('classCode' => $classCode));
        if($classWeekInfoRet['code'] != C('SUCCESS')){
            return $classWeekInfoRet;
        }
        M()->commit();
        return $classWeekInfoRet;
    }

    /**
     * 获取教师列表
     * @param string $shopCode 商家编码
     * @param int $page 页码 a.名师堂列表 默认从 1 开始，依次递增; b.新增开班的时候直接传 0
     * @return mixed
     */
    public function getShopTeacherList($shopCode, $page){
        $shopTeacherMdl = new ShopTeacherModel();
        if($page > 0){
            $limit = \Consts::SHOP_TEACHER_PAGE_SIZE;
            $shopTeacherList = $shopTeacherMdl->getShopTeacherList(array('shopCode' => $shopCode), array('*'), array(), '', $limit, $page);
            $totalCount = $shopTeacherMdl->countShopTeacher(array('shopCode' => $shopCode));
            return array(
                'shopTeacherList' => $shopTeacherList,
                'totalCount' => $totalCount,
                'count' => count($shopTeacherList),
                'page' => $page,
                'nextPage' => UtilsModel::getNextPage($totalCount, $page, $limit)
            );
        }
        return $shopTeacherMdl->getShopTeacherList(array('shopCode' => $shopCode), array('teacherCode', 'teacherName', 'teacherTitle'));
    }

    /**
     * 获取教师详情
     * @param string $teacherCode 教师编码
     * @return array
     */
    public function getShopTeacherInfo($teacherCode){
        $shopTeacherMdl = new ShopTeacherModel();
        $shopTeacherInfo = $shopTeacherMdl->getShopTeacherInfo(array('teacherCode' => $teacherCode));
        $teacherWorkMdl = new TeacherWorkModel();
        $shopTeacherInfo['teacherWork'] = $teacherWorkMdl->getTeacherWorkList(array('teacherCode' => $teacherCode), array('workCode', 'workUrl'), array(), 'workUploadTime desc');
        return $shopTeacherInfo;
    }

    /**
     * 增加或修改教师
     * @param string $teacherCode 教师编码 增加时传空字符串，修改时传对应的教师编码
     * @param string $updateData 数据
     * @return array
     */
    public function editShopTeacher($teacherCode, $updateData){
        $updateData = json_decode($updateData, true);
        $shopTeacherMdl = new ShopTeacherModel();
        if(isset($updateData['teacherWork'])){
            $teacherWork = $updateData['teacherWork'];
            unset($updateData['teacherWork']);
        }
        if($teacherCode){
            $updateData['teacherCode'] = $teacherCode;
        }
        M()->startTrans();
        $shopTeacherRet = $shopTeacherMdl->editShopTeacher($updateData);
        if($shopTeacherRet['code'] != C('SUCCESS')){
            return $shopTeacherRet;
        }
        if(isset($teacherWork) && $teacherWork){
            $teacherWorkMdl = new TeacherWorkModel();
            foreach($teacherWork as $tv){
                if($tv['workUrl']){
                    $tv['teacherCode'] = $shopTeacherRet['teacherCode'];
                    $teacherWorkRet = $teacherWorkMdl->editTeacherWork($tv);
                    if($teacherWorkRet['code'] != C('SUCCESS')){
                        return $teacherWorkRet;
                    }
                }
            }
        }
        M()->commit();
        return $shopTeacherRet;
    }

    /**
     * 删除教师
     * @param string $teacherCode 教师编码
     * @return array
     */
    public function delShopTeacher($teacherCode){
        $shopTeacherMdl = new ShopTeacherModel();
        $shopClassMdl = new ShopClassModel();
        //如果该教师有开课则不能删除
        $shopClassInfo = $shopClassMdl->getShopClassInfo(array('teacherCode' => $teacherCode), array('ShopClass.classCode'));
        if($shopClassInfo){
            return array('code' => C('EDUCATION_SHOP_ERROR_CODE.SHOP_TEACHER_DELETE_ERROR'));
        }
        M()->startTrans();
        $shopTeacherRet = $shopTeacherMdl->delShopTeacher(array('teacherCode' => $teacherCode));
        if($shopTeacherRet['code'] != C('SUCCESS')){
            return $shopTeacherRet;
        }

        $teacherWorkMdl = new TeacherWorkModel();
        $teacherWorkRet = $teacherWorkMdl->delTeacherWork(array('teacherCode' => $teacherCode));
        if($teacherWorkRet['code'] != C('SUCCESS')){
            return $teacherWorkRet;
        }
        M()->commit();
        return $teacherWorkRet;
    }

    /**
     * 获取商家学院之星列表
     * @param string $shopCode 商家编码
     * @param int $page 从1开始，依次递增
     * @return array
     */
    public function getStudentStarList($shopCode, $page){
        $studentStarMdl = new StudentStarModel();
        $limit = \Consts::SHOP_TEACHER_PAGE_SIZE;
        $studentStarList = $studentStarMdl->getStudentStarList(array('shopCode' => $shopCode), array('starCode', 'starName', 'starUrl', 'starInfo', 'signCode'), array(), 'iptTime desc', $limit, $page);
        $totalCount = $studentStarMdl->countStudentStar(array('shopCode' => $shopCode));
        return array(
            'studentStarList' => $studentStarList,
            'totalCount' => $totalCount,
            'count' => count($studentStarList),
            'page' => $page,
            'nextPage' => UtilsModel::getNextPage($totalCount, $page, $limit)
        );
    }

    /**
     * 获取商家最新的学员之星
     * @param string $shopCode 商家编码
     * @return mixed
     */
    public function getNewestStudentStar($shopCode){
        $studentStarMdl = new StudentStarModel();
        $stuStarWorkMdl = new StuStarWorkModel();
        $studentStarInfo = $studentStarMdl->getStudentStarInfo(array('shopCode' => $shopCode), array('starCode', 'starName', 'starUrl', 'starInfo', 'signCode'));
        $studentStarInfo['starWork'] = $stuStarWorkMdl->getStuStarWorkList(array('starCode' => $studentStarInfo['starCode']), array('starWorkCode', 'starImgUrl'), array(), 'starUploadTime desc');
        return $studentStarInfo;
    }

    /**
     * 获取星详情
     * @param string $starCode 星编码
     * @return mixed
     */
    public function getStudentStarInfo($starCode){
        $studentStarMdl = new StudentStarModel();
        $stuStarWorkMdl = new StuStarWorkModel();
        $studentStarInfo = $studentStarMdl->getStudentStarInfo(array('starCode' => $starCode), array('starCode', 'starName', 'starUrl', 'starInfo', 'signCode'));
        $studentStarInfo['starWork'] = $stuStarWorkMdl->getStuStarWorkList(array('starCode' => $starCode), array('starWorkCode', 'starImgUrl'), array(), 'starUploadTime desc');
        return $studentStarInfo;
    }

    /**
     * 增加或修改学院之星
     * @param string $starCode 星编码 增加时传空字符串，修改时传对应的星编码
     * @param string $updateData 数据
     * @return array
     */
    public function editStudentStar($starCode, $updateData){
        $updateData = json_decode($updateData, true);
        $studentStarMdl = new StudentStarModel();
        if(isset($updateData['starWork'])){
            $starWork = $updateData['starWork'];
            unset($updateData['starWork']);
        }
        if($starCode){
            $updateData['starCode'] = $starCode;
        }
        M()->startTrans();
        $studentStarRet = $studentStarMdl->editStudentStar($updateData);
        if($studentStarRet['code'] != C('SUCCESS')){
            return $studentStarRet;
        }
        if(isset($starWork) && $starWork){
            $stuStarWorkMdl = new StuStarWorkModel();
            foreach($starWork as $tv){
                if($tv['starImgUrl']){
                    $tv['starCode'] = $studentStarRet['starCode'];
                    $starWorkRet = $stuStarWorkMdl->editStuStarWork($tv);
                    if($starWorkRet['code'] != C('SUCCESS')){
                        return $starWorkRet;
                    }
                }
            }
        }
        M()->commit();
        return $studentStarRet;
    }

    /**
     * 删除学员之星
     * @param string $starCode 星编码
     * @return array
     */
    public function delStudentStar($starCode){
        $studentStarMdl = new StudentStarModel();
        M()->startTrans();
        $studentStarRet = $studentStarMdl->delStudentStar(array('starCode' => $starCode));
        if($studentStarRet['code'] != C('SUCCESS')){
            return $studentStarRet;
        }

        $stuStarWorkMdl = new StuStarWorkModel();
        $stuStarWorkRet = $stuStarWorkMdl->delStuStarWork(array('starCode' => $starCode));
        if($stuStarWorkRet['code'] != C('SUCCESS')){
            return $stuStarWorkRet;
        }
        M()->commit();
        return $stuStarWorkRet;
    }

    /**
     * 获取商家招生启示
     * @param string $shopCode 商家编码
     * @return mixed
     */
    public function getShopRecruitInfo($shopCode){
        $shopRecruitMdl = new ShopRecruitModel();
        return $shopRecruitMdl->getShopRecruitInfo(array('shopCode' => $shopCode));
    }

    /**
     * 增加或修改招生启示
     * @param string $recruitCode 招生启示编码
     * @param string $updateData 招生启示信息
     * @return array
     */
    public function editShopRecruit($recruitCode, $updateData){
        $updateData = json_decode($updateData, true);
        $shopRecruitMdl = new ShopRecruitModel();
        if($recruitCode){
            $updateData['recruitCode'] = $recruitCode;
        }
        return $shopRecruitMdl->editShopRecruit($updateData);
    }

    /**
     * 获取商家荣誉墙
     * @param string $shopCode 商家编码
     * @param int $page 页码
     * @return array
     */
    public function getShopHonorList($shopCode, $page){
        $shopHonorMdl = new ShopHonorModel();
        $limit = \Consts::SHOP_TEACHER_PAGE_SIZE;
        $shopHonorList = $shopHonorMdl->getShopHonorList(array('shopCode' => $shopCode), array('*'), array(), 'uploadTime desc', $limit, $page);
        $totalCount = $shopHonorMdl->countShopHonor(array('shopCode' => $shopCode));
        return array(
            'shopHonorList' => $shopHonorList,
            'totalCount' => $totalCount,
            'count' => count($shopHonorList),
            'page' => $page,
            'nextPage' => UtilsModel::getNextPage($totalCount, $page, $limit)
        );
    }

    /**
     * 增加荣誉
     * @param string $shopCode 商家编码
     * @param string $honorUrl 荣誉图
     * @return array
     */
    public function addShopHonor($shopCode, $honorUrl){
        $shopHonorMdl = new ShopHonorModel();
        return $shopHonorMdl->editShopHonor(array('shopCode' => $shopCode, 'honorUrl' => $honorUrl));
    }

    /**
     * 删除荣誉
     * @param string $honorCode 商家编码
     * @return array
     */
    public function delShopHonor($honorCode){
        $shopHonorMdl = new ShopHonorModel();
        return $shopHonorMdl->delShopHonor(array('honorCode' => $honorCode));
    }

    /**
     * 获取商家校长之语
     * @param string $shopCode 商家编码
     * @return mixed
     */
    public function getShopHeaderInfo($shopCode){
        $shopHeaderMdl = new ShopHeaderModel();
        return $shopHeaderMdl->getShopHeaderInfo(array('shopCode' => $shopCode));
    }

    /**
     * 增加或修改校长之语
     * @param string $headerCode 校长之语编码
     * @param string $updateData 数据
     * @return array
     */
    public function editShopHeader($headerCode, $updateData){
        $shopHeaderMdl = new ShopHeaderModel();
        $updateData = json_decode($updateData, true);
        if($headerCode){
            $updateData['headerCode'] = $headerCode;
        }
        return $shopHeaderMdl->editShopHeader($updateData);
    }

    /**
     * 获取某个商家某个或全部课程的评论列表
     * @param string $classCode 班级编码
     * @param string $shopCode 商家编码
     * @param int $page 页码 默认从1开始，依次递增
     * @return array
     */
    public function getClassRemarkList($classCode, $shopCode, $page){
        $classRemarkMdl = new ClassRemarkModel();
        $limit = \Consts::SHOP_TEACHER_PAGE_SIZE;
        $joinTableArr = array(
            array(
                'joinTable' => 'User',
                'joinCondition' => 'User.userCode = ClassRemark.userCode',
                'joinType' => 'left',
            )
        );
        $condition = array();
        if($classCode){
            $condition['classCode'] = $classCode;
        }else{
            $classCodeArr = array();
            if($shopCode){
                $shopClassMdl = new ShopClassModel();
                $classCodeArr = $shopClassMdl->getShopClassFieldArr('classCode', array('shopCode' => $shopCode));
            }
            if(empty($classCodeArr)){
                $classCodeArr = array('0');
            }
            $classCodeArr = array_unique($classCodeArr);
            $condition['classCode'] = array('IN', $classCodeArr);
        }

        $classRemarkList = $classRemarkMdl->getClassRemarkList(array('classCode' => $classCode), array('ClassRemark.*', 'User.nickName'), $joinTableArr, 'remarkTime desc', $limit, $page);
        $totalCount = $classRemarkMdl->countClassRemark(array('classCode' => $classCode), $joinTableArr);
        if($classRemarkList){
            $classRemarkImgMdl = new ClassRemarkImgModel();
            foreach($classRemarkList as $k => $v){
                $classRemarkList[$k]['remarkTime'] = substr($v['remarkTime'], 0, 10);
                $classRemarkList[$k]['isRemarkByShop'] = C('NO');
                if($v['shopRemarkTime']){
                    $classRemarkList[$k]['isRemarkByShop'] = C('YES');
                    $classRemarkList[$k]['shopRemarkTime'] = substr($v['shopRemarkTime'], 0, 10);
                }
                $classRemarkList[$k]['classRemarkImg'] = $classRemarkImgMdl->getClassRemarkImgList(array('remarkCode' => $v['remarkCode']), array('remarkImgUrl'));
            }
        }
        return array(
            'classRemarkList' => $classRemarkList,
            'totalCount' => $totalCount,
            'count' => count($classRemarkList),
            'page' => $page,
            'nextPage' => UtilsModel::getNextPage($totalCount, $page, $limit)
        );
    }

    /**
     * 商家回复评论
     * @param string $remarkCode 评论列表
     * @param string $shopRemark 商家回复
     * @return array
     */
    public function replyClassRemark($remarkCode, $shopRemark){
        $classRemarkMdl = new ClassRemarkModel();
        return $classRemarkMdl->editClassRemark(array('remarkCode' => $remarkCode, 'shopRemark' => $shopRemark));
    }
}
