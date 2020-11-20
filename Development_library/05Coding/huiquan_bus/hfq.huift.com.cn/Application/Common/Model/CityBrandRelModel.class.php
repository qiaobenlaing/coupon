<?php
namespace Common\Model;
/**
 * Created by PhpStorm
 */
class CityBrandRelModel extends BaseModel {
    protected $tableName = 'CityBrandRel';

    /**
     * 获得商圈列表
     * @param $field
     * @param $where
     * @return array
     */
    public function listRel($field, $where) {
        if(empty($field)){
            $field = array('*');
        }
        return $this
            ->field($field)
            ->join('Brand on Brand.brandId = CityBrandRel.brandId', 'INNER')
            ->where($where)
            ->order('Brand.inputTime asc')
            ->select();
    }

    public function countRel($where) {
        return $this
            ->join('Brand on Brand.brandId = CityBrandRel.brandId', 'INNER')
            ->where($where)
            ->count('cityBrandRelId');
    }

    public function addLinkedRel($cityId, $linkedBrand) {
        // 删除多余的城市
        $condition['cityId'] =  $cityId;
        $this->startTrans();
        $this->where($condition)->delete();
        if($linkedBrand){
            foreach($linkedBrand as $brand) {
                $brand['cityId'] = $cityId;
                $ret = $this->add($brand) !== false ? true : false;
                if($ret == false) {
                    $this->rollback();
                    return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
                }
            }
        }
        $this->commit();
        return $this->getBusinessCode(C('SUCCESS'));
    }

}