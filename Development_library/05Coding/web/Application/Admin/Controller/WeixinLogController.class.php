<?php
/**
 * Created by PhpStorm.
 * User: 98041
 * Date: 2018/7/24
 * Time: 22:42
 */

namespace Admin\Controller;


use Common\Model\WeixinLogModel;
use Common\Model\Pager;

class WeixinLogController extends  AdminBaseController
{

    public  function  listWeixinLog(){
        $weixinLogModel = new WeixinLogModel();
        $weixinLoglist  = $weixinLogModel->getWeixinLogList(I("get."),$this->pager);

        foreach ($weixinLoglist as $key => $value) {
            $weixinLoglist[$key]['readtime']  = $this->toDate($weixinLoglist[$key]['readtime'],"Y-m-d H:i:s");
        }
        $weixinLogCount = $weixinLogModel->countWeixinLog(I("get."));
        $this->pager->setItemCount($weixinLogCount);
        $assign = array(
            'title' => '微信卡券日志列表',
            'dataList' => $weixinLoglist,
            'get' => I('get.'),
            'pager' => $this->pager
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($weixinLoglist) ? '' : $this->fetch('WeixinLog:WeixinLogWidget');
            $this->ajaxSucc('', null, $html);
        }
    }


//时间戳格式化
    function toDate($time,$format='Y年m月d日 H:i:s'){
        if( empty($time)) {
            return '';
        }
        $format = str_replace('#',':',$format);
        return date($format,$time);
    }


}