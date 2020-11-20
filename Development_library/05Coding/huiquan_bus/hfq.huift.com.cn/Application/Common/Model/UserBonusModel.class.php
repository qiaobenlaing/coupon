<?php
namespace Common\Model;
use Think\Model;
/**
 * activity表
 * @author
 */
class UserBonusModel extends BaseModel {
    protected $tableName = 'UserBonus';
    const zero = 0;

    /**
     * 给用户的邀请人送红包
     * @param string $userCode 邀请人编码
     * @return boolean
     */
    public function sendInviterBonus($userCode) {
        $systemParamMdl = new SystemParamModel();
        // 获得邀请人绑银行卡后，送给邀请人的红包的信息
        $bonusRewardInfo = $systemParamMdl->getParamValue('bonusReward');
        $data = array(
            'userBonusCode' => $this->create_uuid(),
            'userCode' => $userCode,
            'value' => $bonusRewardInfo['value'],
            'getDate' => date('Y-m-d H:i:s'),
            'bonusCode' => '', // 红包编码为空，特指被邀请人绑银行卡后，送给邀请人的红包
        );
        $ret = $this->add($data);
        if($ret !== false ) {
            $bonusStatisticsMdl = new BonusStatisticsModel();
            // 增加用户的红包金额
            $ret = $bonusStatisticsMdl->updateBonusStatistics($userCode, C('HQ_CODE'), $bonusRewardInfo['value']);
        }
        return $ret;
    }

    /**
     * 获得用户得到的奖励红包金额
     * @param string $userCode 用户编码
     * @return int
     */
    public function countUserRewardBonus($userCode) {
        $userRewardBonusAmount = $this->join(array('Bonus ON Bonus.bonusCode = UserBonus.bonusCode'))->where(array('userCode' => $userCode, 'bonusType' => C('BONUS_TYPE.REWARD')))->sum('value');
        // 绑定的银行卡送的红包
        $userRewardBonusAmount += $this->where(array('userCode' => $userCode, 'bonusCode' => ''))->sum('value');
        return $userRewardBonusAmount / C('RATIO');
    }

    /**
     * 给推荐人添加奖励红包
     * @param string $userCode 用户编码
     * @return boolean
     */
    public function addRewardBonus($userCode) {
        $systemParamMdl = new SystemParamModel();
        $paramInfo = $systemParamMdl->getParamValue('inviteStatus');
        if($paramInfo['value'] == C('NO')) {
            return true;
        }
        $bonusMdl = new BonusModel();
        $rewardBonusInfo = $bonusMdl->getRewardBonus();
        $ret = $this->add(array('userBonusCode' => $this->create_uuid(), 'userCode' => $userCode, 'bonusCode' => $rewardBonusInfo['bonusCode'], 'value' => 100, 'getDate' => date('Y-m-d H:i:s')));
        if($ret !== false) {
            $bonusStatisticsMdl = new BonusStatisticsModel();
            $bonusStatisticsMdl->updateBonusStatistics($userCode, $rewardBonusInfo['shopCode'], 100);
        }
    }


    /**
     * 统计用户已经领取的红包总额
     * @param string $bonusCode 红包编码
     * @return float $bonusValue 单位：元
     */
    public function countUserBonusValue($bonusCode) {
        $bonusValue = $this->where(array('bonusCode' => $bonusCode))->sum('value');
        return $bonusValue / C('RATIO');
    }

    /**
     * 更新用户红包的状态
     * @param array $where 条件
     * @param array $data 数据
     * @return boolean
     */
    public function updateUserBonusStatus($where, $data) {
        $userBonusInfo = $this->where($where)->getField('userBonusCode');
        if($userBonusInfo) {
            return $this->where($where)->save($data) !== false ? true : false;
        }
        return true;
    }

    /**
     * 获得实际付款金额
     * @param string $userBonusCode 用户红包编码
     * @param int $realPay 实际付款金额，单位：分
     * @return int $realPay 实际付款金额，单位：分
     */
    public function getRealPay($userBonusCode, $realPay) {
        $userBonusInfo = $this->field(array('value'))->where(array('userBonusCode' => $userBonusCode))->find();
        $realPay = $realPay - $userBonusInfo['value'];
        return $realPay;
    }
    /**
     * 判断用户红包是否可用
     * @param string $userBonusCode 用户红包编码
     * @return boolean||string 如果可用返回true,如果不可用返回错误代码
     */
    public function isUserBonusCanBeUsed($userBonusCode) {
        $userBonusInfo = $this
            ->field(array('userBonusCode', 'getDate', 'Bonus.validityPeriod'))
            ->join('Bonus ON Bonus.bonusCode = UserBonus.bonusCode')
            ->where(array('userBonusCode' => $userBonusCode, 'UserBonus.status' => C('BONUS_STATUS.ACTIVE')))->find();
        if(! $userBonusInfo) {
            return C('BONUS.NOT_AVAILABLE');
        }
        if(strtotime($userBonusInfo['getDate']) + 86400 * $userBonusInfo['validityPeriod'] < time()){
            return C('BONUS.EXPIRED');
        }
        return  true;
    }

    /**
     * 付款时获得用户可使用的红包列表
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @param float $consumeAmount 消费金额，单位：元
     * @return array $userCouponList
     */
    public function listUserBonusWhenPay($userCode, $shopCode, $consumeAmount) {
        $userBonusList = $this
            ->field(array(
                'userBonusCode',
                'value',
                'UNIX_TIMESTAMP(getDate) + (validityPeriod * 86400)' => 'expireTime'
            ))
            ->join('Bonus ON Bonus.bonusCode = UserBonus.bonusCode')
            ->where(array(
                'userCode' => $userCode,
                'shopCode' => $shopCode,
//                'endUseTime' => array('EGT', date('Y-m-d H:i:s', time())),
                'UNIX_TIMESTAMP(getDate) + (validityPeriod * 86400)' => array('EGT', time()),
                'value' => array('ELT', $consumeAmount * C('RATIO')),
            ))
            ->order('value DESC, getDate asc')
            ->select();
        foreach($userBonusList as &$v) {
            $v['value'] = $v['value'] / C('RATIO');
            $v['expireTime'] = date('Y-m-d H:i:s', $v['expireTime']);
        }
        return $userBonusList ? $userBonusList : $userBonusList;
    }

    /**
     * 管理端获取用户所有的红包信息
     * @param array $filterData
     * @param Object $page 偏移值
     * @return null|array 如果为空，返回null；如果不为空，返回二维数组array(array());
     */
    public function listUserBonus($filterData, $page) {
        $where = $this->filterWhere(
            $filterData,
            array('bonusName' => 'like', 'shopName' => 'like', 'mobileNbr' => 'like'),
            $page);
        $where = $this->secondFilterWhere($where);
        $userBonusList = $this
            ->field(array(
                'UserBonus.bonusCode',
                'getDate',
                'UserBonus.status' => 'userBonusStatus',
                'UserBonus.value',
                'bonusName',
                'validityPeriod',
                'Shop.shopName',
                'consumeTime',
                'User.realName',
                'User.nickName',
                'User.mobileNbr',
            ))
            ->join('Bonus ON Bonus.bonusCode = UserBonus.bonusCode', 'LEFT')
            ->join('Shop ON Shop.shopCode = Bonus.shopCode', 'LEFT')
            ->join('UserConsume ON UserConsume.consumeCode=UserBonus.consumeCode', 'LEFT')
            ->join('User ON User.userCode = UserBonus.userCode')
            ->where($where)
            ->order('getDate desc')
            ->pager($page)
            ->select();
        foreach($userBonusList as &$v) {
            $v['value'] = $v['value'] / C('RATIO');
        }
        return $userBonusList;
    }

    /**
     * 管理端获得用户的红包总数
     * @param array $filterData
     * @return int
     */
    public function countUserBonus($filterData) {
        $where = $this->filterWhere(
            $filterData,
            array('bonusName' => 'like', 'shopName' => 'like', 'mobileNbr' => 'like')
        );
        $where = $this->secondFilterWhere($where);
        return $this
            ->join('Bonus ON Bonus.bonusCode = UserBonus.bonusCode', 'LEFT')
            ->join('Shop ON Shop.shopCode = Bonus.shopCode', 'LEFT')
            ->join('UserConsume ON UserConsume.consumeCode=UserBonus.consumeCode', 'LEFT')
            ->join('User ON User.userCode = UserBonus.userCode')
            ->where($where)
            ->count();
    }

    /**
     * 第二次过滤条件
     * @param array $where
     * @return array
     */
    public function secondFilterWhere(&$where) {
        if($where['userCode']) {
            $where['User.userCode'] = $where['userCode'];
            unset($where['userCode']);
        }
        if($where['mobileNbr']) {
            $where['User.mobileNbr'] = $where['mobileNbr'];
            unset($where['mobileNbr']);
        }
        if(isset($where['realName'])){
            $subWhere = array();
            $subWhere['User.realName'] = array('like', '%'.$where['realName'].'%');
            $subWhere['User.nickName'] = array('like', '%'.$where['realName'].'%');
            $subWhere['_logic'] = 'OR';
            $where['_complex'] = $subWhere;
            unset($where['realName']);
        }
        return $where;
    }

    /**
     * 获取某种红包各状态的数量
     * @param string $bonusCodeArr 红包编码
     * @param int $status 红包状态
     * @return int
     */
    public function countBonus($bonusCodeArr, $status = -1) {
        if($status >= 0){
            return $this->where(array('bonusCode' => array('IN', $bonusCodeArr), 'status' => $status))->count('userBonusCode');
        }else{
            return $this->where(array('bonusCode' => array('IN', $bonusCodeArr)))->count('userBonusCode');
        }
    }

    /**
     * 获得今天某红包被抢的数量
     * @param string $bonusCode 红包编码
     * @return int
     */
    public function countTodayGrabNbr($bonusCode) {
        $todayStart = date('Y-m-d 00:00:00', time());
        $todayEnd = date('Y-m-d 24:00:00', time());
        $amount = $this
            ->where(array(
                'bonusCode' => $bonusCode,
                'getDate' => array('BETWEEN', array($todayStart, $todayEnd))
            ))
            ->count('userBonusCode');
        return $amount;
    }

    /**
     * 获得某红包已经被领取的数量
     * @param string $bonusCode 红包编码
     * @return int $grabValue 单位：分
     */
    public function countGrabValue($bonusCode) {
        $grabValue = $this->where(array('bonusCode' => $bonusCode))->sum('value');
        return $grabValue ? $grabValue : self::zero;
    }

    /**
     * 判断顾客是否已经领取过该红包
     * @param string $userCode 用户编码
     * @param string $bonusCode 红包编码
     * @return boolean 若已经领取返回true，未领取返回false
     */
    public function isGrab($userCode, $bonusCode) {
        $userBonusInfo = $this->field(array('userBonusCode'))->where(array('userCode' => $userCode, 'bonusCode' => $bonusCode))->find();
        return $userBonusInfo ? true : false;
    }

    /**
     * 获取某种红包消费总额
     * @param string $bonusCode
     * @return int
     */
    public function getBonusConsume($bonusCode) {
        $totalConsumption =  $this
            ->join('UserConsume on UserConsume.consumeCode = UserBonus.consumeCode','left')
            ->join('ConsumeOrder on ConsumeOrder.orderCode = UserConsume.orderCode')
            ->where(array('UserBonus.bonusCode' => $bonusCode, 'UserBonus.status' => C('BONUS_STATUS.USED')))
            ->sum('ConsumeOrder.orderAmount / '.C('RATIO'));
        return $totalConsumption ? $totalConsumption : 0;
    }

    /**
     * 顾客新增红包
     * @param array $userBonusInfo 用户红包数据
     * @return boolean 成功返回true，失败返回false
     */
    public function addUserBonus($userBonusInfo) {
        $userBonusInfo['getDate'] = date('Y-m-d H:i:s', time());
        $userBonusInfo['status'] = C('USER_BONUS_STATUS.VALID');
        $userBonusInfo['userBonusCode'] = $this->create_uuid();
        return $this->add($userBonusInfo) !== false ? true : false;
    }

    /**
     * 顾客获得抢到的某个商家的红包
     * @param string $userCode 用户编码
     * @param string $shopCode 商家编码
     * @return array $bonusList
     */
    public function listUserShopBonus($userCode, $shopCode) {
        $bonusMdl = new BonusModel();
        $bonusList = $bonusMdl->listBonusCode($shopCode);
        $bonusList = $this->where(array('userCode' => $userCode, 'bonusCode' => array('IN', $bonusList)))->select();
        return $bonusList ? $bonusList : array();
    }

    /**
     * 获得用户红包的信息
     * @param string $userBonusCode 用户红包编码
     * @return mixed
     */
    public function getBonusInfo($userBonusCode){
        $bonusInfo = $this->field(array('UserBonus.*','Bonus.*'))->join('Bonus on Bonus.bonusCode = UserBonus.bonusCode')->where(array('userBonusCode'=>$userBonusCode))->find();
        $bonusInfo = $this->dividedByHundred($bonusInfo, array('value','upperPrice', 'lowerPrice', 'totalValue'));
        return $bonusInfo;
    }

    /**
     * 使用红包
     * @param String $userBonusCode 用户领取的红包编码
     * @param Array $useInfo = array('status'=>2,'consumeCode'=>$consumeCode)
     * @return boolean
     */
    public function useBonus($userBonusCode, $useInfo){
        return $this->where(array('userBonusCode' => $userBonusCode))->save($useInfo) !== false ? true : false;
    }

    /**
     * 获得红包获取人的名单
     * @param string $bonusCode 红包编码
     * @param object $page 页码
     * @return mixed
     */
    public function listGrabUserBonus($bonusCode, $page){
        $bonusList = $this
            ->field(array('UserBonus.value', 'UserBonus.getDate', 'User.userCode', 'User.nickName', 'User.avatarUrl'))
            ->join('User on User.userCode = UserBonus.userCode','LEFT')
            ->where(array('bonusCode' => $bonusCode))
            ->pager($page)
            ->order('UserBonus.getDate desc')
            ->select();
        foreach($bonusList as &$v){
            $v['value'] = $v['value'] / C('RATIO');
        }
        return $bonusList;
    }

    /**
     * 统计人数
     * @param $bonusCode
     * @return int
     */
    public function countGrabUserBonus($bonusCode){
        $condition['bonusCode'] = $bonusCode;
        $bonusCount = $this
            ->join('User on User.userCode = UserBonus.userCode','LEFT')
            ->where($condition)
            ->count('userBonusCode');
        return $bonusCount;
    }
}