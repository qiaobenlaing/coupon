package cn.suanzi.baomi.base.pojo;

import java.util.List;

/**
 * 商店顶部排序   分别为商圈，行业，智能排序，筛选
 * @author yingchen
 *
 */
public class ShopSortType {
	private String zh;
	private List<SubShopSortType> list;
	public ShopSortType() {
		super();
	}
	public ShopSortType(String zh, List<SubShopSortType> list) {
		super();
		this.zh = zh;
		this.list = list;
	}
	public String getZh() {
		return zh;
	}
	public void setZh(String zh) {
		this.zh = zh;
	}
	public List<SubShopSortType> getList() {
		return list;
	}
	public void setList(List<SubShopSortType> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "ShopSortType [zh=" + zh + ", list=" + list + "]";
	}
	
}
