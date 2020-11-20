<?php
/**
 * Created by PhpStorm.
 * User: Jihuafei
 * Date: 15-12-10
 * Time: 下午1:47
 */
namespace Admin\Controller;
use Common\Model\DistrictModel;
use Common\Model\DownloadLogModel;
use Common\Model\ShopModel;
use Common\Model\UserModel;

class QrCodeController extends AdminBaseController {

    /**
     * 获得下载记录
     */
    public function listLog() {
        $districtMdl = new DistrictModel();
        // 获得已经开通的城市
        $openCityList = $districtMdl->listOpenCity();

        $downloadLogMdl = new DownloadLogModel();
        $logList = $downloadLogMdl->listLog(I('get.'), $this->pager);
        $logCount = $downloadLogMdl->countlog(I('get.'));
        $this->pager->setItemCount($logCount);
        $assign = array(
            'title' => '商家端APP更新记录',
            'dataList' => $logList,
            'get' => I('get.'),
            'pager' => $this->pager,
            'openCityList' => $openCityList,
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($logList) ? '' : $this->fetch('QrCode:listLogWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 生成下载惠圈二维码页面
     */
    public function cDownloadQC() {
        $districtMdl = new DistrictModel();
        // 获得已经开通的城市
        $openCityList = $districtMdl->listOpenCity();
        $assign = array(
            'title' => '生成下载惠圈二维码',
            'openCityList' => $openCityList,
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 生成惠圈下载二维码
     */
    public function setCDownloadQC() {
        $data = I('get.');
        $cityIdName = $data['cityIdName'];
        $cityInfo = explode('|', $cityIdName);
        $cityId = $cityInfo[0]; // 城市ID
        $shopCodeList = $data['shopCode']; // 商家编码列表
        $activityNbr = $data['activityNbr']; // 活动编码
        if(empty($cityId) || empty($activityNbr)) {
            $this->ajaxError('请将信息填写完整');
        } else {
            //导入类库
            Vendor('QRcode.phpqrcode');
            $httpHost = $_SERVER['HTTP_HOST'];
            // 点的大小：1到10
            $matrixPointSize = 4;
            // 纠错级别：L、M、Q、H
            $errorCorrectionLevel = 'Q';
            // 保存图片的途径
            $path = "./Public/qrcode/";
            $logo = './Public/img/avatar.png'; // 准备好的logo图片
            if($shopCodeList) { // 有商户时，生成每个商户对应的二维码
                // 获得商家名字
                $shopMdl = new ShopModel();
                $shopList = $shopMdl->getShopList(array('shopCode' => array('IN', $shopCodeList)), array('shopCode', 'shopName'));
                $ret = array();
                foreach($shopList as $k => $shop) {
                    $shopCode = $shop['shopCode'];
                    // 二维码数据
                    $data = 'http://' . $httpHost . '/Wechat/Index/cdownload?cityId=' . $cityId . '&shopCode=' . $shopCode . '&activityNbr=' . $activityNbr;
                    // 生成的文件名
                    $filename = $path . $cityId . '_' . $shopCode . '_' . $activityNbr . '.png';
                    // 保存图片
                    \QRcode::png($data, $filename, $errorCorrectionLevel, $matrixPointSize, 2);
                    // 输出到浏览器
//                \QRcode::png($data, false, $errorCorrectionLevel, $matrixPointSize, 2);

                    // 添加logo
                    $this->addLogo($logo, $filename);

                    // andriod操作一次，保存记录到数据库
                    $this->editDownloadLog($cityId, $shopCode, $activityNbr, \Consts::DOWNLOAD_LOG_OPERATION_AD);
                    // ios操作一次，保存记录到数据库
                    $this->editDownloadLog($cityId, $shopCode, $activityNbr, \Consts::DOWNLOAD_LOG_OPERATION_IOS);

                    // 保存信息，用于返回
                    $ret[$k] = array(
                        'imgUrl' => substr($filename, 1),
                        'shopName' => $shop['shopName']
                    );
                }
            } else { // 没有商户时生成该城市的二维码
                $shopCode = '';
                // 二维码数据
                $data = 'http://' . $httpHost . '/Wechat/Index/cdownload?cityId=' . $cityId . '&shopCode=' . $shopCode . '&activityNbr=' . $activityNbr;
                // 生成的文件名
                $filename = $path . $cityId . '_' . $shopCode . '_' . $activityNbr . '.png';
                // 保存图片
                \QRcode::png($data, $filename, $errorCorrectionLevel, $matrixPointSize, 2);
                // 输出到浏览器
//                \QRcode::png($data, false, $errorCorrectionLevel, $matrixPointSize, 2);

                // 添加logo
                $this->addLogo($logo, $filename);

                // andriod操作一次，保存记录到数据库
                $this->editDownloadLog($cityId, $shopCode, $activityNbr, \Consts::DOWNLOAD_LOG_OPERATION_AD);
                // ios操作一次，保存记录到数据库
                $this->editDownloadLog($cityId, $shopCode, $activityNbr, \Consts::DOWNLOAD_LOG_OPERATION_IOS);

                // 保存信息，用于返回
                $ret[] = array(
                    'imgUrl' => substr($filename, 1),
                    'shopName' => $cityInfo[1]
                );
            }
            $cityName = $cityInfo[1]; // 城市名字
            $this->ajaxSucc('', array('cityName' => $cityName, 'qrData' => $ret));
        }
    }

    /**
     * 二维码上添加logo
     * @param string $logo 准备好的logo图片
     * @param string $QRUrl 已经生成的原始二维码图
     */
    public function addLogo($logo, $QRUrl) {
        if ($logo !== FALSE) {
            $QR = imagecreatefromstring(file_get_contents($QRUrl));
            $logo = imagecreatefromstring(file_get_contents($logo));
            $QR_width = imagesx($QR);//二维码图片宽度
            $QR_height = imagesy($QR);//二维码图片高度
            $logo_width = imagesx($logo);//logo图片宽度
            $logo_height = imagesy($logo);//logo图片高度
            $logo_qr_width = $QR_width / 5;
            $scale = $logo_width/$logo_qr_width;
            $logo_qr_height = $logo_height/$scale;
            $from_width = ($QR_width - $logo_qr_width) / 2;
            //重新组合图片并调整大小
            imagecopyresampled($QR, $logo, $from_width, $from_width, 0, 0, $logo_qr_width, $logo_qr_height, $logo_width, $logo_height);
        }
        //输出图片
        imagepng($QR, $QRUrl);
//        echo '<img src="' . substr($QRUrl, 1) . '">';
    }

    /**
     * 保存该城市该商户该活动的下载记录
     * @param string $cityId 城市ID
     * @param string $shopCode 商家编码
     * @param string $activityNbr 活动编码
     * @param string $operation 操作系统。1-android，2-ios
     * @return boolean 成功返回true，失败返回false
     */
    public function editDownloadLog($cityId, $shopCode, $activityNbr, $operation) {
        $data = array('cityId' => $cityId, 'shopCode' => $shopCode, 'activityNbr' => $activityNbr, 'operation' => $operation);
        $downloadLogMdl = new DownloadLogModel();
        // 判断是否已经有这条记录
        $logInfo = $downloadLogMdl->getLogInfo($data, array('id'));
        if(empty($logInfo)) { // 如果没有这条记录
            $ret = $downloadLogMdl->editLog($data);
        } else {
            $ret = true;
        }
        return $ret;
    }

    public function testCoujiang() {
        if(IS_GET) {
            $this->display();
        } else {
            $userMdl = new UserModel();
            $userList = $userMdl->getUserList(array(), array('mobileNbr'));
            $this->ajaxSucc('', array('userList' => $userList));
        }

    }
}