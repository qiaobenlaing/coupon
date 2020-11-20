<?php
namespace Common\Model;
use Think\Model;


class IndexModuleModel extends BaseModel{
    protected $tableName = 'IndexModule';

    /**
     * 获取首页模块列表
     */
    public function getIndexModuleList($where = array()){
        if(!empty($where)){
            return $this
                ->where($where)
                ->order('id asc')
                ->select();
        }
        return $this
            ->order('id asc')
            ->select();
    }

    public function CountIndexModule($where = array()){
        if(!empty($where)){
            return $this
                ->where($where)
                ->count('id');
        }
        return $this
            ->count('id');
    }

    public function getIndexModuleInfo($id){
        return $this
            ->where(array('id' => $id))
            ->find();
    }

    /**
     * 添加 OR 修改首页模块
     * @param array $data
     * @return array
     */
    public function editIndexModule($data){
        $rules = array(
            array('moduleTitle', 'require', C('INDEX_MODULE.MODULE_TITLE_EMPTY')),
            array('moduleValue', 'require', C('INDEX_MODULE.MODULE_VALUE_EMPTY'))
        );
        if($this->validate($rules)->create($data) != false) {
            $list = $this->getIndexModuleList(array('moduleValue' => $data['moduleValue']));
            if($list){
                return $this->getBusinessCode(C('INDEX_MODULE.MODULE_VALUE_REPEAT'));
            }
            if(isset($data['id']) && !empty($data['id'])){
                $code = $this->where(array('id' => $data['id']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }else{
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            return $this->getBusinessCode($code);
        } else {
            return $this->getValidErrorCode();
        }
    }

}