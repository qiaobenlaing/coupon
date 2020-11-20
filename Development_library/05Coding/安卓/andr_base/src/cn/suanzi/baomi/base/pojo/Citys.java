package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 开通城市
 * @author yanfang.li
 *
 */
public class Citys implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 城市的集合
	 */
	private String name;
	
	/**
	 * 定位的名称
	 */
	private String locationName;
	
	/**
	 * 经度
	 */
	private double longitude;
	
	/**
	 * 纬度
	 */
	private double latitude;
	
	/**
	 * 城市的集合
	 */
	private List<Citys> citys = new ArrayList<Citys>();
	
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public List<Citys> getCitys() {
		return citys;
	}
	public void setCitys(List<Citys> citys) {
		this.citys = citys;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	
}
