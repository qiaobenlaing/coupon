<?php
/**
 * Created by PhpStorm.
 * User: Lulu
 * Date: 16-3-4
 * Time: 上午11:39
 */

namespace Common\Model;
use Think\Model;


class ShopHeaderModel extends BaseModel {
    protected $tableName = 'ShopHeader'; // 校长之语表

    public function editShopHeader($data){
        $rules = array(
            array('shopCode', 'require', C('SHOP.SHOP_CODE_EMPTY')),
            array('expModel', 'require', C('EDUCATION_SHOP_ERROR_CODE.HEADER_EXP_MODEL')),
        );
        if($data['expModel'] == 1){
            $rules[] = array('txtMemo', 'require', C('EDUCATION_SHOP_ERROR_CODE.HEADER_TXT_MEMO_EMPTY'));
        }elseif($data['expModel'] == 2){
            $rules[] = array('imgUrl', 'require', C('EDUCATION_SHOP_ERROR_CODE.HEADER_IMG_EMPTY'));
        }else{
            $rules[] = array('imgUrl', 'require', C('EDUCATION_SHOP_ERROR_CODE.HEADER_IMG_EMPTY'));
            $rules[] = array('txtMemo', 'require', C('EDUCATION_SHOP_ERROR_CODE.HEADER_TXT_MEMO_EMPTY'));
        }
        if($this->validate($rules)->create($data) != false) {
            if(isset($data['headerCode']) && $data['headerCode']){
                $code = $this->where(array('headerCode' => $data['headerCode']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }else{
                $data['headerCode'] = $this->create_uuid();
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            return array('code' => $code, 'headerCode' => $data['headerCode']);
        }else{
            return $this->getValidErrorCode();
        }
    }

    public function getShopHeaderInfo($condition = array(), $field = array(), $joinTableArr = array()){
        if(empty($field)){
            $field = array('ShopHeader.*');
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

    public function getShopHeaderList($condition = array(), $field = array(), $joinTableArr = array(), $order = '', $limit = 0, $page = 0){
        if(empty($field)){
            $field = array('ShopHeader.*');
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

    public function countShopHeader($condition = array(), $joinTableArr = array()){
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        return $this->count('ShopHeader.headerCode');
    }

    public function getShopHeaderFieldArr($field, $condition = array(), $joinTableArr = array()){
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