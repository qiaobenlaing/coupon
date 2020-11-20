<?php
namespace Common\Model;
use Think\Model;
/**
 * Created by PhpStorm.
 * User: huafei.ji
 * Date: 15-8-31
 * Time: 上午9:33
 */
class UserEnterShopInfoRecordModel extends BaseModel{
    protected $tableName = 'UserEnterShopInfoRecord';

    /**
     * 添加用户进入商铺详情的记录
     * @param string $shopCode 商家编码
     * @param string $userCode 用户编码
     * @param int $actionType
     * @return boolean 添加成功返回true，失败返回false
     */
    public function addRecord($shopCode, $userCode, $actionType = 0) {
        $data = array('shopCode' => $shopCode, 'userCode' => $userCode, 'enterTime' => date('Y-m-d H:i:s', time()), 'actionType' => $actionType);
        return $this->add($data) !== false ? true : false;
    }
    
    /**
     * 得到距离当前时间最近的某个用户对某个商户的某个动作
     * @param  $userCode 用户编码
     * @param  $shopCode 商店编码
     * @param  $actionType 动作类型
     * @return array
     */
    public function getRecord($userCode, $shopCode, $actionType) {
        $result = $this->where(array('shopCode' => $shopCode, 'userCode' => $userCode, 'actionType' => $actionType))->order('enterTime desc')->find();
        return $result;
    }
    
    /**
     * 得到商户总操作量
     * @param  $where 查询条件
     * @param  $field 统计字段
     * @return number
     */
    public function getBrowseQuantity($where, $field) {
        return $this->where($where)->count($field);        
    }

    /**
     * 获得最近访问商户详情的用户
     * @param string $shopCode 商户编码
     * @param int $limit 数量
     * @return array $userList
     */
    public function getShopRecentVisitor($shopCode, $limit) {
        $userList = $this
            ->field(array('User.avatarUrl', 'User.nickName'))
            ->join('User ON User.userCode = UserEnterShopInfoRecord.userCode')
            ->where(array('shopCode' => $shopCode))
            ->group('User.userCode')
            ->order('enterTime desc')
            ->limit($limit)
            ->select();
        return $userList;
    }
}