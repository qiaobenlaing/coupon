package cn.suanzi.baomi.base.pojo;

public class NewShopTime implements java.io.Serializable {
	/**店铺关门时间*/
	private String close;
	
	/**店铺开门时间*/
	private String open;

	public NewShopTime() {
		super();
	}

	public NewShopTime(String close, String open) {
		super();
		this.close = close;
		this.open = open;
	}

	public String getClose() {
		return close;
	}

	public void setClose(String close) {
		this.close = close;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}
	
	
}
