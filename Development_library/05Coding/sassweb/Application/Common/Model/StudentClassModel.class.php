<?php
/**
 * Created by PhpStorm.
 * User: Lulu
 * Date: 16-3-4
 * Time: 上午11:39
 */

namespace Common\Model;
use Think\Model;


class StudentClassModel extends BaseModel {
    protected $tableName = 'StudentClass'; // 学员报名课程表

    public function editStudentClass($data){
        $rules = array(
            array('shopSignCode', 'require', C('STUDENT_CLASS.SHOP_SIGN_CODE_EMPTY')),
            array('classCode', 'require', C('STUDENT_CLASS.CLASS_EMPTY')),
        );
        if($this->validate($rules)->create($data) != false) {
            if(isset($data['signCode']) && $data['signCode']){
                $code = $this->where(array('signCode' => $data['signCode']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }else{
                $data['signCode'] = $this->create_uuid();
                $data['signTime'] = date('Y-m-d H:i:s');
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            return array('code' => $code, 'signCode' => $data['signCode']);
        }else{
            return $this->getValidErrorCode();
        }
    }

    public function getStudentClassInfo($condition = array(), $field = array(), $joinTableArr = array()){
        if(empty($field)){
            $field = array('StudentClass.*');
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

    public function getStudentClassList($condition = array(), $field = array(), $joinTableArr = array(), $order = '', $limit = 0, $page = 0){
        if(empty($field)){
            $field = array('StudentClass.*');
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

    public function countStudentClass($condition = array(), $joinTableArr = array()){
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        return $this->count('StudentClass.signCode');
    }

    public function getStudentClassFieldArr($field, $condition = array(), $joinTableArr = array()){
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