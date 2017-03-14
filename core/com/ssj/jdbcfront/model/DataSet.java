package com.ssj.jdbcfront.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class DataSet {

	private Vector<String> header;

	private Vector<String> headerType;

	private LinkedHashMap<Integer,Object[]> data;
	
	private int rows,page;
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public List<String> getHeader() {
		return header;
	}

	public void setHeader(Vector<String> header) {
		this.header = header;
	}

	public Map<Integer, Object[]> getData() {
		return data;
	}

	public void setData(LinkedHashMap<Integer, Object[]> data) {
		this.data = data;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getBeginRow() {
		return (page-1)*rows+1;
	}

	public int getEndRow() {
		return page*rows;
	}

	public Vector<String> getHeaderType() {
		return headerType;
	}

	public void setHeaderType(Vector<String> headerType) {
		this.headerType = headerType;
	}

}
