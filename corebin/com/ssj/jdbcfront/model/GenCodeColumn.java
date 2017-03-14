package com.ssj.jdbcfront.model;

import com.ssj.jdbcfront.template.TransUtil;


public class GenCodeColumn extends DbColumnType {
	
	private String fieldName;
	
	private String fieldType;
	
	private boolean queryCol;
	
	public GenCodeColumn() {
		super();
	}

	public GenCodeColumn(DbColumnType dct) {
		this.setName(dct.getName());
		this.setType(dct.getType());
		this.setTypeLen(dct.getTypeLen());
		this.setDefaul(dct.getDefaul());
		this.setNull(dct.isNull());
		this.setPri(dct.isPri());
		this.setComment(dct.getComment());
		this.setFieldName(TransUtil.getPropertyName(this.getName()));
		this.setFieldType(TransUtil.trans2JavaType(this.getBaseType()));
	}
	
	public String getMaxLength(){
		if("java.lang.String".equals(this.fieldType)){
			return ""+(this.getTypeLen()/2);
		}
		else if("java.lang.Integer".equals(this.fieldType)||"int".equals(this.fieldType)){
			return ""+this.getTypeLen();
		}
		return "0";
	}

	public String getFieldGetter(){
		if(fieldType.toLowerCase().indexOf("boolean")>=0){
			return "is"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
		}
		return "get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
	}

	public String getFieldSetter(){
		return "set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
	}

	public String getJdbcGetter(){
		String fieldType = this.fieldType;
		if(fieldType.toLowerCase().indexOf("date")>=0||fieldType.toLowerCase().indexOf("time")>=0){
			return "getTimestamp";
		}
		if(fieldType.lastIndexOf(".")>=0){
			fieldType = this.fieldType.substring(this.fieldType.lastIndexOf(".")+1);
		}
		return "get"+fieldType.substring(0,1).toUpperCase()+fieldType.substring(1);
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	
	public boolean isQueryCol() {
		return queryCol;
	}

	public void setQueryCol(boolean queryCol) {
		this.queryCol = queryCol;
	}

	public void setQueryCol(String queryCol) {
		this.queryCol = !(queryCol==null||"n".equals(queryCol)||"no".equals(queryCol)||"false".equals(queryCol)||"0".equals(queryCol));
	}

}
