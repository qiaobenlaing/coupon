<?php
 class Weixin{
	
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
			if($res=='json'){
				return json_decode($output,true);
			}
			
		}
		function updataVip(){
			header('content-type:text/html;charset=utf-8');
			 $access_token="10_Rmm2cbeLb8r8y4D0oOw-1Sj1mZdyXTCE19oZgYQoFTapyYEMAR6gS2RKEzQiQPEIs5h4nRQ9-dumv8VsiJNRERieVlUEhPpkBHsnbOEe_tFqtu-eyS3dv7joR-5V9CpWEqNpV8DWxcPc5hRcLAZdAAAZON";
			 $url="https://api.weixin.qq.com/card/membercard/updateuser?access_token=".$access_token;
			 $postArr=array(
			  "code"=> "110201201245",
			"card_id"=> "pQJGr0Y5AN-JUQ_a1qu4p1pDkD14",
  
    "record_bonus"=> urlencode("消费30元，获得3积分"),
    "bonus"=> 3000,
     "add_bonus"=> 30,
    "balance"=> 3000,
    "add_balance"=> +30,
    "record_balance"=> urlencode("购买焦糖玛琪朵一杯，增加金额30元。"),
     "custom_field_value1"=> urlencode("白金"),
      "custom_field_value2"=> urlencode("8折"),
    "notify_optional"=> array(
        "is_notify_bonus"=> true,
        "is_notify_balance"=> true,
        "is_notify_custom_field1"=>true
    )
			 );
			 $postJson=urldecode(json_encode($postArr));
			 $res=$this->http_curl($url,'post','json',$postJson);
 echo '<hr />';
 var_dump($res);
 return $res;

			 
		}
 }
  $wx = new Weixin();
   $c=$wx->updataVip();
   var_dump($c);

?>