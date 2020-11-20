<?php
/**
 * Card Controller
 *
 * User: jiangyufeng
 * Date: 2015-05-19
 */
namespace Admin\Controller;
use Common\Model\DistrictModel;
use Common\Model\ShopModel;
use Common\Model\CardModel;
use Common\Model\BackgroundTemplateModel;
use Org\FirePHPCore\FP;
class CardController extends AdminBaseController {

    /**
     * 会员卡列表
     */
    public function listCard() {
        $cardMdl = new CardModel();
        $cardList = $cardMdl->listCard(I('get.'), $this->pager);
        $cardCount = $cardMdl->countCard(I('get.'));
        $this->pager->setItemCount($cardCount);
        $assign = array(
            'title' => '会员卡列表',
            'dataList' => $cardList,
            'get' => I('get.'),
            'pager' => $this->pager
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($cardList) ? '' : $this->fetch('Card:listCardWidget');
            $this->ajaxSucc('', null, $html);
        }
    }
    
    /**
     * 会员卡新增
     */
    public function editCard() {
        $shopMdl = new ShopModel();
        $cardMdl = new CardModel();
        $backgroundTemplateMdl = new BackgroundTemplateModel();
        if(IS_GET) {
            $cardCode = I('get.cardCode');
            $getShopName = $shopMdl->getManagerShopName();
            $cardInfo = $cardMdl->getCardInfo($cardCode);

            //图片OSS处理
            if(!empty($cardInfo['url'])){
                $cardInfo['url'] = C("urlOSS").$cardInfo['url'];
            }

            $assign = array(
                'getShopName' => $getShopName,
                'title' => '会员卡编辑',
                'cardInfo' => $cardInfo,
                'page' => I('get.page')
            );
            $this->assign($assign);
            $this->display();
        }else {
            $data = I('post.');
            $userInfo = session('USER');
            $staffCode = $userInfo['staffCode'];
            $data['creatorCode'] = $staffCode;

            if(!$data['isRealNameRequired']) $data['isRealNameRequired'] = C('NO');
            if(!$data['isSharable']) $data['isSharable'] = C('NO');

            $ret = $cardMdl->editCard($data);

            if ($ret['code'] === true) {
                $this->ajaxSucc('添加成功');
            } else {
                $this->ajaxError($ret);
            }
        }        
    }
    
    /**
     * 修改会员卡状态
     */
    public function changeStatus()
    {
        $cardMdl = new CardModel();
        $cardCode = I('get.cardCode');
        $status = I('get.status');
        $res = $cardMdl->changeCardStatus($cardCode, $status);
        if ($res === true) {
            $this->ajaxSucc('操作成功!');
        } else {
            $this->ajaxError($res);
        }
    }
    
    /**
     * 会员卡统计分析
     */
    public function analysisCard() {
        $districtMdl = new DistrictModel();
        $cityList = $districtMdl->listOpenCity();
        $cardMdl = new CardModel();
        $data = $cardMdl->analysisCard(I('get.'));
        $assign = array(
            'title' => '会员卡统计分析',
            'cityList' => $cityList,
            'data' => $data,
            'get' => I('get.'),
        );
        $this->assign($assign);
        $this->display();
    }
}