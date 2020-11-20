<?php
ini_set('date.timezone','Asia/Shanghai');
error_reporting(E_ERROR);
require_once "/usr/local/nginx/html/hfq.huift.com.cn/huiquan/lib/WxPay.Api.php";

function printf_info($data)
{
    foreach($data as $key=>$value){
        echo "<font color='#f00;'>$key</font> : $value <br/>";
    }
}
$refund_fee=$GLOBALS['refund_fee'];
$total_fee=$GLOBALS['total_fee'];
$out_trade_no=$GLOBALS['out_trade_no'];

//$out_trade_no= "18052700111805294152";
//$total_fee= '1';
//$refund_fee = 1;

	$input = new WxPayRefund();
	$input->SetOut_trade_no($out_trade_no);
	$input->SetTotal_fee($total_fee);
	$input->SetRefund_fee($refund_fee);
    $input->SetOut_refund_no(WxPayConfig::MCHID.date("YmdHis"));
    $input->SetOp_user_id(WxPayConfig::MCHID);
$weixinInfo=WxPayApi::refund($input);
return $weixinInfo;

?>

