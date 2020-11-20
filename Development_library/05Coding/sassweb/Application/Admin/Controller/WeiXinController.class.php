<?php
/**
 * Created by PhpStorm.
 * User: 98041
 * Date: 2018/7/11
 * Time: 9:04
 */

namespace Admin\Controller;


use Common\Model\WeixinLogModel;
use Think\Controller;

class WeiXinController extends  Controller
{
    // 默认token
    private $token = "A915A3512ED50A4101D6BFDB1DA3EA72";

    // 公众号APPID
    private $appid = "wxdacebf540333a98f";
    // 公众号密钥
    private $secret = "d37c8606678f5faa16ce322b8ead4d03";

    public function __construct() {
        parent::__construct();
        // 确保jsonrpc后面不被挂上一堆调试信息。
        C('SHOW_PAGE_TRACE', false);
    }

    //默认加载
    public function index(){
        if(strlen($_REQUEST["echostr"]) > 0){
            $this->valid();
        }else{
            // 微信推送事件处理
            $this->responseMsg();
        }
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

    // 根据消息内容分配处理
    private function responseMsg() {

        //获取微信推送过来的信息
        $postObj = $this->get_contents();



        //处理不为空的信息
        if ($postObj) {

            // 取得消息类型
            $Event = trim($postObj->MsgType);

            switch($Event)
            {
                // 事件推送、关注、取消关注、扫码关注、关注用户扫码、上报经纬度
                case "event":
                    $resultStr = $this->handleEvent($postObj);
                    break;

                // 普通推送、文字、图片、音频、视频、连接
                default:
                    $resultStr = $this->handleText($postObj);
                    break;
            }

            echo $resultStr;
        }
    }

    // 处理微信端文本推送信息
    private function handleText($postObj){

        // 文本事件类型
        $Event = (string)$postObj->MsgType;

        switch($Event)
        {
            // 文本推送
            case "text":
            $resultStr =    $this->handleTextRun($postObj);
                break;

            // 连接消息
            case "link":
                break;
        }
        return $resultStr;
    }

    // 事件处理
    private function handleEvent($postObj){

        // 事件消息处理
        $Event = $postObj->Event;

        switch($Event)
        {
            // 关注
            case "subscribe":
                break;

            // 取消关注
            case "unsubscribe":
                $resultStr = $this->handleTextRun($postObj,'');
                break;

            //转赠事件
            case "user_gifting_card":
                $resultStr = $this->isGaveToFriends($postObj);
                break;

             //卡券核销
            case "user_consume_card":
                $resultStr = $this->handleConsumeCard($postObj);
                break;

             //用户领取会员卡
            case "user_get_card":
                $resultStr = $this->handleGetCardEvent($postObj);
                break;

            //用户删除卡券
            case "user_del_card":
                $resultStr = $this->handleDelCardEvent($postObj);
                break;

            // 上报地理位置事件
            case "LOCATION":
                $resultStr = $this->handleTextRun($postObj,'上报地理位置事件');
                break;
        }

        return $resultStr;
    }

    //记录日志
    private function weiXinLog($openid,$cardNumber,$userCode,$userCouponCardCode,$event,$cardType=2){

        $WeixinLog = new WeixinLogModel();

            $log = $WeixinLog->add(array("openid"=>$openid,"readtime"=>time(),
                "addip"=>get_client_ip(),"cardType"=>$cardType,
                "cardNumber"=>$cardNumber,"userCode"=>$userCode,"userCouponCardCode"=>$userCouponCardCode,"event"=>$event));

            $WeixinLog->startTrans();

            if($log){

                $WeixinLog->commit();
            }else{

                $WeixinLog->rollback();
            }
    }


    //用户转赠事件
    private function isGaveToFriends($postObj){

        $relationOpenid     =(string) $postObj->FromUserName;
        $userCouponNbr      =(string) $postObj->UserCardCode;
        $cardId             =(string) $postObj->CardId;
        $IsReturnBack       =(string) $postObj->IsReturnBack;
        $FriendUserName     =(string) $postObj->FriendUserName;
        $IsChatRoom         = (string)$postObj->IsChatRoom;

        //查询优惠券
        $userCouponModel = D("UserCoupon");
        $userCouponInfo  = $userCouponModel->where(array("userCouponNbr"=>$userCouponNbr))->field("userCouponCode,userCouponNbr,userCode")->find();

        if(!$userCouponInfo){
            return false;
        }

        //转赠退回(朋友)
        switch ($IsReturnBack){
            //转赠
            case 0:
                $sql = "UPDATE UserCoupon SET status = 0,inWeixinCard=2  WHERE userCouponCode = '".$userCouponInfo['userCouponCode']."'";
                $res =  $userCouponModel->execute($sql);

                // 开启事务
                $userCouponModel->startTrans();
                if($res){

                    $this->weiXinLog($relationOpenid,$userCouponInfo['userCouponNbr'],
                        $userCouponInfo['userCode'],$userCouponInfo['userCouponCode'],"用户".$relationOpenid."转给".$FriendUserName."编号为".$userCouponNbr."的优惠券",2);

                    $userCouponModel->commit();
                }else{
                    $userCouponModel->rollback();
                }
                break;
            //退回
            case 1:
                $sql = "UPDATE UserCoupon SET status = 1,inWeixinCard=1  WHERE userCouponCode = '".$userCouponInfo['userCouponCode']."'";
                $res =  $userCouponModel->execute($sql);

                // 开启事务
                $userCouponModel->startTrans();
                if($res){

                    $this->weiXinLog($relationOpenid,$userCouponInfo['userCouponNbr'],
                        $userCouponInfo['userCode'],$userCouponInfo['userCouponCode'],"用户".$relationOpenid."未领取已退回给".$FriendUserName."编号为".$userCouponNbr."的优惠券",2);


                    $userCouponModel->commit();
                }else{
                    $userCouponModel->rollback();
                }
                break;
        }

    }

    //用户核销卡券事件(核销成功就删除)
    private function  handleConsumeCard($postObj){

        $relationOpenid     =(string) $postObj->FromUserName;
        $userCouponNbr      =(string) $postObj->UserCardCode;
        $cardId             =(string) $postObj->CardId;
        //查询优惠券
        $userCouponModel = D("UserCoupon");

        $userCouponInfo  = $userCouponModel->where(array("userCouponNbr"=>$userCouponNbr))->field("userCouponCode,userCouponNbr,userCode")->find();
        if(!$userCouponInfo){
                return false;//查不到优惠券
        }

        $sql = "UPDATE UserCoupon SET status = 2,inWeixinCard=2  WHERE userCouponCode = '".$userCouponInfo['userCouponCode']."'";

        $res =  $userCouponModel->execute($sql);

        // 开启事务
        $userCouponModel->startTrans();
        if($res){

            $this->weiXinLog($relationOpenid,$userCouponInfo['userCouponNbr'],
                $userCouponInfo['userCode'],$userCouponInfo['userCouponCode'],"商户核销了编号为".$userCouponNbr."的优惠券",2);

            $userCouponModel->commit();
        }else{
            $userCouponModel->rollback();
        }
    }

    //用户删除微信卡券事件
    private  function  handleDelCardEvent($postObj){

        $relationOpenid     =(string) $postObj->FromUserName;
        $userCouponNbr      =(string) $postObj->UserCardCode;
        $cardId             =(string) $postObj->CardId;

        // 查优惠券
        $userCouponModel = D("UserCoupon");
        $userCouponInfo  = $userCouponModel->where(array("userCouponNbr"=>$userCouponNbr))->field("userCouponCode,userCouponNbr,relationOpenid,userCode")->find();

        if(!$userCouponInfo){
            //查会员卡
            $userCardModel = D("UserCard");
            $uerCardInfo =    $userCardModel->where(array("cardNbr"=>$userCouponNbr))->field("cardNbr,userCardcode,userCode,relationOpenid")->find();

            if($uerCardInfo){

                $sql = "UPDATE UserCard SET inWeixinCard = 2 WHERE userCardcode = '".$uerCardInfo['userCardcode']."' and userCode='".$uerCardInfo['userCode']."'";
                $res =  $userCardModel->execute($sql);

                // 开启事务
                    $userCardModel->startTrans();

                if($res){

                    $this->weiXinLog($relationOpenid,$uerCardInfo['cardNbr'],
                        $uerCardInfo['userCode'],$uerCardInfo['userCardcode'],"用户删除微信卡包编号为".$userCouponNbr."的会员卡",1);

                    $userCardModel->commit();
                }else{
                    $userCardModel->rollback();
                }

            }else{
                return false;//查询不到会员卡也查不到优惠券
            }
        }

        $sql = "UPDATE UserCoupon SET status = 0 ,inWeixinCard =2  WHERE userCouponCode = '".$userCouponInfo['userCouponCode']."'";

        $res =  $userCouponModel->execute($sql);

            // 开启事务
            $userCouponModel->startTrans();

        if($res){

              $this->weiXinLog($relationOpenid,$userCouponInfo['userCouponNbr'],
                  $userCouponInfo['userCode'],$userCouponInfo['userCouponCode'],"用户删除微信卡包编号为".$userCouponNbr."的优惠券",2);

            $userCouponModel->commit();

        }else{

            $userCouponModel->rollback();

        }
    }

    //用户领取微信卡或者券事件
    private  function  handleGetCardEvent($postObj){

        $relationOpenid     =(string) $postObj->FromUserName;
        $userCouponNbr      =(string) $postObj->UserCardCode;
        $cardId             =(string) $postObj->CardId;

        // 查询优惠券
         $userCouponModel = D("UserCoupon");
         $userCouponInfo  = $userCouponModel->where(array("userCouponNbr"=>$userCouponNbr))->field("userCouponCode,userCouponNbr,userCode")->find();

        if(!$userCouponInfo){
                //查询会员卡
                $userCardModel = D("UserCard");
                $userCardInfo =     $userCardModel->where(array("cardNbr"=>$userCouponNbr))->field("userCardCode,cardNbr,userCode")->find();

            if($userCardInfo){

                $sql = "UPDATE UserCard SET relationOpenid='".$relationOpenid."', cardId='".$cardId."'  WHERE userCardCode = '".$userCardInfo['userCardCode']."'";

                $res =  $userCardModel->execute($sql);
                    // 开启事务
                    $userCardModel->startTrans();
                    if($res){

                        $this->weiXinLog($relationOpenid,$userCardInfo['cardNbr'],
                            $userCardInfo['userCode'],$userCardInfo['userCardCode'],"用户领取微信卡包编号为".$userCouponNbr."的会员卡",1);

                        $userCardModel->commit();
                    }else{
                        $userCardModel->rollback();
                    }
                }else{
                    return false;//会员卡和优惠券都查不到就return false
                }
         }else{

             $sql = "UPDATE UserCoupon SET relationOpenid='".$relationOpenid."', cardId='".$cardId."'  WHERE userCouponCode = '".$userCouponInfo['userCouponCode']."'";

             $res =  $userCouponModel->execute($sql);

             // 开启事务
             $userCouponModel->startTrans();
             if($res){

                 $this->weiXinLog($relationOpenid,$userCouponInfo['userCouponNbr'],
                     $userCouponInfo['userCode'],$userCouponInfo['userCouponCode'],"用户领取编号为".$userCouponNbr."的优惠券到微信卡包",2);

                 $userCouponModel->commit();
             }else{
                 $userCouponModel->rollback();
             }
         }
    }


    // 默认文本回复
    private function handleTextRun($postObj,$title = "您说的是什么？"){
        return $this->responseText($postObj,$title);
    }


    // 生成回复文本XML文件
    private function responseText($object, $content){
        $textTpl = "<xml>
						<ToUserName><![CDATA[%s]]></ToUserName>
						<FromUserName><![CDATA[%s]]></FromUserName>
						<CreateTime>%s</CreateTime>
						<MsgType><![CDATA[text]]></MsgType>
						<Content><![CDATA[%s]]></Content>
					</xml>";
        $resultStr = sprintf($textTpl, $object->FromUserName, $object->ToUserName, time(), $content);
        return $resultStr;
    }

    // 生成回复图文内容XML文件
    private function responseImaText($object, $content){

        $item		= "";
        $icount		= count($content);
        foreach($content as $key=>$vo){
            $item .= "<item>
							<Title><![CDATA[".$vo['title']."]]></Title> 
							<Description><![CDATA[".$vo['content']."]]></Description>
							<PicUrl><![CDATA[".$vo['logo']."]]></PicUrl>
							<Url><![CDATA[".$vo['url']."]]></Url>
						</item>";
        }

        $textTpl = "<xml>
						<ToUserName><![CDATA[%s]]></ToUserName>
						<FromUserName><![CDATA[%s]]></FromUserName>
						<CreateTime>%s</CreateTime>
						<MsgType><![CDATA[news]]></MsgType>
						<ArticleCount>%s</ArticleCount>
						<Articles>
							".$item."
						</Articles>
					</xml>";

        $resultStr = sprintf($textTpl, $object->FromUserName, $object->ToUserName, time(),$icount);
        return $resultStr;
    }

    //取得腾讯推送内容
    private function get_contents(){

        // 获取腾讯推送过来的内容
        $postObj = file_get_contents("php://input");

        // 判断是否有内容
        if (!empty ($postObj)) {
            // 解析XML文档
            libxml_disable_entity_loader(true);
            $postObj = simplexml_load_string($postObj, 'SimpleXMLElement', LIBXML_NOCDATA);
        }

        return $postObj;
    }

    // 第一次连接验证
    private function valid() {
        $echoStr = $_REQUEST["echostr"];

        if ($this->checkSignature()) {
            echo $echoStr;
            exit;
        }
    }

    // 微信token校验
    private function checkSignature() {
        $signature = $_GET["signature"];
        $timestamp = $_GET["timestamp"];
        $nonce = $_GET["nonce"];

        // 后台设置TOKEN
        $token = $this->token;
        $tmpArr = array (
            $token,
            $timestamp,
            $nonce
        );

        sort($tmpArr);
        $tmpStr = implode($tmpArr);
        $tmpStr = sha1($tmpStr);

        if ($tmpStr == $signature) {
            return true;
        } else {
            return false;
        }
    }
}