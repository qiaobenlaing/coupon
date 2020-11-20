<?php
/**
 * Bonus Controller
 *
 * User: jiangyufeng
 * Date: 2015-05-19
 */
namespace Admin\Controller;
use Common\Model\BonusModel;
use Common\Model\ShopModel;
use Common\Model\DistrictModel;
use Org\FirePHPCore\FP;

class BonusController extends AdminBaseController {

    /**
     * 修改红包状态
     */
    public function changeStatus() {
        $bonusMdl = new BonusModel();
        $bonusCode = I('get.bonusCode');
        $status = I('get.status');
        $res = $bonusMdl->changeBnousStatus($bonusCode, $status);
        if ($res === true) {
            $this->ajaxSucc('操作成功!');
        } else {
            $this->ajaxError($res);
        }
    }

    /**
     * 获得商家红包
     */
    public function listSpBonus() {
        $bonusMdl = new BonusModel();
        $this->assign('title', '商户红包维护');
        $filterData = I('get.');
        $filterData['bonusBelonging'] = C('Bonus_BELONGING.SHOP');
        $listSpBonus = $bonusMdl->listBonus($filterData, $this->pager);
        $this->assign('dataList', $listSpBonus);
        $this->assign('get', I('get.'));
        $this->assign('nowTime', time());
        $countSpBonus = $bonusMdl->countBonus($filterData);
        $this->pager->setItemCount($countSpBonus);
        $this->assign('pager', $this->pager);
        if (! IS_AJAX) {
            $this->display();
        } else {
            $html = empty($listSpBonus) ? '' : $this->fetch('Bonus:listSpBonusWidget');
            // FP::dbg($html);
            $this->ajaxSucc('', null, $html);
        }       
    }

    /**
     * 获得平台红包
     */
    public function listPfBonus() {
        $bonusMdl = new BonusModel();
        $filterData = I('get.');
        $filterData['bonusBelonging'] = C('Bonus_BELONGING.PLAT');
        $pfBonusList = $bonusMdl->listBonus($filterData, $this->pager);
        $countPfBonus = $bonusMdl->countBonus($filterData);
        $this->pager->setItemCount($countPfBonus);
        $assign = array(
            'title' => '平台红包维护',
            'dataList' => $pfBonusList,
            'get' => I('get.'),
            'nowTime' => time(),
            'pager' => $this->pager

        );
        $this->assign($assign);
        if (! IS_AJAX) {
            $this->display();
        } else {
            $html = empty($pfBonusList) ? '' : $this->fetch('Bonus:listPfBonusWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 编辑红包信息
     */
    public function editBonus() {
        $shopMdl = new ShopModel();
        $bonusMdl = new BonusModel();
        if (IS_GET) {
            $bonusCode = I('get.bonusCode');     
            $bmCode = C('HQ_CODE');
            $bonusInfo = $bonusMdl->getBonusInfo($bonusCode);
            $shopNameList = $shopMdl->getShopName();
            $back = I('get.back');
            if(empty($back)) {
                $back = 'listPfBonus';
            }
            $assign = array(
                'title' => '编辑红包',
                'bonusInfo' => $bonusInfo,
                'bmCode' => $bmCode,
                'getShopName' => $shopNameList,
                'back' => $back,
                'page' => I('get.page'),
            );
            $this->assign($assign);
            $this->display();
        }else {
            $data = I('post.');
            $userInfo = session('USER');
            $staffCode = $userInfo['staffCode'];
            $data['creatorCode'] = $staffCode;         
            if(!empty($data['bonusCode'])) {
                if($data['shopCode'] == C('HQ_CODE')) {
                    $data['bonusBelonging'] = C('BONUS_BELONGING.PLAT');
                }else {
                    $data['bonusBelonging'] = C('BONUS_BELONGING.SHOP');
                }
            } else {
                $data['bonusBelonging'] = C('BONUS_BELONGING.PLAT');
                $data['shopCode'] = C('HQ_CODE');
            }
            unset($data['ruleName']);
            unset($data['ruleDes']);
            $ret = $bonusMdl->editBonus($data);
            if ($ret['code'] === true) {
                $this->ajaxSucc('编辑成功');
            } else {
                $this->ajaxError($ret);
            }
        }
    }
    
    public function analysisBonus() {
        $districtMdl = new DistrictModel();
        $cityList = $districtMdl->listOpenCity();
        $bonusMdl = new BonusModel();
        $data = $bonusMdl->analysisBonus(I('get.'), $this->pager);
        $countPfBonus = $bonusMdl->countAnalysisBonus(I('get.'));
        $this->pager->setItemCount($countPfBonus);
        $assign = array(
            'title' => '红包统计分析',
            'cityList' => $cityList,
            'get' => I('get.'),
            'data' => $data,
            'pager' => $this->pager
        );
        $this->assign($assign);
        if (! IS_AJAX) {
            $this->display();
        } else {
            $html = empty($data) ? '' : $this->fetch('Bonus:analysisBonusWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    public function shopBonusAnalysis() {
        $data = I('get.');
        $shopMdl = new ShopModel();
        $shopInfo = $shopMdl->getShopInfo($data['shopCode'], array('Shop.shopCode', 'shopName'));
        $startTime = $data['startTime'];
        $endTime = $data['endTime'];
        if($startTime && !$endTime) {
            $condition['createTime'] = array('EGT', $startTime);
        }
        if(!$startTime && $endTime) {
            $condition['createTime'] = array('ELT', $endTime);
        }
        if($startTime && $endTime) {
            $condition['createTime'] = array('between', array($startTime, $endTime));
        }
        $condition['shopCode'] = $data['shopCode'];
        $bonusMdl = new BonusModel();
        $analysisData = $bonusMdl->analysisShopBonus($condition);
        $assign = array(
            'title' => $shopInfo['shopName'] . '：红包统计分析',
            'shopInfo' => $shopInfo,
            'get' => $data,
            'analysisData' => $analysisData
        );
        $this->assign($assign);
        $this->display();
    }
}