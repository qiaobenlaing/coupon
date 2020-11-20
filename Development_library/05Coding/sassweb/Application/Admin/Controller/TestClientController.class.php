<?php
/**
 * Created by PhpStorm.
 * User: 98041
 * Date: 2018/7/5
 * Time: 9:19
 */
namespace Admin\Controller;

use Common\Model\UserConsumeModel;
use Think\Controller;

class TestClientController extends  Controller
{

    public function __construct() {
        parent::__construct();
        // 确保jsonrpc后面不被挂上一堆调试信息。
        C('SHOW_PAGE_TRACE', false);
//        $this->pager = new Pager(I('page', 1, 'intval'), I('pageSize', Pager::DEFUALT_PAGE_SIZE, 'intval'));
    }

    private $access_token= "";
    private $appid = "wxdacebf540333a98f";
    private $secret = "d37c8606678f5faa16ce322b8ead4d03";

    // 上传临时文件

    /**
     * @param $file
     * @param string $type
     * @return mixed
     */
    public function upload($file){

        $url = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=".$this->access_token."&type=image";

        // 生成上传数据
        $data = array('media'=>curl_file_create($file));
        return $this->http_post_data($url,$data);
    }



//    判断优惠券是否可用
    public function xiugai(){
        $consumeCode        = $_POST['consumeCode'];
        $userConsumeMdl     = new UserConsumeModel();
        // 判断支付账单中使用的优惠券是否可用
        $isCouponCanBeUse = $userConsumeMdl->isConsumeCouponCanBeUse($consumeCode);
        if($isCouponCanBeUse) {
            $userConsumeMdl->bankcardPayConfirm2($consumeCode);
        }
    }


//    创建卡券
    public function  createVipCard(){

        $batchCouponCode = "d213e6b2-ec72-67ad-598c-7d8e0c84a96e";
//        $batchCouponCode  = trim($_REQUEST['batchCouponCode']);

        //获取商家信息
        $shopInfo = M()->table("BatchCoupon as b")->field("s.shopName,s.shopCode,s.logoUrl,s.street,s.tel")->join("Shop as s on b.shopCode=s.shopCode")->where("b.batchCouponCode='$batchCouponCode'")->find();

        $data_uploadimg = $this->upload($_SERVER["DOCUMENT_ROOT"].$shopInfo['logoUrl']);

        $obj_img = json_decode($data_uploadimg);

        $img_url = $obj_img->url;

        // 存储卡券信息
        $GLOBALS['shopInfo'] = $shopInfo;

        $url ="https://api.weixin.qq.com/card/create?access_token=".$this->access_token;
        $data = '{
    "card": {
        "card_type": "MEMBER_CARD",
        "member_card": {
             "background_pic_url": "'.$img_url.'",
            "base_info": {
                "logo_url": "'.$img_url.'",
                "brand_name": "'.$shopInfo['shopName'].'",
                "code_type": "CODE_TYPE_TEXT",
                "title": "'.$shopInfo['shopName'].'会员卡",
                "color": "Color060",
                "notice": "使用时向服务员出示此券",
                
                "description": "不可与其他优惠同享",
                "date_info": {
                    "type": "DATE_TYPE_PERMANENT"
                },
                "sku": {
                    "quantity": 50000000
                },
                "get_limit": 3,
                "use_custom_code": true,
                "can_give_friend": true,
                "location_id_list": [
                    123,
                    12321
                ],
                "custom_url_name": "立即使用",
                "custom_url":"https://hfq.huift.com.cn/huiquan/#/indexCouponDetails/'.$batchCouponCode.'",
                "custom_url_sub_title": "tips"
            },
             "advanced_info": {
               "use_condition": {
                   "accept_category": "鞋类",
                   "reject_category": "阿迪达斯",
                   "can_use_with_other_discount": true
               },
             "abstract": {
                   "abstract": "武汉惠圈，期待您的光临",
                   "icon_url_list": [
                       "http://mmbiz.qpic.cn/mmbiz/p98FjXy8LacgHxp3sJ3vn97bGLz0ib0Sfz1bjiaoOYA027iasqSG0sj
  piby4vce3AtaPu6cIhBHkt6IjlkY9YnDsfw/0"
                   ]
               },
               "text_image_list": [
                   {
                       "image_url": "http://mmbiz.qpic.cn/mmbiz/p98FjXy8LacgHxp3sJ3vn97bGLz0ib0Sfz1bjiaoOYA027iasqSG0sjpiby4vce3AtaPu6cIhBHkt6IjlkY9YnDsfw/0",
                       "text": "仟吉十年，重启影响"
                   },
                   {
                       "image_url":  "'.$img_url.'",
                       "text":"'.$shopInfo['street'].'"
                   }
               ],
               "time_limit": [
                   {
                       "type": "MONDAY",
                       "begin_hour":0,
                       "end_hour":10,
                       "begin_minute":10,
                       "end_minute":59
                   },
                   {
                       "type": "HOLIDAY"
                   }
               ],
               "business_service": [
                   "BIZ_SERVICE_FREE_WIFI",
                   "BIZ_SERVICE_WITH_PET",
                   "BIZ_SERVICE_FREE_PARK",
                   "BIZ_SERVICE_DELIVER"
               ]
           },
            "supply_bonus": true,
            "supply_balance": false,
            "prerogative": "test_prerogative",
            "auto_activate": true,
            "custom_field1": {
                "name_type": "FIELD_NAME_TYPE_LEVEL",
                "url": "https://hfq.huift.com.cn/huiquan/#/indexCouponDetails/'.$batchCouponCode.'"
            },
            "activate_url": "https://hfq.huift.com.cn/huiquan/#/indexCouponDetails/'.$batchCouponCode.'",
            "custom_cell1": {
                "name": "使用入口",
                "tips": "点击进入",
                "url":  "https://hfq.huift.com.cn/huiquan/#/indexCouponDetails/'.$batchCouponCode.'"
            }
        }
    }
}';
        $res  = $this->http_post_data($url,$data);
        $data = json_decode($res);
        return $data;
    }

    // 取得全局 AccessToken
    public function getAccessToken(){


        F();

        // 微信公众appid和密钥
        $appid			= $this->appid;
        $secret			= $this->secret;

        // 获取 access_token
        $data = json_decode(S("access_token_".$appid));

        // 过期时间判断
        if ($data->expire_time < time()) {

            // 获取access_token Url
            $url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=".$appid."&secret=".$secret;

            // 抓取内容
            $res = json_decode($this->http_post_data($url));

            // 获取 access_token
            $access_token = $res->access_token;

            // 写入缓存
            if ($access_token) {

                // 过期时间
                $time = 7000;

                // 组合缓存内容
                $data->expire_time = time() + $time;
                $data->access_token = $access_token;

                // 写入缓存
                S("access_token_".$appid, json_encode($data),$time);
            }
        } else {
            $access_token = $data->access_token;
        }
        var_dump($access_token);
        return $access_token;
    }


//  外部调用注入信息

    /**
     *
     */
    public  function  click(){

        // 获取access_token 并缓存
        $access_token = S("access_token");
        if(!$access_token){
            $url ="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=".$this->appid."&secret=".$this->secret;
            $data =   json_decode($this->http_post_data($url));
            $access_token = $data->access_token;
            $this->access_token = $access_token;
            S("access_token",$access_token,7000);
        }

        // 卡券签名
        $cardinfo   = $this->getApiTiket();

        var_dump($cardinfo);
        exit;

        // 获取微信权限
        $signPakage = $this->getSignPackage();

        //输出卡券信息
        $shopInfo = $GLOBALS['shopInfo'];
        $shopInfo['logoUrl'] = "https://".$_SERVER["HTTP_HOST"].$shopInfo['logoUrl'];

        $this->assign("shopInfo",$shopInfo);
        $this->assign("cardinfo",$cardinfo);
        $this->assign("signPakage",$signPakage);

        $this->display();
    }

    //获取卡券签名
    private  function  getApiTiket(){
        $card_res   = $this->createVipCard(); // 会员卡id
        $timestamp  = time();
        $nonceStr   = $this->createNonceStr();
        $apitiket   = $this->get_api_ticket();
        $code       = "qiao1234324235";
        // 这里参数的顺序要按照 key 值 ASCII 码升序排序
        $arr = array($timestamp ,$apitiket,$code,$nonceStr,$card_res->card_id);
        sort($arr, SORT_STRING);
        $signature = sha1(implode($arr));
        $signPackage = array(
            "nonce_str"  => $nonceStr,
            "timestamp" => $timestamp,
            "signature" => $signature,
            "code"      =>  $code,
            "card_id"   => $card_res->card_id,
        );
        return $signPackage;
    }

    //获取微信权限验证配置
    private function getSignPackage() {
        $jsapiTicket = $this->get_jsApi_ticket();
        $timestamp = time();
        $nonceStr = $this->createNonceStr();
        $url = $this->get_url();

        // 这里参数的顺序要按照 key 值 ASCII 码升序排序
        $string = "jsapi_ticket=$jsapiTicket&noncestr=$nonceStr&timestamp=$timestamp&url=$url";

        $signature = sha1($string);

        $signPackage = array(
            "appid"     => $this->appid,
            "noncestr"  => $nonceStr,
            "timestamp" => $timestamp,
            "url"       => $url,
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
    private function get_jsApi_ticket() {
        // jsapi_ticket 应该全局存储与更新，以下代码以写入到文件中做示例
        $appid	= $this->appid;

        $data = json_decode(S("jsapi_ticket_".$appid));

        if ($data->expire_time < time()) {

            // 如果是企业号用以下 URL 获取 ticket
            $url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=".$this->access_token;

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
    private function get_api_ticket() {
        // jsapi_ticket 应该全局存储与更新，以下代码以写入到文件中做示例
        $appid	= $this->appid;

        $data = json_decode(S("api_ticket_".$appid));

        if ($data->expire_time < time()) {

            // 如果是企业号用以下 URL 获取 ticket
            $url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=".$this->access_token."&type=wx_card";

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


    public function login(){

        import("Vendor.WeiXin.WeixinLogin");

        $appid ="wxdacebf540333a98f";
        $secret = "d37c8606678f5faa16ce322b8ead4d03";
        $getLogininfo = new \WeixinLogin($appid,$secret);
        $userinfo =  $getLogininfo->login();
        return $userinfo;
    }







}