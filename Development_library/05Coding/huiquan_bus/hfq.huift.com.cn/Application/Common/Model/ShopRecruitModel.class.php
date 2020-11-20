<?php
/**
 * Created by PhpStorm.
 * User: Lulu
 * Date: 16-3-4
 * Time: 上午11:39
 */

namespace Common\Model;
use Think\Model;


class ShopRecruitModel extends BaseModel {
    protected $tableName = 'ShopRecruit'; // 招生启示表

    public function editShopRecruit($data){
        $rules = array(
            array('shopCode', 'require', C('SHOP.SHOP_CODE_EMPTY')),
            array('advUrl', 'require', C('EDUCATION_SHOP_ERROR_CODE.ADV_IMG_EMPTY')),
            array('recruitUrl', 'require', C('EDUCATION_SHOP_ERROR_CODE.RECRUIT_IMG_EMPTY')),
        );
        if($this->validate($rules)->create($data) != false) {
            if(isset($data['recruitCode']) && $data['recruitCode']){
                $code = $this->where(array('recruitCode' => $data['recruitCode']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }else{
                $data['recruitCode'] = $this->create_uuid();
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            return array('code' => $code, 'recruitCode' => $data['recruitCode']);
        }else{
            return $this->getValidErrorCode();
        }
    }

    public function getShopRecruitInfo($condition = array(), $field = array(), $joinTableArr = array()){
        if(empty($field)){
            $field = array('ShopRecruit.*');
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

    public function getShopRecruitList($condition = array(), $field = array(), $joinTableArr = array(), $order = '', $limit = 0, $page = 0){
        if(empty($field)){
            $field = array('ShopRecruit.*');
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

    public function countShopRecruit($condition = array(), $joinTableArr = array()){
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        return $this->count('ShopRecruit.recruitCode');
    }

    public function getShopRecruitFieldArr($field, $condition = array(), $joinTableArr = array()){
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