<?php
/**
 * @author
 */
namespace Common\Model;
use Think\Model;
use Common\Model\ActivityModel;
use Common\Model\CardActionLogModel;

class UtilsModel extends BaseModel {
    protected $tableName = 'UserCard';

    /**
     * 进一取整 例：123 => 123, 123.0 => 123, 123.2 => 124, 123.8 => 124
     * @param $value
     * @return mixed
     */
    public static function phpCeil($value){
        $valueInt = (int)$value;
        if($value != $valueInt){
            $valueArr = explode('.', $value);
            if($valueArr[1] > 0){
                $value = $valueArr[0] + 1;
            }
        }
        return (int)$value;
    }

    /**
     * 将字符串以分隔符切成 Array，然后得到有效的元素
     * @param $string
     * @param string $explode
     * @return array
     */
    public static function uniqueArr($string, $explode = '|'){
        $newArr = array();
        if($string){
            $arr = explode($explode, $string);
            if($arr){
                foreach($arr as $v){
                    if($v && !in_array($v, $newArr)){
                        array_push($newArr, $v);
                    }
                }
            }
        }
        return $newArr;
    }

    /**
     * 是否要享受某种优惠
     * @param $price 消费金额（不含不参与优惠的金额）单位：分
     * @param $realPrice 要支付的金额 单位：分
     * @param $noDiscountPrice 不参与优惠的金额 单位：分
     * @param $minRealPay 最小支付金额 单位：分
     * @return bool
     */
    public static function isContinueToDiscount($price, $realPrice, $noDiscountPrice, $minRealPay){
        if($noDiscountPrice > 0 && $realPrice < 0){
            return false;
        }
        $realPrice = $realPrice + $noDiscountPrice * \Consts::HUNDRED;
        if($price > $minRealPay && $minRealPay > $realPrice){
            return false;
        }
        return true;
    }

    /**
     * 获取数组中最小的数
     * @param $arr
     * @return mixed
     */
    public static function getMinNbr($arr){
        $newArr = array();
        foreach($arr as $v){
            $newArr[$v] = $v;
        }
        ksort($newArr);
        return reset($newArr);
    }

    /**
     * 获取数组中最大的数
     * @param $arr
     * @return mixed
     */
    public static function getMaxNbr($arr){
        $newArr = array();
        foreach($arr as $v){
            $newArr[$v] = $v;
        }
        krsort($newArr);
        return reset($newArr);
    }

    /**
     * 得到下一页的数
     * @param $totalCount
     * @param $page
     * @param int $pageSize
     * @return int
     */
    public static function getNextPage($totalCount, $page, $pageSize = \Consts::PAGESIZE){
        if($totalCount <= $pageSize){
            return $page;
        }else{
            $count = $pageSize * $page;
            if($count >= $totalCount){
                return $page;
            }else{
                return ($page + 1);
            }
        }
    }

    public static function getPhoneArea($mobileNbr){
        header('Content-type:text/html;charset=utf-8');
        $apiurl = 'http://apis.juhe.cn/mobile/get';

        if(empty($mobileNbr) || strlen($mobileNbr) != 11){
            return array('ret' => false);
        }
        $params = array(
            'key' => '7acb4261e2f4fca587a0f1226c7c759c', //您申请的手机号码归属地查询接口的appkey
            'phone' => $mobileNbr, //要查询的手机号码
        );
        $paramsString = http_build_query($params);

        $content = @file_get_contents($apiurl.'?'.$paramsString);
//        $content = '{
//            "resultcode":"200",
//                "reason":"Return Successd!",
//                "result":{
//                    "province":"浙江",
//                    "city":"杭州",
//                    "areacode":"0571",
//                    "zip":"310000",
//                    "company":"中国移动",
//                    "card":"移动动感地带卡"
//                    }
//            }';
        $result = json_decode($content, true);
        if($result['error_code'] == '0'){
//            if($isMatched){
//                $province = $result['result']['province'];
//                $matchedProvince = '浙江';
//                if($matchedProvince == $province){
//                    return array('ret'=> true, 'data' => $result['result']);
//                }else{
//                    return array('ret' => false);
//                }
//            }else{
                return array('ret'=> true, 'data' => $result['result']);
//            }
        }else{
            return array('ret' => false);
//            echo $result['reason']."(".$result['error_code'].")";
        }
    }
}