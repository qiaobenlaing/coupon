<?php
/**
 * Created by PhpStorm.
 * User: Administrator
 * Date: 15-1-27
 * Time: 下午7:35
 */

namespace Common\Model;
use Org\Net\CurlRequest;

class baiduMapModel {

    public function getLocation($lat, $lng){
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, "http://api.map.baidu.com/geocoder/v2/?ak=yK0PEo7lS7SmOb5Gu6vne2Is&callback=renderReverse&location=$lat,$lng&output=json&pois=1");
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_HEADER, 0);
        $arr = array("renderReverse&&renderReverse(",")");
        $location = json_decode(str_replace($arr, "", curl_exec($ch)));
        curl_close($ch);
        return $location;
    }

}