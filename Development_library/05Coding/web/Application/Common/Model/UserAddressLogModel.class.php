<?php
/**
 * Created by PhpStorm.
 * User: Jihuafei
 * Date: 15-11-26
 * Time: 下午6:40
 */
namespace Common\Model;
class UserAddressLogModel extends BaseModel {
    protected $tableName = 'UserAddressLog';
    /**
     * 编辑记录
     * @param array $data 数据
     * @return array
     */
    public function editUserAddressLog($data) {
        if($data['id']) {
            $code = $this->where(array('id' => $data['id']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        } else {
            $data['createTime'] = date('Y-m-d H:i:s', time());
            $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        }
        return $this->getBusinessCode($code);
    }

}