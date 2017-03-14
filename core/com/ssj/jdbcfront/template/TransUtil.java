package com.ssj.jdbcfront.template;

import java.util.Map;

import com.ssj.jdbcfront.util.ConfigUtil;
import com.ssj.util.StringUtil;

public class TransUtil {

	public static String getPropertyName(String colName){
		StringBuffer name = new StringBuffer(colName.toLowerCase());
		boolean flag = false;
		for(int i=0;i<name.length();i++){
			if(flag){
				name.setCharAt(i,Character.toUpperCase(name.charAt(i)));
				flag = false;
			}
			else if(name.charAt(i)=='_' || name.charAt(i)=='$' || name.charAt(i)=='#'){
				name.deleteCharAt(i--);
				flag = true;
			}
		}
		if(name.length()>=2)
			name.setCharAt(1,Character.toLowerCase(name.charAt(1)));
		return name.toString();
	}

	private static Map<String,String> map;
	
	public static String trans2JavaType(String dbType){
		if(map==null){
			map = ConfigUtil.getTypeTransMap();
		}
		if(map.get(dbType)!=null){
			return map.get(dbType);
		}
		dbType = StringUtil.toString(dbType).toUpperCase();
		for(String k:map.keySet()){
			if(dbType.contains(k.toUpperCase())){
				map.put(dbType, map.get(k));
				return map.get(k);
			}
		}
		return "java.lang.String";
	}
}
