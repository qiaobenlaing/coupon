<?php
/**
 * Created by PhpStorm.
 * User: Lulu
 * Date: 16-3-4
 * Time: 上午11:39
 */

namespace Common\Model;
use Think\Model;


class CourseTableModel extends BaseModel {
    protected $tableName = 'CourseTable'; // 课程表

    public function editCourse($data){
        $rules = array(
            array('tableCode', 'require', C('')),
            array('courseDate', 'require', C('')),
            array('userCode', 'require', C('')),
            array('courseTime', 'require', C('')),
            array('courseAddr', 'require', C('')),
        );
        if($this->validate($rules)->create($data) != false) {
            if(isset($data['courseCode']) && $data['courseCode']){
                $code = $this->where(array('courseCode' => $data['courseCode']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }else{
                $data['courseCode'] = $this->create_uuid();
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            return array('code' => $code, 'courseCode' => $data['courseCode']);
        }else{
            return $this->getValidErrorCode();
        }
    }

    public function getCourseInfo($condition = array(), $field = array(), $joinTableArr = array()){
        if(empty($field)){
            $field = array('CourseTable.*');
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

    public function getCourseList($condition = array(), $field = array(), $joinTableArr = array(), $order = '', $limit = 0, $page = 0){
        if(empty($field)){
            $field = array('CourseTable.*');
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

    public function countCourse($condition = array(), $joinTableArr = array()){
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        return $this->count('CourseTable.courseCode');
    }

    public function getCourseFieldArr($field, $condition = array(), $joinTableArr = array()){
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