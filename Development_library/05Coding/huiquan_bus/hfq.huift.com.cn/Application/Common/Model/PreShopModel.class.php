<?php
/**
 * Created by PhpStorm.
 * User: Jihuafei
 * Date: 15-10-27
 * Time: 下午3:38
 */
namespace Common\Model;
class PreShopModel extends BaseModel {
    protected $tableName = 'PreShop';


    /**
     * 删除预备商户
     * @param string $shopCode 商家编码
     * @return boolean
     */
    public function delPreShop($shopCode) {
        return $this->where(array('shopCode' => $shopCode))->delete() !== false ? true : false;
    }

    /**
     * 获得预采用商户的信息
     * @param array $where 条件
     * @param array $field 查询字段
     * @param array $joinCondition 联合的表
     * @return array $info
     */
    public function getPreShopInfo($where, $field, $joinCondition = array()) {
        $this->field($field);
        if($joinCondition) {
            foreach($joinCondition as $v) {
                $this->join($v['joinTable'] . ' ON ' . $v['joinCondition'], $v['joinType']);
            }
        }
        $info = $this->where($where)->find();
        return $info;
    }

    /**
     * 添加和修改预采用商户
     * @param array $data
     * @return array
     */
    public function editPreShop($data) {
        $rules = array(
            array('country', 'require', C('PRE_SHOP.COUNTRY_ERROR')),
            array('province', 'require', C('PRE_SHOP.PROVINCE_ERROR')),
            array('city', 'require', C('PRE_SHOP.CITY_ERROR')),
            array('street', 'require', C('PRE_SHOP.STREET_ERROR')),
            array('shopName', 'require', C('PRE_SHOP.NAME_ERROR')),
            array('type', 'require', C('PRE_SHOP.TYPE_ERROR')),
            array('type', 'require', C('PRE_SHOP.TYPE_ERROR')),
            array('licenseNbr', 'require', C('LICENSE_NBR.ERROR')),
            array('licenseExpireTime', 'require', C('PRE_SHOP.LICENSE_EXPIRE_TIME_ERROR')),
            array('licenseExpireTime', '0000-00-00', C('PRE_SHOP.LICENSE_EXPIRE_TIME_ERROR'), 0, 'notequal'),
            array('mobileNbr', 'require', C('PRE_SHOP.MOBILE_NBR_ERROR')),
            array('shopOpeningTime', 'require', C('PRE_SHOP.OPENING_TIME_ERROR')),
            array('shopClosedTime', 'require', C('PRE_SHOP.CLOSED_TIME_ERROR')),
            array('businessHours', 'require', C('SHOP.BUSINESS_HOURS')),
            array('shopOwner', 'require', C('PRE_SHOP.SHOP_OWNER_ERROR')),
            array('bigManager', 'require', C('PRE_SHOP.BIG_MANAGER_EMPTY')),
            array('bMaMobileNbr', 'require', C('PRE_SHOP.BMAMOBILENBR_EMPTY')),
//        	array('protocolUrl', 'require', C('SHOP.PROTOCOL_Url_EMPTY')),
            array('IDcardUrl', 'require', C('PRE_SHOP.ID_CARD_EMPTY')),
        );
        if($this->validate($rules)->create($data)) {
            $temp = array('deliveryFee', 'takeoutRequirePrice');
            foreach($temp as $v) {
                $data[$v] = $data[$v] * \Consts::HUNDRED;// 元转化为分
            }
            if(empty($data['shopCode'])) {
                $data['shopCode'] = $this->create_uuid();
                $data['createDate'] = date('Y-m-d H:i:s');
                $userInfo = session('USER');
                $data['developerCode'] = $userInfo['staffCode'];
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            } else {
                $status = $this->where(array('shopCode' => $data['shopCode']))->getField('status');
                if($status == \Consts::PRE_SHOP_STATUS_USED) {
                    return C('PRE_SHOP.USED');
                }
                $code = $this->where(array('shopCode' => $data['shopCode']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            return array('code' => $code, 'shopCode' => $data['shopCode']);
        } else {
            return $this->getError();
        }
    }

    /**
     * 过滤条件
     * @param array $where 过滤前的条件
     * @return array $where 过滤后的条件
     */
    public function secondFilterWhere(&$where) {
        return $where;
    }

    /**
     * 获得预采用商户列表
     * @param array $filterData 条件
     * @param array $field 字段
     * @param array $joinTable join的表
     * @param array $order 排序
     * @param object $page 偏移值
     * @return array $preShopList 如果为空，返回空数组；如果不为空，返回二维数组array(array());
     */
    public function listPreShop($filterData, $field, $joinTable, $order, $page) {

        $where = $this->filterWhere(
            $filterData,
            array('shopName' => 'like', 'mobileNbr' => 'like', 'shopId' => 'like', 'licenseNbr' => 'like'),
            $page);

        //判断是否为惠圈管理人员
        if($_SESSION['USER']['bank_id']!=-1) {
            $where['bank_id'] = $_SESSION['USER']['bank_id'];
            $where['PreShop.bank_id'] =  $where['bank_id'] ;
            unset($where['bank_id']);
        }

        $this->secondFilterWhere($where);
        $this->field($field);
        if($joinTable) {
            foreach($joinTable as $v) {
                $this->join($v['tableName'] . ' ON ' . $v['joinCondition'], $v['joinType']);
            }
        }
        $preShopList = $this->where($where)
            ->pager($page)
            ->order($order)
            ->select();

        return $preShopList;
    }

    /**
     * 管理端商户总数
     * @param array $filterData
     * @return null|int 如果为空，返回null；如果不为空，返回number;
     */
    public function countPreShop($filterData) {
        $where = $this->filterWhere(
            $filterData,
            array('shopName' => 'like', 'mobileNbr' => 'like', 'shopId' => 'like', 'licenseNbr' => 'like')
        );

        //判断是否为惠圈管理人员
        if($_SESSION['USER']['bank_id']!=-1) {
            $where['bank_id'] = $_SESSION['USER']['bank_id'];
        }

        $this->secondFilterWhere($where);
        return $this
            ->where($where)
            ->count('shopCode');
    }
    
    /**
     * 后台根据条件修改地推商户信息
     * @param array $condition 修改条件
     * @param array $setMessage 修改内容
     * @return array
     */
    public function setPreShopFeild($condition, $setMessage){
    	$result = $this->where($condition)->save($setMessage);
    	if($result){
    		return array('code' => C('SUCCESS'));
    	}else{
    		return array('code' => C('API_INTERNAL_EXCEPTION'));
    	}
    }
}