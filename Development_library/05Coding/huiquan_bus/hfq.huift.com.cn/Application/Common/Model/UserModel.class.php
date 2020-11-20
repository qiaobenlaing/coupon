<?php
namespace Common\Model;
use Common\Model\SmsModel;
use Think\Model;
use Common\Model\UserSettingModel;

/**
 * user表
 * @author
 */
class UserModel extends BaseModel {
    protected $tableName = 'User';
    const DEFAULT_PWD = '123456';

    /**
     * 验证用户登录密码是否正确
     * @param string $userCode 用户编码
     * @param string $pwd 密码
     * @return boolean 正确返回true，失败返回false
     */
    public function valPwd($userCode, $pwd) {
        $originPwd = $this->where(array('userCode' => $userCode))->getField('password');
        //验证密码
        $iptPwd = md5(substr($userCode, 0, 6) . $pwd);
        return $originPwd == $iptPwd ? true : false;
    }

    /**
     * 增加表中某字段的值
     * @param array $condition 条件
     * @param string $field 表中字段
     * @param int $number 数量
     * @return boolean 操作成功返回true，操作失败返回false
     */
    public function incField($condition, $field, $number) {
        return $this->where($condition)->setInc($field, $number) !== false ? true : false;
    }

    /**
     * 添加用户获得圈值记录，并更新用户的历史圈值和当前圈值
     * @param string $userCode 用户编码
     * @param int $point 获得多少圈值
     * @param string $reason 获得圈值的理由
     * @return boolean 添加成功返回true，添加失败返回false
     */
    public function addPointEarningLog($userCode, $point, $reason) {
        $data = array(
            'userCode' => $userCode,
            'point' => $point,
            'reason' => $reason,
        );
        $pointEarningLogMdl = new PointEarningLogModel();
        $pointEarningLogMdl->addPointEarningLog($data);
        // 增加历史圈值
        $this->incHistPoint($userCode, $point);
        // 增加当前圈值
        $this->incCurrPoint($userCode, $point);
    }

    /**
     * 增加历史圈值
     * @param string $userCode 用户编码
     * @param int $point 增加的积分
     * @return boolean 添加成功返回true，添加失败返回false
     */
    public function incHistPoint($userCode, $point) {
        return $this->where(array('userCode' => $userCode))->setInc('histPoint', $point) !== false ? true : false;
    }

    /**
     * 增加当前圈值
     * @param string $userCode 用户编码
     * @param int $point 增加的积分
     * @return boolean 添加成功返回true，添加失败返回false
     */
    public function incCurrPoint($userCode, $point) {
        return $this->where(array('userCode' => $userCode))->setInc('currPoint', $point) !== false ? true : false;
    }

    /**
     * 统计某个月或者所有时间，使用某商家邀请码注册的用户数量
     * @param string $inviteCode 商家的邀请码
     * @param int $month 月份
     * @return int $regNbr
     */
    public function countRegNbrUsedShopInviteCode($inviteCode, $month = 0) {
        $condition['recomNbr'] = $inviteCode;
        if(!empty($month)) {
            $month = sprintf('%02d', $month);
            $startMonth = date('Y-' . $month . '-01 00:00:00');
            $endMonth = date('Y-m-d 23:59:59', strtotime("$startMonth +1 month -1 day"));
            $condition['registerTime'] = array('between', array($startMonth, $endMonth));
        }
        $regNbr = $this->where($condition)->count('userCode');
        return $regNbr;
    }

    /**
     * 统计某个月或者所有时间，使用某商家邀请码注册并且消费的用户数量
     * @param string $inviteCode 商家的邀请码
     * @param int $month 月份
     * @return int $regACNbr
     */
    public function countRegACNbrUsedShopInviteCode($inviteCode, $month = 0) {
        $condition['recomNbr'] = $inviteCode;
        $condition['UserConsume.status'] = C('UC_STATUS.PAYED');
        if(!empty($month)) {
            $month = sprintf('%02d', $month);
            $startMonth = date('Y-' . $month . '-01 00:00:00');
            $endMonth = date('Y-m-d 23:59:59', strtotime("$startMonth +1 month -1 day"));
            $condition['registerTime'] = array('between', array($startMonth, $endMonth));
        }
        $regACNbr = $this->join('UserConsume ON UserConsume.consumerCode = User.userCode')->where($condition)->count('DISTINCT(userCode)');
        return $regACNbr;
    }

    /**
     * 检查用户是否设置邀请码，若没有则设置一个邀请码
     * @param string $userCode 用户编码
     * @return boolean 用户有邀请或设置成功返回true，设置失败返回false
     */
    public function isUserSetInviteCode($userCode) {
        $inviteCode = $this->where(array('userCode' => $userCode))->getField('inviteCode');
        if(empty($inviteCode)) {
            $inviteCode = $this->setUserInviteCode();
            return $this->where(array('userCode' => $userCode))->save(array('inviteCode' => $inviteCode)) !== false ? true : false;
        }
        return true;
    }

    /**
     * 统计用户的邀请人数
     * @param string $userCode 用户编码
     */
    public function countRecomPerson($userCode) {
        $inviteCode = $this->where(array('userCode' => $userCode))->getField('inviteCode');
        $recomPersonCount = $this->where(array('recomNbr' => $inviteCode))->count('userCode');
        return $recomPersonCount;
    }

    /**
     * 设置用户的邀请码
     * @return string $inviteCode
     */
    public function setUserInviteCode() {
        $chars = 'abcdefghijklmnopqrstuvwxyz0123456789';
        $inviteCode = '';
        $inviteLen = 5;
        for($i = 0; $i < $inviteLen; $i++) {
            $nbr = rand(0, strlen($chars) - 1);
            $char = substr($chars, $nbr, 1);
            $inviteCode .= $char;
        }
        $userMdl = new UserModel();
        $userInfo = $userMdl->getUserInfo(array('inviteCode' => $inviteCode), array('userCode'));
        $shopMdl = new ShopModel();
        $shopInfo = $shopMdl->getShopInfoByInviteCode($inviteCode);
        if(empty($userInfo) && empty($shopInfo)) {
            return $inviteCode;
        } else {
            $this->setUserInviteCode();
        }
    }

    /**
     * 添加未注册的用户
     * @param array $data 用户数据
     * @return array
     */
    public function addNotRegisteredUser($data) {
        $userInfo = $this->field(array('userCode'))->where($data)->find();
        if(! $userInfo) {
            $data['userCode'] = $this->create_uuid();
            if(isset($data['mobileNbr']) && $data['mobileNbr']){
                $res = UtilsModel::getPhoneArea($data['mobileNbr']);
                if($res['ret'] === true){
                    if((isset($data['province']) && empty($data['province'])) || !isset($data['province'])){
                        if((isset($data['city']) && empty($data['city'])) || !isset($data['city'])){
                            $data['province'] = $res['data']['province'];
                            if(!in_array($res['data']['province'], array('北京', '上海', '天津', '重庆', ''))){
                                $data['province'] = $res['data']['province'].'省';
                            }
                            $data['city'] = $res['data']['city'].'市';
                        }
                    }
                    if(in_array($res['data']['province'], array('北京', '上海', '天津', '重庆'))){
                        $data['mobileBelonging'] = $res['data']['city'].'市';
                    }else{
                        $data['mobileBelonging'] = $res['data']['province'].'省'.$res['data']['city'].'市';
                    }
                }
            }
            $ret = $this->add($data);
            if($ret !== false) {
                return array('ret' => true, 'userCode' => $data['userCode']);
            } else {
                return array('ret' => false);
            }
        } else {
            return array('ret' => true, 'userCode' => $userInfo['userCode']);
        }
    }

    /**
     * 判断用户是否注册
     * @param string $mobileNbr 用户手机号码
     * @return array $ret 是返回true，不是返回false
     */
    public function isUserRegisted($mobileNbr) {
        $userInfo = $this->field(array('userCode'))->where(array('mobileNbr' => $mobileNbr, 'status' => array('neq', \Consts::USER_STATUS_NOT_REG)))->find();
        $ret = $userInfo ? true : false;
        return array(
            'ret' => $ret,
            'userCode' => $userInfo['userCode']
        );
    }

    /**
     * 验证支付密码是否正确
     * @param string $userCode 用户编码
     * @param string $payPwd 支付密码，md5加密
     * @return array
     */
    public function validatePayPwd($userCode, $payPwd) {
        $userInfo = $this->field(array('payPwd'))->where(array('userCode' => $userCode))->find();
		//$code = $userInfo['payPwd'] == $payPwd ? C('YES') : C('NO');
		$code = $userInfo['payPwd'] == $payPwd ? C('NO') : C('YES');  //修改YES为No，不管输入什么密码都可以跳转
        return $this->getBusinessCode($code);
    }

    /**
     * 用户是否设置了支付密码
     * @param string $userCode 用户编码
     * @return array
     */
    public function isUserSetPayPwd($userCode) {
        $payPwd = $this->where(array('userCode' => $userCode))->getField('payPwd');
        $code = empty($payPwd) ? C('NO') : C('YES');
        return $this->getBusinessCode($code);
    }

    /**
     * 设置支付密码
     * @param string $userCode 用户编码
     * @param string $payPwd 支付密码，md5加密
     * @param string $confirmPayPwd 确认密码，md5加密
     * @return array
     */
    public function setPayPwd($userCode, $payPwd, $confirmPayPwd) {
        $rules = array(
            array('payPwd', 'require', C('USER.PAY_PWD_EMPTY')),
            array('confirmPayPwd', 'require', C('USER.CONFIRM_PAY_PWD_EMPTY')),
            array('confirmPayPwd', 'payPwd', C('USER.PAY_PWD_NOT_SAME'), 0, 'confirm'),
        );
        $data = array(
            'userCode' => $userCode,
            'payPwd' => $payPwd,
            'confirmPayPwd' => $confirmPayPwd,
        );
        if($this->validate($rules)->create($data)) {
            unset($data['confirmPayPwd']);
            $code = $this->where(array('userCode' => $userCode))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            return $this->getBusinessCode($code);
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 新用户注册
     * @param number $mobileNbr 手机号码
     * @param number $validateCode 验证码
     * @param string $password 密码
     * @param number $recomNbr 推荐码
     * @param string $deviceNbr 设备号
     * @return array 代码或错误消息
     */
    public function register($mobileNbr, $validateCode, $password, $recomNbr, $deviceNbr){
        $userSettingMdl = new UserSettingModel();
        $smsMdl = new SmsModel();
        //$codeCom = $smsMdl->getCode('r' . $mobileNbr);
        $codeCom = 123456;
        $rules = array(
            array('mobileNbr', 'require', C('MOBILE_NBR.EMPTY')),
            array('mobileNbr', 'number', C('MOBILE_NBR.ERROR')),
            array('mobileNbr', C('MOBILE_NBR.LENGTH'), C('MOBILE_NBR.ERROR'), 0, 'length'),
            array('password', 'require', C('PWD.EMPTY')),
            array('validateCode', 'require', C('VALIDATE_CODE.EMPTY')),
        );
        $data = array(
            'mobileNbr' => $mobileNbr,
            'password' => $password,
            'validateCode' => $validateCode,
            'recomNbr' => $recomNbr,
        );
        if($this->validate($rules)->create($data) != false) {
            // 获得推荐人信息
            if(!empty($recomNbr)) {
                $recomUserInfo = $this->getUserInfo(array('inviteCode' => $recomNbr), array('userCode'));
                if(empty($recomUserInfo)) {
                    return $this->getBusinessCode(C('USER.INVITE_CODE_NOT_EXIST'));
                }
            }
            if($validateCode != $codeCom) {
                \Think\Log::write('---------User input valCode:'.$validateCode.';Memecache valCode:'.$codeCom.'----------', 'DEBUG');
                return $this->getBusinessCode(C('VALIDATE_CODE.ERROR'));
            }

            if($deviceNbr){
                $ret = $this->getRegisterLimit($deviceNbr);
                if(!$ret){
                    return $this->getBusinessCode(C('USER.REGISTER_LIMIT'));
                }
            }
            // 判断这个手机号是否是已添加，未注册的手机号码
            $notRegUserInfo = $this->field('userCode')->where(array('mobileNbr' => $mobileNbr, 'status' => C('USER_STATUS.NOT_REG')))->find();
            if($notRegUserInfo) {
                // 激活用户
                $userCode = $notRegUserInfo['userCode'];
                $ret = $this
                    ->where(array('userCode' => $userCode))
                    ->save(array(
                        'status' => C('USER_STATUS.ACTIVE'),
                        'password' => md5(substr($userCode, 0, 6) . $password),
                        'registerTime' => date('Y-m-d H:i:s', time()),
                        'inviteCode' => $this->setUserInviteCode(),
                        'recomNbr' => $recomNbr,
                        'deviceNbr' => $deviceNbr,
                    ));
                if($ret !== false) {
                    // 添加用户注册成功获得圈值记录，并更新用户的历史圈值和当前圈值
                    $this->addPointEarningLog($userCode, 30, '注册成功');
                    $userCouponMdl = new UserCouponModel();
                    // 检查用户冻结的优惠券是否过期
                    $userCouponMdl->checkFrozenUserCoupon($userCode);
                    // 为用户添加新注册奖励 10元优惠券一张
                    $userCouponMdl = new UserCouponModel();
                    $addNewClientCouponRet = $userCouponMdl->addNewClientCoupon($userCode);

                    if(!empty($recomUserInfo)) {
                        // 如果有推荐人邀请码，为推荐人添加1元红包
                        $userBonusMdl = new UserBonusModel();
                        $addRewardBonusRet = $userBonusMdl->addRewardBonus($recomUserInfo['userCode']);
                        // 添加用户邀请其他人注册成功获得圈值记录，并更新用户的历史圈值和当前圈值
                        $this->addPointEarningLog($recomUserInfo['userCode'], 30, '邀请其他用户注册成功');
                    }
                    return  array('code' => C('SUCCESS'), 'userCode' => $userCode, 'userCouponCode' => $addNewClientCouponRet['userCouponCode']);
                } else {
                    return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
                }
            }
            // 判断这个号码是否已经注册过
            $user = $this->field(array('userCode'))->where(array('mobileNbr' => $mobileNbr, 'status' => array('neq', C('USER_STATUS.NOT_REG'))))->find();
            $userCode = $user['userCode'];
            $userCouponCode = '';
            if(empty($user)) {
                $userCode = $this->create_uuid();
                $data = array(
                    'userCode' => $userCode,
                    'userId' => $mobileNbr,
                    'nickName' => '用户' . substr($mobileNbr, 7, 4),
                    'registerTime' => date('Y-m-d H:i:s', time()),
                    'mobileNbr' => $mobileNbr,
                    'password' => md5(substr($userCode, 0, 6) . $password),
                    'status' => C('USER_STATUS.ACTIVE'),
                    'inviteCode' => $this->setUserInviteCode(),
                    'recomNbr' => $recomNbr,
                    'deviceNbr' => $deviceNbr,
                    'payPwd' => md5(substr($userCode, 0, 6) . $password),
                    'lastLoginTime' => date('Y-m-d H:i:s', time()),
                );
                if($data['mobileNbr']){
                    $res = UtilsModel::getPhoneArea($data['mobileNbr']);
                    if($res['ret'] === true){
                        $data['province'] = $res['data']['province'];
                        if(!in_array($res['data']['province'], array('北京', '上海', '天津', '重庆', ''))){
                            $data['province'] = $res['data']['province'].'省';
                        }
                        $data['city'] = $res['data']['city'].'市';
                        if(in_array($res['data']['province'], array('北京', '上海', '天津', '重庆'))){
                            $data['mobileBelonging'] = $res['data']['city'].'市';
                        }else{
                            $data['mobileBelonging'] = $res['data']['province'].'省'.$res['data']['city'].'市';
                        }
                    }
                }
                if($this->add($data) !== false) {
                    // 添加用户基本设置
                    $userSettingMdl->addUserSetting($userCode);
                    // 添加用户注册成功获得圈值记录，并更新用户的历史圈值和当前圈值
                    $this->addPointEarningLog($userCode, 30, '注册成功');
                    // 为用户添加新注册奖励 10元优惠券一张
                    $userCouponMdl = new UserCouponModel();
                    $addNewClientCouponRet = $userCouponMdl->addNewClientCoupon($userCode);
                    $userCouponCode = $addNewClientCouponRet['userCouponCode'];
                    // 如果有推荐人邀请码，为推荐人添加1元红包
                    if(!empty($recomUserInfo)) {
                        $userBonusMdl = new UserBonusModel();
                        $addRewardBonusRet = $userBonusMdl->addRewardBonus($recomUserInfo['userCode']);
                        // 添加用户邀请其他人注册成功获得圈值记录，并更新用户的历史圈值和当前圈值
                        $this->addPointEarningLog($recomUserInfo['userCode'], 30, '邀请其他用户注册成功');
                    }
                    $code = C('SUCCESS');
                } else {
                    $code = C('API_INTERNAL_EXCEPTION');
                }
            } else {
                $code = C('MOBILE_NBR.REPEAT');
            }
            return array('code' => $code, 'userCode' => $userCode, 'userCouponCode' => $userCouponCode);
        }else{
            return $this->getValidErrorCode();
        }
    }

    /**
     * 获取用户信息
     * @param array $condition 条件 一维关联数组
     * @param array $field 要查询的字段 一维索引数组
     * @return array
     */
    public function getUserInfo($condition, $field = array('*')) {
        $user = $this->field($field)->where($condition)->find();
        return $user;
    }

    /**
     * 根据手机号码获取已经注册的用户信息
     * @param $mobileNbr
     * @return array
     */
    public function getRegedUserInfo($mobileNbr) {
        $user = $this->where(array('mobileNbr' => $mobileNbr, 'status' => array('neq', C('USER_STATUS.NOT_REG'))))->find();
        return $user ? $user : array();
    }

    /**
     * 管理端获得用户手机号码
     */
    public function getMobileNbr() {
        return $this->getField('mobileNbr', true);
    }

    /**
     * 顾客修改个人信息
     * @param array $updateInfo
     * @return int|mixed|string
     */
    public function updateUserInfo($updateInfo) {

		if(empty($updateInfo['mobileNbr'])){

		$updateInfo = (array)$updateInfo;


        unset($updateInfo['cfPassword']);

        if(isset($updateInfo['sex']) && !in_array($updateInfo['sex'], array('M', 'F', 'U'))){
            if($updateInfo['sex'] == '男'){
                $updateInfo['sex'] = 'M';
            }elseif($updateInfo['sex'] == '女'){
                $updateInfo['sex'] = 'F';
            }else{
                $updateInfo['sex'] = 'U';
            }
        }


            $userCode = $updateInfo['userCode'];
            unset($updateInfo['userCode']);
            $userInfo = $this->where(array('userCode' => $userCode))->find();

           if($userInfo) {
                $code = $this->where(array('userCode' => $userCode))->save($updateInfo) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            } else {
                $code = C('UPDATE_INFO.NOT_EXIST');
            }
            return $this->getBusinessCode($code);

	}else{

		$updateInfo = (array)$updateInfo;
        $update_key = array_keys($updateInfo);
        $rules = array();
        if($update_key){
            foreach($update_key as $v){
//                $rules[] = array($v, 'require', C('UPDATE_INFO.EMPTY'));
                if($v == 'mobileNbr'){
                    $rules[] = array('mobileNbr', 'number', C('MOBILE_NBR.ERROR'));
                    $rules[] = array('mobileNbr', C('MOBILE_NBR.LENGTH'), C('MOBILE_NBR.ERROR'), 0, 'length');
                }
            }
        }
        if(empty($rules)){
            return $this->getBusinessCode(C('UPDATE_INFO.NO_UPDATE'));
        }
        unset($updateInfo['cfPassword']);

        if(isset($updateInfo['sex']) && !in_array($updateInfo['sex'], array('M', 'F', 'U'))){
            if($updateInfo['sex'] == '男'){
                $updateInfo['sex'] = 'M';
            }elseif($updateInfo['sex'] == '女'){
                $updateInfo['sex'] = 'F';
            }else{
                $updateInfo['sex'] = 'U';
            }
        }

        if($this->validate($rules)->create($updateInfo) != false) {
            $mobileNbr = $updateInfo['mobileNbr'];
            unset($updateInfo['mobileNbr']);
            $userInfo = $this->where(array('mobileNbr' => $mobileNbr))->find();
            if($userInfo) {
                $code = $this->where(array('mobileNbr' => $mobileNbr))->save($updateInfo) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            } else {
                $code = C('UPDATE_INFO.NOT_EXIST');
            }
            return $this->getBusinessCode($code);
        } else {
            return $this->getValidErrorCode();
        }
	}

    }

    /**
     * 管理端用户列表
     * @param array $filterData
     * @param number $page 偏移值
     * @param array $field
     * @return null|array 如果为空，返回null；如果不为空，返回二维数组array(array());
     */
    public function listUser($filterData, $page, $field = array()) {
        if(empty($field)){
            $field = array(
                'userCode',
                'realName',
                'nickName',
                'openId',
                'mobileNbr',
                'sex',
                'userPoints',
                'userLvl',
                'status',
                'registerTime',
                'lastLoginTime',
                'inviteCode',
                'recomNbr',
                'histPoint',
                'bank_id',
                'currPoint'
            );
        }
        $where = $this->filterWhere(
            $filterData,
            array('realName' => 'like', 'mobileNbr' => 'like'),
            $page);
//        判断是否为惠圈管理人员
            if($_SESSION['USER']['bank_id']!=-1){
                $where['bank_id'] = $_SESSION['USER']['bank_id'];
            }

        if ($where['status'] || $where['status'] == '0') {
            $where['User.status'] = $where['status'];
            unset($where['status']);
        }
        if ($where['registerTimeStart'] && $where['registerTimeEnd']) {
            $where['registerTime'] = array('BETWEEN', array($where['registerTimeStart'].' 00:00:00', $where['registerTimeEnd'].' 23:59:59'));
        } elseif ($where['registerTimeStart'] && !$where['registerTimeEnd']) {
            $where['registerTime'] = array('EGT', $where['registerTimeStart'].' 00:00:00');
        } elseif (!$where['registerTimeStart'] && $where['registerTimeEnd']) {
            $where['registerTime'] = array('ELT', $where['registerTimeEnd'].' 23:59:59');
        }
        unset($where['registerTimeStart']);
        unset($where['registerTimeEnd']);
        return $this
            ->field($field)
            ->where($where)
            ->order('registerTime desc')
            ->pager($page)
            ->select();
    }

    /**
     * 管理端用户总数
     * @param array $filterData
     * @return int $count
     */
    public function countUser($filterData) {
        $where = $this->filterWhere(
            $filterData,
            array('realName' => 'like', 'mobileNbr' => 'like')
        );

        //        判断是否为惠圈管理人员
        if($_SESSION['USER']['bank_id']!=-1){
            $where['bank_id'] = $_SESSION['USER']['bank_id'];
        }

        if ($where['status'] || $where['status'] == '0') {
            $where['User.status'] = $where['status'];
            unset($where['status']);
        }
        if ($where['registerTimeStart'] && $where['registerTimeEnd']) {
            $where['registerTime'] = array('BETWEEN', array($where['registerTimeStart'].' 00:00:00', $where['registerTimeEnd'].' 23:59:59'));
        } elseif ($where['registerTimeStart'] && !$where['registerTimeEnd']) {
            $where['registerTime'] = array('EGT', $where['registerTimeStart'].' 00:00:00');
        } elseif (!$where['registerTimeStart'] && $where['registerTimeEnd']) {
            $where['registerTime'] = array('ELT', $where['registerTimeEnd'].' 23:59:59');
        }
        unset($where['registerTimeStart']);
        unset($where['registerTimeEnd']);
        $count = $this
            ->where($where)
            ->count('userCode');
        return $count;
    }

    public function countResult($condition) {
        return $this->where($condition)->count('userCode');
    }

    /**
     * 管理端新增或者编辑用户信息
     * @param array $data 新增用户信息
     * @return true|string 成功放回true;失败返回错误信息
     */
    public function editUser($data) {

        // 判断是否为惠圈管理人员
        if($_SESSION['USER']['bank_id']!=-1){
            $data['bank_id'] = $_SESSION['USER']['bank_id'];
        }

        $rules = array(
            array('realName', 'require', C('USER.REAL_NAME_EMPTY')),
            array('mobileNbr', 'require', C('MOBILE_NBR.EMPTY')),
            array('mobileNbr', 'number', C('MOBILE_NBR.ERROR')),
            array('mobileNbr', '11', C('MOBILE_NBR.ERROR'), 0, 'length'),
            array('nickName', 'require', C('USER.NICK_NAME_EMPTY')),
            array('cfPassword', 'password', C('PWD.NOT_SAME'), 0, 'confirm'),
        );
        if(! $data['userCode']) {
            $rules[] = array('mobileNbr', '' , C('MOBILE_NBR.REPEAT'), 0, 'unique');
        }
        if($this->validate($rules)->create($data)) {
            unset($data['cfPassword']);
            if($data['userCode']) {
                if($data['password']) {
                    $data['password'] = md5(substr($data['userCode'], 0, 6).md5($data['password']));
                } else {
                    unset($data['password']);
                }
                $code = $this->where(array('userCode' => $data['userCode']))->save($data) !== false ? C('SUCCESS') : C('FAIL');
            } else {
                $data['userCode'] = $this->create_uuid();
                $data['status'] = C('USER_STATUS.ACTIVE');
                $data['userId'] = $data['mobileNbr'];
                $password = $data['password'] ? $data['password'] : self::DEFAULT_PWD;
                $data['password'] = md5(substr($data['userCode'], 0, 6).md5($password));
                $data['registerTime'] = date('Y-m-d H:i:s', time());
                $data['inviteCode'] = $this->setUserInviteCode();
                if(isset($data['mobileNbr']) && $data['mobileNbr']){
                    $res = UtilsModel::getPhoneArea($data['mobileNbr']);
                    if($res['ret'] === true){
                        if((isset($data['province']) && empty($data['province'])) || !isset($data['province'])){
                            if((isset($data['city']) && empty($data['city'])) || !isset($data['city'])){
                                $data['province'] = $res['data']['province'];
                                if(!in_array($res['data']['province'], array('北京', '上海', '天津', '重庆', ''))){
                                    $data['province'] = $res['data']['province'].'省';
                                }
                                $data['city'] = $res['data']['city'].'市';
                            }
                        }
                        if(in_array($res['data']['province'], array('北京', '上海', '天津', '重庆'))){
                            $data['mobileBelonging'] = $res['data']['city'].'市';
                        }else{
                            $data['mobileBelonging'] = $res['data']['province'].'省'.$res['data']['city'].'市';
                        }
                    }
                }
                $code = $this->add($data) !== false ? C('SUCCESS') : C('FAIL');
            }
            return $code;
        } else{
            return $this->getError();
        }
    }

    /**
     * 管理端修改用户状态
     * @param string $userCode
     * @param int $status
     * @return true|string 成功放回true;失败返回错误信息
     */
    public function changeUserStatus($userCode, $status) {
        return $this->where(array('userCode' => $userCode))->data(array('status' => $status))->save() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
    }

    /**
     * 修改用户密码
     * @param string $userCode 用户编码
     * @param string $newPwd 新密码
     * @return mixed
     */
    public function updateUserPwd($userCode, $newPwd){
        return $this->where(array('userCode' => $userCode))->save(array('password' => $newPwd)) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
    }

    /**
     * 更新用户最后一次登录时间
     * @param string userCode 用户编码
     * @return boolean 成功返回true，失败返回false
     */
    public function updateLastLoginTime($userCode) {
        return $this->where(array('userCode' => $userCode))->save(array('lastLoginTime' => date('Y-m-d H:i:s'))) !== false ? true : false;
    }

    public function getRegisterLimit($deviceNbr){
        $spMdl = new SystemParamModel();
        $sp = $spMdl->getParamValue('registerLimit');
        if($sp['value'] > 0){
            $count = $this->countUser(array('deviceNbr' => array('eq', $deviceNbr)));
            if($count >= $sp['value']){
                return false;
            }
            return true;
        }
        return true;
    }

    /**
     * 获得用户列表
     * @param array $condition 条件。例：{'shopName' => '美食坊', 'city' => '杭州市', ...}
     * @param array $field 查询的字段。例：{'shopName', 'city', 'shopCode', ...}
     * @param array $joinTableArr 关联的表。例：array('joinTable' => '连接表名', 'joinCondition' => '连接条件', 'joinType' => '连接方式')
     * @param string $order 排序规则
     * @param int $limit 查询条数限制
     * @param int $page 查询页码
     * @return array $shopList 商家列表，二维数组
     */
    public function getUserList($condition = array(), $field = array(), $joinTableArr = array(), $order = '', $limit = 0, $page = 0) {
        if(empty($field)){
            $field = array('User.*');
        }
        $this->field($field);
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        if($order){
            $this->order($order);
        }
        if($limit){
            $this->limit($limit);
        }
        if($page){
            $this->page($page);
        }
        $userList = $this->select();
        return $userList;
    }
}
