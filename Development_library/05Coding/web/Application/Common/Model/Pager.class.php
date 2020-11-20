<?php
namespace Common\Model;

/**
 * 翻页参数。
 *
 * @author Weiping
 * @version 1.0.0
 */
class Pager {
	/** 默认页面大小 */
	const DEFUALT_PAGE_SIZE = 20;
	
	/** number 页数，从1开始。 */
	public $page = 1;
	/** number 页面大小，默认为10. */
	public $pageSize = Pager::DEFUALT_PAGE_SIZE;
	/** number 总页数 */
	public $pageCount = 0;
	/** number 总记录数 */
	public $itemCount = 0;
	
	/**
	 * 构造函数
	 * @param int $pg 页数，从1开始，默认为1。
	 * @param int $pgSize 页面大小，必须>0，默认为10.
	 */
	public function __construct($pg = 1, $pgSize = Pager::DEFUALT_PAGE_SIZE) {
		$this->page = $pg >= 1 ? $pg : 0;
		$this->pageSize = $pgSize >= 1 ? $pgSize : Pager::DEFUALT_PAGE_SIZE;
	}
	
	public function getPage() {
		return $this->page;
	}

	public function setPage($pg) {
		$this->page = $pg;
	}
	
	public function getPageSize() {
		return $this->pageSize;
	}
	
	/**
	 * 设置页面大小，同时会更新总页数。
	 * @param int $pgSize
	 */
	public function setPageSize($pgSize) {
		$this->pageSize = $pgSize;
		$this->pageCount = ceil($this->pageCount / $this->pageSize);
	}

	public function getPageCount() {
		return $this->pageCount;
	}

	public function getItemCount() {
		return $this->itemCount;
	}
	
	/**
	 * 设置记录总数，同时会更新总页数。
	 * @param int $itemCount 记录总数
	 */
	public function setItemCount($itemCount) {
		$this->itemCount = $itemCount;
		$this->pageCount = ceil($itemCount / $this->pageSize);
		
	}
	
	
}
