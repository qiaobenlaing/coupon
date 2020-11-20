<?php
/**
 * Created by PhpStorm.
 * User: Jihuafei
 * Date: 15-12-15
 * Time: 下午4:27
 */
namespace Common\Model;
class ShopDeliveryModel extends BaseModel {
    protected $tableName = 'ShopDelivery';

    /**
     * 删除商户配送方案
     * @param array $condition 条件，例{'deliveryId' => 23, ...}
     * @return array
     */
    public function delShopDelivery($condition) {
        $code = $this->where($condition)->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

    /**
     * 获得商户的配送方案
     * @param array $condition 条件，例：{'shopCode' => '12341234', ...}
     * @return array $deliveryList 二维数组
     */
    public function listShopDelivery($condition) {
        $deliveryList = $this
            ->where($condition)
            ->select();
        $tem = array('requireMoney', 'deliveryFee');
        foreach($deliveryList as $k => $v) {
            foreach($tem as $key) {
                $deliveryList[$k][$key] = $deliveryList[$k][$key] / \Consts::HUNDRED;
            }
        }
        return $deliveryList;
    }

    /**
     * 添加或者修改商户配送方案
     * @param array $data 数据，例{'shopCode' => 'afasfadasdf', 'deliveryFee' => 200}
     * @return array
     */
    public function editShopDelivery($data) {

        if($data['deliveryId']) {
            // 修改商户配送方案
            $data['lastEditTime'] = time(); // 添加最近一次修改时间
            // 保存数据
            $ret = $this->where(array('deliveryId' => $data['deliveryId']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        } else {
            // 添加商户配送方案
            $data['iptTime'] = time(); // 添加创建时间
            $data['lastEditTime'] = $data['iptTime']; // 添加最近一次修改时间
            // 保存数据
            $ret = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        }
        return $this->getBusinessCode($ret);
    }
}