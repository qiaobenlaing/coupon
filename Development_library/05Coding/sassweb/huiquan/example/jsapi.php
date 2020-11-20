<?php
ini_set('date.timezone','Asia/Shanghai');
//error_reporting(E_ERROR);
require_once "/usr/local/nginx/html/hfq.huift.com.cn/huiquan/lib/WxPay.Api.php";
require_once "/usr/local/nginx/html/hfq.huift.com.cn/huiquan/example/WxPay.JsApiPay.php";


//打印输出数组信息




//①、获取用户openid
$tools = new JsApiPay();

//$openId='oboBC0X57v_I2hHKMMJ3_3LjjXiU';
//②、统一下单
// $openId=$_POST['openId'];

//$function=$_POST['function'];
//$payPrice=$_POST['payPrice'];


$openId=$GLOBALS['openId'];
$function=$GLOBALS['function'];
$payPrice=$GLOBALS['payPrice'];
$consumeCode=$GLOBALS['consumeCode'];
$orderNbr=$GLOBALS['orderNbr'];
//$batchCouponCode = $GLOBALS['batchCouponCode'];

//$json_info = json_encode(array("batchCouponCode"=>$batchCouponCode,"consumeCode"=>$consumeCode));


$host_url = $_SERVER['HTTP_HOST'];

//$consumeCode=$_POST['consumeCode'];
$input = new WxPayUnifiedOrder();
$input->SetBody($function);//商品描述   Batchcoupon.function
$input->SetAttach($consumeCode);//附加数据
$input->SetOut_trade_no($orderNbr);//商户订单号  
$input->SetTotal_fee($payPrice);//1分钱    Batchcoupon.payPrice
$input->SetTime_start(date("YmdHis"));//交易起始时间
$input->SetTime_expire(date("YmdHis", time() + 600));//time_expire
$input->SetGoods_tag("test");//订单优惠标记
$input->SetNotify_url("https://".$host_url."/huiquan/example/notify.php");//通知地址
$input->SetTrade_type("JSAPI");//交易类型

$input->SetOpenid($openId);
$order = WxPayApi::unifiedOrder($input);

$jsApiParameters = $tools->GetJsApiParameters($order);

$return_data = array(
    'jsapi' => $jsApiParameters,
    'batchCouponCode' => $batchCouponCode,
    'consumeCode' => $consumeCode
);

echo json_encode($return_data,JSON_UNESCAPED_UNICODE);
exit();


//var_dump($jsApiParameters);
//获取共享收货地址js函数参数
//$editAddress = $tools->GetEditAddressParameters();

//③、在支持成功回调通知中处理成功之后的事宜，见 notify.php
/**
 * 注意：
 * 1、当你的回调地址不可访问的时候，回调通知会失败，可以通过查询订单来确认支付是否成功
 * 2、jsapi支付时需要填入用户openid，WxPay.JsApiPay.php中有获取openid流程 （文档可以参考微信公众平台“网页授权接口”，
 * 参考http://mp.weixin.qq.com/wiki/17/c0f37d5704f0b64713d5d2c37b468d75.html）
 *
*/

?>


