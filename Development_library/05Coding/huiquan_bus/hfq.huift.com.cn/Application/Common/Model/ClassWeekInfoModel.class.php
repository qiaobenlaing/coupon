<?php
/**
 * Created by PhpStorm.
 * User: Lulu
 * Date: 16-3-4
 * Time: 上午11:39
 */

namespace Common\Model;
use Think\Model;


class ClassWeekInfoModel extends BaseModel {
    protected $tableName = 'ClassWeekInfo'; // 班级周上课信息

    public function editClassWeek($data){
        $rules = array(
            array('classCode', 'require', C('EDUCATION_SHOP_ERROR_CODE.CLASS_CODE_EMPTY')),
            array('weekName', 'require', C('EDUCATION_SHOP_ERROR_CODE.CW_WEEK_NAME_EMPTY')),
            array('startTime', 'require', C('EDUCATION_SHOP_ERROR_CODE.CW_START_TIME_EMPTY')),
            array('endTime', 'require', C('EDUCATION_SHOP_ERROR_CODE.CW_END_TIME_EMPTY')),
        );
        if($this->validate($rules)->create($data) != false) {
            if(isset($data['weekCode']) && $data['weekCode']){
                $code = $this->where(array('weekCode' => $data['weekCode']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }else{
                $data['weekCode'] = $this->create_uuid();
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            return array('code' => $code, 'weekCode' => $data['weekCode']);
        }else{
            return $this->getValidErrorCode();
        }
    }

    public function getClassWeekInfo($condition = array(), $field = array(), $joinTableArr = array()){
        if(empty($field)){
            $field = array('ClassWeekInfo.*');
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

    public function getClassWeekList($condition = array(), $field = array(), $joinTableArr = array(), $order = '', $limit = 0, $page = 0){
        if(empty($field)){
            $field = array('ClassWeekInfo.*');
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
        $classWeekList = $this->select();
        $newClassWeekList = array();
        if($classWeekList){
            foreach($classWeekList as $v){
                $v['startTime'] = substr($v['startTime'], 0, 5);
                $v['endTime'] = substr($v['endTime'], 0, 5);
                if(isset($newClassWeekList[$v['weekName']])){
                    $newClassWeekList[$v['weekName']]['learnTime'][] = array('startTime' => $v['startTime'], 'endTime' => $v['endTime']);
                }else{
                    switch($v['weekName']){
                        case 1:
                            $weekName = '周一';
                            break;
                        case 2:
                            $weekName = '周二';
                            break;
                        case 3:
                            $weekName = '周三';
                            break;
                        case 4:
                            $weekName = '周四';
                            break;
                        case 5:
                            $weekName = '周五';
                            break;
                        case 6:
                            $weekName = '周六';
                            break;
                        default:
                            $weekName = '周日';
                            break;
                    }
                    $newClassWeekList[$v['weekName']]['weekName'] = $weekName;
                    $newClassWeekList[$v['weekName']]['learnTime'][] = array('startTime' => $v['startTime'], 'endTime' => $v['endTime']);
                }
            }
            $newClassWeekList = array_values($newClassWeekList);
        }
        return $newClassWeekList;
    }

    public function countClassWeek($condition = array(), $joinTableArr = array()){
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        return $this->count('ClassWeekInfo.weekCode');
    }

    public function getClassWeekFieldArr($field, $condition = array(), $joinTableArr = array()){
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

    public function delClassWeekInfo($data){
        $code = $this->where($data)->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }
} 