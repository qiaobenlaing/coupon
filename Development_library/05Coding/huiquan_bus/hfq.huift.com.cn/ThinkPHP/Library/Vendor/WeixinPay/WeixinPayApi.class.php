<?php
//微信支付类
namespace Vendor\WeixinPay;
class WeixinPayApi{

	/**
		+ 微信支付加密串请求接口
		+----------------------------------------------------
		+ $money					实际支付金额（单位：元）
		+ $openid					支付用户openid
		+ $goodid					腾讯系统上的订单ID
		+ $attach					腾讯回掉带回参数
		+ $body						订单名称（会显示到用户系统）
		+ $trade_no					订单前缀，避免不同支付场景订单号重复
	*/
	public function getJsApiParameters($money,$openid,$goodid,$attach="带回参数",$body = "订单支付",$trade_no = "goodid_"){

		// 加载微信SDK包
		require_once "lib/WxPay.Api.php";
		require_once "lib/WxPay.JsApiPay.php";


		//①、获取支付基本信箱
		// 腾讯收款单位为分，我们计价单位为元
		$money = $money * 100;

		// 取得appid
		$appid = \WxPayConfig::APPID;

		//取得支付结果回掉处理地址
		$Notify_url = \WxPayConfig::Notify_url;

		$tools = new \JsApiPay();

		//②、统一下单
		$input = new \WxPayUnifiedOrder();

		//支付订单名称
		$input->SetBody($body);
		$input->SetAttach($attach);							//原数据带回参数

		//订单号，不能重复的喔
		$input->SetOut_trade_no($trade_no.$goodid);

		//支付金额，也分计算
		$input->SetTotal_fee($money);
		$input->SetTime_start(date("YmdHis"));
		$input->SetTime_expire(date("YmdHis", time() + 600));
		//$input->SetGoods_tag("订单");

		//腾讯处理返回消息
		$input->SetNotify_url($Notify_url);
		$input->SetTrade_type("JSAPI");

		//支付用户的openid
		$input->SetOpenid($openid);
		$order = \WxPayApi::unifiedOrder($input);

		$jsApiParameters = $tools->GetJsApiParameters($order);
		//echo "<br />jsAPI:".$jsApiParameters;

		 return $jsApiParameters;
	}


    /** 微信退款
     * @param $out_trade_no
     * @param $total_fee
     * @param $refund_fee
     * @return \成功时返回，其他抛异常
     * @throws \WxPayException
     */
	public function Weixinrefund($out_trade_no,$total_fee,$refund_fee){

        // 加载微信SDK包
        require_once "lib/WxPay.Api.php";

        $input = new \WxPayRefund();
        $input->SetOut_trade_no($out_trade_no);
        $input->SetTotal_fee($total_fee);
        $input->SetRefund_fee($refund_fee);
        $input->SetOut_refund_no(\WxPayConfig::MCHID.date("YmdHis"));
        $input->SetOp_user_id(\WxPayConfig::MCHID);
        $weixinInfo=\WxPayApi::refund($input);
		return $weixinInfo;
	}


	/**
		+ 公众号H5 Ajax调取支付JS代码
		+----------------------------------------------------
		+ $jsApiParameters			支付JSON加密串，生成结果来自 getJsApiParameters 函数
		+ $js_success				支付成功后的回掉函数
		+ $js_fail					支付失败回掉函数

	*/
	public function jsApiCode($jsApiParameters = "",$js_success = "pay_success();",$js_fail = ""){
		$js = "
			//调用微信JS api 支付
			function jsApiCall()
			{
				WeixinJSBridge.invoke(
					'getBrandWCPayRequest',
					".$jsApiParameters.",
					function(res){
						if(res.err_msg.indexOf(\"ok\") > 0){
							".$js_success."
						}else{
							".$js_fail."
						}
						
					}
				);
			}
			
			function callpay()
			{
				if (typeof WeixinJSBridge == \"undefined\"){
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
			callpay();
		";

		return $js;
	}

	/**
		+ 支付手续费计算
		+----------------------------------------------------
		+ $occur_amount				实际支付金额（单位：元）
		+ $discount					收取客户手续费
		+ $jsdiscount				腾讯收取手续费

	*/
	public function runDiscount($occur_amount,$discount = "0.006",$jsdiscount = "0.006"){

		// 腾讯政策
		if($jsdiscount <= 0){
			$jsdiscount			= "0.006";
		}

		// 给加盟商的政策
		if($discount <= 0){
			$discount			= "0.006";
		}

		$amount				= "";						// 实际到账金额
		$surplus			= "";						// 手续费剩余利润

		// 客户手续费
		$poundage			= number_format($occur_amount * $discount,2,'.','');
		if($poundage < 0.01){
			$poundage = 0.01;
		}

		// 腾讯手续费
		$tx_poundage		= $occur_amount * $jsdiscount;
		if($tx_poundage < 0.01){
			$tx_poundage = 0.01;
		}

		// 计算结果
		$array = array();
		$array["jsdiscount"]		= $jsdiscount;		// 腾讯政策
		$array["discount"]			= $discount;		// 给商户的政策
		$array["amount"]			= $occur_amount - $poundage;													// 客户到账金额
		$array["surplus"]			= $occur_amount - $array["amount"] - $tx_poundage;								// 支付结余手续费
		$array["occur_amount"]		= $occur_amount;	// 实际支付金额

		return $array;
	}

}
?>
