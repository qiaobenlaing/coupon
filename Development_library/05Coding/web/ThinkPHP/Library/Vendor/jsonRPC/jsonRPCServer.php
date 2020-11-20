<?php
/*
					COPYRIGHT

Copyright 2007 Sergio Vaccaro <sergio@inservibile.org>

This file is part of JSON-RPC PHP.

JSON-RPC PHP is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

JSON-RPC PHP is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with JSON-RPC PHP; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/

/**
 * This class build a json-RPC Server 1.0
 * http://json-rpc.org/wiki/specification
 *
 * @author sergio <jsonrpcphp@inservibile.org>
 */
class jsonRPCServer {

    protected static $_no_check_method = array(
        'register', // 注册
        'login', // 登录
        'logoff', // 登出
        'findPwd', // 找回密码
        'getHomeInfo', // 获取首页各模块
        'getHomeShopList', // 获得首页的商家列表
        'getValCode', // register (H5)
        'getValidateCode', // 顾客和商家请求验证码
        'getScrollInfo', // 获取首页的滚动活动信息
        'listCoupon', // 用于首页获得可领取的优惠券或者搜索优惠券
        'searchShop', // 搜索商店
        'getShopInfo', // 商家详情
        'getActivityList', // 获取活动列表，包括工行活动，平台活动，商家活动
        'getActivityInfo', // 获得活动详情
        'listOpenCity', // 获得已经开通的城市
        'getPlateBonus', // 获得平台发行的距离当前时间最近的红包信息
        'listZhejiangCity', // 获得浙江省的所有城市
        'addRegId',
        'searchCard', //
        'getNewestShopAppVersion', // 获得最新商家端的APP版本
        'getNewestClientAppVersion', // 获得最新客户端的APP版本
        'activate', // 商家员工激活
        'listActModule', // 首页的两个活动模块
        'cGetShopProductAlbum', // 获得产品相册
        'cGetShopDecoration', // 获得商家环境图片信息
        'cGetSubAlbumPhoto', // 获得子相册的图片
        'getShopBankCardDiscount', // 获得商家对工行银行卡的打折力度和抵扣金额上限
        'addCrashLog', // 添加崩溃日志
        'getSystemParam', // 获得系统参数
        'getNewestPcAppVersion', // 获得最新PC端的APP版本
        'getClientHomePage', // 获得顾客端首页模块和商家类型
        'getAct', // 获得活动
        'editIosToken', // IOS记录应用的token
        'applyEntry', // 商家提交入驻申请
        'listSearchWords', // 得到查询子列表
        'errorInfo', //返回错误信息
        'getBatchCouponInfo', //获取某一批次优惠券详情
        'getServerTime', // 获取服务器时间
        'getActList', //圈广场的活动列表
        'getActType', //活动类型
        'getActRefundChoice', // 获得活动退款的选择
        'recordUserAddress', // 记录用户每次打开App的位置
        'getHomeTabList', // 获取首页选项卡列表
        'getGuideInfo', // 获取app引导页图片
        'ifShowRecommend', // 是否展示推荐好友得豪礼
        'listShopTeacher', // 顾客端获得名师列表
    );

	/**
	 * This function handle a request binding it to a given object
	 *
	 * @param object $object
	 * @return boolean
	 */
	public static function handle($object) {
        Think\Log::write('==================start jsonRPCServer==================', 'DEBUG');
		// checks if a JSON-RCP request has been received
		if ($_SERVER['REQUEST_METHOD'] != 'POST' || empty($_SERVER['CONTENT_TYPE']) || $_SERVER['CONTENT_TYPE'] != 'application/json') {    // This is not a JSON-RPC request
            exit('This is not a JSON-RPC request');
            return false;
		}
		// reads the input data
		$request = json_decode(file_get_contents('php://input'), true);
        Think\Log::write('request:'.json_encode($request), 'DEBUG');
		// executes the task on local object
        try {
            $method = $request['method'];
            Think\Log::write('request method:' . $method, 'DEBUG');
            Think\Log::write('method exists:' . json_encode(method_exists($object , $method)), 'DEBUG');
            // 给调用的方法的参数赋值
            $function = new ReflectionMethod($object, $method);
            $methodParams = $function->getParameters();
            $params = self::setMethodParam($request['params'], $methodParams);
            Think\Log::write('method params:'.json_encode($params), 'DEBUG');
            $request['methodParams'] = $params ? $params : '';
            //method check
            $token_check = false;
            if ( !method_exists($object , $method) ) {
                throw new RPCException("Invalid Method" , C('INVALID_METHOD') , "");
            }
            Think\Log::write('method in not check token array:'.json_encode(in_array($method, self::$_no_check_method)), 'DEBUG');
            if (in_array($method, self::$_no_check_method)) {
                $token_check = true;
            } else {
                $token_check = self::_token_check($request);
                unset($request['params']["reqtime"]);
                unset($request['params']["vcode"]);
            }
            Think\Log::write('token check result:' . json_encode($token_check), 'DEBUG');
            // check token
            if( $token_check !== true){
                throw new RPCException("Security Error" , $token_check , "");
            }

            $result = @call_user_func_array(array($object, $method), $params);
            if (! isset($result)) { //结果返回异常
                throw new RPCException("Internal Error" , C('INTERNAL_ERROR') , "");
            }
            $response = array (
                'id' => $request['id'],
                'result' => $result
            );
        } catch (RPCException $e) {
            $response = array (
                'id' => $request['id'],
                //'result' => NULL,
                'error' => array(
                    'code'=> $e->code,
                    'message'=>$e->message,
                ));
        }
        // output the response
        if (!empty($request['id'])) { // notifications don't want response
            header('content-type: application/json');
            $response['jsonrpc'] = '2.0';
            Think\Log::write('response:' . json_encode($response), 'DEBUG');
            echo json_encode($response);
        }
        Think\Log::write('===================end jsonRPCServer==================', 'DEBUG');
		return true;
	}

    /**
     * token验证
     * @param array $request 请求API传入的参数。例 {"jsonrpc"=>"2.0","method"=>"login","params"=>{"mobileNbr"=>"15868179748","password"=>"e10adc3949ba59abbe56e057f20f883e","loginType"=>"0","reqtime"=>1436324601,"vcode"=>"5e7206c236a64ce8b4a6edebdd5fbcd2a3b166256f22bc66763f8a63b466c38f"},"id"=>1}
     * @return boolean||int 验证通过返回true,验证不通过返回错误代码
     */
    private static function _token_check($request) {
        $firstParam = self::getFirstParam($request['methodParams']);
        //得到发过来的vcode和reqtime
        $params = array(
            'requestTime' => $request['params']["reqtime"],
            'vcode'       => $request['params']["vcode"],
            'method'      => $request['method'],
            'param'       => $firstParam
        );
        $object = new \Common\Model\UserTokenModel();
        $result = @call_user_func_array(array($object, 'validateToken'), $params);
        return $result == C('SUCCESS') ? true : $result;
    }

    /**
     * 获得数组中第一个元素的值，当值不为数组时，返回。
     * @param array $params
     * @return string $firstParam
     */
    private static function getFirstParam($params) {
        // 获得params中所有键名的一个新数组
        $paramsKeyArray = array_keys($params);
        // 获得params数组中第一个参数的值
        $firstParam = $params[$paramsKeyArray[0]];
        if(is_array($firstParam)) {
            return self::getFirstParam($firstParam);
        }else{
            return $firstParam;
        }
    }

    /**
     * 给要调用的方法的参数设置值
     * @param array $requestParams 请求API，传进来的参数
     * @param array $methodParams 要调用的方法的参数。一维数组，每个元素的类型是对象
     * @return array $params 一维数组
     */
    private static function setMethodParam($requestParams, $methodParams) {
        $params = array();
        foreach($methodParams as $v) {
            $params[] = $requestParams[$v->name];
        }
        return $params;
    }

}

if(class_exists('RPCException') != true)
{
    class RPCException extends Exception{
        public $code = 0;
        public $message = "";
        public $data = "";

        public function __construct($msg, $code, $data){
            $this->code = $code;
            $this->message = $msg;
            $this->data = $data;
        }
    }
}
