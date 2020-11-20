<?php
namespace Common\Model;
/**
 * Created by PhpStorm.
 * User: jihuafei
 * Date: 15-10-10
 * Time: 下午2:24
 */
class PointEarningLogModel extends BaseModel {
    protected $tableName = 'PointEarningLog';

    /**
     * 增加惠圈获取记录
     * @param array $data 关联数组，例：{'userCode' => 'abcdefg', 'point' => '23', 'reason' => '注册'}
     * @return boolean 添加成功返回true，添加失败返回false
     */
    public function addPointEarningLog($data) {
        $data['earningTime'] = date('Y-m-d H:i:s');
        return $this->add($data) !== false ? true : false;
    }

    /**
     * 获得用户赚取圈值的记录
     * @param array $filterData
     * @param number $page 偏移值
     * @return null|array 如果为空，返回null；如果不为空，返回二维数组array(array());
     */
    public function listPEL($filterData, $page) {
        $where = $this->filterWhere(
            $filterData,
            array('nickName' => 'like', 'mobileNbr' => 'like'),
            $page);
        return $this
            ->field(array('nickName', 'mobileNbr', 'point', 'reason', 'earningTime'))
            ->where($where)
            ->join('User ON User.userCode = PointEarningLog.userCode')
            ->order('earningTime desc')
            ->pager($page)
            ->select();
    }

    /**
     * 获得用户赚取圈值的记录的总数
     * @param array $filterData
     * @return null|array 如果为空，返回null；如果不为空，返回二维数组array(array());
     */
    public function countPEL($filterData) {
        $where = $this->filterWhere(
            $filterData,
            array('nickName' => 'like', 'mobileNbr' => 'like'),
            $page);
        return $this
            ->where($where)
            ->join('User ON User.userCode = PointEarningLog.userCode')
            ->count('User.userCode');
    }
}