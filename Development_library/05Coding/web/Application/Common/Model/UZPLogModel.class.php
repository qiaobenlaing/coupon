<?php
/**
 * Created by PhpStorm.
 * User: Jihuafei
 * Date: 15-11-26
 * Time: 下午6:40
 */
namespace Common\Model;
class UZPLogModel extends BaseModel {
    protected $tableName = 'UZPLog';

    /**
     * 删除记录
     * @param array $condition 条件
     * @return array
     */
    public function delLog($condition) {
        $code = $this->where($condition)->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

    /**
     * 编辑记录
     * @param array $data 数据
     * @return array
     */
    public function editLog($data) {
        if($data['id']) {
            $code = $this->where(array('id' => $data['id']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        } else {
            $data['time'] = time();
            $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        }
        return $this->getBusinessCode($code);
    }

    /**
     * 获得关注信息
     * @param array $condition 条件
     * @return array
     */
    public function getLogInfo($condition) {
        return $this->where($condition)->find();
    }
}