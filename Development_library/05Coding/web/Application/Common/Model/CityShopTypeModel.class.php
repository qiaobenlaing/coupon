<?php
namespace Common\Model;
/**
 * Created by PhpStorm.
 * User: Jihuafei
 * Date: 15-10-13
 * Time: 上午10:37
 */
class CityShopTypeModel extends BaseModel {
    protected $tableName = 'CityShopType';

    /**
     * 获得该商户类型的城市
     * @param int $shopTypeId 商户类型ID
     * @return array $cityList
     */
    public function listShopTypeCity($shopTypeId) {
        $cityList = $this
            ->field(array('name'))
            ->where(array('shopTypeId' => $shopTypeId))
            ->join('District ON District.id = CityShopType.cityId')
            ->select();
        return $cityList;
    }

    /**
     * 获得该城市的商户类型
     * @param int $cityId 城市ID
     * @return array $shopTypeList
     */
    public function listCityShopType($cityId) {
        $shopTypeList = $this
            ->field(array('shopTypeImg', 'typeZh', 'sign', 'typeValue', 'focusedUrl', 'notFocusedUrl'))
            ->where(array('cityId' => $cityId))
            ->join('ShopType ON ShopType.shopTypeId = CityShopType.shopTypeId')
            ->order('sign asc, sortNbr asc')
            ->select();
        return $shopTypeList;
    }

    /**
     * 获得用户列表
     * @param array $where 条件
     * @return array 二维数组
     */
    public function listLinkedCity($where) {
        return $this->field(array('cityId'))->where($where)->select();
    }

    /**
     * 添加平台优惠券的可用用户
     * @param int $shopTypeId 商家类型ID
     * @param array $linkedCity 城市ID列表
     * @return boolean 成功返回true，失败返回false
     */
    public function addLinkedCity($shopTypeId, $linkedCity) {
        // 删除多余的城市
        $condition['shopTypeId'] =  $shopTypeId;
        if($linkedCity) {
            $condition['cityId'] =  array('NOTIN', $linkedCity);
        }
        $this->where($condition)->delete();
        $data['shopTypeId'] = $shopTypeId;
        $this->startTrans();
        foreach($linkedCity as $city) {
            $cityShopTypeInfo = $this->where(array('shopTypeId' => $shopTypeId, 'cityId' => $city))->find();
            if(empty($cityShopTypeInfo)) {
                $data['cityId'] = $city;
                $ret = $this->add($data) !== false ? true : false;
                if($ret == false) {
                    $this->rollback();
                }
            }
        }
        $this->commit();
        return true;
    }

}