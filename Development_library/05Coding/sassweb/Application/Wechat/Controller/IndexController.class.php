<?php
namespace Wechat\Controller;
use Common\Model\ClientAppLogModel;
use Common\Model\ShopAppLogModel;
use Think\Controller;
class IndexController extends Controller {
    public function index(){
        $token = md5('huiquan');
        $getToken = I('get.Token');
        if($token == $getToken) {
            echo true;
        }
    }

    /**
     * 进入下载页面
     */
    public function download(){
        $clientAppLogMdl = new ClientAppLogModel();
        $cAppInfo = $clientAppLogMdl->getNewestClientAppVersion();
        $this->assign('cDownloadUrl', $cAppInfo['updateUrl']);
        $shopAppLogMdl = new ShopAppLogModel();
        $sAppInfo = $shopAppLogMdl->getNewestShopAppVersion();
        $this->assign('sDownloadUrl', $sAppInfo['updateUrl']);
        $this->display();
    }

    /**
     * 进入下载顾客端页面
     */
    public function cdownload() {
        $cityId = I('get.cityId');
        $shopCode = I('get.shopCode');
        $activityNbr = I('get.activityNbr');
        $clientAppLogMdl = new ClientAppLogModel();
        // 获得android顾客端最新的下载信息
        $cAppInfo = $clientAppLogMdl->getNewestClientAppVersion();
        $assign = array(
            'cDownloadUrl' => $cAppInfo['updateUrl'],
            'cityId' => $cityId,
            'shopCode' => $shopCode,
            'activityNbr' => $activityNbr,
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 进入下载商家端页面
     */
    public function sdownload(){
        $shopAppLogMdl = new ShopAppLogModel();
        $sAppInfo = $shopAppLogMdl->getNewestShopAppVersion();
        $this->assign('sDownloadUrl', $sAppInfo['updateUrl']);
        $this->display();
    }

    /**
     * 分享用户邀请码页面
     */
    public function invitationCodeShare() {
        $code = I('code');
        if(empty($code)) {
            $code = substr($_SERVER["QUERY_STRING"], 5, 5);
        }
        $this->assign('code', $code);
        $clientAppLogMdl = new ClientAppLogModel();
        $cAppInfo = $clientAppLogMdl->getNewestClientAppVersion();
        $this->assign('cDownloadUrl', $cAppInfo['updateUrl']);
        $this->display();
    }
}