package com.whhft.sysmanage.web.utils;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;

/**
 * 封装返回给GRID的数据
 * @author user
 *
 */
public class EasyGrid<T> {
	
	public EasyGrid(PageInfo<T> pageInfo){
		this.rows = pageInfo.getList();
		this.total = pageInfo.getTotal();
		this.page = pageInfo.getPageNum();
		this.pageSize = pageInfo.getPageSize();
	}
	
	/**
	 * 用于单页显示所有结果
	 * @param list
	 */
	public EasyGrid(List<T> list){
		this.rows = list;
		this.total = list.size();
		this.page = 1;
		this.pageSize = list.size();
	}
	
	private static String DEFAULT_PAGE_FIELD = "rows,total,page,pageSize";
	
	//返回的对象列表
	private List<T> rows ;
	//总记录数
	private long total = 0;
	//当前页数
	private int page = 1;
	//每页记录数
	private int pageSize = 10;
	
	public List<?> getRows() {
		return rows;
	}
	public long getTotal() {
		return total;
	}
	public int getPage() {
		return page;
	}
	public int getPageSize() {
		return pageSize;
	}
	
	public String toJSON(){
		return JSON.toJSONString(this);
	}
	
	public String toJSON(String fields, boolean include){
		String[] properties;
		String json ;
		properties = fields.split(",");
		if(include){
			fields = fields + "," + DEFAULT_PAGE_FIELD;
			properties = fields.split(",");
			json = FastJSONUtil.toJSONString(this, FastJSONUtil.Type.INCLUDE, properties, null);
		}else{
			properties = fields.split(",");
			json = FastJSONUtil.toJSONString(this, FastJSONUtil.Type.EXCLUDE, properties, null);
		}
		return json;
	}
}
