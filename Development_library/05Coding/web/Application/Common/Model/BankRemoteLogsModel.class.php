<?php
namespace Common\Model;
use Think\Model;
/**
 * bankRemoteLogs表
 * @author 
 */
class BankRemoteLogsModel extends BaseModel {
    protected $tableName = 'BankRemoteLogs';
    
    /**
    * 添加数据
    * @param array $bankRemoteLogsInfo 关联数组
    * @return boolean||string 添加成功返回true；添加失败返回错误信息
    */
    public function addBankRemoteLogs($bankRemoteLogsInfo) {
        $rules = array(
            array('logCode', 'require', '提醒'),
            array('title', 'require', '提醒'),
            array('content', 'require', '提醒'),
            array('actionTime', 'require', '提醒'),
        );
        if($this->validate($rules)->create() != false) {
            return $this->add($bankRemoteLogsInfo) !== false ? true : '向数据库添加数据失败';
        } else {
            return $this->getValidErrorCode();
        }
    }
    
    /**
    * 删除数据
    * @param number $logCode 主键
    * @return boolean||string 删除成功返回true；删除失败返回错误信息
    */
    public function delBankRemoteLogs($logCode) {
        return $this->where(array('logCode' => $logCode))->delete() !== false ? true : '数据库删除数据失败';
    }
    
    /**
    * 数据列表
    * @param array $condition 条件
    * @param $page 分页
    * @return array||boolean 查询成功返回关联数组；查询结果为空返回NULL；查询出错返回false
    */
    public function listBankRemoteLogs($condition, $page) {
        return $this->where($condition)
                ->pager($page)
                ->select();
    }
    
    /**
    * 根据主键得到数据详情
    * @param number $logCode 主键
    * @return array||boolean 查询成功返回关联数组；查询结果为空返回NULL；查询出错返回false
    */
    public function getBankRemoteLogs($logCode) {
        return $this->where(array('logCode' => $logCode))->find();
    }
    
    /**
    * 更新数据
    * @param number $logCode 主键
    * @param array $bankRemoteLogsInfo 关联数组
    * @return boolean||string
    */
    public function updateBankRemoteLogs($logCode, $bankRemoteLogsInfo) {
        $rules = array(
            array('logCode', 'require', '提醒'),
            array('title', 'require', '提醒'),
            array('content', 'require', '提醒'),
            array('actionTime', 'require', '提醒'),
        );
        if($this->validate($rules)->create() != false) {
            return $this->where(array('logCode' => $logCode))->save($bankRemoteLogsInfo) !== false ? true : '数据库更新数据失败';
        } else {
            return $this->getValidErrorCode();
        }
    }
}