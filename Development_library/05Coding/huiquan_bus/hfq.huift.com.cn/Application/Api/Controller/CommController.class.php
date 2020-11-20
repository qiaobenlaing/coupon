<?php
/**
 * 公共API Controller
 *
 * User: Weiping
 * Date: 2015-04-20
 * Time: 23:49
 */
namespace Api\Controller;
use Common\Model\CrashLogModel;
use Common\Model\IosTokenModel;
use Common\Model\MessageRecipientModel;
use Common\Model\ShopModel;
use Common\Model\ShopStaffModel;
use Common\Model\ShopStaffRelModel;
use Common\Model\SystemParamModel;
use Think\Exception;
use Common\Model\BaseModel;
use Common\Model\SmsModel;
use Common\Model\FeedbackModel;
use Common\Model\JPushRegLogModel;
use Common\Model\JpushModel;
use Common\Model\UserConsumeModel;
use Common\Model\UserModel;
class CommController extends ApiBaseController {

	/**
	 * 测试一般jsonrpc
	 * @return string
	 */
	public function index() {
		return 'Hello, world';
	}

	/**
	 * 测试支持参数传入的jsonrpc
	 * @param string $name 名字
	 * @return string 测试字符串
	 */
	public function tt($name = '') {
		return "Hello, {$name}!";
	}

    /**
     * register (H5)
     * @param $mobileNbr
     * @return array
     */
    public function getValCode($mobileNbr){
        $bMdl = new BaseModel();
        return $bMdl->getValidateCode($mobileNbr, \Consts::MSG_VAL_ACTION_REG);
    }

    /**
     * 发送验证码
     * @param number $mobileNbr 手机号码
     * @param string $action 动作。r：注册或激活，f：找回密码, mr：添加短信接受人
     * @param string $appType app类型：s-商家端；c-顾客段
     * @param string $extra 额外参数
     * @return array
     */
    public function getValidateCode($mobileNbr, $action, $appType, $extra = '') {
        if($extra){
            $extra = str_replace('\"','"',$extra);
            $extra = json_decode($extra, true);
        }

        // 检查手机号码是否有值
        if(empty($mobileNbr)) return array('code' => C('MOBILE_NBR.EMPTY'));
        // 检查手机号码格式
        if(!is_numeric($mobileNbr) || strlen($mobileNbr) != C('MOBILE_NBR.LENGTH')) return array('code' => C('MOBILE_NBR.ERROR'));
        // 检查action是否有值
        if(empty($action)) return array('code' => C('VALIDATE_CODE.ACTION_ERROR'));
        // 检查appType是否有值
        if(empty($appType)) return array('code' => C('VALIDATE_CODE.APP_TYPE_ERROR'));

        $isRegistered = false;
        if($action == \Consts::MSG_VAL_ACTION_REG) { // 注册时，获取验证码
            if($appType == C('APP_TYPE.SHOP')) { // B端获取验证码
                $shopStaffMdl = new ShopStaffModel();
                $shopStaffInfo = $shopStaffMdl->getShopStaffInfo(array('mobileNbr' => $mobileNbr));
                // 判断商户员工是否存在
                if(empty($shopStaffInfo)) return array('code' => C('ACTIVATE.NOT_COMMIT'));
                // 判断商户员工状态
                if($shopStaffInfo['status'] == C('STAFF_STATUS.ACTIVE')) return array('code' =>  C('ACTIVATE.PASSED'));
            } else {
                $userMdl = new UserModel();
                // 根据手机号码获取已经注册的用户信息，判断手机号码是否已经注册
                $userInfo = $userMdl->getRegedUserInfo($mobileNbr);
                $isRegistered = $userInfo ? true : false;
            }
            // 判断用户是否已经注册
            if($isRegistered) {
                return array('code' => C('MOBILE_NBR.REPEAT'));
            }
        } elseif($action == \Consts::MSG_VAL_ACTION_ADD_MR){ //添加短信接受人
            $mrMdl = new MessageRecipientModel();
            $mrInfo = $mrMdl->getMRecipientInfo(array('shopCode' => $extra['shopCode'], 'mobileNbr' => $mobileNbr));
            if($mrInfo){
                return array('code' => C('MOBILE_NBR.REPEAT'));
            }
            $shopStaffRelMdl = new ShopStaffRelModel();
            $shopStaff = $shopStaffRelMdl->getStaffList(array('ShopStaffRel.shopCode' => $extra['shopCode'], 'ShopStaff.status' => \Consts::SHOP_STAFF_STATUS_ACTIVE, 'ShopStaff.userLvl' => array('gt', \Consts::SHOP_STAFF_LVL_EMPLOYEE), 'ShopStaff.isSendPayedMsg' => C('YES'), 'ShopStaff.mobileNbr' => $mobileNbr), array('ShopStaff.staffCode'));
            if($shopStaff){
                return array('code' => C('MOBILE_NBR.REPEAT'));
            }
        } else { // 用户，员工找回密码；员工激活。获取验证码
            if($appType == C('APP_TYPE.SHOP')) {
                $shopStaffMdl = new ShopStaffModel();
                $info = $shopStaffMdl->getShopStaffInfo(array('mobileNbr' => $mobileNbr));
            }else{
                $userMdl = new UserModel();
                $info = $userMdl->getUserInfo(array('mobileNbr' => $mobileNbr));
            }
            // 判断用户或者员工是否存在
            if(empty($info)) {
                return array('code' =>  C('MOBILE_NBR.NO_REGISTER'));
            }
        }
        // 获得并发送验证码
        $baseMdl = new BaseModel();
        $ret = $baseMdl->getValidateCode($mobileNbr, $action, $extra);
        return $ret;
    }

    /**
     * 登录验证
     * @param number $mobileNbr 11位手机号码
     * @param string $password md5加密后的密码
     * @param int $loginType 登录类型。0-商家段；1-顾客端
     * @param string $registrationId
     * @return array $result
     */
    public function login($mobileNbr, $password, $bank_id,$loginType, $registrationId = '') {
        $baseMdl = new BaseModel();
        // $registrationId为null时会报错，数据表registrationId不能为null
        $registrationId = empty($registrationId) ? '' : $registrationId;
        $result = $baseMdl->login($mobileNbr, $password,$bank_id,$loginType, $registrationId);
        return $result;
    }

    /**
     * 退出登录
     * @param string $tokenCode 令牌
     * @param int $appType app类型 0-商家段；1-顾客端
     * @param string $registrationId
     * @return array
     */
    public function logoff($tokenCode, $appType, $registrationId = '') {
        $baseMdl = new BaseModel();
        $ret = $baseMdl->logoff($tokenCode, $appType, $registrationId);
        return $ret;
    }

    /**
     * 修改密码
     * @param string $mobileNbr 11位手机号码
     * @param string $originalPwd 原密码
     * @param string $newPwd 新密码
     * @param number $type 用户类型。1-顾客；0-商店员工
     * @return array $ret
     */
    public function updatePwd($mobileNbr, $originalPwd, $newPwd, $type) {
        $baseMdl = new BaseModel();
        $ret = $baseMdl->updatePwd($mobileNbr, $originalPwd, $newPwd, $type);
        return $ret;
    }


    //获取商圈信息
    public  function getZone(){
           $zone =  D("Bank")->where(array("status"=>1,"parent"=>array("not IN","-1")))->field("name,id")->select();

           if(!$zone){
               return $this->getBusinessCode("并没找到商圈信息");
           }

           $info['zone'] = $zone;
           $info['code'] = "50000";
        return $info;
    }

    /**
     * 找回密码
     * @param string $mobileNbr 11位手机号码
     * @param number $validateCode 6位的验证码
     * @param string $password 密码，长度6到20位
     * @param number $type 用户类型。1-顾客；2-商店员工；3-苞米员工
     * @return array $ret
     */
    public function findPwd($mobileNbr, $validateCode, $password, $type) {
        $baseMdl = new BaseModel();
        $ret = $baseMdl->findPwd($mobileNbr, $validateCode, $password, $type);
        return $ret;
    }

    /**
     * 提交反馈
     * @param string $creatorCode 反馈者编码
     * @param string $content 反馈内容
     * @param string $targetCode 目标指向（用户或商家或系统）
     * @return array $ret
     */
    public function addFeedback($creatorCode, $content, $targetCode) {
        $feedbackMdl = new FeedbackModel();
        $ret = $feedbackMdl->addFeedback($creatorCode, $content, $targetCode);
        return $ret;
    }

    /**
     * 添加程序崩溃日志
     * @param string $url 文件URL
     * @param string $userCode 用户编码
     * @param string $eqpCode 设备编码
     * @param int $appType app类型，0-商家端；1-顾客端
     * @return array {'code'}
     */
    public function addCrashLog($url, $userCode, $eqpCode, $appType) {
        $crashLogMdl = new CrashLogModel();
        $ret = $crashLogMdl->addCrashLog(array('url' => $url, 'userCode' => $userCode, 'equipmentCode' => $eqpCode, 'createTime' => date('Y-m-d H:i:s'), 'appType' => $appType));
        return $ret;
    }

    /**
     * 上传图片接口
     * @return array 成功返回URL，失败返回错误代码
     */
    public function uploadImg($config) {
		
        $config = array(
            'mimes'         =>  array(), //允许上传的文件MiMe类型
            'maxSize'       =>  5242880, //上传的文件大小限制(以字节为单位)(0-不做限制)5M
            'exts'          =>  array('jpg', 'gif', 'png', 'jpeg'), //允许上传的文件后缀
            'autoSub'       =>  true, //自动子目录保存文件
            'subName'       =>  array('date', 'Ymd'), //子目录创建方式，[0]-函数名，[1]-参数，多个参数使用数组
            'rootPath'      =>  '',
            'savePath'      =>  './Public/Uploads/', //保存路径
            'saveName'      =>  array('uniqid', ''), //上传文件命名规则，[0]-函数名，[1]-参数，多个参数使用数组
        );
        $upload = new \Think\Upload($config);// 实例化上传类
        // 上传文件
        \Think\Log::write('IMG_INFO:' . json_encode($_FILES));
        $info = $upload->upload();
		
        if(!$info) {
            // 上传错误提示错误信息
			
            echo json_encode(array('code' => $upload->getError()));
        } else {  // 上传成功，获取上传文件信息
			
                $url = array();
                foreach($info as $file) {
                    $url[] = substr($file['savepath'], 1).$file['savename'];
                }
            echo json_encode(array('code' => $url[0]));
        }
    }

    /**
     * 上传错误日志
     */
    public function uploadCrashLog() {
        $config = array(
            'mimes'         =>  array(), //允许上传的文件MiMe类型
            'maxSize'       =>  5242880, //上传的文件大小限制(以字节为单位)(0-不做限制)5M
            'exts'          =>  array('txt', 'log'), //允许上传的文件后缀
            'autoSub'       =>  true, //自动子目录保存文件
            'subName'       =>  array('date', 'Ymd'), //子目录创建方式，[0]-函数名，[1]-参数，多个参数使用数组
            'rootPath'      =>  '',
            'savePath'      =>  './Public/CrashLog/', //保存路径
            'saveName'      =>  array('uniqid', ''), //上传文件命名规则，[0]-函数名，[1]-参数，多个参数使用数组
        );
        $upload = new \Think\Upload($config);// 实例化上传类
        // 上传文件
        $info = $upload->upload();
        if(!$info) {
            // 上传错误提示错误信息
            echo json_encode(array('code' => $upload->getError()));
        } else {  // 上传成功，获取上传文件信息
            $url = array();
            foreach($info as $file) {
                $url[] = substr($file['savepath'], 1).$file['savename'];
            }
            echo json_encode(array('code' => $url[0]));
        }
    }

    /**
     * 支付结果
     * @param string $consumeCode 支付编码
     * @param string $serialNbr  流水号
     * @param string $result 支付结果。SUCCESS:成功；FAIL:失败
     * @return array
     */
    public function setPayResult($consumeCode, $serialNbr, $result){
        $ucMdl = new UserConsumeModel();
        return $ucMdl->setPayResult($consumeCode, $serialNbr, $result);
    }

    /**
     * 取消支付
     * @param $consumeCode
     * @return array
     */
    public function cancelPay($consumeCode){
        $ucMdl = new UserConsumeModel();
        return $ucMdl->cancelPay($consumeCode);
    }

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
    public function addRegistrationId($device, $regId, $appType) {
        $jPushRegLogMdl = new JPushRegLogModel();
        $jPushRegLogMdl->addRegId($device, $regId, $appType);
    }

    /**
     * IOS记录应用的token
     * @param string $mobileNbr 手机号码
     * @param string $token token（72位）
     * @param int $appType 应用类型。0-商家端；1-顾客端；
     * @return array
     */
    public function editIosToken($mobileNbr, $token, $appType) {
        $data = array(
            'mobileNbr' => $mobileNbr,
            'token' => $token,
            'appType' => $appType
        );
        $iosTokenMdl = new IosTokenModel();
        $ret = $iosTokenMdl->editIosToken($data);
        return $ret;
    }

    /**
     * 获得服务器时间
     * @return int
     */
    public function getServerTime() {
        return array('time' => time());
    }


    /**
     * 获取引导页
     * @param int $appType app类型 0-商家段；1-顾客端
     * @return array
     */
    public function getGuideInfo($appType){
        if($appType == 1){
            $appType = 'c';
            $param = 'guidePageForClient';
        }else{
            $appType = 's';
            $param = 'guidePageForShop';
        }
        $systemParamMdl = new SystemParamModel();
        return $systemParamMdl->getSysParamInfo(array('appType' => $appType, 'param' => $param));
    }
} 