<?php
/**
 * Created by PhpStorm.
 * User: Huafei Ji
 * Date: 15-7-22
 * Time: 下午4:02
 */
namespace Common\Model;
use JPush\Model as M;
use JPush\JPushClient;
use JPush\JPushLog;
use Monolog\Logger;
use Monolog\Handler\StreamHandler;
use JPush\Exception\APIConnectionException;
use JPush\Exception\APIRequestException;

class JpushModel {
    private $appKey;
    private $masterSecret;
    private $client;

    /**
     * 架构函数
     * @access public
     */
    public function __construct($appKey, $masterSecret) {
        if($appKey) $this->appKey = $appKey;
        if($masterSecret) $this->masterSecret = $masterSecret;
        //导入类库
        Vendor('JPush.vendor.autoload');
        JPushLog::setLogHandlers(array(new StreamHandler('jpush.log', Logger::DEBUG)));
        $this->client = new JPushClient($this->appKey, $this->masterSecret);
    }

    /**
     * 删除某个设备的标签
     * @param $registrationId
     * @return M\DeviceResponse
     */
    public function removeTags($registrationId) {
        $ret = $this->client->removeDeviceTag($registrationId);
        return $ret;
    }

    /**
     * 删除符合条件标签的一些设备
     * @param $tags
     * @return M\DeviceResponse
     */
    public function removeDevices($tags) {
        if(empty($tags)){
            return true;
        }
        foreach($tags as $tag){
            $this->client->deleteTag($tag);
        }
        return true;
    }

    /**
     * 设置别名
     */
    public function setAlias() {

    }

    /**
     * 设置标签
     * @param string $registrationId 注册ID
     * @param $addTags
     * @return array
     */
    public function setTags($registrationId, $addTags) {
        // 移除指定RegistrationId的所有tag
        $this->client->removeDeviceTag($registrationId);
        // 更新指定RegistrationId的指定属性，当前支持tags, alias
        $ret = $this->client->updateDeviceTagAlias($registrationId, null, $addTags, null);
        return $ret;
    }

    /**
     * 推送
     * @param array $receiver 接收者信息
     * @param string $content 推送通知
     * @param array $extras 附加字段
     * @param string $action 推送动作
     * @return bool 成功失败
     */
    public function jPushByAction($receiver, $content, $extras, $action) {
        if(!in_array($_SERVER['HTTP_HOST'], array('web.huiquan.suanzi.cn', 'api.huiquan.suanzi.cn', 'huiquan.suanzi.cn')) && !in_array($action, array(C('PUSH_ACTION.INVITE_SHOP'), C('PUSH_ACTION.REMIND_SHOP'), C('PUSH_ACTION.PAY_COUPON_USE'), C('PUSH_ACTION.PAY_COUPON_REFUND'), C('PUSH_ACTION.COUPON_TO_BE_EXPIRED'), C('PUSH_ACTION.LOGIN'), C('PUSH_ACTION.CONSUME')))) {
            return true;
        }

        $extras['content'] = $content;
        $extras['action'] = $action;
        //easy push
        try {
            if(in_array($action, array(C('PUSH_ACTION.CONSUME'), C('PUSH_ACTION.JOIN_ACTIVITY'), C('PUSH_ACTION.EXIT_ACTIVITY')))){
                //买单推送，推送给对应商家在线店长  $receiver = explode('-',$shopCode)  tag_and => &&
                //参加或退出，推送给对应商家在线店长 $receiver = explode('-',$shopCode)  tag_and => &&
                $result = $this->client->push()
                    ->setPlatform(M\all)
                    ->setAudience(M\audience(
                        M\tag_and($receiver)
                    ))
//                ->setNotification(M\notification($content))
                    ->setMessage(M\message($action, null, null, $extras))
                    ->send();
            }elseif(in_array($action, array(C('PUSH_ACTION.INVITE_SHOP'), C('PUSH_ACTION.REMIND_SHOP')))){
                //邀请商家入驻，推送给对应商家在线店长  $receiver = explode('-',$shopCode)  tag_and => &&
                //提醒商家添加商品展示，推送给对应商家在线店长  $receiver = explode('-',$shopCode)  tag_and => &&
                $result = $this->client->push()
                    ->setPlatform(M\all)
                    ->setAudience(M\audience(
                        M\tag_and($receiver)
                    ))
                    ->setNotification(M\notification($content))
                    ->setMessage(M\message($action, null, null, $extras))
                    ->send();
            }elseif(in_array($action, array(C('PUSH_ACTION.LOGIN'), C('PUSH_ACTION.ACT_UPDATE'), C('PUSH_ACTION.COUPON_TO_BE_EXPIRED'), C('PUSH_ACTION.PAY_COUPON_USE'), C('PUSH_ACTION.PAY_COUPON_REFUND')))){
                //多方登陆，推送给上一个登陆设备的用户 $receiver = array($registrationId);  tag => ||
                //活动修改，推送给所有参加该活动的用户 $receiver = array(参加用户的手机号);  tag => ||
                //买的优惠券3天后过期提醒，推送给所有有券的用户 $receiver = array(参加用户的手机号);  tag => ||
                //买的优惠券使用提醒，推送给所有有券的用户 $receiver = array(参加用户的手机号);  tag => ||
                // 优惠券退款 $receiver = array(参加用户的手机号);  tag => ||
                $result = $this->client->push()
                    ->setPlatform(M\all)
                    ->setAudience(M\audience(
                        M\tag($receiver)
                    ))
//                    ->setNotification(M\notification($content))
                    ->setMessage(M\message($action, null, null, $extras))
                    ->send();
            }
            return true;
        } catch (APIRequestException $e) {
            return false;
        } catch (APIConnectionException $e) {
            return false;
        }
    }

    /**
     * 获取当前应用所有标签列表
     * @return object $tags
     */
    public function getTags() {
        $tags = $this->client->getTags();
        return $tags;
    }

    /**
     * 获取指定RegistrationId的所有属性，包含tags, alias
     * @param $registrationId
     * @return M\DeviceResponse
     */
    public function getDeviceTagAlias($registrationId) {
        $device = $this->client->getDeviceTagAlias($registrationId);
        return $device;
    }

    /**
     * 查询别名
     */
    public function getAlias() {
        $url = 'https://device.jpush.cn/v3/aliases/';

    }

    /**
     * 查询设备的别名和标签
     */
    public function getTagsAndAlias() {
        $ret = $this->client->getDeviceTagAlias('010bcd85d2b');
        return $ret;
//        $url = 'https://device.jpush.cn/v3/devices/010bcd85d2b';
//        $data = array();
//        $param = json_encode($data);
//        $base64=base64_encode("$this->appKey:$this->masterSecret");
//        $header=array("Authorization:Basic $base64","Content-Type:application/json");
//        $ch = curl_init();//初始化curl
//        curl_setopt($ch, CURLOPT_URL,$url);//抓取指定网页
//        curl_setopt($ch, CURLOPT_HEADER, 0);//设置header
//        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);//要求结果为字符串且输出到屏幕上
////        curl_setopt($ch, CURLOPT_POST, 1);//post提交方式
//        curl_setopt($ch, CURLOPT_POSTFIELDS, $param);
//        curl_setopt($ch, CURLOPT_HTTPHEADER,$header);
//        // 增加 HTTP Header（头）里的字段
//        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);
//        // 终止从服务端进行验证
//        curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, FALSE);
//        $data = curl_exec($ch);//运行curl
//        curl_close($ch);
//        return $data;
    }

}