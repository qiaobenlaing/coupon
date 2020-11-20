package com.whhft.sysmanage.common.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class EasyPage implements Serializable {
	//当前显示的是第几页
	private int page;
	//每页的行数
	private int rows;
	//GRID的页面排序字段，和页面的field字段关联
	private String sort;
	//GRID组件提供的功能，用来记录升降序，采用小写关键字 desc/asc
	private String order;
	
	private String defaultOrder;
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	
	public String getOrderByClause() {
		List<String> orderList = new ArrayList<String>();
		if(StringUtils.isNotEmpty(sort)){
			String GridOrder  = getGridOrder(sort);
			if(StringUtils.isNotEmpty(GridOrder)){
				orderList.add(GridOrder + " " + order);
			}
		}
		if(StringUtils.isNotEmpty(defaultOrder)){
			orderList.add(defaultOrder);
		}
		if(orderList.size() > 0){
			return StringUtils.join(orderList, ",");
		}else{
			return null;
		}
	}
	
	public void setDefaultOrder(String defaultOrder) {
		this.defaultOrder = defaultOrder;
	}

	public String getGridOrder(String sort){
		return sort;
	}
}
