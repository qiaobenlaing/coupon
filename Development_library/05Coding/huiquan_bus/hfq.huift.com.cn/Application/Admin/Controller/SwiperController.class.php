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
        //  设置商圈
        $where = array("status" => 1);
        if($_SESSION['USER']['bank_id']!=-1){
            $where['id'] = $_SESSION['USER']['bank_id'];
        }else{
            $zone = D("Bank")->where(array("status"=>1,"parent"=>array("not IN","-1")))->field("id,name")->select();
            foreach ($zone as $item =>&$value){
                $idArr[] = $value['id'];
            }
            $where['id'] = array("IN",$idArr);
        }
        $zone = D("Bank")->where($where)->field("name,id")->select();
        $SwiperModel=new SwiperModel();

        $swiperList = $SwiperModel->listSwiper(I('get.name'),I('get.city'),I("get.bank_id"));

        //图片OSS路径设置
        foreach ($swiperList as $item=>&$value){
            if(!empty($value['url'])){
                $value['url'] = C("urlOSS").$value['url'];
            }
        }

        $assign = array(
            'title' => '优惠券滚动图添加',
            'get'=>I('get.'),
            'dataList' => $swiperList,
            "BankList"=> $zone
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
            $bank_id = I("post.bank_id");

            if(empty($arrImgUrl)){
                $this->ajaxError('必须上传图片');
            }

            if(empty($bank_id)){
                $this->ajaxError('必须选择所属商圈');
            }

            $imgArr = array();
            foreach ($arrImgUrl as $item){
                //图片OSS处理
                if(strpos($item,C("urlOSS"))!==false && !empty($item)){
                    $length = mb_strlen(C("urlOSS"));
                    //$length-1 指的是不截取 https://oss.cloud.hfq.huift.com.cn/ 最后的斜杠
                    //mb_strlen($data['brandLogo'])-$length+1 因为下标是从0开始的所以要加上1，不然图片扩展名缺失
                    $item = substr($item,$length-1,mb_strlen($item)-$length+1);
                }
                $imgArr[] = $item;
            }
            $info=$SwiperModel->addSwiper($province,$city,$imgArr,$bank_id,$arractivityWeb,$arrtitle);

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