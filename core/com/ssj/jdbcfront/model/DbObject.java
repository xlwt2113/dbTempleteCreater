package com.ssj.jdbcfront.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.ssj.jdbcfront.dao.RowMapper;
import com.ssj.util.LogUtil;
import com.ssj.util.StringUtil;

public class DbObject {

	private static Map<String,String> dbTypeMap = null;

	/**
	 * ����
	 */
	protected String name;
	
	/** ���ݿ�������ͣ�֧��TABLE VIEW FUNCTION�� */
	protected String type;
	
	/**
	 * ע��
	 */
	protected String comment;
	
	public DbObject() {
		super();
	}

	public DbObject(String name) {
		super();
		this.name = name;
	}
	
	public static RowMapper mapper2 = new RowMapper(){
		public Object mapRow(ResultSet rs, int num) {
			DbObject obj = new DbObject();
			try {
				obj.setName(rs.getString(1));
				obj.setType(rs.getString(2));
			} catch (SQLException e) {
				LogUtil.logError(e);
			}
			return obj;
	}};

	public static RowMapper mapper3 = new RowMapper(){
		public Object mapRow(ResultSet rs, int num) {
			DbObject obj = new DbObject();
			try {
				obj.setName(rs.getString(1));
				obj.setType(rs.getString(2));
				obj.setComment(rs.getString(3));
			} catch (SQLException e) {
				LogUtil.logError(e);
			}
			return obj;
		}};

	public String toString(){
		if(StringUtil.isBlank(comment)){
			return name;
		}
		return name+" ["+comment+"]";
	}

	private Map<String,String> getDbTypeMap(){
		if(dbTypeMap==null){
			dbTypeMap = new LinkedHashMap<String,String>();
			dbTypeMap.put("TABLE","��");
			dbTypeMap.put("VIEW","��ͼ");
			dbTypeMap.put("FUNCTION","����");
			dbTypeMap.put("PROCEDURE","�洢����");
			dbTypeMap.put("PACKAGE","������");
			dbTypeMap.put("PACKAGE BODY","������");
		}
		return dbTypeMap;
	}

	public String getTypeName(){
		String t = type.toUpperCase();
		String tn = getDbTypeMap().get(t);
		if(tn==null){
			tn = t;
		}
		return tn;
	}

	public String getBaseType(){
		String t = type.toUpperCase();
		t = t.replaceAll("\\(\\d+\\)", "");
		return t;
	}

	public boolean isCanSelect() {
		if(type==null)
			return false;
		return "TABLE".equals(type.toUpperCase())||"VIEW".equals(type.toUpperCase());
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getName() {
		//����  �ĳ�Сд
		return name.toLowerCase();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==null||name==null||!(obj instanceof DbObject)){
			return false;
		}
		String t = type;
		if(t==null){
			t = "";
		}
		DbObject that = (DbObject) obj;
		return this.name.equals(that.name)&&t.equals(that.type);
	}
	
}
