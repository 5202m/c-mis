package com.gwghk.mis.util;



import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.util.ClassUtils;

/**
 * Bean通用操作类
 * @author Alan.wu
 * @date 2015/3/10
 */
public class BeanUtils{
	
	private static final Logger logger = Logger.getLogger(BeanUtils.class);
	
	public static <T> T copyExceptNull(T target, Object orig) {
		return copy(target, orig, false);
	}
	
	public static <T> T copy(T target, Object orig, boolean includedNull, String... ignoreProperties) {
		try {
			if(includedNull && ignoreProperties == null){
				PropertyUtils.copyProperties(target, orig);
			}else{
				List<String> ignoreFields = new ArrayList<String>();
				if(ignoreProperties != null){
					for(String p : ignoreProperties){
						ignoreFields.add(p);
					}	
				}
				if(!includedNull){
					@SuppressWarnings("unchecked")
					Map<String, Object> parameterMap = new BeanMap(orig);
					for (Entry<String, Object> entry : parameterMap.entrySet()) {
						if (entry.getValue() == null) {
							String fieldName = entry.getKey();
							if(!ignoreFields.contains(fieldName)){
								ignoreFields.add(fieldName);	
							}
						}
					}					
				}
				/*System.out.println("ignore" + nullFields);*/
				org.springframework.beans.BeanUtils.copyProperties(orig, target, ignoreFields.toArray(new String[ignoreFields.size()]));				
			}
			return target;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	
	@SuppressWarnings("rawtypes")
	private static Map<Class, List<Field>> fieldsMap = new HashMap<Class, List<Field>>();
	public static List<Field> getAllFields(Class<?> clazz) {
		if(!ClassUtils.isCglibProxyClass(clazz)){
			List<Field> result = fieldsMap.get(clazz);
			if(result == null){
				result = _getAllFields(clazz);
				fieldsMap.put(clazz, result);
			}
			return result;
		}else{
			return _getAllFields(clazz);
		}	
	}

	private static List<Field> _getAllFields(Class<?> clazz) {
		List<Field> fieldList = new ArrayList<Field>();
		for (Field field : clazz.getDeclaredFields()) {
			fieldList.add(field);
		}
		Class<?> c = clazz.getSuperclass();
		if (c != null) {
			fieldList.addAll(getAllFields(c));
		}
		return fieldList;
	}

	
	/**
	 * Get field in class and its super class
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static Field getField(Class<?> clazz, String fieldName) {
		for (Field field : clazz.getDeclaredFields()) {
			if(field.getName().equals(fieldName)){
				return field;
			}
		}
		Class<?> c = clazz.getSuperclass();
		if (c != null) {
			return getField(c, fieldName);
		}
		return null;
	}
	
	/**
	 * 字符数组合并
	 * 注：接受多个数组参数的输入，合并成一个数组并返回。
	 * @param arr  输入的数组参数，一个或若干个
	 * @return
	 */
	public static String[] getMergeArray(String[] ...arr) {
		if(arr==null) return null;
		int length=0;
		for(Object[] obj:arr){
			length+=obj.length;
		}
		String[] result = new String[length];
		length=0;
		for(int i=0;i<arr.length;i++){
			System.arraycopy(arr[i], 0, result,length, arr[i].length);
			length+=arr[i].length;
		}
		return result;
    }
	
	public static <T> T clone(Object obj, Class<T> targetClazz){
		if(obj==null){
			return null;
		}
		try {
			return copy(targetClazz.newInstance(), obj, false);
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}
	
	/**
	 * 
	 * @param srcList
	 * @param targetClazz
	 * @return
	 */
	public static <M> List<M> cloneList(List<?> srcList, Class<M> targetClazz) {
		return ListUtil.clone(srcList, targetClazz);
	}
	
	/**
	 * 
	 * @param objs
	 * @return
	 */
	@SafeVarargs
	public static <T> List<T> createList(T... objs){
		return ListUtil.createList(objs);
	}
	
}
