package com.ssj.util;

public class StringUtil {

	/**
	* �����ˣ���˧��
	* ��������: 2010-12-12  ����01:58:45
	* ��������: �ж�ָ���ַ���Ϊ��
	* �����Ĳ����ͷ���ֵ: 
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
	* �����ˣ���˧��
	* ��������: 2010-12-12  ����01:58:36
	* ��������: �ж�һ���ַ����ǿ�
	* �����Ĳ����ͷ���ֵ: 
	* @param str
	* @return
	*/
	public static boolean isNotBlank(String str){
		return !isBlank(str);
	}
	
	/**
	* �����ˣ���˧��
	* ��������: 2010-12-12  ����01:57:45
	* ��������: �ж������ַ����Ƿ�ȼۣ�ע��null����ַ����ȼ�
	* �����Ĳ����ͷ���ֵ: 
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
	* �����ˣ���˧��
	* ��������: 2010-12-12  ����01:58:55
	* ��������: ������ת��Ϊ�ַ�����nullֵ�ᱻת��Ϊ""
	* �����Ĳ����ͷ���ֵ: 
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
	* �����ˣ���˧��
	* ��������: 2010-12-12  ����01:59:19
	* ��������: �õ�ָ�����ȵĿո�
	* �����Ĳ����ͷ���ֵ: 
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
