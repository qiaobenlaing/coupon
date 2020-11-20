package com.whhft.sysmanage.service.listener;



import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;

import com.whhft.sysmanage.common.entity.DictDefine;
import com.whhft.sysmanage.common.entity.DictDetail;
import com.whhft.sysmanage.service.cache.EhcacheConfig;
import com.whhft.sysmanage.service.utils.DictUtil;
import com.whhft.sysmanage.service.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;



@Component
public class ShareniuSpringApplicationEventListener implements
		ApplicationListener<ApplicationReadyEvent> {

	@Value("${REDIS_ENABLE}")
	private String redisEnable;

	@Autowired
	private DictUtil dictUtil;
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event)  {
		ConfigurableApplicationContext applicationContext = event
				.getApplicationContext();
		
		//获取SqlSessionFactory,主要是为了通过其获取Configuration修改二级缓存的配置
		SqlSessionFactory factory = (SqlSessionFactory) applicationContext
				.getBean("sqlSessionFactory");
		Configuration config = factory.getConfiguration();
		
		if(redisEnable!=null){
			if(redisEnable.equalsIgnoreCase("false")) {				
				EhcacheConfig.startCache(config);	//开启二级缓存
				System.err.println("开启二级缓存");
			} else if(redisEnable.equalsIgnoreCase("true")){
				
				System.err.println("关闭二级缓存，加载redis");
				EhcacheConfig.closeCache(config);//关闭二级缓存
				
				dictUtil.loadDataToRedis();// 加载系统参数到redis
				
				//测试DictUtil功能		
				/*Map<String,DictDetail> map=dictUtil.getDetails(17);			
				System.err.println(map.get("3").getDictDetailName());
				
				DictDefine define=dictUtil.getDictDefine(19);
				System.err.println(define.getDictDefineKey());
				
				List<String> list1=dictUtil.getDictDetailNames(17);
				System.err.println(list1.get(0));
				
				List<DictDetail> list=dictUtil.getDictDetails(17);
				System.err.println(list.get(0).getDictDetailName());
				
				Map<String,String> map2=dictUtil.getDictValueName(17);
				System.err.println(map2.get("2"));
				
				Map<String,String> map3=dictUtil.getDictValueRemark(17);
				System.err.println(map3.get("2"));
				
				DictDetail detail =dictUtil.getDictDetail(17, "3");
				System.err.println(JsonUtil.objectToJson(detail));
				
				String name=dictUtil.getDictDetailName(17, "2");
				System.err.println(name);
				System.err.println(dictUtil.getDictDetailValue(17, "春"));
				
				System.err.println(dictUtil.getDictDefineName(19));*/
			}else{
				try {
					throw new Exception("请配置REDIS_ENABLE为true或者false");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
