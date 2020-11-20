package com.whhft.sysmanage.web.utils;

import java.util.ArrayList;
import java.util.List;

public class IterableUtils {
	public  static  <T> List<T>   toList(Iterable<T> its){
		List<T> list = new ArrayList<T>();
		for(T entity: its){
			list.add(entity);
		}
		return list;
	}
}
