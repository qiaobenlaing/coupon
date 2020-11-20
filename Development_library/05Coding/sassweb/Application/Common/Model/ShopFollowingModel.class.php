<?php
namespace Common\Model;
use Think\Model;
/**
 * shopFollowing表
 * @author 
 */
class ShopFollowingModel extends BaseModel {
    protected $tableName = 'ShopFollowing';

    /**
     * 获得商家所有粉丝
     * @param string $shopCode 商家编码
     * @return array
     */
    public function listShopFollowedUser($shopCode) {
        return $this->where(array('shopCode' => $shopCode))->select();
    }

    /**
     * 获得用户关注的所有
     * @param string $userCode 用户编码
     * @return array
     */
    public function listUserFollowShop($userCode) {
        return $this->where(array('userCode' => $userCode))->select();
    }

    /**
     * 判断用户是否关注了该商家
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @return boolean 若关注返回true,未关注返回false
     */
    public function isUserFollowedShop($userCode, $shopCode) {
        $ret = $this->where(array('userCode' => $userCode, 'shopCode' => $shopCode))->getField('followingCode');
        return $ret ? true : false;
    }

    /**
     * 关注商家
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @return array
     */
    public function followShop($userCode, $shopCode) {
        $shopFollow = $this->where(array('userCode' => $userCode, 'shopCode' => $shopCode))->find();
        if($shopFollow){
            return $this->getBusinessCode(C('FOLLOW_SHOP_REPEAT'));
        }
        $data = array(
            'followingCode' => $this->create_uuid(),
            'userCode' => $userCode,
            'shopCode' => $shopCode,
            'createTime' => date('Y-m-d H:i:s', time())
        );
        if($this->add($data) !== false) {
            $shopMdl = new ShopModel();
            if($shopMdl->updateFollowedCount($shopCode) !== false) {
                return $this->getBusinessCode(C('SUCCESS'));
            };
        }
        return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
    }

    /**
     * 取消关注商家
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @return array
     */
    public function cancelFollowShop($userCode, $shopCode) {
        $code = $this->where(array('userCode' => $userCode, 'shopCode' => $shopCode))->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

    /**
     * 获得用户关注的商家列表
     * @param string $userCode 用户编码
     * @param number $page 页码
     * @return array
     */
    public function listFollowShop($userCode, $page) {
        return $this->where(array('userCode' => $userCode))->pager($page)->select();
    }

    /**
     * 获得某商家的粉丝
     * @param string $shopCode 商家编码
     * @param number $page 页码
     * @return array
     */
    public function listFollowClient($shopCode, $page) {
        return $this->where(array('shopCode' => $shopCode))->pager($page)->select();
    }

    /**
     * 获得用户关注的商家的商家编码
     * @param string $userCode 用户编码
     * @return array 有数据时返回一维数组。为空时返回空数组
     */
    public function getShopCode($userCode) {
        $shopCodeList =  $this->where(array('userCode' => $userCode))->getField('shopCode', true);
        return $shopCodeList ? $shopCodeList : array();
    }

}