<?php
/**
 * Created by PhpStorm.
 * User: Lulu
 * Date: 16-3-4
 * Time: 上午11:39
 */

namespace Common\Model;
use Think\Model;


class ShopHonorModel extends BaseModel {
    protected $tableName = 'ShopHonor'; // 荣誉表

    public function editShopHonor($data){
        $rules = array(
            array('shopCode', 'require', C('SHOP.SHOP_CODE_EMPTY')),
            array('honorUrl', 'require', C('EDUCATION_SHOP_ERROR_CODE.SHOP_HONOR_IMG_EMPTY')),
        );
        if($this->validate($rules)->create($data) != false) {
            if(isset($data['honorCode']) && $data['honorCode']){
                $code = $this->where(array('honorCode' => $data['honorCode']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }else{
                $data['honorCode'] = $this->create_uuid();
                $data['uploadTime'] = date('Y-m-d H:i:s');
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            return array('code' => $code, 'honorCode' => $data['honorCode']);
        }else{
            return $this->getValidErrorCode();
        }
    }

    public function getShopHonorInfo($condition = array(), $field = array(), $joinTableArr = array()){
        if(empty($field)){
            $field = array('ShopHonor.*');
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

    public function getShopHonorList($condition = array(), $field = array(), $joinTableArr = array(), $order = '', $limit = 0, $page = 0){
        if(empty($field)){
            $field = array('ShopHonor.*');
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

    public function countShopHonor($condition = array(), $joinTableArr = array()){
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        return $this->count('ShopHonor.honorCode');
    }

    public function getShopHonorFieldArr($field, $condition = array(), $joinTableArr = array()){
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

    public function delShopHonor($data){
        $code = $this->where($data)->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

} 