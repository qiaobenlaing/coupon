<?php
/**
 * Created by PhpStorm.
 * User: Lulu
 * Date: 16-3-4
 * Time: 上午11:39
 */

namespace Common\Model;
use Think\Model;


class ShopClassModel extends BaseModel {
    protected $tableName = 'ShopClass'; // 开班表

    public function editShopClass($data){
        $rules = array(
            array('shopCode', 'require', C('SHOP.SHOP_CODE_EMPTY')),
            array('teacherCode', 'require', C('EDUCATION_SHOP_ERROR_CODE.TEACHER_CODE_EMPTY')),
            array('className', 'require', C('EDUCATION_SHOP_ERROR_CODE.CLASS_NAME_EMPTY')),
            array('learnStartDate', 'require', C('EDUCATION_SHOP_ERROR_CODE.LEARN_START_DATE_EMPTY')),
            array('learnEndDate', 'require', C('EDUCATION_SHOP_ERROR_CODE.LEARN_END_DATE_EMPTY')),
            array('learnMemo', 'require', C('EDUCATION_SHOP_ERROR_CODE.LEARN_MEMO_EMPTY')),
            array('learnFee', 'require', C('EDUCATION_SHOP_ERROR_CODE.LEARN_FEE_EMPTY')),
            array('learnNum', 'require', C('EDUCATION_SHOP_ERROR_CODE.LEARN_NUM_EMPTY')),
            array('signStartDate', 'require', C('EDUCATION_SHOP_ERROR_CODE.SIGN_START_DATE_EMPTY')),
            array('signEndDate', 'require', C('EDUCATION_SHOP_ERROR_CODE.SIGN_END_DATE_EMPTY')),
        );
        if($this->validate($rules)->create($data) != false) {
            if(isset($data['classCode']) && $data['classCode']){
                $code = $this->where(array('classCode' => $data['classCode']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }else{
                $data['classCode'] = $this->create_uuid();
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            return array('code' => $code, 'classCode' => $data['classCode']);
        }else{
            return $this->getValidErrorCode();
        }
    }

    public function getShopClassInfo($condition = array(), $field = array(), $joinTableArr = array()){
        if(empty($field)){
            $field = array('ShopClass.*');
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

    public function getShopClassList($condition = array(), $field = array(), $joinTableArr = array(), $order = '', $limit = 0, $page = 0){
        if(empty($field)){
            $field = array('ShopClass.*');
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

    public function countShopClass($condition = array(), $joinTableArr = array()){
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        return $this->count('ShopClass.classCode');
    }

    public function getShopClassFieldArr($field, $condition = array(), $joinTableArr = array()){
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

    public function delShopClass($data){
        $code = $this->where($data)->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

} 