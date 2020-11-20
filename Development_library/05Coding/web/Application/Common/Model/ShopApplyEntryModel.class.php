<?php
/**
 * Created by PhpStorm.
 * User: Jihuafei
 * Date: 15-11-19
 * Time: 上午11:45
 */

namespace Common\Model;

class ShopApplyEntryModel extends BaseModel {
    protected $tableName = 'ShopApplyEntry';

    /**
     * 获得申请的记录
     * @param array $filterData
     * @param object $page 偏移值
     * @return array
     */
    public function listApply($filterData, $page) {
        $where = $this->filterWhere($filterData);
        return $this
            ->field(array('*'))
            ->where($where)
            ->pager($page)
            ->order('applyTime desc')
            ->select();
    }

    /**
     * 获得申请数量
     * @param array $filterData 条件
     * @return int
     */
    public function countApply($filterData) {
        $where = $this->filterWhere($filterData);
        return $this->where($where)->count('id');
    }

    /**
     * 保存入驻申请
     * @param array $condition 条件
     * @param array $data 数据信息
     * @return array
     */
    public function editApplyEntry($condition, $data) {
        if(empty($data['id'])) {
            $data['applyTime'] = date('Y-m-d H:i:s');
            $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        } else {
            $code = $this->where($condition)->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        }
        return $this->getBusinessCode($code);
    }
} 