<?php
namespace Admin\Controller;
use Common\Model\BankAccountLocalLogModel;
use Common\Model\BankAccountModel;
use Common\Model\ConsumeOrderModel;
use Common\Model\DistrictBankIdModel;
use Common\Model\DistrictModel;
use Common\Model\IcbcModel;
use Common\Model\OrderCouponModel;
use Common\Model\OrderProductModel;
use Common\Model\Pager;
use Common\Model\PreShopModel;
use Common\Model\RefundLogModel;
use Common\Model\ShopModel;
use Common\Model\UserActCodeModel;
use Common\Model\UserConsumeModel;
use Common\Model\UserCouponModel;
use Common\Model\UserModel;
use Common\Model\UtilsModel;
use JPush\Exception\APIRequestException;

/**
 * Created by PhpStorm.
 * User: Huafei Ji
 * Date: 15-8-18
 * Time: 下午7:04
 */
class ConsumeOrderController extends AdminBaseController {

    // 惠圈项目组人员的手机号码
    private $hqMobile = array(
        '13958185810',
        '13066726492',
        '13082825339',
        '18814884950',
        '17098040661',
        '18767174523',
        '18758216513',
        '18667115776',
        '18137782363',
        '15259252239',
        '15224004200',
        '15958015927',
        '13858044594',
        '13965036751',
        '15868178413', // 蒋雨枫
        '15868177459', // 梅璐璐
        '15868179748', // 季华飞
        '15827037207',
        '15071030661',
        '18627885265',
        '13738157214',
        '13588305490',
        '18969128707',
        '15757117822', // 毛毅晗
    );

    /**
     * 按订单日期获得每日的订单统计信息
     */
    public function listOrderStatisticsByDay() {
        $data = I('get.');
        $dataList = array();
        if(!empty($data['startDate']) && !empty($data['endDate'])) {
            $consumeOrderMdl = new ConsumeOrderModel();
            $endTimestamp = strtotime($data['endDate']);
            $aDayTimestamp = 86400;
            for($startTimestamp = strtotime($data['startDate']); $startTimestamp <= $endTimestamp; $startTimestamp += $aDayTimestamp) {
                $con['UNIX_TIMESTAMP(UserConsume.payedTime)'] = array('between', array($startTimestamp, $startTimestamp + $aDayTimestamp -1)); // 支付时间
                $con['ConsumeOrder.status'] = \Consts::PAY_STATUS_PAYED; // 支付状态为已付款
                $con['UserConsume.status'] = \Consts::PAY_STATUS_PAYED; // 支付状态为已付款
                $toDayPayedStatistics = $consumeOrderMdl->sumConsumeStatistics($con);

                unset($con['UNIX_TIMESTAMP(UserConsume.payedTime)']);
                $con['UNIX_TIMESTAMP(UserConsume.consumeTime)'] = array('between', array($startTimestamp, $startTimestamp + $aDayTimestamp -1)); // 支付记录时间
                $con['ConsumeOrder.status'] = \Consts::PAY_STATUS_CANCELED; // 支付状态为已取消付款
                $con['UserConsume.status'] = \Consts::PAY_STATUS_CANCELED; // 支付状态为已付款
                $toDayCanceledStatistics = $consumeOrderMdl->sumConsumeStatistics($con);

                $con['UNIX_TIMESTAMP(UserConsume.consumeTime)'] = array('ELT', $startTimestamp + $aDayTimestamp - 1); // 支付记录时间
                $canceledStatistics = $consumeOrderMdl->sumConsumeStatistics($con);

                unset($con['UNIX_TIMESTAMP(UserConsume.consumeTime)']);
                $con['UNIX_TIMESTAMP(UserConsume.payedTime)'] = array('ELT', $startTimestamp + $aDayTimestamp - 1); // 支付时间
                $con['ConsumeOrder.status'] = \Consts::PAY_STATUS_PAYED; // 支付状态为已付款
                $con['UserConsume.status'] = \Consts::PAY_STATUS_PAYED; // 支付状态为已付款
                $payedStatistics = $consumeOrderMdl->sumConsumeStatistics($con);

                $dataList[] = array(
                    'orderDate' => date('Y-m-d', $startTimestamp),
                    'toDayPayedAmount' => $toDayPayedStatistics['orderAmount'], // 获得成交金额
                    'toDayPayedCount' => $toDayPayedStatistics['consumeCount'], // 获得成交笔数
                    'toDayCanceledAmount' => $toDayCanceledStatistics['orderAmount'], // 获得取消金额
                    'toDayCanceledCount' => $toDayCanceledStatistics['consumeCount'], // 获得取消笔数
                    'canceledAmount' => $canceledStatistics['orderAmount'], // 获得累计取消金额
                    'canceledCount' => $canceledStatistics['consumeCount'], // 获得累计取消笔数
                    'payedAmount' => $payedStatistics['orderAmount'], // 获得累计成交金额
                    'payedCount' => $payedStatistics['consumeCount'], // 获得累计成交笔数
                );
            }
        }

        $assign = array(
            'title' => '每日订单成交统计',
            'get' => I('get.'),
            'dataList' => $dataList,
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 获得订单详情
     */
    public function getOrderInfo() {
        $orderCode = I('get.orderCode');
        $consumeOrderMdl = new ConsumeOrderModel();
        $userConsumeMdl = new UserConsumeModel();
        // 获得订单详情
        $orderInfo = $consumeOrderMdl->getOrderInfo(array('orderCode' => $orderCode));
        $temp = array('orderAmount', 'actualOrderAmount');
        foreach($temp as $v) {
            $orderInfo[$v] = number_format($orderInfo[$v] / C('RATIO'), '2', '.', '');
        }

        $orderCode = $orderInfo['orderCode']; // 订单编码
        // 获得订单购买的商品
        switch($orderInfo['orderType']) {
            case \Consts::ORDER_TYPE_OTHER: // 其他订单
                $goodList = array();
                break;
            case \Consts::ORDER_TYPE_NO_TAKE_OUT: // 堂食订单
                $orderProductMdl = new OrderProductModel();
                $goodList = $orderProductMdl->getOrderProductList(
                    array('orderCode' => $orderCode),
                    array('productName', 'productUnitPrice'),
                    array(array('table' => 'Product', 'con' => 'Product.productId = OrderProduct.productId', 'type' => 'inner'))
                );
                foreach($goodList as $k => $v) {
                    if($v['productUnitPrice']) {
                        $goodList[$k]['productUnitPrice'] = $goodList[$k]['productUnitPrice'] / \Consts::HUNDRED;
                    }
                }
                break;
            case \Consts::ORDER_TYPE_TAKE_OUT: // 外卖订单
                $orderProductMdl = new OrderProductModel();
                $goodList = $orderProductMdl->getOrderProductList(
                    array('orderCode' => $orderCode),
                    array('productName', 'productUnitPrice'),
                    array(array('table' => 'Product', 'con' => 'Product.productId = OrderProduct.productId', 'type' => 'inner'))
                );
                foreach($goodList as $k => $v) {
                    if($v['productUnitPrice']) {
                        $goodList[$k]['productUnitPrice'] = $goodList[$k]['productUnitPrice'] / \Consts::HUNDRED;
                    }
                }
                break;
            case \Consts::ORDER_TYPE_COUPON: // 买券订单
                $orderCouponMdl = new OrderCouponModel();
                $goodList = $orderCouponMdl->getOrderCouponList(array('orderCode' => $orderCode), array());
                $userCouponMdl = new UserCouponModel();
                foreach($goodList as $k => $orderCoupon) {
                    if($orderCoupon['status'] == \Consts::ORDER_COUPON_STATUS_USED) {
                        $userCouponInfo = $userCouponMdl->getUserCouponInfoB(array('orderCouponCode' => $orderCoupon['orderCouponCode']), array('consumeCode'));

                        $goodList[$k]['consumeTime'] = $userCouponMdl->getFieldInfo('UserConsume', array('consumeCode' => $userCouponInfo['consumeCode']), 'consumeTime');
                    } else {
                        $goodList[$k]['consumeTime'] = '';
                    }
                }
                break;
            case \Consts::ORDER_TYPE_ACT: // 活动订单
                $userActCodeMdl = new UserActCodeModel();
                $goodList = $userActCodeMdl->getActCodeList(array('orderCode' => $orderCode));
                break;
            default:
                $goodList = array();
                break;
        }
        $orderInfo['goodList'] = $goodList;

        // 获得商家详情
        $shopMdl = new ShopModel();
        $orderInfo['shopInfo'] = $shopMdl->getShopInfo($orderInfo['shopCode'], array('shopName', 'shopId'));

        // 获得消费者详情
        $userMdl = new UserModel();
        $orderInfo['userInfo'] = $userMdl->getUserInfo(array('userCode' => $orderInfo['clientCode']), array('nickName', 'mobileNbr'));

        // 获得支付记录
        $userPayList = $userConsumeMdl->getUserPayList(
            array('orderCode' => $orderCode),
            array('UserConsume.consumeCode', 'UserConsume.consumeTime', 'UserConsume.payType', 'UserConsume.status', 'UserConsume.userCouponCode', 'deduction', 'realPay', 'platBonus', 'shopBonus', 'bankCardDeduction', 'couponDeduction', 'cardDeduction', 'firstDeduction', 'BankAccount.accountNbrLast4', 'payedTime')
        );

        // 获得支付记录中使用的优惠券和赠送的优惠券
        $userCouponMdl = new UserCouponModel();
        foreach($userPayList as $k => $userPay) {
            $userPayList[$k]['usedCouponList'] = $userCouponMdl->getUserCouponList(array('consumeCode' => $userPay['consumeCode']), array('userCouponNbr', 'couponType'));
            $sendCouponCodeList = explode('|', $userPayList[$k]['userCouponCode']);
            $userPayList[$k]['sendCouponList'] = array();
            if($sendCouponCodeList) {
                $userPayList[$k]['sendCouponList'] = $userCouponMdl->getUserCouponList(array('userCouponCode' => array('IN', $sendCouponCodeList)), array('userCouponNbr', 'couponType'));
            }
        }

        $payCount = count($userPayList);
        $orderInfo['payType'] = $userPayList[$payCount - 1]['payType']; // 支付成功的支付方式
        $orderInfo['couponType'] = $userPayList[$payCount - 1]['usedCouponList'][0]['couponType']; // 支付成功使用的优惠券

        // 获得订单的退款记录
        $refundLogMdl = new RefundLogModel();
        $refundLog = $refundLogMdl->getRefundLogList(array('orderNbr' => $orderInfo['orderNbr']), array(), array(), 'createTime asc');
        foreach($refundLog as $k => $v) {
            $refundLog[$k]['refundPrice'] = $refundLog[$k]['refundPrice'] / \Consts::HUNDRED;
        }

        $page = I('get.page'); // 列表页的页码
        // 路径导航
        $route = array(
            array('title' => '商户订单', 'url' => '/Admin/ConsumeOrder/listConsumeOrder?page=' . $page),
        );
        $user = $this->user;
        $showRefundBtn = in_array($user['mobileNbr'], array('15868179748')) ? true : false;
        $assign = array(
            'title' => '订单详情',
            'orderInfo' => $orderInfo,
            'userPayList' => $userPayList,
            'route' => $route,
            'page' => $page,
            'showRefundBtn' => $showRefundBtn,
            'refundLog' => $refundLog,
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 获得商家订单统计信息
     */
    public function listShopConsumeStatistics() {
        $data = I('get.');
        // 设置订单的消费时间
        if ($data['consumeDateStart'] && $data['consumeDateEnd']) {
            $data['UserConsume.payedTime'] = array('BETWEEN', array($data['consumeDateStart'], $data['consumeDateEnd'] . ' 23:59:59'));
        } elseif ($data['consumeDateStart'] && !$data['consumeDateEnd']) {
            $data['UserConsume.payedTime'] = array('EGT', $data['consumeDateStart']);
        } elseif (! $data['consumeDateStart'] && $data['consumeDateEnd']) {
            $data['UserConsume.payedTime'] = array('ELT', $data['consumeDateEnd'] . ' 23:59:59');
        }
        unset($data['consumeDateStart']);
        unset($data['consumeDateEnd']);

        $data['ConsumeOrder.status'] = \Consts::PAY_STATUS_PAYED; // 支付状态为已付款
        $data['UserConsume.status'] = \Consts::PAY_STATUS_PAYED; // 支付状态为已付款

        // 设置顾客用户手机号码非惠圈项目组人员的手机号码
        if($this->hqMobile && $_SERVER['HTTP_HOST'] == \Consts::WEB_PRODUCTION) {
            $mobileCon = array('NOTIN', $this->hqMobile); // 顾客用户手机号码非惠圈项目组人员的手机号码
//            $data['User.mobileNbr'] = $mobileCon;
        }

        $consumeOrderMdl = new ConsumeOrderModel();
        $consumeStatisticsList = $consumeOrderMdl->listConsumeStatistics($data, $this->pager);
        $consumeStatisticsCount = $consumeOrderMdl->countConsumeStatistics($data);
        $this->pager->setItemCount($consumeStatisticsCount);

        // 获得统计数据
        $consumeStatisticsSum = $consumeOrderMdl->sumConsumeStatistics($data);
        $assign = array(
            'title' => '商户订单统计',
            'dataList' => $consumeStatisticsList,
            'get' => I('get.'),
            'pager' => $this->pager,
            'consumeStatisticsSum' => $consumeStatisticsSum,
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($consumeStatisticsList) ? '' : $this->fetch('ConsumeOrder:listShopConsumeStatisticsWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 导出商户订单统计的excel表格
     */
    public function exportShopConsumeStatistics() {
        $data = I('get.');
        // 设置订单的消费时间
        if ($data['consumeDateStart'] && $data['consumeDateEnd']) {
            $data['ConsumeOrder.orderTime'] = array('BETWEEN', array($data['consumeDateStart'], $data['consumeDateEnd'] . ' 23:59:59'));
        } elseif ($data['consumeDateStart'] && !$data['consumeDateEnd']) {
            $data['ConsumeOrder.orderTime'] = array('EGT', $data['consumeDateStart']);
        } elseif (! $data['consumeDateStart'] && $data['consumeDateEnd']) {
            $data['ConsumeOrder.orderTime'] = array('ELT', $data['consumeDateEnd'] . ' 23:59:59');
        }
        unset($data['consumeDateStart']);
        unset($data['consumeDateEnd']);

        $data['ConsumeOrder.status'] = \Consts::PAY_STATUS_PAYED; // 支付状态为已付款
        $data['UserConsume.status'] = \Consts::PAY_STATUS_PAYED; // 支付状态为已付款

        $consumeOrderMdl = new ConsumeOrderModel();
        $consumeStatisticsList = $consumeOrderMdl->listConsumeStatistics($data, $this->pager);

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
        $title = array('商户号', '内部户商户号', '商户名称', '营业执照编号', '成交金额（元）', '实际支付（元）','成交笔数', '优惠券使用张数', '平台红包抵扣金额（元）', '商家红包抵扣金额（元）', '工行卡专享抵扣金额（元）', '用券金额（元）', '会员卡抵扣金额（元）', '首单立减优惠金额', '人气', '回头客', '客单价', '录入人');
        $column = count($title); // 得到有多少列
        for($i = 0;$i < $column;$i++){
            //设置表格宽度
            $objActSheet->getColumnDimension("$letter[$i]")->setWidth(20);
            //设置表头样式
            $head_row = 1;
//            $objActSheet->getStyle("$letter[$i]$head_row")->getFont()->setSize(16);
            $objActSheet->getStyle("$letter[$i]$head_row")->getFont()->setBold(true);
            //填充表头
            $objActSheet->setCellValue("$letter[$i]$head_row", $title[$i]);
        }

        $row = 2;
        $preShopMdl = new PreShopModel();
        foreach($consumeStatisticsList as $k => $v){
            unset($consumeStatisticsList[$k]['location']);
            $objActSheet->setCellValueExplicit("$letter[0]$row", $v['shopId'], \PHPExcel_Cell_DataType::TYPE_STRING); // 写入商户号
            $objActSheet->getStyle("$letter[0]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValueExplicit("$letter[1]$row", $v['hqIcbcShopNbr'], \PHPExcel_Cell_DataType::TYPE_STRING); // 写入内部户商户号
            $objActSheet->getStyle("$letter[1]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValue("$letter[2]$row", $v['shopName']); // 写入商户名字
            $objActSheet->setCellValueExplicit("$letter[3]$row", $v['licenseNbr'], \PHPExcel_Cell_DataType::TYPE_STRING); // 写入营业执照编号
            $objActSheet->getStyle("$letter[3]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValue("$letter[4]$row", $v['orderAmount']); // 写入成交金额
            $objActSheet->setCellValue("$letter[5]$row", $v['realPay']); // 写入实际支付金额
            $objActSheet->setCellValue("$letter[6]$row", $v['consumeCount']); // 写入成交笔数
            $objActSheet->setCellValue("$letter[7]$row", $v['couponUsed']); // 写入优惠券使用数量
            $objActSheet->setCellValue("$letter[8]$row", $v['platBonus']); // 写入平台红包抵扣金额
            $objActSheet->setCellValue("$letter[9]$row", $v['shopBonus']); // 写入商家红包抵扣金额
            $objActSheet->setCellValue("$letter[10]$row", $v['bankCardDeduction']); // 写入工行优惠抵扣金额
            $objActSheet->setCellValue("$letter[11]$row", $v['couponDeduction']); // 写入优惠券抵扣金额
            $objActSheet->setCellValue("$letter[12]$row", $v['cardDeduction']); // 写入会员卡抵扣金额
            $objActSheet->setCellValue("$letter[13]$row", $v['firstDeduction']); // 写首单立减优惠金额
            $objActSheet->setCellValue("$letter[14]$row", $v['popularity']); // 写入人气
            $objActSheet->setCellValue("$letter[15]$row", $v['repeatCustomers']); // 写入回头客
            $objActSheet->setCellValue("$letter[16]$row", $v['perCustomerTransaction']); // 写入客单价
            // 获得商户的地推录入人
            $preShopInfo = $preShopMdl->getPreShopInfo(array('useShopCode' => $v['shopCode']), array('BMStaff.realName'), array(array('joinTable' => 'BMStaff', 'joinCondition' => 'BMStaff.staffCode = PreShop.developerCode', 'joinType' => 'inner')));
            $objActSheet->setCellValue("$letter[17]$row", $preShopInfo['realName'] ? $preShopInfo['realName'] : ''); // 写入录入人
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
        header('Content-Disposition:attachment;filename="商户订单统计-'.date('Y-m-d', time()).'.xlsx"');
        header("Content-Transfer-Encoding:utf-8");
        $write->save('php://output');
    }

    /**
     * 获得用户支付记录
     */
    public function listConsumeOrder() {


        $consumeOrderMdl = new ConsumeOrderModel();
        // 获得商家订单列表
        $consumeOrderList = $consumeOrderMdl->listConsumeOrder(I('get.'), $this->pager);

        // 获得商家订单总数
        $batchCouponCount = $consumeOrderMdl->countConsumeOrder(I('get.'));
        // 设置页码类的记录总数，同时会更新总页数。
        $this->pager->setItemCount($batchCouponCount);
        // 获得开放城市
        // 判断是否为惠圈人员
        if($_SESSION['USER']['bank_id']!=-1){
            $zone_id=   $_SESSION['USER']['bank_id'];
        }

        $districtBankMdl = new DistrictBankIdModel();
        $cityList = $districtBankMdl->zonelistOpenCity($zone_id);

        $assign = array(
            'title' => '商户订单列表',
            'dataList' => $consumeOrderList,
            'get' => I('get.'),
            'pager' => $this->pager,
            'openCity' => $cityList,
        );
        $this->assign($assign);

        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($consumeOrderList) ? '' : $this->fetch('ConsumeOrder:listConsumeOrderWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 改变用户消费状态
     */
    public function changeUserConsumeStatus() {
        $consumeCode = I('post.consumeCode');
        $status = I('post.status');
        $userConsumeMdl = new UserConsumeModel();
        $ret = $userConsumeMdl->changeUserConsumeStatus($consumeCode, $status);
        if($ret === true) {
            $this->ajaxSucc();
        } else {
            $this->ajaxError('修改失败');
        }
    }

    public function exportConsumeOrder(){
        $data = I('get.');
        unset($data['page']);
        $userConsumeMdl = new UserConsumeModel();
        // 获得账单列表
        $userConsumeList = $userConsumeMdl->listUserConsume($data, new Pager(0), 'clientCode, consumeTime desc, orderTime desc');
        if(empty($userConsumeList)){
            $url = '/Admin/ConsumeOrder/listConsumeOrder?orderTimeStart='.$data['orderTimeStart'].'&orderTimeEnd='.$data['orderTimeEnd'].'&orderNbr='.$data['orderNbr'].'&shopName='.$data['shopName'].'&mobileNbr='.$data['mobileNbr'].'&status='.$data['status'].'&orderType='.$data['orderType'].'&page=1';
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
        $letter = array('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');
        //表头
        $title = array('订单号', '商家名称', '商户号', '地区号', '消费者手机号', '消费金额', '消费时间', '支付时间', '实缴金额', '抵扣金额', '付款方式', '支付状态', '会员卡抵扣金额', '优惠券抵扣金额', '使用的优惠券券号', '平台红包使用量', '商家红包使用量', '工行卡抵扣金额', '首单立减金额', '满就送的优惠券券号', '订单是否完成');

        $column = count($title);
        for($i = 0;$i < $column;$i++){
            //设置表格宽度
            if(in_array($i, array(0,1,6,7,14,19))){
                $objActSheet->getColumnDimension("$letter[$i]")->setWidth(25);
            }else{
                $objActSheet->getColumnDimension("$letter[$i]")->setWidth(15);
            }

            //设置表头样式
            $head_row = 1;
//            $objActSheet->getStyle("$letter[$i]$head_row")->getFont()->setSize(16);
            $objActSheet->getStyle("$letter[$i]$head_row")->getFont()->setBold(true);
            //填充表头
            $objActSheet->setCellValue("$letter[$i]$head_row", $title[$i]);
        }

        $row = 2;
        $a = 0; //有效订单笔数
        $b = 0; //总消费金额
        $c = 0; //总支付金额
        $d = 0; //总线上支付金额
        $e = 0; //总线下支付金额
        $userCouponMdl = new UserCouponModel();
        $couponType = array();
        foreach(C('COUPON_TYPE') as $ctk => $ctv){
            $couponType[$ctv] = C('COUPON_TYPE_NAME.'.$ctk);
        }
        foreach($userConsumeList as $v){
            $objActSheet->setCellValueExplicit("$letter[0]$row", $v['orderNbr'], \PHPExcel_Cell_DataType::TYPE_STRING);
            $objActSheet->getStyle("$letter[0]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValue("$letter[1]$row", $v['shopName']);
            $objActSheet->setCellValueExplicit("$letter[2]$row", $v['hqIcbcShopNbr'], \PHPExcel_Cell_DataType::TYPE_STRING);
            $objActSheet->getStyle("$letter[2]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValueExplicit("$letter[3]$row", $v['areaNbr'], \PHPExcel_Cell_DataType::TYPE_STRING);
            $objActSheet->getStyle("$letter[3]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValueExplicit("$letter[4]$row", $v['userMobileNbr'], \PHPExcel_Cell_DataType::TYPE_STRING);
            $objActSheet->getStyle("$letter[4]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValue("$letter[5]$row", $v['orderAmount']);
            $objActSheet->setCellValue("$letter[6]$row", $v['consumeTime']);
            $objActSheet->setCellValue("$letter[7]$row", $v['payedTime']);
            $objActSheet->setCellValue("$letter[8]$row", $v['realPay']);
            $objActSheet->setCellValue("$letter[9]$row", $v['deduction']);
            $payType = '未选择支付方式';
            if($v['payType'] == 1){
                $payType = '在线支付';
            }elseif($v['payType'] == 2){
                $payType = 'POS机支付';
            }elseif($v['payType'] == 3){
                $payType = '现金支付';
            }elseif($v['payType'] == 5){
                $payType = '0元消费（实物券或者体验券）';
            }
            $objActSheet->setCellValue("$letter[10]$row", $payType);
            $userConsumeStatus = '未知';
            if($v['userConsumeStatus'] == 0){
                $userConsumeStatus = '不能支付';
            }elseif($v['userConsumeStatus'] == 1){
                $userConsumeStatus = '未付款';
            }elseif($v['userConsumeStatus'] == 2){
                $userConsumeStatus = '付款中';
            }elseif($v['userConsumeStatus'] == 3){
                $userConsumeStatus = '已付款';
                $a += 1;
                $b += $v['orderAmount'];
                $c += $v['realPay'];
                if($v['payType'] == 1){
                    $d += $v['realPay'];
                }elseif($v['payType'] == 3){
                    $e += $v['realPay'];
                }
            }elseif($v['userConsumeStatus'] == 4){
                $userConsumeStatus = '已取消付款';
            }elseif($v['userConsumeStatus'] == 5){
                $userConsumeStatus = '付款失败';
            }elseif($v['userConsumeStatus'] == 6){
                $userConsumeStatus = '退款申请中';
            }elseif($v['userConsumeStatus'] == 7){
                $userConsumeStatus = '退款成功';
            }
            $objActSheet->setCellValue("$letter[11]$row", $userConsumeStatus);
            $objActSheet->setCellValue("$letter[12]$row", $v['cardDeduction']);
            $objActSheet->setCellValue("$letter[13]$row", $v['couponDeduction']);

            $usedCouponString = '';
            $usedCouponList = $userCouponMdl->getUserCouponList(array('consumeCode' => $v['consumeCode']), array('userCouponNbr', 'couponType'));
            if($usedCouponList){
                foreach($usedCouponList as $uck => $ucv){
                    if($uck == 0){
                        $usedCouponString .= $couponType[$ucv['couponType']].': '.$ucv['userCouponNbr'];
                    }else{
                        $usedCouponString .= ', '.$ucv['userCouponNbr'];
                    }
                }
            }
            $objActSheet->setCellValue("$letter[14]$row", $usedCouponString);

            $objActSheet->setCellValue("$letter[15]$row", $v['platBonus']);
            $objActSheet->setCellValue("$letter[16]$row", $v['shopBonus']);
            $objActSheet->setCellValue("$letter[17]$row", $v['bankCardDeduction']);
            $objActSheet->setCellValue("$letter[18]$row", $v['firstDeduction']);

            $sendCouponString = '';
            $sendCouponCodeList = UtilsModel::uniqueArr($v['userCouponCode']);
            if($sendCouponCodeList) {
                $sendCouponList = $userCouponMdl->getUserCouponList(array('userCouponCode' => array('IN', $sendCouponCodeList)), array('userCouponNbr', 'couponType'));
                if($sendCouponList){
                    foreach($sendCouponList as $sck => $scv){
                        if($sck == 0){
                            $sendCouponString .= $couponType[$scv['couponType']].': '.$scv['userCouponNbr'];
                        }else{
                            $sendCouponString .= ', '.$scv['userCouponNbr'];
                        }
                    }
                }
            }
            $objActSheet->setCellValue("$letter[19]$row", $sendCouponString);
            $objActSheet->setCellValue("$letter[20]$row", $v['orderConfirm'] ? '已完成' : '未完成');

            $row += 1;
        }
        $row += 4;
        $objActSheet->mergeCells('A'.$row.':F'.$row);
        $objActSheet->getStyle("A$row")->getAlignment()->setHorizontal(\PHPExcel_Style_Alignment::HORIZONTAL_CENTER);
        $objActSheet->getStyle("A$row")->getFont()->setBold(true);
        $objActSheet->setCellValue("A$row", '汇总');
        $row += 1;
        $objActSheet->getStyle("A$row")->getAlignment()->setHorizontal(\PHPExcel_Style_Alignment::HORIZONTAL_CENTER);
        $objActSheet->setCellValue("A$row", '有效订单笔数');
        $objActSheet->getStyle("B$row")->getAlignment()->setHorizontal(\PHPExcel_Style_Alignment::HORIZONTAL_CENTER);
        $objActSheet->setCellValue("B$row", '总消费金额');
        $objActSheet->getStyle("C$row")->getAlignment()->setHorizontal(\PHPExcel_Style_Alignment::HORIZONTAL_CENTER);
        $objActSheet->setCellValue("C$row", '总支付金额');
        $objActSheet->getStyle("D$row")->getAlignment()->setHorizontal(\PHPExcel_Style_Alignment::HORIZONTAL_CENTER);
        $objActSheet->setCellValue("D$row", '总优惠金额');
        $objActSheet->getStyle("E$row")->getAlignment()->setHorizontal(\PHPExcel_Style_Alignment::HORIZONTAL_CENTER);
        $objActSheet->setCellValue("E$row", '总线上支付金额');
        $objActSheet->getStyle("F$row")->getAlignment()->setHorizontal(\PHPExcel_Style_Alignment::HORIZONTAL_CENTER);
        $objActSheet->setCellValue("F$row", '总线下支付金额');
        $row += 1;
        $objActSheet->getStyle("A$row")->getAlignment()->setHorizontal(\PHPExcel_Style_Alignment::HORIZONTAL_CENTER);
        $objActSheet->setCellValue("A$row", $a);
        $objActSheet->getStyle("B$row")->getAlignment()->setHorizontal(\PHPExcel_Style_Alignment::HORIZONTAL_CENTER);
        $objActSheet->setCellValue("B$row", $b);
        $objActSheet->getStyle("C$row")->getAlignment()->setHorizontal(\PHPExcel_Style_Alignment::HORIZONTAL_CENTER);
        $objActSheet->setCellValue("C$row", $c);
        $objActSheet->getStyle("D$row")->getAlignment()->setHorizontal(\PHPExcel_Style_Alignment::HORIZONTAL_CENTER);
        $objActSheet->setCellValue("D$row", ($b - $c));
        $objActSheet->getStyle("E$row")->getAlignment()->setHorizontal(\PHPExcel_Style_Alignment::HORIZONTAL_CENTER);
        $objActSheet->setCellValue("E$row", $d);
        $objActSheet->getStyle("F$row")->getAlignment()->setHorizontal(\PHPExcel_Style_Alignment::HORIZONTAL_CENTER);
        $objActSheet->setCellValue("F$row", $e);

        $write = new \PHPExcel_Writer_Excel2007($excel);
        ob_end_clean();
        header("Pragma: public");
        header("Expires: 0");
        header("Cache-Control:must-revalidate, post-check=0, pre-check=0");
        header("Content-Type:application/force-download");
        header("Content-Type:application/vnd.ms-excel");
        header("Content-Type:application/octet-stream");
        header("Content-Type:application/download");
        header('Content-Disposition:attachment;filename="consume_record.xlsx"');
        header("Content-Transfer-Encoding:utf-8");
        $write->save('php://output');
    }

    /**
     * 查询订单交易结果
     */
    public function queryTransaction() {
        $orderCode = I('get.orderCode');
        // 获得订单详情
        $consumeOrderMdl = new ConsumeOrderModel();
        $orderInfo = $consumeOrderMdl->getOrderInfo(array('orderCode' => $orderCode), array('shopCode', 'orderNbr'));
        // 获得商户详情
        $shopMdl = new ShopModel();
        $shopInfo = $shopMdl->getShopInfo($orderInfo['shopCode'], array('hqIcbcShopNbr'));
        // 获得支付卡号
        $userConsumeMdl = new UserConsumeModel();
        $userConsumeInfo = $userConsumeMdl->getOrderCurrPayInfo($orderCode, array('consumeCode'));
        if($userConsumeInfo) {
            $bALLMdl = new BankAccountLocalLogModel();
            $bankAccountInfo = $bALLMdl->getBankAccountInfo(array('consumeCode' => $userConsumeInfo['consumeCode']), array('BankAccount.bankcard'));
            // 查询交易
            $icbcMdl = new IcbcModel();
            $ret = $icbcMdl->getTransactionRet($shopInfo['sellerid'], $orderInfo['orderNbr'], $bankAccountInfo['bankcard']);
            dump($ret);
        } else {
            dump('无支付记录');
        }
    }

    /**
     * 确认订单
     */
    public function confirmOrder() {
        if(IS_AJAX) {
            $orderCode = I('post.orderCode');
            $consumeOrderMdl = new ConsumeOrderModel();
            $ret = $consumeOrderMdl->updateConsumeOrder(array('orderCode' => $orderCode), array('orderConfirm' => 1));
            if($ret) {
                $this->ajaxSucc();
            } else {
                $this->ajaxError('确认失败');
            }
        }
    }

    /**
     * 订单退款 (针对其他类型且付款成功的订单)
     */
    public function refund() {
        if(IS_AJAX) {
            $orderCode = I('post.orderCode');
            $userConsumeMdl = new UserConsumeModel();

            $userConsumeInfo = $userConsumeMdl->getOrderCurrPayInfo($orderCode, array('consumeCode'));
            $refundRet = $userConsumeMdl->refund($userConsumeInfo['consumeCode']);
//            $this->ajaxError($refundRet);
            if($refundRet['code'] === C('SUCCESS')) {
                $this->ajaxSucc('退款成功！');
            } else {
                $this->ajaxError($refundRet['code']);
            }
        }
    }
}
