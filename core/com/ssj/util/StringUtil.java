package com.ssj.util;

public class StringUtil {

	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-12-12  下午01:58:45
	* 功能描述: 判断指定字符串为空
	* 方法的参数和返回值: 
	* @param str
	* @return
	*/
	public static boolean isBlank(String str) {
		
		return str==null||str.trim().length()==0;
	}

	public static boolean isBlank(Object str) {
		
		return str==null||str.toString().trim().length()==0;
	}
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-12-12  下午01:58:36
	* 功能描述: 判断一个字符串非空
	* 方法的参数和返回值: 
	* @param str
	* @return
	*/
	public static boolean isNotBlank(String str){
		return !isBlank(str);
	}
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-12-12  下午01:57:45
	* 功能描述: 判断两个字符串是否等价，注：null与空字符串等价
	* 方法的参数和返回值: 
	* @param arg1
	* @param arg2
	* @return
	*/
	public static boolean isEqual(String arg1,String arg2){
		return toString(arg1).equals(toString(arg2));
	}

	public static boolean isEqual(Object arg1,Object arg2){
		return toString(arg1).equals(toString(arg2));
	}

	public static boolean isNotEqual(String arg1,String arg2){
		return !isEqual(arg1,arg2);
	}
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-12-12  下午01:58:55
	* 功能描述: 将对象转化为字符串，null值会被转化为""
	* 方法的参数和返回值: 
	* @param obj
	* @return
	*/
	public static String toString(Object obj){
		if(obj==null){
			return "";
		}
		return obj.toString();
	}

	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-12-12  下午01:59:19
	* 功能描述: 得到指定长度的空格
	* 方法的参数和返回值: 
	* @param len
	* @return
	*/
	public static String getBlank(int len){
		StringBuffer buff = new StringBuffer();
		while(len-->0){
			buff.append(" ");
		}
		return buff.toString();
	}

}
