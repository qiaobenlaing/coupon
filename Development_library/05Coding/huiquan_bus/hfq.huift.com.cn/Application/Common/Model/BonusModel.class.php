<?php
namespace Common\Model;
use Think\Model;
/**
 * activity表
 * @author
 */
class BonusModel extends BaseModel {
    protected $tableName = 'Bonus';
    const ZERO = 0;

    /**
     * 获得奖励邀请人的1元红包
     * @return array
     */
    public function getRewardBonus() {
        return $this->field(array('bonusCode', 'shopCode', 'lowerPrice'))->where(array('bonusType' => C('BONUS_TYPE.REWARD')))->find();
    }

    /**
     * 删除商家员工
     * @param string $shopCode 商家编码
     * @return boolean 成功返回true，失败返回false
     */
    public function delBonus($shopCode) {
        return $this->where(array('shopCode' => $shopCode))->delete() !== false ? true : false;
    }

    /**
     * 顾客端获得红包详情
     * @param string $bonusCode 红包编码
     * @return array $bonusInfo
     */
    public function sGetBonusInfo($bonusCode) {
        $bonusInfo = $this
            ->field(array(
                'bonusCode',
                'batchNbr',
                'totalVolume',
                'lowerPrice',
                'upperPrice',
                'startTime',
                'endTime',
                'totalValue',
                'status',
            ))
            ->where(array('bonusCode' => $bonusCode))
            ->find();
        $temp = array('lowerPrice', 'upperPrice', 'totalValue');
        foreach($temp as $v) {
            $bonusInfo[$v] = $bonusInfo[$v] / C('RATIO');
        }
        $bonusCode = $bonusInfo['bonusCode'];
        $userBonusMdl = new UserBonusModel();
        $bonusInfo['getNbr'] = $userBonusMdl->countBonus($bonusCode);
        $bonusInfo['getPercent'] = round($bonusInfo['getNbr'] / $bonusInfo['totalVolume'] * C('RATIO'), 2);
        $bonusInfo['getValue'] = $userBonusMdl->countUserBonusValue($bonusCode);
        $bonusInfo['getValuePercent'] =  round($bonusInfo['getValue'] / $bonusInfo['totalValue'] * C('RATIO'), 2);
        return $bonusInfo;
    }

    /**
     * 获得发红包数量最多的商家编码
     */
    public function getShopCodeByBonusAmount() {
        $shopList = $this
            ->field(array('shopCode', 'count(bonusCode)' => 'bonusCount'))
            ->where(array('shopCode' => array('NEQ', C('HQ_CODE'))))
            ->group('shopCode')
            ->order('bonusCount desc')
            ->limit(1)
            ->select();
        return $shopList[0]['shopCode'];
    }

    /**
     * 管理端修改红包状态
     * @param string $bonusCode 红包编码
     * @param int $status
     * @return true|string 成功放回true;失败返回错误信息
     */
    public function changeBnousStatus($bonusCode, $status) {
        return $this->where(array('bonusCode' => $bonusCode))->data(array('status' => $status))->save() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
    }

    /**
     * 阿拉伯数字转中文简体字
     * @param number $num 数组
     * @param bool $mode
     * @return string
     */
    function numToChar($num,$mode = true) {
        $char = array('零','一','二','三','四','五','六','七','八','九');
        //$char = array('零','壹','贰','叁','肆','伍','陆','柒','捌','玖);
        $dw = array('','十','百','千','','万','亿','兆');
        //$dw = array('','拾','佰','仟','','萬','億','兆');
        $dec = '点';  //$dec = '點';
        $retval = '';
        if($mode) {
            preg_match_all('/^0*(\d*)\.?(\d*)/',$num, $ar);
        }else{
            preg_match_all('/(\d*)\.?(\d*)/',$num, $ar);
        }
        if($ar[2][0] != ''){
            $retval = $dec . $this->numToChar($ar[2][0],false); //如果有小数，先递归处理小数
        }
        if($ar[1][0] != ''){
            $str = strrev($ar[1][0]);
            for($i=0;$i<strlen($str);$i++) {
                $out[$i] = $char[$str[$i]];
                if($mode){
                    $out[$i] .= $str[$i] != '0'? $dw[$i%4] : '';
                    if($str[$i]+$str[$i-1] == 0){
                        $out[$i] = '';
                    }
                    if($i%4 == 0){
                        $out[$i] .= $dw[4+floor($i/4)];
                    }
                }
            }
            $retval = join('',array_reverse($out)) . $retval;
        }
        return $retval;
    }

    /**
     * 管理端获取红包列表
     * @param array $filterData
     * @param object $page 页码
     * @return null|array 如果为空，返回null；如果不为空，返回二维数组array(array());
     */
    public function listBonus($filterData, $page) {
        $where = $this->filterWhere(
            $filterData,
            array('shopName' => 'like', 'bonusName' => 'like', 'startTime' => 'egt', 'endTime' => 'elt'),
            $page);
        $where = $this->secondFilterWhere($where);
        $bonusList = $this
            ->field(array(
                'bonusCode',
                'shopName',
                'bonusName',
                'totalVolume',
                'remaining',
                'totalValue',
                'startTime',
                'endTime',
                'lowerPrice',
                'upperPrice',
                'createTime',
                'Bonus.status',
                'batchNbr'
            ))
            ->join('Shop ON Shop.shopCode = Bonus.shopCode', 'LEFT')
            ->where($where)
            ->order('createTime DESC')
            ->pager($page)
            ->select();
        if($bonusList) {
            $userBonusMdl = new UserBonusModel();
            foreach($bonusList as &$v) {
                $v['totalValue'] = $v['totalValue'] / C('RATIO');
                $v['lowerPrice'] = $v['lowerPrice'] / C('RATIO');
                $v['upperPrice'] = $v['upperPrice'] / C('RATIO');
                $grabValue = $userBonusMdl->countGrabValue($v['bonusCode']);//已经被抢的金额，单位分
                $v['restValue'] = $v['totalValue'] - $grabValue / C('RATIO');
            }
        }
        return $bonusList;
    }

    /**
     * 管理端红包总数
     * @param array $filterData
     * @return null|int 如果为空，返回null；如果不为空，返回number;
     */
    public function countBonus($filterData) {
        $where = $this->filterWhere(
            $filterData,
            array('shopName' => 'like', 'bonusName' => 'like', 'startTime' => 'egt', 'endTime' => 'elt')
        );
        $where = $this->secondFilterWhere($where);
        return $this
            ->join('Shop ON Shop.shopCode = Bonus.shopCode', 'LEFT')
            ->where($where)
            ->count('bonusCode');
    }

    /**
     * 第二次过滤条件
     * @param array $where 条件
     * @return array $where
     */
    public function secondFilterWhere(&$where) {
        if($where['status'] || $where['status'] == '0') {
            $where['Bonus.status'] = $where['status'];
            unset($where['status']);
        }
        return $where;
    }

    /**
     * 红包详情
     * @param string $bonusCode 红包编码
     * @return array$bonusInfo
     */
    public function getBonusInfo($bonusCode) {
        $bonusInfo = $this
            ->field(array('Bonus.*', 'Shop.shopName', 'Shop.shopCode', 'BackgroundTemplate.url'))
            ->join('Shop on Shop.shopCode = Bonus.shopCode')
            ->join('BackgroundTemplate on BackgroundTemplate.bgCode = Bonus.bgImgCode', 'LEFT')
            ->where(array('bonusCode' => $bonusCode))->find();
        $bonusInfo = $this->dividedByHundred($bonusInfo, array('upperPrice', 'lowerPrice', 'totalValue'));
        return $bonusInfo;
    }

    /**
     * 编辑红包
     * @param array $bonusInfo 红包信息
     * @return array
     */
    public function editBonus($bonusInfo) {
        $rules = array(
            array('bonusName', 'require', C('BONUS.NAME_ERROR')),
            array('bonusBelonging', 'require', C('BONUS.TYPE_ERROR')),
            array('shopCode', 'require', C('SHOP.SHOP_CODE_ERROR')),
            array('creatorCode', 'require', C('BONUS.CREATOR_CODE_ERROR')),
            array('upperPrice', 'require', C('BONUS.UPPERPRICE_ERROR')),
            array('upperPrice', 'is_numeric', C('BONUS.UPPERPRICE_ERROR'), 0, 'function'),
            array('lowerPrice', 'require', C('BONUS.LOWERPRICE_ERROR')),
            array('lowerPrice', 'is_numeric', C('BONUS.UPPERPRICE_ERROR'), 0, 'function'),
            array('totalValue', 'require', C('BONUS.TOTAL_VALUE_ERROR')),
            array('totalValue', 'is_numeric', C('BONUS.UPPERPRICE_ERROR'), 0, 'function'),
            array('totalVolume', 'require', C('BONUS.TOTAL_VOLUME_ERROR')),
            array('totalVolume', 'is_numeric', C('BONUS.TOTAL_VOLUME_ERROR'), 1, 'function'),
            array('startTime', 'require', C('BONUS.START_TIME_ERROR')),
            array('endTime', 'require', C('BONUS.END_TIME_ERROR')),
            array('startUseTime', 'require', C('BONUS.START_USE_TIME_ERROR')),
            array('endUseTime', 'require', C('BONUS.END_USE_TIME_ERROR')),
            array('validityPeriod', 'require', C('BONUS.VALIDITY_PERIOD_ERROR')),
        );
        if($bonusInfo['upperPrice'] < $bonusInfo['lowerPrice']) {
            return $this->getBusinessCode(C('BONUS.UPPER_LT_LOWER'));
        }
        if(strtotime($bonusInfo['endTime']) < strtotime($bonusInfo['startTime'])) {
            return $this->getBusinessCode(C('BONUS.START_GT_END'));
        }
        if($bonusInfo['totalValue'] < $bonusInfo['upperPrice'] && $bonusInfo['totalValue'] >= 0) {
            return $this->getBusinessCode(C('BONUS.TOTAL_VALUE_TOO_LOW'));
        }
        if($bonusInfo['bonusType'] == C('BONUS_TYPE.REWARD')) {
            $bonusInfo['startTime'] = $bonusInfo['endTime'] = '0000-00-00 00:00:00';
        }
        if($this->validate($rules)->create($bonusInfo) != false) {
            if($bonusInfo['totalValue'] > $bonusInfo['upperPrice'] * $bonusInfo['totalVolume']) {
                return $this->getBusinessCode(C('BONUS.TOTAL_VALUE_TOO_HIGH'));
            }
            if($bonusInfo['totalValue'] < $bonusInfo['lowerPrice'] * $bonusInfo['totalVolume']) {
                return $this->getBusinessCode(C('BONUS.TOTAL_VALUE_TOO_LOW'));
            }
            if($bonusInfo['url']) {
                $backgroundTemplateMdl = new BackgroundTemplateModel();
                $creatorCode = $bonusInfo['creatorCode'] ? $bonusInfo['creatorCode'] : $bonusInfo['shopCode'];
                $bgCode = $backgroundTemplateMdl->addBackgroundTemplate($bonusInfo['url'], $creatorCode, C('BACKGROUND_TYPE.BONUS'));
                $bonusInfo['bgImgCode'] = $bgCode == C('API_INTERNAL_EXCEPTION') ? '' : $bgCode;
            }
            unset($bonusInfo['url']);

            // 将元转化为分
            $bonusInfo = $this->byHundred($bonusInfo, array('upperPrice', 'lowerPrice', 'totalValue'));

            if(isset($bonusInfo['bonusCode']) && $bonusInfo['bonusCode']) {
                $code = $this->where(array(
                    'bonusCode' => $bonusInfo['bonusCode']
                ))->save($bonusInfo) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }else{
                $bonusInfo['bonusCode'] = $this->create_uuid();
                $bonusInfo['createTime'] = date('Y-m-d H:i:s', time());
                $bonusInfo['remaining'] = $bonusInfo['totalVolume'];
                $startTime = date('Y-m-d 00:00:00', time());
                $endTime = date('Y-m-d 23:59:59', time());
                $bonusCount = $this
                    ->where(array(
                        'shopCode' => $bonusInfo['shopCode'],
                        'createTime' => array('BETWEEN', array($startTime, $endTime))
                    ))
                    ->count('bonusCode');
                $bonusInfo['batchNbr'] = $this->setBonusBatchNbr($bonusCount);
                $code = $this->add($bonusInfo) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            if($code == C('SUCCESS')) {
                // 发商家广播
                $msgMdl = new MessageModel();
                $msgInfo = C('SHOP_BROADCASTING.ISSUE_BONUS');
                $msgMdl->shopBroadcasting($bonusInfo['shopCode'], $msgInfo['TITLE'], $msgInfo['CONTENT']);
            }
            return $this->getBusinessCode($code);
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 设置红包批次号
     * @param string $bonusCount 当日发红包总数
     * @return string $batchNbr
     */
    public function setBonusBatchNbr($bonusCount) {
        $batchNbr = date('Ymd', time()) . (intval($bonusCount) + 1);
        return $batchNbr;
    }

    /**
     * 获取某一商家红包的统计信息
     * @param $filterData array('shopCode'=>$shopCode,...)
     * @param $page
     * @return array
     */
    public function getBonusList($filterData, $page) {
        $where = $this->filterWhere(
            $filterData,
            $page);
        $bonusList = $this
            ->field(array('Bonus.bonusCode', 'Bonus.batchNbr', 'Bonus.totalVolume', 'BackgroundTemplate.url', 'Bonus.createTime', 'logoUrl', 'Bonus.status'))
            ->join('BackgroundTemplate ON BackgroundTemplate.bgCode = Bonus.bgImgCode', 'LEFT')
            ->join('Shop ON Shop.shopCode = Bonus.shopCode')
            ->where($where)
            ->pager($page)
            ->order('createTime desc, batchNbr asc')
            ->select();
        if($bonusList) {
            $userBonusMdl = new UserBonusModel();
            foreach($bonusList as &$v){
                $v['getNbr'] = $userBonusMdl->countBonus(array($v['bonusCode']));
//                $v['usedNbr'] = $userBonusMdl->countBonus(array($v['bonusCode']), C('BONUS_STATUS.USED'));
//                $v['totalConsumption'] = $userBonusMdl->getBonusConsume($v['bonusCode']);
                $v['getPercent'] = round($v['getNbr'] / $v['totalVolume'] * C('RATIO'), 2);
                unset($v['url']);
            }
        }
        return $bonusList;
    }

    /**
     * 统计某一商家红包的统计信息的记录数
     * @param $filterData array('shopCode'=>$shopCode,...)
     * @return array
     */
    public function countShopBonus($filterData) {
        $where = $this->filterWhere(
            $filterData);
        $bonusCount = $this
            ->field(array('Bonus.bonusCode', 'Bonus.batchNbr', 'BackgroundTemplate.url'))
            ->join('BackgroundTemplate ON BackgroundTemplate.bgCode = Bonus.bgImgCode', 'LEFT')
            ->where($where)
            ->count('bonusCode');
        return $bonusCount;
    }

    /**
     * 顾客抢红包
     * @param string $userCode 顾客编码
     * @param string $bonusCode 红包编码
     * @return array
     */
    public function grabBonus($userCode, $bonusCode) {
        M()->startTrans();
        $bonusInfo = $this
            ->field(array(
                'shopCode',
                'upperPrice',
                'lowerPrice',
                'totalValue',
                'nbrPerDay',
                'totalVolume',
                'remaining',
                'startTime',
                'endTime',
            ))
            ->where(array('bonusCode' => $bonusCode))
            ->find();

        // 随机获得一个商家
        $shopMdl = new ShopModel();
        $shop = $shopMdl->getShopInfoRandomly();
        // 是否达到抢红包时间，
        if(strtotime($bonusInfo['startTime']) > time()) {
            return array('code' => C('BONUS.NO_BEGINNING'), 'shopCode' => $shop['shopCode'], 'logoUrl' => $shop['logoUrl']);
        }
        // 是否已经过了抢红包的时间
        if(time() > strtotime($bonusInfo['endTime'])) {
            return array('code' => C('BONUS.IS_OVER'), 'shopCode' => $shop['shopCode'], 'logoUrl' => $shop['logoUrl']);
        }
        // 判断红包是否已经被领完
        $remaining = $bonusInfo['remaining'];
        if($remaining == self::ZERO) {
            return array('code' => C('BONUS.EMPTY'), 'shopCode' => $shop['shopCode'], 'logoUrl' => $shop['logoUrl']);
        }
        $userBonusMdl = new UserBonusModel();
        $todayGrabNbr = $userBonusMdl->countTodayGrabNbr($bonusCode);
        // 若限制每日红包领取数量，判断今天的红包是否已经被领完
        if($todayGrabNbr >= $bonusInfo['nbrPerDay'] && $bonusInfo['nbrPerDay'] != self::ZERO) {
            return array('code' => C('BONUS.REACH_DAY_LIMIT'), 'shopCode' => $bonusInfo['shopCode'], 'logoUrl' => $shop['logoUrl']);
        }
        // 用户是否可以领取红包
        $isGrab = $userBonusMdl->isGrab($userCode, $bonusCode);
        if($isGrab) {
            return array('code' => C('BONUS.REPEAT'), 'shopCode' => $shop['shopCode'], 'logoUrl' => $shop['logoUrl']);
        }
        $grabValue = $userBonusMdl->countGrabValue($bonusCode);//已经被抢的金额，单位分
        $restValue = $bonusInfo['totalValue'] - $grabValue;
        $lowerPrice = $bonusInfo['lowerPrice'];
        $upperPrice = $bonusInfo['upperPrice'];
        if($restValue < $lowerPrice) {
            return array('code' => C('BONUS.EMPTY'), 'shopCode' => $shop['shopCode'], 'logoUrl' => $shop['logoUrl']);
        }
        if($bonusInfo['remaining'] == 1) {
            $value = $restValue;//单位：分
        } else {
            $value = $this->setUserBonusValue($restValue, $bonusInfo['remaining'], $upperPrice, $lowerPrice);//单位：分
        }
        $userBonusInfo = array(
            'userCode' => $userCode,
            'bonusCode' => $bonusCode,
            'value' => $value, // 单位：分
        );
        $ret1 = $userBonusMdl->addUserBonus($userBonusInfo);
        $ret2 = $this->where(array('bonusCode' => $bonusCode))->setDec('remaining', 1);
        if($ret1 && $ret2) {
            M()->commit();
            $bsMdl = new BonusStatisticsModel();
            $bsMdl->updateBonusStatistics($userCode, $bonusInfo['shopCode'], $value);
            $code = C('SUCCESS');
            return array('code' => $code, 'value' => $value / C('RATIO'), 'shopCode' => $shop['shopCode'], 'logoUrl' => $shop['logoUrl']);
        } else {
            M()->rollback();
            $code = C('API_INTERNAL_EXCEPTION');
            return array('code' => $code, 'shopCode' => $shop['shopCode'], 'logoUrl' => $shop['logoUrl']);
        }
    }

    /**
     * 获得用户红包金额。
     * @param int $bonusRestValue 红包剩余金额 单位：分
     * @param int $remaining 红包剩余数量
     * @param int $upperValue 红包上限，单位：分
     * @param int $lowerValue 红包下限，单位：分
     * @return int $userBonusValue 用户红包金额 ，单位：分
     */
    public function setUserBonusValue($bonusRestValue, $remaining, $upperValue, $lowerValue) {
        if($remaining * $upperValue == $bonusRestValue) {
            $userBonusValue = $upperValue;
            return $userBonusValue;
        }
        if($remaining * $lowerValue == $bonusRestValue) {
            $userBonusValue = $lowerValue;
            return $userBonusValue;
        }
        $userBonusValue = mt_rand($lowerValue / C('RATIO'), $upperValue / C('RATIO')) * C('RATIO');
        $restValue = $bonusRestValue - $userBonusValue;
        $restAverage = $restValue / ($remaining - 1);
        if($restAverage >= $lowerValue && $restAverage <= $upperValue) {
            return $userBonusValue;
        } else {
            return $this->setUserBonusValue($bonusRestValue, $remaining, $upperValue, $lowerValue);
        }
    }

    /**
     * 得到红包编码的列表
     * @param string $shopCode 商家编码
     * @return array
     */
    public function listBonusCode($shopCode) {
        return $this->where(array('shopCode' => $shopCode))->getField('bonusCode', true);
    }

    /**
     * 获得平台发行距离当前时间最近的红包
     * @return array
     */
    public function getPlateBonus() {
        $createTime = $this
            ->where(array(
                'bonusBelonging' => C('BONUS_BELONGING.PLAT'),
//                'remaining' => array('GT', self::ZERO),
                'endTime' => array('EGT', date('Y-m-d H:i:s', time())),
                'status' => C('BONUS_STATUS.ACTIVE'),
                'bonusType' => C('BONUS_TYPE.GRAB'),
            ))
            ->max('createTime');
        return $this
            ->field(array('bonusCode', 'startTime', 'endTime', 'preUrl', 'ingUrl', 'afterUrl',))
            ->where(array(
                'bonusBelonging' => C('BONUS_BELONGING.PLAT'),
                'createTime' => $createTime,
                'status' => C('BONUS_STATUS.ACTIVE'),
                'bonusType' => C('BONUS_TYPE.GRAB'),
            ))
            ->find();
    }

    /**
     * 分析某个商户的红包
     * @param array $condition 条件
     * @return array
     */
    public function analysisShopBonus($condition){
        $totalVolume = $this->where($condition)->sum('totalVolume');
        $bonusCodeList = $this->where($condition)->getField('bonusCode', true);
        $userBonusMdl = new UserBonusModel();
        $takeAmount = $bonusCodeList ? $userBonusMdl->countBonus($bonusCodeList) : 0;
        $userConsumeMdl = new UserConsumeModel();
        if($condition['createTime']) {
            $where['consumeTime'] = $condition['createTime'];
        }
        $where['location'] = $condition['shopCode'];
        $where['status'] = C('UC_STATUS.PAYED');
        $shopBonusUsedAmount = $userConsumeMdl->shopBonusConsumeAmount($where);
        $shopBonusConsumeAmount = $userConsumeMdl->shopBonusConsumeTotalAmount($where);
        $platBonusUsedAmount = $userConsumeMdl->platBonusConsumeAmount($where);
        $platBonusConsumeAmount = $userConsumeMdl->platBonusConsumeTotalAmount($where);
        return array(
            'totalVolume' => $totalVolume ? $totalVolume : 0, // 商户红包发放数量
            'takeAmount' => $takeAmount ? $takeAmount : 0, // 商户红包领用数量
            'shopBonusUsedAmount' => $shopBonusUsedAmount ? $shopBonusUsedAmount : 0, // 商户红包使用总额
            'shopBonusConsumeAmount' => $shopBonusConsumeAmount ? $shopBonusConsumeAmount : 0, // 使用商家红包的消费总额
            'platBonusUsedAmount' => $platBonusUsedAmount ? $platBonusUsedAmount : 0, // 平台红包使用总额
            'platBonusConsumeAmount' => $platBonusConsumeAmount ? $platBonusConsumeAmount : 0, // 使用平台红包的消费总额
            // TODO
        );
    }

    /**
     * 管理端红包统计分析
     * @param $condition
     * @param $page
     * @return mixed
     */
    public function analysisBonus($condition, $page) {
        $condition = $this->filterWhere($condition, array('city' => 'like'));
        $where = array();
        if($condition['time']) {
            $where['Bonus.createTime'] = array('EGT', date('Y-m-d H:i:s', time() - $condition['time'] * 86400));
        }
        if($condition['city']) {
            $where['Shop.city'] = $condition['city'];
        }
        if($condition['type']) {
            $shopTypeRelMdl = new ShopTypeRelModel();
            $shopCodeArr = $shopTypeRelMdl->getFieldList(array('typeValue' => $condition['type']), 'shopCode', array(array('joinTable' => 'ShopType', 'joinCondition' => 'ShopType.shopTypeId = ShopTypeRel.typeId', 'joinType' => 'inner')));
            if(empty($shopCodeArr)){
                $shopCodeArr = array('0');
            }
            $shopCodeArr = array_unique($shopCodeArr);
            $where['Shop.shopCode'] = array('IN', $shopCodeArr);
        }

        $lowerValue = $condition['lowerValue'];
        $upperValue = $condition['upperValue'];
        if($lowerValue > 0) {
            $where['Bonus.lowerPrice'] = array('EGT', $lowerValue  * 100);
        }
        if($upperValue){
            $where['Bonus.upperPrice'] = array('ELT', $upperValue  * 100);
        }

        $bonusList = $this
            ->field(array('Shop.shopName', 'Bonus.bonusCode', 'Bonus.bonusName', 'Bonus.totalValue', 'Bonus.totalVolume', 'Bonus.lowerPrice', 'Bonus.upperPrice'))
            ->join('Shop ON Shop.shopCode = Bonus.shopCode', 'LEFT')
            ->where($where)
            ->order('createTime desc')
            ->pager($page)
            ->select();
        foreach($bonusList as &$v){
            $v['totalValue'] = number_format($v['totalValue'] / C('RATIO'), 2, '.', '');
            $v['lowerPrice'] = number_format($v['lowerPrice'] / C('RATIO'), 2, '.', '');
            $v['upperPrice'] = number_format($v['upperPrice'] / C('RATIO'), 2, '.', '');
            $userBonusMdl = new UserBonusModel();
            $v['countGrab'] = $userBonusMdl->countBonus(array($v['bonusCode']));
            $totalGrabValue = $userBonusMdl->countGrabValue($v['bonusCode']);
            $v['totalGrabValue'] = number_format($totalGrabValue / C('RATIO'), 2, '.', '');
        }
        return $bonusList;
    }

    public function countAnalysisBonus($condition) {
        $condition = $this->filterWhere($condition, array('city' => 'like'));
        $where = array();
        if($condition['time']) {
            $where['Bonus.createTime'] = array('EGT', date('Y-m-d H:i:s', time() - $condition['time'] * 86400));
        }
        if($condition['city']) {
            $where['Shop.city'] = $condition['city'];
        }
        if($condition['type']) {
            $shopTypeRelMdl = new ShopTypeRelModel();
            $shopCodeArr = $shopTypeRelMdl->getFieldList(array('typeValue' => $condition['type']), 'shopCode', array(array('joinTable' => 'ShopType', 'joinCondition' => 'ShopType.shopTypeId = ShopTypeRel.typeId', 'joinType' => 'inner')));
            if(empty($shopCodeArr)){
                $shopCodeArr = array('0');
            }
            $shopCodeArr = array_unique($shopCodeArr);
            $where['Shop.shopCode'] = array('IN', $shopCodeArr);
        }

        $lowerValue = $condition['lowerValue'];
        $upperValue = $condition['upperValue'];
        if($lowerValue > 0) {
            $where['Bonus.lowerPrice'] = array('EGT', $lowerValue  * 100);
        }
        if($upperValue){
            $where['Bonus.upperPrice'] = array('ELT', $upperValue  * 100);
        }
        $bonusCount = $this
            ->join('Shop ON Shop.shopCode = Bonus.shopCode', 'LEFT')
            ->where($where)
            ->count('bonusCode');
        return $bonusCount?$bonusCount:0;
    }
}