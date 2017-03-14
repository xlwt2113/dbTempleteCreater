package com.ssj.jdbcfront.dao;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import com.ssj.jdbcfront.exception.AppException;
import com.ssj.jdbcfront.model.DataBaseView;
import com.ssj.jdbcfront.model.DataSet;
import com.ssj.jdbcfront.model.DbColumnType;
import com.ssj.jdbcfront.model.DbObject;
import com.ssj.jdbcfront.model.DbTableType;
import com.ssj.util.LogUtil;
import com.ssj.util.StringUtil;

/**
* 创建人：宋帅杰
* 创建日期：2010-11-25
* 创建时间：下午09:41:42
* 功能描述：抽象的数据库访问类
* ==============================================
* 修改历史
* 修改人		修改时间		修改原因
*
* ==============================================
*/
public abstract class DaoAccess {
	
	protected DataBaseView dbv;
	
	protected Connection con;
	
	protected static String LINE_SEPARATOR = System.getProperty("line.separator");
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2009-4-10  下午10:38:26
	* 功能描述: 得到所有数据库对象的类型
	* 方法的参数和返回值: 
	* @return
	*/
	public abstract List<String> getObjectTypeList();
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2009-4-10  下午10:39:36
	* 功能描述: 得到某一类数据库对象的类型
	* 方法的参数和返回值: 
	* @param type
	* @return
	*/
	public abstract List<DbObject> getObjectList(String type);
	
	public List<DbObject> getAllTables(){
		return getObjectList("表[Tables]");
	}

	public DataBaseView getDbv() {
		return dbv;
	}

	public void setDbv(DataBaseView dbv) {
		this.dbv = dbv;
	}
	
	private static HashMap<Integer,String> typeMap = new HashMap<Integer,String>();
	static{
		for(Field f:java.sql.Types.class.getDeclaredFields()){
			if(f.getType().equals(int.class)){
				try {
					typeMap.put(f.getInt(null),f.getName());
				} catch (IllegalArgumentException e) {
					LogUtil.logError(e);
				} catch (IllegalAccessException e) {
					LogUtil.logError(e);
				}
			}
		}
	}

	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-11-28  下午08:56:00
	* 功能描述: 根据JDBC数据类型的值获取类型名称
	* 方法的参数和返回值: 
	* @param type
	* @return
	*/
	public static String getJdbcTypeName(int type){
		String name = typeMap.get(type);
		if(StringUtil.isBlank(name)){
			name = "未知类型";
		}
		return name;
	}
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-11-28  下午08:56:20
	* 功能描述: 判断是否为已知JDBC类型
	* 方法的参数和返回值: 
	* @param type
	* @return
	*/
	public static boolean isKnowJdbcType(int type){
		return typeMap.containsKey(type);
	}

	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-11-28  下午11:01:32
	* 功能描述: 获取可编辑的SQL查询语句
	* 方法的参数和返回值: 
	* @param name
	* @return
	*/
	public abstract String getEditQuerySql(String name);
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-11-29  下午09:00:38
	* 功能描述: 判断数据库对象是否存在
	* 方法的参数和返回值: 
	* @param name
	* @return
	*/
	public abstract boolean isObjectExist(String name);
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-11-29  下午09:08:08
	* 功能描述: 获取指定数据库对象的生成SQL语句
	* 方法的参数和返回值: 
	* @param name
	* @return
	*/
	public abstract String getObjectSql(String name);
	public abstract String getObjectSql(DbObject obj);
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-11-29  下午10:48:45
	* 功能描述: 获取指定数据库对象的注释
	* 方法的参数和返回值: 
	* @param name
	* @return
	*/
	public abstract DbTableType getTableType(String name);

	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-12-12  上午09:39:36
	* 功能描述: 将表结构转化为生成语句
	* 方法的参数和返回值: 
	* @param dtt
	* @return
	*/
	public String getTableCreateSql(DbTableType dtt) {
		StringBuffer buff = new StringBuffer();
		if(StringUtil.isNotBlank(dtt.getComment())){
			buff.append(getCommentStart()+getSafeStr(dtt.getComment())+LINE_SEPARATOR);
		}
		buff.append("create table "+dtt.getName()+LINE_SEPARATOR);
		buff.append("("+LINE_SEPARATOR);
		int nameMax=0,typeMax=0,defMax=0,nullMax=0;
		for(DbColumnType dct:dtt.getColumns()){
			nameMax = Math.max(nameMax, dct.getName().length());
			typeMax = Math.max(typeMax, dct.getType().length());
			defMax = Math.max(defMax, dct.getDefaul()==null?0:"default ".length()+dct.getDefaul().trim().length());
			nullMax = Math.max(nullMax, dct.isIsNull()?0:"not null".length());
		}
		int i=1;
		for(DbColumnType dct:dtt.getColumns()){
			buff.append(getColumnGenSql(dct,nameMax,typeMax,defMax,nullMax,i++==dtt.getColumns().size()));
		}
		buff.append(");"+LINE_SEPARATOR);
		if(StringUtil.isNotBlank(dtt.getComment())){
			buff.append("comment on table ").append(dtt.getName()).append(LINE_SEPARATOR);
			buff.append("  is '").append(dtt.getComment()).append("';"+LINE_SEPARATOR);
		}
		StringBuffer key = new StringBuffer();
		for(DbColumnType dct:dtt.getColumns()){
			buff.append(getColumnCommentSql(dtt.getName(),dct.getName(),dct.getComment()));
			if(dct.isIsPri()){
				key.append(",").append(dct.getName());
			}
		}
		if(key.length()>0){
			buff.append(getPrimaryKeySql(dtt.getName(),dtt.getPkName(),key.substring(1)));
		}
		return buff.toString();
	}
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-12-12  下午08:33:43
	* 功能描述: 获取每列的SQL生成语句片段
	* 方法的参数和返回值: 
	* @param dct
	* @param nameMax
	* @param typeMax
	* @param defMax
	* @param nullMax
	* @param last
	* @return
	*/
	public String getColumnGenSql(DbColumnType dct,int nameMax,int typeMax,int defMax,int nullMax,boolean last){
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
			if(!dct.isIsNull()){
				buff.append("not null ");
			}
			else{
				buff.append(StringUtil.getBlank(nullMax+1));
			}
		}
		buff.append(last?" ":",");
		if(StringUtil.isNotBlank(dct.getComment())){
			buff.append(getCommentStart());
			buff.append(getSafeStr(dct.getComment()));
		}
		buff.append(LINE_SEPARATOR);
		return buff.toString();
	}
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-12-12  下午08:48:44
	* 功能描述: 获取每列的注释语句
	* 方法的参数和返回值: 
	* @param tblName
	* @param name
	* @param comment
	* @return
	*/
	public String getColumnCommentSql(String tblName,String name,String comment){
		StringBuffer buff = new StringBuffer();
		if(StringUtil.isNotBlank(comment)){
			buff.append("comment on column ").append(tblName).append(".").append(name).append(LINE_SEPARATOR);
			buff.append("  is '").append(comment).append("';"+LINE_SEPARATOR);
		}
		return buff.toString();
	}
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-12-13  上午08:55:27
	* 功能描述: 增加数据库列
	* 方法的参数和返回值: 
	* @param tblName
	* @param col
	*/
	public abstract void addTableColumn(String tblName,DbColumnType col);

	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-12-13  上午08:56:11
	* 功能描述: 更新数据库列
	* 方法的参数和返回值: 
	* @param tblName
	* @param oldCol
	* @param newCol
	*/
	public abstract void updateTableColumn(String tblName,DbColumnType oldCol,DbColumnType newCol);

	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-12-13  上午08:56:42
	* 功能描述: 删除数据库列
	* 方法的参数和返回值: 
	* @param tblName
	* @param col
	*/
	public void delTableColumn(String tblName,DbColumnType col){
		DbUtil.executeSqlUpdate("alter table "+tblName+" drop column "+col.getName());
	}
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-12-13  上午09:04:24
	* 功能描述: 更新主键
	* 方法的参数和返回值: 
	* @param tblName
	* @param oldPkName
	* @param oldPkCols
	* @param newPkCols
	*/
	public abstract void updatePrimaryKey(String tblName,String oldPkName,String oldPkCols,String newPkCols);
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-12-12  下午08:51:44
	* 功能描述: 获取生成主键的语句
	* 方法的参数和返回值: 
	* @param tblName
	* @param pkName
	* @param pkCols
	* @return
	*/
	public String getPrimaryKeySql(String tblName,String pkName,String pkCols){
		StringBuffer buff = new StringBuffer();
		buff.append("alter table ").append(tblName).append(LINE_SEPARATOR);
		buff.append("  add constraint ").append(pkName).append(" primary key (").append(pkCols).append(")"+LINE_SEPARATOR);
		buff.append("  using index;");
		return buff.toString();
	}
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-12-12  下午03:26:15
	* 功能描述: 返回数据库中支持的数据类型
	* 方法的参数和返回值: 
	* @return
	*/
	public List<String> getDataTypes(){
		List<String> list = new ArrayList<String>();
		list.add("VARCHAR()");
		list.add("DATE");
		return list;
	}
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-12-13  上午10:52:43
	* 功能描述: 更新表注释
	* 方法的参数和返回值: 
	* @param tblName
	* @param tblComment
	*/
	public void commentTable(String tblName,String tblComment){
		DbUtil.executeSqlUpdate("comment on table "+tblName+" is '"+tblComment+"'");
	}

	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-12-24  下午04:42:02
	* 功能描述: 获取禁用表触发器的SQL语句
	* 方法的参数和返回值: 
	* @param name
	* @return
	*/
	public abstract String getTableDisableTigger(String name);
	public abstract String getTableEnableTigger(String name);

	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-12-25  上午09:28:58
	* 功能描述: 返回导出SQL语句的前缀
	* 方法的参数和返回值: 
	* @return
	*/
	public String getExportPre(){return "";}
	public String getExportExt(){return "";}
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-12-25  上午08:38:46
	* 功能描述: 只能转换字符串类型及数字型，子类必须实现日期型及其它类型的转换
	* 方法的参数和返回值: 
	* @param obj
	* @return
	*/
	public String getExportSqlValue(Object obj){
		if(obj==null){
			return "null";
		}
		if(obj instanceof String){
			return "'"+obj.toString().replace("'", "''")+"'";
		}
		if(obj instanceof java.sql.Timestamp){
			return "to_date('"+obj.toString().substring(0,obj.toString().length()-4)+"','yyyy-mm-dd hh24:mi:ss')";
		}
		else if(obj instanceof BigDecimal||obj instanceof Integer||obj instanceof Long||obj instanceof Float||obj instanceof Double||obj instanceof Short){
			return obj.toString();
		}
		else{
			return null;
		}
	}

	public String getSearchQuerySql(String name) {
		// TODO Auto-generated method stub
		return "select t.* from "+name+" t";
	}
	
	public DataSet getData(String sql,int rows,int page) throws AppException{
		if(rows<=0){
			rows = Integer.MAX_VALUE;
		}
		if(page<1){
			page = 1;
		}
		DataSet ds = new DataSet();
		ds.setRows(rows);
		ds.setPage(page);
		LinkedHashMap<Integer,Object[]> data = new LinkedHashMap<Integer,Object[]>();
		Vector<String> header = new Vector<String>();
		Vector<String> headerType = new Vector<String>();
		ds.setHeader(header);
		ds.setData(data);
		ds.setHeaderType(headerType);
		Connection con = null;
		try{
			con = DbUtil.createConnection();
			if(con==null){
				throw new AppException("数据库尚未连接！");
			}
			Statement st = con.createStatement();
			LogUtil.logSql(sql);
			ResultSet rs = st.executeQuery(sql);
			int cols = rs.getMetaData().getColumnCount();
			Object[] row = null;
			int i=0,index=1;
			int[] types = new int[cols];
			for(i=0;i<cols;i++){
				header.add(rs.getMetaData().getColumnName(i+1));
				headerType.add(rs.getMetaData().getColumnTypeName(i+1));
				types[i] = rs.getMetaData().getColumnType(i+1);
			}
			if(page>1){
				boolean hasNext=true;
				for(i=0;i<rows*(page-1)&&hasNext;i++){
					hasNext = rs.next();
				}
			}
			while(rs.next()){
				row = new Object[cols];
				for(i=0;i<cols;i++){
					if(types[i]==Types.CLOB){
						row[i] = rs.getString(i+1);
					}
					else{
						row[i] = rs.getObject(i+1);
					}
				}
				data.put(rows*(page-1)+index++,row);
			}
			rs.close();
			st.close();
		}catch(Exception e){
			LogUtil.logError(e);
		}
		finally{
			try {
				con.close();
			} catch (SQLException e) {
				LogUtil.logError(e);
			}
		}
		return ds;
	}
	
	public abstract Clob getEmptyClob();

	/** 将字符串中的换行转为字符 */
	protected String getSafeStr(String s){
		if(s==null){
			return "";
		}
		return s.replaceAll("\r", "\\r").replaceAll("\n", "\\n");
	}
	
	/** 获取单行注释的起始字符串 */
	protected String getCommentStart(){
		return "--";
	}
}
