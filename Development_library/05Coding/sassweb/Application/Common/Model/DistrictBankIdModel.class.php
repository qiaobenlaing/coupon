<?php
/**
 * Created by PhpStorm.
 * User: 98041
 * Date: 2018/9/13
 * Time: 15:54
 * Desc: 城市saas化
 */

namespace Common\Model;
use Common\Model\BaseModel;

class DistrictBankIdModel extends BaseModel
{
    protected $tableName = 'District';
    const NO_PARENT = 0;

    //获得所有开通的城市列表
    public function SaaslistCity($filterData, $page){
        $where = $this->filterWhere($filterData, array(), $page);
        $where = $this->filterWhereKey($where);

        //查询未开通
        if($where['db.isOpen']=="0"){

            //先剔除查询条件
            unset($where['db.isOpen']);
//            判断是否选择了所属商圈
            if($where['bank_id']!=""){
                //字段转换
                $where['db.bank_id'] = $where['bank_id'];
                if($where['d2.name']!=""){

                       $bank =    D("District_bank_id")->where("bank_id='".$where['db.bank_id']."'")->field("dist_id as id")->select();
                       if(is_array($bank)&& !empty($bank)){ //查了所属商圈和所属省份（并且找到了该商圈的数据的）
                           //存在则剔除存在的城市数据
                           foreach ($bank as $item =>&$value){
                               $dist_ids[] = $value['id'];
                           }
                           $dist_ids_string  = implode(",",$dist_ids);
                           $list_exitence =  $this->table('District as d1')
                               ->field(array('d1.name', 'd1.id','d1.parentId'))
                               ->join('District AS d2 on d1.parentId = d2.id')
                               ->where("d1.id not in (".$dist_ids_string.") and d2.name='".$where['d2.name']."'")
                               ->pager($page)
                               ->order('d1.parentId asc')
                               ->select();

                           foreach ($list_exitence as $item => &$value){
                               $value['isOpen'] = "0";
                               $value['bank_id'] = $where['bank_id'];
                           }
                            return $list_exitence;

                       }else{
                           //查了所属商圈和所属省份（没找到了该商圈的数据的）
                           $list  = $this
                               ->table('District as d1')
                               ->field(array('d1.name', 'd1.id', 'd1.parentId'))
                               ->join('District AS d2 on d1.parentId = d2.id')
                               ->where("d2.name='".$where['d2.name']."'")
                               ->pager($page)
                               ->order('d1.parentId asc')
                               ->select();

                           foreach ($list as $item => &$value){
                               $value['isOpen'] = "0";
                               $value['bank_id'] = $where['bank_id'];
                           }
                           return $list;

                       }

                }else{
                    //查了商圈没查地区
                    $exitence_bank =  D("District_bank_id")->where(array("isOpen=1"=>"1","bank_id"=>$where['db.bank_id']))->getField("dist_id",true);
                    if(is_array($exitence_bank) && !empty($exitence_bank)){
                        //存在则剔除存在的城市数据
                        $where['d1.id'] = array('not IN', $exitence_bank);

                    }else{

                        $list  = $this
                            ->table('District as d1')
                            ->field(array('d1.name', 'd1.id', 'd1.parentId'))
                            ->join('District AS d2 on d1.parentId = d2.id')
                            ->pager($page)
                            ->order('d1.parentId asc')
                            ->select();

                        foreach ($list as $item => &$value){
                            $value['isOpen'] = "0";
                            $value['bank_id'] = $where['bank_id'];
                        }
                        return $list;

                    }

                }
            }else{

                //没查商圈查了地区
                    $list = $this->table('District as d1')
                        ->field(array( 'd1.id'))
                        ->join('District AS d2 on d1.parentId = d2.id')
                        ->join("left join District_bank_id as db on db.dist_id= d1.id")
                        ->order('d1.parentId asc')
                        ->where("d2.name='".$where['d2.name']."' and isOpen='1'")
                        ->select();

                    if(is_array($list) && !empty($list)){
                        //存在则剔除存在的城市数据
                        $where['d1.id'] = array('not IN', $list);
                    }

                //没查商圈也没查地区
                $exitence_bank =  D("District_bank_id")->where(array("isOpen=1"=>"1"))->getField("dist_id",true);

                    if(is_array($exitence_bank) && !empty($exitence_bank)){
                        //存在则剔除存在的城市数据
                        $where['d1.id'] = array('not IN', $exitence_bank);

                    }else{

                        $list  = $this
                            ->table('District as d1')
                            ->field(array('d1.name', 'd1.id', 'd1.parentId'))
                            ->join('District AS d2 on d1.parentId = d2.id')
                            ->pager($page)
                            ->order('d1.parentId asc')
                            ->select();

                        foreach ($list as $item => &$value){
                            $value['isOpen'] = "0";
                            $value['bank_id'] = $where['bank_id'];
                        }
                        return $list;

                    }
            }
            unset($where['bank_id']);
            unset($where['db.bank_id']);
        }

        return $this
            ->table('District as d1')
            ->field(array('d1.name', 'd1.id', 'db.isOpen', 'd1.parentId','db.bank_id'))
            ->join('District AS d2 on d1.parentId = d2.id')
            ->join("left join District_bank_id as db on db.dist_id= d1.id")
            ->where($where)
            ->pager($page)
            ->order('d1.parentId asc')
            ->select();
    }

    /**
     * 统计共有多少个城市
     * @param array $filterData 查询条件
     * @return int
     */
    public function  SaascountCity($filterData) {
        $where = $this->filterWhere($filterData, array(), $page);
        $where = $this->filterWhereKey($where);

        //查询未开通
        if($where['db.isOpen']=="0"){

            //先剔除查询条件
            unset($where['db.isOpen']);
//            判断是否选择了所属商圈
            if($where['bank_id']!=""){
                //字段转换
                $where['db.bank_id'] = $where['bank_id'];
                if($where['d2.name']!=""){

                    $bank =    D("District_bank_id")->where("bank_id='".$where['db.bank_id']."'")->field("dist_id as id")->select();
                    if(is_array($bank)&& !empty($bank)){ //查了所属商圈和所属省份（并且找到了该商圈的数据的）
                        //存在则剔除存在的城市数据
                        foreach ($bank as $item =>&$value){
                            $dist_ids[] = $value['id'];
                        }
                        $dist_ids_string  = implode(",",$dist_ids);
                        $list_exitence =  $this->table('District as d1')
                            ->field(array('d1.name', 'd1.id','d1.parentId'))
                            ->join('District AS d2 on d1.parentId = d2.id')
                            ->where("d1.id not in (".$dist_ids_string.") and d2.name='".$where['d2.name']."'")
                            ->count("d1.id");

                        foreach ($list_exitence as $item => &$value){
                            $value['isOpen'] = "0";
                            $value['bank_id'] = $where['bank_id'];
                        }
                        return $list_exitence;

                    }else{
                        //查了所属商圈和所属省份（没找到了该商圈的数据的）
                        $list  = $this
                            ->table('District as d1')
                            ->field(array('d1.name', 'd1.id', 'd1.parentId'))
                            ->join('District AS d2 on d1.parentId = d2.id')
                            ->where("d2.name='".$where['d2.name']."'")
                            ->count("d1.id");

                        foreach ($list as $item => &$value){
                            $value['isOpen'] = "0";
                            $value['bank_id'] = $where['bank_id'];
                        }
                        return $list;

                    }

                }else{
                    //查了商圈没查地区
                    $exitence_bank =  D("District_bank_id")->where(array("isOpen=1"=>"1","bank_id"=>$where['db.bank_id']))->getField("dist_id",true);

                    if(is_array($exitence_bank) && !empty($exitence_bank)){
                        //存在则剔除存在的城市数据
                        $where['d1.id'] = array('not IN', $exitence_bank);

                    }else{

                        $list  = $this
                            ->table('District as d1')
                            ->field(array('d1.name', 'd1.id', 'd1.parentId'))
                            ->join('District AS d2 on d1.parentId = d2.id')
                            ->count("d1.id");

                        foreach ($list as $item => &$value){
                            $value['isOpen'] = "0";
                            $value['bank_id'] = $where['bank_id'];
                        }
                        return $list;

                    }

                }
            }else{

                //没查商圈查了地区
                $list = $this->table('District as d1')
                    ->field(array( 'd1.id'))
                    ->join('District AS d2 on d1.parentId = d2.id')
                    ->join("left join District_bank_id as db on db.dist_id= d1.id")
                    ->order('d1.parentId asc')
                    ->where("d2.name='".$where['d2.name']."' and isOpen='1'")
                    ->select();

                if(is_array($list) && !empty($list)){
                    //存在则剔除存在的城市数据
                    $where['d1.id'] = array('not IN', $list);
                }

                //没查商圈也没查地区
                $exitence_bank =  D("District_bank_id")->where(array("isOpen=1"=>"1"))->getField("dist_id",true);

                if(is_array($exitence_bank) && !empty($exitence_bank)){
                    //存在则剔除存在的城市数据
                    $where['d1.id'] = array('not IN', $exitence_bank);

                }else{

                    $list  = $this
                        ->table('District as d1')
                        ->field(array('d1.name', 'd1.id', 'd1.parentId'))
                        ->join('District AS d2 on d1.parentId = d2.id')
                        ->order('d1.parentId asc')
                        ->count("d1.id");

                    foreach ($list as $item => &$value){
                        $value['isOpen'] = "0";
                        $value['bank_id'] = $where['bank_id'];
                    }
                    return $list;

                }
            }
            unset($where['bank_id']);
            unset($where['db.bank_id']);
        }

            return $this
                ->table('District as d1')
                ->field(array('d1.name', 'd1.id', 'd1.isOpen', 'd1.parentId'))
                ->join('District AS d2 on d1.parentId = d2.id')
                ->join("left join District_bank_id as db on db.dist_id= d1.id")
                ->where($where)
                ->count('d1.id');
    }


    /**
     * 过滤条件中的字段
     * @param array $where
     * @return array $where
     */
    private function filterWhereKey(&$where) {

        // 判断是否为惠圈人员
        if($_SESSION['USER']['bank_id']!=-1){
            $where['bank_id'] = $_SESSION['USER']['bank_id'];
        }

        //所属省份
        if($where['name'] == ''){
            $provinceIdList = $this->where(array('parentId' => self::NO_PARENT))->getField('id', true);
            $where['d1.parentId'] = array('IN', $provinceIdList);
        } else {
            $where['d2.name'] = $where['name'];
        }
        unset($where['name']);
        //是否开通
        if($where['isOpen'] || $where['isOpen'] === '0') {
            $where['db.isOpen'] = $where['isOpen'];
        } else {
            $where['db.isOpen'] = C('YES');
        }
        unset($where['isOpen']);
        return $where;
    }


    /**
     * 获取所属商圈通已经开通的城市
     * @return array $cityList
     */
    public function zonelistOpenCity($zoneId) {
        //查询条件
        $where =array();
        $where['bank_id'] = $zoneId;
        $where['isOpen'] = "1";

        if($zoneId<1){
                unset($where['bank_id']);
        }

        //查询该商圈所有的开通城市id编号
        $list_ids =    D("District_bank_id")->where($where)->field("dist_id as id")->select();

        foreach ($list_ids as $item => &$value){
            $idArr[] = $value['id'];
        }

        if(is_array($idArr) && !empty($idArr)){
            $where1['d1.id'] = array("IN",$idArr);
        }

        return  $this->table('District as d1')
            ->field(array('d1.name', 'd1.id','d1.parentId'))
            ->join('District AS d2 on d1.parentId = d2.id')
            ->where($where1)
            ->select();
    }

}