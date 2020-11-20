<?php
namespace Vendor\WeixinPay;
//加载核心处理类库文件
require_once "lib/WxPay.Api.php";

//消息返回处理类
class WeixinNotify
{
	//取得微信支付验证成功后的结果
	public function getResult(){
//		$xml = $GLOBALS['HTTP_RAW_POST_DATA'];
		$xml = file_get_contents('php://input');
		$result = \WxPayResults::Init($xml);
		if($result["return_code"] == "SUCCESS"){
			return $result;
		}
	}

	//订单信息查询
	public function Queryorder($transaction_id){

		$input = new \WxPayOrderQuery();
		$input->SetTransaction_id($transaction_id);
		$result = \WxPayApi::orderQuery($input);
		if(array_key_exists("return_code", $result)
			&& array_key_exists("result_code", $result)
			&& $result["return_code"] == "SUCCESS"
			&& $result["result_code"] == "SUCCESS")
		{
			return true;
		}
		return false;
	}

	//取得返回信息
	public function getReply($bool = false){

		$return_code = "FAIL";
		$return_msg = "error";

		if($bool){
			$return_code = "SUCCESS";
			$return_msg = "OK";
		}

		$xml = '<xml>
					<return_code><![CDATA['.$return_code.']]></return_code>
					<return_msg><![CDATA['.$return_msg.']]></return_msg>
				</xml>';

		return $xml;
	}
}

?>
