<?php
use Org\FirePHPCore\FP;
/**
 * 生成上传文件的文件名
 * @return string
 */
function gen_upload_filename() {
	return date('ymdhis') . rand(1000, 9999);
}


// 选中判断
function isCheck($iCid,$arrId,$ed = "checked",$html=""){
    if($iCid!=$arrId){
        $ed = $html;
    }
    return $ed;
}

/**
 * 检查给定的值是否在某个数组的一列中
 * @param string|number $needle 待检查的值
 * @param array $haystack 待检查的二维数组
 * @param string $col 二维数组的某一列名/对象数组的属性名
 * @param string $isArray
 * @return boolean true:存在于$col列中; 否则返回false
 */
function in_array_col($needle, $haystack, $col, $isArray = true) {

	if (!$haystack)
		return false;
	 if ($isArray) {
	 	foreach ($haystack as $item) {
// 	 		FP::dbg($haystack[$col]);
// 	 		FP::dbg($needle);
// 	 		FP::dbg('$item[$col]==$needle:' . ($haystack[$col] == $needle));
	 		if (isset($item[$col]) && $item[$col] == $needle) {
	 			return true;
	 		}
	 	}
	 	return false;
	 } else {
	 	foreach ($haystack as $item) {
// 	 		FP::dbg($item->$col);
// 	 		FP::dbg($needle);
// 	 		FP::dbg('$item->$col==$needle:' . ($item[$col] == $needle));
	 		if (isset($item->$col) && $item->$col == $needle) {
	 			return true;
	 		}
	 	}
	 	return false;
	 }
}


/*关联信息
    $datable  数据模型
    $info 关联字段信息
    $sql查询条件
*/
function getName($datable,$info,$sql){
    $table=D($datable);
    $list=$table->where($sql)->order("id desc")->find();
    return $list[$info];
}