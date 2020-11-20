<?php
namespace Admin\Controller;
use Common\Model\ShopDecorationModel;

/**
 * Created by PhpStorm.
 * User: Jihuafei
 * Date: 15-11-6
 * Time: 上午11:42
 */
class ShopDecorationController extends AdminBaseController {

    /**
     * 删除商家背景图片
     */
    public function delDecoration() {
        if(IS_AJAX) {
            $decorationCode = I('post.decorationCode');
            $shopDecorationMdl = new ShopDecorationModel();
            $ret = $shopDecorationMdl->delShopDecoration($decorationCode);
            if($ret['code'] == C('SUCCESS')) {
                $this->ajaxSucc();
            } else {
                $this->ajaxError('删除失败');
            }
        }
    }
}