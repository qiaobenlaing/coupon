<?php
namespace Common\Model;
/**
 * Created by PhpStorm.
 * User: Jihuafei
 * Date: 15-10-12
 * Time: 下午5:25
 */
class ShopTypeModel extends BaseModel {
    protected $tableName = 'ShopType';

    /**
     * 获得所有商家类型
     * @return array $shopTypeList
     */
    public function getAllShopTypeList() {
        $shopTypeList = $this->field(array('shopTypeId', 'typeValue', 'typeZh'))->select();
        return $shopTypeList;
    }


    /**
     * 获得所有商家类型(乔本亮管理端saas化)
     * @return array $shopTypeList
     */
    public function getAllShopTypeList2($zoneId) {

        if(!empty($zoneId['bank_id'])){
            $shopTypeList = $this->field(array('shopTypeId', 'typeValue', 'typeZh','bank_id'))->where($zoneId)->select();
        }else{
            $shopTypeList = $this->field(array('shopTypeId', 'typeValue', 'typeZh',"bank_id"))->select();
        }
        return $shopTypeList;
    }

    public function getFieldValue($condition, $field){
        return $this->where($condition)->getField($field, true);
    }


    //检查类型值
    function  checkTypeValue(){
        $typeValue = trim($_REQUEST['typeValue']);
        $bank_id = intval($_REQUEST['bank_id']);
        $res=       $this->where(array("typeValue"=>$typeValue,"bank_id"=>$bank_id))->field("typeValue")->find();
        if($res){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 编辑商家类型
     * @param array $data
     * @return boolean||string $ret 成功返回true，失败返回false或者错误信息
     */
    public function editShopType($data) {
		
        $rules = array(
            array('typeValue', 'require', C('SHOP_TYPE.TYPE_VALUE_EMPTY')),
            array('typeValue', 'checkTypeValue' , "类型值已经存在,请重新填写", 1, 'callback',1),
            array('typeZh', 'require', C('SHOP_TYPE.TYPEZH_EMPTY')),
        );
        if($this->validate($rules)->create($data) != false) {
            if(! empty($data['shopTypeId'])) {
                $ret = $this->where(array('shopTypeId' => $data['shopTypeId']))->save($data) !== false ? true : false;
            }else{
                $ret = $this->add($data)!== false ? true : false;
			
			}
            return $ret;
        } else {
            return $this->getError();
        }
    }

    /**
     * 获得商家类型详情
     * @param array $condition 一维关联数组
     * @param array $field 要查询的字段
     * @return array $shopTypeInfo
     */
    public function getShopTypeInfo($condition, $field) {
        $shopTypeInfo = $this->field($field)->where($condition)->find();
        return $shopTypeInfo;
    }

    /**
     * 获得商家类型列表
     * @param array $filterData
     * @param object $page
     * @return array $shopTypeList 如果为空，返回空数组；
     */
    public function listShopType($filterData, $page) {
        $where = $this->filterWhere(
            $filterData,
            array('typeZh' => 'like'),
            $page);
        $where = $this->secondFilterWhere($where);

        //判断是否为惠圈人员
        if($_SESSION['USER']['bank_id']!=-1){
            $where['bank_id'] = $_SESSION['USER']['bank_id'];
        }

        $shopTypeList = $this
            ->field(array('*'))
            ->where($where)
            ->order('sortNbr asc')
            ->pager($page)
            ->select();
        return $shopTypeList;
    }

    /**
     * 活动商家类型总数
     * @param array $filterData
     * @return array $shopTypeCount 如果为空，返回空数组；
     */
    public function countShopType($filterData) {
        $where = $this->filterWhere(
            $filterData,
            array('typeZh' => 'like'),
            $page);
        $where = $this->secondFilterWhere($where);

        //判断是否为惠圈人员
        if($_SESSION['USER']['bank_id']!=-1){
            $where['bank_id'] = $_SESSION['USER']['bank_id'];
        }

        $shopTypeCount = $this
            ->where($where)
            ->count('shopTypeId');
        return $shopTypeCount;
    }

    /**
     * 第二次过滤条件
     * @param array $where 条件
     * @return array $where
     */
    public function secondFilterWhere(&$where) {
        return $where;
    }

}