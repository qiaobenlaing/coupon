<?php
/**
 * Created by PhpStorm.
 * User: Lulu
 * Date: 16-3-4
 * Time: 上午11:39
 */

namespace Common\Model;
use Think\Model;


class ClassRemarkModel extends BaseModel {
    protected $tableName = 'ClassRemark'; // 班级点评信息

    /**
     * 判断用户是否有权限评价该课程
     * @param string $userCode 用户编码
     * @param string $classCode 课程编码
     * @return boolean
     */
    public function ifUserCanRemark($userCode, $classCode) {
        $shopSignInfoMdl = new ShopSignInfoModel();
        // 判断用户是否已经报名该课程，已付款
        $shopSignInfo = $shopSignInfoMdl->getShopSignInfo(array('ShopSignInfo.userCode' => $userCode, 'StudentClass.classCode' => $classCode, 'StudentClass.feeFlag' => \Consts::YES), array('StudentClass.signCode'), array(array('joinTable' => 'StudentClass', 'joinCondition' => 'StudentClass.shopSignCode = ShopSignInfo.signCode')));
        return $shopSignInfo ? true : false;
    }

    public function editClassRemark($data){
        $rules = array(
            array('userCode', 'require', C('USER.USER_CODE_EMPTY')),
            array('classCode', 'require', C('EDUCATION_SHOP_ERROR_CODE.CLASS_CODE_EMPTY')),
            array('wholeLvl', 'require', C('EDUCATION_SHOP_ERROR_CODE.REMARK_WHOLE_LVL_EMPTY')),
            array('effectLvl', 'require', C('EDUCATION_SHOP_ERROR_CODE.REMARK_EFFECT_LVL_EMPTY')),
            array('teacherLvl', 'require', C('EDUCATION_SHOP_ERROR_CODE.REMARK_TEACHER_LVL_EMPTY')),
            array('envLvl', 'require', C('EDUCATION_SHOP_ERROR_CODE.REMARK_ENV_LVL_EMPTY')),
            array('shopRemark', 'require', C('EDUCATION_SHOP_ERROR_CODE.SHOP_REMARK_EMPTY')),
        );
        if($this->validate($rules)->create($data) != false) {
            if(isset($data['remarkCode']) && $data['remarkCode']){
                $data['shopRemarkTime'] = date('Y-m-d H:i:s');
                $code = $this->where(array('remarkCode' => $data['remarkCode']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }else{
                $data['remarkCode'] = $this->create_uuid();
                $data['remarkTime'] = date('Y-m-d H:i:s');
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            return array('code' => $code, 'remarkCode' => $data['remarkCode']);
        }else{
            return $this->getValidErrorCode();
        }
    }

    public function getClassRemarkInfo($condition = array(), $field = array(), $joinTableArr = array()){
        if(empty($field)){
            $field = array('ClassRemark.*');
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

    public function getClassRemarkList($condition = array(), $field = array(), $joinTableArr = array(), $order = '', $limit = 0, $page = 0){
        if(empty($field)){
            $field = array('ClassRemark.*');
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

    public function countClassRemark($condition = array(), $joinTableArr = array()){
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        return $this->count('ClassRemark.remarkCode');
    }

    public function getClassRemarkFieldArr($field, $condition = array(), $joinTableArr = array()){
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