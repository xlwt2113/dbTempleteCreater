package com.ssj.jdbcfront.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;
import java.util.Date;

import com.ssj.jdbcfront.exception.TransObjectException;
import com.ssj.util.DateUtil;
import com.ssj.util.LogUtil;

public class JdbcType {

	protected int type;
	
	protected int maxLength;

	public JdbcType(int type){
		this.type = type;
	}

	public JdbcType(int type,int maxLength){
		this.type = type;
		this.maxLength = maxLength;
	}
	
	public Object tansValue(Object obj) throws Exception{
		return tansValue(obj,type);
	}
	
	public static Object tansValue(Object obj,int type) throws TransObjectException{
		if(obj==null||"".equals(obj))
			return null;
		String msg = null;
		try {
			switch(type){
			case Types.DATE:
			case Types.TIME:
			case Types.TIMESTAMP:
				msg = "日期格式为yyyy-MM-dd HH:mm:ss";
				if(obj instanceof Date){
					return new java.sql.Timestamp(((Date)obj).getTime());
				}
				else{
					String val = obj.toString();
					return new java.sql.Timestamp(DateUtil.parse(val).getTime());
				}
			case Types.BIGINT:
				msg = "["+obj+"]不能转换成整数";
				return new BigInteger(obj.toString(),10);
			case Types.BOOLEAN:
				return new Boolean("true".equals(obj.toString().toLowerCase()));
			case Types.CHAR:
				msg = "["+obj+"]不能转换成字符";
				return new Character(obj.toString().charAt(0));
			case Types.DECIMAL:
				msg = "["+obj+"]不能转换成数字";
				return Float.parseFloat(obj.toString());
			case Types.DOUBLE:
				msg = "["+obj+"]不能转换成数字";
				return Double.parseDouble(obj.toString());
			case Types.FLOAT:
				msg = "["+obj+"]不能转换成数字";
				return Float.parseFloat(obj.toString());
			case Types.NUMERIC:
				msg = "["+obj+"]不能转换成数字";
				return new BigDecimal(obj.toString());
			case Types.SMALLINT:
			case Types.TINYINT:
			case Types.INTEGER:
				msg = "["+obj+"]不能转换成整数";
				return Integer.parseInt(obj.toString());
			case Types.VARCHAR:
			case Types.LONGVARCHAR:
				return obj.toString();
			case Types.CLOB:
				return null;
			}
		} catch (Exception e) {
			LogUtil.logError(e);
			if(e.getMessage()!=null)
				throw new TransObjectException(msg+e.getMessage());
			else
				throw new TransObjectException(msg);
		}
		return obj;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
}