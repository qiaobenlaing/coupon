<?php
/**
 * Created by PhpStorm.
 * User: Lulu
 * Date: 16-02-23
 * Time: 上午10:34
 */
namespace Common\Model;
class MessageRecipientModel extends BaseModel {
    protected $tableName = 'MessageRecipient';
    const MAX_MR = 5;

    /**
     * 编辑记录
     * @param array $data 数据
     * @return array
     */
    public function editMRecipient($data) {
        $rules = array(
            array('mobileNbr', 'require', C('MOBILE_NBR.EMPTY')),
            array('mobileNbr', 'number', C('MOBILE_NBR.ERROR')),
            array('mobileNbr', C('MOBILE_NBR.LENGTH'), C('MOBILE_NBR.ERROR'), 0, 'length'),
            array('validateCode', 'require', C('VALIDATE_CODE.EMPTY')),
        );
        if($this->validate($rules)->create($data) != false) {
            $shopStaffMdl = new ShopStaffModel();
            if(isset($data['creatorCode']) && $data['creatorCode']){
                $creatorInfo = $shopStaffMdl->getShopStaffInfo(array('staffCode' => $data['creatorCode']), array('staffCode', 'userLvl'));
                if($creatorInfo && !in_array($creatorInfo['userLvl'], array(\Consts::SHOP_STAFF_LVL_MANAGER, \Consts::SHOP_STAFF_LVL_BIG_MANAGER))){
                    return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
                }
//            }else{
//                return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
            }
            $shopStaffRelMdl = new ShopStaffRelModel();
            $shopStaffArr = $shopStaffRelMdl->getStaffList(array('ShopStaffRel.shopCode' => $data['shopCode'], 'ShopStaff.status' => \Consts::SHOP_STAFF_STATUS_ACTIVE, 'ShopStaff.userLvl' => array('gt', \Consts::SHOP_STAFF_LVL_EMPLOYEE), 'ShopStaff.isSendPayedMsg' => C('YES')), array('ShopStaff.mobileNbr'));
            $countMRecipient = $this->countMRecipient(array('shopCode' => $data['shopCode']));
//            if(count($shopStaffArr) + $countMRecipient >= self::MAX_MR){ //数量是否大于5
            if($countMRecipient >= self::MAX_MR){ //数量是否大于5
                return $this->getBusinessCode(C('M_RECIPIENT.LIMIT_NBR'));
            }
            $mrInfo = $this->getMRecipientInfo(array('shopCode' => $data['shopCode'], 'mobileNbr' => $data['mobileNbr']));
            if($mrInfo){ //是否已经存在
                return $this->getBusinessCode(C('MOBILE_NBR.REPEAT'));
            }
            if(in_array(array('mobileNbr' => $data['mobileNbr']), $shopStaffArr)){ //是否已经存在
                return $this->getBusinessCode(C('MOBILE_NBR.REPEAT'));
            }
            $smsMdl = new SmsModel();
            $codeCom = $smsMdl->getCode('mr' . $data['mobileNbr']);
            if($data['validateCode'] != $codeCom) { //验证码是否正确
                return $this->getBusinessCode(C('VALIDATE_CODE.ERROR'));
            }
            unset($data['validateCode']);
            $shopStaff = $shopStaffRelMdl->getStaffList(array('ShopStaffRel.shopCode' => $data['shopCode'], 'ShopStaff.status' => \Consts::SHOP_STAFF_STATUS_ACTIVE, 'ShopStaff.userLvl' => array('gt', \Consts::SHOP_STAFF_LVL_EMPLOYEE), 'ShopStaff.isSendPayedMsg' => C('NO'), 'ShopStaff.mobileNbr' => $data['mobileNbr']), array('ShopStaff.staffCode'));
            if($shopStaff){
                return $shopStaffMdl->updateStaff(array('staffCode' => $shopStaff[0]['staffCode'], 'isSendPayedMsg' => C('YES')));
            }else{
                if($data['id']) {
                    $code = $this->where(array('id' => $data['id']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
                } else {
                    $data['createTime'] = date('Y-m-d H:i:s', time());
                    $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
                }
                return $this->getBusinessCode($code);
            }
        }else{
            return $this->getValidErrorCode();
        }
    }

    /**
     * 获得短信接收人详情
     * @param array $condition 条件。
     * @param array $field 查询的字段。
     * @param array $joinTableArr 关联的表。例：array('joinTable' => '连接表名', 'joinCondition' => '连接条件', 'joinType' => '连接方式')
     * @return array
     */
    public function getMRecipientInfo($condition = array(), $field = array(), $joinTableArr = array()){
        if(empty($field)){
            $field = array('MessageRecipient.*');
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
     * 获得短信接收人列表
     * @param array $condition 条件。
     * @param array $field 查询的字段。
     * @param array $joinTableArr 关联的表。例：array('joinTable' => '连接表名', 'joinCondition' => '连接条件', 'joinType' => '连接方式')
     * @param string $order 排序规则
     * @param int $limit 查询条数限制
     * @param int $page 查询页码
     * @return array
     */
    public function getMRecipientList($condition = array(), $field = array(), $joinTableArr = array(), $order = '', $limit = 0, $page = 0) {
        if(empty($field)){
            $field = array('MessageRecipient.*');
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
     * 获得短信接收人列表
     * @param array $condition 条件。
     * @param array $joinTableArr 关联的表。例：array('joinTable' => '连接表名', 'joinCondition' => '连接条件', 'joinType' => '连接方式')
     * @return array
     */
    public function countMRecipient($condition = array(), $joinTableArr = array()) {
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        return $this->count('MessageRecipient.id');
    }

    /**
     * 删除短信接收人
     * @param $condition
     * @return array
     */
    public function delMRecipient($condition){
        $code = $this->where($condition)->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

}