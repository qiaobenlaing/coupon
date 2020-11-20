<?php
/**
 * Created by PhpStorm.
 * User: Jihuafei
 * Date: 15-11-12
 * Time: 上午9:40
 */

namespace Common\Model;


class IosTokenModel extends BaseModel{
    protected $tableName = 'IosToken';

    /**
     * 编辑IOS的token记录
     * @param array $data
     * @return array
     */
    public function editIosToken($data) {
        $tokenInfo = $this->where(array('token' => $data['token']))->find();
        if($tokenInfo) {
            $code = $this->where(array('token' => $data['token']))->save(array('updateTime' => time())) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        } else {
            $data['inputTime'] = $data['updateTime'] = time();
            $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        }
        return $this->getBusinessCode($code);
    }
} 