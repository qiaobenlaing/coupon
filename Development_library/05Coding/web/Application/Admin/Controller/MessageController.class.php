<?php
/**
 * Message Controller
 *
 * User: jiangyufeng
 * Date: 2015-05-19
 */
namespace Admin\Controller;
use Common\Model\MessageModel;
use Common\Model\ShopTypeRelModel;
use Org\FirePHPCore\FP;


class MessageController extends AdminBaseController {

    /**
     * 平台消息维护
     */
    public function listMessage() {
        $MessageMdl = new MessageModel();
        $where = I('get.');
        $shopTypeRelMdl = new ShopTypeRelModel();
        $shopCodeArr = $shopTypeRelMdl->getFieldList(array('ShopType.typeValue' => array('EQ', \Consts::SHOP_TYPE_PLAT)), 'shopCode', array(array('joinTable' => 'ShopType', 'joinCondition' => 'ShopType.shopTypeId = ShopTypeRel.typeId', 'joinType' => 'inner')));
        if(empty($shopCodeArr)){
            $shopCodeArr = array('0');
        }
        $shopCodeArr = array_unique($shopCodeArr);
        $where['Shop.shopCode'] = array('IN', $shopCodeArr);
        $listMessage = $MessageMdl->getMessageList($where, $this->pager);
        $countMessage = $MessageMdl->countMessage($where);
        $this->pager->setItemCount($countMessage);
        $assign = array(
            'title' => '平台发送的消息',
            'dataList' => $listMessage,
            'get' => I('get.'),
            'pager' => $this->pager
        );
        $this->assign($assign);
        if (!IS_AJAX) {
			$this->display();
		} else {
			$html = empty($listMessage) ? '' : $this->fetch('Message:listMessageWidget');
			$this->ajaxSucc('', null, $html);
		}
    }
    
    /**
     * 消息查询统计
     */
    public function analysisMessage() {
        $this->assign('title', '平台消息维护');
        $this->display();
    }
    

}