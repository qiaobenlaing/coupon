package cn.suanzi.baomi.base.pojo;

/**
 * 商店顶部排序
 * @author yingchen
 *
 */
public class ShopSort {
	private ShopSortType filter;//筛选
	private ShopSortType intelligentSorting; //智能排序
	private ShopSortType circle; //商圈
	private ShopSortType type;//行业
	public ShopSort() {
		super();
	}
	public ShopSort(ShopSortType filter, ShopSortType intelligentSorting, ShopSortType circle, ShopSortType type) {
		super();
		this.filter = filter;
		this.intelligentSorting = intelligentSorting;
		this.circle = circle;
		this.type = type;
	}
	
	public ShopSortType getFilter() {
		return filter;
	}
	public void setFilter(ShopSortType filter) {
		this.filter = filter;
	}
	public ShopSortType getIntelligentSorting() {
		return intelligentSorting;
	}
	public void setIntelligentSorting(ShopSortType intelligentSorting) {
		this.intelligentSorting = intelligentSorting;
	}
	public ShopSortType getCircle() {
		return circle;
	}
	public void setCircle(ShopSortType circle) {
		this.circle = circle;
	}
	public ShopSortType getType() {
		return type;
	}
	public void setType(ShopSortType type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "ShopSort [filter=" + filter + ", intelligentSorting=" + intelligentSorting + ", circle=" + circle + ", type=" + type + "]";
	}
	
	
}
