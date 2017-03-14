package com.ssj.jdbcfront.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ssj.jdbcfront.template.TransUtil;
import com.ssj.util.DateUtil;

public class GenCodeModel extends DbTableType {

	/**
	 * 指定的URL
	 */
	private String url;
	
	/**
	 * 指定的基础包路径
	 */
	private String basePackage;
	
	/**
	 * 开发人姓名
	 */
	private String develpor;
	
	private String genDate = DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss");
	
	private String dbUser;
	
	/**
	 * 查询用的SQL
	 */
	private String querySql;
	
	private GenCodeModel subTable;

	private List<GenCodeColumn> keyList;

	private List<GenCodeColumn> columnList;
	
	public String getClassPre(){
		return url.substring(0,1).toUpperCase()+url.substring(1);
	}

	public String getBasePackagePath() {
		return basePackage.replace(".", "/");
	}
	
	public List<GenCodeColumn> getAllColumn(){
		List<GenCodeColumn> list = new ArrayList<GenCodeColumn>();
		list.addAll(keyList);
		list.addAll(columnList);
		return list;
	}
	
	public String getOrderField(){
		List<GenCodeColumn> list = getAllColumn();
		if(list==null||list.size()==0){
			return "";
		}
		for(GenCodeColumn col:list){
			if("java.util.Date".equals(col.getFieldType())){
				return col.getName()+","+list.get(0).getName();
			}
		}
		return list.get(0).getName();
	}

	public String getQuerySql() {
		return querySql;
	}

	public void setQuerySql(String charSet) {
		this.querySql = charSet;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public String getDevelpor() {
		return develpor;
	}

	public void setDevelpor(String develpor) {
		this.develpor = develpor;
	}

	public List<GenCodeColumn> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<GenCodeColumn> columnList) {
		this.columnList = columnList;
	}

	public List<GenCodeColumn> getKeyList() {
		return keyList;
	}

	public void setKeyList(List<GenCodeColumn> keyList) {
		this.keyList = keyList;
	}

	public String getGenDate() {
		return genDate;
	}

	public void setGenDate(String genDate) {
		this.genDate = genDate;
	}
	
	public GenCodeModel getSubTable() {
		return subTable;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public void setSubTable(DbTableType tbl) {
		this.subTable = new GenCodeModel();
		subTable.setName(tbl.getName());
		subTable.setUrl(TransUtil.getPropertyName(tbl.getName()));
		subTable.setComment(tbl.getComment());
		subTable.setPkName(tbl.getPkName());
		List<GenCodeColumn> keys = new ArrayList<GenCodeColumn>();
		List<GenCodeColumn> cols = new ArrayList<GenCodeColumn>();
		subTable.setKeyList(keys);
		subTable.setColumnList(cols);
		for(DbColumnType col:tbl.getColumns()){
			if(col.isPri()){
				keys.add(new GenCodeColumn(col));
			}
			else{
				cols.add(new GenCodeColumn(col));
			}
		}
	}

}
