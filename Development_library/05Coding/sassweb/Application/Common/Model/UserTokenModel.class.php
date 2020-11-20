<?php
/**
 * Created by PhpStorm.
 * User: Administrator
 * Date: 15-4-27
 * Time: 上午9:03
 */
namespace Common\Model;
use Think\Model;

class UserTokenModel extends BaseModel {
    const VALID_TIME = 172800;  //48小时
    protected $tableName = 'UserToken';

    /**
     * 获得注册ID
     * @param string $tokenCode
     * @return string jpush注册ID
     */
    public function getRegistrationId($tokenCode) {
        return $this->where(array('tokenCode' => $tokenCode))->getField('registrationId');
    }

    /**
     * 生成一个令牌
     * @param string $userCode 用户编码
     * @param string $registrationId
     * @return string 成功返回主键，失败返回错误信息
     */
    public function createToken($userCode, $registrationId) {
        $this->delToken($userCode);
        $userTokenCode = $this->create_uuid();
        $tokenCode = md5(uniqid(mt_rand(), true));
        $expireTime = time() + self::VALID_TIME;
        return $this->add(array(
            'userTokenCode' => $userTokenCode,
            'tokenCode' => $tokenCode,
            'expireTime' => $expireTime,
            'userCode' => $userCode,
            'registrationId' => $registrationId,
        )) ? $userTokenCode : C('API_INTERNAL_EXCEPTION');
    }

    /**
     * 获取令牌
     * @param string $userCode 用户编码
     * @return array $logInfo
     */
    public function getUserToken($userCode){
        $logInfo = $this
            ->field(array('userTokenCode','tokenCode','expireTime', 'registrationId'))
            ->where(array('userCode' => $userCode))
            ->order('expireTime desc')
            ->find();
        return $logInfo;
    }

    /**
     * 获取令牌
     * @param string $userTokenCode 用户token编码
     * @return array $logInfo
     */
    public function getToken($userTokenCode){
        $logInfo = $this
            ->field(array('tokenCode','expireTime'))
            ->where(array('userTokenCode' => $userTokenCode))
            ->order('expireTime desc')
            ->find();
        return $logInfo;
    }

    /**
     * 检查令牌
     * @param string $requestTime 请求时间
     * @param string $vcode vcode
     * @param string $method 方法名称
     * @param string $param 第一个参数的值
     * @return string
     */
    public function validateToken($requestTime,$vcode, $method, $param) {
        //判断是否缺少必需的参数$requestTime,
     /**   if (empty($requestTime) || empty($vcode) || empty($method) || !isset($param)) {  
			 
            return C('INVALID_PARAMS');
       }**/
	  //  if ( empty($method) || !isset($param)) {  
			 
     //       return C('INVALID_PARAMS');
     //  }
	   
	 
	   
   //     $userTokenInfo = $this
   //         ->field(array('userTokenCode', 'tokenCode', 'userCode', 'expireTime'))
   //         ->where(array('tokenCode' => substr($vcode, 0, 32)))
    //        ->order('expireTime desc')
    //        ->find();
        //判断令牌是否存在
     //   if (empty($userTokenInfo)) {
     //      return C('INVALID_TOKEN');
      //  }
    //    $expireTime = $userTokenInfo['expireTime'];
        // 是否验证token有效期
    //    if(\Consts::IS_VALIDATE_TOKEN_EXPIRE == C('YES')) {
            //判断令牌是否过期
     //       if ($expireTime < time()) {
     //           $this->where(array('userTokenCode' => $userTokenInfo['userTokenCode']))->delete();
      //          return C('TOKEN_OVERTIME');
      //      }
     //   }
    //    $tcode = $userTokenInfo['tokenCode'] . md5($method . $param . md5(substr($userTokenInfo['userCode'], 0, 6)));
		
    //    if($tcode != $vcode){
    //        return C('INVALID_TOKEN');
    //    }
        return C('SUCCESS');
    }

    /**
     * 销毁用户token
     * @param string $tokenCode
     * @return mixed
     */
    public function destroyToken($tokenCode){
        return $this->where(array('tokenCode' => $tokenCode))->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
    }

    /**
     * 销毁用户token
     * @param string $userCode
     * @return mixed
     */
    public function delToken($userCode){
        return $this->where(array('userCode' => $userCode))->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
    }

    public function updateByUserTokenCode($userTokenCode,$data){
        return $this->where(array('userTokenCode' => $userTokenCode))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
    }

}
