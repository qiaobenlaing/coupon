<?php
namespace Common\Model;
/**
 * Created by PhpStorm.
 * User: Jihuafei
 * Date: 15-10-13
 * Time: 下午2:02
 */
class ProductCategoryModel extends BaseModel {
    protected $tableName = 'ProductCategory';

    /**
     * 删除产品类别
     * @param int $categoryId 类别ID
     * @return array $ret
     */
    public function delProductCategory($categoryId) {
        $code = $this->where(array('categoryId' => $categoryId))->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

    /**
     * 获得所有类别列表
     * @param array $condition 条件
     * @param array $field 要查询的字段
     * @param array $joinTable 联合的表
     * @return array $categoryList
     */
    public function getProductCategoryList($condition, $field = array(), $joinTable = array()) {
        $field = $field ? $field : array('ProductCategory.*');
        $this->field($field);
        if($joinTable) {
            foreach($joinTable as $v) {
                $this->join($v['joinTable'] . ' ON ' . $v['joinCondition'], $v['joinType']);
            }
        }
        $categoryList = $this
            ->where($condition)
            ->select();
        return $categoryList;
    }

    /**
     * 获得产品类别信息
     * @param array $condition 条件
     * @param array $field 要查询的字段
     * @param array $joinTable 联合的表
     * @return array $categoryInfo
     */
    public function getProductCategoryInfo($condition, $field = array(), $joinTable = array()) {
        $field = $field ? $field : array('ProductCategory.*');
        $this->field($field);
        if($joinTable) {
            foreach($joinTable as $v) {
                $this->join($v['joinTable'] . ' ON ' . $v['joinCondition'], $v['joinType']);
            }
        }
        $categoryInfo = $this
            ->where($condition)
            ->find();
        return $categoryInfo;
    }

    /**
     * 获得类别总数
     * @param string $shopCode 商家编码
     * @return int $categoryCount
     */
    public function countProductCategory($shopCode) {
        $categoryCount = $this
            ->where(array('shopCode' => $shopCode))
            ->count('categoryId');
        return $categoryCount;
    }

    /**
     * 添加或者修改产品类别
     * @param array $data
     * @return array
     */
    public function editProductCategory($data) {
        $rules = array(
            array('categoryName', 'require', C('PRODUCT_CATEGORY.CATEGORY_NAME_EMPTY')),
            array('shopCode', 'require', C('SHOP.SHOP_CODE_EMPTY')),
            array('categoryId', 'require', C('PRODUCT_CATEGORY.CATEGORY_ID_EMPTY')),
        );
        if($this->validate($rules)->create($data)) {
            if($data['categoryId']) {
                $code = $this->where(array('categoryId' => $data['categoryId']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
                $categoryId = $data['categoryId'];
            } else {
                $categoryId = $this->add($data);
                $code = $categoryId !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            return array(
                'code' => $code,
                'categoryId' => $categoryId !== false ? $categoryId : 0,
            );
        } else {
            return $this->getValidErrorCode();
        }
    }
}