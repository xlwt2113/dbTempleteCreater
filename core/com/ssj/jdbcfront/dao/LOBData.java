package com.ssj.jdbcfront.dao;

import java.sql.Types;

public class LOBData {
	private Object data;
	private int type;
	public int getType() {
		return type;
	}

	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public LOBData(Object data, int type) {
		super();
		this.data = data;
		this.type = type;
	}
	
	public String toString(){
		if(type == Types.CLOB){
			return "<CLOB>";
		}
		if(type == Types.BLOB){
			return "<BLOB>";
		}
		else{
			return "<UNKNOW>";
		}
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
