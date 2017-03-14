package com.ssj.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public static String format(Date date,String format){
		if(date==null)
			return null;
		sdf.applyPattern(format);
		return sdf.format(date);
	}

	public static String format(Date date){
		return format(date,"yyyy-MM-dd");
	}
	
	public static Date parse(String arg,String format){
		if(arg==null)
			return null;
		sdf.applyPattern(format);
		try {
			return sdf.parse(arg);
		} catch (ParseException e) {
			LogUtil.logError(e);
		}
		return null;
	}
	
	public static Date parse(String arg){
		if(arg==null)
			return null;
		Calendar c = Calendar.getInstance();
		int year=-1,month=-1,day=-1,hour=-1,minute=-1,second=-1,mill=-1;
		String[] tmp = arg.split("\\D");
		for(int i=0;i<tmp.length;i++){
			if(tmp[i].length()==4)
				year = Integer.parseInt(tmp[i],10);
			else if(month==-1){
				month = Integer.parseInt(tmp[i],10);
			}
			else if(day==-1){
				day = Integer.parseInt(tmp[i],10);
			}
			else if(hour==-1){
				hour = Integer.parseInt(tmp[i],10);
			}
			else if(minute==-1){
				minute = Integer.parseInt(tmp[i],10);
			}
			else if(second==-1){
				second = Integer.parseInt(tmp[i],10);
			}
			else if(mill==-1){
				mill = Integer.parseInt(tmp[i],10);
			}
		}
		if(arg.length()>2&&arg.substring(arg.length()-2).toLowerCase().equals("pm"))
			hour += 12;
		if(year<0)year = 2000;
		if(month<0)month = 0;
		if(day<0)day = 1;
		if(hour<0)hour = 0;
		if(minute<0)minute = 0;
		if(second<0)second = 0;
		if(mill<0)mill = 0;
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month-1);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, second);
		c.set(Calendar.MILLISECOND, mill);
		return c.getTime();
	}
	
	public static void main(String[] args){
		System.out.println(format(parse("2009年1月2日23点26分38秒569毫秒"),"yyyy-MM-dd HH:mm:ss.SSS"));
	}

}
