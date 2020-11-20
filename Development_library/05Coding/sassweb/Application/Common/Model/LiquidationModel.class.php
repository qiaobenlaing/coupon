<?php
namespace Common\Model;
use Think\Model;
use Think\Page;
/**
 * Created by PhpStorm.
 * User: yufeng.jiang
 * Date: 15-11-23
 * Time: 下午14:41
 */
class LiquidationModel extends BaseModel {
    protected $tableName = 'Liquidation';


    /**
     * 新增或修改清算记录
     * @param array $data 修改的数据
     * @param array $where 查询条件
     * @return array
     */
    public function editLiquidationRecord($data, $where = array()){
        if($where) {
            $code = $this->where($where)->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        } else {
            $data['createTime'] = time();
            $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        }
        return $this->getBusinessCode($code);
    }

    /**
     * @param array $condition
     * @param array $field
     * @param array $joinTableArr
     * @param string $order
     * @param int $limit
     * @param int $page
     * @return mixed
     */
    public function listLiquidation($condition = array(), $field = array(), $joinTableArr = array(), $order = '', $limit = 0, $page = 0){
        if(empty($field)){
            $field = array('*');
        }
        $this->field($field);
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        if($order){
            $this->order($order);
        }
        if($limit){
            $this->limit($limit);
        }
        if($page){
            $this->page($page);
        }
        $shopList = $this->select();
        return $shopList;
    }

    public function countLiquidation($condition = array(), $joinTableArr = array()){
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        $shopList = $this->count('Liquidation.id');
        return $shopList;
    }


    public function getLiquidationInfo($condition, $field = array(), $joinTableArr = array(), $order = '') {
        if(empty($field)){
            $field = array('Liquidation.*');
        }
        $this->field($field);
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        if($order){
            $this->order($order);
        }
        return $this->find();
    }
}