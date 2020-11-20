<?php
/**
 * Created by PhpStorm.
 * User: Lulu
 * Date: 16-02-22
 * Time: 下午3:10
 */
namespace Common\Model;
class RefundLogModel extends BaseModel {
    protected $tableName = 'RefundLog';

    /**
     * 统计退款总金额
     * @param array $condition 条件
     * @param array $joinTableArr 联合表
     * @return int $refundAmount 单位：分
     */
    public function sumRefundAmount($condition, $joinTableArr) {
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        $refundAmount = $this->where($condition)->sum('RefundLog.refundPrice');
        return $refundAmount ? $refundAmount : 0;
    }

    /**
     * 统计退款订单的总金额
     * @param array $condition 条件
     * @return int $orderAmount 单位：分
     */
    public function sumOrderAmount($condition) {
        $orderList = $this
            ->field(array('DISTINCT(RefundLog.orderNbr)' => 'orderNbr'))
            ->join('ConsumeOrder ON ConsumeOrder.orderNbr = RefundLog.orderNbr')
            ->where($condition)
            ->select();
        $orderNbrList = array();
        foreach($orderList as $v) {
            $orderNbrList[] = $v['orderNbr'];
        }
        $orderAmount = 0;
        if($orderNbrList) {
            $consumeOrderMdl = new ConsumeOrderModel();
            $orderAmount = $consumeOrderMdl->sumOrderFiled(array('orderNbr' => array('IN', $orderNbrList)), 'orderAmount');
        }
        return $orderAmount;
    }

    /**
     * 编辑记录
     * @param array $data 数据
     * @return array
     */
    public function editRefundLog($data) {
        if($data['refundCode']) {
            $code = $this->where(array('refundCode' => $data['refundCode']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        } else {
            if(!isset($data['createTime'])) $data['createTime'] = date('Y-m-d H:i:s', time());
            $data['refundCode'] = $this->create_uuid();
            $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        }
        return $this->getBusinessCode($code);
    }

    /**
     * 获得退款流水详情
     * @param array $condition 条件。
     * @param array $field 查询的字段。
     * @param array $joinTableArr 关联的表。例：array('joinTable' => '连接表名', 'joinCondition' => '连接条件', 'joinType' => '连接方式')
     * @return array
     */
    public function getRefundLogInfo($condition = array(), $field = array(), $joinTableArr = array()){
        if(empty($field)){
            $field = array('RefundLog.*');
        }
        $this->field($field);
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        return $this->find();
    }

    /**
     * 获得退款流水列表
     * @param array $condition 条件。
     * @param array $field 查询的字段。
     * @param array $joinTableArr 关联的表。例：array('joinTable' => '连接表名', 'joinCondition' => '连接条件', 'joinType' => '连接方式')
     * @param string $order 排序规则
     * @param int $limit 查询条数限制
     * @param int $page 查询页码
     * @return array
     */
    public function getRefundLogList($condition = array(), $field = array(), $joinTableArr = array(), $order = '', $limit = 0, $page = 0) {
        if(empty($field)){
            $field = array('RefundLog.*');
        }
        $this->field($field);
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        if($order){
            $this->order($order);
        }
        if($limit){
            $this->limit($limit);
        }
        if($page){
            $this->page($page);
        }
        return $this->select();
    }

    /**
     * 获得退款流水总记录数
     * @param array $condition 条件。
     * @param array $joinTableArr 关联的表。例：array('joinTable' => '连接表名', 'joinCondition' => '连接条件', 'joinType' => '连接方式')
     * @return int $count
     */
    public function countRefundLog($condition = array(), $joinTableArr = array()) {
        if(empty($field)){
            $field = array('RefundLog.*');
        }
        $this->field($field);
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        $count = $this->count('RefundLog.refundCode');
        return $count ? $count : 0;
    }
}