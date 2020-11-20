<?php
namespace Admin\Controller;
use Common\Model\ActivityModel;
use Common\Model\Pager;
use Common\Model\ShopModel;
use Common\Model\UtilsModel;
use Org\FirePHPCore\FP;
/**
 * Activity Controller
 *
 * User: jiangyufeng
 * Date: 2015-05-19
 */
class ActivityController extends AdminBaseController {

    /**
     * 商户活动维护
     */
    public function listSpActivity() {
        $activityMdl = new ActivityModel();
        $filterData = I('get.');
        $page = $filterData['page'] ? $filterData['page'] : 1;
        unset($filterData['page']);
        $filterData['Activity.activityName'] = array('like', '%'.$filterData['activityName'].'%');
        unset($filterData['activityName']);
        $filterData['Shop.shopName'] = array('like', '%'.$filterData['shopName'].'%');
        unset($filterData['shopName']);
        if($filterData['status'] || $filterData['status'] == 0){
            $filterData['Activity.status'] = array('like', '%'.$filterData['status'].'%');
        }
        unset($filterData['status']);
        $filterData['activityBelonging'] = C('ACTIVITY_BELONGING.SHOP');
        $activityList = $activityMdl->getActList($filterData, array('Activity.*', 'shopName'), array(array('joinTable' => 'Shop', 'joinCondition' => 'Shop.shopCode = Activity.shopCode', 'joinType' => 'inner')), 'Activity.createTime desc', Pager::DEFUALT_PAGE_SIZE, $page);
        $activityCount = $activityMdl->countActList($filterData, array(array('joinTable' => 'Shop', 'joinCondition' => 'Shop.shopCode = Activity.shopCode', 'joinType' => 'inner')));
        $this->pager->setItemCount($activityCount);
        $assign = array(
            'title' => '商户活动维护',
            'dataList' => $activityList,
            'get' => I('get.'),
            'pager' => $this->pager
        );
        $this->assign($assign);
        if (! IS_AJAX) {
            $this->display();
        } else {
            $html = empty($activityList) ? '' : $this->fetch('Activity:listSpActivityWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 平台活动维护
     */
    public function listPfActivity() {
        $activityMdl = new ActivityModel();
        $filterData = I('get.');
        $page = $filterData['page'] ? $filterData['page'] : 1;
        unset($filterData['page']);
        $filterData['Activity.activityName'] = array('like', '%'.$filterData['activityName'].'%');
        unset($filterData['activityName']);
//        $filterData['Shop.shopName'] = array('like', '%'.$filterData['shopName'].'%');
//        unset($filterData['shopName']);
        if($filterData['status'] || $filterData['status'] == 0){
            $filterData['Activity.status'] = array('like', '%'.$filterData['status'].'%');
        }
        unset($filterData['status']);
        $filterData['activityBelonging'] = C('ACTIVITY_BELONGING.PLAT');
        $pfActList = $activityMdl->getActList($filterData, array('Activity.*', 'shopName'), array(array('joinTable' => 'Shop', 'joinCondition' => 'Shop.shopCode = Activity.shopCode', 'joinType' => 'inner')), 'Activity.createTime desc', Pager::DEFUALT_PAGE_SIZE, $page);
        $pfActCount = $activityMdl->countActList($filterData, array(array('joinTable' => 'Shop', 'joinCondition' => 'Shop.shopCode = Activity.shopCode', 'joinType' => 'inner')));
        $this->pager->setItemCount($pfActCount);
        $this->assign('dataList', $pfActList);
        $this->assign('get', I('get.'));
        $this->assign('title', '平台活动');
        $this->assign('pager', $this->pager);
        if (! IS_AJAX) {
            $this->display();
        } else {
            $html = empty($pfActList) ? '' : $this->fetch('Activity:listPfActivityWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 工行活动
     */
    public function listIcbcActivity() {
        $activityMdl = new ActivityModel();
        $filterData = I('get.');
        $page = $filterData['page'] ? $filterData['page'] : 1;
        unset($filterData['page']);
        $filterData['Activity.activityName'] = array('like', '%'.$filterData['activityName'].'%');
        unset($filterData['activityName']);
//        $filterData['Shop.shopName'] = array('like', '%'.$filterData['shopName'].'%');
//        unset($filterData['shopName']);
//        if($filterData['status'] || $filterData['status'] == 0){
//            $filterData['Activity.status'] = array('like', '%'.$filterData['status'].'%');
//        }
//        unset($filterData['status']);
        $filterData['activityBelonging'] = C('ACTIVITY_BELONGING.BANK');
        $listIcbcActivity = $activityMdl->getActList($filterData, array('Activity.*', 'shopName'), array(array('joinTable' => 'Shop', 'joinCondition' => 'Shop.shopCode = Activity.shopCode', 'joinType' => 'inner')), 'Activity.createTime desc', Pager::DEFUALT_PAGE_SIZE, $page);
        $countIcbcActivity = $activityMdl->countActList($filterData, array(array('joinTable' => 'Shop', 'joinCondition' => 'Shop.shopCode = Activity.shopCode', 'joinType' => 'inner')));
        $this->pager->setItemCount($countIcbcActivity);
        $this->assign('dataList', $listIcbcActivity);
        $this->assign('get', I('get.'));
        $this->assign('pager', $this->pager);
        $this->assign('title', '工行活动维护');
        if (! IS_AJAX) {
            $this->display();
        } else {
            $html = empty($listIcbcActivity) ? '' : $this->fetch('Activity:listIcbcActivityWidget');
            // FP::dbg($html);
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 活动新增，修改活动
     */
    public function editActivity() {
        $shopMdl = new ShopModel();
        $activityMdl = new ActivityModel();
        if (IS_GET) {
            $activityCode = I('get.activityCode');
            $actInfo = $activityMdl->getActInfo(array('activityCode' => $activityCode), array('Activity.*', 'Shop.shopName'), array(array('joinTable' => 'Shop', 'joinCondition' => 'Shop.shopCode = Activity.shopCode', 'joinType' => 'INNER')));
            $feeScale = $actInfo['feeScale'] ? $actInfo['feeScale'] : array();

            // 设置h5里的按钮
            $actInfo['exButtonList'] = json_decode($actInfo['exButtonList'], true);
            $exButtonList = $actInfo['exButtonList'] ? $actInfo['exButtonList'] : array();
            $actData = array(
                'feeScale' => $feeScale,
                'exButtonList' => $exButtonList,
            );

            $pos = array();
            foreach(C('ACT_POS') as $v){
                $pos[] = array('value' => $v, 'name' => C('ACT_POS_VALUE.'.$v));
            }
            $refundRequired = array();
            foreach(C('ACTIVITY_REFUND_REQUIRED_VALUE') as $k => $v){
                $refundRequired[] = array('value' => $v, 'name' => C('ACTIVITY_REFUND_REQUIRED_NAME.'.$k));
            }
            $activityType = array();
            foreach(C('ACTIVITY_TYPE') as $k => $v){
                $activityType[] = array('value' => $v, 'name' => C('ACTIVITY_TYPE_NAME.'.$k));
            }

            $getShopName = $shopMdl->getShopName();
            $assign = array(
                'getShopName' => $getShopName,
                'activityInfo' => $actInfo,
                'title' => '编辑活动',
                'posArr' => $pos,
                'refundRequired' => $refundRequired,
                'activityType' => $activityType,
                'actData' => $actData,
                'bk' => I('get.bk', 'Sp')
            );
            $this->assign($assign);
            $this->display();
        }else {
            $data = I('post.');
//            $this->ajaxError('', $data);
            // 活动持续时间转化为分钟
            $data['duration'] = (strtotime($data['endTime']) - strtotime($data['startTime'])) / 60;
            $userInfo = session('USER');
            $staffCode = $userInfo['staffCode'];
            $data['creatorCode'] = $staffCode;
            if(isset($data['totalPayment']) && $data['totalPayment'] == 0){
                $data['isRegisterRequired'] = 0;
            }else{
                $totalPayment = UtilsModel::getMaxNbr($data['scalePrice']);
                $data['totalPayment'] = $totalPayment * \Consts::HUNDRED;
                $feeScaleArr = array();
                $maxNbr = UtilsModel::getMaxNbr($data['scaleId']);
                foreach($data['scaleId'] as $k => $id){
                    $price = $data['scalePrice'][$k] * \Consts::HUNDRED;
                    if(!empty($data['scaleDes'][$k]) || $price >= 0){
                        if(empty($id)){
                            $maxNbr += 1;
                            $id = $maxNbr;
                        }
                        $feeScaleArr[] = array(
                            'id' => $id,
                            'des' => $data['scaleDes'][$k],
                            'price' => $price
                        );
                    }

                }
                if($feeScaleArr){
                    $data['feeScale'] = json_encode($feeScaleArr);
                }
            }
            unset($data['scaleId']);
            unset($data['scaleDes']);
            unset($data['scalePrice']);

            if($data['pos'] == C('ACT_POS.SCROLL')){
                if(empty($data['exButtonList']['ebLink'])){
                    unset($data['exButtonList']);
                }else{
                    $exButtonList = array();
                    foreach($data['exButtonList']['ebLink'] as $key => $value){
                        if($value){
                            $exButtonList[] = array(
                                'ebLink' => $data['exButtonList']['ebLink'][$key],
                                'ebWidth' => $data['exButtonList']['ebWidth'][$key],
                                'ebHeight' => $data['exButtonList']['ebHeight'][$key],
                                'ebLeft' => $data['exButtonList']['ebLeft'][$key],
                                'ebTop' => $data['exButtonList']['ebTop'][$key],
                            );
                        }
                    }
                    if($exButtonList){
                        $data['exButtonList'] = json_encode($exButtonList);
                    }else{
                        unset($data['exButtonList']);
                    }
                }
            }else{
                $data['webUrl'] = '';
                unset($data['exButtonList']);
            }

            $ret = $activityMdl->editActivity($data);

            if ($ret['code'] === true) {
                $this->ajaxSucc('编辑成功');
            } else {
                $this->ajaxError($ret);
            }
        }
    }

    /**
     * 修改活动状态
     */
    public function changeStatus() {
        if(IS_AJAX) {
            $activityMdl = new ActivityModel();
            $activityCode = I('get.activityCode');
            $status = I('get.status');
            $res = $activityMdl->changeActivityStatus($activityCode, $status, 1);
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
     * 活动统计分析
     */
    public function analysisActivity()
    {
        $this->assign('title', '活动统计分析');
        $this->display();
    }
}