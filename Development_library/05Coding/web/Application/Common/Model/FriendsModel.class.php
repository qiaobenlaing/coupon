<?php
namespace Common\Model;
use Think\Model;
/**
 * friends表
 * @author 
 */
class FriendsModel extends BaseModel {
    protected $tableName = 'Friends';
    
    /**
    * 添加数据
    * @param array $friendsInfo 关联数组
    * @return boolean||string 添加成功返回true；添加失败返回错误信息
    */
    public function addFriends($friendsInfo) {
        $rules = array(
            array('friendCode', 'require', '提醒'),
            array('userFriendCode', 'require', '提醒'),
            array('userBasicCode', 'require', '提醒'),
            array('sharedCoupon', 'require', '提醒'),
            array('saledCoupon', 'require', '提醒'),
            array('createTime', 'require', '提醒'),
        );
        if($this->validate($rules)->create() != false) {
            return $this->add($friendsInfo) !== false ? true : '向数据库添加数据失败';
        } else {
            return $this->getValidErrorCode();
        }
    }
    
    /**
    * 删除数据
    * @param number $friendsCode 主键
    * @return boolean||string 删除成功返回true；删除失败返回错误信息
    */
    public function delFriends($friendsCode) {
        return $this->where(array('friendsCode' => $friendsCode))->delete() !== false ? true : '数据库删除数据失败';
    }
    
    /**
    * 数据列表
    * @param array $condition 条件
    * @param $page 分页
    * @return array||boolean 查询成功返回关联数组；查询结果为空返回NULL；查询出错返回false
    */
    public function listFriends($condition, $page) {
        return $this->where($condition)
                ->pager($page)
                ->select();
    }
    
    /**
    * 根据主键得到数据详情
    * @param number $friendsCode 主键
    * @return array||boolean 查询成功返回关联数组；查询结果为空返回NULL；查询出错返回false
    */
    public function getFriends($friendsCode) {
        return $this->where(array('friendsCode' => $friendsCode))->find();
    }
    
    /**
    * 更新数据
    * @param number $friendsCode 主键
    * @param array $friendsInfo 关联数组
    * @return boolean||string
    */
    public function updateFriends($friendsCode, $friendsInfo) {
        $rules = array(
            array('friendCode', 'require', '提醒'),
            array('userFriendCode', 'require', '提醒'),
            array('userBasicCode', 'require', '提醒'),
            array('sharedCoupon', 'require', '提醒'),
            array('saledCoupon', 'require', '提醒'),
            array('createTime', 'require', '提醒'),
        );
        if($this->validate($rules)->create() != false) {
            return $this->where(array('friendsCode' => $friendsCode))->save($friendsInfo) !== false ? true : '数据库更新数据失败';
        } else {
            return $this->getValidErrorCode();
        }
    }
}