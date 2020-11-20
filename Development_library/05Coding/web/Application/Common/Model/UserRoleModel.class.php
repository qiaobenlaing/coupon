<?php
namespace Common\Model;
use Think\Model;
/**
 * userRole表
 * @author 
 */
class UserRoleModel extends BaseModel {
    protected $tableName = 'UserRole';
    
    /**
    * 添加数据
    * @param array $userRoleInfo 关联数组
    * @return boolean||string 添加成功返回true；添加失败返回错误信息
    */
    public function addUserRole($userRoleInfo) {
        $rules = array(
            array('userRoleCode', 'require', '提醒'),
            array('userCode', 'require', '提醒'),
            array('roleId', 'require', '提醒'),
            array('remark', 'require', '提醒'),
        );
        if($this->validate($rules)->create() != false) {
            return $this->add($userRoleInfo) !== false ? true : '向数据库添加数据失败';
        } else {
            return $this->getValidErrorCode();
        }
    }
    
    /**
    * 删除数据
    * @param number $userRoleCode 主键
    * @return boolean||string 删除成功返回true；删除失败返回错误信息
    */
    public function delUserRole($userRoleCode) {
        return $this->where(array('userRoleCode' => $userRoleCode))->delete() !== false ? true : '数据库删除数据失败';
    }
    
    /**
    * 数据列表
    * @param array $condition 条件
    * @param $page 分页
    * @return array||boolean 查询成功返回关联数组；查询结果为空返回NULL；查询出错返回false
    */
    public function listUserRole($condition, $page) {
        return $this->where($condition)
                ->pager($page)
                ->select();
    }
    
    /**
    * 根据主键得到数据详情
    * @param number $userRoleCode 主键
    * @return array||boolean 查询成功返回关联数组；查询结果为空返回NULL；查询出错返回false
    */
    public function getUserRole($userRoleCode) {
        return $this->where(array('userRoleCode' => $userRoleCode))->find();
    }
    
    /**
    * 更新数据
    * @param number $userRoleCode 主键
    * @param array $userRoleInfo 关联数组
    * @return boolean||string
    */
    public function updateUserRole($userRoleCode, $userRoleInfo) {
        $rules = array(
            array('userRoleCode', 'require', '提醒'),
            array('userCode', 'require', '提醒'),
            array('roleId', 'require', '提醒'),
            array('remark', 'require', '提醒'),
        );
        if($this->validate($rules)->create() != false) {
            return $this->where(array('userRoleCode' => $userRoleCode))->save($userRoleInfo) !== false ? true : '数据库更新数据失败';
        } else {
            return $this->getValidErrorCode();
        }
    }
}