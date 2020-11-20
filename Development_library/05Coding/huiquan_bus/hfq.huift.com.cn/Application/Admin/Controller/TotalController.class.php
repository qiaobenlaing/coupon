<?php
/**
 * Created by PhpStorm.
 * User: 98041
 * Date: 2019/6/6
 * Time: 13:06
 */

namespace Admin\Controller;


use Common\Model\ConsumeOrderModel;

class TotalController extends AdminBaseController
{
    /**
     * 统计列表
     */
    public function listConsumeTotal(){
        $data = I("get.");
        $consumerOrderModel = new ConsumeOrderModel();
        $list =   $consumerOrderModel->listConsumeStatistics($data,$this->pager);
        $count = $consumerOrderModel->countConsumeStatistics($data);
        $this->pager->setItemCount($count);
        // 获得统计数据
        $consumeStatisticsSum = $consumerOrderModel->sumConsumeStatistics($data);
        //所属商圈
        //判断用户所属商圈列表
        $where = array();
        if($_SESSION['USER']['bank_id']!=-1){
            $where['id'] = $_SESSION['USER']['bank_id'];
        }

        $bankList = D("Bank")->where($where)->field("id,name")->select();
        $assign = array(
            'title' => '商户统计',
            'dataList' => $list,
            'bankList'=> $bankList,
            'get' => I('get.'),
            'pager' => $this->pager,
            'consumeStatisticsSum' => $consumeStatisticsSum,
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 导出统计数据
     */
    public function exportShopConsumeStatistics(){
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
        $title = array('商户编号','商户名称',  '成交金额（元）', '实际支付（元）','成交笔数', '优惠券使用张数', '用券金额（元）', '人气', '回头客', '客单价');
        $column = count($title); // 得到有多少列
        for($i = 0;$i < $column;$i++){
            //设置表格宽度
            $objActSheet->getColumnDimension("$letter[$i]")->setWidth(20);
            //设置表头样式
            $head_row = 1;
            $objActSheet->getStyle("$letter[$i]$head_row")->getFont()->setSize(16);
            $objActSheet->getStyle("$letter[$i]$head_row")->getFont()->setBold(true);
            //填充表头
            $objActSheet->setCellValue("$letter[$i]$head_row", $title[$i]);
        }

        $row = 2;
        $data = I("get.");
        $consumerOrderModel = new ConsumeOrderModel();
        $list =   $consumerOrderModel->listConsumeStatistics($data,$this->pager);
        foreach($list as $k => $v){
            $objActSheet->setCellValueExplicit("$letter[0]$row", $v['shopCode'], \PHPExcel_Cell_DataType::TYPE_STRING); // 写入商户号
            $objActSheet->getStyle("$letter[0]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValue("$letter[2]$row", $v['shopName']); // 写入商户名字
            $objActSheet->getStyle("$letter[3]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValue("$letter[4]$row", $v['orderAmount']); // 写入成交金额
            $objActSheet->setCellValue("$letter[5]$row", $v['realPay']); // 写入实际支付金额
            $objActSheet->setCellValue("$letter[6]$row", $v['consumeCount']); // 写入成交笔数
            $objActSheet->setCellValue("$letter[7]$row", $v['couponUsed']); // 写入优惠券使用数量
            $objActSheet->setCellValue("$letter[11]$row", $v['couponDeduction']); // 写入优惠券抵扣金额
            $objActSheet->setCellValue("$letter[14]$row", $v['popularity']); // 写入人气
            $objActSheet->setCellValue("$letter[15]$row", $v['repeatCustomers']); // 写入回头客
            $objActSheet->setCellValue("$letter[16]$row", $v['perCustomerTransaction']); // 写入客单价
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
        header('Content-Disposition:attachment;filename="商户统计-'.date('Y-m-d', time()).'.xlsx"');
        header("Content-Transfer-Encoding:utf-8");
        $write->save('php://output');
    }
}
