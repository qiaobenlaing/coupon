<?php
/**
 * Created by PhpStorm.
 * User: Huafei.ji
 * Date: 16-2-19
 * Time: 上午10:24
 */

namespace Common\Model;


class ShopTypeRelModel extends BaseModel {
    protected $tableName = 'ShopTypeRel';

    /**
     * 补充商户类型关系
     * @param array $shopList
     */
    public function addShopTypeRel($shopList) {
        foreach($shopList as $v) {
            if($v['type'] == 11) {
                $typeId = 6;
            } elseif($v['type'] == 21) {
                $typeId = 7;
            } elseif($v['type'] == 0) {
                $typeId = 8;
            } elseif($v['type'] == 6) {
                $typeId = 5;
            } else {
                $typeId = $v['type'];
            }
            $data = array(
                'relCode' => $this->create_uuid(),
                'shopCode' => $v['shopCode'],
                'typeId' => $typeId
            );
            $this->add($data);
        }
    }

    /**
     * 获得商户的所属类型
     * @param string $shopCode 商户编码
     * @return array $shopTypeList
     */
    public function getShopType($shopCode) {
        $shopTypeList = $this
            ->join('ShopType ON ShopType.shopTypeId = ShopTypeRel.typeId')
            ->where(array('shopCode' => $shopCode))
            ->getField('typeValue', true);
        return $shopTypeList;
    }

    /**
     * 保存商家类型
     * @param array $arrTypeId 商家类型ID，一维索引数组
     * @param string $shopCode 商家编码
     * @return boolean
     */
    public function saveShopTypeRel($arrTypeId, $shopCode) {
        $ret = true;
        $this->where(array('typeId' => array('notin', $arrTypeId), 'shopCode' => $shopCode))->delete();
        $typeIdList = $this->where(array('shopCode' => $shopCode))->getField('typeId', true);
        $typeIdList = $typeIdList ? $typeIdList : array();
        $typeDiff = array_diff($arrTypeId, $typeIdList);
        foreach($typeDiff as $v) {
            $ret = $this->add(array('relCode' => $this->create_uuid(), 'shopCode' => $shopCode, 'typeId' => $v)) !== false ? true : false;
        }
        return $ret;
    }

    public function getFieldList($condition = array(), $field, $joinTableArr = array()){
        if($condition){
            $this->where($condition);
        }
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        return $this->getField($field, true);
    }
} 