<?php
/**
 * Created by PhpStorm.
 * User: Huafei Ji
 * Date: 15-7-16
 * Time: 下午2:52
 */
namespace Admin\Controller;
use Common\Model\UserConsumeModel;
use Org\FirePHPCore\FP;
use Common\Model\UserCouponModel;
class UserCouponController extends AdminBaseController {

    /**
     * 释放优惠券
     */
    public function release() {
//        $userCouponCode = I('post.userCouponCode'); // 用户优惠券编码
        $page = I('post.page'); // 页码
        $consumeCode = I('post.key'); // 支付编码
        $userConsumeMdl = new UserConsumeModel();
        $ret = $userConsumeMdl->cancelBankcardPay($consumeCode);
//        $userCouponMdl = new UserCouponModel();
//        $ret = $userCouponMdl->releaseUserCoupon($userCouponCode);
        if($ret['code'] === C('SUCCESS')) {
            $this->ajaxSucc('', array('page' => $page));
        } else {
            $this->ajaxError('释放失败', $ret);
        }
    }

    /**
     * 删除
     */
    public function delUserCoupon() {
        $userCouponCode = I('post.userCouponCode');
        $userCouponMdl = new UserCouponModel();
        $ret = $userCouponMdl->delUserCoupon($userCouponCode);
        if($ret) {
            $this->ajaxSucc('');
        } else {
            $this->ajaxError('删除失败');
        }
    }

    /**
     * 获得优惠券的使用详细信息
     */
    public function getConsumeInfo() {
        if(IS_AJAX) {
            $userCouponCode = I('post.userCouponCode');
            $userCouponMdl = new UserCouponModel();
            $consumeInfo = $userCouponMdl->getConsumeInfo($userCouponCode);
            $this->ajaxSucc('', $consumeInfo);
        }
    }

    /**
     * 用户优惠券
     */
    public function listUsCoupon() {
        $userCouponMdl = new UserCouponModel();
        $batchCouponList = $userCouponMdl->listUserCoupon(I('get.'), $this->pager);
        $batchCouponCount = $userCouponMdl->countUserCoupon(I('get.'));
        $this->pager->setItemCount($batchCouponCount);
        foreach(C('SHOP_NORMAL_COUPON') as $k => $v){
            $couponTypeList[] = array(
                'name'  => C('COUPON_TYPE_NAME.'.$k),
                'value' => $v
            );
        }
        $assign = array(
            'title' => '用户优惠券',
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
            $html = empty($batchCouponList) ? '' : $this->fetch('UserCoupon:listUsCouponWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 获得某个用户的优惠券
     */
    public function getUserCouponList() {
        $userCouponMdl = new UserCouponModel();
        $batchCouponList = $userCouponMdl->listUserCoupon(I('get.'), $this->pager);
        $batchCouponCount = $userCouponMdl->countUserCoupon(I('get.'));
        $this->pager->setItemCount($batchCouponCount);

        $assign = array(
            'title' => '用户优惠券',
            'dataList' => $batchCouponList,
            'get' => I('get.'),
            'nowTime' => time(),
            'pager' => $this->pager,
        );
        $this->assign($assign);

        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($batchCouponList) ? '' : $this->fetch('UserCoupon:UserInfoCouponWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    public function exportCoupon(){
        $userCouponMdl = new UserCouponModel();
        $batchCouponList = $userCouponMdl->listUserCoupon(I('get.'), $this->pager);
        if(empty($batchCouponList)){
            $url = '/Admin/UserCoupon/listUsCoupon';
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
        $title = array('用户手机号', '商户名称', '优惠券批次', '券号', '类型', '获取时间', '状态', '使用时间');

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
        foreach($batchCouponList as $v){
            $objActSheet->setCellValueExplicit("$letter[0]$row", $v['mobileNbr'], \PHPExcel_Cell_DataType::TYPE_STRING);
            $objActSheet->getStyle("$letter[0]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValue("$letter[1]$row", $v['shopName']);
            $objActSheet->setCellValueExplicit("$letter[2]$row", $v['batchNbr'], \PHPExcel_Cell_DataType::TYPE_STRING);
            $objActSheet->getStyle("$letter[2]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValueExplicit("$letter[3]$row", $v['userCouponNbr'], \PHPExcel_Cell_DataType::TYPE_STRING);
            $objActSheet->getStyle("$letter[3]$row")->getNumberFormat()->setFormatCode("@");
            $couponType = '';
            switch($v['couponType']){
                case C('COUPON_TYPE.N_PURCHASE'):
                    $couponType = C('COUPON_TYPE_NAME.N_PURCHASE');
                    break;
                case C('COUPON_TYPE.REDUCTION'):
                    $couponType = C('COUPON_TYPE_NAME.REDUCTION');
                    break;
                case C('COUPON_TYPE.DISCOUNT'):
                    $couponType = C('COUPON_TYPE_NAME.DISCOUNT');
                    break;
                case C('COUPON_TYPE.PHYSICAL'):
                    $couponType = C('COUPON_TYPE_NAME.PHYSICAL');
                    break;
                case C('COUPON_TYPE.EXPERIENCE'):
                    $couponType = C('COUPON_TYPE_NAME.EXPERIENCE');
                    break;
                case C('COUPON_TYPE.EXCHANGE'):
                    $couponType = C('COUPON_TYPE_NAME.EXCHANGE');
                    break;
                case C('COUPON_TYPE.VOUCHER'):
                    $couponType = C('COUPON_TYPE_NAME.VOUCHER');
                    break;
                case C('COUPON_TYPE.NEW_CLIENT_REDUCTION'):
                    $couponType = C('COUPON_TYPE_NAME.NEW_CLIENT_REDUCTION');
                    break;
                case C('COUPON_TYPE.SEND_INVITER'):
                    $couponType = C('COUPON_TYPE_NAME.SEND_INVITER');
                    break;

            }
            $objActSheet->setCellValue("$letter[4]$row", $couponType);
            $objActSheet->setCellValue("$letter[5]$row", $v['applyTime']);
            switch($v['status']){
                case 1:
                    $status = '未使用';
                    break;
                case 2:
                    $status = '已使用';
                    break;
                case 3:
                    $status = '冻结';
                    break;
                default:
                    $status = '禁用';
                    break;
            }
            if($v['expireTime'] <= date('Y-m-d H:i:s', time())){
                $status .= ' (已失效)';
            }
            $objActSheet->setCellValue("$letter[6]$row", $status);
            if($v['status'] != 2){
                $v['consumeTime'] = '';
            }
            $objActSheet->setCellValue("$letter[7]$row", $v['consumeTime']);
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
        header('Content-Disposition:attachment;filename="用户优惠券-'.date('Y-m-d', time()).'.xlsx"');
        header("Content-Transfer-Encoding:utf-8");
        $write->save('php://output');
    }


    //改变用户添加进微信卡包状态微信（乔本亮添加）
    public function inWeixinCard(){
            $data = I('get.');
            $userCouponModel = new UserCouponModel();
            $res =      $userCouponModel->editUserCoupon(array("userCouponCode"=>$data['userCouponCode']),array("inWeixinCard"=>$data['inWeixinCard']));
            if ($res['code'] === C('SUCCESS')) {
                $this->ajaxSucc('操作成功!');
            } else {
                $this->ajaxError($res['code']);
            }
    }


}