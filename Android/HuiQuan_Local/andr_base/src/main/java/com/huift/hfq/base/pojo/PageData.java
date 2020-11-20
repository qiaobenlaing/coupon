package com.huift.hfq.base.pojo;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;
import android.util.Log;

import com.google.gson.Gson;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class PageData implements Serializable{

	private final static String TAG = "PageData";
	
	private int page;//当前页
	private int totalCount;//总记录数
	private int pageSize;//当前页显示数据的条数
	private boolean hasNextPage; //是否还有下一页
	private List<?> list;//数据源
	
	private static final Gson gson = new Gson();
	
	public PageData(){ 
		this.page=1;
		this.pageSize=10;
		this.totalCount=0;
		this.list=null;
		hasNextPage=false;
	}
	
	public PageData(JSONObject result,String listName,Type jsonType){
		// 总记录数
		int totalCount = (Integer.parseInt(result.get("totalCount").toString()));
		// 当前页数
		int page = (Integer.parseInt(result.get("page").toString()));
		// 显示记录数
		int count = (Integer.parseInt(result.get("count").toString()));
		this.page = page;
		if(this.page < 1){
			this.page = 1;
		}
		this.pageSize = count;
		if(this.pageSize < 1){
			this.pageSize = 10;
		}
		this.totalCount = totalCount;
		if(this.totalCount < 1){
			this.totalCount = 0;
		}
		try{
			JSONArray mArray = (JSONArray) result.get(listName);
			if(mArray != null){
				list = gson.fromJson(mArray.toJSONString(), jsonType);
				Log.d(TAG, "list aa === " +list);
			}
		}catch(Exception e){
			Log.d(TAG, "error=="+e.getMessage());
			list = null;
		}
		
		int totalpage = 0;
		if (totalCount % 10 == 0) {
			totalpage = totalCount / 10;
		} else {
			totalpage = totalCount / 10 + 1;
		}
		hasNextPage = false;
		if(totalpage > this.page){
			hasNextPage = true;
		}
	}
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public List<?> getList() {
		return list;
	}
	public void setList(List<?> list) {
		this.list = list;
	}
	public boolean hasNextPage() {
		return hasNextPage;
	}
	
}
