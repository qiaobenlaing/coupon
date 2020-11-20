<?php
/**
 * Created by PhpStorm.
 * User: Administrator
 * Date: 15-4-22
 * Time: 下午5:10
 */

namespace Home\Controller;

use Common\Model\baiduMapModel;
use Common\Model\SmsModel;
use Common\Model\UserModel;
use Org\Net\CurlRequest;
use Think\Controller;
use Org\FirePHPCore\FP;
use Think\Cache\Driver\Memcache;
use Think\Exception;
Vendor("jsonRPC.jsonRPCServer");
Vendor("jsonRPC.jsonRPCClient");

class UserController extends Controller {


    /**
     * @var $sms SmsModel
     */
    private $sms;

    public function _initialize(){
        $this->sms = new SmsModel();
    }

    //获取验证码
    public function getValidateCode($action){
        try{
            $code = $this->sms->createCode($action);
        }catch (Exception $e){
            $code = $this->sms->getCode($action);
        }
        $phone = I('post.phone');
        try{
            $this->sms->send('【苞米】短信验证码：'.$code.'，有效时间为15分钟。为了保证您的账户安全，请勿向任何人提供您收到的短信验证码。', $phone);
            return json_encode(array(
                'result'=>true,
                'data'=>$code,
            ));
        }catch (Exception $e){
            throw new RPCException($e->getMessage(),40009,'');
        }
    }

    public function index(){

        $cc = new \jsonRPCServer();
        $userMld = new UserModel();
        $cc->handle($userMld);

    }

    public function test(){
        $url = U("/Home/User",null,false,true);
        $client = new \jsonRPCClient($url);
        $params = array(
            'mobileNo'=>'15868177459',
            'validateCode'=>'',
            'password'=>'000000',
            'recommendedCode'=>'',
        );
        try {
            $res = $client->register($params);
            dump($res);
        }catch (Exception $e) {
            echo $e->getMessage();
        }
    }

}

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
