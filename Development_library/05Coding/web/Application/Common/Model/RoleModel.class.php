<?php
namespace Common\Model;
use Think\Model;
class RoleModel extends BaseModel {
    protected $tableName = 'Role';
    
    /**
    * 添加数据
    * @param array $roleInfo 关联数组
    * @return boolean||string 添加成功返回true；添加失败返回错误信息
    */
    public function addRole($roleInfo) {
        $rules = array(
            array('roleId', 'require', '提醒'),
            array('roleName', 'require', '提醒'),
            array('roleValue', 'require', '提醒'),
            array('roleType', 'require', '提醒'),
            array('roleDes', 'require', '提醒'),
            array('parentRole', 'require', '提醒'),
            array('remark', 'require', '提醒'),
        );
        if($this->validate($rules)->create() != false) {
            return $this->add($roleInfo) !== false ? true : '向数据库添加数据失败';
        } else {
            return $this->getValidErrorCode();
        }
    }
    
    /**
    * 删除数据
    * @param number $roleCode 主键
    * @return boolean||string 删除成功返回true；删除失败返回错误信息
    */
    public function delRole($roleCode) {
        return $this->where(array('roleCode' => $roleCode))->delete() !== false ? true : '数据库删除数据失败';
    }
    
    /**
    * 数据列表
    * @param array $condition 条件
    * @param $page 分页
    * @return array||boolean 查询成功返回关联数组；查询结果为空返回NULL；查询出错返回false
    */
    public function listRole($condition, $page) {
        return $this->where($condition)
                ->pager($page)
                ->select();
    }
    
    /**
    * 根据主键得到数据详情
    * @param number $roleCode 主键
    * @return array||boolean 查询成功返回关联数组；查询结果为空返回NULL；查询出错返回false
    */
    public function getRole($roleCode) {
        return $this->where(array('roleCode' => $roleCode))->find();
    }
    
    /**
    * 更新数据
    * @param number $roleCode 主键
    * @param array $roleInfo 关联数组
    * @return boolean||string
    */
    public function updateRole($roleCode, $roleInfo) {
        $rules = array(
            array('roleId', 'require', '提醒'),
            array('roleName', 'require', '提醒'),
            array('roleValue', 'require', '提醒'),
            array('roleType', 'require', '提醒'),
            array('roleDes', 'require', '提醒'),
            array('parentRole', 'require', '提醒'),
            array('remark', 'require', '提醒'),
        );
        if($this->validate($rules)->create() != false) {
            return $this->where(array('roleCode' => $roleCode))->save($roleInfo) !== false ? true : '数据库更新数据失败';
        } else {
            return $this->getValidErrorCode();
        }
    }
}