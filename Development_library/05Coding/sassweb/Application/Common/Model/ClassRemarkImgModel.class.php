<?php
/**
 * Created by PhpStorm.
 * User: Lulu
 * Date: 16-3-4
 * Time: 上午11:39
 */

namespace Common\Model;
use Think\Model;


class ClassRemarkImgModel extends BaseModel {
    protected $tableName = 'ClassRemarkImg'; // 班级点评照片

    public function editClassRemarkImg($data){
        $rules = array(
            array('remarkCode', 'require', C('')),
            array('remarkImgUrl', 'require', C('')),
        );
        if($this->validate($rules)->create($data) != false) {
            if(isset($data['remarkImgCode']) && $data['remarkImgCode']){
                $code = $this->where(array('remarkImgCode' => $data['remarkImgCode']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }else{
                $data['remarkImgCode'] = $this->create_uuid();
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            return array('code' => $code, 'remarkImgCode' => $data['remarkImgCode']);
        }else{
            return $this->getValidErrorCode();
        }
    }

    public function getClassRemarkImgInfo($condition = array(), $field = array(), $joinTableArr = array()){
        if(empty($field)){
            $field = array('ClassRemarkImg.*');
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

    public function getClassRemarkImgList($condition = array(), $field = array(), $joinTableArr = array(), $order = '', $limit = 0, $page = 0){
        if(empty($field)){
            $field = array('ClassRemarkImg.*');
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

    public function countClassRemarkImg($condition = array(), $joinTableArr = array()){
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        return $this->count('ClassRemarkImg.remarkImgCode');
    }

    public function getClassRemarkImgFieldArr($field, $condition = array(), $joinTableArr = array()){
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