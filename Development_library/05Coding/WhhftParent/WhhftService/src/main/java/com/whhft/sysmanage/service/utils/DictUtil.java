package com.whhft.sysmanage.service.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.whhft.sysmanage.common.entity.DictDefine;
import com.whhft.sysmanage.common.entity.DictDetail;
import com.whhft.sysmanage.common.rmi.DictDefineService;
import com.whhft.sysmanage.common.rmi.DictDetailService;
import com.whhft.sysmanage.service.redis.JedisClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Component
public class DictUtil {

	@Autowired
	private DictDefineService dictDefineService;

	@Autowired
	private DictDetailService dictDetailService;

	@Value("${REDIS_ENABLE}")
	private String redisEnable;

	private JedisClient jedisClient;

	//由于如果没有配置redis的话，通过注解注入会报错，所以，在此通过判断是否有Redis然后手动获取jedisClient
	private void getJedisClient(){
		if (redisEnable.equalsIgnoreCase("true")) {
			if(jedisClient==null){
				ApplicationContext context = new FileSystemXmlApplicationContext(
						"src/main/resources/applicationContext-redis-1.xml");
				
				jedisClient = (JedisClient) context.getBean("jedisClientPool");
				//jedisClient = (JedisClient) context.getBean("jedisClientCluster");
			}
		}
	}

	/**
	 * 加载数据字典到redis
	 * 
	 * @return
	 * @throws Exception
	 */
	public void loadDataToRedis() {
		
		getJedisClient();//获取jedisClient
		
		List<DictDefine> dictDefineList = dictDefineService.getAll();

		for (DictDefine dictDefine : dictDefineList) {

			Integer dictDefineId = dictDefine.getDictDefineId();

			List<DictDetail> dictDetailList = dictDetailService
					.getDictDetails(dictDefineId);

			// 加载过程,如果有子集以map形式存入redis，如果没有子集，将DictDefine对象存入redis
			if (dictDetailList.size() > 0) {
				Map<String, DictDetail> map = new HashMap<String, DictDetail>();

				for (DictDetail dictDetail : dictDetailList) {
					map.put(dictDetail.getDictDetailValue(), dictDetail);
				}

				jedisClient.set(dictDefineId.toString(),
						JsonUtil.objectToJson(map));
			} else {
				jedisClient.set(dictDefineId.toString(),
						JsonUtil.objectToJson(dictDefine));
			}
		}
	}

	/**
	 * 根据defineId加载DictDetail集合，如果有redis在redis缓存读取，否则通过数据库或二级缓存
	 * 
	 * @param dictDefineId
	 * @return Map键为DictDetail的name
	 */
	public Map<String, DictDetail> getDetails(Integer dictDefineId) {
		
		Map<String, DictDetail> map = null;

		if (redisEnable.equalsIgnoreCase("true")) {
			
			getJedisClient();//获取jedisClient
			
			
			Gson gson = new Gson();
			Type type = new TypeToken<Map<String, DictDetail>>() {
			}.getType();
			
			//将json转为Map
			map = gson.fromJson(jedisClient.get(dictDefineId.toString()), type);
			//System.err.println("从redis中获取");
		} else {
			List<DictDetail> list = dictDetailService.getDictDetails(dictDefineId);

			map = new HashMap<String, DictDetail>();
			for (DictDetail dictDetail : list) {
				map.put(dictDetail.getDictDetailValue(), dictDetail);
			}

			//System.err.println("从ehcache或数据库中获取");
		}
		return map;
	}

	/**
	 * 根据dictDefineId查询一组DictDetail列表
	 * 
	 * @param dictDefineId
	 * @return
	 */
	public List<DictDetail> getDictDetails(Integer dictDefineId) {

		Map<String, DictDetail> details = getDetails(dictDefineId);

		List<DictDetail> list = new ArrayList<DictDetail>();

		Set<String> keys = details.keySet();

		for (String key : keys) {
			if (details.get(key) != null) {
				list.add(details.get(key));
			}
		}
		return list;
	}

	/**
	 * 根据dictDefineId获取detailName的List集合
	 * 
	 * @param dictDefineId
	 * @return
	 */
	public List<String> getDictDetailNames(Integer dictDefineId) {

		List<DictDetail> list = getDictDetails(dictDefineId);

		List<String> nameList = new ArrayList<String>();
		for (DictDetail dictDetail : list) {
			nameList.add(dictDetail.getDictDetailName());
		}
		return nameList;
	}

	/**
	 * 根据dictDefineId，得到一组以value:name组成的Map集合
	 * 
	 * @param dictDefineId
	 * @return
	 */
	public Map<String, String> getDictValueName(Integer dictDefineId) {
		List<DictDetail> list = getDictDetails(dictDefineId);

		Map<String, String> map = new HashMap<String, String>();

		for (DictDetail dictDetail : list) {
			map.put(dictDetail.getDictDetailValue(),
					dictDetail.getDictDetailName());
		}
		return map;
	}

	/**
	 * 根据dictDefineId，得到一组以value:remark组成的Map集合
	 * 
	 * @param dictDefineId
	 * @return
	 */
	public Map<String, String> getDictValueRemark(Integer dictDefineId) {
		List<DictDetail> list = getDictDetails(dictDefineId);

		Map<String, String> map = new HashMap<String, String>();

		for (DictDetail dictDetail : list) {
			map.put(dictDetail.getDictDetailValue(), dictDetail.getRemark());
		}
		return map;
	}

	/**
	 * 根据dictDefineId以及dictDetailValue，查询对应的信息记录
	 * 
	 * @param dictDefineId
	 * @param dictDetailName
	 * @return
	 */
	public DictDetail getDictDetail(Integer dictDefineId, String dictDetailValue) {

		Map<String, DictDetail> map = getDetails(dictDefineId);

		DictDetail detail = map.get(dictDetailValue);

		return detail;
	}

	/**
	 * 根据dictDefineId以及dictDetailValue，查询对应的remark
	 * 
	 * @param dictDefineId
	 * @param dictDetailValue
	 * @return
	 */
	public String getDictDetailName(Integer dictDefineId, String dictDetailValue) {
		return getDictDetail(dictDefineId, dictDetailValue).getDictDetailName();
	}

	/**
	 * 根据dictDefineId以及dictDetailValue，查询对应的remark
	 * 
	 * @param dictDefineId
	 * @param dictDetailValue
	 * @return
	 */
	public String getDictDetailRemark(Integer dictDefineId,
			String dictDetailValue) {
		return getDictDetail(dictDefineId, dictDetailValue).getRemark();
	}

	/**
	 * 根据dictDefineId以及dictDetailName，查询对应的dictDetailValue
	 * 
	 * @param dictDefineId
	 * @param dictDetailName
	 * @return
	 */
	public String getDictDetailValue(Integer dictDefineId, String dictDetailName) {

		List<DictDetail> list = getDictDetails(dictDefineId);

		for (DictDetail dictDetail : list) {
			if (dictDetailName.equals(dictDetail.getDictDetailName())) {
				return dictDetail.getDictDetailValue();
			}
		}
		return null;
	}

	/**
	 * 根据dictDefineId查询DictDefine对象，如果有redis，在redis，否则在二级缓存或数据库
	 * 
	 * @param dictDefineId
	 * @return
	 */
	public DictDefine getDictDefine(Integer dictDefineId) {

		DictDefine define = null;

		if (redisEnable.equalsIgnoreCase("true")) {
			
			getJedisClient();//获取jedisClient
			
			define = (DictDefine) JsonUtil.jsonToBean(
					jedisClient.get(dictDefineId.toString()), DictDefine.class);
		} else {
			define = dictDefineService.findOne(dictDefineId);
		}

		return define;
	}

	/**
	 * 根据dictDefineId，查询对应的defineValue
	 * 
	 * @param dictDefineId
	 * @return
	 */
	public String getDictDefineValue(Integer dictDefineId) {
		return getDictDefine(dictDefineId).getDictDefineValue();
	}

	/**
	 * 根据dictDefineId，查询对应的defineName
	 * 
	 * @param dictDefineId
	 * @return
	 */
	public String getDictDefineName(Integer dictDefineId) {
		return getDictDefine(dictDefineId).getDictDefineName();
	}

	/**
	 * 根据dictDefineId，查询对应的remark
	 * 
	 * @param dictDefineId
	 * @return
	 */
	public String getDictDefineRemark(Integer dictDefineId) {
		return getDictDefine(dictDefineId).getRemark();
	}

	/**
	 * 根据dictDefineId，更新redis缓存
	 * 
	 * @param dictDefineId
	 */
	public void updateRedisByDefineId(Integer dictDefineId) {
		getJedisClient();
		List<DictDetail> dictDetailList = dictDetailService
				.getDictDetails(dictDefineId);

		if (dictDetailList.size() > 0) {
			Map<String, DictDetail> map = new HashMap<String, DictDetail>();

			for (DictDetail dictDetail : dictDetailList) {
				map.put(dictDetail.getDictDetailName(), dictDetail);
			}

			jedisClient.set(dictDefineId.toString(), JsonUtil.objectToJson(map));
		} else {
			jedisClient.set(dictDefineId.toString(), JsonUtil
					.objectToJson(dictDefineService.findOne(dictDefineId)));
		}

	}

	/**
	 * 从redis缓存中清除
	 * 
	 * @param dictDefineId
	 */
	public void delete(Integer dictDefineId) {
		
		getJedisClient();//获取jedisClient
		jedisClient.expire(dictDefineId.toString(), 0);
	}

	
}
