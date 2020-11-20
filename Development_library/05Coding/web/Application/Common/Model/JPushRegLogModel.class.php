<?php
/**
 * Created by PhpStorm.
 * User: Huafei Ji
 * Date: 15-7-25
 * Time: 下午7:09
 */
namespace Common\Model;

class JPushRegLogModel extends BaseModel {
    protected $table = 'JPushRegLog';

    /**
     * 记录设备第一次注册到JPush后返回的RegistrationID
     * @param string $device 设备信息
     * android格式为:
     * a AndroidOS版本号_分辨率_应用版本号_手机厂商_联网方式_手机唯一标识，如(a4.4_720*1280*2.0_100_XiaoMi_WIFI_XXXXXXXXXXXX)
     * iOS格式为:
     * i iPhoneOS版本号_分辨率_应用版本号_手机厂商_联网方式_手机唯一标识，如(i8.1_640*1136*2.0_100_iPhone4s_WIFI_XXXXXXXXXXXX)
     * @param string $regId 注册ID
     * @param int $appType 应用类型。1-惠圈。2-惠圈商户
     * @return array
     */
    public function addRegId($device, $regId, $appType) {
        $rules = array(
            array('device', 'require', C('.'), 1),
            array('registrationId', 'require', C('.'), 1),
        );
        $data = array('device' => $device, 'registrationId' => $regId, 'appType' => $appType);
        if($this->validate($rules)->create($data) != false) {
            $data['logCode'] = $this->create_uuid();
            $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            return $this->getBusinessCode($code);
        } else {
            return $this->getValidErrorCode();
        }
    }
}