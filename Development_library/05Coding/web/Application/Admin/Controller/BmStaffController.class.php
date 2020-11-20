<?php
/**
 * User Controller
 * User: jiangyufeng
 * Date: 2015-05-19
 */
namespace Admin\Controller;
use Common\Model\BankAccountModel;
use Common\Model\CommunicationModel;
use Common\Model\FeedbackModel;
use Common\Model\ShopTypeModel;
use Org\FirePHPCore\FP;
use Common\Model\BmStaffModel;
use Common\Model\BaseModel;
use Common\Model\ShopModel;
use Common\Model\UserModel;
use Common\Model\CardModel;
use Common\Model\BatchCouponModel;
use Common\Model\StaffActionLogModel;
class BmStaffController extends AdminBaseController {

    /**
     * 管理端首页
     */
    public function homepage() {
        $staffInfo = $this->user;
        if($staffInfo['userLvl'] == \Consts::HQ_STAFF_TYPE_GROUND_PERSON) { // 角色为地推人员
            redirect('/Admin/PreShop/listPreShop'); // 直接跳到预采用商户列表页
        }

        $shopTypeMdl = new ShopTypeModel();
        $shopTypeIdArr = $shopTypeMdl->getFieldValue(array('typeValue' => array('IN', array(\Consts::SHOP_TYPE_PLAT, \Consts::SHOP_TYPE_ICBC))), 'shopTypeId');

        $shopMdl = new ShopModel();
        $countShop = $shopMdl->countShop(array('ShopTypeRel.typeId' => array('NOTIN', $shopTypeIdArr)));
        // 获得开通支付商户个数
        $onlinePayShopCount = $shopMdl->countShop(array('ShopTypeRel.typeId' => array('NOTIN', $shopTypeIdArr), 'isAcceptBanKCard' => C('YES')));
        $userMdl = new UserModel();
        // 获得总用户数
        $countUser = $userMdl->countUser(array());
        // 获得总注册用户数
        $regUserCount = $userMdl->countUser(array('status' => array('NEQ', \Consts::USER_STATUS_NOT_REG)));
        // 获得上周注册用户数
        $lastWeekStart = date("Y-m-d 00:00:00",strtotime("-1 week last monday"));
        $lastWeekEnd = date('Y-m-d 23:59:59', strtotime('-1 week Sunday'));
        $lastWeekRegCount = $userMdl->countUser(array('registerTime' => array('BETWEEN', array($lastWeekStart, $lastWeekEnd)), 'status' => array('NEQ', C('USER_STATUS.NOT_REG'))));

        // 获得昨日用户注册数
        $yesterdayStart = date('Y-m-d 00:00:00', strtotime('-1 Day'));
        $yesterdayEnd = date('Y-m-d 23:59:59', strtotime('-1 Day'));
        $yesterdayRegUserCount = $userMdl->countUser(array('registerTime' => array('BETWEEN', array($yesterdayStart, $yesterdayEnd)), 'status' => array('NEQ', C('USER_STATUS.NOT_REG'))));

        $bankAccountMdl = new BankAccountModel();
        // 获得总绑卡用户数
        $signCardCount = $bankAccountMdl->getBankAccountCount(array('BankAccount.status' => \Consts::BANKACCOUNT_STATUS_SIGNED), array(), 'DISTINCT(BankAccount.userCode)');
        // 获得上个月绑卡用户数
        $lastMonthEnd = date('Y-m-d H:i:s', strtotime(date('Y-m-1 23:59:59') . '-1 day'));
        $lastMonthStart = date('Y-m-01 00:00:00', strtotime($lastMonthEnd));
        $lMSignCardCount = $bankAccountMdl->getBankAccountCount(array('BankAccount.createTime' => array('BETWEEN', array($lastMonthStart, $lastMonthEnd)), 'status' => \Consts::BANKACCOUNT_STATUS_SIGNED), array(), 'DISTINCT(userCode)');
        // 获得昨天总绑卡用户数
        $ystdSignCardCount = $bankAccountMdl->getBankAccountCount(array('BankAccount.createTime' => array('BETWEEN', array($yesterdayStart, $yesterdayEnd)), 'status' => \Consts::BANKACCOUNT_STATUS_SIGNED), array(), 'DISTINCT(userCode)');

        $cardMdl = new CardModel();
        $todayAddCard = $cardMdl->todayAddCard();
        $batchCouponMdl = new BatchCouponModel();
        $todayAddBatchCoupon = $batchCouponMdl->todayAddBatchCoupon();
        $cMdl = new CommunicationModel();
        $userUnreadCount =  $cMdl->countUnreadMsg('', C('HQ_CODE'), C('COMMUNICATION_APP.USER'));
        $shopUnreadCount =  $cMdl->countUnreadMsg(C('HQ_CODE'), '', C('COMMUNICATION_APP.SHOP'));
        $assign = array(
            'title' => '中旅BUS验票平台',
            'fdUnreadCount' => ($userUnreadCount + $shopUnreadCount),
            'countShop' => $countShop,
            'onlinePayShopCount' => $onlinePayShopCount,
            'countUser' => $countUser,
            'regUserCount' => $regUserCount,
            'lastWeekRegCount' => $lastWeekRegCount,
            'yesterdayRegUserCount' => $yesterdayRegUserCount,
            'signCardCount' => $signCardCount,
            'lMSignCardCount' => $lMSignCardCount,
            'ystdSignCardCount' => $ystdSignCardCount,
            'todayAddCard' => $todayAddCard,
            'todayAddBatchCoupon' => $todayAddBatchCoupon,
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 用户登录。必须有三个参数：name, password, role。
     */
    public function login() {
        if (IS_GET) {
            $this->display();
        } else {
            $mobileNbr = I('post.mobileNbr');
            $password = md5(I('post.password'));
            $loginType = C('LOGIN_TYPE.ADMIN');
            $baseMdl = new BaseModel();
            $ret = $baseMdl->login($mobileNbr, $password, $loginType);
            if ($ret === true) {
                $this->ajaxSucc('登录成功。', array('firstLogin' => $ret));
            } else {
                $this->ajaxError($ret['code']);
            }
        }
    }

    /**
     * 用户退出登录
     */
    public function logout() {
        session('[destroy]');
        $this->redirect('/Admin/BmStaff/login');
    }

    /**
     * 获得平台操作员列表
     */
    public function listBmStaff() {
        $bmStaffMdl = new BmStaffModel();
        $data = I('get.');
        $data['status'] = array('neq', \Consts::HQ_STAFF_STATUS_DELETE); // 惠圈员工状态不是已删除
        $bmStaffList = $bmStaffMdl->listBmStaff($data, $this->pager);
        $countStaffActionLog = $bmStaffMdl->countBmStaff($data);
        $this->pager->setItemCount($countStaffActionLog);

        $assign = array(
            'title' => '惠圈人员',
            'dataList' => $bmStaffList,
            'get' => I('get.'),
            'nowTime' => time(),
            'pager' => $this->pager,
            'hqStaffType' => C('HQ_STAFF_TYPE_ARRAY'),
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($bmStaffList) ? '' : $this->fetch('BmStaff:listBmStaffWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 管理员修改本人信息
     */
    public function editBmStaff() {
        $mobileNbr = I('get.mobileNbr');
        $bmStaffMdl = new BmStaffModel();
        $bmStaffInfo = $bmStaffMdl->getBMStaffInfo(array('mobileNbr' => $mobileNbr));
        $assign = array(
            'title' => '修改个人信息',
            'bmStaffInfo' => $bmStaffInfo,
            'hqStaffType' => C('HQ_STAFF_TYPE_ARRAY')
        );
        $this->assign($assign);
        if (IS_GET) {
            $this->display();
        } else {
            $data = I('post.');
            $ret = $bmStaffMdl->editBmStaff($data);
            if ($ret === true) {
                $this->ajaxSucc('编辑成功');
            } else {
                $this->ajaxError($ret);
            }
        }
    }

    /**
     * 修改操作员状态
     */
    public function changeStatus() {
        $bmStaffMdl = new BmStaffModel();
        $staffCode = I('get.staffCode');
        $status = I('get.status');
        $res = $bmStaffMdl->changeBmStaffStatus($staffCode, $status);
        if ($res === true) {
            $this->ajaxSucc('操作成功!');
        } else {
            $this->ajaxError($res);
        }
    }

    /**
     * 删除惠圈员工
     */
    public function deleteHqStaff() {
        if(IS_AJAX) {
            $staffCode = I('post.staffCode');
            $bmStaffMdl = new BmStaffModel();
            $ret = $bmStaffMdl->editBmStaff(array('staffCode' => $staffCode, 'status' => \Consts::HQ_STAFF_STATUS_DELETE));
            if($ret === true) {
                $this->ajaxSucc();
            } else {
                $this->ajaxError('删除失败');
            }
        } else {
            echo 'WELCOME TO HUIQUAN';
        }
    }

















}
