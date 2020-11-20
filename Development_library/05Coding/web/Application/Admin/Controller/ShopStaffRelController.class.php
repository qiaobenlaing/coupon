<?php
namespace Admin\Controller;
use Common\Model\ShopStaffRelModel;

/**
 * Created by PhpStorm.
 * User: jihuafei
 * Date: 15-11-4
 * Time: 下午2:41
 */
class ShopStaffRelController extends AdminBaseController {

    /**
     * 编辑商家与店员的关系
     */
    public function editRel() {
        if(IS_AJAX) {
            $data = I('post.');
            $shopStaffRelMdl = new ShopStaffRelModel();
            if($data['id'] && empty($data['staffCode'])) {
                $ret = $shopStaffRelMdl->delRel($data['id']);
                $id = '';
            } else {
                $ret = $shopStaffRelMdl->editShopStaffRel($data);
                $id = $ret['id'];
            }
            if($ret['code'] === C('SUCCESS')) {
                $this->ajaxSucc('保存成功', array('id' => $id));
            } else {
                $this->ajaxError($ret['code']);
            }
        }
    }

    /**
     * 删除店员和商户的关系
     */
    public function delRel() {
        $id = I('get.id');
        $shopStaffRelMdl = new ShopStaffRelModel();
        $ret = $shopStaffRelMdl->delRel($id);
        if($ret['code'] == C('SUCCESS')) {
            $staffCode = I('get.staffCode');
            redirect('/Admin/ShopStaff/listStaffShop?staffCode=' . $staffCode);
        }
    }
}