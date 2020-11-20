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
        $userConsumeList = $userConsumeMdl->listUserConsume(I('get.'), $this->pager, '', array('ConsumeOrder.orderAmount', 'ConsumeOrder.orderType','shopName', 'User.mobileNbr' => 'userMobileNbr','User.userCode','User.nickName','User.realName','User.bank_id','deduction', 'realPay', 'platBonus', 'shopBonus', 'UserConsume.status' => 'userConsumeStatus', 'consumeCode', 'couponUsed', 'firstDeduction', 'usedUserCouponCode', 'consumeTime', 'realPay', 'UserConsume.payedTime', 'UserConsume.subsidySettlementStatus', 'UserConsume.couponUsed', 'UserConsume.orderCode'));

        $orderCodeList = array_column($userConsumeList,'orderCode');
        if(count($orderCodeList) > 0){
            $couponCodeList = M('ordercoupon')//设计只显示一张券码
            ->field('couponCode couponCode,orderCode,function')
                ->where(array('orderCode' => array('in',$orderCodeList)))
                ->join('batchcoupon on batchcoupon.batchcouponCode = ordercoupon.batchcouponCode')
                ->group('orderCode')
                ->select();
            foreach ($userConsumeList as &$v){
                foreach ($couponCodeList as $v1){
                    if($v['orderCode'] == $v1['orderCode']){
                        $v['couponNbr'] = $v1['couponCode'];
                        $v['function'] = $v1['function'];
                    }
                }
                $v['bank_name'] = getName("Bank","name","id='".$v['bank_id']."'");
            }
        }


        $userConsumeCount = $userConsumeMdl->countUserConsume(I('get.'));
        $this->pager->setItemCount($userConsumeCount);
        $assign = array(
            'title' => '惠圈账单',
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
    public function exportUserConsume(){
        $userConsumeMdl = new UserConsumeModel();
        $userConsumeList = $userConsumeMdl->listUserConsume(I('get.'), $this->pager, '', array('ConsumeOrder.orderAmount', 'ConsumeOrder.orderType','shopName', 'User.mobileNbr' => 'userMobileNbr','User.userCode','User.nickName','User.realName','User.bank_id','deduction', 'realPay', 'platBonus', 'shopBonus', 'UserConsume.status' => 'userConsumeStatus', 'consumeCode', 'couponUsed', 'firstDeduction', 'usedUserCouponCode', 'consumeTime', 'realPay', 'UserConsume.payedTime', 'UserConsume.subsidySettlementStatus', 'UserConsume.couponUsed','UserConsume.orderCode'));
        $orderCodeList = array_column($userConsumeList,'orderCode');
        if(count($orderCodeList) > 0){
            $couponCodeList = M('ordercoupon')//设计只显示一张券码
            ->field('couponCode couponCode,orderCode,function')
                ->where(array('orderCode' => array('in',$orderCodeList)))
                ->join('batchcoupon on batchcoupon.batchcouponCode = ordercoupon.batchcouponCode')
                ->group('orderCode')
                ->select();
            foreach ($userConsumeList as &$v){
                foreach ($couponCodeList as $v1){
                    if($v['orderCode'] == $v1['orderCode']){
                        $v['couponNbr'] = $v1['couponCode'];
                        $v['function'] = $v1['function'];
                    }
                }
                $v['bank_name'] = getName("Bank","name","id='".$v['bank_id']."'");
            }
        }
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
        $title = array('手机', '用户昵称','姓名','商户','券号','券票名称（功能）','所属商圈','生成时间','实际支付','支付状态');
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

            $objActSheet->setCellValueExplicit("$letter[0]$row", $v['userMobileNbr'], \PHPExcel_Cell_DataType::TYPE_STRING); //手机
            $objActSheet->getStyle("$letter[0]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValue("$letter[1]$row", $v['nickName']); // 用户名称
            $objActSheet->getStyle("$letter[1]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValueExplicit("$letter[2]$row", $v['realName'], \PHPExcel_Cell_DataType::TYPE_STRING); //真实名称
            $objActSheet->getStyle("$letter[2]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValue("$letter[3]$row", $v['shopName']); //商店名称
            $objActSheet->getStyle("$letter[3]$row")->getFont()->setBold(true);
            $objActSheet->getStyle("$letter[3]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValue("$letter[4]$row", $v['couponNbr']); //券码
            $objActSheet->getStyle("$letter[4]$row")->getFont()->setBold(true);
            $objActSheet->getStyle("$letter[4]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValue("$letter[5]$row", $v['function']); //功能
            $objActSheet->getStyle("$letter[5]$row")->getFont()->setBold(true);
            $objActSheet->getStyle("$letter[5]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValue("$letter[6]$row", $v['bank_name']); //商圈
            $objActSheet->getStyle("$letter[6]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValue("$letter[7]$row", $v['consumeTime']); //生成时间
            $objActSheet->getStyle("$letter[7]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValue("$letter[8]$row", $v['realPay']); //实际支付
            $objActSheet->getStyle("$letter[8]$row")->getNumberFormat()->setFormatCode("@");
            switch ($v['userConsumeStatus']){
                case 0:
                    $userConsumeStatus = '不能支付';
                    break;
                case 1:
                    $userConsumeStatus = '未付款';
                    break;
                case 2:
                    $userConsumeStatus = '付款中';
                    break;
                case 3:
                    $userConsumeStatus = '已付款';
                    break;
                case 4:
                    $userConsumeStatus = '已取消付款';
                    break;
                case 5:
                    $userConsumeStatus = '付款失败';
                    break;
                case 6:
                    $userConsumeStatus = '退款申请中';
                    break;
                case 7:
                    $userConsumeStatus = '退款成功';
                    break;
                case 8:
                    $userConsumeStatus = '部分退款成功';
                    break;
                default : $userConsumeStatus = '未知';
            }
            $objActSheet->setCellValue("$letter[9]$row", $userConsumeStatus); //生成时间
            $objActSheet->getStyle("$letter[9]$row")->getNumberFormat()->setFormatCode("@");
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
}