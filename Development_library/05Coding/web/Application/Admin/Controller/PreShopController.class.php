<?php
/**
 * Created by PhpStorm.
 * User: Jihuafei
 * Date: 15-10-27
 * Time: 下午3:46
 */
namespace Admin\Controller;
use Common\Model\BmStaffModel;
use Common\Model\ConsumeOrderModel;
use Common\Model\PreShopModel;
use Common\Model\PSNALogModel;
use Common\Model\ShopDecorationModel;
use Common\Model\ShopModel;
use Common\Model\ShopStaffModel;
use Common\Model\ShopStaffRelModel;
use Common\Model\ShopTypeModel;
use Common\Model\ShopTypeRelModel;

class PreShopController extends AdminBaseController {

    /**
     * 数据导出
     */
    public function dataExport() {
        $this->assign(array(
            'title' => '数据导出',
            'get' => I('get.'),
        ));
        $this->display();
    }

    /**
     * 导出交易统计
     */
    public function exportDeal() {
        $preShopMdl = new PreShopModel();
        $formData = I('get.');
        $startTime = $formData['startTime'];
        $endTime = $formData['endTime'];
        $startYear = (int)date('Y', strtotime($startTime));
        $startMonth = (int)date('m', strtotime($startTime));
        $endYear = (int)date('Y', strtotime($endTime));
        $endMonth = (int)date('m', strtotime($endTime));
        $count = ($endYear - $startYear) * 12 + $endMonth + 1 - $startMonth;
        $this->pager->setPage(0);
        $preShopList = $preShopMdl->listPreShop(
            array('PreShop.status' => \Consts::PRE_SHOP_STATUS_USED),
            array('PreShop.useShopCode', 'PreShop.developerCode', 'Shop.shopName', 'Shop.province', 'Shop.city'),
            array(
                array('tableName' => 'Shop', 'joinCondition' => 'Shop.shopCode = PreShop.useShopCode', 'joinType' => 'inner'),
            ), '', $this->pager
        );
        $bmStaffMdl = new BmStaffModel();
        $consumeOrderMdl = new ConsumeOrderModel();
        foreach($preShopList as $k => $preShop) {
            // 获得录入人的信息
            $staffInfo = $bmStaffMdl->getBMStaffInfo(array('staffCode' => $preShop['developerCode']));
            $preShopList[$k]['developerName'] = $staffInfo['realName']; // 地推人员的姓名
            for($i = 0; $i < $count; $i++){
                $condition = array(
                    'orderTime' => array('like', date('Y-m', strtotime($startTime) + $i * (31 * 86400))."%"),
                    'shopCode' => $preShop['useShopCode'],
                    'status' => \Consts::PAY_STATUS_PAYED
                );
                $preShopList[$k]['consumeList'][$i] = $consumeOrderMdl->field(array('count(orderCode)' => 'count', 'sum(orderAmount)' => 'price'))->where($condition)->find();

            }
        }
        if(empty($preShopList)){
            $string = '';
            foreach($formData as $k => $v){
                if($string){
                    $string .= '&'.$k.'='.$v;
                }else{
                    $string .= '?'.$k.'='.$v;
                }
            }
            $url = '/Admin/PreShop/dataExport'.$string;
            header('content-type:text/html;charset=utf-8;');
            echo '<script>alert("没有符合条件的数据！");</script>';
            echo "<SCRIPT LANGUAGE=\"JavaScript\">location.href='$url'</SCRIPT>";
            exit;
        }
        //导入类库
        Vendor("PHPExcel.PHPExcel");
        $excel = new \PHPExcel();
        $excel->getDefaultStyle()->getAlignment()->setHorizontal(\PHPExcel_Style_Alignment::HORIZONTAL_LEFT);
        $excel->getDefaultStyle()->getAlignment()->setVertical(\PHPExcel_Style_Alignment::VERTICAL_CENTER);
        $excel->getDefaultStyle()->getAlignment()->setWrapText(1);
        $objActSheet = $excel->getActiveSheet();

        //列
        $letter = array('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','AA','AB','AC','AD','AE','AF','AG','AH','AI','AJ','AK','AL','AM');
        //表头
        $title = array('地区', '地推人员姓名', '商户名称');
        for($i = 0; $i < $count; $i++){
            $title[] = date('Y-m', strtotime($startTime) + $i * (31 * 86400));
        }

        for($i = 0;$i < ($count+ 3);$i++){
            //设置表头样式
            $head_row = 1;
            if($i < 3){
                //设置表格宽度
                $objActSheet->getColumnDimension($letter[$i])->setWidth(30);
                $objActSheet->mergeCells($letter[$i].$head_row.':'.$letter[$i].($head_row+1));
                $objActSheet->getStyle("$letter[$i]$head_row")->getFont()->setSize(14);
                $objActSheet->getStyle("$letter[$i]$head_row")->getFont()->setBold(true);
                //填充表头
                $objActSheet->setCellValue("$letter[$i]$head_row", $title[$i]);
            }else{
                //设置表格宽度
                $objActSheet->getColumnDimension($letter[($i-3)*2+3])->setWidth(15);
                $objActSheet->getColumnDimension($letter[($i-3)*2+4])->setWidth(15);

                $objActSheet->mergeCells($letter[($i-3)*2+3].$head_row.':'.$letter[($i-3)*2+4].$head_row);
                $objActSheet->getStyle($letter[($i-3)*2+3].$head_row)->getFont()->setSize(14);
                $objActSheet->getStyle($letter[($i-3)*2+3].$head_row)->getFont()->setBold(true);
                //填充表头
                $objActSheet->getStyle($letter[($i-3)*2+3].$head_row)->getAlignment()->setHorizontal(\PHPExcel_Style_Alignment::HORIZONTAL_CENTER);
                $objActSheet->setCellValue($letter[($i-3)*2+3].$head_row, $title[$i]);


                $objActSheet->getStyle($letter[($i-3)*2+3].($head_row + 1))->getFont()->setSize(14);
                $objActSheet->getStyle($letter[($i-3)*2+3].($head_row + 1))->getFont()->setBold(true);
                //填充表头
                $objActSheet->setCellValue($letter[($i-3)*2+3].($head_row + 1), '交易笔数');

                $objActSheet->getStyle($letter[($i-3)*2+4].($head_row + 1))->getFont()->setSize(14);
                $objActSheet->getStyle($letter[($i-3)*2+4].($head_row + 1))->getFont()->setBold(true);
                //填充表头
                $objActSheet->setCellValue($letter[($i-3)*2+4].($head_row + 1), '交易金额');
            }
        }
        $row = 3;
        foreach($preShopList as $v){
            $objActSheet->setCellValue("$letter[0]$row", $v['province'].$v['city']);
            $objActSheet->setCellValue("$letter[1]$row", $v['developerName']);
            $objActSheet->setCellValue("$letter[2]$row", $v['shopName']);
            foreach($v['consumeList'] as $k => $cv){
                $count = $cv['count']?$cv['count']:0;
                $price = $cv['price'] ? number_format($cv['price']/100, 2, '.', '') : 0;
                $objActSheet->setCellValue($letter[$k*2+3].$row, $count);
                $objActSheet->setCellValue($letter[$k*2+4].$row, $price);
            }
            $row += 1;
        }

        $write = new \PHPExcel_Writer_Excel2007($excel);
        ob_end_clean();
        header("Pragma: public");
        header("Expires: 0");
        header("Cache-Control:must-revalidate, post-check=0, pre-check=0");
        header("Content-Type:application/force-download");
        header("Content-Type:application/vnd.ms-excel");
        header("Content-Type:application/octet-stream");
        header("Content-Type:application/download");
        header('Content-Disposition:attachment;filename="地推商户交易额.xlsx"');
        header("Content-Transfer-Encoding:utf-8");
        $write->save('php://output');
    }

    /**
     * 获得不采纳的理由
     */
    public function listNALog() {
        if(IS_AJAX) {
            $data = I('post.');
            $pSNALogMdl = new PSNALogModel();
            $list = $pSNALogMdl->listLog($data, array('*'));
            $this->ajaxSucc('', $list);
        } else {
            echo 'WELCOME TO HUIQUAN';
        }
    }

    /**
     * 保存不采纳商户的理由
     */
    public function editNotAdoptLog() {
        if(IS_AJAX) {
            $data = I('post.');
            $pSNALogMdl = new PSNALogModel();
            $ret = $pSNALogMdl->editLog(array(), $data);
            if($ret['code'] === C('SUCCESS')) {
                $this->ajaxSucc();
            } else {
                $this->ajaxError($ret['code']);
            }
        } else {
            echo 'WELCOME TO HUIQUAN';
        }
    }

    /**
     * 删除预采用商家
     */
    public function deletePreShop() {
        $preShopCode = I('get.shopCode');
        $preShopMdl = new PreShopModel();
        $preShopMdl->delPreShop($preShopCode);
        redirect('/Admin/PreShop/listPreShop');
    }

    /**
     * 采用商户
     */
    public function useTheShop() {
        if(IS_AJAX) {
            $shopCode = I('post.shopCode');
            $preShopMdl = new PreShopModel();
            $preShopInfo = $preShopMdl->getPreShopInfo(array('shopCode' => $shopCode), array('*'));

            // 保存商户所属的类型
            if(empty($preShopInfo['type'])) {
                $this->ajaxError(C('SHOP.TYPE_EMPTY'));
            }
            $shopTypeMdl = new ShopTypeModel();
            $shopTypeInfo = $shopTypeMdl->getShopTypeInfo(array('typeValue' => $preShopInfo['type']), array('shopTypeId'));
            $shopTypeList = array($shopTypeInfo['shopTypeId']);
            unset($preShopInfo['type']);

            $bMaMobileNbr = $preShopInfo['bMaMobileNbr'];
            $shopStaffMdl = new ShopStaffModel();

            // 根据手机号获得商家员工
            $bigManagerInfo = $shopStaffMdl->getShopStaffInfo(array('mobileNbr' => $bMaMobileNbr), array('staffCode', 'realName'));
            if($bigManagerInfo) {
                // 若已存在的大店长姓名与建档时录入姓名不同，则提示错误
                if($bigManagerInfo['realName'] != $preShopInfo['bigManager']) {
                    $this->ajaxError(C('SHOP_STAFF.BIG_MNG_NAME_ERROR'));
                } elseif ($bigManagerInfo['realName'] == $preShopInfo['bigManager']) {
                    $bigManagerCode = $bigManagerInfo['staffCode'];
                }
            } else {
                // 添加大店长
                $bigManagerData = array(
                    'realName' => $preShopInfo['bigManager'],
                    'mobileNbr' => $bMaMobileNbr,
                    'userLvl' => C('STAFF_LVL.BIG_MANAGER'),
                );
                $ret = $shopStaffMdl->editShopStaff($bigManagerData);
                if($ret['code'] !== true) {
                    $this->ajaxError($ret['code']);
                }
                $bigManagerCode = $ret['staffCode'];
            }

            // 采用该商户：添加该商家的商家预留手机号
            $preShopInfo['mobileNbr'] =  $preShopInfo['bMaMobileNbr'];
            $tem = array('shopCode', 'useTime', 'useShopCode', 'bMaMobileNbr', 'bigManager', 'status', 'developerCode', 'createDate', 'shopOwner');
            foreach($tem as $v) {
                unset($preShopInfo[$v]);
            }

            $shopMdl = new ShopModel();
            $preShopInfo['status'] = C('SHOP_STATUS.DISABLE'); // 商户状态为禁用

            // 保存商户信息
            $editShopRet = $shopMdl->editShop($preShopInfo);
            if($editShopRet['code'] !== true) {
                $this->ajaxError($editShopRet['code']);
            }

            // 添加大店长与商家的关系
            $data = array(
                'staffCode' => $bigManagerCode,
                'shopCode' => $editShopRet['shopCode'],
            );
            $shopStaffRelMdl = new ShopStaffRelModel();
            $ret = $shopStaffRelMdl->editShopStaffRel($data);
            if($ret['code'] !== C('SUCCESS')) {
                $this->ajaxError($ret['code']);
            }

            // 保存商户所属的类型
            $shopTypeRelMdl = new ShopTypeRelModel();
            $shopTypeRelMdl->saveShopTypeRel($shopTypeList, $editShopRet['shopCode']);

            // 更新商户的背景图片
            $shopDecorationMdl = new ShopDecorationModel();
            $useDecorationRet = $shopDecorationMdl->usePreShopDecoration($shopCode, $editShopRet['shopCode']);

            // 更新预采用商户的采用时间，采用后的商家编码，状态
            $data = array(
                'shopCode' => $shopCode,
                'useTime' => time(),
                'useShopCode' => $editShopRet['shopCode'],
                'status' => \Consts::PRE_SHOP_STATUS_USED,
            );
            $ret = $preShopMdl->editPreShop($data);

            if($ret['code'] == C('SUCCESS')) {
                $this->ajaxSucc('操作成功', array('useTime' => date('Y-m-d H:i:s', $data['useTime'])));
            } else {
                $this->ajaxError($ret);
            }
        }
    }

    /**
     * 获得预采用的商店列表
     */
    public function listPreShop() {
        $userInfo = $this->user;
        $preShopMdl = new PreShopModel();
        $data = I('get.');
        if($userInfo['userLvl'] == \Consts::HQ_STAFF_TYPE_ADMIN) {
        } else {
            $data['developerCode'] = $userInfo['staffCode'];
        }
        $preShopList = $preShopMdl->listPreShop($data, array('PreShop.shopName', 'PreShop.bigManager', 'PreShop.bMaMobileNbr', 'PreShop.developerCode', 'PreShop.shopCode', 'PreShop.licenseNbr', 'PreShop.createDate', 'PreShop.type', 'PreShop.province', 'PreShop.city', 'PreShop.district', 'PreShop.street', 'PreShop.status'), array(), 'createDate DESC', $this->pager);
        $bmStaffMdl = new BmStaffModel();
        foreach($preShopList as $k => $preShop) {
            $preShopList[$k]['useTime'] = date('Y-m-d H:i:s', $preShopList[$k]['useTime']); // unix时间戳转其他时间格式
            // 获得录入人的信息
            $staffInfo = $bmStaffMdl->getBMStaffInfo(array('staffCode' => $preShop['developerCode']));
            $preShopList[$k]['developerName'] = $staffInfo['realName']; // 地推人员的姓名
            // 获得未采纳的理由的数量
            $pSNALogMdl = new PSNALogModel();
            $preShopList[$k]['nAReasonCount'] = $pSNALogMdl->countLog(array('preShopCode' => $preShop['shopCode']));
        }

        $preShopCount = $preShopMdl->countPreShop(I('get.'));
        $this->pager->setItemCount($preShopCount);

        $shopTypeMdl = new ShopTypeModel();
        $shopTypeList = $shopTypeMdl->getAllShopTypeList();

        $assign = array(
            'title' => '辛苦拉来的商户',
            'dataList' => $preShopList,
            'get' => I('get.'),
            'pager' => $this->pager,
            'shopTypeList' => $shopTypeList
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($preShopList) ? '' : $this->fetch('PreShop:listPreShopWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 组成商户的营业时间
     * @param array $shopOpeningTime 一维索引数组
     * @param array $shopClosedTime 一维索引数组
     * @return string $businessHours 商户营业时间 json格式的字符串
     */
    private function formBusinessHours($shopOpeningTime, $shopClosedTime) {
        $businessHours = array();
        foreach($shopOpeningTime as $openK => $openV){
            $businessHours[] = array(
                'open' => $openV,
                'close' => $shopClosedTime[$openK]
            );
        }
        return json_encode($businessHours);
    }

    /**
     * 新增或者编辑商家
     */
    public function editPreShop() {
        if(IS_GET) {
            $shopCode = I('get.shopCode');
            $preShopMdl = new PreShopModel();
            $title = $shopCode ? '修改商户' : '增加商户';
            $shopInfo = $preShopMdl->getPreShopInfo(array('shopCode' => $shopCode), array('*'));
            if(empty($shopInfo['businessHours'])) $shopInfo['businessHours'] = '[]';

            // 商家营业时间，jsonString -> array
//            if(isset($shopInfo['businessHours'])){
//                $shopInfo['businessHours'] = json_decode($shopInfo['businessHours'], true);
//            }

            $temp = array('deliveryFee', 'takeoutRequirePrice');
            foreach($temp as $v) {
                $shopInfo[$v] = $shopInfo[$v] / C('RATIO');// 分转化为元
            }
            // 工行卡折扣,单位“百分数”转化为“折”
            if(isset($shopInfo['onlinePaymentDiscount'])) {
                $shopInfo['onlinePaymentDiscount'] = $shopInfo['onlinePaymentDiscount'] / 10;
            }
            
            // 获得商家所有环境图片
            $shopDecorationMdl = new ShopDecorationModel();
            $decorationList = $shopDecorationMdl->getShopDecorationList($shopCode);
            // 获得商家所有类型
            $shopTypeMdl = new ShopTypeModel();
            $shopTypeList = $shopTypeMdl->getAllShopTypeList();
            // IDcardUrl字段string转array
            $shopInfo['IDcardUrl'] = empty($shopInfo['IDcardUrl']) ? array() : explode('|', $shopInfo['IDcardUrl']);

            $assign = array(
                'shopInfo' => $shopInfo,
                'page' => I('get.page'),
                'title' => $title,
                'shopTypeList' => $shopTypeList,
                'decorationList' => $decorationList,
            );
            $this->assign($assign);
            $this->display();
        } else {
            $data = I('post.');
            // 提取出商家的背景图片的相关信息
            $decorationCodeArr = $data['decorationCode'];
            $imgUrlArr = $data['imgUrl'];
            $titleArr = $data['title'];
            $shortDesArr = $data['decoShortDes'];
            $temp = array('decorationCode', 'imgUrl', 'title', 'decoShortDes');
            foreach($temp as $v) {
                unset($data[$v]);
            }

            // IDcardUrl字段array转string
            $data['IDcardUrl'] = $data['IDcardUrl'] ? implode('|', $data['IDcardUrl']) : '';

            // 工行卡折扣,单位“折”转化为“百分数”
            $data['onlinePaymentDiscount'] = $data['onlinePaymentDiscount'] * 10;
            // 组成营业时间
            $data['businessHours'] = $this->formBusinessHours($data['shopOpeningTime'], $data['shopClosedTime']);
            unset($data['shopOpeningTime']);
            unset($data['shopClosedTime']);

            $preShopMdl = new PreShopModel();
            $ret = $preShopMdl->editPreShop($data);
            if ($ret['code'] === C('SUCCESS')) {
                // 保存商户的环境图片
                $this->saveShopDecImg($ret['shopCode'], $decorationCodeArr, $imgUrlArr, $titleArr, $shortDesArr);
                $this->ajaxSucc();
            } else {
                $this->ajaxError($ret);
            }
        }
    }

    /**
     * 保存商户的背景图片
     * @param string $shopCode 商家编码
     * @param array $decorationCodeArr 编码一维数组
     * @param array $imgUrlArr 图片url一维数组
     * @param array $titleArr 图片标题一维数组
     * @param array $shortDesArr 图片简短描述一维数组
     */
    private function saveShopDecImg($shopCode, $decorationCodeArr, $imgUrlArr, $titleArr, $shortDesArr) {
        // 删除多余的的图片
        $condition = array('shopCode' => $shopCode);
        if($decorationCodeArr) {
            $condition['decorationCode'] = array('NOTIN', $decorationCodeArr);
        }
        $shopDecorationMdl = new ShopDecorationModel();
        $shopDecorationMdl->delDecoration($condition);

        foreach($decorationCodeArr as $k => $v) {
            $shopDecorationData = array(
                'decorationCode' => $decorationCodeArr[$k],
                'imgUrl' => $imgUrlArr[$k],
                'title' => $titleArr[$k],
                'shortDes' => $shortDesArr[$k],
                'shopCode' => $shopCode,
            );
            // 修改或添加图片信息
            $shopDecorationMdl->editShopDecoration($shopDecorationData);
        }
    }

    /**
     * 上传预采用商户的背景图片
     */
    public function uploadDecoration(){
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
        $shopCode = I('post.shopCode');
        $upload_type = I('post.upload_type');

        $info = $upload->upload();
        if(!$info) {
            // 上传错误提示错误信息
            $this->ajaxError('上传失败');
        } else {  // 上传成功，获取上传文件信息
            $url = substr($info['userfile']['savepath'], 1).$info['userfile']['savename'];
            $return_json = array(
                'upload_type' => $upload_type,
                'url'=> $url,
                'shopCode' => $shopCode,
            );
            $this->ajaxSucc('上传成功', $return_json);
        }
    }
    
    /**
     * 获得地推商户详情
     */
    public function getPreShopInfo() {
    	$preShopMdl = new PreShopModel();
    	$shopCode = I('get.shopCode');
    	$preShopInfo = $preShopMdl->getPreShopInfo(array('shopCode' => $shopCode), array('*'));
    	if(!empty($preShopInfo['IDcardUrl'])){
    		$data = explode('||', $preShopInfo['IDcardUrl']);
    		$idCardPhoto = array();
    		foreach ($data as $d){
    			$idCardPhoto[] = (array)json_decode($d);
    		}
    		$preShopInfo['IDcardUrl'] = array_filter($idCardPhoto);
    	}
    	if($preShopInfo){    	
    		$this->ajaxSucc('', $preShopInfo);
    	}else {
    		$this->ajaxError('商户不存在');
    	}
    }

    /**
     * 补充商户的营业时间
     */
    public function updateBusinessHours() {
        $preShopMdl = new PreShopModel();
        $preShopList = $preShopMdl->field(array('shopCode', 'shopOpeningTime', 'shopClosedTime'))->select();
        foreach($preShopList as $v) {
            $arrBusinessHours = array(array('open' => $v['shopOpeningTime'], 'close' => $v['shopClosedTime']));
            $stringBusinessHours = json_encode($arrBusinessHours);
            $preShopMdl->where(array('shopCode' => $v['shopCode']))->save(array('businessHours' => $stringBusinessHours));
        }
    }
} 