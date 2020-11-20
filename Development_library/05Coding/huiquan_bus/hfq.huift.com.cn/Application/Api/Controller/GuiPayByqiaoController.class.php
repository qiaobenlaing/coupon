<?php
/**
 * Created by PhpStorm.
 * User: 98041
 * Date: 2019/7/3
 * Time: 15:52
 */

namespace Api\Controller;

use Common\Model\UserModel;

/**
 * 乔本亮 2019-07-03
 * Class GuiPayByqiaoController.class
 * @package Api\Controller
 */
class GuiPayByqiaoController extends ApiBaseController
{
    /** 判断用户是否签约
     * @param $userCode 用户编号
     * @return array 结果数组
     */
    public function hasSign($userCode){
            $userModel = new UserModel();
            $userInfo = $userModel->getUserInfo(array('userCode'=>$userCode),array('sign'));
            if($userInfo){
                $code = 50000;
            }else{
                $code =20000;
            }

            if($userInfo['sign']==1){
                $data[0] = array(
                    'signBank'=> array('name'=>'贵州银行','img'=>'https://'.$_SERVER['HTTP_HOST'].'/huiquan/static/img/guizhoubanklogo.png'),
                    'sign'=> $userInfo['sign'], //签约状态码
                    'BkAcctNo'=> '6214***********4549',
                    'signTime'=>'2019-07-03 09-18',
                    'signType'=>'贵州银行网上支付平台',
                    'SgnNo'=>'2921838481dfdkfidj',
                    'signStatus'=>'已签约，贵州银行代扣',
                );
            }else{
                $data[0] = array(
                    'signBank'=> array('name'=>'贵州银行','img'=>'https://'.$_SERVER['HTTP_HOST'].'/huiquan/static/img/guizhoubanklogo.png'),
                    'sign'=> $userInfo['sign'], //签约状态码
                );
            }


            return array('data'=>$data,'code'=>$code);
    }

    /** 短信签约申请
     * @param $realName 真实姓名
     * @param $mobileBank 银行手机号
     * @param $idCard 身份证号
     * @param $BkAcctNo 银行卡号
     * @return array 申请结果返回
     */
    public function applySign($realName,$mobileBank,$idCard,$BkAcctNo){
        $code = 50000;
        $data = array(
            'code'=>$code,
            'SgnNo'=> '2921838481dfdkfidj', //签约协议号
        );
        return $data;
    }

    /** 短信签约确认
     * @param $realName 用户真实姓名
     * @param $mobileBank 银行预留手机号
     * @param $idCard 身份证号
     * @param $BkAcctNo 银行卡号
     * @param $msgCode 短信验证码
     * @return array 用户签约结果
     */
    public function confirmSign($realName,$mobileBank,$idCard,$BkAcctNo,$msgCode){

        $code = 50000;
        $data = array(
            'code'=>$code,
            'BkAcctNo'=> '6214***********4549',
            'signTime'=>'2019-07-03 09-18',
            'signType'=>'贵州银行网上支付平台',
            'SgnNo'=>'2921838481dfdkfidj',
            'signStatus'=>'已签约，贵州银行代扣',
        );
        return $data;
    }

    /** 解约申请
     * @param $SgnNo 协议号
     * @return array 解约结果
     */
    public function cancelSign($SgnNo){
        $code = 50000;
        $data = array(
            'code'=>$code,
            'sign'=> 4, //解约申请中
        );
        return $data;
    }



}
