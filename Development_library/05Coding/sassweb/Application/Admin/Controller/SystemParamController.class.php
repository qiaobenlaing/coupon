<?php
namespace Admin\Controller;
use Common\Model\BaseModel;
use Common\Model\SystemParamModel;

/**
 * Created by PhpStorm.
 * User: Huafei.ji
 * Date: 15-9-25
 * Time: 下午4:06
 */
class SystemParamController extends AdminBaseController {

    /**
     * 删除系统参数
     */
    public function delSystemParam() {
        $id = I('post.id');
        $systemParamMdl = new SystemParamModel();
        $ret = $systemParamMdl->delSystemParam($id);
        if($ret == true) {
            $this->ajaxSucc('');
        } else {
            $this->ajaxError('删除失败');
        }
    }

    /**
     * 编辑系统参数
     */
    public function editSystemParam() {
        $systemParamMdl = new SystemParamModel();
        if(IS_GET) {
            $id = I('get.id');
            $systemParamInfo = $systemParamMdl->getSystemParamInfo($id);
            $this->assign('title', '编辑系统参数');
            $this->assign('systemParamInfo', $systemParamInfo);
            $this->display();
        } else {
            $data = I('post.');
//            $this->ajaxError('',$data);
            if($data['paramType'] == 2){
                $data['value'] = $data['imgUrl'];
            }
            unset($data['imgUrl']);
            $ret = $systemParamMdl->editSystemParam($data);
            if ($ret === true) {
                $this->ajaxSucc('操作成功');
            } else {
                $this->ajaxError($ret);
            }
        }
    }

    /**
     * 获得顾客端的系统参数
     */
    public function listClientSystemParam() {
        $crashLogMdl = new SystemParamModel();
        $systemParamList = $crashLogMdl->listClientSystemParam(I('get.'), $this->pager);
        $systemParamCount = $crashLogMdl->countClientSystemParam(I('get.'));
        $this->pager->setItemCount($systemParamCount);
        $assign = array(
            'title' => '顾客端系统参数',
            'dataList' => $systemParamList,
            'get' => I('get.'),
            'pager' => $this->pager
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($systemParamList) ? '' : $this->fetch('SystemParam:listClientSystemParamWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 获得商家端的系统参数
     */
    public function listShopSystemParam() {
        $crashLogMdl = new SystemParamModel();
        $systemParamList = $crashLogMdl->listShopSystemParam(I('get.'), $this->pager);
        $systemParamCount = $crashLogMdl->countShopSystemParam(I('get.'));
        $this->pager->setItemCount($systemParamCount);
        $assign = array(
            'title' => '商家端系统参数',
            'dataList' => $systemParamList,
            'get' => I('get.'),
            'pager' => $this->pager
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($systemParamList) ? '' : $this->fetch('SystemParam:listShopSystemParamWidget');
            $this->ajaxSucc('', null, $html);
        }
    }
}