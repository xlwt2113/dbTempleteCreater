package com.ssj.jdbcfront.model;

import java.util.List;

public class DbTableType extends DbObject {
	
	protected String pkName;

	protected List<DbColumnType> columns;

	public List<DbColumnType> getColumns() {
		return columns;
	}
	
	public DbColumnType getColumn(String name){
		if(name==null){
			return null;
		}
		for(DbColumnType dct:columns){
			if(name.equals(dct.getName())){
				return dct;
			}
		}
		return null;
	}
	
	/**
	* �����ˣ���˧��
	* ��������: 2010-12-12  ����02:27:34
	* ��������: ��ȡ�����Ķ�������
	* �����Ĳ����ͷ���ֵ: 
	* @return
	*/
	public String getPkName(){
		return pkName==null?name+"_pk":pkName;
	}
	
	/**
	* �����ˣ���˧��
	* ��������: 2010-12-12  ����02:27:49
	* ��������: ��ȡ��������������
	* �����Ĳ����ͷ���ֵ: 
	* @return
	*/
	public String getPkCols(){
		StringBuffer buff = new StringBuffer();
		for(DbColumnType dct:columns){
			if(dct.isIsPri()){
				buff.append(",").append(dct.getName());
			}
		}
		if(buff.length()>0){
			return buff.substring(1);
		}
		return null;
	}

	public void setColumns(List<DbColumnType> columns) {
		this.columns = columns;
	}

	public void setPkName(String pkName) {
		this.pkName = pkName;
	}
	
}
