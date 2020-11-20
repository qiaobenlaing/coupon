<?php
/**
 * Created by PhpStorm.
 * User: Jihuafei
 * Date: 15-11-30
 * Time: 下午6:22
 */
namespace Common\Model;
class PPRelModel extends BaseModel {
    protected $tableName = 'PPRel';

    /**
     * 获得套餐中包含的产品
     * @param string $packageId 套餐ID
     * @return array $productList 产品列表
     */
    public function getPkgPList($packageId) {
        $productList = $this
            ->field(array('PPRel.productId', 'productNbr', 'productName'))
            ->join('Product ON Product.productId = PPRel.productId')
            ->where(array('packageId' => $packageId))
            ->select();
        return $productList;
    }

    /**
     * 添加或者编辑套餐中的单品
     * @param array $condition 条件。例：{'id' => '2'}
     * @param array $data 产品的信息
     * @return array
     */
    public function editPPRel($condition, $data) {
        if($condition) {
            $ret = $this->where($condition)->save($data);
            $id = $data['id'];
        } else {
            $ret = $this->add($data);
            $id = $ret;
        }
        $code = $ret !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return array('code' => $code, 'id' => $id);
    }

    /**
     * 获得详情信息
     * @param array $condition 条件。例：{'id' => '2'}
     * @return array $info
     */
    public function getPPRelInfo($condition) {
        $info = $this->where($condition)->find();
        return $info;
    }

    /**
     * 获得详情列表
     * @param array $condition 条件。例：{'id' => '2'}
     * @return array $info
     */
    public function getPPRelList($condition) {
        $info = $this->where($condition)->select();
        return $info;
    }

}