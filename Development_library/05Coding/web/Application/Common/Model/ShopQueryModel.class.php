<?php
namespace Common\Model;
use Think\Model;
use Think\Page;
/**
 * Created by PhpStorm.
 * User: yufeng.jiang
 * Date: 15-11-23
 * Time: 下午14:41
 */
class ShopQueryModel extends BaseModel {
    protected $tableName = 'ShopQuery';
    
    public static $fieldArr = array(
        30 => '智能排序',
        40 => '筛选',
    );
    
    /**
     * 获得子字段名
     * @param  $field 所属字段
     * @param  $where 查询条件
     */
    public function getSubField($field, $where) {
        return $this->field($field)->where($where)->select();
    }
    
    /**
     * 获得某条记录信息
     * @param  $field 所属字段
     * @param  $where 查询条件
     * @return
     */
    public function getShopQueryInfo($field, $where) {
        return $this->field($field)->where($where)->find();
    }
    /**
     * 编辑查询条件
     * @param array $data 新增数据
     */
    public function editShopQuery($data) {
        $rules = array(
            array(
                'queryName',
                'require',
                C('SHOP_QUERY.QUERY_NAME_EMPTY')
            ),
            array(
                'field',
                'require',
                C('SHOP_QUERY.FIELD_EMPTY')
            ),
            array(
                'isDisplay',
                'require',
                C('SHOP_QUERY.IS_DISPLAY_EMPTY'))
        );
        if($this->validate($rules)->create($data) != false) {
            if(isset($data['id']) && !empty($data['id'])){
                $code = $this->where(array('id' => $data['id']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }else{
                $lastValue = $this->field(array('value'))->order('value desc')->find();
                if(!empty($lastValue)){
                    $data['value'] = $lastValue['value']+1;
                }else{
                    $data['value'] = 1;
                }
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            return $this->getBusinessCode($code);
        } else {
            return $this->getValidErrorCode();
        }
    }
}