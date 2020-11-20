<?php
/**
 * Created by PhpStorm.
 * User: Lulu
 * Date: 16-3-4
 * Time: 上午11:39
 */

namespace Common\Model;
use Think\Model;


class CourseMainTableModel extends BaseModel {
    protected $tableName = 'CourseMainTable'; // 课程主表

    public function editMainCourse($data){
        $rules = array(
            array('userCode', 'require', C('')),
            array('courseName', 'require', C('')),
            array('courseStartDate', 'require', C('')),
            array('courseEndDate', 'require', C('')),
            array('weekStep', 'require', C('')),
        );
        if($this->validate($rules)->create($data) != false) {
            if(isset($data['tableCode']) && $data['tableCode']){
                $code = $this->where(array('tableCode' => $data['tableCode']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }else{
                $data['tableCode'] = $this->create_uuid();
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            return array('code' => $code, 'tableCode' => $data['tableCode']);
        }else{
            return $this->getValidErrorCode();
        }
    }

    public function getMainCourseInfo($condition = array(), $field = array(), $joinTableArr = array()){
        if(empty($field)){
            $field = array('CourseMainTable.*');
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

    public function getMainCourseList($condition = array(), $field = array(), $joinTableArr = array(), $order = '', $limit = 0, $page = 0){
        if(empty($field)){
            $field = array('CourseMainTable.*');
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

    public function countMainCourse($condition = array(), $joinTableArr = array()){
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        return $this->count('CourseMainTable.tableCode');
    }

    public function getMainCourseFieldArr($field, $condition = array(), $joinTableArr = array()){
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