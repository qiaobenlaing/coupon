<?php
namespace Common\Model;
use Think\Model;

class PrizeRuleModel extends BaseModel{
    protected $tableName = 'PrizeRule';


    public function getPrizeRule($field, $where){
        if(empty($field)){
            $field = array('PrizeRule.*');
        }
        $prizeRule = $this
            ->field($field)
            ->where($where)
            ->order('rand()')
            ->find();
        return $prizeRule;
    }

    /**
     * 添加和修改规则
     * @param array $data
     * @return array $ret
     */
    public function editPrizeRule($data) {
        if(isset($data['id'])){
            $code = $this->where(array('id' => $data['id']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        }else{
            $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        }
        return $this->getBusinessCode($code);
    }

}