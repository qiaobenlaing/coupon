<?php
namespace Org\Util;
class OAuth{

	 private $appId;
	  private $appSecret;

	public function __construct($appId, $appSecret) {
		$this->appId = $appId;
		$this->appSecret = $appSecret;
	}


//获取用户信息
    public  function userinfo($zoneId){
	        $code=$_REQUEST['code'];
            if(strlen($code) < 10){
			// 跳转微信获得授权
			$redirect_url = "http://".$_SERVER['SERVER_NAME'].$_SERVER['REQUEST_URI'].'?zoneId='.$zoneId;
		   $url=" https://open.weixin.qq.com/connect/oauth2/authorize?appid=".$this->appId."&redirect_uri=".$redirect_url."&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
			header("Location:".$url);
			exit;
	       	}

       // 取得 access_token 和 openid
			$url2 = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=".$this->appId."&secret=".$this->appSecret."&code=".$code."&grant_type=authorization_code";
			$res = json_decode($this->curl_request($url2),1);


       // 取得用户信息
			$url3 = "https://api.weixin.qq.com/sns/userinfo?access_token=".$res['access_token']."&openid=".$res['openid']."&lang=zh_CN ";
			$userInfo = json_decode($this->curl_request($url3),1);
           return $userInfo;
    }


    //获取用户信息
    public  function base_userinfo(){
          $code=$_REQUEST['code'];
            if(strlen($code) < 10){
      // 跳转微信获得授权
      $redirect_url = "http://".$_SERVER['SERVER_NAME'].$_SERVER['REQUEST_URI'];
       $url=" https://open.weixin.qq.com/connect/oauth2/authorize?appid=".$this->appId."&redirect_uri=".$redirect_url."&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
      header("Location:".$url);
      exit;
          }

       // 取得 access_token 和 openid
      $url2 = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=".$this->appId."&secret=".$this->appSecret."&code=".$code."&grant_type=authorization_code";
      $res = json_decode($this->curl_request($url2),1);
           return $res;
    }



    //模拟发送get和post 请求
    private  function curl_request($url, $type = "GET", $data = ''){
            $ch = curl_init();
            curl_setopt($ch,CURLOPT_URL,$url);
            curl_setopt($ch,CURLOPT_RETURNTRANSFER,1);
            curl_setopt($ch,CURLOPT_SSL_VERIFYPEER,0);
            curl_setopt($ch,CURLOPT_SSL_VERIFYHOST,0);
            curl_setopt($ch,CURLOPT_HEADER,0);

            $type = strtolower($type);
            switch ($type){
                case 'get':
                    break;
                case 'post':
                    //post请求配置
                    curl_setopt($ch, CURLOPT_POST,1);
                    curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
                    break;
            }
            $result = curl_exec($ch);
            curl_close($ch);
            return $result;
    }
}
?>
