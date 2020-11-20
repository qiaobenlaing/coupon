<?php
namespace Admin\Controller;
use Common\Model\PcAppLogModel;

/**
 * Created by PhpStorm.
 * User: jihuafei
 * Date: 15-10-19
 * Time: 下午4:01
 */
class PcAppLogController extends AdminBaseController {

    public function delPcAppLog() {
        if(IS_AJAX) {
            $logCode = I('post.logCode'); // 优惠券编码
            $pcAppLogMdl = new PcAppLogModel();
            $ret = $pcAppLogMdl->delLog(array('logCode' => $logCode));
            if($ret === true) {
                $this->ajaxSucc();
            } else {
                $this->ajaxError('删除失败');
            }
        } else {
            echo 'WELCOME TO HUIQUAN';
        }
    }

    /**
     * 获取更新列表
     */
    public function listPcAppLog() {
        $pcAppLogMdl = new PcAppLogModel();
        $logList = $pcAppLogMdl->listUpdateLog(I('get.'), $this->pager);
        $logCount = $pcAppLogMdl->countUpdatelog(I('get.'));
        $this->pager->setItemCount($logCount);
        $assign = array(
            'title' => '商家端APP更新记录',
            'dataList' => $logList,
            'get' => I('get.'),
            'pager' => $this->pager
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($logList) ? '' : $this->fetch('PcAppLog:listPcAppLogWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 编辑更新记录
     */
    public function editPcAppLog() {
        $pcAppLogMdl = new PcAppLogModel();
        if(IS_GET) {
            $logCode = I('get.logCode');
            $logInfo = $pcAppLogMdl->getUpdateLogInfo($logCode);
            $assign = array(
                'title' => '编辑PC端APP更新记录',
                'logInfo' => $logInfo,
                'page' => I('get.page'),
            );
            $this->assign($assign);
            $this->display();
        }else {
            $data = I('post.');
            $userInfo = session('USER');
            $staffCode = $userInfo['staffCode'];
            $data['creatorCode'] = $staffCode;
            $data['createTime'] = date('Y-m-d H:i:s');
            $ret = $pcAppLogMdl->editUpdateLog($data);
            if ($ret === true) {
                $this->ajaxSucc('添加成功');
            } else {
                $this->ajaxError($ret);
            }
        }
    }

    /**
     * 用于上传PC端的应用
     */
    public function uploadPcApp() {
        $config = array(
            'mimes'         =>  array(), //允许上传的文件MiMe类型
            'maxSize'       =>  41943040, //上传的文件大小限制(以字节为单位)(0-不做限制)，40M
            'exts'          =>  array('exe'), //允许上传的文件后缀
            'autoSub'       =>  true, //自动子目录保存文件
            'subName'       =>  array('date', 'Ymd'), //子目录创建方式，[0]-函数名，[1]-参数，多个参数使用数组
            'rootPath'      =>  '',
            'savePath'      =>  './Public/App/Pc/', //保存路径
            'saveName'      =>  array('uniqid', ''), //上传文件命名规则，[0]-函数名，[1]-参数，多个参数使用数组
        );
        $upload = new \Think\Upload($config);// 实例化上传类
        $info = $upload->upload();
        if(!$info) {
            // 上传错误提示错误信息
            $error = $upload->getError();
            if($error == '80020') {
                $error = '上传文件MIME类型不允许！';
            }
            $this->ajaxError($error, $_FILES);
        } else {  // 上传成功，获取上传文件信息
            $url = substr($info['userfile']['savepath'], 1).$info['userfile']['savename'];
            $this->ajaxSucc($url);
        }
    }
}