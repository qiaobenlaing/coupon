<?php
/**
 * Created by PhpStorm.
 * User: 10718
 * Date: 2018/5/14
 * Time: 14:32
 */
namespace Admin\Controller;
use Think\Controller;
use  Org\icbc\DefaultIcbcClient;
use  Org\icbc\IcbcConstants;
use  Org\icbc\IcbcEncrypt;
use Org\icbc\UiIcbcClient;
use Org\icbc\AES;

use Common\Model\Pager;
use Org\FirePHPCore\FP;
class TestController extends  AdminBaseController
{



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





    function aaa(){
		
        header('content-type:text/html;charset=utf-8');
        $url='http://192.168.8.119:8878/zhifu';
        $priKey = 'TKhSiK9Blwr6+aCq+O0MFg==';// AES密钥
	

        $request = array(
            //"serviceUrl" => 'http://ip:port/ui/thirdparty/order/pay/V2',
            "serviceUrl" => 'http://192.168.8.119:8878/zhifu',
            "method" => 'POST',
            "isNeedEncrypt" => true,
            "biz_content" => array(
                "mer_id"=>"020002040095",
                "store_code"=>"02000015087",
               "cust_id"=>"1076dAbpBsrJXpI4J+/HXthaHj+mORib",
                "out_trade_no"=>"ZHL777O15002039",
               "order_amt"=>"7370",
                "trade_date"=>"20170112",
                "trade_time"=>"160346",
                "attach"=>"abcdefg",// 该字段非必输项
                "pay_expire"=>"1200",
                "notify_url"=>"127.0.0.1",// 该字段非必输项
                "notify_flag"=>"1",
                "auto_submit_flag"=>"1",
                "goods_name"=>"商品001",// 该字段非必输项
                "all_points_flag"=>"1",// 该字段非必输项
                "good_points"=>"500"// 该字段非必输项
			
            )

        );

        $client = new UiIcbcClient('10000000000000002156',
            'MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIJBzZNaiobplRUgtJ4OzmUvZRK99ih/fUyDBOoFLtpJJCCRzp8T6V11YNlE7Xg5dt+EG7byQs2NImqg0eWEj/mBdZ7UmlAct7BNw2hyF2h4R5OEfXyjoH3wqGjKJayhaHTvLM1DYy/mDFBb0ShJYI1QMScQJZhsOhMMFhrrZwIZAgMBAAECgYAA2kdrOIOBoJPOQJmOE1C8jtPdjIrI9xSt5Imqsn/9A8+NuwacOfgkGXmZ0n6vc8jYa7f2uZ1AVTUtd4IIO5bpq8s0Tw2BfWALYwr/JdUuNKSjHVQsh/Do+wl8BgOgB4RqsNXWNGtoMC8lHKHmrVcpyJMfDc3cP07NZ1wG2zB0lQJBAM+dNZv2L/Z4RzvQcoVZEthYavZ4pkFoWGYC4jwc5G8um76zoQyrtxWYrtTP0GS+xFFX2dEuiGXxwzmSQJrPdrMCQQCgnUXcQe/if2c6TFt4x9v+6L0pmFClYyiOi9RuBSz1sHmPouuc/YYvuxAOdOzu3yzOkeo7b5KcCKITTWvKI+oDAkA5dl6vIw2VXycAJCp+Q/AWVyqLu0rw0Yud+HBbiPek2jabKqaJlkFfRdol5rrcF3zIstMDtahk5uxM0/DzqDZHAkBGnZ8vfdYIUVeDbDrzWXvCEXXJqewbKwOT2KqnTKM9yj9IBatttJGgvrAKiyH4zCqZD9JaG23sKGeJ8QopL60dAkEAtc4tlKoj3XZzRUXboqz0EhkgkjzDj50zpCD1sJKZ2EZH+A/7tXwPug+RnuSmKpM2uv3msqw3prdS3K4En8+rog==',
            IcbcConstants::$SIGN_TYPE_RSA2,
            '',
            '',
            '',
            $priKey,
            IcbcConstants::$ENCRYPT_TYPE_AES,
            '',
            '');

        $resp = $client->buildPostForm($request,'msgid','');

       $res=$this->http_curl($url,'post','json',$resp);



       // $IcbcEncrypt=new IcbcEncrypt();
      // $aaa= $IcbcEncrypt->decryptContent( "app_id=10000000000000002156&sign_type=RSA2&charset=UTF-8&format=json&ca=''&app_auth_token=''&msg_id=msgid&timestamp=2018-05-17 01:57:49&encrypt_type=AES&biz_content= YKakI/lDr4KIVUyviAV59g5nv/vA058KGt8QiYeOa1l8Dza6z2QUr6z6IHAehkHBYDVkmCVORLfHdg4kMt3VLU3oBeIXinEvJ7m0gkNjMtcmV1X/bxzP/6BZJPOJlXdHC3ikHzZuYVPO6ZjQdmV5r/77kIMg7ymJHuAf2UIEGaWOC AwlkZCV4kEmcKtCYadR1GOGsNNYLmbLSC4wxclxwRFb wIglfl38CRqaoudZG7EKz5utLH1QVzsA0yTvuZ2LjTB0kcgFCzbDRGsG0drg/AkcLfweZAZcc3sxbrEEkM/EYvAmtOPgEm2tNqo5Fa FTwfrGi7iDHwHz9DSmIEkszxF5uPhaeXNDCSCG73/2SzScLZzNUIK1WdiK/cUOr djTADGPAB5EjxzOHTEenYvDsqhOO3ZjviMHKP05WfdQQU2yquAQNcSZsCwS7RTU6wPhSaaDxgKNEcf0vlsZcGJ2zpBGw3eQyx0wEZ5oV6PSkDd7ypL203IKVbtvhtJ&sign=ANlqL6a0A 7GrINPTfS3fsvX1XswniDIUFfHtj9cuEQlyI2lfqxuokg HnCEw Hmf4nAiJBrEdo1hhC4R3aVLxbeQUxA7GHXM2bpNp5CCWux3Bbh0u5B1wMzkWBxkHfUo7IQNVjf8Ji5wN/ucrOrAbwEBPWEUDFoyYMCSlR X4k'",'AES','TKhSiK9Blwr6+aCq+O0MFg==','UTF-8');

   // var_dump($aaa);die;
    }

     public function lcy(){
		
        $big_content=I('get.big_content');
		$big_content=str_replace(' ','+',$big_content);
	
      //  $key=I('get.key');
		//$key=str_replace(' ','+',$key);
		//echo $key;die;
         $IcbcEncrypt=new IcbcEncrypt();
	
         $aaa= $IcbcEncrypt->decryptContent($big_content,'AES','TKhSiK9Blwr6+aCq+O0MFg==','UTF-8');
		// return $aaa;
         var_dump($aaa);die;
    }
	
	
	//notify_url
	public function isSuccess(){
		$info=I('get.info');
		$merId=I('get.mer_id');
		if(isset($info)&&isset($merId)){
			echo "success";
		}else{
			echo "error";
		}
	}

}