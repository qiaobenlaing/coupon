<?php
/**
 * Created by PhpStorm.
 * User: Jihuafei
 * Date: 15-12-9
 * Time: 下午2:35
 */
namespace Admin\Controller;
use Common\Model\BankAccountModel;
class BankAccountController extends AdminBaseController {

    /**
     * 用户银行卡列表
     */
    public function listBankAccount() {
        $bankAccount = new BankAccountModel();
        $filterData = I('get.');
        $getBankAccountList = $bankAccount->listBankAccount($filterData, $this->pager);
        $bankAccountCount = $bankAccount->bankAccountCount(I('get.'));
        $this->pager->setItemCount($bankAccountCount);
        $assign = array(
            'title' => '用户银行卡列表',
            'dataList' => $getBankAccountList,
            'get' => I('get.'),
            'pager' => $this->pager
        );
        $this->assign($assign);
        if (! IS_AJAX) {
            $this->display();
        } else {
            $html = empty($getBankAccountList) ? '' : $this->fetch('BankAccount:listBankAccountWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    public function exportBankAccount(){
        $bankAccount = new BankAccountModel();
        $filterData = I('get.');
        $getBankAccountList = $bankAccount->listBankAccount($filterData, $this->pager);
        if(empty($getBankAccountList)){
            $url = '/Admin/BankAccount/listBankAccount';
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
        $title = array('用户手机号', '卡号', '预留手机号', '创建时间', '签约状态', '错误类型');

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

        $row = 2;
        foreach($getBankAccountList as $v){
            $objActSheet->setCellValueExplicit("$letter[0]$row", $v['userMobilNbr'], \PHPExcel_Cell_DataType::TYPE_STRING);
            $objActSheet->getStyle("$letter[0]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValueExplicit("$letter[1]$row", $v['accountNbrPre6'].'******'.$v['accountNbrLast4'], \PHPExcel_Cell_DataType::TYPE_STRING);
            $objActSheet->getStyle("$letter[1]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValueExplicit("$letter[2]$row", $v['mobileNbr'], \PHPExcel_Cell_DataType::TYPE_STRING);
            $objActSheet->getStyle("$letter[2]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValue("$letter[3]$row", $v['createTime']);
            $status = '';
            switch($v['status']){
                case 0:
                    $status = '禁用';
                    break;
                case 1:
                    $status = '未签订协议';
                    break;
                case 2:
                    $status = '已签订协议';
                    break;
                case 3:
                    $status = '已解除协议';
                    break;
                case 4:
                    $status = '已取消签订协议';
                    break;
            }
            $objActSheet->setCellValue("$letter[4]$row", $status);
            $objActSheet->setCellValue("$letter[5]$row", $v['errMsg']);

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
        header('Content-Disposition:attachment;filename="用户银行卡-'.date('Y-m-d', time()).'.xlsx"');
        header("Content-Transfer-Encoding:utf-8");
        $write->save('php://output');
    }
}