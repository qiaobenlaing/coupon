<?php
/**
 * Created by PhpStorm.
 * User: 10718
 * Date: 2018/5/23
 * Time: 14:49
 */

namespace Admin\Controller;


use Think\Controller;

class OpenIdController extends Controller
{
	
	public function __construct() {
        parent::__construct();
        // 确保jsonrpc后面不被挂上一堆调试信息。
        C('SHOW_PAGE_TRACE', false);
//        $this->pager = new Pager(I('page', 1, 'intval'), I('pageSize', Pager::DEFUALT_PAGE_SIZE, 'intval'));
    }
	
	
	 //获取openId
	 public function getUserOpenId(){

		$openid=$_GET['openid'];
		header('location:'."https://hfq.huift.com.cn/huiquan?openId=".$openid);
		//echo $access_token=$_GET['access_token'];

	 }

    public function getUserOpenIdPublic(){
        $openid=$_GET['openid'];
        echo  $openid;
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
	
	 public  function getAccessToken(){
		$url="http://wx.huift.com.cn/WechatConfirm/transfer/getAccessToken?appid=HQ0000&rtype=1";
		$res=$this->http_curl($url,'post','json',$resp);
		 echo $res;
	}
	 
	
	
}