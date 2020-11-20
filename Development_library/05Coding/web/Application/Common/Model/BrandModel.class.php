<?php
namespace Common\Model;
/**
 * Created by PhpStorm.
 * User: jihuafei
 * Date: 15-11-3
 * Time: 下午5:03
 */
class BrandModel extends BaseModel {
    protected $tableName = 'Brand';

    /**
     * 编辑品牌
     * @param array $data
     * @return array
     */
    public function editBrand($data) {
        $rules = array(
            array(''),
        );
        if(empty($data['brandId'])) {
            $data['inputTime'] = date('Y-m-d H:i:s');
            $ret = $this->add($data);
            $data['brandId'] = $ret;
        } else {
            $ret = $this->where(array('brandId' => $data['brandId']))->save($data);
        }
        $code = $ret === false ? C('API_INTERNAL_EXCEPTION') : C('SUCCESS');
        return array('code' => $code, 'brandId' => $data['brandId']);
    }

    /**
     * 获得品牌列表
     * @param array $where 条件
     * @param array $field 查询字段
     * @return array
     */
    public function getBrandList($where, $field) {
        $where = $this->filterWhere($where);
        $brandList = $this->field($field)->where($where)->select();
        return $brandList;
    }

    /**
     * 获得品牌信息
     * @param array $where 条件
     * @param array $field 查询字段
     * @return array
     */
    public function getBrandInfo($where, $field) {
        $where = $this->filterWhere($where);
        $brandInfo = $this->field($field)->where($where)->find();
        return $brandInfo;
    }

    /**
     * 第二次过滤条件
     * @param array $where 条件
     * @return array $where
     */
    private function secondFilter(&$where) {
        return $where;
    }

    /**
     * 管理端用户列表
     * @param array $filterData
     * @param number $page 偏移值
     * @return null|array 如果为空，返回null；如果不为空，返回二维数组array(array());
     */
    public function listBrand($filterData, $page) {
        $where = $this->filterWhere(
            $filterData,
            array('brandName' => 'like'),
            $page);
        $where = $this->secondFilter($where);
        return $this
            ->field(array('*'))
            ->where($where)
            ->pager($page)
            ->select();
    }

    /**
     * 管理端用户总数
     * @param array $filterData
     * @return null|int 如果为空，返回null；如果不为空，返回number;
     */
    public function countBrand($filterData) {
        $where = $this->filterWhere(
            $filterData,
            array('brandName' => 'like')
        );
        $where = $this->secondFilter($where);
        return $this
            ->where($where)
            ->count('brandId');
    }

}