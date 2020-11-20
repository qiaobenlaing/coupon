<?php
ini_set('date.timezone','Asia/Shanghai');
error_reporting(E_ERROR);

require_once "../lib/WxPay.Api.php";
require_once '../lib/WxPay.Notify.php';
require_once 'log.php';

//初始化日志
$logHandler= new CLogFileHandler("../logs/".date('Y-m-d').'.log');
$log = Log::Init($logHandler, 15);

class PayNotifyCallBack extends WxPayNotify
{
	//查询订单
	public function Queryorder($transaction_id)
	{
		$input = new WxPayOrderQuery();
		$input->SetTransaction_id($transaction_id);
		$result = WxPayApi::orderQuery($input);
		Log::DEBUG("query:" . json_encode($result));
		if(array_key_exists("return_code", $result)
			&& array_key_exists("result_code", $result)
			&& $result["return_code"] == "SUCCESS"
			&& $result["result_code"] == "SUCCESS")
		{
			return true;
		}
		return false;
	}
	
	//重写回调处理函数
	public function NotifyProcess($data, &$msg)
	{

        if($data['result_code']=='SUCCESS'&&$data['return_code']=='SUCCESS'){
		$consumeCode=$data['attach'];
		$res=array("consumeCode"=>$consumeCode);

		$host_url = $_SERVER['HTTP_HOST'];

		$url="https://".$host_url."/Admin/Lcy/xiugai";

            $this->http_curl($url,'post','json',$res);

			return true;
		}else{
			return false;
		}
	}
	
	
	function http_curl($url,$type='get',$res='json',$arr=''){
        //1.初始化curl
        $ch=curl_init();

        //2.设置curl的参数

        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        if($type=='post'){
            curl_setopt($ch, CURLOPT_POST, 1);
            curl_setopt($ch,CURLOPT_POSTFIELDS,$arr);
        }

        //3.采集
        $output=curl_exec($ch);

        //4.关闭curl
        curl_close($ch);


        return $output;


    }
}

Log::DEBUG("begin notify");
$notify = new PayNotifyCallBack();

$notify->Handle(false);
//$res=array("userCode"=>"7703b19d-759a-3b4f-f4dd-876a71d28680");
//$url="https://hfq.huift.com.cn/Admin/Lcy/xiugai";
//$res=array("consumeCode"=>"6aee6fe2-9db3-b879-01b5-1103f640364d");

 //$notify->http_curl($url,'post','json',$res);
