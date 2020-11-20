/**
 *
 * @Author: Jianping Chen
 * @Date: 2015.5.8
 * @Version: 1.0.0
 * @Copyright Suanzi Co.,Ltd. @ 2015
 * 
 */

package cn.suanzi.baomi.base.pojo;

import java.util.List;

public class ShopDecoration implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键.
	 */
	private String decorationCode;

	/**
	 * 商铺编码.
	 */
	private String shopCode;

	/**
	 * 装饰类型.
	 */
	private Integer type;

	/**
	 * 装饰的主要描述.
	 */
	private String shortDes;

	/**
	 * 装饰的详细描述.
	 */
	private String detailDes;

	/**
	 * 装饰的图片.
	 */
	private String imgUrl;

	/**
	 * 装饰的音频信息.
	 */
	private String audioUrl;
	
	/**
	 * 产品图片
	 */
	private String url;
	
	/**
	 * 子相册编码
	 */
	private String code;
	/**
	 * 子相册名字
	 */
	private String name;
	/**
	 * 相册属性
	 */
	private String belonging;
	/**
	 * 子相册编码
	 */
	private String subAlbumCode;
	
	/**
	 * 产品标题
	 */
	private String title;
	
	/**
	 * 产品价格
	 */
	private String price;
	
	/**
	 * 产品描述
	 */
	private String des;
	
	
	/**
	 * 创建时间
	 */
	private String createTime;
	
	/**
	 * 菜品
	 */
	private List<Decoration> photoList;
	
	/**
	 * 商家环境
	 */
	private List<Decoration> decoration;
	
	/**
	 * 总共的张数
	 */
	private String photoCount;

	public ShopDecoration() {
	}

	public ShopDecoration(String decorationCode) {
		this.decorationCode = decorationCode;
	}

	public ShopDecoration(String decorationCode, String shopCode, Integer type,
			String shortDes, String detailDes, String imgUrl, String audioUrl) {
		this.decorationCode = decorationCode;
		this.shopCode = shopCode;
		this.type = type;
		this.shortDes = shortDes;
		this.detailDes = detailDes;
		this.imgUrl = imgUrl;
		this.audioUrl = audioUrl;
	}

	/**
	 * 获取 主键.
	 */
	public String getDecorationCode() {
		return this.decorationCode;
	}

	/**
	 * 设置 主键.
	 */
	public void setDecorationCode(String decorationCode) {
		this.decorationCode = decorationCode;
	}

	/**
	 * 获取 商铺编码.
	 */
	public String getShopCode() {
		return this.shopCode;
	}

	/**
	 * 设置 商铺编码.
	 */
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	/**
	 * 获取 装饰类型.
	 */
	public Integer getType() {
		return this.type;
	}

	/**
	 * 设置 装饰类型.
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * 获取 装饰的主要描述.
	 */
	public String getShortDes() {
		return this.shortDes;
	}

	/**
	 * 设置 装饰的主要描述.
	 */
	public void setShortDes(String shortDes) {
		this.shortDes = shortDes;
	}

	/**
	 * 获取 装饰的详细描述.
	 */
	public String getDetailDes() {
		return this.detailDes;
	}

	/**
	 * 设置 装饰的详细描述.
	 */
	public void setDetailDes(String detailDes) {
		this.detailDes = detailDes;
	}

	/**
	 * 获取 装饰的图片.
	 */
	public String getImgUrl() {
		return this.imgUrl;
	}

	/**
	 * 设置 装饰的图片.
	 */
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	/**
	 * 获取 装饰的音频信息.
	 */
	public String getAudioUrl() {
		return this.audioUrl;
	}

	/**
	 * 设置 装饰的音频信息.
	 */
	public void setAudioUrl(String audioUrl) {
		this.audioUrl = audioUrl;
	}

	
	public List<Decoration> getPhotoList() {
		return photoList;
	}

	public void setPhotoList(List<Decoration> photoList) {
		this.photoList = photoList;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBelonging() {
		return belonging;
	}

	public void setBelonging(String belonging) {
		this.belonging = belonging;
	}

	public String getSubAlbumCode() {
		return subAlbumCode;
	}

	public void setSubAlbumCode(String subAlbumCode) {
		this.subAlbumCode = subAlbumCode;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPhotoCount() {
		return photoCount;
	}

	public void setPhotoCount(String photoCount) {
		this.photoCount = photoCount;
	}

	public List<Decoration> getDecoration() {
		return decoration;
	}

	public void setDecoration(List<Decoration> decoration) {
		this.decoration = decoration;
	}
}
