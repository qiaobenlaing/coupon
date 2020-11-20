<?php
namespace Common\Model;
/**
 * Created by PhpStorm.
 */
class CityIndexModuleRelModel extends BaseModel {
    protected $tableName = 'CityIndexModuleRel';

    public function getRelInfo($relId){
        return $this->where(array('relId' => $relId))->find();
    }

    public function getIndexModuleArr($cityId){
        return $this->where(array('cityId' => $cityId))->getField('indexModuleId', true);
    }

    /**
     * 根据搜索条件查询相关记录
     * @param $field
     * @param $where
     * @return mixed
     */
    public function listCityIndexModuleRel($field, $where) {
        if(empty($field)){
            $field = array('*');
        }
        return $this
            ->field($field)
            ->where($where)
            ->join('IndexModule ON IndexModule.id = CityIndexModuleRel.indexModuleId')
            ->order('orderNbr asc, id asc')
            ->select();
    }

    public function countCityIndexModuleRel($where) {
        return $this
            ->where($where)
            ->join('IndexModule ON IndexModule.id = CityIndexModuleRel.indexModuleId')
            ->count('relId');
    }

    /**
     * @param $cityId
     * @param $linkedModule
     * @return bool
     */
    public function addLinkedCity($cityId, $linkedModule) {
        $data['cityId'] = $cityId;
        $data['orderNbr'] = time();
        $this->startTrans();
        foreach($linkedModule as $module) {
            $data['indexModuleId'] = $module;
            $data['orderNbr'] = $data['orderNbr'] + 1;
            $ret = $this->add($data) !== false ? true : false;
            if($ret == false) {
                $this->rollback();
                return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
            }
        }
        $this->commit();
        return $this->getBusinessCode(C('SUCCESS'));
    }

    /**
     * 添加 OR 修改首页模块
     * @param array $data
     * @return array
     */
    public function editRel($data){
        if(isset($data['relId']) && !empty($data['relId'])){
            $code = $this->where(array('relId' => $data['relId']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        }else{
            $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        }
        return $this->getBusinessCode($code);

    }

}