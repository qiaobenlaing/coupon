<?php
/**
 * Created by PhpStorm.
 * User: 98041
 * Date: 2018/7/24
 * Time: 16:35
 */

namespace Common\Model;


class WeixinLogModel extends BaseModel
{
    protected $trueTableName  = 'WeixinLog';

    /*获取微信用户操作日志列表
      * @param array $filterData
     * @param number $page 偏移值
     * @param array $field
     * @return null|array 如果为空，返回null；如果不为空，返回二维数组array(array());
     * */
    public function getWeixinLogList($filterData,$page,$field){
            if(empty($field)){
                $field = array(
                    "id",
                    "readtime",
                    "addip",
                    "openid",
                    "cardType",
                    "cardNumber",
                    "event",
                    "userCode",
                    "userCouponCardCode"
                );
            }
        $where = $this->filterWhere(
            $filterData,
            $page);

        if ($where['readtimeStart'] && $where['readtimeEnd']) {
            $where['readtime'] = array('BETWEEN', array(strtotime($where['readtimeStart'].' 00:00:00'), strtotime($where['readtimeEnd'].' 23:59:59')));
        } elseif ($where['readtimeStart'] && !$where['readtimeEnd']) {
            $where['readtime'] = array('EGT', $where['readtimeStart'].' 00:00:00');
        } elseif (!$where['readtimeStart'] && $where['readtimeEnd']) {
            $where['readtime'] = array('ELT', $where['readtimeEnd'].' 23:59:59');
        }
        unset($where['readtimeStart']);
        unset($where['readtimeEnd']);

        return $this
            ->field($field)
            ->where($where)
            ->order('readtime desc')
            ->pager($page)
            ->select();
    }

    /**
     * 管理微信用户操作日志总数
     * @param array $filterData
     * @return int $count
     */
    public function countWeixinLog($filterData) {
        $where = $this->filterWhere(
            $filterData
        );

        if ($where['readtimeStart'] && $where['readtimeEnd']) {
            $where['readtime'] = array('BETWEEN', array(strtotime($where['readtimeStart'].' 00:00:00'), strtotime($where['readtimeEnd'].' 23:59:59')));
        } elseif ($where['readtimeStart'] && !$where['readtimeEnd']) {
            $where['readtime'] = array('EGT', $where['readtimeStart'].' 00:00:00');
        } elseif (!$where['readtimeStart'] && $where['readtimeEnd']) {
            $where['readtime'] = array('ELT', $where['readtimeEnd'].' 23:59:59');
        }
        unset($where['readtimeStart']);
        unset($where['readtimeEnd']);

        $count = $this
            ->where($where)
            ->count('id');
        return $count;
    }

}