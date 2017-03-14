package com.ssj.jdbcfront.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ssj.jdbcfront.dao.RowMapper;
import com.ssj.jdbcfront.util.EqualsBuilder;
import com.ssj.util.LogUtil;

public class DbColumnType extends DbObject {

	private String defaul;
	
	private long typeLen;
	
	private boolean isNull,isPri;
	
	public static RowMapper mapper7 = new RowMapper(){
		public Object mapRow(ResultSet rs, int num) {
			DbColumnType dct = new DbColumnType();
			try {
				dct.setName(rs.getString(1));
				dct.setType(rs.getString(2));
				dct.setTypeLen(rs.getLong(3));
				dct.setComment(rs.getString(4));
				dct.setDefaul(rs.getString(5));
				dct.setIsNull(rs.getString(6));
				dct.setIsPri(rs.getString(7));
			} catch (SQLException e) {
				LogUtil.logError(e);
			}
			return dct;
		}};
	
	public DbColumnType(){
		
	}
	public DbColumnType(String name){
		this.name = name;
		this.isNull = true;
	}
	public DbColumnType(String name,String type){
		this.name = name;
		this.type = type;
		this.isNull = true;
	}
	public String toString(){
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof DbColumnType)){
			return false;
		}
		DbColumnType that = (DbColumnType) obj;
		return new EqualsBuilder()
		.append(this.name, that.name)
		.append(this.type, that.type)
		.append(this.isNull, that.isNull)
		.append(this.isPri, that.isPri)
		.append(this.defaul, that.defaul)
		.append(this.comment, that.comment)
		.isEquals();
	}
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-11-29  下午09:03:42
	* 功能描述: 可接受范围  y/n  yes/no  true/false  1/0
	* 方法的参数和返回值: 
	* @param isNull
	*/
	public void setIsNull(String isNull){
		isNull = isNull.toLowerCase();
		this.isNull = !(isNull==null||"n".equals(isNull)||"no".equals(isNull)||"false".equals(isNull)||"0".equals(isNull));
	}

	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-11-29  下午09:03:53
	* 功能描述: 可接受范围  y/n  yes/no  true/false  1/0
	* 方法的参数和返回值: 
	* @param isPri
	*/
	public void setIsPri(String isPri){
		isPri = isPri.toLowerCase();
		this.isPri = !(isPri==null||"n".equals(isPri)||"no".equals(isPri)||"false".equals(isPri)||"0".equals(isPri));
	}

	public String getDefaul() {
		return defaul==null?"":defaul;
	}

	public void setDefaul(String defaul) {
		this.defaul = defaul;
	}

	public boolean isNull() {
		return isNull;
	}

	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}

	public boolean isPri() {
		return isPri;
	}

	public void setPri(boolean isPri) {
		this.isPri = isPri;
	}

	public long getTypeLen() {
		return typeLen;
	}

	public void setTypeLen(long typeLen) {
		this.typeLen = typeLen;
	}
}
