<?php
namespace Common\Model;
use Think\Model;

class OpenGiftLogModel extends BaseModel{
    protected $tableName = 'OpenGiftLog';

    /**
     * @param $field
     * @param $where
     * @param $page
     * @return mixed
     */
    public function listOpenLog($field, $where, $page) {
        if(empty($field)){
            $field = array('OpenGiftLog.*');
        }
        $list = $this
            ->join('User ON User.userCode = OpenGiftLog.userCode')
            ->field($field)
            ->where($where)
            ->order('openTime desc')
            ->pager($page)
            ->select();
        return $list;
    }

    public function countOpenLog($where) {
        return $subAlbumList = $this
            ->join('UserGift ON UserGift.id = OpenGiftLog.userGiftId')
            ->where($where)
            ->count('distinct(OpenGiftLog.userCode)');
    }

    /**
     * 拆礼盒
     * @param array $data 商家信息
     * @return array $ret
     */
    public function addOpenLog($data) {
        $ugMdl = new UserGiftModel();
        $userGiftInfo = $ugMdl->getUserGiftInfo($data['userGiftId']);

        // 是否在拆的时间内
        $prMdl = new PrizeRuleModel();
        $prizeRule = $prMdl->getPrizeRule('', array('id' => $userGiftInfo['prizeRuleId']));
        if(isset($prizeRule['limitTime']) && $prizeRule['limitTime'] > 0){
            $time = $userGiftInfo['grabTime'] + $prizeRule['limitTime'] * 3600;
            if($time < time()){
                return $this->getBusinessCode(C('OPEN_GIFT.LIMIT_TIME'));
            }
        }

        //每人每天限拆次数（3次）
        $listOpenLog = $this->listOpenLog('', array('OpenGiftLog.userCode' => $data['userCode'], 'FROM_UNIXTIME(openTime, "%Y-%m-%d")' => date('Y-m-d', time())), $this->getPager(0));
        if(count($listOpenLog) >= 3){
            return $this->getBusinessCode(C('OPEN_GIFT.LIMIT'));
        }

        //自己不能给自己拆
        if($userGiftInfo['userCode'] == $data['userCode']){
            return $this->getBusinessCode(C('OPEN_GIFT.DISABLE'));
        }

        $res = $this->listOpenLog('', array('userGiftId' => $data['userGiftId'], 'OpenGiftLog.userCode' => $data['userCode']), $this->getPager(0));
        if($res){ // 已经拆过了
            $code = C('OPEN_GIFT.ALREADY');
        }else{
            M()->startTrans();
            $ret = $this->add($data);
            $ret1 = true;
            $ret2 = true;
            if($ret !== false){
                $ret1 = $ugMdl->incOpenNbr($data['userGiftId']);
                $ret2 = $this->convertPrize($data['userGiftId']);
            }
            if($ret && $ret1 && $ret2){
                M()->commit();
                $code = C('SUCCESS');
            }else{
                M()->rollback();
                $code = C('API_INTERNAL_EXCEPTION');
            }
        }
        return $this->getBusinessCode($code);
    }

    public function convertPrize($userGiftId){
        $ugMdl = new UserGiftModel();
        $userGiftInfo = $ugMdl->getUserGiftInfo($userGiftId);
        $prMdl = new PrizeRuleModel();
        $prizeRule = $prMdl->getPrizeRule('', array('id' => $userGiftInfo['prizeRuleId']));
        $code = C('SUCCESS');
        $userGiftList = $ugMdl->listUserGift('', array('prizeRuleId' => $userGiftInfo['prizeRuleId'], 'currentDay' => $userGiftInfo['currentDay'], 'userGiftCode' => array('neq', "")), $this->getPager(0));
        if(count($userGiftList) >= $prizeRule['limitNbr']){
            return $this->getBusinessCode($code);
        }
        if($userGiftInfo['prizeRuleId'] && empty($userGiftInfo['userGiftCode'])){
            if($userGiftInfo['openNbr'] >= $prizeRule['convertNbr']){
                if($prizeRule['prizeType'] == C('PRIZE_TYPE.COUPON')){ // 礼品为优惠券
                    $userCouponMdl = new UserCouponModel();
                    $ret = $userCouponMdl->grabCoupon($prizeRule['giftCode'], $userGiftInfo['userCode'], C('COUPON_SHARED_LVL.ALL'), 1);
                    if($ret['code'] == C('SUCCESS')){
                        $ugMdl->giveGift($userGiftId, array('userGiftCode' => $ret['userCouponCode']));
                        $list = $this
                            ->where(array('userGiftId' => $userGiftId))
                            ->order('openTime asc')
                            ->limit($prizeRule['convertNbr'])
                            ->getField('id', true);
                        $this->where(array('id' => array('in', $list)))->save(array('isUsed' => 1, 'usedTime' => time()));
                    }
                }else{ // TODO 礼品为其他东西(暂无)

                }
            }
        }
        return $this->getBusinessCode($code);
    }

    public function getPager($page){
        if(! isset($page) || $page === '')
            $page = 1;
        return new Pager($page, C('PAGESIZE'));
    }
}