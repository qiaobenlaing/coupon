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
//        所属商圈
	    $zoneId= $_GET['zoneId'];
	    //存储所属商圈编号
         $_SESSION['bank_id'] = $zoneId;
         $url = "http://wx.huift.com.cn/WechatConfirm/transfer/goConfirm?appid=HQ0000&rtype=1&zoneId={$zoneId}";
          header("location:".$url);
	 }



    public  function getAccessToken(){
        $url="http://wx.huift.com.cn/WechatConfirm/transfer/getAccessToken?appid=HQ0000&rtype=1";
        $res=$this->http_curl($url,'post','json',$resp);
        echo $res;
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

    // 抓取https内容
    private function http_post_data($url,$data=array(),$useSSL = false,$outtime = 30){
        $ch = curl_init();
        curl_setopt ( $ch, CURLOPT_SAFE_UPLOAD, false);
        curl_setopt($ch, CURLOPT_URL, $url);
        //设置超时
        curl_setopt($ch, CURLOPT_TIMEOUT, $outtime);
        // ssl 方式提交
        if($useSSL){
            curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);
        }
        curl_setopt($ch, CURLOPT_HEADER, false);
        curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
        curl_setopt($ch, CURLOPT_REFERER, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
        //post提交方式
        if(!empty($data)){
            curl_setopt($ch, CURLOPT_POST, TRUE);
            curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
        }
        $result = curl_exec($ch);
        curl_close($ch);
        return $result;
    }
}