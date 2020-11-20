<?php
namespace Common\Model;
use Think\Model;
/**
 * userMessage表
 * @author
 */
class UserMessageModel extends BaseModel {
    protected $tableName = 'UserMessage';

    /**
     * 获取用户消息
     * @param string $userCode 用户编码
     * @param number $type 消息类型。0-商家消息；1-会员卡消息；2-优惠券消息；
     * @param object $page 分页
     * @return array
     */
    public function getMessageList($userCode, $type, $page){
//        $condition['readingStatus'] = C('MESSAGE_READING_TYPE.UNREAD');
        if($userCode){
            $condition['receiverCode'] = $userCode;
        }
        $condition['Message.type'] = $type;
        $field = array(
            'userMsgCode',
            'title',
            'content',
            'createTime',
            'shopName',
            'logoUrl',
            'readingStatus',
        );
        if($type == C('MESSAGE_TYPE.COUPON')) {
            $field[] = 'UserMessage.userCouponCode';
            $field[] = 'UserCoupon.batchCouponCode';
        } elseif($type == C('MESSAGE_TYPE.SHOP')) {
            $field['senderCode'] = 'shopCode';
        }
        $this->field($field)
            ->join('Message ON Message.msgCode = UserMessage.msgCode')
            ->join('Shop ON Shop.shopCode = Message.senderCode');
        if($type == C('MESSAGE_TYPE.COUPON')) {
            $this->join('UserCoupon ON UserCoupon.userCouponCode = UserMessage.userCouponCode');
        }
        $this->where($condition)
            ->order('readingStatus asc, Message.createTime desc')
            ->pager($page);
        $messageList = $this->select();
        return $messageList ? $messageList : array();
    }

    /**
     * 统计用户获得消息总记录数
     * @param string $userCode 用户编码
     * @param int $type 消息类型
     * @param int $unRead 消息阅读状态 0-未读，1-已读，2-所有
     * @return int
     */
    public function countMessage($userCode, $type, $unRead = 0) {
        $condition = array();
        if($unRead != 2) {
            $condition['readingStatus'] = $unRead;
        }

        if($userCode){
            $condition['receiverCode'] = $userCode;
        }
        if($type != null && $type != ''){
            $condition['Message.type'] = $type;
        }
        $messageCount = $this
            ->join('Message on Message.msgCode = UserMessage.msgCode')
            ->join('Shop ON Shop.shopCode = Message.senderCode')
            ->where($condition)
            ->count('userMsgCode');
        return $messageCount;
    }

    /**
     * 设置有已接收者某种类型的消息为已读状态
     * @param array $condition 条件
     * @return array
     */
    public function readMsg($condition) {
        $userMsgCodeList = $this
            ->field(array('userMsgCode'))
            ->join('Message ON Message.msgCode = UserMessage.msgCode')
            ->where($condition)
            ->getField('userMsgCode', true);
        $code = C('SUCCESS');
        if($userMsgCodeList) {
            $code = $this
                ->where(array('userMsgCode' => array('IN', $userMsgCodeList)))
                ->save(array('readingStatus'=>C('MESSAGE_READING_TYPE.READ'))) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        }
        return $this->getBusinessCode($code);
    }

    /**
     * 添加用户消息
     * @param array $userMsgInfo 用户信息
     * @return boolean 添加成功，返回true；添加失败，返回false
     */
    public function addUserMsg($userMsgInfo) {
        return $this->add($userMsgInfo) !== false ? true : false ;
    }

    /**
     * 商家获得最近一周各类消息包括优惠券消息，会员卡消息，，商家广播
     * @param string $shopCode 商家编码
     * @param object $page 页码
     * @return array $msgList
     */
    public function sListMsg($shopCode, $page) {
        $limitTime = 24 * 60 *60 * 7; // 一周时间
        $date = date('Y-m-d H:i:s', time() - $limitTime);
        $couponMsgList = $this
            ->field(
                array('nickName', 'avatarUrl', 'Message.createTime', 'content'))
            ->join('Message on Message.msgCode = UserMessage.msgCode')
            ->join('User on User.userCode = Message.senderCode')
            ->where(array(
                'receiverCode' => $shopCode,
                'Message.type' => C('MESSAGE_TYPE.COUPON')
//                'Message.createTime' => array('EGT', $date)
            ))
            ->pager($page)
            ->select();
        $cardMsgList = $this
            ->field(
                array('nickName', 'avatarUrl', 'Message.createTime', 'content'))
            ->join('Message on Message.msgCode = UserMessage.msgCode')
            ->join('User on User.userCode = Message.senderCode')
            ->where(array(
                'receiverCode' => $shopCode,
                'Message.type' => C('MESSAGE_TYPE.CARD')
//                'Message.createTime' => array('EGT', $date)
            ))
            ->pager($page)
            ->select();
        $shopMsgList = $this
            ->field(
                array('shopName', 'logoUrl', 'Message.createTime', 'content'))
            ->join('Message on Message.msgCode = UserMessage.msgCode')
            ->join('Shop on Shop.shopCode = Message.senderCode')
            ->where(array(
                'receiverCode' => $shopCode,
                'Message.type' => C('MESSAGE_TYPE.SHOP')
//                'Message.createTime' => array('EGT', $date)
            ))
            ->pager($page)
            ->select();
        return array(
            'couponMsgList' => $couponMsgList,
            'cardMsgList' => $cardMsgList,
            'shopMsgList' => $shopMsgList,
        );
    }

    /**
     * 商家获得最近一周各类消息数量包括优惠券消息，会员卡消息，商家广播
     * @param string $shopCode 商家编码
     * @return array $msgList
     */
    public function sCountMsg($shopCode) {
        $limitTime = 24 * 60 *60 * 7; // 一周时间
        $date = date('Y-m-d H:i:s', time() - $limitTime);
        $couponMsgCount = $this
            ->join('Message on Message.msgCode = UserMessage.msgCode')
            ->join('User on User.userCode = Message.senderCode')
            ->where(array(
                'receiverCode' => $shopCode,
                'Message.type' => C('MESSAGE_TYPE.COUPON')
            ))
            ->count('userMsgCode');
        $cardMsgCount = $this
            ->join('Message on Message.msgCode = UserMessage.msgCode')
            ->join('User on User.userCode = Message.senderCode')
            ->where(array(
                'receiverCode' => $shopCode,
                'Message.type' => C('MESSAGE_TYPE.CARD')
            ))
            ->count('userMsgCode');
        $shopMsgCount = $this
            ->join('Message on Message.msgCode = UserMessage.msgCode')
            ->join('Shop on Shop.shopCode = Message.senderCode')
            ->where(array(
                'receiverCode' => $shopCode,
                'Message.type' => C('MESSAGE_TYPE.SHOP')
            ))
            ->count('userMsgCode');
        return array(
            'couponMsgCount' => $couponMsgCount,
            'cardMsgCount' => $cardMsgCount,
            'shopMsgCount' => $shopMsgCount,
        );
    }
}