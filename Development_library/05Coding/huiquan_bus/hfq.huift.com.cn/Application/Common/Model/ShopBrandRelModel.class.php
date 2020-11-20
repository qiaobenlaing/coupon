<?php
/**
 * Created by PhpStorm.
 * User: Jihuafei
 * Date: 15-11-19
 * Time: 下午5:18
 */

namespace Common\Model;


class ShopBrandRelModel extends BaseModel {
    protected $tableName = 'ShopBrandRel';

    /**
     * 删除关系
     * @param array $condition
     * @return array
     */
    public function delRel($condition) {
        $code = $this->where($condition)->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

    /**
     * 获得某个字段值
     * @param string $field 字段值
     * @param array $where 条件
     * @return array 一维索引数组
     */
    public function arrCBR($field, $where) {
        return $this
            ->where($where)
            ->getField($field, true);
    }

    /**
     * 保存关系
     * @param array $data
     * @return array
     */
    public function editRel($data) {
        if($data['shopBrandRelId']) {
            $code = $this->where(array('shopBrandRelId' => $data['shopBrandRelId']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        } else {
            $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        }
        return $this->getBusinessCode($code);
    }

    /**
     * 获得关系信息
     * @param array $condition 条件
     * @param array $field 字段
     * @return array
     */
    public function getRelList($condition, $field) {
        return $this
            ->field($field)
            ->join('Shop ON Shop.shopCode = ShopBrandRel.shopCode')
            ->join('Brand ON Brand.brandId = ShopBrandRel.brandId')
            ->where($condition)
            ->select();
    }

    /**
     * 获得关系详情
     * @param array $condition 条件
     * @param array $field 字段
     * @return array
     */
    public function getRelInfo($condition, $field) {
        return $this
            ->field($field)
            ->join('Shop ON Shop.shopCode = ShopBrandRel.shopCode')
            ->join('Brand ON Brand.brandId = ShopBrandRel.brandId')
            ->where($condition)
            ->find();
    }

} 