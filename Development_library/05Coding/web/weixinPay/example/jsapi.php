<?php 
ini_set('date.timezone','Asia/Shanghai');
//error_reporting(E_ERROR);
require_once "../lib/WxPay.Api.php";
require_once "WxPay.JsApiPay.php";
require_once 'log.php';

//初始化日志
$logHandler= new CLogFileHandler("../logs/".date('Y-m-d').'.log');
$log = Log::Init($logHandler, 15);

//打印输出数组信息
function printf_info($data)
{
    foreach($data as $key=>$value)
	{
        echo "<font color='#00ff55;'>$key</font> : $value <br/>";
    }
}
$userName = $_GET["userName"];
$userSex  = $_GET["userSex"];
if($userName==null){
    $userName = "名字";
}
if($userSex==null){
    $userName = "性别";
}

$host_url = $_SERVER['HTTP_HOST'];

/** 下面是微信提供的测试支付回调地址，如果你自己有服务器，请修改为你的 */
$payCallBack = "https://".$host_url."/weixinPay/example/notify.php";

//①、获取用户openid
$tools = new JsApiPay();
$openId = $tools->GetOpenid($userName,$userSex); /** 页面重定向 */

//②、统一下单
$input = new WxPayUnifiedOrder();
/** SetBody 设置商品或支付单简要描述 */
$input->SetBody("test");
/** SetAttach 设置附加数据，在查询API和支付通知中原样返回 */
$input->SetAttach("test");
/** SetOut_trade_no 设置商户系统内部的订单号,32个字符内、可包含字母 */
$input->SetOut_trade_no(WxPayConfig::MCHID.date("YmdHis"));
/** SetTotal_fee 设置订单总金额，单位为分，只能为整数 */
$input->SetTotal_fee("1");
/** SetTime_start 设置订单生成时间，格式为yyyyMMddHHmmss */
$input->SetTime_start(date("YmdHis"));
/** SetTime_expire 设置订单失效时间，格式为yyyyMMddHHmmss */
$input->SetTime_expire(date("YmdHis", time() + 600));
/** SetGoods_tag 设置商品标记，代金券或立减优惠功能的参数 */
$input->SetGoods_tag("test");
/** SetNotify_url 设置接收微信支付异步通知回调地址 */
$input->SetNotify_url($payCallBack);
/** SetTrade_type 设置支付方式，设置取值如下：JSAPI，NATIVE，APP,分别是网页、扫码、APP,刷卡不在这 */
$input->SetTrade_type("JSAPI");
/** SetOpenid 设置我们获取的用户OpenId */
$input->SetOpenid($openId);
/** 进行支付 */
$order = WxPayApi::unifiedOrder($input);
echo '<font color="#f00"><b>统一下单支付单信息</b></font><br/>';
printf_info($order);
$jsApiParameters = $tools->GetJsApiParameters($order);

//获取共享收货地址js函数参数
$editAddress = $tools->GetEditAddressParameters();

//③、在支持成功回调通知中处理成功之后的事宜，见 notify.php
/**
 * 注意：
 * 1、当你的回调地址不可访问的时候，回调通知会失败，可以通过查询订单来确认支付是否成功
 * 2、jsapi支付时需要填入用户openid，WxPay.JsApiPay.php中有获取openid流程 （文档可以参考微信公众平台“网页授权接口”，
 * 参考http://mp.weixin.qq.com/wiki/17/c0f37d5704f0b64713d5d2c37b468d75.html）
 */
?>

<html>
<head>
    <meta http-equiv="content-type" content="text/html;charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/> 
    <title>微信支付样例-支付</title>
    <script type="text/javascript">
	//调用微信JS api 支付
	function jsApiCall()
	{
		WeixinJSBridge.invoke(
			'getBrandWCPayRequest',
			<?php echo $jsApiParameters; ?>,
			function(res){
				WeixinJSBridge.log(res.err_msg);
				//alert(res.err_code+res.err_desc+res.err_msg);
                if(res.err_msg == "get_brand_wcpay_request：ok" ) {
                    alert("支付成功");
                    // 支付成功
                }else if((res.err_msg =="get_brand_wcpay_request：fail"){
                    alert("支付失败");
                    // 支付失败
                }else if((res.err_msg =="get_brand_wcpay_request：cancel"){
                    alert("支付过程中用户取消");
                    // 支付过程中用户取消
                }
			}
		);
	}

	function callpay()
	{
		if (typeof WeixinJSBridge == "undefined"){
		    if( document.addEventListener ){
		        document.addEventListener('WeixinJSBridgeReady', jsApiCall, false);
		    }else if (document.attachEvent){
		        document.attachEvent('WeixinJSBridgeReady', jsApiCall); 
		        document.attachEvent('onWeixinJSBridgeReady', jsApiCall);
		    }
		}else{
		    jsApiCall();
		}
	}
	</script>
	<script type="text/javascript">
	//获取共享地址
	function editAddress()
	{
		WeixinJSBridge.invoke(
			'editAddress',
			<?php echo $editAddress; ?>,
			function(res){
				var value1 = res.proviceFirstStageName;
				var value2 = res.addressCitySecondStageName;
				var value3 = res.addressCountiesThirdStageName;
				var value4 = res.addressDetailInfo;
				var tel = res.telNumber;
				
				alert(value1 + value2 + value3 + value4 + ":" + tel);
			}
		);
	}
	
	window.onload = function(){
		if (typeof WeixinJSBridge == "undefined"){
		    if( document.addEventListener ){
		        document.addEventListener('WeixinJSBridgeReady', editAddress, false);
		    }else if (document.attachEvent){
		        document.attachEvent('WeixinJSBridgeReady', editAddress); 
		        document.attachEvent('onWeixinJSBridgeReady', editAddress);
		    }
		}else{
			editAddress(); /** 注释后，将不会获取用户的地址 */
		}
	};
	
	</script>
</head>
<body>
    <br/>
    <font color="#9ACD32"><b>该笔订单支付金额为<span style="color:#f00;font-size:50px">1分</span>钱</b></font><br/><br/>
	<div align="center">
		<button style="width:210px; height:50px; border-radius: 15px;background-color:#FE6714; border:0px #FE6714 solid; cursor: pointer;  color:white;  font-size:16px;" type="button" onclick="callpay()" >立即支付</button>
	</div>
</body>
</html>