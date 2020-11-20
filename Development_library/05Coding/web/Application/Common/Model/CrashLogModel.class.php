<?php
namespace Common\Model;
/**
 * Created by PhpStorm.
 * User: Huafei Ji
 * Date: 15-9-18
 * Time: 下午5:17
 */
class CrashLogModel extends BaseModel{
    protected $tableName = 'CrashLog';

    /**
     * 获得顾客端app崩溃日志列表
     * @param array $filterData
     * @param object $page
     * @return array $crashLogList 二维数组，{{'createTime', 'url'},...}
     */
    public function listClientCrashLog($filterData, $page) {
        $where = $this->filterWhere(
            $filterData,
            array(),
            $page);
        $where = $this->secondClientFilterWhere($where);
        $crashLogList = $this
            ->field(array('createTime', 'url'))
            ->where($where)
            ->order('createTime desc')
            ->pager($page)
            ->select();
        return $crashLogList;
    }

    /**
     * 获得顾客端app崩溃日志总计路数
     * @param array $filterData
     * @return int $crashLogCount
     */
    public function CountClientCrashLog($filterData) {
        $where = $this->filterWhere(
            $filterData,
            array(),
            $page);
        $where = $this->secondClientFilterWhere($where);
        $crashLogCount = $this
            ->where($where)
            ->count('logCode');
        return $crashLogCount;
    }

    public function secondClientFilterWhere(&$where) {
        $where['appType'] = '1';//顾客端
        if ($where['startTime'] && $where['endTime']) {
            $where['createTime'] = array('BETWEEN', array($where['startTime'], $where['endTime']));
        } elseif ($where['startTime'] && !$where['endTime']) {
            $where['createTime'] = array('EGT', $where['startTime']);
        } elseif (!$where['startTime'] && $where['endTime']) {
            $where['createTime'] = array('ELT', $where['endTime']);
        }
        unset($where['startTime']);
        unset($where['endTime']);
        return $where;
    }

    /**
     * 获得商家端app崩溃日志列表
     * @param array $filterData
     * @param object $page
     * @return array $crashLogList 二维数组，{{'createTime', 'url'},...}
     */
    public function listShopCrashLog($filterData, $page) {
        $where = $this->filterWhere(
            $filterData,
            array(),
            $page);
        $where = $this->secondShopFilterWhere($where);
        $crashLogList = $this
            ->field(array('createTime', 'url'))
            ->where($where)
            ->order('createTime desc')
            ->pager($page)
            ->select();
        return $crashLogList;
    }

    /**
     * 获得商家端app崩溃日志总计路数
     * @param array $filterData
     * @return int $crashLogCount
     */
    public function CountShopCrashLog($filterData) {
        $where = $this->filterWhere(
            $filterData,
            array(),
            $page);
        $where = $this->secondShopFilterWhere($where);
        $crashLogCount = $this
            ->where($where)
            ->count('logCode');
        return $crashLogCount;
    }

    public function secondShopFilterWhere(&$where) {
        $where['appType'] = '0';//商家端
        if ($where['startTime'] && $where['endTime']) {
            $where['createTime'] = array('BETWEEN', array($where['startTime'], $where['endTime']));
        } elseif ($where['startTime'] && !$where['endTime']) {
            $where['createTime'] = array('EGT', $where['startTime']);
        } elseif (!$where['startTime'] && $where['endTime']) {
            $where['createTime'] = array('ELT', $where['endTime']);
        }
        unset($where['startTime']);
        unset($where['endTime']);
        return $where;
    }

    /**
     * 添加崩溃日志
     * @param array $data {'url', 'userCode', 'equipmentCode'}
     * @return array {'code'}
     */
    public function addCrashLog($data) {
        $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }
}