<?php
/**
 * Created by PhpStorm.
 * User: 98041
 * Date: 2019/6/12
 * Time: 11:36
 */

namespace Api\Controller;


use Common\Model\QAdvModel;

class NewsController extends ApiBaseController
{
    //获取首页动态活动栏目
    public function newsList($city,$zoneId){

        $QAdvModel = new QAdvModel();
        $list = $QAdvModel->alias('adv')->join("right join q_advlist on q_advlist.usedid=adv.id")
            ->where(array('q_advlist.state'=>1,'adv.state'=>1,'bank_id'=>$zoneId))
            ->field('adv.title as adtitle,adv.color,adv.width,adv.height,q_advlist.title,sub_title,q_advlist.link,pic,usedid,back_pic')
            ->order('adv.sort,q_advlist.sort,adv.readtime,q_advlist.readtime')->select();

        $advList = array();
        foreach ($list as $item => &$value){
            if(!empty($value['pic'])){
                $value['pic'] = C("urlOSS").$value['pic'];
            }
            if(!empty($value['back_pic'])){
                $value['back_pic'] = C("urlOSS").$value['back_pic'];
            }
            $advList[] = $value;
        }

        $unqieList =   $this->array_val_chunk($advList);
        return $unqieList;
    }


    //二维数组去掉重复值,并保留键值
    public  function array_val_chunk($array){
        $result = array();  //初始化一个数组
        foreach($array as $k=>$v){
            $result[$v['usedid']][]    =   $v;  //根据initial 进行数组重新赋值
        }
        return $result;
    }

}
