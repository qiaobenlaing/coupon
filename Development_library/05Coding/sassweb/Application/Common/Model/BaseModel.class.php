<?php
namespace Common\Model;
use Think\Model;
use Think\Exception;
use Common\Model\SmsModel;
use Common\Model\UserLoginLogModel;
use Org\FirePHPCore\FP;
use Common\Model\UserModel;
use Common\Model\ShopStaffModel;
use Common\Model\BmStaffModel;
use Common\Model\JpushModel;

class BaseModel extends Model {
    protected $autoCheckFields  =   false;

    /**
     * 获取验证码
     * @param number $mobileNbr 手机号码
     * @param $action
     * @param $extra
     * @return mixed
     * @throws RPCException
     */
    public function getValidateCode($mobileNbr, $action, $extra = array()){
        if($mobileNbr == null || $mobileNbr == '') {
            return $this->getBusinessCode(C('MOBILE_NBR.EMPTY'));
        }
        if(! is_numeric($mobileNbr) || strlen($mobileNbr) != C('MOBILE_NBR.LENGTH')) {
            return $this->getBusinessCode(C('MOBILE_NBR.ERROR'));
        }
        $smsMdl = new SmsModel();
        $validateCode = $smsMdl->getCode($action . $mobileNbr);
        if($validateCode === false) {
            $validateCode = $smsMdl->createCode($action . $mobileNbr);
        }
        try{
            if($action == \Consts::MSG_VAL_ACTION_REG) { // 注册，激活
                $message = str_replace('{{validateCode}}', $validateCode, C('SEND_MESSAGE.REGISTER'));
            } elseif($action == \Consts::MSG_VAL_ACTION_FIND_PWD) { //找回密码
                $message = str_replace('{{validateCode}}', $validateCode, C('SEND_MESSAGE.FIND_PWD'));
            } elseif($action == \Consts::MSG_VAL_ACTION_SET_PAY_PWD) { // 设置支付密码
                $message = str_replace('{{validateCode}}', $validateCode, C('SEND_MESSAGE.SET_PAY_PWD'));
            } elseif($action == \Consts::MSG_VAL_ACTION_ADD_MR) { // 添加短信接收人
                $shopName = '';
                if(isset($extra['shopCode']) && $extra['shopCode']){
                    $shopMdl = new ShopModel();
                    $shopInfo = $shopMdl->getOneShopInfo(array('shopCode' => $extra['shopCode']), array('shopName'));
                    if($shopInfo){
                        $shopName = $shopInfo['shopName'];
                    }
                }
                $message = str_replace(array('{{validateCode}}', '{{shopName}}'), array($validateCode, $shopName), C('SEND_MESSAGE.ADD_MR'));
            } else {
                $message = '';
            }
            $smsMdl->send($message, $mobileNbr);
            return array('code' => C('SUCCESS'), 'valCode' => S($action . $mobileNbr.'validateCode'));
        }catch (Exception $e){
            return $this->getBusinessCode(C('VALIDATE_CODE.SEND_FAIL'));
        }
    }

    /**
     * 过滤掉无用的筛选条件，主要是过滤掉值为空的选项。
     * 如果给定pager，从where条件中剥离出pager。
     * @param array $where 带过滤的where条件: array(字段名, 值);
     * @param null $assembles 组装where条件。格式: array(字段名, 组装方式). <br/>
     *      组装方式可以为：like, likeStart, likeEnd, neq, gt, egt, lt, elt. <br/>
     *      like: 组装为： '字段名 LIKE %{$where[字段名]}%' <br/>
     *      likeStart: 组装为： '字段名 LIKE %{$where[字段名]}' <br/>
     *      likeEnd: 组装为： '字段名 LIKE {$where[字段名]}%' <br/>
     *      neq: 组装为： '字段名 <> {$where[字段名]}' <br/>
     *      gt: 组装为： '字段名 > {$where[字段名]}' <br/>
     *      egt: 组装为： '字段名 >= {$where[字段名]}' <br/>
     *      lt: 组装为： '字段名 < {$where[字段名]}' <br/>
     *      elt: 组装为： '字段名 <= {$where[字段名]}'
     * @param Pager $pager 从where条件中剥离出pager
     * @return array
     */
    protected function filterWhere($where, $assembles = null, &$pager = null) {
        // 首先过滤掉值为空的字段，以及Pager的字段
        if ($where) {
            foreach ($where as $field => $val) {
                if (empty($where[$field]) && $val !== '0') {
                    unset($where[$field]);
                }
            }
        }

        // 从where条件中剥离出pager
        if ($pager) {
            if ($where['page']) {
                $pager->setPage($where['page']);
            }
            if ($where['pageSize']) {
                $pager->setPageSize($where['pageSize']);
            }
        }
        unset($where['page']);
        unset($where['pageSize']);

        // 处理where条件：like, likeStart, likeEnd, neq, gt, egt, lt, elt。将默认为“=”的条件换成给定的条件。
        $allowedAssembles = array('like', 'likeStart', 'likeEnd', 'neq', 'gt', 'egt', 'lt', 'elt', 'not in');
        if (!empty($where) && !empty($assembles)) {
            foreach ($assembles as $field => $assemble) {
                if ($where[$field] !== null && $where[$field] !== '') {
                    if (in_array($assemble, $allowedAssembles)) { // 检查是否允许
                        switch ($assemble) {
                            case 'like':
                                $where[$field] = array('like', "%{$where[$field]}%");
                                break;
                            case 'likeStart':
                                $where[$field] = array('like', "%{$where[$field]}");
                                break;
                            case 'likeEnd':
                                $where[$field] = array('like', "{$where[$field]}%");
                                break;
                            default: // 其他的将直接组装即可
                                $where[$field] = array($assemble, $where[$field]);
                                break;
                        }
                    } else {
                        throw_exception('不合法的where字段组装类型.');
                    }
                }
            }
        }
        return $where != null ? $where : array();
    }

    /**
     * 将给定数组中的空值过滤出去，只保留非空值
     * @param array $data 待过滤的的数组。
     * @param string $fields 如果给定，只过滤这个数组中的键；否则清除所有空值。
     * @param string $isKeepZero true：0不被当作空值；false：0将被当作空值清除。
     * @return Model 返回model本身
     */
    protected function filterEmpty(&$data, $fields = null, $isKeepZero = false) {
        foreach ($data as $key => $val) {
            if (empty($val) && (!$isKeepZero || $val != 0) && (!$fields || in_array($key, $fields))) {
                unset($data[$key]);
            }
        }
        return $this;
    }

    /**
     * 从where条件中剥离出pager
     * @param array $where
     * @param Pager $pager
     * @return Model 返回model本身
     */
    protected function filterPager($where, &$pager) {
        if ($where['page']) {
            $pager->setPage($where['page']);
        }
        if ($where['pageSize']) {
            $pager->setpageSize($where['pageSize']);
        }
        return $this;
    }

    /**
     * 设置分页
     * @param Pager $pager 分页Model
     * @return Model 返回model本身
     */
    protected function pager($pager) {
        if ($pager->getPage() > 0 && $pager->getPageSize() > 0) {
            $this->page($pager->getPage(), $pager->getPageSize());
        }
        return $this;
    }

    /**
     * 用户登录
     * @param number $mobileNbr 手机号码
     * @param string $password md5加密后的密码
     * @param number $loginType 登录类型。0-商家端；1-顾客端；2-管理端
     * @param string $registrationId
     * @return array
     */
    public function login($mobileNbr, $password,$bank_id,$loginType, $registrationId = '') {
        $rules = array(
            array('mobileNbr', 'require', C('MOBILE_NBR.EMPTY')),
            array('mobileNbr', 'number', C('MOBILE_NBR.ERROR')),
            array('mobileNbr', C('MOBILE_NBR.LENGTH'), C('MOBILE_NBR.ERROR'), 0, 'length'),
            array('password', 'require', C('PWD.EMPTY')),
            array("bank_id","require","所属商圈必须选择"),
//            array('registrationId', 'require', C('REGISTRATION_Id_EMPTY')),
        );

        $data = array(
            'mobileNbr' => $mobileNbr,
            'password'  => $password,
            'bank_id'   => $bank_id,
        );
        if ($this->validate($rules)->create($data)) {
            $user = null;
            if($loginType == C('LOGIN_TYPE.USER')) { // 顾客端登录
                $userMdl = new UserModel();
                $user = $userMdl->getUserInfo(array('mobileNbr' => $mobileNbr));
                $shopCode = '';
                $userCode = $user['userCode'];
                $userType = 'userCode';
                $userLvl = '';
            } elseif($loginType == C('LOGIN_TYPE.SHOP')) { // 商家端登陆

                $shopStaffMdl = new ShopStaffModel();
                $user = $shopStaffMdl->getShopStaffInfo(array('mobileNbr' => $mobileNbr,"bank_id"=>$bank_id));

                F("user",$user);

                // 如果商户员工的状态是未激活（-1）的话，返回错误
                if($user['status'] == \Consts::SHOP_STAFF_STATUS_NOT_ACTIVA) {
                    return $this->getBusinessCode(C('SHOP_STAFF.STAFF_NOT_ACTIVATE'));
                }
                $userCode = $user['staffCode'];
                // 店员随便获得一家自己所属的店
                $shopStaffRelMdl = new ShopStaffRelModel();
                $shopInfo = $shopStaffRelMdl->getShopInfoByStaffCode($user['staffCode'], array('Shop.shopCode'));

                $shopCode = $shopInfo['shopCode'] ? $shopInfo['shopCode'] : '';
                $userType = 'staffCode';
                $userLvl = $user['userLvl'];
            } elseif($loginType == C('LOGIN_TYPE.ADMIN')) { // 管理端登陆
                $bmStaffMdl = new BmStaffModel();
                $user = $bmStaffMdl->getBMStaffInfo(array('mobileNbr' => $mobileNbr,"bank_id"=>$bank_id));

                $userCode = $user['staffCode'];
                $userLvl = '';
            }
            //验证密码
          $password = md5(substr($userCode, 0, 6).$password);

            F("password",$password);

            if ($user && $password == $user['password'] && $user['status'] == C('USER_STATUS.ACTIVE')) { // 用户存在，且密码正确，且状态为启用状态
                // 验证通过,先清除密码信息.
                unset($user['password']);
                if($loginType == C('LOGIN_TYPE.ADMIN')) { // 管理端登陆
                    session('USER', $user);
                    return C('SUCCESS');
                } else {
                    $cJMdl = new JpushModel(\Consts::J_PUSH_APP_KEY_C, \Consts::J_PUSH_MASTER_SECRET_C);
                    $bJMdl = new JpushModel(\Consts::J_PUSH_APP_KEY_B, \Consts::J_PUSH_MASTER_SECRET_B);
                    if($loginType == C('LOGIN_TYPE.SHOP') && $user['userLvl'] != C('STAFF_LVL.EMPLOYEE') && $registrationId) { // 商家端登陆，且用户等级不是普通员工，且jpush的registrationId存在
                        $shopStaffRelMdl = new ShopStaffRelModel();
                        // 获得该商家员工所属的商户数组
                        $shopList = $shopStaffRelMdl->getShopListByStaffCode($user['staffCode'], array('Shop.shopCode'));
                        if($shopList) {
                            $bTags = array();
                            foreach($shopList as $sv){
                                $split = explode("-", $sv['shopCode']);
                                $bTags = array_unique(array_merge($bTags, $split));
                            }
                            // 设置标签
                            $bJMdl->setTags($registrationId, $bTags);
                        }
                    } elseif($loginType == C('LOGIN_TYPE.USER') && $registrationId) { // 顾客端登录，且jpush的registrationId存在
                        $cTags = array($mobileNbr);
                        // 设置标签
                        $cJMdl->setTags($registrationId, $cTags);
                    }

                    if($loginType == C('LOGIN_TYPE.USER')) {
                        $userLoginLogMdl = new UserLoginLogModel();
                        $userMdl = new UserModel();
                        // 增加顾客登录记录
                        $userLoginLogMdl->addUserLoginLog(array(
                            'logCode' => $this->create_uuid(),
                            'userId' => $userCode,
                            'loginTime' => date('Y-m-d H:i:s', time())
                        ));
                        // 更新顾客最后一次登录时间
                        $userMdl->updateLastLoginTime($userCode);
                    } else {
                        $shopStaffMdl = new ShopStaffModel();
                        // 更新商家员工最后一次登录时间
                        $shopStaffMdl->updateLastLoginTime($userCode);
                    }

                    $userTokenMdl = new UserTokenModel();
                    $userToken = $userTokenMdl->getUserToken($userCode);
                    if($userToken && $userToken['expireTime'] > time()) {
                        if($userToken['registrationId'] && $userToken['registrationId'] != $registrationId) {
                            if($loginType == C('LOGIN_TYPE.USER')) {
                                $tags_login = array($userToken['registrationId']);
                                $cJMdl->setTags($userToken['registrationId'], $tags_login);
                                $content = C('PUSH_MESSAGE.LOGIN_REPEAT');
                                $receiver = array($userToken['registrationId']);
                                $cJMdl->jPushByAction($receiver, $content, array('code'=>C('MOBILE_NBR.LOGIN_REPEAT'), 'loginTime'=>date('Y-m-d H:i', time())), C('PUSH_ACTION.LOGIN'));
                                $cJMdl->removeTags($userToken['registrationId']);
                            } else{
                                $bJMdl->removeTags($userToken['registrationId']);
                            }
                        }
                        if($userToken['registrationId'] != $registrationId){
                            $userTokenMdl->updateByUserTokenCode($userToken['userTokenCode'], array('registrationId'=>$registrationId));
                        }
                    } else {
                        $userTokenCode = $userTokenMdl->createToken($userCode, $registrationId);
                        $userToken = $userTokenMdl->getToken($userTokenCode);
                    }

                    // 返回
                    return array(
                        'code' => C('SUCCESS'),
                        'expiresAt' => $userToken['expireTime'],
                        'tokenCode' => $userToken['tokenCode'],
                        $userType => $userCode,
                        'shopCode' => $shopCode,
                        'userLvl' => $userLvl,
                    );
                }

            }else {
                $code = $user && $user['status'] != C('USER_STATUS.NOT_REG') ? ( $user['status'] == C('USER_STATUS.ACTIVE') ? C('PWD.WRONG') : C('MOBILE_NBR.DISABLE') ) : C('MOBILE_NBR.NO_REGISTER');
                return $this->getBusinessCode($code);
            }
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 修改密码
     * @param string $mobileNbr 手机号码
     * @param string $originalPwd 原密码
     * @param string $newPwd 新密码
     * @param number $type 用户类型。0-商店员工；1-顾客；2-苞米员工
     * @return array
     */
    public function updatePwd($mobileNbr, $originalPwd, $newPwd, $type){
        $rules = array(
            array('mobileNbr', 'require', C('MOBILE_NBR.EMPTY')),
            array('mobileNbr', 'number', C('MOBILE_NBR.ERROR')),
            array('mobileNbr', C('MOBILE_NBR.LENGTH'), C('MOBILE_NBR.ERROR'), 0, 'length'),
            array('originalPwd', 'require', C('PWD.ORIGINAL_PWD_ERROR')),
            array('newPwd', 'require', C('PWD.NEW_PWD_EMPTY')),
            array('newPwd', $originalPwd, C('PWD.PWD_SAME'), 0, 'notequal'),
            array('type', 'require', C('UPDATE_INFO.USER_ROLE_EMPTY')),
        );
        $data = array(
            'mobileNbr' => $mobileNbr,
            'originalPwd' => $originalPwd,
            'newPwd' => $newPwd,
            'type' => $type,
        );
        if($this->validate($rules)->create($data)) {
            if($type == C('LOGIN_TYPE.USER')) {
                $userMdl = new UserModel();
                $user = $userMdl->getUserInfo(array('mobileNbr' => $mobileNbr));
            }
            if($type == C('LOGIN_TYPE.SHOP')) {
                $shopStaffMdl = new ShopStaffModel();
                $user = $shopStaffMdl->getShopStaffInfo(array('mobileNbr' => $mobileNbr));
            }
            if($type == C('LOGIN_TYPE.ADMIN')) {
                $bmStaffMdl = new BmStaffModel();
                $user = $bmStaffMdl->getBMStaffInfo(array('mobileNbr' => $mobileNbr));
            }
            if($user) {
                if(isset($user['staffCode'])) {$userCode = $user['staffCode'];}
                if(isset($user['userCode'])) {$userCode = $user['userCode'];}
                $originalP = md5(substr($userCode, 0, 6).$originalPwd);
                $newP = md5(substr($userCode, 0, 6).$newPwd);
                if($originalP != $user['password']) {
                    return $this->getBusinessCode(C('PWD.ORIGINAL_PWD_ERROR'));
                }
                if($type == C('LOGIN_TYPE.USER')){
                    $userMdl = new UserModel();
                    $code = $userMdl->updateUserPwd($userCode, $newP);
                }
                if($type == C('LOGIN_TYPE.SHOP')){
                    $shopStaffMdl = new ShopStaffModel();
                    $code = $shopStaffMdl->updateShopStaffPwd($userCode, $newP);
                }
                if($type == C('LOGIN_TYPE.ADMIN')){
                    $bmStaffMdl = new BmStaffModel();
                    return $bmStaffMdl->updateBMStaffPwd($userCode, $newP);
                }
            } else {
                $code = C('MOBILE_NBR.NO_REGISTER');
            }
            return $this->getBusinessCode($code);
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 找回密码
     * @param string $mobileNbr 手机号码
     * @param string $validateCode 验证码
     * @param string $password 密码
     * @param number $type 用户类型。0-商店员工；1-顾客；2-苞米员工
     * @return array
     */
    public function findPwd($mobileNbr, $validateCode, $password, $type){
        $smsMdl = new SmsModel();
        $codeCom = $smsMdl->getCode('f'.$mobileNbr);
        $rules = array(
            array('mobileNbr', 'require', C('MOBILE_NBR.EMPTY')),
            array('mobileNbr', 'number', C('MOBILE_NBR.ERROR')),
            array('mobileNbr', C('MOBILE_NBR.LENGTH'), C('MOBILE_NBR.ERROR'), 0, 'length'),
            array('validateCode', 'require', C('VALIDATE_CODE.EMPTY')),
            array('password', 'require', C('PWD.EMPTY')),
            array('type', 'require', C('UPDATE_INFO.USER_ROLE_EMPTY')),
        );
        $data = array(
            'mobileNbr' => $mobileNbr,
            'validateCode' => $validateCode,
            'password' => $password,
            'type' => $type,
        );
        if($this->validate($rules)->create($data) != false){
            if($validateCode != $codeCom){
                return $this->getBusinessCode(C('VALIDATE_CODE.ERROR'));
            }

            // 判断这个号码是否已经注册过
            if($type == C('LOGIN_TYPE.USER')) {
                $userMdl = new UserModel();
                $user = $userMdl->getUserInfo(array('mobileNbr' => $mobileNbr));
            }
            if($type == C('LOGIN_TYPE.SHOP')) {
                $shopStaffMdl = new ShopStaffModel();
                $user = $shopStaffMdl->getShopStaffInfo(array('mobileNbr' => $mobileNbr));
            }
            if($type == C('LOGIN_TYPE.ADMIN')) {
                $bmStaffMdl = new BmStaffModel();
                $user = $bmStaffMdl->getBMStaffInfo(array('mobileNbr' => $mobileNbr));
            }
            if($user) {
                if(isset($user['staffCode'])) {
                    $userCode = $user['staffCode'];
                }elseif(isset($user['userCode'])) {
                    $userCode = $user['userCode'];
                }
                $tempPwd = md5(substr($userCode, 0, 6).$password);
                if($type == C('LOGIN_TYPE.USER')){
                    $userMdl = new UserModel();
                    $code = $userMdl->updateUserPwd($userCode, $tempPwd);
                }
                if($type == C('LOGIN_TYPE.SHOP')){
                    $shopStaffMdl = new ShopStaffModel();
                    $code = $shopStaffMdl->updateShopStaffPwd($userCode, $tempPwd);
                }
                if($type == C('LOGIN_TYPE.ADMIN')){
                    $bmStaffMdl = new BmStaffModel();
                    return $bmStaffMdl->updateBMStaffPwd($userCode, $tempPwd);
                }
            } else {
                $code = C('MOBILE_NBR.NO_REGISTER');
            }
            return $this->getBusinessCode($code);
        }else{
            return $this->getValidErrorCode();
        }
    }

    /**
     * 用户登出
     * @param string $tokenCode 令牌
     * @param string $appType
     * @param string $registrationId
     * @return array
     */
    public function logoff($tokenCode, $appType, $registrationId = ''){
        $userToken = new UserTokenModel();
        if(is_null($registrationId)){
            $oldRegistrationId = $userToken->getRegistrationId($tokenCode);
            if($oldRegistrationId){
                if($appType == C('LOGIN_TYPE.SHOP')){
                    $jpushMdl = new JpushModel(C('SHOP_APP_KEY'), C('SHOP_MASTER_SECRET'));
                    $jpushMdl->removeTags($oldRegistrationId);
                }elseif($appType == C('LOGIN_TYPE.USER')){
                    $jpushMdl = new JpushModel(C('CLIENT_APP_KEY'), C('CLIENT_MASTER_SECRET'));
                    $jpushMdl->removeTags($oldRegistrationId);
                }
            }
        }
        if($registrationId){
            if($appType == C('LOGIN_TYPE.SHOP')){
                $jpushMdl = new JpushModel(C('SHOP_APP_KEY'), C('SHOP_MASTER_SECRET'));
                $jpushMdl->removeTags($registrationId);
            }elseif($appType == C('LOGIN_TYPE.USER')){
                $jpushMdl = new JpushModel(C('CLIENT_APP_KEY'), C('CLIENT_MASTER_SECRET'));
                $jpushMdl->removeTags($registrationId);
            }
        }
        if($appType == C('LOGIN_TYPE.USER')){
            $code = $userToken->destroyToken($tokenCode);
        }
        return $this->getBusinessCode(isset($code)?$code:C('SUCCESS'));
    }

    /**
     * PHP生成 uuid 唯一标识码
     * @param string $prefix
     * @return string
     */
    public function create_uuid($prefix = ""){    //可以指定前缀
        $str = md5(uniqid(mt_rand(), true));
        $uuid  = substr($str,0,8) . '-';
        $uuid .= substr($str,8,4) . '-';
        $uuid .= substr($str,12,4) . '-';
        $uuid .= substr($str,16,4) . '-';
        $uuid .= substr($str,20,12);
        return $prefix.$uuid;
    }

    /**
     * 返回验证的错误代码
     * @return array
     */
    protected function getValidErrorCode() {
        return array('code' => $this->getError());
    }

    /**
     * 返回业务逻辑的代码
     * @param number $code 代码
     * @return array
     */
    protected function getBusinessCode($code) {
        return array('code' => $code);
    }

    /**
     * 将分转为元
     * @param array $data 一维数组
     * @param array $target 由要操作的键值组成的数组
     * @return array
     */
    protected function dividedByHundred($data, $target) {
        foreach($target as $v) {
            if(isset($data[$v])) {
                $data[$v] = $data[$v] / C('RATIO');
            }
        }
        return $data;
    }

    /**
     * 将元转为分
     * @param array $data 一维数组
     * @param array $target 由要操作的键值组成的数组
     * @return array
     */
    protected function byHundred($data, $target) {
        foreach($target as $v) {
            if(isset($data[$v])) {
                $data[$v] = $data[$v] * C('RATIO');
            }
        }
        return $data;
    }

    /**
     * 转化日期格式
     * @param string $datetime 日期，格式：xxxx-xx-xx xx:xx:xx
     * @return string 格式化后的日期，格式：xxxx.xx.xx 或者 空字符串
     */
    protected function formatDate1($datetime) {
        $zeroDate = '';
        if($datetime == '0000-00-00 00:00:00') {
            return $zeroDate;
        }
        $time = strtotime($datetime);
        if($time !== false) {
            return date('Y.m.d', $time);
        } else {
            return $zeroDate;
        }
    }

    /**
     * 格式化城市， 去掉“市”
     * @param string $city 城市
     * @return string $city
     */
    protected function formatCity($city) {
        $city = mb_substr($city, 0, mb_strlen($city - 1), "utf-8");
        return $city;
    }

    /**
     * 判断商家当前时间是否营业
     * @param $timeData
     * @return bool
     */
    public function isTimeMeet($timeData){
        if(empty($timeData)){
            return false;
        }
        foreach($timeData as $v){
            if(strtotime($v['open']) <= time() && strtotime($v['close']) >= time()){
                return true;
            }
        }
        return false;
    }

    /**
     * 单表查询
     * @param string $tableName 表名称
     * @param array $condition 条件 一维关联数组
     * @param array $field 字段值 一维索引数组
     * @return array 一维关联数组
     */
    public function getTableInfo($tableName, $condition, $field) {
        return $this->table($tableName)->field($field)->where($condition)->find();
    }

    /**
     * 获得某张表中某个字段的值，单表查询
     * @param string $tableName 表名称
     * @param array $condition 条件 一维关联数组
     * @param string $field 字段值
     * @return mixed
     */
    public function getFieldInfo($tableName, $condition, $field) {
        return $this->table($tableName)->where($condition)->getField($field);
    }

}

class RPCException extends Exception{
    public $code = 0;
    public $message = "";
    public $data = "";

    public function __construct($msg, $code, $data){
        $this->code = $code;
        $this->message = $msg;
        $this->data = $data;
    }
}