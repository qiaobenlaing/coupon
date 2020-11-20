<?php
/**
 * Created by PhpStorm.
 * User: Lulu
 * Date: 16-3-4
 * Time: 上午11:39
 */

namespace Common\Model;
use Think\Model;


class ClassClickInfoModel extends BaseModel {
    protected $tableName = 'ClassClickInfo'; // 班级点赞信息

    /**
     * 删除点赞记录
     * @param array $con
     * @return array
     */
    public function delClassClick($con) {
        $code = $this->where($con)->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

    /**
     * 获得点赞数量
     * @param array $con 条件
     * @return int $clickNbr 数量
     */
    public function countClickNbr($con) {
        $clickNbr = $this->where($con)->count('clickCode');
        return $clickNbr ? $clickNbr : 0;
    }

    /**
     * 新增 or 修改点赞
     * @param array $data
     * @return array
     */
    public function editClassClick($data){
        $rules = array(
            array('userCode', 'require', C('')),
            array('classCode', 'require', C('')),
        );
        if($this->validate($rules)->create($data) != false) {
            if(isset($data['clickCode']) && $data['clickCode']){
                $code = $this->where(array('clickCode' => $data['clickCode']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }else{
                $data['clickCode'] = $this->create_uuid();
                $data['clickTime'] = date('Y-m-d H:i:s');
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            return array('code' => $code, 'clickCode' => $data['clickCode']);
        }else{
            return $this->getValidErrorCode();
        }
    }

    /**
     * 获取某一条点赞详情
     * @param array $condition
     * @param array $field
     * @param array $joinTableArr
     * @return mixed
     */
    public function getClickInfo($condition = array(), $field = array(), $joinTableArr = array()){
        if(empty($field)){
            $field = array('ClassClickInfo.*');
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

    /**
     * 获取点赞列表
     * @param array $condition
     * @param array $field
     * @param array $joinTableArr
     * @param string $order
     * @param int $limit
     * @param int $page
     * @return mixed
     */
    public function getClickList($condition = array(), $field = array(), $joinTableArr = array(), $order = '', $limit = 0, $page = 0){
        if(empty($field)){
            $field = array('ClassClickInfo.*');
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

    /**
     * 获取数量
     * @param array $condition
     * @param array $joinTableArr
     * @return mixed
     */
    public function countClassClick($condition = array(), $joinTableArr = array()){
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        return $this->count('ClassClickInfo.clickCode');
    }

    /**
     * 获取某一个字段的数组
     * @param $field
     * @param array $condition
     * @param array $joinTableArr
     * @return mixed
     */
    public function getClickFieldArr($field, $condition = array(), $joinTableArr = array()){
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