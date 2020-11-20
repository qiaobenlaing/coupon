<?php
namespace Common\Model;
use Think\Model;
/**
 * roleModule表
 * @author 
 */
class RoleModuleModel extends BaseModel {
    protected $tableName = 'RoleModule';
    
    /**
    * 添加数据
    * @param array $roleModuleInfo 关联数组
    * @return boolean||string 添加成功返回true；添加失败返回错误信息
    */
    public function addRoleModule($roleModuleInfo) {
        $rules = array(
            array('roleModuleCode', 'require', '提醒'),
            array('roleId', 'require', '提醒'),
            array('moduleCode', 'require', '提醒'),
            array('authValue', 'require', '提醒'),
            array('remark', 'require', '提醒'),
        );
        if($this->validate($rules)->create() != false) {
            return $this->add($roleModuleInfo) !== false ? true : '向数据库添加数据失败';
        } else {
            return $this->getValidErrorCode();
        }
    }
    
    /**
    * 删除数据
    * @param number $roleModuleCode 主键
    * @return boolean||string 删除成功返回true；删除失败返回错误信息
    */
    public function delRoleModule($roleModuleCode) {
        return $this->where(array('roleModuleCode' => $roleModuleCode))->delete() !== false ? true : '数据库删除数据失败';
    }
    
    /**
    * 数据列表
    * @param array $condition 条件
    * @param $page 分页
    * @return array||boolean 查询成功返回关联数组；查询结果为空返回NULL；查询出错返回false
    */
    public function listRoleModule($condition, $page) {
        return $this->where($condition)
                ->pager($page)
                ->select();
    }
    
    /**
    * 根据主键得到数据详情
    * @param number $roleModuleCode 主键
    * @return array||boolean 查询成功返回关联数组；查询结果为空返回NULL；查询出错返回false
    */
    public function getRoleModule($roleModuleCode) {
        return $this->where(array('roleModuleCode' => $roleModuleCode))->find();
    }
    
    /**
    * 更新数据
    * @param number $roleModuleCode 主键
    * @param array $roleModuleInfo 关联数组
    * @return boolean||string
    */
    public function updateRoleModule($roleModuleCode, $roleModuleInfo) {
        $rules = array(
            array('roleModuleCode', 'require', '提醒'),
            array('roleId', 'require', '提醒'),
            array('moduleCode', 'require', '提醒'),
            array('authValue', 'require', '提醒'),
            array('remark', 'require', '提醒'),
        );
        if($this->validate($rules)->create() != false) {
            return $this->where(array('roleModuleCode' => $roleModuleCode))->save($roleModuleInfo) !== false ? true : '数据库更新数据失败';
        } else {
            return $this->getValidErrorCode();
        }
    }
}