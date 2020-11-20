<?php
/**
 * Created by PhpStorm.
 * User: Jihuafei
 * Date: 15-12-11
 * Time: 下午3:58
 */
namespace Common\Model;
class DownloadLogModel extends BaseModel {
    protected $tableName = 'DownloadLog';

    /**
     * 第二次过滤条件
     * @param array $where
     * @return array
     */
    public function secondFilterWhere(&$where) {
        if($where['shopCode']) {
            $where['DownloadLog.shopCode'] = $where['shopCode'];
            unset($where['shopCode']);
        }
        if($where['cityId']) {
            $cityInfo = explode('|', $where['cityId']);
            $where['cityId'] = $cityInfo[0];
        }
        return $where;
    }

    /**
     * PC端APP更新列表
     * @param array $filterData
     * @param object $page
     * @return null|array 如果为空，返回null；如果不为空，返回二维数组;
     */
    public function listLog($filterData, $page) {
        $where = $this->filterWhere($filterData, array(
            'activityNbr' => 'like'
        ), $page);
        $this->secondFilterWhere($where);
        return $this
            ->field(array('DownloadLog.id', 'District.name', 'Shop.shopName', 'activityNbr', 'downloadTimes', 'DownloadLog.operation'))
            ->join('Shop ON Shop.shopCode = DownloadLog.shopCode', 'left')
            ->join('District ON District.id = DownloadLog.cityId')
            ->where($where)
            ->order('downloadTimes desc, id asc')
            ->pager($page)
            ->select();
    }

    /**
     * 统计更新记录数量
     * @param array $filterData
     * @return null|int 如果为空，返回null；如果不为空，返回number;
     */
    public function countlog($filterData) {
        $where = $this->filterWhere($filterData, array(
            'activityNbr' => 'like'
        ));
        $this->secondFilterWhere($where);
        return $this
            ->join('Shop ON Shop.shopCode = DownloadLog.shopCode', 'left')
            ->join('District ON District.id = DownloadLog.cityId')
            ->where($where)
            ->count('DownloadLog.id');
    }

    /**
     * 增加字段的数量
     * @param array $condition 条件
     * @param string $field [字符串] 字段
     * @param int $nbr 数量
     * @return boolean 成功返回true，失败返回false
     */
    public function incField($condition, $field, $nbr) {
        return $this->where($condition)->setInc($field, $nbr) !== false ? true : false;
    }

    /**
     * 获得记录信息
     * @param array $condition 条件
     * @param array $field 字段数组
     * @return array $logInfo
     */
    public function getLogInfo($condition, $field){
        $logInfo = $this->field($field)->where($condition)->find();
        return $logInfo;
    }

    /**
     * 保存记录
     * @param array $data
     * @return boolean 成功返回true，失败返回false
     */
    public function editLog($data) {
        if($data['id']) { // 存在主键，则更新
            $ret = $this->where(array('id' => $data['id']))->save($data) !== false ? true : false;
        } else { // 新添记录
            $ret = $this->add($data) !== false ? true : false;
        }
        return $ret;
    }
}