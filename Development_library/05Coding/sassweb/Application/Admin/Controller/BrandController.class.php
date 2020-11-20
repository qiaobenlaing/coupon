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

        // 判断是否是OSS显示的图片（显示）
            foreach ($brandList as $item=>&$value){
                if(!is_null($value['brandLogo'])){
                    $value['brandLogo'] = C("urlOSS").$value['brandLogo'];//添加域名前缀
                }
            }
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

            //修改（显示）
            if(!empty($brandInfo['brandLogo'])){
                $brandInfo['brandLogo'] = C("urlOSS").$brandInfo['brandLogo'];//添加域名前缀
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

            //判断是否为惠圈管理人员
            if($_SESSION['USER']['bank_id']!=-1) {
                $data['bank_id'] = $_SESSION['USER']['bank_id'];
            }

            // 判断是否是OSS显示的图片（保存）
            if(!empty($data['brandLogo'])){
                //截取掉前面的https://oss.cloud.hfq.huift.com.cn/
                $start = stripos($data['brandLogo'],C("urlOSS"));
                $length = mb_strlen(C("urlOSS"));
                //$length-1 指的是不截取 https://oss.cloud.hfq.huift.com.cn/ 最后的斜杠
                //mb_strlen($data['brandLogo'])-$length+1 因为下标是从0开始的所以要加上1，不然图片扩展名缺失
                $data['brandLogo'] = substr($data['brandLogo'],$length-1,mb_strlen($data['brandLogo'])-$length+1);
            }

            $ret = $brandMdl->editBrand($data);
            if ($ret['code'] === C('SUCCESS')) {
                $this->ajaxSucc('保存成功', array('brandId' => $ret['brandId']));
            } else {
                $this->ajaxError($ret['code']);
            }
        }
    }
}