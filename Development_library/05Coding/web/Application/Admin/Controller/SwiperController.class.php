<?php
/**
 * Created by PhpStorm.
 * User: 10718
 * Date: 2018/5/28
 * Time: 14:23
 */
namespace Admin\Controller;
use Common\Model\SwiperModel;
use Common\Model\UserModel;
use Common\Model\ShopModel;
use Common\Model\PreShopModel;

class SwiperController  extends  AdminBaseController
{
    public  function  swiperRoll(){
        $SwiperModel=new SwiperModel();
        $swiperList = $SwiperModel->listSwiper(I('get.name'),I('get.city'));


        $assign = array(
            'title' => '优惠券滚动图添加',
            'get'=>I('get.'),
            'dataList' => $swiperList

        );
        $this->assign($assign);


        if (!IS_AJAX) {

            $this->display();
        } else {

            $province=I('post.current-province');
            $city=I('post.current-city');
            if(empty($province)&&empty($city)){
                $this->ajaxError('请先查询所在地');
            }
            $arrImgUrl=I('post.imgUrl');
            $arractivityWeb=I('post.activityWeb');
            $arrtitle=I('post.title');
            $info=$SwiperModel->addSwiper($province,$city,$arrImgUrl,$arractivityWeb,$arrtitle);

            if($info=="success"){
                $this->ajaxSucc('保存成功');
            }else{
                $this->ajaxError('上传失败');
            }

        }
    }

    public function  do_shop_upload(){
        $config = array(
            'mimes'         =>  array(), //允许上传的文件MiMe类型
            'maxSize'       =>  5242880, //上传的文件大小限制(以字节为单位)(0-不做限制)5M
            'exts'          =>  array('jpg', 'gif', 'png', 'jpeg'), //允许上传的文件后缀
            'autoSub'       =>  true, //自动子目录保存文件
            'subName'       =>  array('date', 'Ymd'), //子目录创建方式，[0]-函数名，[1]-参数，多个参数使用数组
            'rootPath'      =>  '',
            'savePath'      =>  './Public/Uploads/', //保存路径
            'saveName'      =>  array('uniqid', ''), //上传文件命名规则，[0]-函数名，[1]-参数，多个参数使用数组
        );
        $upload = new \Think\Upload($config);// 实例化上传类
        $info = $upload->upload(); // 上传图片
        $shopMdl = new ShopModel();
        $preShopMdl = new PreShopModel();
        $url = substr($info['userfile']['savepath'], 1).$info['userfile']['savename'];
        $return_json = array(
            'code' => 200,
            'msg' => 'upload success',
            'url'=> $url,
        );
        echo json_encode($return_json);
        exit;


    }

}