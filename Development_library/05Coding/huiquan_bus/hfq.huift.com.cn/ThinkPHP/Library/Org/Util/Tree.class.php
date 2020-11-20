<?php
/**
 * Created by PhpStorm.
 * User: 98041
 * Date: 2018/8/7
 * Time: 13:43
 */

namespace Org\Util;


class Tree{
    public $data=array();
    public $cateArray=array();
    public $arr = array();

    function Tree(){

    }

    public   function setNode ($id,$parent,$value,$arr=""){
        $parent = $parent?$parent:0;
        $this->data[$id]		=	$value;
        $this->cateArray[$id]	=	$parent;
        $this->arr[$id]			=	$arr;
    }

    private  function getChildsTree($id=0){
        $childs=array();
        foreach ($this->cateArray as $child=>$parent){
            if($parent==$id){
                $childs[$child]=$this->getChildsTree($child);
            }
        }
        return $childs;
    }

    public function getChilds($id=0){
        $childArray=array();
        $childs=$this->getChild($id);
        foreach ($childs as $child){
            $childArray[]=$child;
            $childArray=array_merge($childArray,$this->getChilds($child));
        }
        return $childArray;
    }

    private function getChild($id){
        $childs=array();
        foreach ($this->cateArray as $child=>$parent){
            if($parent==$id){
                $childs[$child]=$child;
            }
        }
        return $childs;
    }

    //单线获取父节点
    public function getNodeLever($id){
        $parents=array();
        if (key_exists($this->cateArray[$id],$this->cateArray)){
            $parents[]=$this->cateArray[$id];
            $parents=array_merge($parents,$this->getNodeLever($this->cateArray[$id]));
        }
        return $parents;
    }

    public function getLayer($id,$preStr='|— '){
        return str_repeat($preStr,count($this->getNodeLever($id)));
    }

    public  function getValue ($id){
        return $this->data[$id];
    } // end func

    public  function getArr($id){
        return $this->arr[$id];
    }

}