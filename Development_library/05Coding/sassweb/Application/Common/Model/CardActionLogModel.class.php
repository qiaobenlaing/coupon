<?php
namespace Common\Model;
use Think\Model;
/**
 * cardActionLog表
 * @author 
 */
class CardActionLogModel extends BaseModel {
    const ZERO = 0;
    protected $tableName = 'CardActionLog';

    /**
     * 回滚用户会员卡使用记录
     * @param string $consumeCode 支付记录编码
     * @return boolean 成功返回true，失败返回false
     */
    public function rollbackCardAction($consumeCode) {
        $ret = $this->where(array('consumeCode' => $consumeCode))->delete() !== false ? true : false;
        if($ret === true) {
            // TODO 有待完善
            $cardActionLogInfo = $this->field(array('userCardCode', 'consumeAmount'))->where(array('consumeCode' => $consumeCode))->find();
            $userCardMdl = new UserCardModel();
            $ret = $userCardMdl->decPoint($cardActionLogInfo['userCardCode'], $cardActionLogInfo['consumeAmount'] / C('RATIO'));
        }
        return $ret;
    }

    /**
     * 获得所有使用会员卡消费的消费金额总和
     * @param array $cardCodeList 会员卡编码
     * @return double
     */
    public function getALLConsumeCount($cardCodeList) {
        if(!$cardCodeList) return 0;
        return $this->where(array('cardCode' => array('IN', $cardCodeList)))->count('logCode');
    }

    /**
     * 获得所有使用会员卡消费的消费金额总和
     * @param array $cardCodeList 会员卡编码
     * @return double
     */
    public function getALLConsumeAmount($cardCodeList) {
        if(!$cardCodeList) return 0;
        $consumeAmount = $this->where(array('cardCode' => array('IN', $cardCodeList)))->sum('consumeAmount');
        return $consumeAmount / C('RATIO');
    }

    /**
     * 获得会员卡使用记录详情
     * @param array $where 条件
     * @return array
     */
    public function getCardActionLogInfo($where) {
        return $this->field(array('userCode', 'cardCode', 'userCardCode'))->where($where)->find();
    }
    /**
    * 添加数据
    * @param array $cardActionLogInfo 关联数组
    * @return boolean 添加成功返回true；添加失败返回false
    */
    public function addCardActionLog($cardActionLogInfo) {
        return $this->add($cardActionLogInfo) !== false ? true : false;
    }

    /**
     * 某种会员卡的总消费金额
     * @param string $cardCode 会员卡编码
     * @return int 单位：元
     */
    public function countConsumeAmount($cardCode){
        if(empty($condition)){
            return self::ZERO;
        }
        $condition = array(
            'cardCode' => $cardCode,
            'UserConsume.status' => C('UC_STATUS.PAYED')
        );
        $consumeAmount = $this
            ->join('UserConsume ON UserConsume.consumeCode = CardActionLog.consumeCode')
            ->join('ConsumeOrder ON ConsumeOrder.orderCode = UserConsume.orderCode')
            ->where($condition)
            ->sum('orderAmount');
        return $consumeAmount / C('RATIO');
    }

    /**
     * 获得某个会员某种会员卡的总消费次数、总消费金额、最后一次消费时间
     * @param string $cardCode 会员卡编码
     * @param string $userCode 用户编码
     * @return array
     */
    public function countUserConsumeInfo($cardCode, $userCode) {
        if($cardCode) $condition['cardCode'] = $cardCode;
        if($userCode) $condition['userCode'] = $userCode;
        $condition['UserConsume.status'] = C('UC_STATUS.PAYED');
        $consumeTimes = $this->join('UserConsume ON UserConsume.consumeCode = CardActionLog.consumeCode')->where($condition)->count('logCode');//消费次数
        $consumeAmount = $this->join('UserConsume ON UserConsume.consumeCode = CardActionLog.consumeCode')->where($condition)->sum('consumeAmount');//消费总金额
        $lastActionTime = $this->join('UserConsume ON UserConsume.consumeCode = CardActionLog.consumeCode')->where($condition)->max('actionTime');//最后一次消费时间
        return array(
            'consumeTimes' => $consumeTimes ? $consumeTimes : 0,
            'consumeAmount' => $consumeAmount ? $consumeAmount  / C('RATIO') : 0,
            'lastActionTime' => $lastActionTime ? $lastActionTime : '',
        );
    }

    /**
     * 获得用户使用某会员卡进行消费时抵扣金额的总和
     * @param string $cardCode 会员卡编码
     * @param string $userCode 用户编码
     * @return float $deductionPrice 抵扣金额。单位：元
     */
    public function countDeductionPrice($cardCode, $userCode) {
        $deductionPrice = $this
            ->where(array('cardCode' => $cardCode, 'userCode' => $userCode, 'UserConsume.status' => C('UC_STATUS.PAYED')))
            ->join('UserConsume ON UserConsume.consumeCode = CardActionLog.consumeCode')
            ->sum('deduction');
        return $deductionPrice ? $deductionPrice / 100 : 0;
    }

    /**
     * 根据消费编码获得会员卡积分兑换比例和消费金额
     * @param string $consumeCode 消费编码
     * @return mixed
     */
    public function getConsumeCardInfo($consumeCode){
        return $this
            ->field(array('Card.pointsPerCash', 'CardActionLog.consumeAmount', 'discount'))
            ->join('Card on Card.cardCode = CardActionLog.cardCode')
            ->where(array('consumeCode' => $consumeCode))
            ->find();
    }

    /**
     * 统计用户会员卡在3个月内要到期的积分
     * @param string $userCode 用户编码
     * @param string $cardCode 会员卡编码
     * @return int
     */
    public function countToExpiredPoints($userCode, $cardCode) {
        $userCardMdl = new UserCardModel();
        $field = array('pointLifetime', 'User.userCode', 'pointsPerCash');
        $userCardInfo = $userCardMdl->getUserCardInfoByUC($userCode, $cardCode, $field);
        $pointLifetime = $userCardInfo['pointLifetime'] * 24 * 3600; // 秒数
        $pointsPerCash = $userCardInfo['pointsPerCash'];
        $cardActionLogList = $this->where(array('userCode' => $userCode, 'cardCode' => $cardCode))->select();
        $toExpiredPoints = 0;
        foreach($cardActionLogList as $v) {
            $cosumeTime = $v['consumeTime'];
            if($pointLifetime - (time() - strtotime($cosumeTime)) < $this->toEpiredTime) {
                $toExpiredPoints += ($v['consumeAmount'] * $pointsPerCash);
            }
        }
        return $toExpiredPoints;
    }

    /**
     * 删除会员卡使用记录
     * @param array $condition
     * @return boolean 成功返回true，失败返回false
     */
    public function delCardActionLog($condition){
        return $this->where($condition)->delete() !== false ? true : false;
    }

}