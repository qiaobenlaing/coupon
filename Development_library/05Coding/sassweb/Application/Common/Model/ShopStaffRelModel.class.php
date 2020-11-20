<?php
namespace Common\Model;
/**
 * Created by PhpStorm.
 * User: jihuafei
 * Date: 15-11-4
 * Time: 下午1:59
 */
class ShopStaffRelModel extends BaseModel {
    protected $tableName = 'ShopStaffRel';

    /**
     * 获得没有店长的商家
     * @return array $shopList
     */
    public function getNoMngShop() {

        $where['userLvl'] = C('STAFF_LVL.MANAGER');

        $shopList = $this
            ->field(array('ShopStaffRel.shopCode,ShopStaff.bank_id'))
            ->where($where)
            ->join('ShopStaff ON ShopStaff.staffCode = ShopStaffRel.staffCode')
            ->select();
        $shopTypeRelMdl = new ShopTypeRelModel();
        $shopCodeArr = $shopTypeRelMdl->getFieldList(array('typeValue' => array('IN', array(\Consts::SHOP_TYPE_ICBC, \Consts::SHOP_TYPE_PLAT))), 'shopCode', array(array('joinTable' => 'ShopType', 'joinCondition' => 'ShopType.shopTypeId = ShopTypeRel.typeId', 'joinType' => 'inner')));
//        $condition['type'] = array('NOTIN', array(\Consts::SHOP_TYPE_ICBC, \Consts::SHOP_TYPE_PLAT));
        if($shopList) {
            $shopCodeList = array();
            foreach($shopList as $shop) {
                $shopCodeList[] = $shop['shopCode'];
            }
            $shopCodeArr = array_unique(array_merge($shopCodeArr, $shopCodeList));
        }
        if(empty($shopCodeArr)){$shopCodeArr = array('0');}
        $condition['shopCode'] = array('NOTIN', $shopCodeArr);
        $shopMdl = new ShopModel();
        $shopList = $shopMdl->getShopList($condition, array('shopCode', 'shopName', 'province', 'city'));
        return $shopList;
    }

    /**
     * 获得没有店长的商家(乔本亮ssas更改)
     * @return array $shopList
     */
    public function getNoMngShop2($zoneId) {

        $where['userLvl'] = C('STAFF_LVL.MANAGER');

        if(!empty($zoneId)){
            $shopList = $this
                ->field(array('ShopStaffRel.shopCode'))
                ->where($where)
                ->join('ShopStaff ON ShopStaff.staffCode = ShopStaffRel.staffCode and ShopStaff.bank_id='.$zoneId)
                ->select();
        }else{
            $shopList = $this
                ->field(array('ShopStaffRel.shopCode'))
                ->where($where)
                ->join('ShopStaff ON ShopStaff.staffCode = ShopStaffRel.staffCode')
                ->select();
        }

        foreach($shopList as $shop) {
            $shopCodeArr[] = $shop['shopCode'];
        }

        if(empty($shopCodeArr)){$shopCodeArr = array('0');}
        $condition['shopCode'] = array('NOTIN', $shopCodeArr);

        if(!empty($zoneId)){
            $condition['bank_id']  = $zoneId;
        }
        $shopMdl = new ShopModel();
        $shopList = $shopMdl->getShopList($condition, array('shopCode', 'shopName', 'province', 'city'));
        return $shopList;
    }

    /**
     * 获得没有大店长的商家（乔本亮saas更改）
     * @return array $shopList
     */
    public function getNoBigMngShop2($zoneId) {
        $where['userLvl'] = C('STAFF_LVL.BIG_MANAGER');
        if(!empty($zoneId)){
            $shopList = $this
                ->field(array('ShopStaffRel.shopCode'))
                ->where($where)
                ->join('ShopStaff ON ShopStaff.staffCode = ShopStaffRel.staffCode and ShopStaff.bank_id='.$zoneId)
                ->select();
        }else{
            $shopList = $this
                ->field(array('ShopStaffRel.shopCode'))
                ->where($where)
                ->join('ShopStaff ON ShopStaff.staffCode = ShopStaffRel.staffCode')
                ->select();
        }

        foreach($shopList as $shop) {
            $shopCodeArr[] = $shop['shopCode'];
        }

        if(empty($shopCodeArr)){$shopCodeArr = array('0');}
        $condition['shopCode'] = array('NOTIN', $shopCodeArr);

        if(!empty($zoneId)){
            $condition['bank_id']  = $zoneId;
        }
        $shopMdl = new ShopModel();
        $shopList = $shopMdl->getShopList($condition, array('shopCode', 'shopName', 'province', 'city'));
        return $shopList;
    }


    /**
     * 获得没有大店长的商家
     * @return array $shopList
     */
    public function getNoBigMngShop() {

        $where['userLvl'] = C('STAFF_LVL.BIG_MANAGER');

        $shopList = $this
            ->field(array('ShopStaffRel.shopCode,ShopStaff.bank_id'))
            ->where($where)
            ->join('ShopStaff ON ShopStaff.staffCode = ShopStaffRel.staffCode')
            ->select();
        $shopTypeRelMdl = new ShopTypeRelModel();
        $shopCodeArr = $shopTypeRelMdl->getFieldList(array('typeValue' => array('IN', array(\Consts::SHOP_TYPE_ICBC, \Consts::SHOP_TYPE_PLAT))), 'shopCode', array(array('joinTable' => 'ShopType', 'joinCondition' => 'ShopType.shopTypeId = ShopTypeRel.typeId', 'joinType' => 'inner')));
//        $condition['type'] = array('NOTIN', array(\Consts::SHOP_TYPE_ICBC, \Consts::SHOP_TYPE_PLAT));
        if($shopList) {
            $shopCodeList = array();
            foreach($shopList as $shop) {
                $shopCodeList[] = $shop['shopCode'];
            }
            $shopCodeArr = array_unique(array_merge($shopCodeArr, $shopCodeList));
//            $condition['shopCode'] = array('NOTIN', $shopCodeList);
        }
        if(empty($shopCodeArr)){$shopCodeArr = array('0');}
        $condition['shopCode'] = array('NOTIN', $shopCodeArr);
        $shopMdl = new ShopModel();
        $shopList = $shopMdl->getShopList($condition, array('shopCode', 'shopName', 'province', 'city'));
        return $shopList;
    }

    /**
     * 获得关系信息
     * @param array $condition
     * @return array
     */
    public function getRelInfo($condition) {
        return $this->where($condition)->find();
    }

    /**
     * 获得商家店长或者员工列表
     * @param array $condition 条件
     * @param array $field
     * @return array
     */
    public function getStaffList($condition, $field = array()) {
        if(empty($field)) $field = array('*');
        return $this->field($field)->where($condition)->join('ShopStaff ON ShopStaff.staffCode = ShopStaffRel.staffCode')->select();
    }

    /**
     * 删除
     * @param array $condition 条件
     * @return array
     */
    public function delOriginalMng($condition) {
        $code = $this
            ->join('ShopStaff ON ShopStaff.staffCode = ShopStaffRel.staffCode')
            ->where($condition)
            ->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

    /**
     * 删除
     * @param array $condition 条件
     * @return array
     */
    public function delShopStaffRel($condition) {
        $code = $this->where($condition)->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

    /**
     * 删除关系
     * @param int $id 关系ID
     * @return array
     */
    public function delRel($id) {
        $code = $this->where(array('id' => $id))->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

    /**
     * 删除关系
     * @param array $condition 条件
     * @return array
     */
    public function delRelByCon($condition) {
        $code = $this->where($condition)->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

    /**
     * 店员获得随便获得一个自己所属的商家
     * @param string $staffCode 店员编码
     * @param array $field
     * @return array $shopInfo
     */
    public function getShopInfoByStaffCode($staffCode, $field) {
        $shopInfo = $this
            ->field($field)
            ->where(array('ShopStaffRel.staffCode' => $staffCode))
            ->join('Shop ON Shop.shopCode = ShopStaffRel.shopCode')
            ->find();
        return $shopInfo;
    }

    /**
     * 获得商家信息
     * @param string $staffCode 店员编码
     * @param array $field
     * @return array $shopList
     */
    public function getShopListByStaffCode($staffCode, $field) {
        $shopList = $this
            ->field($field)
            ->where(array('ShopStaffRel.staffCode' => $staffCode))
            ->join('Shop ON Shop.shopCode = ShopStaffRel.shopCode')
            ->select();
        return $shopList;
    }

    /**
     * 获得商家店员信息
     * @param string $shopCode 商家编码
     * @param int $userLvl 用户角色 1-普通员工，2-店长，3-大店长
     * @return array
     */
    public function getStaffInfoByShopCode($shopCode, $userLvl) {
        $staffInfo = $this
            ->field(array('ShopStaffRel.id', 'ShopStaffRel.shopCode', 'ShopStaff.staffCode', 'ShopStaff.realName', 'ShopStaff.mobileNbr', 'ShopStaff.access', 'ShopStaff.status'))
            ->where(array('ShopStaffRel.shopCode' => $shopCode, 'userLvl' => $userLvl))
            ->join('ShopStaff ON ShopStaff.staffCode = ShopStaffRel.staffCode', 'left')
            ->find();
        return $staffInfo;
    }

    /**
     * 编辑商家与店员关系
     * @param array $data
     * @return array $ret
     */
    public function editShopStaffRel($data) {
        $rules = array(
            array('shopCode', 'require', C('SHOP.SHOP_EMPTY')),
            array('staffCode', 'require', C('SHOP_STAFF.STAFF_CODE_EMPTY')),
        );
        if($this->validate($rules)->create($data)) {
            if(empty($data['id'])) {
                $id = $this->add($data);
                $code = $id !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            } else {
                $code = $this->where(array('id' => $data['id']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
                $id = $data['id'];
            }
            return array('code' => $code, 'id' => $id);
        } else {
            return $this->getValidErrorCode();
        }
    }

}