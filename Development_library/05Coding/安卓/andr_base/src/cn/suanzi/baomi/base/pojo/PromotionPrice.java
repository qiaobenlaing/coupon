package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;

/**
 * 购买活动的价格对应的实体类
 * @author yingchen
 *
 */
public class PromotionPrice implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**价格规格名称 */
	private String des;
	/**价格id*/
	private int id;
	/**价格*/
	private double price;
	/**购买的数量*/
	private int nbr;
	public PromotionPrice() {
		super();
	}
	public PromotionPrice(String des, int id, double price,int nbr) {
		super();
		this.des = des;
		this.id = id;
		this.price = price;
		this.nbr = nbr;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getNbr() {
		return nbr;
	}
	public void setNbr(int nbr) {
		this.nbr = nbr;
	}
	@Override
	public String toString() {
		return "PromotionPrice [des=" + des + ", id=" + id + ", price=" + price + ", nbr=" + nbr + "]";
	}
	
	
	
}
