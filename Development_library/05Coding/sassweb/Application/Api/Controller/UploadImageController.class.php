<?php
/**
 * Created by PhpStorm.
 * User: 10718
 * Date: 2018/4/16
 * Time: 15:23
 */

namespace Api\Controller;
use Think\Controller;
use Common\Model\UserModel;
use Common\Model\CollectCouponModel;

class UploadImageController extends  Controller
{
    /**
     *H5上传头像
     */
    public function uploadHeadImage(){
	$UserModel=D('User');	
        if(IS_POST) {
            $userCode=$_POST['userCode'];

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
			
			//获取图片名称
			$fileUploadInfo=$info['Img'];			
			$ImgName=$fileUploadInfo['savename'];
			//获取图片路径
			$savepath=$fileUploadInfo['savepath'];
			$filepath=str_replace('.','',$savepath);
            if(!$info) {
                // 上传错误提示错误信息
                //return  json_encode(array('code' => $upload->getError()));
                return "error";
            }else {  // 上传成功，获取上传文件信息
							
				$UserModel->avatarUrl=$filepath.$ImgName;
				$UserModel->where("userCode='$userCode'")->save();						  			
				return  "上传成功";
            }
        }
    }
	public function aaa(){
		$CollectCouponModel=D('CollectCoupon');
		if(!empty(I('post.*'))){
			$arr=array(
            'userCode'=>I('post.*')
			);
			$CollectCouponModel->add($arr);
		}
		
		
	}
}