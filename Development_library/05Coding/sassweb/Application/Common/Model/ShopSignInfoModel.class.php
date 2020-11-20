<?php
/**
 * Created by PhpStorm.
 * User: Lulu
 * Date: 16-3-4
 * Time: 上午11:39
 */

namespace Common\Model;
use Think\Model;


class ShopSignInfoModel extends BaseModel {
    protected $tableName = 'ShopSignInfo'; // 报名信息表

    public function editShopSignInfo($data){
        $rules = array(
            array('shopCode', 'require', C('')),
            array('userCode', 'require', C('')),
            array('studentName', 'require', C('SHOP_SIGN_INFO.STU_NAME_EMPTY')),
            array('studentSex', 'require', C('SHOP_SIGN_INFO.STU_SEX_EMPTY')),
            array('studentBirthday', 'require', C('SHOP_SIGN_INFO.STU_BIRTHDAY_EMPTY')),
            array('studentSchool', 'require', C('SHOP_SIGN_INFO.STU_SCHOOL_EMPTY')),
            array('studentGrade', 'require', C('SHOP_SIGN_INFO.STU_GRADE_EMPTY')),
            array('studentTel', 'require', C('SHOP_SIGN_INFO.STU_TEL_EMPTY')),
        );
        if($this->validate($rules)->create($data) != false) {
            if(isset($data['signCode']) && $data['signCode']){
                $code = $this->where(array('signCode' => $data['signCode']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }else{
                $data['signCode'] = $this->create_uuid();
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            return array('code' => $code, 'signCode' => $data['signCode']);
        }else{
            return $this->getValidErrorCode();
        }
    }

    public function getShopSignInfo($condition = array(), $field = array(), $joinTableArr = array()){
        if(empty($field)){
            $field = array('ShopSignInfo.*');
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

    public function getShopSignList($condition = array(), $field = array(), $joinTableArr = array(), $order = '', $limit = 0, $page = 0){
        if(empty($field)){
            $field = array('ShopSignInfo.*');
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

    public function countShopSign($condition = array(), $joinTableArr = array()){
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        return $this->count('ShopSignInfo.signCode');
    }

    public function getShopSignFieldArr($field, $condition = array(), $joinTableArr = array()){
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