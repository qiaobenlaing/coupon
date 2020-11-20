<?php
namespace Common\Model;
use Think\Model;
/**
 * Communication 表
 * @author
 */
class CommunicationModel extends BaseModel {
    protected $tableName = 'Communication';

    /**
     * 检查平台是否有未读的消息
     * @return int||boolean 有返回数量，没有返回false
     */
    public function checkPlatMsg() {
        $count = $this->where(array('shopCode' => C('HQ_CODE'), 'app' => C('COMMUNICATION_APP.USER'), 'readingStatus' => \Consts::READING_STATUS_UNREAD))->count();
        return $count ? $count : false;
    }

    /**
     * 发送消息
     * @param string $shopCode 商家编码或者平台编码
     * @param string $userCode
     * @param string $staffCode
     * @param string $message
     * @param string $app
     * @return mixed|string
     */
    public function sendMsg($shopCode, $userCode, $staffCode, $message, $app) {
        $rules = array(
            array('userCode', 'require', C('COMMUNICATION.USER')),
            array('shopCode', 'require', C('COMMUNICATION.SHOP')),
            array('message', 'require', C('COMMUNICATION.MESSAGE')),
        );
        $data = array(
            'userCode'=>$userCode,
            'shopCode'=>$shopCode,
            'message'=>$message
        );
        if($this->validate($rules)->create($data) != false) {
            $sendInfo = array(
                'msgCode'=>$this->create_uuid(),
                'userCode'=>$userCode,
                'staffCode'=>$staffCode,
                'shopCode'=>$shopCode,
                'message'=>$message,
                'createTime' => date('Y-m-d H:i:s',time()),
                'readingStatus'=>0,
                'app'=>$app
            );
            $code = $this->add($sendInfo) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            // 顾客端发消息而且shopCode等于平台编码
            if($app = 1 && $shopCode == C('HQ_CODE')) {
                // 给向惠圈提反馈建议的用户添加圈值记录，并更新历史圈值和当前圈值
                $userMdl = new UserModel();
                $userMdl->addPointEarningLog($userCode, 10, '意见反馈');
            }
            return $this->getBusinessCode($code);
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 将未读消息的阅读状态改为已读
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param string $app 发消息的一方。0-商家；1-顾客
     * @return array 格式：array('code' => '错误编码')
     */
    public function readMsg($userCode, $shopCode, $app) {
        $condition = array(
            'userCode' => $userCode,
            'shopCode' => $shopCode,
            'readingStatus' => \Consts::READING_STATUS_UNREAD,
            'app' => $app,
            'unix_timestamp(createTime)' => array('ELT', time()),
        );
        $code = $this->where($condition)->save(array('readingStatus' => \Consts::READING_STATUS_READ)) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

    /**
     * 获得未读消息数量
     * @param $userCode
     * @param $shopCode
     * @param $app
     * @return mixed
     */
    public function countUnreadMsg($userCode, $shopCode, $app) {
        if($userCode){
            $condition['userCode'] = $userCode;
        }else{
            $condition['userCode'] = array('notin', array(C('HQ_CODE')));
        }
        if($shopCode){
            $condition['shopCode'] = $shopCode;
        }else{
            $condition['shopCode'] = array('notin', array(C('HQ_CODE')));
        }
        $condition['readingStatus'] = 0;
        $condition['app'] = $app;
        $condition['unix_timestamp(createTime)'] = array('ELT',time());
        return $this->where($condition)->count('msgCode');
    }

    /**
     * 获得消息记录
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param int $page 页码
     * @param string $staffCode 商家员工编码
     * @return array
     */
    public function getMsg($userCode, $shopCode, $page, $staffCode = ''){
        $condition = array();
        if($userCode){
            $condition['Communication.userCode'] = $userCode;
        }
        if($shopCode){
            $condition['Communication.shopCode'] = $shopCode;
        }
        if($staffCode){
            $condition['Communication.staffCode'] = $staffCode;
        }

        $msgList = $this
            ->field(array('Communication.message', 'Communication.createTime', 'Communication.errorInfo', 'Communication.errorImg', 'Communication.type', 'Communication.toShopCode','Shop.shopName', 'Shop.logoUrl', 'User.nickName'=>'userName','User.avatarUrl'=>'userAvatar','ShopStaff.realName'=>'staffName','ShopStaff.avatarUrl'=>'staffAvatar','Communication.app'=>'from_where'))
            ->join('Shop on Shop.shopCode = Communication.shopCode','LEFT')
            ->join('User on User.userCode = Communication.userCode','LEFT')
            ->join('ShopStaff on ShopStaff.staffCode = Communication.staffCode','LEFT')
            ->where($condition)
            ->order('unix_timestamp(Communication.createTime) desc')
            ->pager($page)
            ->select();
        $count = count($msgList);
        $trueMsgList = array();
        if($msgList){
            for($i = 0; $i < $count; $i++) {
                $trueMsgList[] = $msgList[$count - 1 - $i];
            }
        }
        return $trueMsgList;
    }

    /**
     * 获得消息记录总数
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param string $staffCode 商家员工编码
     * @return mixed
     */
    public function getMsgCount($userCode, $shopCode, $staffCode = ''){
        $condition = array();
        if($userCode){
            $condition['Communication.userCode'] = $userCode;
        }
        if($shopCode){
            $condition['Communication.shopCode'] = $shopCode;
        }
        if($staffCode){
            $condition['Communication.staffCode'] = $staffCode;
        }

        return $this
            ->join('Shop on Shop.shopCode = Communication.shopCode','LEFT')
            ->join('User on User.userCode = Communication.userCode','LEFT')
            ->join('ShopStaff on ShopStaff.staffCode = Communication.staffCode','LEFT')
            ->where($condition)
            ->count('msgCode');
    }


    /**
     * 获取最新一条记录
     * @param $where
     * @param $whereCode
     * @param $groupByWhat
     * @param $page
     * @return mixed
     */
    public function getMsgGroup($where, $whereCode, $groupByWhat, $page){
        $condition['Communication.'.$groupByWhat] = array('NOTIN', array(C('HQ_CODE')));
        $condition['Communication.'.$where] = $whereCode;

        //        判断是否惠圈人员
        if($_SESSION['USER']['bank_id']!=-1){
            $condition['Shop.bank_id'] = $_SESSION['USER']['bank_id'];
        }

        $subQuery = $this
            ->field(array('Communication.shopCode','Communication.staffCode','Communication.userCode','Communication.message','Communication.createTime', 'Communication.errorInfo', 'Communication.errorImg', 'Communication.toShopCode', 'Shop.tel'=>'shopTel', 'Shop.mobileNbr'=>'shopMobileNbr', 'Shop.shopName', 'Shop.logoUrl', 'User.mobileNbr'=>'userMobileNbr', 'User.nickName'=>'userName','User.avatarUrl'=>'userAvatar'))
            ->table('Communication')
            ->join('Shop on Shop.shopCode = Communication.shopCode','LEFT')
            ->join('User on User.userCode = Communication.userCode','LEFT')
            ->where($condition)
            ->order('unix_timestamp(Communication.createTime) desc')
            ->buildSql();
        $msgList = $this
            ->table($subQuery.' a')
            ->group($groupByWhat)
            ->order('unix_timestamp(createTime) desc')
            ->pager($page)
            ->select();
        return $msgList;
    }

    /**
     * 统计记录数
     * @param $where
     * @param $whereCode
     * @param $groupByWhat
     * @return int
     */
    public function countMsgGroup($where, $whereCode, $groupByWhat){
        $condition['Communication.'.$groupByWhat] = array('NOTIN', array(C('HQ_CODE')));
        $condition['Communication.'.$where] = $whereCode;

        //        判断是否惠圈人员
        if($_SESSION['USER']['bank_id']!=-1){
            $condition['Shop.bank_id'] = $_SESSION['USER']['bank_id'];
        }

        $subQuery = $this
            ->field(array('Communication.shopCode','Communication.staffCode','Communication.userCode','Communication.message','Communication.createTime','Shop.shopName','User.nickName'=>'userName','User.avatarUrl'=>'userAvatar'))
            ->table('Communication')
            ->join('Shop on Shop.shopCode = Communication.shopCode','LEFT')
            ->join('User on User.userCode = Communication.userCode','LEFT')
            ->where($condition)
            ->buildSql();
        $msgList = $this
            ->table($subQuery.' a')
            ->group($groupByWhat)
            ->select();
        return count($msgList);
    }

    /**
     * 发送纠错信息
     * @param string $userCode 用户编码
     * @param string $shopCode 商户编码
     * @param string $staffCode 商户员工编码
     * @param string $message 消息
     * @param string $app 发起人
     * @param string $errorInfo 错误信息
     * @param string $errorImg 错误图片
     * @param string $type 消息类型
     * @param string $toShopCode 被反馈的商户编码
     * @return array
     */
    public function addErrorInfo($userCode, $shopCode, $staffCode, $message, $app, $errorInfo, $errorImg, $type, $toShopCode) {
        $rules = array(
            array('userCode', 'require', C('COMMUNICATION.USER')),
            array('shopCode', 'require', C('COMMUNICATION.SHOP')),
            array('errorInfo', 'require', C('COMMUNICATION.ERRORINFO')),
//            array('errorImg', 'require', C('COMMUNICATION.ERRORIMG')),
            array('toShopCode', 'require', C('COMMUNICATION.TOSHOP')),
        );
        $data = array(
            'userCode' => $userCode,
            'shopCode' => $shopCode,
            'errorInfo' => $errorInfo,
            'errorImg' => $errorImg,
            'toShopCode' => $toShopCode,
        );
        if($this->validate($rules)->create($data) != false) {
            $sendInfo = array(
                'msgCode'=>$this->create_uuid(),
                'userCode'=>$userCode,
                'staffCode'=>$staffCode,
                'shopCode'=>$shopCode,
                'message'=>$message,
                'createTime' => date('Y-m-d H:i:s',time()),
                'readingStatus'=>0,
                'app'=>$app,
                'errorInfo' => $errorInfo,
                'errorImg'=>$errorImg,
                'type' => $type,
                'toShopCode' => $toShopCode,
            );
            $code = $this->add($sendInfo) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            // 顾客端发消息而且shopCode等于平台编码
            if($app = 1 && $shopCode == C('HQ_CODE')) {
                // 给向惠圈提反馈建议的用户添加圈值记录，并更新历史圈值和当前圈值
                $userMdl = new UserModel();
                $userMdl->addPointEarningLog($userCode, 10, '意见反馈');
            }
            return $this->getBusinessCode($code);
        } else {
            return $this->getValidErrorCode();
        }
    }
}