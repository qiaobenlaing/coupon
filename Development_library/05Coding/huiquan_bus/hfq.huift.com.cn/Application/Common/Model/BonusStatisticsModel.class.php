<?php
namespace Common\Model;
use Think\Model;
/**
 * activity表
 * @author
 */
class BonusStatisticsModel extends BaseModel {
    protected $tableName = 'BonusStatistics';

    /**
     * 回滚用户的商家或者平台红包
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param int $value 回滚值
     * @return boolean 成功返回true，失败返回false
     */
    public function incBonusValue($userCode, $shopCode, $value) {
        return $this
            ->where(array('userCode' => $userCode, 'shopCode' => $shopCode))
            ->setInc('totalValue', $value) !== false ? true : false;
    }

    /**
     * 抢红包之后，把抢的金额累加到对应的账户
     * @param $userCode
     * @param $shopCode
     * @param $value
     * @return bool
     */
    public function updateBonusStatistics($userCode, $shopCode, $value) {
        $log = $this->getUserBonusStatistics($userCode, $shopCode);
        if(isset($log['logCode'])){
            return $this->where(array('shopCode'=>$shopCode, 'userCode'=>$userCode))->setInc('totalValue', $value) !== false ? true : false;
        } else {
            return $this->addBonusStatistics($userCode, $shopCode, $value) !== false ? true : false;
        }
    }

    /**
     * 新增抢红包累加记录
     * @param $userCode
     * @param $shopCode
     * @param $value
     * @return bool
     */
    public function addBonusStatistics($userCode, $shopCode, $value) {
        $bonusStatisticsInfo['logCode'] = $this->create_uuid();
        $bonusStatisticsInfo['userCode'] = $userCode;
        $bonusStatisticsInfo['shopCode'] = $shopCode;
        $bonusStatisticsInfo['totalValue'] = $value;
        return $this->add($bonusStatisticsInfo) !== false ? true : false;
    }

    /**
     * @param $userCode
     * @param $shopCode
     * @return mixed
     */
    public function getUserBonusStatistics($userCode, $shopCode){
        $condition['userCode'] = $userCode;
        $condition['shopCode'] = $shopCode;
        return $this->where($condition)->find();
    }

    /**
     * 用户买单使用了红包，对应的红包总额减少
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param $value
     * @return array
     */
    public function reduceBonusStatistics($userCode, $shopCode, $value) {
        $log = $this->getUserBonusStatistics($userCode, $shopCode);
        if(empty($log)) {
            return $this->getBusinessCode(C('BONUS.NOT_AVAILABLE'));
        }
        if($value > $log['totalValue']) {
            if($shopCode == C('HQ_CODE')) {
                $code = C('BONUS.PLAT_UPPER_ERROR');
            } else {
                $code = C('BONUS.SHOP_UPPER_ERROR');
            }
            return $this->getBusinessCode($code);
        }
        return $this->where(array('shopCode'=>$shopCode, 'userCode'=>$userCode))->setDec('totalValue', $value) !== false ? array('code' => C('SUCCESS')) : array('code' => C('API_INTERNAL_EXCEPTION'));
    }

    /**
     * 获取用户拥有的各商家红包总额
     * @param $userCode
     * @return mixed
     */
    public function getMyBonus($userCode){
        $condition['BonusStatistics.userCode'] = $userCode;
        $condition['BonusStatistics.totalValue'] = array('GT', 0);
        $bonusList = $this
            ->field(array('Shop.shopCode', 'Shop.shopName', 'Shop.logoUrl', 'BonusStatistics.totalValue'))
            ->join('Shop on Shop.shopCode = BonusStatistics.shopCode','inner')
            ->where($condition)
            ->select();
        foreach($bonusList as &$v){
            $v['totalValue'] = (float)number_format($v['totalValue'] / C('RATIO'),2,'.','');
        }
        return $bonusList;
    }

    /**
     * 获得用户拥有某商家的红包总额
     * @param $userCode
     * @param $shopCode
     * @return float
     */
    public function userTotalBonusValue($userCode, $shopCode) {
        $condition['userCode'] = $userCode;
        $condition['shopCode'] = $shopCode;
        $bonus = $this
            ->field(array('totalValue'))
            ->where($condition)
            ->find();
        return number_format($bonus['totalValue'] / C('RATIO'),2,'.','');
    }

}