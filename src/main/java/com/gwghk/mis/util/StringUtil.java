package com.gwghk.mis.util;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

public class StringUtil {

	/**
	 * 替换字符串中对应节点中的<![CDATA[]]>
	 * @param  str       字符串
	 * @param  node      节点 
	 * @return  返回替换后的字符串
	 */
	public static String replaceCdataToBlack(String str,String node){
		String s1 = "<"+node+">";
		String s2 = "</"+node+">";
		int s1i = str.indexOf(s1);
		int s2i = str.indexOf(s2);
		String newStr =  str.substring(s1i+s1.length(), s2i);
	    return str.replace(newStr, newStr.substring(newStr.lastIndexOf("[")+1, newStr.indexOf("]")));
	}
	
	/**
	 * 输入字符是否包含关键字
	 * 备注：以英式逗号,分割
	 * @param source
	 * @param keyword
	 * @return
	 */
	public static boolean containKeyword(String source,String keyword,boolean isIgnoreCase){
		if(StringUtils.isBlank(keyword)||StringUtils.isBlank(source)) 
			return false;
		if(isIgnoreCase){
			keyword=keyword.toUpperCase();
			source=source.toUpperCase();
		}
		return (",".concat(source).concat(",")).contains((",".concat(keyword).concat(",")));
	}
	
	/**
	 * 获取子串在字符串出现的次数
	 * @param str
	 * @param sub
	 * @return
	 */
	 public static int getSubCount(String str,String sub)  
	 {  
        int index = 0;  
        int count = 0;  
        while((index = str.indexOf(sub,index))!=-1)  
        {  
            index = index + sub.length();  
            count++;  
        }  
        return count;  
	} 
	
	/**
	 * 功能：判断json字符串是否是数组
	 * @param str    json字符串
	 * @return  	 true 数组  false 不是数组
	 */
	public static boolean isArrayWithJsonStr(String str){
		if(str.startsWith("[")){
			return true;
		}
		return false;
	}
	
	/**
	 * 功能：判断数组中是否包含某元素
	 */
	public static boolean isContainKeyWithArray(String key,String[] arr){
		if(null == key || null == arr){
			return false;
		}
		for(String str : arr){
			if(key.equalsIgnoreCase(str)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 功能：判断json字符串是否是对象
	 * @param str    json字符串
	 * @return  	 true 对象  false 不是对象
	 */
	public static boolean isObjectWithJsonStr(String str){
		if(str.startsWith("{")){
			return true;
		}
		return false;
	}
	
	/**
	 * 功能：保留字符串两位小数
	 * @param source
	 * @return 两位小数的字符串
	 */
	public static String formatTwoDights(String source){
		if(source.indexOf(".") == -1){
			return source+".00";
		}else{
			String split = source.split("\\.")[1];
			if(split.length() == 1){
				return source+"0";
			}else if(split.length() == 2){
				return source;
			}else{
				return source.substring(0,source.indexOf(".")+3);
			}
		}
	}
	
	/**
	 * 功能：判断表达式是否满足对应的规则
	 * @param str    字符串
	 * @param regex  正则规则
	 * @return boolean 表达式是满足对应的规则
	 */
	public static boolean examine(String str,String regex){
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		return m.matches();
	}
	
	/**
	 * 功能：转换null字符串为""
	 */
	public static String convertNullString(String str){
		if(null ==  str){
			return "";
		}
		return str.trim();
	}
	
    /**
     * 功能：将obj转换成String
     */
	public static String objToString(Object obj) {
		if (obj == null) {
			return "";
		} else {
			if (obj instanceof String) {
				return (String) obj;
			} else if (obj instanceof Date) {
				return null;
			} else {
				return obj.toString();
			}
		}
	}
	
	/**
	 * 功能：将字符串数组转换成整形数组
	 */
	public static Long[] strArrToLongArr(String[] strArr){
		if(null == strArr || strArr.length == 0){
			return  null;
		}
		int len = strArr.length;
		Long[] integerArr = new Long[len];
		for(int i=0;i<len;i++){
			integerArr[i] = Long.valueOf(strArr[i]);
		}
		return integerArr;
	}
	
	/**
	 * 填充字符
	 * @param sign 填充的标识
	 * @param len 填充的长度
	 * @return
	 */
	public static String fillChar(char sign, int len){
        len = len > 0 ? len : 0;
        char[] result = new char[len];
        for (int i = 0; i < len; i++){
        	result[i] = sign;
        }
        return new String(result);
    }

	/**
	 * 转成模糊匹配形式的字符串
	 * @param value
	 */
	public static String toFuzzyMatch(String value){
		return ".*?"+value+".*";
	}
	
	/**
	 * 字符串拼接
	 * @param value
	 * @return
	 */
	public static String concatStr(Object ...value){
	    StringBuffer buffer=new StringBuffer();
		for(Object val:value){
			buffer.append(val);
		}
		return buffer.toString();
	}
	
	/**
	 * 功能：随即生成指定位数的含验证码字符串
	 * @param bit 指定生成验证码位数(默认6位)
	 * @return String  生成指定位数的字符串
	 */
	public static String random(int bit) {
		if(bit == 0){
			bit = 6;
		}
		// 因为o和0,l和1很难区分,所以,去掉大小写的o和l
		String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz";	// 初始化种子
		return RandomStringUtils.random(bit, str);
	}
	
	/**
	 * 功能：随即生成指定位数的含验证码字符串
	 * @param bit 指定生成验证码位数(默认6位)
	 * @return String  生成指定位数的字符串
	*/
	public static String randomNum(int bit) {
		if(bit == 0){
			bit = 6;
		}
		// 因为o和0,l和1很难区分,所以,去掉大小写的o和l
		String str = "0123456789";	// 初始化种子
		return RandomStringUtils.random(bit, str);
	}
	
	/**
	 * 过滤html代码
	 * @param inputString 含html标签的字符串
	 * @return
	*/
   public static String html2Text(String inputString) {
	    if(StringUtils.isBlank(inputString)){
	    	return "";
	    }
        String htmlStr = inputString;
        String textStr = "";
        java.util.regex.Pattern p_script,p_style,p_html;
        java.util.regex.Matcher m_script,m_style,m_html;
        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; 
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; 
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); //过滤script标签
            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); //过滤style标签
            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); //过滤html标签
            textStr = htmlStr;
        } catch (Exception e) {
            System.err.println("Html2Text:" + e.getMessage());
        }
        return textStr;// 返回文本字符串
    }
   
   /**
    * 字符串补齐
    * @param source 源字符串
    * @param fillLength 补齐长度
    * @param fillChar 补齐的字符
    * @param isLeftFill true为左补齐，false为右补齐
    * @return  String
    * @exception 
    * @author:jade.zhu   
    * @since  1.0.0
    */
   public static String stringFill(String source, int fillLength, char fillChar, boolean isLeftFill) {
	   if (source == null || source.length() >= fillLength){ 
		   return source;
	   }
	    
	   StringBuilder result = new StringBuilder(fillLength);
	   int len = fillLength - source.length();
	   if (isLeftFill) {
    	   for (; len > 0; len--) {
    		   result.append(fillChar);
    	   }
    	   result.append(source);
	   } 
	   else {
    	   result.append(source);
    	   for (; len > 0; len--) {
    		   result.append(fillChar);
    	   }
	   }
	   return result.toString();
   }

	/**
	 * 根据手机号返回userId
	 * @param mobilePhone
	 * @return
	 */
	public static String formatMobileToUserId(String mobilePhone){
		if(mobilePhone == null){
			return "";
		}
		String[] str = new String[]{"p", "x", "i", "u", "d", "c", "v", "s", "n", "f"};
		String userId = "";
		for(int i = 0,length = mobilePhone.length(); i < length; i++){
			userId = userId.concat(str[Integer.parseInt(mobilePhone.substring(i, i+1))]);
		}
		return userId;
	}

}
