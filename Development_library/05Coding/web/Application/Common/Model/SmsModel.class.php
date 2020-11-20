<?php
/**
 * Created by PhpStorm.
 * User: Administrator
 * Date: 15-1-27
 * Time: 下午7:35
 */
namespace Common\Model;
use Think\Exception;
use Org\Net\CurlRequest;
use Common\Model;
use Think\Cache\Driver\Memcache;
use Think\Log;

class SmsModel extends sms{
    private $source = '1234567890'; // 选取验证码的字符串
    private $codeLen = 6; // 验证码长度
    private $timeout = 900; // 有效期900秒

    /**
     * 不设时间限制
     */
    public function noTimeLimit(){
        $this->timeOut = 0;
    }

    /**
     * 设置时间限制，必须为数字，且大于0，单位 秒
     * @param $int
     * @return bool
     */
    public function setTimeOut($int){
        $int = (int)$int;
        if($int > 0){
            $this->timeOut = $int;
            return true;
        }
        return false;
    }

    /**
     * @param string $string 设置随即字符串的字符来源
     */
    public function setSource($string){
        $this->source = $string;
    }

    /**
     * @param string $action 动作
     * @return string 返回一个生成的随即字符串，一般用于验证码
     */
    public function createCode($action) {
        $codeLength = $this->codeLen;
        $code = '';
        for($i = 0; $i < $codeLength; $i++){
            $sourceLength = strlen($this->source);
            $n = mt_rand(1, $sourceLength);
            $n--;
            $code .= substr($this->source, $n, 1);
        }
        $this->saveCode($code, $action);
        return $code;
    }

    public function saveCode($code, $action) {
        $this->delCode($action . 'validateCode');
        S(array(
                'type' => 'memcache',
                'host' => '127.0.0.1',
                'port' => '11211',
                'prefix' => '',
                'expire' => $this->timeout,
            )
        );
        S($action . 'validateCode', $code);
    }

    /**
     * 删除Code
     */
    public function delCode($action){
        S($action, null);
    }

    /**
     * 获取Code
     * @param string $action 动作，表示注册或者找回密码
     * @return number||false
     */
    public function getCode($action){
        $code = S($action.'validateCode');
        if($code){
            return $code;
        }
        return false;
    }

}


class sms {

    private $api = 'http://202.91.244.252/qd/SMSSendYD?';
    private $user = '7025';
    private $passwd = 'xinli7025@hy';

    /** @var $curl CurlRequest */
    public $curl;

    public function __construct(){
        $this->curl = new CurlRequest();
    }
    public function send($msg, $phone, $sendTime = null) {
        Log::write('---------SEND MESSAGE START---------', 'DEBUG', '', C('LOG_PATH').'sendMsg'.date('y_m_d').'.log');
        if(!in_array($_SERVER['HTTP_HOST'], array('web.huiquan.suanzi.cn', 'api.huiquan.suanzi.cn', 'huiquan.suanzi.cn')) && !in_array($phone, array('15868177459', '15827037207', '18667115776', '13965036751', '15868179748', '15259252239'))) {
            return true;
        }

        /** 转换电话号码格式 */
        if(is_array($phone)){
            $tmpPhone = '';
            $i = 1;
            foreach($phone as $v){
                if($i == 1){
                    $tmpPhone = $v;
                }else{
                    $tmpPhone .= ','.$v;
                }
                $i++;
            }
            $phone = $tmpPhone;
        }
        if(!is_string($phone)){
            $phone = (string)$phone;
        }

        if(empty($phone)){
            throw new Exception(C('MOBILENBR.EMPTY'));
        }

        $msg = mb_convert_encoding($msg, 'gbk', 'utf-8');

        $data = array(
            'usr'       => $this->user,
            'pwd'       => $this->passwd,
            'mobile'    => $phone,
            'sms'       => $msg,
            'extdsrcid' => '',
        );
        Log::write("mobile:$phone;msg:$msg", 'DEBUG', '', C('LOG_PATH').'sendMsg'.date('y_m_d').'.log');
        $url = $this->api;
        foreach($data as $k=>$v){
            $v = urlencode($v);
            if($k == 'usr'){
                $url = $url.$k.'='.$v;
            }else{
                $url = $url.'&'.$k.'='.$v;
            }
        }

        //方法 1：
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        $output = curl_exec($ch);
        curl_close($ch);
        // 打印获得的数据
        Log::write('SEND MESSAGE RET:' . json_encode($output), 'DEBUG', '', C('LOG_PATH').'sendMsg'.date('y_m_d').'.log');

        //方法 2：
//        $params = array(
//            'url' => $url,
//            'method' => 'GET',
//            'timeout' => 20,
//            'post_fields' => $data,
//        );
//        $this->curl->init($params);
//        $ch = $this->curl->exec();
//        var_dump($ch);
        Log::write('---------SEND MESSAGE END---------', 'DEBUG', '', C('LOG_PATH').'sendMsg'.date('y_m_d').'.log');
    }

}