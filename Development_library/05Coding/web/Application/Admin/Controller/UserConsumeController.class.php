<?php
/**
 * Created by PhpStorm.
 * User: Huafei Ji
 * Date: 16-3-3
 * Time: 下午2:36
 */
namespace Admin\Controller;

use Common\Model\UserConsumeModel;
use Common\Model\UserCouponModel;
use Common\Model\UtilsModel;

class UserConsumeController extends AdminBaseController {

    /**
     * 获得账单
     */
    public function listUserConsume() {
        $userConsumeMdl = new UserConsumeModel();
        $userConsumeList = $userConsumeMdl->listUserConsume(I('get.'), $this->pager, '', array('ConsumeOrder.orderAmount',
            'BatchCoupon.function', 'shopName','OrderCoupon.status'=>'userOrderStatus', 'User.mobileNbr' => 'userMobileNbr', 'User.nickName',
            'deduction', 'realPay', 'ConsumeOrder.orderNbr','UserConsume.platBonus', 'UserConsume.shopBonus','OrderCoupon.couponCode',
            'UserConsume.status' => 'userConsumeStatus', 'consumeCode', 'couponUsed', 'firstDeduction', 'usedUserCouponCode', 'consumeTime',
            'realPay', 'UserConsume.payedTime'));
        // 计算平台补贴金额
        $userCouponMdl = new UserCouponModel();
        foreach($userConsumeList as $k => $userConsume) {
            $couponPlatSubsidy = 0;
            if($userConsume['usedUserCouponCode']) {
                $usedUserCouponCodeArr = explode('|', $userConsume['usedUserCouponCode']);
                foreach($usedUserCouponCodeArr as $userCouponCode) {
                    $userCouponInfo = $userCouponMdl->getUserCouponInfoB(array('userCouponCode' => $userCouponCode), array('BatchCoupon.hqSubsidy', 'BatchCoupon.couponType'));
                    if($userCouponInfo['couponType'] == \Consts::COUPON_TYPE_DISCOUNT) {
                        $couponPlatSubsidy += $userConsume['orderAmount'] * $userCouponInfo['hqSubsidy']; // 优惠券平台补贴金额，单位：分
                    } else {
                        $couponPlatSubsidy += $userCouponInfo['hqSubsidy']; // 优惠券平台补贴金额，单位：分
                    }
                }
            }
            $userConsumeList[$k]['hqSubsidy'] = $couponPlatSubsidy / \Consts::HUNDRED + $userConsume['platBonus'] + $userConsume['firstDeduction'];
        }

        $userConsumeCount = $userConsumeMdl->countUserConsume(I('get.'));
        $this->pager->setItemCount($userConsumeCount);
        $assign = array(
            'title' => '中旅巴士-惠圈账单',
            'dataList' => $userConsumeList,
            'get' => I('get.'),
            'pager' => $this->pager,
        );
        $this->assign($assign);
        if(!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($userConsumeList) ? '' : $this->fetch('UserConsume:listUserConsumeWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    public function exportUserConsume(){
        $userConsumeMdl = new UserConsumeModel();
        $userConsumeList = $userConsumeMdl->listUserConsume(I('get.'), $this->pager, '', array('ConsumeOrder.orderAmount',
            'BatchCoupon.function', 'shopName','OrderCoupon.status'=>'userOrderStatus', 'User.mobileNbr' => 'userMobileNbr', 'User.nickName',
            'deduction', 'realPay', 'UserConsume.platBonus', 'OrderCoupon.couponCode','UserConsume.shopBonus',
            'UserConsume.status' => 'userConsumeStatus', 'consumeCode', 'couponUsed', 'firstDeduction', 'usedUserCouponCode', 'consumeTime',
            'realPay', 'UserConsume.payedTime'));

        if(empty($userConsumeList)){
            $url = '/Admin/UserConsume/listUserConsume';
            header('content-type:text/html;charset=utf-8;');
            echo '<script>alert("没有符合条件的数据！");</script>';
            echo "<SCRIPT LANGUAGE=\"JavaScript\">location.href='$url'</SCRIPT>";
            exit;
        }

        //导入类库
        Vendor("PHPExcel.PHPExcel");
        $excel = new \PHPExcel();
        $excel->getDefaultStyle()->getAlignment()->setHorizontal(\PHPExcel_Style_Alignment::HORIZONTAL_LEFT); // 水平向左对齐
        $excel->getDefaultStyle()->getAlignment()->setVertical(\PHPExcel_Style_Alignment::VERTICAL_CENTER); // 上下居中对齐
        $excel->getDefaultStyle()->getAlignment()->setWrapText(1);
        $objActSheet = $excel->getActiveSheet();
        //列
        $letter = array('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');
        //表头
        $title = array('商户名称', '中旅用户','券票名称（功能）','券号','实际支付','支付状态','使用状态','生成时间 ');
        $column = count($title);
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

        $row = 2; // 从第2行开始写入数据

        foreach($userConsumeList as $v){

            $userConsumeList['totalCount'] +=1;
            $userConsumeList['totalConsume'] += $v['realPay'];
            if($v['userOrderStatus']==30){
                $userConsumeList['totalUsedMoney'] +=$v['realPay'];
                $userConsumeList['totalUsedCount'] +=1;
            }else if($v['userOrderStatus']==11){
                $userConsumeList['totalCount'] -=1;
                $userConsumeList['totalConsume'] -= $v['realPay'];
            }



            $objActSheet->setCellValueExplicit("$letter[0]$row", $v['shopName'], \PHPExcel_Cell_DataType::TYPE_STRING); //商户名称
            $objActSheet->getStyle("$letter[0]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValue("$letter[1]$row", $v['nickName']); // 用户名称
            $objActSheet->getStyle("$letter[1]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValueExplicit("$letter[2]$row", $v['function'], \PHPExcel_Cell_DataType::TYPE_STRING); //券票功能
            $objActSheet->getStyle("$letter[2]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValue("$letter[3]$row", $v['couponCode']); //核销券号
            $objActSheet->getStyle("$letter[3]$row")->getFont()->setBold(true);
            $objActSheet->getStyle("$letter[3]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValue("$letter[4]$row", $v['realPay']); //实际支付
            $objActSheet->getStyle("$letter[4]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValue("$letter[5]$row", $this->ConsumeStatus($v['userConsumeStatus'])); //支付状态
            $objActSheet->getStyle("$letter[5]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValue("$letter[6]$row", $this->OrderStatus($v['userOrderStatus'])); //使用状态
            $objActSheet->getStyle("$letter[6]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValue("$letter[7]$row", $v['consumeTime']); //生成时间
            $objActSheet->getStyle("$letter[7]$row")->getNumberFormat()->setFormatCode("@");

            $row += 1;
        }

        $objActSheet->setCellValue("$letter[2]$row", '总支付笔数');
        $objActSheet->setCellValue("$letter[3]$row", $userConsumeList['totalCount']); //总计
        $objActSheet->getStyle("$letter[2]$row")->getFont()->setBold(true);
        $objActSheet->getStyle("$letter[3]$row")->getFont()->setBold(true);

        $objActSheet->setCellValue("$letter[4]$row", '总支付金额');
        $objActSheet->setCellValue("$letter[5]$row", $userConsumeList['totalConsume']); //总计
        $objActSheet->getStyle("$letter[4]$row")->getFont()->setBold(true);
        $objActSheet->getStyle("$letter[5]$row")->getFont()->setBold(true);

        $toal =  $row+3;

        $objActSheet->setCellValue("$letter[2]$toal", '总使用笔数');
        $objActSheet->setCellValue("$letter[3]$toal", $userConsumeList['totalUsedCount']); //总计
        $objActSheet->getStyle("$letter[2]$toal")->getFont()->setBold(true);
        $objActSheet->getStyle("$letter[3]$toal")->getFont()->setBold(true);

        $objActSheet->setCellValue("$letter[4]$toal", '总使用金额');
        $objActSheet->setCellValue("$letter[5]$toal", $userConsumeList['totalUsedMoney']); //总计
        $objActSheet->getStyle("$letter[4]$toal")->getFont()->setBold(true);
        $objActSheet->getStyle("$letter[5]$toal")->getFont()->setBold(true);

        $write = new \PHPExcel_Writer_Excel2007($excel);
        ob_end_clean();
        header("Pragma: public");
        header("Expires: 0");
        header("Cache-Control:must-revalidate, post-check=0, pre-check=0");
        header("Content-Type:application/force-download");
        header("Content-Type:application/vnd.ms-excel");
        header("Content-Type:application/octet-stream");
        header("Content-Type:application/download");
        header('Content-Disposition:attachment;filename="账单导出-'.date('Y-m-d', time()).'.xlsx"');
        header("Content-Transfer-Encoding:utf-8");
        $write->save('php://output');
    }

    /**使用状态
     * @param $status
     * @return string
     */
    private function  OrderStatus($status){
        switch ($status){
            case 10:
                $text = '订单未付款，不可用';
                break;
            case 11:
                $text = '已退款，不可用';
                break;
            case 12:
                $text = '申请退款，不可用';
                break;
            case 20:
                $text = '可用';
                break;
            case 30:
                $text = '已使用';
                break;
            default:
                $text = '未知';
                break;
        }
        return $text;
    }

    /**消费状态
     * @param $status
     * @return string
     */
    private function ConsumeStatus($status){
        switch ($status){
            case 0:
                $text = "不能支付";
                break;
            case 1:
                $text = "未付款";
                break;
            case 2:
                $text = "付款中";
                break;
            case 3:
                $text = "已付款";
                break;
            case 4:
                $text = "已取消付款";
                break;
            case 5:
                $text = "付款失败";
                break;
            case 6:
                $text = "退款申请中";
                break;
            case 7:
                $text = "退款成功";
                break;
            case 8:
                $text = "部分退款成功";
                break;
            default:
                $text = '未知';
                break;
        }
        return $text;
    }

    /**
     * 结算惠圈的补贴
     */
    public function settleHqSubsidy() {
        if(IS_AJAX) {
            $data = I('post.');
            $userConsumeMdl = new UserConsumeModel();
            $ret = $userConsumeMdl->settleHqSubsidy($data);
            if($ret['code'] === C('SUCCESS')) {
                $this->ajaxSucc($ret);
            } else {
                $this->ajaxError('操作失败，请重试');
            }
        } else {
            redirect('/Admin');
        }
    }
}
