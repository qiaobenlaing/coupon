<?php
namespace Common\Model;
use Think\Model;
/**
 * message表
 * @author Huafei Ji
 */
class MessageModel extends BaseModel {
    protected $tableName = 'Message';

    /**
     * 商家发送广播
     * 1、发券：亲们，本店新一批优惠券大派送，快来领券咯
     * 2、活动：亲们，本店有新活动咯，欢迎大家参与
     * 3、红包：亲们，本店大派红包，快来领取福利
     * 4、上新：亲们，本店有新品推出，快来体验
     * @param string $shopCode 商家编码
     * @param string $title 消息标题
     * @param string $content 消息内容
     * @return boolean 成功返回true，失败返回false
     */
    public function shopBroadcasting($shopCode, $title, $content) {
        $msgCode = $this->create_uuid();
        $ret = $this->add(array('msgCode' => $msgCode, 'title' => $title, 'content' => $content, 'createTime' => date('Y-m-d H:i:s'), 'senderCode' => $shopCode, 'type' => C('MESSAGE_TYPE.SHOP')));
        if($ret !== false) {
            $shopFollowingMdl = new ShopFollowingModel();
            $userList = $shopFollowingMdl->listShopFollowedUser($shopCode);
            $userMsgMdl = new UserMessageModel();
            $userMsgMdl->startTrans();
            foreach($userList as $user) {
                $userMsgInfo = array(
                    'userMsgCode' => $this->create_uuid(),
                    'receiverCode' => $user['userCode'],
                    'msgCode' => $msgCode,
                    'readingStatus' => C('MESSAGE_READING_TYPE.UNREAD'),
                    'msgStatus' => '',
                    'userCouponCode' => ''
                );
                $addUserMsgRet = $userMsgMdl->addUserMsg($userMsgInfo);
                if(!$addUserMsgRet) {
                    $userMsgMdl->rollback();
                    return false;
                }
            }
            $userMsgMdl->commit();
            return true;
        } else {
            return false;
        }

    }

    // TODO 添加优惠券过期通知
    /**
     * 添加消息
     * @param array $msgInfo 消息信息 array('msgCode','title','content','createTime','senderCode','type');
     * @param string $receiverCode 接收信息的用户编码
     * @return boolean 添加成功返回true，添加失败返回false
     */
    public function addMsg($msgInfo, $receiverCode) {
        $userCouponCode = $msgInfo['userCouponCode'];
        unset($msgInfo['userCouponCode']);
        if(! $msgInfo['msgCode'])
            $msgInfo['msgCode'] = $this->create_uuid();
        if($this->add($msgInfo)) {
            $userMsgInfo = array(
                'userMsgCode' => $this->create_uuid(),
                'receiverCode' => $receiverCode,
                'msgCode' => $msgInfo['msgCode'],
                'readingStatus' => C('MESSAGE_READING_TYPE.UNREAD'),
                'msgStatus' => '',
                'userCouponCode' => $userCouponCode
            );
            $userMsgMdl = new UserMessageModel();
            return $userMsgMdl->addUserMsg($userMsgInfo) !== false ? true : false;
        }
        return false;
    }

    /**
     * 管理端消息总数
     * @param array $filterData
     * @return null|int 如果为空，返回null；如果不为空，返回number;
     */
    public function countMessage($filterData) {
        $where = $this->filterWhere(
            $filterData
        );
        return $this
            ->where($where)
            ->join('User ON User.userCode = Message.senderCode', 'LEFT')
            ->join('Shop ON Shop.shopCode = Message.senderCode', 'LEFT')
            ->count('msgCode');
    }

    /**
     * 消息列表
     * @param array $filterData
     * @param object $page 页码
     * @return array
     */
    public function getMessageList($filterData, $page) {
        $where = $this->filterWhere(
            $filterData,
            $page);
        return $this
            ->field(array('msgCode', 'title', 'content', 'createTime', 'realName', 'Shop.shopName'))
            ->where($where)
            ->join('User ON User.userCode = Message.senderCode', 'LEFT')
            ->join('Shop ON Shop.shopCode = Message.senderCode', 'LEFT')
            ->order('createTime desc')
            ->pager($page)
            ->select();
    }

}