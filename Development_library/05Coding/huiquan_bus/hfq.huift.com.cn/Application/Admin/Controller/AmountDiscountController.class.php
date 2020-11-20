<?php
namespace Admin\Controller;
use Common\Model\ActivityModel;
use Common\Model\AmountDiscountModel;
use Common\Model\CardModel;
use Common\Model\Pager;
use Common\Model\ShopModel;
use Common\Model\UtilsModel;
use Org\FirePHPCore\FP;
/**
 * Activity Controller
 *
 * User: jiangyufeng
 * Date: 2015-05-19
 */
class AmountDiscountController extends AdminBaseController {

    /**
     * 商户活动维护
     */
    public function discountList() {

        $where = array();
        if($shopName = I('get.shopName'))
            $where['c.shopName'] = array('like',"%$shopName%");
        if($activityName = I('get.activityName'))
            $where['b.activityName'] = array('like',"%$activityName%");
        $status = I('get.status');
        if($status != null)
            $where['a.status'] = array('eq',$status);
        $this->page = I('get.page') ? I('get.page') : 1;
        //设置总的记录数
        $activityCount = M('amountdiscount')
            ->alias('a')
            ->join('LEFT JOIN __ACTIVITY__ b ON a.activityCode=b.activityCode
                    LEFT JOIN __SHOP__ c ON b.shopCode=c.shopCode')
            ->where($where)
            ->count();
        $this->pager->setItemCount($activityCount);
//        $amountDiscountMdl = new AmountDiscountModel();
//        dump($this->pager);
//        die;
        $amountData = M('amountdiscount')
            ->alias('a')
            ->field('a.*,b.activityName,b.activityBelonging,c.shopName')
            ->join('LEFT JOIN __ACTIVITY__ b ON a.activityCode=b.activityCode
                    LEFT JOIN __SHOP__ c ON b.shopCode=c.shopCode')
            ->where($where)
            ->page($this->page,$this->pageSize)
            ->select();

        $this->assign(array(
            'amountData' => $amountData,
            'pager' => $this->pager,
            'get' => I('get.')
        ));
        if (! IS_AJAX) {
            $this->display();
        } else {
            $html = empty($amountData) ? '' : $this->fetch('AmountDiscount:listDiscountAjaxWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

}