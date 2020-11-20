package com.huift.hfq.base.utils;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.Citys;

/**
 * 百度定位
 * @author liyanfang
 */
public class LocationUtil {
	
	public static final String TAG = LocationUtil.class.getSimpleName();
	
	/** 保存城市的key*/
	public static final String CITY_KEY = "cityKey";
	/** 声明LocationClient类*/
	private static LocationClient mLocationClient;
	/**  获取当前的定位模式*/
	private static LocationMode mTempMode = LocationMode.Hight_Accuracy;
	
	/**
	 * 获取经纬度
	 */
	public static void getLocationClient() {
		initLocationClient(); // 获得经纬度
		mLocationClient.start();
		mLocationClient.requestLocation();
	}
	
	/**
	 * 获取经纬度
	 * @param v
	 */
	private static void initLocationClient() {
		mLocationClient = new LocationClient(AppUtils.getContext());
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(mTempMode);// 设置定位模式
		int span = 500; // 定位请求的间隔时间为5000ms
		option.setScanSpan(span);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true); 
		option.setNeedDeviceDirect(true);
		option.setOpenGps(true); // 是否打开GPS
		// 百度定位的坐标系 国测局加密坐标其中 还有三种：bd09ll（百度经纬度坐标）、bd09mc（百度摩卡托坐标）、gcj02（国测局加密坐标）、wgs84（gps设备获取的坐标）
		option.setCoorType("bd09ll"); // 设置返回值的坐标类型。 百度摩卡托坐标
		// option.setProdName("LocationDemo"); //
		// 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
		// option.setScanSpan(UPDATE_TIME);// 设置定时定位的时间间隔。单位毫秒
		mLocationClient.setLocOption(option);
		mLocationClient.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation bdLocation) {
				if (bdLocation == null) { return; }
				// String jiedao = bdLocation.getAddrStr(); // 准备地址
				// String jiedao1 = bdLocation.getDistrict(); // 区
				// getProvince() // 省份
				// 保存经纬度
				Citys city = new Citys();
				city.setLocationName(bdLocation.getCity());
				city.setLatitude(bdLocation.getLatitude());
				city.setLongitude(bdLocation.getLongitude());
				DB.saveObj(CITY_KEY, city);
				Log.d(TAG, "☆☆☆  initLocationClients  BDLocation 2:lat:" + city.getLatitude() + ",lon:" + city.getLongitude() + ",name:" + city.getLocationName());
			}
		});
	}
	
}
