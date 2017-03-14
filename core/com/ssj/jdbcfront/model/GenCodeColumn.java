package com.ssj.jdbcfront.model;

import com.ssj.jdbcfront.template.TransUtil;
import com.ssj.util.StringUtil;


public class GenCodeColumn extends DbColumnType {

	/**
	 * 保存为文件时要保存的数据项
	 */
	private String[] savePropts = "name,fieldName,fieldType,type,typeLen,comment,isNull,isPri,isHidden,queryCol,queryType,dictName,seq".split(",");

	private String fieldName;
	
	private String fieldType;
	
	private boolean queryCol;
	
	/*** 查询框类型 */
	private String queryType;

	/*** 填写或查询时用到的公共字典名称 */
	private String dictName;

	/*** 在添加修改查询时是否为隐藏字段 */
	private Boolean isHidden;

	/*** 是否参与默认排序，按数量从小到大 */
	private Integer seq;
	
	public GenCodeColumn() {
		super();
	}

	public GenCodeColumn(DbColumnType dct) {
		this.setName(dct.getName());
		this.setType(dct.getType());
		this.setTypeLen(dct.getTypeLen());
		this.setDefaul(dct.getDefaul());
		this.setIsNull(dct.isIsNull());
		this.setIsPri(dct.isIsPri());
		System.out.println("===dct.getComment()==="+dct.getComment());
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
			fieldType = fieldType.substring(fieldType.lastIndexOf(".")+1);
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
		return StringUtil.isNotBlank(queryType) || queryCol;
	}

	public void setQueryCol(boolean queryCol) {
		this.queryCol = queryCol;
	}

	public void setQueryCol(String queryCol) {
		if(StringUtil.isBlank(queryCol)){
			this.queryCol =  false;
		}
		else{
			this.queryCol = !(queryCol==null||"n".equals(queryCol)||"no".equals(queryCol)||"false".equals(queryCol)||"0".equals(queryCol));
		}
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	String hideCols = ",vcid,id,vcpid,vcmid,vcdelflag,isdel,vcdel,vcadd,dtadd,vcmodify,dtmodify,";
	public Boolean getIsHidden() {
		if(isHidden==null){
			String name = ","+this.getFieldName().toLowerCase()+",";
			isHidden = hideCols.indexOf(name)>=0;
		}
		return isHidden;
	}

	public void setIsHidden(Boolean isHidden) {
		this.isHidden = isHidden;
	}

	public void setIsHidden(String queryCol) {
		this.isHidden = !(queryCol==null||"n".equals(queryCol)||"no".equals(queryCol)||"false".equals(queryCol)||"0".equals(queryCol));
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String[] getSavePropts() {
		return savePropts;
	}

}
