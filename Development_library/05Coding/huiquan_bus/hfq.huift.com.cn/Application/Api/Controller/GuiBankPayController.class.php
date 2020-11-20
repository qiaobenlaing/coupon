<?php
/**
 * 收藏商家列表
 */
/**
 * 商店端API Controller
 * User: Weiping
 * Date: 2015-04-20
 * Time: 23:49
 */
namespace Api\Controller;
use Common\Model\BankAccountModel;
use Common\Model\BaseModel;
use Common\Model\ClassRemarkImgModel;
use Common\Model\ClassRemarkModel;
use Common\Model\ClassWeekInfoModel;
use Common\Model\ConsumeOrderModel;
use Common\Model\CouponRuleModel;
use Common\Model\GuiBankPayModel;
use Common\Model\MessageRecipientModel;
use Common\Model\OrderCouponModel;
use Common\Model\OrderProductModel;
use Common\Model\PcAppLogModel;
use Common\Model\PPRelModel;
use Common\Model\ProductCategoryModel;
use Common\Model\ProductModel;
use Common\Model\RefundLogModel;
use Common\Model\ShopAppLogModel;
use Common\Model\ShopApplyEntryModel;
use Common\Model\ShopClassModel;
use Common\Model\ShopDecorationModel;
use Common\Model\BatchCouponModel;
use Common\Model\ShopDeliveryModel;
use Common\Model\ShopHeaderModel;
use Common\Model\ShopHonorModel;
use Common\Model\ShopPhotoModel;
use Common\Model\ShopRecruitModel;
use Common\Model\ShopStaffRelModel;
use Common\Model\ShopTeacherModel;
use Common\Model\StudentStarModel;
use Common\Model\StuStarWorkModel;
use Common\Model\SubAlbumModel;
use Common\Model\SystemParamModel;
use Common\Model\TeacherWorkModel;
use Common\Model\UserActCodeModel;
use Common\Model\UserActivityModel;
use Common\Model\FeedbackModel;
use Common\Model\UserBonusModel;
use Common\Model\UserCardModel;
use Common\Model\ShopStaffModel;
use Common\Model\ShopModel;
use Common\Model\BankModel;
use Common\Model\BankAccountLocalLogModel;
use Common\Model\CardModel;
use Common\Model\UserCouponModel;
use Common\Model\BonusModel;
use Common\Model\ActivityModel;
use Common\Model\UserConsumeModel;
use Common\Model\CommunicationModel;
use Common\Model\UserMessageModel;
use Common\Model\PosServerModel;
use Common\Model\JpushModel;
use Common\Model\MessageModel;
use Common\Model\BonusStatisticsModel;
use Common\Model\UserModel;
use Common\Model\UtilsModel;
use JPush\Exception\APIRequestException;
use Common\Model\UserEnterShopInfoRecordModel;
use \Think\Cache\Driver\Memcache;
use  Org\aliyun\api_demo\WxPayData;
use Admin\Controller\OpenIdController;

class GuiBankPayController extends ApiBaseController
{
    /*
     * 获取调用微信扫一扫的js必要数据
     * */
    public function getWeChatJsSdkData($url){
        vendor('WeChatSdk.jssdk');
        $jssdk = new \JSSDK();
        $OpenIdObj = new OpenIdController();
        $res = $OpenIdObj->base();
        $access_token = json_decode($res)->access_token;
        $signPackage = $jssdk->GetSignPackage($url,$access_token);
        return array(
            'appId' => C('AppID'),
            'timestamp' => $signPackage["timestamp"],
            'nonceStr' => $signPackage["nonceStr"],
            'signature' => $signPackage["signature"],
            );
    }
    /*扫描二维码后获取商家信息，优惠金额
     *
     * */
    public function getPayInfo($shopCode){
        //判断商户状态及信息是否真实且已开通贵行卡
        if(!$shopCode)
            return array('status' => 'error','msg' => '数据错误','shopInfo' => array());
        $condition['shopCode'] = $shopCode;
        $condition['status'] = 1;//店铺已启用
        $condition['shopStatus'] = 2;//店铺已入驻
        $condition['isAcceptBankCard'] = 1;//支持银行卡
        $shopMdl = new ShopModel();
        $shopInfo = $shopMdl->getOneShopInfo($condition,array('shopName'));
        if(!$shopInfo)
            return array('status' => 'error','msg' => '店铺不存在或店铺暂不支持银行卡支付','shopInfo' => array());
        //获取贵行卡消费的优惠额度
        $bankMdl = new BankModel();
        $bankCardDiscount = $bankMdl->getBankDiscount($bankId=2);
        $shopInfo['bankDiscount'] = $bankCardDiscount['discount']/100;//分化元
        return array('status' => 'success','msg' => '数据获取成功','shopInfo' => $shopInfo);
    }
    /*
     * 接收支付数据生成订单，调用贵行预支付接口完成预支付
     * $payAmount,支付总价
     * $realPay 实际付款
     * $userCode 用户code
     * $shopCode 商户code
     * $payType 支付类型（正常支付/免验证码支付）
     * $couponNbr 支付使用的优惠券，默认不使用优惠券
     * */
    public function makeOrderAndPay($payAmount,$realPay,$userCode,$shopCode,$payType='normalPay'){
        if(empty($payAmount) || empty($realPay) || empty($userCode) || empty($shopCode))
            return array('status' => 'error','msg' => '参数错误');
        //获取贵行卡消费的优惠额度
        $bankMdl = new BankModel();
        $bankCardDiscount = $bankMdl->getBankDiscount($bankId=2);

        $realPay = $realPay * 100;//元化分
        $payAmount = $payAmount * 100;//元化分
        $pay = $payAmount - $bankCardDiscount['discount'];

        if(strval($pay) != strval($realPay))
            return array('status' => 'error','msg' => '参数错误');

        //添加订单
        $consumeOrderMdl = new ConsumeOrderModel();
        $orderRet = $consumeOrderMdl->addConsumeOrder($userCode,$payAmount,$shopCode, $payType = 0, $orderType = 50);
        if($orderRet['code'] !== C('SUCCESS'))
            return $orderRet;
        $orderCode = $orderRet['orderCode']; // 订单编码
        $orderNbr = $orderRet['orderNbr']; // 订单号
        $userConsumeMdl = new UserConsumeModel();
        // 生成在线支付记录
        $consumeRet = $userConsumeMdl->bankCardPayForGz($orderCode,$realPay);
        if($consumeRet['code'] !== C('SUCCESS'))
            return $consumeRet;
        //支付必须数据（支付额度，订单号等）
        //获取用户银行签约协议号等信息，获取商家银行账号等信息
        $UserInfo = $this->getUserPayInfo($orderNbr);
        $TrxAmt = $realPay;//交易金额
        $TrxCcyCd = '';//货币代码
        $TrxTp = 1000;//消费类型
        $SgnNo = $UserInfo['SgnNo'];//签约协议号
        $PyerNm = $UserInfo['PyerNm'];//付款人名称
        $PyerAcctId = $UserInfo['PyerAcctId'];//付款方账号（协议号）
        $PyerAcctTp = $UserInfo['PyerAcctTp'];//银行账号类型
        $PyerAcctIssrId = $UserInfo['PyerAcctIssrId'];//付款方账户所属机构标识

        //获取商户收款账号信息
        $shopInfo = $this->getShopAccountInfo($shopCode);
        $MrchntNm = $shopInfo['MrchntNm'];//商户名称
        $MrchntNo = $shopInfo['MrchntNo'];//商户编码
        $MrchntTp = $shopInfo['MrchntTp'];//商户类型
        $MrchntCertTp = $shopInfo['MrchntCertTp'];//商户证件类型
        $MrchntCertId = $shopInfo['MrchntCertId'];//商户证件编码
        $MrchntCtgyCd = $shopInfo['MrchntCtgyCd'];//商户行业类别
        $OrdrId = $shopInfo['OrdrId'];//订单编码
        $OrdrDesc = $shopInfo['OrdrDesc'];//订单详情

        if($payType == 'freePay'){
            //调取免验支付接口直接支付
            $PayReturnInfo = array(
                'SysTtnCd' => 000000,
                'BkTrxId' => date('Ymd His').time(),
            );//免验支付返回结果
            if($PayReturnInfo['SysRtnCd'] != 000000)
                return false;
            M('userconsume')->where(array('consumeCode' => $consumeRet['consumeCode']))->setField(array('status' => 3,'serialNbr' => $PayReturnInfo['BkTrxId']));
            M('consumeorder')->where(array('orderCode' => $orderCode))->setField('status',3);
            return array('status' => 'success','msg' => '支付成功');
        }else{
            //调取协议支付接口完成预支付
            $PayReturnInfo = array(//正常支付返回结果(测试数据）
                'SysTtnCd' => 000000,
                'SmsSeqNo' => 12312,
                'PyerAcctId' => '6111222445532178',
                'MobNo' => 13786541232,
                'SgnNo' => 987854665456,
                'TrxAmt' => 300,
                'BkTrxId' => date('Ymd His').time(),
            );
            if($PayReturnInfo['SysRtnCd'] != 000000)
                return false;
            $BkTrxId = $PayReturnInfo['BkTrxId'];//银行流水号
            //绑定银行流水号与订单号
            $this->bindNbr($BkTrxId,$consumeRet['consumeCode']);

            $SmsSeqNo = $PayReturnInfo['SmsSeqNo'];//短信验证码序号,贵州银行账号才有，其他银行账号无
            $PyerAcctId = $PayReturnInfo['PyerAcctId'];//付款账户
            $MobNo = $PayReturnInfo['MobNo'];//手机号码
            $SgnNo = $PayReturnInfo['SgnNo'];//签约协议号
            $TrxAmt = $PayReturnInfo['TrxAmt'];//交易金额
            //$TrxCcyCd = $PayReturnInfo['TrxCcyCd'];//交易货币代码
            //$BkSttlDt = $PayReturnInfo['BkSttlDt'];//银行清算日期
            //$SysRtnDesc = $PayReturnInfo['SysRtnDesc'];//系统返回说明
            S($orderNbr,serialize(compact($SmsSeqNo,$TrxAmt,$PyerAcctId,$MobNo,$SgnNo)),600);
            return array('status' => 'success','msg' => '预支付成功，请接收贵行验证码完成支付','orderNbr' => $orderNbr);
        }
    }
    /*
     * 接收验证码，完成确认支付
     * */
    public function makeSurePay($checkCode,$orderNbr){
        if(empty($checkCode) || empty($orderNbr))
            return array('status' => 'error','msg' => '参数错误');
        if(!S('userPayInfo'))
            return array('status' => 'error','msg' => '支付错误');
        $userPayInfo = unserialize(S($orderNbr));//短信验证码序号,交易金额，付款账户（敏感信息），手机号码（敏感信息），签约协议号
        S($orderNbr,'');
        //调取银行支付验证结果完成支付
        $userPayInfo['SmsValidationCode'] = $checkCode;
        $userPayInfo['SmsSeqNo'] = '';//短信验证码序号,贵州银行用户必须上送，其他银行用户无

        //接收接口信息，根据付款完成情况更改订单支付记录等状态
        $mkSurePayReturnInfo = array(//支付完成返回结果，临时测试数据
            'SysRtnCd' => 000000,
            'BkTrxId' => 2019070513308965484,
        );
        if($mkSurePayReturnInfo['SysRtnCd'] !== 000000)
            return false;
        $BkTrxId = $mkSurePayReturnInfo['BkTrxId'];//银行流水号
        //回调更改订单状态
        $this->changeStatus($orderNbr);
        return array('status' => 'success','msg' => '支付成功');
        //$SgnNo = $mkSurePayReturnInfo['SgnNo'];//签约协议号
        //$TrxAmt = $mkSurePayReturnInfo['TrxAmt'];//交易金额
        //$TrxCcyCd = $mkSurePayReturnInfo['TrxCcyCd'];//交易货币代码
        //$BkSttlDt = $mkSurePayReturnInfo['BkSttlDt'];//银行清算日期
        //$SysRtnDesc = $mkSurePayReturnInfo['SysRtnDesc'];//系统返回说明
    }
    //获取用户支付信息
    private function getUserPayInfo($userCode){
        $userPayInfo = '';
        return $userPayInfo;
    }
    //获取商铺账号信息
    private function getShopAccountInfo($shopCode){
        $shopAccountInfo = '';
        return $shopAccountInfo ;
    }
    //付款成功更改订单消费记录状态
    private function changeStatus($orderNbr){
        $orderCode = M('consumeorder')->where(array('orderNbr' => $orderNbr))->getField('orderCode');
        $consumeRes = M('userconsume')->where(array('orderCode' => $orderCode))->setField('status',3);
        $orderRes = M('consumeorder')->where(array('orderNbr' => $orderNbr))->setField('status',3);
    }
    //绑定银行流水号和订单号
    private function bindNbr($Nbr,$consumeCode){
        $res = M('userconsume')->where(array('consumeCode' => $consumeCode))->setField('serialNbr',$Nbr);
        return $res;
    }
    //取消支付
    public function cancelPay($userCode,$orderNbr){
        if(empty($userCode) || empty($orderNbr))
            return array('status' => 'error','msg' => '参数错误');
        $orderCode = M('consumeorder')->where(array('clientCode' => $userCode,'orderNbr' => $orderNbr))->getField('orderCode');
        M('consumeorder')->where(array('clientCode' => $userCode,'orderNbr' => $orderNbr))->setField('status', 4);
        M('userconsume')->where(array('orderCode' => $orderCode))->setField('status', 4);
        return array('status' => 'success','msg' => '取消成功');
    }
}
