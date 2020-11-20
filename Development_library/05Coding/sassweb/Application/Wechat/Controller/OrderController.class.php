<?php
namespace Wechat\Controller;
use Common\Model\wxApiModel;
use Common\Model\WechatModel;
use Common\Model\ConsumeOrderModel;
use Common\Model\ShopModel;
use Think\Controller;
use Behavior\BrowserCheckBehavior;
/**
 * Created by PhpStorm.
 * User: yufeng.jiang
 * Date: 15-11-05
 * Time: 下午午18:00
 */
class OrderController extends WechatBaseController {
    /**
     * @var $wxApi wxApiModel
     */
    private $wxApi;

    /**
     * 微信开发者申请的appID
     * @var string
     */
//    private $appid = 'wxc9e9cb8a4460d5ae'; // 测试环境
    private $appid = 'wx8d82faa45f550847'; // 生产环境

    /**
     * 微信开发者申请的appSecret
     * @var string
     */
//    private $appSecret = '6e2c154b30efced9d4951c4cc0cd1dd8'; // 测试环境
   private $appSecret = '5c08733568fcec1cf539e3a894b2e728'; // 生产环境

    public function _initialize(){
        parent::_initialize();
        header("content-type:text/html;charset=utf-8");
        $this->wxApi = new wxApiModel();
    }
    
    /**
     * 获取微信授权链接
     *
     * @param string $redirect_uri 跳转地址
     * @param mixed $state 参数
     */
    public function get_authorize_url($redirect_uri = '', $state = '1')
    {
        $redirect_uri = urlencode($redirect_uri);
        return "https://open.weixin.qq.com/connect/oauth2/authorize?appid={$this->appid}&redirect_uri={$redirect_uri}&response_type=code&scope=snsapi_userinfo&state={$state}#wechat_redirect";
    }
    
    /**
     * 获取授权token
     *
     * @param string $code 通过get_authorize_url获取到的code
     */
    public function get_access_token($app_id = '', $app_secret = '', $code = '')
    {
        $token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={$app_id}&secret={$app_secret}&code={$code}&grant_type=authorization_code";
        $token_data = $this->http($token_url);
        if($token_data[0] == 200)
        {
			
          return json_decode($token_data[1], TRUE);
        }
    
        return FALSE;
    }
    
    /**
     * 获取授权后的微信用户信息
     *
     * @param string $access_token
     * @param string $open_id
     */
    public function get_user_info($access_token = '', $open_id = '')
    {
        if($access_token && $open_id)
        {
            $info_url = "https://api.weixin.qq.com/sns/userinfo?access_token={$access_token}&openid={$open_id}&lang=zh_CN";
            $info_data = $this->http($info_url);
    
            if($info_data[0] == 200)
            {
                return json_decode($info_data[1], TRUE);
            }
        }
    
        return FALSE;
    }
    /**
     * 验证授权
     *
     * @param string $access_token
     * @param string $open_id
     */
    public function check_access_token($access_token = '', $open_id = '')
    {
        if($access_token && $open_id)
        {
            $info_url = "https://api.weixin.qq.com/sns/auth?access_token={$access_token}&openid={$open_id}&lang=zh_CN";
            $info_data = $this->http($info_url);
    
            if($info_data[0] == 200)
            {
                return json_decode($info_data[1], TRUE);
            }
        }
    
        return FALSE;
    }
    //curl
    public function http($url, $method = '', $postfields = null, $headers = array(), $debug = false)
    {
        $ci = curl_init();
        /* Curl settings */
        curl_setopt($ci, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
        curl_setopt($ci, CURLOPT_CONNECTTIMEOUT, 30);
        curl_setopt($ci, CURLOPT_TIMEOUT, 30);
        curl_setopt($ci, CURLOPT_RETURNTRANSFER, true);
    
        switch ($method) {
            case 'POST':
                curl_setopt($ci, CURLOPT_POST, true);
                if (!empty($postfields)) {
                    curl_setopt($ci, CURLOPT_POSTFIELDS, $postfields);
                    $this->postdata = $postfields;
                }
                break;
        }
        curl_setopt($ci, CURLOPT_URL, $url);
        curl_setopt($ci, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ci, CURLINFO_HEADER_OUT, true);
    
        $response = curl_exec($ci);
        $http_code = curl_getinfo($ci, CURLINFO_HTTP_CODE);
    
        if ($debug) {
            echo "=====post data======\r\n";
            var_dump($postfields);
    
            echo '=====info=====' . "\r\n";
            print_r(curl_getinfo($ci));
    
            echo '=====$response=====' . "\r\n";
            print_r($response);
        }
        curl_close($ci);
        return array($http_code, $response);
    }
    
    public function GrantAuth($shopCode){
        $token = session('token'); //查看是否已经授权
        if (!empty($token)) {
            $state = $this->check_access_token($token['access_token'],$token['openid']); //检验token是否可用(成功的信息："errcode":0,"errmsg":"ok")
        }
        if(in_array($_SERVER['HTTP_HOST'], array('api.huiquan.suanzi.cn', 'web.huiquan.suanzi.cn', 'huiquan.suanzi.cn'))) {
            $url="http://api.huiquan.suanzi.cn/Wechat/Order/wxrurl?shopCode=".$shopCode;
        } elseif($_SERVER['HTTP_HOST'] == 'api.test.huiquan.suanzi.cn:1080') {
            $url="http://api.test.huiquan.suanzi.cn:1080/Wechat/Order/wxrurl?shopCode=".$shopCode;
        }else {
            $url="http://baomi.suanzi.cn/Wechat/Order/wxrurl?shopCode=".$shopCode;
        }
        $code=$this->get_authorize_url($url,'1');
        redirect("{$code}");
    }
    
    //微信返回字符串
    public function wxrurl(){
        $token = $this->get_access_token($this->appid, $this->appSecret, $_GET['code']); //确认授权后会，根据返回的code获取token
        session('token',$token); //保存授权信息
        $user_info = $this->get_user_info($token['access_token'],$token['openid']); //获取用户信息
        $consumeOrderMdl = new ConsumeOrderModel();
        $result = $consumeOrderMdl->isConsumeOrder(array('Shop.shopCode' => $_GET['shopCode'], 'wechatId' => $token['openid']));
        $shopMdl = new ShopModel();
        // 获得商家信息（工行入账的商户号，工行入账的商户名称）
        $shopInfo = $shopMdl->getShopInfo($_GET['shopCode'], array('Shop.*'));
        if($shopInfo['eatPayType'] == C('EAT_PAY_TYPE.AFTER')){     
            if(empty($result)){
                if(in_array($_SERVER['HTTP_HOST'], array('api.huiquan.suanzi.cn', 'web.huiquan.suanzi.cn', 'huiquan.suanzi.cn'))) {
                    redirect('http://api.huiquan.suanzi.cn/Api/Browser/getShopInfo?shopCode='.$_GET['shopCode']."&openId=".$token['openid']);
                } elseif($_SERVER['HTTP_HOST'] == 'api.test.huiquan.suanzi.cn:1080') {
                    redirect('http://api.test.huiquan.suanzi.cn:1080/Api/Browser/getShopInfo?shopCode='.$_GET['shopCode']."&openId=".$token['openid']);
                }else {
                    redirect('http://baomi.suanzi.cn/Api/Browser/getShopInfo?shopCode='.$_GET['shopCode']."&openId=".$token['openid']);
                }                                  
            }else{            
                if(in_array($_SERVER['HTTP_HOST'], array('api.huiquan.suanzi.cn', 'web.huiquan.suanzi.cn', 'huiquan.suanzi.cn'))) {
                    redirect('http://api.huiquan.suanzi.cn/Api/Browser/cGetOrder?shopCode='.$_GET['shopCode']."&openId=".$token['openid']."&userCode=".$result['clientCode']);
                } elseif($_SERVER['HTTP_HOST'] == 'api.test.huiquan.suanzi.cn:1080') {
                    redirect('http://api.test.huiquan.suanzi.cn:1080/Api/Browser/cGetOrder?shopCode='.$_GET['shopCode']."&openId=".$token['openid']."&userCode=".$result['clientCode']);
                }else {
                    redirect('http://baomi.suanzi.cn/Api/Browser/cGetOrder?shopCode='.$_GET['shopCode']."&openId=".$token['openid']."&userCode=".$result['clientCode']);
                }
            }
        }else{
           redirect('http://api.huiquan.suanzi.cn/Wechat/Index/cdownload');
        }
    }
    
}

