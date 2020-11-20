<?php
namespace Common\Model;
use Think\Model;
/**
 * bankAccount表
 * @author 
 */
class BankAccountModel extends BaseModel {
    protected $tableName = 'BankAccount';

    /**
     * 修改账户信息
     * @param array $condition 条件
     * @param array $data 数据
     * @return boolean 成功返回true,失败返回false
     */
    public function editBankAccount($condition, $data) {
        return $this->where($condition)->save($data) !== false ? true : false;
    }

    /**
     * 用户银行卡列表
     * @param $filterData 查询条件
     * @param $page 页数
     * @return array
     */
    public function listBankAccount($filterData, $page) {
        $where = $this->filterWhere(
            $filterData,
            array('mobileNbr' => 'like', 'orderNbr' => 'like'),
            $page);       
        $where = $this->secondFilterWhere($where);

        //   判断是否为惠圈人员
        if($_SESSION['USER']['bank_id']!=-1){
            $where['User.bank_id'] = $_SESSION['USER']['bank_id'];
        }

        $listResult = $this
            ->field(array('nickName', 'BankAccount.mobileNbr', 'accountNbrPre6', 'accountNbrLast4', 'BankAccount.status', 'BankAccount.createTime', 'errMsg', 'orderNbr', 'User.mobileNbr' => 'userMobilNbr', 'RIGHT(idNbr, 4)' => 'idNbr', 'BankAccount.bankcard'))
            ->join('User ON User.userCode = BankAccount.userCode')
            ->where($where)
            ->order('createTime desc')
            ->pager($page)
            ->select();
        return $listResult;
    }
    
    /**
     * 用户银行卡总计
     * @param array $filterData 查询条件
     * @return array
     */
    public function bankAccountCount($filterData) {
        $where = $this->filterWhere(
            $filterData);
        $where = $this->secondFilterWhere($where);

        //   判断是否为惠圈人员
        if($_SESSION['USER']['bank_id']!=-1){
            $where['User.bank_id'] = $_SESSION['USER']['bank_id'];
        }

        $listResult = $this->join('User ON User.userCode = BankAccount.userCode')->where($where)->count();
        return $listResult;
    }

    /**
     * 第二次过滤条件
     * @param array $where 条件
     * @return array $where
     */
    public function secondFilterWhere(&$where) {
        if($where['status'] || $where['status'] == '0') {
            $where['BankAccount.status'] = array('IN', $where['status']);
            unset($where['status']);
        }
        if(!empty($where['startTime']) && !empty($where['endTime'])){
            $where['endTime'] = $where['endTime']." 23:59:59";
            $where['createTime'] = array('between', array($where['startTime'], $where['endTime']));
        }elseif (!empty($where['startTime']) && empty($where['endTime'])){
            $where['createTime'] = array('egt', $where['startTime']);
        }elseif (empty($where['startTime']) && !empty($where['endTime'])){
            $where['endTime'] = $where['endTime']." 23:59:59";
            $where['createTime'] = array('elt', $where['endTime']);
        }
        unset($where['startTime']);
        unset($where['endTime']);
        if($where['mobileNbr']) {
            $where['BankAccount.mobileNbr'] = $where['mobileNbr'];
            unset($where['mobileNbr']);
        }
        if($where['userMobileNbr']) {
            $where['User.mobileNbr'] = $where['userMobileNbr'];
            unset($where['userMobileNbr']);
        }
        return $where;
    }

    /**
     * 获得统计数量
     * @param array $condition 条件
     * @param array $joinTable 联合的表
     * @param string $field 要统计的字段
     * @return int $count
     */
    public function getBankAccountCount($condition, $joinTable, $field) {
        if($joinTable) {
            foreach($joinTable as $v) {
                $this->join($v['joinTable'] . ' ON ' . $v['joinCon'], $v['joinType']);
            }
        }
        $count = $this->where($condition)->count($field);
        return intval($count);
    }

    /**
     * 获得不重复的用户数量
     * @param array $condition
     * @return int
     */
    public function countUserDistinct($condition) {
        return $this->getBankAccountCount($condition, array(), 'DISTINCT(userCode)');
    }

    /**
     * 用户是否第一次绑卡
     * @param string $userCode 用户编码
     * @return boolean 是返回true，否返回false
     */
    public function isFirst($userCode) {
        $count = $this
            ->where(array('userCode' => $userCode, 'status' => array('IN', array(\Consts::BANKACCOUNT_STATUS_SIGNED, \Consts::BANKACCOUNT_STATUS_TERMINATE))))
            ->count('bankAccountCode');
        return $count > 0 ? false : true;
    }

    /**
     * 统计已经绑过该银行卡的用户数
     * @param array $condition
     * @return int
     */
    public function countSignedCard($condition) {
        return $this->where($condition)->count('bankAccountCode');
    }

    /**
     * 减少用户银行账号的消费次数
     * @param string $bankAccountCode 银行账号编码
     * @param int $number 数字
     * @return boolean 成功返回true，失败返回false
     */
    public function decConsumeCount($bankAccountCode, $number) {
        return $this->where(array('bankAccountCode' => $bankAccountCode))->setDec('consumeCount', $number) !== false ? true : false;
    }

    /**
     * 增加用户银行账号的消费次数
     * @param string $bankAccountCode 银行账号编码
     * @param int $number 数字
     * @return boolean 成功返回true，失败返回false
     */
    public function incConsumeCount($bankAccountCode, $number) {
        return $this->where(array('bankAccountCode' => $bankAccountCode))->setInc('consumeCount', $number) !== false ? true : false;
    }

    /**
     * 更新用户关联银行卡信息
     * @param array $data 更新数据
     * @return boolean 成功返回true，失败返回false
     */
    public function updateBankAccount($data) {
        $data['lastOperationTime'] = date('Y-m-d H:i:s', time());
        return $this->where(array('bankAccountCode' => $data['bankAccountCode']))->save($data) !== false ? true : false;
    }

    /**
     * 获取用户银行卡，状态为已签约
     * @param string $userCode 用户编码
     * @param object $page 分页
     * @return array||boolean 查询成功返回关联数组；查询结果为空返回NULL；查询出错返回false
     */
    public function getBankAccountList($userCode, $page) {
        return $this
            ->field(array('bankAccountCode', 'accountNbrPre6', 'accountNbrLast4', 'bankName', 'mobileNbr'))
            ->where(array('userCode' => $userCode, 'status' => \Consts::BANKACCOUNT_STATUS_SIGNED))
            ->pager($page)
            ->select();
    }

    /**
     * 统计用户银行卡的总记录数
     * @param string $userCode 用户编码
     * @return array||boolean 查询成功返回关联数组；查询结果为空返回NULL；查询出错返回false
     */
    public function countBankAccount($userCode){
        return $this->where(array('userCode' => $userCode))->count('bankAccountCode');
    }

    /**
     * 设置支付协议签订交易的订单编号。
     */
    public function setOrderNbr() {
        $count = $this->where(array('createTime' => array('BETWEEN', array(date('Y-m-d 00:00:00', time()), date('Y-m-d 23:59:59', time())))))->count('bankAccountCode');
        $serialId = sprintf('%05d', $count + 1);
        $orderNbr = date('ymdHis', time()) . $serialId;
        return $orderNbr;
    }

    /**
     * 添加银行卡支付协议签订
     * @param string $userCode 用户编码
     * @param string $accountName 账号姓名
     * @param string $idType 证件类型
     * @param string $idNbr 证件号
     * @param string $accountNbrPre6 银行卡卡号前6位
     * @param string $accountNbrLast4 银行卡卡号后4位
     * @param string $mobileNbr 预留手机号
     * @param string $orderNbr 订单号
     * @return array {'code','orderNbr'}
     */
    public function addBankAccount($userCode, $accountName, $idType, $idNbr, $accountNbrPre6, $accountNbrLast4, $mobileNbr, $orderNbr = '') {
        $rules = array(
            array('accountName', 'require', C('BANK_ACCOUNT.ACCOUNT_NAME_WRONG'), 1),
            array('idType', 'require', C('BANK_ACCOUNT.ID_TYPE_WRONG'), 1),
            array('idType', 'is_numeric', C('BANK_ACCOUNT.ID_TYPE_WRONG'), 1, 'function'),
            array('idNbr', 'require', C('BANK_ACCOUNT.ID_NBR_WRONG'), 1),
            array('accountNbrPre6', 'require', C('BANK_ACCOUNT.ACCOUNT_NBR_WRONG'), 1),
            array('accountNbrPre6', '6', C('BANK_ACCOUNT.ACCOUNT_NBR_WRONG'), 1, 'length'),
            array('accountNbrLast4', 'require', C('BANK_ACCOUNT.ACCOUNT_NBR_WRONG'), 1),
            array('accountNbrLast4', '4', C('BANK_ACCOUNT.ACCOUNT_NBR_WRONG'), 1, 'length'),
            array('mobileNbr', 'require', C('MOBILE_NBR.EMPTY'), 1),
            array('mobileNbr', 'is_numeric', C('MOBILE_NBR.RESERVED_ERROR'), 1, 'function'),
        );
        $userMdl = new UserModel();
        $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('realName'));
        $bankAccountCode = $this->create_uuid();
        $bankAccount = array(
            'bankAccountCode' => $bankAccountCode,
            'userCode' => $userCode,
            'accountNbrPre6' => $accountNbrPre6,
            'accountNbrLast4' => $accountNbrLast4,
            'accountName' => $accountName,
            'bankName' => '中国工商银行',
            'status' => \Consts::BANKACCOUNT_STATUS_NO_SIGN, // 设置状态为未签约
            'createTime' => date('Y-m-d H:i:s', time()),
            'lastOperationTime' => date('Y-m-d H:i:s', time()),
            'realName' => $userInfo['realName'],
            'idType' => $idType,
            'idNbr' => $idNbr,
            'mobileNbr' => $mobileNbr,
            'orderNbr' => empty($orderNbr) ? $this->setOrderNbr() : $orderNbr,
        );
        if($this->validate($rules)->create($bankAccount) != false) {
            // 判断用户是否绑定了相同卡号的银行卡
            $userBankAccountInfo = $this->getBankAccountInfo(array(
                'userCode' => $userCode,
                'accountNbrPre6' => $accountNbrPre6,
                'accountNbrLast4' => $accountNbrLast4,
                'status' => \Consts::BANKACCOUNT_STATUS_SIGNED
            ));
            if($userBankAccountInfo) {
                return array('code' => C('BANK_ACCOUNT.REPEAT'));
            }

            return $this->add($bankAccount) !== false ? array('code' => C('SUCCESS'), 'orderNbr' => $bankAccount['orderNbr'], 'bankAccountCode' => $bankAccountCode) : array('code' => C('API_INTERNAL_EXCEPTION'));
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 添加银行卡支付协议签订(改)
     * @param string $userCode 用户编码
     * @param string $accountName 账号姓名
     * @param string $idType 证件类型
     * @param string $idNbr 证件号
     * @param string $bankCard 银行卡卡号
     * @param string $mobileNbr 预留手机号
     * @param string $orderNbr 订单号
     * @return array {'code','orderNbr'}
     */
    public function addBankAccountModify($userCode, $accountName, $idType, $idNbr, $bankCard, $mobileNbr, $orderNbr = '') {
        $rules = array(
            array('accountName', 'require', C('BANK_ACCOUNT.ACCOUNT_NAME_WRONG'), 1),
            array('idType', 'require', C('BANK_ACCOUNT.ID_TYPE_WRONG'), 1),
            array('idType', 'is_numeric', C('BANK_ACCOUNT.ID_TYPE_WRONG'), 1, 'function'),
            array('idNbr', 'require', C('BANK_ACCOUNT.ID_NBR_WRONG'), 1),
            array('bankCard', 'require', C('BANK_ACCOUNT.ACCOUNT_NBR_WRONG'), 1),
            array('mobileNbr', 'require', C('MOBILE_NBR.EMPTY'), 1),
            array('mobileNbr', 'is_numeric', C('MOBILE_NBR.RESERVED_ERROR'), 1, 'function'),
        );
        $userMdl = new UserModel();
        $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('realName'));
        $bankAccountCode = $this->create_uuid();
        $accountNbrPre6 = substr($bankCard , 0 , 6);
        $accountNbrLast4 = substr($bankCard, -4);
        $bankAccount = array(
            'bankAccountCode' => $bankAccountCode,
            'userCode' => $userCode,
            'accountNbrPre6' => $accountNbrPre6,
            'accountNbrLast4' => $accountNbrLast4,
            'bankCard' => $bankCard,
            'accountName' => $accountName,
            'bankName' => '中国工商银行',
            'status' => \Consts::BANKACCOUNT_STATUS_NO_SIGN, // 设置状态为未签约
            'createTime' => date('Y-m-d H:i:s'),
            'lastOperationTime' => date('Y-m-d H:i:s'),
            'realName' => $userInfo['realName'],
            'idType' => $idType,
            'idNbr' => $idNbr,
            'mobileNbr' => $mobileNbr,
            'orderNbr' => empty($orderNbr) ? $this->setOrderNbr() : $orderNbr,
        );
        if($this->validate($rules)->create($bankAccount) != false) {
            // 判断用户是否签约了相同卡号的账户
            $userBankAccountInfo = $this->getBankAccountInfo(array(
                'userCode' => $userCode,
                'accountNbrPre6' => $accountNbrPre6,
                'accountNbrLast4' => $accountNbrLast4,
                'bankCard' => $bankCard,
                'status' => \Consts::BANKACCOUNT_STATUS_SIGNED,
            ));
            if($userBankAccountInfo) {
                return array('code' => C('BANK_ACCOUNT.REPEAT'));
            }
            // 检查是否有相同订单号的账号，以免出现重复订单号的记录
            if(!empty($orderNbr)) {
                $bankAccountInfo = $this->where(array('orderNbr' => $orderNbr, 'bankAccountCode'))->find();
                if($bankAccountInfo) {
                    unset($bankAccount['bankAccountCode']);
                    $ret = $this->where(array('orderNbr' => $orderNbr))->save($bankAccount) !== false ? true : false;
                    $bankAccountCode = $bankAccountInfo['bankAccountCode'];
                } else {
                    $ret = $this->add($bankAccount) !== false ? true : false;
                }
            } else {
                $ret = $this->add($bankAccount) !== false ? true : false;
            }

            return $ret !== false ? array('code' => C('SUCCESS'), 'orderNbr' => $bankAccount['orderNbr'], 'bankAccountCode' => $bankAccountCode) : array('code' => C('API_INTERNAL_EXCEPTION'));
        } else {
            return $this->getValidErrorCode();
        }
    }
    
    /**
     * 取消添加银行卡
     * @param array $where 条件
     * @return array
     */
    public function cancelBankAccount($where) {
        // 设置状态为取消签约，更新最后操作时间
        $code = $this
            ->where($where)
            ->save(array('status' => \Consts::BANKACCOUNT_STATUS_CANCELLED, 'lastOperationTime' => date('Y-m-d H:i:s', time()))) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

    /**
     * 删除用户关联银行卡
     * @param array $where 条件
     * @return boolean 成功返回true,失败返回false
     */
    public function delBankAccount($where) {
        return $this->where($where)->delete() !== false ? true : false;
    }

    /**
     * 银行卡详情
     * @param array $where 条件
     * @param array $field 字段
     * @param string $sort 排序字段
     * @return array $bankAccountInfo
     */
    public function getBankAccountInfo($where, $field = array(), $sort = 'createTime desc') {
        if(empty($field)){
            $field = array(
                'bankAccountCode',
                'accountNbrPre6',
                'accountNbrLast4',
                'accountName',
                'idType',
                'idNbr',
                'mobileNbr',
                'userCode',
                'bankName',
                'lastOperationTime',
                'expireTime',
                'consumeCount',
                'status',
                'bankCard'
            );
        }
        $bankAccountInfo = $this
            ->field($field)
            ->where($where)
            ->order($sort)
            ->find();
        if($bankAccountInfo) { // 如果查到了记录
            if(isset($bankAccountInfo['bankCard']) && empty($bankAccountInfo['bankCard'])) { // 如果完整银行卡号不存在
                // 拼接银行卡号前6位和后4位作为银行卡号
                $bankAccountInfo['bankCard'] = $bankAccountInfo['accountNbrPre6'] . $bankAccountInfo['accountNbrLast4'];
            }
        }
        return $bankAccountInfo;
    }

    /**
     * 根据搜索条件返回某字段的一维数组
     * @param $field 字段名
     * @param $condition 搜索条件
     * @return mixed
     */
    public function getFieldArr($field, $condition){
        return $this->where($condition)->group($field)->getField($field, true);
    }

    public function getFirstBankCard($userCode, $isSuccess = 0){
        if($isSuccess == 1){
            $field = array('idType', 'idNbr');
            $condition = array('userCode' => $userCode, 'status' => array('IN', array(\Consts::BANKACCOUNT_STATUS_SIGNED)));
        }else{
            $field = array('bankCard', 'createTime');
            $condition = array('userCode' => $userCode, 'status' => array('IN', array(\Consts::BANKACCOUNT_STATUS_SIGNED, \Consts::BANKACCOUNT_STATUS_TERMINATE)));
        }
        return $this->field($field)->where($condition)->order('createTime asc')->find();
    }
}