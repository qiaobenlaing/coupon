package com.huift.hfq.base.pojo;

import java.io.Serializable;

public class ShopMaster implements Serializable{
	private int  expModel;
	private String  imgUrl;
	private String   txtMemo;
	public ShopMaster() {
		super();
	}
	public ShopMaster(int expModel, String imgUrl, String txtMemo) {
		super();
		this.expModel = expModel;
		this.imgUrl = imgUrl;
		this.txtMemo = txtMemo;
	}
	public int getExpModel() {
		return expModel;
	}
	public void setExpModel(int expModel) {
		this.expModel = expModel;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getTxtMemo() {
		return txtMemo;
	}
	public void setTxtMemo(String txtMemo) {
		this.txtMemo = txtMemo;
	}
	
	
}
