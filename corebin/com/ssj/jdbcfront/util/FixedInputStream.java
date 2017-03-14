package com.ssj.jdbcfront.util;

import java.io.InputStream;

public class FixedInputStream {

	private InputStream in;
	private int len;
	public FixedInputStream(InputStream in, int len) {
		super();
		this.in = in;
		this.len = len;
	}
	public InputStream getIn() {
		return in;
	}
	public void setIn(InputStream in) {
		this.in = in;
	}
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
	
}
