<?php
/**
 * Created by PhpStorm.
 * User: 10718
 * Date: 2018/5/28
 * Time: 14:27
 */

namespace Common\Model;
use Think\Model;
use Common\Model\DistrictModel;
class SwiperModel extends BaseModel
{
    protected $trueTableName  = 'Swiper';

    public function listSwiper($province,$city,$bank_id) {
        //根据省查询到Id
      $arrUrl=$this->field(array('url','title','activityWeb'))->where("province='$province' and city='$city'and bank_id='$bank_id'")->select();

      return $arrUrl;
    }

    public function addSwiper($province,$city,$arrImgUrl,$bank_id,$arractivityWeb,$arrtitle){
        //查询是否添加过
        $arrUrl=$this->field('url')->where("province='$province' and city='$city' and bank_id='$bank_id'")->select();
        if($arrUrl){
            $this->where("province='$province' and city='$city' and bank_id='$bank_id'")->delete(); // 删除用户数据
        }
       /* foreach($arrImgUrl as $val){
            $arr=array(
                'province'=>$province,
                'city'=>$city,
                'url'=>$val,
                'createTime' => date('Y-m-d H:i:s', time()),
            );

            $this->add($arr);
        }*/
        foreach ($arrImgUrl as $key => $value) {
            $arr=array(
                'province'=>$province,
                'city'=>$city,
                'url'=>$value,
                'activityWeb'=>$arractivityWeb[$key],
                'title'=>$arrtitle[$key],
                'bank_id'=>$bank_id,
                'createTime' => date('Y-m-d H:i:s', time()),
            );

            $this->add($arr);
        }
        return "success";
    }
}