<?php
/**
 * Created by PhpStorm.
 * User: 98041
 * Date: 2019/6/4
 * Time: 16:34
 */

namespace Admin\Controller;


use Common\Model\ShopModel;

class ShopLinkController extends AdminBaseController{

    /**
     * 第三方关联信息
     */
        public function linkShopList(){
            $shopModel = new ShopModel();
            //判断用户所属商圈列表
            $where = array();
            $where['shop.status'] = 1;
            $get = I("get.");
            if(!empty($get['shopCode'])){
                $where['shoplink.shopCode'] = $get['shopCode'];
            }

            if(!empty($get['shopName'])){
                $where['shop.shopName'] =array("LIKE", '%'.$get['shopName'].'%');
            }

            if(!empty($get['thirdShopCode'])){
                $where['thirdShopCode'] = $get['thirdShopCode'];
            }


            $where = array();
            if($_SESSION['USER']['bank_id']!=-1){
                $where['bank_id'] = $_SESSION['USER']['bank_id'];
            }

            $shopLinkList =      $shopModel->join("shoplink ON shoplink.shopCode = shop.shopCode")
                ->field(array("shoplink.shopCode,shopName,logoUrl,thirdShopCode,shop.status,id"))->where($where)->select();

            $shopLinkCount = $shopModel->join("shoplink ON shoplink.shopCode = shop.shopCode")
                ->field(array("shoplink.shopCode,shopName,logoUrl,thirdShopCode,shop.status,id"))->where($where)->count('shop.shopCode');
            foreach ($shopLinkList as $item => $value){
                $shopLinkList[$item]['logoUrl'] = C("urlOSS").$value['logoUrl'];
            }
            $this->pager->setItemCount($shopLinkCount);
            $assign = array(
                'title' => '商户关联信息列表',
                'dataList' => $shopLinkList,
                'get' => I('get.'),
                'pager' => $this->pager
            );
            $this->assign($assign);
            if (!IS_AJAX) {
                $this->display();
            } else {
                $html = empty($shopLinkList) ? '' : $this->fetch('ShopLink:linkShopListWidget');
                $this->ajaxSucc('', null, $html);
            }

        }

    /**
     * 添加商户信息
     */
        public function addShopLink(){
            $this->assign('title', '关联信息编辑');
            $zoneId = array();
            $where = array();
            if($_SESSION['USER']['bank_id']!=-1){
                $zoneId['bank_id'] =  $_SESSION['USER']['bank_id'];
                $where['bank_id'] = array('in',$zoneId);
            }
            $where['status'] = array('eq',1);
            $shopModel = new ShopModel();
            $shopList =     $shopModel->where($where)->field('shopCode,shopName')->select();
            $assign = array(
                'title' => '商户关联信息列表',
                'dataList' => $shopList,
            );
            $this->assign($assign);
            $this->display();
        }

    /**
     * 操作商户关联信息
     */
        public function insertShopLink(){
            $rules = array(
                array('shopCode','require','商家编号必须填写或已存在'),
                array('thirdShopCode','require','第三方商家编号必须填写'),
            );

            $shopLinkModel = D("shoplink");
            if($shopLinkModel->validate($rules)->create()){
                $data = I('post.');

                $info =    $shopLinkModel->where(array('shopCode'=>$data['shopCode']))->find();
                if($info){
                    $this->ajaxError("不能重复添加商户");
                }

                $result =   $shopLinkModel->add($data);
                if($result){
                    $this->ajaxSucc('操作成功');
                }else{
                    $this->ajaxError($shopLinkModel->getError());
                }
            }else{
                $this->ajaxError($shopLinkModel->getError());
            }
        }

    /**
     * 操作删除
     */
        public function deleteShopLink(){
            $id =I("get.id");
            $shopLinkModel = D("shoplink");
            $result =   $shopLinkModel->where(array('id'=>$id))->delete();
            if($result){
                header("Location:/Admin/ShopLink/linkShopList");
            }else{
                $this->ajaxError("操作失败");
            }
        }


    /**
     * 修改商户关联信息表
     */
        public function editShopLink(){

            $this->assign('title', '关联信息编辑');
            if(IS_GET) {
                $id = I("get.id");
                $shopModel = new ShopModel();
                $shopLinkInfo =$shopModel->where(array('id'=>$id,'status'=>1))->join("shoplink ON shoplink.shopCode = shop.shopCode")
                    ->field(array("shoplink.shopCode,shopName,logoUrl,thirdShopCode,shop.status,id"))->find();
                if(strlen($shopLinkInfo['logoUrl'])>10){
                    $shopLinkInfo['logoUrl'] = C("urlOSS").$shopLinkInfo['logoUrl'];
                }
                $this->assign('shopLink', $shopLinkInfo);
                $this->display();
            }elseif (IS_POST){
                $data = I('post.');
                $result =   D("shoplink")->save($data);
                if($result){
                    $this->ajaxSucc('操作成功');
                }else{
                    $this->ajaxError("操作失败");
                }
            }
        }

    /**导出商户数据
     * @throws \Exception
     */
    public function exportAllLinkShop(){
        $condition = I('get.');
        if(!empty($get['shopCode'])){
            $where['shoplink.shopCode'] = $condition['shopCode'];
        }

        if(!empty($get['shopName'])){
            $where['shop.shopName'] =array("LIKE", '%'.$condition['shopName'].'%');
        }

        if(!empty($get['thirdShopCode'])){
            $where['thirdShopCode'] = $condition['thirdShopCode'];
        }
        $shopModel = new ShopModel();
        $shopLinkList =  $shopModel->join("shoplink ON shoplink.shopCode = shop.shopCode")
            ->field(array("shoplink.shopCode,shopName,logoUrl,thirdShopCode,shop.status,id"))->where($where)->select();
        if(empty($shopLinkList)){
            $url = '/Admin/Shop/linkShopList';
            header('content-type:text/html;charset=utf-8;');
            echo '<script>alert("没有符合条件的数据！");</script>';
            echo "<SCRIPT LANGUAGE=\"JavaScript\">location.href='$url'</SCRIPT>";
            exit;
        }
        //导入类库
        Vendor("PHPExcel.PHPExcel");
        $excel = new \PHPExcel();
        $excel->getDefaultStyle()->getAlignment()->setHorizontal(\PHPExcel_Style_Alignment::HORIZONTAL_LEFT);
        $excel->getDefaultStyle()->getAlignment()->setVertical(\PHPExcel_Style_Alignment::VERTICAL_CENTER);
        $excel->getDefaultStyle()->getAlignment()->setWrapText(1);
        $objActSheet = $excel->getActiveSheet();

        //列
        $letter = array('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');
        //表头
        $title = array('商户编号', '商户名称', '第三方商户编号');

        $column = count($title);

        for($i = 0;$i < $column;$i++){
            //设置表格宽度
            $objActSheet->getColumnDimension("$letter[$i]")->setWidth(20);

            //设置表头样式
            $head_row = 1;
            $objActSheet->getStyle("$letter[$i]$head_row")->getFont()->setSize(12);
            $objActSheet->getStyle("$letter[$i]$head_row")->getFont()->setBold(true);
            //填充表头
            $objActSheet->setCellValue("$letter[$i]$head_row", $title[$i]);
        }

        $row = 2;
        foreach($shopLinkList as $v){
            $objActSheet->setCellValue("$letter[0]$row", $v['shopCode']);
            $objActSheet->setCellValue("$letter[1]$row", $v['shopName']);
            $objActSheet->setCellValue("$letter[2]$row", $v['thirdShopCode']);
            $row += 1;
        }

        $write = new \PHPExcel_Writer_Excel2007($excel);
        ob_end_clean();
        header("Pragma: public");
        header("Expires: 0");
        header("Cache-Control:must-revalidate, post-check=0, pre-check=0");
        header("Content-Type:application/force-download");
        header("Content-Type:application/vnd.ms-excel");
        header("Content-Type:application/octet-stream");
        header("Content-Type:application/download");
        header('Content-Disposition:attachment;filename="关联商户数据信息-'.date('Y-m-d', time()).'.xlsx"');
        header("Content-Transfer-Encoding:utf-8");
        $write->save('php://output');
    }
}
