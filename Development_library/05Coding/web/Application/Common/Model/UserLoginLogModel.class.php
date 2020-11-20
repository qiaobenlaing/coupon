<?php
namespace Common\Model;
use Think\Model;
/**
 * userLoginLog表
 * @author 
 */
class UserLoginLogModel extends BaseModel {
    protected $tableName = 'UserLoginLog';
    
    /**
    * 添加登录记录
    * @param array $userLoginLogInfo 关联数组
    * @return boolean||string 添加成功返回true；添加失败返回错误信息
    */
    public function addUserLoginLog($userLoginLogInfo) {
        return $this->add($userLoginLogInfo);
    }

}