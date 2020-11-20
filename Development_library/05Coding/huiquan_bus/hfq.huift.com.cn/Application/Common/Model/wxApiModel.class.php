<?php
/**
 * Created by PhpStorm.
 * User: Administrator
 * Date: 15-1-27
 * Time: 下午7:35
 */

namespace Common\Model;
use Com\Wechat;
use Org\Net\CurlRequest;
use Think\Exception;

//define(APPID , "wx156ca5fcb9ebc520");  //appid
//define(APPKEY , "253f9bf5057804c529034a918ff6b98c"); //paysign key
//define(SIGNTYPE, "sha1"); //method
//define(PARTNERKEY, "");//通加密串
//define(APPSERCERT, "253f9bf5057804c529034a918ff6b98c");
define(APPID , "wx8d82faa45f550847");  //appid
define(APPKEY , "5c08733568fcec1cf539e3a894b2e728"); //paysign key
define(SIGNTYPE, "sha1"); //method
define(PARTNERKEY, "");//通加密串
define(APPSERCERT, "5c08733568fcec1cf539e3a894b2e728");


class wxApiModel {

    private $appId;
    private $openId;
    private $secret;
    private $accessToken;

    public function __construct($appId="", $secret=""){
        $this->appId = $appId;
        $this->secret = $secret;
        if(empty($this->appId)){
            $this->appId = APPID;
        }
        if(empty($this->secret)){
            $this->secret = APPSERCERT;
        }
    }

    public function getCode(){
        $url = $this->createOauthUrlForCode(U('/', array(), false, true) . substr(__SELF__, 1));
        $code = I('get.code');
        if(empty($code)){
            //触发微信返回code码
            Header("Location: $url");
            exit;
        }else{

            return $code;
        }
    }

    public function init(){
        $code = $this->getCode();
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, "https://api.weixin.qq.com/sns/oauth2/access_token?appid={$this->appId}&secret=".APPSERCERT."&code=$code&grant_type=authorization_code");
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_HEADER, 0);
        $arr = json_decode(curl_exec($ch));
        // dump($getOpenid);
        curl_close($ch);

        if(isset($arr->openid)){
            $this->setOpenId($arr->openid);
        }
        if(isset($arr->access_token)){
            $this->setAccessToken($arr->access_token);
        }
    }

    public function setOpenId($value){
        $this->openId = $value;
    }

    public function getOpenId($code = false){
        if($code !== false){
            $ch = curl_init();
            curl_setopt($ch, CURLOPT_URL, "https://api.weixin.qq.com/sns/oauth2/access_token?appid={$this->appId}&secret=".APPSERCERT."&code=$code&grant_type=authorization_code");
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
            curl_setopt($ch, CURLOPT_HEADER, 0);
            $getOpenid = json_decode(curl_exec($ch));
            // dump($getOpenid);
            curl_close($ch);
            $this->openId = $getOpenid;
        }

        return $this->openId;
    }

    public function setAccessToken($value){
        $this->accessToken = $value;
    }

    public function getAccessToken(){
        if(empty($this->accessToken)){
            $curl = new CurlRequest();
            $params_token = array('url' => "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=$this->appId&secret=$this->secret",
                'method' => 'GET', // 'POST','GET'
                'timeout' => 20
            );
            $curl->init($params_token);
            $result = $curl->exec();

            $ACC_TOKEN = json_decode($result['body']);
            $this->accessToken = $ACC_TOKEN->access_token;
        }

        return $this->accessToken;
    }

    public function getUserInfo(){
        if(empty($this->openId)){
            throw new Exception('OpenId has error');
        }
        $this->getAccessToken();

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, "https://api.weixin.qq.com/sns/userinfo?access_token=$this->accessToken&openid=$this->openId&lang=zh_CN");
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_HEADER, 0);
        $getInformation = json_decode(curl_exec($ch));
        curl_close($ch);

        return $getInformation;
    }

    public function getSignPackage() {
        $jsapiTicket = $this->getJsApiTicket();
        $protocol = (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off' || $_SERVER['SERVER_PORT'] == 443) ? "https://" : "http://";
        $url = "$protocol$_SERVER[HTTP_HOST]$_SERVER[REQUEST_URI]";
        $timestamp = time();
        $nonceStr = $this->createNonceStr();

        // 这里参数的顺序要按照 key 值 ASCII 码升序排序
        $string = "jsapi_ticket=$jsapiTicket&noncestr=$nonceStr&timestamp=$timestamp&url=$url";
        $signature = sha1($string);

        $signPackage = array(
            "appId"     => $this->appId,
            "nonceStr"  => $nonceStr,
            "timestamp" => $timestamp,
            "url"       => $url,
            "signature" => $signature,
            "rawString" => $string
        );
        return $signPackage;
    }

    private function createNonceStr($length = 16) {
        $chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        $str = "";
        for ($i = 0; $i < $length; $i++) {
            $str .= substr($chars, mt_rand(0, strlen($chars) - 1), 1);
        }
        return $str;
    }

    private function getJsApiTicket() {
        $curl = new CurlRequest();
        $accessToken = $this->getAccessToken();
        $params_ticket = array('url' => 'https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token='.$accessToken.'&type=jsapi',
            'method' => 'GET', // 'POST','GET'
            'timeout' => 20
        );
        $curl->init($params_ticket);
        $result = $curl->exec();
        $JS_ticket = json_decode($result['body']);
        $ticket = $JS_ticket->ticket;

        return $ticket;
    }

    function formatBizQueryParaMap($paraMap, $urlencode)
    {
        $buff = "";
        ksort($paraMap);
        foreach ($paraMap as $k => $v)
        {
            if($urlencode)
            {
                $v = urlencode($v);
            }
            //$buff .= strtolower($k) . "=" . $v . "&";
            $buff .= $k . "=" . $v . "&";
        }
        $reqPar = null;
        if (strlen($buff) > 0)
        {
            $reqPar = substr($buff, 0, strlen($buff)-1);
        }
        return $reqPar;
    }

    function createOauthUrlForCode($redirectUrl)
    {
        $urlObj["appid"] = APPID;
        $urlObj["redirect_uri"] = "$redirectUrl";
        $urlObj["response_type"] = "code";
        $urlObj["scope"] = "snsapi_base";
        $urlObj["state"] = "STATE"."#wechat_redirect";
        $bizString = $this->formatBizQueryParaMap($urlObj, false);
        return "https://open.weixin.qq.com/connect/oauth2/authorize?".$bizString;
    }
    
    /**
     * WEB Api 通过redirect_uri获取微信授权链接
     * @param $redirect_uri
     */
    public function getAuthorizeUrl($redirect_uri, $state = null, $scope = null) {
        $wechatAuth = new WechatAuth($this->appId, $this->secret, $this->getAccessToken());
        $result = $wechatAuth->getRequestCodeURL($redirect_uri, $state, $scope);
        return $result;
    }
}