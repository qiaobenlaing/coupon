<?php
/**
 * Shop Controller
 * User: jiangyufeng
 * Date: 2015-12-24
 */
namespace Client\Controller;
use Think\Model;
use Org\FirePHPCore\FP; 
use Common\Model\ShopAppLogModel;
use Common\Model\ClientAppLogModel;

class HomeController extends BaseController{
	
	/**
	 * 下载页
	 */
	public function download() {
		$shopAppLogMdl = new ShopAppLogModel();
		$clientAppLogMdl = new ClientAppLogModel();
		// 获得android顾客端最新的下载信息
		$cAppInfo = $clientAppLogMdl->getNewestClientAppVersion();
		// 获得android商家端最新的下载信息
		$sAppInfo = $shopAppLogMdl->getNewestShopAppVersion();
		$this->assign('cDownloadUrl', $cAppInfo['updateUrl']);
		$this->assign('sDownloadUrl', $sAppInfo['updateUrl']);
		$this->display();
	}
}