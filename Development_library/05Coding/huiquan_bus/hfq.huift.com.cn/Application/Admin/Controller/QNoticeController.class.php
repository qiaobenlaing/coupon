<?php
/**
 * Created by PhpStorm.
 * User: 98041
 * Date: 2019/6/13
 * Time: 8:42
 */

namespace Admin\Controller;


use Common\Model\BankModel;
use Common\Model\QAdvListModel;
use Common\Model\QAdvModel;

class QNoticeController extends AdminBaseController
{

    //广告通知列表
    public function listAdv(){
        $Qadv =new QAdvModel();

        $where = array();
        if($_SESSION['USER']['bank_id']!=-1){
            $where['bank_id']=  $_SESSION['USER']['bank_id'];
        }

        $qaList = $Qadv->advList(array_merge(I("get."),$where),$this->pager);

        $list = array();
        foreach ($qaList as $item => &$value){
            $value['readtime'] = date('Y-m-d', $value['readtime']);
            if(!empty($value['pic'])){
                $value['pic'] = C("urlOSS").$value['pic'];
            }
            if(!empty($value['back_pic'])){
                $value['back_pic'] = C("urlOSS").$value['back_pic'];
            }
            $list[] = $value;
        }
        //所属分类列表
        $qadvModel = new QAdvModel();
        $sortList =    $qadvModel->adSort(array('id,title'),array_merge(array('state'=>1),$where));
        //所属商圈
        $zoneId = array();
        $where = array();
        if($_SESSION['USER']['bank_id']!=-1){
            $zoneId['bank_id'] =  $_SESSION['USER']['bank_id'];
            $where['id'] = $zoneId['bank_id'];
        }
        $where['status'] = array('eq',1);
        $bankModel  = new BankModel();
        $bankList = $bankModel->where($where)->select();

        $qacount = $Qadv->advCount(I('get.'));
        $this->pager->setItemCount($qacount);
        $assign = array(
            'title' => '广告列表',
            'dataList' => $list,
            'sortList'=>$sortList,
            'bankList'=>$bankList,
            'get' => I('get.'),
            'pager' => $this->pager
        );

        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($qaList) ? '' : $this->fetch('Notice:listAdvWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    //广告分类列表
    public function listSort(){
        $Qadv =new QAdvModel();

        $where = array();
        if($_SESSION['USER']['bank_id']!=-1){
            $where['bank_id']=  $_SESSION['USER']['bank_id'];
        }

        $list = $Qadv->listSort(array_merge(I("get."),$where),$this->pager);
        $count = $Qadv->countSort(I('get.'));
        $this->pager->setItemCount($count);
        $advlist = array();
        foreach ($list as $item => &$value){
            $value['readtime'] = date('Y-m-d', $value['readtime']);
            $advlist[] = $value;
        }
        //所属商圈
        $zoneId = array();
        $where = array();
        if($_SESSION['USER']['bank_id']!=-1){
            $zoneId['bank_id'] =  $_SESSION['USER']['bank_id'];
            $where['id'] = array('in',$zoneId);
        }
        $where['status'] = array('eq',1);
        $bankModel  = new BankModel();
        $bankList = $bankModel->where($where)->select();
        $assign = array(
            'title' => '分类列表',
            'dataList' => $advlist,
            'bankList'=>$bankList,
            'get' => I('get.'),
            'pager' => $this->pager
        );
        $this->assign($assign);
        $this->display();
    }

    //编辑分类
    public function editSort(){
        if (IS_GET) {
            //所属分类列表
            $qadvModel = new QAdvModel();
            $sortInfo = $qadvModel->where(array('id'=>I('get.id')))->find();
            //所属商圈
            $zoneId = array();
            $where = array();
            if($_SESSION['USER']['bank_id']!=-1){
                $zoneId['bank_id'] =  $_SESSION['USER']['bank_id'];
                $where['id'] = $zoneId['bank_id'];
            }
            $where['status'] = array('eq',1);
            $bankModel = new BankModel();
            $bankList = $bankModel->where($where)->select();
            $assign = array(
                'bankList' => $bankList,
                'sortInfo' => $sortInfo,
                'page' => I('get.page'),
                'title' => '编辑分类',
            );
            $this->assign($assign);
            $this->display();
        }else{
            $data = I('post.');
            $qadvModel = new QAdvModel();
            $vo = $qadvModel->create($data);
            if ( !$vo){
                // 如果创建失败 表示验证没有通过 输出错误提示信息
                $this->ajaxError($qadvModel->getError());
            }else{
                if(!empty($data['id'])){
                    $result =    $qadvModel->save($vo);
                }else{
                    $result =    $qadvModel->add($vo);
                }
                if($result){
                    $this->ajaxSucc('编辑成功');
                }else{
                    $this->ajaxError('编辑失败');
                }
            }
        }
    }

    //删除分类
    public function deleteSort(){

        $id = I('get.id');
        $qadvModel = new QAdvModel();
        $res = $qadvModel->where(array('id'=>$id))->delete();
        $advListModel = new QAdvListModel();
        $result = $advListModel->where(array('usedid'=>$id))->delete();
        if($res){
            header("Location:/Admin/QNotice/listSort");
        }else{
            $this->ajaxError('删除失败');
        }
    }

    //删除广告
    public function deleteAdv(){
        $advListModel = new QAdvListModel();
        $result = $advListModel->where(array('id'=>I('get.id')))->delete();
        if($result){
            header("Location:/Admin/QNotice/listAdv");
        }else{
            $this->ajaxError('删除失败');
        }
    }

    //编辑广告
    public function editAdv(){

        if (IS_GET) {
            //所属分类列表
            $qadvModel = new QAdvModel();
            //所属商圈
            $where = array();
            if($_SESSION['USER']['bank_id']!=-1){
                $where['bank_id']=  $_SESSION['USER']['bank_id'];
            }
            $sortList = $qadvModel->adSort(array('id,title'),array_merge(array('state'=>1),$where));
            //广告详情信息
            $advListModel = new QAdvListModel();
            $advListInfo =  $advListModel->getAdvListInfo(array('*'),array_merge(array('id'=>I('get.id'))));

            //商家LOGO OSS设置
            if(!empty($advListInfo['pic'])){
                $advListInfo['pic'] = C("urlOSS").$advListInfo['pic'];
                $advListInfo['back_pic'] = C("urlOSS").$advListInfo['back_pic'];
            }

            $assign = array(
                'advListInfo'=>$advListInfo,
                'sortList' => $sortList,
                'page' => I('get.page'),
                'title' => '编辑广告',
            );
            $this->assign($assign);
            $this->display();
        }else{
            $data = I('post.');

            // 判断是否是OSS显示的图片（保存）
            if(strpos($data['pic'],C("urlOSS"))!==false){
                //截取掉前面的https://oss.cloud.hfq.huift.com.cn/
                $start = stripos($data['pic'],C("urlOSS"));
                $length = mb_strlen(C("urlOSS"));
                //$length-1 指的是不截取 https://oss.cloud.hfq.huift.com.cn/ 最后的斜杠
                //mb_strlen($data['brandLogo'])-$length+1 因为下标是从0开始的所以要加上1，不然图片扩展名缺失
                $data['pic'] = substr($data['pic'],$length-1,mb_strlen($data['pic'])-$length+1);
            }
            if(strpos($data['back_pic'],C("urlOSS"))!==false){
                //截取掉前面的https://oss.cloud.hfq.huift.com.cn/
                $start = stripos($data['back_pic'],C("urlOSS"));
                $length = mb_strlen(C("urlOSS"));
                //$length-1 指的是不截取 https://oss.cloud.hfq.huift.com.cn/ 最后的斜杠
                //mb_strlen($data['brandLogo'])-$length+1 因为下标是从0开始的所以要加上1，不然图片扩展名缺失
                $data['back_pic'] = substr($data['back_pic'],$length-1,mb_strlen($data['back_pic'])-$length+1);
            }

            $advListModel = new QAdvListModel();
            $vo = $advListModel->create($data);
            if ( !$vo){
                // 如果创建失败 表示验证没有通过 输出错误提示信息
               $this->ajaxError($advListModel->getError());
            }else{
                if(!empty($data['id'])){
                    $result =    $advListModel->save($vo);
                }else{
                    $result =    $advListModel->add($vo);
                }
                if($result){
                    $this->ajaxSucc('编辑成功');
                }else{
                    $this->ajaxError('编辑失败');
                }
            }
        }
    }

}
