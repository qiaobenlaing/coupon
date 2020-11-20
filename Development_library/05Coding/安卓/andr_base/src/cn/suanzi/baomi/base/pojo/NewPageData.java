package cn.suanzi.baomi.base.pojo;

import java.lang.reflect.Type;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.google.gson.Gson;

/**
 * 分页数据
 * @author yingchen
 *
 */
public class NewPageData {
	private int page;
	private int totalCount;
	private int nextPage;
	private List<?> list;
	
	public NewPageData(JSONObject result,Type jsonType) {
		this.page = (Integer.parseInt(result.get("page").toString()));
		this.totalCount = (Integer.parseInt(result.get("totalCount").toString()));
		this.nextPage = (Integer.parseInt(result.get("nextPage").toString()));
		try{
			JSONArray mArray = (JSONArray) result.get("list");
			if(mArray != null){
				this.list = new Gson().fromJson(mArray.toJSONString(), jsonType);
			}
		}catch(Exception e){
			this.list = null;
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
	public int getNextPage() {
		return nextPage;
	}
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
	public List<?> getList() {
		return list;
	}
	public void setList(List<?> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "NewPageData [page=" + page + ", totalCount=" + totalCount + ", nextPage=" + nextPage + ", list=" + list + "]";
	}
	
	
}
