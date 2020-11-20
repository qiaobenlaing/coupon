<?php
namespace Common\Model;
use Think\Model;
/**
 * Created by PhpStorm.
 * User: Huafei Ji
 * Date: 15-8-7
 * Time: 下午5:25
 */
class PCouponShopModel extends BaseModel {
    protected $tableName = 'PCouponShop';

    /**
     * 判断商家是否优惠券的关联商家
     * @param string $batchCouponCode 优惠券编码
     * @param string $shopCode 商家编码
     * @return boolean $isLinked 是返回tue，否返回false
     */
    public function isLinkedShop($batchCouponCode, $shopCode) {
        $isLinked = $this->where(array('batchCouponCode' => $batchCouponCode, 'shopCode' => $shopCode))->find() ? true : false;
        return $isLinked;
    }

    /**
     * 获得用户列表
     * @param array $where 条件
     * @return array 二维数组
     */
    public function listLinkedShop($where) {
        return $this->field(array('shopCode'))->where($where)->select();
    }

    /**
     * 添加平台优惠券的可用用户
     * @param string $batchCouponCode 优惠券编码
     * @param array $linkedShop 可用商户的商户编码列表
     * @return boolean 成功返回true，失败返回false
     */
    public function addLinkedShop($batchCouponCode, $linkedShop) {
        // 删除之前的存储记录
        $this->where(array('batchCouponCode' => $batchCouponCode))->delete();
        $data['batchCouponCode'] = $batchCouponCode;
        $this->startTrans();
        foreach($linkedShop as $shopCode) {
            $data['logCode'] = $this->create_uuid();
            $data['shopCode'] = $shopCode;
            $ret = $this->add($data) !== false ? true : false;
            if($ret == false) {
                $this->rollback();
            }
        }
        $this->commit();
        return true;
    }

}
