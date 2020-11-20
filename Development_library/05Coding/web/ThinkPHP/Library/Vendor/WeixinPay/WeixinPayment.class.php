<?php
/**
 * 企业微信账户向普通微信账户付款
 * +++++++++++++++++++++++++++++++++++++++
 * 
*/

class WeixinPayment{

	private $AppID;						// 微信服务号ID
	private $AppSecret;					// 微信号Secret
	
	private $MCHID;						// 支付ID
	private $PayKEY;					// 支付KEY密钥
	private $SSLCERT_PATH;				// 支付证书路径
	private $SSLKEY_PATH;				// 支付证书密钥路径
	
	
	public function __construct() {
		
		//读取系统config配置
		$conf = C("WxPayConfig");
		
		// 如果系统没有配置就调取第方库配置
		if(!$conf){
			require_once "lib/WxPay.Config.php";
			
			$conf["APPID"]				= WxPayConfig::APPID;
			$conf["APPSECRET"]			= WxPayConfig::APPSECRET;
			$conf["MCHID"]				= WxPayConfig::MCHID;
			$conf["KEY"]				= WxPayConfig::KEY;
			
			// 证书路径填写 index.php 的相对路径
			$conf["SSLCERT_PATH"]		= $_SERVER['DOCUMENT_ROOT'].WxPayConfig::SSLCERT_PATH;
			$conf["SSLKEY_PATH"]		= $_SERVER['DOCUMENT_ROOT'].WxPayConfig::SSLKEY_PATH;
		}
		
		//写入类变量
		$this->AppID			= $conf["APPID"];
		$this->AppSecret		= $conf["APPSECRET"];
		
		$this->MCHID			= $conf["MCHID"];
		$this->PayKEY			= $conf["KEY"];
		$this->SSLCERT_PATH		= $conf["SSLCERT_PATH"];
		$this->SSLKEY_PATH		= $conf["SSLKEY_PATH"];
	}


	/**
		+ 企业付款
		+------------------------------------------------------
		+ $amount		付款金额
		+ $financeID	财务记录ID（必须唯一）
		+ $openid		接受收款的微信用户openid
	*/
	
	public function Payment($amount,$financeID,$openid,$desc = "奖励"){
			
		$mch_appid							= $this->AppID;
		$mchid								= $this->MCHID;					// 商户号
		$nonce_str							= 'qyzf_'.$financeID;			// 随机字符串
		$partner_trade_no					= $financeID;					// 商户订单号（唯一）
		$openid								= $openid;						// 收款用户OPENID
		
		// 校验用户姓名选项，NO_CHECK：不校验真实姓名 FORCE_CHECK：强校验真实姓名（未实名认证的用户会校验失败，无法转账）OPTION_CHECK：针对已实名认证的用户才校验真实姓名（未实名认证用户不校验，可以转账成功）
		$check_name							= 'NO_CHECK';
		$re_user_name						= '';							// 用户姓名效验，默认为空
		$amount								= $amount * 100;				// 金额（以分为单位，必须大于100）
		$desc								= $desc;						// 描述
		$spbill_create_ip					= $_SERVER["REMOTE_ADDR"];		// 发起请求ip
		
		
		//封装成数据生成签名字符
		$dataArr=array();
		$dataArr['mch_appid']				= $mch_appid;					// 公众账号appid
		$dataArr['mchid']					= $mchid;						// 支付商户号
		$dataArr['nonce_str']				= $nonce_str;
		$dataArr['partner_trade_no']		= $partner_trade_no;
		$dataArr['openid']					= $openid;
		$dataArr['check_name']				= $check_name;
		$dataArr['re_user_name']			= $re_user_name;
		$dataArr['amount']					= $amount;
		$dataArr['desc']					= $desc;
		$dataArr['spbill_create_ip']		= $spbill_create_ip;
		
		// 生存签名
		$sign								= $this->getSign($dataArr);
		
		//生成提交XML文档
		$data="<xml>
			<mch_appid>".$mch_appid."</mch_appid>
			<mchid>".$mchid."</mchid>
			<nonce_str>".$nonce_str."</nonce_str>
			<partner_trade_no>".$partner_trade_no."</partner_trade_no>
			<openid>".$openid."</openid>
			<check_name>".$check_name."</check_name>
			<re_user_name>".$re_user_name."</re_user_name>
			<amount>".$amount."</amount>
			<desc>".$desc."</desc>
			<spbill_create_ip>".$spbill_create_ip."</spbill_create_ip>
			<sign>".$sign."</sign>
			</xml>";
		
		// 提交地址
		$url			= "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
		$info			= $this->http_post_data($url,$data);
		
		// XML序列化
		libxml_disable_entity_loader(true);
		$postObj = simplexml_load_string($info, 'SimpleXMLElement', LIBXML_NOCDATA);
		
		// 将XMLArray对象转换为普通数组
		$postObj = json_encode($postObj);
		$postObj = json_decode($postObj,true);
		
		//返回腾讯数据
		return $postObj;
		
	}
	
	
	// SSL POST 发送程序
	/**
		$url		提交URL
		$data		POST提交的数据
		$useSSL		判断是否SSL提交
		$useCert	是否提交SSL加密证书
	*/
	private function http_post_data($url, $data, $second = 30) {  
	  
		$ch = curl_init();
			
		//设置超时
		curl_setopt($ch, CURLOPT_TIMEOUT, $second);
			
		// 设置请求URL
		curl_setopt($ch, CURLOPT_URL, $url);
		//设置header
		curl_setopt($ch, CURLOPT_HEADER, FALSE);
		//要求结果为字符串且输出到屏幕上
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
			
		// SSL 验证方式
		curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);
		curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, FALSE);
		
		$SSLCERT		= $this->SSLCERT_PATH;
		$SSLKEY			= $this->SSLKEY_PATH;

		//使用证书：cert 与 key 分别属于两个.pem文件
		curl_setopt($ch,CURLOPT_SSLCERTTYPE,'PEM');
		curl_setopt($ch,CURLOPT_SSLCERT, $SSLCERT);		// 提交SSL证书路径
		curl_setopt($ch,CURLOPT_SSLKEYTYPE,'PEM');
		curl_setopt($ch,CURLOPT_SSLKEY, $SSLKEY);		// 提交SLL证书密钥
			
		//post提交方式 
		if(!empty($data)){
			curl_setopt($ch, CURLOPT_POST, TRUE);
			curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
		}
	
		$output = curl_exec($ch);
		if($output){
			curl_close($ch);
			return $output;
		}else{
			$error = curl_errno($ch);
			curl_close($ch);
			return $error;
		}
	}



	/**
	 * 	作用：生成签名
	 */
	private function getSign($Parameters){
	
		//签名步骤一：按字典序排序参数
		$String = "";
		ksort($Parameters);	//  ASCII码从小到大排序
		foreach ($Parameters as $k => $v){
			if(strlen($v) > 0){
				$String .= $k . "=" . $v . "&";
			}
		}
		
		//签名步骤二：在string后加入KEY
		$String = $String."key=".$this->PayKEY;
	
		//签名步骤三：MD5加密
		$String = md5($String);
	
		//签名步骤四：所有字符转为大写
		$result_ = strtoupper($String);
	
		return $result_;
	}
	
	
}

?>