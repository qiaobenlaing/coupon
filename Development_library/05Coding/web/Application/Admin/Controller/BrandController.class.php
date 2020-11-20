<?php
namespace Admin\Controller;
use Common\Model\BrandModel;

/**
 * Created by PhpStorm.
 * User: jihuafei
 * Date: 15-11-3
 * Time: 下午5:05
 */
class BrandController extends AdminBaseController {
    /**
     * 获得品牌列表
     */
    public function listBrand() {
        $brandMdl = new BrandModel();
        $filterData = I('get.');
        $brandList = $brandMdl->listBrand($filterData, $this->pager);
        $brandCount = $brandMdl->countBrand($filterData);
        $this->pager->setItemCount($brandCount);

        $assign = array(
            'title' => '品牌列表',
            'get' => I('get.'),
            'dataList' => $brandList,
            'pager' => $this->pager
        );
        $this->assign($assign);
        if (! IS_AJAX) {
            $this->display();
        } else {
            $html = empty($brandList) ? '' : $this->fetch('Brand:listBrandWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     *
     */
    public function editBrand() {
        if(IS_GET) {
            $brandId = I('get.brandId');
            if(empty($brandId)){
                $brandInfo = array();
            } else {
                $brandMdl = new BrandModel();
                $brandInfo = $brandMdl->getBrandInfo(array('brandId' => $brandId), array('*'));
            }
            $assign = array(
                'brandInfo' => $brandInfo,
                'page' => I('get.page'),
                'title' => '编辑品牌',
            );
            $this->assign($assign);
            $this->display();
        } else {
            $data = I('post.');
            $brandMdl = new BrandModel();
            $ret = $brandMdl->editBrand($data);
            if ($ret['code'] === C('SUCCESS')) {
                $this->ajaxSucc('保存成功', array('brandId' => $ret['brandId']));
            } else {
                $this->ajaxError($ret['code']);
            }
        }
    }
}