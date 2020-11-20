<?php
/**
 * Created by PhpStorm.
 * User: huafei ji
 * Date: 15-7-6
 * Time: 下午5:18
 */
namespace Admin\Controller;
use Common\Model\UserCardModel;
use Org\FirePHPCore\FP;


class UserCardController extends AdminBaseController {

    /**
     * 获得用户会员卡列表
     */
    public function listUserCard() {
        $userCardMdl = new UserCardModel();
        $userCardList = $userCardMdl->listUserCard(I('get.'), $this->pager);

        $userCardCount = $userCardMdl->countUserCard(I('get.'));
        $this->pager->setItemCount($userCardCount);
        $assign = array(
            'title' => '用户会员卡列表',
            'dataList' => $userCardList,
            'get' => I('get.'),
            'pager' => $this->pager
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($userCardList) ? '' : $this->fetch('UserCard:listUserCardWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 获得用户会员卡列表
     */
    public function getUserCardList() {
        $userCardMdl = new UserCardModel();
        $userCardList = $userCardMdl->getUserCardList(I('get.'), $this->pager);
        $userCardCount = $userCardMdl->countUserCard(I('get.'));
        $this->pager->setItemCount($userCardCount);
        $assign = array(
            'title' => '用户会员卡列表',
            'dataList' => $userCardList,
            'get' => I('get.'),
            'pager' => $this->pager
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($userCardList) ? '' : $this->fetch('UserCard:UserInfoCardWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    //更改会员卡是否能添加进微信卡包（乔本亮添加）
    public  function inWeixinCard(){
        $data = I("get.");
        $code = D("UserCard")->where(array("userCardCode"=>$data['userCardCode']))
            ->save(array("inWeixinCard"=>$data['inWeixinCard'])) !== false ? C("SUCCESS"):C("API_INTERNAL_EXCEPTION");
        $code = array("code"=>$code);
        if ($code['code'] === C('SUCCESS')) {
            $this->ajaxSucc('操作成功!');
        } else {
            $this->ajaxError($code['code']);
        }
    }
}