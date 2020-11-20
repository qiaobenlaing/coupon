<?php
/**
 * Feedback Controller
 *
 * User: jiangyufeng
 * Date: 2015-05-19
 */
namespace Admin\Controller;
use Common\Model\BatchCouponModel;
use Common\Model\DistrictModel;
use Common\Model\LiquidationModel;
use Common\Model\OrderCouponModel;
use Common\Model\RefundLogModel;
use Common\Model\ShopModel;
use Common\Model\UserActCodeModel;
use Org\FirePHPCore\FP;
use Common\Model\Pager;
use Common\Model\UserConsumeModel;

class LiquidationController extends AdminBaseController {

    /**
     * 查询清算列表
     */
    public function getLiquidationRecord() {
        $date = I('get.date');
        $areaNbr = I('get.areaNbr');
        if($date){
            $userConsumeMdl = new UserConsumeModel();
            $result = $userConsumeMdl->shopFundSettlement($areaNbr, $date, 0);
        }
        $districtMdl = new DistrictModel();
        $districtList = $districtMdl->getAreaNbrList(array('areaNbr', 'name'), array('areaNbr' => array('NEQ', '')));
        $assign = array(
            'title' => '清算数据',
            'get' => I('get.'),
            'HUNDRED' => \Consts::HUNDRED,
            'dataList' => isset($result['log'])?$result['log']:array(),
            'districtList' => $districtList
        );
        $this->assign($assign);
        if (IS_AJAX) {
            $date = I('post.date');
            $areaNbr = I('post.areaNbr');
            $liquidationMdl = new LiquidationModel();
            $condition = array(
                'isLiquidated' => array('neq', 1),
                'transDate' => $date,
            );
            if($areaNbr){
                $condition['areaNbr'] = $areaNbr;
            }
            $unLiquidationList = $liquidationMdl->listLiquidation($condition);
            if($unLiquidationList){
                $this->ajaxError('有清算文件未处理!');
            }
            $ret = $this->generateLiquidationFile($date, $areaNbr);
            $this->ajaxSucc('', $ret);
        }
        $this->display();
    }


    /**
     * 生成清算文件
     */
    public function generateLiquidationFile($date, $areaNbr){
        if(!is_dir('./Public/bankAccountLog/')){
            mkdir('./Public/bankAccountLog/');
        }
//        $liquidateDay = date('Y-m-d', strtotime($date) + 86400); //清算日期 = 交易日期的后一天
        $liquidateDay = date('Y-m-d', strtotime($date)); //清算日期 = 交易日期
        //文件存放目录
        $uploadPath = './Public/bankAccountLog/'.$liquidateDay.'/';
        if(!is_dir($uploadPath)){
            mkdir($uploadPath);
        }

        if($areaNbr){
            $districtList[] = array('areaNbr' => $areaNbr);
        }else{
            $districtMdl = new DistrictModel();
            $districtList = $districtMdl->getAreaNbrList(array('areaNbr'), array('areaNbr' => array('NEQ', '')));
        }

        foreach($districtList as $dv){
            //文件名
            for($i = 1; ; $i++){
                $i = str_pad($i, 3, '0', STR_PAD_LEFT);
                $fileName = 'huiquan.settle.'. substr($dv['areaNbr'], 0, 4) .'.'.date('Ymd', strtotime($liquidateDay)).'.'.$i.'.txt';
                if(!file_exists($uploadPath.$fileName)){
                    break;
                }
            }

            //数据
            $userConsumeMdl = new UserConsumeModel();
            $result = $userConsumeMdl->shopFundSettlement($dv['areaNbr'], $date);
            if($result['records']){
                //打开文件，若没有则创建文件
                $myfile = fopen($uploadPath.$fileName, "w") or die("Unable to open file!");

                //记录总数
                $records = str_pad($result['records'], 12); //12位字符串

                //金额汇总
                $result['totalAmount'] = number_format($result['totalAmount'] / \Consts::HUNDRED, 2, '.', '');
                $totalAmount = str_pad($result['totalAmount'], 18); //18位字符串

                //写入“首行汇总行”
                $txt = $records.$totalAmount."\r\n";
                fwrite($myfile, $txt);

                foreach($result['log'] as $v){
                    //数据长度处理
                    $v['cmpyTransferId'] = str_pad($v['cmpyTransferId'], 30); //30位字符串
                    $v['acctNo'] = str_pad($v['acctNo'], 40); //40位字符串
                    $v['dueAmt'] = number_format($v['dueAmt'] / \Consts::HUNDRED, 2, '.', '');
                    $v['dueAmt'] = str_pad($v['dueAmt'], 16); //16位字符串
                    //账户名处理
                    preg_match_all('/[\x{4e00}-\x{9fa5}]/u', $v['acctName'], $match); //匹配字符串中所有的汉字
                    $chineseCount = count($match[0]);
                    $acctName = str_pad($v['acctName'], 60 + $chineseCount);

                    // 区分退货或清分
                    $v['varInfo'] = str_pad($v['varInfo'], 12);

                    //写入“交易记录”
                    $txt = $v['cmpyTransferId'].$v['acctNo'].$acctName.$v['dueAmt'].$v['settleDate'].$v['varInfo']."\r\n";
                    $txt = iconv('utf-8', 'gbk', $txt);
                    fwrite($myfile, $txt);
                }

                $liquidationMdl = new LiquidationModel();
                $ret = $liquidationMdl->editLiquidationRecord(array('initialFileUrl' => substr($uploadPath.$fileName, 1), 'areaNbr' => $dv['areaNbr'], 'transDate' => $date));
                //关闭文件
                fclose($myfile);
            }
        }
        return array('ret' => true, 'date' => $liquidateDay);
    }

    /**
     * 清算文件列表
     */
    public function getLiquidationFileList(){
        $data = I('get.');
        $liquidationMdl = new LiquidationModel();
        $condition = array();
        if(isset($data['date']) && $data['date']){
            $condition['initialFileUrl'] = array('like', '%'.$data['date'].'%');
        }
        if(isset($data['areaNbr']) && $data['areaNbr']){
            $condition['Liquidation.areaNbr'] = $data['areaNbr'];
        }
        if(isset($data['isLiquidated']) && $data['isLiquidated']){
            if($data['isLiquidated'] == 2){
                $condition['Liquidation.isLiquidated'] = 0;
            }else{
                $condition['Liquidation.isLiquidated'] = $data['isLiquidated'];
            }
        }
        $joinTableArr = array(
            array(
                'joinTable' => 'District',
                'joinCondition' => 'District.areaNbr = Liquidation.areaNbr',
                'joinType' => 'inner'
            )
        );
        $fileList = $liquidationMdl->listLiquidation($condition, array('Liquidation.*', 'District.name'), $joinTableArr, 'Liquidation.createTime desc', Pager::DEFUALT_PAGE_SIZE, $data['page']);
        $fileCount = $liquidationMdl->countLiquidation($condition, $joinTableArr);
        $this->pager->setItemCount($fileCount);

        $districtMdl = new DistrictModel();
        $districtList = $districtMdl->getAreaNbrList(array('areaNbr', 'name'), array('areaNbr' => array('NEQ', '')));

        $assign = array(
            'title' => '清算文件列表',
            'dataList' => $fileList,
            'get' => I('get.'),
            'districtList' => $districtList,
            'pager' => $this->pager
        );
        $this->assign($assign);
        if (! IS_AJAX) {
            $this->display();
        } else {
            $html = empty($fileList) ? '' : $this->fetch('Liquidation:listLiquidationWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 上传清算结果文件
     */
    public function uploadLiquidationResultFile() {
        if(!is_dir('./Public/bankAccountResultLog/')){
            mkdir('./Public/bankAccountResultLog/');
        }

        $liquidationId = I('post.upload_id');
        $date = I('post.upload_date');
        $liquidationMdl = new LiquidationModel();
        $liquidationInfo = $liquidationMdl->getLiquidationInfo(array('id' => $liquidationId));
        if(file_exists('.'.$liquidationInfo['resultFileUrl'])){
            unlink('.'.$liquidationInfo['resultFileUrl']);  //删除文件
        }
        //文件存放目录
        $uploadPath = './Public/bankAccountResultLog/'.$date.'/';
        if(!is_dir($uploadPath)){
            mkdir($uploadPath);
        }
        $config = array(
            'mimes'         =>  array(), //允许上传的文件MiMe类型
            'maxSize'       =>  5242880, //上传的文件大小限制(以字节为单位)(0-不做限制)5M
            'exts'          =>  array('txt'), //允许上传的文件后缀
            'autoSub'       =>  true, //自动子目录保存文件
            'subName'       =>  '', //子目录创建方式，[0]-函数名，[1]-参数，多个参数使用数组
            'rootPath'      =>  '',
            'savePath'      =>  $uploadPath, //保存路径
            'saveName'      =>  '', //上传文件命名规则，[0]-函数名，[1]-参数，多个参数使用数组
        );
        $upload = new \Think\Upload($config);// 实例化上传类

        $info = $upload->upload();
        if(!$info) {
            // 上传错误,提示错误信息
            echo json_encode(array('code' => $upload->getError()));
            exit;
        } else {  // 上传成功，获取上传文件信息
            $url = substr($info['userfile']['savepath'], 1).$info['userfile']['savename'];
            if(!preg_match('/^huiquan.settle.result.\d{4}.\d{8}.\d{3}.txt$/', $info['userfile']['savename'])){
                $return_json = array('msgCode'=> 401, 'code'=>'文件命名格式错误！');
                unlink('.'.$url);  //删除文件
                echo json_encode($return_json);
                exit;
            }

            if(substr($info['userfile']['savename'], 23) != substr($liquidationInfo['initialFileUrl'], 50)){
                $return_json = array('msgCode'=> 402, 'code'=>'上传的文件与清算文件不匹配！');
                unlink('.'.$url);  //删除文件
                echo json_encode($return_json);
                exit;
            }
            $liquidationInfo = $liquidationMdl->editLiquidationRecord(array('resultFileUrl' => $url), array('id' => $liquidationId));
            if($liquidationInfo['code'] == true){
                $return_json = array(
                    'msgCode' => 200,
                    'code' => '上传成功！',
                    'url'=> $url,
                );
            }else{
                unlink('.'.$url);  //删除文件
                $return_json = array('msgCode'=> 400, 'code'=>'操作失败，请重试！');
            }
            echo json_encode($return_json);
            exit;
        }
    }

    /**
     * 读取银行对账结果文件(处理清算结果)
     * @return bool
     */
    public function liquidate(){
        $da = I('post.');
        $liquidationMdl = new LiquidationModel();
        $liquidationInfo = $liquidationMdl->getLiquidationInfo(array('id' => $da['id']));
        $file = '.'.$liquidationInfo['resultFileUrl'];
        //文件存放目录
        if(!file_exists($file)){
            $this->ajaxError('文件不存在！');
        }else{
            $date = $liquidationInfo['transDate'];
            $data = file($file);

            $ret = $this->isRightFormat(substr($data[0], 0, 12));
            if($ret !== true){
                $this->ajaxError('记录总数格式错误！', substr($data[0], 0, 12));
            }
            $records = (int)trim(substr($data[0], 0, 12)); //记录总数

            $ret = $this->isRightFormat(substr($data[0], 12, 18));
            if($ret !== true){
                $this->ajaxError('汇总金额格式错误！', substr($data[0], 12, 18));
            }
//            $totalAmount = trim(substr($data[0], 12, 18)); //金额汇总
            array_shift($data); //删除删除第一个元素，重置key值
            $result = array();
            if($records > 0){ //有交易记录
                M()->startTrans();
                foreach($data as $k=>$v){
                    $ret1 = $this->isRightFormat(substr($v, 0, 30));
                    $ret2 = $this->isRightFormat(substr($v, 30, 40));
                    $ret3 = $this->isRightFormat(substr($v, 70, 60));
                    $ret4 = $this->isRightFormat(substr($v, 130, 16));
                    $ret5 = $this->isRightFormat(substr($v, 146, 16));
                    $ret6 = $this->isRightFormat(substr($v, 162, 8));
                    $ret7 = $this->isRightFormat(substr($v, 170, 5));
                    $ret8 = $this->isRightFormat(substr($v, 175, 60));
                    $ret9 = $this->isRightFormat(substr($v, 235, 10));
                    if($ret1 !== true){
                        M()->rollback();
                        $this->ajaxError('第'.($k + 1).'行清算流水号格式错误！', substr($v, 0, 30));
                    }
                    if($ret2 !== true){
                        M()->rollback();
                        $this->ajaxError('第'.($k + 1).'行入账账户格式错误！', substr($v, 30, 40));
                    }
                    if($ret3 !== true){
                        M()->rollback();
                        $this->ajaxError('第'.($k + 1).'行入账账户名格式错误！', substr($v, 70, 60));
                    }
                    if($ret4 !== true){
                        M()->rollback();
                        $this->ajaxError('第'.($k + 1).'行应划款金额格式错误！', substr($v, 130, 16));
                    }
                    if($ret5 !== true){
                        M()->rollback();
                        $this->ajaxError('第'.($k + 1).'行实划款金额格式错误！', substr($v, 146, 16));
                    }
                    if($ret6 !== true){
                        M()->rollback();
                        $this->ajaxError('第'.($k + 1).'行清算日期格式错误！', substr($v, 162, 8));
                    }
                    if($ret7 !== true){
                        M()->rollback();
                        $this->ajaxError('第'.($k + 1).'行响应代码格式错误！', substr($v, 170, 5));
                    }
                    if($ret8 !== true){
                        M()->rollback();
                        $this->ajaxError('第'.($k + 1).'行错误信息格式错误！', substr($v, 175, 60));
                    }
                    if($ret9 !== true){
                        M()->rollback();
                        $this->ajaxError('第'.($k + 1).'行清算类型格式错误！', substr($v, 235, 10));
                    }
//                    if($ret1 !== true || $ret2 !== true || $ret3 !== true || $ret4 !== true || $ret5 !== true || $ret6 !== true || $ret7 !== true || $ret8 !== true || $ret9 !== tru){
//                        M()->rollback();
//                        $this->ajaxError('文件格式错误！');
//                    }
                    $resultData = array(
                        'cmpyTransferId' => trim(substr($v, 0, 30)),
                        'acctNo' => trim(substr($v, 30, 40)),
                        'acctName' => trim(substr($v, 70, 60)),
                        'dueAmt' => trim(substr($v, 130, 16)),
                        'payAmt' => trim(substr($v, 146, 16)),
                        'settleDate' => trim(substr($v, 162, 8)),
                        'respond' => trim(substr($v, 170, 5)),
                        'errorMsg' => trim(substr($v, 175, 60)),
                        'varInfo' => trim(substr($v, 235, 10)),
                    );

                    //商家
                    $shopMdl = new ShopModel();
                    $shopInfo = $shopMdl->getOneShopInfo(array('hqIcbcShopNbr' => $resultData['cmpyTransferId']), array('shopCode'));

                    //查询这条记录是否为退货
                    $refundLogMdl = new RefundLogModel();
                    if($resultData['varInfo'] == iconv('utf-8', 'gbk', '退货')){
                        $refundLog = $refundLogMdl->getRefundLogInfo(
                            array(
                                'BankAccount.bankCard' => $resultData['acctNo'],
                                'RefundLog.createTime' => array('like', "$date%"),
                                'RefundLog.liquidationStatus' => \Consts::LIQUIDATION_STATUS_ING,
                            ),
                            array('refundCode'),
                            array(
                                array(
                                    'joinTable' => 'BankAccount',
                                    'joinCondition' => 'BankAccount.bankAccountCode = RefundLog.refundAccount',
                                    'joinType' => 'inner'
                                )
                            )
                        );
                    }

                    //券
                    $orderCouponMdl = new OrderCouponModel();
                    $condition = array(
                        'UserConsume.payedTime' => array('like', "$date%"),
                        'UserConsume.liquidationStatus' => \Consts::LIQUIDATION_STATUS_ING, //清算中
                        'UserConsume.location' => $shopInfo['shopCode'],
                        'OrderCoupon.liquidationStatus' => \Consts::LIQUIDATION_STATUS_ING,
                    );
                    $joinTableArr = array(
                        array(
                            'joinTable' => 'UserCoupon',
                            'joinCondition' => 'UserCoupon.orderCouponCode = OrderCoupon.orderCouponCode',
                            'joinType' => 'inner'
                        ),
                        array(
                            'joinTable' => 'UserConsume',
                            'joinCondition' => 'UserCoupon.consumeCode = UserConsume.consumeCode',
                            'joinType' => 'inner'
                        )
                    );
                    $couponOrderCodeArr = $orderCouponMdl->listField($condition, 'OrderCoupon.orderCode', $joinTableArr);

                    //活动
                    $userActCodeMdl = new UserActCodeModel();
                    $condition = array(
                        'UserActCode.valTime' => array('like', "$date%"),
                        'UserActCode.liquidationStatus' => \Consts::LIQUIDATION_STATUS_ING, //清算中
                        'Activity.shopCode' => $shopInfo['shopCode'],
                    );
                    $joinTableArr = array(
                        array(
                            'joinTable' => 'UserActivity',
                            'joinCondition' => 'UserActivity.userActivityCode = UserActCode.userActCode',
                            'joinType' => 'inner'
                        ),
                        array(
                            'joinTable' => 'Activity',
                            'joinCondition' => 'Activity.activityCode = UserActivity.activityCode',
                            'joinType' => 'inner'
                        )
                    );
                    $actOrderCodeArr = $userActCodeMdl->getActCodeField($condition, 'UserActCode.orderCode', $joinTableArr);

                    $userConsumeMdl = new UserConsumeModel();
                    $userConsumeRet = true;
                    $orderCouponRet['code'] = true;
                    $orderActRet['code'] = true;
                    $refundRet['code'] = true;
                    if($resultData['respond'] == '00000'){ //清算结果成功
                        if(isset($refundLog)){ //退货
                            $refundRet = $refundLogMdl->editRefundLog(array('refundCode' => $refundLog['refundCode'], 'refundTime' => date('Y-m-d H:i:s'), 'liquidationStatus' => \Consts::LIQUIDATION_STATUS_HAD));
                        }else{ //清分
                            //将账单改为已清算
                            $userConsumeRet = $userConsumeMdl->updateConsumeStatus(array('location'=>$shopInfo['shopCode'], 'liquidationStatus' => \Consts::LIQUIDATION_STATUS_ING, 'payedTime' => array('like', "$date%")), array('liquidationStatus' => \Consts::LIQUIDATION_STATUS_HAD));

                            if($couponOrderCodeArr){
                                //将买券的流水改为已清算
                                $orderCouponRet = $orderCouponMdl->editOrderCoupon(array('orderCode' => array('IN', $couponOrderCodeArr)), array('liquidationStatus' => \Consts::LIQUIDATION_STATUS_HAD));
                            }

                            if($actOrderCodeArr){
                                //将买活动的流水改为已清算
                                $orderActRet = $userActCodeMdl->updateUserActCode(array('orderCode' => array('IN', $actOrderCodeArr)), array('liquidationStatus' => \Consts::LIQUIDATION_STATUS_HAD));
                            }
                        }
                    }else{ //清算结果失败
                        if(isset($refundLog)){ //退货
                            $refundRet = $refundLogMdl->editRefundLog(array('refundCode' => $refundLog['refundCode'], 'liquidationStatus' => \Consts::LIQUIDATION_STATUS_HAD_NOT));
                        }else{ //清分
                            //将账单改为未清算
                            $userConsumeRet = $userConsumeMdl->updateConsumeStatus(array('location'=>$shopInfo['shopCode'], 'liquidationStatus' => \Consts::LIQUIDATION_STATUS_ING, 'payedTime' => array('like', "$date%")), array('liquidationStatus' => \Consts::LIQUIDATION_STATUS_HAD_NOT));

                            if($couponOrderCodeArr){
                                //将买券的流水改为未清算
                                $orderCouponRet = $orderCouponMdl->editOrderCoupon(array('orderCode' => array('IN', $couponOrderCodeArr)), array('liquidationStatus' => \Consts::LIQUIDATION_STATUS_HAD_NOT));
                            }

                            if($actOrderCodeArr){
                                //将买活动的流水改为未清算
                                $orderActRet = $userActCodeMdl->updateUserActCode(array('orderCode' => array('IN', $actOrderCodeArr)), array('liquidationStatus' => \Consts::LIQUIDATION_STATUS_HAD_NOT));
                            }
                        }
                    }

                    if(!($userConsumeRet === true && $orderCouponRet['code'] === true && $orderActRet['code'] === true && $refundRet['code'] === true)){
                        M()->rollback();
                        $this->ajaxError('清算结果失败，请重试！');
                    }

                    $result[] = array(
                        'shopCode' => $shopInfo['shopCode'],
                        'dueAmt' => $resultData['dueAmt'] * 100,
                        'payAmt' => $resultData['payAmt'] * 100,
                        'settleDate' => $resultData['settleDate'],
                        'respond' => $resultData['respond'],
                        'errorMsg' => iconv('gbk', 'utf-8', $resultData['errorMsg']),
                        'varInfo' => iconv('gbk', 'utf-8', $resultData['varInfo']),
                    );
                }
                M()->commit();
            }
            $saveData = array('liquidateTime' => time(), 'isLiquidated' => 1);
            if($result){
                $saveData['result'] = json_encode($result);
            }
            $liquidationMdl->editLiquidationRecord($saveData, array('id' => $da['id']));

            $this->ajaxSucc('清算结果已处理！');
        }
    }

    public function liquidateResult(){
        $data = I('post.');
        $liquidationMdl = new LiquidationModel();
        $liquidationInfo = $liquidationMdl->getLiquidationInfo(array('id' => $data['id']));
        $result = json_decode($liquidationInfo['result'], true);

        if(!empty($result)){
            $html = '<table class="fp-table table table-striped table-bordered table-condensed"><thead><tr><td>商户名</td><td>清算类型</td><td>应划款金额（单位：元）</td><td>实划款金额（单位：元）</td><td>清算资金日期</td><td>响应代码，00000-成功，其他-失败</td><td>错误信息</td></tr></thead><tbody>';
            $shopMdl = new ShopModel();
            foreach($result as $rv){
                if(isset($rv['varInfo'])){
                    $rv['varInfo'] = empty($rv['varInfo'])?'清分':$rv['varInfo'];
                }else{
                    $rv['varInfo'] = '清分';
                }
                $shopInfo = $shopMdl->getOneShopInfo(array('shopCode' => $rv['shopCode']), array('shopName'));
                $html .= '<tr><td>'.$shopInfo['shopName'].'</td><td>'.$rv['varInfo'].'</td><td>'.number_format($rv['dueAmt']/100, 2).'</td><td>'.number_format($rv['payAmt']/100, 2).'</td><td>'.$rv['settleDate'].'</td><td>'.$rv['respond'].'</td><td>'.$rv['errorMsg'].'</td></tr>';
            }
            $html .= '</tbody></table>';
        }else{
            $html = '<p style="font-size: 18px;font-weight: bold;">成功！</p>';
        }

        $this->ajaxSucc('', $result, $html);
    }

    public function isRightFormat($string){
        if(stripos($string, ' ') !== false){ //判断字符串首位是否为空格
            if(stripos($string, ' ') == 0){
                return false;
            }
        }
        if(strripos($string, ' ') !== false){ //判断字符串末位是否为空格
            if(strripos($string, ' ') < strlen($string) - 1){
                return false;
            }
        }
        return true;
    }
}