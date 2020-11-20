package com.whhft.sysmanage.web.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;

public class FastJSONUtil {
	
	public  static String toJSONString(Object obj, Type filterType, String propertiesStr , String nameMapStr){
		String[] properties = propertiesStr.split(",");
		for(String property: properties){
			property = property.trim();
		}
		Map<String, String> nameMap = new HashMap<String, String>() ;
		for(String names: nameMapStr.split(",")){
			String[] nameChange = names.split("=");
			nameMap.put(nameChange[0].trim(), nameChange[1].trim());
		}
		return toJSONString(obj, filterType, properties ,  nameMap);
	}
	/*
	 * 根据filterType的类型对obj生成JSON字符串
	 */
	public static String toJSONString(Object obj, Type filterType, String[] properties , Map<String, String> nameMap){
		SerializeWriter out = new SerializeWriter();
		JSONSerializer serializer = new JSONSerializer(out);
		PropertyFilter pf = null;
		switch(filterType.value){
			case 1: 
				pf = new FastJSONUtil.IncludePropertyFilter(properties);
				break;
			case 2: 
				pf = new FastJSONUtil.ExcludePropertyFilter(properties);
				break;
			default:
				pf = new FastJSONUtil.ExcludePropertyFilter(properties);
		}
	
		serializer.getPropertyFilters().add(pf);
		
		if( nameMap != null){
			serializer.getNameFilters().add(new FastJSONameFilter(nameMap)); 
		}
		serializer.write(obj);
		return out.toString();
	}
	/*
	 * 内部类，构建过滤器，被指定的字段内容会被过滤掉
	 */
	private static class ExcludePropertyFilter implements PropertyFilter {
		public ExcludePropertyFilter(String[] excludeProperties){
			el = Arrays.asList(excludeProperties);
		}
		private List<String> el;
		@Override
		public boolean apply(Object source, String name, Object value) {
			return !(el.contains(name));
		}
	}
	
	private static class IncludePropertyFilter implements PropertyFilter {
		public IncludePropertyFilter(String[] includeProperties){
			el = Arrays.asList(includeProperties);
		}
		private List<String> el;
		@Override
		public boolean apply(Object source, String name, Object value) {
			return el.contains(name);
		}
	}
	
	public static enum Type {
		INCLUDE(1), EXCLUDE(2);
		public int value;

		Type(int value) {
			this.value = value;
		}
	}
	
	public static String toJSONString(Object obj, Map<String, String> nameMap){
		SerializeWriter out = new SerializeWriter();
		JSONSerializer serializer = new JSONSerializer(out);
		
		if( nameMap != null){
			serializer.getNameFilters().add(new FastJSONameFilter(nameMap)); 
		}
		serializer.write(obj);
		return out.toString();
	}
	
}
