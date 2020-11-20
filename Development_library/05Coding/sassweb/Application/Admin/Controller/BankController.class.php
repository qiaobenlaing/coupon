<?php
/**
 * Created by PhpStorm.
 * User: 98041
 * Date: 2018/9/20
 * Time: 10:41
 */

namespace Admin\Controller;


use Common\Model\BankModel;

class BankController extends AdminBaseController
{


    /**
     * 获得品牌列表
     */
    public function listBank()
    {
        $bankMdl = new BankModel();
        $filterData = I('get.');
        $bankList = $bankMdl->listBank($filterData, $this->pager);
        $bankCount = $bankMdl->countBank($filterData);
        $this->pager->setItemCount($bankCount);

        $assign = array(
            'title' => '品牌列表',
            'get' => I('get.'),
            'dataList' => $bankList,
            'pager' => $this->pager
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($bankList) ? '' : $this->fetch('Bank:listBankWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     *
     */
    public function editBank()
    {
        if (IS_GET) {
            $bankId = I('get.id');
            if (empty($bankId)) {
                $bankInfo = array();
            } else {
                $bankMdl = new BankModel();
                $bankInfo = $bankMdl->getBankInfo(array('id' => $bankId), array('*'));
            }
            $assign = array(
                'bankInfo' => $bankInfo,
                'page' => I('get.page'),
                'title' => '编辑商圈',
            );
            $this->assign($assign);
            $this->display();
        } else {
            $data = I('post.');
            $bankMdl = new BankModel();

            //判断是否为惠圈管理人员
            if ($_SESSION['USER']['bank_id'] != -1) {
                $data['bank_id'] = $_SESSION['USER']['bank_id'];
            }

            $ret = $bankMdl->editBank($data);
            if ($ret['code'] === C('SUCCESS')) {
                $this->ajaxSucc('保存成功', array('id' => $ret['id']));
            } else {
                $this->ajaxError($ret);
            }
        }

    }
}