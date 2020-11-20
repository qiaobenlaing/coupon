<?php
/**
 * Created by PhpStorm.
 * User: 98041
 * Date: 2019/12/20
 * Time: 13:05
 */

namespace Wechat\Controller;


use Vendor\WeixinPay\WeixinNotify;
use Vendor\WeixinPay\WeixinPayApi;

class WeixinController extends WechatBaseController
{
    public function __construct() {
        parent::__construct();
        // 确保jsonrpc后面不被挂上一堆调试信息。
        C('SHOW_PAGE_TRACE', false);
        // $this->pager = new Pager(I('page', 1, 'intval'), I('pageSize', Pager::DEFUALT_PAGE_SIZE, 'intval'));
    }

    //微信登陆
    public function weixinLogin()
    {
        $param = I('get.openid');
        $url = '//test.hotels.huift.com.cn/Home/Weixin/login?param=' . $param;
        header("Location:" . $url);
    }

    //微信支付发起
    public function weixinPay()
    {
        //用户openid
        $openid = I('post.openid');
        //支付金额 （单位：元）
        $money = I('post.money');
        //订单编号
        $orderNbr = I('post.OrderID');
        //附加参数
        $attachParam = I('post.attachParam');
        //订单名称
        $body = I('post.orderTitle');
        //订单编号前缀
        $tradeNo = I('get.tradeNo');
        $weixinPay = new  WeixinPayApi();
        $out_trade_no = $osn = date('Ymd') . str_pad(mt_rand(1, 55555), 5, '0', STR_PAD_LEFT).$orderNbr; //退款时追踪微信订单的凭据
        echo $weixinPay->getJsApiParameters($money, 'oboBC0RedeiSd0YXDCdkO3H7GCsw', $out_trade_no, $orderNbr, $body,$tradeNo);

    }

    //微信支付通知
    public function weixinNotify()
    {
        $weixinNotify = new WeixinNotify();
        $notify = $weixinNotify->getResult();
        $url = 'https://test.hotels.huift.com.cn/Home/Weixin/weixinNotify';
        $this->http_post($url,$notify);
    }

}
