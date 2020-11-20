<?php
namespace Admin\Controller;
use Common\Model\ClientActModuleModel;

/**
 * Created by PhpStorm.
 * User: Huafei Ji
 * Date: 15-8-22
 * Time: 下午1:29
 */
class ClientActModuleController extends AdminBaseController {
    /**
     * 活动模块列表
     */
    public function listClientActModule() {
        $clientAMMdl = new ClientActModuleModel();
        $moduleList = $clientAMMdl->listClientActModule(I('get.'), $this->pager);
        $moduleCount = $clientAMMdl->countClientActModule(I('get.'));
        $this->pager->setItemCount($moduleCount);
        $assign = array(
            'title' => '活动列表',
            'dataList' => $moduleList,
            'get' => I('get.'),
            'pager' => $this->pager
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($moduleList) ? '' : $this->fetch('ClientActModule:listClientActModuleWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    public function editClientActModule() {
        $clientAMMdl = new ClientActModuleModel();
        if(IS_GET){
            $moduleCode = I('get.moduleCode');
            $moduleInfo = $clientAMMdl->getClientActModuleInfo($moduleCode);
            date($moduleInfo);
            $assign = array(
                'title' => '编辑活动',
                'moduleInfo' => $moduleInfo,
            );
            $this->assign($assign);
            $this->display();
        } else {
            $data = I('post.');
            $ret = $clientAMMdl->editModule($data);
            if($ret === true) {
                $this->ajaxSucc();
            } else {
                $this->ajaxError($ret);
            }
        }
    }
}
