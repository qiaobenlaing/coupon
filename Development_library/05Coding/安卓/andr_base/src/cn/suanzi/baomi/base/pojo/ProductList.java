package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;

/**
 * 订单详情中的订单列表
 * 
 * @author ad
 * 
 */
public class ProductList implements Serializable {

	/**
	 * 产品ID
	 */
	private String productId;

	/**
	 * 产品名称
	 */
	private String productName;

	/**
	 * 单价
	 */
	private String productUnitPrice;

	/**
	 * 数量
	 */
	private String productNbr;

	/**
	 * 产品单位
	 */
	private String unit;

	/**
	 * 排序号
	 */
	private String sortNbr;

	/**
	 * 该产品金额
	 */
	private String productPrice;

	/**
	 * 已上数量
	 */
	private String availableNbr;

	/**
	 * 未上数量
	 */
	private String unavailableNbr;

	/**
	 * 是否为新添的菜
	 */
	private String isNewLyAdd;

	/**
	 * 
	 */
	private String categoryId;

	/**
	 * 订单产品ID
	 */
	private String orderProductId;

	private boolean Checked = true;

	public ProductList(String productId, String productName, String productUnitPrice, String productNbr, String unit, String sortNbr, String productPrice, String availableNbr, String unavailableNbr, String isNewLyAdd, String categoryId, String orderProductId, boolean checked) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.productUnitPrice = productUnitPrice;
		this.productNbr = productNbr;
		this.unit = unit;
		this.sortNbr = sortNbr;
		this.productPrice = productPrice;
		this.availableNbr = availableNbr;
		this.unavailableNbr = unavailableNbr;
		this.isNewLyAdd = isNewLyAdd;
		this.categoryId = categoryId;
		this.orderProductId = orderProductId;
		this.Checked = checked;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductUnitPrice() {
		return productUnitPrice;
	}

	public void setProductUnitPrice(String productUnitPrice) {
		this.productUnitPrice = productUnitPrice;
	}

	public String getProductNbr() {
		return productNbr;
	}

	public void setProductNbr(String productNbr) {
		this.productNbr = productNbr;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getSortNbr() {
		return sortNbr;
	}

	public void setSortNbr(String sortNbr) {
		this.sortNbr = sortNbr;
	}

	public String getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}

	public String getAvailableNbr() {
		return availableNbr;
	}

	public void setAvailableNbr(String availableNbr) {
		this.availableNbr = availableNbr;
	}

	public String getUnavailableNbr() {
		return unavailableNbr;
	}

	public void setUnavailableNbr(String unavailableNbr) {
		this.unavailableNbr = unavailableNbr;
	}

	public String getIsNewLyAdd() {
		return isNewLyAdd;
	}

	public void setIsNewLyAdd(String isNewLyAdd) {
		this.isNewLyAdd = isNewLyAdd;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getOrderProductId() {
		return orderProductId;
	}

	public void setOrderProductId(String orderProductId) {
		this.orderProductId = orderProductId;
	}

	public boolean getChecked() {
		return Checked;
	}

	public void setChecked(boolean checked) {
		Checked = checked;
	}

	@Override
	public String toString() {
		return "ProductList [productId=" + productId + ", productName=" + productName + ", productUnitPrice=" + productUnitPrice + ", productNbr=" + productNbr + ", unit=" + unit + ", sortNbr=" + sortNbr + ", productPrice=" + productPrice + ", availableNbr=" + availableNbr + ", unavailableNbr=" + unavailableNbr + ", isNewLyAdd=" + isNewLyAdd + ", categoryId=" + categoryId + ", orderProductId=" + orderProductId + ", Checked=" + Checked + "]";
	}
	
	

}
