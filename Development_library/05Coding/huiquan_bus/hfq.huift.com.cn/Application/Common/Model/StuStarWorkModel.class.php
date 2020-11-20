<?php
/**
 * Created by PhpStorm.
 * User: Lulu
 * Date: 16-3-4
 * Time: 上午11:39
 */

namespace Common\Model;
use Think\Model;


class StuStarWorkModel extends BaseModel {
    protected $tableName = 'StuStarWork'; // 学院之星作品表

    public function editStuStarWork($data){
        $rules = array(
            array('starCode', 'require', C('EDUCATION_SHOP_ERROR_CODE.STAR_CODE_EMPTY')),
        );
        if($this->validate($rules)->create($data) != false) {
            if(isset($data['starWorkCode']) && $data['starWorkCode']){
                $code = $this->where(array('starWorkCode' => $data['starWorkCode']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }else{
                $data['starWorkCode'] = $this->create_uuid();
                $data['starUploadTime'] = date('Y-m-d H:i:s');
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            return $this->getBusinessCode($code);
        }else{
            return $this->getValidErrorCode();
        }
    }

    public function getStuStarWorkInfo($condition = array(), $field = array(), $joinTableArr = array()){
        if(empty($field)){
            $field = array('StuStarWork.*');
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

    public function getStuStarWorkList($condition = array(), $field = array(), $joinTableArr = array(), $order = '', $limit = 0, $page = 0){
        if(empty($field)){
            $field = array('StuStarWork.*');
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

    public function countStuStarWork($condition = array(), $joinTableArr = array()){
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        return $this->count('StuStarWork.starWorkCode');
    }

    public function getStuStarWorkFieldArr($field, $condition = array(), $joinTableArr = array()){
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

    public function delStuStarWork($data){
        $code = $this->where($data)->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }
} 