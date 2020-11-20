<?php
/**
 * Created by PhpStorm.
 * User: Administrator
 * Date: 16-3-17
 * Time: 下午6:05
 */

namespace Home\Controller;


use Think\Controller;

class RechargeController extends Controller{

    private $appKey = 'f1fa8cb54265d4a41aa4e19060b279f2'; //从聚合申请的话费充值appkey
    private $openId = 'JHc98a2589965fa2469405fde2bb1a30c1'; //注册聚合账号就会分配的openid
    private $recharge;

    /**
     * 架构函数
     * @access public
     */
    public function __construct() {
        parent::__construct();
        header('Content-type:text/html;charset=utf-8');
        //导入类库
        Vendor("recharge.recharge");
        $this->recharge = new \recharge($this->appKey,$this->openId);
    }

    /**
     * 检测手机号码以及面额是否可以充值
     * @param string $mobileNbr 充值手机号
     * @param int $price 可以选择的面额 5、10、20、30、50、100、300...
     * @return bool
     */
    public function checkSupport($mobileNbr, $price){
        $telCheckRes = $this->recharge->telcheck($mobileNbr, $price);
        if($telCheckRes){
            //支持充值
            return true;
        }else{
            //暂不支持充值
            return false;
        }
    }

    /**
     * 根据手机号码以及面额查询商品信息
     * @param string $mobileNbr 充值手机号
     * @param int $price
     * @return array
     */
    public function getPriceInfo($mobileNbr, $price){
        $telQueryRes = $this->recharge->telquery($mobileNbr, $price);
        if($telQueryRes['error_code'] == '0'){
            //正常获取到话费商品信息
            $proInfo = $telQueryRes['result'];
            /*
            [cardid] => 191406
            [cardname] => 江苏电信话费10元直充
            [inprice] => 10.02
            [game_area] => 江苏苏州电信
            */
            return $proInfo;
        }else{
            //查询失败，可能维护、不支持面额等情况
            return array(
                'errorCode' => $telQueryRes["error_code"],
                'reason' => $telQueryRes['reason'],
            );
        }
    }

    /**
     * 提交话费充值
     * @param $mobileNbr
     * @param $price
     * @param $orderNbr
     * @return bool
     */
    public function submit($mobileNbr, $price, $orderNbr){
        $telRechargeRes = $this->recharge->telcz($mobileNbr, $price, $orderNbr);
        if($telRechargeRes['error_code'] =='0'){
            //提交话费充值成功
            return true;
        }else{
            //提交充值失败
            return false;
        }
    }

    /**
     * 订单状态查询
     * @param $orderNbr
     * @return array
     */
    public function getOrderStatus($orderNbr){
        $orderStatusRes = $this->recharge->sta($orderNbr);
        if($orderStatusRes['error_code'] == '0'){
            //查询成功
            if($orderStatusRes['result']['game_state'] =='1'){
                echo "充值成功";
            }elseif($orderStatusRes['result']['game_state'] =='9'){
                echo "充值失败";
            }elseif($orderStatusRes['result']['game_state'] =='-1'){
                echo "提交充值失败"; //可能是如运营商维护、账户余额不足等情况
            }
        }else{
            //查询失败
            return array(
                'errorCode' => $orderStatusRes["error_code"],
                'reason' => $orderStatusRes['reason'],
            );
        }
    }
} 