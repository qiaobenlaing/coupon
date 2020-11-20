<?php
namespace Common\Model;
use Think\Model;
use Common\Model\BankAccountModel;
/**
 * bankAccountLocalLog表
 * @author 
 */
class BankAccountLocalLogModel extends BaseModel {
    protected $tableName = 'BankAccountLocalLog';

    /**
     * 获得银行账号信息
     * @param array $condition 条件
     * @param array $field 要查询的字段
     * @return array 一维数组
     */
    public function getBankAccountInfo($condition, $field) {
        return $this
            ->field($field)
            ->join('BankAccount ON BankAccount.bankAccountCode = BankAccountLocalLog.accountCode')
            ->where($condition)
            ->find();
    }

    /**
     * 回滚银行卡的支付行为。（修改银行卡支付记录的状态为已退款；回滚银行卡消费次数）
     * @param string $consumeCode 支付记录编码
     * @param string $bankAccountCode 用户银行账号编码
     * @return boolean 操作成功返回true，操作失败返回false
     */
    public function rollbackBankAccountPayAction($consumeCode, $bankAccountCode) {
        // 修改银行卡支付记录的状态为已退款
        $ret = $this->where(array('consumeCode' => $consumeCode))->save(array('status' => C('BALL_STATUS.REFUNDED'))) !== false ? true : false;
        if($ret === true) {
            // 回滚银行卡消费次数
            $bankAccountMdl = new BankAccountModel();
            $ret = $bankAccountMdl->decConsumeCount($bankAccountCode, 1);
        }
        return $ret;
    }

    /**
     * 更新银行卡本地支付记录的状态
     * @param array $where 条件
     * @param array $data 数据
     * @return boolean 成功返回true，失败返回false
     */
    public function updateBALLStatus($where, $data) {
        return $this->where($where)->save($data) !== false ? true : false;
    }

    /**
     * 更新状态为已支付
     * @param array $where 条件
     * @return boolean 成功发挥true，失败返回false
     */
    public function updateStatus($where) {
        return $this->where($where)->save(array('status' => C('BALL_STATUS.PAYED'))) !== false ? true : false;
    }

    /**
     * 添加银行卡消费记录
     * @param array $bankAccountLocalLogInfo
     * @return boolean 成功返回true，失败返回false
     */
    public function addBankAccountLocalLog($bankAccountLocalLogInfo) {
        $bankAccountLocalLogInfo['logCode'] = $this->create_uuid();
        return $this->add($bankAccountLocalLogInfo) !== false ? true : false;
    }
    
    /**
    * 删除数据
    * @param number $logCode 主键
    * @return boolean||string 删除成功返回true；删除失败返回错误信息
    */
    public function delBankAccountLocalLog($logCode) {
        return $this->where(array('logCode' => $logCode))->delete() !== false ? true : '数据库删除数据失败';
    }
    
    /**
    * 按日获得银行卡刷卡对账的统计列表
    * @param String $shopCode 商家编码
    * @param number $time 1表示最近一周，2表示最近一个月，3表示最近一年
    * @param object $page 分页
    * @return array 查询成功返回关联数组；否则返回空数组
    */
    public function listBankCardCountBill($shopCode, $time, $page) {
        //TODO
        $condition['location'] = $shopCode;
        if($time){
            if($time == 1) {$time = strtotime('-1 week');}
            if($time == 2) {$time = strtotime('-1 month');}
            if($time == 3) {$time = strtotime('-1 year');}
            $condition['unix_timestamp(actionTime)'] = array('GT',$time);
        }
        $countBill = $this
            ->field(array(
                'from_unixtime(unix_timestamp(actionTime),"%Y-%m-%d")' => 'date',
                'count(logCode)' => 'consumeCount',
                'sum(consumeAmount) / '.C('RATIO') => 'consumeAmount'))
            ->where($condition)
            ->group('date')
            ->pager($page)
            ->select();
        if($countBill) {
            foreach($countBill as &$v) {
                $v['consumeAmount'] = number_format($v['consumeAmount'], 2, '.', '');
            }
        }
        return $countBill ? $countBill : array();
    }

    /**
     * 统计按日获得银行卡刷卡对账的统计信息的记录数
     * @param String $shopCode 商家编码
     * @param number $time 1表示最近一周，2表示最近一个月，3表示最近一年
     * @return int
     */
    public function countBankCardCountBill($shopCode, $time) {
        //TODO
        $condition['location'] = $shopCode;
        if($time){
            if($time == 1) {$time = strtotime('-1 week');}
            if($time == 2) {$time = strtotime('-1 month');}
            if($time == 3) {$time = strtotime('-1 year');}
            $condition['unix_timestamp(actionTime)'] = array('GT',$time);
        }
        $countBillList = $this
            ->field(array('from_unixtime(unix_timestamp(actionTime),"%Y-%m-%d")' => 'date'))
            ->where($condition)
            ->group('date')
            ->select();
        return count($countBillList);
    }

    /**
     * 获得某日银行卡刷卡对账的统计信息
     * @param string $shopCode 商家编码
     * @param string $actionTime 日期
     * @return array $bankCardCountBillInfo
     */
    public function getBankCardCountBill($shopCode, $actionTime) {
        $condition['location'] = $shopCode;
        $condition['from_unixtime(unix_timestamp(actionTime),"%Y-%m-%d")'] = $actionTime;
        $bankCardCountBillInfo = $this
            ->field(array(
                'from_unixtime(unix_timestamp(actionTime),"%Y-%m-%d")' => 'date',
                'count(logCode)' => 'consumeCount',
                'sum(consumeAmount) / '.C('RATIO') => 'consumeAmount'))
            ->where($condition)
            ->group('date')
            ->select();
        if($bankCardCountBillInfo) {
            foreach($bankCardCountBillInfo as &$v) {
                $v['consumeAmount'] = number_format($v['consumeAmount'], 2, '.', '');
            }
        }
        return $bankCardCountBillInfo;
    }

    /**
     * 获得某日的银行卡刷卡对账列表
     * @param String $shopCode 商家编码
     * @param String $datetime 日期
     * @param object $page 分页
     * @return array 查询成功返回关联数组
     */
    public function listBankCardBill($shopCode, $datetime, $page){
        $condition['location'] = $shopCode;
        $condition['from_unixtime(unix_timestamp(actionTime),"%Y-%m-%d")'] = $datetime;
        $result = $this
            ->where($condition)
            ->order('unix_timestamp(actionTime) desc')
            ->pager($page)
            ->select();
        if($result){
            $bankAccountMdl= new BankAccountModel();
            $cardBill = array();
            foreach($result as $k => $v){
                $cardBill['time'] = $v['actionTime'];
                $cardBill['accountNbr'] = '';
                $cardBill['bankName'] = '';
                $cardBill['Type'] = '现金';
                if($v['accountCode']){
                    $bankAccount = $bankAccountMdl->getBankAccountInfo(array('bankAccountCode' => $v['accountCode']));
                    $cardBill['accountNbr'] = isset($bankAccount['accountNbr']) && $bankAccount['accountNbr']?$bankAccount['accountNbr']:'';
                    $cardBill['bankName'] = isset($bankAccount['bankName']) && $bankAccount['bankName']?$bankAccount['bankName']:'';
                    $cardBill['Type'] = '刷卡';
                }
                $cardBill['price'] = $v['consumeAmount'] / C('RATIO');//交易金额
                $cardBill['channel'] = '线下';
                $cardBill['serialNbr'] = $v['logCode'];
                $cardBill['coderievalNbr'] = $v['logCode'];
                $cardBill['rebate'] = 0;//交易回佣
                $cardBill['situation'] = 0;
                $cardBill['installmentFee'] = 0;//分期手续费
                $cardBill['installmentNbr'] = 0;
                $cardBill['rate'] = 0;
                $cardBill['fee'] = 0;//持卡人手续费
                unset($result[$k]);
                $result[$k] = $cardBill;
                unset($cardBill);
            }
        }
        return $result;
    }

    /**
     * 统计某日的银行卡刷卡对账的记录数
     * @param String $shopCode 商家编码
     * @param String $datetime 日期
     * @return array 查询成功返回关联数组
     */
    public function countBankCardBill($shopCode, $datetime){
        $condition['location'] = $shopCode;
        $condition['from_unixtime(unix_timestamp(actionTime),"%Y-%m-%d")'] = $datetime;
        $result = $this
            ->where($condition)
            ->count();
        return $result;
    }

    /**
    * 根据主键得到数据详情
    * @param string $bankAccountLocalLogCode 主键
    * @return array||boolean 查询成功返回关联数组；查询结果为空返回NULL；查询出错返回false
    */
    public function getBankAccountLocalLog($bankAccountLocalLogCode) {
        return $this->where(array('bankAccountLocalLogCode' => $bankAccountLocalLogCode))->find();
    }
    
    /**
    * 更新数据
    * @param string $bankAccountLocalLogCode 主键
    * @param array $bankAccountLocalLogInfo 关联数组
    * @return boolean||string
    */
    public function updateBankAccountLocalLog($bankAccountLocalLogCode, $bankAccountLocalLogInfo) {
        $rules = array(
            array('logCode', 'require', '提醒'),
            array('accountCode', 'require', '提醒'),
            array('consumingContent', 'require', '提醒'),
            array('consumingAmount', 'require', '提醒'),
            array('actionTime', 'require', '提醒'),
            array('location', 'require', '提醒'),
            array('consumeCode', 'require', '提醒'),
        );
        if($this->validate($rules)->create() != false) {
            return $this->where(array('bankAccountLocalLogCode' => $bankAccountLocalLogCode))->save($bankAccountLocalLogInfo) !== false ? true : '数据库更新数据失败';
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 获得该笔消费中，银行卡的使用记录
     * @param string $consumeCode 消费编码
     * @return array
     */
    public function bankAccountLogConsume($consumeCode){
        return $this->where(array('consumeCode' => $consumeCode))->find();
    }
}