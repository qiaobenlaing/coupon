<?php
/**
 * Created by PhpStorm.
 * User: Lulu
 * Date: 16-3-4
 * Time: 上午11:39
 */

namespace Common\Model;
use Think\Model;


class TeacherWorkModel extends BaseModel {
    protected $tableName = 'TeacherWork'; // 教师作品表

    public function editTeacherWork($data){
        $rules = array(
            array('teacherCode', 'require', C('EDUCATION_SHOP_ERROR_CODE.TEACHER_CODE_EMPTY')),
        );
        if($this->validate($rules)->create($data) != false) {
            if(isset($data['workCode']) && $data['workCode']){
                $code = $this->where(array('workCode' => $data['workCode']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }else{
                $data['workCode'] = $this->create_uuid();
                $data['workUploadTime'] = date('Y-m-d H:i:s');
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            return $this->getBusinessCode($code);
        }else{
            return $this->getValidErrorCode();
        }
    }

    public function getTeacherWorkInfo($condition = array(), $field = array(), $joinTableArr = array()){
        if(empty($field)){
            $field = array('TeacherWork.*');
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

    public function getTeacherWorkList($condition = array(), $field = array(), $joinTableArr = array(), $order = '', $limit = 0, $page = 0){
        if(empty($field)){
            $field = array('TeacherWork.*');
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

    public function countTeacherWork($condition = array(), $joinTableArr = array()){
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        return $this->count('TeacherWork.workCode');
    }

    public function getTeacherWorkFieldArr($field, $condition = array(), $joinTableArr = array()){
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

    public function delTeacherWork($data){
        $code = $this->where($data)->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

} 