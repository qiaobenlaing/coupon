<?php
/**
 * Created by PhpStorm.
 * User: 98041
 * Date: 2019/6/21
 * Time: 9:58
 */

namespace Org\Util;


class WeixinLogin{

    private $appid;				// 公众号APPID
    private $secret;			// 公众号密钥
    private $scope;				// 是否取得用户详细信息

    public function __construct($appid, $secret,$scope = false) {

        // 预设配置参数
        $appid 				= !empty($appid) ? $appid : C("appid");
        $secret 			= !empty($secret) ? $secret : C("secret");

        $this->appid 		= $appid;
        $this->secret 		= $secret;
        $this->scope 		= $scope;
    }

    // 微信调用登陆
    public function login($redirect_url,$zoneId){

        $_SESSION['zoneId'] = $zoneId;

        $appid			= $this->appid;
        $secret			= $this->secret;

        //返回用户信息数组
        $wxinfo		= "";

        //code 获取判断
        $code = trim($_REQUEST['code']);

        if(strlen($code) < 10){

            // 如果没有取到code，执行跳转重新获取
            $url = $this->get_weixin_redirect_url($redirect_url);

            header("Location:".$url);
            exit;
        }


        if(strlen($code) >= 10){

            // 获取openid
            // 拼接获取openidURL地址
            $url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=".$appid;
            $url .= "&secret=".$secret."&code=".$code."&grant_type=authorization_code";

            // 抓取内容
            $json = $this->get_https($url);
            $res = json_decode($json,1);

            // 判断是否获取用户详细信息
            if($this->scope){
                // 取得用户信息
                $url = "https://api.weixin.qq.com/sns/userinfo?access_token=";
                $url .= $res['access_token']."&openid=".$res['openid']."&lang=zh_CN ";

                $json = $this->get_https($url);
                $userinfo = json_decode($json,1);

                // 将头像更换为小头像
                if(strlen($userinfo['headimgurl']) > 10){
                    // 给尾部加个特殊代码，方便替换
                    $pic = $userinfo['headimgurl']."images";
                    $userinfo['headimgurl'] = str_replace(array('/0images','/132images'), "/132",$pic);
                }
            }

            /**
            + 判断用户信息是否获取成功
             */
            if(strlen($res['openid']) > 10){

                // 基本微信信息
                $wxinfo = $res;

                // APPID
                $wxinfo["appid"] = $this->appid;

                // 用户详细信息
                $wxinfo["userinfo"] = $userinfo;

            }else{
                // 如果取二维码失败，就重新跳转一次
                $url = $this->get_weixin_redirect_url($redirect_url);
                header("Location:".$url);
                exit;
            }
        }
            $_SESSION['userinfo'] = $userinfo;
        // 返回获取的用户信息
        return $wxinfo;
    }


    // 取得跳转地址
    private function get_weixin_redirect_url($uri = ""){

        // 全局变量
        $appid			= $this->appid;
        $secret			= $this->secret;
        $scope			= $this->scope;

        // 用户详细信息获取
        if($scope){
            $scope = "snsapi_userinfo";
        }else{
            $scope = "snsapi_base";
        }

        // 生成返回地址
        if(strlen($uri) < 10){
            // 取得当前地址是http 还是 https
            $http = $this->get_is_http();
            $uri = $http.$_SERVER['SERVER_NAME'].$_SERVER['REDIRECT_URL'];
//            $uri ="https://hfq.huift.com.cn/lcy_acctoken.php?appid=".$this->appid."&secret=".$this->secret;
        }

        // 请求URL拼接
        $url  = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=".$appid;
        $url .= "&userCode=".$_SESSION['userCode']."&zoneId=".$_SESSION['zoneId'];
        $url .= "&redirect_uri=".$uri."&response_type=code&scope=".$scope."&state=STATE#wechat_redirect";
        return $url;
    }


    // 取得当前页面是http 还是 https
    private function get_is_http(){

        // 判断当前URL是http还是https
        $bool = $this->is_https();
        if($bool){
            return "https://";
        }else{
            return "http://";
        }
    }


    // 地址判断
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

    // 抓取https内容
    private function get_https($url,$outtime = 15){
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);

        //设置超时
        curl_setopt($ch, CURLOPT_TIMEOUT, $outtime);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);
        curl_setopt($ch, CURLOPT_HEADER, false);
        curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
        curl_setopt($ch, CURLOPT_REFERER, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
        $result = curl_exec($ch);
        curl_close($ch);
        return $result;
    }
}
