<?php
namespace Common\Model;
/**
 * Created by PhpStorm.
 */
class ShopTradingAreaRelModel extends BaseModel {
    protected $tableName = 'ShopTradingAreaRel';

    /**
     * 获得商圈列表
     * @param $field
     * @param $where
     * @return array
     */
    public function listShopTradingAreaRel($field, $where) {
        if(empty($field)){
            $field = array('*');
        }
        return $this
            ->field($field)
            ->where($where)
            ->select();
    }

    public function arrSTAR($field, $where) {
        return $this
            ->where($where)
            ->getField($field, true);
    }


    public function addLinkedShopTradingAreaRel($shopCode, $linkedTradingArea) {
        // 删除多余的城市
        $condition['shopCode'] =  $shopCode;
        $this->startTrans();
        $this->where($condition)->delete();
        $data['shopCode'] = $shopCode;
        foreach($linkedTradingArea as $ta) {
            $data['subModuleId'] = $ta;
            $ret = $this->add($data) !== false ? true : false;
            if($ret == false) {
                $this->rollback();
                return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
            }

        }
        $this->commit();
        return $this->getBusinessCode(C('SUCCESS'));
    }

}