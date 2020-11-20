package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;

public class Decoration implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 环境图片
	 */
	private String imgUrl;
	/**
	 * 环境图片的编码
	 */
	private String decorationCode;
	/**
	 * 产品标题
	 */
	private String title;
	
	/**
	 * 产品价格
	 */
	private String price;
	
	/**
	 * 拍照的图片
	 */
	private int addPhoto;
	
	/**
	 * 产品描述
	 */
	private String des;
	/**
	 * 图片编码
	 */
	private String code;
	/**
	 * 子相册编码
	 */
	private String subAlbumCode;

	/**
	 * 产品图片
	 */
	private String url;
	
	/**
	 * 商铺编码.
	 */
	private String shopCode;
	/**
	 * 子相册名称
	 * @return
	 */
	private String subAlbumName;
	/**
	 * 标示是否有修改
	 * @return
	 */
	private boolean flag;
	
	public int getAddPhoto() {
		return addPhoto;
	}

	public void setAddPhoto(int addPhoto) {
		this.addPhoto = addPhoto;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSubAlbumCode() {
		return subAlbumCode;
	}

	public void setSubAlbumCode(String subAlbumCode) {
		this.subAlbumCode = subAlbumCode;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getDecorationCode() {
		return decorationCode;
	}

	public void setDecorationCode(String decorationCode) {
		this.decorationCode = decorationCode;
	}

	public String getSubAlbumName() {
		return subAlbumName;
	}

	public void setSubAlbumName(String subAlbumName) {
		this.subAlbumName = subAlbumName;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
