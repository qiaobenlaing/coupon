package cn.suanzi.baomi.base.pojo;

/**
 * 红包列表的实体
 * 
 * @author wensi.yu
 *
 */
public class ActListMoneyItem implements java.io.Serializable {

	
	/**
	 * 红包编码
	 */
	private String bonusCode;
	
	/**
	 * 红包期数
	 */
	private String batchNbr;
	
	/**
	 * 商家logo
	 */
	private String logoUrl;
	
	/**
	 * 获取人数
	 */
	private String getNbr;
	
	/**
	 * 领取率
	 */
	private String getPercent;
	
	/**总人数*/
	private String totalVolume;
	
	/**红包状态*/
	private int status;
	
	public ActListMoneyItem() {
		super();
	}

	
	
	
	public ActListMoneyItem(String bonusCode, String batchNbr, String logoUrl,
			String getNbr, String getPercent, String totalVolume, int status) {
		super();
		this.bonusCode = bonusCode;
		this.batchNbr = batchNbr;
		this.logoUrl = logoUrl;
		this.getNbr = getNbr;
		this.getPercent = getPercent;
		this.totalVolume = totalVolume;
		this.status = status;
	}




	public String getBonusCode() {
		return bonusCode;
	}

	public void setBonusCode(String bonusCode) {
		this.bonusCode = bonusCode;
	}

	public String getBatchNbr() {
		return batchNbr;
	}

	public void setBatchNbr(String batchNbr) {
		this.batchNbr = batchNbr;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getGetNbr() {
		return getNbr;
	}

	public void setGetNbr(String getNbr) {
		this.getNbr = getNbr;
	}

	public String getGetPercent() {
		return getPercent;
	}

	public void setGetPercent(String getPercent) {
		this.getPercent = getPercent;
	}

	public String getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(String totalVolume) {
		this.totalVolume = totalVolume;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
