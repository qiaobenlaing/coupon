<?php
/**
 * Coupon Controller
 * User: Huafei Ji
 * Date: 2015-05-19
 */
namespace Admin\Controller;
use Common\Model\DistrictBankIdModel;
use Common\Model\DistrictModel;
use Common\Model\PCouponShopModel;
use Common\Model\PrizeRuleModel;
use Common\Model\UserConsumeModel;
use Org\FirePHPCore\FP;
use Common\Model\BatchCouponModel;
use Common\Model\UserCouponModel;
use Common\Model\ShopModel;
use Common\Model\CouponRuleModel;
use Think\Model;

class BatchCouponController extends AdminBaseController {

    /**
     * 商户优惠券
     */
    public function listSpCoupon() {
        $BatchCouponMdl = new BatchCouponModel();
        $userCouponMdl = new UserCouponModel();
        $type = C('Coupon_BELONG.SHOP');
        $batchCouponList = $BatchCouponMdl->listBatchCoupon(I('get.'), $type, $this->pager);

        foreach ($batchCouponList as $k => $batchCoupon){
            $batchCouponList[$k]['used'] = $userCouponMdl->countUsedCoupon($batchCoupon['batchCouponCode']);
        }

        $batchCouponCount = $BatchCouponMdl->countBatchCoupon(I('get.'), $type);

        $this->pager->setItemCount($batchCouponCount);
        foreach(C('SHOP_NORMAL_COUPON') as $k => $v){
            $couponTypeList[] = array(
                'name'  => C('COUPON_TYPE_NAME.'.$k),
                'value' => $v
            );
        }
        // 获得开放城市
//        判断是否为惠圈人员
        if($_SESSION['USER']['bank_id']!=-1){
            $zone_id=   $_SESSION['USER']['bank_id'];
        }

        $districtBankMdl = new DistrictBankIdModel();
        $cityList = $districtBankMdl->zonelistOpenCity($zone_id);

        $assign = array(
            'title' => '商户优惠券',
            'dataList' => $batchCouponList,
            'get' => I('get.'),
            'nowTime' => time(),
            'couponTypeList' => $couponTypeList,
            'pager' => $this->pager,
            'cityList' => $cityList,
        );
        $this->assign($assign);

        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($batchCouponList) ? '' : $this->fetch('BatchCoupon:listSpCouponWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 平台优惠券
     */
    public function listPfCoupon() {
        $BatchCouponMdl = new BatchCouponModel();
        $type = C('Coupon_BELONG.PLATFORM');
        $batchCouponList = $BatchCouponMdl->listBatchCoupon(I('get.'), $type, $this->pager);
        $batchCouponCount = $BatchCouponMdl->countBatchCoupon(I('get.'), $type);
        $this->pager->setItemCount($batchCouponCount);
        foreach(C('SHOP_NORMAL_COUPON') as $k => $v){
            $couponTypeList[] = array(
                'name'  => C('COUPON_TYPE_NAME.'.$k),
                'value' => $v
            );
        }
        $assign = array(
            'title' => '平台优惠券',
            'dataList' => $batchCouponList,
            'get' => I('get.'),
            'nowTime' => time(),
            'couponTypeList' => $couponTypeList,
            'pager' => $this->pager,
        );
        $this->assign($assign);

        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($batchCouponList) ? '' : $this->fetch('BatchCoupon:listPfCouponWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 检查平台补贴和商家让利的合法性
     * @param string $couponType 优惠券类型
     * @param float $insteadPrice 面值，单位：元
     * @param float $discountPercent 折扣，单位：折
     * @param float $hqSubsidy 平台补贴，单位：元或折
     * @param float $shopSubsidy 商家让利，单位：元或折
     * @param float $icbcSubsidy 工行补贴，单位：元或折
     * @return boolean 如果合法，返回true，如果不合法，返回false
     */
    private function checkSubsidy($couponType, $insteadPrice, $discountPercent, $hqSubsidy, $shopSubsidy, $icbcSubsidy) {
        return true;
        if($couponType == \Consts::COUPON_TYPE_DISCOUNT) {
            if($discountPercent + $hqSubsidy + $shopSubsidy + $icbcSubsidy != 10) {
                return false;
            }
        } else {
            if($hqSubsidy + $shopSubsidy + $icbcSubsidy != $insteadPrice) {
                return false;
            }
        }
        return true;
    }

    /**
     * 编辑优惠券
     */
    public function editCoupon() {
        $shopMdl = new ShopModel();
        $batchCouponMdl = new BatchCouponModel();
        $couponRuleMdl = new CouponRuleModel();
        $prizeRuleMdl = new PrizeRuleModel();
        if(IS_GET) {
            $batchCouponCode = I('get.batchCouponCode');
            $shopCode = I('get.shopCode');
            // 获得所有商户的列表，二维数组
            $getShopName = $shopMdl->getManagerShopName();

            $hqCode = C('HQ_CODE');
            $getData = I('get.');
            $couponInfo = $batchCouponMdl->getCouponInfo($batchCouponCode);

            // 转化平台补贴和商家让利的单位
            $radio = $couponInfo['couponType'] == \Consts::COUPON_TYPE_DISCOUNT ? C('TEN') : \Consts::HUNDRED;
            $temp = array('hqSubsidy', 'shopSubsidy', 'icbcSubsidy');
            foreach($temp as $v) {
                if(isset($couponInfo[$v])) {
                    $couponInfo[$v] = $couponInfo[$v] / $radio;
                }
            }

            if(empty($shopCode) && $couponInfo){
                $shopCode = $couponInfo['shopCode'];
            }
            $shopInfo = $shopMdl->getShopInfo($shopCode, array('shopCode', 'businessHours'));

            // 添加是否24小时可用
            if($couponInfo['dayStartUsingTime'] == '00:00:00' && $couponInfo['dayEndUsingTime'] == '23:59:59') {
                $couponInfo['allDay'] = C('YES');
            }
            if($couponInfo['isUniversal'] == C('NO')) {
                $pCouponShopMdl = new PCouponShopModel();
                $linkedShopList = $pCouponShopMdl->listLinkedShop(array('batchCouponCode' => $batchCouponCode));
            } else {
                $linkedShopList = array();
            }
            $exRuleList[] = $couponInfo['exRuleList'] ? $couponInfo['exRuleList'] : array();
            $prizeRuleInfo = $prizeRuleMdl->getPrizeRule('', array('giftCode' => $batchCouponCode, 'prizeType' => C('PRIZE_TYPE.COUPON')));

            $couponTypeList = array();
            foreach(C('COUPON_TYPE') as $k => $v) {
                $couponTypeList[] = array(
                    'value' => $v,
                    'name' => C('COUPON_TYPE_NAME.' . $k)
                );
            }

            //商家LOGO OSS设置
            if(!empty($couponInfo['url'])){
                $couponInfo['url'] = C("urlOSS").$couponInfo['url'];
            }

            $assign = array(
                'title' => '编辑优惠券',
                'getShopName' => $getShopName,
                'bmCode' => $hqCode,
                'shopInfo' => $shopInfo,
                'couponInfo' => $couponInfo,
                'exRuleList' => $exRuleList,
                'linkedShopList' => $linkedShopList,
                'prizeRuleInfo' => $prizeRuleInfo,
                'couponTypeList' => $couponTypeList,
                'getData' => $getData
            );
            $this->assign($assign);
            $this->display();
        }else {
            $data = I('post.');


            // 判断是否是OSS显示的图片（保存）
            if(strpos($data['url'],C("urlOSS"))!==false){
                //截取掉前面的https://oss.cloud.hfq.huift.com.cn/
                $start = stripos($data['url'],C("urlOSS"));
                $length = mb_strlen(C("urlOSS"));
                //$length-1 指的是不截取 https://oss.cloud.hfq.huift.com.cn/ 最后的斜杠
                //mb_strlen($data['brandLogo'])-$length+1 因为下标是从0开始的所以要加上1，不然图片扩展名缺失
                $data['url'] = substr($data['url'],$length-1,mb_strlen($data['url'])-$length+1);
            }

            // 验证平台补贴和商家让利的合法性
            $checkSubsidy = $this->checkSubsidy($data['couponType'], $data['insteadPrice'], $data['discountPercent'], $data['hqSubsidy'], $data['shopSubsidy'], $data['icbcSubsidy']);
            if(!$checkSubsidy) {
                $this->ajaxError(array('code' => '请检查平台补贴，商家让利，工行补贴'));
            }

            // 转化平台补贴和商家让利的单位
            if($data['couponType'] == \Consts::COUPON_TYPE_DISCOUNT) {
                $data['hqSubsidy'] = $data['hqSubsidy'] * C('TEN');
                $data['shopSubsidy'] = $data['shopSubsidy'] * C('TEN');
                $data['icbcSubsidy'] = $data['icbcSubsidy'] * C('TEN');
            } else {
                $data['hqSubsidy'] = $data['hqSubsidy'] * \Consts::HUNDRED;
                $data['shopSubsidy'] = $data['shopSubsidy'] * \Consts::HUNDRED;
                $data['icbcSubsidy'] = $data['icbcSubsidy'] * \Consts::HUNDRED;
            }

            if(empty($data['prizeRuleId']) && $data['convertNbr']) {
                $prizeRuleData = array(
                    'convertNbr' => $data['convertNbr'],
                    'limitTime' => $data['limitTime'],
                    'limitNbr' => $data['limitNbr'],
                    'startDay' => $data['prizeStartDay'],
                    'endDay' => $data['prizeEndDay'],
//                    'prizeLevel' => $data['prizeLevel'],
                    'prizeType' => C('PRIZE_TYPE.COUPON'),
                );
            } elseif($data['prizeRuleId']) {
                $prizeRuleData = array(
                    'id' => $data['prizeRuleId'],
                    'convertNbr' => $data['convertNbr'],
                    'limitTime' => $data['limitTime'],
                    'limitNbr' => $data['limitNbr'],
                    'startDay' => $data['prizeStartDay'],
                    'endDay' => $data['prizeEndDay'],
//                    'prizeLevel' => $data['prizeLevel'],
                );
            }

            unset($data['prizeRuleId']);
            unset($data['convertNbr']);
            unset($data['limitTime']);
            unset($data['limitNbr']);
            unset($data['prizeStartDay']);
            unset($data['prizeEndDay']);
            unset($data['prizeLevel']);
            if(in_array($data['couponType'], array(C('COUPON_TYPE.N_PURCHASE'), C('COUPON_TYPE.REDUCTION'), C('COUPON_TYPE.DISCOUNT'), C('COUPON_TYPE.PHYSICAL'), C('COUPON_TYPE.EXPERIENCE'), C('COUPON_TYPE.EXCHANGE'), C('COUPON_TYPE.VOUCHER')))) {
                if(empty($data['startTakingTime'])) {$data['startTakingTime'] = date('Y-m-d', time());}
                if(empty($data['endTakingTime'])) {$data['endTakingTime'] = date('Y-m-d', strtotime($data['startTakingTime']) + 7 * 86400);}
                if(empty($data['startUsingTime'])) {$data['startUsingTime'] = date('Y-m-d', time());}
                if(empty($data['expireTime'])) {$data['expireTime'] = date('Y-m-d', strtotime($data['startUsingTime']) + 7 * 86400);}
                $dayLessOneSecond = 86400 - 1;
                $data['endTakingTime'] = date('Y-m-d H:i:s', strtotime($data['endTakingTime']) + $dayLessOneSecond);
                $data['expireTime'] = date('Y-m-d H:i:s', strtotime($data['expireTime']) + $dayLessOneSecond);
            } else {
                $tmp = array('startTakingTime', 'endTakingTime');
                foreach($tmp as $v) {
                    if(empty($data[$v])) {
                        $data[$v] = '0000-00-00 00:00:00';
                    }
                }
                $tmp = array('startUsingTime', 'expireTime');
                foreach($tmp as $v) {
                    $data[$v] = '0000-00-00 00:00:00';
                }
            }

            if($data['shopCode'] != C('HQ_CODE')) {
                $data['couponBelonging'] = C('COUPON_BELONG.SHOP');
            } else {
                $data['couponBelonging'] = C('COUPON_BELONG.PLATFORM');
                $data['shopCode'] = C('HQ_CODE');
            }

            $tempArray = array('isConsumeRequired', 'isAvailable', 'isUniversal', 'isSend');
            foreach($tempArray as $v) {
                if(!$data[$v]) $data[$v] = C('NO');
            }

            $userInfo = session('USER');
            $staffCode = $userInfo['staffCode'];
            $data['creatorCode'] = $staffCode;

            $ruleCode = array();
            for($i = 0; $i < count($data['ruleCode']); $i++) {
                if($data['ruleCode'][$i]) {
                    if($data['ruleDes'][$i]) {
                        $ruleInfo = array(
                            'ruleDes' => $data['ruleDes'][$i],
                            'creatorCode' => $staffCode,
                            'createTime' => date('Y-m-d H:i:s')
                        );
                        $ret = $couponRuleMdl->updateCouponRule($data['ruleCode'][$i], $ruleInfo);
                        if($ret) {
                            $ruleCode[] = $data['ruleCode'][$i];
                        }
                    }else{
                        $couponRuleMdl->delCouponRule($data['ruleCode'][$i]);
                    }
                }else{
                    if($data['ruleDes'][$i]) {
                        $ruleData = array(
                            'ruleDes' => $data['ruleDes'][$i],
                            'creatorCode' => $staffCode,
                            'createTime' => date('Y-m-d H:i:s')
                        );
                        $ret = $couponRuleMdl->addCouponRule($ruleData);
                        if(isset($ret['ruleCode'])){
                            $ruleCode[] = $ret['ruleCode'];
                        }
                    }
                }
            }
            if($ruleCode) {
                $data['exRuleList'] = json_encode($ruleCode);
            }
            unset($data['ruleCode']);
            unset($data['ruleDes']);

            $data['url'] = $data['url'] ? $data['url'] : str_replace('{{couponType}}', $data['couponType'], C('COUPON_DEFAULT_IMG'));
            $data['discountPercent'] = $data['discountPercent'] * C('DISCOUNT_RATIO');
            if(!empty($data['allDay'])) {
                $data['dayStartUsingTime'] = '00:00:00';
                $data['dayEndUsingTime'] = '23:59:59';
                unset($data['allDay']);
            }
            if(isset($data['batchCouponCode']) && !empty($data['batchCouponCode'])){
                unset($data['batchCount']);
            }

            $ret = $batchCouponMdl->editBatchCoupon($data);

            if ($ret['code'] === true) {
                if(isset($prizeRuleData)){
                    $prizeRuleData['giftCode'] = $ret['batchCouponCode'];
                    $prizeRuleMdl->editPrizeRule($prizeRuleData);
               }
                $this->ajaxSucc('添加成功');
            } else {
                $this->ajaxError($ret);
            }
        }
    }

    /**
     * 修改用户优惠券状态
     */
    public function changeStatus() {
        $userCouponMdl = new UserCouponModel();
        $userCouponCode = I('get.userCouponCode');
        $status = I('get.status');
        $res = $userCouponMdl->changeCouponStatus($userCouponCode, $status);
        if ($res === true) {
            $this->ajaxSucc('操作成功!');
        } else {
            $this->ajaxError($res);
        }
    }

    /**
     * 优惠券统计分析
     */
    public function analysisCoupon() {
        $condition = I('get.');
        // 券的使用时间
        if ($condition['startDate'] && $condition['endDate']) {
            $condition['UserConsume.consumeTime'] = array('BETWEEN', array($condition['startDate'], $condition['endDate'] . ' 23:59:59'));
        } elseif ($condition['startDate'] && !$condition['endDate']) {
            $condition['UserConsume.consumeTime'] = array('EGT', $condition['startDate']);
        } elseif (! $condition['startDate'] && $condition['endDate']) {
            $condition['UserConsume.consumeTime'] = array('ELT', $condition['endDate'] . ' 23:59:59');
        }
        unset($condition['startDate']);
        unset($condition['endDate']);

        $batchCouponMdl = new BatchCouponModel();
        // 得到优惠券统计分析数据
        $data = $batchCouponMdl->analysisCoupon($condition);

        $batchCouponList = $batchCouponMdl->listBatchCouponGroupByShop($condition, $this->pager);
        $batchCouponCount = $batchCouponMdl->countBatchCouponGroupByShop($condition);
        // 设置记录总数，同时会更新总页数。
        $this->pager->setItemCount($batchCouponCount);
        $data['rate'] =  sprintf("%.2f", $data['usedCount'] / $data['takeCount'] * 100); // 整体转化率
        // 获得优惠券类型
        $couponType = $batchCouponMdl->getCouponType();
        // 获得开放的城市
        $districtMdl = new DistrictModel();
        $cityList = $districtMdl->listOpenCity();
        $assign = array(
            'title' => '商户优惠券统计',
            'cityList' => $cityList,
            'get' => I('get.'),
            'couponTypeList' => $couponType,
            'data' => $data,
            'batchCouponList' => $batchCouponList,
            'pager' => $this->pager,
        );
        $this->assign($assign);
        if(!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($batchCouponList) ? '' : $this->fetch('BatchCoupon:analysisCouponWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 导出商户优惠券统计数据为excel表格
     */
    public function exportCouponStatistics() {
        $condition = I('get.');
        // 券的使用时间
        if ($condition['startDate'] && $condition['endDate']) {
            $condition['UserConsume.consumeTime'] = array('BETWEEN', array($condition['startDate'], $condition['endDate'] . ' 23:59:59'));
        } elseif ($condition['startDate'] && !$condition['endDate']) {
            $condition['UserConsume.consumeTime'] = array('EGT', $condition['startDate']);
        } elseif (! $condition['startDate'] && $condition['endDate']) {
            $condition['UserConsume.consumeTime'] = array('ELT', $condition['endDate'] . ' 23:59:59');
        }
        unset($condition['startDate']);
        unset($condition['endDate']);

        $batchCouponMdl = new BatchCouponModel();
        $this->pager->setPage(0); // 设置搜索所有记录
        $batchCouponList = $batchCouponMdl->listBatchCouponGroupByShop($condition, $this->pager);

        //导入类库
        Vendor("PHPExcel.PHPExcel");
        $excel = new \PHPExcel();
        $excel->getDefaultStyle()->getAlignment()->setHorizontal(\PHPExcel_Style_Alignment::HORIZONTAL_LEFT);
        $excel->getDefaultStyle()->getAlignment()->setVertical(\PHPExcel_Style_Alignment::VERTICAL_CENTER);
        $excel->getDefaultStyle()->getAlignment()->setWrapText(1);
        $objActSheet = $excel->getActiveSheet();
        //列
        $letter = array('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');
        //表头
        $title = array('地市', '商户名', '发放数量', '使用量', '应补贴金额', '实际补贴金额');
        $column = count($title);
        for($i = 0;$i < $column;$i++){
            //设置表格宽度
            $objActSheet->getColumnDimension("$letter[$i]")->setWidth(20);
            //设置表头样式
            $head_row = 1;
            $objActSheet->getStyle("$letter[$i]$head_row")->getFont()->setBold(true);
            //填充表头
            $objActSheet->setCellValue("$letter[$i]$head_row", $title[$i]);
        }
        $row = 2;
        foreach($batchCouponList as $v){
            $objActSheet->setCellValue("$letter[0]$row", $v['city']);
            $objActSheet->setCellValue("$letter[1]$row", $v['shopName']);
            $objActSheet->setCellValue("$letter[2]$row", $v['totalVolume']);
            $objActSheet->setCellValue("$letter[3]$row", $v['usedCount']);
            $objActSheet->setCellValue("$letter[4]$row", $v['totalOriginSubsidy']);
            $objActSheet->setCellValue("$letter[5]$row", $v['totalSubsidy']);
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
        header('Content-Disposition:attachment;filename="商户优惠券统计.xlsx"');
        header("Content-Transfer-Encoding:utf-8");
        $write->save('php://output');
    }

    /**
     * 某个商户的优惠券统计分析
     */
    public function shopCouponAnalysis() {
        $data = I('get.');
        $shopMdl = new ShopModel();
        $shopInfo = $shopMdl->getShopInfo($data['shopCode'], array('Shop.shopCode', 'shopName'));
        $startTime = $data['startTime'];
        $endTime = $data['endTime'];
        if($startTime && !$endTime) {
            $condition['createTime'] = array('EGT', $startTime);
        }
        if(!$startTime && $endTime) {
            $condition['createTime'] = array('ELT', $endTime);
        }
        if($startTime && $endTime) {
            $condition['createTime'] = array('between', array($startTime, $endTime));
        }
        $condition['shopCode'] = $data['shopCode'];
        $condition['totalVolume'] = array('GT', 0);
        $batchCouponMdl = new BatchCouponModel();
        $analysisData = $batchCouponMdl->analysisShopCoupon($condition);
        $assign = array(
            'title' => $shopInfo['shopName'] . '：优惠券分析',
            'shopInfo' => $shopInfo,
            'get' => $data,
            'analysisData' => $analysisData
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 删除优惠券
     */
    public function delBatchCoupon() {
        if(IS_AJAX) {
            $batchCouponCode = I('post.key'); // 优惠券编码
            $batchCouponMdl = new BatchCouponModel();
            $ret = $batchCouponMdl->delBatchCoupon(array('batchCouponCode' => $batchCouponCode));
            if($ret === true) {
                $this->ajaxSucc();
            } else {
                $this->ajaxError('删除失败');
            }
        } else {
            echo 'WELCOME TO HUIQUAN';
        }
    }

    public function sendCoupon($userCode, $shopCode, $price, $consumeCode){
        // 满就送， 派发一张优惠券
        $batchCouponMdl = new BatchCouponModel();
        $sendCouponRet = $batchCouponMdl->sendCoupon($userCode, $shopCode, $price);
        if($sendCouponRet['code'] == C('SUCCESS')){
            $userConsumeMdl = new UserConsumeModel();
            $userConsumeMdl->where(array('consumeCode'=>$consumeCode))->save(array('userCouponCode'=>$sendCouponRet['userCouponCode']));
        }
//        var_dump($sendCouponRet);
        return $sendCouponRet;
    }

    public function getShopBusinessHours(){
        $data = I('post.');
        $shopMdl = new ShopModel();
        $shopInfo = $shopMdl->getShopInfo($data['shopCode'], array('businessHours'));
        $html = '商家营业时间： ';
        if($shopInfo['businessHours']){
            foreach($shopInfo['businessHours'] as $k => $v){
                if($k == count($shopInfo['businessHours']) - 1){
                    $html .= $v['open'].' - '.$v['close'];
                }else{
                    $html .= $v['open'].' - '.$v['close'].', ';
                }
            }
        }else{
            $html .= '未设置';
        }
        $this->ajaxSucc('', null, $html);
    }
}
