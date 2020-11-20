<?php
namespace Common\Model;
/**
 * Created by PhpStorm.
 * User: Jihuafei
 * Date: 15-10-12
 * Time: 下午4:16
 */
class UserAddressModel extends BaseModel {
    protected $tableName = 'UserAddress';

    /**
     * 获得用户地址详情
     * @param int $userAddressId 用户地址ID
     * @return array $userAddressInfo
     */
    public function getUserAddressInfo($userAddressId) {
        $userAddressInfo = $this
            ->field(array('userAddressId', 'contactName', 'mobileNbr', 'province', 'city', 'district', 'street', 'position', 'longitude', 'latitude'))
            ->where(array('userAddressId' => $userAddressId))
            ->find();
        return $userAddressInfo;
    }

    /**
     * 获得用户地址列表
     * @param string $userCode 用户编码
     * @return array $userAddressList
     */
    public function getUserAddressList($userCode) {
        $userAddressList = $this
            ->field(array('userAddressId', 'contactName', 'mobileNbr', 'province', 'city', 'district', 'street', 'position', 'longitude', 'latitude'))
            ->where(array('userCode' => $userCode))
            ->select();
        return $userAddressList;
    }

    /**
     * 删除用户地址
     * @param int $userAddressId 用户地址ID
     * @return array $ret
     */
    public function delUserAddress($userAddressId) {
        $code = $this->where(array('userAddressId' => $userAddressId))->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

    /**
     * 编辑用户地址
     * @param array $data
     * @return array
     */
    public function editUserAddress($data) {
        $rules = array(
            array('userCode', 'require', C('USER.USER_CODE_EMPTY')),
            array('contactName', 'require', C('USER_ADDRESS.CONTACT_NAME_EMPTY')),
            array('mobileNbr', 'require', C('USER_ADDRESS.CONTACT_MOBILE_EMPTY')),
            array('province', 'require', C('USER_ADDRESS.PROVINCE_EMPTY')),
            array('city', 'require', C('USER_ADDRESS.CITY_EMPTY')),
            array('district', 'require', C('USER_ADDRESS.DISTRICT_EMPTY')),
            array('street', 'require', C('USER_ADDRESS.STREET_EMPTY')),
        );
        if($this->validate($rules)->create($data) != false) {
            if(! empty($data['userAddressId'])) {
                $code = $this->where(array('userAddressId' => $data['userAddressId']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
                $userAddressId = $data['userAddressId'];
                $action = 'edit';
            }else{
                $userAddressId = $this->add($data);
                if($userAddressId){
                    $code = C('SUCCESS');
                }else{
                    $code = C('API_INTERNAL_EXCEPTION');
                }
                $action = 'add';
            }
            return array('code' => $code, 'userAddressId'=> $userAddressId, 'action' => $action);
        } else {
            return $this->getValidErrorCode();
        }
    }

}