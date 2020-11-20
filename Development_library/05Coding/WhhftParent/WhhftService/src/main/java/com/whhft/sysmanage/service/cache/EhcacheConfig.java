package com.whhft.sysmanage.service.cache;
import org.apache.ibatis.session.Configuration;


public class EhcacheConfig {
	
	//开启二级缓存
	public static void startCache(Configuration conf){		
		conf.setCacheEnabled(true);
	}
	
	//关闭二级缓存
	public static void closeCache(Configuration conf){
		conf.setCacheEnabled(false);
	}

}
