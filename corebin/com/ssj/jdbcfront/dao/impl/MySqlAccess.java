package com.ssj.jdbcfront.dao.impl;

import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ssj.jdbcfront.dao.DaoAccess;
import com.ssj.jdbcfront.dao.DbUtil;
import com.ssj.jdbcfront.dao.RowMapper;
import com.ssj.jdbcfront.exception.AppException;
import com.ssj.jdbcfront.model.DataSet;
import com.ssj.jdbcfront.model.DbColumnType;
import com.ssj.jdbcfront.model.DbObject;
import com.ssj.jdbcfront.model.DbTableType;
import com.ssj.util.DateUtil;
import com.ssj.util.LogUtil;
import com.ssj.util.StringUtil;

public class MySqlAccess extends DaoAccess{
	
	private Map<String,String> dbTypeMap;

	private Map<String,String> getDbTypeMap(){
		if(dbTypeMap==null){
			dbTypeMap = new LinkedHashMap<String,String>();
			dbTypeMap.put("表[Tables]", "TABLE");
			dbTypeMap.put("视图[Views]", "VIEW");
			dbTypeMap.put("方法[Functions]", "FUNCTION");
			dbTypeMap.put("存储过程[Procedures]", "PROCEDURE");
			dbTypeMap.put("触发器[Triggers]", "TRIGGER");
			dbTypeMap.put("数据库[Database]", "DATABASE");
		}
		return dbTypeMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DbObject> getObjectList(String type) {
		String realType = dbTypeMap.get(type);
		String sql = "select t.specific_name,'"+realType+"' object_type,t.routine_comment from information_schema.routines t " +
				"where t.routine_schema='"+dbv.getSid()+"' and t.routine_type='"+realType+"' order by t.specific_name";
		if("TABLE".equals(realType)){
			sql = "select t.table_name,'"+realType+"' table_type,t.table_comment from information_schema.tables t " +
					"where t.table_type='BASE TABLE' and t.table_schema='"+dbv.getSid()+"' order by t.table_name";
			return (List<DbObject>)DbUtil.executeSqlQuery(sql,new RowMapper(){
				public Object mapRow(ResultSet rs, int num) {
					DbObject obj = new DbObject();
					try {
						obj.setName(rs.getString(1));
						obj.setType(rs.getString(2));
						obj.setComment(getSafeComment(rs.getString(3)));
					} catch (SQLException e) {
						LogUtil.logError(e);
					}
					return obj;
				}});
		}
		else if("VIEW".equals(realType)){
			sql = "select t.table_name,'"+realType+"' table_type,t.table_comment from information_schema.tables t " +
					"where t.table_type='VIEW' and t.table_schema='"+dbv.getSid()+"' order by t.table_name";
		}
		else if("FUNCTION".equals(realType)){
			sql = "select name,'"+realType+"' table_type,comment from mysql.proc t " +
					"where type='FUNCTION' and db='"+dbv.getSid()+"' order by name";
		}
		else if("PROCEDURE".equals(realType)){
			sql = "select name,'"+realType+"' table_type,comment from mysql.proc t " +
					"where type='PROCEDURE' and db='"+dbv.getSid()+"' order by name";
		}
		else if("TRIGGER".equals(realType)){
			sql = "select trigger_name,'TRIGGER','' from information_schema.TRIGGERS where trigger_schema='"+dbv.getSid()+"'";
		}
		else if("DATABASE".equals(realType)){
			sql = "select schema_name,'DATABASE' type,default_character_set_name from information_schema.SCHEMATA";
		}
		return (List<DbObject>)DbUtil.executeSqlQuery(sql,DbObject.mapper3);
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
		if(DbUtil.executeSqlInt("select count(*) from "+name)>1000){
			return "select t.* from "+name+" t limit 1000 for update";
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
		String sql = "select count(*) from (select specific_name name,routine_schema user from information_schema.routines " +
				"union select table_name name,table_schema user from information_schema.tables " +
				"union select trigger_name name,trigger_schema user from information_schema.TRIGGERS where " +
				"union select schema_name name,'"+name+"' user from information_schema.SCHEMATA) t where t.user='"+dbv.getSid()+"' and t.name='"+name+"'";
		return DbUtil.executeSqlInt(sql)>0;
	}

	public DbObject getDbObject(String name) {
		String sql = "select t.name,t.type,t.comment from(select specific_name name,routine_type type,routine_comment comment,routine_schema user from information_schema.routines " +
				"union select table_name name,(case when table_type='BASE TABLE' then 'TABLE' else table_type end) type,table_comment comment,table_schema user from information_schema.tables) t " +
				"where t.user='"+dbv.getSid()+"' and t.name='"+name+"'";
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
			sql = DbUtil.executeSqlString("select view_definition from information_schema.views where table_schema='"+dbv.getSid()+"' and table_name='"+dbo.getName()+"'");
			if(StringUtil.isNotBlank(sql)){
				sql = "create or replace view "+dbo.getName()+" as"+LINE_SEPARATOR+sql;
			}
		}
		else if("FUNCTION".equals(type)||"PROCEDURE".equals(type)){
			List<Object[]> list = (List<Object[]>)DbUtil.executeSqlQuery("select param_list,returns,body from mysql.proc where db='"+dbv.getSid()+"' and type='"+type+"' and name='"+dbo.getName()+"'");
			if(list.size()>0){
				Object[] objs = list.get(0);
				String body = new String((byte[])objs[2]);
				sql = "create "+type+" "+dbo.getName()+" ("+new String((byte[])objs[0])+")"+LINE_SEPARATOR+("FUNCTION".equals(type)?"  returns "+objs[1]+LINE_SEPARATOR:"")+body;
			}
		}
		return sql;
	}

	public List<String> getDataTypes(){
		List<String> list = new ArrayList<String>();
		list.add("CHAR()");
		list.add("VARCHAR()");
		list.add("DATE");
		list.add("DATETIME");
		list.add("INT");
		list.add("FLOAT");
		list.add("DOUBLE");
		list.add("TEXT");
		list.add("BLOB");
		list.add("TIMESTAMP");
		return list;
	}

	public String getTableCreateSql(DbTableType dtt) {
		StringBuffer buff = new StringBuffer();
		buff.append(getCommentStart()+getSafeStr(dtt.getComment())+LINE_SEPARATOR);
		buff.append("create table "+dtt.getName()+LINE_SEPARATOR);
		buff.append("("+LINE_SEPARATOR);
		int nameMax=0,typeMax=0,defMax=0,nullMax=0;
		StringBuffer key = new StringBuffer();
		for(DbColumnType dct:dtt.getColumns()){
			if(dct.isPri()){
				key.append(",").append(dct.getName());
			}
			nameMax = Math.max(nameMax, dct.getName().length());
			typeMax = Math.max(typeMax, dct.getType().length());
			defMax = Math.max(defMax, dct.getDefaul()==null?0:"default ".length()+dct.getDefaul().trim().length());
			nullMax = Math.max(nullMax, dct.isNull()?0:"not null".length());
		}
		int i=1;
		for(DbColumnType dct:dtt.getColumns()){
			buff.append(getColumnGenSql(dct,nameMax,typeMax,defMax,nullMax,i++==dtt.getColumns().size()));
		}
		if(key.length()>0){
			buff.append("  ,primary key ("+key.substring(1)+")");
		}
		buff.append(LINE_SEPARATOR+")");
		if(StringUtil.isNotBlank(dtt.getComment())){
			buff.append(" Comment='").append(dtt.getComment()).append("'"+LINE_SEPARATOR);
		}
		return buff.toString();
	}
	@Override
	public String getColumnGenSql(DbColumnType dct, int nameMax, int typeMax, int defMax, int nullMax, boolean last) {
		StringBuffer buff = new StringBuffer();
		buff.append("  ");
		buff.append(dct.getName());
		buff.append(StringUtil.getBlank(nameMax+1-dct.getName().length()));
		buff.append(dct.getType());
		buff.append(StringUtil.getBlank(typeMax+1-dct.getType().length()));
		if(defMax>0){
			if(StringUtil.isNotBlank(dct.getDefaul())){
				buff.append("default "+dct.getDefaul().trim()+StringUtil.getBlank(defMax+1-dct.getDefaul().trim().length()-"default ".length()));
			}
			else{
				buff.append(StringUtil.getBlank(defMax+1));
			}
		}
		if(nullMax>0){
			if(!dct.isNull()){
				buff.append("not null ");
			}
			else{
				buff.append(StringUtil.getBlank(nullMax+1));
			}
		}
		if(StringUtil.isNotBlank(dct.getComment())){
			buff.append(" Comment '").append(dct.getComment()).append("'");
		}
		buff.append(last?" ":",").append(LINE_SEPARATOR);
		return buff.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public DbTableType getTableType(String name) {
		DbTableType dtt = new DbTableType();
		dtt.setName(name);
		dtt.setComment(getSafeComment(DbUtil.executeSqlString("select table_comment from information_schema.tables where table_schema='"+dbv.getSid()+"' and table_name='"+name+"'")));
		dtt.setColumns((List<DbColumnType>)DbUtil.executeSqlQuery("select t.COLUMN_NAME,t.COLUMN_TYPE," +
				"floor(t.character_maximum_length),t.column_comment,t.column_DEFAULT,t.is_NULLABLE," +
				"(case when t.column_key='PRI' then 'y' else 'n' end) pri " +
				"from information_schema.columns t where t.table_schema='"+dbv.getSid()+"' " +
				"and t.table_name='"+name+"' order by t.ordinal_position",DbColumnType.mapper7));
		return dtt;
	}

	@Override
	public void addTableColumn(String tblName, DbColumnType dct) {
		String sql = "alter table "+tblName+" add "+dct.getName()+" "+dct.getType()+
				(StringUtil.isNotBlank(dct.getDefaul())?" default "+dct.getDefaul():"")+(dct.isNull()?"":" not null");
		if(StringUtil.isNotBlank(dct.getComment())){
			sql += " comment '"+dct.getComment()+"'";
		}
		DbUtil.executeSqlUpdate(sql);
	}

	@Override
	public void updateTableColumn(String tblName, DbColumnType oldCol, DbColumnType newCol) {
		if(!newCol.equals(oldCol)){
			String sql = "alter table "+tblName+" CHANGE COLUMN "+oldCol.getName()+" "+newCol.getName()+" "+newCol.getType()+
					(StringUtil.isNotBlank(newCol.getDefaul())?" default "+newCol.getDefaul():"")+
					(newCol.isNull()!=oldCol.isNull()?(newCol.isNull()?" null":" not null"):"")+" comment '"+newCol.getComment()+"'";
			DbUtil.executeSqlUpdate(sql);
		}
	}

	@Override
	public String getExportSqlValue(Object obj) {
		if(obj instanceof Date){
			return "'"+DateUtil.format((Date) obj, "yyyy-MM-dd HH:mm:ss")+"'";
		}
		return super.getExportSqlValue(obj);
	}

	@Override
	public void updatePrimaryKey(String tblName, String pkName, String oldPkCols, String newPkCols) {
		if(StringUtil.isNotBlank(oldPkCols)){
			DbUtil.executeSqlUpdate("alter table "+tblName+" drop PRIMARY key");
		}
		if(StringUtil.isNotBlank(newPkCols)){
			DbUtil.executeSqlUpdate("alter table "+tblName+" add PRIMARY key ("+newPkCols+")");
		}
	}

	@Override
	public void commentTable(String tblName, String tblComment) {
		DbUtil.executeSqlUpdate("ALTER TABLE "+tblName+" COMMENT='"+tblComment+"'");
	}

	@Override
	public String getTableDisableTigger(String name) {
		
		return "LOCK TABLES "+name+" WRITE";
	}

	@Override
	public String getTableEnableTigger(String name) {

		return "UNLOCK TABLES";
	}

	@Override
	public String getSearchQuerySql(String name) {
		if(DbUtil.executeSqlInt("select count(*) from "+name)>1000){
			return super.getSearchQuerySql(name)+" limit 1000";
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
		if(page<1){
			page = 1;
		}
		sql = sql + " limit "+rows*(page-1)+","+rows;
		return super.getData(sql, rows, 1);
	}

	@Override
	public Clob getEmptyClob() {
		return null;
	}

	protected String getSafeComment(String s){
		if(s==null){
			return "";
		}
		return s.replaceAll("\\s*;?\\s*InnoDB free: \\d+ kB\\s*;?\\s*", "");
	}
	/** 获取单行注释的起始字符串 */
	protected String getCommentStart(){
		return "-- ";
	}
}
