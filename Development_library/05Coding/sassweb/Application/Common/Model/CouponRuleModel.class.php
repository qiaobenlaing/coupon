<?php
namespace Common\Model;
use Think\Model;
/**
 * couponRule表
 * @author 
 */
class CouponRuleModel extends BaseModel {
    protected $tableName = 'CouponRule';
    
    /**
    * 添加优惠券扩展规则
    * @param array $couponRuleInfo 扩展规则信息：{}
    * @return array 成功返回成功编码和规则编码，失败返回失败编码
    */
    public function addCouponRule($couponRuleInfo) {
        $rules = array();
        if($this->validate($rules)->create($couponRuleInfo) != false) {
            $couponRuleInfo['ruleCode'] = $this->create_uuid();
            $couponRuleInfo['createTime'] = date('Y-m-d H:i:s', time());
            if($this->add($couponRuleInfo) !== false) {
                return array('code' => C('SUCCESS'), 'ruleCode' => $couponRuleInfo['ruleCode']);
            } else {
                return array('code' => C('API_INTERNAL_EXCEPTION'));
            }
        } else {
            return $this->getValidErrorCode();
        }
    }
    
    /**
    * 删除数据
    * @param number $couponRuleCode 主键
    * @return boolean||string 删除成功返回true；删除失败返回错误信息
    */
    public function delCouponRule($ruleCode) {
        return $this->where(array('ruleCode' => $ruleCode))->delete() !== false ? true : '数据库删除数据失败';
    }
    
    /**
    * 得到扩展规则
    * @param array $condition 条件
    * @param array $filed 字段
    * @return array||boolean 查询成功返回关联数组；查询结果为空返回NULL；查询出错返回false
    */
    public function listCouponRule($condition, $filed) {
        return $this
            ->field($filed)
            ->where($condition)
            ->select();
    }
    
    /**
    * 根据主键得到数据详情
    * @param number $ruleCode 主键
    * @return array||boolean 查询成功返回关联数组；查询结果为空返回NULL；查询出错返回false
    */
    public function getCouponRule($ruleCode) {
        return $this->where(array('ruleCode' => $ruleCode))->find();
    }
    
    /**
    * 更新数据
    * @param number $ruleCode 主键
    * @param array $ruleInfo 关联数组
    * @return boolean||string
    */
    public function updateCouponRule($ruleCode, $ruleInfo) {
        $rules = array();
        if($this->validate($rules)->create($ruleInfo) != false) {
            return $this->where(array('ruleCode' => $ruleCode))->save($ruleInfo) !== false ? true : '数据库更新数据失败';
        } else {
            return $this->getValidErrorCode();
        }
    }
}