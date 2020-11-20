<?php
namespace Common\Model;
use Common\Model\BaseModel;

/**
 * 下拉菜单
 * @author Weiping 1.0.0 2014-11-21 新建
 */
class DropdownModel extends BaseModel {
	
	/**
	 * 获取一般dropbox选项
	 * @param string $id
	 * @return array 返回区域数组array(name => val). name为显示的字符串，val为真正保存的值。
	 */
	public function getNormal($id) {
		return $this
				->field('name,val')
				->where(array('parentId' => $id))
				->order('sortId')
				->select();
// 				->getField('name, val');
	}
	
	/**
	 * 获取行政区域选项（获取市级，县级区域）
	 * @param string $name 父级区域的名字。省份的父级为'', 城市的父级为省名，行政区的父级为城市名。
	 * @return array 返回区域数组array(name=>name).
	 */
	public function getDistrict($name) {
		return $this->table('District as d1')
				->field('d1.name, d1.name AS val')
				->join('District AS d2 on d1.parentId=d2.id')
				->where(array('d2.name' => $name))
				->order('d1.sortId')
				->select();
// 				->getField('d1.name, d1.name AS val');
	}

    /**
     * 获取省份选项
     * @return array 返回区域数组array(name=>name).
     */
    public function getProvince() {
        return $this->table('District')
            ->field('name, name AS val')
            ->where(array('parentId' => 0))
            ->order('sortId')
            ->select();
    }

}