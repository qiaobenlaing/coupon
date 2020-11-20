package cn.suanzi.baomi.base.pojo;

/**
 * 首页模板
 * @author yanfang.li
 */
public class HomeTemplate {
	
	/**
	 * 图片url 滚屏
	 */
	private String img;
	
	/**
	 * 跳转是h5 还是原生态  
	 * 0:H5 , 1:商家列表 , 2:优惠券列表 
	 */
	private String linkType;
	
	/**
	 * 跳转链接
	 */
	private String link;
	
	/**
	 * 图片URL
	 */
	private String imgUrl;
	
	/**
	 * 品牌区的图片
	 */
	private String showImg;
	
	/**
	 * 模块背景颜色
	 */
	private String bgColor;
	
	/**
	 * 跳转去哪 主要针对H5
	 */
	private String content;
	
	/**
	 * 小模块的子标题的标题
	 */
	private String subTitle;
	
	/**
	 * 图片尺寸
	 */
	private String imgSize;
	
	/**
	 * 标题
	 */
	private String title;
	
	/**
	 * 图片位置 0:左 1:上 2:右 3:下 4：全
	 */
	private String imgPosition;
	
	/**
	 * 子标题的颜色
	 */
	private String subTitleColor;
	
	/**
	 * 标题的颜色
	 */
	private String titleColor;
	
	/**
	 * 图片的尺寸宽
	 */
	private String imgWidth;
	
	/**
	 * 图片的高
	 */
	private String imgHeight;
	
	/**
	 * 店铺详情
	 */
	private  String shopCode;
	
	/**
	 * 商家类别的位置
	 */
	private int subModulePosition;
	
	/**
	 * 图片宽/图片高
	 */
	private double imgRate;
	
	/**
	 * 图片宽/屏幕宽
	 */
	private double screenRate;
	
	public String getShowImg() {
		return showImg;
	}

	public void setShowImg(String showImg) {
		this.showImg = showImg;
	}

	public double getImgRate() {
		return imgRate;
	}

	public void setImgRate(double imgRate) {
		this.imgRate = imgRate;
	}

	public double getScreenRate() {
		return screenRate;
	}

	public void setScreenRate(double screenRate) {
		this.screenRate = screenRate;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public int getSubModulePosition() {
		return subModulePosition;
	}

	public void setSubModulePosition(int subModulePosition) {
		this.subModulePosition = subModulePosition;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getImgSize() {
		return imgSize;
	}

	public void setImgSize(String imgSize) {
		this.imgSize = imgSize;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImgPosition() {
		return imgPosition;
	}

	public void setImgPosition(String imgPosition) {
		this.imgPosition = imgPosition;
	}

	public String getSubTitleColor() {
		return subTitleColor;
	}

	public void setSubTitleColor(String subTitleColor) {
		this.subTitleColor = subTitleColor;
	}

	public String getTitleColor() {
		return titleColor;
	}

	public void setTitleColor(String titleColor) {
		this.titleColor = titleColor;
	}

	public String getImgWidth() {
		return imgWidth;
	}

	public void setImgWidth(String imgWidth) {
		this.imgWidth = imgWidth;
	}

	public String getImgHeight() {
		return imgHeight;
	}

	public void setImgHeight(String imgHeight) {
		this.imgHeight = imgHeight;
	}
	
	
}
