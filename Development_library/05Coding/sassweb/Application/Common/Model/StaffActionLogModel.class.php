<?php
namespace Common\Model;
use Think\Model;
/**
 * Created by PhpStorm.
 * Date: 15-5-26
 * Time: 上午10:08
 * StaffActionLog表
 * @author jihuafei
 */
class StaffActionLogModel extends BaseModel {
    protected $tableName = 'StaffActionLog';
    
    /**
     * 管理端操作日志列表
     * @param array $filterData
     * @param object $page 页码
     * @return null|array 如果为空，返回null；如果不为空，返回二维数组array(array());
     */
    public function listActionLog($filterData, $page) {
        $where = $this->filterWhere($filterData,
            array('realName' => 'like', 'actionTime' => 'like'),
            $page);

        //判断是否为惠圈人员
        if($_SESSION['USER']['bank_id']!=-1){
            $where['StaffActionLog.bank_id'] = $_SESSION['USER']['bank_id'];
        }


        return $this
                ->field(array('realName', 'actionTime', 'actionDes','StaffActionLog.bank_id'))
                ->join('BMStaff ON BMStaff.staffCode = StaffActionLog.StaffId', 'INNER')
                ->where($where)
                ->order('actionTime DESC')
                ->pager($page)
                ->select();
    }
    
    /**
     * 管理端操作日志总数
     * @param array $filterData
     * @return null|int 如果为空，返回null；如果不为空，返回number;
     */
    public function countActionLog($filterData) {
        $where = $this->filterWhere($filterData,
            array('realName' => 'like', 'actionTime' => 'like'));

        //判断是否为惠圈人员
        if($_SESSION['USER']['bank_id']!=-1){
            $where['StaffActionLog.bank_id'] = $_SESSION['USER']['bank_id'];
        }

        return $this
                ->join('BMStaff ON BMStaff.staffCode = StaffActionLog.StaffId', 'INNER')
                ->where($where)
                ->count('logCode');
    }

    /**
     * 管理端增加操作日志
     * @param array $actionData 操作信息:{'staffCode','actionDes'}
     * @return true|string 成功放回true;失败返回错误信息
     */
    public function addActionLog($actionData) {
        //判断是否为惠圈人员
        if($_SESSION['USER']['bank_id']!=-1){
            $actionData['bank_id'] = $_SESSION['USER']['bank_id'];
        }

        $ip = get_client_ip();
        $data = array(
            'logCode'       =>  $this->create_uuid(),
            'staffId'       => $actionData['staffCode'],
            'actionDes'     => $actionData['actionDes'],
            'actionTime'    => date('Y-m-d H:i:s'),
            'bank_id'       => $actionData['bank_id'],
            'ipAddr'        => $ip
        );
        $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }
    
    /**
     * 管理端删除操作日志
     * @param string $logCode
     * @return true|string 成功放回true;失败返回错误信息
     */
    public function delActionLog($logCode) {
        return  $this->where(array('logCode' => $logCode))->delete() ? true : '删除失败!';
    }
}
