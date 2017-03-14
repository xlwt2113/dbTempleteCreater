package com.ssj.jdbcfront.dao.impl;

import java.io.BufferedReader;
import java.lang.reflect.Method;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ssj.jdbcfront.dao.DaoAccess;
import com.ssj.jdbcfront.dao.DbUtil;
import com.ssj.jdbcfront.exception.AppException;
import com.ssj.jdbcfront.model.DataSet;
import com.ssj.jdbcfront.model.DbColumnType;
import com.ssj.jdbcfront.model.DbObject;
import com.ssj.jdbcfront.model.DbTableType;
import com.ssj.util.DateUtil;
import com.ssj.util.LogUtil;
import com.ssj.util.StringUtil;

public class OracleAccess extends DaoAccess{
	
	private Map<String,String> dbTypeMap;

	private Map<String,String> getDbTypeMap(){
		if(dbTypeMap==null){
			dbTypeMap = new LinkedHashMap<String,String>();
			dbTypeMap.put("表[Tables]", "TABLE");
			dbTypeMap.put("视图[Views]", "VIEW");
			dbTypeMap.put("方法[Functions]", "FUNCTION");
			dbTypeMap.put("存储过程[Procedures]", "PROCEDURE");
			dbTypeMap.put("包[Packages]", "PACKAGE");
			dbTypeMap.put("包体[Packages Body]", "PACKAGE BODY");
		}
		return dbTypeMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DbObject> getObjectList(String type) {
		String realType = dbTypeMap.get(type);
		String sql = "select t.object_name,t.object_type,t2.comments from user_objects t,user_tab_comments t2 where t.object_name=t2.table_name(+) and t.object_type='"+realType+"' order by t.object_name";
		if("TABLE".equals(realType)){
			sql = "select t.table_name,'"+realType+"' table_type,t2.comments from user_tables t,user_tab_comments t2 where t.table_name=t2.table_name(+) order by t.table_name";
		}
		return (List<DbObject>) DbUtil.executeSqlQuery(sql,DbObject.mapper3);
	}

	@Override
	public List<String> getObjectTypeList() {
		List<String> list = new ArrayList<String>();
		Iterator<String> it = getDbTypeMap().keySet().iterator();
		while(it.hasNext()){
			list.add(it.next());
		}
		return list;
	}
	
	@Override
	public String getEditQuerySql(String name) {
		name = name.toLowerCase();
		if(DbUtil.executeSqlInt("select count(*) from "+name)>1000){
			return "select t.* from "+name+" t where rownum<1000 for update";
		}
		else{
			return "select t.* from "+name+" t for update";
		}
	}

	@Override
	public boolean isObjectExist(String name) {
		if(name==null){
			return false;
		}
		name = name.toUpperCase();
		return DbUtil.executeSqlInt("select count(*) from user_objects where object_name='"+name+"'")>0;
	}

	public DbObject getDbObject(String name) {
		String sql = "select t.object_name,t.object_type,t2.comments from user_objects t,user_tab_comments t2 where t.object_name=t2.table_name(+) and t.object_name='"+name+"'";
		List<?> list = DbUtil.executeSqlQuery(sql, DbObject.mapper3);
		if(list.isEmpty()){
			return null;
		}
		return (DbObject) list.get(0);
	}

	@Override
	public String getObjectSql(String name) {
		DbObject dbo = getDbObject(name);
		return getObjectSql(dbo);
	}
	@SuppressWarnings("unchecked")
	public String getObjectSql(DbObject dbo){
		if(dbo==null){
			return "";
		}
		String sql = "";
		String type = dbo.getType().toUpperCase();
		if("TABLE".equals(type)){
			sql = getTableCreateSql(getTableType(dbo.getName()));
		}
		else if("VIEW".equals(type)){
			sql = DbUtil.executeSqlString("select v.text from sys.obj$ o, sys.view$ v where o.obj# = v.obj# and o.owner# = userenv('SCHEMAID') and o.name='"+dbo.getName()+"'");
			if(StringUtil.isNotBlank(sql)){
				sql = "create or replace view "+dbo.getName()+" as"+LINE_SEPARATOR+sql;
			}
		}
		else if("FUNCTION".equals(type)||"PROCEDURE".equals(type)||"PACKAGE".equals(type)||"PACKAGE BODY".equals(type)){
			List<String> list = (List<String>) DbUtil.executeSqlQuery("select text from user_source where type='"+type+"' and name='"+dbo.getName()+"' order by line");
			StringBuffer buff = new StringBuffer("create or replace ");
			for(String s:list){
				buff.append(s);
			}
			sql = buff.toString();
		}
		return sql;
	}

	public List<String> getDataTypes(){
		List<String> list = new ArrayList<String>();
		list.add("VARCHAR2()");
		list.add("NUMBER");
		list.add("DATE");
		list.add("CLOB");
		list.add("BLOB");
		list.add("CHAR()");
		list.add("LONG");
		list.add("TIMESTAMP");
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DbTableType getTableType(String name) {
		name = name.toUpperCase();
		System.out.println("获取Oracel表格式");
		DbTableType dtt = new DbTableType();
		dtt.setName(name);
		dtt.setComment(DbUtil.executeSqlString("select comments from user_tab_comments where table_name='"+name+"'"));
		dtt.setPkName(DbUtil.executeSqlString("select constraint_name from user_CONSTRAINTS where table_name='"+name+"' and CONSTRAINT_TYPE= 'P'"));
		dtt.setColumns((List<DbColumnType>) DbUtil.executeSqlQuery("select t.COLUMN_NAME,t.DATA_TYPE||" +
				"(case when t.data_type like '%CHAR%' then '('||t.DATA_LENGTH||')' " +
				"when t.data_type='NUMBER' and t.DATA_PRECISION is not null and t.DATA_SCALE>0 then '('||t.DATA_PRECISION||','||t.DATA_SCALE||')' " +
				"when t.data_type='NUMBER' and t.DATA_PRECISION is not null and t.DATA_SCALE=0 then '('||t.DATA_PRECISION||')' end)," +
				"t.DATA_LENGTH,t2.comments,t.DATA_DEFAULT,t.NULLABLE," +
				"(SELECT count(*) from user_CONS_COLUMNS A,user_CONSTRAINTS B WHERE A.CONSTRAINT_NAME=B.CONSTRAINT_NAME(+) " +
				"AND B.CONSTRAINT_TYPE= 'P' and t.TABLE_NAME=a.table_name and a.column_name=t.COLUMN_NAME) pri " +
				"from user_tab_columns t,user_col_comments t2 where t.TABLE_NAME=t2.table_name(+) " +
				"and t.COLUMN_NAME=t2.column_name(+) and t.table_name='"+name+"' order by t.COLUMN_ID",DbColumnType.mapper7));
		return dtt;
	}

	@Override
	public void addTableColumn(String tblName, DbColumnType dct) {
		DbUtil.executeSqlUpdate("alter table "+tblName+" add "+dct.getName()+" "+dct.getType()+
				(StringUtil.isNotBlank(dct.getDefaul())?" default "+dct.getDefaul():"")+(dct.isIsNull()?"":" not null"));
		if(StringUtil.isNotBlank(dct.getComment())){
			DbUtil.executeSqlUpdate(new StringBuffer("comment on column ").append(tblName).append(".").append(dct.getName())
					.append("  is '").append(dct.getComment()).append("'").toString());
		}
	}

	@Override
	public void updateTableColumn(String tblName, DbColumnType oldCol, DbColumnType newCol) {
		if(!newCol.equals(oldCol)){
			DbUtil.executeSqlUpdate("alter table "+tblName+" modify "+oldCol.getName()+" "+newCol.getType()+
					(StringUtil.isNotBlank(newCol.getDefaul())?" default "+newCol.getDefaul():"")+(newCol.isIsNull()!=oldCol.isIsNull()?(newCol.isIsNull()?" null":" not null"):""));
			if(!StringUtil.isEqual(oldCol.getComment(),newCol.getComment())){
				DbUtil.executeSqlUpdate(new StringBuffer("comment on column ").append(tblName).append(".").append(newCol.getName())
						.append("  is '").append(newCol.getComment()).append("'").toString());
			}
		}
	}

	@Override
	public void updatePrimaryKey(String tblName, String pkName, String oldPkCols, String newPkCols) {
		if(StringUtil.isNotBlank(oldPkCols)){
			DbUtil.executeSqlUpdate("alter table "+tblName+" drop constraint "+pkName+" cascade");
		}
		if(StringUtil.isNotBlank(newPkCols)){
			DbUtil.executeSqlUpdate("alter table "+tblName+" add constraint "+pkName+" primary key ("+newPkCols+") using index");
		}
	}

	@Override
	public String getTableDisableTigger(String name) {
		
		return "alter table "+name+" disable all triggers";
	}

	@Override
	public String getTableEnableTigger(String name) {

		return "alter table "+name+" enable all triggers";
	}

	@Override
	public String getExportExt() {
		return "set feedback off;"+LINE_SEPARATOR+"set define off;";
	}

	@Override
	public String getExportPre() {
		return "set feedback on;"+LINE_SEPARATOR+"set define on;";
	}

	@Override
	public String getExportSqlValue(Object obj) {
		if(obj instanceof Date){
			return "to_date('"+DateUtil.format((Date) obj, "yyyy-MM-dd HH:mm:ss")+"','yyyy-mm-dd hh24:mi:ss')";
		}
		else if(obj instanceof java.sql.Timestamp){
			return "to_date('"+DateUtil.format((java.sql.Timestamp) obj, "yyyy-MM-dd HH:mm:ss")+"','yyyy-mm-dd hh24:mi:ss')";
		}
		else if(obj instanceof oracle.sql.TIMESTAMP){
			try {
				return "to_date('"+DateUtil.format(((oracle.sql.TIMESTAMP) obj).dateValue(), "yyyy-MM-dd HH:mm:ss")+"','yyyy-mm-dd hh24:mi:ss')";
			} catch (SQLException e) {
				LogUtil.logError(e);
			}
		}
		else if(obj instanceof Clob){
			Clob c = (Clob) obj;
			StringBuffer buff = new StringBuffer();
			try {
				BufferedReader in = new BufferedReader(c.getCharacterStream());
				String line = in.readLine();
				while(line!=null){
					buff.append(line.replace("'", "''")).append(LINE_SEPARATOR);
					line = in.readLine();
				}
			} catch (Exception e) {
				LogUtil.logError(e);
			}
			return "'"+buff.toString()+"'";
		}
		return super.getExportSqlValue(obj);
	}

	@Override
	public String getSearchQuerySql(String name) {
		if(DbUtil.executeSqlInt("select count(*) from "+name)>1000){
			return super.getSearchQuerySql(name)+" where rownum<1000";
		}
		else{
			return super.getSearchQuerySql(name);
		}
	}

	@Override
	public DataSet getData(String sql, int rows, int page) throws AppException {
		if(rows<=0){
			rows = Integer.MAX_VALUE;
		}
		if(page<=1){
			page = 1;
			sql = "select t_$_o.*,rownum t_$_O_C from ("+sql + ") t_$_o where rownum<="+rows*(page);
		}
		else{
			sql = "select * from (select t_$_o.*,rownum t_$_O_C from ("+sql + ") t_$_o where rownum<="+rows*(page)+") where t_$_O_C>"+rows*(page-1);
		}
		DataSet ds = super.getData(sql, rows, 1);
		if(ds.getHeader().size()>0){
			ds.getHeader().remove(ds.getHeader().size()-1);
		}
		return ds;
	}

	@Override
	public Clob getEmptyClob() {
		try {
			Class<?> cla = DbUtil.loadClass("oracle.sql.CLOB");
			Method m = cla.getDeclaredMethod("getEmptyCLOB", new Class[0]);
			return (Clob) m.invoke(null, new Object[0]);
		} catch (Exception e) {
			LogUtil.logError(e);
		}
		return null;
	}

}
