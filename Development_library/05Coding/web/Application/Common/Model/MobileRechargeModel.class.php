<?php
/**
 * Created by PhpStorm.
 * User: Lulu
 * Date: 16-3-4
 * Time: 上午11:39
 */

namespace Common\Model;
use Think\Model;


class MobileRechargeModel extends BaseModel {
    protected $tableName = 'MobileRecharge'; // 话费流量充值表

    public function editMobileRecharge($data){
        $rules = array(
            array('userCode', 'require', C('')),
            array('shopCode', 'require', C('')),
            array('rechargeType', 'require', C('')),
            array('rechargeValue', 'require', C('')),
            array('payAmt', 'require', C('')),
            array('mobileNbr', 'require', C('')),
        );
        if($this->validate($rules)->create($data) != false) {
            if(isset($data['rechargeCode']) && $data['rechargeCode']){
                $code = $this->where(array('rechargeCode' => $data['rechargeCode']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }else{
                $data['rechargeCode'] = $this->create_uuid();
                $data['rechargeTime'] = date('Y-m-d H:i:s');
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            return array('code' => $code, 'rechargeCode' => $data['rechargeCode']);
        }else{
            return $this->getValidErrorCode();
        }
    }

    public function getMobileRechargeInfo($condition = array(), $field = array(), $joinTableArr = array()){
        if(empty($field)){
            $field = array('MobileRecharge.*');
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
        return $this->find();
    }

    public function getMobileRechargeList($condition = array(), $field = array(), $joinTableArr = array(), $order = '', $limit = 0, $page = 0){
        if(empty($field)){
            $field = array('MobileRecharge.*');
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
        return $this->select();
    }

    public function countMobileRecharge($condition = array(), $joinTableArr = array()){
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        return $this->count('MobileRecharge.rechargeCode');
    }

    public function getMobileRechargeFieldArr($field, $condition = array(), $joinTableArr = array()){
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        return $this->getField($field, true);
    }
} 