<?php
/**
 * User Controller
 *
 * User: jiangyufeng
 * Date: 2015-05-19
 */
namespace Admin\Controller;
use Common\Model\BankAccountModel;
use Common\Model\ConsumeOrderModel;
use Common\Model\DistrictModel;
use Common\Model\PointEarningLogModel;
use Common\Model\UserConsumeModel;
use Common\Model\UserCouponModel;
use Common\Model\UtilsModel;
use Org\FirePHPCore\FP;
use Common\Model\UserModel;
use Common\Model\SmsModel;
use Common\Model\UserBonusModel;
use Common\Model\UserCardModel;
use Common\Model\Pager;


class UserController extends AdminBaseController {

    /**
     * 获取用户的详细基本信息
     * @param string $userCode 用户编码
     */
    public function getUserInfo($userCode) {
        $userMdl = new UserModel();
        $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('userCode', 'mobileNbr', 'realName', 'sex', 'nickName', 'district', 'province', 'city', 'avatarUrl'));
        $assign = array(
            'title'=> '用户详情',
            'userInfo' => $userInfo,
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($userInfo) ? '' : $this->fetch('User:userInfoWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 用户列表
     */
    public function listUser() {
        $userMdl = new UserModel();
        $userList = $userMdl->listUser(I('get.'), $this->pager);

        //判断用户所属商圈列表
        $where = array();
        if($_SESSION['USER']['bank_id']!=-1){
            $where['id'] = $_SESSION['USER']['bank_id'];
        }
        $bankList = D("Bank")->where($where)->field("id,name")->select();
        $userCount = $userMdl->countUser(I('get.'));
        $this->pager->setItemCount($userCount);
        $assign = array(
            'title' => '用户列表',
            'dataList' => $userList,
            'bankList'=>$bankList,
            'get' => I('get.'),
            'pager' => $this->pager
        );

        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($userList) ? '' : $this->fetch('User:listUserWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 用户列表
     */
    public function listUserPoint() {
        $pELMdl = new PointEarningLogModel();
        $pELList = $pELMdl->listPEL(I('get.'), $this->pager);
        $pELCount = $pELMdl->countPEL(I('get.'));
        $this->pager->setItemCount($pELCount);
        $assign = array(
            'title' => '用户圈值',
            'dataList' => $pELList,
            'get' => I('get.'),
            'pager' => $this->pager
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($pELList) ? '' : $this->fetch('User:listUserPointWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 用户添加
     */
    public function editUser() {
        $userMdl = new UserModel();
        $this->assign('title', '用户编辑');
        if(IS_GET) {
            $userCode = I('get.userCode');
            $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('userCode', 'realName', 'mobileNbr', 'nickName', 'sex'));
            $this->assign('userInfo', $userInfo);
            $this->display();
        } elseif(IS_POST) {
            $data = I('post.');
            $res = $userMdl->editUser($data);
            if ($res === C('SUCCESS')) {
                $this->ajaxSucc('操作成功');
            } else {
                $this->ajaxError($res);
            }
        }
    }

    /**
     * 修改用户状态
     */
    public function changeStatus() {
        if(IS_AJAX) {
            $userMdl = new UserModel();
            $userCode = I('get.userCode');
            $status = I('get.status');
            $res = $userMdl->changeUserStatus($userCode, $status);
            if ($res === true) {
                $this->ajaxSucc('操作成功!');
            } else {
                $this->ajaxError($res);
            }
        } else {
            echo 'WELCOME TO HUIQUAN';
        }
    }

    /**
     * 用户消费统计页面
     */
    public function consumptionStatistics() {
        $data = I('get.');
        if($data['startTime'] && !$data['endTime']) {
            $orderTime = array('EGT', $data['startTime']);
        } elseif(!$data['startTime'] && $data['endTime']) {
            $orderTime = array('ELT', $data['endTime'] . ' 23:59:59');
        } elseif($data['startTime'] && $data['endTime']) {
            $orderTime = array('BETWEEN', array($data['startTime'], $data['endTime'] . ' 23:59:59'));
        } else {
            $orderTime = '';
        }

        // 获得消费金额的统计数据
        $consumeOrderMdl = new ConsumeOrderModel();
        $arrayAmountData = array(
            array('name' => '20元以下'),
            array('name' => '20.01-50元以下'),
            array('name' => '50.01-100元以下'),
            array('name' => '100.01-200元'),
            array('name' => '200.01-300元'),
            array('name' => '300.01-400元'),
            array('name' => '400.01-500元'),
            array('name' => '500.01-600元'),
            array('name' => '600元以上'),
        );
        $condition['status'] = \Consts::PAY_STATUS_PAYED; // 支付状态为已付款
        if($orderTime) {
            $condition['orderTime'] = $orderTime;
        }
        foreach($arrayAmountData as $k => $v) {
            if($k == 0) {
                $condition['orderAmount'] = array('ELT', 2000);
            } elseif($k == 1) {
                $condition['orderAmount'] = array('BETWEEN', array(2001, 5000));
            } elseif($k == 2) {
                $condition['orderAmount'] = array('BETWEEN', array(5001, 100));
            } elseif($k == 8) {
                $condition['orderAmount'] = array('GT', 60000);
            } else {
                $condition['orderAmount'] = array('BETWEEN', array(10000 * $k + 1, 10000 * ($k + 1)));
            }
            $arrayAmountData[$k]['y'] = $consumeOrderMdl->countConsumeOrder($condition);
        }
        $jsonAmountData = json_encode($arrayAmountData);

        // 获得消费次数的统计数据
        unset($condition['orderAmount']);
        $arrayTimesData = $consumeOrderMdl->getConsumptionStatistics($condition);
        $jsonTimesData = json_encode($arrayTimesData);
        $assign = array(
            'title' => '用户消费统计',
            'jsonAmountData' => $jsonAmountData,
            'jsonTimesData' => $jsonTimesData,
            'get' => I('get.'),
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 获得用户性别统计图的数据
     * @param string $startTime 开始时间
     * @param string $endTime 结束时间
     * @return array
     */
    private function getDataBySex($startTime, $endTime) {
        //按性别进行统计
        $male = C('SEX.MALE');
        $female = C('SEX.FEMALE');
        $unknown = C('SEX.UNKNOWN');
        $conditionTotal['status'] = array('neq', C('USER_STATUS.NOT_REG'));
        $conditionTotal['registerTime'] = array('ELT', $endTime.' 23:59:59');
        $conditionTotal['sex'] = $male;
        $userMdl = new UserModel();
        $countManTotal = $userMdl->countResult($conditionTotal);
        $countManTotal = $countManTotal?$countManTotal:0;

        $conditionInc['status'] = array('neq', C('USER_STATUS.NOT_REG'));
        $conditionInc['registerTime'] = array('between', array($startTime.' 00:00:00', $endTime.' 23:59:59'));
        $conditionInc['sex'] = $male;
        $countManInc = $userMdl->countResult($conditionInc);
        $countManInc = $countManInc?$countManInc:0;

        $conditionTotal['sex'] = $female;
        $countFemaleTotal = $userMdl->countResult($conditionTotal);
        $countFemaleTotal = $countFemaleTotal?$countFemaleTotal:0;

        $conditionInc['sex'] = $female;
        $countFemaleInc = $userMdl->countResult($conditionInc);
        $countFemaleInc = $countFemaleInc?$countFemaleInc:0;

        $conditionTotal['sex'] = $unknown;
        $countUnknownTotal = $userMdl->countResult($conditionTotal);
        $countUnknownTotal = $countUnknownTotal?$countUnknownTotal:0;

        $conditionInc['sex'] = $unknown;
        $countUnknownInc = $userMdl->countResult($conditionInc);
        $countUnknownInc = $countUnknownInc?$countUnknownInc:0;

        $countTotal = $countManTotal + $countFemaleTotal + $countUnknownTotal;
        $countInc = $countManInc + $countFemaleInc + $countUnknownInc;

        $data = array(
            C('SEX.MALE')=>array('name'=>'男', 'totalAmount'=>$countManTotal, 'incAmount'=>$countManInc),
            C('SEX.FEMALE')=>array('name'=>'女', 'totalAmount'=>$countFemaleTotal, 'incAmount'=>$countFemaleInc),
            C('SEX.UNKNOWN')=>array('name'=>'未知', 'totalAmount'=>$countUnknownTotal, 'incAmount'=>$countUnknownInc),
        );
        return array(
            'data' => $data,
            'countTotal' => $countTotal,
            'countInc' => $countInc,
        );
    }

    /**
     * 获得用户区域统计图的数据
     * @param string $startTime 开始时间
     * @param string $endTime 结束时间
     * @return array
     */
    private function getUserDistrictData($startTime, $endTime) {
        // 获得X轴的数据
        $districtMdl = new DistrictModel();
        $zjCityList = $districtMdl->getCityList('浙江省');
        $cityNameList = array();
        foreach($zjCityList as $city) {
            $cityNameList[] = $city['name'];
        }
        $xAxisData = $cityNameList;
        $xAxisData[] = '浙江省外';
        // 获得画图用的数据
        $userMdl = new UserModel();
        $bankAccountMdl = new BankAccountModel();
        $series = array(
            array('name' => '注册并绑卡人数', 'data' => array()),
            array('name' => '注册人数', 'data' => array()),
        );
        $regSignedNbr = 0; // 注册并绑卡人数
        $regNbr = 0; // 注册人数
        foreach($series as $k => $v) {
            if($k == 0) { // 注册并绑卡人数
                $con1 = array('User.status' => array('IN', array(\Consts::USER_STATUS_ACTIVE, \Consts::USER_STATUS_DISABLE)), 'User.registerTime' => array('BETWEEN', array($startTime.' 00:00:00', $endTime.' 23:59:59')), 'BankAccount.createTime' => array('BETWEEN', array($startTime.' 00:00:00', $endTime.' 23:59:59')), 'BankAccount.status' => \Consts::BANKACCOUNT_STATUS_SIGNED);
                foreach($xAxisData as $city) {
                    $con1['User.mobileBelonging'] = $city == '浙江省外' ? array('NOTLIKE', '浙江%') : array('like', "%$city%");
                    $nbr = $bankAccountMdl->getBankAccountCount($con1, array(array('joinTable' => 'User', 'joinCon' => 'User.userCode = BankAccount.userCode', 'joinType' => 'inner')), 'DISTINCT(User.userCode)');
                    $series[$k]['data'][] = $nbr;
                    $regSignedNbr += $nbr;
                }
            } else { // 注册人数
                $con2 = array('User.status' => array('IN', array(\Consts::USER_STATUS_ACTIVE, \Consts::USER_STATUS_DISABLE)), 'User.registerTime' => array('BETWEEN', array($startTime.' 00:00:00', $endTime.' 23:59:59')));
                foreach($xAxisData as $city) {
                    $con2['User.mobileBelonging'] = $city == '浙江省外' ? array('NOTLIKE', '浙江%') : array('like', "%$city%");
                    $nbr = $userMdl->countUser($con2);
                    $series[$k]['data'][] = intval($nbr);
                    $regNbr += $nbr;
                }
            }
        }
        return array(
            'xAxisData' => json_encode($xAxisData),
            'series' => json_encode($series),
            'regNbr' => $regNbr,
            'regSignedNbr' => $regSignedNbr,
        );
    }

    /**
     * 用户统计分析
     */
    public function userAnalysis() {
        $data = I('get.');
        $startTime = $data['startTime'];
        $endTime = $data['endTime'];
        if($startTime && !$endTime) {
            $endTime = date('Y-m-d', strtotime($startTime) + 6 * 86400);
        }elseif(!$startTime && $endTime) {
            $startTime = date('Y-m-d', strtotime($endTime) - 6 * 86400);
        }elseif(!$startTime && !$endTime) {
            $startTime = date('Y-m-d', time() - 6 * 86400);
            $endTime = date('Y-m-d', time());
        }
        $data['startTime'] = $startTime;
        $data['endTime'] = $endTime;

        $timeOnHour = date('Y-m-d', strtotime('-1 day'));
        $timeOnStartDay = date('Y-m-d', time() - 6 * 86400);
        $timeOnEndDay = date('Y-m-d', time());
        $timeOnStartMonth = date('Y-m', strtotime('-2 month'));
        $timeOnEndMonth = date('Y-m', time());
        if($data['options'] == 2){ // 按日
            $timeOnStartDay = $data['timeOne'];
            $timeOnEndDay = $data['timeTwo'];
            if($timeOnStartDay && !$timeOnEndDay) {
                $timeOnEndDay = date('Y-m-d', strtotime($timeOnStartDay) + 6 * 86400);
            }elseif(!$timeOnStartDay && $timeOnEndDay) {
                $timeOnStartDay = date('Y-m-d', strtotime($timeOnEndDay) - 6 * 86400);
            }elseif(!$timeOnStartDay && !$timeOnEndDay) {
                $timeOnStartDay = date('Y-m-d', time() - 6 * 86400);
                $timeOnEndDay = date('Y-m-d', time());
            }
        }elseif($data['options'] == 3){ // 按月
            $timeOnStartMonth = $data['timeOne'];
            $timeOnEndMonth = $data['timeTwo'];
            if($timeOnStartMonth && !$timeOnEndMonth) {
                $timeOnEndMonth = date('Y-m', time());
            }elseif(!$timeOnStartMonth && $timeOnEndMonth) {
                $timeOnStartMonth = date('Y-m', strtotime('-2 month', $timeOnEndMonth));
            }elseif(!$timeOnStartMonth && !$timeOnEndMonth) {
                $timeOnStartMonth = date('Y-m', strtotime('-2 month'));
                $timeOnEndMonth = date('Y-m', time());
            }
        }else{ // 分时
            if(!$data['timeOne']){
                $timeOnHour = date('Y-m-d', strtotime('-1 day'));
            }else{
                $timeOnHour = $data['timeOne'];
            }
        }
        unset($data['timeOne']);
        unset($data['timeTwo']);
        $data['timeOnStartDay'] = $timeOnStartDay;
        $data['timeOnEndDay'] = $timeOnEndDay;
        $data['timeOnStartMonth'] = $timeOnStartMonth;
        $data['timeOnEndMonth'] = $timeOnEndMonth;
        $data['timeOnHour'] = $timeOnHour;

        $userMdl = new UserModel();

        $hourData = array();
        $dayData = array();
        $monthData = array();
        $dayCount = (strtotime($timeOnEndDay) - strtotime($timeOnStartDay) + 86400)/86400;
        $year1 = (int)substr($timeOnStartMonth, 0, 4);
        $month1 = (int)substr($timeOnStartMonth, 5);
        $year2 = (int)substr($timeOnEndMonth, 0, 4);
        $month2 = (int)substr($timeOnEndMonth, 5);
        $monthCount = ($year2 - $year1) * 12 - $month1 + 1 +$month2;
        // 获得用户性别统计图的数据
        $dataBySex = $this->getDataBySex($startTime, $endTime);
        // 获得用户区域统计图的数据
        $userDistrictData = $this->getUserDistrictData($startTime, $endTime);

        //按日期进行统计
        //1.分时   $timeOnHour
        $hourCondition['status'] = array('neq', C('USER_STATUS.NOT_REG'));
        for($i = 0; $i < 24; $i++){
            if($i < 10){
                $hourCondition['registerTime'] = array('like', $timeOnHour.' 0'.$i.'%');
            }else{
                $hourCondition['registerTime'] = array('like', $timeOnHour.' '.$i.'%');
            }
            $perData = $userMdl->countResult($hourCondition);
            $hourData[$i] = $perData?$perData:0;
        }
        //2.按日    $timeOnStartDay      $timeOnEndDay
        $dayCondition['status'] = array('neq', C('USER_STATUS.NOT_REG'));
        for($j = 0; $j < $dayCount; $j++){
            $day = date('Y-m-d', strtotime($timeOnStartDay) + $j * 86400);
            $dayCondition['registerTime'] = array('like', $day.'%');
            $perData = $userMdl->countResult($dayCondition);
            $dayData[$day] = $perData?$perData:0;
        }
        //3.按月    $timeOnStartMonth    $timeOnEndMonth
        $monthCondition['status'] = array('neq', C('USER_STATUS.NOT_REG'));
        for($m = 0; $m < $monthCount; $m++){
            $nextMonth = $month1 + $m;
            if($nextMonth > 12){
                $nextYear = $year1 + ($nextMonth - $nextMonth % 12) / 12;
                $nextMonth = $nextMonth % 12;
            }else{
                $nextYear = $year1;
            }
            if($nextMonth < 10){
                $day = date('Y-m', strtotime($nextYear.'-0'.$nextMonth));
            }else{
                $day = date('Y-m', strtotime($nextYear.'-'.$nextMonth));
            }
            $monthCondition['registerTime'] = array('like', $day.'%');
            $perData = $userMdl->countResult($monthCondition);
            $monthData[$day] = $perData?$perData:0;
        }

        //用户绑卡统计
        $bankAccountMdl = new BankAccountModel();
        $conditionTotalBind['_string'] = 'BankAccount.createTime <= "'.$endTime.' 23:59:59" AND ((BankAccount.status = '.\Consts::BANKACCOUNT_STATUS_SIGNED.') OR (BankAccount.status = '.\Consts::BANKACCOUNT_STATUS_TERMINATE.' AND BankAccount.lastOperationTime >= "'.$endTime.' 23:59:59"))';
        $countTotalBind = $bankAccountMdl->getBankAccountCount($conditionTotalBind, array(), 'DISTINCT(userCode)'); //总绑卡人数
        $conditionTotalUnbind = array('BankAccount.status' => \Consts::BANKACCOUNT_STATUS_TERMINATE, 'BankAccount.lastOperationTime' => array('ELT', $endTime.' 23:59:59'));
        $countTotalUnbind = $bankAccountMdl->getBankAccountCount($conditionTotalUnbind, array(), 'DISTINCT(userCode)');//总解绑人数
        $countDay = (strtotime($endTime) - strtotime($startTime) + 86400)/86400;
        $bankAccount = array();
        $conditionDayBind['BankAccount.status'] = array('IN', array(\Consts::BANKACCOUNT_STATUS_TERMINATE, \Consts::BANKACCOUNT_STATUS_SIGNED));
        for($n = 0; $n < $countDay; $n++){
            $day = date('Y-m-d', strtotime($startTime) + $n * 86400);
            $conditionDayBind['BankAccount.createTime'] = array('like', $day.'%');
            $conditionTotalUnbind['BankAccount.lastOperationTime'] = array('like', $day.'%');
            $bind = $bankAccountMdl->getBankAccountCount($conditionDayBind, array(), 'DISTINCT(userCode)');
            $unbind = $bankAccountMdl->getBankAccountCount($conditionTotalUnbind, array(), 'DISTINCT(userCode)');
            $bind = $bind ? $bind : 0;
            $unbind = $unbind ? $unbind : 0;
            $bankAccount[$day] = array(
                'bind' => $bind,
                'unbind' => $unbind
            );
        }
        $assign = array(
            'title' => '用户统计分析',
            'get' => $data,
            'totalAmount' => $dataBySex['countTotal'],
            'incAmount' => $dataBySex['countInc'],
            'totalBind' => $countTotalBind ? $countTotalBind : 0,
            'totalUnbind' => $countTotalUnbind ? $countTotalUnbind : 0,
            'data' => $dataBySex['data'],
            'hourData' => $hourData,
            'dayData' => $dayData,
            'monthData' => $monthData,
            'bankAccount' => $bankAccount,
            'userDistrictData' => $userDistrictData,
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * phpExcel 方法生成 excel
     */
    public function exportExcelRegister(){
        $this->assign('title', '用户绑卡数据导出');
        if(IS_GET) {
            $this->assign('data', I('get.'));
            $this->display();
        } elseif(IS_POST) {
            $userMdl = new UserModel();
            //获取数据
            $formData = I('post.');
            $bankAccountMdl = new BankAccountModel();
            $bACondition = array('status' => array('IN', array(C('BANKACCOUNT_STATUS.SIGNED'), C('BANKACCOUNT_STATUS.TERMINATE'))));
            $bACondition['UNIX_TIMESTAMP(createTime)'] = array('between', array(strtotime($formData['startTime']), strtotime($formData['endTime']) + 86399));
            $userCodeArr = $bankAccountMdl->getFieldArr('userCode', $bACondition);
            $userCodeArr = array_unique($userCodeArr);
            if(empty($userCodeArr)){
                $userCodeArr = array('0');
            }
            if(isset($formData['isTiedCard']) && $formData['isTiedCard']){
                $condition['userCode'] = array('IN', $userCodeArr);
            }else{
                $condition['userCode'] = array('NOTIN', $userCodeArr);
            }

            $condition['status'] = $formData['userStatus'];
            $condition['mobileBelonging'] = array('like', '%'.$formData['mobileBelonging'].'%');
            $data = $userMdl->listUser($condition, $this->getPager(0), array('userCode', 'mobileNbr', 'realName', 'registerTime', 'status', 'mobileBelonging'));
            foreach($data as $k => $v){
                $firstBankCard = $bankAccountMdl->getFirstBankCard($v['userCode']);
                if($firstBankCard && (strtotime($firstBankCard['createTime']) >= strtotime($formData['startTime']) && strtotime($firstBankCard['createTime']) <= strtotime($formData['endTime']) + 86399)){
                    $data[$k]['bankCard'] = $firstBankCard['bankCard'];
                    $data[$k]['bdTime'] = $firstBankCard['createTime'];
                }else{
                    unset($data[$k]);
                }
            }
            if(empty($data)){
                $url = '/Admin/User/exportExcelRegister?startTime='.$formData['startTime'].'&endTime='.$formData['endTime'].'&isTiedCard='.$formData['isTiedCard'].'&userStatus='.$formData['userStatus'].'&mobileBelonging='.$formData['mobileBelonging'];
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
            $letter = array('A','B','C','D','E','F','G','H');
            //表头
            $title = array('#', '姓名', '手机号', '注册时间', '用户状态', '绑定的卡号', '绑定的时间', '手机号归属地');
            //设置表格宽度
            $objActSheet->getColumnDimension('A')->setWidth(5);
            $objActSheet->getColumnDimension('B')->setWidth(30);
            $objActSheet->getColumnDimension('C')->setWidth(30);
            $objActSheet->getColumnDimension('D')->setWidth(30);
            $objActSheet->getColumnDimension('E')->setWidth(30);
            $objActSheet->getColumnDimension('F')->setWidth(30);
            $objActSheet->getColumnDimension('G')->setWidth(30);
            $objActSheet->getColumnDimension('H')->setWidth(30);

            for($i = 0;$i < 8;$i++){
                //设置表头样式
                $head_row = 1;
                $objActSheet->getStyle("$letter[$i]$head_row")->getFont()->setSize(16);
                $objActSheet->getStyle("$letter[$i]$head_row")->getFont()->setBold(true);
                if($i == 0){
                    $objActSheet->getStyle("$letter[$i]$head_row")->getAlignment()->setHorizontal(\PHPExcel_Style_Alignment::HORIZONTAL_RIGHT);
                }
                //填充表头
                $objActSheet->setCellValue("$letter[$i]$head_row", $title[$i]);
            }
            $row = 2;
            foreach($data as $v){
                $objActSheet->getStyle("$letter[0]$row")->getAlignment()->setHorizontal(\PHPExcel_Style_Alignment::HORIZONTAL_RIGHT);
                $objActSheet->setCellValue("$letter[0]$row", $row - 1);
                $objActSheet->setCellValue("$letter[1]$row", $v['realName']);
                $objActSheet->setCellValueExplicit("$letter[2]$row", $v['mobileNbr'], \PHPExcel_Cell_DataType::TYPE_STRING);
                $objActSheet->getStyle("$letter[2]$row")->getNumberFormat()->setFormatCode("@");
                $objActSheet->setCellValue("$letter[3]$row", $v['registerTime']);
                if($v['status'] == C('USER_STATUS.ACTIVE')){
                    $status = '启用';
                }elseif($v['status'] == C('USER_STATUS.DISABLE')){
                    $status = '禁用';
                }else{
                    $status = '未注册';
                }
                $objActSheet->setCellValue("$letter[4]$row", $status);
                $objActSheet->setCellValueExplicit("$letter[5]$row", $v['bankCard'], \PHPExcel_Cell_DataType::TYPE_STRING);
                $objActSheet->getStyle("$letter[5]$row")->getNumberFormat()->setFormatCode("@");
                $objActSheet->setCellValue("$letter[6]$row", $v['bdTime']);
                $objActSheet->setCellValue("$letter[7]$row", $v['mobileBelonging']);
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
            header('Content-Disposition:attachment;filename="REGISTER.xlsx"');
            header("Content-Transfer-Encoding:utf-8");
            $write->save('php://output');
//        $filename = 'REGISTER_'.date('Y-m-d', time()).'.xlsx';
//        if(!is_dir('./Public/export/')){
//            mkdir('./Public/export/');
//        }
//        $url = './Public/export/'.$filename;
//        $write->save($url);
        }
    }

    public function exportAllUser(){
        $condition = I('get.');
        if($condition['mobileNbr']){
            $condition['mobileNbr'] = array('like', '%'.$condition['mobileNbr'].'%');
        }else{
            unset($condition['mobileNbr']);
        }
        if($condition['realName']){
            $condition['realName'] = array('like', '%'.$condition['realName'].'%');
        }else{
            unset($condition['realName']);
        }
        if($condition['inviteCode']){
            $condition['inviteCode'] = array('like', '%'.$condition['inviteCode'].'%');
        }else{
            unset($condition['inviteCode']);
        }
        if($condition['recomNbr']){
            $condition['recomNbr'] = array('like', '%'.$condition['recomNbr'].'%');
        }else{
            unset($condition['recomNbr']);
        }
        if ($condition['status'] || $condition['status'] == '0') {
            $condition['User.status'] = $condition['status'];
        }
        if($condition['registerTimeStart'] && $condition['registerTimeEnd']) {
            $condition['registerTime'] = array('BETWEEN', array($condition['registerTimeStart'].' 00:00:00', $condition['registerTimeEnd'].' 23:59:59'));
        }elseif($condition['registerTimeStart'] && !$condition['registerTimeEnd']) {
            $condition['registerTime'] = array('EGT', $condition['registerTimeStart'].' 00:00:00');
        }elseif(!$condition['registerTimeStart'] && $condition['registerTimeEnd']) {
            $condition['registerTime'] = array('ELT', $condition['registerTimeEnd'].' 23:59:59');
        }
        unset($condition['status']);
        unset($condition['registerTimeStart']);
        unset($condition['registerTimeEnd']);
        unset($condition['page']);
        $userMdl = new UserModel();
        $bankAccountMdl = new BankAccountModel();
        $userList = $userMdl->getUserList($condition, array('userCode','mobileNbr','registerTime', 'mobileBelonging','inviteCode','histPoint', 'lastLoginTime'), array(), 'registerTime asc');
        if(empty($userList)){
            $url = '/Admin/User/listUser';
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
        $title = array('用户手机号', '手机号归属地', '出生年月', '性别', '注册时间', '第一次绑卡时间', '邀请码使用次数', '最后一次登录时间', '历史圈值');

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
        foreach($userList as $v){
            $objActSheet->setCellValueExplicit("$letter[0]$row", $v['mobileNbr'], \PHPExcel_Cell_DataType::TYPE_STRING);
            $objActSheet->getStyle("$letter[0]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValue("$letter[1]$row", $v['mobileBelonging']);
            $bankAccount = $bankAccountMdl->getFirstBankCard($v['userCode'], 1);
            if($bankAccount && $bankAccount['idType'] == 0 && $bankAccount['idNbr']){
                $birthDate = substr($bankAccount['idNbr'], 6, 8);
                $sex = substr($bankAccount['idNbr'], 16, 1);
                $sex = ($sex % 2 == 0) ? '女' : '男';
            }else{
                $birthDate = '';
                $sex = '';
            }
            $objActSheet->setCellValue("$letter[2]$row", $birthDate);
            $objActSheet->setCellValue("$letter[3]$row", $sex);
            $objActSheet->setCellValue("$letter[4]$row", $v['registerTime']);
            $bankAccount = $bankAccountMdl->getFirstBankCard($v['userCode']);
            $objActSheet->setCellValue("$letter[5]$row", isset($bankAccount['createTime']) && $bankAccount['createTime'] ? $bankAccount['createTime'] : '');
            $inviteCount = $userMdl->countResult(array('recomNbr' => $v['inviteCode']));
            $objActSheet->setCellValue("$letter[6]$row", $inviteCount);
            $objActSheet->setCellValue("$letter[7]$row", $v['lastLoginTime']);
            $objActSheet->setCellValue("$letter[8]$row", $v['histPoint']);
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
        header('Content-Disposition:attachment;filename="USER-'.date('Y-m-d', time()).'.xlsx"');
        header("Content-Transfer-Encoding:utf-8");
        $write->save('php://output');
    }

    /**
     * 获得页数对象
     * @param int $page 页数
     * @return Object Pager
     */
    public function getPager($page){
        if(! isset($page) || $page === '')
            $page = 1;
        return new Pager($page, C('PAGESIZE'));
    }


    /**
     * 判断手机号归属地是否属于浙江
     * @param $mobileNbr
     * @return bool
     */
    public function getTelBelonging($mobileNbr){
        if(empty($mobileNbr) || strlen($mobileNbr) != 11){
            return false;
        }
        $ch = curl_init();
        $url = 'http://apis.baidu.com/apistore/mobilephoneservice/mobilephone?tel='.$mobileNbr;
        $header = array(
            'apikey: 92bb8810ba77c441922d73a007482f95',
        );
        // 添加apikey到header
        curl_setopt($ch, CURLOPT_HTTPHEADER  , $header);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        // 执行HTTP请求
        curl_setopt($ch , CURLOPT_URL , $url);
        $res = curl_exec($ch);

        var_dump(json_decode($res));
    }

    /**
     * 判断手机号归属地是否属于浙江
     * @param $mobileNbr
     * @return bool
     */
    function getMobileArea($mobileNbr){
        if(empty($mobileNbr) || strlen($mobileNbr) != 11){
            return false;
        }
        //根据淘宝的数据库调用返回值
        $url = "http://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=".$mobileNbr."&t=".time();
        $content = file_get_contents($url);
        $content= iconv('gbk', 'utf-8', $content);
//        var_dump($content);
        $province = '浙江';
        if(mb_strpos($content, $province)){
            return true;
        }else{
            return false;
        }
    }
}
