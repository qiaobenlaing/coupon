<?php
namespace Common\Model;
use Think\Model;
/**
 * authorization表
 * @author 
 */
class AuthorizationModel extends BaseModel {
    protected $tableName = 'Authorization';

    /**
     * 编辑权限
     * @param $authorizationInfo
     * @return array
     */
    public function editAuthorization($authorizationInfo) {
        $authorizationInfo = (array)$authorizationInfo;
        $update_key = array_keys($authorizationInfo);
        $rules = array();
        if($update_key){
            foreach($update_key as $v){
                $rules[] = array($v, 'require', C('UPDATE_INFO.EMPTY'));
            }
        }

        if(empty($rules)){
            return $this->getBusinessCode(C('UPDATE_INFO.NO_UPDATE'));
        }

        if($this->validate($rules)->create($authorizationInfo) != false) {
            if(isset($authorizationInfo['authorizationId'])){
                $code = $this->where(array('authorizationId' => $authorizationInfo['authorizationId']))
                    ->save($authorizationInfo) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }else{
                $authorizationInfo['authorizationId'] = $this->create_uuid();
                $code = $this->add($authorizationInfo) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }

            return $this->getBusinessCode($code);
        } else {
            return $this->getValidErrorCode();
        }
    }


    /**
     * @param $authorizationId
     * @return bool|string
     */
    public function delAuthorization($authorizationId) {
        $code = $this->where(array('authorizationId' => $authorizationId))->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }
    
    /**
    * 数据列表
    * @param array $condition 条件
    * @param $page 分页
    * @return array||boolean 查询成功返回关联数组；查询结果为空返回NULL；查询出错返回false
    */
    public function listAuthorization($condition, $page) {
        return $this->where($condition)
                ->pager($page)
                ->select();
    }
    
    /**
    * 根据主键得到数据详情
    * @param number $authorizationCode 主键
    * @return array||boolean 查询成功返回关联数组；查询结果为空返回NULL；查询出错返回false
    */
    public function getAuthorization($authorizationId) {
        return $this->where(array('authorizationId' => $authorizationId))->find();
    }
}