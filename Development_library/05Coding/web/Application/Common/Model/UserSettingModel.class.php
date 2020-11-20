<?php
namespace Common\Model;
use Think\Model;
/**
 * userSetting表
 * @author 
 */
class UserSettingModel extends BaseModel {
    protected $tableName = 'UserSetting';

    /**
     * 初始化用户设置
     * @param $userCode
     * @return boolean
     */
    public function addUserSetting($userCode) {
        return $this->add(array(
            'settingCode'=>$this->create_uuid(),
            'userCode'=>$userCode,
            'isBroadcastOn'=>C('YES'),
            'isMsgBingOn'=>C('YES'),
            'isCouponMsgOn'=>C('YES'),
        ));
    }

    /**
     * 获取用户设置
     * @param string $userCode 用户编码
     * @return array
     */
    public function getUserSetting($userCode) {
        return $this->where(array('userCode' => $userCode))->find();
    }

    /**
     * 修改用户设置
     * @param string $userCode
     * @param number $isBroadcastOn 是否接受广播消息。1-是；0-否
     * @param number $isMsgBingOn 是否开启消息提醒声音 。1-是；0-否
     * @param number $isCouponMsgOn 是否接受优惠券推送消息。1-是；0-否
     * @return array
     */
    public function updateUserSetting($userCode, $isBroadcastOn, $isMsgBingOn, $isCouponMsgOn) {
        $userSetting = $this->getUserSetting($userCode);
        if($userSetting['isBroadcastOn'] == $isBroadcastOn && $userSetting['isMsgBingOn'] == $isMsgBingOn && $userSetting['isCouponMsgOn'] == $isCouponMsgOn) {
            return $this->getBusinessCode(C('UPDATE_INFO.NO_UPDATE'));
        }
        $settingInfo = array();
        if($userSetting['isBroadcastOn'] != $isBroadcastOn) {$settingInfo['isBroadcastOn'] = $isBroadcastOn;}
        if($userSetting['isMsgBingOn'] != $isMsgBingOn) {$settingInfo['isMsgBingOn'] = $isMsgBingOn;}
        if($userSetting['isCouponMsgOn'] != $isCouponMsgOn) {$settingInfo['isCouponMsgOn'] = $isCouponMsgOn;}
        $code = $this->where(array('userCode' => $userCode))->save($settingInfo) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }
}