<?php
/**
 * Created by PhpStorm.
 * User: Huafei Ji
 * Date: 15-7-8
 * Time: 下午6:38
 */
namespace Common\Model;
use Common\Model\BaseModel;

class DistrictModel extends BaseModel {
    protected $tableName = 'District';
    const NO_PARENT = 0;

    /**
     * 获得区域信息
     * @param array $condition 条件
     * @param array $field 字段
     * @return array $cityInfo
     */
    public function getCityInfo($condition, $field) {
        $cityInfo = $this->field($field)->where($condition)->find();
        return $cityInfo;
    }

    public function getAreaNbrList($field = array(), $condition){
        if(empty($field)){
            $field = array('*');
        }
        $list = $this->field($field)->where($condition)->select();
        return $list;
    }

    /**
     * 获得省的所有城市列表
     * @param string $province 省份
     * @return array $cityNameList 一维数组
     */
    public function getCityList($province) {
        $cityList = $this
            ->table('District as d1')
            ->field(array('d1.name', 'd1.areaNbr'))
            ->join('District AS d2 on d1.parentId = d2.id')
            ->where(array('d2.name' => array('like', '%'.$province.'%')))
            ->order('d1.sortId asc')
            ->select();
        return $cityList;
    }

    /**
     * 获取所有省份名字列表
     * @return array
     */
    public function listProvince() {
        return $this->field(array('name', 'id'))->where(array('parentId' => self::NO_PARENT))->select();
    }

    /**
     * 获得城市名称列表
     * @param array $filterData 查询条件
     * @param object $page
     * @return array
     */
    public function listCity($filterData, $page) {
        $where = $this->filterWhere($filterData, array(), $page);
        $where = $this->filterWhereKey($where);
        return $this
            ->table('District as d1')
            ->field(array('d1.name', 'd1.id', 'd1.isOpen', 'd1.parentId'))
            ->join('District AS d2 on d1.parentId = d2.id')
            ->where($where)
            ->order('d1.parentId asc')
            ->pager($page)
            ->select();
    }

    /**
     * 统计共有多少个城市
     * @param array $filterData 查询条件
     * @return int
     */
    public function countCity($filterData) {
        $where = $this->filterWhere($filterData, array(), $page);
        $where = $this->filterWhereKey($where);
        return $this
            ->table('District as d1')
            ->join('District AS d2 on d1.parentId = d2.id')
            ->where($where)
            ->count('d1.name');
    }

    /**
     * 过滤条件中的字段
     * @param array $where
     * @return array $where
     */
    private function filterWhereKey(&$where) {
        if($where['name'] == ''){
            $provinceIdList = $this->where(array('parentId' => self::NO_PARENT))->getField('id', true);
            $where['d1.parentId'] = array('IN', $provinceIdList);
        } else {
            $where['d2.name'] = $where['name'];
        }
        unset($where['name']);

        if($where['isOpen'] || $where['isOpen'] === '0') {
            $where['d1.isOpen'] = $where['isOpen'];
        } else {
            $where['d1.isOpen'] = C('YES');
        }
        unset($where['isOpen']);
        return $where;
    }

    /**
     * 获取地区名字列表
     * @param int $cityId 城市ID
     * @return array
     */
    public function listDistrict($cityId) {
        return $this->field(array('name', 'id'))->where(array('parentId' => $cityId))->select();
    }

    /**
     * 获取已经开通的城市
     * @return array $cityList
     */
    public function listOpenCity() {
        return $this->field(array('id', 'name'))->where(array('isOpen' => C('YES')))->select();
    }
	
	
	 /**
     * 获取已经开通的城市H5
     */
    public function listOpenCityH5($parentId) {
        return $this->field(array('id', 'name'))->where(array('isOpen' => C('YES'),'parentId'=>$parentId))->select();
    }
	
	
	

    /**
     * 修改城市是否开通的状态
     * @param int $id 城市ID
     * @param int $isOpen 是否开通。1-开通，0-不开通
     * @return boolean 修改成功返回true，修改失败返回false
     */
    public function changeCityStatus($id, $isOpen) {
        return $this->where(array('id' => $id))->save(array('isOpen' => $isOpen)) !== false ? true : false;
    }

    public function getProvinceByCity($city){
        $parentId = $this->getCityInfo(array('name' => array('LIKE', "%$city%")), array('parentId'));
        $province = $this->getCityInfo(array('id'=> array('eq', $parentId['parentId'])), array('name'));
        return isset($province['name']) && $province['name'] ? $province['name'] : '';
    }
}