<?php
/**
 * Created by PhpStorm.
 * User: 98041
 * Date: 2018/7/10
 * Time: 10:13
 */

namespace Api\Controller;
use Api\Controller\ApiBaseController;
use Common\Model\CardModel;
use Common\Model\UserCardModel;

class WeiXinCardController extends ApiBaseController
{
//    private $appid = "wxeea0a43338406e57";
    private $appid = "wxdacebf540333a98f";
    private $secret = "d37c8606678f5faa16ce322b8ead4d03";
    private $access_token= "";

    public function __construct() {
        parent::__construct();
        // 确保jsonrpc后面不被挂上一堆调试信息。
        C('SHOW_PAGE_TRACE', false);
    }

    //1. 领取会员卡到用户账户
    public function  addCardUser($userCode,$shopCode){
        // 判断用户code
        if(strlen($userCode)<2){
            return array("code"=>50500,"state"=>false);
        }
        // 判断商家code
        if(strlen($shopCode)<2){
            return array("code"=>50314,"state"=>false);
        }

        //(1).判断商家是否有会员卡并启用
        $shopCardCode =  D("Card")->where(array("shopCode"=>$shopCode,"status"=>"1"))->find();
        if($shopCardCode==null || !is_array($shopCardCode)){
            return array("code"=>50331,"state"=>false);
        }

        // （2）.判断用户是否拥有该会员卡
        $userCardModel = new UserCardModel();
        $hasCard =  $userCardModel->isUserHasShopCard($userCode,$shopCode);

        //（3）.添加会员卡到用户会员卡列表(如果用户没有会员卡则添加)
        if(!$hasCard){
            $userCardModel->checkUserCard($userCode,$shopCode);
            return array("code"=>50000,"state"=>true);
        }
        return array("code"=>50332,"state"=>false);
    }

    // 2.添加会员卡到微信卡包
    public function  addWeixinCard($shopCode,$userCode){

        // 获取access_token 并缓存
        $access_token = S("access_token");
        if(!$access_token){
            $url ="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=".$this->appid."&secret=".$this->secret;
            $data =   json_decode($this->http_post_data($url));
            $access_token = $data->access_token;
            $this->access_token = $access_token;
            S("access_token",$access_token,7000);
        }

        // 1.判断用户code
        if(strlen($userCode)<2){
            return array("code"=>50500,"state"=>false);
        }
        // 2.判断商家code
        if(strlen($shopCode)<2){
            return array("code"=>50314,"state"=>false);
        }

        //3..判断商家是否有会员卡并启用
        $shopCardCode =  D("Card")->where(array("shopCode"=>$shopCode,"status"=>"1"))->find();
        if($shopCardCode==null || !is_array($shopCardCode)){
            return array("code"=>50331,"state"=>false);
        }

        // 4.获取用户会员卡信息
        $CardShopInfo =  M()->table("UserCard")
            ->join('User ON User.userCode = UserCard.userCode')
            ->join('Card ON Card.cardCode = UserCard.cardCode')
            ->join('Shop ON Shop.shopCode = Card.shopCode')
            ->where(array("Shop.shopCode"=>$shopCode,"UserCard.userCode"=>$userCode,"UserCard.status"=>1))
            ->field("Shop.ShopName,Shop.shopCode,Shop.logoUrl,userCardCode,Card.discount,Card.remark,
            realName,nickName,UserCard.userCode,Shop.tel,Card.cardLvl,Card.cardType,Card.discountRequire,User.openId,
            Shop.street,UserCard.inWeixinCard,UserCard.cardNbr")
            ->find();

        // 用户没有该商户启用状态会员卡
        if(!is_array($CardShopInfo)){
            return array("code"=>50500,"state"=>false);
        }

        //用户会员卡已添加到微信卡包
        if($CardShopInfo['inWeixinCard']==1){
            return array("code"=>50334,"state"=>false);
        }

        //添加卡券到微信卡包
        // 卡券签名
        $cardinfo   = $this->getApiTiket($CardShopInfo,$access_token);

        // 获取微信权限
        $signPakage = $this->getSignPackage($access_token);

        $info = array("cardInfo"=>$cardinfo,"signPakage"=>$signPakage,"userCardCode"=>$CardShopInfo['userCardCode']);

        return $info;
    }

    //3.判断用户是否拥有该商户会员卡
    public function  hasCard($userCode,$shopCode){

        // 判断用户code
        if(strlen($userCode)<2){
            return array("code"=>50500,"state"=>false);
        }
        // 判断商家code
        if(strlen($shopCode)<2){
            return array("code"=>50314,"state"=>false);
        }

        // 2.判断用户是否拥有该会员卡
        $userCardModel = new UserCardModel();
        $hasCard =  $userCardModel->isWeiXinCard($userCode,$shopCode);

        if($hasCard==1){
            return  array("code"=>50000,"state"=>1);
        }elseif ($hasCard==2){
            return  array("code"=>50000,"state"=>2);
        }else{
            return   array("code"=>50000,"state"=>false);
        }
    }

    //4.查询用户会员卡列表($inWeixinCard 参数1.代表已添加到微信卡包 2.代表未添加到微信卡包)
    public function listWeixinCard($userCode,$inWeixinCard){

        // 判断用户code
        if(strlen($userCode)<2){
            return array("code"=>50500,"state"=>false);
        }

        //查询到该用户的所有会员卡
        $userCodelModel = new UserCardModel();
        $listCard       = $userCodelModel->getUserList($userCode,$inWeixinCard);
        return $listCard;
    }


    //7.添加优惠券到微信卡包
    public function  addWeixinCoupon($userCode,$shopCode,$userCouponCode,$zoneId){

        //1.请选择一张优惠券
        if(strlen($userCouponCode)<2){
            return array("code"=>51000,"state"=>false);
        }

        // 1.判断用户code
        if(strlen($userCode)<2){
            return array("code"=>50500,"state"=>false);
        }
        // 2.判断商家code
        if(strlen($shopCode)<2){
            return array("code"=>50314,"state"=>false);
        }


        $batchCoupon = D("UserCoupon")->where(array("userCouponCode"=>$userCouponCode,"status"=>"1"))
            ->field("userCouponCode,inWeixinCard")->find();

        //3.判断用户是否有该优惠券
        if(!$batchCoupon){
            return array("code"=>80223,"state"=>false);
        }

        //3.判断优惠券是否已添加微信卡包
        if($batchCoupon['inWeixinCard']==1){
            return array("code"=>50335,"state"=>false);
        }

        // 获取access_token 并缓存
        $access_token = S("access_token");
        if(!$access_token){
            $url ="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=".$this->appid."&secret=".$this->secret;
            $data =   json_decode($this->http_post_data($url));
            $access_token = $data->access_token;
            $this->access_token = $access_token;
            S("access_token",$access_token,7000);
        }

        $batchCouponInfo = D()->table("UserCoupon")
            ->join("BatchCoupon on BatchCoupon.batchCouponCode=UserCoupon.batchCouponCode")
            ->join("User on User.userCode=UserCoupon.userCode")
            ->join("Shop on Shop.shopCode=BatchCoupon.shopCode")
            ->where(array("UserCoupon.userCode"=>$userCode,"UserCoupon.status"=>"1",
                "Shop.shopCode"=>$shopCode,"UserCoupon.userCouponCode"=>$userCouponCode))
            ->field("BatchCoupon.couponName,BatchCoupon.couponBelonging,
                BatchCoupon.batchCouponCode,BatchCoupon.expireTime,Shop.logoUrl,
                Shop.shopCode,Shop.shopName,Shop.tel,Shop.street,BatchCoupon.function,BatchCoupon.couponType,
                BatchCoupon.remark,BatchCoupon.startUsingTime,BatchCoupon.totalVolume,BatchCoupon.nbrPerPerson,
                BatchCoupon.availablePrice,BatchCoupon.insteadPrice,BatchCoupon.discountPercent,BatchCoupon.function,User.openId,
                BatchCoupon.dayStartUsingTime,BatchCoupon.dayEndUsingTime,UserCoupon.userCouponNbr")->find();

        // 3.判断商家是否存在该优惠券
        if(!is_array($batchCouponInfo) || strlen($batchCouponInfo['shopCode'])<2){
            return array("code"=>80223,"state"=>false);
        }

        //4.获取上传微信的卡券图片
        //图片要OSS上面的
        $imgUrl =  C("urlOSS").$batchCouponInfo['logoUrl'];
        $data_uploadimg = $this->upload($imgUrl,$access_token);

        $obj_img = json_decode($data_uploadimg);
        $img_url = $obj_img->url;

        //优惠券折扣
        $batchCouponInfo['discountPercent'] = 100- $batchCouponInfo['discountPercent'];  //表示打折额度（百分比）。填30就是七折。

        //转换时间 为时间撮
        $batchCouponInfo['startUsingTime'] =strtotime($batchCouponInfo['startUsingTime']); // 优惠券开始使用时间
        $batchCouponInfo['expireTime'] = strtotime($batchCouponInfo['expireTime']);         // 优惠券结束使用时间

        //转换小时分钟
        $batchCouponInfo['day_begin_hour'] =(int) substr($batchCouponInfo['dayStartUsingTime'],0,strpos($batchCouponInfo['dayStartUsingTime'], ':'));
        $batchCouponInfo['day_end_hour']   =(int) substr($batchCouponInfo['dayEndUsingTime'],0,strpos($batchCouponInfo['dayEndUsingTime'], ':'));
        $batchCouponInfo['day_begin_minute'] =(int) substr($batchCouponInfo['dayStartUsingTime'],1,strpos($batchCouponInfo['dayStartUsingTime'], ':')-1);
        $batchCouponInfo['day_end_minute'] = (int)  substr($batchCouponInfo['dayEndUsingTime'],1,strpos($batchCouponInfo['dayEndUsingTime'], ':')-1);

        //判断优惠券类型
          switch (  $batchCouponInfo['couponType']){
              case "4":
                  $batchCouponInfo['couponType'] = "DISCOUNT";
                  break;
              case "7":
                  $batchCouponInfo['couponType'] = "GIFT";
                  break;
              case "8":
                  $batchCouponInfo['couponType'] = "CASH";
                break;
              default:
                  $batchCouponInfo['couponType'] = "GENERAL_COUPON";
                  break;
          }

        // 5.创建优惠券

        //6拿到用户openId
        $openId = D("User")->where(array("userCode"=>$userCode))->field("openId")->find();

        $url ="https://api.weixin.qq.com/card/create?access_token=".$access_token;
        $data = $this->couponType($batchCouponInfo['couponType'],$img_url,$batchCouponInfo,$userCouponCode,$zoneId,$openId['openId']);

        $res  = $this->http_post_data($url,$data);

        $info = json_decode($res);

        $card_id = $info->card_id;
        $userCouponNbr = $batchCouponInfo['userCouponNbr'];

        // H5 JSSDK卡券投放
        $getCoupon     = $this->getCouponApiTiket($userCouponNbr,$card_id,$access_token);

        $signPackage   = $this->getSignPackageCoup($access_token);

        $CouponInfo = array("CouponInfo"=>$getCoupon,"signPakage"=>$signPackage);
        return $CouponInfo;
    }

    //优惠券类型
    private function  couponType($type,$img_url,$batchCouponInfo,$userCouponCode,$zoneId,$openId){

        $batchCouponInfo['shopName'] = $this->cn_substr($batchCouponInfo['shopName'],11);

     $center_url_dai_dui = "\"https://".$_SERVER['HTTP_HOST']."/huiquan/coupondetails/".$userCouponCode."?from=weixin&openid_other=".$openId."&zoneId={$zoneId}\"";
     $center_url_di_zhe = "\"https://".$_SERVER['HTTP_HOST']."/huiquan/ksubmit/".$userCouponCode."?from=weixin&openid_other=".$openId."&zoneId={$zoneId}\"";
//             $center_url_dai_dui = "\"https://".$_SERVER['HTTP_HOST']."/huiquan/\"";
//             $center_url_di_zhe = "\"https://".$_SERVER['HTTP_HOST']."/huiquan/\"";


            switch ($type){

                case "CASH":
                    $data = '{
                        "card": {
                        "card_type": "CASH",
                        "cash": {
                          "base_info": {
                              "logo_url":  "'.$img_url.'",
                              "brand_name": "'.$batchCouponInfo['shopName'].'",
                              "code_type": "CODE_TYPE_BARCODE",
                              "title": "代金券",
                              "color": "Color070",
                              "notice": "使用时向服务员出示此券",
                              "service_phone": "'.$batchCouponInfo['tel'].'",
                              "description": "'.$batchCouponInfo['remark'].' 微信卡包删除后系统将无法使用",
                               "date_info": {
                                     "type": "DATE_TYPE_FIX_TIME_RANGE",
                                     "begin_timestamp": '.$batchCouponInfo['startUsingTime'].' ,
                                     "end_timestamp":'.$batchCouponInfo['expireTime'].'
                                 },
                              "sku": {
                                  "quantity": '.$batchCouponInfo['totalVolume'].'
                              },
                              "get_limit": '.$batchCouponInfo['nbrPerPerson'].',
                              "use_custom_code": true,
                              "center_title": "立即使用",
                         
                              "center_url":'.$center_url_dai_dui.'
                          },
                          
                          "advanced_info": {          
                            "time_limit": [
                                   {   "type":"MONDAY",
                                       "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   },
                                   
                                  {   "type":"TUESDAY",
                                      "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   },   
                                   
                                   {   "type":"WEDNESDAY",
                                        "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   },
                                   
                                   {   "type":"THURSDAY",
                                      "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   },
                                   
                                   {   "type":"FRIDAY",
                                        "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   },
                                   
                                  {   "type":"SATURDAY",
                                        "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   },
                                   
                                   {   "type":"SUNDAY",
                                       "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   }
                                   
                               ]
                         },
                           
                         "least_cost": '.$batchCouponInfo['availablePrice'].',
                         "reduce_cost": '.$batchCouponInfo['insteadPrice'].'
                      }
                  }
                }';
                    break;


                case "GIFT":
                    $data = '{
                        "card": {
                        "card_type": "GIFT",
                        "gift": {
                          "base_info": {
                              "logo_url":  "'.$img_url.'",
                              "brand_name": "'.$batchCouponInfo['shopName'].'",
                              "code_type": "CODE_TYPE_BARCODE",
                              "title": "兑换券",
                              "color": "Color070",
                              "notice": "使用时向服务员出示此券",
                              "service_phone": "'.$batchCouponInfo['tel'].'",
                              "description": "'.$batchCouponInfo['remark'].'微信卡包删除后系统将无法使用",
                               "date_info": {
                                     "type": "DATE_TYPE_FIX_TIME_RANGE",
                                     "begin_timestamp": '.$batchCouponInfo['startUsingTime'].' ,
                                     "end_timestamp":'.$batchCouponInfo['expireTime'].'
                                 },
                              "sku": {
                                  "quantity": '.$batchCouponInfo['totalVolume'].'
                              },
                              "get_limit": '.$batchCouponInfo['nbrPerPerson'].',
                              "use_custom_code": true,
                              "center_title": "立即使用",
                              "center_url":'.$center_url_dai_dui.'
                          },
                          
                          "advanced_info": {
                            "time_limit": [
                                   {   "type":"MONDAY",
                                       "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   },
                                   
                                  {   "type":"TUESDAY",
                                      "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   },   
                                   
                                   {   "type":"WEDNESDAY",
                                        "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   },
                                   
                                   {   "type":"THURSDAY",
                                      "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   },
                                   
                                   {   "type":"FRIDAY",
                                        "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   },
                                   
                                  {   "type":"SATURDAY",
                                        "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   },
                                   
                                   {   "type":"SUNDAY",
                                       "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   }
                                   
                               ]
                         },                  
                               	"gift": "'.$batchCouponInfo['couponName'].'"
                      }
                  }
                }';
                    break;

                case "DISCOUNT":
                    $data = '{
                        "card": {
                        "card_type": "DISCOUNT",
                        "discount": {
                          "base_info": {
                              "logo_url":  "'.$img_url.'",
                              "brand_name": "'.$batchCouponInfo['shopName'].'",
                              "code_type": "CODE_TYPE_TEXT",
                              "title": "折扣券",
                              "color": "Color060",
                              "notice": "使用时向服务员出示此券",
                              "service_phone": "'.$batchCouponInfo['tel'].'",
                              "description": "'.$batchCouponInfo['remark'].'微信卡包删除后系统将无法使用",
                               "date_info": {
                                     "type": "DATE_TYPE_FIX_TIME_RANGE",
                                     "begin_timestamp": '.$batchCouponInfo['startUsingTime'].' ,
                                     "end_timestamp":'.$batchCouponInfo['expireTime'].'
                                 },
                              "sku": {
                                  "quantity": '.$batchCouponInfo['totalVolume'].'
                              },
                              "get_limit": '.$batchCouponInfo['nbrPerPerson'].',
                              "use_custom_code": true,
                              "center_title": "立即使用",
                              "sub_title":"'.$batchCouponInfo['userCouponNbr'].'",
                               "center_url":'.$center_url_di_zhe.'
                          },
                          
                          "advanced_info": {
                            "time_limit": [
                                   {   "type":"MONDAY",
                                       "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   },
                                   
                                  {   "type":"TUESDAY",
                                      "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   },   
                                   
                                   {   "type":"WEDNESDAY",
                                        "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   },
                                   
                                   {   "type":"THURSDAY",
                                      "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   },
                                   
                                   {   "type":"FRIDAY",
                                        "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   },
                                   
                                  {   "type":"SATURDAY",
                                        "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   },
                                   
                                   {   "type":"SUNDAY",
                                       "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   }
                                   
                               ]
                         },
                       "discount": '.$batchCouponInfo['discountPercent'].'
                      }
                  }
                }';
                    break;

                default:
                    $data = '{
                        "card": {
                        "card_type": "GENERAL_COUPON",
                        "general_coupon": {
                          "base_info": {
                              "logo_url":  "'.$img_url.'",
                              "brand_name": "'.$batchCouponInfo['shopName'].'",
                              "code_type": "CODE_TYPE_TEXT",
                              "title": "优惠券",
                              "color": "Color050",
                              "notice": "使用时向服务员出示此券",
                              "service_phone": "'.$batchCouponInfo['tel'].'",
                              "description": "'.$batchCouponInfo['remark'].'微信卡包删除后系统将无法使用",
                               "date_info": {
                                     "type": "DATE_TYPE_FIX_TIME_RANGE",
                                     "begin_timestamp": '.$batchCouponInfo['startUsingTime'].' ,
                                     "end_timestamp":'.$batchCouponInfo['expireTime'].'
                                 },
                              "sku": {
                                  "quantity": '.$batchCouponInfo['totalVolume'].'
                              },
                              "get_limit": '.$batchCouponInfo['nbrPerPerson'].',
                              "use_custom_code": true,
                              "center_title": "立即使用",
                              "sub_title":"'.$batchCouponInfo['userCouponNbr'].'",
                              "center_url":'.$center_url_di_zhe.'
                          },
                          
                          "advanced_info": {
                            "time_limit": [
                                   {   "type":"MONDAY",
                                       "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   },
                                   
                                  {   "type":"TUESDAY",
                                      "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   },   
                                   
                                   {   "type":"WEDNESDAY",
                                        "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   },
                                   
                                   {   "type":"THURSDAY",
                                      "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   },
                                   
                                   {   "type":"FRIDAY",
                                        "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   },
                                   
                                  {   "type":"SATURDAY",
                                        "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   },
                                   
                                   {   "type":"SUNDAY",
                                       "begin_hour":'.$batchCouponInfo['day_begin_hour'].',
                                       "end_hour":'.$batchCouponInfo['day_end_hour'].',
                                       "begin_minute":'.$batchCouponInfo['day_begin_minute'].',
                                       "end_minute":'.$batchCouponInfo['day_end_minute'].'
                                   }
                                   
                               ]
                         },
                       "default_detail":"'.$batchCouponInfo['shopName'].'"
                      }
                  }
                }';
                    break;
            }
            return $data;
    }

    //11.已领取优惠券到微信卡包
    public  function  updateUserWeixinCoupon($userCouponCode){
        //1.请选择一张优惠券
        if(strlen($userCouponCode)<2){
            return array("code"=>51000,"state"=>false);
        }

        $userCouponModel = D("UserCoupon");
        $updateStatus =  $userCouponModel->where(array("userCouponCode"=>$userCouponCode))
            ->save(array("inWeixinCard"=>1));

        // 开启事务
        $userCouponModel->startTrans();

        if($updateStatus){
            $userCouponModel->commit();
            return array("code"=>50000,"state"=>true);
        }else{
            $userCouponModel->rollback();
            return array("code"=>80219,"state"=>false);
        }
    }

    //10.已领取会员卡到微信卡包
    public  function  updateUserWeixinCard($userCardCode){
        // 判断会员卡编号是否有效
        if(strlen($userCardCode)<4){
            return array("code"=>50335,"state"=>false);
        }

        $userCardModel = D("UserCard");
        $updateStatus =  $userCardModel->where(array("UserCardCode"=>$userCardCode))
            ->save(array("inWeixinCard"=>1));

        // 开启事务
        $userCardModel->startTrans();

        if($updateStatus){
            $userCardModel->commit();
            return array("code"=>50000,"state"=>true);
        }else{
            $userCardModel->rollback();
            return array("code"=>50344,"state"=>false);
        }
    }

//    链接微信处理------------------------------------------------------------------------------------------------------


//    创建会员卡
    public function  createVipCard($CardShopInfo,$accesstoken){

       $shoname = $this->cn_substr($CardShopInfo['ShopName'],8);
        $imgUrlCard = C("urlOSS").$CardShopInfo['logoUrl'];
        $data_uploadimg = $this->upload($imgUrlCard,$accesstoken);

        $obj_img = json_decode($data_uploadimg);

        $img_url = $obj_img->url;

        $custom_url ="https://".$_SERVER['HTTP_HOST'].'/huiquan?openid='.$CardShopInfo['openId'];

        $url ="https://api.weixin.qq.com/card/create?access_token=".$accesstoken;

        $data = '{
                "card": {
                    "card_type": "MEMBER_CARD",
                    "member_card": {
                        "background_pic_url": "'.$img_url.'",
                        "base_info": {
                            "logo_url": "'.$img_url.'",
                            "brand_name": "'.$shoname.'",
                            "code_type": "CODE_TYPE_TEXT",
                            "title": "会员卡",
                            "color": "Color030",
                            "notice": "使用时向服务员出示此卡",
                            "service_phone": "'.$CardShopInfo['tel'].'",
                            "description": "不可与其他会员卡同享",
                            "date_info": {
                                "type": "DATE_TYPE_PERMANENT"
                            },
                            "sku": {
                                    "quantity": 10000000
                                },
                            "get_limit": 1,
                            "use_custom_code": true,
                            "can_give_friend": false,
                            "custom_url_name": "立即使用",
                            "custom_url":"'.$custom_url.'"
                        },
                        "supply_bonus": false,
                        "supply_balance": false,
                        "prerogative": "会员特权说明",
                        "auto_activate": true
                    }
                }
            }';

        $res  = $this->http_post_data($url,$data);

        $data = json_decode($res);

        return $data;
    }

    //在本地服务器生成文件
    private  function UploadFile($path){
        $pathinfo = pathinfo($path);
        //目录
        $dir = $_SERVER['DOCUMENT_ROOT']."/Public/Uploads/";
        if(!is_dir($dir)){
            mkdir($dir);
        }
        $name = $dir.$pathinfo[basename];
        $source=file_get_contents($path);
        $res =  file_put_contents($name,$source);

        if($res){
            return $name;
        }else{
            return false;
        }
    }


// 上传临时文件到微信
    private function upload($file,$access_token){
        $url = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=".$access_token."&type=image";
        //生成在服务器文件
        $fileName =  $this->UploadFile($file);
        // 生成上传数据
        $data = array('media'=>curl_file_create($fileName));

        return $this->http_post_data($url,$data);
    }

    //获取优惠券签名
    private function  getCouponApiTiket($userCouponNbr,$card_id,$accesstoken){
        $timestamp  = time();
        $nonceStr   = $this->createNonceStr();
        $apitiket   = $this->get_api_ticket($accesstoken);
        $code       = $userCouponNbr;
        // 这里参数的顺序要按照 key 值 ASCII 码升序排序
        $arr = array($timestamp ,$apitiket,$code,$nonceStr,$card_id);
        sort($arr, SORT_STRING);
        $signature = sha1(implode($arr));
        $signPackage = array(
            "nonce_str"  => $nonceStr,
            "timestamp" => $timestamp,
            "signature" => $signature,
            "apitiket"  => $apitiket,
            "code"       => $code,
            "card_id"   => $card_id,
        );
        return $signPackage;
    }


    //获取会员卡卡签名
    private  function  getApiTiket($CardShopInfo,$accesstoken){
        $card_res   = $this->createVipCard($CardShopInfo,$accesstoken); // 会员卡id
        $timestamp  = time();
        $nonceStr   = $this->createNonceStr();
        $apitiket   = $this->get_api_ticket($accesstoken);
        $code       = $CardShopInfo['cardNbr'];
        // 这里参数的顺序要按照 key 值 ASCII 码升序排序
        $arr = array($timestamp ,$apitiket,$code,$nonceStr,$card_res->card_id);
        sort($arr, SORT_STRING);
        $signature = sha1(implode($arr));
        $signPackage = array(
            "nonce_str"  => $nonceStr,
            "timestamp" => $timestamp,
            "signature" => $signature,
            "apitiket"  => $apitiket,
            "code"       => $code,
            "card_id"   => $card_res->card_id,
        );
        return $signPackage;
    }


    //获取微信权限验证配置(优惠券)
    private function getSignPackageCoup($accesstoken) {
        $jsapiTicket = $this->get_jsApi_ticket($accesstoken);
        $timestamp = time();
        $nonceStr = $this->createNonceStr();
        //$url = $this->get_url();
        $url = "https://".$_SERVER['HTTP_HOST']."/huiquan/";//验证微信返回url地址
        // 这里参数的顺序要按照 key 值 ASCII 码升序排序
        $string = "jsapi_ticket=$jsapiTicket&noncestr=$nonceStr&timestamp=$timestamp&url=$url";

        $signature = sha1($string);

        $signPackage = array(
            "appid"      => $this->appid,
            "noncestr"  => $nonceStr,
            "timestamp" => $timestamp,
            "url"        => $url,
            "signature" => $signature,
            "rawstring" => $string
        );
        return $signPackage;
    }

    //获取微信权限验证配置
    private function getSignPackage($accesstoken) {
        $jsapiTicket = $this->get_jsApi_ticket($accesstoken);
        $timestamp = time();
        $nonceStr = $this->createNonceStr();
//        $url = $this->get_url();
        $url = "https://".$_SERVER['HTTP_HOST']."/huiquan/";//验证微信返回url地址
        // 这里参数的顺序要按照 key 值 ASCII 码升序排序
        $string = "jsapi_ticket=$jsapiTicket&noncestr=$nonceStr&timestamp=$timestamp&url=$url";

        $signature = sha1($string);

        $signPackage = array(
            "appid"      => $this->appid,
            "noncestr"  => $nonceStr,
            "timestamp" => $timestamp,
            "url"        => $url,
            "signature" => $signature,
            "rawstring" => $string
        );
        return $signPackage;
    }

//    获取随机签名
    private function createNonceStr($length = 16) {
        $chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        $str = "";
        for ($i = 0; $i < $length; $i++) {
            $str .= substr($chars, mt_rand(0, strlen($chars) - 1), 1);
        }
        return $str;
    }

    // 获取微信注入的tiket
    private function get_jsApi_ticket($accesstoken) {
        // jsapi_ticket 应该全局存储与更新，以下代码以写入到文件中做示例
        $appid	= $this->appid;

        $data = json_decode(S("jsapi_ticket_".$appid));

        if ($data->expire_time < time()) {

            // 如果是企业号用以下 URL 获取 ticket
            $url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=".$accesstoken;

            $res = json_decode($this->http_post_data($url));
            $ticket = $res->ticket;

            if ($ticket) {
                // 过期时间
                $time = 7000;
                $data->expire_time = time() + $time;
                $data->jsapi_ticket = $ticket;

                //写入缓存
                S("jsapi_ticket_".$appid,json_encode($data),$time);
            }
        } else {
            $ticket = $data->jsapi_ticket;
        }
        return $ticket;
    }

    //获取卡券api_tiket
    private function get_api_ticket($accesstoken) {
        // jsapi_ticket 应该全局存储与更新，以下代码以写入到文件中做示例
        $appid	= $this->appid;

        $data = json_decode(S("api_ticket_".$appid));

        if ($data->expire_time < time()) {

            // 如果是企业号用以下 URL 获取 ticket
            $url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=".$accesstoken."&type=wx_card";

            $res = json_decode($this->http_post_data($url));
            $ticket = $res->ticket;

            if ($ticket) {
                // 过期时间
                $time = 7000;
                $data->expire_time = time() + $time;
                $data->jsapi_ticket = $ticket;

                //写入缓存
                S("api_ticket_".$appid,json_encode($data),$time);
            }
        } else {
            $ticket = $data->jsapi_ticket;
        }
        return $ticket;
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

    //取得当前服务器URL
    private function get_url(){
        // 取得当前URL地址
        return $this->get_is_http().$_SERVER["HTTP_HOST"].$_SERVER["REQUEST_URI"];
    }

    // 取得当前页面是http 还是 https
    private function get_is_http(){
        $bool = $this->is_https();
        if($bool){
            return "https://";
        }else{
            return "http://";
        }
    }

    //UTF-8切割字符串
    private function cn_substr($string, $length, $etc = '...'){
        $result = '';
        $string = html_entity_decode(trim(strip_tags($string)), ENT_QUOTES, 'UTF-8');
        $strlen = strlen($string);
        for($i = 0; (($i < $strlen) && ($length > 0)); $i++){
            if($number = strpos(str_pad(decbin(ord(substr($string, $i, 1))), 8, '0', STR_PAD_LEFT), '0')){
                if($length < 1.0){
                    break;
                }

                $result .= substr($string, $i, $number);
                $length -= 1.0;
                $i += $number - 1;

            }else{

                $result .= substr($string, $i, 1);
                $length -= 0.5;
            }
        }

        $result = htmlspecialchars($result, ENT_QUOTES, 'UTF-8');
        if($i < $strlen){
            $result .= $etc;
        }
        return $result;
    }

    // https or http 判断
    private function is_https() {
        if (!empty($_SERVER['HTTPS']) && strtolower($_SERVER['HTTPS']) !== 'off') {
            return TRUE;
        } elseif (isset($_SERVER['HTTP_X_FORWARDED_PROTO']) && $_SERVER['HTTP_X_FORWARDED_PROTO'] === 'https') {
            return TRUE;
        } elseif (!empty($_SERVER['HTTP_FRONT_END_HTTPS']) && strtolower($_SERVER['HTTP_FRONT_END_HTTPS']) !== 'off') {
            return TRUE;
        }
        return FALSE;
    }
//    链接微信处理---------------------------------------------------------------------------------------------------------
}

