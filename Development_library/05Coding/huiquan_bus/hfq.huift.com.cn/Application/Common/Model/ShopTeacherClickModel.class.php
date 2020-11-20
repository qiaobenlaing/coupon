<?php
/**
 * Created by PhpStorm.
 * User: Huafei Ji
 * Date: 16-3-21
 * Time: 下午4:44
 */
namespace Common\Model;
class ShopTeacherClickModel extends BaseModel {
    protected $tableName = 'ShopTeacherClick';

    /**
     * 保存点赞数据
     * @param array $data
     * @return array
     */
    public function editClickInfo($data){
        $rules = array(
        );
        if($this->validate($rules)->create($data) != false) {
            if(!empty($data['clickCode'])){
                $code = $this->where(array('clickCode' => $data['clickCode']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }else{
                $data['clickCode'] = $this->create_uuid();
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            return array('code' => $code, 'clickCode' => $data['clickCode']);
        }else{
            return $this->getValidErrorCode();
        }
    }

    /**
     * 获得点赞详情
     * @param array $condition 条件
     * @param array $field 字段
     * @param array $joinTableArr 联合的表
     * @return array 没有数组时，返回空数组
     */
    public function getClickInfo($condition = array(), $field = array(), $joinTableArr = array()){
        if(empty($field)){
            $field = array('ShopTeacherClick.*');
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
}