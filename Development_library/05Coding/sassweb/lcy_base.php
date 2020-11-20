<?php
	class weixi{
	
	 //获取用户的基本信息
		function  getBaseInfo(){
        $appid="wxdacebf540333a98f";
        $redirect_uri="https://hfq.huift.com.cn/lcy_acctoken.php";

        //1.获取code
        $url="https://open.weixin.qq.com/connect/oauth2/authorize?appid=".$appid."&redirect_uri=".$redirect_uri."&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
        header('location:'.$url);
    }
	}
	$wx=new weixi();
	$wx->getBaseInfo();
?>








