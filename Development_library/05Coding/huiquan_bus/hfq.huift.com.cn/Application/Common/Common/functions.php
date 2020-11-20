<?php

function dbg($obj) {
	$firephp = FirePHP::getInstance(true);
	$firephp->fb('Hello World');
}


//UTF-8切割字符串
function cn_substr($string, $length, $etc = '...'){
    $result = '';
    $string = html_entity_decode(trim(strip_tags($string)), ENT_QUOTES, 'UTF-8');
    $strlen = strlen($string);
    for($i = 0; (($i < $strlen) && ($length > 0)); $i++){
        if($number = strpos(str_pad(decbin(ord(substr($string, $i, 1))), 8, '0', STR_PAD_LEFT), '0')){
            if($length < 1.0){
                break;
            }

            $result .= substr($string, $i, $number);
            $length -= 1.0;
            $i += $number - 1;

        }else{

            $result .= substr($string, $i, 1);
            $length -= 0.5;
        }
    }

    $result = htmlspecialchars($result, ENT_QUOTES, 'UTF-8');
    if($i < $strlen){
        $result .= $etc;
    }
    return $result;
}
function buildCode(){
    date_default_timezone_set('PRC');
    $actCode = date('Y',time())-2016;
    strlen($actCode)>2 ? ($actCode) : $actCode=('0'.$actCode);
    $actCode .= date('m',time());
    for($i=0;$i<6;$i++) {
        $actCode .= rand(0, 9);
    }
    return $actCode;
}
