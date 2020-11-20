<?php
/**
 * Created by PhpStorm.
 * User: Administrator
 * Date: 16-3-17
 * Time: 下午6:06
 */

namespace Home\Controller;


use Think\Controller;

class FlowController extends Controller {
    private $appKey = 'b7f3e1ca1ae0a03fe000ed44709134bd';

    /**
     * 架构函数
     * @access public
     */
    public function __construct() {
        parent::__construct();
        header('Content-type:text/html;charset=utf-8');
        //导入类库
    }

    /**
     * 请求接口返回内容
     * @param string $url [请求的URL地址]
     * @param string $params [请求的参数]
     * @param int $ispost [是否采用POST形式]
     * @return string
     */
    private function juhecurl($url, $params = false, $ispost = 0){
        $httpInfo = array();
        $ch = curl_init();

        curl_setopt( $ch, CURLOPT_HTTP_VERSION , CURL_HTTP_VERSION_1_1 );
        curl_setopt( $ch, CURLOPT_USERAGENT , 'JuheData' );
        curl_setopt( $ch, CURLOPT_CONNECTTIMEOUT , 60 );
        curl_setopt( $ch, CURLOPT_TIMEOUT , 60);
        curl_setopt( $ch, CURLOPT_RETURNTRANSFER , true );
        curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
        if( $ispost ) {
            curl_setopt( $ch , CURLOPT_POST , true );
            curl_setopt( $ch , CURLOPT_POSTFIELDS , $params );
            curl_setopt( $ch , CURLOPT_URL , $url );
        } else {
            if($params){
                curl_setopt( $ch , CURLOPT_URL , $url.'?'.$params );
            }else{
                curl_setopt( $ch , CURLOPT_URL , $url);
            }
        }
        $response = curl_exec( $ch );
        if ($response === FALSE) {
            //echo "cURL Error: " . curl_error($ch);
            return false;
        }
        $httpCode = curl_getinfo( $ch , CURLINFO_HTTP_CODE );
        $httpInfo = array_merge( $httpInfo , curl_getinfo( $ch ) );
        curl_close( $ch );
        return $response;
    }

    //************1.全部流量套餐列表************
    public function getAllFlowList(){
        $url = "http://v.juhe.cn/flow/list";
        $params['key'] = $this->appKey;
        $paramString = http_build_query($params);
        $content = $this->juhecurl($url, $paramString);
        $result = json_decode($content, true);
        if($result){
            if($result['error_code'] == '0'){
                return $result;
            }else{
                return array(
                    'errorCode' => $result['error_code'],
                    'reason' => $result['reason']
                );
            }
        }else{
            return false; //请求失败
        }
    }

    //************2.检测号码支持的流量套餐************
    public function getSupportFlowList($mobileNbr){
        $url = 'http://v.juhe.cn/flow/telcheck';
        $params['phone'] = $mobileNbr;
        $params['key'] = $this->appKey;
        $paramString = http_build_query($params);
        $content = $this->juhecurl($url, $paramString);
        $result = json_decode($content, true);
        if($result){
            if($result['error_code'] == '0'){
                return $result;
            }else{
                return array(
                    'errorCode' => $result['error_code'],
                    'reason' => $result['reason']
                );
            }
        }else{
            return false; //请求失败
        }
    }

    //************3.提交流量充值************
    public function submit($mobileNbr, $pId, $orderNbr){
        $url = "http://v.juhe.cn/flow/recharge";
        $params = array(
            "phone" => $mobileNbr, //需要充值流量的手机号码
            "pid" => $pId, //流量套餐ID
            "orderid" => $orderNbr, //自定义订单号，8-32字母数字组合
            "key" => $this->appKey,
            "sign" => "", //校验值，md5(<b>OpenID</b>+key+phone+pid+orderid)，结果转为小写
        );
        $paramString = http_build_query($params);
        $content = $this->juhecurl($url, $paramString);
        $result = json_decode($content, true);
        if($result){
            if($result['error_code'] == '0'){
                return $result;
            }else{
                return array(
                    'errorCode' => $result['error_code'],
                    'reason' => $result['reason']
                );
            }
        }else{
            return false; //请求失败
        }
    }

    //************4.订单状态查询************
    public function getOrderInfo($orderNbr){
        $url = "http://v.juhe.cn/flow/batchquery";
        $params = array(
            "orderid" => $orderNbr, //用户订单号，多个以英文逗号隔开，最大支持50组
            "key" => $this->appKey,
        );
        $paramString = http_build_query($params);
        $content = $this->juhecurl($url, $paramString);
        $result = json_decode($content, true);
        if($result){
            if($result['error_code'] == '0'){
                return $result;
            }else{
                return array(
                    'errorCode' => $result['error_code'],
                    'reason' => $result['reason']
                );
            }
        }else{
            return false; //请求失败
        }
    }

    //************5.充值订单列表************
    public function getOrderList($mobileNbr, $page){
        $url = "http://v.juhe.cn/flow/orderlist";
        $params = array(
            "pagesize" => \Consts::PAGESIZE, //每页返回条数，最大200，默认50
            "page" => $page, //页数，默认1
            "phone" => $mobileNbr, //指定要查询的手机号码
            "key" => $this->appKey,
        );
        $paramString = http_build_query($params);
        $content = $this->juhecurl($url, $paramString);
        $result = json_decode($content, true);
        if($result){
            if($result['error_code'] == '0'){
                return $result;
            }else{
                return array(
                    'errorCode' => $result['error_code'],
                    'reason' => $result['reason']
                );
            }
        }else{
            return false; //请求失败
        }
    }

    //************6.运营商状态查询************
    public function getOperatorStatus(){
        $url = "http://v.juhe.cn/flow/operatorstate";
        $params = array(
            "key" => $this->appKey,
        );
        $paramString = http_build_query($params);
        $content = $this->juhecurl($url, $paramString);
        $result = json_decode($content, true);
        if($result){
            if($result['error_code'] == '0'){
                return $result;
            }else{
                return array(
                    'errorCode' => $result['error_code'],
                    'reason' => $result['reason']
                );
            }
        }else{
            return false; //请求失败
        }
    }

} 