<?php
/**
 * Created by PhpStorm.
 * User: Jihuafei
 * Date: 16-1-4
 * Time: 上午11:23
 */
namespace Common\Model;
class PSNALogModel extends BaseModel {
    protected $tableName = 'PSNALog';

    /**
     * 获得不采纳的理由列表
     * @param array $condition 条件
     * @param array $filed 字段
     * @return array 二维数组
     */
    public function listLog($condition, $filed) {
        return $this->field($filed)->where($condition)->select();
    }

    /**
     * 获得统计数量
     * @param array $condition 条件
     * @return int
     */
    public function countLog($condition) {
        return $this->where($condition)->count('logId');
    }

    /**
     * 保存数据
     * @param array $condition 条件
     * @param array $data 保存的数据
     * @return array
     */
    public function editLog($condition, $data) {
        $rules = array(
            array('reason', 'require', C('PSNA_LOG.REASON_EMPTY')),
        );
        if($this->validate($rules)->create($data)) {
            if($condition) {
                $code = $this->where($condition)->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            } else {
                $data['iptTime'] = time();
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            return $this->getBusinessCode($code);
        } else {
            return $this->getValidErrorCode();
        }
    }

}